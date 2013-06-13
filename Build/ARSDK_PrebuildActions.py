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

configureAcFile = open (MYDIR + '/configure.ac', 'rb')
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
# Generic infos about the lib   #
#################################

# Directories
SRC_DIR      = MYDIR + '/../Sources/'
INC_DIR      = MYDIR + '/../Includes/' + LIB_NAME + '/'
BUILD_DIR    = MYDIR + '/'
JNI_C_DIR    = MYDIR + '/../JNI/c/'
JNI_JAVA_DIR = MYDIR + '/../JNI/java/'

# Create dir if neededif not os.path.exists (SRC_DIR):
if not os.path.exists (SRC_DIR):
    os.makedirs (SRC_DIR)
if not os.path.exists (INC_DIR):
    os.makedirs (INC_DIR)
if not os.path.exists (JNI_C_DIR):
    os.makedirs (JNI_C_DIR)
if not os.path.exists (JNI_JAVA_DIR):
    os.makedirs (JNI_JAVA_DIR)

# Java/JNI package
JAVA_PACKAGE='com.parrot.arsdk.' + LIB_MODULE.lower ()

# Generated file disclaimer
GENERATED_FILE_DISCLAIMER='/*\n * GENERATED FILE\n *  Do not modify this file, it will be erased during the next configure run\n */\n'

#################################
# Internal functions            #
#################################

def ARPrint (msg, noNewLine=0):
    sys.stdout.write (msg)
    if 0 == noNewLine:
        sys.stdout.write ('\n')
    else:
        sys.stdout.write (' ')

def ARCapitalize (arstr):
    return arstr[0].upper () + arstr[1:]

def ARStringIsInteger (arstr):
    try:
        int (arstr)
        return True
    except ValueError:
        return False

#################################
# Add Prebuild processing here  #
#################################

#################################
# Generate JAVA Enums from C    #
#  Enums                        #
#################################

class AREnumType:
    def __init__(self):
        self.name = ''
        self.entries = []
        self.hasToString = False
        self.toStringFileName = ''
        self.toStringPrototype = ''
    def addEntry(self, entry):
        self.entries.append (entry)
    def setName(self, name):
        self.name = name
    def setToStringFileName(self, fname, prototype):
        self.hasToString = True
        self.toStringFileName = fname
        self.toStringPrototype = prototype

class AREnumEntry:
    "Represent an enum entry in C"
    def __init__(self, name, value, comment):
        self.name = name
        self.value = value
        self.comment = comment

def readEnumEntriesFromFile (filename):
    ALL_LINES = [line.strip () for line in open (filename)]
    DATA_LINES = []
    # Strip empty lines
    for line in ALL_LINES:
        if line:
            DATA_LINES.append (line)
    # Loop over lines to find enums
    currentEnumType = AREnumType ()
    allEnums = []
    foundEnum = False
    previousValue = -1 # So next value is zero
    for line in DATA_LINES:
        if not foundEnum:
            if line.startswith ('typedef enum'):
                foundEnum = True
                previousValue = -1
            elif 'ToString' in line:
                _, _, enumName = line.partition ('(')
                enumName, _, _ = enumName.partition (' ')
                for enum in allEnums:
                    if enum.name == enumName:
                        cFileName = filename.replace ('.h', '.c')
                        cFileName = cFileName.replace (INC_DIR, SRC_DIR)
                        prototype = line.rstrip (';')
                        enum.setToStringFileName (cFileName, prototype)
                        break
        else:
            if line.startswith ('}'):
                foundEnum = False
                enumName, _, _ = line.partition (';')
                _, _, enumName = enumName.partition ('}')
                enumName = enumName.strip ()
                currentEnumType.setName (enumName)
                allEnums.append (currentEnumType)
                currentEnumType = AREnumType ()
            elif not line.startswith ('{'):
                # Get name
                name, _, _ = line.partition (' ')
                name = name.strip (',')
                # Get value
                _, _, value = line.partition ('=')
                value, _, _ = value.partition (',')
                value = value.strip ()
                if not value:
                    value = str (previousValue + 1)
                while not ARStringIsInteger (value):
                    for prevEntry in currentEnumType.entries:
                        if prevEntry.name == value:
                            value = prevEntry.value
                            break
                    break
                previousValue = int (value)
                # Get comment
                _, _, comment = line.partition ('/**<')
                comment, _, _ = comment.partition ('*/')
                comment = comment.strip ()
                # If the comment is not in /**< */ format, try ///< format
                if comment == '':
                    _, _, comment = line.partition ('///<')
                    comment = comment.strip ()
                entry = AREnumEntry (name, value, comment)
                currentEnumType.addEntry (entry)
    return allEnums

