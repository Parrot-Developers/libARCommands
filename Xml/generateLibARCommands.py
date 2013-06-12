from xml.dom.minidom import parseString
import sys
import os
import re

MYDIR=os.path.dirname(sys.argv[0])
if '' == MYDIR:
    MYDIR='.'

#################################
# Get info from configure.ac    #
# file                          #
#################################

def ARPrint (msg, noNewLine=0):
    sys.stdout.write (msg)
    if 0 == noNewLine:
        sys.stdout.write ('\n')
    else:
        sys.stdout.write (' ')

configureAcFile = open (MYDIR + '/../Build/configure.ac', 'rb')
AC_INIT_LINE=configureAcFile.readline ()
while (not AC_INIT_LINE.startswith ('AC_INIT')) and ('' != AC_INIT_LINE):
    AC_INIT_LINE=configureAcFile.readline ()
if '' == AC_INIT_LINE:
    ARPrint ('Unable to read from configure.ac file !')
    sys.exit (1)

AC_ARGS=re.findall(r'\[[^]]*\]', AC_INIT_LINE)
LIB_NAME=AC_ARGS[0].replace ('[', '').replace (']', '')
LIB_MODULE=LIB_NAME.replace ('lib', '')
LIB_VERSION=AC_ARGS[1].replace ('[', '').replace (']', '')

#################################
# CONFIGURATION :               #
#################################
# Setup XML and C/HFiles Names  #
# Public header names must be   #
# LIB_NAME + '/fileName.h'      #
#################################

SDK_PACKAGE_ROOT='com.parrot.arsdk.'
JNI_PACKAGE_NAME=SDK_PACKAGE_ROOT + LIB_MODULE.lower ()

# Default project name
DEFAULTPROJECTNAME='mykonos3'

#Name (and path) of the xml file
XMLFILENAME_PREFIX=MYDIR + '/../Xml/'
XMLFILENAME_SUFFIX='_commands.xml'

#Name of the output private header containing id enums
COMMANDSID_HFILE_NAME='ARCOMMANDS_Ids.h'

#Name of the output public header containing encoder helpers
COMMANDSGEN_HFILE_NAME=LIB_NAME + '/ARCOMMANDS_Generator.h'

#Name of the output public header containing decoder helpers
COMMANDSDEC_HFILE_NAME=LIB_NAME + '/ARCOMMANDS_Decoder.h'

#Name of the output C file containing encoder helpers
COMMANDSGEN_CFILE_NAME='ARCOMMANDS_Generator.c'

#Name of the output C file containing decoder helpers
COMMANDSDEC_CFILE_NAME='ARCOMMANDS_Decoder.c'

#Name of the output C/H common testbench file
TB_CFILE_NAME='autoTest.c'
TB_HFILE_NAME='autoTest.h'
#Tag for tb ARSAL_PRINT calls
TB_TAG='AutoTest'

#Name of the linux entry point file for autotest
TB_LIN_CFILE_NAME='autoTest_linux.c'

#Name of the JNI C File
JNI_CFILE_NAME='ARCOMMANDS_JNI.c'

#Name of the JNI JAVA File
JNI_JFILE_NAME='ARCommand.java'
JNIClassName, _ = os.path.splitext (JNI_JFILE_NAME)

#Name of the JNI JAVA Interfaces files (DO NOT MODIFY)
JAVA_INTERFACES_FILES_NAME=JNIClassName + '*Listener.java'

#Name of the JNI-Android Makefile (DO NOT MODIFY)
JNI_MAKEFILE_NAME='Android.mk'

#Relative path of SOURCE dir
SRC_DIR=MYDIR + '/../Sources/'

#Relative path of INCLUDES dir
INC_DIR=MYDIR + '/../Includes/'

#Relative path of TESTBENCH dir
TB__DIR=MYDIR + '/../TestBench/'

#Relative path of unix-like (Linux / os-x) TESTBENCH dir
LIN_TB_DIR=TB__DIR + 'linux/'

#Relative path of multiplatform code for testbenches
COM_TB_DIR=TB__DIR + 'common/'

#Relative path of JNI dir
JNI_DIR="../JNI/"

#Relative path of JNI/C dir
JNIC_DIR=JNI_DIR + 'c/'

#Relative path of JNI/Java dir
JNIJ_DIR=JNI_DIR + 'java/'

##### END OF CONFIG #####

# Create array of generated files (so we can cleanup only our files)
GENERATED_FILES = []
COMMANDSID_HFILE=SRC_DIR + COMMANDSID_HFILE_NAME
GENERATED_FILES.append (COMMANDSID_HFILE)
COMMANDSGEN_HFILE=INC_DIR + COMMANDSGEN_HFILE_NAME
GENERATED_FILES.append (COMMANDSGEN_HFILE)
COMMANDSGEN_CFILE=SRC_DIR + COMMANDSGEN_CFILE_NAME
GENERATED_FILES.append (COMMANDSGEN_CFILE)
COMMANDSDEC_HFILE=INC_DIR + COMMANDSDEC_HFILE_NAME
GENERATED_FILES.append (COMMANDSDEC_HFILE)
COMMANDSDEC_CFILE=SRC_DIR + COMMANDSDEC_CFILE_NAME
GENERATED_FILES.append (COMMANDSDEC_CFILE)
TB_CFILE=COM_TB_DIR + TB_CFILE_NAME
GENERATED_FILES.append (TB_CFILE)
TB_HFILE=COM_TB_DIR + TB_HFILE_NAME
GENERATED_FILES.append (TB_HFILE)
TB_LIN_CFILE=LIN_TB_DIR + TB_LIN_CFILE_NAME
GENERATED_FILES.append (TB_LIN_CFILE)
JNI_CFILE=JNIC_DIR + JNI_CFILE_NAME
GENERATED_FILES.append (JNI_CFILE)
JNI_JFILE=JNIJ_DIR + JNI_JFILE_NAME
GENERATED_FILES.append (JNI_JFILE)
JNI_MAKEFILE=JNIC_DIR + JNI_MAKEFILE_NAME
GENERATED_FILES.append (JNI_MAKEFILE)
JAVA_INTERFACES_FILES=JNIJ_DIR + JAVA_INTERFACES_FILES_NAME

# Create names for #ifndef _XXX_ statements in .h files
COMMANDSID_DEFINE='_' + COMMANDSID_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSDEC_DEFINE='_' + COMMANDSDEC_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSGEN_DEFINE='_' + COMMANDSGEN_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
TB_DEFINE='_' + TB_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'

# Internal function to ensure that the first letter of a word is capitalized
# But keeps the capitalization of the rest of the word
def ARCapitalize (arstr):
    return arstr[0].upper () + arstr[1:]

# Submodules names
ID_SUBMODULE='ID'
GEN_SUBMODULE='Generator'
DEC_SUBMODULE='Decoder'
TB_SUBMODULE='Testbench'
JNI_SUBMODULE='JNI'

#############################
# NAME GENERATORS           #
#############################
def ARMacroName (Submodule, Name):
    # MODULE_SUBMODULE_NAME
    return LIB_MODULE.upper () + '_' + Submodule.upper () + '_' + Name.upper ()

def ARFunctionName (Submodule, Name):
    # MODULE_Submodule_Name
    return LIB_MODULE.upper () + '_' + ARCapitalize (Submodule) + '_' + ARCapitalize (Name)

def ARTypeName (Submodule, Name=''):
    # MODULE_Submodule[_Name]_t
    if '' != Name:
        return LIB_MODULE.upper () + '_' + ARCapitalize (Submodule) + '_' + ARCapitalize (Name) + '_t'
    else:
        return LIB_MODULE.upper () + '_' + ARCapitalize (Submodule) + '_t'

def ARGlobalName (Submodule, Name):
    # MODULE_Submodule_Name
    return LIB_MODULE.upper () + '_' + ARCapitalize (Submodule) + '_' + ARCapitalize (Name)

def ARGlobalConstName (Submodule, Name):
    # cMODULE_Submodule_Name
    return 'c' + LIB_MODULE.upper () + '_' + ARCapitalize (Submodule) + '_' + ARCapitalize (Name)

def AREnumValue (Submodule, Enum, Name):
    # MODULE_SUBMODULE_ENUM_NAME
    return LIB_MODULE.upper () + '_' + Submodule.upper () + '_' + Enum.upper () + '_' + Name.upper ()

def AREnumName (Submodule, Enum):
    # eMODULE_SUBMODULE_ENUM
    return 'e' + LIB_MODULE.upper () + '_' + Submodule.upper () + '_' + Enum.upper ()

def ARJavaEnumType (Submodule, Enum):
    # MODULE_SUBMODULE_ENUM_"ENUM"
    return LIB_MODULE.upper () + '_' + Submodule.upper () + '_' + Enum.upper () + '_ENUM'

def ARJavaEnumValue (Submodule, Enum, Name):
    # MODULE_SUBMODULE_ENUM_"ENUM".MODULE_SUBMODULE_ENUM_NAME
    return ARJavaEnumType (Submodule, Enum) + '.' + AREnumValue (Submodule, Enum, Name)
    

#Type conversion from XML Defined types to many other types
# XML Defined types
XMLTYPES = ['u8',       'i8',
            'u16',      'i16',
            'u32',      'i32',
            'u64',      'i64',
            'float',    'double',
            'string']
# Equivalent C types
CTYPES   = ['uint8_t',  'int8_t',
            'uint16_t', 'int16_t',
            'uint32_t', 'int32_t',
            'uint64_t', 'int64_t',
            'float',    'double',
            'char *']
# Equivalent C types with const char *
CTYPES_WC = ['uint8_t',  'int8_t',
            'uint16_t', 'int16_t',
            'uint32_t', 'int32_t',
            'uint64_t', 'int64_t',
            'float',    'double',
            'const char *']
