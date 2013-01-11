from xml.dom.minidom import parseString
import sys
import os

#################################
# CONFIGURATION :               #
#################################
# Setup XML and C/HFiles Names  #
# Public header names must be   #
# LIB_NAME+'/fileName.h'        #
#################################

LIB_NAME='libARCommands'
JNI_PACKAGE_NAME='com.parrot.ardsdk.'+LIB_NAME.lower()

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

#Name of the output C/H common testbench file
TB_CFILE_NAME='autoTest.c'
TB_HFILE_NAME='autoTest.h'
#Tag for tb SAL_PRINT calls
TB_TAG='AutoTest'

#Name of the linux entry point file for autotest
TB_LIN_CFILE_NAME='autoTest_linux.c'

#Name of the JNI C File
JNI_CFILE_NAME='ARCommandsJNI.c'

#Name of the JNI JAVA File
JNI_JFILE_NAME='ARCommand.java'

#Relative path of SOURCE dir
SRC_DIR='../Sources/'

#Relative path of INCLUDES dir
INC_DIR='../Includes/'

#Relative path of TESTBENCH dir
TB__DIR='../TestBench/'

#Relative path of unix-like (Linux / os-x) TESTBENCH dir
LIN_TB_DIR=TB__DIR+'linux/'

#Relative path of multiplatform code for testbenches
COM_TB_DIR=TB__DIR+'common/'

#Relative path of JNI dir
JNI_DIR="../JNI/"

#Relative path of JNI/C dir
JNIC_DIR=JNI_DIR+'c/'

#Relative path of JNI/Java dir
JNIJ_DIR=JNI_DIR+'java/'

##### END OF CONFIG #####

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
TB_CFILE=COM_TB_DIR+TB_CFILE_NAME
GENERATED_FILES.append (TB_CFILE)
TB_HFILE=COM_TB_DIR+TB_HFILE_NAME
GENERATED_FILES.append (TB_HFILE)
TB_LIN_CFILE=LIN_TB_DIR+TB_LIN_CFILE_NAME
GENERATED_FILES.append (TB_LIN_CFILE)
JNI_CFILE=JNIC_DIR+JNI_CFILE_NAME
GENERATED_FILES.append (JNI_CFILE)
JNI_JFILE=JNIJ_DIR+JNI_JFILE_NAME
GENERATED_FILES.append (JNI_JFILE)


COMMANDSID_DEFINE='_'+COMMANDSID_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'
COMMANDSDEC_DEFINE='_'+COMMANDSDEC_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'
COMMANDSGEN_DEFINE='_'+COMMANDSGEN_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'
TB_DEFINE='_'+TB_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'

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
CTYPES_WC = ['uint8_t',  'int8_t',
            'uint16_t', 'int16_t',
            'uint32_t', 'int32_t',
            'uint64_t', 'int64_t',
            'float',    'double',
            'const char *']
SZETYPES = ['U8',       'U8',
            'U16',      'U16',
            'U32',      'U32',
            'U64',      'U64',
            'Float',    'Double',
            'String']
CREADERS = ['read8FromBuffer',     '(int8_t)read8FromBuffer',
            'read16FromBuffer',    '(int16_t)read16FromBuffer',
            'read32FromBuffer',    '(int32_t)read32FromBuffer',
            'read64FromBuffer',    '(int64_t)read64FromBuffer',
            'readFloatFromBuffer', 'readDoubleFromBuffer',
            'readStringFromBuffer']
# No unsigned types in java, so use signed types everywhere
JAVATYPES = ['byte',    'byte',
             'short',   'short',
             'int',     'int',
             'long',    'long',
             'float',   'double',
             'String']
JNITYPES  = ['jbyte',    'jbyte',
             'jshort',   'jshort',
             'jint',     'jint',
             'jlong',    'jlong',
             'jfloat',   'jdouble',
             'jstring']

def xmlToC(typ):
    xmlIndex = XMLTYPES.index(typ)
    return CTYPES [xmlIndex]

def xmlToCwithConst(typ):
    xmlIndex = XMLTYPES.index(typ)
    return CTYPES_WC [xmlIndex]

def xmlToSize(typ):
    xmlIndex = XMLTYPES.index(typ)
    return SZETYPES [xmlIndex]

def xmlToReader(typ):
    xmlIndex = XMLTYPES.index(typ)
    return CREADERS [xmlIndex]

def xmlToJava(typ):
    xmlIndex = XMLTYPES.index(typ)
    return JAVATYPES [xmlIndex]

def xmlToJni(typ):
    xmlIndex = XMLTYPES.index(typ)
    return JNITYPES [xmlIndex]

#Sample args for testbench
SAMPLEARGS = ['42',              '-42',
              '4200',            '-4200',
              '420000',          '-420000',
              '420102030405ULL', '-420102030405LL',
              '42.125',          '-42.000001',
              '"Test string with spaces"']
PRINTFF    = ['%u',   '%d',
              '%u',   '%d',
              '%u',   '%d',
              '%llu', '%lld',
              '%f',   '%f',
              '%s']

def xmlToSample(typ):
    xmlIndex = XMLTYPES.index(typ)
    return SAMPLEARGS [xmlIndex]

def xmlToPrintf(typ):
    xmlIndex = XMLTYPES.index(typ)
    return PRINTFF [xmlIndex]


#################################
# If "-fname" is passed as an   #
# argument, just output the     #
# name of the generated files   #
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
# name of the generated dirs    #
#################################
if 2 <= len (sys.argv):
    if "-dname" == sys.argv[1]:
        print SRC_DIR,
        print INC_DIR+LIB_NAME,
        print INC_DIR,
        print LIN_TB_DIR,
        print COM_TB_DIR,
        print TB__DIR,
        print JNIJ_DIR,
        print JNIC_DIR,
        print JNI_DIR
        sys.exit (0)
