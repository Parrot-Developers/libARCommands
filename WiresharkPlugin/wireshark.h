/*
 * Wireshark dissector for ARSDK packets
 *
 * This file contains definitions/macros/typedefs necessary for building
 * the dissector out-of-tree. It lacks a large part of Wireshark API which is
 * not currently used in the dissector.
 *
 * Author: Ivan Djelic <ivan.djelic@parrot.com>
 * Copyright (C) 2015 Parrot S.A.
 */
#ifndef _ARSDK_WIRESHARK_H
#define _ARSDK_WIRESHARK_H

#include <stdint.h>

#define WS_DLL_PUBLIC_DEF __attribute__ ((visibility ("default")))
#define WS_DLL_PUBLIC     WS_DLL_PUBLIC_DEF extern

/* GLib types */
typedef unsigned int guint;
typedef int gint;
typedef uint8_t guint8;
typedef int8_t gint8;
typedef uint16_t guint16;
typedef int16_t gint16;
typedef uint32_t guint32;
typedef int32_t gint32;
typedef uint64_t guint64;
typedef int64_t gint64;
typedef char gchar;
typedef float gfloat;
typedef double gdouble;
typedef int gboolean;

#define g_free free

#define TRUE 1
#define FALSE 0

/* Useful when you have an array whose size you can tell at compile-time */
#define array_length(x)	(sizeof x / sizeof x[0])

/* This handle is opaque outside of "packet.c". */
struct dissector_handle;
typedef struct dissector_handle *dissector_handle_t;

struct tvbuff;
typedef struct tvbuff tvbuff_t;

struct _packet_info;

struct epan_column_info;

typedef struct _address {
	int          type;         /* type of address */
	int          hf;           /* the specific field that this addr is */
	int          len;          /* length of address, in bytes */
	const void  *data;          /* pointer to address data */
} address;

/** data structure to hold time values with nanosecond resolution*/
typedef struct nstime_t {
        time_t  secs;
        int     nsecs;
} nstime_t;

/* anonymize some fields */
typedef struct _packet_info {
	const char *current_proto;
	struct epan_column_info *cinfo;
	void *fd;
	void *pseudo_header;
	int file_type_subtype;
	void *phdr;
	void *data_src;
	address dl_src;
	address dl_dst;
	address net_src;
	address net_dst;
	address src;
	address dst;
	guint32 ipproto;
	int ctype;
	guint32 circuit_id;
	const char *noreassembly_reason;
	gboolean fragmented;
	struct {
		guint32 in_error_pkt:1;
		guint32 in_gre_pkt:1;
	} flags;
	int ptype;
	guint32 srcport;
	guint32 destport;
	guint32 match_uint;
	const char *match_string;
	guint16 can_desegment;
	guint16 saved_can_desegment;
	int desegment_offset;
#define DESEGMENT_ONE_MORE_SEGMENT 0x0fffffff
#define DESEGMENT_UNTIL_FIN        0x0ffffffe
	guint32 desegment_len;
	guint16 want_pdu_tracking;
	guint32 bytes_until_next_pdu;
	int     p2p_dir;
#define DECRYPT_GSSAPI_NORMAL   1
#define DECRYPT_GSSAPI_DCE  2
	guint16 decrypt_gssapi_tvb;
	tvbuff_t *gssapi_wrap_tvb;
	tvbuff_t *gssapi_encrypted_tvb;
	tvbuff_t *gssapi_decrypted_tvb;
	gboolean gssapi_data_encrypted;

	void    *private_data;
	void *private_table;

	void *layers;
	guint8 curr_layer_num;
	guint16 link_number;
	guint8  annex_a_used;
	guint16 profinet_type;
	void *sccp_info;
	guint16 clnp_srcref;
	guint16 clnp_dstref;

	int link_dir;

	void* proto_data;
	void* dependent_frames;
	void* frame_end_routines;

	void *pool;
	void *epan;
	nstime_t     rel_ts;
	const gchar  *pkt_comment;
	const gchar *heur_list_name;
} packet_info;

struct _proto_node;

/** A protocol tree element. */
typedef struct _proto_node proto_tree;
/** A protocol item element. */
typedef struct _proto_node proto_item;

struct epan_range;