# Equivalent size for the Generator internal functions
SZETYPES = ['U8',       'U8',
            'U16',      'U16',
            'U32',      'U32',
            'U64',      'U64',
            'Float',    'Double',
            'String']
# Equivalent calls for the Decoder internal functions
CREADERS = [ARFunctionName ('Decoder', 'Read8FromBuffer'),     ' (int8_t)' + ARFunctionName ('Decoder', 'Read8FromBuffer'),
            ARFunctionName ('Decoder', 'Read16FromBuffer'),    ' (int16_t)' + ARFunctionName ('Decoder', 'Read16FromBuffer'),
            ARFunctionName ('Decoder', 'Read32FromBuffer'),    ' (int32_t)' + ARFunctionName ('Decoder', 'Read32FromBuffer'),
            ARFunctionName ('Decoder', 'Read64FromBuffer'),    ' (int64_t)' + ARFunctionName ('Decoder', 'Read64FromBuffer'),
            ARFunctionName ('Decoder', 'ReadFloatFromBuffer'), ARFunctionName ('Decoder', 'ReadDoubleFromBuffer'),
            ARFunctionName ('Decoder', 'ReadStringFromBuffer')]
# Equivalent JAVA Types
# No unsigned types in java, so use signed types everywhere
JAVATYPES = ['byte',    'byte',
             'short',   'short',
             'int',     'int',
             'long',    'long',
             'float',   'double',
             'String']
# Equivalent JNI Signatures
JAVASIG   = ['B',        'B',
             'S',        'S',
             'I',        'I',
             'J',        'J',
             'F',        'D',
             'Ljava/lang/String;']
# Equivalent JNI types
JNITYPES  = ['jbyte',    'jbyte',
             'jshort',   'jshort',
             'jint',     'jint',
             'jlong',    'jlong',
             'jfloat',   'jdouble',
             'jstring']

def xmlToC (typ):
    xmlIndex = XMLTYPES.index (typ)
    return CTYPES [xmlIndex]

def xmlToCwithConst (typ):
    xmlIndex = XMLTYPES.index (typ)
    return CTYPES_WC [xmlIndex]

def xmlToSize (typ):
    xmlIndex = XMLTYPES.index (typ)
    return SZETYPES [xmlIndex]

def xmlToReader (typ):
    xmlIndex = XMLTYPES.index (typ)
    return CREADERS [xmlIndex]

def xmlToJava (typ):
    xmlIndex = XMLTYPES.index (typ)
    return JAVATYPES [xmlIndex]

def xmlToJavaSig (typ):
    xmlIndex = XMLTYPES.index (typ)
    return JAVASIG [xmlIndex]

def xmlToJni (typ):
    xmlIndex = XMLTYPES.index (typ)
    return JNITYPES [xmlIndex]

# Sample args for testbench
SAMPLEARGS = ['42',              '-42',
              '4200',            '-4200',
              '420000',          '-420000',
              '420102030405ULL', '-420102030405LL',
              '42.125',          '-42.000001',
              '"Test string with spaces"']
# Formatter for printf
PRINTFF    = ['%u',   '%d',
              '%u',   '%d',
              '%u',   '%d',
              '%llu', '%lld',
              '%f',   '%f',
              '%s']

def xmlToSample (typ):
    xmlIndex = XMLTYPES.index (typ)
    return SAMPLEARGS [xmlIndex]

def xmlToPrintf (typ):
    xmlIndex = XMLTYPES.index (typ)
    return PRINTFF [xmlIndex]

noGen = "no"
projectName = DEFAULTPROJECTNAME
args = sys.argv
args.pop (0)
while len(args) > 0:
    a = args.pop (0)
    #################################
    # If "-fname" is passed as an   #
    # argument, just output the     #
    # name of the generated files   #
    #################################
    if a == "-fname":
        for fil in GENERATED_FILES:
            ARPrint (fil, 1)
        ARPrint (JAVA_INTERFACES_FILES, 1)
        ARPrint ('')
        sys.exit (0)
    #################################
    # If "-dname" is passed as an   #
    # argument, just output the     #
    # name of the generated dirs    #
    #################################
    elif a == "-dname":
        ARPrint (SRC_DIR, 1)
        ARPrint (INC_DIR + LIB_NAME, 1)
        ARPrint (INC_DIR, 1)
        ARPrint (LIN_TB_DIR, 1)
        ARPrint (COM_TB_DIR, 1)
        ARPrint (TB__DIR, 1)
        ARPrint (JNIJ_DIR, 1)
        ARPrint (JNIC_DIR, 1)
        ARPrint (JNI_DIR)
        sys.exit (0)
    #################################
    # If "-nogen" is passed as an   #
    # argument, don't generate any  #
    # file                          #
    #################################
    elif a == "-nogen":
        noGen="yes"
    #################################
    # If -projectname is specified, #
    # use its value to set the      #
    # project name instead of the   #
    # default one.                  #
    #################################
    elif a == "-projectname":
        projectName = args.pop(0)
    elif a != "":
        print("Invalid parameter %s." %(a))


if not os.path.exists (SRC_DIR):
    os.mkdir (SRC_DIR)
if not os.path.exists (INC_DIR):
    os.mkdir (INC_DIR)
if not os.path.exists (INC_DIR + LIB_NAME):
    os.mkdir (INC_DIR + LIB_NAME)
if not os.path.exists (TB__DIR):
    os.mkdir (TB__DIR)
if not os.path.exists (LIN_TB_DIR):
    os.mkdir (LIN_TB_DIR)
if not os.path.exists (COM_TB_DIR):
    os.mkdir (COM_TB_DIR)
if not os.path.exists (JNI_DIR):
    os.mkdir (JNI_DIR)
if not os.path.exists (JNIC_DIR):
    os.mkdir (JNIC_DIR)
if not os.path.exists (JNIJ_DIR):
    os.mkdir (JNIJ_DIR)


# Python class definitions

class ARArg:
    "Represent an argument of a command"
    def __init__(self, argName, argType):
        self.name     = argName
        self.type     = argType
        self.comments = []
    def addCommentLine(self, newCommentLine):
        self.comments.append(newCommentLine)

class ARCommand:
    "Represent a command"
    def __init__(self, cmdName):
        self.name     = cmdName
        self.comments = []
        self.args     = []
    def addCommentLine(self, newCommentLine):
        self.comments.append(newCommentLine)
    def addArgument(self, newArgument):
        self.args.append(newArgument)

class ARClass:
    "Represent a class of commands"
    def __init__(self, className):
        self.name     = className
        self.comments = []
        self.cmds     = []
    def addCommentLine(self, newCommentLine):
        self.comments.append(newCommentLine)
    def addCommand(self, newCommand):
        self.cmds.append(newCommand)

#################################
# 1ST PART :                    #
#################################
# Read XML file to local arrays #
# of commands / classes         #
#################################
file = open (XMLFILENAME_PREFIX + projectName + XMLFILENAME_SUFFIX, 'r')
data = file.read ()
file.close ()

xmlfile = parseString (data)

allClasses = []

classes = xmlfile.getElementsByTagName ('class')
for cmdclass in classes:
    currentClass = ARClass(cmdclass.attributes["name"].nodeValue)
    classComments = cmdclass.firstChild.data.splitlines ()
    for classComm in classComments:
        stripName = classComm.strip ()
        if len (stripName) != 0:
            currentClass.addCommentLine(stripName)
    commands = cmdclass.getElementsByTagName ('cmd')
    for command in commands:
        currentCommand = ARCommand(command.attributes["name"].nodeValue)
        commandComments = command.firstChild.data.splitlines ()
        for commandComm in commandComments:
            stripName = commandComm.strip ()
            if len (stripName) != 0:
                currentCommand.addCommentLine (stripName)
        args = command.getElementsByTagName ('arg')
        for arg in args:
            currentArg = ARArg (arg.attributes["name"].nodeValue, arg.attributes["type"].nodeValue)
            argComments = arg.firstChild.data.splitlines ()
            for argComm in argComments:
                stripName = argComm.strip ()
                if len (stripName) != 0:
                    currentArg.addCommentLine (stripName)
            currentCommand.addArgument (currentArg)
        currentClass.addCommand (currentCommand)
    allClasses.append (currentClass)

if "yes" == noGen: # called with "-nogen"
    ARPrint ('Commands parsed:')
    for cl in allClasses:
        ARPrint ('-> ' + cl.name)
        ARPrint ('   /* ')
        for comment in cl.comments:
            ARPrint ('    * ' + comment)
        ARPrint ('    */')
        for cmd in cl.cmds:
            ARPrint (' --> ' + cmd.name)
            ARPrint ('     /* ')
            for comment in cmd.comments:
                ARPrint ('      * ' + comment)
            ARPrint ('      */')
            for arg in cmd.args:
                ARPrint ('   (' + arg.type + ' ' + arg.name + ')')
                ARPrint ('    /* ')
                for comment in arg.comments:
                    ARPrint ('     * ' + comment)
                ARPrint ('     */')
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
hfile.write ('#ifndef ' + COMMANDSID_DEFINE + '\n')
hfile.write ('#define ' + COMMANDSID_DEFINE + ' (1)\n')
hfile.write ('\n')
hfile.write ('typedef enum {\n')
first = 1
for cl in allClasses:
    ENAME='CLASS'
    if 1 == first:
        hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, cl.name) + ' = 0,\n')
        first = 0
    else:
        hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, cl.name) + ',\n')
hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, 'MAX') + ',\n')
hfile.write ('} ' + AREnumName (ID_SUBMODULE, ENAME) + ';\n')
hfile.write ('\n')
hfile.write ('\n')
for cl in allClasses:
    hfile.write ('typedef enum {\n')
    ENAME=cl.name + '_CMD'
    first = 1
    for cmd in cl.cmds:
        if 1 == first:
            hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, cmd.name) + ' = 0,\n')
            first = 0
        else:
            hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, cmd.name) + ',\n')
    hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, 'MAX') + ',\n')
    hfile.write ('} ' + AREnumName (ID_SUBMODULE, ENAME) + ';\n')
    hfile.write ('\n')

