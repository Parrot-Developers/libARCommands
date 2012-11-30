#include <libARCommands/commands.h>
#include <libSAL/endianness.h>
#include <libSAL/print.h>
#include <stdarg.h>
#include <inttypes.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void
dumpHex (uint8_t *buffer, int bufflen)
{
    int i = 0;
    SAL_PRINT (PRINT_WARNING, "Dumping buffer :\n[ ");
    for (i = 0; i < bufflen; i++)
    {
        printf ("0x%02x ", buffer[i]);
    }
    printf ("]\n");
}

int
add8ToBuffer (uint8_t *buffer, uint8_t newVal, int oldOffset, int buffCap)
{
    buffer [oldOffset] = newVal;
    return oldOffset + sizeof (newVal);
}

int
add16ToBuffer (uint8_t *buffer, uint16_t newVal, int oldOffset, int buffCap)
{
    uint16_t *buffaddr = (uint16_t *)&buffer [oldOffset];
    *buffaddr = htods (newVal);
    return oldOffset + sizeof (newVal);
}

int
add32ToBuffer (uint8_t *buffer, uint32_t newVal, int oldOffset, int buffCap)
{
    uint32_t *buffaddr = (uint32_t *)&buffer [oldOffset];
    *buffaddr = htodl (newVal);
    return oldOffset + sizeof (newVal);
}

int
add64ToBuffer (uint8_t *buffer, uint64_t newVal, int oldOffset, int buffCap)
{
    uint64_t *buffaddr = (uint64_t *)&buffer [oldOffset];
    *buffaddr = htodll (newVal);
    return oldOffset + sizeof (newVal);
}

int addFloatToBuffer (uint8_t *buffer, float newVal, int oldOffset, int buffCap)
{
    return add32ToBuffer (buffer, *(uint32_t *)&newVal, oldOffset, buffCap);
}

int addDoubleToBuffer (uint8_t *buffer, double newVal, int oldOffset, int buffCap)
{
    return add64ToBuffer (buffer, *(uint64_t *)&newVal, oldOffset, buffCap);
}

int addStringToBuffer (uint8_t *buffer, const char *newVal, int oldOffset, int buffCap)
{
    char *buffaddr = (char *)&buffer [oldOffset];
    strcpy (buffaddr, newVal);
    return oldOffset + strlen (newVal) + 1;
}

#define DEFAULT_BUFFSIZE (128)

uint8_t *
generateCommand (eLIBARCOMMANDS_COMMAND_CLASS class, int id, ...)
{
    libARCommandsCmd_t *cmd = getCommandArgsWithClassAndId (class, id);
    int i = 0;
    va_list ap;
    uint8_t *buffer = NULL;
    int bufSize = 0;
    int buffCap = 0;

    if (NULL == cmd)
    {
        return buffer;
    }

    buffer = malloc (DEFAULT_BUFFSIZE * sizeof (uint8_t));
    if (NULL == buffer)
    {
        return NULL;
    }
    buffCap = DEFAULT_BUFFSIZE;


    bufSize = add16ToBuffer (buffer, class, bufSize, buffCap);
    bufSize = add16ToBuffer (buffer, id, bufSize, buffCap);

    va_start (ap, id);
    while (i < MAX_ARGS_PER_COMMAND && LIBARCOMMANDS_ARG_TYPE_END != (*cmd)[i])
    {
        eLIBARCOMMANDS_ARG_TYPE argtype = (*cmd)[i];

        switch (argtype)
        {
        case LIBARCOMMANDS_ARG_TYPE_U8: {
            uint8_t u8 = (uint8_t)va_arg (ap, uint32_t);
            bufSize = add8ToBuffer (buffer, u8, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_I8: {
            int8_t i8 = (int8_t)va_arg (ap, int32_t);
            bufSize = add8ToBuffer (buffer, (uint8_t)i8, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_U16: {
            uint16_t u16 = (uint16_t)va_arg (ap, uint32_t);
            bufSize = add16ToBuffer (buffer, u16, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_I16: {
            int16_t i16 = (int16_t)va_arg (ap, int32_t);
            bufSize = add16ToBuffer (buffer, (uint16_t)i16, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_U32: {
            uint32_t u32 = (uint32_t)va_arg (ap, uint32_t);
            bufSize = add32ToBuffer (buffer, u32, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_I32: {
            int32_t i32 = (int32_t)va_arg (ap, int32_t);
            bufSize = add32ToBuffer (buffer, (uint32_t)i32, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_U64: {
            uint64_t u64 = (uint64_t)va_arg (ap, uint64_t);
            bufSize = add64ToBuffer (buffer, u64, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_I64: {
            int64_t i64 = (int64_t)va_arg (ap, int64_t);
            bufSize = add64ToBuffer (buffer, (uint64_t)i64, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_FLOAT: {
            float flt = (float)va_arg (ap, double);
            bufSize = addFloatToBuffer (buffer, flt, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_DOUBLE: {
            double dbl = (double)va_arg (ap, double);
            bufSize = addDoubleToBuffer (buffer, dbl, bufSize, buffCap);
        } break;
        case LIBARCOMMANDS_ARG_TYPE_STRING: {
            char *str = (char *)va_arg (ap, char *);
            bufSize = addStringToBuffer (buffer, str, bufSize, buffCap);
        } break;
        default: {
        } break;
        }
        i++;
    }

    dumpHex (buffer, bufSize);
    //SAL_PRINT (PRINT_WARNING, "\n");
    return buffer;
}
