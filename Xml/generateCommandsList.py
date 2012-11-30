from xml.dom.minidom import parseString
import sys


#################################
# CONFIGURATION :               #
#################################
# Setup XML and C/HFiles Names  #
# Public H files must include   #
# the libARCommands/ prefix     #
#################################

#Name (and path) of the xml file
XMLFILENAME='../Xml/commands.xml'

#Name of the output C/H file for commandsList
COMMANDSLIST_CFILE_NAME='commandsList.c'
COMMANDSLIST_HFILE_NAME='libARCommands/commandsList.h'

#Name (and path) of the output C/H files for commandsHelpers
HELPERS_CFILE_NAME='commandsHelpers.c'
HELPERS_HFILE_NAME='libARCommands/commandsHelpers.h'

#Name (and path) of the output C testbench file
TB_CFILE_NAME='autoTest.c'

##### END OF CONFIG #####
GENERATED_FILES = []
COMMANDSLIST_HFILE='../Includes/'+COMMANDSLIST_HFILE_NAME
GENERATED_FILES.append (COMMANDSLIST_HFILE)
COMMANDSLIST_CFILE='../Sources/'+COMMANDSLIST_CFILE_NAME
GENERATED_FILES.append (COMMANDSLIST_CFILE)
HELPERS_CFILE='../Sources/'+HELPERS_CFILE_NAME
GENERATED_FILES.append (HELPERS_CFILE)
HELPERS_HFILE='../Includes/'+HELPERS_HFILE_NAME
GENERATED_FILES.append (HELPERS_HFILE)
TB_CFILE='../TestBench/'+TB_CFILE_NAME
GENERATED_FILES.append (TB_CFILE)




COMMANDSLIST_DEFINE='_'+COMMANDSLIST_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'
HELPERS_DEFINE='_'+HELPERS_HFILE_NAME.upper().replace('/', '_').replace('.', '_')+'_'

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

def xmlToC(typ):
    xmlIndex = XMLTYPES.index(typ)
    return CTYPES [xmlIndex]

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