def writeEnumToJavaFile (enumType):
    CLASS_NAME = enumType.name.lstrip ('e') + '_ENUM'
    JFILE_NAME = JNI_JAVA_DIR + CLASS_NAME + '.java'
    jfile      = open (JFILE_NAME, 'w')

    jfile.write (GENERATED_FILE_DISCLAIMER)
    jfile.write ('\n')
    jfile.write ('package ' + JAVA_PACKAGE + ';\n')
    jfile.write ('\n')
    jfile.write ('import java.util.HashMap;\n')
    jfile.write ('\n')
    jfile.write ('/**\n')
    jfile.write (' * Java copy of the ' + enumType.name + ' enum\n')
    jfile.write (' */\n')
    jfile.write ('public enum ' + CLASS_NAME + ' {\n')
    for entry in enumType.entries[:-1]:
        jfile.write ('    ' + entry.name + ' (' + entry.value + '), /**< ' + entry.comment + ' */\n')
    entry = enumType.entries[-1]
    jfile.write ('    ' + entry.name + ' (' + entry.value + '); /**< ' + entry.comment + ' */\n')
    jfile.write ('\n')
    jfile.write ('    private final int value;\n')
    jfile.write ('    static HashMap<Integer, ' + CLASS_NAME + '> valuesList;\n')
    jfile.write ('\n')
    jfile.write ('    ' + CLASS_NAME + ' (int value) {\n')
    jfile.write ('        this.value = value;\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Gets the int value of the enum\n')
    jfile.write ('     * @return int value of the enum\n')
    jfile.write ('     */\n')
    jfile.write ('    public int getValue () {\n')
    jfile.write ('        return value;\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Gets the ' + CLASS_NAME + ' instance from a C enum value\n')
    jfile.write ('     * @param value C value of the enum\n')
    jfile.write ('     * @return The ' + CLASS_NAME + ' instance, or null if the C enum value was not valid\n')
    jfile.write ('     */\n')
    jfile.write ('    public static ' + CLASS_NAME + ' getFromValue (int value) {\n')
    jfile.write ('        if (null == valuesList) {\n')
    jfile.write ('            ' + CLASS_NAME + ' [] valuesArray = ' + CLASS_NAME + '.values ();\n')
    jfile.write ('            valuesList = new HashMap<Integer, ' + CLASS_NAME + '> (valuesArray.length);\n')
    jfile.write ('            for (' + CLASS_NAME + ' entry : valuesArray) {\n')
    jfile.write ('                valuesList.put (entry.getValue (), entry);\n')
    jfile.write ('            }\n')
    jfile.write ('        }\n')
    jfile.write ('        return valuesList.get (value);\n')
    jfile.write ('    }\n')
    jfile.write ('}\n')
    jfile.close ()

def writeToStringFunction (enumType):
    if not enumType.hasToString:
        return
    CNAME = os.path.basename (enumType.toStringFileName)
    HNAME = LIB_NAME + '/' + CNAME.replace ('.c', '.h')
    VARNAME, _, _ = enumType.toStringPrototype.partition (')')
    _, _, VARNAME = VARNAME.partition (enumType.name + ' ')

    cfile = open (enumType.toStringFileName, 'w')

    cfile.write (GENERATED_FILE_DISCLAIMER)
    cfile.write ('\n')
    cfile.write ('/**\n')
    cfile.write (' * @file ' + CNAME + '\n')
    cfile.write (' * @brief ToString function for ' + enumType.name + ' enum\n')
    cfile.write (' */\n')
    cfile.write ('\n')
    cfile.write ('#include <' + HNAME + '>\n')
    cfile.write ('\n')
    cfile.write (enumType.toStringPrototype + '\n')
    cfile.write ('{\n')
    cfile.write ('    switch (' + VARNAME + ')\n')
    cfile.write ('    {\n')
    for entry in enumType.entries:
        cfile.write ('    case ' + entry.name + ':\n')
        cfile.write ('        return "' + entry.comment + '";\n')
        cfile.write ('        break;\n')
    cfile.write ('    default:\n')
    cfile.write ('        return "Unknown value";\n')
    cfile.write ('        break;\n')
    cfile.write ('    }\n')
    cfile.write ('    return "Unknown value";\n')
    cfile.write ('}\n')

    cfile.close ()




for fname in os.listdir (INC_DIR):
    if fname.endswith ('.h'):
        completeFile = INC_DIR + fname
        allEnums = readEnumEntriesFromFile (completeFile)
        for enumType in allEnums:
            writeEnumToJavaFile (enumType)
            writeToStringFunction (enumType)

#END
