/**
 * @file libCommands/commandsTypes.h
 * @brief This file contains all types needed for commands declaration and handling
 * @date 13/11/2012
 * @author nicolas.brulez@parrot.com
 */
#ifndef _LIBCOMMANDS_COMMANDS_TYPES_H_
#define _LIBCOMMANDS_COMMANDS_TYPES_H_ (1)

#include <endian.h>

/**
 * @brief List of all args types for commands
 */
typedef enum
{
    LIBCOMMANDS_ARG_TYPE_NONE = 0, /*!< No args to the command */
    LIBCOMMANDS_ARG_TYPE_END = LIBCOMMANDS_ARG_TYPE_NONE, /*!< End of arg list (equivalent to LIBCOMMANDS_ARG_TYPE_NONE) */
    LIBCOMMANDS_ARG_TYPE_U8, /*!< 8bit Unsigned int */
    LIBCOMMANDS_ARG_TYPE_I8, /*!< 8bit Signed int */
    LIBCOMMANDS_ARG_TYPE_U16, /*!< 16bit Unsigned int */
    LIBCOMMANDS_ARG_TYPE_I16, /*!< 16bit Signed int */
    LIBCOMMANDS_ARG_TYPE_U32, /*!< 32bit Unsigned int */
    LIBCOMMANDS_ARG_TYPE_I32, /*!< 32bit Signed int */
    LIBCOMMANDS_ARG_TYPE_U64, /*!< 64bit Unsigned int */
    LIBCOMMANDS_ARG_TYPE_I64, /*!< 64bit Signed int */
    LIBCOMMANDS_ARG_TYPE_FLOAT, /*!< IEEE 754-1985 Single precision floating point number (32 bits) */
    LIBCOMMANDS_ARG_TYPE_DOUBLE, /*!< IEEE 754-1985 Double precision floating point number (64 bits) */
    LIBCOMMANDS_ARG_TYPE_STRING, /*!< NULL-terminated UTF-8 string (as a char *) */
} eLIBCOMMANDS_ARG_TYPE;

#if __BYTE_ORDER == __LITTLE_ENDIAN
#elif __BYTE_ORDER == __BIG_ENDIAN
#ense
#endif

#endif /* _LIBCOMMANDS_COMMANDS_TYPES_H_ */
