/*
 * Wireshark dissector for ARSDK packets
 *
 * Author: Ivan Djelic <ivan.djelic@parrot.com>
 * Copyright (C) 2015 Parrot S.A.
 */
#ifndef _WIRESHARK_ARSDK_PROTO_H
#define _WIRESHARK_ARSDK_PROTO_H

#include <libARNetworkAL/ARNETWORKAL_Frame.h>
#include <libARCommands/ARCOMMANDS_Version.h>
#include "tree.h"

/* Buffer IDs: not exposed in public headers, not supposed to ever change */

#define NETWORK_CD_NONACK_ID 10
#define NETWORK_CD_ACK_ID 11
#define NETWORK_CD_EMERGENCY_ID 12
#define NETWORK_CD_VIDEO_ACK_ID 13
#define NETWORK_CD_SOUND_ACK_ID 14
#define NETWORK_CD_SOUND_DATA_ID 15

#define NETWORK_DC_NONACK_ID 127
#define NETWORK_DC_ACK_ID 126
#define NETWORK_DC_VIDEO_DATA_ID 125
#define NETWORK_DC_SOUND_DATA_ID 124
#define NETWORK_DC_SOUND_ACK_ID 123

#define NETWORK_CUSTOM_ID 42

#endif /* _WIRESHARK_ARSDK_PROTO_H */
