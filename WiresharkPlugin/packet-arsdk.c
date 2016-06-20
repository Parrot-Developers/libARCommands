/* packet-arsdk.c
 * Routines for Parrot ARSDK protocol packet disassembly
 * Copyright 2015 Parrot S.A.
 *
 * Wireshark - Network traffic analyzer
 * By Gerald Combs <gerald@wireshark.org>
 * Copyright 1998 Gerald Combs
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <stdarg.h>
#include <ctype.h>
#include <time.h>

#define DEBUG(...) do {} while (0)
/*#define DEBUG(...) fprintf(stderr, __VA_ARGS__)*/

#if defined(STANDALONE_BUILD)
#include "wireshark.h"
#else
#include "config.h"
#include <glib.h>
#include <epan/packet.h>
#include <epan/expert.h>
#include <epan/range.h>
#include <epan/value_string.h>
#include <epan/prefs.h>
#endif

#include "protocol.h"

void proto_register_arsdk(void);
void proto_reg_handoff_arsdk(void);

/* Initialize the protocol and registered fields */
static int proto_arsdk = -1;

static int hf_arsdk_frame = -1;
static int hf_arsdk_frame_type = -1;
static int hf_arsdk_frame_id = -1;
static int hf_arsdk_frame_seq = -1;
static int hf_arsdk_frame_size = -1;
static int hf_arsdk_frame_data = -1;
static int hf_arsdk_ping = -1;
static int hf_arsdk_ping_timestamp = -1;
static int hf_arsdk_ack = -1;
static int hf_arsdk_ack_seq = -1;
static int hf_arsdk_arcommand = -1;
static int hf_arsdk_arcommand_project = -1;
static int hf_arsdk_arcommand_args = -1;
static int hf_arsdk_arstream = -1;
static int hf_arsdk_arstream_frame_number = -1;
static int hf_arsdk_arstream_frame_flags = -1;
static int hf_arsdk_arstream_fragment_number = -1;
static int hf_arsdk_arstream_fragments_per_frame = -1;
static int hf_arsdk_arstream_payload = -1;

static hf_register_info *hf_arsdk_info;
static unsigned int hf_arsdk_nitems;
static unsigned int hf_arsdk_allocated;
#define HF_ARSDK_CHUNK 256

/* Global sample port preference - real port preferences should generally
 * default to 0 unless there is an IANA-registered (or equivalent) port for your
 * protocol. */
#define ARSDK_DEFAULT_PORTS "54321,43210,34512,34521,9988"
static range_t *global_arsdk_port_range;

/* Initialize the subtree pointers */
static gint ett_arsdk = -1;
static gint ett_arsdk_frame = -1;
static gint ett_arsdk_arcommand = -1;
static gint ett_arsdk_arcommand_args = -1;
static gint ett_arsdk_arstream = -1;
static gint ett_arsdk_ping = -1;
static gint ett_arsdk_ack = -1;

/* Setup protocol subtree array */
static gint *ett[] = {
    &ett_arsdk,
    &ett_arsdk_frame,
    &ett_arsdk_arcommand,
    &ett_arsdk_arcommand_args,
    &ett_arsdk_arstream,
    &ett_arsdk_ping,
    &ett_arsdk_ack,
};

/* A sample #define of the minimum length (in bytes) of the protocol data.
 * If data is received with fewer than this many bytes it is rejected by
 * the current dissector. */
#define arsdk_MIN_LENGTH 7

/* Allocate a field in an array for registration */
static void add_field(const header_field_info *info, int *p_id)
{
    hf_register_info *hf;

    if (hf_arsdk_nitems+1 > hf_arsdk_allocated) {
	hf_arsdk_allocated += HF_ARSDK_CHUNK;
	hf_arsdk_info = (hf_register_info *)realloc(hf_arsdk_info,
						    hf_arsdk_allocated*
						    sizeof(*hf_arsdk_info));
	assert(hf_arsdk_info);
    }

    hf = &hf_arsdk_info[hf_arsdk_nitems++];

    memcpy(&hf->hfinfo, info, sizeof(*info));
    hf->p_id = p_id;

    DEBUG("added field: name=%s abbrev=%s\n", info->name, info->abbrev);
}

static int *add_dynamic_field(const header_field_info *info)
{
    int *p_id;

    /* cannot put these in a reallocated array, as we keep refs on them */
    p_id = (int *)malloc(sizeof(*p_id));
    assert(p_id);
    *p_id = -1;
    add_field(info, p_id);

    return p_id;
}

static enum ftenum arsdk_to_ws_type(enum arsdk_arg_type n)
{
    switch (n) {
    case ARSDK_ARG_TYPE_ENUM:   return FT_UINT32;
    case ARSDK_ARG_TYPE_U8:     return FT_UINT8;
    case ARSDK_ARG_TYPE_I8:     return FT_INT8;
    case ARSDK_ARG_TYPE_U16:    return FT_UINT16;
    case ARSDK_ARG_TYPE_I16:    return FT_INT16;
    case ARSDK_ARG_TYPE_U32:    return FT_UINT32;
    case ARSDK_ARG_TYPE_I32:    return FT_INT32;
    case ARSDK_ARG_TYPE_U64:    return FT_UINT64;
    case ARSDK_ARG_TYPE_I64:    return FT_INT64;
    case ARSDK_ARG_TYPE_FLOAT:  return FT_FLOAT;
    case ARSDK_ARG_TYPE_DOUBLE: return FT_DOUBLE;
    case ARSDK_ARG_TYPE_STRING: return FT_STRINGZ;
    default:                    break;
    };
    return FT_NONE;
}

