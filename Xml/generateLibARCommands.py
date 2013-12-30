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

def ARPrint (msg, noNewLine=False):
    sys.stdout.write (msg)
    if not noNewLine:
        sys.stdout.write ('\n')
    else:
        sys.stdout.write (' ')

def EXIT (code):
    if code != 0:
        ARPrint ('-- ABORTING --')
    sys.exit (code)

configureAcFile = open (MYDIR + '/../Build/configure.ac', 'rb')
AC_INIT_LINE=configureAcFile.readline ()
while (not AC_INIT_LINE.startswith ('AC_INIT')) and ('' != AC_INIT_LINE):
    AC_INIT_LINE=configureAcFile.readline ()
if '' == AC_INIT_LINE:
    ARPrint ('Unable to read from configure.ac file !')
    EXIT (1)

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
JNI_PACKAGE_DIR = JNI_PACKAGE_NAME.replace ('.', '/')

# Default project name
DEFAULTPROJECTNAME='common'

#Name (and path) of the xml file
XMLFILENAME_PREFIX=MYDIR + '/../Xml/'
XMLFILENAME_SUFFIX='_commands.xml'
XMLDEBUGFILENAME_SUFFIX="_debug.xml"

#Name of the output private header containing id enums
COMMANDSID_HFILE_NAME='ARCOMMANDS_Ids.h'

#Name of the output public header containing typedefs
COMMANDSTYPES_HFILE_NAME=LIB_NAME + '/ARCOMMANDS_Types.h'

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
JNI_DIR=MYDIR + "/../JNI/"

#Relative path of JNI/C dir
JNIC_DIR=JNI_DIR + 'c/'

#Relative path of JNI/Java dir
JNIJ_DIR=JNI_DIR + 'java/'
JNIJ_OUT_DIR=JNIJ_DIR + JNI_PACKAGE_DIR + '/'

##### END OF CONFIG #####

# Create array of generated files (so we can cleanup only our files)
GENERATED_FILES = []
COMMANDSID_HFILE=SRC_DIR + COMMANDSID_HFILE_NAME
GENERATED_FILES.append (COMMANDSID_HFILE)
COMMANDSGEN_HFILE=INC_DIR + COMMANDSGEN_HFILE_NAME
GENERATED_FILES.append (COMMANDSGEN_HFILE)
COMMANDSTYPES_HFILE=INC_DIR + COMMANDSTYPES_HFILE_NAME
GENERATED_FILES.append (COMMANDSTYPES_HFILE)
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
JNI_JFILE=JNIJ_OUT_DIR + JNI_JFILE_NAME
GENERATED_FILES.append (JNI_JFILE)
JAVA_INTERFACES_FILES=JNIJ_OUT_DIR + JAVA_INTERFACES_FILES_NAME

# Create names for #ifndef _XXX_ statements in .h files
COMMANDSID_DEFINE='_' + COMMANDSID_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSDEC_DEFINE='_' + COMMANDSDEC_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSGEN_DEFINE='_' + COMMANDSGEN_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSTYPES_DEFINE='_' + COMMANDSTYPES_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
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
    if Enum.upper () == 'ERROR' and (Name.upper () == 'OK' or Name.upper () == 'ERROR'):
        return LIB_MODULE.upper () + '_' + Submodule.upper () + '_' + Name.upper ()
    else:
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

# Equivalent calls for the Decoder print internal functions
CPRINTERS = [ARFunctionName ('Decoder', 'PrintU8'),    ARFunctionName ('Decoder', 'PrintI8'),
             ARFunctionName ('Decoder', 'PrintU16'),   ARFunctionName ('Decoder', 'PrintI16'),
             ARFunctionName ('Decoder', 'PrintU32'),   ARFunctionName ('Decoder', 'PrintI32'),
             ARFunctionName ('Decoder', 'PrintU64'),   ARFunctionName ('Decoder', 'PrintI64'),
             ARFunctionName ('Decoder', 'PrintFloat'), ARFunctionName ('Decoder', 'PrintDouble'),
             ARFunctionName ('Decoder', 'PrintString')]

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

