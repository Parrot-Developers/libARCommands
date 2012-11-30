from xml.dom.minidom import parseString
import sys
import os


#################################
# CONFIGURATION :               #
#################################
# Setup XML and C/HFiles Names  #
# Public header names must be   #
# LIB_NAME/fileName.h           #
#################################

LIB_NAME='libARCommands'

#Name (and path) of the xml file
XMLFILENAME='../Xml/commands.xml'

#Name of the output private header containing id enums
COMMANDSID_HFILE_NAME='ARCommandsId.h'

#Name of the output public header containing encoder helpers
COMMANDSGEN_HFILE_NAME=LIB_NAME+'/ARCommandsGen.h'

#Name of the output public header containing decoder helpers
COMMANDSDEC_HFILE_NAME=LIB_NAME+'/ARCommandsDec.h'

#Name of the output C file containing encoder helpers
COMMANDSGEN_CFILE_NAME='ARCommandsGen.c'

#Name of the output C file containing decoder helpers
COMMANDSDEC_CFILE_NAME='ARCommandsDec.c'

#Name of the output C testbench file
TB_CFILE_NAME='autoTest.c'

#Relative path of SOURCE dir
SRC_DIR='../Sources/'

#Relative path of INCLUDES dir
INC_DIR='../Includes/'

#Relative path of TESTBENCH dir
TB__DIR='../TestBench/'

##### END OF CONFIG #####

if not os.path.exists (SRC_DIR):
    os.mkdir(SRC_DIR)
if not os.path.exists (INC_DIR):
    os.mkdir(INC_DIR)
if not os.path.exists (INC_DIR+'/'+LIB_NAME):
    os.mkdir(INC_DIR+'/'+LIB_NAME)
if not os.path.exists (TB__DIR):
    os.mkdir(TB__DIR)

GENERATED_FILES = []
COMMANDSID_HFILE=SRC_DIR+COMMANDSID_HFILE_NAME
GENERATED_FILES.append (COMMANDSID_HFILE)
COMMANDSGEN_HFILE=INC_DIR+COMMANDSGEN_HFILE_NAME
GENERATED_FILES.append (COMMANDSGEN_HFILE)
COMMANDSGEN_CFILE=SRC_DIR+COMMANDSGEN_CFILE_NAME
GENERATED_FILES.append (COMMANDSGEN_CFILE)
COMMANDSDEC_HFILE=INC_DIR+COMMANDSDEC_HFILE_NAME
GENERATED_FILES.append (COMMANDSDEC_HFILE)
COMMANDSDEC_CFILE=SRC_DIR+COMMANDSDEC_CFILE_NAME
GENERATED_FILES.append (COMMANDSDEC_CFILE)
TB_CFILE=TB__DIR+TB_CFILE_NAME
GENERATED_FILES.append (TB_CFILE)


COMMANDSID_DEFINE='_'+COMMANDSID_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'
COMMANDSDEC_DEFINE='_'+COMMANDSDEC_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'
COMMANDSGEN_DEFINE='_'+COMMANDSGEN_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'

#XML to C type conversion
XMLTYPES = ['u8',       'i8',
            'u16',      'i16',
            'u32',      'i32',
            'u64',      'i64',
            'float',    'double',
            'string']
CTYPES   = ['uint8_t',  'int8_t',
            'uint16_t', 'int16_t',
            'uint32_t', 'int32_t',
            'uint64_t', 'int64_t',
            'float',    'double',
            'char *']
SZETYPES = ['8',        '8',
            '16',       '16',
            '32',       '32',
            '64',       '64',
            'Float',    'Double',
            'String']

def xmlToC(typ):
    xmlIndex = XMLTYPES.index(typ)
    return CTYPES [xmlIndex]

def xmlToSize(typ):
    xmlIndex = XMLTYPES.index(typ)
    return SZETYPES [xmlIndex]

#Sample args for testbench
SAMPLEARGS = ['42',              '-42',
              '4200',            '-4200',
              '420000',          '-420000',
              '420102030405ULL', '-420102030405LL',
              '42.000001',       '-42.000001',
              '"Test string with spaces"']

def xmlToSample(typ):
    xmlIndex = XMLTYPES.index(typ)
    return SAMPLEARGS [xmlIndex]