static int arsdk_type_size(enum arsdk_arg_type n)
{
    switch (n) {
    case ARSDK_ARG_TYPE_ENUM:   return 4;
    case ARSDK_ARG_TYPE_U8:     return 1;
    case ARSDK_ARG_TYPE_I8:     return 1;
    case ARSDK_ARG_TYPE_U16:    return 2;
    case ARSDK_ARG_TYPE_I16:    return 2;
    case ARSDK_ARG_TYPE_U32:    return 4;
    case ARSDK_ARG_TYPE_I32:    return 4;
    case ARSDK_ARG_TYPE_U64:    return 8;
    case ARSDK_ARG_TYPE_I64:    return 8;
    case ARSDK_ARG_TYPE_FLOAT:  return 4;
    case ARSDK_ARG_TYPE_DOUBLE: return 8;
    default:	                break;
    };
    return 0;
}

static char *make_abbrev(const char *fmt, ...)
{
    va_list ap;
    char buf[512];
    char *s;

    va_start(ap, fmt);
    vsnprintf(buf, sizeof(buf), fmt, ap);
    va_end(ap);

    /* abbrev should be lowercase */
    for (s = buf; *s; s++)
	*s = tolower(*s);

    s = strdup(buf);
    assert(s);

    return s;
}

static void add_arg_field(struct arsdk_project *proj,
			  struct arsdk_class *cl,
			  struct arsdk_cmd *cmd,
			  struct arsdk_arg *arg)
{
    header_field_info info;
    value_string *strings;
    unsigned int i;

    info.name = arg->name;
    info.abbrev = make_abbrev("arsdk.command.%s.%s.%s.%s", proj->name,
			      cl->name, cmd->name, arg->name);
    info.type = arsdk_to_ws_type(arg->type);

    if ((info.type == FT_UINT8) ||
	(info.type == FT_UINT16)||
	(info.type == FT_INT8)  ||
	(info.type == FT_INT16) ||
	(info.type == FT_INT32) ||
	(info.type == FT_INT64)) {
	info.display = BASE_DEC;
    } else if ((info.type == FT_UINT32)||
	       (info.type == FT_UINT64)) {
	info.display = BASE_HEX;
    } else if (info.type == FT_STRINGZ) {
	info.display = STR_ASCII;
    } else {
	info.display = BASE_NONE;
    }
    info.bitmask = 0;
    info.strings = NULL;

    /* add enum symbolic names */
    if (arg->nenums > 0) {
	strings = (value_string *)malloc((1+arg->nenums)*sizeof(*strings));
	if (strings) {
	    for (i = 0; i < arg->nenums; i++) {
		strings[i].value = arg->enums[i].value;
		strings[i].strptr = arg->enums[i].name;
	    }
	    strings[arg->nenums].value = 0;
	    strings[arg->nenums].strptr = NULL;
	    info.strings = VALS(strings);
	}
    }

    info.blurb = arg->comment;
    arg->priv = add_dynamic_field(&info);
}

static void add_cmd_field(struct arsdk_project *proj,
			  struct arsdk_class *cl)
{
    header_field_info info;
    value_string *strings;
    unsigned int i;

    info.name = "Command";
    info.abbrev = make_abbrev("arsdk.command.%s.%s.cmd", proj->name, cl->name);
    info.type = FT_UINT16;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;

    /* add command names */
    if (cl->ncmds > 0) {
	strings = (value_string *)malloc((1+cl->ncmds)*sizeof(*strings));
	if (strings) {
	    for (i = 0; i < cl->ncmds; i++) {
		strings[i].value = cl->cmds[i].id;
		strings[i].strptr = cl->cmds[i].name;
	    }
	    strings[cl->ncmds].value = 0;
	    strings[cl->ncmds].strptr = NULL;
	    info.strings = VALS(strings);
	}
    }

    info.blurb = NULL;
    cl->priv = add_dynamic_field(&info);
}

static void add_class_field(struct arsdk_project *proj)
{
    header_field_info info;
    value_string *strings;
    unsigned int i;

    info.name = "Class";
    info.abbrev = make_abbrev("arsdk.command.%s.class", proj->name);
    info.type = FT_UINT8;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;

    /* add class names */
    if (proj->nclasses > 0) {
	strings = (value_string *)malloc((1+proj->nclasses)*sizeof(*strings));
	if (strings) {
	    for (i = 0; i < proj->nclasses; i++) {
		strings[i].value  = proj->classes[i].ident;
		strings[i].strptr = proj->classes[i].name;
	    }
	    strings[proj->nclasses].value = 0;
	    strings[proj->nclasses].strptr = NULL;
	    info.strings = VALS(strings);
	}
    }

    info.blurb = NULL;
    proj->priv = add_dynamic_field(&info);
}