/*
 * Dissector that returns:
 *
 *	The amount of data in the protocol's PDU, if it was able to
 *	dissect all the data;
 *
 *	0, if the tvbuff doesn't contain a PDU for that protocol;
 *
 *	The negative of the amount of additional data needed, if
 *	we need more data (e.g., from subsequent TCP segments) to
 *	dissect the entire PDU.
 */
typedef int (*new_dissector_t)(tvbuff_t *, packet_info *, proto_tree *, void *);

WS_DLL_PUBLIC dissector_handle_t new_create_dissector_handle(new_dissector_t dissector,
					       const int proto);

/* Add an entry to a uint dissector table. */
WS_DLL_PUBLIC void dissector_add_uint(const char *abbrev, const guint32 pattern,
			dissector_handle_t handle);

/* Add an range of entries to a uint dissector table. */
WS_DLL_PUBLIC void dissector_add_uint_range(const char *abbrev, struct epan_range *range,
    dissector_handle_t handle);
/* Delete the entry for a dissector in a uint dissector table
   with a particular pattern. */
WS_DLL_PUBLIC void dissector_delete_uint(const char *name, const guint32 pattern,
			   dissector_handle_t handle);

/* Delete an range of entries from a uint dissector table. */
WS_DLL_PUBLIC void dissector_delete_uint_range(const char *abbrev, struct epan_range *range,
    dissector_handle_t handle);

/** @file
 * Range strings a variant of value_strings
 */

/**@todo where's the best place for these? */
#define MAX_SCTP_PORT 65535
#define MAX_TCP_PORT 65535
#define MAX_UDP_PORT 65535
#define MAX_DCCP_PORT 65535

typedef struct range_admin_tag {
    guint32 low;
    guint32 high;
} range_admin_t;

/** user specified range(s) */
typedef struct epan_range {
    guint           nranges;   /**< number of entries in ranges */
    range_admin_t   ranges[1]; /**< variable-length array */
} range_t;

/**
 * Return value from range_convert_str().
 */
typedef enum {
    CVT_NO_ERROR,
    CVT_SYNTAX_ERROR,
    CVT_NUMBER_TOO_BIG
} convert_ret_t;

WS_DLL_PUBLIC range_t *range_empty(void);


/*** Converts a range string to a fast comparable array of ranges.
 * This function allocates a range_t large enough to hold the number
 * of ranges specified, and fills the array range->ranges containing
 * low and high values with the number of ranges being range->nranges.
 * After having called this function, the function value_is_in_range()
 * determines whether a given number is within the range or not.<BR>
 * In case of a single number, we make a range where low is equal to high.
 * We take care on wrongly entered ranges; opposite order will be taken
 * care of.
 *
 * The following syntax is accepted :
 *
 *   1-20,30-40     Range from 1 to 20, and packets 30 to 40
 *   -20,30         Range from 1 to 20, and packet 30
 *   20,30,40-      20, 30, and the range from 40 to the end
 *   20-10,30-25    Range from 10 to 20, and from 25 to 30
 *   -              All values
 * @param range the range
 * @param es points to the string to be converted.
 * @param max_value specifies the maximum value in a range.
 * @return convert_ret_t
 */
WS_DLL_PUBLIC convert_ret_t range_convert_str(range_t **range, const gchar *es,
    guint32 max_value);

convert_ret_t range_convert_str_work(range_t **range, const gchar *es,
    guint32 max_value, gboolean err_on_max);

/** This function returns TRUE if a given value is within one of the ranges
 * stored in the ranges array.
 * @param range the range
 * @param val the value to check
 * @return TRUE if the value is in range
 */
WS_DLL_PUBLIC gboolean value_is_in_range(range_t *range, guint32 val);

/** This function returns TRUE if the two given range_t's are equal.
 * @param a first range
 * @param b second range
 * @return TRUE if the value is in range
 */
WS_DLL_PUBLIC gboolean ranges_are_equal(range_t *a, range_t *b);

/** This function calls the provided callback function for each value in
 * in the range.
 * @param range the range
 * @param callback the callback function
 */
WS_DLL_PUBLIC void range_foreach(range_t *range, void (*callback)(guint32 val));

/**
 * This function converts a range_t to a (ep_alloc()-allocated) string.
 */
WS_DLL_PUBLIC char *range_convert_range(range_t *range);

