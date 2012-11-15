/**
 * @file libARCommands/commandsTypes.h
 * @brief This file contains all types needed for commands declaration and handling
 * @date 13/11/2012
 * @author nicolas.brulez@parrot.com
 */
#ifndef _LIBARCOMMANDS_COMMANDS_TYPES_H_
#define _LIBARCOMMANDS_COMMANDS_TYPES_H_ (1)

/**
 * @brief List of all args types for commands
 */
typedef enum {
    LIBARCOMMANDS_ARG_TYPE_END = LIBARCOMMANDS_ARG_TYPE_NONE, /*!< End of arg list */
    LIBARCOMMANDS_ARG_TYPE_U8, /*!< 8bit Unsigned int */
    LIBARCOMMANDS_ARG_TYPE_I8, /*!< 8bit Signed int */
    LIBARCOMMANDS_ARG_TYPE_U16, /*!< 16bit Unsigned int */
    LIBARCOMMANDS_ARG_TYPE_I16, /*!< 16bit Signed int */
    LIBARCOMMANDS_ARG_TYPE_U32, /*!< 32bit Unsigned int */
    LIBARCOMMANDS_ARG_TYPE_I32, /*!< 32bit Signed int */
    LIBARCOMMANDS_ARG_TYPE_U64, /*!< 64bit Unsigned int */
    LIBARCOMMANDS_ARG_TYPE_I64, /*!< 64bit Signed int */
    LIBARCOMMANDS_ARG_TYPE_FLOAT, /*!< IEEE 754-1985 Single precision floating point number (32 bits) */
    LIBARCOMMANDS_ARG_TYPE_DOUBLE, /*!< IEEE 754-1985 Double precision floating point number (64 bits) */
    LIBARCOMMANDS_ARG_TYPE_STRING, /*!< NULL-terminated UTF-8 string (as a char *) */
} eLIBARCOMMANDS_ARG_TYPE;

/**
 * Command type
 */
#define MAX_ARGS_PER_COMMAND (20)
typedef eLIBARCOMMANDS_ARG_TYPE libARCommandsCmd_t[MAX_ARGS_PER_COMMAND];

#endif /* _LIBARCOMMANDS_COMMANDS_TYPES_H_ */