static void add_command_fields(void)
{
    header_field_info info;
    struct arsdk_project *proj;
    struct arsdk_class *cl;
    struct arsdk_cmd *cmd;
    struct arsdk_arg *arg;
    unsigned int i, j, k, l;
    value_string *strings;

    info.name = "ARCommand";
    info.abbrev = "arsdk.command";
    info.type = FT_NONE;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "ARCOMMAND command";
    add_field(&info, &hf_arsdk_arcommand);

    info.name = "Project";
    info.abbrev = "arsdk.command.project";
    info.type = FT_UINT8;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;

    /* add project names */
    if (arsdk_nprojects > 0) {
	strings = (value_string *)malloc((1+arsdk_nprojects)*sizeof(*strings));
	if (strings) {
	    for (i = 0; i < arsdk_nprojects; i++) {
		strings[i].value  = arsdk_projects[i].ident;
		strings[i].strptr = arsdk_projects[i].name;
	    }
	    strings[arsdk_nprojects].value = 0;
	    strings[arsdk_nprojects].strptr = NULL;
	    info.strings = VALS(strings);
	}
    }

    info.blurb = NULL;
    add_field(&info, &hf_arsdk_arcommand_project);

    info.name = "Args";
    info.abbrev = "arsdk.command.args";
    info.type = FT_NONE;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "ARCOMMAND command arguments";
    add_field(&info, &hf_arsdk_arcommand_args);

    /* add specific class/commands/args fields */
    for (i = 0; i < arsdk_nprojects; i++) {
	proj = &arsdk_projects[i];
	add_class_field(proj);
	for (j = 0; j < proj->nclasses; j++) {
	    cl = &proj->classes[j];
	    add_cmd_field(proj, cl);
	    for (k = 0; k < cl->ncmds; k++) {
		cmd = &cl->cmds[k];
		for (l = 0; l < cmd->nargs; l++) {
		    arg = &cmd->args[l];
		    add_arg_field(proj, cl, cmd, arg);
		}
	    }
	}
    }

}

static void add_frame_fields(void)
{
    header_field_info info;

    static value_string type_strings[] = {
	{.value = ARNETWORKAL_FRAME_TYPE_UNINITIALIZED,
	 .strptr = "UNINITIALIZED"},
	{.value = ARNETWORKAL_FRAME_TYPE_ACK,
	 .strptr = "ACK"},
	{.value = ARNETWORKAL_FRAME_TYPE_DATA,
	 .strptr = "DATA"},
	{.value = ARNETWORKAL_FRAME_TYPE_DATA_LOW_LATENCY,
	 .strptr = "DATA_LOW_LATENCY"},
	{.value = ARNETWORKAL_FRAME_TYPE_DATA_WITH_ACK,
	 .strptr = "DATA_WITH_ACK"},
	{.value = 0,
	 .strptr = NULL},
    };

    static value_string id_strings[] = {
	{.value = 0, .strptr = "PING_REQUEST"},
	{.value = 1, .strptr = "PING_REPLY"},

	{.value = NETWORK_CD_NONACK_ID,
	 .strptr = "CD_NONACK"},
	{.value = NETWORK_CD_ACK_ID,
	 .strptr = "CD_ACK"},
	{.value = NETWORK_CD_EMERGENCY_ID,
	 .strptr = "CD_EMERGENCY"},
	{.value = NETWORK_CD_VIDEO_ACK_ID,
	 .strptr = "CD_VIDEO_ACK"},
	{.value = NETWORK_CD_SOUND_ACK_ID,
	 .strptr = "CD_SOUND_ACK"},
	{.value = NETWORK_CD_SOUND_DATA_ID,
	 .strptr = "CD_SOUND_DATA"},
	{.value = NETWORK_DC_NONACK_ID,
	 .strptr = "DC_NONACK"},
	{.value = NETWORK_DC_ACK_ID,
	 .strptr = "DC_ACK"},
	{.value = NETWORK_DC_VIDEO_DATA_ID,
	 .strptr = "DC_VIDEO_DATA"},
	{.value = NETWORK_DC_SOUND_DATA_ID,
	 .strptr = "DC_SOUND_DATA"},
	{.value = NETWORK_DC_SOUND_ACK_ID,
	 .strptr = "DC_SOUND_ACK"},

	{.value = NETWORK_CUSTOM_ID,
	 .strptr = "Fake packet"},

	{.value = NETWORK_CD_NONACK_ID+128,
	 .strptr = "ACK_CD_NONACK"},
	{.value = NETWORK_CD_ACK_ID+128,
	 .strptr = "ACK_CD_ACK"},
	{.value = NETWORK_CD_EMERGENCY_ID+128,
	 .strptr = "ACK_CD_EMERGENCY"},
	{.value = NETWORK_CD_VIDEO_ACK_ID+128,
	 .strptr = "ACK_CD_VIDEO_ACK"},
	{.value = NETWORK_CD_SOUND_ACK_ID+128,
	 .strptr = "ACK_CD_SOUND_ACK"},
	{.value = NETWORK_CD_SOUND_DATA_ID+128,
	 .strptr = "ACK_CD_SOUND_DATA"},
	{.value = NETWORK_DC_NONACK_ID+128,
	 .strptr = "ACK_DC_NONACK"},
	{.value = NETWORK_DC_ACK_ID+128,
	 .strptr = "ACK_DC_ACK"},
	{.value = NETWORK_DC_VIDEO_DATA_ID+128,
	 .strptr = "ACK_DC_VIDEO_DATA"},
	{.value = NETWORK_DC_SOUND_DATA_ID+128,
	 .strptr = "ACK_DC_SOUND_DATA"},
	{.value = NETWORK_DC_SOUND_ACK_ID+128,
	 .strptr = "ACK_DC_SOUND_ACK"},

	{.value = 0, .strptr = NULL},
    };

    info.name = "ARNETWORK Frame";
    info.abbrev = "arsdk.frame";
    info.type = FT_NONE;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = NULL;
    add_field(&info, &hf_arsdk_frame);

    info.name = "Type";
    info.abbrev = "arsdk.frame.type";
    info.type = FT_UINT8;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = VALS(&type_strings);
    info.blurb = "ARNETWORKAL Manager frame type";
    add_field(&info, &hf_arsdk_frame_type);

    info.name = "ID";
    info.abbrev = "arsdk.frame.id";
    info.type = FT_UINT8;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = VALS(&id_strings);
    info.blurb = "ARNETWORKAL identifier of the buffer sending the frame";
    add_field(&info, &hf_arsdk_frame_id);

    info.name = "Sequence Number";
    info.abbrev = "arsdk.frame.seq";
    info.type = FT_UINT8;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "ARNETWORKAL sequence number of the frame";
    add_field(&info, &hf_arsdk_frame_seq);

    info.name = "Size";
    info.abbrev = "arsdk.frame.size";
    info.type = FT_UINT32;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "ARNETWORKAL total frame size in bytes";
    add_field(&info, &hf_arsdk_frame_size);

    info.name = "Data";
    info.abbrev = "arsdk.frame.data";
    info.type = FT_NONE;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "ARNETWORKAL frame payload";
    add_field(&info, &hf_arsdk_frame_data);

}