/**
 * Create a copy of a range.
 * @param src the range to copy
 * @return ep allocated copy of the range
 */
WS_DLL_PUBLIC range_t *range_copy(range_t *src);
/*
 * Routines to let modules that have preference settings register
 * themselves by name, and to let them register preference settings
 * by name.
 */
struct pref_module;

struct pref_custom_cbs;

typedef struct pref_module module_t;

/*
 * Register that a protocol has preferences.
 */
WS_DLL_PUBLIC module_t *prefs_register_protocol(int id, void (*apply_cb)(void));

/*
 * Register a preference with an unsigned integral value.
 */
WS_DLL_PUBLIC void prefs_register_uint_preference(module_t *module, const char *name,
				    const char *title, const char *description,
				    guint base, guint *var);
/*
 * Register a preference with a ranged value.
 */
WS_DLL_PUBLIC void prefs_register_range_preference(module_t *module, const char *name,
    const char *title, const char *description, range_t **var,
    guint32 max_value);

/* VALUE TO STRING MATCHING */

typedef struct _value_string {
	guint32      value;
	const gchar *strptr;
} value_string;

/** Make a const value_string[] look like a _value_string pointer, used to set header_field_info.strings */
#define VALS(x)	(const struct _value_string*)(x)

/* field types */
enum ftenum {
        FT_NONE,        /* used for text labels with no value */
        FT_PROTOCOL,
        FT_BOOLEAN,     /* TRUE and FALSE come from <glib.h> */
        FT_UINT8,
        FT_UINT16,
        FT_UINT24,      /* really a UINT32, but displayed as 3 hex-digits if FD_HEX*/
        FT_UINT32,
        FT_UINT64,
        FT_INT8,
        FT_INT16,
        FT_INT24,       /* same as for UINT24 */
        FT_INT32,
        FT_INT64,
        FT_FLOAT,
        FT_DOUBLE,
        FT_ABSOLUTE_TIME,
        FT_RELATIVE_TIME,
        FT_STRING,
        FT_STRINGZ,     /* for use with proto_tree_add_item() */
        FT_UINT_STRING, /* for use with proto_tree_add_item() */
        FT_ETHER,
        FT_BYTES,
        FT_UINT_BYTES,
        FT_IPv4,
        FT_IPv6,
        FT_IPXNET,
        FT_FRAMENUM,    /* a UINT32, but if selected lets you go to frame with that number */
        FT_PCRE,        /* a compiled Perl-Compatible Regular Expression object */
        FT_GUID,        /* GUID, UUID */
        FT_OID,         /* OBJECT IDENTIFIER */
        FT_EUI64,
        FT_AX25,
        FT_VINES,
        FT_REL_OID,     /* RELATIVE-OID */
        FT_SYSTEM_ID,
        FT_STRINGZPAD,  /* for use with proto_tree_add_item() */
        FT_NUM_TYPES /* last item number plus one */
};

typedef enum ftenum ftenum_t;

struct _ftype_t;
typedef struct _ftype_t ftype_t;

#define ENC_BIG_ENDIAN		0x00000000
#define ENC_LITTLE_ENDIAN	0x80000000

/*
 * Historically FT_TIMEs were only timespecs; the only question was whether
 * they were stored in big- or little-endian format.
 *
 * For backwards compatibility, we interpret an encoding of 1 as meaning
 * "little-endian timespec", so that passing TRUE is interpreted as that.
 */
#define ENC_TIME_TIMESPEC	0x00000000	/* "struct timespec" */
#define ENC_TIME_NTP		0x00000002	/* NTP times */
#define ENC_TIME_TOD		0x00000004	/* System/3xx and z/Architecture time-of-day clock */

