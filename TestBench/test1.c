#include <libARCommands/commands.h>

int
main (int argc, char *argv[])
{
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_U8, 42);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_I8, -42);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_U16, 4200);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_I16, -4200);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_U32, 420000);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_I32, -420000);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_U64, 420102030405ll);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_I64, -420102030405ll);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_FLOAT, 42.000001);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_DOUBLE, -42.000001);
    generateCommand (COMMAND_TYPE_TEST, TEST_CMD_STRING, "Quarante deux !");

    return 0;
}