#################################
# If "-fname" is passed as an   #
# argument, just output the     #
# name of the files to generate #
#################################
if 2 <= len (sys.argv):
    if "-fname" == sys.argv[1]:
        for fil in GENERATED_FILES:
            print fil,
        print ''
        sys.exit (0)
#################################
# If "-dname" is passed as an   #
# argument, just output the     #
# name of the files to generate #
#################################
if 2 <= len (sys.argv):
    if "-dname" == sys.argv[1]:
        print TB__DIR,
        print SRC_DIR,
        print INC_DIR+LIB_NAME,
        print INC_DIR
        sys.exit (0)

#################################
# 1ST PART :                    #
#################################
# Read XML file to local arrays #
# of commands / classes         #
#################################
file = open (XMLFILENAME, 'r')
data = file.read ()
file.close ()

xmlfile = parseString (data)

# List of all classes names
allClassesNames = []
# List of all classes comment arrays
allClassesComments = []
# List of all commands names (for each class)
commandsNameByClass = []
# List of all commands comment arrays (for each class)
commandsCommentsByClass = []
# List of all arg names (for each class / command)
argNamesByClassAndCommand = []
# List of all arg types (for each class / command)
argTypesByClassAndCommand = []
# List of all arg comment arrays (for each class / command)
argCommentsByClassAndCommand = []

classes = xmlfile.getElementsByTagName('class')
for cmdclass in classes:
    classname = cmdclass.attributes["name"].nodeValue
    classcommArrayUnstripped = cmdclass.firstChild.data.splitlines ()
    classcommArray = []
    for classComm in classcommArrayUnstripped:
        stripName = classComm.strip ()
        if len(stripName) != 0:
            classcommArray.append (stripName)
    allClassesNames.append (classname)
    allClassesComments.append (classcommArray)
    CNCurrClass = []
    CCCurrClass = []
    ANCurrClass = []
    ATCurrClass = []
    ACCurrClass = []
    commands = cmdclass.getElementsByTagName('cmd')
    for command in commands:
        commandname = command.attributes["name"].nodeValue
        commandcommArrayUnstripped = command.firstChild.data.splitlines ()
        commandcommArray = []
        for commandComm in commandcommArrayUnstripped:
            stripName = commandComm.strip ()
            if len (stripName) != 0:
                commandcommArray.append (stripName)
        CNCurrClass.append (commandname)
        CCCurrClass.append (commandcommArray)
        ANCurrCommand = []
        ATCurrCommand = []
        ACCurrCommand = []
        args = command.getElementsByTagName('arg')
        for arg in args:
            argName = arg.attributes["name"].nodeValue
            ANCurrCommand.append (argName)
            argType = arg.attributes["type"].nodeValue
            ATCurrCommand.append (argType)
            argCommArrayUnstripped = arg.firstChild.data.splitlines()
            argCommArray = []
            for argComm in argCommArrayUnstripped:
                stripName = argComm.strip ()
                if len (stripName) != 0:
                    argCommArray.append (stripName)
            ACCurrCommand.append (argCommArray)
        ANCurrClass.append (ANCurrCommand)
        ATCurrClass.append (ATCurrCommand)
        ACCurrClass.append (ACCurrCommand)
    commandsNameByClass.append (CNCurrClass)
    commandsCommentsByClass.append (CCCurrClass)
    argNamesByClassAndCommand.append (ANCurrClass)
    argTypesByClassAndCommand.append (ATCurrClass)
    argCommentsByClassAndCommand.append (ACCurrClass)


print 'Commands parsed:'
for cl in allClassesNames:
    cIndex = allClassesNames.index(cl)
    print '-> ' + cl
    print '   /* '
    for classComm in allClassesComments[cIndex]:
        print '    * ' + classComm
    print '    */'
    cmdList = commandsNameByClass[cIndex]
    cmdCommList = commandsCommentsByClass[cIndex]
    for cmd in cmdList:
        cmIndex = cmdList.index(cmd)
        print ' --> ' + cmd
        print '     /* '
        for cmdComm in cmdCommList[cmIndex]:
            print '      * ' + cmdComm
        print '      */'
        ANList = argNamesByClassAndCommand [cIndex][cmIndex]
        ATList = argTypesByClassAndCommand [cIndex][cmIndex]
        ACList = argCommentsByClassAndCommand [cIndex][cmIndex]
        for argN in ANList:
            aIndex = ANList.index(argN)
            argT = ATList [aIndex]
            print '   (' + argT + ' ' + argN + ')'
            print '    /* '
            for argC in ACList [aIndex]:
                print '     * ' + argC
            print '     */'