sys.exit (0)
"""

#################################
# 2ND PART :                    #
#################################
# Write C part of commandsList  #
#################################

cfile = open (COMMANDSLIST_CFILE, 'w')

cfile.write ('/********************************************\n')
cfile.write (' *            AUTOGENERATED FILE            *\n')
cfile.write (' *             DO NOT MODIFY IT             *\n')
cfile.write (' *                                          *\n')
cfile.write (' * To add new commands :                    *\n')
cfile.write (' *  - Modify ../Xml/commands.xml file       *\n')
cfile.write (' *  - Re-run generateCommandsList.py script *\n')
cfile.write (' *                                          *\n')
cfile.write (' ********************************************/\n')
cfile.write ('#include <'+COMMANDSLIST_HFILE_NAME+'>\n')
cfile.write ('#include <libSAL/print.h>\n')
cfile.write ('#include <stdlib.h>\n')
cfile.write ('\n')
cfile.write ('libARCommandsCmd_t ***libARCommandsCmdList = NULL;\n')
cfile.write ('int *libARCommandsCmdClassesMaxId = NULL;\n')
cfile.write ('int libARCommandsListInitOk = 0;\n')
cfile.write ('\n')
cfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cfile.write ('libARCommandsCmd_t **' + cl + 'CommandsList = NULL;\n')
    cfile.write ('\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        cfile.write ('libARCommandsCmd_t ' + cl + '_' + cmd + '_cmd = { ')
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        for argT in ATList:
            cfile.write ('LIBARCOMMANDS_ARG_TYPE_' + argT.upper() + ', ')
        cfile.write ('LIBARCOMMANDS_ARG_TYPE_END };\n')
    cfile.write ('\n')


cfile.write ('\n')
cfile.write ('\n')
cfile.write ('int libARCommandsListInit ()\n')
cfile.write ('{\n')
cfile.write ('    int res = 1;\n')
cfile.write ('    if (1 == libARCommandsListInitOk)\n')
cfile.write ('    {\n')
cfile.write ('        return libARCommandsListInitOk;\n')
cfile.write ('    }\n')
cfile.write ('\n')
for cl in allClassesNames:
    cfile.write ('    if (1 == res)\n')
    cfile.write ('    {\n')
    sname = cl + 'CommandsList'
    cfile.write ('        ' + sname + ' = calloc (' + cl.upper () + '_CMD_MAX, sizeof (libARCommandsCmd_t));\n')
    cfile.write ('        if (NULL != ' + sname + ')\n')
    cfile.write ('        {\n')
    cmdList = commandsNameByClass [allClassesNames.index (cl)]
    for cmd in cmdList:
        cfile.write ('            ' + sname + ' [' + cl.upper () + '_CMD_' + cmd.upper () + '] = &' + cl + '_' + cmd + '_cmd;\n')
    cfile.write ('        }\n')
    cfile.write ('        else\n')
    cfile.write ('        {\n')
    cfile.write ('            res = 0;\n')
    cfile.write ('        }\n')
    cfile.write ('    }\n')
    cfile.write ('\n')
cfile.write ('\n')

cfile.write ('    if (1 == res)\n')
cfile.write ('    {\n')
cfile.write ('        libARCommandsCmdList = calloc (COMMAND_CLASS_MAX, sizeof (libARCommandsCmd_t *));\n')
cfile.write ('        if (NULL != libARCommandsCmdList)\n')
cfile.write ('        {\n')
for cl in allClassesNames:
    cfile.write ('            libARCommandsCmdList [COMMAND_CLASS_' + cl.upper () + '] = ' + cl + 'CommandsList;\n')
cfile.write ('        }\n')
cfile.write ('        else\n')
cfile.write ('        {\n')
cfile.write ('            res = 0;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('\n')

cfile.write ('    if (1 == res)\n')
cfile.write ('    {\n')
cfile.write ('        libARCommandsCmdClassesMaxId = calloc (COMMAND_CLASS_MAX, sizeof (int));\n')
cfile.write ('        if (NULL != libARCommandsCmdClassesMaxId)\n')
cfile.write ('        {\n')
for cl in allClassesNames:
    cfile.write ('            libARCommandsCmdClassesMaxId [COMMAND_CLASS_' + cl.upper () + '] = ' + cl.upper () + '_CMD_MAX;\n')
cfile.write ('        }\n')
cfile.write ('        else\n')
cfile.write ('        {\n')
cfile.write ('            res = 0;\n')
cfile.write ('        }\n')
cfile.write ('    }\n')
cfile.write ('\n')

cfile.write ('    if (0 == res)\n')
cfile.write ('    {\n')
cfile.write ('        libARCommandsListDestroy ();\n')
cfile.write ('    }\n')
cfile.write ('    else\n')
cfile.write ('    {\n')
cfile.write ('        libARCommandsListInitOk = res;\n')
cfile.write ('    }\n')
cfile.write ('    return res;\n')
cfile.write ('}\n')
cfile.write ('\n')
cfile.write ('\n')

cfile.write ('void libARCommandsListDestroy ()\n')
cfile.write ('{\n')
for cl in allClassesNames:
    sname = cl + 'CommandsList'
    cfile.write ('    if (NULL != ' + sname + ') { free (' + sname + '); ' + sname + ' = NULL; }\n')
cfile.write ('    if (NULL != libARCommandsCmdList) { free (libARCommandsCmdList); libARCommandsCmdList = NULL; }\n')
cfile.write ('    if (NULL != libARCommandsCmdClassesMaxId) { free (libARCommandsCmdClassesMaxId); libARCommandsCmdClassesMaxId = NULL; }\n')
cfile.write ('    libARCommandsListInitOk = 0;\n')
cfile.write ('}\n')
cfile.write ('\n')


cfile.write ('libARCommandsCmd_t *getCommandArgsWithClassAndId (eLIBARCOMMANDS_COMMAND_CLASS class, int id)\n')
cfile.write ('{\n')
cfile.write ('    int maxId = 0;\n')
cfile.write ('\n')
cfile.write ('    if (0 == libARCommandsListInitOk && 1 != libARCommandsListInit ())\n')
cfile.write ('    {\n')
cfile.write ('        SAL_PRINT (PRINT_ERROR, "Unable to initialize libARCommandsList\\n");\n')
cfile.write ('        return NULL;\n')
cfile.write ('    }\n')
cfile.write ('    if (COMMAND_CLASS_MAX <= class)\n')
cfile.write ('    {\n')
cfile.write ('        SAL_PRINT (PRINT_DEBUG, "Class %d is unknown (I know only %d classes)\\n", class, COMMAND_CLASS_MAX);\n')
cfile.write ('        return NULL;\n')
cfile.write ('    }\n')
cfile.write ('    maxId = libARCommandsCmdClassesMaxId [class];\n')
cfile.write ('    if (maxId <= id)\n')
cfile.write ('    {\n')
cfile.write ('        SAL_PRINT (PRINT_DEBUG, "Id %d is unknown for class %d (I know only %d id to this class)\\n", id, class, maxId);\n')
cfile.write ('        return NULL;\n')
cfile.write ('    }\n')
cfile.write ('    return libARCommandsCmdList [class][id];\n')
cfile.write ('}\n')

cfile.close ()

#################################
# 3RD PART :                    #
#################################
# Write H part of commandsList  #
#################################

hfile = open (COMMANDSLIST_HFILE, 'w')

hfile.write ('/********************************************\n')
hfile.write (' *            AUTOGENERATED FILE            *\n')
hfile.write (' *             DO NOT MODIFY IT             *\n')
hfile.write (' *                                          *\n')
hfile.write (' * To add new commands :                    *\n')
hfile.write (' *  - Modify ../../Xml/commands.xml file    *\n')
hfile.write (' *  - Re-run generateCommandsList.py script *\n')
hfile.write (' *                                          *\n')
hfile.write (' ********************************************/\n')
hfile.write ('\n')
hfile.write ('#ifndef '+COMMANDSLIST_DEFINE+'\n')
hfile.write ('#define '+COMMANDSLIST_DEFINE+' (1)\n')
hfile.write ('\n')
hfile.write ('#include <libARCommands/commandsTypes.h>\n')
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
hfile.write ('extern libARCommandsCmd_t ***libARCommandsCmdList;\n')
hfile.write ('extern int *libARCommandsCmdClassesMaxId;\n')
hfile.write ('extern int libARCommandsListInitOk;\n')
hfile.write ('\n')
hfile.write ('int libARCommandsListInit ();\n')
hfile.write ('void libARCommandsListDestroy ();\n')
hfile.write ('libARCommandsCmd_t *getCommandArgsWithClassAndId (eLIBARCOMMANDS_COMMAND_CLASS class, int id);\n')
hfile.write ('\n')
hfile.write ('#endif /* '+COMMANDSLIST_DEFINE+' */\n')

hfile.close ()

#################################
# 4TH PART :                    #
#################################
# Generate H part of helpers    #
#################################

hfile = open (HELPERS_HFILE, 'w')

hfile.write ('/********************************************\n')
hfile.write (' *            AUTOGENERATED FILE            *\n')
hfile.write (' *             DO NOT MODIFY IT             *\n')
hfile.write (' *                                          *\n')
hfile.write (' * To add new commands :                    *\n')
hfile.write (' *  - Modify ../../Xml/commands.xml file    *\n')
hfile.write (' *  - Re-run generateCommandsList.py script *\n')
hfile.write (' *                                          *\n')
hfile.write (' ********************************************/\n')
hfile.write ('#ifndef '+HELPERS_DEFINE+'\n')
hfile.write ('#define '+HELPERS_DEFINE+' (1)\n')
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
        hfile.write (' * @warning This function allocate memory. You need to call free() on the buffer !\n')
        for comm in commList:
            hfile.write (' * ' + comm + '\n')
        for argN in ANList:
            ACListCurrArg = ACList [ANList.index (argN)]
            for argC in ACListCurrArg:
                hfile.write (' * @param ' + argN + ' ' + argC + '\n')
        hfile.write (' * @return Pointer to the command buffer (NULL if any error occured)\n')
        hfile.write (' */\n')
        hfile.write ('uint8_t *libARCommands' + cl.capitalize() + 'Send' + cmd.capitalize() + ' (')
        first = 1
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            if 1 == first:
                first = 0
            else:
                hfile.write (', ')
            hfile.write (xmlToC(argT) + ' ' + argN)
        hfile.write (');\n')
    hfile.write ('\n')

hfile.write ('#endif /* '+HELPERS_DEFINE+' */\n')

hfile.close ()

#################################
# 5TH PART :                    #
#################################
# Generate C part of helpers    #
#################################

cfile = open (HELPERS_CFILE, 'w')

cfile.write ('/********************************************\n')
cfile.write (' *            AUTOGENERATED FILE            *\n')
cfile.write (' *             DO NOT MODIFY IT             *\n')
cfile.write (' *                                          *\n')
cfile.write (' * To add new commands :                    *\n')
cfile.write (' *  - Modify ../Xml/commands.xml file       *\n')
cfile.write (' *  - Re-run generateCommandsList.py script *\n')
cfile.write (' *                                          *\n')
cfile.write (' ********************************************/\n')
cfile.write ('#include <'+HELPERS_HFILE_NAME+'>\n')
cfile.write ('#include <'+COMMANDSLIST_HFILE_NAME+'>\n')
cfile.write ('#include <libARCommands/commands.h>')
cfile.write ('\n')
cfile.write ('\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cfile.write ('// Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        cfile.write ('uint8_t *libARCommands' + cl.capitalize() + 'Send' + cmd.capitalize() + ' (')
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        first = 1
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            if 1 == first:
                first = 0
            else:
                cfile.write (', ')
            cfile.write (xmlToC(argT) + ' ' + argN)
        cfile.write (')\n')
        cfile.write ('{\n')
        cfile.write ('    return generateCommand (COMMAND_CLASS_' + cl.upper () + ', ' + cl.upper () + '_CMD_' + cmd.upper ())
        for argN in ANList:
            cfile.write (', ' + argN)
        cfile.write (');\n')
        cfile.write ('}\n\n')
    cfile.write ('\n')

cfile.write ('// END GENERATED CODE\n')

cfile.close ()

#################################
# 6TH PART :                    #
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
cfile.write ('#include <'+HELPERS_HFILE_NAME+'>\n')
cfile.write ('#include <libSAL/print.h>\n')
cfile.write ('#include <stdlib.h>')
cfile.write ('\n')
cfile.write ('\n')
cfile.write ('int\n')
cfile.write ('main (int argc, char *argv[])\n')
cfile.write ('{\n')
cfile.write ('    uint8_t *res = NULL;\n')
cfile.write ('    int errcount = 0;\n')
for cl in allClassesNames:
    clIndex = allClassesNames.index (cl)
    cfile.write ('    // Command class ' + cl + '\n')
    cmdList = commandsNameByClass [clIndex]
    for cmd in cmdList:
        cmdIndex = cmdList.index (cmd)
        cfile.write ('    res = libARCommands' + cl.capitalize() + 'Send' + cmd.capitalize() + ' (')
        ATList = argTypesByClassAndCommand [clIndex][cmdIndex]
        ANList = argNamesByClassAndCommand [clIndex][cmdIndex]
        first = 1
        for argN in ANList:
            argT = ATList [ANList.index (argN)]
            if 1 == first:
                first = 0
            else:
                cfile.write (', ')
            cfile.write (xmlToSample(argT))
        cfile.write (');\n')
        cfile.write ('    if (NULL == res)\n')
        cfile.write ('    {\n')
        cfile.write ('        SAL_PRINT (PRINT_ERROR, "Error while sending command ' + cl.capitalize() + '.' + cmd.capitalize() + '\\n");\n')
        cfile.write ('        errcount++;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        SAL_PRINT (PRINT_DEBUG, "Sending command ' + cl.capitalize() + '.' + cmd.capitalize() + ' succeded\\n");\n')
        cfile.write ('        free (res); res = NULL;\n')
        cfile.write ('    }\n')
        cfile.write ('\n')
    cfile.write ('\n')

cfile.write ('    return errcount;\n')
cfile.write ('}\n')

cfile.close ()