static void add_stream_fields(void)
{
    header_field_info info;

    info.name = "ARSTREAM Frame";
    info.abbrev = "arsdk.stream";
    info.type = FT_NONE;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = NULL;
    add_field(&info, &hf_arsdk_arstream);

    info.name = "Frame Number";
    info.abbrev = "arsdk.stream.framenumber";
    info.type = FT_UINT16;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "ID of the current frame";
    add_field(&info, &hf_arsdk_arstream_frame_number);

    info.name = "Frame Flags";
    info.abbrev = "arsdk.stream.frameflags";
    info.type = FT_UINT8;
    info.display = BASE_HEX;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "Info on the current frame";
    add_field(&info, &hf_arsdk_arstream_frame_flags);

    info.name = "Fragment Number";
    info.abbrev = "arsdk.stream.fragmentnumber";
    info.type = FT_UINT8;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "Index of the current fragment in current frame";
    add_field(&info, &hf_arsdk_arstream_fragment_number);

    info.name = "Fragments per frame";
    info.abbrev = "arsdk.stream.fragmentsperframes";
    info.type = FT_UINT8;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "Number of fragments in current frame";
    add_field(&info, &hf_arsdk_arstream_fragments_per_frame);

    info.name = "Payload";
    info.abbrev = "arsdk.stream.payload";
    info.type = FT_NONE;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "Frame data";
    add_field(&info, &hf_arsdk_arstream_payload);
}

static void add_pingpong_fields(void)
{
    header_field_info info;

    info.name = "ARNETWORK Keepalive Frame";
    info.abbrev = "arsdk.ping";
    info.type = FT_NONE;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = NULL;
    add_field(&info, &hf_arsdk_ping);

    info.name = "Timestamp";
    info.abbrev = "arsdk.ping.timestamp";
    info.type = FT_RELATIVE_TIME;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "Timestamp of PING request";
    add_field(&info, &hf_arsdk_ping_timestamp);
}

static void add_ack_fields(void)
{
    header_field_info info;

    info.name = "ARNETWORK ACK Frame";
    info.abbrev = "arsdk.ack";
    info.type = FT_NONE;
    info.display = BASE_NONE;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = NULL;
    add_field(&info, &hf_arsdk_ack);

    info.name = "Sequence Number";
    info.abbrev = "arsdk.ack.seq";
    info.type = FT_UINT8;
    info.display = BASE_DEC;
    info.bitmask = 0;
    info.strings = NULL;
    info.blurb = "Acknowledge sequence number";
    add_field(&info, &hf_arsdk_ack_seq);
}