#sys.exit (0)


#################################
# 2ND PART :                    #
#################################
# Write private H file          #
#################################

hfile = open (COMMANDSID_HFILE, 'w')

hfile.write ('/********************************************\n')
hfile.write (' *            AUTOGENERATED FILE            *\n')
hfile.write (' *             DO NOT MODIFY IT             *\n')
hfile.write (' *                                          *\n')
hfile.write (' * To add new commands :                    *\n')
hfile.write (' *  - Modify ../Xml/commands.xml file       *\n')
hfile.write (' *  - Re-run generateCommandsList.py script *\n')
hfile.write (' *                                          *\n')
hfile.write (' ********************************************/\n')
hfile.write ('\n')
hfile.write ('#ifndef '+COMMANDSID_DEFINE+'\n')
hfile.write ('#define '+COMMANDSID_DEFINE+' (1)\n')
hfile.write ('\n')
hfile.write ('typedef enum {\n')
first = 1
for cl in allClassesNames:
    if 1 == first:
        hfile.write ('    COMMAND_CLASS_' + cl.upper () + ' = 0,\n')
        first = 0
    else:
        hfile.write ('    COMMAND_CLASS_' + cl.upper () + ',\n')
hfile.write ('    COMMAND_CLASS_MAX,\n')
hfile.write ('} eLIBARCOMMANDS_COMMAND_CLASS;\n')
hfile.write ('\n')
hfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    hfile.write ('typedef enum {\n')
    tname = cl.upper ()
    cmdList = commandsNameByClass [clIndex]
    first = 1
    for cmd in cmdList:
        if 1 == first:
            hfile.write ('    ' + tname + '_CMD_' + cmd.upper () + ' = 0,\n')
            first = 0
        else:
            hfile.write ('    ' + tname + '_CMD_' + cmd.upper () + ',\n')
    hfile.write ('    ' + tname  + '_CMD_MAX,\n')
    hfile.write ('} eLIBARCOMMANDS_' + tname + '_CMD;\n')
    hfile.write ('\n')

hfile.write ('\n')
hfile.write ('#endif /* '+COMMANDSID_DEFINE+' */\n')

hfile.close ()

#sys.exit (0)

#################################
# 3RD PART :                    #
#################################
# Generate public coder H file  #
#################################

hfile = open (COMMANDSGEN_HFILE, 'w')

