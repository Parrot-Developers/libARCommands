#include <libARCommands/commands.h>
#include <libSAL/print.h>
#include <stdarg.h>
#include <inttypes.h>

int
generateCommand (eLIBARCOMMANDS_COMMAND_CLASS class, int id, ...)
{
    libARCommandsCmd_t *cmd = getCommandArgsWithClassAndId (class, id);
    int i = 0;
    va_list ap;

    if (NULL == cmd)
    {
        return -1;
    }

    va_start (ap, id);
    SAL_PRINT (PRINT_WARNING, "New command : [%d][%d]\n", class, id);
    while (i < MAX_ARGS_PER_COMMAND && LIBARCOMMANDS_ARG_TYPE_END != (*cmd)[i])
    {
        eLIBARCOMMANDS_ARG_TYPE argtype = (*cmd)[i];
        SAL_PRINT (PRINT_WARNING, "Arg [%d] type = %d\n", i, argtype);

        switch (argtype)
        {
        case LIBARCOMMANDS_ARG_TYPE_U8: {
            uint8_t u8 = (uint8_t)va_arg (ap, uint32_t);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %u\n", i, u8);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_I8: {
            int8_t i8 = (int8_t)va_arg (ap, int32_t);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %d\n", i, i8);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_U16: {
            uint16_t u16 = (uint16_t)va_arg (ap, uint32_t);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %u\n", i, u16);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_I16: {
            int16_t i16 = (int16_t)va_arg (ap, int32_t);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %d\n", i, i16);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_U32: {
            uint32_t u32 = (uint32_t)va_arg (ap, uint32_t);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %u\n", i, u32);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_I32: {
            int32_t i32 = (int32_t)va_arg (ap, int32_t);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %d\n", i, i32);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_U64: {
            uint64_t u64 = (uint64_t)va_arg (ap, uint64_t);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %llu\n", i, u64);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_I64: {
            int64_t i64 = (int64_t)va_arg (ap, int64_t);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %lld\n", i, i64);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_FLOAT: {
            float flt = (float)va_arg (ap, double);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %f\n", i, flt);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_DOUBLE: {
            double dbl = (double)va_arg (ap, double);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %lf\n", i, dbl);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_STRING: {
            char *str = (char *)va_arg (ap, char *);
            SAL_PRINT (PRINT_WARNING, "Arg [%d] val = %s\n", i, str);
        } break;
        default: {
        } break;
        }
        i++;
    }
    SAL_PRINT (PRINT_WARNING, "\n");
    return 0;
}