/* surprisingly complicated implementation of an arg pretty printer */
static void print_arg_value(char *buf, unsigned int bufsize,
			    const struct arsdk_arg *arg, tvbuff_t *tvb,
			    guint offset)
{
    uint32_t value;
    gint dummy;
    unsigned int i;
    unsigned int enumvalue = 0;

    if (arg->nenums > 0) {
	enumvalue = 1;

	switch (arg->type) {
	case ARSDK_ARG_TYPE_U8:
	case ARSDK_ARG_TYPE_I8:
	    value = (uint32_t)tvb_get_guint8(tvb, offset);
	    break;
	case ARSDK_ARG_TYPE_U16:
	case ARSDK_ARG_TYPE_I16:
	    value = (uint32_t)tvb_get_letohs(tvb, offset);
	    break;
	case ARSDK_ARG_TYPE_U32:
	case ARSDK_ARG_TYPE_I32:
	case ARSDK_ARG_TYPE_ENUM:
	    value = tvb_get_letohl(tvb, offset);
	    break;
	case ARSDK_ARG_TYPE_U64:
	case ARSDK_ARG_TYPE_I64:
	    value = (uint32_t)tvb_get_letoh64(tvb, offset);
	    break;
	default:
	    enumvalue = 0;
	    break;
	};
    }

    if (enumvalue) {
	for (i = 0; i < arg->nenums; i++) {
	    if (arg->enums[i].value == value) {
		snprintf(buf, bufsize, "%s", arg->enums[i].name);
		return;
	    }
	}
    }

    switch (arg->type) {
    case ARSDK_ARG_TYPE_U8:
	snprintf(buf, bufsize, "%d", tvb_get_guint8(tvb, offset));
	break;
    case ARSDK_ARG_TYPE_I8:
	snprintf(buf, bufsize, "%d", (int8_t)tvb_get_guint8(tvb, offset));
	break;
    case ARSDK_ARG_TYPE_U16:
	snprintf(buf, bufsize, "%d", tvb_get_letohs(tvb, offset));
	break;
    case ARSDK_ARG_TYPE_I16:
	snprintf(buf, bufsize, "%d", (int16_t)tvb_get_letohs(tvb, offset));
	break;
    case ARSDK_ARG_TYPE_U32:
    case ARSDK_ARG_TYPE_I32:
    case ARSDK_ARG_TYPE_ENUM:
	snprintf(buf, bufsize, "0x%08x", tvb_get_letohl(tvb, offset));
	break;
    case ARSDK_ARG_TYPE_U64:
    case ARSDK_ARG_TYPE_I64:
	snprintf(buf, bufsize, "0x%016llx",
		 (unsigned long long)tvb_get_letoh64(tvb, offset));
	break;
    case ARSDK_ARG_TYPE_FLOAT:
	snprintf(buf, bufsize, "%g",
		 (double)tvb_get_letohieee_float(tvb, offset));
	break;
    case ARSDK_ARG_TYPE_DOUBLE:
	snprintf(buf, bufsize, "%g", tvb_get_letohieee_double(tvb, offset));
	break;
    case ARSDK_ARG_TYPE_STRING:
	snprintf(buf, bufsize, "%s",
		 (char *)tvb_get_const_stringz(tvb, offset, &dummy));
	break;
    default:
	snprintf(buf, bufsize, "unknown type");
	break;
    };
}

static int dissect_arcommand(tvbuff_t *tvb, packet_info *pinfo,
			     proto_tree *tree, guint offset, guint32 size,
			     int position, int seq)
{
    proto_item *ti;
    proto_tree *ctree;
    guint8 fproject, fclass;
    guint16 fcmd;
    unsigned int i, pos;
    struct arsdk_project *proj = NULL;
    struct arsdk_class *cl = NULL;
    struct arsdk_cmd *cmd = NULL;
    struct arsdk_arg *arg = NULL;
    int encoding, len, tsize;
    char buf[256];
    int n, remaining = sizeof(buf);
    char valbuf[256];

    if (size < 4)
	return 0;

    len = size;

    fproject = tvb_get_guint8(tvb, offset);
    fclass   = tvb_get_guint8(tvb, offset+1);
    fcmd     = tvb_get_letohs(tvb, offset+2);

    /* lookup project */
    for (i = 0; i < arsdk_nprojects; i++) {
	if (arsdk_projects[i].ident == fproject) {
	    proj = &arsdk_projects[i];
	    break;
	}
    }
    if (proj == NULL)
	return 0;

    /* lookup class */
    for (i = 0; i < proj->nclasses; i++) {
	if (proj->classes[i].ident == fclass) {
	    cl = &proj->classes[i];
	    break;
	}
    }
    if (cl == NULL)
	return 0;

    /* lookup command */
    for (i = 0; i < cl->ncmds; i++) {
	if (cl->cmds[i].id == fcmd) {
	    cmd = &cl->cmds[i];
	    break;
	}
    }

    if (cmd == NULL)
	return 0;

    /* add a subtree for this command */
    ti = proto_tree_add_item(tree, hf_arsdk_arcommand, tvb,
			     offset, size, ENC_NA);
    ctree = proto_item_add_subtree(ti, ett_arsdk_arcommand);

    proto_tree_add_item(ctree, hf_arsdk_arcommand_project, tvb,
			offset+0, 1, ENC_LITTLE_ENDIAN);
    proto_tree_add_item(ctree, *(int *)proj->priv, tvb,
			offset+1, 1, ENC_LITTLE_ENDIAN);
    proto_tree_add_item(ctree, *(int *)cl->priv, tvb,
			offset+2, 2, ENC_LITTLE_ENDIAN);

    /* build summary of command for INFO column */
    n = snprintf(buf, remaining, "%sARCOMMAND(%03d) %s.%s",
		 (position > 0) ? " | " : "", seq, cl->name, cmd->name);
    if (n >= remaining)
	n = remaining-1;
    pos = n;
    remaining -= n;

    if (cmd->nargs > 0) {
	ti = proto_tree_add_item(ctree, hf_arsdk_arcommand_args, tvb,
				 offset+4, len-4, ENC_NA);
	ctree = proto_item_add_subtree(ti, ett_arsdk_arcommand_args);

	offset += 4;
	len -= 4;

	/* Process args */
	for (i = 0; i < cmd->nargs; i++) {
	    arg = &cmd->args[i];
	    if (arg->type == ARSDK_ARG_TYPE_STRING) {
		tsize = tvb_strsize(tvb, offset);
		encoding = ENC_ASCII;
	    } else {
		tsize = arsdk_type_size(arg->type);
		encoding = ENC_LITTLE_ENDIAN;
	    }

	    if (len < tsize)
		break;

	    print_arg_value(valbuf, sizeof(valbuf), arg, tvb, offset);
	    n = snprintf(&buf[pos], remaining, "%s%s:%s%s",
			 (i == 0) ? "(" : "",
			 arg->name, valbuf,
			 (i == cmd->nargs-1) ? ")" : ", ");
	    if (n >= remaining)
		n = remaining-1;
	    pos += n;
	    remaining -= n;

	    proto_tree_add_item(ctree, *(int *)arg->priv, tvb, offset, tsize,
				encoding);

	    offset += tsize;
	    len -= tsize;
	}
    }
    col_add_str(pinfo->cinfo, COL_INFO, buf);
    col_set_fence(pinfo->cinfo, COL_INFO);

    return size-len;
}