#################################
# If "-nogen" is passed as an   #
# argument, don't generate any  #
# file                          #
#################################
noGen="no"
if 2 <= len (sys.argv):
    if "-nogen" == sys.argv[1]:
        noGen="yes"


if not os.path.exists (SRC_DIR):
    os.mkdir(SRC_DIR)
if not os.path.exists (INC_DIR):
    os.mkdir(INC_DIR)
if not os.path.exists (INC_DIR+LIB_NAME):
    os.mkdir(INC_DIR+LIB_NAME)
if not os.path.exists (TB__DIR):
    os.mkdir(TB__DIR)
if not os.path.exists (LIN_TB_DIR):
    os.mkdir(LIN_TB_DIR)
if not os.path.exists (COM_TB_DIR):
    os.mkdir(COM_TB_DIR)
if not os.path.exists (JNI_DIR):
    os.mkdir(JNI_DIR)
if not os.path.exists (JNIC_DIR):
    os.mkdir(JNIC_DIR)
if not os.path.exists (JNIJ_DIR):
    os.mkdir(JNIJ_DIR)

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

"""
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
"""

if "yes" == noGen: # called with "-nogen"
    sys.exit (0)


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
hfile.write ('} eARCOMMANDS_COMMAND_CLASS;\n')
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
    hfile.write ('} eARCOMMANDS_' + tname + '_CMD;\n')
    hfile.write ('\n')

hfile.write ('\n')
hfile.write ('#endif /* '+COMMANDSID_DEFINE+' */\n')

hfile.close ()

#################################
# 3RD PART :                    #
#################################
# Generate public coder H file  #
#################################

hfile = open (COMMANDSGEN_HFILE, 'w')

hfile.write ('/**\n')
hfile.write (' * @file ' + COMMANDSGEN_HFILE_NAME + '\n')
hfile.write (' * @brief libARCommands encoder header.\n')
hfile.write (' * This file contains all declarations needed to encode commands\n')
hfile.write (' * @note Autogenerated file\n')
hfile.write (' **/\n')
hfile.write ('#ifndef '+COMMANDSGEN_DEFINE+'\n')
hfile.write ('#define '+COMMANDSGEN_DEFINE+'\n')
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
        first = 1
        for comm in commList:
            if 1 == first:
                first = 0
            else:
                hfile.write (' * ' + comm + '\n')
        hfile.write (' * @warning This function allocate memory. You need to call libARCommandsFree() on the buffer pointer !\n')
        hfile.write (' * @param buffLen pointer to an integer that will contain the size of the allocated command\n')
        for argN in ANList:
            ACListCurrArg = ACList [ANList.index (argN)]
            for argC in ACListCurrArg:
                hfile.write (' * @param ' + argN + ' ' + argC + '\n')
        hfile.write (' * @return Pointer to the command buffer (NULL if any error occured)\n')
        hfile.write (' */\n')
        hfile.write ('uint8_t *ARCommandsGenerate' + cl.capitalize() + cmd.capitalize() + ' (int32_t *buffLen')
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            hfile.write (', ' + xmlToCwithConst(argT) + ' ' + argN)
        hfile.write (');\n')
    hfile.write ('\n')

