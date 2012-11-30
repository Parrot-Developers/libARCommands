/**
 * @file libARCommands/commands.h
 * @brief This file contains all the prototypes for commands codec
 * @date 15/11/2012
 * @author nicolas.brulez@parrot.com
 */
#ifndef _LIBARCOMMANDS_COMMANDS_H_
#define _LIBARCOMMANDS_COMMANDS_H_ (1)

#include <libARCommands/commandsList.h>
#include <inttypes.h>

uint8_t *generateCommand (eLIBARCOMMANDS_COMMAND_CLASS class, int id, ...);

#endif /* _LIBARCOMMANDS_COMMANDS_H_ */