static int dissect_arstream(tvbuff_t *tvb, packet_info *pinfo,
			    proto_tree *tree, guint offset, guint32 size,
			    int position, int seq)
{
    proto_item *ti;
    proto_tree *ctree;
    guint16 framenumber;
    guint8 frameflags, fragnumber, fragsperframe;

    if (size < 5)
	return 0;

    framenumber  = tvb_get_letohs(tvb, offset);
    frameflags   = tvb_get_guint8(tvb, offset+2);
    fragnumber   = tvb_get_guint8(tvb, offset+3);
    fragsperframe = tvb_get_guint8(tvb, offset+4);

    /* add a subtree for this frame */
    ti = proto_tree_add_item(tree, hf_arsdk_arstream, tvb,
			     offset, size, ENC_NA);
    ctree = proto_item_add_subtree(ti, ett_arsdk_arstream);

    proto_tree_add_item(ctree, hf_arsdk_arstream_frame_number, tvb,
			offset+0, 2, ENC_LITTLE_ENDIAN);
    proto_tree_add_item(ctree, hf_arsdk_arstream_frame_flags, tvb,
			offset+2, 1, ENC_LITTLE_ENDIAN);
    proto_tree_add_item(ctree, hf_arsdk_arstream_fragment_number, tvb,
			offset+3, 1, ENC_LITTLE_ENDIAN);
    proto_tree_add_item(ctree, hf_arsdk_arstream_fragments_per_frame, tvb,
			offset+4, 1, ENC_LITTLE_ENDIAN);
    proto_tree_add_item(ctree, hf_arsdk_arstream_payload, tvb,
			offset+5, size-5, ENC_NA);

    col_add_fstr(pinfo->cinfo, COL_INFO,
		 "%sARSTREAM (%03d) (number:%d, flags:0x%x, frag:%d, "
		 "frags_per_frame:%d)",
		 (position > 0) ? " | " : "", seq, framenumber, frameflags,
		 fragnumber, fragsperframe);

    col_set_fence(pinfo->cinfo, COL_INFO);

    return size;
}

static int dissect_pingpong(tvbuff_t *tvb, packet_info *pinfo,
			    proto_tree *tree, guint offset, guint32 size,
			    int position, int id)
{
    proto_item *ti;
    proto_tree *ctree;

    if (size != 8)
	return 0;

    /* add a subtree for this frame */
    ti = proto_tree_add_item(tree, hf_arsdk_ping, tvb,
			     offset, size, ENC_NA);
    ctree = proto_item_add_subtree(ti, ett_arsdk_ping);

    proto_tree_add_item(ctree, hf_arsdk_ping_timestamp, tvb,
			offset, 8, ENC_LITTLE_ENDIAN);

    col_add_fstr(pinfo->cinfo, COL_INFO, "%s%s",
		 (position > 0) ? " | " : "", id ? "PONG" : "PING");
    col_set_fence(pinfo->cinfo, COL_INFO);

    return size;
}

static int dissect_ack(tvbuff_t *tvb, packet_info *pinfo,
		       proto_tree *tree, guint offset, guint32 size,
		       int position)
{
    proto_item *ti;
    proto_tree *ctree;
    guint8 seq;

    if (size != 1)
	return 0;

    seq  = tvb_get_guint8(tvb, offset);

    /* add a subtree for this frame */
    ti = proto_tree_add_item(tree, hf_arsdk_ack, tvb,
			     offset, size, ENC_NA);
    ctree = proto_item_add_subtree(ti, ett_arsdk_ack);

    proto_tree_add_item(ctree, hf_arsdk_ack_seq, tvb,
			offset, 1, ENC_LITTLE_ENDIAN);

    col_add_fstr(pinfo->cinfo, COL_INFO, "%sACK(%03d)",
		 (position > 0) ? " | " : "", seq);
    col_set_fence(pinfo->cinfo, COL_INFO);

    return size;
}