/*
 * Historically, the only place the representation mattered for strings
 * was with FT_UINT_STRINGs, where we had FALSE for the string length
 * being big-endian and TRUE for it being little-endian.
 *
 * We now have encoding values for the character encoding.  The encoding
 * values are encoded in all but the top bit (which is the byte-order
 * bit, required for FT_UINT_STRING and for UCS-2 and UTF-16 strings)
 * and the bottom bit (which we ignore for now so that programs that
 * pass TRUE for the encoding just do ASCII).  (The encodings are given
 * directly as even numbers in hex, so that make-init-lua.pl can just
 * turn them into numbers for use in init.lua.)
 *
 * We don't yet process ASCII and UTF-8 differently.  Ultimately, for
 * ASCII, all bytes with the 8th bit set should be mapped to some "this
 * is not a valid character" code point, as ENC_ASCII should mean "this
 * is ASCII, not some extended variant thereof".  We should also map
 * 0x00 to that as well - null-terminated and null-padded strings
 * never have NULs in them, but counted strings might.  (Either that,
 * or the values for strings should be counted, not null-terminated.)
 * For UTF-8, invalid UTF-8 sequences should be mapped to the same
 * code point.
 *
 * For display, perhaps we should also map control characters to the
 * Unicode glyphs showing the name of the control character in small
 * caps, diagonally.  (Unfortunately, those only exist for C0, not C1.)
 */
#define ENC_CHARENCODING_MASK		0x7FFFFFFE	/* mask out byte-order bits */
#define ENC_ASCII			0x00000000
#define ENC_UTF_8			0x00000002
#define ENC_UTF_16			0x00000004
#define ENC_UCS_2			0x00000006
#define ENC_UCS_4			0x00000008
#define ENC_ISO_8859_1			0x0000000A
#define ENC_ISO_8859_2			0x0000000C
#define ENC_ISO_8859_3			0x0000000E
#define ENC_ISO_8859_4			0x00000010
#define ENC_ISO_8859_5			0x00000012
#define ENC_ISO_8859_6			0x00000014
#define ENC_ISO_8859_7			0x00000016
#define ENC_ISO_8859_8			0x00000018
#define ENC_ISO_8859_9			0x0000001A
#define ENC_ISO_8859_10			0x0000001C
#define ENC_ISO_8859_11			0x0000001E
/* #define ENC_ISO_8859_12			0x00000020 ISO 8859-12 was abandoned */
#define ENC_ISO_8859_13			0x00000022
#define ENC_ISO_8859_14			0x00000024
#define ENC_ISO_8859_15			0x00000026
#define ENC_ISO_8859_16			0x00000028
#define ENC_WINDOWS_1250		0x0000002A
#define ENC_3GPP_TS_23_038_7BITS	0x0000002C
#define ENC_EBCDIC			0x0000002E
#define ENC_MAC_ROMAN			0x00000030
#define ENC_CP437			0x00000032
#define ENC_ASCII_7BITS			0x00000034

/*
 * TODO:
 *
 * These could probably be used by existing code:
 *
 *	"IBM MS DBCS"
 *	JIS C 6226
 *
 * As those are added, change code such as the code in packet-bacapp.c
 * to use them.
 */

/*
 * For protocols (FT_PROTOCOL), aggregate items with subtrees (FT_NONE),
 * opaque byte-array fields (FT_BYTES), and other fields where there
 * is no choice of encoding (either because it's "just a bucket
 * of bytes" or because the encoding is completely fixed), we
 * have ENC_NA (for "Not Applicable").
 */
#define ENC_NA			0x00000000

/* For cases where either native type or string encodings could both be
 * valid arguments, we need something to distinguish which one is being
 * passed as the argument, because ENC_BIG_ENDIAN and ENC_ASCII are both
 * 0x00000000. So we use ENC_STR_NUM or ENC_STR_HEX bit-or'ed with
 * ENC_ASCII and its ilk.
 */
/* this is for strings as numbers "12345" */
#define ENC_STR_NUM             0x01000000
/* this is for strings as hex "1a2b3c" */
#define ENC_STR_HEX             0x02000000
/* a convenience macro for either of the above */
#define ENC_STRING              0x03000000
/* mask out ENC_STR_* and related bits - should this replace ENC_CHARENCODING_MASK? */
#define ENC_STR_MASK            0x0000FFFE

/* for cases where the number is allowed to have a leading '+'/'-' */
/* this can't collide with ENC_SEP_* because they can be used simultaneously */
#define ENC_NUM_PREF    0x00200000

/* For cases where a string encoding contains hex, bit-or one or more
 * of these for the allowed separator(s), as well as with ENC_STR_HEX.
 * See hex_str_to_bytes_encoding() in epan/strutil.h for details.
 */