hfile.write ('/********************************************\n')
hfile.write (' *            AUTOGENERATED FILE            *\n')
hfile.write (' *             DO NOT MODIFY IT             *\n')
hfile.write (' *                                          *\n')
hfile.write (' * To add new commands :                    *\n')
hfile.write (' *  - Modify ../../Xml/commands.xml file    *\n')
hfile.write (' *  - Re-run generateCommandsList.py script *\n')
hfile.write (' *                                          *\n')
hfile.write (' ********************************************/\n')
hfile.write ('#ifndef '+COMMANDSGEN_DEFINE+'\n')
hfile.write ('#define '+COMMANDSGEN_DEFINE+' (1)\n')
hfile.write ('#include <inttypes.h>\n')
hfile.write ('\n')
hfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    hfile.write ('// Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        hfile.write ('\n/**\n')
        commList = commandsCommentsByClass [clIndex][cmdIndex]
        commZero = commList [0]
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        ACList = argCommentsByClassAndCommand [clIndex][cmdIndex]
        hfile.write (' * @brief ' + commZero + '\n')
        for comm in commList:
            hfile.write (' * ' + comm + '\n')
        hfile.write (' * @param buffLen pointer to an unsigned integer that will contain the size of the allocated command\n')
        hfile.write (' * @warning This function allocate memory. You need to call free() on the buffer !\n')
        for argN in ANList:
            ACListCurrArg = ACList [ANList.index (argN)]
            for argC in ACListCurrArg:
                hfile.write (' * @param ' + argN + ' ' + argC + '\n')
        hfile.write (' * @return Pointer to the command buffer (NULL if any error occured)\n')
        hfile.write (' */\n')
        hfile.write ('uint8_t *libARCommands' + cl.capitalize() + 'Generate' + cmd.capitalize() + ' (int32_t *buffLen')
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            hfile.write (', ' + xmlToC(argT) + ' ' + argN)
        hfile.write (');\n')
    hfile.write ('\n')

hfile.write ('#endif /* '+COMMANDSGEN_DEFINE+' */\n')

hfile.close ()

#################################
# 4TH PART :                    #
#################################
# Generate coder C part         #
#################################

cfile = open (COMMANDSGEN_CFILE, 'w')

cfile.write ('/********************************************\n')
cfile.write (' *            AUTOGENERATED FILE            *\n')
cfile.write (' *             DO NOT MODIFY IT             *\n')
cfile.write (' *                                          *\n')
cfile.write (' * To add new commands :                    *\n')
cfile.write (' *  - Modify ../Xml/commands.xml file       *\n')
cfile.write (' *  - Re-run generateCommandsList.py script *\n')
cfile.write (' *                                          *\n')
cfile.write (' ********************************************/\n')
cfile.write ('#include <config.h>\n')
cfile.write ('#include <inttypes.h>\n')
cfile.write ('#include <stdlib.h>\n')
cfile.write ('#include <string.h>\n')
cfile.write ('#include <'+COMMANDSGEN_HFILE_NAME+'>\n')
cfile.write ('#include <'+COMMANDSID_HFILE_NAME+'>\n')
cfile.write ('#include <libSAL/endianness.h>\n')
cfile.write ('\n')
cfile.write ('/*\n')
cfile.write (' * Default size of a generated buffer\n')
cfile.write (' * This is also the size of each increment if we need a larger buffer\n')
cfile.write (' */\n')
cfile.write ('#define LIBARCOMMANDS_GEN_BUFFER_SIZE (128)\n')
cfile.write ('\n')
cfile.write ('// Add an 8 bit value to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t add8ToBuffer (uint8_t **buffer, uint8_t newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + LIBARCOMMANDS_GEN_BUFFER_SIZE;\n')
cfile.write ('        uint8_t *newBuf = realloc (*buffer, newSize);\n')
cfile.write ('        if (NULL == newBuf)\n')
cfile.write ('        {\n')
cfile.write ('            free (*buffer);\n')
cfile.write ('            *buffer = NULL;\n')
cfile.write ('            *buffCap = 0;\n')
cfile.write ('            return -1;\n')
cfile.write ('        }\n')
cfile.write ('        else\n')
cfile.write ('        {\n')
cfile.write ('            *buffer = newBuf;\n')
cfile.write ('            *buffCap = newSize;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('    *buffer [oldOffset] = newVal;\n')
cfile.write ('    return oldOffset + sizeof (newVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a 16 bit value to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t add16ToBuffer (uint8_t **buffer, uint16_t newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + LIBARCOMMANDS_GEN_BUFFER_SIZE;\n')
cfile.write ('        uint8_t *newBuf = realloc (*buffer, newSize);\n')
cfile.write ('        if (NULL == newBuf)\n')
cfile.write ('        {\n')
cfile.write ('            free (*buffer);\n')
cfile.write ('            *buffer = NULL;\n')
cfile.write ('            *buffCap = 0;\n')
cfile.write ('            return -1;\n')
cfile.write ('        }\n')
cfile.write ('        else\n')
cfile.write ('        {\n')
cfile.write ('            *buffer = newBuf;\n')
cfile.write ('            *buffCap = newSize;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('    uint16_t *buffptr = (uint16_t *)&((*buffer) [oldOffset]);\n')
cfile.write ('    *buffptr = htods (newVal);\n')
cfile.write ('    return oldOffset + sizeof (newVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a 32 bit value to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t add32ToBuffer (uint8_t **buffer, uint32_t newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + LIBARCOMMANDS_GEN_BUFFER_SIZE;\n')
cfile.write ('        uint8_t *newBuf = realloc (*buffer, newSize);\n')
cfile.write ('        if (NULL == newBuf)\n')
cfile.write ('        {\n')
cfile.write ('            free (*buffer);\n')
cfile.write ('            *buffer = NULL;\n')
cfile.write ('            *buffCap = 0;\n')
cfile.write ('            return -1;\n')
cfile.write ('        }\n')
cfile.write ('        else\n')
cfile.write ('        {\n')
cfile.write ('            *buffer = newBuf;\n')
cfile.write ('            *buffCap = newSize;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('    uint32_t *buffptr = (uint32_t *)&((*buffer) [oldOffset]);\n')
cfile.write ('    *buffptr = htodl (newVal);\n')
cfile.write ('    return oldOffset + sizeof (newVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a 64 bit value to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t add64ToBuffer (uint8_t **buffer, uint64_t newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + LIBARCOMMANDS_GEN_BUFFER_SIZE;\n')
cfile.write ('        uint8_t *newBuf = realloc (*buffer, newSize);\n')
cfile.write ('        if (NULL == newBuf)\n')
cfile.write ('        {\n')
cfile.write ('            free (*buffer);\n')
cfile.write ('            *buffer = NULL;\n')
cfile.write ('            *buffCap = 0;\n')
cfile.write ('            return -1;\n')
cfile.write ('        }\n')
cfile.write ('        else\n')
cfile.write ('        {\n')
cfile.write ('            *buffer = newBuf;\n')
cfile.write ('            *buffCap = newSize;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('    uint64_t *buffptr = (uint64_t *)&((*buffer) [oldOffset]);\n')
cfile.write ('    *buffptr = htodll (newVal);\n')
cfile.write ('    return oldOffset + sizeof (newVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a string to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t addStringToBuffer (uint8_t **buffer, const char *newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + strlen (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + LIBARCOMMANDS_GEN_BUFFER_SIZE;\n')
cfile.write ('        uint8_t *newBuf = realloc (*buffer, newSize);\n')
cfile.write ('        if (NULL == newBuf)\n')
cfile.write ('        {\n')
cfile.write ('            free (*buffer);\n')
cfile.write ('            *buffer = NULL;\n')
cfile.write ('            *buffCap = 0;\n')
cfile.write ('            return -1;\n')
cfile.write ('        }\n')
cfile.write ('        else\n')
cfile.write ('        {\n')
cfile.write ('            *buffer = newBuf;\n')
cfile.write ('            *buffCap = newSize;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('    char *buffptr = (char *)&((*buffer) [oldOffset]);\n')
cfile.write ('    strcpy (buffptr, newVal);\n')
cfile.write ('    return oldOffset + strlen (newVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a float to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t addFloatToBuffer (uint8_t **buffer, float newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    return add32ToBuffer (buffer, *(uint32_t *)&newVal, oldOffset, buffCap);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a double to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t addDoubleToBuffer (uint8_t **buffer, double newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    return add64ToBuffer (buffer, *(uint64_t *)&newVal, oldOffset, buffCap);\n')
cfile.write ('}\n')
cfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cfile.write ('// Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        cfile.write ('uint8_t *libARCommands' + cl.capitalize() + 'Generate' + cmd.capitalize() + ' (int32_t *buffLen')
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            cfile.write (', ' + xmlToC(argT) + ' ' + argN)
        cfile.write (')\n')
        cfile.write ('{\n')
        cfile.write ('    uint8_t *buffer = NULL;\n')
        cfile.write ('    int32_t currBufferSize = 0;\n')
        cfile.write ('    int32_t currIndexInBuffer = 0;\n')
        cfile.write ('    int noError = 1;\n')
        cfile.write ('    if (NULL == buffLen)\n')
        cfile.write ('    {\n')
        cfile.write ('        return NULL;\n')
        cfile.write ('    }\n')
        cfile.write ('    buffer = malloc (LIBARCOMMANDS_GEN_BUFFER_SIZE * sizeof (uint8_t));\n')
        cfile.write ('    if (NULL == buffer)\n')
        cfile.write ('        noError = 0;\n')
        cfile.write ('\n')
        cfile.write ('    // Write class header\n')
        cfile.write ('    if (1 == noError)\n')
        cfile.write ('    {\n')
        cfile.write ('        currIndexInBuffer = add16ToBuffer (&buffer, COMMAND_CLASS_' + cl.upper() + ', currIndexInBuffer, &currBufferSize);\n')
        cfile.write ('        if (-1 == currIndexInBuffer)\n')
        cfile.write ('        {\n')
        cfile.write ('            noError = 0;\n')
        cfile.write ('        }\n')
        cfile.write ('    }\n')
        cfile.write ('    // Write id header\n')
        cfile.write ('    if (1 == noError)\n')
        cfile.write ('    {\n')
        cfile.write ('        currIndexInBuffer = add16ToBuffer (&buffer, ' + cl.upper () + '_CMD_' + cmd.upper() + ', currIndexInBuffer, &currBufferSize);\n')
        cfile.write ('        if (-1 == currIndexInBuffer)\n')
        cfile.write ('        {\n')
        cfile.write ('            noError = 0;\n')
        cfile.write ('        }\n')
        cfile.write ('    }\n')
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList[aIndex]
            cfile.write ('    // Write arg '+ argN + '\n')
            cfile.write ('    if (1 == noError)\n')
            cfile.write ('    {\n')
            cfile.write ('        currIndexInBuffer = add' + xmlToSize (argT) + 'ToBuffer (&buffer, ' + argN + ', currIndexInBuffer, &currBufferSize);\n')
            cfile.write ('        if (-1 == currIndexInBuffer)\n')
            cfile.write ('        {\n')
            cfile.write ('            noError = 0;\n')
            cfile.write ('        }\n')
            cfile.write ('    }\n')
        cfile.write ('    if (1 == noError)\n')
        cfile.write ('    {\n')
        cfile.write ('        *buffLen = currIndexInBuffer;\n')
        cfile.write ('    }\n')
        cfile.write ('    return buffer;\n')
        cfile.write ('}\n\n')
    cfile.write ('\n')

cfile.write ('// END GENERATED CODE\n')

cfile.close ()

#################################
# 4TH PART :                    #
#################################
# Generate C Testbench          #
#################################

cfile = open (TB_CFILE, 'w')

cfile.write ('/********************************************\n')
cfile.write (' *            AUTOGENERATED FILE            *\n')
cfile.write (' *             DO NOT MODIFY IT             *\n')
cfile.write (' *                                          *\n')
cfile.write (' * To add new commands :                    *\n')
cfile.write (' *  - Modify ../Xml/commands.xml file       *\n')
cfile.write (' *  - Re-run generateCommandsList.py script *\n')
cfile.write (' *                                          *\n')
cfile.write (' ********************************************/\n')
cfile.write ('#include <'+COMMANDSGEN_HFILE_NAME+'>\n')
cfile.write ('#include <libSAL/print.h>\n')
cfile.write ('#include <stdlib.h>\n')
cfile.write ('\n')
cfile.write ('\n')
cfile.write ('int\n')
cfile.write ('main (int argc, char *argv[])\n')
cfile.write ('{\n')
cfile.write ('    uint8_t *res = NULL;\n')
cfile.write ('    int32_t resSize = 0;\n')
cfile.write ('    int errcount = 0;\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cfile.write ('    // Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        cfile.write ('    res = libARCommands' + cl.capitalize() + 'Generate' + cmd.capitalize() + ' (&resSize')
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            cfile.write (', ' + xmlToSample(argT))
        cfile.write (');\n')
        cfile.write ('    if (NULL == res)\n')
        cfile.write ('    {\n')
        cfile.write ('        SAL_PRINT (PRINT_ERROR, "Error while generating command ' + cl.capitalize() + '.' + cmd.capitalize() + '\\n");\n')
        cfile.write ('        errcount++;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        SAL_PRINT (PRINT_DEBUG, "Generating command ' + cl.capitalize() + '.' + cmd.capitalize() + ' succeded\\n");\n')
        cfile.write ('        free (res); res = NULL;\n')
        cfile.write ('    }\n')
        cfile.write ('\n')
    cfile.write ('\n')

cfile.write ('    return errcount;\n')
cfile.write ('}\n')

cfile.close ()