/* Code to actually dissect the packets */
static int dissect_arsdk(tvbuff_t *tvb, packet_info *pinfo, proto_tree *tree,
			 void *data __attribute__((unused)))
{
    /* Set up structures needed to add the protocol subtree and manage it */
    proto_item *fti;
    //proto_tree *arsdk_tree;
    proto_tree *frame_tree;
    /* Other misc. local variables. */
    guint offset = 0;
    unsigned int len = 0;
    guint8 frametype, frameid, frameseq;
    guint32 framesize;
    int frames = 0;

    /*** HEURISTICS ***/

    /* First, if at all possible, do some heuristics to check if the packet
     * cannot possibly belong to your protocol.  This is especially important
     * for protocols directly on top of TCP or UDP where port collisions are
     * common place (e.g., even though your protocol uses a well known port,
     * someone else may set up, for example, a web server on that port which,
     * if someone analyzed that web server's traffic in Wireshark, would result
     * in Wireshark handing an HTTP packet to your dissector).
     *
     * For example:
     */

    /* Check that the packet is long enough for it to belong to us. */
    if (tvb_reported_length(tvb) < arsdk_MIN_LENGTH)
        return 0;

    /* Check that there's enough data present to run the heuristics. If there
     * isn't, reject the packet; it will probably be dissected as data and if
     * the user wants it dissected despite it being short they can use the
     * "Decode-As" functionality. If your heuristic needs to look very deep into
     * the packet you may not want to require *all* data to be present, but you
     * should ensure that the heuristic does not access beyond the captured
     * length of the packet regardless. */
    if (tvb_captured_length(tvb) < arsdk_MIN_LENGTH)
        return 0;

    /* Fetch some values from the packet header using tvb_get_*(). If these
     * values are not valid/possible in your protocol then return 0 to give
     * some other dissector a chance to dissect it. */

    /*** COLUMN DATA ***/

    /* There are two normal columns to fill in: the 'Protocol' column which
     * is narrow and generally just contains the constant string 'arsdk',
     * and the 'Info' column which can be much wider and contain misc. summary
     * information (for example, the port number for TCP packets).
     *
     * If you are setting the column to a constant string, use "col_set_str()",
     * as it's more efficient than the other "col_set_XXX()" calls.
     *
     * If
     * - you may be appending to the column later OR
     * - you have constructed the string locally OR
     * - the string was returned from a call to val_to_str()
     * then use "col_add_str()" instead, as that takes a copy of the string.
     *
     * The function "col_add_fstr()" can be used instead of "col_add_str()"; it
     * takes "printf()"-like arguments. Don't use "col_add_fstr()" with a format
     * string of "%s" - just use "col_add_str()" or "col_set_str()", as it's
     * more efficient than "col_add_fstr()".
     *
     * For full details see section 1.4 of README.dissector.
     */

    /* Set the Protocol column to the constant string of arsdk */
    col_set_str(pinfo->cinfo, COL_PROTOCOL, "ARSDK");

    /* If you will be fetching any data from the packet before filling in
     * the Info column, clear that column first in case the calls to fetch
     * data from the packet throw an exception so that the Info column doesn't
     * contain data left over from the previous dissector: */
    col_clear(pinfo->cinfo, COL_INFO);

    /*** PROTOCOL TREE ***/

    /* Now we will create a sub-tree for our protocol and start adding fields
     * to display under that sub-tree. Most of the time the only functions you
     * will need are proto_tree_add_item() and proto_item_add_subtree().
     *
     * NOTE: The offset and length values in the call to proto_tree_add_item()
     * define what data bytes to highlight in the hex display window when the
     * line in the protocol tree display corresponding to that item is selected.
     *
     * Supplying a length of -1 tells Wireshark to highlight all data from the
     * offset to the end of the packet.
     */

    /* create display subtree for the protocol */
    /*
      ti = proto_tree_add_item(tree, proto_arsdk, tvb, 0, -1, ENC_NA);
      arsdk_tree = proto_item_add_subtree(ti, ett_arsdk);
    */

    /* parse frames */
    while (len < tvb_captured_length(tvb)) {

	/* get ARNetwork frame fields */
	frametype = tvb_get_guint8(tvb, offset);
	frameid   = tvb_get_guint8(tvb, offset+1);
	frameseq  = tvb_get_guint8(tvb, offset+2);
	framesize = tvb_get_letohl(tvb, offset+3);

	if ((framesize < 7) || (framesize + offset > tvb_captured_length(tvb)))
	    /* extracted length is invalid */
	    break;

	if (frametype >= ARNETWORKAL_FRAME_TYPE_MAX)
	    /* invalid frame type */
	    break;

	fti = proto_tree_add_item(tree, hf_arsdk_frame, tvb,
				  offset, framesize, ENC_NA);

	/* add a subtree for this frame */
	frame_tree = proto_item_add_subtree(fti, ett_arsdk_frame);
	proto_tree_add_item(frame_tree, hf_arsdk_frame_type, tvb,
			    offset, 1, ENC_LITTLE_ENDIAN);
	proto_tree_add_item(frame_tree, hf_arsdk_frame_id, tvb,
			    offset+1, 1, ENC_LITTLE_ENDIAN);
	proto_tree_add_item(frame_tree, hf_arsdk_frame_seq, tvb,
			    offset+2, 1, ENC_LITTLE_ENDIAN);
	proto_tree_add_item(frame_tree, hf_arsdk_frame_size, tvb,
			    offset+3, 4, ENC_LITTLE_ENDIAN);

	/* fuzzy heuristic is fuzzy */
	switch (frameid) {

	case 0: /* PING */
	case 1: /* PONG */
	    (void)dissect_pingpong(tvb, pinfo, frame_tree, offset+7,
				   framesize-7, frames, frameid);
	    break;

	case NETWORK_CD_NONACK_ID:
	case NETWORK_CD_ACK_ID:
	case NETWORK_CD_EMERGENCY_ID:
	case NETWORK_DC_NONACK_ID:
	case NETWORK_DC_ACK_ID:
	case NETWORK_CUSTOM_ID:
	    (void)dissect_arcommand(tvb, pinfo, frame_tree, offset+7,
				    framesize-7, frames, frameseq);
	    break;

	case NETWORK_CD_VIDEO_ACK_ID:
	case NETWORK_CD_SOUND_ACK_ID:
	case NETWORK_CD_SOUND_DATA_ID:
	case NETWORK_DC_VIDEO_DATA_ID:
	case NETWORK_DC_SOUND_DATA_ID:
	case NETWORK_DC_SOUND_ACK_ID:
	    (void)dissect_arstream(tvb, pinfo, frame_tree, offset+7,
				   framesize-7, frames, frameseq);
	    break;

	case 128 ... 255:
	    (void)dissect_ack(tvb, pinfo, frame_tree, offset+7,
			      framesize-7, frames);
	    break;

	default:
	    /* unknown, add raw data item */
	    proto_tree_add_item(frame_tree, hf_arsdk_frame_data, tvb,
				offset+7, framesize-7, ENC_LITTLE_ENDIAN);
	    break;
	}

	offset += framesize;
	len += framesize;
	frames++;
    }

    /* If this protocol has a sub-dissector call it here, see section 1.8 of
     * README.dissector for more information. */

    /* Return the amount of data this dissector was able to dissect (which may
     * or may not be the total captured packet as we return here). */
    return len;
}