def xmlToC (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return AREnumName(proj.name + '_' + cl.name, cmd.name + '_' + arg.name);
    xmlIndex = XMLTYPES.index (arg.type)
    return CTYPES [xmlIndex]

def xmlToCwithConst (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return AREnumName(proj.name + '_' + cl.name, cmd.name + '_' + arg.name);
    xmlIndex = XMLTYPES.index (arg.type)
    return CTYPES_WC [xmlIndex]

def xmlToSize (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return 'U32';
    xmlIndex = XMLTYPES.index (arg.type)
    return SZETYPES [xmlIndex]

def xmlToReader (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return '(' + AREnumName(proj.name + '_' + cl.name, cmd.name + '_' + arg.name) + ')' + ARFunctionName ('Decoder', 'Read32FromBuffer')
    xmlIndex = XMLTYPES.index (arg.type)
    return CREADERS [xmlIndex]

def xmlToPrinter (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return '(' + AREnumName(proj.name + '_' + cl.name, cmd.name + '_' + arg.name) + ')' + ARFunctionName ('Decoder', 'PrintI32')
    xmlIndex = XMLTYPES.index (arg.type)
    return CPRINTERS [xmlIndex]

def xmlToJava (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return ARJavaEnumType(proj.name + '_' + cl.name, cmd.name + '_' + arg.name);
    xmlIndex = XMLTYPES.index (arg.type)
    return JAVATYPES [xmlIndex]

def xmlToJavaSig (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return 'I';
    xmlIndex = XMLTYPES.index (arg.type)
    return JAVASIG [xmlIndex]

def xmlToJni (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return 'jint';
    xmlIndex = XMLTYPES.index (arg.type)
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

def xmlToSample (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return '(' + AREnumName(proj.name + '_' + cl.name, cmd.name + '_' + arg.name) + ')0';
    xmlIndex = XMLTYPES.index (arg.type)
    return SAMPLEARGS [xmlIndex]

def xmlToPrintf (proj, cl, cmd, arg):
    if 'enum' == arg.type:
        return '%d';
    xmlIndex = XMLTYPES.index (arg.type)
    return PRINTFF [xmlIndex]

noGen = False
genDebug = False
projects = [DEFAULTPROJECTNAME]
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
            ARPrint (fil, True)
        ARPrint (JAVA_INTERFACES_FILES, True)
        ARPrint ('')
        EXIT (0)
    #################################
    # If "-dname" is passed as an   #
    # argument, just output the     #
    # name of the generated dirs    #
    #################################
    elif a == "-dname":
        ARPrint (SRC_DIR, True)
        ARPrint (INC_DIR + LIB_NAME, True)
        ARPrint (INC_DIR, True)
        ARPrint (LIN_TB_DIR, True)
        ARPrint (COM_TB_DIR, True)
        ARPrint (TB__DIR, True)
        ARPrint (JNIJ_OUT_DIR, True)
        ARPrint (JNIJ_DIR, True)
        ARPrint (JNIC_DIR, True)
        ARPrint (JNI_DIR)
        EXIT (0)
    #################################
    # If "-nogen" is passed as an   #
    # argument, don't generate any  #
    # file                          #
    #################################
    elif a == "-nogen":
        noGen=True
    #################################
    # If -projectname is specified, #
    # use its value to set the      #
    # project name instead of the   #
    # default one.                  #
    #################################
    elif a == "-projects":
        projectsList = args.pop(0)
        for project in projectsList.split(','):
            projects.append (project)
    #################################
    # If -debug-cmds is specified   #
    # and set to 'yes', generate    #
    # commands for _debug.xml files.#
    #################################
    elif a == "-debug-cmds":
        val = args.pop(0)
        if val == 'yes':
            genDebug = True
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
if not os.path.exists (JNIJ_OUT_DIR):
    os.makedirs (JNIJ_OUT_DIR)


# Python class definitions

class AREnum:
    "Represent an enum of an argument"
    def __init__(self, enumName):
        self.name     = enumName
        self.comments = []
    def addCommentLine(self, newCommentLine):
        self.comments.append(newCommentLine)
    def check(self):
        ret = ''
        if len (self.comments) == 0:
            ret = ret + '\n--- Enum ' + self.name + ' don\'t have any comment !'
        return ret

class ARArg:
    "Represent an argument of a command"
    def __init__(self, argName, argType):
        self.name     = argName
        self.type     = argType
        self.comments = []
        self.enums    = []
    def addCommentLine(self, newCommentLine):
        self.comments.append(newCommentLine)
    def addEnum(self, newEnum):
        self.enums.append(newEnum)
    def check(self):
        ret = ''
        enumret = ''
        if len (self.comments) == 0:
            ret = ret + '\n--- Argument ' + self.name + ' don\'t have any comment !'
        for enum in self.enums:
            enumret = enumret + enum.check ()
        if len (enumret) != 0:
            ret = ret + '\n-- Argument ' + self.name + ' has errors in its enums:'
        ret = ret + enumret
        return ret

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
    def check(self):
        ret = ''
        argret = ''
        if len (self.comments) == 0:
            ret = ret + '\n-- Command ' + self.name + ' don\'t have any comment !'
        for arg in self.args:
            argret = argret + arg.check ()
        if len (argret) != 0:
            ret = ret + '\n-- Command ' + self.name + ' has errors in its arguments:'
        ret = ret + argret
        return ret

MAX_CLASS_ID = 255
CLASS_MAX_CMDS = 65536
class ARClass:
    "Represent a class of commands"
    def __init__(self, className, ident):
        self.name     = className
        self.ident    = ident
        self.comments = []
        self.cmds     = []
    def addCommentLine(self, newCommentLine):
        self.comments.append(newCommentLine)
    def addCommand(self, newCommand):
        self.cmds.append(newCommand)
    def check(self):
        ret = ''
        cmdret = ''
        if len (self.comments) == 0:
            ret = ret + '\n- Class ' + self.name + ' don\'t have any comment !'
        if int (self.ident) > MAX_PROJECT_ID:
            ret = ret + '\n- Class ' + self.name + ' has a too big id number (' + self.ident + '). Maximum is ' + str (MAX_CLASS_ID) + '.'
        if len (self.cmds) > CLASS_MAX_CMDS:
            ret = ret + '\n- Class ' + self.name + ' has too many commands (' + str (len (self.cmds)) + '). Maximum number of commands is ' + str(CLASS_MAX_CMDS) + '.'
        for cmd in self.cmds:
            cmdret = cmdret + cmd.check ()
        if len (cmdret) != 0:
            ret = ret + '\n- Class ' + self.name + ' has errors in its commands:'
        ret = ret + cmdret
        return ret

PROJECT_MAX_CLASS = 256
MAX_PROJECT_ID = 255
class ARProject:
    "Represent a project (an XML file)"
    def __init__(self, projectName, ident):
        self.name     = projectName
        self.ident    = ident
        self.comments = []
        self.classes  = []
    def addCommentLine(self, newCommentLine):
        self.comments.append (newCommentLine)
    def addClass(self, newClass):
        self.classes.append(newClass)
    def check(self):
        ret = ''
        clsret = ''
        if len (self.comments) == 0:
            ret = ret + '\nProject ' + self.name + ' don\'t have any comment !'
        if int (self.ident) > MAX_PROJECT_ID:
            ret = ret + '\nProject ' + self.name + ' has a too big id number (' + self.ident + '). Maximum is ' + str (MAX_PROJECT_ID) + '.'
        if len (self.classes) > PROJECT_MAX_CLASS:
            ret = ret + '\nProject ' + self.name + ' has too many classes (' + str (len (self.classes)) + '). Maximum number of classes is ' + str(PROJECT_MAX_CLASS) + '.'
        for cls in self.classes:
            clsret = clsret + cls.check ()
        if len (clsret) != 0:
            ret = ret + '\nProject ' + self.name + ' has errors in its classes:'
        ret = ret + clsret
        return ret

#################################
# 1ST PART :                    #
#################################
# Read XML file to local arrays #
# of commands / classes         #
#################################

# If the 'all' project is in our list, replace our list contents with the
# list of all xml files here
if 'all' in projects:
    projects = []
    for files in os.listdir (XMLFILENAME_PREFIX):
        if files.endswith (XMLFILENAME_SUFFIX):
            proj = files.replace (XMLFILENAME_SUFFIX,'')
            projects.append (proj)

allProjects = []

def parseXml(FNAME, projectName):
    if not os.path.exists(FNAME):
        return
    file = open (FNAME, 'r')
    data = file.read ()
    file.close ()
    xmlfile = parseString (data)

    # Check if the XMLFile only contains ONE project (not zero, nor more)
    xmlproj = xmlfile.getElementsByTagName ('project')
    if len (xmlproj) != 1:
        ARPrint (FNAME + ' should contain exactly ONE project tag.')
        EXIT (1)
    proj = ARProject (projectName, xmlproj[0].attributes["id"].nodeValue)

    # Check if project id and name are unique
    for p2 in allProjects:
        if p2.ident == proj.ident:
            ARPrint ('Project `' + projectName + '` has the same id as project `' + p2.name + '`.')
            ARPrint (' --> Project ID must be unique, and must NEVER change')
            ARPrint (' --> Debug Project ID are usually Project ID + 128')
            EXIT (1)
        if p2.name == proj.name:
            ARPrint ('Project `' + projectName + '` exists twice.')
            ARPrint (' --> Project must have a unique name within the application')
            EXIT (1)

    # Get project comments
    projComments = xmlproj[0].firstChild.data.splitlines ()
    for projComm in projComments:
        stripName = projComm.strip ()
        if len (stripName) != 0:
            proj.addCommentLine (stripName)

    classes = xmlfile.getElementsByTagName ('class')
    for cmdclass in classes:
        # Get class name
        currentClass = ARClass(cmdclass.attributes["name"].nodeValue, cmdclass.attributes["id"].nodeValue)
        # Check if class id and name are unique within the project
        for cls in proj.classes:
            if cls.ident == currentClass.ident:
                ARPrint ('Class `' + currentClass.name + '` has the same id as class `' + cls.name + '` within project `' + proj.name + '`.')
                ARPrint (' --> Class ID must be unique within their project, and must NEVER change')
                EXIT (1)
            if cls.name == currentClass.name:
                ARPrint ('Class `' + cls.name + '` appears multiple times in `' + proj.name + '` !')
                ARPrint (' --> Classes must have unique names in a given project (but can exist in multiple projects)')
                EXIT (1)
        # Get class comments
        classComments = cmdclass.firstChild.data.splitlines ()
        for classComm in classComments:
            stripName = classComm.strip ()
            if len (stripName) != 0:
                currentClass.addCommentLine(stripName)
        commands = cmdclass.getElementsByTagName ('cmd')
        for command in commands:
            # Get command name
            currentCommand = ARCommand(command.attributes["name"].nodeValue)
            # Check if command name is unique
            for cmd in currentClass.cmds:
                if cmd.name == currentCommand.name:
                    ARPrint ('Command `' + cmd.name + '` appears multiple times in `' + proj.name + '.' + currentClass.name + '` !')
                    ARPrint (' --> Commands must have unique names in a given class (but can exist in multiple classes)')
                    EXIT (1)
            # Get command comments
            commandComments = command.firstChild.data.splitlines ()
            for commandComm in commandComments:
                stripName = commandComm.strip ()
                if len (stripName) != 0:
                    currentCommand.addCommentLine (stripName)
            args = command.getElementsByTagName ('arg')
            for arg in args:
                # Get arg name / type
                currentArg = ARArg (arg.attributes["name"].nodeValue, arg.attributes["type"].nodeValue)
                # Check if arg name is unique
                for argTest in currentCommand.args:
                    if argTest.name == currentArg.name:
                        ARPrint ('Arg `' + currentArg.name + '` appears multiple time in `' + proj.name + '.' + currentClass.name + '.' + currentCommand.name + '` !')
                        ARPrint (' --> Args must have unique name in a given command (but can exist in multiple commands)')
                        EXIT (1)
                # Get arg comments
                argComments = arg.firstChild.data.splitlines ()
                for argComm in argComments:
                    stripName = argComm.strip ()
                    if len (stripName) != 0:
                        currentArg.addCommentLine (stripName)

                enums = arg.getElementsByTagName ('enum')
                for enum in enums:
                    currentEnum = AREnum(enum.attributes["name"].nodeValue)
                    # Get command comments
                    enumComments = enum.firstChild.data.splitlines ()
                    for enumComm in enumComments:
                        stripName = enumComm.strip ()
                        if len (stripName) != 0:
                            currentEnum.addCommentLine (stripName)
                    # Check if arg name is unique
                    for enumTest in currentArg.enums:
                        if enumTest.name == currentEnum.name:
                            ARPrint ('Enum `' + currentEnum.name + '` appears multiple time in `' + proj.name + '.' + currentClass.name + '.' + currentCommand.name + '` !')
                            ARPrint (' --> Enums must have unique name in a given command (but can exist in multiple commands)')
                            EXIT (1)
                    currentArg.addEnum(currentEnum)
                currentCommand.addArgument (currentArg)
            currentClass.addCommand (currentCommand)
        proj.addClass (currentClass)
    allProjects.append (proj)

for projectName in projects:
    parseXml(XMLFILENAME_PREFIX + projectName + XMLFILENAME_SUFFIX, projectName)
    if genDebug:
        parseXml(XMLFILENAME_PREFIX + projectName + XMLDEBUGFILENAME_SUFFIX, projectName + 'Debug')
    

    

# Check all
err = ''
for proj in allProjects:
    err = err + proj.check ()
if len (err) > 0:
    ARPrint ('Your XML Files contain errors:', True)
    ARPrint (err)
    EXIT (1)

if noGen: # called with "-nogen"
    ARPrint ('Commands parsed:')
    for proj in allProjects:
        ARPrint ('Project ' + proj.name)
        ARPrint ('/*')
        for comment in proj.comments:
            ARPrint (' * ' + comment)
        ARPrint (' */')
        for cl in proj.classes:
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
                    for enum in arg.enums:
                        ARPrint ('   (typedef enum ' + enum.name + ')')
                        ARPrint ('    /* ')
                        for comment in enum.comments:
                            ARPrint ('     * ' + comment)
                        ARPrint ('     */')

    EXIT (0)


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
for proj in allProjects:
    ENAME='PROJECT'
    hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, proj.name) + ' = ' + proj.ident + ',\n')
hfile.write ('} ' + AREnumName (ID_SUBMODULE, ENAME) + ';\n')
hfile.write ('\n')
hfile.write ('\n')
for proj in allProjects:
    if proj.classes:
        ENAME=proj.name + '_CLASS'
        hfile.write ('typedef enum {\n')
        for cl in proj.classes:
            hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, cl.name) + ' = ' + cl.ident + ',\n')
        hfile.write ('} ' + AREnumName (ID_SUBMODULE, ENAME) + ';\n')
        hfile.write ('\n')
hfile.write ('\n')
hfile.write ('\n')
for proj in allProjects:
    for cl in proj.classes:
        hfile.write ('typedef enum {\n')
        ENAME=proj.name + '_' + cl.name + '_CMD'
        first = True
        for cmd in cl.cmds:
            if first:
                hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, cmd.name) + ' = 0,\n')
                first = False
            else:
                hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, cmd.name) + ',\n')
        hfile.write ('    ' + AREnumValue (ID_SUBMODULE, ENAME, 'MAX') + ',\n')
        hfile.write ('} ' + AREnumName (ID_SUBMODULE, ENAME) + ';\n')
        hfile.write ('\n')
    hfile.write ('\n')

hfile.write ('\n')
hfile.write ('#endif /* ' + COMMANDSID_DEFINE + ' */\n')

hfile.close ()

#################################
# 3RD PART :                    #
#################################
# Generate public Types H file  #
#################################

hfile = open (COMMANDSTYPES_HFILE, 'w')

hfile.write ('/**\n')
hfile.write (' * @file ' + COMMANDSTYPES_HFILE_NAME + '\n')
hfile.write (' * @brief libARCommands types header.\n')
hfile.write (' * This file contains all types declarations needed to use commands\n')
hfile.write (' * @note Autogenerated file\n')
hfile.write (' **/\n')
hfile.write ('#ifndef ' + COMMANDSTYPES_DEFINE + '\n')
hfile.write ('#define ' + COMMANDSTYPES_DEFINE + '\n')
hfile.write ('#include <inttypes.h>\n')
hfile.write ('\n')

for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            for arg in cmd.args:
                if len(arg.enums) != 0:
                    hfile.write ('// Project ' + proj.name + '\n')
                    hfile.write ('// Class ' + cl.name + '\n')
                    hfile.write ('// Command ' + cmd.name + '\n')

                    submodules=proj.name.upper() + '_' + cl.name.upper()
                    macro_name=cmd.name.upper() + '_' + arg.name.upper();
                    hfile.write ('\n/**\n')
                    hfile.write (' * @brief ' + arg.comments[0] + '\n')
                    for comm in arg.comments[1:]:
                        hfile.write (' * ' + comm + '\n')
                    hfile.write (' */\n')
                    hfile.write ('typedef enum\n')
                    hfile.write ('{\n')
                    first = True
                    for enum in arg.enums:
                        if first:
                            hfile.write ('    ' + AREnumValue(submodules, macro_name, enum.name) + ' = 0,    ///< ' + enum.comments[0] + '\n')
                            first = False
                        else:
                            hfile.write ('    ' + AREnumValue(submodules, macro_name, enum.name) + ',    ///< ' + enum.comments[0] + '\n')
                    hfile.write ('    ' + AREnumValue(submodules, macro_name, 'MAX') + '\n')
                    hfile.write ('} ' + AREnumName(submodules, macro_name) + ';\n\n')
hfile.write ('\n')
hfile.write ('#endif /* ' + COMMANDSTYPES_DEFINE + ' */\n')

hfile.close ()

#################################
# 4TH PART :                    #
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
hfile.write ('#include <' + COMMANDSTYPES_HFILE_NAME + '>\n')
hfile.write ('#include <inttypes.h>\n')
hfile.write ('\n')
if genDebug:
    hfile.write('/**\n')
    hfile.write(' * Defined only if the library includes debug commands\n')
    hfile.write(' */\n')
    hfile.write('#define ' + ARMacroName (GEN_SUBMODULE, 'HAS_DEBUG_COMMANDS') + ' (1)\n')
hfile.write ('\n')
hfile.write ('/**\n')
hfile.write (' * @brief Error codes for ' + ARFunctionName (GEN_SUBMODULE, 'GenerateClassCommand') + ' functions\n')
hfile.write (' */\n')
GEN_ERR_ENAME='ERROR'
hfile.write ('typedef enum {\n')
hfile.write ('    ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ' = 0, ///< No error occured\n')
hfile.write ('    ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ', ///< At least one of the arguments is invalid\n')
hfile.write ('    ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ', ///< The given output buffer was not large enough for the command\n')
hfile.write ('    ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR') + ', ///< Any other error\n')
hfile.write ('} ' +  AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ';\n')
hfile.write ('\n')
hfile.write ('\n')
for proj in allProjects:
    hfile.write ('// Project ' + proj.name + '\n\n')
    for cl in proj.classes:
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
                    hfile.write (' * @param _' + arg.name + ' ' + comm + '\n')
            hfile.write (' * @return Error code (see ' + AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ')\n')
            hfile.write (' */\n')
            hfile.write (AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' ' + ARFunctionName (GEN_SUBMODULE, 'Generate' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name)) + ' (uint8_t *buffer, int32_t buffLen, int32_t *cmdLen')
            for arg in cmd.args:
                hfile.write (', ' + xmlToCwithConst (proj, cl, cmd, arg) + ' _' + arg.name)
            hfile.write (');\n')
        hfile.write ('\n')
    hfile.write ('\n')

hfile.write ('\n')
hfile.write ('#endif /* ' + COMMANDSGEN_DEFINE + ' */\n')

hfile.close ()

#################################
# 5TH PART :                    #
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
for proj in allProjects:
    cfile.write ('// Project ' + proj.name + '\n\n')
    for cl in proj.classes:
        cfile.write ('// Command class ' + cl.name + '\n')
        for cmd in cl.cmds:
            cfile.write (AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' ' + ARFunctionName (GEN_SUBMODULE, 'Generate' +  ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name)) + ' (uint8_t *buffer, int32_t buffLen, int32_t *cmdLen')
            for arg in cmd.args:
                cfile.write (', ' + xmlToCwithConst (proj, cl, cmd, arg) + ' _' + arg.name)
            cfile.write (')\n')
            cfile.write ('{\n')
            cfile.write ('    int32_t currIndexInBuffer = 0;\n')
            cfile.write ('    ' + AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' retVal = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ';\n')
            cfile.write ('    if ((buffer == NULL) ||\n')
            cfile.write ('        (cmdLen == NULL))\n')
            cfile.write ('    {\n')
            cfile.write ('        return ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ';\n')
            cfile.write ('    }\n')
            hasStringArgs = False
            for arg in cmd.args:
                if arg.type == 'string':
                    hasStringArgs = True
                    break
            if hasStringArgs:
                cfile.write ('    // Test all String args (if any)\n')
                cfile.write ('    if (')
                first = True
                for arg in cmd.args:
                    if 'string' == arg.type:
                        if first:
                            first = False
                        else:
                            cfile.write ('        ')
                        cfile.write ('(_' + arg.name + ' == NULL) ||\n')
                cfile.write ('       (0))\n')
                cfile.write ('    {\n')
                cfile.write ('        return ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ';\n')
                cfile.write ('    }\n')
                cfile.write ('\n')
            cfile.write ('    // Write project header\n')
            cfile.write ('    if (retVal == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        currIndexInBuffer = ' + ARFunctionName (GEN_SUBMODULE, 'AddU8ToBuffer') + ' (buffer, ' + AREnumValue (ID_SUBMODULE, 'PROJECT', proj.name) + ', currIndexInBuffer, buffLen);\n')
            cfile.write ('        if (currIndexInBuffer == -1)\n')
            cfile.write ('        {\n')
            cfile.write ('            retVal = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
            cfile.write ('        }\n')
            cfile.write ('    }\n')
            cfile.write ('    // Write class header\n')
            cfile.write ('    if (retVal == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        currIndexInBuffer = ' + ARFunctionName (GEN_SUBMODULE, 'AddU8ToBuffer') + ' (buffer, ' + AREnumValue (ID_SUBMODULE, proj.name + '_CLASS', cl.name) + ', currIndexInBuffer, buffLen);\n')
            cfile.write ('        if (currIndexInBuffer == -1)\n')
            cfile.write ('        {\n')
            cfile.write ('            retVal = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
            cfile.write ('        }\n')
            cfile.write ('    }\n')
            cfile.write ('    // Write id header\n')
            cfile.write ('    if (retVal == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        currIndexInBuffer = ' + ARFunctionName (GEN_SUBMODULE, 'AddU16ToBuffer') + ' (buffer, ' + AREnumValue (ID_SUBMODULE, proj.name + '_' + cl.name + '_CMD', cmd.name) + ', currIndexInBuffer, buffLen);\n')
            cfile.write ('        if (currIndexInBuffer == -1)\n')
            cfile.write ('        {\n')
            cfile.write ('            retVal = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
            cfile.write ('        }\n')
            cfile.write ('    }\n')
            for arg in cmd.args:
                cfile.write ('    // Write arg _' + arg.name + '\n')
                cfile.write ('    if (retVal == ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
                cfile.write ('    {\n')
                cfile.write ('        currIndexInBuffer = ' + ARFunctionName (GEN_SUBMODULE, 'Add' + xmlToSize (proj, cl, cmd, arg) + 'ToBuffer') + ' (buffer, _' + arg.name + ', currIndexInBuffer, buffLen);\n')
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

cfile.write ('\n')
cfile.write ('// END GENERATED CODE\n')

cfile.close ()

#################################
# 6TH PART :                    #
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
hfile.write ('#include <' + COMMANDSTYPES_HFILE_NAME + '>\n')
hfile.write ('#include <inttypes.h>\n')
hfile.write ('\n')
if genDebug:
    hfile.write('/**\n')
    hfile.write(' * Defined only if the library includes debug commands\n')
    hfile.write(' */\n')
    hfile.write('#define ' + ARMacroName (GEN_SUBMODULE, 'HAS_DEBUG_COMMANDS') + ' (1)\n')
hfile.write ('\n')
hfile.write ('/**\n')
hfile.write (' * @brief Error codes for ' + ARFunctionName (DEC_SUBMODULE, 'DecodeBuffer') + ' function\n')
hfile.write (' */\n')
DEC_ERR_ENAME='ERROR'
hfile.write ('typedef enum {\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' = 0, ///< No error occured\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NO_CALLBACK') + ', ///< No error, but no callback was set (so the call had no effect)\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ', ///< The command buffer contained an unknown command\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ', ///< The command buffer did not contain enough data for the specified command\n')
hfile.write ('    ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ', ///< The string buffer was not big enough for the command description\n')
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
hfile.write ('\n/**\n')
hfile.write (' * @brief Describe a comand buffer\n')
hfile.write (' * @param buffer the command buffer to decode\n')
hfile.write (' * @param buffLen the length of the command buffer\n')
hfile.write (' * @param resString the string pointer in which the description will be stored\n')
hfile.write (' * @param stringLen the length of the string pointer\n')
hfile.write (' * @return ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' on success, any error code otherwise\n')
hfile.write (' */\n')
hfile.write (AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
hfile.write (ARFunctionName (DEC_SUBMODULE, 'DescribeBuffer') + ' (uint8_t *buffer, int32_t buffLen, char *resString, int32_t stringLen);\n')
hfile.write ('\n')
for proj in allProjects:
    hfile.write ('// Project ' + proj.name + '\n\n')
    for cl in proj.classes:
        hfile.write ('// Command class ' + cl.name + '\n')
        for cmd in cl.cmds:
            hfile.write ('\n/**\n')
            hfile.write (' * @brief callback type for the command ' + proj.name + '.' + cl.name + '.' + cmd.name + '\n')
            hfile.write (' */\n')
            hfile.write ('typedef void (*' + ARTypeName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ') (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    hfile.write (', ')
                hfile.write (xmlToC (proj, cl, cmd, arg) + ' ' + arg.name)
            if not first:
                hfile.write (', ')
            hfile.write ('void *custom);\n')
            hfile.write ('/**\n')
            hfile.write (' * @brief callback setter for the command ' + proj.name + '.' + cl.name + '.' + cmd.name + '\n')
            hfile.write (' * @param callback new callback for the command ' + proj.name + '.' + cl.name + '.' + cmd.name + '\n')
            hfile.write (' * @param custom pointer that will be passed to all calls to the callback\n')
            hfile.write (' */\n')
            hfile.write ('void ' + ARFunctionName (DEC_SUBMODULE, 'Set' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' (' + ARTypeName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' callback, void *custom);\n')
        hfile.write ('\n')
    hfile.write ('\n')

hfile.write ('#endif /* ' + COMMANDSDEC_DEFINE + ' */\n')

hfile.close ()

#################################
# 7TH PART :                    #
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
cfile.write ('#include <stdio.h>\n')
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
cfile.write ('    uint8_t *buffAddr = &buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (uint16_t);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    memcpy (&retVal, buffAddr, sizeof (uint16_t));\n')
cfile.write ('    retVal = dtohs (retVal);\n')
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
cfile.write ('    uint8_t *buffAddr = &buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (uint32_t);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    memcpy (&retVal, buffAddr, sizeof (uint32_t));\n')
cfile.write ('    retVal = dtohl (retVal);\n')
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
cfile.write ('    uint8_t *buffAddr = &buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (uint64_t);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    memcpy (&retVal, buffAddr, sizeof (uint64_t));\n')
cfile.write ('    retVal = dtohll (retVal);\n')
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
cfile.write ('    uint8_t *buffAddr = &buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (float);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    memcpy (&retVal, buffAddr, sizeof (float));\n')
cfile.write ('    retVal = dtohf (retVal);\n')
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
cfile.write ('    uint8_t *buffAddr = &buffer[*offset];\n')
cfile.write ('    int newOffset = *offset + sizeof (double);\n')
cfile.write ('    if (newOffset > capacity)\n')
cfile.write ('    {\n')
cfile.write ('        *error = 1;\n')
cfile.write ('        return retVal;\n')
cfile.write ('    }\n')
cfile.write ('    memcpy (&retVal, buffAddr, sizeof (double));\n')
cfile.write ('    retVal = dtohd (retVal);\n')
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
cfile.write ('// Write a string in a buffer\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('static int ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (const char *stringToWrite, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity = outputLen - outputOffset - 1;\n')
cfile.write ('    int len = strlen (stringToWrite);\n')
cfile.write ('    if (capacity < len)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    strncat (output, stringToWrite, len);\n')
cfile.write ('    return outputOffset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from an uint8_t arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintU8') + ' (const char *name, uint8_t arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('#if HAVE_DECL_PRIU8\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%" PRIu8, arg);\n')
cfile.write ('#else\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%u", arg);\n')
cfile.write ('#endif\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from an int8_t arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintI8') + ' (const char *name, int8_t arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('#if HAVE_DECL_PRII8\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%" PRIi8, arg);\n')
cfile.write ('#else\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%d", arg);\n')
cfile.write ('#endif\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from an uint16_t arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintU16') + ' (const char *name, uint16_t arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('#if HAVE_DECL_PRIU16\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%" PRIu16, arg);\n')
cfile.write ('#else\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%u", arg);\n')
cfile.write ('#endif\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from an int16_t arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintI16') + ' (const char *name, int16_t arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('#if HAVE_DECL_PRII16\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%" PRIi16, arg);\n')
cfile.write ('#else\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%d", arg);\n')
cfile.write ('#endif\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from an uint32_t arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintU32') + ' (const char *name, uint32_t arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('#if HAVE_DECL_PRIU32\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%" PRIu32, arg);\n')
cfile.write ('#else\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%u", arg);\n')
cfile.write ('#endif\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from an int32_t arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintI32') + ' (const char *name, int32_t arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('#if HAVE_DECL_PRII32\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%" PRIi32, arg);\n')
cfile.write ('#else\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%d", arg);\n')
cfile.write ('#endif\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from an uint64_t arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintU64') + ' (const char *name, uint64_t arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('#if HAVE_DECL_PRIU64\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%" PRIu64, arg);\n')
cfile.write ('#else\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%llu", arg);\n')
cfile.write ('#endif\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from an int64_t arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintI64') + ' (const char *name, int64_t arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('#if HAVE_DECL_PRII64\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%" PRIi64, arg);\n')
cfile.write ('#else\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%lld", arg);\n')
cfile.write ('#endif\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from float arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintFloat') + ' (const char *name, float arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%f", arg);\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from a double arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintDouble') + ' (const char *name, double arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int capacity, len;\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    capacity = outputLen - offset - 1;\n')
cfile.write ('    len = snprintf (& output [offset], capacity, "%f", arg);\n')
cfile.write ('    if (len >= capacity)\n')
cfile.write ('    {\n')
cfile.write ('        return -1;\n')
cfile.write ('    }\n')
cfile.write ('    return offset + len;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('// Write a string in a buffer from a string arg\n')
cfile.write ('// On error, return -1, else return offset in string\n')
cfile.write ('int ' + ARFunctionName (DEC_SUBMODULE, 'PrintString') + ' (const char *name, char *arg, char *output, int outputLen, int outputOffset)\n')
cfile.write ('{\n')
cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
cfile.write ('    int offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
cfile.write ('    if (offset < 0)\n')
cfile.write ('    {\n')
cfile.write ('        return offset;\n')
cfile.write ('    }\n')
cfile.write ('    offset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' (arg, output, outputLen, offset);\n')
cfile.write ('    return offset;\n')
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
for proj in allProjects:
    cfile.write ('// Project ' + proj.name + '\n\n')
    for cl in proj.classes:
        cfile.write ('// Command class ' + cl.name + '\n')
        for cmd in cl.cmds:
            cfile.write ('static ' + ARTypeName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' ' + ARGlobalName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb') + ' = NULL;\n')
            cfile.write ('static void *' + ARGlobalName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Custom') + ' = NULL;\n')
            cfile.write ('void ' + ARFunctionName (DEC_SUBMODULE, 'Set' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' (' + ARTypeName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' callback, void *custom)\n')
            cfile.write ('{\n')
            cfile.write ('    if (' + ARFunctionName (DEC_SUBMODULE, 'Init') + ' () == 1)\n')
            cfile.write ('    {\n')
            cfile.write ('        ARSAL_Mutex_Lock (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ');\n')
            cfile.write ('        ' + ARGlobalName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb') + ' = callback;\n')
            cfile.write ('        ' + ARGlobalName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Custom') + ' = custom;\n')
            cfile.write ('        ARSAL_Mutex_Unlock (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ');\n')
            cfile.write ('    }\n')
            cfile.write ('}\n')
        cfile.write ('\n')
    cfile.write ('\n')

cfile.write ('// DECODER ENTRY POINT\n')
cfile.write (AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
cfile.write (ARFunctionName (DEC_SUBMODULE, 'DecodeBuffer') + ' (uint8_t *buffer, int32_t buffLen)\n')
cfile.write ('{\n')
cfile.write ('    ' + AREnumName (ID_SUBMODULE, 'PROJECT') + ' commandProject = -1;\n')
cfile.write ('    int commandClass = -1;\n')
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
cfile.write ('        commandProject = ' + ARFunctionName (DEC_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
cfile.write ('        if (error == 1)\n')
cfile.write ('            retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
cfile.write ('    {\n')
cfile.write ('        commandClass = ' + ARFunctionName (DEC_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
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
cfile.write ('        switch (commandProject)\n')
cfile.write ('        {\n')
for proj in allProjects:
    cfile.write ('        case ' + AREnumValue (ID_SUBMODULE, 'PROJECT', proj.name) + ':\n')
    cfile.write ('        {\n')
    cfile.write ('            switch (commandClass)\n')
    cfile.write ('            {\n')
    for cl in proj.classes:
        cfile.write ('            case ' + AREnumValue (ID_SUBMODULE, proj.name + '_CLASS', cl.name) + ':\n')
        cfile.write ('            {\n')
        cfile.write ('                switch (commandId)\n')
        cfile.write ('                {\n')
        for cmd in cl.cmds:
            cfile.write ('                case ' + AREnumValue (ID_SUBMODULE, proj.name + '_' + cl.name + '_CMD', cmd.name) + ':\n')
            cfile.write ('                {\n')
            CBNAME = ARGlobalName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb')
            CBCUSTOMNAME = ARGlobalName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Custom')
            cfile.write ('                    ARSAL_Mutex_Lock (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ');\n')
            cfile.write ('                    if (' + CBNAME + ' != NULL)\n')
            cfile.write ('                    {\n')
            for arg in cmd.args:
                if 'string' == arg.type:
                    cfile.write ('                        ' + xmlToC (proj, cl, cmd, arg) + ' _' + arg.name + ' = NULL;\n')
                else:
                    cfile.write ('                        ' + xmlToC (proj, cl, cmd, arg) + ' _' + arg.name + ';\n')
            for arg in cmd.args:
                cfile.write ('                        if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                cfile.write ('                        {\n')
                cfile.write ('                            _' + arg.name + ' = ' + xmlToReader (proj, cl, cmd, arg) + ' (buffer, buffLen, &offset, &error);\n')
                cfile.write ('                            if (error == 1)\n')
                cfile.write ('                                retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                cfile.write ('                        }\n')
            cfile.write ('                        if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('                        {\n')
            cfile.write ('                            ' + CBNAME + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    cfile.write (', ')
                cfile.write ('_' + arg.name)
            if not first:
                cfile.write (', ')
            cfile.write (CBCUSTOMNAME + ');\n')
            cfile.write ('                        }\n')
            cfile.write ('                    }\n')
            cfile.write ('                    else\n')
            cfile.write ('                    {\n')
            cfile.write ('                        retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NO_CALLBACK') + ';\n')
            cfile.write ('                    }\n')
            cfile.write ('                    ARSAL_Mutex_Unlock (&' + ARGlobalName (DEC_SUBMODULE, 'mutex') + ');\n')
            cfile.write ('                }\n')
            cfile.write ('                break; /* ' + AREnumValue (ID_SUBMODULE, proj.name + '_' + cl.name + '_CMD', cmd.name) + ' */\n')
        cfile.write ('                default:\n')
        cfile.write ('                    retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
        cfile.write ('                    break;\n')
        cfile.write ('                }\n')
        cfile.write ('            }\n')
        cfile.write ('            break; /* ' + AREnumValue (ID_SUBMODULE, proj.name + '_CLASS', cl.name) + ' */\n')
    cfile.write ('            default:\n')
    cfile.write ('                retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
    cfile.write ('                break;\n')
    cfile.write ('            }\n')
    cfile.write ('        }\n')
    cfile.write ('        break; /* ' + AREnumValue (ID_SUBMODULE, 'PROJECT', proj.name) + ' */\n')

cfile.write ('        default:\n')
cfile.write ('            retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
cfile.write ('            break;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('    return retVal;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write (AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
cfile.write (ARFunctionName (DEC_SUBMODULE, 'DescribeBuffer') + ' (uint8_t *buffer, int32_t buffLen, char *resString, int32_t stringLen)\n')
cfile.write ('{\n')
cfile.write ('    ' + AREnumName (ID_SUBMODULE, 'PROJECT') + ' commandProject = -1;\n')
cfile.write ('    int commandClass = -1;\n')
cfile.write ('    int commandId = -1;\n')
cfile.write ('    int32_t offset = 0;\n')
cfile.write ('    int32_t error = 0;\n')
cfile.write ('    int stroffset = 0;\n')
cfile.write ('    ' + AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + ' retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
cfile.write ('    if (NULL == buffer || NULL == resString)\n')
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
cfile.write ('        commandProject = ' + ARFunctionName (DEC_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
cfile.write ('        if (error == 1)\n')
cfile.write ('            retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
cfile.write ('    {\n')
cfile.write ('        commandClass = ' + ARFunctionName (DEC_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
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
cfile.write ('    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' && stringLen > 0)\n')
cfile.write ('    {\n')
cfile.write ('        resString[0] = \'\\0\';\n')
cfile.write ('    }\n')
cfile.write ('\n')
cfile.write ('    if (retVal == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
cfile.write ('    {\n')
cfile.write ('        switch (commandProject)\n')
cfile.write ('        {\n')
for proj in allProjects:
    cfile.write ('        case ' + AREnumValue (ID_SUBMODULE, 'PROJECT', proj.name) + ':\n')
    cfile.write ('        {\n')
    cfile.write ('            switch (commandClass)\n')
    cfile.write ('            {\n')
    for cl in proj.classes:
        cfile.write ('            case ' + AREnumValue (ID_SUBMODULE, proj.name + '_CLASS', cl.name) + ':\n')
        cfile.write ('            {\n')
        cfile.write ('                switch (commandId)\n')
        cfile.write ('                {\n')
        for cmd in cl.cmds:
            cfile.write ('                case ' + AREnumValue (ID_SUBMODULE, proj.name + '_' + cl.name + '_CMD', cmd.name) + ':\n')
            cfile.write ('                {\n')
            cfile.write ('                    stroffset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' ("' + proj.name + '.' + cl.name + '.' + cmd.name + ':", resString, stringLen, stroffset) ;\n')
            for arg in cmd.args:
                cfile.write ('                    if (stroffset > 0)\n')
                cfile.write ('                    {\n')
                cfile.write ('                        ' + xmlToC (proj, cl, cmd, arg) + ' arg = ' + xmlToReader (proj, cl, cmd, arg) + ' (buffer, buffLen, &offset, &error);\n')
                cfile.write ('                        if (error == 0)\n')
                cfile.write ('                        {\n')
                cfile.write ('                            stroffset = ' + xmlToPrinter (proj, cl, cmd, arg) + ' (" | ' + arg.name + ' -> ", arg, resString, stringLen, stroffset);\n')
                cfile.write ('                        }\n')
                cfile.write ('                    }\n')
            cfile.write ('                    if (stroffset < 0)\n')
            cfile.write ('                    {\n')
            cfile.write ('                        retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
            cfile.write ('                    }\n')
            cfile.write ('                }\n')
            cfile.write ('                break; /* ' + AREnumValue (ID_SUBMODULE, proj.name + '_' + cl.name + '_CMD', cmd.name) + ' */\n')
        cfile.write ('                default:\n')
        cfile.write ('                    stroffset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' ("' + proj.name + '.' + cl.name + '.UNKNOWN -> Unknown command", resString, stringLen, stroffset);\n')
        cfile.write ('                    retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
        cfile.write ('                    break;\n')
        cfile.write ('                }\n')
        cfile.write ('            }\n')
        cfile.write ('            break; /* ' + AREnumValue (ID_SUBMODULE, proj.name + '_CLASS', cl.name) + ' */\n')
    cfile.write ('            default:\n')
    cfile.write ('                stroffset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' ("' + proj.name + '.UNKNOWN -> Unknown command", resString, stringLen, stroffset);\n')
    cfile.write ('                retVal = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
    cfile.write ('                break;\n')
    cfile.write ('            }\n')
    cfile.write ('        }\n')
    cfile.write ('        break; /* ' + AREnumValue (ID_SUBMODULE, 'PROJECT', proj.name) + ' */\n')

cfile.write ('        default:\n')
cfile.write ('            stroffset = ' + ARFunctionName (DEC_SUBMODULE, 'WriteString') + ' ("UNKNOWN -> Unknown command", resString, stringLen, stroffset);\n')
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
# 8TH PART :                    #
#################################
# Generate C Testbench          #
#################################

def TB_CALL_VARNAME (proj, cls, cmd):
    return proj.name + ARCapitalize (cls.name) + ARCapitalize (cmd.name) + 'ShouldBeCalled'

def TB_CREATE_VARNAME (proj, cls, cmd):
    return 'int ' + TB_CALL_VARNAME (proj, cls, cmd) + ' = 0;'

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
cfile.write ('char describeBuffer [1024] = {0};\n')
cfile.write ('\n')
for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            cfile.write (TB_CREATE_VARNAME (proj, cl, cmd) + '\n')
cfile.write ('\n')
for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            cfile.write ('void ' + ARFunctionName (TB_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb') + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    cfile.write (', ')
                cfile.write (xmlToC (proj, cl, cmd, arg) + ' ' + arg.name)
            if not first:
                cfile.write (', ')
            cfile.write ('void *custom)\n')
            cfile.write ('{\n')
            cfile.write ('    ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "Callback for command ' + proj.name + '.' + cl.name + '.' + cmd.name + ' --> Custom PTR = %p", custom);\n')
            for arg in cmd.args:
                cfile.write ('    ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "' + arg.name + ' value : <' + xmlToPrintf (proj, cl, cmd, arg) + '>", ' + arg.name + ');\n')
                if "string" == arg.type:
                    cfile.write ('    if (strcmp (' + xmlToSample (proj, cl, cmd, arg) + ', ' + arg.name + ') != 0)\n')
                else:
                    cfile.write ('    if (' + arg.name + ' != ' + xmlToSample (proj, cl, cmd, arg) + ')\n')
                cfile.write ('    {\n')
                if "string" == arg.type:
                    cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "BAD ARG VALUE !!! --> Expected <%s>", ' + xmlToSample (proj, cl, cmd, arg) + ');\n')
                else:
                    cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "BAD ARG VALUE !!! --> Expected <' + xmlToSample (proj, cl, cmd, arg) + '>");\n')
                cfile.write ('        errcount++ ;\n')
                cfile.write ('    }\n')
            cfile.write ('    if (' + TB_CALL_VARNAME (proj, cl, cmd) + ' == 0)\n')
            cfile.write ('    {\n')
            cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "BAD CALLBACK !!! --> This callback should not have been called for this command");\n')
            cfile.write ('        errcount++ ;\n')
            cfile.write ('    }\n')
            cfile.write ('}\n')
        cfile.write ('\n')
    cfile.write ('\n')

cfile.write ('\n')
cfile.write ('void ' + ARFunctionName (TB_SUBMODULE, 'initCb') + ' (void)\n')
cfile.write ('{\n')
cfile.write ('    intptr_t cbCustom = 0;\n')
for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            cfile.write ('    ' + ARFunctionName (DEC_SUBMODULE, 'Set' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' ((' + ARTypeName (DEC_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ') ' + ARFunctionName (TB_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb') + ', (void *)cbCustom++ );\n')
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
for proj in allProjects:
    cfile.write ('    // Project ' + proj.name + '\n')
    for cl in proj.classes:
        cfile.write ('    // Command class ' + cl.name + '\n')
        for cmd in cl.cmds:
            cfile.write ('    res = ' + ARFunctionName (GEN_SUBMODULE, 'Generate' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name)) + ' (buffer, buffSize, &resSize')
            for arg in cmd.args:
                cfile.write (', ' + xmlToSample (proj, cl, cmd, arg))
            cfile.write (');\n')
            cfile.write ('    if (res != ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while generating command ' + ARCapitalize (proj.name) + '.' + ARCapitalize (cl.name) + '.' + ARCapitalize (cmd.name) + '\\n\\n");\n')
            cfile.write ('        errcount++ ;\n')
            cfile.write ('    }\n')
            cfile.write ('    else\n')
            cfile.write ('    {\n')
            cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "Generating command ' + ARCapitalize (proj.name) + '.' + ARCapitalize (cl.name) + '.' + ARCapitalize (cmd.name) + ' succeded");\n')
            cfile.write ('        ' + AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + ' err;\n')
            cfile.write ('        err = ' + ARFunctionName (DEC_SUBMODULE, 'DescribeBuffer') + ' (buffer, resSize, describeBuffer, 1024);\n')
            cfile.write ('        if (err != ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('        {\n')
            cfile.write ('            ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while describing buffer: %d", err);\n')
            cfile.write ('            errcount++ ;\n')
            cfile.write ('        }\n')
            cfile.write ('        else\n')
            cfile.write ('        {\n')
            cfile.write ('            ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "%s", describeBuffer);\n')
            cfile.write ('        }\n')
            cfile.write ('        ' + TB_CALL_VARNAME (proj, cl, cmd) + ' = 1;\n')
            cfile.write ('        err = ' + ARFunctionName (DEC_SUBMODULE, 'DecodeBuffer') + ' (buffer, resSize);\n')
            cfile.write ('        ' + TB_CALL_VARNAME (proj, cl, cmd) + ' = 0;\n')
            cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "Decode return value : %d\\n\\n", err);\n')
            cfile.write ('        if (err != ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('        {\n')
            cfile.write ('            errcount++ ;\n')
            cfile.write ('        }\n')
            cfile.write ('    }\n')
            cfile.write ('\n')
        cfile.write ('\n')
    cfile.write ('\n')

cfile.write ('    if (errcount == 0)\n')
cfile.write ('    {\n')
cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "No errors !");\n')
cfile.write ('    }\n')
cfile.write ('    else\n')
cfile.write ('    {\n')
cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "%d errors detected during autoTest", errcount);\n')
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
cfile.write ('#include "' + COM_TB_DIR + TB_HFILE_NAME + '"\n')
cfile.write ('\n')
cfile.write ('int main (int argc, char *argv[])\n')
cfile.write ('{\n')
cfile.write ('    return ' + ARFunctionName (TB_SUBMODULE, 'autoTest') + ' ();\n')
cfile.write ('}\n')

cfile.close ()

#################################
# 9TH PART :                    #
#################################
# Generate JNI C/Java code      #
#################################

def interfaceName (proj, cls, cmd):
    return JNIClassName + ARCapitalize (proj.name) + ARCapitalize (cls.name) + ARCapitalize (cmd.name) + 'Listener'
def interfaceVar (proj, cls, cmd):
    return '_' + interfaceName (proj,cls,cmd)
def javaCbName (proj, cls, cmd):
    return 'on' + ARCapitalize (proj.name) + ARCapitalize (cls.name) + ARCapitalize (cmd.name) + 'Update'

for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            jfile = open (JNIJ_OUT_DIR + interfaceName (proj,cl,cmd) + '.java', 'w')
            jfile.write ('package ' + JNI_PACKAGE_NAME + ';\n')
            jfile.write ('\n')
            jfile.write ('/**\n')
            jfile.write (' * Interface for the command <code>' + ARCapitalize (cmd.name) + '</code> of class <code>' + ARCapitalize (cl.name) + '</code> in project <code>' + ARCapitalize (proj.name) + '</code> listener\n')
            jfile.write (' * @author Parrot (c) 2013\n')
            jfile.write (' * @version ' + LIB_VERSION + '\n')
            jfile.write (' */\n')
            jfile.write ('public interface ' + interfaceName (proj,cl,cmd) + ' {\n')
            jfile.write ('\n')
            jfile.write ('    /**\n')
            jfile.write ('     * Called when a command <code>' + ARCapitalize (cmd.name) + '</code> of class <code>' + ARCapitalize (cl.name) + '</code> in project <code>' + ARCapitalize (proj.name) + '</code> is decoded\n')
            for arg in cmd.args:
                for comm in arg.comments:
                    jfile.write ('     * @param _' + arg.name + ' ' + comm + '\n')
            jfile.write ('     */\n')
            jfile.write ('    void ' + javaCbName (proj,cl,cmd) + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    jfile.write (', ')
                jfile.write (xmlToJava (proj, cl, cmd, arg) + ' ' + arg.name)
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
jfile.write ('public class ' + JNIClassName + ' extends ARNativeData {\n')
jfile.write ('\n')
jfile.write ('    public static final boolean ' + ARMacroName(JNIClassName, 'HAS_DEBUG_COMMANDS') + ' = ')
if genDebug:
    jfile.write ('true;\n')
else:
    jfile.write ('false;\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Creates a new, empty ' + JNIClassName + ' with the default size.<br>\n')
jfile.write ('     * This is a typical constructor for app-generated ' + JNIClassName + '.<br>\n')
jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
jfile.write ('     * object after it was disposed.\n')
jfile.write ('     */\n')
jfile.write ('    public ' + JNIClassName + ' () {\n')
jfile.write ('        super ();\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Creates a new, empty ' + JNIClassName + ' with an user-specified size.<br>\n')
jfile.write ('     * This is a typical constructor for app-generated ' + JNIClassName + '.<br>\n')
jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
jfile.write ('     * object after it was disposed.\n')
jfile.write ('     * @param capacity user specified capacity of the command buffer\n')
jfile.write ('     */\n')
jfile.write ('    public ' + JNIClassName + ' (int capacity) {\n')
jfile.write ('        super (capacity);\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Creates a new ' + JNIClassName + ' from another ARNativeData instance.<br>\n')
jfile.write ('     * This is a typical constructor for network-generated ' + JNIClassName + '.<br>\n')
jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
jfile.write ('     * object after it was disposed.\n')
jfile.write ('     * @param oldData ARNativeData which contains original data\n')
jfile.write ('     */\n')
jfile.write ('    public ' + JNIClassName + ' (ARNativeData oldData) {\n')
jfile.write ('        super (oldData);\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Creates a new ' + JNIClassName + ' from another ARNativeData, with a given minimum capacity.<br>\n')
jfile.write ('     * This is a typical constructor for network-generated ' + JNIClassName + '.<br>\n')
jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
jfile.write ('     * object after it was disposed.\n')
jfile.write ('     * @param oldData ARNativeData which contains original data\n')
jfile.write ('     * @param capacity Minimum capacity of this object\n')
jfile.write ('     */\n')
jfile.write ('    public ' + JNIClassName + ' (ARNativeData oldData, int capacity) {\n')
jfile.write ('        super (oldData, capacity);\n')
jfile.write ('    }\n')
jfile.write ('\n')
jfile.write ('    /**\n')
jfile.write ('     * Describe a ' + JNIClassName + '.<br>\n')
jfile.write ('     * @return A String describing the ' + JNIClassName + ', with arguments values included\n')
jfile.write ('     */\n')
jfile.write ('    public String toString () {\n')
jfile.write ('        return nativeToString (pointer, used);\n')
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
jfile.write ('        if (!valid) {\n')
jfile.write ('            return err;\n')
jfile.write ('        }\n')
jfile.write ('        int errInt = nativeDecode (pointer, used);\n')
jfile.write ('        if (' + ARJavaEnumType (DEC_SUBMODULE, DEC_ERR_ENAME) + '.getFromValue (errInt) != null) {\n')
jfile.write ('            err = ' + ARJavaEnumType (DEC_SUBMODULE, DEC_ERR_ENAME) + '.getFromValue (errInt);\n')
jfile.write ('        }\n')
jfile.write ('        return err;\n')
jfile.write ('    }\n')
jfile.write ('\n')
for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            jfile.write ('    /**\n')
            jfile.write ('     * Set an ' + JNIClassName + ' to hold the command <code>' + ARCapitalize (cmd.name) + '</code> of class <code>' + ARCapitalize (cl.name) + '</code> in project <code>' + ARCapitalize (proj.name) + '</code><br>\n')
            jfile.write ('     * <br>\n')
            jfile.write ('     * Project ' + ARCapitalize (proj.name) + ' description:<br>\n')
            for comm in proj.comments:
                jfile.write ('     * ' + comm + '<br>\n')
            jfile.write ('     * <br>\n')
            jfile.write ('     * Class ' + ARCapitalize (cl.name) + ' description:<br>\n')
            for comm in cl.comments:
                jfile.write ('     * ' + comm + '<br>\n')
            jfile.write ('     * <br>\n')
            jfile.write ('     * Command ' + ARCapitalize (cmd.name) + ' description:<br>\n')
            for comm in cmd.comments:
                jfile.write ('     * ' + comm + '<br>\n')
            jfile.write ('     * <br>\n')
            jfile.write ('     * This function reuses the current ' + JNIClassName + ', replacing its content with a\n')
            jfile.write ('     * new command created from the current params\n')
            for arg in cmd.args:
                for comm in arg.comments:
                    jfile.write ('     * @param _' + arg.name + ' ' + comm + '\n')
            jfile.write ('     * @return An ' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + ' error code.\n')
            jfile.write ('     */\n')
            jfile.write ('    public ' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + ' set' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    jfile.write (', ')
                jfile.write (xmlToJava (proj, cl, cmd, arg) + ' ' + arg.name)
            jfile.write (') {\n')
            jfile.write ('        ' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + ' err = ' + ARJavaEnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR') + ';\n')
            jfile.write ('        if (!valid) {\n')
            jfile.write ('            return err;\n')
            jfile.write ('        }\n')
            jfile.write ('        int errInt = nativeSet' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + ' (pointer, capacity')
            for arg in cmd.args:
                jfile.write (', ' + arg.name)
            jfile.write (');\n')
            jfile.write ('        if (' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + '.getFromValue (errInt) != null) {\n')
            jfile.write ('            err = ' + ARJavaEnumType (GEN_SUBMODULE, GEN_ERR_ENAME) + '.getFromValue (errInt);\n')
            jfile.write ('        }\n')
            jfile.write ('        return err;\n')
            jfile.write ('    }\n')
            jfile.write ('\n')


for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            jfile.write ('    private static ' + interfaceName (proj,cl,cmd) + ' ' + interfaceVar (proj,cl,cmd) + ' = null;\n')
            jfile.write ('\n')
            jfile.write ('    /**\n')
            jfile.write ('     * Set the listener for the command <code>' + ARCapitalize (cmd.name) + '</code> of class <code>' + ARCapitalize (cl.name) + '</code> in project <code>' + ARCapitalize (proj.name) + '</code><br>\n')
            jfile.write ('     * Listeners are static to the class, and are not to be set on every object\n')
            jfile.write ('     * @param ' + interfaceVar (proj,cl,cmd) + '_PARAM New listener for the command\n')
            jfile.write ('     */\n')
            jfile.write ('    public static void set' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Listener (' + interfaceName (proj,cl,cmd) + ' ' + interfaceVar (proj,cl,cmd) + '_PARAM) {\n')
            jfile.write ('        ' + interfaceVar (proj,cl,cmd) + ' = ' + interfaceVar (proj,cl,cmd) + '_PARAM;\n')
            jfile.write ('    }\n')
            jfile.write ('\n')
        jfile.write ('\n')
    jfile.write ('\n')
jfile.write ('\n')
jfile.write ('    private native String  nativeToString (long jpdata, int jdataSize);\n')
jfile.write ('    private native int     nativeDecode (long jpdata, int jdataSize);\n')
jfile.write ('\n')
for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            jfile.write ('    private native int     nativeSet' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + ' (long pdata, int dataTotalLength')
            for arg in cmd.args:
                jfile.write (', ' + xmlToJava (proj, cl, cmd, arg) + ' ' + arg.name)
            jfile.write (');\n')
        jfile.write ('\n')
    jfile.write ('\n')
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
cfile.write ('#define TOSTRING_STRING_SIZE (1024)\n')
cfile.write ('\n')
cfile.write ('static jfieldID g_dataSize_id = 0;\n')
cfile.write ('static JavaVM *g_vm = NULL;\n')
cfile.write ('\n')
cfile.write ('JNIEXPORT jstring JNICALL\n')
cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeToString (' + JNI_FIRST_ARGS + ', jlong jpdata, jint jdataSize)\n')
cfile.write ('{\n')
cfile.write ('    jstring ret = NULL;\n')
cfile.write ('    ' + AREnumName (DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
cfile.write ('    char *cstr = calloc (TOSTRING_STRING_SIZE, 1);\n')
cfile.write ('    if (cstr == NULL)\n')
cfile.write ('    {\n')
cfile.write ('        return ret;\n')
cfile.write ('    }\n')
cfile.write ('    err = ' + ARFunctionName (DEC_SUBMODULE, 'DescribeBuffer') + ' ((uint8_t *)(intptr_t)jpdata, jdataSize, cstr, TOSTRING_STRING_SIZE);\n')
cfile.write ('    if (err == ' + AREnumValue (DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
cfile.write ('    {\n')
cfile.write ('        ret = (*env)->NewStringUTF(env, cstr);\n')
cfile.write ('    }\n')
cfile.write ('    free (cstr);\n')
cfile.write ('    return ret;\n')
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
for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            cfile.write ('JNIEXPORT jint JNICALL\n')
            cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeSet' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + ' (' + JNI_FIRST_ARGS + ', jlong c_pdata, jint dataLen')
            for arg in cmd.args:
                cfile.write (', ' + xmlToJni (proj, cl, cmd, arg) + ' ' + arg.name)
            cfile.write (')\n')
            cfile.write ('{\n')
            cfile.write ('    int32_t c_dataSize = 0;\n')
            cfile.write ('    ' + AREnumName (GEN_SUBMODULE, GEN_ERR_ENAME) + ' err = ' + AREnumValue (GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR') + ';\n');
            cfile.write ('    if (g_dataSize_id == 0)\n')
            cfile.write ('    {\n')
            cfile.write ('        jclass clz = (*env)->GetObjectClass (env, thizz);\n')
            cfile.write ('        if (clz != 0)\n')
            cfile.write ('        {\n')
            cfile.write ('            g_dataSize_id = (*env)->GetFieldID (env, clz, "used", "I");\n')
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
            cfile.write ('    err = ' + ARFunctionName (GEN_SUBMODULE, 'Generate' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name)) + ' ((uint8_t *) (intptr_t) c_pdata, dataLen, &c_dataSize')
            for arg in cmd.args:
                if 'string' == arg.type:
                    cfile.write (', c_' + arg.name)
                else:
                    cfile.write (', (' + xmlToC (proj, cl, cmd, arg) + ')' + arg.name)
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
        cfile.write ('\n')
    cfile.write ('\n')

def cCallbackName (proj,cls,cmd):
    return ARFunctionName (JNI_SUBMODULE, ARCapitalize (proj.name) + ARCapitalize (cls.name) + ARCapitalize (cmd.name) + 'nativeCb')

for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            cfile.write ('void ' + cCallbackName (proj,cl,cmd) + ' (')
            for arg in cmd.args:
                cfile.write (xmlToC (proj, cl, cmd, arg) + ' ' + arg.name + ', ')
            cfile.write ('void *custom)\n')
            cfile.write ('{\n')
            cfile.write ('    jclass clazz = (jclass)custom;\n')
            cfile.write ('    jint res;\n')
            cfile.write ('    JNIEnv *env = NULL;\n')
            cfile.write ('    res = (*g_vm)->GetEnv (g_vm, (void **)&env, JNI_VERSION_1_6);\n')
            cfile.write ('    if (res < 0) { return; }\n')
            cfile.write ('    jfieldID delegate_fid = (*env)->GetStaticFieldID (env, clazz, "' + interfaceVar (proj,cl,cmd) + '", "L' + JNI_PACKAGE_NAME.replace ('.', '/') + '/' + interfaceName (proj,cl,cmd) + ';");\n')
            cfile.write ('    jobject delegate = (*env)->GetStaticObjectField (env, clazz, delegate_fid);\n')
            cfile.write ('    if (delegate == NULL) { return; }\n')
            cfile.write ('\n')
            cfile.write ('    jclass d_clazz = (*env)->GetObjectClass (env, delegate);\n')
            cfile.write ('    jmethodID d_methodid = (*env)->GetMethodID (env, d_clazz, "' + javaCbName (proj,cl,cmd) + '", "(')
            for arg in cmd.args:
                cfile.write ('' + xmlToJavaSig (proj, cl, cmd, arg))
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
        cfile.write ('\n')
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
for proj in allProjects:
    for cl in proj.classes:
        for cmd in cl.cmds:
            cfile.write ('    ' + ARFunctionName (DEC_SUBMODULE, 'Set' + ARCapitalize (proj.name) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback') + ' (' + cCallbackName (proj,cl,cmd) + ', (void *)g_class);\n')
        cfile.write ('\n')
    cfile.write ('\n')
cfile.write ('\n')
cfile.write ('    return JNI_VERSION_1_6;\n')
cfile.write ('}\n')
cfile.write ('/* END OF GENERAED CODE */\n')

cfile.close ()