#define ENC_SEP_NONE    0x00010000
#define ENC_SEP_COLON   0x00020000
#define ENC_SEP_DASH    0x00040000
#define ENC_SEP_DOT   0x00080000
#define ENC_SEP_SPACE   0x00100000
/* a convenience macro for the above */
#define ENC_SEP_MASK    0x001F0000

/* For cases where a string encoding contains a timestamp, use one
 * of these (but only one). These values can collide with above, because
 * you can't do both at the same time.
 */
#define ENC_ISO_8601_DATE       0x00010000
#define ENC_ISO_8601_TIME       0x00020000
#define ENC_ISO_8601_DATE_TIME  0x00030000
#define ENC_RFC_822             0x00040000
#define ENC_RFC_1123            0x00080000
/* a convenience macro for the above - for internal use only */
#define ENC_STR_TIME_MASK       0x000F0000

/* Values for header_field_info.display */

/* For integral types, the display format is a BASE_* field_display_e value
 * possibly ORed with BASE_*_STRING */

/** FIELD_DISPLAY_E_MASK selects the field_display_e value.  Its current
 * value means that we may have at most 16 field_display_e values. */
#define FIELD_DISPLAY_E_MASK 0x0F

typedef enum {
/* Integral types */
	BASE_NONE    = 0,   /**< none */
	BASE_DEC     = 1,   /**< decimal */
	BASE_HEX     = 2,   /**< hexadecimal */
	BASE_OCT     = 3,   /**< octal */
	BASE_DEC_HEX = 4,   /**< decimal (hexadecimal) */
	BASE_HEX_DEC = 5,   /**< hexadecimal (decimal) */
	BASE_CUSTOM  = 6,   /**< call custom routine (in ->strings) to format */

/* String types */
	STR_ASCII    = BASE_NONE, /**< shows non-printable ASCII characters as C-style escapes */
	/* XXX, support for format_text_wsp() ? */
	STR_UNICODE  = 7          /**< shows non-printable UNICODE characters as \\uXXXX (XXX for now non-printable characters display depends on UI) */
} field_display_e;

/* Following constants have to be ORed with a field_display_e when dissector
 * want to use specials value-string MACROs for a header_field_info */
#define BASE_RANGE_STRING 0x10
#define BASE_EXT_STRING   0x20
#define BASE_VAL64_STRING 0x40

/** BASE_ values that cause the field value to be displayed twice */
#define IS_BASE_DUAL(b) ((b)==BASE_DEC_HEX||(b)==BASE_HEX_DEC)

/* For FT_ABSOLUTE_TIME, the display format is an absolute_time_display_e
 * as per time_fmt.h. */
typedef enum {
    HF_REF_TYPE_NONE,       /**< Field is not referenced */
    HF_REF_TYPE_INDIRECT,   /**< Field is indirectly referenced (only applicable for FT_PROTOCOL) via. its child */
    HF_REF_TYPE_DIRECT      /**< Field is directly referenced */
} hf_ref_type;

/** information describing a header field */
typedef struct _header_field_info header_field_info;

/** information describing a header field */
struct _header_field_info {
	/* ---------- set by dissector --------- */
	const char		*name;           /**< [FIELDNAME] full name of this field */
	const char		*abbrev;         /**< [FIELDABBREV] abbreviated name of this field */
	enum ftenum		 type;           /**< [FIELDTYPE] field type, one of FT_ (from ftypes.h) */
	int			     display;        /**< [FIELDDISPLAY] one of BASE_, or field bit-width if FT_BOOLEAN and non-zero bitmask */
	const void		*strings;        /**< [FIELDCONVERT] value_string, val64_string, range_string or true_false_string,
				                      typically converted by VALS(), RVALS() or TFS().
				                      If this is an FT_PROTOCOL then it points to the
				                      associated protocol_t structure */
	guint32			 bitmask;        /**< [BITMASK] bitmask of interesting bits */
	const char		*blurb;          /**< [FIELDDESCR] Brief description of field */

	/* ------- set by proto routines (prefilled by HFILL macro, see below) ------ */
	int				     id;                /**< Field ID */
	int					 parent;            /**< parent protocol tree */
	hf_ref_type			 ref_type;          /**< is this field referenced by a filter */
	int                  same_name_prev_id; /**< ID of previous hfinfo with same abbrev */
	header_field_info	*same_name_next;    /**< Link to next hfinfo with same abbrev */
};