/* Register the protocol with Wireshark.
 *
 * This format is require because a script is used to build the C function that
 * calls all the protocol registration.
 */
void proto_register_arsdk(void)
{
    module_t *arsdk_module;

    /* add ARNetworkAL fields */
    add_frame_fields();
    add_pingpong_fields();
    add_ack_fields();

    /* add ARCOMMANDS fields */
    add_command_fields();

    /* add ARStream fields */
    add_stream_fields();

    /* Register the protocol name and description */
    proto_arsdk = proto_register_protocol("ARSDK", "ARSDK", "arsdk");

    /* Required function calls to register the header fields and subtrees */
    proto_register_field_array(proto_arsdk, hf_arsdk_info, hf_arsdk_nitems);
    proto_register_subtree_array(ett, array_length(ett));

    /* Register a preferences module (see section 2.6 of README.dissector
     * for more details). Registration of a prefs callback is not required
     * if there are no preferences that affect protocol registration (an example
     * of a preference that would affect registration is a port preference).
     * If the prefs callback is not needed, use NULL instead of
     * proto_reg_handoff_arsdk in the following.
     */
    arsdk_module = prefs_register_protocol(proto_arsdk,
                                           proto_reg_handoff_arsdk);

    /* Register an example port preference */
    range_convert_str(&global_arsdk_port_range, ARSDK_DEFAULT_PORTS, MAX_UDP_PORT);

    prefs_register_range_preference(arsdk_module, "udp_ports",
                                    "ARSDK UDP destination port",
                                    "Port numbers used by ARSDK protocol "
                                    "(default " ARSDK_DEFAULT_PORTS ")",
                                    &global_arsdk_port_range, MAX_UDP_PORT);
}

/* If this dissector uses sub-dissector registration add a registration routine.
 * This exact format is required because a script is used to find these
 * routines and create the code that calls these routines.
 *
 * If this function is registered as a prefs callback (see
 * prefs_register_protocol above) this function is also called by Wireshark's
 * preferences manager whenever "Apply" or "OK" are pressed. In that case, it
 * should accommodate being called more than once by use of the static
 * 'initialized' variable included below.
 *
 * This form of the reg_handoff function is used if if you perform registration
 * functions which are dependent upon prefs. See below this function for a
 * simpler form which can be used if there are no prefs-dependent registration
 * functions.
 */
void
proto_reg_handoff_arsdk(void)
{
    static gboolean initialized = FALSE;
    static dissector_handle_t arsdk_handle;
    static range_t *arsdk_port_range;

    if (!initialized) {
        /* Use new_create_dissector_handle() to indicate that
         * dissect_arsdk() returns the number of bytes it dissected (or 0
         * if it thinks the packet does not belong to PROTONAME).
         */
        arsdk_handle = new_create_dissector_handle(dissect_arsdk, proto_arsdk);
        initialized = TRUE;

    } else {
        /* If you perform registration functions which are dependent upon
         * prefs then you should de-register everything which was associated
         * with the previous settings and re-register using the new prefs
         * settings here. In general this means you need to keep track of
         * the arsdk_handle and the value the preference had at the time
         * you registered.  The arsdk_handle value and the value of the
         * preference can be saved using local statics in this
         * function (proto_reg_handoff).
         */
        dissector_delete_uint_range("udp.port", arsdk_port_range, arsdk_handle);
        g_free(arsdk_port_range);
    }

    arsdk_port_range = range_copy(global_arsdk_port_range);
    dissector_add_uint_range("udp.port", arsdk_port_range, arsdk_handle);
}

#if defined(STANDALONE_BUILD)
#define VERSION ARCOMMANDS_VERSION_STRING;

WS_DLL_PUBLIC_DEF void plugin_register (void);
WS_DLL_PUBLIC_DEF const gchar version[] = VERSION;

/* Start the functions we need for the plugin stuff */

WS_DLL_PUBLIC_DEF void
plugin_register (void)
{
    {extern void proto_register_arsdk (void); proto_register_arsdk ();}
}

WS_DLL_PUBLIC_DEF void plugin_reg_handoff(void);

WS_DLL_PUBLIC_DEF void
plugin_reg_handoff(void)
{
    {extern void proto_reg_handoff_arsdk (void); proto_reg_handoff_arsdk ();}
}

#endif /* STANDALONE_BUILD */