hfile.write ('/**\n')
hfile.write (' * @brief Free a command, and sets its pointer to NULL\n')
hfile.write (' * @param cmdBuf pointer to the command pointer to be freed\n')
hfile.write (' */\n')
hfile.write ('void ARCommandsFree (uint8_t **cmdBuf);\n')
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
cfile.write ('#define ARCOMMANDS_GEN_BUFFER_SIZE (128)\n')
cfile.write ('\n')
cfile.write ('// Add an 8 bit value to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t addU8ToBuffer (uint8_t **buffer, uint8_t newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + ARCOMMANDS_GEN_BUFFER_SIZE;\n')
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
cfile.write ('    uint8_t *buffptr = (uint8_t *)&((*buffer) [oldOffset]);\n')
cfile.write ('    *buffptr = newVal;\n')
cfile.write ('    return oldOffset + sizeof (newVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a 16 bit value to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t addU16ToBuffer (uint8_t **buffer, uint16_t newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + ARCOMMANDS_GEN_BUFFER_SIZE;\n')
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
cfile.write ('int32_t addU32ToBuffer (uint8_t **buffer, uint32_t newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + ARCOMMANDS_GEN_BUFFER_SIZE;\n')
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
cfile.write ('int32_t addU64ToBuffer (uint8_t **buffer, uint64_t newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    while (*buffCap <= (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        // Need to realloc\n')
cfile.write ('        int32_t newSize = *buffCap + ARCOMMANDS_GEN_BUFFER_SIZE;\n')
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
cfile.write ('        int32_t newSize = *buffCap + ARCOMMANDS_GEN_BUFFER_SIZE;\n')
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
cfile.write ('    return oldOffset + strlen (newVal) + 1;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a float to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t addFloatToBuffer (uint8_t **buffer, float newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    return addU32ToBuffer (buffer, *(uint32_t *)&newVal, oldOffset, buffCap);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a double to the buffer (auto expand buffer if needed)\n')
cfile.write ('// Return -1 and set buffCap to zero if a realloc fails\n')
cfile.write ('int32_t addDoubleToBuffer (uint8_t **buffer, double newVal, int32_t oldOffset, int32_t *buffCap)\n')
cfile.write ('{\n')
cfile.write ('    return addU64ToBuffer (buffer, *(uint64_t *)&newVal, oldOffset, buffCap);\n')
cfile.write ('}\n')
cfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cfile.write ('// Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        cfile.write ('uint8_t *ARCommandsGenerate' +  cl.capitalize() + cmd.capitalize() + ' (int32_t *buffLen')
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            cfile.write (', ' + xmlToCwithConst(argT) + ' ' + argN)
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
        cfile.write ('    buffer = malloc (ARCOMMANDS_GEN_BUFFER_SIZE * sizeof (uint8_t));\n')
        cfile.write ('    currBufferSize = ARCOMMANDS_GEN_BUFFER_SIZE;\n')
        cfile.write ('    if (NULL == buffer)\n')
        cfile.write ('        noError = 0;\n')
        cfile.write ('\n')
        cfile.write ('    // Write class header\n')
        cfile.write ('    if (1 == noError)\n')
        cfile.write ('    {\n')
        cfile.write ('        currIndexInBuffer = addU16ToBuffer (&buffer, COMMAND_CLASS_' + cl.upper() + ', currIndexInBuffer, &currBufferSize);\n')
        cfile.write ('        if (-1 == currIndexInBuffer)\n')
        cfile.write ('        {\n')
        cfile.write ('            noError = 0;\n')
        cfile.write ('        }\n')
        cfile.write ('    }\n')
        cfile.write ('    // Write id header\n')
        cfile.write ('    if (1 == noError)\n')
        cfile.write ('    {\n')
        cfile.write ('        currIndexInBuffer = addU16ToBuffer (&buffer, ' + cl.upper () + '_CMD_' + cmd.upper() + ', currIndexInBuffer, &currBufferSize);\n')
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

cfile.write ('void ARCommandsFree (uint8_t **cmdBuf)\n')
cfile.write ('{\n')
cfile.write ('    if (NULL != cmdBuf &&\n')
cfile.write ('        NULL != *cmdBuf)\n')
cfile.write ('    {\n')
cfile.write ('        free (*cmdBuf);\n')
cfile.write ('        *cmdBuf = NULL;\n')
cfile.write ('    }\n')
cfile.write ('}\n')

cfile.write ('// END GENERATED CODE\n')

cfile.close ()

#################################
# 5TH PART :                    #
#################################
# Generate public decoder H file#
#################################

hfile = open (COMMANDSDEC_HFILE, 'w')

hfile.write ('/**\n')
hfile.write (' * @file ' + COMMANDSDEC_HFILE_NAME + '\n')
hfile.write (' * @brief libARCommands decoder header.\n')
hfile.write (' * This file contains all declarations needed to decode commands\n')
hfile.write (' * @note Autogenerated file\n')
hfile.write (' **/\n')
hfile.write ('#ifndef '+COMMANDSDEC_DEFINE+'\n')
hfile.write ('#define '+COMMANDSDEC_DEFINE+'\n')
hfile.write ('#include <inttypes.h>\n')
hfile.write ('\n')
hfile.write ('\n')
hfile.write ('/**\n')
hfile.write (' * @brief Error codes for libARCommandsDecodeBuffer() function\n')
hfile.write (' **/\n')
hfile.write ('typedef enum {\n')
hfile.write ('    ARCOMMANDS_COMMANDSDEC_NOERROR = 0, ///< No error occured\n')
hfile.write ('    ARCOMMANDS_COMMANDSDEC_NOCALLBACK, ///< No error, but no callback was set (so the call had no effect)\n')
hfile.write ('    ARCOMMANDS_COMMANDSDEC_UNKNOWN, ///< The command buffer contained an unknown command\n')
hfile.write ('    ARCOMMANDS_COMMANDSDEC_NOTENOUGHDATA, ///< The command buffer did not contain enough data for the specified command\n')
hfile.write ('    ARCOMMANDS_COMMANDSDEC_ERROR, ///< Any other error\n')
hfile.write ('} eARCOMMANDS_COMMANDSDEC_ERRTYPE;\n')
hfile.write ('\n/**\n')
hfile.write (' * @brief Decode a comand buffer\n')
hfile.write (' * On success, the callback set for the command will be called in the current thread.\n')
hfile.write (' * @param buffer the command buffer to decode\n')
hfile.write (' * @param buffLen the length of the command buffer\n')
hfile.write (' * @return ARCOMMANDS_COMMANDSDEC_NOERROR on success, any error code otherwise\n')
hfile.write (' */\n')
hfile.write ('eARCOMMANDS_COMMANDSDEC_ERRTYPE\n')
hfile.write ('ARCommandsDecodeBuffer (uint8_t *buffer, int32_t buffLen);\n')
hfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    hfile.write ('// Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        commList = commandsCommentsByClass [clIndex][cmdIndex]
        commZero = commList [0]
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        ACList = argCommentsByClassAndCommand [clIndex][cmdIndex]
        hfile.write ('\n/**\n')
        hfile.write (' * @brief callback type for the command ' + cl + '.' + cmd + '\n')
        hfile.write (' */\n')
        hfile.write ('typedef void (*ARCommands' + cl.capitalize() + cmd.capitalize() + 'Callback) (')
        first = 1
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            if first == 1:
                first = 0
            else:
                hfile.write (', ')
            hfile.write (xmlToC(argT) + ' ' + argN)
        if 0 == first:
            hfile.write (', ')
        hfile.write ('void *custom);\n')
        hfile.write ('/**\n')
        hfile.write (' * @brief callback setter for the command ' + cl + '.' + cmd + '\n')
        hfile.write (' * @param callback new callback for the command ' + cl + '.' + cmd + '\n')
        hfile.write (' * @param custom pointer that will be passed to all calls to the callback\n')
        hfile.write (' */\n')
        hfile.write ('void ARCommandsSet' + cl.capitalize() + cmd.capitalize() + 'Callback (ARCommands' + cl.capitalize() + cmd.capitalize() + 'Callback callback, void *custom);\n')
    hfile.write ('\n')

hfile.write ('#endif /* '+COMMANDSDEC_DEFINE+' */\n')

hfile.close ()

#################################
# 6TH PART :                    #
#################################
# Generate decoder C part       #
#################################

cfile = open (COMMANDSDEC_CFILE, 'w')

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
cfile.write ('#include <'+COMMANDSDEC_HFILE_NAME+'>\n')
cfile.write ('#include <'+COMMANDSID_HFILE_NAME+'>\n')
cfile.write ('#include <libSAL/endianness.h>\n')
cfile.write ('#include <libSAL/mutex.h>\n')
cfile.write ('\n')
cfile.write ('// READ FROM BUFFER HELPERS\n')
cfile.write ('\n')
cfile.write ('// Read an 8 bit value from the buffer\n')
cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
cfile.write ('uint8_t read8FromBuffer (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    uint8_t retVal = 0;\n')
cfile.write ('    int newOffset = *offset + sizeof (uint8_t);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    retVal = buffer [*offset];\n')
cfile.write ('    *offset = newOffset;\n')
cfile.write ('    *error = 0;\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Read a 16 bit value from the buffer\n')
cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
cfile.write ('uint16_t read16FromBuffer (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    uint16_t retVal = 0;\n')
cfile.write ('    uint16_t *buffAddr = (uint16_t *)&buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (uint16_t);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    retVal = dtohs (*buffAddr);\n')
cfile.write ('    *offset = newOffset;\n')
cfile.write ('    *error = 0;\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Read a 32 bit value from the buffer\n')
cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
cfile.write ('uint32_t read32FromBuffer (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    uint32_t retVal = 0;\n')
cfile.write ('    uint32_t *buffAddr = (uint32_t *)&buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (uint32_t);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    retVal = dtohl (*buffAddr);\n')
cfile.write ('    *offset = newOffset;\n')
cfile.write ('    *error = 0;\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Read a 64 bit value from the buffer\n')
cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
cfile.write ('uint64_t read64FromBuffer (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    uint64_t retVal = 0;\n')
cfile.write ('    uint64_t *buffAddr = (uint64_t *)&buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (uint64_t);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    retVal = dtohll (*buffAddr);\n')
cfile.write ('    *offset = newOffset;\n')
cfile.write ('    *error = 0;\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Read a float value from the buffer\n')
cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
cfile.write ('float readFloatFromBuffer (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    float retVal = 0;\n')
cfile.write ('    float *buffAddr = (float *)&buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (float);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    retVal = dtohf (*buffAddr);\n')
cfile.write ('    *offset = newOffset;\n')
cfile.write ('    *error = 0;\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Read a double value from the buffer\n')
cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
cfile.write ('double readDoubleFromBuffer (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    double retVal = 0;\n')
cfile.write ('    double *buffAddr = (double *)&buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (double);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    retVal = dtohd (*buffAddr);\n')
cfile.write ('    *offset = newOffset;\n')
cfile.write ('    *error = 0;\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Read a string value from the buffer\n')
cfile.write ('// On error, return NULL and set *error to 1, else set *error to 0\n')
cfile.write ('char *readStringFromBuffer (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    char *retVal = NULL;\n')
cfile.write ('    char *buffAddr = (char *)&buffer[*offset];\n')
cfile.write ('    int newOffset = *offset;\n')
cfile.write ('    int str_len = 0;\n')
cfile.write ('    while (newOffset < capacity && \'\\0\' != (char) buffer [newOffset])\n')
cfile.write ('    {\n')
cfile.write ('        str_len++;\n');
cfile.write ('        newOffset += sizeof (char);\n');
cfile.write ('    }\n')
cfile.write ('    if (newOffset == capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n');
cfile.write ('        return retVal;\n');
cfile.write ('    }\n')
cfile.write ('    retVal = calloc (str_len + 1, sizeof (char));\n')
cfile.write ('    if (NULL == retVal)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    strcpy (retVal, buffAddr);\n')
cfile.write ('    *offset = newOffset;\n')
cfile.write ('    *error = 0;\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// CALLBACK VARIABLES + SETTERS\n')
cfile.write ('\n')
cfile.write ('static sal_mutex_t clbMutex;\n')
cfile.write ('static int isInit = 0;\n')
cfile.write ('int ARCommandsDecInit (void)\n')
cfile.write ('{\n')
cfile.write ('    if (0 == isInit && 0 == sal_mutex_init (&clbMutex))\n')
cfile.write ('    {\n')
cfile.write ('        isInit = 1;\n')
cfile.write ('    }\n')
cfile.write ('    return isInit;\n')
cfile.write ('}\n')
cfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cfile.write ('// Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        commList = commandsCommentsByClass [clIndex][cmdIndex]
        commZero = commList [0]
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        ACList = argCommentsByClassAndCommand [clIndex][cmdIndex]
        cfile.write ('static ARCommands' + cl.capitalize() + cmd.capitalize() + 'Callback ' + cl + cmd.capitalize() + 'Cb = NULL;\n')
        cfile.write ('static void *' + cl + cmd.capitalize() + 'Custom = NULL;\n')
        cfile.write ('void ARCommandsSet' + cl.capitalize() + cmd.capitalize() + 'Callback (ARCommands' + cl.capitalize() + cmd.capitalize() + 'Callback callback, void *custom)\n')
        cfile.write ('{\n')
        cfile.write ('    if (1 == ARCommandsDecInit ())\n')
        cfile.write ('    {\n')
        cfile.write ('        sal_mutex_lock (&clbMutex);\n')
        cfile.write ('        ' + cl + cmd.capitalize () + 'Cb = callback;\n')
        cfile.write ('        ' + cl + cmd.capitalize () + 'Custom = custom;\n')
        cfile.write ('        sal_mutex_unlock (&clbMutex);\n')
        cfile.write ('    }\n')
        cfile.write ('}\n')
    cfile.write ('\n')

cfile.write ('// DECODER ENTRY POINT\n')
cfile.write ('eARCOMMANDS_COMMANDSDEC_ERRTYPE ARCommandsDecodeBuffer (uint8_t *buffer, int32_t buffLen)\n')
cfile.write ('{\n')
cfile.write ('    eARCOMMANDS_COMMAND_CLASS _class = COMMAND_CLASS_MAX;\n')
cfile.write ('    int _id = -1;\n')
cfile.write ('    int32_t _error = 0;\n')
cfile.write ('    int32_t _offset = 0;\n')
cfile.write ('    eARCOMMANDS_COMMANDSDEC_ERRTYPE retVal = ARCOMMANDS_COMMANDSDEC_NOERROR;\n')
cfile.write ('    if (NULL == buffer)\n')
cfile.write ('    {\n')
cfile.write ('        retVal = ARCOMMANDS_COMMANDSDEC_ERROR;\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (ARCOMMANDS_COMMANDSDEC_NOERROR == retVal)\n')
cfile.write ('    {\n')
cfile.write ('        if (0 == ARCommandsDecInit ())\n')
cfile.write ('        {\n')
cfile.write ('            retVal = ARCOMMANDS_COMMANDSDEC_ERROR;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (ARCOMMANDS_COMMANDSDEC_NOERROR == retVal)\n')
cfile.write ('    {\n')
cfile.write ('        _class = read16FromBuffer (buffer, buffLen, &_offset, &_error);\n')
cfile.write ('        if (1 == _error)\n')
cfile.write ('            retVal = ARCOMMANDS_COMMANDSDEC_NOTENOUGHDATA;\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (ARCOMMANDS_COMMANDSDEC_NOERROR == retVal)\n')
cfile.write ('    {\n')
cfile.write ('        _id = read16FromBuffer (buffer, buffLen, &_offset, &_error);\n')
cfile.write ('        if (1 == _error)\n')
cfile.write ('            retVal = ARCOMMANDS_COMMANDSDEC_NOTENOUGHDATA;\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (ARCOMMANDS_COMMANDSDEC_NOERROR == retVal)\n')
cfile.write ('    {\n')
cfile.write ('        switch (_class)\n')
cfile.write ('        {\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cmdList = commandsNameByClass [clIndex]
    cfile.write ('        case COMMAND_CLASS_' + cl.upper () + ':\n')
    cfile.write ('        {\n')
    cfile.write ('            switch (_id)\n')
    cfile.write ('            {\n')
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        cfile.write ('            case ' + cl.upper () + '_CMD_' + cmd.upper () + ':\n')
        cfile.write ('            {\n')
        CBNAME=cl+cmd.capitalize()+'Cb'
        LOCCB='_'+CBNAME
        CBTYPE='ARCommands' + cl.capitalize () + cmd.capitalize () + 'Callback'
        cfile.write ('                sal_mutex_lock (&clbMutex);\n')
        cfile.write ('                ' + CBTYPE + ' ' + LOCCB + ' = ' + CBNAME + ';\n')
        cfile.write ('                void *cbCustom = '+cl+cmd.capitalize()+'Custom;\n')
        cfile.write ('                sal_mutex_unlock (&clbMutex);\n')
        cfile.write ('                if (NULL != ' + LOCCB + ')\n')
        cfile.write ('                {\n')
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            if 'string' == argT:
                cfile.write ('                    ' + xmlToC (argT) + ' ' + argN + ' = NULL;\n')
            else:
                cfile.write ('                    ' + xmlToC (argT) + ' ' + argN + ';\n')
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            cfile.write ('                    if (ARCOMMANDS_COMMANDSDEC_NOERROR == retVal)\n')
            cfile.write ('                    {\n')
            cfile.write ('                        ' + argN + ' = ' + xmlToReader(argT) + '(buffer, buffLen, &_offset, &_error);\n')
            cfile.write ('                        if (1 == _error)\n')
            cfile.write ('                            retVal = ARCOMMANDS_COMMANDSDEC_NOTENOUGHDATA;\n')
            cfile.write ('                    }\n')
        cfile.write ('                    if (ARCOMMANDS_COMMANDSDEC_NOERROR == retVal)\n')
        cfile.write ('                    {\n')
        cfile.write ('                        ' + LOCCB + ' (')
        first = 1
        for argN in ANList:
            if 1 == first:
                first = 0
            else:
                cfile.write (', ')
            cfile.write (argN)
        if 0 == first:
            cfile.write (', ')
        cfile.write ('cbCustom);\n')
        cfile.write ('                    }\n')
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            if 'string' == argT:
                cfile.write ('                    if (NULL != ' + argN + ') { free (' + argN + '); }\n')
        cfile.write ('                }\n')
        cfile.write ('                else\n')
        cfile.write ('                {\n')
        cfile.write ('                    retVal = ARCOMMANDS_COMMANDSDEC_NOCALLBACK;\n')
        cfile.write ('                }\n')
        cfile.write ('            }\n')
        cfile.write ('            break; /* ' + cl.upper () + '_CMD_' + cmd.upper () + ' */\n')
    cfile.write ('            default:\n')
    cfile.write ('                retVal = ARCOMMANDS_COMMANDSDEC_UNKNOWN;\n')
    cfile.write ('                break;\n')
    cfile.write ('            }\n')
    cfile.write ('        }\n')
    cfile.write ('        break; /* COMMAND_CLASS_' + cl.upper () + ' */\n')
cfile.write ('        default:\n')
cfile.write ('            retVal = ARCOMMANDS_COMMANDSDEC_UNKNOWN;\n')
cfile.write ('            break;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')


cfile.write ('// END GENERATED CODE\n')

cfile.close ()

#################################
# 7TH PART :                    #
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
cfile.write ('#include <'+COMMANDSDEC_HFILE_NAME+'>\n')
cfile.write ('#include <libSAL/print.h>\n')
cfile.write ('#include <stdlib.h>\n')
cfile.write ('#include <string.h>\n')
cfile.write ('\n')
cfile.write ('int errcount;\n')
cfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cIndex = cmdList.index (cmd)
        ANList = argNamesByClassAndCommand [clIndex][cIndex]
        ATList = argTypesByClassAndCommand [clIndex][cIndex]
        cfile.write ('void ' + cl + cmd.capitalize () + 'Cb (')
        first = 1
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            if 1 == first:
                first = 0
            else:
                cfile.write (', ')
            cfile.write (xmlToC (argT) + ' ' + argN)
        if 0 == first:
            cfile.write (', ')
        cfile.write ('void *custom)\n')
        cfile.write ('{\n')
        cfile.write ('    SAL_PRINT (PRINT_WARNING, "'+TB_TAG+'", "Callback for command ' + cl + '.' + cmd + ' --> Custom PTR = %p\\n", custom);\n')
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            cfile.write ('    SAL_PRINT (PRINT_WARNING, "'+TB_TAG+'", "' + argN + ' value : <' + xmlToPrintf(argT) + '>\\n", ' + argN + ');\n')
            if "string" == argT:
                cfile.write ('    if (0 != strcmp (' + xmlToSample (argT) + ', ' + argN + '))\n')
            else:
                cfile.write ('    if (' + xmlToSample (argT) + ' != ' + argN + ')\n')
            cfile.write ('    {\n')
            if "string" == argT:
                cfile.write ('        SAL_PRINT (PRINT_ERROR, "'+TB_TAG+'", "BAD ARG VALUE !!! --> Expected <%s>\\n", ' + xmlToSample(argT) + ');\n')
            else:
                cfile.write ('        SAL_PRINT (PRINT_ERROR, "'+TB_TAG+'", "BAD ARG VALUE !!! --> Expected <' + xmlToSample(argT) + '>\\n");\n')
            cfile.write ('        errcount++;\n')
            cfile.write ('    }\n')
        cfile.write ('}\n');

cfile.write ('\n')
cfile.write ('void initCb (void)\n')
cfile.write ('{\n')
cfile.write ('    int cbCustom = 0;\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cfile.write ('    ARCommandsSet' + cl.capitalize () + cmd.capitalize () + 'Callback ((ARCommands' + cl.capitalize () + cmd.capitalize () + 'Callback) ' + cl + cmd.capitalize () + 'Cb, (void *)cbCustom++);\n')
cfile.write ('}\n')

cfile.write ('\n')
cfile.write ('int autoTest ()\n')
cfile.write ('{\n')
cfile.write ('    uint8_t *res = NULL;\n')
cfile.write ('    int32_t resSize = 0;\n')
cfile.write ('    errcount = 0;\n')
cfile.write ('    initCb ();\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cfile.write ('    // Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        cfile.write ('    res = ARCommandsGenerate' + cl.capitalize() + cmd.capitalize() + ' (&resSize')
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            cfile.write (', ' + xmlToSample(argT))
        cfile.write (');\n')
        cfile.write ('    if (NULL == res)\n')
        cfile.write ('    {\n')
        cfile.write ('        SAL_PRINT (PRINT_ERROR, "'+TB_TAG+'", "Error while generating command ' + cl.capitalize() + '.' + cmd.capitalize() + '\\n\\n");\n')
        cfile.write ('        errcount++;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        SAL_PRINT (PRINT_DEBUG, "'+TB_TAG+'", "Generating command ' + cl.capitalize() + '.' + cmd.capitalize() + ' succeded\\n");\n')
        cfile.write ('        eARCOMMANDS_COMMANDSDEC_ERRTYPE err;\n')
        cfile.write ('        err = ARCommandsDecodeBuffer (res, resSize);\n')
        cfile.write ('        SAL_PRINT (PRINT_WARNING, "'+TB_TAG+'", "Decode return value : %d\\n\\n", err);\n')
        cfile.write ('        ARCommandsFree (&res);\n')
        cfile.write ('    }\n')
        cfile.write ('\n')
    cfile.write ('\n')

cfile.write ('    if (0 == errcount)\n')
cfile.write ('    {\n')
cfile.write ('        SAL_PRINT (PRINT_WARNING, "'+TB_TAG+'", "No errors !\\n");\n')
cfile.write ('    }\n')
cfile.write ('    else\n')
cfile.write ('    {\n')
cfile.write ('        SAL_PRINT (PRINT_ERROR, "'+TB_TAG+'", "%d errors detected during autoTest\\n", errcount);\n')
cfile.write ('    }\n')
cfile.write ('    return errcount;\n')
cfile.write ('}\n')

cfile.close ()

hfile = open (TB_HFILE, 'w')

hfile.write ('/********************************************\n')
hfile.write (' *            AUTOGENERATED FILE            *\n')
hfile.write (' *             DO NOT MODIFY IT             *\n')
hfile.write (' *                                          *\n')
hfile.write (' * To add new commands :                    *\n')
hfile.write (' *  - Modify ../Xml/commands.xml file       *\n')
hfile.write (' *  - Re-run generateCommandsList.py script *\n')
hfile.write (' *                                          *\n')
hfile.write (' ********************************************/\n')
hfile.write ('#ifndef '+TB_DEFINE+'\n')
hfile.write ('#define '+TB_DEFINE+' (1)\n')
hfile.write ('\n')
hfile.write ('int autoTest ();\n')
hfile.write ('\n')
hfile.write ('#endif /* '+TB_DEFINE+' */\n')

hfile.close ()

cfile = open (TB_LIN_CFILE, 'w')

cfile.write ('/********************************************\n')
cfile.write (' *            AUTOGENERATED FILE            *\n')
cfile.write (' *             DO NOT MODIFY IT             *\n')
cfile.write (' *                                          *\n')
cfile.write (' * To add new commands :                    *\n')
cfile.write (' *  - Modify ../Xml/commands.xml file       *\n')
cfile.write (' *  - Re-run generateCommandsList.py script *\n')
cfile.write (' *                                          *\n')
cfile.write (' ********************************************/\n')
cfile.write ('#include "../'+COM_TB_DIR+TB_HFILE_NAME+'"\n')
cfile.write ('\n')
cfile.write ('int main (int argc, char *argv[])\n')
cfile.write ('{\n')
cfile.write ('    return autoTest ();\n')
cfile.write ('}\n')

cfile.close ()

#################################
# 8TH PART :                    #
#################################
# Generate JNI C/Java code      #
#################################

JNIClassName, _ = os.path.splitext (JNI_JFILE_NAME)

jfile = open (JNI_JFILE, 'w')

jfile.write ('package '+JNI_PACKAGE_NAME+';\n')
jfile.write ('\n');
jfile.write ('public class '+JNIClassName+' {\n')
jfile.write ('    private long pdata;\n')
jfile.write ('    private int dataSize;\n')
jfile.write ('    private boolean valid;\n')
jfile.write ('    private boolean createdFromArray;\n')
jfile.write ('\n')
jfile.write ('    public ARCommand () {\n')
jfile.write ('        pdata = 0;\n')
jfile.write ('        dataSize = 0;\n')
jfile.write ('        valid = false;\n')
jfile.write ('        createdFromArray = false;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    public ARCommand (byte [] oldData) {\n')
jfile.write ('        pdata = 0;\n')
jfile.write ('        dataSize = 0;\n')
jfile.write ('        valid = copyFromArray (oldData);\n')
jfile.write ('        createdFromArray = true;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    public void close () {\n')
jfile.write ('        if (0 != pdata) {\n')
jfile.write ('            freeData (pdata, createdFromArray);\n')
jfile.write ('            pdata = 0;\n')
jfile.write ('            dataSize = 0;\n')
jfile.write ('            valid = false;\n')
jfile.write ('        }\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    public void finalize () {\n')
jfile.write ('        close ();\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    public byte [] getData () {\n')
jfile.write ('        if (false == valid) {\n')
jfile.write ('            return null;\n')
jfile.write ('        }\n')
jfile.write ('        return nativeGetData (pdata, dataSize);\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    public boolean decode () {\n')
jfile.write ('        return nativeDecode (pdata, dataSize);\n')
jfile.write ('    }\n')
jfile.write ('\n')
for cl in allClassesNames:
    cIndex = allClassesNames.index(cl)
    cmdList = commandsNameByClass[cIndex]
    for cmd in cmdList:
        cmIndex = cmdList.index(cmd)
        ANList = argNamesByClassAndCommand [cIndex][cmIndex]
        ATList = argTypesByClassAndCommand [cIndex][cmIndex]
        jfile.write ('    public boolean set'+cl.capitalize ()+cmd.capitalize ()+' (')
        first = 1
        for argN in ANList:
            aIndex = ANList.index(argN)
            argT = ATList [aIndex]
            jargT = xmlToJava (argT)
            if 1 == first:
                first = 0
            else:
                jfile.write (', ')
            jfile.write (jargT+' '+argN)
        jfile.write (') {\n')
        jfile.write ('        close ();\n')
        jfile.write ('        valid = nativeSet'+cl.capitalize ()+cmd.capitalize ()+' (')
        first = 1
        for argN in ANList:
            if 1 == first:
                first = 0
            else:
                jfile.write (', ')
            jfile.write (argN)
        jfile.write (');\n')
        jfile.write ('        return valid;\n')
        jfile.write ('    }\n')
        jfile.write ('\n')
jfile.write ('\n')
jfile.write ('    private native boolean nativeDecode (long jpdata, int jdataSize);\n')
jfile.write ('    private native boolean copyFromArray (byte [] oldData);\n')
jfile.write ('    private native void freeData (long dataToFree, boolean wasCreatedFromArray);\n')
jfile.write ('    private native byte [] nativeGetData (long jpdata, int jdataSize);\n')
jfile.write ('\n')
for cl in allClassesNames:
    cIndex = allClassesNames.index(cl)
    cmdList = commandsNameByClass[cIndex]
    for cmd in cmdList:
        jfile.write ('    private native boolean nativeSet'+cl.capitalize ()+cmd.capitalize ()+' (')
        cmIndex = cmdList.index(cmd)
        ANList = argNamesByClassAndCommand [cIndex][cmIndex]
        ATList = argTypesByClassAndCommand [cIndex][cmIndex]
        first = 1
        for argN in ANList:
            aIndex = ANList.index(argN)
            argT = ATList [aIndex]
            jargT = xmlToJava (argT)
            if 1 == first:
                first = 0
            else:
                jfile.write (', ')
            jfile.write (jargT+' '+argN)
        jfile.write (');\n')
jfile.write ('}\n')

jfile.close ()

cfile = open (JNI_CFILE, 'w')

JNI_FUNC_PREFIX='Java_'+JNI_PACKAGE_NAME.replace ('.', '_')+'_'
JNI_FIRST_ARGS='JNIEnv *env, jobject thizz'

cfile.write ('/********************************************\n')
cfile.write (' *            AUTOGENERATED FILE            *\n')
cfile.write (' *             DO NOT MODIFY IT             *\n')
cfile.write (' *                                          *\n')
cfile.write (' * To add new commands :                    *\n')
cfile.write (' *  - Modify ../../Xml/commands.xml file    *\n')
cfile.write (' *  - Re-run generateCommandsList.py script *\n')
cfile.write (' *                                          *\n')
cfile.write (' ********************************************/\n')
cfile.write ('#include <'+COMMANDSGEN_HFILE_NAME+'>\n')
cfile.write ('#include <'+COMMANDSDEC_HFILE_NAME+'>\n')
cfile.write ('#include <jni.h>\n')
cfile.write ('#include <stdlib.h>\n')
cfile.write ('\n')
cfile.write ('static jfieldID j_pdata_id = 0;\n')
cfile.write ('static jfieldID j_dataSize_id = 0;\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT jboolean JNICALL\n')
cfile.write (JNI_FUNC_PREFIX+JNIClassName+'_copyFromArray ('+JNI_FIRST_ARGS+', jbyteArray oldData)\n')
cfile.write ('{\n')
cfile.write ('    jboolean valid = JNI_TRUE;\n')
cfile.write ('    jint len = (*env)->GetArrayLength (env, oldData);\n')
cfile.write ('    uint8_t *c_pdata = malloc (len * sizeof (uint8_t));\n')
cfile.write ('    jbyte *j_pdata = NULL;\n')
cfile.write ('    if (NULL == c_pdata)\n')
cfile.write ('    {\n')
cfile.write ('        valid = JNI_FALSE;\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (JNI_TRUE == valid)\n')
cfile.write ('    {\n')
cfile.write ('        j_pdata = (*env)->GetByteArrayElements (env, oldData, NULL);\n')
cfile.write ('        if (NULL == j_pdata)\n')
cfile.write ('        {\n')
cfile.write ('            valid = JNI_FALSE;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (JNI_TRUE == valid)\n')
cfile.write ('    {\n')
cfile.write ('        memcpy (c_pdata, j_pdata, len);\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (JNI_FALSE == valid &&\n')
cfile.write ('        NULL != c_pdata)\n')
cfile.write ('    {\n')
cfile.write ('        free (c_pdata);\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (NULL != j_pdata)\n')
cfile.write ('    {\n')
cfile.write ('        (*env)->ReleaseByteArrayElements (env, oldData, j_pdata, JNI_ABORT); // Use JNI_ABORT because we NEVER want to modify oldData\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    return valid;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT void JNICALL\n')
cfile.write (JNI_FUNC_PREFIX+JNIClassName+'_freeData ('+JNI_FIRST_ARGS+', jlong dataToFree, jboolean jcreatedFromArray)\n')
cfile.write ('{\n')
cfile.write ('    uint8_t *c_pdata = (uint8_t *)(intptr_t)dataToFree;\n')
cfile.write ('    if (JNI_FALSE == jcreatedFromArray)\n')
cfile.write ('    {\n')
cfile.write ('        ARCommandsFree (&c_pdata);\n')
cfile.write ('    }\n')
cfile.write ('    else\n')
cfile.write ('    {\n')
cfile.write ('        free (c_pdata);\n')
cfile.write ('    }\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT jbyteArray JNICALL\n')
cfile.write (JNI_FUNC_PREFIX+JNIClassName+'_nativeGetData ('+JNI_FIRST_ARGS+', jlong jpdata, jint jdataSize)\n')
cfile.write ('{\n')
cfile.write ('    jbyteArray retArray = (*env)->NewByteArray (env, jdataSize);\n')
cfile.write ('    if (NULL != retArray)\n')
cfile.write ('    {\n')
cfile.write ('        (*env)->SetByteArrayRegion(env, retArray, 0, jdataSize, (jbyte *)(intptr_t)jpdata);\n')
cfile.write ('    }\n')
cfile.write ('    return retArray;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT jboolean JNICALL\n')
cfile.write (JNI_FUNC_PREFIX+JNIClassName+'_nativeDecode ('+JNI_FIRST_ARGS+', jlong jpdata, int jdataSize)\n')
cfile.write ('{\n')
cfile.write ('    // TODO !!!\n')
cfile.write ('    return JNI_TRUE;\n')
cfile.write ('}\n')
cfile.write ('\n')
for cl in allClassesNames:
    cIndex = allClassesNames.index (cl)
    cmdList = commandsNameByClass[cIndex]
    for cmd in cmdList:
        cmIndex = cmdList.index (cmd)
        ANList = argNamesByClassAndCommand [cIndex][cmIndex]
        ATList = argTypesByClassAndCommand [cIndex][cmIndex]
        cfile.write ('JNIEXPORT jboolean JNICALL\n')
        cfile.write (JNI_FUNC_PREFIX+JNIClassName+'_nativeSet'+cl.capitalize ()+cmd.capitalize ()+' ('+JNI_FIRST_ARGS)
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            jargT = xmlToJni (argT)
            cfile.write (', '+jargT+' '+argN)
        cfile.write (')\n')
        cfile.write ('{\n')
        cfile.write ('    jboolean retVal = JNI_TRUE;\n')
        cfile.write ('    int32_t c_dataSize = 0;\n')
        cfile.write ('    if (0 == j_pdata_id && 0 == j_dataSize_id)\n')
        cfile.write ('    {\n')
        cfile.write ('        jclass clz = (*env)->GetObjectClass (env, thizz);\n')
        cfile.write ('        if (0 != clz)\n')
        cfile.write ('        {\n')
        cfile.write ('            j_pdata_id = (*env)->GetFieldID (env, clz, "pdata", "J");\n')
        cfile.write ('            j_dataSize_id = (*env)->GetFieldID (env, clz, "dataSize", "I");\n')
        cfile.write ('            (*env)->DeleteLocalRef (env, clz);\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            return JNI_FALSE;\n')
        cfile.write ('        }\n')
        cfile.write ('    }\n')
        cfile.write ('\n')
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            if 'string' == argT:
                cfile.write ('    const char *c_'+argN+' = (*env)->GetStringUTFChars (env, '+argN+', NULL);\n')
        cfile.write ('    uint8_t *c_pdata = ARCommandsGenerate'+cl.capitalize ()+cmd.capitalize ()+' (&c_dataSize')
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            cargT = xmlToC (argT)
            if 'string' == argT:
                cfile.write (', c_'+argN)
            else:
                cfile.write (', ('+cargT+')'+argN)
        cfile.write (');\n')
        for argN in ANList:
            aIndex = ANList.index (argN)
            argT = ATList [aIndex]
            if 'string' == argT:
                cfile.write ('    (*env)->ReleaseStringUTFChars (env, '+argN+', c_'+argN+');\n')
        cfile.write ('    if (NULL == c_pdata)\n')
        cfile.write ('    {\n')
        cfile.write ('        retVal = JNI_FALSE;\n')
        cfile.write ('    }\n')
        cfile.write ('    (*env)->SetLongField (env, thizz, j_pdata_id, (jlong)(intptr_t)c_pdata);\n')
        cfile.write ('    (*env)->SetIntField (env, thizz, j_dataSize_id, (jint)c_dataSize);\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')

cfile.write ('/* END OF GENERATED CODE */\n')

cfile.close ()