/**
 * HFILL initializes all the "set by proto routines" fields in a
 * _header_field_info. If new fields are added or removed, it should
 * be changed as necessary.
 */
#define HFILL -1, 0, HF_REF_TYPE_NONE, -1, NULL

#define HFILL_INIT(hf)   \
	hf.hfinfo.id				= -1;   \
	hf.hfinfo.parent			= 0;   \
	hf.hfinfo.ref_type			= HF_REF_TYPE_NONE;   \
	hf.hfinfo.same_name_prev_id	= -1;   \
	hf.hfinfo.same_name_next	= NULL;

/** Used when registering many fields at once, using proto_register_field_array() */
typedef struct hf_register_info {
	int				*p_id;	/**< written to by register() function */
	header_field_info		hfinfo;	/**< the field info to be registered */
} hf_register_info;

/** Create a subtree under an existing item.
 @param ti the parent item of the new subtree
 @param idx one of the ett_ array elements registered with proto_register_subtree_array()
 @return the new subtree */
WS_DLL_PUBLIC proto_tree* proto_item_add_subtree(proto_item *ti, const gint idx);

WS_DLL_PUBLIC proto_item *
proto_tree_add_item(proto_tree *tree, int hfindex, tvbuff_t *tvb,
		    const gint start, gint length, const guint encoding);

/** Register a new protocol.
 @param name the full name of the new protocol
 @param short_name abbreviated name of the new protocol
 @param filter_name protocol name used for a display filter string
 @return the new protocol handle */
WS_DLL_PUBLIC int
proto_register_protocol(const char *name, const char *short_name, const char *filter_name);

/** Register a header_field array.
 @param parent the protocol handle from proto_register_protocol()
 @param hf the hf_register_info array
 @param num_records the number of records in hf */
WS_DLL_PUBLIC void
proto_register_field_array(const int parent, hf_register_info *hf, const int num_records);

/** Register a protocol subtree (ett) array.
 @param indices array of ett indices
 @param num_indices the number of records in indices */
WS_DLL_PUBLIC void
proto_register_subtree_array(gint *const *indices, const int num_indices);

/* Get amount of captured data in the buffer (which is *NOT* necessarily the
 * length of the packet). You probably want tvb_reported_length instead. */
WS_DLL_PUBLIC guint tvb_captured_length(const tvbuff_t *tvb);

/* Get reported length of buffer */
WS_DLL_PUBLIC guint tvb_reported_length(const tvbuff_t *tvb);

/************** START OF ACCESSORS ****************/
/* All accessors will throw an exception if appropriate */

WS_DLL_PUBLIC guint8 tvb_get_guint8(tvbuff_t *tvb, const gint offset);

