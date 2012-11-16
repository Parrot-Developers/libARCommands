/**
 * @file libARCommands/commands.h
 * @brief This file contains all the prototypes for commands codec
 * @date 15/11/2012
 * @author nicolas.brulez@parrot.com
 */
#ifndef _LIBARCOMMANDS_COMMANDS_H_
#define _LIBARCOMMANDS_COMMANDS_H_ (1)

#include <libARCommands/commandsList.h>

void generateCommand (eLIBARCOMMANDS_COMMAND_TYPE type, int id, ...);

#endif /* _LIBARCOMMANDS_COMMANDS_H_ */