hfile.write ('\n')
hfile.write ('#endif /* ' + COMMANDSID_DEFINE + ' */\n')

hfile.close ()

#################################
# 3RD PART :                    #
#################################
# Generate public coder H file  #
#################################

hfile = open (COMMANDSGEN_HFILE, 'w')

hfile.write ('/**\n')
hfile.write (' * @file ' + COMMANDSGEN_HFILE_NAME + '\n')
hfile.write (' * @brief libARCommands generator header.\n')
hfile.write (' * This file contains all declarations needed to generate commands\n')
hfile.write (' * @note Autogenerated file\n')
hfile.write (' **/\n')
hfile.write ('#ifndef ' + COMMANDSGEN_DEFINE + '\n')
hfile.write ('#define ' + COMMANDSGEN_DEFINE + '\n')
hfile.write ('#include <inttypes.h>\n')
hfile.write ('\n')
hfile.write ('\n')
hfile.write ('/**\n')
hfile.write (' * @brief Error codes for ' + ARFunctionName (GEN_SUBMODULE, 'GenerateClassCommand') + ' functions\n')
hfile.write (' */\n')
GEN_ERR_ENAME='ERRVAL'
hfile.write ('typedef enum {\n')
hfile.write ('    ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ' = 0, ///< No error occured\n')
hfile.write ('    ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ', ///< At least one of the arguments is invalid\n')
hfile.write ('    ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ', ///< The given output buffer was not large enough for the command\n')
hfile.write ('    ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR') + ', ///< Any other error\n')
hfile.write ('} ' +  AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ';\n')
hfile.write ('\n')
hfile.write ('\n')
for cl in allClasses:
    hfile.write ('// Command class ' + cl.name + '\n')
    for cmd in cl.cmds:
        hfile.write ('\n/**\n')
        hfile.write (' * @brief ' + cmd.comments[0] + '\n')
        for comm in cmd.comments[1:]:
            hfile.write (' * ' + comm + '\n')
        hfile.write (' * @warning A command is not NULL terminated and can contain NULL bytes.\n')
        hfile.write (' * @param buffer Pointer to the buffer in which the library should store the command\n')
        hfile.write (' * @param buffLen Size of the buffer\n')
        hfile.write (' * @param cmdLen Pointer to an integer that will hold the actual size of the command\n')
        for arg in cmd.args:
            for comm in arg.comments:
                hfile.write (' * @param ' + arg.name + ' ' + comm + '\n')
        hfile.write (' * @return Error code (see ' + AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ')\n')
        hfile.write (' */\n')
        hfile.write (AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' ' + ARFunctionName (GEN_SUBMODULE, 'Generate' + ARCapitalize (cl.name) + ARCapitalize (cmd.name)) + ' (uint8_t *buffer, int32_t buffLen, int32_t *cmdLen')
        for arg in cmd.args:
            hfile.write (', ' + xmlToCwithConst (arg.type) + ' ' + arg.name)
        hfile.write (');\n')
    hfile.write ('\n')

hfile.write ('\n')
hfile.write ('#endif /* ' + COMMANDSGEN_DEFINE + ' */\n')

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
cfile.write ('#include <' + COMMANDSGEN_HFILE_NAME + '>\n')
cfile.write ('#include <' + COMMANDSID_HFILE_NAME + '>\n')
cfile.write ('#include <libARSAL/ARSAL_Endianness.h>\n')
cfile.write ('\n')
cfile.write ('// Add an 8 bit value to the buffer\n')
cfile.write ('// Returns -1 if the buffer is not big enough\n')
cfile.write ('// Returns the new offset in the buffer on success\n')
cfile.write ('static int32_t ' + ARFunctionName (GEN_SUBMODULE, 'AddU8ToBuffer') + ' (uint8_t *buffer, uint8_t newVal, int32_t oldOffset, int32_t buffCap)\n')
cfile.write ('{\n')
cfile.write ('    if (buffCap < (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    uint8_t *buffptr = &(buffer [oldOffset]);\n')
cfile.write ('    uint8_t localVal = newVal;\n')
cfile.write ('    memcpy (buffptr, &localVal, sizeof (localVal));\n')
cfile.write ('    return oldOffset + sizeof (localVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a 16 bit value to the buffer\n')
cfile.write ('// Returns -1 if the buffer is not big enough\n')
cfile.write ('// Returns the new offset in the buffer on success\n')
cfile.write ('static int32_t ' + ARFunctionName (GEN_SUBMODULE, 'AddU16ToBuffer') + ' (uint8_t *buffer, uint16_t newVal, int32_t oldOffset, int32_t buffCap)\n')
cfile.write ('{\n')
cfile.write ('    if (buffCap < (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    uint16_t *buffptr = (uint16_t *)& (buffer [oldOffset]);\n')
cfile.write ('    uint16_t localVal = htods (newVal);\n')
cfile.write ('    memcpy (buffptr, &localVal, sizeof (localVal));\n')
cfile.write ('    return oldOffset + sizeof (localVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a 32 bit value to the buffer\n')
cfile.write ('// Returns -1 if the buffer is not big enough\n')
cfile.write ('// Returns the new offset in the buffer on success\n')
cfile.write ('static int32_t ' + ARFunctionName (GEN_SUBMODULE, 'AddU32ToBuffer') + ' (uint8_t *buffer, uint32_t newVal, int32_t oldOffset, int32_t buffCap)\n')
cfile.write ('{\n')
cfile.write ('    if (buffCap < (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    uint32_t *buffptr = (uint32_t *)& (buffer [oldOffset]);\n')
cfile.write ('    uint32_t localVal = htodl (newVal);\n')
cfile.write ('    memcpy (buffptr, &localVal, sizeof (localVal));\n')
cfile.write ('    return oldOffset + sizeof (localVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a 64 bit value to the buffer\n')
cfile.write ('// Returns -1 if the buffer is not big enough\n')
cfile.write ('// Returns the new offset in the buffer on success\n')
cfile.write ('static int32_t ' + ARFunctionName (GEN_SUBMODULE, 'AddU64ToBuffer') + ' (uint8_t *buffer, uint64_t newVal, int32_t oldOffset, int32_t buffCap)\n')
cfile.write ('{\n')
cfile.write ('    if (buffCap < (oldOffset + sizeof (newVal)))\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    uint64_t *buffptr = (uint64_t *)& (buffer [oldOffset]);\n')
cfile.write ('    uint64_t localVal = htodll (newVal);\n')
cfile.write ('    memcpy (buffptr, &localVal, sizeof (localVal));\n')
cfile.write ('    return oldOffset + sizeof (localVal);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a NULL Terminated String to the buffer\n')
cfile.write ('// Returns -1 if the buffer is not big enough\n')
cfile.write ('// Returns the new offset in the buffer on success\n')
cfile.write ('static int32_t ' + ARFunctionName (GEN_SUBMODULE, 'AddStringToBuffer') + ' (uint8_t *buffer, const char *newVal, int32_t oldOffset, int32_t buffCap)\n')
cfile.write ('{\n')
cfile.write ('    if (buffCap < (oldOffset + strlen (newVal) + 1))\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    char *buffptr = (char *)& (buffer [oldOffset]);\n')
cfile.write ('    strcpy (buffptr, newVal);\n')
cfile.write ('    return oldOffset + strlen (newVal) + 1;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a float to the buffer\n')
cfile.write ('// Returns -1 if the buffer is not big enough\n')
cfile.write ('// Returns the new offset in the buffer on success\n')
cfile.write ('static int32_t ' + ARFunctionName (GEN_SUBMODULE, 'AddFloatToBuffer') + ' (uint8_t *buffer, float newVal, int32_t oldOffset, int32_t buffCap)\n')
cfile.write ('{\n')
cfile.write ('    return ' + ARFunctionName (GEN_SUBMODULE, 'AddU32ToBuffer') + ' (buffer, * (uint32_t *)&newVal, oldOffset, buffCap);\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Add a float to the buffer\n')
cfile.write ('// Returns -1 if the buffer is not big enough\n')
cfile.write ('// Returns the new offset in the buffer on success\n')
cfile.write ('static int32_t ' + ARFunctionName (GEN_SUBMODULE, 'AddDoubleToBuffer') + ' (uint8_t *buffer, double newVal, int32_t oldOffset, int32_t buffCap)\n')
cfile.write ('{\n')
cfile.write ('    return ' + ARFunctionName (GEN_SUBMODULE, 'AddU64ToBuffer') + ' (buffer, * (uint64_t *)&newVal, oldOffset, buffCap);\n')
cfile.write ('}\n')
cfile.write ('\n')
for cl in allClasses:
    cfile.write ('// Command class ' + cl.name + '\n')
    for cmd in cl.cmds:
        cfile.write (AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' ' + ARFunctionName (GEN_SUBMODULE, 'Generate' +  ARCapitalize (cl.name) + ARCapitalize (cmd.name)) + ' (uint8_t *buffer, int32_t buffLen, int32_t *cmdLen')
        for arg in cmd.args:
            cfile.write (', ' + xmlToCwithConst (arg.type) + ' ' + arg.name)
        cfile.write (')\n')
        cfile.write ('{\n')
        cfile.write ('    int32_t currIndexInBuffer = 0;\n')
        cfile.write ('    ' + AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' retVal = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ';\n')
        cfile.write ('    if ((buffer == NULL) ||\n')
        cfile.write ('        (cmdLen == NULL))\n')
        cfile.write ('    {\n')
        cfile.write ('        return ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ';\n')
        cfile.write ('    }\n')
        hasStringArgs=0
        for arg in cmd.args:
            if arg.type == 'string':
                hasStringArgs=1
                break
        if hasStringArgs == 1:
            cfile.write ('    // Test all String args (if any)\n')
            cfile.write ('    if (')
            first=1
            for arg in cmd.args:
                if 'string' == arg.type:
                    if 1 == first:
                        first = 0
                    else:
                        cfile.write ('        ')
                    cfile.write ('(' + arg.name + ' == NULL) ||\n')
            cfile.write ('       (0))\n')
            cfile.write ('    {\n')
            cfile.write ('        return ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ';\n')
            cfile.write ('    }\n')
            cfile.write ('\n')
        cfile.write ('    // Write class header\n')
        cfile.write ('    if (retVal == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
        cfile.write ('    {\n')
        cfile.write ('        currIndexInBuffer = ' + ARFunctionName (GEN_SUBMODULE, 'AddU16ToBuffer') + ' (buffer, ' + AREnumValue (ID_SUBMODULE, 'CLASS', cl.name) + ', currIndexInBuffer, buffLen);\n')
        cfile.write ('        if (currIndexInBuffer == -1)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
        cfile.write ('        }\n')
        cfile.write ('    }\n')
        cfile.write ('    // Write id header\n')
        cfile.write ('    if (retVal == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
        cfile.write ('    {\n')
        cfile.write ('        currIndexInBuffer = ' + ARFunctionName (GEN_SUBMODULE, 'AddU16ToBuffer') + ' (buffer, ' + AREnumValue (ID_SUBMODULE, cl.name + '_CMD', cmd.name) + ', currIndexInBuffer, buffLen);\n')
        cfile.write ('        if (currIndexInBuffer == -1)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
        cfile.write ('        }\n')
        cfile.write ('    }\n')
        for arg in cmd.args:
            cfile.write ('    // Write arg ' + arg.name + '\n')
            cfile.write ('    if (retVal == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        currIndexInBuffer = ' + ARFunctionName (GEN_SUBMODULE, 'Add' + xmlToSize (arg.type) + 'ToBuffer') + ' (buffer, ' + arg.name + ', currIndexInBuffer, buffLen);\n')
            cfile.write ('        if (currIndexInBuffer == -1)\n')
            cfile.write ('        {\n')
            cfile.write ('            retVal = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
            cfile.write ('        }\n')
            cfile.write ('    }\n')
        cfile.write ('    if (retVal == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
        cfile.write ('    {\n')
        cfile.write ('        *cmdLen = currIndexInBuffer;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n\n')
    cfile.write ('\n')

cfile.write ('\n')
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
hfile.write ('#ifndef ' + COMMANDSDEC_DEFINE + '\n')
hfile.write ('#define ' + COMMANDSDEC_DEFINE + '\n')
hfile.write ('#include <inttypes.h>\n')
hfile.write ('\n')
hfile.write ('\n')
hfile.write ('/**\n')
hfile.write (' * @brief Error codes for ' + ARFunctionName (DEC_SUBMODULE, 'DecodeBuffer') + ' function\n')
hfile.write (' */\n')
DEC_ERR_ENAME='ERRVAL'
hfile.write ('typedef enum {\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' = 0, ///< No error occured\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NO_CALLBACK') + ', ///< No error, but no callback was set (so the call had no effect)\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ', ///< The command buffer contained an unknown command\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ', ///< The command buffer did not contain enough data for the specified command\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ', ///< Any other error\n')
hfile.write ('} ' + AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + ';\n')
hfile.write ('\n/**\n')
hfile.write (' * @brief Decode a comand buffer\n')
hfile.write (' * On success, the callback set for the command will be called in the current thread.\n')
hfile.write (' * @param buffer the command buffer to decode\n')
hfile.write (' * @param buffLen the length of the command buffer\n')
hfile.write (' * @return ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' on success, any error code otherwise\n')
hfile.write (' */\n')
hfile.write (AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
hfile.write (ARFunctionName (DEC_SUBMODULE, 'DecodeBuffer') + ' (uint8_t *buffer, int32_t buffLen);\n')
hfile.write ('\n')
for cl in allClasses:
    hfile.write ('// Command class ' + cl.name + '\n')
    for cmd in cl.cmds:
        hfile.write ('\n/**\n')
        hfile.write (' * @brief callback type for the command ' + cl.name + '.' + cmd.name + '\n')
        hfile.write (' */\n')
        hfile.write ('typedef void (*' + ARTypeName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ') (')
        first = 1
        for arg in cmd.args:
            if first == 1:
                first = 0
            else:
                hfile.write (', ')
            hfile.write (xmlToC (arg.type) + ' ' + arg.name)
        if 0 == first:
            hfile.write (', ')
        hfile.write ('void *custom);\n')
        hfile.write ('/**\n')
        hfile.write (' * @brief callback setter for the command ' + cl.name + '.' + cmd.name + '\n')
        hfile.write (' * @param callback new callback for the command ' + cl.name + '.' + cmd.name + '\n')
        hfile.write (' * @param custom pointer that will be passed to all calls to the callback\n')
        hfile.write (' */\n')
        hfile.write ('void ' + ARFunctionName (DEC_SUBMODULE, 'Set' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' (' + ARTypeName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' callback, void *custom);\n')
    hfile.write ('\n')

hfile.write ('#endif /* ' + COMMANDSDEC_DEFINE + ' */\n')

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
cfile.write ('#include <' + COMMANDSDEC_HFILE_NAME + '>\n')
cfile.write ('#include <' + COMMANDSID_HFILE_NAME + '>\n')
cfile.write ('#include <libARSAL/ARSAL_Endianness.h>\n')
cfile.write ('#include <libARSAL/ARSAL_Mutex.h>\n')
cfile.write ('\n')
cfile.write ('// READ FROM BUFFER HELPERS\n')
cfile.write ('\n')
cfile.write ('// Read an 8 bit value from the buffer\n')
cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
cfile.write ('static uint8_t ' + ARFunctionName (DEC_SUBMODULE, 'Read8FromBuffer') + ' (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
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
cfile.write ('static uint16_t ' + ARFunctionName (DEC_SUBMODULE, 'Read16FromBuffer') + ' (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
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
cfile.write ('static uint32_t ' + ARFunctionName (DEC_SUBMODULE, 'Read32FromBuffer') + ' (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
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
cfile.write ('static uint64_t ' + ARFunctionName (DEC_SUBMODULE, 'Read64FromBuffer') + ' (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
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
cfile.write ('static float ' + ARFunctionName (DEC_SUBMODULE, 'ReadFloatFromBuffer') + ' (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
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
cfile.write ('static double ' + ARFunctionName (DEC_SUBMODULE, 'ReadDoubleFromBuffer') + ' (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
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
cfile.write ('static char* ' + ARFunctionName (DEC_SUBMODULE, 'ReadStringFromBuffer') + ' (uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    char *retVal = NULL;\n')
cfile.write ('    char *buffAddr = (char *)&buffer[*offset];\n')
cfile.write ('    int newOffset = *offset;\n')
cfile.write ('    int str_len = 0;\n')
cfile.write ('    while (newOffset < capacity && \'\\0\' != (char) buffer [newOffset])\n')
cfile.write ('    {\n')
cfile.write ('        str_len++ ;\n');
cfile.write ('        newOffset += sizeof (char);\n');
cfile.write ('    }\n')
cfile.write ('    if (newOffset == capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n');
cfile.write ('        return retVal;\n');
cfile.write ('    }\n')
cfile.write ('    retVal = buffAddr;\n')
cfile.write ('    *offset = newOffset + 1;\n')
cfile.write ('    *error = 0;\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// CALLBACK VARIABLES + SETTERS\n')
cfile.write ('\n')
cfile.write ('static ARSAL_Mutex_t ' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ';\n')
cfile.write ('static int ' + ARGlobalName (DEC_SUBMODULE, 'isInit') + ' = 0;\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'Init') + ' (void)\n')
cfile.write ('{\n')
cfile.write ('    if ((' + ARGlobalName (DEC_SUBMODULE, 'isInit') + ' == 0) &&\n')
cfile.write ('        (ARSAL_Mutex_Init (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ') == 0))\n')
cfile.write ('    {\n')
cfile.write ('        ' + ARGlobalName (DEC_SUBMODULE, 'isInit') + ' = 1;\n')
cfile.write ('    }\n')
cfile.write ('    return ' + ARGlobalName (DEC_SUBMODULE, 'isInit') + ';\n')
cfile.write ('}\n')
cfile.write ('\n')
for cl in allClasses:
    cfile.write ('// Command class ' + cl.name + '\n')
    for cmd in cl.cmds:
        cfile.write ('static ' + ARTypeName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' ' + ARGlobalName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb') + ' = NULL;\n')
        cfile.write ('static void *' + ARGlobalName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Custom') + ' = NULL;\n')
        cfile.write ('void ' + ARFunctionName (DEC_SUBMODULE, 'Set' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' (' + ARTypeName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' callback, void *custom)\n')
        cfile.write ('{\n')
        cfile.write ('    if (' + ARFunctionName (DEC_SUBMODULE, 'Init') + ' () == 1)\n')
        cfile.write ('    {\n')
        cfile.write ('        ARSAL_Mutex_Lock (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ');\n')
        cfile.write ('        ' + ARGlobalName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb') + ' = callback;\n')
        cfile.write ('        ' + ARGlobalName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Custom') + ' = custom;\n')
        cfile.write ('        ARSAL_Mutex_Unlock (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ');\n')
        cfile.write ('    }\n')
        cfile.write ('}\n')
    cfile.write ('\n')

cfile.write ('// DECODER ENTRY POINT\n')
cfile.write (AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
cfile.write (ARFunctionName (DEC_SUBMODULE, 'DecodeBuffer') + ' (uint8_t *buffer, int32_t buffLen)\n')
cfile.write ('{\n')
cfile.write ('    ' + AREnumName (ID_SUBMODULE, 'CLASS') + ' commandClass = ' + AREnumValue (ID_SUBMODULE, 'CLASS', 'MAX') + ';\n')
cfile.write ('    int commandId = -1;\n')
cfile.write ('    int32_t error = 0;\n')
cfile.write ('    int32_t offset = 0;\n')
cfile.write ('    ' + AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + ' retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
cfile.write ('    if (NULL == buffer)\n')
cfile.write ('    {\n')
cfile.write ('        retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
cfile.write ('    {\n')
cfile.write ('        if (' + ARFunctionName (DEC_SUBMODULE, 'Init') + ' () == 0)\n')
cfile.write ('        {\n')
cfile.write ('            retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
cfile.write ('    {\n')
cfile.write ('        commandClass = ' + ARFunctionName (DEC_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
cfile.write ('        if (error == 1)\n')
cfile.write ('            retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
cfile.write ('    {\n')
cfile.write ('        commandId = ' + ARFunctionName (DEC_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
cfile.write ('        if (error == 1)\n')
cfile.write ('            retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
cfile.write ('    {\n')
cfile.write ('        switch (commandClass)\n')
cfile.write ('        {\n')
for cl in allClasses:
    cfile.write ('        case ' + AREnumValue (ID_SUBMODULE, 'CLASS', cl.name) + ':\n')
    cfile.write ('        {\n')
    cfile.write ('            switch (commandId)\n')
    cfile.write ('            {\n')
    for cmd in cl.cmds:
        cfile.write ('            case ' + AREnumValue (ID_SUBMODULE, cl.name + '_CMD', cmd.name) + ':\n')
        cfile.write ('            {\n')
        CBNAME = ARGlobalName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb')
        CBCUSTOMNAME = ARGlobalName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Custom')
        cfile.write ('                ARSAL_Mutex_Lock (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ');\n')
        cfile.write ('                if (' + CBNAME + ' != NULL)\n')
        cfile.write ('                {\n')
        for arg in cmd.args:
            if 'string' == arg.type:
                cfile.write ('                    ' + xmlToC (arg.type) + ' ' + arg.name + ' = NULL;\n')
            else:
                cfile.write ('                    ' + xmlToC (arg.type) + ' ' + arg.name + ';\n')
        for arg in cmd.args:
            cfile.write ('                    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('                    {\n')
            cfile.write ('                        ' + arg.name + ' = ' + xmlToReader (arg.type) + ' (buffer, buffLen, &offset, &error);\n')
            cfile.write ('                        if (error == 1)\n')
            cfile.write ('                            retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
            cfile.write ('                    }\n')
        cfile.write ('                    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
        cfile.write ('                    {\n')
        cfile.write ('                        ' + CBNAME + ' (')
        first = 1
        for arg in cmd.args:
            if 1 == first:
                first = 0
            else:
                cfile.write (', ')
            cfile.write (arg.name)
        if 0 == first:
            cfile.write (', ')
        cfile.write (CBCUSTOMNAME + ');\n')
        cfile.write ('                    }\n')
        cfile.write ('                }\n')
        cfile.write ('                else\n')
        cfile.write ('                {\n')
        cfile.write ('                    retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NO_CALLBACK') + ';\n')
        cfile.write ('                }\n')
        cfile.write ('                ARSAL_Mutex_Unlock (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ');\n')
        cfile.write ('            }\n')
        cfile.write ('            break; /* ' + AREnumValue (ID_SUBMODULE, cl.name + '_CMD', cmd.name) + ' */\n')
    cfile.write ('            default:\n')
    cfile.write ('                retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
    cfile.write ('                break;\n')
    cfile.write ('            }\n')
    cfile.write ('        }\n')
    cfile.write ('        break; /* ' + AREnumValue (ID_SUBMODULE, 'CLASS', cl.name) + ' */\n')
cfile.write ('        default:\n')
cfile.write ('            retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
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
cfile.write ('#include <' + COMMANDSGEN_HFILE_NAME + '>\n')
cfile.write ('#include <' + COMMANDSDEC_HFILE_NAME + '>\n')
cfile.write ('#include <libARSAL/ARSAL_Print.h>\n')
cfile.write ('#include <stdlib.h>\n')
cfile.write ('#include <string.h>\n')
cfile.write ('\n')
cfile.write ('int errcount;\n')
cfile.write ('\n')
for cl in allClasses:
    for cmd in cl.cmds:
        cfile.write ('void ' + ARFunctionName (TB_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb') + ' (')
        first = 1
        for arg in cmd.args:
            if 1 == first:
                first = 0
            else:
                cfile.write (', ')
            cfile.write (xmlToC (arg.type) + ' ' + arg.name)
        if 0 == first:
            cfile.write (', ')
        cfile.write ('void *custom)\n')
        cfile.write ('{\n')
        cfile.write ('    ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "Callback for command ' + cl.name + '.' + cmd.name + ' --> Custom PTR = %p\\n", custom);\n')
        for arg in cmd.args:
            cfile.write ('    ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "' + arg.name + ' value : <' + xmlToPrintf (arg.type) + '>\\n", ' + arg.name + ');\n')
            if "string" == arg.type:
                cfile.write ('    if (strcmp (' + xmlToSample (arg.type) + ', ' + arg.name + ') != 0)\n')
            else:
                cfile.write ('    if (' + arg.name + ' != ' + xmlToSample (arg.type) + ')\n')
            cfile.write ('    {\n')
            if "string" == arg.type:
                cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "BAD ARG VALUE !!! --> Expected <%s>\\n", ' + xmlToSample (arg.type) + ');\n')
            else:
                cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "BAD ARG VALUE !!! --> Expected <' + xmlToSample (arg.type) + '>\\n");\n')
            cfile.write ('        errcount++ ;\n')
            cfile.write ('    }\n')
        cfile.write ('}\n');

cfile.write ('\n')
cfile.write ('void ' + ARFunctionName (TB_SUBMODULE, 'initCb') + ' (void)\n')
cfile.write ('{\n')
cfile.write ('    int cbCustom = 0;\n')
for cl in allClasses:
    for cmd in cl.cmds:
        cfile.write ('    ' + ARFunctionName (DEC_SUBMODULE, 'Set' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' ((' + ARTypeName (DEC_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ') ' + ARFunctionName (TB_SUBMODULE, ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb') + ', (void *)cbCustom++ );\n')
cfile.write ('}\n')

cfile.write ('\n')
cfile.write ('int ' + ARFunctionName (TB_SUBMODULE, 'autoTest') + ' ()\n')
cfile.write ('{\n')
cfile.write ('    int32_t buffSize = 128;\n')
cfile.write ('    uint8_t *buffer = malloc (buffSize * sizeof (uint8_t));\n')
cfile.write ('    ' + AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' res = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ';\n')
cfile.write ('    int32_t resSize = 0;\n')
cfile.write ('    errcount = 0;\n')
cfile.write ('    ' + ARFunctionName (TB_SUBMODULE, 'initCb') + ' ();\n')
for cl in allClasses:
    cfile.write ('    // Command class ' + cl.name + '\n')
    for cmd in cl.cmds:
        cfile.write ('    res = ' + ARFunctionName (GEN_SUBMODULE, 'Generate' + ARCapitalize (cl.name) + ARCapitalize (cmd.name)) + ' (buffer, buffSize, &resSize')
        for arg in cmd.args:
            cfile.write (', ' + xmlToSample (arg.type))
        cfile.write (');\n')
        cfile.write ('    if (res != ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
        cfile.write ('    {\n')
        cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while generating command ' + ARCapitalize (cl.name) + '.' + ARCapitalize (cmd.name) + '\\n\\n");\n')
        cfile.write ('        errcount++ ;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_DEBUG, "' + TB_TAG + '", "Generating command ' + ARCapitalize (cl.name) + '.' + ARCapitalize (cmd.name) + ' succeded\\n");\n')
        cfile.write ('        ' + AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + ' err;\n')
        cfile.write ('        err = ' + ARFunctionName (DEC_SUBMODULE, 'DecodeBuffer') + ' (buffer, resSize);\n')
        cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "Decode return value : %d\\n\\n", err);\n')
        cfile.write ('    }\n')
        cfile.write ('\n')
    cfile.write ('\n')

cfile.write ('    if (errcount == 0)\n')
cfile.write ('    {\n')
cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "No errors !\\n");\n')
cfile.write ('    }\n')
cfile.write ('    else\n')
cfile.write ('    {\n')
cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "%d errors detected during autoTest\\n", errcount);\n')
cfile.write ('    }\n')
cfile.write ('    if (buffer != NULL)\n')
cfile.write ('    {\n')
cfile.write ('        free (buffer);\n')
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
hfile.write ('#ifndef ' + TB_DEFINE + '\n')
hfile.write ('#define ' + TB_DEFINE + ' (1)\n')
hfile.write ('\n')
hfile.write ('int ' + ARFunctionName (TB_SUBMODULE, 'autoTest') + ' ();\n')
hfile.write ('\n')
hfile.write ('#endif /* ' + TB_DEFINE + ' */\n')

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
cfile.write ('#include "../' + COM_TB_DIR + TB_HFILE_NAME + '"\n')
cfile.write ('\n')
cfile.write ('int main (int argc, char *argv[])\n')
cfile.write ('{\n')
cfile.write ('    return ' + ARFunctionName (TB_SUBMODULE, 'autoTest') + ' ();\n')
cfile.write ('}\n')

cfile.close ()

#################################
# 8TH PART :                    #
#################################
# Generate JNI C/Java code      #
#################################

def interfaceName (cls, cmd):
    return JNIClassName + ARCapitalize (cls.name) + ARCapitalize (cmd.name) + 'Listener'
def interfaceVar (cls, cmd):
    return '_' + interfaceName (cls,cmd)
def javaCbName (cls, cmd):
    return 'on' + ARCapitalize (cls.name) + ARCapitalize (cmd.name) + 'Update'

for cl in allClasses:
    for cmd in cl.cmds:
        jfile = open (JNIJ_DIR + interfaceName (cl,cmd) + '.java', 'w')
        jfile.write ('package ' + JNI_PACKAGE_NAME + ';\n')
        jfile.write ('\n')
        jfile.write ('/**\n')
        jfile.write (' * Interface for the command <code>' + ARCapitalize (cmd.name) + '</code> of class <code>' + ARCapitalize (cl.name) + '</code> listener\n')
        jfile.write (' * @author Parrot (c) 2013\n')
        jfile.write (' * @version ' + LIB_VERSION + '\n')
        jfile.write (' */\n')
        jfile.write ('public interface ' + interfaceName (cl,cmd) + ' {\n')
        jfile.write ('\n')
        jfile.write ('    /**\n')
        jfile.write ('     * Called when a command <code>' + ARCapitalize (cmd.name) + '</code> of class <code>' + ARCapitalize (cl.name) + '</code> is decoded\n')
        for arg in cmd.args:
            for comm in arg.comments:
                jfile.write ('     * @param ' + arg.name + ' ' + comm + '\n')
        jfile.write ('     */\n')
        jfile.write ('    void ' + javaCbName (cl,cmd) + ' (')
        first = 1
        for arg in cmd.args:
            if 1 == first:
                first = 0
            else:
                jfile.write (', ')
            jfile.write (xmlToJava (arg.type) + ' ' + arg.name)
        jfile.write (');\n')
        jfile.write ('}\n')
        jfile.close ()

jfile = open (JNI_JFILE, 'w')

jfile.write ('package ' + JNI_PACKAGE_NAME + ';\n')
jfile.write ('\n')
jfile.write ('import ' + SDK_PACKAGE_ROOT + 'arsal.ARNativeData;\n')
jfile.write ('\n')
jfile.write ('/**\n')
jfile.write (' * Java representation of a C ' + JNIClassName + ' object<br>\n')
jfile.write (' * This class holds either app-generated objects, that are to be sent\n')
jfile.write (' * to the device, or network-generated objects, that are to be decoded by\n')
jfile.write (' * the application\n')
jfile.write (' * @author Parrot (c) 2013\n')
jfile.write (' * @version ' + LIB_VERSION + '\n')
jfile.write (' */\n')
jfile.write ('public class ' + JNIClassName + ' implements ARNativeData {\n')
jfile.write ('\n')
jfile.write ('    private static final int dataDefaultLength = 128; // Default size of allocated memory\n')
jfile.write ('\n')
jfile.write ('    private long pdata;\n')
jfile.write ('    private int dataSize; // Size of the actual command\n')
jfile.write ('    private int dataTotalLength; // Total size of allocated memory\n')
jfile.write ('    private boolean inUse;\n')
jfile.write ('    private boolean valid;\n')
jfile.write ('\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Creates a new, empty ' + JNIClassName + ' with the default size.<br>\n')
jfile.write ('     * This is a typical constructor for app-generated ' + JNIClassName + '.<br>\n')
jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
jfile.write ('     * object after it was disposed.\n')
jfile.write ('     */\n')
jfile.write ('    public ' + JNIClassName + ' () {\n')
jfile.write ('        this (dataDefaultLength);\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Creates a new, empty ' + JNIClassName + ' with an user-specified size.<br>\n')
jfile.write ('     * This is a typical constructor for app-generated ' + JNIClassName + '.<br>\n')
jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
jfile.write ('     * object after it was disposed.\n')
jfile.write ('     * @param dataLen user specified length of the command buffer\n')
jfile.write ('     */\n')
jfile.write ('    public ' + JNIClassName + ' (int dataLen) {\n')
jfile.write ('        pdata = nativeAlloc (dataLen);\n')
jfile.write ('        dataSize = 0;\n')
jfile.write ('        valid = false;\n')
jfile.write ('        if (pdata != 0) {\n')
jfile.write ('            dataTotalLength = dataLen;\n')
jfile.write ('            valid = true;\n')
jfile.write ('        } else {\n')
jfile.write ('            dataTotalLength = 0;\n')
jfile.write ('        }\n')
jfile.write ('        inUse = false;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Creates a new ' + JNIClassName + ' from a byte array.<br>\n')
jfile.write ('     * This is a typical constructor for network-generated ' + JNIClassName + '.<br>\n')
jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
jfile.write ('     * object after it was disposed.\n')
jfile.write ('     * @note This function uses the full byte array as data, to use only a part of the data use <code>' + JNIClassName + ' (byte [], int)</code>.\n')
jfile.write ('     * @param oldData byte array which contains original data\n')
jfile.write ('     */\n')
jfile.write ('    public ' + JNIClassName + ' (byte [] oldData) {\n')
jfile.write ('        this (oldData, oldData.length);\n');
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Creates a new ' + JNIClassName + ' from a byte array, with specified maximum length.<br>\n')
jfile.write ('     * This is a typical constructor for network-generated ' + JNIClassName + '.<br>\n')
jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
jfile.write ('     * object after it was disposed.\n')
jfile.write ('     * @param oldData byte array which contains original data\n')
jfile.write ('     * @param dataLen used size in the byte array\n')
jfile.write ('     */\n')
jfile.write ('    public ' + JNIClassName + ' (byte [] oldData, int dataLen) {\n')
jfile.write ('        int totalSize = dataLen;\n')
jfile.write ('        if (dataDefaultLength > totalSize) {\n')
jfile.write ('            totalSize = dataDefaultLength;\n')
jfile.write ('        }\n')
jfile.write ('        pdata = nativeAlloc (totalSize);\n')
jfile.write ('        dataSize = 0;\n')
jfile.write ('        valid = false;\n')
jfile.write ('        if (pdata != 0) {\n')
jfile.write ('            valid = true;\n')
jfile.write ('            dataTotalLength = totalSize;\n')
jfile.write ('            inUse = nativeCopyFromArray (oldData, dataLen, pdata, dataTotalLength);\n')
jfile.write ('        }\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Tells if the ' + JNIClassName + ' is available for reuse<br>\n')
jfile.write ('     * @return <code>true</code> if the ' + JNIClassName + ' is available for reuse, <code>false</code> otherwise\n')
jfile.write ('     */\n')
jfile.write ('    public boolean isAvailable () {\n')
jfile.write ('        return !inUse;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Tells if the ' + JNIClassName + ' is already in use<br>\n')
jfile.write ('     * @return <code>true</code> if the ' + JNIClassName + ' is already used by the application, <code>false</code> otherwise\n')
jfile.write ('     */\n')
jfile.write ('    public boolean isInUse () {\n')
jfile.write ('        return inUse;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Gets a String representing the ' + JNIClassName + '<br>\n')
jfile.write ('     * This is a debugging functions, as the returned String only describes\n')
jfile.write ('     * the container (Java object), and not the actual command (C buffer)\n')
jfile.write ('     * @return Description of the Java Container\n')
jfile.write ('     */\n')
jfile.write ('    public String toString () {\n')
jfile.write ('        return "{ pdata : " + pdata + " | dataSize : " + dataSize + " | valid : " + valid + " | inUse : " + inUse + " }";\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Tells the native part that the ' + JNIClassName + ' is no longer used.<br>\n')
jfile.write ('     * Calling <code>dispose ()</code> on an ' + JNIClassName + ' mark it as invalid,\n')
jfile.write ('     * but don\'t change its contents.<br>\n')
jfile.write ('     */\n')
jfile.write ('    public void dispose () {\n')
jfile.write ('        inUse = false;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    protected void finalize () throws Throwable {\n')
jfile.write ('        try {\n')
jfile.write ('            dispose ();\n')
jfile.write ('            nativeFreeData (pdata);\n')
jfile.write ('        } finally {\n')
jfile.write ('            super.finalize ();\n')
jfile.write ('        }\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Gets the native data pointer<br>\n')
jfile.write ('     * This function is specified by <code>ARNativeData</code> interface\n')
jfile.write ('     * @return The native data pointer for the ' + JNIClassName + ', or <code>0</code> if the ' + JNIClassName + ' is not valid nor in use\n')
jfile.write ('     */\n')
jfile.write ('    public long getData () {\n')
jfile.write ('        if (!valid || !inUse) {\n')
jfile.write ('            return 0;\n')
jfile.write ('        }\n');
jfile.write ('        return pdata;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Gets the native data size<br>\n')
jfile.write ('     * This function is specified by <code>ARNativeData</code> interface\n')
jfile.write ('     * @return The native size of the ' + JNIClassName + ', or <code>0</code> if the ' + JNIClassName + ' is not valid nor in use\n')
jfile.write ('     */\n')
jfile.write ('    public int getDataSize () {\n')
jfile.write ('        if (!valid || !inUse) {\n')
jfile.write ('            return 0;\n')
jfile.write ('        }\n');
jfile.write ('        return dataSize;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Gets a copy of the native data as a byte array<br>\n')
jfile.write ('     * This function is specified by <code>ARNativeData</code> interface\n')
jfile.write ('     * @return A byte-equivalent copy of the native data, or <code>null</code> if the ' + JNIClassName + ' is not valid nor in use\n')
jfile.write ('     */\n')
jfile.write ('    public byte [] getByteData () {\n')
jfile.write ('        if (!valid || !inUse) {;\n')
jfile.write ('            return null;\n')
jfile.write ('        }\n')
jfile.write ('        return nativeGetData (pdata, dataSize);\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Decodes the current ' + JNIClassName + ', calling commands listeners<br>\n')
jfile.write ('     * If a listener was set for the Class/Command contained within the ' + JNIClassName + ',\n')
jfile.write ('     * its <code>onClassCommandUpdate(...)</code> function will be called in the current thread.\n')
jfile.write ('     * @return An ' + ARJavaEnumType (DEC_SUBMODULE, DEC_ERR_ENAME) + ' error code\n')
jfile.write ('     */\n')
jfile.write ('    public ' + ARJavaEnumType (DEC_SUBMODULE, DEC_ERR_ENAME) + ' decode () {\n')
jfile.write ('        ' + ARJavaEnumType (DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + ARJavaEnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
jfile.write ('        if (!valid || !inUse) {\n')
jfile.write ('            return err;\n')
jfile.write ('        }\n')
jfile.write ('        int errInt = nativeDecode (pdata, dataSize);\n')
jfile.write ('        if (' + ARJavaEnumType (DEC_SUBMODULE, DEC_ERR_ENAME) + '.getFromValue (errInt) != null) {\n')
jfile.write ('            err = ' + ARJavaEnumType (DEC_SUBMODULE, DEC_ERR_ENAME) + '.getFromValue (errInt);\n')
jfile.write ('        }\n')
jfile.write ('        return err;\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Reusable equivalent of the <code>' + JNIClassName + ' (byte [] oldData)</code> constructor<br>\n')
jfile.write ('     * This function reuses the current ' + JNIClassName + ', replacing its old content with the\n')
jfile.write ('     * byte array content, typically retreived from a network stream\n')
jfile.write ('     * @note This function uses the full byte array as data, to use only a part of the data use <code>setFromArray (byte [], int)</code>.\n')
jfile.write ('     * @param oldData byte array which contains original data\n')
jfile.write ('     * @return <code>true</code> if the command was properly set, <code>false</code> if a copy error occured, or if the ' + JNIClassName + ' was already in use\n')
jfile.write ('     */\n')
jfile.write ('    public boolean setFromArray (byte [] oldData) {\n')
jfile.write ('        return setFromArray (oldData, oldData.length);\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Reusable equivalent of the <code>' + JNIClassName + ' (byte [] oldData)</code> constructor<br>\n')
jfile.write ('     * This function reuses the current ' + JNIClassName + ', replacing its old content with the\n')
jfile.write ('     * byte array content, typically retreived from a network stream\n')
jfile.write ('     * @param oldData byte array which contains original data\n')
jfile.write ('     * @param dataLen used size in the byte array\n')
jfile.write ('     * @return <code>true</code> if the command was properly set, <code>false</code> if a copy error occured, or if the ' + JNIClassName + ' was already in use\n')
jfile.write ('     */\n')
jfile.write ('    public boolean setFromArray (byte [] oldData, int dataLen) {\n')
jfile.write ('        if (!valid || inUse) {\n')
jfile.write ('            return false;\n')
jfile.write ('        }\n')
jfile.write ('        inUse = nativeCopyFromArray (oldData, dataLen, pdata, dataTotalLength);\n')
jfile.write ('        return inUse;\n')
jfile.write ('    }\n')
jfile.write ('\n')
for cl in allClasses:
    for cmd in cl.cmds:
        jfile.write ('    /**\n')
        jfile.write ('     * Set an ' + JNIClassName + ' to hold the command <code>' + ARCapitalize (cmd.name) + '</code> of class <code>' + ARCapitalize (cl.name) + '</code><br>\n')
        jfile.write ('     * <br>\n')
        jfile.write ('     * Class ' + ARCapitalize (cl.name) + ' description:<br>\n')
        for comm in cl.comments:
            jfile.write ('     * ' + comm + '<br>\n')
        jfile.write ('     * <br>\n')
        jfile.write ('     * Command ' + ARCapitalize (cmd.name) + ' description:<br>\n')
        for comm in cmd.comments:
            jfile.write ('     * ' + comm + '<br>\n')
        jfile.write ('     * <br>\n')
        jfile.write ('     * This function reuses the current ' + JNIClassName + ', then replacing its content with a\n')
        jfile.write ('     * new command created from the current params\n')
        for arg in cmd.args:
            for comm in arg.comments:
                jfile.write ('     * @param ' + arg.name + ' ' + comm + '\n')
        jfile.write ('     * @return An ' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + ' error code.\n')
        jfile.write ('     */\n')
        jfile.write ('    public ' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + ' set' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + ' (')
        first = 1
        for arg in cmd.args:
            if 1 == first:
                first = 0
            else:
                jfile.write (', ')
            jfile.write (xmlToJava (arg.type) + ' ' + arg.name)
        jfile.write (') {\n')
        jfile.write ('        ' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + ' err = ' + ARJavaEnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR') + ';\n')
        jfile.write ('        if (!valid || inUse) {\n')
        jfile.write ('            return err;\n')
        jfile.write ('        }\n')
        jfile.write ('        int errInt = nativeSet' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + ' (pdata, dataTotalLength')
        for arg in cmd.args:
            jfile.write (', ' + arg.name)
        jfile.write (');\n')
        jfile.write ('        if (' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + '.getFromValue (errInt) != null) {\n')
        jfile.write ('            err = ' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + '.getFromValue (errInt);\n')
        jfile.write ('            inUse = (err == ' + ARJavaEnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ');\n')
        jfile.write ('        } else {\n')
        jfile.write ('            inUse = false;\n')
        jfile.write ('        }\n')
        jfile.write ('        return err;\n')
        jfile.write ('    }\n')
        jfile.write ('\n')


for cl in allClasses:
    for cmd in cl.cmds:
        jfile.write ('    private static ' + interfaceName (cl,cmd) + ' ' + interfaceVar (cl,cmd) + ' = null;\n')
        jfile.write ('\n')
        jfile.write ('    /**\n')
        jfile.write ('     * Set the listener for the command <code>' + ARCapitalize (cmd.name) + '</code> of class <code>' + ARCapitalize (cl.name) + '</code><br>\n')
        jfile.write ('     * Listeners are static to the class, and are not to be set on every object\n')
        jfile.write ('     * @param ' + interfaceVar (cl,cmd) + '_PARAM New listener for the command\n')
        jfile.write ('     */\n')
        jfile.write ('    public static void set' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Listener (' + interfaceName (cl,cmd) + ' ' + interfaceVar (cl,cmd) + '_PARAM) {\n')
        jfile.write ('        ' + interfaceVar (cl,cmd) + ' = ' + interfaceVar (cl,cmd) + '_PARAM;\n')
        jfile.write ('    }\n')
        jfile.write ('\n')
    jfile.write ('\n')
jfile.write ('\n')
jfile.write ('    private native int     nativeDecode (long jpdata, int jdataSize);\n')
jfile.write ('    private native boolean nativeCopyFromArray (byte [] oldData, int oldDataLen, long pdata, int dataLen);\n')
jfile.write ('    private native long    nativeAlloc (int size);\n')
jfile.write ('    private native void    nativeFreeData (long dataToFree);\n')
jfile.write ('    private native byte [] nativeGetData (long jpdata, int jdataSize);\n')
jfile.write ('\n')
for cl in allClasses:
    for cmd in cl.cmds:
        jfile.write ('    private native int     nativeSet' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + ' (long pdata, int dataTotalLength')
        for arg in cmd.args:
            jfile.write (', ' + xmlToJava (arg.type) + ' ' + arg.name)
        jfile.write (');\n')
jfile.write ('}\n')

jfile.close ()

cfile = open (JNI_CFILE, 'w')

JNI_FUNC_PREFIX='Java_' + JNI_PACKAGE_NAME.replace ('.', '_') + '_'
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
cfile.write ('#include <' + COMMANDSGEN_HFILE_NAME + '>\n')
cfile.write ('#include <' + COMMANDSDEC_HFILE_NAME + '>\n')
cfile.write ('#include <jni.h>\n')
cfile.write ('#include <stdlib.h>\n')
cfile.write ('\n')
cfile.write ('static jfieldID g_dataSize_id = 0;\n')
cfile.write ('static JavaVM *g_vm = NULL;\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT jboolean JNICALL\n')
cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeCopyFromArray (' + JNI_FIRST_ARGS + ', jbyteArray oldData, jint oldDataLen, jlong c_pdata, jint j_dataLen)\n')
cfile.write ('{\n')
cfile.write ('    jboolean valid = JNI_TRUE;\n')
cfile.write ('\n')
cfile.write ('    if (g_dataSize_id == 0)\n')
cfile.write ('    {\n')
cfile.write ('        jclass clz = (*env)->GetObjectClass (env, thizz);\n')
cfile.write ('        if (clz != 0)\n')
cfile.write ('        {\n')
cfile.write ('            g_dataSize_id = (*env)->GetFieldID (env, clz, "dataSize", "I");\n')
cfile.write ('            (*env)->DeleteLocalRef (env, clz);\n')
cfile.write ('        }\n')
cfile.write ('        else\n')
cfile.write ('        {\n')
cfile.write ('            return JNI_FALSE;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (oldDataLen > j_dataLen)\n')
cfile.write ('    {\n')
cfile.write ('        valid = JNI_FALSE;\n')
cfile.write ('    }\n')
cfile.write ('    jbyte *j_pdata = NULL;\n')
cfile.write ('\n')
cfile.write ('    if (valid == JNI_TRUE)\n')
cfile.write ('    {\n')
cfile.write ('        j_pdata = (*env)->GetByteArrayElements (env, oldData, NULL);\n')
cfile.write ('        if (j_pdata == NULL)\n')
cfile.write ('        {\n')
cfile.write ('            valid = JNI_FALSE;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (valid == JNI_TRUE)\n')
cfile.write ('    {\n')
cfile.write ('        memcpy ((uint8_t *) (intptr_t) c_pdata, j_pdata, oldDataLen);\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (j_pdata != NULL)\n')
cfile.write ('    {\n')
cfile.write ('        (*env)->ReleaseByteArrayElements (env, oldData, j_pdata, JNI_ABORT); // Use JNI_ABORT because we NEVER want to modify oldData\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (valid == JNI_TRUE)\n')
cfile.write ('    {\n')
cfile.write ('        (*env)->SetIntField (env, thizz, g_dataSize_id, (jint)oldDataLen);\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    return valid;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT jlong JNICALL\n')
cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeAlloc (' + JNI_FIRST_ARGS + ', jint sizeInBytes)\n')
cfile.write ('{\n')
cfile.write ('    uint8_t *c_pdata = (uint8_t *) malloc ((int)sizeInBytes);\n')
cfile.write ('    return (jlong) (intptr_t) c_pdata;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT void JNICALL\n')
cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeFreeData (' + JNI_FIRST_ARGS + ', jlong dataToFree)\n')
cfile.write ('{\n')
cfile.write ('    uint8_t *c_pdata = (uint8_t *) (intptr_t)dataToFree;\n')
cfile.write ('    if (c_pdata != NULL)\n')
cfile.write ('    {\n')
cfile.write ('        free (c_pdata);\n')
cfile.write ('    }\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT jbyteArray JNICALL\n')
cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeGetData (' + JNI_FIRST_ARGS + ', jlong jpdata, jint jdataSize)\n')
cfile.write ('{\n')
cfile.write ('    jbyteArray retArray = (*env)->NewByteArray (env, jdataSize);\n')
cfile.write ('    if (retArray != NULL)\n')
cfile.write ('    {\n')
cfile.write ('        (*env)->SetByteArrayRegion (env, retArray, 0, jdataSize, (jbyte *) (intptr_t)jpdata);\n')
cfile.write ('    }\n')
cfile.write ('    return retArray;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT jint JNICALL\n')
cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeDecode (' + JNI_FIRST_ARGS + ', jlong jpdata, jint jdataSize)\n')
cfile.write ('{\n')
cfile.write ('    uint8_t *pdata = (uint8_t *) (intptr_t)jpdata;\n')
cfile.write ('    ' + AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + ARFunctionName (DEC_SUBMODULE, 'DecodeBuffer') + ' (pdata, jdataSize);\n')
cfile.write ('    return err;\n')
cfile.write ('}\n')
cfile.write ('\n')
for cl in allClasses:
    for cmd in cl.cmds:
        cfile.write ('JNIEXPORT jint JNICALL\n')
        cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeSet' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + ' (' + JNI_FIRST_ARGS + ', jlong c_pdata, jint dataLen')
        for arg in cmd.args:
            cfile.write (', ' + xmlToJni (arg.type) + ' ' + arg.name)
        cfile.write (')\n')
        cfile.write ('{\n')
        cfile.write ('    int32_t c_dataSize = 0;\n')
        cfile.write ('    ' + AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' err = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR') + ';\n');
        cfile.write ('    if (g_dataSize_id == 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        jclass clz = (*env)->GetObjectClass (env, thizz);\n')
        cfile.write ('        if (clz != 0)\n')
        cfile.write ('        {\n')
        cfile.write ('            g_dataSize_id = (*env)->GetFieldID (env, clz, "dataSize", "I");\n')
        cfile.write ('            (*env)->DeleteLocalRef (env, clz);\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            return err;\n')
        cfile.write ('        }\n')
        cfile.write ('    }\n')
        cfile.write ('\n')
        for arg in cmd.args:
            if 'string' == arg.type:
                cfile.write ('    const char *c_' + arg.name + ' = (*env)->GetStringUTFChars (env, ' + arg.name + ', NULL);\n')
        cfile.write ('    err = ' + ARFunctionName (GEN_SUBMODULE, 'Generate' + ARCapitalize (cl.name) + ARCapitalize (cmd.name)) + ' ((uint8_t *) (intptr_t) c_pdata, dataLen, &c_dataSize')
        for arg in cmd.args:
            if 'string' == arg.type:
                cfile.write (', c_' + arg.name)
            else:
                cfile.write (', (' + xmlToC (arg.type) + ')' + arg.name)
        cfile.write (');\n')
        for arg in cmd.args:
            if 'string' == arg.type:
                cfile.write ('    (*env)->ReleaseStringUTFChars (env, ' + arg.name + ', c_' + arg.name + ');\n')
        cfile.write ('    if (err == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
        cfile.write ('    {\n')
        cfile.write ('        (*env)->SetIntField (env, thizz, g_dataSize_id, (jint)c_dataSize);\n')
        cfile.write ('    }\n')
        cfile.write ('    return err;\n')
        cfile.write ('}\n')
        cfile.write ('\n')

def cCallbackName (cls,cmd):
    return ARFunctionName (JNI_SUBMODULE, ARCapitalize (cls.name) + ARCapitalize (cmd.name) + 'nativeCb')

for cl in allClasses:
    for cmd in cl.cmds:
        cfile.write ('void ' + cCallbackName (cl,cmd) + ' (')
        for arg in cmd.args:
            cfile.write (xmlToC (arg.type) + ' ' + arg.name + ', ')
        cfile.write ('void *custom)\n')
        cfile.write ('{\n')
        cfile.write ('    jclass clazz = (jclass)custom;\n')
        cfile.write ('    jint res;\n')
        cfile.write ('    JNIEnv *env = NULL;\n')
        cfile.write ('    res = (*g_vm)->GetEnv (g_vm, (void **)&env, JNI_VERSION_1_6);\n')
        cfile.write ('    if (res < 0) { return; }\n')
        cfile.write ('    jfieldID delegate_fid = (*env)->GetStaticFieldID (env, clazz, "' + interfaceVar (cl,cmd) + '", "L' + JNI_PACKAGE_NAME.replace ('.', '/') + '/' + interfaceName (cl,cmd) + ';");\n')
        cfile.write ('    jobject delegate = (*env)->GetStaticObjectField (env, clazz, delegate_fid);\n')
        cfile.write ('    if (delegate == NULL) { return; }\n')
        cfile.write ('\n')
        cfile.write ('    jclass d_clazz = (*env)->GetObjectClass (env, delegate);\n')
        cfile.write ('    jmethodID d_methodid = (*env)->GetMethodID (env, d_clazz, "' + javaCbName (cl,cmd) + '", "(')
        for arg in cmd.args:
            cfile.write ('' + xmlToJavaSig (arg.type))
        cfile.write (')V");\n')
        cfile.write ('    (*env)->DeleteLocalRef (env, d_clazz);\n')
        cfile.write ('    if (d_methodid != NULL)\n')
        cfile.write ('    {\n')
        for arg in cmd.args:
            if 'string' == arg.type:
                cfile.write ('        jstring j_' + arg.name + ' = (*env)->NewStringUTF (env, ' + arg.name + ');\n')
        cfile.write ('        (*env)->CallVoidMethod (env, delegate, d_methodid')
        for arg in cmd.args:
            if 'string' == arg.type:
                cfile.write (', j_' + arg.name)
            else:
                cfile.write (', ' + arg.name)
        cfile.write (');\n')
        for arg in cmd.args:
            if 'string' == arg.type:
                cfile.write ('        (*env)->DeleteLocalRef (env, j_' + arg.name + ');\n')
        cfile.write ('    }\n')
        cfile.write ('    (*env)->DeleteLocalRef (env, delegate);\n')
        cfile.write ('}\n')
        cfile.write ('\n')

cfile.write ('JNIEXPORT jint JNICALL\n')
cfile.write ('JNI_OnLoad (JavaVM *vm, void *reserved)\n')
cfile.write ('{\n')
cfile.write ('    g_vm = vm;\n')
cfile.write ('    JNIEnv *env = NULL;\n')
cfile.write ('    if ((*vm)->GetEnv (vm, (void **)&env, JNI_VERSION_1_6) != JNI_OK)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    jclass clazz = (*env)->FindClass (env, "' + JNI_PACKAGE_NAME.replace ('.', '/') + '/' + JNIClassName + '");\n')
cfile.write ('    if (clazz == NULL)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    jclass g_class = (*env)->NewGlobalRef (env, clazz);\n')
cfile.write ('    if (g_class == NULL)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('\n')
for cl in allClasses:
    for cmd in cl.cmds:
        cfile.write ('    ' + ARFunctionName (DEC_SUBMODULE, 'Set' + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' (' + cCallbackName (cl,cmd) + ', (void *)g_class);\n')
    cfile.write ('\n')
cfile.write ('\n')
cfile.write ('    return JNI_VERSION_1_6;\n')
cfile.write ('}\n')
cfile.write ('/* END OF GENERAED CODE */\n')

cfile.close ()

mfile = open (JNI_MAKEFILE, 'w')

mfile.write ('LOCAL_PATH := $(call my-dir)\n')
mfile.write ('\n')
mfile.write ('#' + LIB_NAME + '\n')
mfile.write ('include $(CLEAR_VARS)\n')
mfile.write ('\n')
mfile.write ('LOCAL_MODULE := ' + LIB_NAME.upper () + '-prebuilt\n')
mfile.write ('LOCAL_SRC_FILES := lib/' + LIB_NAME.lower () + '.a # Add _dbg before .a for debug version\n')
mfile.write ('\n')
mfile.write ('include $(PREBUILT_STATIC_LIBRARY)\n')
mfile.write ('\n')
mfile.write ('#JNI Wrapper\n')
mfile.write ('include $(CLEAR_VARS)\n')
mfile.write ('\n')
mfile.write ('LOCAL_C_INCLUDES := $(LOCAL_PATH)/include $(LOCAL_PATH)/../libARSAL/include/ # CHECK/EDIT THIS\n')
mfile.write ('LOCAL_LDLIBS := -llog\n')
mfile.write ('LOCAL_MODULE := ' + LIB_NAME.lower () + '_android\n')
mfile.write ('LOCAL_SRC_FILES := ' + JNI_CFILE_NAME + '\n')
mfile.write ('LOCAL_STATIC_LIBRARIES := ' + LIB_NAME.upper () + '-prebuilt\n')
mfile.write ('LOCAL_SHARED_LIBRARIES := LIBARSAL-prebuilt\n')
mfile.write ('\n')
mfile.write ('include $(BUILD_SHARED_LIBRARY)\n')

mfile.close ()