WS_DLL_PUBLIC guint16 tvb_get_ntohs(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint32 tvb_get_ntoh24(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint32 tvb_get_ntohl(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint64 tvb_get_ntoh40(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gint64 tvb_get_ntohi40(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint64 tvb_get_ntoh48(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gint64 tvb_get_ntohi48(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint64 tvb_get_ntoh56(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gint64 tvb_get_ntohi56(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint64 tvb_get_ntoh64(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gfloat tvb_get_ntohieee_float(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gdouble tvb_get_ntohieee_double(tvbuff_t *tvb,
    const gint offset);

WS_DLL_PUBLIC guint16 tvb_get_letohs(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint32 tvb_get_letoh24(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint32 tvb_get_letohl(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint64 tvb_get_letoh40(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gint64 tvb_get_letohi40(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint64 tvb_get_letoh48(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gint64 tvb_get_letohi48(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint64 tvb_get_letoh56(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gint64 tvb_get_letohi56(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC guint64 tvb_get_letoh64(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gfloat tvb_get_letohieee_float(tvbuff_t *tvb, const gint offset);
WS_DLL_PUBLIC gdouble tvb_get_letohieee_double(tvbuff_t *tvb,
    const gint offset);

/** Find size of stringz (NUL-terminated string) by looking for terminating
 * NUL.  The size of the string includes the terminating NUL.
 *
 * If the NUL isn't found, it throws the appropriate exception.
 */
WS_DLL_PUBLIC guint tvb_strsize(tvbuff_t *tvb, const gint offset);

/**
 * Given a tvbuff and an offset, with the offset assumed to refer to
 * a null-terminated string, find the length of that string (and throw
 * an exception if the tvbuff ends before we find the null), allocate
 * a buffer big enough to hold the string, copy the string into it,
 * and return a pointer to the string.  Also return the length of the
 * string (including the terminating null) through a pointer.
 *
 * This returns a constant (unmodifiable) string that does not need
 * to be freed; instead, it will automatically be freed once the next
 * packet is dissected.
 *
 * It is slightly more efficient than the other routines, but does *NOT*
 * do any translation to UTF-8 - the string consists of the raw octets
 * of the string, in whatever encoding they happen to be in, and, if
 * the string is not valid in that encoding, with invalid octet sequences
 * as they are in the packet.
 */
WS_DLL_PUBLIC const guint8 *tvb_get_const_stringz(tvbuff_t *tvb,
				    const gint offset, gint *lengthp);

struct epan_dissect;

/** @file
 *  Helper routines for column utility structures and routines.
 */

struct epan_column_info;
typedef struct epan_column_info column_info;

/**
 * All of the possible columns in summary listing.
 *
 * NOTE1: The entries MUST remain in this order, or else you need to reorder
 *        the slist[] and dlist[] arrays in column.c to match!
 *
 * NOTE2: Please add the COL_XYZ entry in the appropriate spot, such that the
 *        dlist[] array remains in alphabetical order!
 */
enum {
  COL_8021Q_VLAN_ID,  /**< 0) 802.1Q vlan ID */
  COL_ABS_YMD_TIME,   /**< 1) Absolute date, as YYYY-MM-DD, and time */
  COL_ABS_YDOY_TIME,  /**< 2) Absolute date, as YYYY/DOY, and time */
  COL_ABS_TIME,       /**< 3) Absolute time */
  COL_CIRCUIT_ID,     /**< 4) Circuit ID */
  COL_DSTIDX,         /**< 5) !! DEPRECATED !! - Dst port idx - Cisco MDS-specific */
  COL_SRCIDX,         /**< 6) !! DEPRECATED !! - Src port idx - Cisco MDS-specific */
  COL_VSAN,           /**< 7) VSAN - Cisco MDS-specific */
  COL_CUMULATIVE_BYTES, /**< 8) Cumulative number of bytes */
  COL_CUSTOM,         /**< 9) Custom column (any filter name's contents) */
  COL_DCE_CALL,       /**< 10) DCE/RPC connection oriented call id OR datagram sequence number */
  COL_DCE_CTX,        /**< 11) !! DEPRECATED !! - DCE/RPC connection oriented context id */
  COL_DELTA_TIME,     /**< 12) Delta time */
  COL_DELTA_CONV_TIME,/**< 13) Delta time to last frame in conversation */
  COL_DELTA_TIME_DIS, /**< 14) Delta time displayed*/
  COL_RES_DST,        /**< 15) Resolved dest */
  COL_UNRES_DST,      /**< 16) Unresolved dest */
  COL_RES_DST_PORT,   /**< 17) Resolved dest port */
  COL_UNRES_DST_PORT, /**< 18) Unresolved dest port */
  COL_DEF_DST,        /**< 19) Destination address */
  COL_DEF_DST_PORT,   /**< 20) Destination port */
  COL_EXPERT,         /**< 21) Expert Info */
  COL_IF_DIR,         /**< 22) FW-1 monitor interface/direction */
  COL_OXID,           /**< 23) !! DEPRECATED !! - Fibre Channel OXID */
  COL_RXID,           /**< 24) !! DEPRECATED !! - Fibre Channel RXID */
  COL_FR_DLCI,        /**< 25) !! DEPRECATED !! - Frame Relay DLCI */
  COL_FREQ_CHAN,      /**< 26) IEEE 802.11 (and WiMax?) - Channel */
  COL_BSSGP_TLLI,     /**< 27) !! DEPRECATED !! - GPRS BSSGP IE TLLI */
  COL_HPUX_DEVID,     /**< 28) !! DEPRECATED !! - HP-UX Nettl Device ID */
  COL_HPUX_SUBSYS,    /**< 29) !! DEPRECATED !! - HP-UX Nettl Subsystem */
  COL_DEF_DL_DST,     /**< 30) Data link layer dest address */
  COL_DEF_DL_SRC,     /**< 31) Data link layer source address */
  COL_RES_DL_DST,     /**< 32) Resolved DL dest */
  COL_UNRES_DL_DST,   /**< 33) Unresolved DL dest */
  COL_RES_DL_SRC,     /**< 34) Resolved DL source */
  COL_UNRES_DL_SRC,   /**< 35) Unresolved DL source */
  COL_RSSI,           /**< 36) IEEE 802.11 - received signal strength */
  COL_TX_RATE,        /**< 37) IEEE 802.11 - TX rate in Mbps */
  COL_DSCP_VALUE,     /**< 38) IP DSCP Value */
  COL_INFO,           /**< 39) Description */
  COL_COS_VALUE,      /**< 40) !! DEPRECATED !! - L2 COS Value */
  COL_RES_NET_DST,    /**< 41) Resolved net dest */
  COL_UNRES_NET_DST,  /**< 42) Unresolved net dest */
  COL_RES_NET_SRC,    /**< 43) Resolved net source */
  COL_UNRES_NET_SRC,  /**< 44) Unresolved net source */
  COL_DEF_NET_DST,    /**< 45) Network layer dest address */
  COL_DEF_NET_SRC,    /**< 46) Network layer source address */
  COL_NUMBER,         /**< 47) Packet list item number */
  COL_PACKET_LENGTH,  /**< 48) Packet length in bytes */
  COL_PROTOCOL,       /**< 49) Protocol */
  COL_REL_TIME,       /**< 50) Relative time */
  COL_REL_CONV_TIME,  /**< 51) !! DEPRECATED !! - Relative time to beginning of conversation */
  COL_DEF_SRC,        /**< 52) Source address */
  COL_DEF_SRC_PORT,   /**< 53) Source port */
  COL_RES_SRC,        /**< 54) Resolved source */
  COL_UNRES_SRC,      /**< 55) Unresolved source */
  COL_RES_SRC_PORT,   /**< 56) Resolved source port */
  COL_UNRES_SRC_PORT, /**< 57) Unresolved source port */
  COL_TEI,            /**< 58) Q.921 TEI */
  COL_UTC_YMD_TIME,   /**< 59) UTC date, as YYYY-MM-DD, and time */
  COL_UTC_YDOY_TIME,  /**< 60) UTC date, as YYYY/DOY, and time */
  COL_UTC_TIME,       /**< 61) UTC time */
  COL_CLS_TIME,       /**< 62) Command line-specified time (default relative) */
  NUM_COL_FMTS        /**< 63) Should always be last */
};

/** Add (replace) the text of a column element, the text will be copied.
 *
 * @param cinfo the current packet row
 * @param col the column to use, e.g. COL_INFO
 * @param str the string to add
 */
WS_DLL_PUBLIC void	col_add_str(column_info *cinfo, const gint col, const gchar *str);

/** Add (replace) the text of a column element, the text will be formatted and copied.
 *
 * Same function as col_add_str() but using a printf-like format string.
 *
 * @param cinfo the current packet row
 * @param col the column to use, e.g. COL_INFO
 * @param format the format string
 * @param ... the variable number of parameters
 */
WS_DLL_PUBLIC void col_add_fstr(column_info *cinfo, const gint col, const gchar *format, ...);

/** Sets a fence for the current column content,
 * so this content won't be affected by further col_... function calls.
 *
 * This can be useful if a protocol is more than once in a single packet,
 * e.g. multiple HTTP calls in a single TCP packet.
 *
 * @param cinfo the current packet row
 * @param col the column to use, e.g. COL_INFO
 */
WS_DLL_PUBLIC void	col_set_fence(column_info *cinfo, const gint col);

/** Clears the text of a column element.
 *
 * @param cinfo the current packet row
 * @param col the column to use, e.g. COL_INFO
 */
WS_DLL_PUBLIC void	col_clear(column_info *cinfo, const gint col);
/** Set (replace) the text of a column element, the text won't be copied.
 *
 * Usually used to set const strings!
 *
 * @param cinfo the current packet row
 * @param col the column to use, e.g. COL_INFO
 * @param str the string to set
 */
WS_DLL_PUBLIC void	col_set_str(column_info *cinfo, const gint col, const gchar * str);

#endif /* _ARSDK_WIRESHARK_H */
