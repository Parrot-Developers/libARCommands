'''
    Copyright (C) 2014 Parrot SA

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in
      the documentation and/or other materials provided with the
      distribution.
    * Neither the name of Parrot nor the names
      of its contributors may be used to endorse or promote products
      derived from this software without specific prior written
      permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
    FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
    COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
    BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
    OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
    AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
    OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
    SUCH DAMAGE.
'''
import sys
import os
import re
import arsdkparser

MYDIR=os.path.abspath(os.path.dirname(__file__))
LIBARCOMMANDS_DIR=os.path.realpath(os.path.join(MYDIR, ".."))
PACKAGES_DIR=os.path.realpath(os.path.join(MYDIR, "../.."))

sys.path.append('%(MYDIR)s/../../ARSDKBuildUtils/Utils/Python' % locals())

from ARFuncs import *
from arsdkparser import *

LIB_NAME = 'libARCommands'
LIB_MODULE = LIB_NAME.replace ('lib', '')

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

#Name of the output public header containing id enums
COMMANDSID_HFILE_NAME=LIB_NAME + '/ARCOMMANDS_Ids.h'

#Name of the output public header containing typedefs
COMMANDSTYPES_HFILE_NAME=LIB_NAME + '/ARCOMMANDS_Types.h'

#Name of the output public header containing encoder helpers
COMMANDSGEN_HFILE_NAME=LIB_NAME + '/ARCOMMANDS_Generator.h'

#Name of the output public header containing decoder helpers
COMMANDSDEC_HFILE_NAME=LIB_NAME + '/ARCOMMANDS_Decoder.h'

#Name of the output public header containing filter helpers
COMMANDSFIL_HFILE_NAME=LIB_NAME + '/ARCOMMANDS_Filter.h'

#Name of the output internal header containing reader/writer functions prototypes
COMMANDSRW_HFILE_NAME='ARCOMMANDS_ReadWrite.h'

#Name of the output C file containing reader/writer functions
COMMANDSRW_CFILE_NAME='ARCOMMANDS_ReadWrite.c'

#Name of the output C file containing encoder helpers
COMMANDSGEN_CFILE_NAME='ARCOMMANDS_Generator.c'

#Name of the output C file containing decoder helpers
COMMANDSDEC_CFILE_NAME='ARCOMMANDS_Decoder.c'

#Name of the output C file containing filter helpers
COMMANDSFIL_CFILE_NAME='ARCOMMANDS_Filter.c'

#Name of the output C/H common testbench file
TB_CFILE_NAME='autoTest.c'
TB_HFILE_NAME='autoTest.h'
#Tag for tb ARSAL_PRINT calls
TB_TAG='AutoTest'

#Name of the linux entry point file for autotest
TB_LIN_CFILE_NAME='autoTest_linux.c'

#Name of the JNI C File
JNI_CFILE_NAME='ARCOMMANDS_JNI.c'
JNI_DECODER_CFILE_NAME='ARCOMMANDS_JNIDecoder.c'
JNI_FILTER_CFILE_NAME='ARCOMMANDS_JNIFilter.c'

#Name of the JNI JAVA File
JNI_JFILE_NAME='ARCommand.java'
JNI_DECODER_JFILE_NAME='ARCommandsDecoder.java'
JNIClassName, _ = os.path.splitext (JNI_JFILE_NAME)
JNIDecoderClassName, _ = os.path.splitext (JNI_DECODER_JFILE_NAME)
JNI_FILTER_JFILE_NAME='ARCommandsFilter.java'
JNIFilterClassName, _ = os.path.splitext (JNI_FILTER_JFILE_NAME)

#Name of the JNI JAVA Interfaces files (DO NOT MODIFY)
JAVA_INTERFACES_FILES_NAME=JNIClassName + '*Listener.java'
JAVA_ENUM_FILES_NAME=JNIClassName.upper() + '*_ENUM.java'

def _get_args_without_multiset(args):
    for arg in args:
        if not isinstance(arg.argType, arsdkparser.ArMultiSetting):
            yield arg

def _get_args_multiset(args):
    for arg in args:
        if isinstance(arg.argType, arsdkparser.ArMultiSetting):
            yield arg

class Paths:
    def __init__(self, outdir):
        #Relative path of SOURCE dir
        self.SRC_DIR=outdir+'/Sources/'

        #Relative path of INCLUDES dir
        self.INC_DIR=outdir+'/Includes/'

        #Relative path of TESTBENCH dir
        self.TB__DIR=outdir+'/TestBench/'

        #Relative path of unix-like (Linux / os-x) TESTBENCH dir
        self.LIN_TB_DIR=self.TB__DIR + 'linux/'

        #Relative path of multiplatform code for testbenches
        self.COM_TB_DIR=self.TB__DIR + 'common/'

        #Relative path of JNI dir
        self.JNI_DIR=outdir+'/JNI/'

        #Relative path of JNI/C dir
        self.JNIC_DIR=self.JNI_DIR + 'c/'

        #Relative path of JNI/Java dir
        self.JNIJ_DIR=self.JNI_DIR + 'java/'
        self.JNIJ_OUT_DIR=self.JNIJ_DIR + JNI_PACKAGE_DIR + '/'

        # Create array of generated files (so we can cleanup only our files)
        self.GENERATED_FILES = []
        self.COMMANDSID_HFILE=self.INC_DIR + COMMANDSID_HFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSID_HFILE)
        self.COMMANDSGEN_HFILE=self.INC_DIR + COMMANDSGEN_HFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSGEN_HFILE)
        self.COMMANDSTYPES_HFILE=self.INC_DIR + COMMANDSTYPES_HFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSTYPES_HFILE)
        self.COMMANDSGEN_CFILE=self.SRC_DIR + COMMANDSGEN_CFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSGEN_CFILE)
        self.COMMANDSDEC_HFILE=self.INC_DIR + COMMANDSDEC_HFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSDEC_HFILE)
        self.COMMANDSDEC_CFILE=self.SRC_DIR + COMMANDSDEC_CFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSDEC_CFILE)
        self.COMMANDSFIL_HFILE=self.INC_DIR + COMMANDSFIL_HFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSFIL_HFILE)
        self.COMMANDSFIL_CFILE=self.SRC_DIR + COMMANDSFIL_CFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSFIL_CFILE)
        self.COMMANDSRW_HFILE=self.SRC_DIR + COMMANDSRW_HFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSRW_HFILE)
        self.COMMANDSRW_CFILE=self.SRC_DIR + COMMANDSRW_CFILE_NAME
        self.GENERATED_FILES.append (self.COMMANDSRW_CFILE)
        self.TB_CFILE=self.COM_TB_DIR + TB_CFILE_NAME
        self.GENERATED_FILES.append (self.TB_CFILE)
        self.TB_HFILE=self.COM_TB_DIR + TB_HFILE_NAME
        self.GENERATED_FILES.append (self.TB_HFILE)
        self.TB_LIN_CFILE=self.LIN_TB_DIR + TB_LIN_CFILE_NAME
        self.GENERATED_FILES.append (self.TB_LIN_CFILE)
        # Create array of generated JNI files (so we can cleanup only our files)
        self.GENERATED_JNI_FILES = []
        self.JNI_CFILE=self.JNIC_DIR + JNI_CFILE_NAME
        self.GENERATED_JNI_FILES.append (self.JNI_CFILE)
        self.JNI_DECODER_CFILE=self.JNIC_DIR + JNI_DECODER_CFILE_NAME
        self.GENERATED_JNI_FILES.append (self.JNI_DECODER_CFILE)
        self.JNI_FILTER_CFILE=self.JNIC_DIR + JNI_FILTER_CFILE_NAME
        self.GENERATED_JNI_FILES.append (self.JNI_FILTER_CFILE)
        # Create array of generated JAVA files (so we can cleanup only our files)
        self.GENERATED_JAVA_FILES = []
        self.JNI_JFILE=self.JNIJ_OUT_DIR + JNI_JFILE_NAME
        self.GENERATED_JAVA_FILES.append (self.JNI_JFILE)
        self.JNI_DECODER_JFILE=self.JNIJ_OUT_DIR + JNI_DECODER_JFILE_NAME
        self.GENERATED_JAVA_FILES.append (self.JNI_DECODER_JFILE)
        self.JNI_FILTER_JFILE=self.JNIJ_OUT_DIR + JNI_FILTER_JFILE_NAME
        self.GENERATED_JAVA_FILES.append (self.JNI_FILTER_JFILE)
        self.JAVA_INTERFACES_FILES=self.JNIJ_OUT_DIR + JAVA_INTERFACES_FILES_NAME
        self.JAVA_ENUM_FILES=self.JNIJ_OUT_DIR + JAVA_ENUM_FILES_NAME

##### END OF CONFIG #####

# Create names for #ifndef _XXX_ statements in .h files
COMMANDSID_DEFINE='_' + COMMANDSID_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSDEC_DEFINE='_' + COMMANDSDEC_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSGEN_DEFINE='_' + COMMANDSGEN_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSTYPES_DEFINE='_' + COMMANDSTYPES_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSRW_DEFINE='_' + COMMANDSRW_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
COMMANDSFIL_DEFINE='_' + COMMANDSFIL_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'
TB_DEFINE='_' + TB_HFILE_NAME.upper ().replace ('/', '_').replace ('.', '_') + '_'

# Submodules names
ID_SUBMODULE='ID'
GEN_SUBMODULE='Generator'
DEC_SUBMODULE='Decoder'
FIL_SUBMODULE='Filter'
RW_SUBMODULE='ReadWrite'
TB_SUBMODULE='Testbench'
JNI_SUBMODULE='JNI'
JNI_FILTER_SUBMODULE='JNI_FILTER'

hasArgOfType = {ArArgType.U8 : True,        ArArgType.I8 : False,
                ArArgType.U16 : True,       ArArgType.I16 : False,
                ArArgType.U32 : False,      ArArgType.I32 : False,
                ArArgType.U64 : False,      ArArgType.I64 : False,
                ArArgType.FLOAT : False,    ArArgType.DOUBLE : False,
                ArArgType.STRING : False,   ArArgType.ENUM : False,
                ArArgType.BITFIELD : False, ArArgType.MULTISETTING : False}

#Type conversion from XML Defined types to many other types
# XML Defined types
XMLTYPES = [ArArgType.U8,     ArArgType.I8,
            ArArgType.U16,    ArArgType.I16,
            ArArgType.U32,    ArArgType.I32,
            ArArgType.U64,    ArArgType.I64,
            ArArgType.FLOAT,  ArArgType.DOUBLE,
            ArArgType.STRING]
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
CREADERS = [ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer'),     ' (int8_t)' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer'),
            ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer'),    ' (int16_t)' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer'),
            ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read32FromBuffer'),    ' (int32_t)' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read32FromBuffer'),
            ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read64FromBuffer'),    ' (int64_t)' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read64FromBuffer'),
            ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadFloatFromBuffer'), ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadDoubleFromBuffer'),
            ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadStringFromBuffer')]

# Equivalent calls for the Decoder print internal functions
CPRINTERS = [ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU8'),    ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI8'),
             ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU16'),   ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI16'),
             ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU32'),   ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI32'),
             ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU64'),   ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI64'),
             ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintFloat'), ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintDouble'),
             ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintString')]

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
# JNI UnsignedToSigned casts
JNIUTSCASTS = ['(jbyte)', '',
               '(jshort)', '',
               '(jint)', '',
               '(jlong)', '',
               '', '',
               '']

def xmlToC (module, ftr, cmd, arg, is_arg=False):
    if isinstance(arg.argType, ArEnum):
        return AREnumName (module, get_ftr_old_name(ftr), arg.argType.name)
    if isinstance(arg.argType, ArMultiSetting):
        ctype = ARTypeName (module, get_ftr_old_name(ftr), arg.argType.name)
        if (is_arg):
            return ctype + ' *'
        return ctype
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return CTYPES [xmlIndex]

def xmlToCcharAreConst (module, ftr, cmd, arg, is_arg=False):
    if isinstance(arg.argType, ArEnum):
        return AREnumName (module, get_ftr_old_name(ftr), arg.argType.name)
    if isinstance(arg.argType, ArMultiSetting):
        ctype = ARTypeName (module, get_ftr_old_name(ftr), arg.argType.name)
        if (is_arg):
            return ctype + ' *'
        return ctype
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return CTYPES_WC [xmlIndex]

def xmlToCwithConst (module, ftr, cmd, arg, is_arg=False):
    if isinstance(arg.argType, ArEnum):
        return AREnumName (module, get_ftr_old_name(ftr), arg.argType.name)
    if isinstance(arg.argType, ArMultiSetting):
        ctype = 'const ' + ARTypeName (module, get_ftr_old_name(ftr), arg.argType.name)
        if is_arg:
            return ctype + ' *'
        return ctype
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return CTYPES_WC [xmlIndex]

def xmlToSize (ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return 'U32';
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return SZETYPES [xmlIndex]

def xmlToReader (ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return '(' + AREnumName (LIB_MODULE, get_ftr_old_name(ftr), arg.argType.name) + ')' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read32FromBuffer')
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return CREADERS [xmlIndex]

def xmlToPrinter (ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return '(' + AREnumName (LIB_MODULE, get_ftr_old_name(ftr), arg.argType.name) + ')' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI32')
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return CPRINTERS [xmlIndex]

def xmlToJava (module, ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return ARJavaEnumType (module, get_ftr_old_name(ftr), arg.argType.name)
    if isinstance(arg.argType, ArMultiSetting):
        return ARJavaMultiSetType (module, get_ftr_old_name(ftr), arg.argType.name)
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return JAVATYPES [xmlIndex]

def jniEnumClassName (ftr, cmd, arg):
    if not isinstance(arg.argType, ArEnum):
        return ''
    return JNI_PACKAGE_DIR + '/' + ARJavaEnumType (LIB_MODULE, get_ftr_old_name(ftr), arg.argType.name)

def jniClassName (ftr, cmd, arg):
    return JNI_PACKAGE_DIR + '/' + ARJavaMultiSetType (LIB_MODULE, get_ftr_old_name(ftr), arg.argType.name)

def xmlToJavaSig (ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return 'L' + jniEnumClassName (ftr, cmd, arg) + ';'
    if isinstance(arg.argType, ArMultiSetting):
        return 'L' + jniClassName (ftr, cmd, arg) + ';'
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return JAVASIG [xmlIndex]

def xmlToJni (ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return 'jint'
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return JNITYPES [xmlIndex]

def xmlToJniCast (ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return '(jint)'
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return JNIUTSCASTS [xmlIndex]

def format_cmd_name(msg, underscore=False):#projetc only
    if underscore:
        return ARCapitalize(msg.name) if msg.cls is None else ARCapitalize(msg.cls.name) + '_'+  ARCapitalize(msg.name)
    else:
        return msg.name if msg.cls is None else msg.cls.name + ARCapitalize(msg.name)

def get_arg_doc(arg):
    doc = ''
    if arg.argType in ArArgType.TO_STRING:
        doc = arg.doc
    else:
        if arg.doc:
            doc = arg.doc + '\n'

        if isinstance(arg.argType, ArEnum):
            doc = doc + arg.argType.doc
        elif isinstance(arg.argType, ArBitfield):
            doc = doc + arg.argType.enum.doc
        elif isinstance(arg.argType, ArMultiSetting):
            doc = doc + arg.argType.doc

    return doc

def get_ftr_old_name(ftr):
    FROM_NEW_NAME = { 'ardrone3':'ARDrone3', 'common_dbg':'commonDebug',
                        'jpsumo':'JumpingSumo', 'minidrone':'MiniDrone',
                        'skyctrl':'SkyController'}
    if ftr.name in FROM_NEW_NAME:
        return FROM_NEW_NAME[ftr.name]
    else:
        return ftr.name

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

def xmlToSample (ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return '(' + AREnumName (LIB_MODULE, get_ftr_old_name(ftr), arg.argType.name) + ')0';
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return SAMPLEARGS [xmlIndex]

def xmlToPrintf (ftr, cmd, arg):
    if isinstance(arg.argType, ArEnum):
        return '%d';
    if isinstance(arg.argType, ArBitfield):
        xmlIndex = XMLTYPES.index (arg.argType.btfType)
    else:
        xmlIndex = XMLTYPES.index (arg.argType)
    return PRINTFF [xmlIndex]

LICENCE_HEADER='''/*
    Copyright (C) 2014 Parrot SA

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
    * Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in
    the documentation and/or other materials provided with the
    distribution.
    * Neither the name of Parrot nor the names
    of its contributors may be used to endorse or promote products
    derived from this software without specific prior written
    permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
    FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
    COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
    BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
    OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
    AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
    OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
    SUCH DAMAGE.
*/
'''

DEC_ERR_ENAME='ERROR'
GEN_ERR_ENAME='ERROR'
FIL_STATUS_ENAME='STATUS'
FIL_ERROR_ENAME='ERROR'

def interfaceName (ftr, cmd):
    return JNIClassName + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Listener'

def interfaceVar (ftr, cmd):
    return '_' + interfaceName (ftr, cmd)

def javaCbName (ftr, cmd):
    return 'on' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Update'

def native_generateCmds(ctx, paths):
    genDebug = True
    genTreeFilename = None
    projects = [DEFAULTPROJECTNAME]

    if not os.path.exists (paths.SRC_DIR):
        os.makedirs (paths.SRC_DIR)
    if not os.path.exists (paths.INC_DIR):
        os.makedirs (paths.INC_DIR)
    if not os.path.exists (paths.INC_DIR + LIB_NAME):
        os.makedirs (paths.INC_DIR + LIB_NAME)

    #################################
    # 1ST PART :                    #
    #################################
    # Read XML file to local arrays #
    # of commands / classes         #
    #################################

    allFeatures = ctx.features

    # Check types used
    for ftr in allFeatures:
        for msg in ftr.getMsgs():
            for arg in msg.args:
                if isinstance(arg.argType, ArEnum):
                    hasArgOfType[ArArgType.ENUM] = True
                elif isinstance(arg.argType, ArBitfield):
                    hasArgOfType[ArArgType.BITFIELD] = True
                    hasArgOfType[arg.argType.btfType] = True
                elif isinstance(arg.argType, ArMultiSetting):
                    hasArgOfType[ArArgType.MULTISETTING] = True
                else:
                    hasArgOfType[arg.argType] = True

    #################################
    # 2ND PART :                    #
    #################################
    # Write private H files         #
    #################################

    hfile = open (paths.COMMANDSID_HFILE, 'w')

    hfile.write (LICENCE_HEADER)
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
    hfile.write ('// ARSDK_NO_ENUM_PREPROCESS //\n')
    hfile.write ('typedef enum {\n')
    for ftr in allFeatures:
        ENAME='FEATURE'
        hfile.write ('    ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, ENAME, get_ftr_old_name(ftr)) + ' = ' + str(ftr.featureId) + ',\n')
    hfile.write ('} ' + AREnumName (LIB_MODULE, ID_SUBMODULE, ENAME) + ';\n')
    hfile.write ('\n')
    hfile.write ('\n')

    hfile.write ('#define ' + ARMacroName (LIB_MODULE, ID_SUBMODULE, 'FEATURE_CLASS') + ' (0) //default class id use by features.\n')
    hfile.write ('\n')
    hfile.write ('\n')

    #project only
    for ftr in allFeatures:
        if ftr.classes:
            ENAME=get_ftr_old_name(ftr) + '_CLASS'
            hfile.write ('typedef enum {\n')
            for cl in ftr.classes:
                hfile.write ('    ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, ENAME, cl.name) + ' = ' + str(cl.classId) + ',\n')
            hfile.write ('} ' + AREnumName (LIB_MODULE, ID_SUBMODULE, ENAME) + ';\n')
            hfile.write ('\n')
    hfile.write ('\n')
    for ftr in allFeatures:
        if ftr.classes: #project only
            for cl in ftr.classes:
                hfile.write ('typedef enum {\n')
                ENAME=get_ftr_old_name(ftr) + '_' + cl.name + '_CMD'
                first = True
                for cmd in cl.cmds:
                    hfile.write ('    ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, ENAME, cmd.name) + ' = ' + str(cmd.cmdId) + ',\n')
                hfile.write ('} ' + AREnumName (LIB_MODULE, ID_SUBMODULE, ENAME) + ';\n')
                hfile.write ('\n')
        else:
            hfile.write ('typedef enum {\n')
            ENAME=get_ftr_old_name(ftr) + '_CMD'
            first = True
            for cmd in ftr.cmds + ftr.evts:
                hfile.write ('    ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, ENAME, cmd.name) + ' = ' + str(cmd.cmdId) + ',\n')
            hfile.write ('} ' + AREnumName (LIB_MODULE, ID_SUBMODULE, ENAME) + ';\n')
            hfile.write ('\n')
        hfile.write ('\n')

    hfile.write ('\n')
    hfile.write ('#endif /* ' + COMMANDSID_DEFINE + ' */\n')

    hfile.close ()

    hfile = open(paths.COMMANDSRW_HFILE, 'w')

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
    hfile.write ('#ifndef ' + COMMANDSRW_DEFINE + '\n')
    hfile.write ('#define ' + COMMANDSRW_DEFINE + ' (1)\n')
    hfile.write ('\n')
    hfile.write ('#include <inttypes.h>\n')
    hfile.write ('#include <string.h>\n')
    hfile.write ('#include <stdlib.h>\n')
    hfile.write ('\n')
    hfile.write ('// ------- //\n')
    hfile.write ('// WRITERS //\n')
    hfile.write ('// ------- //\n')
    hfile.write ('\n')
    if hasArgOfType[ArArgType.U8] or hasArgOfType[ArArgType.I8]:
        hfile.write ('// Add an 8 bit value to the buffer\n')
        hfile.write ('// Returns -1 if the buffer is not big enough\n')
        hfile.write ('// Returns the new offset in the buffer on success\n')
        hfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU8ToBuffer') + ' (uint8_t *buffer, uint8_t newVal, int32_t oldOffset, int32_t buffCap);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U16] or hasArgOfType[ArArgType.I16]:
        hfile.write ('// Add a 16 bit value to the buffer\n')
        hfile.write ('// Returns -1 if the buffer is not big enough\n')
        hfile.write ('// Returns the new offset in the buffer on success\n')
        hfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU16ToBuffer') + ' (uint8_t *buffer, uint16_t newVal, int32_t oldOffset, int32_t buffCap);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U32] or hasArgOfType[ArArgType.I32] or hasArgOfType[ArArgType.FLOAT] or hasArgOfType[ArArgType.ENUM]:
        hfile.write ('// Add a 32 bit value to the buffer\n')
        hfile.write ('// Returns -1 if the buffer is not big enough\n')
        hfile.write ('// Returns the new offset in the buffer on success\n')
        hfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU32ToBuffer') + ' (uint8_t *buffer, uint32_t newVal, int32_t oldOffset, int32_t buffCap);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U64] or hasArgOfType[ArArgType.I64] or hasArgOfType[ArArgType.DOUBLE]:
        hfile.write ('// Add a 64 bit value to the buffer\n')
        hfile.write ('// Returns -1 if the buffer is not big enough\n')
        hfile.write ('// Returns the new offset in the buffer on success\n')
        hfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU64ToBuffer') + ' (uint8_t *buffer, uint64_t newVal, int32_t oldOffset, int32_t buffCap);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.STRING]:
        hfile.write ('// Add a NULL Terminated String to the buffer\n')
        hfile.write ('// Returns -1 if the buffer is not big enough\n')
        hfile.write ('// Returns the new offset in the buffer on success\n')
        hfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddStringToBuffer') + ' (uint8_t *buffer, const char *newVal, int32_t oldOffset, int32_t buffCap);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.FLOAT]:
        hfile.write ('// Add a float to the buffer\n')
        hfile.write ('// Returns -1 if the buffer is not big enough\n')
        hfile.write ('// Returns the new offset in the buffer on success\n')
        hfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddFloatToBuffer') + ' (uint8_t *buffer, float newVal, int32_t oldOffset, int32_t buffCap);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.DOUBLE]:
        hfile.write ('// Add a double to the buffer\n')
        hfile.write ('// Returns -1 if the buffer is not big enough\n')
        hfile.write ('// Returns the new offset in the buffer on success\n')
        hfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddDoubleToBuffer') + ' (uint8_t *buffer, double newVal, int32_t oldOffset, int32_t buffCap);\n')
        hfile.write ('\n')
    hfile.write ('// ------- //\n')
    hfile.write ('// READERS //\n')
    hfile.write ('// ------- //\n')
    hfile.write ('\n')
    if hasArgOfType[ArArgType.U8] or hasArgOfType[ArArgType.I8]:
        hfile.write ('// Read an 8 bit value from the buffer\n')
        hfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        hfile.write ('uint8_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U16] or hasArgOfType[ArArgType.I16]:
        hfile.write ('// Read a 16 bit value from the buffer\n')
        hfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        hfile.write ('uint16_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U32] or hasArgOfType[ArArgType.I32] or hasArgOfType[ArArgType.ENUM]:
        hfile.write ('// Read a 32 bit value from the buffer\n')
        hfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        hfile.write ('uint32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read32FromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U64] or hasArgOfType[ArArgType.I64]:
        hfile.write ('// Read a 64 bit value from the buffer\n')
        hfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        hfile.write ('uint64_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read64FromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.FLOAT]:
        hfile.write ('// Read a float value from the buffer\n')
        hfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        hfile.write ('float ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadFloatFromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.DOUBLE]:
        hfile.write ('// Read a double value from the buffer\n')
        hfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        hfile.write ('double ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadDoubleFromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.STRING]:
        hfile.write ('// Read a string value from the buffer\n')
        hfile.write ('// On error, return NULL and set *error to 1, else set *error to 0\n')
        hfile.write ('const char* ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadStringFromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error);\n')
        hfile.write ('\n')
    hfile.write ('// -------- //\n')
    hfile.write ('// TOSTRING //\n')
    hfile.write ('// -------- //\n')
    hfile.write ('\n')
    if (hasArgOfType[ArArgType.U8]  or hasArgOfType[ArArgType.I8] or
        hasArgOfType[ArArgType.U16] or hasArgOfType[ArArgType.I16] or
        hasArgOfType[ArArgType.U32] or hasArgOfType[ArArgType.I32] or
        hasArgOfType[ArArgType.U64] or hasArgOfType[ArArgType.I64] or
        hasArgOfType[ArArgType.FLOAT] or hasArgOfType[ArArgType.DOUBLE] or
        hasArgOfType[ArArgType.STRING] or hasArgOfType[ArArgType.ENUM]):
        hfile.write ('// Write a string in a buffer\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (const char *stringToWrite, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U8]:
        hfile.write ('// Write a string in a buffer from an uint8_t arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU8') + ' (const char *name, uint8_t arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.I8]:
        hfile.write ('// Write a string in a buffer from an int8_t arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI8') + ' (const char *name, int8_t arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U16]:
        hfile.write ('// Write a string in a buffer from an uint16_t arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU16') + ' (const char *name, uint16_t arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.I16]:
        hfile.write ('// Write a string in a buffer from an int16_t arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI16') + ' (const char *name, int16_t arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U32]:
        hfile.write ('// Write a string in a buffer from an uint32_t arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU32') + ' (const char *name, uint32_t arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.I32] or hasArgOfType[ArArgType.ENUM]:
        hfile.write ('// Write a string in a buffer from an int32_t arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI32') + ' (const char *name, int32_t arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.U64]:
        hfile.write ('// Write a string in a buffer from an uint64_t arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU64') + ' (const char *name, uint64_t arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.I64]:
        hfile.write ('// Write a string in a buffer from an int64_t arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI64') + ' (const char *name, int64_t arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.FLOAT]:
        hfile.write ('// Write a string in a buffer from float arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintFloat') + ' (const char *name, float arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.DOUBLE]:
        hfile.write ('// Write a string in a buffer from a double arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintDouble') + ' (const char *name, double arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')
    if hasArgOfType[ArArgType.STRING]:
        hfile.write ('// Write a string in a buffer from a string arg\n')
        hfile.write ('// On error, return -1, else return offset in string\n')
        hfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintString') + ' (const char *name, const char *arg, char *output, int outputLen, int outputOffset);\n')
        hfile.write ('\n')


    hfile.write ('\n')
    hfile.write ('#endif /* ' + COMMANDSRW_DEFINE + ' */\n')

    hfile.close ()


    #################################
    # 3RD PART :                    #
    #################################
    # Generate private ReadWrite C  #
    # file                          #
    #################################

    cfile = open (paths.COMMANDSRW_CFILE, 'w')

    cfile.write (LICENCE_HEADER)
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
    cfile.write ('#include <stdio.h>\n')
    cfile.write ('#include "' + COMMANDSRW_HFILE_NAME + '"\n')
    cfile.write ('#include <libARSAL/ARSAL_Endianness.h>\n')
    cfile.write ('\n')
    cfile.write ('// ------- //\n')
    cfile.write ('// WRITERS //\n')
    cfile.write ('// ------- //\n')
    cfile.write ('\n')
    if hasArgOfType[ArArgType.U8] or hasArgOfType[ArArgType.I8]:
        cfile.write ('// Add an 8 bit value to the buffer\n')
        cfile.write ('// Returns -1 if the buffer is not big enough\n')
        cfile.write ('// Returns the new offset in the buffer on success\n')
        cfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU8ToBuffer') + ' (uint8_t *buffer, uint8_t newVal, int32_t oldOffset, int32_t buffCap)\n')
        cfile.write ('{\n')
        cfile.write ('    int32_t retVal = 0;\n')
        cfile.write ('    int32_t size = oldOffset + sizeof(newVal);\n')
        cfile.write ('\n')
        cfile.write ('    if (buffCap < size)\n')
        cfile.write ('    {\n')
        cfile.write ('        retVal = -1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        uint8_t *buffptr = &(buffer [oldOffset]);\n')
        cfile.write ('        uint8_t localVal = newVal;\n')
        cfile.write ('        memcpy (buffptr, &localVal, sizeof (localVal));\n')
        cfile.write ('        retVal = oldOffset + sizeof (localVal);\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U16] or hasArgOfType[ArArgType.I16]:
        cfile.write ('// Add a 16 bit value to the buffer\n')
        cfile.write ('// Returns -1 if the buffer is not big enough\n')
        cfile.write ('// Returns the new offset in the buffer on success\n')
        cfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU16ToBuffer') + ' (uint8_t *buffer, uint16_t newVal, int32_t oldOffset, int32_t buffCap)\n')
        cfile.write ('{\n')
        cfile.write ('    int32_t retVal = 0;\n')
        cfile.write ('    int32_t size = oldOffset + sizeof(newVal);\n')
        cfile.write ('\n')
        cfile.write ('    if (buffCap < size)\n')
        cfile.write ('    {\n')
        cfile.write ('        retVal = -1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        uint8_t *buffptr = &(buffer [oldOffset]);\n')
        cfile.write ('        uint16_t localVal = htods (newVal);\n')
        cfile.write ('        memcpy (buffptr, &localVal, sizeof (localVal));\n')
        cfile.write ('        retVal = oldOffset + sizeof (localVal);\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U32] or hasArgOfType[ArArgType.I32] or hasArgOfType[ArArgType.FLOAT] or hasArgOfType[ArArgType.ENUM]:
        cfile.write ('// Add a 32 bit value to the buffer\n')
        cfile.write ('// Returns -1 if the buffer is not big enough\n')
        cfile.write ('// Returns the new offset in the buffer on success\n')
        cfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU32ToBuffer') + ' (uint8_t *buffer, uint32_t newVal, int32_t oldOffset, int32_t buffCap)\n')
        cfile.write ('{\n')
        cfile.write ('    int32_t retVal = 0;\n')
        cfile.write ('    int32_t size = oldOffset + sizeof(newVal);\n')
        cfile.write ('\n')
        cfile.write ('    if (buffCap < size)\n')
        cfile.write ('    {\n')
        cfile.write ('        retVal = -1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        uint8_t *buffptr = &(buffer [oldOffset]);\n')
        cfile.write ('        uint32_t localVal = htodl (newVal);\n')
        cfile.write ('        memcpy (buffptr, &localVal, sizeof (localVal));\n')
        cfile.write ('        retVal = oldOffset + sizeof (localVal);\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U64] or hasArgOfType[ArArgType.I64] or hasArgOfType[ArArgType.DOUBLE]:
        cfile.write ('// Add a 64 bit value to the buffer\n')
        cfile.write ('// Returns -1 if the buffer is not big enough\n')
        cfile.write ('// Returns the new offset in the buffer on success\n')
        cfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU64ToBuffer') + ' (uint8_t *buffer, uint64_t newVal, int32_t oldOffset, int32_t buffCap)\n')
        cfile.write ('{\n')
        cfile.write ('    int32_t retVal = 0;\n')
        cfile.write ('    int32_t size = oldOffset + sizeof(newVal);\n')
        cfile.write ('\n')
        cfile.write ('    if (buffCap < size)\n')
        cfile.write ('    {\n')
        cfile.write ('        retVal = -1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        uint8_t *buffptr = &(buffer [oldOffset]);\n')
        cfile.write ('        uint64_t localVal = htodll (newVal);\n')
        cfile.write ('        memcpy (buffptr, &localVal, sizeof (localVal));\n')
        cfile.write ('        retVal = oldOffset + sizeof (localVal);\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.STRING]:
        cfile.write ('// Add a NULL Terminated String to the buffer\n')
        cfile.write ('// Returns -1 if the buffer is not big enough\n')
        cfile.write ('// Returns the new offset in the buffer on success\n')
        cfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddStringToBuffer') + ' (uint8_t *buffer, const char *newVal, int32_t oldOffset, int32_t buffCap)\n')
        cfile.write ('{\n')
        cfile.write ('    int32_t retVal = 0;\n')
        cfile.write ('    int32_t size = oldOffset + sizeof(newVal);\n')
        cfile.write ('\n')
        cfile.write ('    if (buffCap < size)\n')
        cfile.write ('    {\n')
        cfile.write ('        retVal = -1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        char *buffptr = (char *)& (buffer [oldOffset]);\n')
        cfile.write ('        strcpy (buffptr, newVal);\n')
        cfile.write ('        retVal = oldOffset + strlen (newVal) + 1;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.FLOAT]:
        cfile.write ('// Add a float to the buffer\n')
        cfile.write ('// Returns -1 if the buffer is not big enough\n')
        cfile.write ('// Returns the new offset in the buffer on success\n')
        cfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddFloatToBuffer') + ' (uint8_t *buffer, float newVal, int32_t oldOffset, int32_t buffCap)\n')
        cfile.write ('{\n')
        cfile.write ('    union {\n')
        cfile.write ('        float f;\n')
        cfile.write ('        uint32_t u32;\n')
        cfile.write ('    } val = { .f = newVal };\n')
        cfile.write ('    return ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU32ToBuffer') + ' (buffer, val.u32, oldOffset, buffCap);\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.DOUBLE]:
        cfile.write ('// Add a double to the buffer\n')
        cfile.write ('// Returns -1 if the buffer is not big enough\n')
        cfile.write ('// Returns the new offset in the buffer on success\n')
        cfile.write ('int32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddDoubleToBuffer') + ' (uint8_t *buffer, double newVal, int32_t oldOffset, int32_t buffCap)\n')
        cfile.write ('{\n')
        cfile.write ('    union {\n')
        cfile.write ('        double d;\n')
        cfile.write ('        uint64_t u64;\n')
        cfile.write ('    } val = { .d = newVal };\n')
        cfile.write ('    return ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU64ToBuffer') + ' (buffer, val.u64, oldOffset, buffCap);\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    cfile.write ('// ------- //\n')
    cfile.write ('// READERS //\n')
    cfile.write ('// ------- //\n')
    cfile.write ('\n')
    if hasArgOfType[ArArgType.U8] or hasArgOfType[ArArgType.I8]:
        cfile.write ('// Read an 8 bit value from the buffer\n')
        cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        cfile.write ('uint8_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    uint8_t retVal = 0;\n')
        cfile.write ('    int newOffset = *offset + sizeof (uint8_t);\n')
        cfile.write ('    if (newOffset > capacity)\n')
        cfile.write ('    {\n')
        cfile.write ('        *error = 1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        retVal = buffer [*offset];\n')
        cfile.write ('        *offset = newOffset;\n')
        cfile.write ('        *error = 0;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U16] or hasArgOfType[ArArgType.I16]:
        cfile.write ('// Read a 16 bit value from the buffer\n')
        cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        cfile.write ('uint16_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    uint16_t retVal = 0;\n')
        cfile.write ('    const uint8_t *buffAddr = &buffer[*offset];\n')
        cfile.write ('    int newOffset = *offset + sizeof (uint16_t);\n')
        cfile.write ('    if (newOffset > capacity)\n')
        cfile.write ('    {\n')
        cfile.write ('        *error = 1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        memcpy (&retVal, buffAddr, sizeof (uint16_t));\n')
        cfile.write ('        retVal = dtohs (retVal);\n')
        cfile.write ('        *offset = newOffset;\n')
        cfile.write ('        *error = 0;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U32] or hasArgOfType[ArArgType.I32] or hasArgOfType[ArArgType.ENUM]:
        cfile.write ('// Read a 32 bit value from the buffer\n')
        cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        cfile.write ('uint32_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read32FromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    uint32_t retVal = 0;\n')
        cfile.write ('    const uint8_t *buffAddr = &buffer[*offset];\n')
        cfile.write ('    int newOffset = *offset + sizeof (uint32_t);\n')
        cfile.write ('    if (newOffset > capacity)\n')
        cfile.write ('    {\n')
        cfile.write ('        *error = 1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        memcpy (&retVal, buffAddr, sizeof (uint32_t));\n')
        cfile.write ('        retVal = dtohl (retVal);\n')
        cfile.write ('        *offset = newOffset;\n')
        cfile.write ('        *error = 0;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U64] or hasArgOfType[ArArgType.I64]:
        cfile.write ('// Read a 64 bit value from the buffer\n')
        cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        cfile.write ('uint64_t ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read64FromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    uint64_t retVal = 0;\n')
        cfile.write ('    const uint8_t *buffAddr = &buffer[*offset];\n')
        cfile.write ('    int newOffset = *offset + sizeof (uint64_t);\n')
        cfile.write ('    if (newOffset > capacity)\n')
        cfile.write ('    {\n')
        cfile.write ('        *error = 1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        memcpy (&retVal, buffAddr, sizeof (uint64_t));\n')
        cfile.write ('        retVal = dtohll (retVal);\n')
        cfile.write ('        *offset = newOffset;\n')
        cfile.write ('        *error = 0;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.FLOAT]:
        cfile.write ('// Read a float value from the buffer\n')
        cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        cfile.write ('float ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadFloatFromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    float retVal = 0;\n')
        cfile.write ('    const uint8_t *buffAddr = &buffer[*offset];\n')
        cfile.write ('    int newOffset = *offset + sizeof (float);\n')
        cfile.write ('    if (newOffset > capacity)\n')
        cfile.write ('    {\n')
        cfile.write ('        *error = 1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        memcpy (&retVal, buffAddr, sizeof (float));\n')
        cfile.write ('        retVal = dtohf (retVal);\n')
        cfile.write ('        *offset = newOffset;\n')
        cfile.write ('        *error = 0;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.DOUBLE]:
        cfile.write ('// Read a double value from the buffer\n')
        cfile.write ('// On error, return zero and set *error to 1, else set *error to 0\n')
        cfile.write ('double ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadDoubleFromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    double retVal = 0;\n')
        cfile.write ('    const uint8_t *buffAddr = &buffer[*offset];\n')
        cfile.write ('    int newOffset = *offset + sizeof (double);\n')
        cfile.write ('    if (newOffset > capacity)\n')
        cfile.write ('    {\n')
        cfile.write ('        *error = 1;\n')
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        memcpy (&retVal, buffAddr, sizeof (double));\n')
        cfile.write ('        retVal = dtohd (retVal);\n')
        cfile.write ('        *offset = newOffset;\n')
        cfile.write ('        *error = 0;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.STRING]:
        cfile.write ('// Read a string value from the buffer\n')
        cfile.write ('// On error, return NULL and set *error to 1, else set *error to 0\n')
        cfile.write ('const char* ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'ReadStringFromBuffer') + ' (const uint8_t *buffer, int32_t capacity, int32_t *offset, int32_t *error)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    const char *retVal = NULL;\n')
        cfile.write ('    const char *buffAddr = (char *)&buffer[*offset];\n')
        cfile.write ('    int newOffset = *offset;\n')
        cfile.write ('    while ((newOffset < capacity) && (\'\\0\' != (char) buffer [newOffset]))\n')
        cfile.write ('    {\n')
        cfile.write ('        newOffset += sizeof (char);\n');
        cfile.write ('    }\n')
        cfile.write ('    if (newOffset >= capacity)\n')
        cfile.write ('    {\n')
        cfile.write ('        *error = 1;\n');
        cfile.write ('    }\n')
        cfile.write ('    else\n')
        cfile.write ('    {\n')
        cfile.write ('        retVal = buffAddr;\n')
        cfile.write ('        *offset = newOffset + 1;\n')
        cfile.write ('        *error = 0;\n')
        cfile.write ('    }\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    cfile.write ('// -------- //\n')
    cfile.write ('// TOSTRING //\n')
    cfile.write ('// -------- //\n')
    cfile.write ('\n')
    if (hasArgOfType[ArArgType.U8]  or hasArgOfType[ArArgType.I8] or
        hasArgOfType[ArArgType.U16] or hasArgOfType[ArArgType.I16] or
        hasArgOfType[ArArgType.U32] or hasArgOfType[ArArgType.I32] or
        hasArgOfType[ArArgType.U64] or hasArgOfType[ArArgType.I64] or
        hasArgOfType[ArArgType.FLOAT] or hasArgOfType[ArArgType.DOUBLE] or
        hasArgOfType[ArArgType.STRING] or hasArgOfType[ArArgType.ENUM]):
        cfile.write ('// Write a string in a buffer\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (const char *stringToWrite, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int retVal = -1;\n')
        cfile.write ('    int capacity = outputLen - outputOffset - 1;\n')
        cfile.write ('    int len = strlen (stringToWrite);\n')
        cfile.write ('    if (capacity >= len)\n')
        cfile.write ('    {\n')
        cfile.write ('        strncat (output, stringToWrite, len);\n')
        cfile.write ('        retVal = outputOffset + len;\n')
        cfile.write ('    } // No else --> If capacity is not enough, keep retVal to -1\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U8]:
        cfile.write ('// Write a string in a buffer from an uint8_t arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU8') + ' (const char *name, uint8_t arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = -1;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('#if HAVE_DECL_PRIU8\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%" PRIu8, arg);\n')
        cfile.write ('#else\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%u", arg);\n')
        cfile.write ('#endif\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.I8]:
        cfile.write ('// Write a string in a buffer from an int8_t arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI8') + ' (const char *name, int8_t arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = -1;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('#if HAVE_DECL_PRII8\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%" PRIi8, arg);\n')
        cfile.write ('#else\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%d", arg);\n')
        cfile.write ('#endif\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U16]:
        cfile.write ('// Write a string in a buffer from an uint16_t arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU16') + ' (const char *name, uint16_t arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = -1;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('#if HAVE_DECL_PRIU16\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%" PRIu16, arg);\n')
        cfile.write ('#else\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%u", arg);\n')
        cfile.write ('#endif\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.I16]:
        cfile.write ('// Write a string in a buffer from an int16_t arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI16') + ' (const char *name, int16_t arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = offset;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('#if HAVE_DECL_PRII16\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%" PRIi16, arg);\n')
        cfile.write ('#else\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%d", arg);\n')
        cfile.write ('#endif\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U32]:
        cfile.write ('// Write a string in a buffer from an uint32_t arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU32') + ' (const char *name, uint32_t arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = offset;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('#if HAVE_DECL_PRIU32\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%" PRIu32, arg);\n')
        cfile.write ('#else\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%u", arg);\n')
        cfile.write ('#endif\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.I32] or hasArgOfType[ArArgType.ENUM]:
        cfile.write ('// Write a string in a buffer from an int32_t arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI32') + ' (const char *name, int32_t arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = offset;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('#if HAVE_DECL_PRII32\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%" PRIi32, arg);\n')
        cfile.write ('#else\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%d", arg);\n')
        cfile.write ('#endif\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.U64]:
        cfile.write ('// Write a string in a buffer from an uint64_t arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintU64') + ' (const char *name, uint64_t arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = offset;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('#if HAVE_DECL_PRIU64\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%" PRIu64, arg);\n')
        cfile.write ('#else\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%llu", arg);\n')
        cfile.write ('#endif\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.I64]:
        cfile.write ('// Write a string in a buffer from an int64_t arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintI64') + ' (const char *name, int64_t arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = offset;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('#if HAVE_DECL_PRII64\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%" PRIi64, arg);\n')
        cfile.write ('#else\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%lld", arg);\n')
        cfile.write ('#endif\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.FLOAT]:
        cfile.write ('// Write a string in a buffer from float arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintFloat') + ' (const char *name, float arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = offset;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%f", arg);\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.DOUBLE]:
        cfile.write ('// Write a string in a buffer from a double arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintDouble') + ' (const char *name, double arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int capacity, len;\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    int retVal = offset;\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        capacity = outputLen - offset - 1;\n')
        cfile.write ('        len = snprintf (& output [offset], capacity, "%f", arg);\n')
        cfile.write ('        if (len >= capacity)\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = -1;\n')
        cfile.write ('        }\n')
        cfile.write ('        else\n')
        cfile.write ('        {\n')
        cfile.write ('            retVal = offset + len;\n')
        cfile.write ('        }\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return retVal;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    if hasArgOfType[ArArgType.STRING]:
        cfile.write ('// Write a string in a buffer from a string arg\n')
        cfile.write ('// On error, return -1, else return offset in string\n')
        cfile.write ('int ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'PrintString') + ' (const char *name, const char *arg, char *output, int outputLen, int outputOffset)\n')
        cfile.write ('{\n')
        cfile.write ('    // We don\'t check args because this function is only called by autogenerated code\n')
        cfile.write ('    int offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (name, output, outputLen, outputOffset);\n')
        cfile.write ('    if (offset >= 0)\n')
        cfile.write ('    {\n')
        cfile.write ('        offset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' (arg, output, outputLen, offset);\n')
        cfile.write ('    } // No else --> Do nothing if the previous WriteString failed\n')
        cfile.write ('    return offset;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
    cfile.close ()

    #################################
    # 4TH PART :                    #
    #################################
    # Generate public Types H file  #
    #################################

    hfile = open (paths.COMMANDSTYPES_HFILE, 'w')

    hfile.write ('// ARSDK_NO_ENUM_PREPROCESS //')
    hfile.write ('\n')
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
    hfile.write ('/**\n')
    hfile.write (' * @brief Size of the ARCommands header.\n')
    hfile.write (' * This is the minimum size of a zero-arg command.\n')
    hfile.write (' * The size of a command is equal to this, plus the size\n')
    hfile.write (' * of its arguments.\n')
    hfile.write (' */\n')
    hfile.write ('#define ' + ARMacroName (LIB_MODULE, 'HEADER', 'SIZE') + ' (4)\n')
    hfile.write ('\n')

    if genDebug:
        hfile.write ('/**\n')
        hfile.write (' * Defined only if the library includes debug commands\n')
        hfile.write (' */\n')
        hfile.write ('#define ' + ARMacroName (LIB_MODULE, 'HAS', 'DEBUG_COMMANDS') + ' (1)\n')
        hfile.write ('\n')

    for ftr in allFeatures:
        hfile.write ('// Feature ' + get_ftr_old_name(ftr) + '\n')
        for enum in ftr.enums:
            submodules=get_ftr_old_name(ftr).upper()
            macro_name=enum.name.upper();
            hfile.write ('\n/**\n')
            hfile.write (' * @brief ' + enum.doc.replace('\n', '\\n') + '\n')
            hfile.write (' */\n')
            hfile.write ('typedef enum\n')
            hfile.write ('{\n')
            first = True
            for eVal in enum.values:
                hfile.write ('    ' + AREnumValue (LIB_MODULE, submodules, macro_name, eVal.name))

                if eVal.value:
                    hfile.write (' = ' + str(eVal.value))
                elif first:
                    hfile.write (' = 0')
                hfile.write (',    ///< ' + eVal.doc.replace('\n', '\\n') + '\n')
                first = False

            hfile.write ('    ' + AREnumValue (LIB_MODULE, submodules, macro_name, 'MAX') + '\n')
            hfile.write ('} ' + AREnumName (LIB_MODULE, submodules, macro_name) + ';\n\n')

            #If the enum is used as bit field
            if enum.usedLikeBitfield:
                #Generate bit field flags
                for eVal in enum.values:
                    base = 'UINT32_C(1)' if len(enum.values) <= 32 else 'UINT64_C(1)'
                    hfile.write ('#define ' + ARFlagValue (LIB_MODULE, submodules, macro_name, eVal.name) + ' ('+base+' << '+AREnumValue (LIB_MODULE, submodules, macro_name, eVal.name)+ ')    ///< ' + eVal.doc.replace('\n', '\\n') + '\n')
                hfile.write ('\n')
    for ftr in allFeatures:
        hfile.write ('// Feature ' + get_ftr_old_name(ftr) + '\n')
        for multiset in ftr.multisets:
            submodules=get_ftr_old_name(ftr)
            hfile.write ('\n/**\n')
            hfile.write (' * @brief ' + multiset.doc.replace('\n', '\\n') + '\n')
            hfile.write (' */\n')
            hfile.write ('typedef struct\n')
            hfile.write ('{\n')
            for msg in multiset.msgs:
                hfile.write ('    struct\n')
                hfile.write ('    {\n')
                hfile.write ('        uint8_t isSet;\n')
                for arg in msg.args:
                    hfile.write ('        '+xmlToC (LIB_MODULE, msg.ftr, msg, arg) +' '+arg.name+';\n')
                hfile.write ('    } '+msg.name+';\n')
                hfile.write ('\n')

            hfile.write ('} ' + ARTypeName (LIB_MODULE, submodules, multiset.name) + ';\n\n')
    hfile.write ('\n')
    hfile.write ('#endif /* ' + COMMANDSTYPES_DEFINE + ' */\n')

    hfile.close ()

    #################################
    # 5TH PART :                    #
    #################################
    # Generate public coder H file  #
    #################################

    hfile = open (paths.COMMANDSGEN_HFILE, 'w')

    hfile.write (LICENCE_HEADER)
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
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Error codes for ' + ARFunctionName (LIB_MODULE, GEN_SUBMODULE, 'GenerateCommand') + ' functions\n')
    hfile.write (' */\n')
    hfile.write ('typedef enum {\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ' = 0, ///< No error occured\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ', ///< At least one of the arguments is invalid\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ', ///< The given output buffer was not large enough for the command\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR') + ', ///< Any other error\n')
    hfile.write ('} ' +  AREnumName (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ';\n')
    hfile.write ('\n')
    hfile.write ('\n')
    for ftr in allFeatures:
        hfile.write ('// Feature ' + get_ftr_old_name(ftr) + '\n\n')
        for cmd in ftr.cmds + ftr.evts:
            hfile.write ('\n/**\n')
            hfile.write (' * @brief ' + cmd.doc.desc.replace('\n', '\n * ') + '\n')
            if cmd.isDeprecated:
                hfile.write (' * @deprecated\n')
            hfile.write (' * @warning A command is not NULL terminated and can contain NULL bytes.\n')
            hfile.write (' * @param buffer Pointer to the buffer in which the library should store the command\n')
            hfile.write (' * @param buffLen Size of the buffer\n')
            hfile.write (' * @param cmdLen Pointer to an integer that will hold the actual size of the command\n')
            for arg in cmd.args:
                hfile.write (' * @param _' + arg.name + ' ' + get_arg_doc(arg).replace('\n', '\\n') + '\n')
                #If the argument is a bitfield
                if isinstance(arg.argType, ArBitfield):
                    hfile.write (' * @param _' + arg.name + ' a combination of')

                    #Find the feature owning the enum
                    for bitFieldFtr in allFeatures:
                        for enum2 in bitFieldFtr.enums:
                            if enum2 == arg.argType.enum:
                                break;
                        else:
                            continue
                        break

                    for eVal in arg.argType.enum.values:
                        hfile.write (' ; ' + ARFlagValue(LIB_MODULE, bitFieldFtr.name , arg.argType.enum.name, eVal.name))
                    hfile.write ('\n')
            hfile.write (' * @return Error code (see ' + AREnumName (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ')\n')
            hfile.write (' */\n')
            hfile.write (AREnumName (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ' ' + ARFunctionName (LIB_MODULE, GEN_SUBMODULE, 'Generate' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd))) + ' (uint8_t *buffer, int32_t buffLen, int32_t *cmdLen')
            for arg in cmd.args:
                hfile.write (', ' + xmlToCwithConst (LIB_MODULE, ftr, cmd, arg, True) + ' _' + arg.name)
            hfile.write (');\n')
        hfile.write ('\n')

        hfile.write ('\n')

    hfile.write ('\n')
    hfile.write ('#endif /* ' + COMMANDSGEN_DEFINE + ' */\n')

    hfile.close ()

    #################################
    # 6TH PART :                    #
    #################################
    # Generate coder C part         #
    #################################

    cfile = open (paths.COMMANDSGEN_CFILE, 'w')

    cfile.write (LICENCE_HEADER)
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
    cfile.write ('#include "' + COMMANDSRW_HFILE_NAME + '"\n')
    cfile.write ('#include <' + COMMANDSTYPES_HFILE_NAME + '>\n')
    cfile.write ('#include <' + COMMANDSGEN_HFILE_NAME + '>\n')
    cfile.write ('#include <' + COMMANDSID_HFILE_NAME + '>\n')
    cfile.write ('\n')

    for ftr in allFeatures:
        cfile.write ('// Feature ' + get_ftr_old_name(ftr) + '\n\n')
        for cmd in ftr.cmds + ftr.evts:
            cfile.write (AREnumName (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ' ' + ARFunctionName (LIB_MODULE, GEN_SUBMODULE, 'Generate' +  ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd))) + ' (uint8_t *buffer, int32_t buffLen, int32_t *cmdLen')
            for arg in cmd.args:
                cfile.write (', ' + xmlToCwithConst (LIB_MODULE, ftr, cmd, arg, True) + ' _' + arg.name)
            cfile.write (')\n')
            cfile.write ('{\n')
            cfile.write ('    int32_t currIndexInBuffer = 0;\n')

            if [arg for arg in cmd.args if isinstance(arg.argType, ArMultiSetting)]:
                    cfile.write ('    int32_t currFreeSizeInBuffer = 0;\n')
                    cfile.write ('    int32_t multisetSize = 0;\n')
                    cfile.write ('    int32_t multisetSizeIndex = 0;\n')
                    cfile.write ('    int32_t cmdSize = 0;\n')
                    cfile.write ('    int32_t cmdSizeIndex = 0;\n')
                    cfile.write ('    int32_t cmdIndex = 0;\n')

            cfile.write ('    ' + AREnumName (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ' retVal = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ';\n')
            cfile.write ('    if ((buffer == NULL) ||\n')
            for arg in cmd.args:
                if isinstance(arg.argType, ArMultiSetting):
                    cfile.write ('        (_' + arg.name + ' == NULL) ||\n')
            cfile.write ('        (cmdLen == NULL))\n')
            cfile.write ('    {\n')
            cfile.write ('        return ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ';\n')
            cfile.write ('    } // No else --> Args Check\n')
            hasStringArgs = False
            for arg in cmd.args:
                if arg.argType == ArArgType.STRING:
                    hasStringArgs = True
                    break
            if hasStringArgs:
                cfile.write ('    // Test all String args (if any)\n')
                cfile.write ('    if (')
                first = True
                for arg in cmd.args:
                    if ArArgType.STRING == arg.argType:
                        if first:
                            first = False
                        else:
                            cfile.write ('        ')
                        cfile.write ('(_' + arg.name + ' == NULL) ||\n')
                cfile.write ('       (0))\n')
                cfile.write ('    {\n')
                cfile.write ('        return ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'BAD_ARGS') + ';\n')
                cfile.write ('    } // No else --> Args Check\n')
                cfile.write ('\n')
            cfile.write ('    // Write feature header\n')
            cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        currIndexInBuffer = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU8ToBuffer') + ' (buffer, ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(ftr)) + ', currIndexInBuffer, buffLen);\n')
            cfile.write ('        if (currIndexInBuffer == -1)\n')
            cfile.write ('        {\n')
            cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
            cfile.write ('        } // No else --> Do not modify retVal if no issue was found\n')
            cfile.write ('    } // No else --> Processing block\n')
            cfile.write ('    // Write class header\n')
            cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            classIdName = ARMacroName (LIB_MODULE, ID_SUBMODULE, 'FEATURE_CLASS') if cmd.cls is None else AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CLASS', cmd.cls.name)
            cfile.write ('        currIndexInBuffer = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU8ToBuffer') + ' (buffer, ' + classIdName + ', currIndexInBuffer, buffLen);\n')
            cfile.write ('        if (currIndexInBuffer == -1)\n')
            cfile.write ('        {\n')
            cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
            cfile.write ('        } // No else --> Do not modify retVal if no issue was found\n')
            cfile.write ('    } // No else --> Processing block\n')
            cfile.write ('    // Write id header\n')
            cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')

            cmdIdName = AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CMD', cmd.name) if cmd.cls is None else AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_' + cmd.cls.name + '_CMD', cmd.name)

            cfile.write ('        currIndexInBuffer = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU16ToBuffer') + ' (buffer, ' + cmdIdName + ', currIndexInBuffer, buffLen);\n')
            cfile.write ('        if (currIndexInBuffer == -1)\n')
            cfile.write ('        {\n')
            cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
            cfile.write ('        } // No else --> Do not modify retVal if no issue was found\n')
            cfile.write ('    } // No else --> Processing block\n')
            for arg in cmd.args:
                if isinstance(arg.argType, ArMultiSetting):
                    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        multisetSizeIndex = currIndexInBuffer;\n')
                    cfile.write ('        currIndexInBuffer += sizeof(uint16_t);\n')
                    cfile.write ('    }\n')
                    cfile.write ('\n')
                    for multiset_msg in arg.argType.msgs:
                        cfile.write ('    if ((retVal == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ') && (_'+arg.name+'->'+multiset_msg.name+'.isSet))\n')
                        cfile.write ('    {\n')
                        cfile.write ('        cmdSizeIndex = currIndexInBuffer;\n')
                        cfile.write ('        cmdIndex = cmdSizeIndex + sizeof(uint16_t);\n')
                        cfile.write ('        currFreeSizeInBuffer = buffLen - cmdIndex;\n')
                        cfile.write ('        // Write the command\n')
                        cfile.write ('        retVal = ' + ARFunctionName (LIB_MODULE, GEN_SUBMODULE, 'Generate' +  ARCapitalize (get_ftr_old_name(multiset_msg.ftr)) + ARCapitalize (format_cmd_name(multiset_msg))) + ' (buffer + cmdIndex, currFreeSizeInBuffer, &cmdSize')
                        for multiset_msg_arg in multiset_msg.args:
                            cfile.write (', _' + arg.name+'->'+multiset_msg.name+'.'+multiset_msg_arg.name)
                        cfile.write (');\n')
                        cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') +')\n')
                        cfile.write ('        {\n')
                        cfile.write ('            // Write command size before the command\n')
                        cfile.write ('            currIndexInBuffer = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU16ToBuffer') + ' (buffer, cmdSize, cmdSizeIndex, cmdSizeIndex + sizeof(uint16_t));\n')
                        cfile.write ('            if (currIndexInBuffer == -1)\n')
                        cfile.write ('                retVal = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                        cfile.write ('            // Update current Index\n')
                        cfile.write ('            currIndexInBuffer += cmdSize;\n')
                        cfile.write ('            // Update Multiset Size\n')
                        cfile.write ('            multisetSize += sizeof(uint16_t) + cmdSize;\n')
                        cfile.write ('        }\n')
                        cfile.write ('    } // No else --> Processing block\n')
                        cfile.write ('\n')
                    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        // Write multiset size before all commands\n')
                    cfile.write ('        multisetSizeIndex = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'AddU16ToBuffer') + ' (buffer, multisetSize, multisetSizeIndex, multisetSizeIndex + sizeof(uint16_t));\n')
                    cfile.write ('        if (multisetSizeIndex == -1)\n')
                    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                    cfile.write ('    }\n')
                    cfile.write ('\n')
                else:
                    cfile.write ('    // Write arg _' + arg.name + '\n')
                    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        currIndexInBuffer = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Add' + xmlToSize (ftr, cmd, arg) + 'ToBuffer') + ' (buffer, _' + arg.name + ', currIndexInBuffer, buffLen);\n')
                    cfile.write ('        if (currIndexInBuffer == -1)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                    cfile.write ('        } // No else --> Do not modify retVal if no issue was found\n')
                    cfile.write ('    } // No else --> Processing block\n')
            cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        *cmdLen = currIndexInBuffer;\n')
            cfile.write ('    } // No else --> Do not set cmdLen if an error occured\n')
            cfile.write ('    return retVal;\n')
            cfile.write ('}\n\n')
        cfile.write ('\n')

    cfile.write ('\n')
    cfile.write ('// END GENERATED CODE\n')

    cfile.close ()

    #################################
    # 7TH PART :                    #
    #################################
    # Generate public decoder H file#
    #################################

    hfile = open (paths.COMMANDSDEC_HFILE, 'w')

    hfile.write (LICENCE_HEADER)
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
    hfile.write ('#ifdef __GNUC__\n')
    hfile.write ('#define DEPRECATED __attribute__ ((deprecated))\n')
    hfile.write ('#elif defined(_MSC_VER)\n')
    hfile.write ('#define DEPRECATED __declspec(deprecated)\n' )
    hfile.write ('#else\n')
    hfile.write ('#define DEPRECATED\n')
    hfile.write ('#endif\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Error codes for ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeBuffer') + ' function\n')
    hfile.write (' */\n')
    hfile.write ('typedef enum {\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' = 0, ///< No error occured\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NO_CALLBACK') + ', ///< No error, but no callback was set (so the call had no effect)\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ', ///< The command buffer contained an unknown command\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ', ///< The command buffer did not contain enough data for the specified command\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ', ///< The string buffer was not big enough for the command description\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ', ///< Any other error\n')
    hfile.write ('} ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ';\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief ARCOMMANDS_Decoder object holder\n')
    hfile.write (' */\n')
    hfile.write ('typedef struct ARCOMMANDS_Decoder_t ARCOMMANDS_Decoder_t;\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Creates a new ARCOMMANDS_Decoder_t\n')
    hfile.write (' * @warning This function allocates memory.\n')
    hfile.write (' * @note The memory must be freed by a call to ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DeleteDecoder') + '.\n')
    hfile.write (' * @return A new ARCOMMANDS_Decoder_t instance. NULL in case of error.\n')
    hfile.write (' */\n')
    hfile.write ('ARCOMMANDS_Decoder_t* ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'NewDecoder') + ' (' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' *error);\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Deletes an ARCOMMANDS_Decoder_t\n')
    hfile.write (' * @param decoder The Decoder to delete.\n')
    hfile.write (' */\n')
    hfile.write ('void ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DeleteDecoder') + ' (ARCOMMANDS_Decoder_t **decoder);\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Decode a comand\n')
    hfile.write (' * On success, the callback set for the command will be called in the current thread.\n')
    hfile.write (' * @param decoder the decoder instance\n')
    hfile.write (' * @param buffer the command buffer to decode\n')
    hfile.write (' * @param buffLen the length of the command buffer\n')
    hfile.write (' * @return ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' on success, any error code otherwise\n')
    hfile.write (' */\n')
    hfile.write (AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
    hfile.write (ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeCommand') + ' (ARCOMMANDS_Decoder_t *decoder, const uint8_t *buffer, int32_t buffLen);\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Decode a comand buffer\n')
    hfile.write (' * On success, the callback set for the command will be called in the current thread.\n')
    hfile.write (' * @param buffer the command buffer to decode\n')
    hfile.write (' * @param buffLen the length of the command buffer\n')
    hfile.write (' * @return ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' on success, any error code otherwise\n')
    hfile.write (' */\n')
    hfile.write (AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
    hfile.write (ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeBuffer') + ' (const uint8_t *buffer, int32_t buffLen) DEPRECATED;\n')
    hfile.write ('\n')
    hfile.write ('\n/**\n')
    hfile.write (' * @brief Describe a comand buffer\n')
    hfile.write (' * @param buffer the command buffer to decode\n')
    hfile.write (' * @param buffLen the length of the command buffer\n')
    hfile.write (' * @param resString the string pointer in which the description will be stored\n')
    hfile.write (' * @param stringLen the length of the string pointer\n')
    hfile.write (' * @return ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' on success, any error code otherwise\n')
    hfile.write (' */\n')
    hfile.write (AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
    hfile.write (ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DescribeBuffer') + ' (uint8_t *buffer, int32_t buffLen, char *resString, int32_t stringLen);\n')
    hfile.write ('\n')
    for ftr in allFeatures:
        hfile.write ('// Feature ' + get_ftr_old_name(ftr) + '\n\n')
        for cmd in ftr.cmds + ftr.evts:
            for  multiset_arg in [arg for arg in cmd.args if isinstance(arg.argType, ArMultiSetting)]:
                hfile.write ('/**\n')
                hfile.write (' * @brief Decode a '+ARTypeName (LIB_MODULE, get_ftr_old_name(ftr), multiset_arg.argType.name)+'\n')
                hfile.write (' * On success, the callback set for each commands of the multisetting will be called in the current thread.\n')
                hfile.write (' * @param decoder the decoder instance\n')
                hfile.write (' * @param multisetting the multisetting to decode\n')
                hfile.write (' * @return ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' on success, any error code otherwise\n')
                hfile.write (' */\n')
                hfile.write (AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
                hfile.write (ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Decode'+ARCapitalize(ftr.name)+ARCapitalize(cmd.name)) + ' (ARCOMMANDS_Decoder_t *decoder, '+xmlToC(LIB_MODULE, ftr, cmd, multiset_arg, True)+' multisetting);\n')
                hfile.write ('\n')

            brefCmdName = cmd.name if cmd.cls is None else cmd.cls.name + '.' + cmd.name
            hfile.write ('\n/**\n')
            hfile.write (' * @brief callback type for the command ' + get_ftr_old_name(ftr) + '.' + brefCmdName + '\n')
            hfile.write (' */\n')
            hfile.write ('typedef void (*' + ARTypeName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ') (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    hfile.write (', ')
                hfile.write (xmlToCcharAreConst (LIB_MODULE, ftr, cmd, arg, True) + ' ' + arg.name)
            if not first:
                hfile.write (', ')
            hfile.write ('void *custom);\n')
            hfile.write ('/**\n')
            hfile.write (' * @brief Set Decoder callback setter for the command ' + get_ftr_old_name(ftr) + '.' + brefCmdName + '\n')
            hfile.write (' * @param decoder the decoder instance\n')
            hfile.write (' * @param callback new callback for the command ' + get_ftr_old_name(ftr) + '.' + brefCmdName + '\n')
            hfile.write (' * @param custom pointer that will be passed to all calls to the callback\n')
            hfile.write (' */\n')
            hfile.write ('void ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Cb') + ' (ARCOMMANDS_Decoder_t *decoder, ' + ARTypeName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' callback, void *custom);\n')
            hfile.write ('/**\n')
            hfile.write (' * @brief callback setter for the command ' + get_ftr_old_name(ftr) + '.' + brefCmdName + '\n')
            hfile.write (' * @param callback new callback for the command ' + get_ftr_old_name(ftr) + '.' + brefCmdName + '\n')
            hfile.write (' * @param custom pointer that will be passed to all calls to the callback\n')
            hfile.write (' */\n')
            hfile.write ('void ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' (' + ARTypeName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' callback, void *custom) DEPRECATED;\n')
        hfile.write ('\n')

    hfile.write ('#endif /* ' + COMMANDSDEC_DEFINE + ' */\n')

    hfile.close ()

    #################################
    # 8TH PART :                    #
    #################################
    # Generate decoder C part       #
    #################################

    cfile = open (paths.COMMANDSDEC_CFILE, 'w')

    cfile.write (LICENCE_HEADER)
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
    cfile.write ('#include <stdio.h>\n')
    cfile.write ('#include "' + COMMANDSRW_HFILE_NAME + '"\n')
    cfile.write ('#include <' + COMMANDSTYPES_HFILE_NAME + '>\n')
    cfile.write ('#include <' + COMMANDSDEC_HFILE_NAME + '>\n')
    cfile.write ('#include <' + COMMANDSID_HFILE_NAME + '>\n')
    cfile.write ('#include <libARSAL/ARSAL_Mutex.h>\n')
    cfile.write ('\n')
    cfile.write ('// ARCOMMANDS_Decoder_t structure definition\n')
    cfile.write ('struct ARCOMMANDS_Decoder_t\n')
    cfile.write ('{\n')
    cfile.write ('    ARSAL_Mutex_t mutex;\n\n')
    for ftr in allFeatures:
        cfile.write ('    // Feature ' + get_ftr_old_name(ftr) + '\n')
        for cmd in ftr.cmds + ftr.evts:
            cfile.write('    ' + ARTypeName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' ' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(format_cmd_name(cmd)) + 'Callback;\n')
            cfile.write('    void *' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(format_cmd_name(cmd)) + 'Custom;\n');
        cfile.write ('\n')
    cfile.write ('};\n')
    cfile.write ('\n')
    cfile.write ('\n')
    cfile.write ('// Constructor\n')
    cfile.write ('ARCOMMANDS_Decoder_t* ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'NewDecoder') + ' (' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' *error)\n')
    cfile.write ('{\n')
    cfile.write ('    ARCOMMANDS_Decoder_t *decoder = NULL;\n')
    cfile.write ('    int err = 0;\n')
    cfile.write ('\n')
    cfile.write ('    decoder = calloc(1, sizeof(*decoder));\n')
    cfile.write ('    if (decoder == NULL)\n')
    cfile.write ('        goto end;\n')
    cfile.write ('\n')
    cfile.write ('    err = ARSAL_Mutex_Init (&decoder->mutex);\n')
    cfile.write ('    if (err != 0) {\n')
    cfile.write ('        free(decoder);\n')
    cfile.write ('        decoder = NULL;\n')
    cfile.write ('    }\n')
    cfile.write ('\n')
    cfile.write ('end:\n')
    cfile.write ('    if (error)\n')
    cfile.write ('        *error = decoder ? ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' : ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
    cfile.write ('\n');
    cfile.write ('    return decoder;\n')
    cfile.write ('}\n\n')
    cfile.write ('// Destructor\n')
    cfile.write ('void ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DeleteDecoder') + ' (ARCOMMANDS_Decoder_t **decoder)\n')
    cfile.write ('{\n')
    cfile.write ('    if (decoder && (*decoder)) {\n');
    cfile.write ('        ARSAL_Mutex_Destroy(&(*decoder)->mutex);\n')
    cfile.write ('        free(*decoder);\n');
    cfile.write ('        *decoder = NULL;\n');
    cfile.write ('    }\n');
    cfile.write ('}\n\n')
    cfile.write ('// CALLBACK VARIABLES + SETTERS\n')
    cfile.write ('\n')
    cfile.write ('static ARSAL_Mutex_t ' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'mutex') + ';\n')
    cfile.write ('static int ' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'isInit') + ' = 0;\n')
    cfile.write ('static int ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Init') + ' (void)\n')
    cfile.write ('{\n')
    cfile.write ('    if ((' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'isInit') + ' == 0) &&\n')
    cfile.write ('        (ARSAL_Mutex_Init (&' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'mutex') + ') == 0))\n')
    cfile.write ('    {\n')
    cfile.write ('        ' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'isInit') + ' = 1;\n')
    cfile.write ('    } // No else --> Do nothing if already initialized\n')
    cfile.write ('    return ' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'isInit') + ';\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    for ftr in allFeatures:
        cfile.write ('// Feature ' + get_ftr_old_name(ftr) + '\n\n')
        for cmd in ftr.cmds + ftr.evts:
            for  multiset_arg in [arg for arg in cmd.args if isinstance(arg.argType, ArMultiSetting)]:
                cfile.write (AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
                cfile.write (ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Decode'+ARCapitalize(ftr.name)+ARCapitalize(cmd.name)) + ' (ARCOMMANDS_Decoder_t *decoder, '+xmlToC(LIB_MODULE, ftr, cmd, multiset_arg, True)+' multisetting)\n')
                cfile.write ('{\n')
                cfile.write ('    ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
                cfile.write ('    if ((NULL == decoder) ||\n')
                cfile.write ('        (NULL == multisetting))\n')
                cfile.write ('    {\n')
                cfile.write ('        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
                cfile.write ('    } // No else --> Arg check\n')
                cfile.write ('    \n')
                cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                cfile.write ('    {\n')
                for multiset_msg in multiset_arg.argType.msgs:
                    if multiset_msg.cls:
                        DECODER_CBNAME = ARCapitalize (get_ftr_old_name(multiset_msg.ftr)) + ARCapitalize (multiset_msg.cls.name) + ARCapitalize (multiset_msg.name) + 'Callback'
                        DECODER_CBCUSTOMNAME = ARCapitalize (get_ftr_old_name(multiset_msg.ftr)) + ARCapitalize (multiset_msg.cls.name) + ARCapitalize (multiset_msg.name) + 'Custom'
                    else:
                        DECODER_CBNAME = ARCapitalize (get_ftr_old_name(multiset_msg.ftr)) + ARCapitalize (multiset_msg.name) + 'Callback'
                        DECODER_CBCUSTOMNAME = ARCapitalize (get_ftr_old_name(multiset_msg.ftr)) + ARCapitalize (multiset_msg.name) + 'Custom'

                    cfile.write ('        if ((multisetting->'+multiset_msg.name+'.isSet) && (decoder->' + DECODER_CBNAME + ')) {\n')
                    cfile.write ('            decoder->' + DECODER_CBNAME + ' (')
                    first = True
                    for arg in multiset_msg.args:
                        if first:
                            first = False
                        else:
                            cfile.write (', ')
                        cfile.write ('multisetting->'+multiset_msg.name+'.' + arg.name)
                    if not first:
                        cfile.write (', ')
                    cfile.write ('decoder->' + DECODER_CBCUSTOMNAME + ');\n')
                    cfile.write ('        }\n')
                cfile.write ('    }\n')
                cfile.write ('    return retVal;\n')
                cfile.write ('}\n')
                cfile.write ('\n')

            cfile.write ('void ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Cb') + ' (ARCOMMANDS_Decoder_t *decoder, ' + ARTypeName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' callback, void *custom)\n')
            cfile.write ('{\n')
            cfile.write ('    if (!decoder)\n')
            cfile.write ('        return;\n')
            cfile.write ('\n')
            cfile.write ('    ARSAL_Mutex_Lock (&decoder->mutex);\n')
            cfile.write ('    decoder->' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback = callback;\n')
            cfile.write ('    decoder->' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Custom = custom;\n')
            cfile.write ('    ARSAL_Mutex_Unlock (&decoder->mutex);\n')
            cfile.write ('}\n\n')
            cfile.write ('static ' + ARTypeName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' ' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Cb') + ' = NULL;\n')
            cfile.write ('static void *' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Custom') + ' = NULL;\n')
            cfile.write ('void ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' (' + ARTypeName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' callback, void *custom)\n')
            cfile.write ('{\n')
            cfile.write ('    if (' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Init') + ' () == 1)\n')
            cfile.write ('    {\n')
            cfile.write ('        ARSAL_Mutex_Lock (&' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'mutex') + ');\n')
            cfile.write ('        ' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Cb') + ' = callback;\n')
            cfile.write ('        ' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Custom') + ' = custom;\n')
            cfile.write ('        ARSAL_Mutex_Unlock (&' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'mutex') + ');\n')
            cfile.write ('    } // No else --> do nothing if library can not be initialized\n')
            cfile.write ('}\n')
        cfile.write ('\n')

    for ftr in allFeatures:
        for cmd in [cmdx for cmdx in ftr.cmds + ftr.evts if cmdx.args]:
            cfile.write ('static ' +AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) +' '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'DecodeArgs') + ' (const uint8_t *buffer, int32_t buffLen, int32_t *offset')
            for arg in cmd.args:
                cfile.write (', '+ xmlToCcharAreConst (LIB_MODULE, ftr, cmd, arg) +' *_' + arg.name)
            cfile.write (');\n')
            cfile.write ('static ' +AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) +' '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'DescribeArgs') + ' (uint8_t *buffer, int32_t buffLen, int32_t *offset, char *resString, int32_t strLen, int32_t *strOffset);\n')

    cfile.write ('\n')
    for ftr in allFeatures:
        for cmd in [cmdx for cmdx in ftr.cmds + ftr.evts if cmdx.args]:
            cfile.write ('static ' +AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) +' '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'DecodeArgs') + ' (const uint8_t *buffer, int32_t buffLen, int32_t *offset')
            for arg in cmd.args:
                cfile.write (', '+ xmlToCcharAreConst (LIB_MODULE, ftr, cmd, arg) +' *_' + arg.name)
            cfile.write (')\n')
            cfile.write ('{\n')

            cfile.write ('    ' +AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) +' retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
            cfile.write ('    int32_t error = 0;\n')
            hasMutiSet = bool([arg for arg in cmd.args if isinstance(arg.argType, ArMultiSetting)])
            if hasMutiSet:
                cfile.write ('    eARCOMMANDS_ID_FEATURE commandFeature = -1;\n')
                cfile.write ('    int commandClass = -1;\n')
                cfile.write ('    int commandId = -1;\n')
                cfile.write ('    uint16_t multisetSize = 0;\n')
                cfile.write ('    int32_t multisetEnd = 0;\n')
                cfile.write ('    uint16_t cmdSize = 0;\n')
            cfile.write ('\n')
            cfile.write ('    if ((NULL == buffer)')
            for arg in cmd.args:
                cfile.write (' ||\n        (NULL == _' + arg.name+')')
            cfile.write (')\n')
            cfile.write ('    {\n')
            cfile.write ('        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
            cfile.write ('    } // No else --> Arg check\n')
            cfile.write ('\n')

            for arg in cmd.args:
                if isinstance(arg.argType, ArMultiSetting):
                    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        memset(_' + arg.name+', 0, sizeof(*_' + arg.name+'));\n')
                    cfile.write ('        multisetSize = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('        if (error == 1)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('    } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('    if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        multisetEnd = *offset + multisetSize;\n')
                    cfile.write ('        cmdSize = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('        if (error == 1)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('    } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('    while ((retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ') &&\n')
                    cfile.write ('           (*offset < multisetEnd))\n')
                    cfile.write ('    {\n')
                    cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('        {\n')
                    cfile.write ('            commandFeature = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('            if (error == 1)\n')
                    cfile.write ('            {\n')
                    cfile.write ('                retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('            } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('        } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('        {\n')
                    cfile.write ('            commandClass = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('            if (error == 1)\n')
                    cfile.write ('            {\n')
                    cfile.write ('                retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('            } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('        } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('        {\n')
                    cfile.write ('            commandId = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('            if (error == 1)\n')
                    cfile.write ('            {\n')
                    cfile.write ('                retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('            } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('        } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('        {\n')
                    cfile.write ('            switch (commandFeature)\n')
                    cfile.write ('            {\n')

                    #regroup multisetting msgs by feature and by class
                    multiset_sorted = {}
                    for multiset_msg in arg.argType.msgs:
                        multiset_cl = multiset_msg.cls if multiset_msg.cls else ArClass('defaultCls', 0, '')
                        if not multiset_msg.ftr in multiset_sorted:
                            multiset_sorted[multiset_msg.ftr] = {}
                        if not multiset_cl in multiset_sorted[multiset_msg.ftr]:
                            multiset_sorted[multiset_msg.ftr][multiset_cl] = []
                        multiset_sorted[multiset_msg.ftr][multiset_cl].append(multiset_msg)

                    get_name = lambda x: x.name

                    for multiset_ftr in sorted(list(multiset_sorted.keys()), key=get_name):
                        cfile.write ('            case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(multiset_ftr)) + ':\n')
                        cfile.write ('                switch (commandClass)\n')
                        cfile.write ('                {\n')
                        for multiset_cl in sorted(list(multiset_sorted[multiset_ftr].keys()), key=get_name):
                            cfile.write ('                case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(multiset_ftr) + '_CLASS', multiset_cl.name) + ':\n')
                            cfile.write ('                    switch (commandId)\n')
                            cfile.write ('                    {\n')
                            for multiset_msg in multiset_sorted[multiset_ftr][multiset_cl]:
                                cfile.write ('                    case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(multiset_ftr) + '_' + multiset_cl.name + '_CMD', multiset_msg.name) + ':\n')
                                cfile.write ('                        retVal = '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(multiset_ftr)) + ARCapitalize (format_cmd_name(multiset_msg)) + 'DecodeArgs') + '(buffer, buffLen, offset')
                                for multiset_msg_arg in multiset_msg.args:
                                    cfile.write (', &_' + arg.name+'->'+multiset_msg.name+'.' + multiset_msg_arg.name)
                                cfile.write (');\n')
                                cfile.write ('                        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                                cfile.write ('                        {\n')
                                cfile.write ('                            _' + arg.name+'->'+multiset_msg.name+'.isSet = 1;\n')
                                cfile.write ('                        }\n')
                                cfile.write ('                        break;\n')
                            cfile.write ('                    default:\n')
                            cfile.write ('                        // Command unknown\n')
                            cfile.write ('                        *offset += cmdSize;\n')
                            cfile.write ('                       break;\n')
                            cfile.write ('                    }\n')
                            cfile.write ('                    break;\n')
                        cfile.write ('                default:\n')
                        cfile.write ('                    // Command unknown\n')
                        cfile.write ('                    *offset += cmdSize;\n')
                        cfile.write ('                    break;\n')
                        cfile.write ('                }\n')
                        cfile.write ('                break;\n')

                    cfile.write ('            default:\n')
                    cfile.write ('                // Command unknown\n')
                    cfile.write ('                *offset += cmdSize;\n')
                    cfile.write ('                break;\n')
                    cfile.write ('            }\n')
                    cfile.write ('        }\n')
                    cfile.write ('\n')

                    cfile.write ('        if ((retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ') &&\n')
                    cfile.write ('           (*offset < multisetEnd))\n')
                    cfile.write ('        {\n')
                    cfile.write ('            cmdSize = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('            if (error == 1)\n')
                    cfile.write ('            {\n')
                    cfile.write ('                retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('            } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('        }\n')
                    cfile.write ('    }\n')
                else:
                    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        *_' + arg.name + ' = ' + xmlToReader (ftr, cmd, arg) + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('        if (error == 1)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('    } // No else --> Processing block\n')

            cfile.write ('    return retVal;\n')
            cfile.write ('}\n')
            cfile.write ('\n')

            cfile.write ('static ' +AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) +' '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'DescribeArgs') + ' (uint8_t *buffer, int32_t buffLen, int32_t *offset, char *resString, int32_t strLen, int32_t *strOffset)\n')
            cfile.write ('{\n')
            cfile.write ('    int32_t error = 0;\n')
            hasMutiSet = bool([arg for arg in cmd.args if isinstance(arg.argType, ArMultiSetting)])
            if hasMutiSet:
                cfile.write ('    eARCOMMANDS_ID_FEATURE commandFeature = -1;\n')
                cfile.write ('    int commandClass = -1;\n')
                cfile.write ('    int commandId = -1;\n')
                cfile.write ('    uint16_t multisetSize = 0;\n')
                cfile.write ('    int32_t multisetEnd = 0;\n')
                cfile.write ('    uint16_t cmdSize = 0;\n')
            cfile.write ('    ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
            cfile.write ('\n')
            cfile.write ('    if ((NULL == buffer) || (NULL == resString))\n')
            cfile.write ('    {\n')
            cfile.write ('        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
            cfile.write ('    } // No else --> Arg check\n')
            cfile.write ('\n')
            for arg in cmd.args:
                if isinstance(arg.argType, ArMultiSetting):
                    cfile.write ('    if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        multisetSize = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('        if (error == 1)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('    } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('    if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        multisetEnd = *offset + multisetSize;\n')
                    cfile.write ('        cmdSize = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('        if (error == 1)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('    } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('    if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        *strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("{", resString, strLen, *strOffset) ;\n')
                    cfile.write ('        if (*strOffset < 0)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                    cfile.write ('        } // No else --> Do not modify retVal if no error occured\n')
                    cfile.write ('    } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('    while ((retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ') &&\n')
                    cfile.write ('           (*offset < multisetEnd))\n')
                    cfile.write ('    {\n')
                    cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('        {\n')
                    cfile.write ('            commandFeature = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('            if (error == 1)\n')
                    cfile.write ('            {\n')
                    cfile.write ('                retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('            } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('        } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('        {\n')
                    cfile.write ('            commandClass = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('            if (error == 1)\n')
                    cfile.write ('            {\n')
                    cfile.write ('                retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('            } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('        } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('        {\n')
                    cfile.write ('            commandId = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('            if (error == 1)\n')
                    cfile.write ('            {\n')
                    cfile.write ('                retVal = '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('            } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('        } // No else --> Processing block\n')
                    cfile.write ('\n')
                    cfile.write ('        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('        {\n')
                    cfile.write ('            switch (commandFeature)\n')
                    cfile.write ('            {\n')

                    #regroup multisetting msgs by feature and by class
                    multiset_sorted = {}
                    for multiset_msg in arg.argType.msgs:
                        multiset_cl = multiset_msg.cls if multiset_msg.cls else ArClass('defaultCls', 0, '')
                        if not multiset_msg.ftr in multiset_sorted:
                            multiset_sorted[multiset_msg.ftr] = {}
                        if not multiset_cl in multiset_sorted[multiset_msg.ftr]:
                            multiset_sorted[multiset_msg.ftr][multiset_cl] = []
                        multiset_sorted[multiset_msg.ftr][multiset_cl].append(multiset_msg)

                    get_name = lambda x: x.name

                    for multiset_ftr in sorted(list(multiset_sorted.keys()), key=get_name):
                        cfile.write ('            case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(multiset_ftr)) + ':\n')
                        cfile.write ('                switch (commandClass)\n')
                        cfile.write ('                {\n')
                        for multiset_cl in sorted(list(multiset_sorted[multiset_ftr].keys()), key=get_name):
                            cfile.write ('                case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(multiset_ftr) + '_CLASS', multiset_cl.name) + ':\n')
                            cfile.write ('                    switch (commandId)\n')
                            cfile.write ('                    {\n')
                            for multiset_msg in multiset_sorted[multiset_ftr][multiset_cl]:
                                cfile.write ('                    case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(multiset_ftr) + '_' + multiset_cl.name + '_CMD', multiset_msg.name) + ':\n')
                                if multiset_msg.cls:
                                    cfile.write ('                        *strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("{' + get_ftr_old_name(multiset_ftr) + '.' + multiset_cl.name +'.' + multiset_msg.name + ':", resString, strLen, *strOffset) ;\n')
                                else:
                                    cfile.write ('                        *strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("{' + get_ftr_old_name(multiset_ftr) + '.' + multiset_msg.name + ':", resString, strLen, *strOffset) ;\n')
                                cfile.write ('                        if (*strOffset < 0)\n')
                                cfile.write ('                        {\n')
                                cfile.write ('                            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                                cfile.write ('                        } // No else --> Do not modify retVal if no error occured\n')
                                cfile.write ('\n')
                                cfile.write ('                        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                                cfile.write ('                        {\n')
                                cfile.write ('                            retVal = '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(multiset_ftr)) + ARCapitalize (format_cmd_name(multiset_msg)) + 'DescribeArgs') + '(buffer, buffLen, offset, resString, strLen, strOffset);\n')
                                cfile.write ('                        }\n')
                                cfile.write ('\n')
                                cfile.write ('                        if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                                cfile.write ('                        {\n')
                                cfile.write ('                            *strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("}", resString, strLen, *strOffset) ;\n')
                                cfile.write ('                            if (*strOffset < 0)\n')
                                cfile.write ('                            {\n')
                                cfile.write ('                                retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                                cfile.write ('                            } // No else --> Do not modify retVal if no error occured\n')
                                cfile.write ('                        }\n')
                                cfile.write ('                        break;\n')
                            cfile.write ('                    default:\n')
                            cfile.write ('                        // Command unknown\n')
                            cfile.write ('                        *offset += cmdSize;\n')
                            cfile.write ('                       break;\n')
                            cfile.write ('                    }\n')
                            cfile.write ('                    break;\n')
                        cfile.write ('                default:\n')
                        cfile.write ('                    // Command unknown\n')
                        cfile.write ('                    *offset += cmdSize;\n')
                        cfile.write ('                    break;\n')
                        cfile.write ('                }\n')
                        cfile.write ('                break;\n')

                    cfile.write ('            default:\n')
                    cfile.write ('                // Command unknown\n')
                    cfile.write ('                *offset += cmdSize;\n')
                    cfile.write ('                break;\n')
                    cfile.write ('            }\n')
                    cfile.write ('        }\n')
                    cfile.write ('\n')

                    cfile.write ('        if ((retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ') &&\n')
                    cfile.write ('           (*offset < multisetEnd))\n')
                    cfile.write ('        {\n')
                    cfile.write ('            cmdSize = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('            if (error == 1)\n')
                    cfile.write ('            {\n')
                    cfile.write ('                /* buffer end */\n')
                    cfile.write ('                break;\n')
                    cfile.write ('            } // No else --> Do not modify retVal if read went fine\n')
                    cfile.write ('        }\n')
                    cfile.write ('    }\n')
                    cfile.write ('\n')
                    cfile.write ('    if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        *strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("}", resString, strLen, *strOffset) ;\n')
                    cfile.write ('        if (*strOffset < 0)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                    cfile.write ('        } // No else --> Do not modify retVal if no error occured\n')
                    cfile.write ('    } // No else --> Processing block\n')
                else:
                    cfile.write ('    if (retVal == '+ AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('    {\n')
                    cfile.write ('        ' + xmlToCcharAreConst (LIB_MODULE, ftr, cmd, arg) + ' arg = ' + xmlToReader (ftr, cmd, arg) + ' (buffer, buffLen, offset, &error);\n')
                    cfile.write ('        if (error == 0)\n')
                    cfile.write ('        {\n')
                    cfile.write ('            *strOffset = ' + xmlToPrinter (ftr, cmd, arg) + ' (" | ' + arg.name + ' -> ", arg, resString, strLen, *strOffset);\n')
                    cfile.write ('            if (*strOffset < 0)\n')
                    cfile.write ('            {\n')
                    cfile.write ('               retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                    cfile.write ('            } // No else --> Do not modify retVal if no error occured\n')
                    cfile.write ('        }\n')
                    cfile.write ('        else\n')
                    cfile.write ('        {\n')
                    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
                    cfile.write ('        }\n')
                    cfile.write ('    } // No else --> Processing block\n')
            cfile.write ('\n')
            cfile.write ('    return retVal;\n')
            cfile.write ('}\n')
            cfile.write ('\n')

    cfile.write ('// DECODER ENTRY POINT\n')
    cfile.write (AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
    cfile.write (ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeBuffer') + ' (const uint8_t *buffer, int32_t buffLen)\n')
    cfile.write ('{\n')
    cfile.write ('    return ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeCommand') + ' (NULL, buffer, buffLen);\n');
    cfile.write ('}\n\n')
    cfile.write (AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
    cfile.write (ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeCommand') + ' (ARCOMMANDS_Decoder_t *decoder, const uint8_t *buffer, int32_t buffLen)\n')
    cfile.write ('{\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, ID_SUBMODULE, 'FEATURE') + ' commandFeature = -1;\n')
    cfile.write ('    int commandClass = -1;\n')
    cfile.write ('    int commandId = -1;\n')
    cfile.write ('    int32_t error = 0;\n')
    cfile.write ('    int32_t offset = 0;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
    cfile.write ('    if (NULL == buffer)\n')
    cfile.write ('    {\n')
    cfile.write ('        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
    cfile.write ('    } // No else --> Arg check\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        if (' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Init') + ' () == 0)\n')
    cfile.write ('        {\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
    cfile.write ('        } // No else --> keep retVal to OK if init went fine\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandFeature = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
    cfile.write ('        if (error == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandClass = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
    cfile.write ('        if (error == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandId = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
    cfile.write ('        if (error == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        switch (commandFeature)\n')
    cfile.write ('        {\n')
    for ftr in allFeatures:
        cfile.write ('        case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(ftr)) + ':\n')
        cfile.write ('        {\n')

        if ftr.classes : #project only
            cfile.write ('            switch (commandClass)\n')
            cfile.write ('            {\n')
            for cl in ftr.classes:
                cfile.write ('            case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CLASS', cl.name) + ':\n')
                cfile.write ('            {\n')
                cfile.write ('                switch (commandId)\n')
                cfile.write ('                {\n')
                for cmd in cl.cmds:
                    cfile.write ('                case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_' + cl.name + '_CMD', cmd.name) + ':\n')
                    cfile.write ('                {\n')
                    CBNAME = ARGlobalName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Cb')
                    CBCUSTOMNAME = ARGlobalName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Custom')
                    DECODER_CBNAME = ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Callback'
                    DECODER_CBCUSTOMNAME = ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Custom'
                    cfile.write ('                    if (decoder) {\n')
                    cfile.write ('                        ARSAL_Mutex_Lock (&decoder->mutex);\n')
                    cfile.write ('                    } else {\n')
                    cfile.write ('                        ARSAL_Mutex_Lock (&' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'mutex') + ');\n')
                    cfile.write ('                    }\n')
                    cfile.write ('                    if ((decoder && decoder->' + DECODER_CBNAME + ') || (!decoder && ' + CBNAME + '))\n')
                    cfile.write ('                    {\n')
                    for arg in cmd.args:
                        if ArArgType.STRING == arg.argType:
                            cfile.write ('                        ' + xmlToCcharAreConst (LIB_MODULE, ftr, cmd, arg) + ' _' + arg.name + ' = NULL;\n')
                        else:
                            cfile.write ('                        ' + xmlToC (LIB_MODULE, ftr, cmd, arg) + ' _' + arg.name + ';\n')

                    if cmd.args:
                        cfile.write ('                        retVal = '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize(cl.name) + ARCapitalize (cmd.name) + 'DecodeArgs') + '(buffer, buffLen, &offset')
                        for arg in cmd.args:
                            cfile.write (', &_' + arg.name)
                        cfile.write (');\n')
                    cfile.write ('                        if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                    cfile.write ('                        {\n')
                    cfile.write ('                            if (decoder && decoder->' + DECODER_CBNAME + ') {\n')
                    cfile.write ('                                decoder->' + DECODER_CBNAME + ' (')
                    first = True
                    for arg in cmd.args:
                        if first:
                            first = False
                        else:
                            cfile.write (', ')
                        if isinstance(arg.argType, ArMultiSetting):
                            cfile.write ('&_' + arg.name)
                        else:
                            cfile.write ('_' + arg.name)
                    if not first:
                        cfile.write (', ')
                    cfile.write ('decoder->' + DECODER_CBCUSTOMNAME + ');\n')
                    cfile.write ('                            } else {\n')
                    cfile.write ('                                ' + CBNAME + ' (')
                    first = True
                    for arg in cmd.args:
                        if first:
                            first = False
                        else:
                            cfile.write (', ')
                        if isinstance(arg.argType, ArMultiSetting):
                            cfile.write ('&_' + arg.name)
                        else:
                            cfile.write ('_' + arg.name)
                    if not first:
                        cfile.write (', ')
                    cfile.write (CBCUSTOMNAME + ');\n')
                    cfile.write ('                            }\n')
                    cfile.write ('                        } // No else --> Processing block\n')
                    cfile.write ('                    }\n')
                    cfile.write ('                    else\n')
                    cfile.write ('                    {\n')
                    cfile.write ('                        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NO_CALLBACK') + ';\n')
                    cfile.write ('                    }\n')
                    cfile.write ('                    if (decoder) {\n')
                    cfile.write ('                        ARSAL_Mutex_Unlock (&decoder->mutex);\n')
                    cfile.write ('                    } else {\n')
                    cfile.write ('                        ARSAL_Mutex_Unlock (&' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'mutex') + ');\n')
                    cfile.write ('                    }\n')
                    cfile.write ('                }\n')
                    cfile.write ('                break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_' + cl.name + '_CMD', cmd.name) + ' */\n')
                cfile.write ('                default:\n')
                cfile.write ('                    retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
                cfile.write ('                    break;\n')
                cfile.write ('                }\n')
                cfile.write ('            }\n')
                cfile.write ('            break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CLASS', cl.name) + ' */\n')
            cfile.write ('            default:\n')
            cfile.write ('                retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
            cfile.write ('                break;\n')
            cfile.write ('            }\n')
        else:
            cfile.write ('            if (commandClass == ' + ARMacroName (LIB_MODULE, ID_SUBMODULE, 'FEATURE_CLASS') + ')\n')
            cfile.write ('            {\n')
            cfile.write ('                switch (commandId)\n')
            cfile.write ('                {\n')
            for cmd in ftr.cmds + ftr.evts:
                cfile.write ('                case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CMD', cmd.name) + ':\n')
                cfile.write ('                {\n')
                CBNAME = ARGlobalName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cmd.name) + 'Cb')
                CBCUSTOMNAME = ARGlobalName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cmd.name) + 'Custom')
                DECODER_CBNAME = ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cmd.name) + 'Callback'
                DECODER_CBCUSTOMNAME = ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cmd.name) + 'Custom'
                cfile.write ('                    if (decoder) {\n')
                cfile.write ('                        ARSAL_Mutex_Lock (&decoder->mutex);\n')
                cfile.write ('                    } else {\n')
                cfile.write ('                        ARSAL_Mutex_Lock (&' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'mutex') + ');\n')
                cfile.write ('                    }\n')
                cfile.write ('                    if ((decoder && decoder->' + DECODER_CBNAME + ') || (!decoder && ' + CBNAME + '))\n')
                cfile.write ('                    {\n')
                for arg in cmd.args:
                    if ArArgType.STRING == arg.argType:
                        cfile.write ('                        ' + xmlToCcharAreConst (LIB_MODULE, ftr, cmd, arg) + ' _' + arg.name + ' = NULL;\n')
                    else:
                        cfile.write ('                        ' + xmlToC (LIB_MODULE, ftr, cmd, arg) + ' _' + arg.name + ';\n')

                if cmd.args:
                    cfile.write ('                        retVal = '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'DecodeArgs') + '(buffer, buffLen, &offset')
                    for arg in cmd.args:
                        cfile.write (', &_' + arg.name)
                    cfile.write (');\n')

                cfile.write ('                        if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
                cfile.write ('                        {\n')
                cfile.write ('                            if (decoder && decoder->' + DECODER_CBNAME + ') {\n')
                cfile.write ('                                decoder->' + DECODER_CBNAME + ' (')
                first = True
                for arg in cmd.args:
                    if first:
                        first = False
                    else:
                        cfile.write (', ')
                    if isinstance(arg.argType, ArMultiSetting):
                        cfile.write ('&_' + arg.name)
                    else:
                        cfile.write ('_' + arg.name)
                if not first:
                    cfile.write (', ')
                cfile.write ('decoder->' + DECODER_CBCUSTOMNAME + ');\n')
                cfile.write ('                            } else {\n')
                cfile.write ('                                ' + CBNAME + ' (')
                first = True
                for arg in cmd.args:
                    if first:
                        first = False
                    else:
                        cfile.write (', ')
                    if isinstance(arg.argType, ArMultiSetting):
                        cfile.write ('&_' + arg.name)
                    else:
                        cfile.write ('_' + arg.name)
                if not first:
                    cfile.write (', ')
                cfile.write (CBCUSTOMNAME + ');\n')
                cfile.write ('                            }\n')
                cfile.write ('                        } // No else --> Processing block\n')
                cfile.write ('                    }\n')
                cfile.write ('                    else\n')
                cfile.write ('                    {\n')
                cfile.write ('                        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NO_CALLBACK') + ';\n')
                cfile.write ('                    }\n')
                cfile.write ('                    if (decoder) {\n')
                cfile.write ('                        ARSAL_Mutex_Unlock (&decoder->mutex);\n')
                cfile.write ('                    } else {\n')
                cfile.write ('                        ARSAL_Mutex_Unlock (&' + ARGlobalName (LIB_MODULE, DEC_SUBMODULE, 'mutex') + ');\n')
                cfile.write ('                    }\n')
                cfile.write ('                }\n')
                cfile.write ('                break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_' + cl.name + '_CMD', cmd.name) + ' */\n')
            cfile.write ('                default:\n')
            cfile.write ('                    retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
            cfile.write ('                    break;\n')
            cfile.write ('                }\n')
            cfile.write ('            }\n')
            cfile.write ('            else\n')
            cfile.write ('            {\n')
            cfile.write ('                retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
            cfile.write ('                break;\n')
            cfile.write ('            }\n')
        cfile.write ('        }\n')
        cfile.write ('        break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(ftr)) + ' */\n')

    cfile.write ('        default:\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
    cfile.write ('            break;\n')
    cfile.write ('        }\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('    return retVal;\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    cfile.write (AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '\n')
    cfile.write (ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DescribeBuffer') + ' (uint8_t *buffer, int32_t buffLen, char *resString, int32_t stringLen)\n')
    cfile.write ('{\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, ID_SUBMODULE, 'FEATURE') + ' commandFeature = -1;\n')
    cfile.write ('    int commandClass = -1;\n')
    cfile.write ('    int commandId = -1;\n')
    cfile.write ('    int32_t offset = 0;\n')
    cfile.write ('    int32_t error = 0;\n')
    cfile.write ('    int strOffset = 0;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
    cfile.write ('    if ((NULL == buffer) || (NULL == resString))\n')
    cfile.write ('    {\n')
    cfile.write ('        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
    cfile.write ('    } // No else --> Arg check\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        if (' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Init') + ' () == 0)\n')
    cfile.write ('        {\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
    cfile.write ('        } // No else --> keep retVal to OK if init went fine\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandFeature = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
    cfile.write ('        if (error == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandClass = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
    cfile.write ('        if (error == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandId = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, buffLen, &offset, &error);\n')
    cfile.write ('        if (error == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_DATA') + ';\n')
    cfile.write ('        } // No else --> Do not modify retVal if read went fine\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ' && stringLen > 0)\n')
    cfile.write ('    {\n')
    cfile.write ('        resString[0] = \'\\0\';\n')
    cfile.write ('    }\n')
    cfile.write ('    else\n')
    cfile.write ('    {\n')
    cfile.write ('        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR') + ';\n')
    cfile.write ('    }\n')
    cfile.write ('\n')
    cfile.write ('    if (retVal == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        switch (commandFeature)\n')
    cfile.write ('        {\n')
    for ftr in allFeatures:
        cfile.write ('        case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(ftr)) + ':\n')
        cfile.write ('        {\n')

        if ftr.classes: #project only
            cfile.write ('            switch (commandClass)\n')
            cfile.write ('            {\n')
            for cl in ftr.classes:
                cfile.write ('            case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CLASS', cl.name) + ':\n')
                cfile.write ('            {\n')
                cfile.write ('                switch (commandId)\n')
                cfile.write ('                {\n')
                for cmd in cl.cmds:
                    cfile.write ('                case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_' + cl.name + '_CMD', cmd.name) + ':\n')
                    cfile.write ('                {\n')
                    cfile.write ('                    strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("' + get_ftr_old_name(ftr) + '.' + cl.name + '.' + cmd.name + ':", resString, stringLen, strOffset) ;\n')
                    if cmd.args:
                        cfile.write ('                    if (strOffset > 0)\n')
                        cfile.write ('                    {\n')
                        cfile.write ('                        retVal = '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'DescribeArgs') + '(buffer, buffLen, &offset, resString, stringLen, &strOffset);\n')
                        cfile.write ('                    } // No else --> If first print failed, the next if will set the error code\n')
                        cfile.write ('\n')
                    cfile.write ('                    if (strOffset < 0)\n')
                    cfile.write ('                    {\n')
                    cfile.write ('                        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                    cfile.write ('                    } // No else --> Do not modify retVal if no error occured\n')
                    cfile.write ('                }\n')
                    cfile.write ('                break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_' + cl.name + '_CMD', cmd.name) + ' */\n')
                cfile.write ('                default:\n')
                cfile.write ('                    strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("' + get_ftr_old_name(ftr) + '.' + cl.name + '.UNKNOWN -> Unknown command", resString, stringLen, strOffset);\n')
                cfile.write ('                    retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
                cfile.write ('                    break;\n')
                cfile.write ('                }\n')
                cfile.write ('            }\n')
                cfile.write ('            break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CLASS', cl.name) + ' */\n')
            cfile.write ('            default:\n')
            cfile.write ('                strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("' + get_ftr_old_name(ftr) + '.UNKNOWN -> Unknown command", resString, stringLen, strOffset);\n')
            cfile.write ('                retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
            cfile.write ('                break;\n')
            cfile.write ('            }\n')

        else:
            cfile.write ('            if (commandClass == ' + ARMacroName (LIB_MODULE, ID_SUBMODULE, 'FEATURE_CLASS') + ')\n')
            cfile.write ('            {\n')
            cfile.write ('                switch (commandId)\n')
            cfile.write ('                {\n')
            for cmd in ftr.cmds + ftr.evts:
                cfile.write ('                case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CMD', cmd.name) + ':\n')
                cfile.write ('                {\n')
                cfile.write ('                    strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("' + get_ftr_old_name(ftr) + '.' + cmd.name + ':", resString, stringLen, strOffset) ;\n')
                if cmd.args:
                    cfile.write ('                    if (strOffset > 0)\n')
                    cfile.write ('                    {\n')
                    cfile.write ('                        retVal = '+ ARFunctionName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'DescribeArgs') + '(buffer, buffLen, &offset, resString, stringLen, &strOffset);\n')
                    cfile.write ('                    } // No else --> If first print failed, the next if will set the error code\n')
                    cfile.write ('\n')
                cfile.write ('                    if (strOffset < 0)\n')
                cfile.write ('                    {\n')
                cfile.write ('                        retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'NOT_ENOUGH_SPACE') + ';\n')
                cfile.write ('                    } // No else --> Do not modify retVal if no error occured\n')
                cfile.write ('                }\n')
                cfile.write ('                break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CMD', cmd.name) + ' */\n')
            cfile.write ('                default:\n')
            cfile.write ('                    strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("' + get_ftr_old_name(ftr) +'.UNKNOWN -> Unknown command", resString, stringLen, strOffset);\n')
            cfile.write ('                    retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
            cfile.write ('                    break;\n')
            cfile.write ('                }\n')
            cfile.write ('            }\n')
            cfile.write ('            else\n')
            cfile.write ('            {\n')
            cfile.write ('                strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("' + get_ftr_old_name(ftr) + '.UNKNOWN -> Unknown command", resString, stringLen, strOffset);\n')
            cfile.write ('                retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
            cfile.write ('            }\n')

        cfile.write ('        }\n')
        cfile.write ('        break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(ftr)) + ' */\n')

    cfile.write ('        default:\n')
    cfile.write ('            strOffset = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'WriteString') + ' ("UNKNOWN -> Unknown command", resString, stringLen, strOffset);\n')
    cfile.write ('            retVal = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'UNKNOWN_COMMAND') + ';\n')
    cfile.write ('            break;\n')
    cfile.write ('        }\n')
    cfile.write ('    } // No else --> Processing block\n')
    cfile.write ('    return retVal;\n')
    cfile.write ('}\n')
    cfile.write ('\n')


    cfile.write ('// END GENERATED CODE\n')

    cfile.close ()

    #################################
    # 9TH PART :                    #
    #################################
    # Generate filter h file        #
    #################################

    hfile = open (paths.COMMANDSFIL_HFILE, 'w')

    hfile.write (LICENCE_HEADER)
    hfile.write ('/**\n')
    hfile.write (' * @file ' + COMMANDSFIL_HFILE_NAME + '\n')
    hfile.write (' * @brief libARCommands filter header.\n')
    hfile.write (' * This file contains all declarations needed to create and use a commands filter\n')
    hfile.write (' * @note Autogenerated file\n')
    hfile.write (' **/\n')
    hfile.write ('#ifndef ' + COMMANDSFIL_DEFINE + '\n')
    hfile.write ('#define ' + COMMANDSFIL_DEFINE + '\n')
    hfile.write ('#include <' + COMMANDSTYPES_HFILE_NAME + '>\n')
    hfile.write ('#include <inttypes.h>\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Error code for ARCOMMANDS_Filter functions.\n')
    hfile.write (' */\n')
    hfile.write ('typedef enum {\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ' = 0, ///< No error.\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'ALLOC') + ', ///< Memory allocation error.\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_STATUS') + ', ///< The given status is not a valid status.\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_FILTER') + ', ///< The given filter is not a valid filter.\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_BUFFER') + ', ///< The given buffer is not a valid buffer.\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OTHER') + ', ///< Any other error.\n')
    hfile.write ('} ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ';\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Status code for ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' function\n')
    hfile.write (' */\n')
    hfile.write ('typedef enum {\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ' = 0, ///< The command should pass the filter\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ', ///< The command should not pass the filter\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'UNKNOWN') + ', ///< Unknown command. The command was possibly added in a newer version of libARCommands, or is an invalid command.\n')
    hfile.write ('    ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ERROR') + ', ///< The filtering of the command failed.\n')
    hfile.write ('} ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ';\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief ARCOMMANDS_Filter object holder\n')
    hfile.write (' */\n')
    hfile.write ('typedef struct ARCOMMANDS_Filter_t ARCOMMANDS_Filter_t;\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Creates a new ARCOMMANDS_Filter_t\n')
    hfile.write (' * @param defaultBehavior The default behavior of the filter (must be either ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ' or ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ').\n')
    hfile.write (' * @param error Optionnal pointer which will hold the error code.\n')
    hfile.write (' * @warning This function allocates memory.\n')
    hfile.write (' * @note The memory must be freed by a call to ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'DeleteFilter') + '.\n')
    hfile.write (' * @return A new ARCOMMANDS_Filter_t instance. NULL in case of error.\n')
    hfile.write (' */\n')
    hfile.write ('ARCOMMANDS_Filter_t* ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'NewFilter') + ' (' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' defaultBehavior, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' *error);\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Deletes an ARCOMMANDS_Filter_t\n')
    hfile.write (' * @param filter The filter to delete.\n')
    hfile.write (' */\n')
    hfile.write ('void ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'DeleteFilter') + ' (ARCOMMANDS_Filter_t **filter);\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief Filter an ARCommand\n')
    hfile.write (' * @param filter The ARCOMMANDS_Filter_t to use for filtering.\n')
    hfile.write (' * @param buffer The ARCommand buffer.\n')
    hfile.write (' * @param len The ARCommand buffer length.\n')
    hfile.write (' * @param error Optionnal pointer which will hold the error code.\n')
    hfile.write (' * @return An ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' status code\n')
    hfile.write (' */\n')
    hfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' (ARCOMMANDS_Filter_t *filter, uint8_t *buffer, uint32_t len, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' *error);\n')
    hfile.write ('\n')
    hfile.write ('\n')
    hfile.write ('// Filter ON/OFF functions')
    hfile.write ('\n')
    for ftr in allFeatures:
        hfile.write ('// Feature ' + get_ftr_old_name(ftr) + '\n')
        hfile.write ('\n')

        hfile.write ('/**\n')
        hfile.write (' * @brief Sets the filter behavior for all commands ' + get_ftr_old_name(ftr) + '.XXX.XXX.\n')
        hfile.write (' * @param filter The filter to be modified.\n')
        hfile.write (' * @param behavior The behavior to use for the commands (must be either ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ' or ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ').\n')
        hfile.write (' * @return An ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' enum.\n')
        hfile.write (' */\n')
        hfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + 'Behavior (ARCOMMANDS_Filter_t *filter, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' behavior);\n')
        hfile.write ('\n')

        if ftr.classes: #project only
            for cl in ftr.classes:
                hfile.write ('// Command class ' + cl.name + '\n')
                hfile.write ('\n')

                hfile.write ('/**\n')
                hfile.write (' * @brief Sets the filter behavior for all commands ' + get_ftr_old_name(ftr) + '.' + cl.name + '.XXX.\n')
                hfile.write (' * @param filter The filter to be modified.\n')
                hfile.write (' * @param behavior The behavior to use for the commands (must be either ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ' or ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ').\n')
                hfile.write (' * @return An ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' enum.\n')
                hfile.write (' */\n')
                hfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + 'Behavior (ARCOMMANDS_Filter_t *filter, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' behavior);\n')
                hfile.write ('\n')

                for cmd in cl.cmds:
                    hfile.write ('/**\n')
                    hfile.write (' * @brief Sets the filter behavior for the command ' + get_ftr_old_name(ftr) + '.' + cl.name + '.' + cmd.name + '.\n')
                    hfile.write (' * @param filter The filter to be modified.\n')
                    hfile.write (' * @param behavior The behavior to use for the command (must be either ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ' or ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ').\n')
                    hfile.write (' * @return An ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' enum.\n')
                    hfile.write (' */\n')
                    hfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Behavior (ARCOMMANDS_Filter_t *filter, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' behavior);\n')
                    hfile.write ('\n')
                hfile.write ('\n')
        else:
            for cmd in ftr.cmds + ftr.evts:
                hfile.write ('/**\n')
                hfile.write (' * @brief Sets the filter behavior for the command ' + get_ftr_old_name(ftr) + '.' + cmd.name + '.\n')
                hfile.write (' * @param filter The filter to be modified.\n')
                hfile.write (' * @param behavior The behavior to use for the command (must be either ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ' or ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ').\n')
                hfile.write (' * @return An ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' enum.\n')
                hfile.write (' */\n')
                hfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cmd.name) + 'Behavior (ARCOMMANDS_Filter_t *filter, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' behavior);\n')
                hfile.write ('\n')
            hfile.write ('\n')
        hfile.write ('\n')


    hfile.write ('#endif /* ' + COMMANDSFIL_DEFINE + ' */\n')

    hfile.close ()

    #################################
    # 10TH PART :                   #
    #################################
    # Generate filter c file        #
    #################################

    cfile = open (paths.COMMANDSFIL_CFILE, 'w')

    cfile.write (LICENCE_HEADER)
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
    cfile.write ('#include <stdlib.h>\n')
    cfile.write ('#include "' + COMMANDSRW_HFILE_NAME + '"\n')
    cfile.write ('#include <' + COMMANDSTYPES_HFILE_NAME + '>\n')
    cfile.write ('#include <' + COMMANDSFIL_HFILE_NAME + '>\n')
    cfile.write ('#include <' + COMMANDSID_HFILE_NAME + '>\n')
    cfile.write ('\n')
    cfile.write ('\n')
    cfile.write ('// ARCOMMANDS_Filter_t structure definition\n')
    cfile.write ('struct ARCOMMANDS_Filter_t\n')
    cfile.write ('{\n')
    for ftr in allFeatures:
        cfile.write ('    // Feature ' + get_ftr_old_name(ftr) + '\n')
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' Cmd' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Behavior;\n')
        cfile.write ('\n')
    cfile.write ('};\n')
    cfile.write ('\n')
    cfile.write ('\n')
    cfile.write ('// Constructor\n')
    cfile.write ('ARCOMMANDS_Filter_t* ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'NewFilter') + ' (' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' defaultBehavior, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' *error)\n')
    cfile.write ('{\n')
    cfile.write ('    ARCOMMANDS_Filter_t *retFilter = NULL;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ';\n')
    cfile.write ('    if ((defaultBehavior != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ') &&\n')
    cfile.write ('        (defaultBehavior != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + '))\n')
    cfile.write ('    {\n')
    cfile.write ('        localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_STATUS') + ';\n')
    cfile.write ('    } // No else : Args check\n')
    cfile.write ('\n')
    cfile.write ('    if (localError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        retFilter = malloc (sizeof (struct ARCOMMANDS_Filter_t));\n')
    cfile.write ('        if (retFilter == NULL)\n')
    cfile.write ('        {\n')
    cfile.write ('            localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'ALLOC') + ';\n')
    cfile.write ('        } // No else : Error processing.\n')
    cfile.write ('    } // No else : Processing block\n')
    cfile.write ('\n')
    cfile.write ('    // Setup default behavior\n')
    cfile.write ('    if (localError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    for ftr in allFeatures:
        cfile.write ('        // Feature ' + get_ftr_old_name(ftr) + '\n')
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('        retFilter->Cmd' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Behavior = defaultBehavior;\n')
    cfile.write ('    } // No else : Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (error != NULL)\n')
    cfile.write ('    {\n')
    cfile.write ('        *error = localError;\n')
    cfile.write ('    } // No else : Set error only if pointer is not NULL\n')
    cfile.write ('    return retFilter;\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    cfile.write ('void ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'DeleteFilter') + ' (ARCOMMANDS_Filter_t **filter)\n')
    cfile.write ('{\n')
    cfile.write ('    if ((filter != NULL) &&\n')
    cfile.write ('        (*filter != NULL))\n')
    cfile.write ('    {\n')
    cfile.write ('        free (*filter);\n')
    cfile.write ('        *filter = NULL;\n')
    cfile.write ('    } // No else : No need to delete an invalid filter instance\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    cfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' (ARCOMMANDS_Filter_t *filter, uint8_t *buffer, uint32_t len, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' *error)\n')
    cfile.write ('{\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, ID_SUBMODULE, 'FEATURE') + ' commandFeature = -1;\n')
    cfile.write ('    int commandClass = -1;\n')
    cfile.write ('    int commandId = -1;\n')
    cfile.write ('    int32_t offset = 0;\n')
    cfile.write ('    int32_t readError = 0;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ';\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' retStatus = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'UNKNOWN') + ';\n')
    cfile.write ('\n')
    cfile.write ('    // Args check\n')
    cfile.write ('    if (filter == NULL)\n')
    cfile.write ('    {\n')
    cfile.write ('        localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_FILTER') + ';\n')
    cfile.write ('    } // No else : Args check\n')
    cfile.write ('\n')
    cfile.write ('    if ((buffer == NULL) ||\n')
    cfile.write ('        (len < 4))\n')
    cfile.write ('    {\n')
    cfile.write ('        localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_BUFFER') + ';\n')
    cfile.write ('    } // No else : Args check\n')
    cfile.write ('\n')
    cfile.write ('    if (localError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandFeature = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, len, &offset, &readError);\n')
    cfile.write ('        if (readError == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_BUFFER') + ';\n')
    cfile.write ('        }\n')
    cfile.write ('    } // No else : Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (localError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandClass = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read8FromBuffer') + ' (buffer, len, &offset, &readError);\n')
    cfile.write ('        if (readError == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_BUFFER') + ';\n')
    cfile.write ('        }\n')
    cfile.write ('    } // No else : Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (localError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        commandId = ' + ARFunctionName (LIB_MODULE, RW_SUBMODULE, 'Read16FromBuffer') + ' (buffer, len, &offset, &readError);\n')
    cfile.write ('        if (readError == 1)\n')
    cfile.write ('        {\n')
    cfile.write ('            localError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_BUFFER') + ';\n')
    cfile.write ('        }\n')
    cfile.write ('    } // No else : Processing block\n')
    cfile.write ('\n')
    cfile.write ('    if (localError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        switch (commandFeature)\n')
    cfile.write ('        {\n')
    for ftr in allFeatures:
        cfile.write ('        case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(ftr)) + ':\n')
        cfile.write ('        {\n')

        if ftr.classes:
            cfile.write ('            switch (commandClass)\n')
            cfile.write ('            {\n')
            for cl in ftr.classes:
                cfile.write ('            case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CLASS', cl.name) + ':\n')
                cfile.write ('            {\n')
                cfile.write ('                switch (commandId)\n')
                cfile.write ('                {\n')
                for cmd in cl.cmds:
                    cfile.write ('                case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_' + cl.name + '_CMD', cmd.name) + ':\n')
                    cfile.write ('                {\n')
                    cfile.write ('                    retStatus = filter->Cmd' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Behavior;\n')
                    cfile.write ('                }\n')
                    cfile.write ('                break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_' + cl.name + '_CMD', cmd.name) + ' */\n')
                cfile.write ('                default:\n')
                cfile.write ('                    // Do nothing, the default answer is already UNKNOWN\n')
                cfile.write ('                    break;\n')
                cfile.write ('                }\n')
                cfile.write ('            }\n')
                cfile.write ('            break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CLASS', cl.name) + ' */\n')
            cfile.write ('            default:\n')
            cfile.write ('                // Do nothing, the default answer is already UNKNOWN\n')
            cfile.write ('                break;\n')
            cfile.write ('            }\n')
        else:
            cfile.write ('            if (commandClass == '+ARMacroName (LIB_MODULE, ID_SUBMODULE, 'FEATURE_CLASS')+')\n')
            cfile.write ('            {\n')
            cfile.write ('                switch (commandId)\n')
            cfile.write ('                {\n')
            for cmd in ftr.cmds + ftr.evts:
                cfile.write ('                case ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CMD', cmd.name) + ':\n')
                cfile.write ('                {\n')
                cfile.write ('                    retStatus = filter->Cmd' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cmd.name) + 'Behavior;\n')
                cfile.write ('                }\n')
                cfile.write ('                break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, get_ftr_old_name(ftr) + '_CMD', cmd.name) + ' */\n')
            cfile.write ('                default:\n')
            cfile.write ('                    // Do nothing, the default answer is already UNKNOWN\n')
            cfile.write ('                    break;\n')
            cfile.write ('                }\n')
            cfile.write ('            }\n')
            cfile.write ('            //Else Do nothing, the default answer is already UNKNOWN\n')

        cfile.write ('        }\n')
        cfile.write ('        break; /* ' + AREnumValue (LIB_MODULE, ID_SUBMODULE, 'FEATURE', get_ftr_old_name(ftr)) + ' */\n')

    cfile.write ('        default:\n')
    cfile.write ('            // Do nothing, the default answer is already UNKNOWN\n')
    cfile.write ('            break;\n')
    cfile.write ('        }\n')
    cfile.write ('    }\n')
    cfile.write ('\n')
    cfile.write ('    if (localError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        retStatus = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ERROR') + ';\n')
    cfile.write ('    } // No else : Keep retStatus if no error occured\n')
    cfile.write ('\n')
    cfile.write ('    if (error != NULL)\n')
    cfile.write ('    {\n')
    cfile.write ('        *error = localError;\n')
    cfile.write ('    } // No else : Set error only if pointer is not NULL\n')
    cfile.write ('\n')
    cfile.write ('    return retStatus;\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    cfile.write ('\n')
    cfile.write ('// Filter ON/OFF functions')
    cfile.write ('\n')
    for ftr in allFeatures:
        cfile.write ('// Feature ' + get_ftr_old_name(ftr) + '\n')
        cfile.write ('\n')

        cfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + 'Behavior (ARCOMMANDS_Filter_t *filter, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' behavior)\n')
        cfile.write ('{\n')
        cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ';\n')
        cfile.write ('\n')
        cfile.write ('    if (filter == NULL)\n')
        cfile.write ('    {\n')
        cfile.write ('        retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_FILTER') + ';\n')
        cfile.write ('    } // No else : Args check\n')
        cfile.write ('\n')
        cfile.write ('    if ((behavior != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ') &&\n')
        cfile.write ('        (behavior != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + '))\n')
        cfile.write ('    {\n')
        cfile.write ('        retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_STATUS') + ';\n')
        cfile.write ('    } // No else : Arg check\n')
        cfile.write ('\n')
        cfile.write ('    if (retError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
        cfile.write ('    {\n')
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('        filter->Cmd' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Behavior = behavior;\n')
        cfile.write ('    }\n')
        cfile.write ('\n')
        cfile.write ('    return retError;\n')
        cfile.write ('}\n')
        cfile.write ('\n')

        if ftr.classes:#project only
            for cl in ftr.classes:
                cfile.write ('// Command class ' + cl.name + '\n')
                cfile.write ('\n')
                cfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + 'Behavior (ARCOMMANDS_Filter_t *filter, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' behavior)\n')
                cfile.write ('{\n')
                cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ';\n')
                cfile.write ('\n')
                cfile.write ('    if (filter == NULL)\n')
                cfile.write ('    {\n')
                cfile.write ('        retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_FILTER') + ';\n')
                cfile.write ('    } // No else : Args check\n')
                cfile.write ('\n')
                cfile.write ('    if ((behavior != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ') &&\n')
                cfile.write ('        (behavior != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + '))\n')
                cfile.write ('    {\n')
                cfile.write ('        retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_STATUS') + ';\n')
                cfile.write ('    } // No else : Arg check\n')
                cfile.write ('\n')
                cfile.write ('    if (retError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
                cfile.write ('    {\n')
                for cmd in cl.cmds:
                    cfile.write ('        filter->Cmd' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + ARCapitalize (cmd.name) + 'Behavior = behavior;\n')
                cfile.write ('    }\n')
                cfile.write ('\n')
                cfile.write ('    return retError;\n')
                cfile.write ('}\n')
                cfile.write ('\n')

        for cmd in ftr.cmds + ftr.evts:
            cfile.write (AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Behavior (ARCOMMANDS_Filter_t *filter, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' behavior)\n')
            cfile.write ('{\n')
            cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ';\n')
            cfile.write ('    if (filter == NULL)\n')
            cfile.write ('    {\n')
            cfile.write ('        retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_FILTER') + ';\n')
            cfile.write ('    } // No else : Args check\n')
            cfile.write ('\n')
            cfile.write ('    if ((behavior != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ') &&\n')
            cfile.write ('        (behavior != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + '))\n')
            cfile.write ('    {\n')
            cfile.write ('        retError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'BAD_STATUS') + ';\n')
            cfile.write ('    } // No else : Arg check\n')
            cfile.write ('\n')
            cfile.write ('    if (retError == ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        filter->Cmd' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Behavior = behavior;\n')
            cfile.write ('    }\n')
            cfile.write ('\n')
            cfile.write ('    return retError;\n')
            cfile.write ('}\n')
            cfile.write ('\n')
        cfile.write ('\n')

    cfile.write ('// END GENERATED CODE\n')

    cfile.close ()

def tb_generateCmds(ctx, paths):
    genDebug = True
    genTreeFilename = None
    projects = [DEFAULTPROJECTNAME]

    if not os.path.exists (paths.TB__DIR):
        os.makedirs (paths.TB__DIR)
    if not os.path.exists (paths.LIN_TB_DIR):
        os.makedirs (paths.LIN_TB_DIR)
    if not os.path.exists (paths.COM_TB_DIR):
        os.makedirs (paths.COM_TB_DIR)

    allFeatures = ctx.features

    # Check types used
    for ftr in allFeatures:
        for msg in ftr.getMsgs():
            for arg in msg.args:
                if isinstance(arg.argType, ArEnum):
                    hasArgOfType[ArArgType.ENUM] = True
                elif isinstance(arg.argType, ArBitfield):
                    hasArgOfType[ArArgType.BITFIELD] = True
                    hasArgOfType[arg.argType.btfType] = True
                else:
                    hasArgOfType[arg.argType] = True

    #################################
    # 11TH PART :                   #
    #################################
    # Generate C Testbench          #
    #################################

    def TB_CALL_VARNAME (ftr, cmd):
        return get_ftr_old_name(ftr) + ARCapitalize (format_cmd_name(cmd)) + 'ShouldBeCalled'

    def TB_CREATE_VARNAME (ftr, cmd):
        return 'int ' + TB_CALL_VARNAME (ftr, cmd) + ' = 0;'

    cfile = open (paths.TB_CFILE, 'w')

    cfile.write (LICENCE_HEADER)
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
    cfile.write ('#include <' + COMMANDSFIL_HFILE_NAME + '>\n')
    cfile.write ('#include <libARSAL/ARSAL_Print.h>\n')
    cfile.write ('#include <stdlib.h>\n')
    cfile.write ('#include <string.h>\n')
    cfile.write ('\n')
    cfile.write ('int errcount;\n')
    cfile.write ('char describeBuffer [1024] = {0};\n')
    cfile.write ('\n')
    for ftr in allFeatures:
        for msg in ftr.cmds + ftr.evts:
            cfile.write (TB_CREATE_VARNAME (ftr, msg) + '\n')
    cfile.write ('\n')
    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            cmdNamePrint = cmd.name if cmd.cls is None else cmd.cls.name + '.' + cmd.name
            cfile.write ('void ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Cb') + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    cfile.write (', ')
                cfile.write (xmlToC (LIB_MODULE, ftr, cmd, arg, True) + ' ' + arg.name)
            if not first:
                cfile.write (', ')
            cfile.write ('void *custom)\n')
            cfile.write ('{\n')
            cfile.write ('    ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "Callback for command ' + get_ftr_old_name(ftr) + '.' + cmdNamePrint + ' --> Custom PTR = %p", custom);\n')
            for arg in cmd.args:
                cfile.write ('    ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "' + arg.name + ' value : <' + xmlToPrintf (ftr, cmd, arg) + '>", ' + arg.name + ');\n')
                if ArArgType.STRING == arg.argType:
                    cfile.write ('    if (strcmp (' + xmlToSample (ftr, cmd, arg) + ', ' + arg.name + ') != 0)\n')
                else:
                    cfile.write ('    if (' + arg.name + ' != ' + xmlToSample (ftr, cmd, arg) + ')\n')
                cfile.write ('    {\n')
                if ArArgType.STRING == arg.argType:
                    cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "BAD ARG VALUE !!! --> Expected <%s>", ' + xmlToSample (ftr, cmd, arg) + ');\n')
                else:
                    cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "BAD ARG VALUE !!! --> Expected <' + xmlToSample (ftr, cmd, arg) + '>");\n')
                cfile.write ('        errcount++ ;\n')
                cfile.write ('    }\n')
            cfile.write ('    if (' + TB_CALL_VARNAME (ftr, cmd) + ' == 0)\n')
            cfile.write ('    {\n')
            cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "BAD CALLBACK !!! --> This callback should not have been called for this command");\n')
            cfile.write ('        errcount++ ;\n')
            cfile.write ('    }\n')
            cfile.write ('}\n')
            cfile.write ('\n')
        cfile.write ('\n')

    cfile.write ('\n')
    cfile.write ('void ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, 'initCb') + ' (void)\n')
    cfile.write ('{\n')
    cfile.write ('    intptr_t cbCustom = 0;\n')
    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('    ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ' ((' + ARTypeName (LIB_MODULE, DEC_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Callback') + ') ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Cb') + ', (void *)cbCustom++ );\n')
    cfile.write ('}\n')

    cfile.write ('\n')
    cfile.write ('\n')
    cfile.write ('int ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, 'filterTest') + ' (uint8_t *buffer, uint32_t size, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' (*setter)(ARCOMMANDS_Filter_t *, ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + '))\n')
    cfile.write ('{\n')
    cfile.write ('    int errors = 0;\n')
    cfile.write ('    ARCOMMANDS_Filter_t *testFilter = NULL;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' filterError = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ';\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' filterStatus = ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'UNKNOWN') + ';\n')
    cfile.write ('    // Default allow, set to block after\n')
    cfile.write ('    testFilter = ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'NewFilter') + ' (' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ', &filterError);\n')
    cfile.write ('    if (filterError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while creating allow filter : %d", filterError);\n')
    cfile.write ('        errors++;\n')
    cfile.write ('    }\n')
    cfile.write ('    else\n')
    cfile.write ('    {\n')
    cfile.write ('        filterStatus = ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' (testFilter, buffer, size, &filterError);\n')
    cfile.write ('        if ((filterStatus != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ') ||\n')
    cfile.write ('            (filterError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + '))\n')
    cfile.write ('        {\n')
    cfile.write ('            ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while filtering : expected status %d / error %d, got status %d, error %d !", ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ', ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ', filterStatus, filterError);\n')
    cfile.write ('            errors++;\n')
    cfile.write ('        }\n')
    cfile.write ('        // Change filter status\n')
    cfile.write ('        filterError = setter (testFilter, ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ');\n')
    cfile.write ('        if (filterError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('        {\n')
    cfile.write ('            ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while setting filter state to blocked : %d", filterError);\n')
    cfile.write ('            errors++;\n')
    cfile.write ('        }\n')
    cfile.write ('        else\n')
    cfile.write ('        {\n')
    cfile.write ('            filterStatus = ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' (testFilter, buffer, size, &filterError);\n')
    cfile.write ('            if ((filterStatus != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ') ||\n')
    cfile.write ('                (filterError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + '))\n')
    cfile.write ('            {\n')
    cfile.write ('                ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while filtering : expected status %d / error %d, got status %d, error %d !", ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ', ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ', filterStatus, filterError);\n')
    cfile.write ('                errors++;\n')
    cfile.write ('            }\n')
    cfile.write ('        }\n')
    cfile.write ('        ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'DeleteFilter') + ' (&testFilter);\n')
    cfile.write ('    }\n')
    cfile.write ('    // Default block, set to allow after\n')
    cfile.write ('    testFilter = ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'NewFilter') + ' (' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ', &filterError);\n')
    cfile.write ('    if (filterError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while creating block filter : %d", filterError);\n')
    cfile.write ('        errors++;\n')
    cfile.write ('    }\n')
    cfile.write ('    else\n')
    cfile.write ('    {\n')
    cfile.write ('        filterStatus = ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' (testFilter, buffer, size, &filterError);\n')
    cfile.write ('        if ((filterStatus != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ') ||\n')
    cfile.write ('            (filterError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + '))\n')
    cfile.write ('        {\n')
    cfile.write ('            ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while filtering : expected status %d / error %d, got status %d, error %d !", ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'BLOCKED') + ', ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ', filterStatus, filterError);\n')
    cfile.write ('            errors++;\n')
    cfile.write ('        }\n')
    cfile.write ('        // Change filter status\n')
    cfile.write ('        filterError = setter (testFilter, ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ');\n')
    cfile.write ('        if (filterError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ')\n')
    cfile.write ('        {\n')
    cfile.write ('            ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while setting filter state to allowed : %d", filterError);\n')
    cfile.write ('            errors++;\n')
    cfile.write ('        }\n')
    cfile.write ('        else\n')
    cfile.write ('        {\n')
    cfile.write ('            filterStatus = ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' (testFilter, buffer, size, &filterError);\n')
    cfile.write ('            if ((filterStatus != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ') ||\n')
    cfile.write ('                (filterError != ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + '))\n')
    cfile.write ('            {\n')
    cfile.write ('                ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while filtering : expected status %d / error %d, got status %d, error %d !", ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME, 'ALLOWED') + ', ' + AREnumValue (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME, 'OK') + ', filterStatus, filterError);\n')
    cfile.write ('                errors++;\n')
    cfile.write ('            }\n')
    cfile.write ('        }\n')
    cfile.write ('        ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'DeleteFilter') + ' (&testFilter);\n')
    cfile.write ('    }\n')
    cfile.write ('    return errors;\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    cfile.write ('int ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, 'autoTest') + ' ()\n')
    cfile.write ('{\n')
    cfile.write ('    int32_t buffSize = 128;\n')
    cfile.write ('    uint8_t *buffer = malloc (buffSize * sizeof (uint8_t));\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ' res = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ';\n')
    cfile.write ('    int32_t resSize = 0;\n')
    cfile.write ('    errcount = 0;\n')
    cfile.write ('    ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, 'initCb') + ' ();\n')
    for ftr in allFeatures:
        cfile.write ('    // Feature ' + get_ftr_old_name(ftr) + '\n')
        for cmd in ftr.cmds + ftr.evts:
            cmdNamePrint = cmd.name if cmd.cls is None else cmd.cls.name + '.' + cmd.name
            cfile.write ('    res = ' + ARFunctionName (LIB_MODULE, GEN_SUBMODULE, 'Generate' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd))) + ' (buffer, buffSize, &resSize')
            for arg in cmd.args:
                cfile.write (', ' + xmlToSample (ftr, cmd, arg))
            cfile.write (');\n')
            cfile.write ('    if (res != ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while generating command ' + ARCapitalize (get_ftr_old_name(ftr)) + '.' + cmdNamePrint + '\\n\\n");\n')
            cfile.write ('        errcount++ ;\n')
            cfile.write ('    }\n')
            cfile.write ('    else\n')
            cfile.write ('    {\n')
            cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "Generating command ' + ARCapitalize (get_ftr_old_name(ftr)) + '.' + cmdNamePrint + ' succeded");\n')
            cfile.write ('        ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' err;\n')
            cfile.write ('        err = ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DescribeBuffer') + ' (buffer, resSize, describeBuffer, 1024);\n')
            cfile.write ('        if (err != ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('        {\n')
            cfile.write ('            ARSAL_PRINT (ARSAL_PRINT_ERROR, "' + TB_TAG + '", "Error while describing buffer: %d", err);\n')
            cfile.write ('            errcount++ ;\n')
            cfile.write ('        }\n')
            cfile.write ('        else\n')
            cfile.write ('        {\n')
            cfile.write ('            ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "%s", describeBuffer);\n')
            cfile.write ('        }\n')
            cfile.write ('        errcount += ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, 'filterTest') + ' (buffer, resSize, ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Behavior);\n')
            cfile.write ('        ' + TB_CALL_VARNAME (ftr, cmd) + ' = 1;\n')
            cfile.write ('        err = ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeBuffer') + ' (buffer, resSize);\n')
            cfile.write ('        ' + TB_CALL_VARNAME (ftr, cmd) + ' = 0;\n')
            cfile.write ('        ARSAL_PRINT (ARSAL_PRINT_WARNING, "' + TB_TAG + '", "Decode return value : %d\\n\\n", err);\n')
            cfile.write ('        if (err != ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('        {\n')
            cfile.write ('            errcount++ ;\n')
            cfile.write ('        }\n')
            cfile.write ('    }\n')
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

    hfile = open (paths.TB_HFILE, 'w')

    hfile.write (LICENCE_HEADER)
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
    hfile.write ('int ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, 'autoTest') + ' ();\n')
    hfile.write ('\n')
    hfile.write ('#endif /* ' + TB_DEFINE + ' */\n')

    hfile.close ()

    cfile = open (paths.TB_LIN_CFILE, 'w')

    cfile.write (LICENCE_HEADER)
    cfile.write ('/********************************************\n')
    cfile.write (' *            AUTOGENERATED FILE            *\n')
    cfile.write (' *             DO NOT MODIFY IT             *\n')
    cfile.write (' *                                          *\n')
    cfile.write (' * To add new commands :                    *\n')
    cfile.write (' *  - Modify ../Xml/commands.xml file       *\n')
    cfile.write (' *  - Re-run generateCommandsList.py script *\n')
    cfile.write (' *                                          *\n')
    cfile.write (' ********************************************/\n')
    cfile.write ('#include "' + TB_HFILE_NAME + '"\n')
    cfile.write ('\n')
    cfile.write ('int main (int argc, char *argv[])\n')
    cfile.write ('{\n')
    cfile.write ('    return ' + ARFunctionName (LIB_MODULE, TB_SUBMODULE, 'autoTest') + ' ();\n')
    cfile.write ('}\n')

    cfile.close ()

def java_generateCmds(ctx, paths):
    genDebug = True
    genTreeFilename = None
    projects = [DEFAULTPROJECTNAME]

    if not os.path.exists (paths.JNI_DIR):
        os.makedirs (paths.JNI_DIR)
    if not os.path.exists (paths.JNIJ_OUT_DIR):
        os.makedirs (paths.JNIJ_OUT_DIR)

    allFeatures = ctx.features

    # Check types used
    for ftr in allFeatures:
        for msg in ftr.getMsgs():
            for arg in msg.args:
                if isinstance(arg.argType, ArEnum):
                    hasArgOfType[ArArgType.ENUM] = True
                elif isinstance(arg.argType, ArBitfield):
                    hasArgOfType[ArArgType.BITFIELD] = True
                    hasArgOfType[arg.argType.btfType] = True
                else:
                    hasArgOfType[arg.argType] = True

    #################################
    # 12TH PART :                   #
    #################################
    # Generate JNI C/Java code      #
    #################################

    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            jfile = open (paths.JNIJ_OUT_DIR + interfaceName (ftr, cmd) + '.java', 'w')
            jfile.write (LICENCE_HEADER)
            jfile.write ('package ' + JNI_PACKAGE_NAME + ';\n')
            jfile.write ('\n')
            jfile.write ('/**\n')
            jfile.write (' * Interface for the command <code>' + ARCapitalize (format_cmd_name(cmd)) + '</code> in feature <code>' + ARCapitalize (get_ftr_old_name(ftr)) + '</code> listener\n')
            jfile.write (' * @author Parrot (c) 2013\n')
            jfile.write (' */\n')
            jfile.write ('public interface ' + interfaceName (ftr, cmd) + ' {\n')
            jfile.write ('\n')
            jfile.write ('    /**\n')
            jfile.write ('     * Called when a command <code>' + ARCapitalize (format_cmd_name(cmd)) + '</code> in feature <code>' + ARCapitalize (get_ftr_old_name(ftr)) + '</code> is decoded\n')
            for arg in cmd.args:
                jfile.write ('     * @param _' + arg.name + ' ' + get_arg_doc(arg).replace('\n', '\\n') + '\n')
            jfile.write ('     */\n')
            jfile.write ('    void ' + javaCbName (ftr, cmd) + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    jfile.write (', ')
                jfile.write (xmlToJava (LIB_MODULE, ftr, cmd, arg) + ' ' + arg.name)
            jfile.write (');\n')
            jfile.write ('}\n')
            jfile.close ()

    jfile = open (paths.JNI_JFILE, 'w')

    jfile.write (LICENCE_HEADER)
    jfile.write ('package ' + JNI_PACKAGE_NAME + ';\n')
    jfile.write ('\n')
    jfile.write ('import ' + SDK_PACKAGE_ROOT + 'arsal.ARNativeData;\n')
    jfile.write ('\n')
    jfile.write ('/**\n')
    jfile.write (' * Java representation of a C ' + JNIClassName + ' object.<br>\n')
    jfile.write (' * This class holds either app-generated objects, that are to be sent\n')
    jfile.write (' * to the device, or network-generated objects, that are to be decoded by\n')
    jfile.write (' * the application.\n')
    jfile.write (' * @author Parrot (c) 2013\n')
    jfile.write (' */\n')
    jfile.write ('public class ' + JNIClassName + ' extends ARNativeData {\n')
    jfile.write ('\n')
    jfile.write ('    public static final int ' + ARMacroName (LIB_MODULE, JNIClassName, 'HEADER_SIZE') + ' = 4;\n')
    jfile.write ('    public static final boolean ' + ARMacroName (LIB_MODULE, JNIClassName, 'HAS_DEBUG_COMMANDS') + ' = ')
    if genDebug:
        jfile.write ('true;\n')
    else:
        jfile.write ('false;\n')
    jfile.write ('    private static final ' + JNIDecoderClassName + '  _decoder = new '+JNIDecoderClassName+'();\n')
    jfile.write ('\n')

    # Generate bit field flags
    for ftr in allFeatures:
        oldEnumValFrm = False if ftr.classes == None else True
        for enum in ftr.enums:
            if enum.usedLikeBitfield:
                for eVal in enum.values:
                    type_, base = ('int', '1') if len(enum.values) <= 32 else ('long', '1L')
                    jfile.write ('    public static final '+type_+' ' + ARFlagValue (LIB_MODULE, get_ftr_old_name(ftr), enum.name, eVal.name) + ' = ('+base+' << '+ARJavaEnumValue (LIB_MODULE, get_ftr_old_name(ftr), enum.name, eVal.name, oldEnumValFrm)+ '.getValue());    ///< ' + eVal.doc.replace('\n', '\\n') + '\n')
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
    jfile.write ('     * Creates a new ' + JNIClassName + ' from a c pointer and size.<br>\n')
    jfile.write ('     * To optimize memory, the application can reuse an ' + JNIClassName + '\n')
    jfile.write ('     * object after it was disposed.\n')
    jfile.write ('     * @param data The original data buffer to copy\n')
    jfile.write ('     * @param dataSize The original data buffer size\n')
    jfile.write ('     */\n')
    jfile.write ('    public ' + JNIClassName + ' (long data, int dataSize) {\n')
    jfile.write ('        super (data, dataSize);\n')
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
    jfile.write ('     * Try to describe an ARNativeData as if it was an ' + JNIClassName + '.<br>\n')
    jfile.write ('     * @return A String describing the ARNativeData, if possible as an ' + JNIClassName + '.\n')
    jfile.write ('     */\n')
    jfile.write ('    public static String arNativeDataToARCommandString (ARNativeData data) {\n')
    jfile.write ('        if (data == null) { return "null"; }\n')
    jfile.write ('        String ret = nativeStaticToString(data.getData(), data.getDataSize());\n')
    jfile.write ('        if (ret == null) { ret = data.toString(); }\n')
    jfile.write ('        return ret;\n');
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * @deprecated\n')
    jfile.write ('     * Decodes the current ' + JNIClassName + ', calling commands listeners<br>\n')
    jfile.write ('     * If a listener was set for the Class/Command contained within the ' + JNIClassName + ',\n')
    jfile.write ('     * its <code>onClassCommandUpdate(...)</code> function will be called in the current thread.\n')
    jfile.write ('     * @return An ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' error code\n')
    jfile.write ('     */\n')
    jfile.write ('    public ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' decode () {\n')
    jfile.write ('        ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + ARJavaEnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR', True) + ';\n')
    jfile.write ('        if (!valid) {\n')
    jfile.write ('            return err;\n')
    jfile.write ('        }\n')
    jfile.write ('        return _decoder.decode (this);\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            jfile.write ('    /**\n')
            jfile.write ('     * Set an ' + JNIClassName + ' to hold the command <code>' + ARCapitalize (format_cmd_name(cmd)) + '</code> in feature <code>' + ARCapitalize (get_ftr_old_name(ftr)) + '</code><br>\n')
            jfile.write ('     * <br>\n')
            jfile.write ('     * Feature ' + ARCapitalize (get_ftr_old_name(ftr)) + ' description:<br>\n')
            jfile.write ('     * ' + ftr.doc.replace('\n', '\\n') + '<br>\n')
            jfile.write ('     * <br>\n')
            if cmd.cls:
                jfile.write ('     * Class ' + ARCapitalize (cmd.cls.name) + ' description:<br>\n')
                jfile.write ('     * ' + cmd.cls.doc.replace('\n', '\\n') + '<br>\n')
                jfile.write ('     * <br>\n')
            jfile.write ('     * Command ' + ARCapitalize (cmd.name) + ' description:<br>\n')
            if cmd.isDeprecated:
                jfile.write ('     * @deprecated\n')
            jfile.write ('     * ' + cmd.doc.desc.replace('\n', '<br>\n     * ') + '<br>\n')
            jfile.write ('     * <br>\n')
            jfile.write ('     * This function reuses the current ' + JNIClassName + ', replacing its content with a\n')
            jfile.write ('     * new command created from the current params\n')
            for arg in cmd.args:
                jfile.write ('     * @param _' + arg.name + ' ' + get_arg_doc(arg).replace('\n', '\\n') + '\n')
                #If the argument is a bitfield
                if isinstance(arg.argType, ArBitfield):
                    jfile.write ('     * @param _' + arg.name + ' a combination of')

                    #find the feature owning the enum
                    for bitFieldFtr in allFeatures:
                        for enum2 in bitFieldFtr.enums:
                            if enum2 == arg.argType.enum:
                                break;
                        else:
                            continue
                        break

                    for eVal in arg.argType.enum.values:
                        jfile.write (' ; ' + ARFlagValue(LIB_MODULE, bitFieldFtr.name , arg.argType.enum.name, eVal.name))
                    jfile.write ('\n')

            jfile.write ('     * @return An ' + ARJavaEnumType (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ' error code.\n')
            jfile.write ('     */\n')
            jfile.write ('    public ' + ARJavaEnumType (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ' set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    jfile.write (', ')
                jfile.write (xmlToJava (LIB_MODULE, ftr, cmd, arg) + ' _' + arg.name)
            jfile.write (') {\n')
            jfile.write ('        ' + ARJavaEnumType (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ' err = ' + ARJavaEnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR', True) + ';\n')
            jfile.write ('        if (!valid) {\n')
            jfile.write ('            return err;\n')
            jfile.write ('        }\n')
            jfile.write ('        int errInt = nativeSet' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + ' (pointer, capacity')
            for arg in cmd.args:
                if isinstance(arg.argType, ArEnum):
                    jfile.write (', _' + arg.name + '.getValue()')
                elif isinstance(arg.argType, ArMultiSetting):
                    for multiset_msg in arg.argType.msgs:
                        jfile.write (', _' + arg.name + '.get'+ARCapitalize(multiset_msg.ftr.name)+ARCapitalize(multiset_msg.name)+'IsSet()')
                        for multiset_msg_arg in multiset_msg.args:
                            jfile.write (', _' + arg.name + '.get'+ARCapitalize(multiset_msg.ftr.name)+ARCapitalize(multiset_msg.name)+ARCapitalize(multiset_msg_arg.name)+'()')
                else:
                    jfile.write (', _' + arg.name)
            jfile.write (');\n')
            jfile.write ('        if (' + ARJavaEnumType (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + '.getFromValue (errInt) != null) {\n')
            jfile.write ('            err = ' + ARJavaEnumType (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + '.getFromValue (errInt);\n')
            jfile.write ('        }\n')
            jfile.write ('        return err;\n')
            jfile.write ('    }\n')
            jfile.write ('\n')

    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            jfile.write ('    /**\n')
            jfile.write ('     * @deprecated\n')
            jfile.write ('     * Set the listener for the command <code>' + ARCapitalize (format_cmd_name(cmd)) + '</code> in feature <code>' + ARCapitalize (get_ftr_old_name(ftr)) + '</code><br>\n')
            jfile.write ('     * Listeners are static to the class, and are not to be set on every object\n')
            jfile.write ('     * @param ' + interfaceVar (ftr, cmd) + '_PARAM New listener for the command\n')
            jfile.write ('     */\n')
            jfile.write ('    public static void set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Listener (' + interfaceName (ftr, cmd) + ' ' + interfaceVar (ftr, cmd) + '_PARAM) {\n')
            jfile.write ('        _decoder.set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Listener(' + interfaceVar (ftr, cmd) + '_PARAM);\n')
            jfile.write ('    }\n')
            jfile.write ('\n')
        jfile.write ('\n')
    jfile.write ('\n')
    jfile.write ('    private native String  nativeToString (long jpdata, int jdataSize);\n')
    jfile.write ('    private static native String  nativeStaticToString (long jpdata, int jdataSize);\n')
    jfile.write ('\n')
    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            jfile.write ('    private native int     nativeSet' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + ' (long pdata, int dataTotalLength')
            for arg in cmd.args:
                if isinstance (arg.argType, ArEnum):
                    jfile.write (', int ' + arg.name)
                elif isinstance(arg.argType, ArMultiSetting):
                    for multiset_msg in arg.argType.msgs:
                        jfile.write (', int '+ARCapitalize(multiset_msg.ftr.name)+ARCapitalize(multiset_msg.name)+'IsSet')
                        for multiset_msg_arg in multiset_msg.args:
                            jfile.write (', ' + xmlToJava (LIB_MODULE, multiset_msg.ftr, multiset_msg, multiset_msg_arg) + ' '+ARUncapitalize(multiset_msg.ftr.name)+ARCapitalize(multiset_msg.name)+ARCapitalize(multiset_msg_arg.name)+'')
                else:
                    jfile.write (', ' + xmlToJava (LIB_MODULE, ftr, cmd, arg) + ' ' + arg.name)
            jfile.write (');\n')
            jfile.write ('\n')
        jfile.write ('\n')
    jfile.write ('}\n')

    jfile.close ()

    jfile = open (paths.JNI_DECODER_JFILE, 'w')

    jfile.write (LICENCE_HEADER)
    jfile.write ('package ' + JNI_PACKAGE_NAME + ';\n')
    jfile.write ('import com.parrot.arsdk.arsal.ARSALPrint;\n')
    jfile.write ('\n')
    jfile.write ('/**\n')
    jfile.write (' * Java representation of a C ' + JNIDecoderClassName + ' object.<br>\n')
    jfile.write (' * This class allow to decode ARCommands.\n')
    jfile.write (' * @author Parrot (c) 2016\n')
    jfile.write (' */\n')
    jfile.write ('public class ' + JNIDecoderClassName + ' {\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Storage of the C Pointer\n')
    jfile.write ('     */\n')
    jfile.write ('    protected long pointer;\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Check validity before all native calls\n')
    jfile.write ('     */\n')
    jfile.write ('    protected boolean valid;\n')
    jfile.write ('\n')
    jfile.write ('/**\n')
    jfile.write (' * Dummy throwable to keep the constructors call stack\n')
    jfile.write (' */\n')
    jfile.write ('\n')
    jfile.write ('    private Throwable constructorCallStack;\n')
    jfile.write ('\n')
    jfile.write ('    protected static final String TAG = "'+JNIDecoderClassName+'";\n')
    jfile.write ('\n')

    jfile.write ('    private static native void nativeStaticInit ();\n')
    jfile.write ('    static\n')
    jfile.write ('    {\n')
    jfile.write ('        nativeStaticInit();\n')
    jfile.write ('    }\n')
    jfile.write ('\n')

    jfile.write ('    /**\n')
    jfile.write ('     * Creates a new, ' + JNIDecoderClassName + '\n')
    jfile.write ('     */\n')
    jfile.write ('    public ' + JNIDecoderClassName + ' () {\n')
    jfile.write ('\n')
    jfile.write ('        this.pointer = nativeNewDecoder ();\n')
    jfile.write ('        this.valid = false;\n')
    jfile.write ('        if (this.pointer != 0) {\n')
    jfile.write ('            this.valid = true;\n')
    jfile.write ('        }\n')
    jfile.write ('        this.constructorCallStack = new Throwable();\n')
    jfile.write ('    }\n')
    jfile.write ('\n')


    jfile.write ('    /* ********** */\n')
    jfile.write ('    /* DESTRUCTOR */\n')
    jfile.write ('    /* ********** */\n')

    jfile.write ('    protected void finalize () throws Throwable {\n')
    jfile.write ('        try {\n')
    jfile.write ('            if (valid) {\n')
    jfile.write ('                ARSALPrint.w (TAG, this + ": Finalize error -> dispose () was not called !", this.constructorCallStack);\n')
    jfile.write ('                dispose ();\n')
    jfile.write ('            }\n')
    jfile.write ('        }\n')
    jfile.write ('        finally {\n')
    jfile.write ('            super.finalize ();\n')
    jfile.write ('        }\n')
    jfile.write ('    }\n')
    jfile.write ('\n')

    jfile.write ('    /* ************** */\n')
    jfile.write ('    /* IMPLEMENTATION */\n')
    jfile.write ('    /* ************** */\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('    * Checks the object validity\n')
    jfile.write ('    * @return <code>true</code> if the object is valid (buffer properly alloc and usable)<br><code>false</code> if the object is invalid (alloc error, disposed object)\n')
    jfile.write ('    */\n')
    jfile.write ('    public boolean isValid () {\n')
    jfile.write ('        return valid;\n')
    jfile.write ('    }\n')
    jfile.write ('\n')

    jfile.write ('    /**\n')
    jfile.write ('    * Marks a native data as unused (so C-allocated memory can be freed)<br>\n')
    jfile.write ('    * A disposed data is marked as invalid\n')
    jfile.write ('    */\n')
    jfile.write ('    public void dispose () {\n')
    jfile.write ('        if (valid)\n')
    jfile.write ('            nativeDeleteDecoder (pointer);\n')
    jfile.write ('        this.valid = false;\n')
    jfile.write ('        this.pointer = 0;\n')
    jfile.write ('    }\n')

    jfile.write ('    /**\n')
    jfile.write ('     * Decodes a ' + JNIClassName + ', calling commands listeners<br>\n')
    jfile.write ('     * If a listener was set for the Class/Command contained within the ' + JNIDecoderClassName + ',\n')
    jfile.write ('     * its <code>onClassCommandUpdate(...)</code> function will be called in the current thread.\n')
    jfile.write ('     * @param command command to decode.\n')
    jfile.write ('     * @return An ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' error code\n')
    jfile.write ('     */\n')
    jfile.write ('    public ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' decode ('+JNIClassName+' command) {\n')
    jfile.write ('        ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + ARJavaEnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR', True) + ';\n')
    jfile.write ('        if ((!valid) || (command == null) || (!command.isValid())) {\n')
    jfile.write ('            return err;\n')
    jfile.write ('        }\n')
    jfile.write ('        int errInt = nativeDecode (pointer, command.getData(), command.getDataSize());\n')
    jfile.write ('        if (' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '.getFromValue (errInt) != null) {\n')
    jfile.write ('            err = ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '.getFromValue (errInt);\n')
    jfile.write ('        }\n')
    jfile.write ('        return err;\n')
    jfile.write ('    }\n')
    jfile.write ('\n')

    jfile.write ('    /**\n')
    jfile.write ('     * Decodes a command calling commands listeners<br>\n')
    jfile.write ('     * If a listener was set for the Class/Command contained within the ' + JNIDecoderClassName + ',\n')
    jfile.write ('     * its <code>onClassCommandUpdate(...)</code> function will be called in the current thread.\n')
    jfile.write ('     * @param command command to decode.\n')
    jfile.write ('     * @return An ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' error code\n')
    jfile.write ('     */\n')
    jfile.write ('    public ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' decode (long data, int size) {\n')
    jfile.write ('        ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + ARJavaEnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'ERROR', True) + ';\n')
    jfile.write ('        if (!valid) {\n')
    jfile.write ('            return err;\n')
    jfile.write ('        }\n')
    jfile.write ('        int errInt = nativeDecode (pointer, data, size);\n')
    jfile.write ('        if (' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '.getFromValue (errInt) != null) {\n')
    jfile.write ('            err = ' + ARJavaEnumType (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '.getFromValue (errInt);\n')
    jfile.write ('        }\n')
    jfile.write ('        return err;\n')
    jfile.write ('    }\n')
    jfile.write ('\n')


    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            jfile.write ('    private ' + interfaceName (ftr, cmd) + ' ' + interfaceVar (ftr, cmd) + ';\n')
            jfile.write ('\n')
            jfile.write ('    /**\n')
            jfile.write ('     * Set the listener for the command <code>' + ARCapitalize (format_cmd_name(cmd)) + '</code> in feature <code>' + ARCapitalize (get_ftr_old_name(ftr)) + '</code><br>\n')
            jfile.write ('     * Listeners are static to the class, and are not to be set on every object\n')
            #~ jfile.write ('     * @param ' + interfaceVar (ftr, cmd) + '_PARAM New listener for the command\n')nativeDecode
            jfile.write ('     */\n')
            jfile.write ('    public void set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Listener (' + interfaceName (ftr, cmd) + ' ' + interfaceVar (ftr, cmd) + '_PARAM) {\n')
            jfile.write ('        ' + interfaceVar (ftr, cmd) + ' = ' + interfaceVar (ftr, cmd) + '_PARAM;\n')
            jfile.write ('    }\n')
            jfile.write ('\n')
        jfile.write ('\n')

    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            jfile.write ('    void ' + javaCbName (ftr, cmd) + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    jfile.write (', ')
                jfile.write (xmlToJava (LIB_MODULE, ftr, cmd, arg) + ' ' + arg.name)
            jfile.write (') {\n')
            jfile.write ('        if(' + interfaceVar (ftr, cmd) + ' != null) {\n')
            jfile.write ('            ' + interfaceVar (ftr, cmd) + '.' + javaCbName (ftr, cmd) + ' (')
            first = True
            for arg in cmd.args:
                if first:
                    first = False
                else:
                    jfile.write (', ')
                jfile.write (arg.name)
            jfile.write (');\n')
            jfile.write ('        }\n')
            jfile.write ('    }\n')
            jfile.write ('\n')

    jfile.write ('    /* **************** */\n')
    jfile.write ('    /* NATIVE FUNCTIONS */\n')
    jfile.write ('    /* **************** */\n')
    jfile.write ('\n')

    jfile.write ('    /**\n')
    jfile.write ('     * Memory allocation in native memory space<br>\n')
    jfile.write ('     * Allocates a decoder and return its C-Pointer\n')
    jfile.write ('     * @return C-Pointer on the decoder, or 0 (C-NULL) if the alloc failed\n')
    jfile.write ('     */\n')
    jfile.write ('    private native long nativeNewDecoder ();\n')
    jfile.write ('\n')

    jfile.write ('    /**\n')
    jfile.write ('     * Memory release in native memory space<br>\n')
    jfile.write ('     * Frees a decoder from its C-Pointer<br>\n')
    jfile.write ('     * This call is needed because JVM do not know about native memory allocs\n')
    jfile.write ('     * @param decoder C-Pointer on the decoder to free\n')
    jfile.write ('     */\n')
    jfile.write ('    private native void nativeDeleteDecoder (long decoder);\n')
    jfile.write ('\n')

    jfile.write ('    private native int     nativeDecode (long jdecoder, long jpdata, int jdataSize);\n')
    jfile.write ('\n')

    jfile.write ('}\n')
    jfile.write ('\n')

    jfile.close ()

    jfile = open (paths.JNI_FILTER_JFILE, 'w')

    jfile.write (LICENCE_HEADER)
    jfile.write ('package ' + JNI_PACKAGE_NAME + ';\n')
    jfile.write ('\n')
    jfile.write ('import com.parrot.arsdk.arsal.ARSALPrint;\n')
    jfile.write ('\n')
    jfile.write ('/**\n')
    jfile.write (' * Java implementation of a C ' + JNIFilterClassName + ' object.<br>\n')
    jfile.write (' * @author Parrot (c) 2014\n')
    jfile.write (' */\n')
    jfile.write ('public class ' + JNIFilterClassName + '\n')
    jfile.write ('{\n')
    jfile.write ('    private long cFilter;\n')
    jfile.write ('    private boolean valid;\n')
    jfile.write ('    private static final String TAG = ' + JNIFilterClassName + '.class.getSimpleName();\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Creates a new ' + JNIFilterClassName + ' which allows all commands.\n')
    jfile.write ('     */\n')
    jfile.write ('    public ' + JNIFilterClassName + ' () {\n')
    jfile.write ('        this(ARCOMMANDS_FILTER_STATUS_ENUM.ARCOMMANDS_FILTER_STATUS_ALLOWED);\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Creates a new ' + JNIFilterClassName + ' with the given default behavior.\n')
    jfile.write ('     * @param behavior The default behavior of the filter.\n')
    jfile.write ('     * @warning Only ALLOWED and BLOCK are allowed as default behavior. Providing any other value will create an invalid object.\n')
    jfile.write ('     */\n')
    jfile.write ('    public ' + JNIFilterClassName + ' (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {\n')
    jfile.write ('        this.cFilter = nativeNewFilter (behavior.getValue());\n')
    jfile.write ('        this.valid = (this.cFilter != 0);\n')
    jfile.write ('        if (! this.valid) {\n')
    jfile.write ('            dispose();\n')
    jfile.write ('        }\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Checks the object validity.\n')
    jfile.write ('     * @return <code>true</code> if the object is valid<br><code>false</code> if the object is invalid.\n')
    jfile.write ('     */\n')
    jfile.write ('    public boolean isValid () {\n')
    jfile.write ('        return valid;\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Marks a ' + JNIFilterClassName + ' as unused (so C-allocated memory can be freed)<br>\n')
    jfile.write ('     * A disposed ' + JNIFilterClassName + ' is marked as invalid.\n')
    jfile.write ('     */\n')
    jfile.write ('    public void dispose () {\n')
    jfile.write ('        if (valid) {\n')
    jfile.write ('            nativeDeleteFilter (cFilter);\n')
    jfile.write ('        }\n')
    jfile.write ('        this.valid = false;\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Gets the native pointer for this filter\n')
    jfile.write ('     * @return The pointer.\n')
    jfile.write ('     */\n')
    jfile.write ('    public long getFilter () {\n')
    jfile.write ('        return cFilter;\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    protected void finalize () throws Throwable {\n')
    jfile.write ('        try {\n')
    jfile.write ('            if (valid) {\n')
    jfile.write ('                ARSALPrint.e (TAG, this + ": Finalize error -> dispose () was not called !");\n')
    jfile.write ('                dispose ();\n')
    jfile.write ('            }\n')
    jfile.write ('        }\n')
    jfile.write ('        finally {\n')
    jfile.write ('            super.finalize ();\n')
    jfile.write ('        }\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    /**\n')
    jfile.write ('     * Filters a command.<br>\n')
    jfile.write ('     * This function returns the filter behavior for the given ' + JNIClassName + '.<br>\n')
    jfile.write ('     * @param command The command to be filtered.\n')
    jfile.write ('     * @return The filter status.\n')
    jfile.write ('     */\n')
    jfile.write ('    public ARCOMMANDS_FILTER_STATUS_ENUM filterCommand (' + JNIClassName + ' command) {\n')
    jfile.write ('        if (! valid) { return ARCOMMANDS_FILTER_STATUS_ENUM.ARCOMMANDS_FILTER_STATUS_ERROR; }\n')
    jfile.write ('        int cStatus = nativeFilterCommand (cFilter, command.getData(), command.getDataSize());\n')
    jfile.write ('        return ARCOMMANDS_FILTER_STATUS_ENUM.getFromValue(cStatus);\n')
    jfile.write ('    }\n')
    jfile.write ('\n')
    jfile.write ('    private native long nativeNewFilter (int behavior);\n')
    jfile.write ('    private native void nativeDeleteFilter (long cFilter);\n')
    jfile.write ('    private native int nativeFilterCommand (long cFilter, long command, int len);\n')
    jfile.write ('\n')
    for ftr in allFeatures:
        jfile.write ('    // Feature ' + get_ftr_old_name(ftr) + '\n')
        jfile.write ('    private native int nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + 'Behavior (long cFilter, int behavior);\n')
        jfile.write ('    /**\n')
        jfile.write ('     * Sets the behavior for all commands ' + ARCapitalize(get_ftr_old_name(ftr)) + '.XXX.XXX.\n')
        jfile.write ('     * @param behavior The behavior to set.\n')
        jfile.write ('     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.\n')
        jfile.write ('     */\n')
        jfile.write ('    public ARCOMMANDS_FILTER_ERROR_ENUM set' + ARCapitalize(get_ftr_old_name(ftr)) + 'Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {\n')
        jfile.write ('        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }\n')
        jfile.write ('        int cErr = nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + 'Behavior (this.cFilter, behavior.getValue());\n')
        jfile.write ('        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);\n')
        jfile.write ('    }\n')
        jfile.write ('\n')

        if ftr.classes: # projetc only
            for cl in ftr.classes:
                jfile.write ('    // - Class ' + cl.name + '\n')
                jfile.write ('    private native int nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cl.name)  + 'Behavior (long cFilter, int behavior);\n')
                jfile.write ('    /**\n')
                jfile.write ('     * Sets the behavior for all commands ' + ARCapitalize(get_ftr_old_name(ftr)) + '.' + ARCapitalize(cl.name) + '.XXX.\n')
                jfile.write ('     * @param behavior The behavior to set.\n')
                jfile.write ('     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.\n')
                jfile.write ('     */\n')
                jfile.write ('    public ARCOMMANDS_FILTER_ERROR_ENUM set' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cl.name)  + 'Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {\n')
                jfile.write ('        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }\n')
                jfile.write ('        int cErr = nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cl.name) + 'Behavior (this.cFilter, behavior.getValue());\n')
                jfile.write ('        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);\n')
                jfile.write ('    }\n')
                jfile.write ('\n')

                for cmd in cl.cmds:
                    jfile.write ('    private native int nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cl.name) + ARCapitalize(cmd.name) + 'Behavior (long cFilter, int behavior);\n')
                    jfile.write ('    /**\n')
                    jfile.write ('     * Sets the behavior for the command ' + ARCapitalize(get_ftr_old_name(ftr)) + '.' + ARCapitalize(cl.name) + '.' + ARCapitalize(cmd.name) + '.\n')
                    jfile.write ('     * @param behavior The behavior to set.\n')
                    jfile.write ('     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.\n')
                    jfile.write ('     */\n')
                    jfile.write ('    public ARCOMMANDS_FILTER_ERROR_ENUM set' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cl.name) + ARCapitalize(cmd.name) + 'Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {\n')
                    jfile.write ('        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }\n')
                    jfile.write ('        int cErr = nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cl.name) + ARCapitalize(cmd.name) + 'Behavior (this.cFilter, behavior.getValue());\n')
                    jfile.write ('        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);\n')
                    jfile.write ('    }\n')
                    jfile.write ('\n')
                jfile.write ('\n')
        else:
            for cmd in ftr.cmds + ftr.evts:
                jfile.write ('    private native int nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cmd.name) + 'Behavior (long cFilter, int behavior);\n')
                jfile.write ('    /**\n')
                jfile.write ('     * Sets the behavior for the command ' + ARCapitalize(get_ftr_old_name(ftr)) + '.' + ARCapitalize(cmd.name) + '.\n')
                jfile.write ('     * @param behavior The behavior to set.\n')
                jfile.write ('     * @return An ARCOMMANDS_FILTER_ERROR_ENUM value.\n')
                jfile.write ('     */\n')
                jfile.write ('    public ARCOMMANDS_FILTER_ERROR_ENUM set' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cmd.name) + 'Behavior (ARCOMMANDS_FILTER_STATUS_ENUM behavior) {\n')
                jfile.write ('        if (! valid) { return ARCOMMANDS_FILTER_ERROR_ENUM.ARCOMMANDS_FILTER_ERROR_BAD_FILTER; }\n')
                jfile.write ('        int cErr = nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cmd.name) + 'Behavior (this.cFilter, behavior.getValue());\n')
                jfile.write ('        return ARCOMMANDS_FILTER_ERROR_ENUM.getFromValue(cErr);\n')
                jfile.write ('    }\n')
                jfile.write ('\n')
        jfile.write ('\n')
    jfile.write ('}\n')

    jfile.close ()

    # Generate java enums type
    for ftr in allFeatures:
        for enum in ftr.enums:

            oldEnumValFrm = False if ftr.classes == None else True
            CLASS_NAME = ARJavaEnumType (LIB_MODULE, get_ftr_old_name(ftr), enum.name)
            JFILE_NAME = paths.JNIJ_OUT_DIR + CLASS_NAME + '.java'
            UNKNOWN_VALUE = ARJavaEnumValDef(LIB_MODULE, get_ftr_old_name(ftr), enum.name, 'UNKNOWN') if ftr.classes == None else 'e'+AREnumValue(LIB_MODULE, get_ftr_old_name(ftr), enum.name,'UNKNOWN_ENUM_VALUE')

            jfile = open(JFILE_NAME, 'w')

            jfile.write(LICENCE_HEADER)
            jfile.write('\n')
            jfile.write('package ' + JNI_PACKAGE_NAME + ';\n')
            jfile.write('\n')
            jfile.write('import java.util.HashMap;\n')
            jfile.write('\n')
            jfile.write('/**\n')
            jfile.write(' * Java copy of the ' + AREnumName (LIB_MODULE, get_ftr_old_name(ftr), enum.name) + ' enum\n')
            jfile.write(' */\n')
            jfile.write('public enum ' + CLASS_NAME + ' {\n')
            jfile.write('    /** Dummy value for all unknown cases */\n')
            jfile.write('    ' + UNKNOWN_VALUE + ' (Integer.MIN_VALUE, "Dummy value for all unknown cases"),\n')

            previousVal = -1
            for eVal in enum.values:
                val = eVal.value if eVal.value is not None else previousVal +1
                previousVal = int(val)

                jfile.write('    ')
                if eVal.doc:
                    jfile.write('/** '+eVal.doc.replace('\n', ' ')+' */\n    ')
                if eVal.doc:
                    jfile.write(ARJavaEnumValDef(LIB_MODULE, get_ftr_old_name(ftr), enum.name, eVal.name, oldEnumValFrm)+ ' (' + str(val)+ ', "'+eVal.doc.replace('\n', ' ')+'")')

                else:
                    jfile.write(ARJavaEnumValDef(LIB_MODULE, get_ftr_old_name(ftr), enum.name, eVal.name, oldEnumValFrm) + ' (' + str(val) + ')')

                #If it is the last value of a feature enum.
                if ftr.classes == None and eVal ==  enum.values[-1]:
                    jfile.write(';\n')
                else:
                    jfile.write(',\n')

            # Add MAX value only if it is an old enum.
            if ftr.classes:
                MAX_VALUE = ARJavaEnumValDef(LIB_MODULE, get_ftr_old_name(ftr), enum.name, 'MAX', oldEnumValFrm)
                jfile.write('    ' + MAX_VALUE + ' ('+ str(previousVal + 1) +');\n')
            jfile.write('\n')

            jfile.write('\n')
            jfile.write('    private final int value;\n')
            jfile.write('    private final String comment;\n');
            jfile.write('    static HashMap<Integer, ' + CLASS_NAME + '> valuesList;\n')
            jfile.write('\n')
            jfile.write('    ' + CLASS_NAME + ' (int value) {\n')
            jfile.write('        this.value = value;\n')
            jfile.write('        this.comment = null;\n')
            jfile.write('    }\n')
            jfile.write('\n')
            jfile.write('    ' + CLASS_NAME + ' (int value, String comment) {\n')
            jfile.write('        this.value = value;\n')
            jfile.write('        this.comment = comment;\n')
            jfile.write('    }\n')
            jfile.write('\n')
            jfile.write('    /**\n')
            jfile.write('     * Gets the int value of the enum\n')
            jfile.write('     * @return int value of the enum\n')
            jfile.write('     */\n')
            jfile.write('    public int getValue () {\n')
            jfile.write('        return value;\n')
            jfile.write('    }\n')
            jfile.write('\n')
            jfile.write('    /**\n')
            jfile.write('     * Gets the ' + CLASS_NAME + ' instance from a C enum value\n')
            jfile.write('     * @param value C value of the enum\n')
            jfile.write('     * @return The ' + CLASS_NAME + ' instance, or null if the C enum value was not valid\n')
            jfile.write('     */\n')
            jfile.write('    public static ' + CLASS_NAME + ' getFromValue (int value) {\n')
            jfile.write('        if (null == valuesList) {\n')
            jfile.write('            ' + CLASS_NAME + ' [] valuesArray = ' + CLASS_NAME + '.values ();\n')
            jfile.write('            valuesList = new HashMap<Integer, ' + CLASS_NAME + '> (valuesArray.length);\n')
            jfile.write('            for (' + CLASS_NAME + ' entry : valuesArray) {\n')
            jfile.write('                valuesList.put (entry.getValue (), entry);\n')
            jfile.write('            }\n')
            jfile.write('        }\n')
            jfile.write('        ' + CLASS_NAME + ' retVal = valuesList.get (value);\n')
            jfile.write('        if (retVal == null) {\n')
            jfile.write('            retVal = ' + UNKNOWN_VALUE + ';\n')
            jfile.write('        }\n')
            jfile.write('        return retVal;')
            jfile.write('    }\n')
            jfile.write('\n')
            jfile.write('    /**\n')
            jfile.write('     * Returns the enum comment as a description string\n')
            jfile.write('     * @return The enum description\n')
            jfile.write('     */\n')
            jfile.write('    public String toString () {\n')
            jfile.write('        if (this.comment != null) {\n')
            jfile.write('            return this.comment;\n')
            jfile.write('        }\n')
            jfile.write('        return super.toString ();\n')
            jfile.write('    }\n')
            jfile.write('}\n')
            jfile.close()

        for multiset in ftr.multisets:
            oldEnumValFrm = False if ftr.classes == None else True
            CLASS_NAME = ARJavaMultiSetType (LIB_MODULE, get_ftr_old_name(ftr), multiset.name)
            JFILE_NAME = paths.JNIJ_OUT_DIR + CLASS_NAME + '.java'

            jfile = open(JFILE_NAME, 'w')

            jfile.write(LICENCE_HEADER)
            jfile.write('\n')
            jfile.write('package ' + JNI_PACKAGE_NAME + ';\n')
            jfile.write('\n')
            jfile.write('/**\n')
            jfile.write(' * Java copy of the ' + ARJavaMultiSetType (LIB_MODULE, get_ftr_old_name(ftr), enum.name) + '\n')
            jfile.write(' */\n')
            jfile.write('public class ' + CLASS_NAME + ' {\n')
            jfile.write('\n')
            for multiset_msg in multiset.msgs:
                jfile.write('    private static class ' + ARCapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + ' {\n')
                jfile.write('        public int isSet;\n')
                for multiset_msg_arg in multiset_msg.args:
                    jfile.write('        public '+ xmlToJava(LIB_MODULE, multiset_msg.ftr, multiset_msg, multiset_msg_arg) +' ' + multiset_msg_arg.name + ';\n')
                jfile.write('    }\n')
                jfile.write('\n')
            jfile.write('    public ' + CLASS_NAME + ' () {\n')
            jfile.write('    }\n')
            jfile.write('\n')
            for multiset_msg in multiset.msgs:
                jfile.write('    private final ' + ARCapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + ' _' + ARUncapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + ' = new ' + ARCapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + '();\n')
            jfile.write('\n')
            for multiset_msg in multiset.msgs:
                jfile.write('    public void set' + ARCapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + ' (')
                isFirst = True
                for multiset_msg_arg in multiset_msg.args:
                    if not isFirst:
                        jfile.write(', ')
                    isFirst = False
                    jfile.write( xmlToJava(LIB_MODULE, multiset_msg.ftr, multiset_msg, multiset_msg_arg) +' ' + multiset_msg_arg.name )
                jfile.write(') {\n')
                jfile.write( '        _' + ARUncapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + '.isSet = 1;\n')
                for multiset_msg_arg in multiset_msg.args:
                    jfile.write( '        _' + ARUncapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + '.'+multiset_msg_arg.name+' = ' + multiset_msg_arg.name+';\n' )
                jfile.write('    }\n')
                jfile.write('\n')
            for multiset_msg in multiset.msgs:
                jfile.write('    public int get' + ARCapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + 'IsSet () {\n')
                jfile.write( '        return _' + ARUncapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + '.isSet;\n')
                jfile.write('    }\n')
                jfile.write('\n')
                for multiset_msg_arg in multiset_msg.args:
                    jfile.write('    public '+ xmlToJava(LIB_MODULE, multiset_msg.ftr, multiset_msg, multiset_msg_arg) +' get' + ARCapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + ARCapitalize(multiset_msg_arg.name) +' () {\n')
                    jfile.write( '        return _' + ARUncapitalize(multiset_msg.ftr.name) + ARCapitalize(multiset_msg.name) + '.'+multiset_msg_arg.name+';\n')
                    jfile.write('    }\n')
                    jfile.write('\n')
            jfile.write('}\n')
            jfile.close()

    # Generate java enums

    #enumDecErr = ArEnum(DEC_SUBMODULE+'_'+DEC_ERR_ENAME, 'Error codes for ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeBuffer') + ' function')
    enumDecErr = ArEnum(DEC_ERR_ENAME, 'Error codes for ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeBuffer') + ' function')
    enumDecErr.values.append(ArEnumValue('OK', 0, 'No error occured'))
    enumDecErr.values.append(ArEnumValue('NO_CALLBACK', 1, 'No error, but no callback was set (so the call had no effect)'))
    enumDecErr.values.append(ArEnumValue('UNKNOWN_COMMAND', 2, 'The command buffer contained an unknown command'))
    enumDecErr.values.append(ArEnumValue('NOT_ENOUGH_DATA', 3, 'The command buffer did not contain enough data for the specified command'))
    enumDecErr.values.append(ArEnumValue('NOT_ENOUGH_SPACE', 4, 'The string buffer was not big enough for the command description'))
    enumDecErr.values.append(ArEnumValue('ERROR', 5, 'Any other error'))

    #enumFilterErr = ArEnum(FIL_SUBMODULE+'_'+FIL_ERROR_ENAME, 'Error code for ARCOMMANDS_Filter functions.')
    enumFilterErr = ArEnum(FIL_ERROR_ENAME, 'Error code for ARCOMMANDS_Filter functions.')
    enumFilterErr.values.append(ArEnumValue('OK', 0,'No error.'))
    enumFilterErr.values.append(ArEnumValue('ALLOC', 1,'Memory allocation error.'))
    enumFilterErr.values.append(ArEnumValue('BAD_STATUS', 2,'The given status is not a valid status.'))
    enumFilterErr.values.append(ArEnumValue('BAD_FILTER', 3,'The given filter is not a valid filter.'))
    enumFilterErr.values.append(ArEnumValue('BAD_BUFFER', 4,'The given buffer is not a valid buffer.'))
    enumFilterErr.values.append(ArEnumValue('OTHER', 5,'Any other error.'))

    #enumFilterStatus = ArEnum(FIL_SUBMODULE+'_'+FIL_STATUS_ENAME,'Status code for ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' function')
    enumFilterStatus = ArEnum(FIL_STATUS_ENAME,'Status code for ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' function')
    enumFilterStatus.values.append(ArEnumValue('ALLOWED', 0,'The command should pass the filter'))
    enumFilterStatus.values.append(ArEnumValue('BLOCKED', 1,'The command should not pass the filter'))
    enumFilterStatus.values.append(ArEnumValue('UNKNOWN', 2,'Unknown command. The command was possibly added in a newer version of libARCommands, or is an invalid command.'))
    enumFilterStatus.values.append(ArEnumValue('ERROR', 3, 'The filtering of the command failed.'))

    #enumGenErr = ArEnum(GEN_SUBMODULE+'_'+GEN_ERR_ENAME,'Error codes for ' + ARFunctionName (LIB_MODULE, GEN_SUBMODULE, 'GenerateCommand') + ' functions')
    enumGenErr = ArEnum(GEN_ERR_ENAME,'Error codes for ' + ARFunctionName (LIB_MODULE, GEN_SUBMODULE, 'GenerateCommand') + ' functions')
    enumGenErr.values.append(ArEnumValue( 'OK', 0,'No error occured'))
    enumGenErr.values.append(ArEnumValue('BAD_ARGS', 1, 'At least one of the arguments is invalid'))
    enumGenErr.values.append(ArEnumValue('NOT_ENOUGH_SPACE', 2, 'The given output buffer was not large enough for the command'))
    enumGenErr.values.append(ArEnumValue('ERROR', 3, 'Any other error'))

    enums = [enumDecErr, enumFilterErr, enumFilterStatus, enumGenErr]
    subModules = [DEC_SUBMODULE, FIL_SUBMODULE, FIL_SUBMODULE, GEN_SUBMODULE]

    for enum in enums:
        submodule = subModules[enums.index(enum)]
        #CLASS_NAME =  LIB_MODULE.upper () +  submodule.upper() + '_' + enum.name.upper () + '_ENUM'
        CLASS_NAME = ARJavaEnumType (LIB_MODULE, submodule, enum.name)
        JFILE_NAME =  paths.JNIJ_OUT_DIR + CLASS_NAME + '.java'
        UNKNOWN_VALUE = 'e'+ARJavaEnumValDef(LIB_MODULE, submodule, enum.name, 'UNKNOWN_ENUM_VALUE', True)

        jfile = open(JFILE_NAME, 'w')

        jfile.write(LICENCE_HEADER)
        jfile.write('\n')
        jfile.write('package ' + JNI_PACKAGE_NAME + ';\n')
        jfile.write('\n')
        jfile.write('import java.util.HashMap;\n')
        jfile.write('\n')
        jfile.write('/**\n')
        jfile.write(' * Java copy of the ' + AREnumName (LIB_MODULE, submodule, enum.name) + ' enum\n')
        jfile.write(' */\n')
        jfile.write('public enum ' + CLASS_NAME + ' {\n')
        jfile.write('    /** Dummy value for all unknown cases */\n')
        jfile.write('    ' + UNKNOWN_VALUE + ' (Integer.MIN_VALUE, "Dummy value for all unknown cases"),\n')

        previousVal = -1
        for eVal in enum.values:
            val = eVal.value if eVal.value is not None else previousVal +1
            previousVal = int(val)

            jfile.write('    ')
            if eVal.doc:
                jfile.write('/** '+eVal.doc.replace('\n', ' ')+' */\n    ')
            if eVal.doc:
                jfile.write(ARJavaEnumValDef(LIB_MODULE, submodule, enum.name, eVal.name, True)+ ' (' + str(val)+ ', "'+eVal.doc.replace('\n', ' ')+'")')
            else:
                jfile.write(ARJavaEnumValDef(LIB_MODULE, submodule, enum.name, eVal.name, True) + ' (' + str(val) + ')')

            #If it is the last value of the enum.
            if eVal ==  enum.values[-1]:
                jfile.write(';\n')
            else:
                jfile.write(',\n')

        jfile.write('\n')
        jfile.write('    private final int value;\n')
        jfile.write('    private final String comment;\n');
        jfile.write('    static HashMap<Integer, ' + CLASS_NAME + '> valuesList;\n')
        jfile.write('\n')
        jfile.write('    ' + CLASS_NAME + ' (int value) {\n')
        jfile.write('        this.value = value;\n')
        jfile.write('        this.comment = null;\n')
        jfile.write('    }\n')
        jfile.write('\n')
        jfile.write('    ' + CLASS_NAME + ' (int value, String comment) {\n')
        jfile.write('        this.value = value;\n')
        jfile.write('        this.comment = comment;\n')
        jfile.write('    }\n')
        jfile.write('\n')
        jfile.write('    /**\n')
        jfile.write('     * Gets the int value of the enum\n')
        jfile.write('     * @return int value of the enum\n')
        jfile.write('     */\n')
        jfile.write('    public int getValue () {\n')
        jfile.write('        return value;\n')
        jfile.write('    }\n')
        jfile.write('\n')
        jfile.write('    /**\n')
        jfile.write('     * Gets the ' + CLASS_NAME + ' instance from a C enum value\n')
        jfile.write('     * @param value C value of the enum\n')
        jfile.write('     * @return The ' + CLASS_NAME + ' instance, or null if the C enum value was not valid\n')
        jfile.write('     */\n')
        jfile.write('    public static ' + CLASS_NAME + ' getFromValue (int value) {\n')
        jfile.write('        if (null == valuesList) {\n')
        jfile.write('            ' + CLASS_NAME + ' [] valuesArray = ' + CLASS_NAME + '.values ();\n')
        jfile.write('            valuesList = new HashMap<Integer, ' + CLASS_NAME + '> (valuesArray.length);\n')
        jfile.write('            for (' + CLASS_NAME + ' entry : valuesArray) {\n')
        jfile.write('                valuesList.put (entry.getValue (), entry);\n')
        jfile.write('            }\n')
        jfile.write('        }\n')
        jfile.write('        ' + CLASS_NAME + ' retVal = valuesList.get (value);\n')
        jfile.write('        if (retVal == null) {\n')
        jfile.write('            retVal = ' + UNKNOWN_VALUE + ';\n')
        jfile.write('        }\n')
        jfile.write('        return retVal;')
        jfile.write('    }\n')
        jfile.write('\n')
        jfile.write('    /**\n')
        jfile.write('     * Returns the enum comment as a description string\n')
        jfile.write('     * @return The enum description\n')
        jfile.write('     */\n')
        jfile.write('    public String toString () {\n')
        jfile.write('        if (this.comment != null) {\n')
        jfile.write('            return this.comment;\n')
        jfile.write('        }\n')
        jfile.write('        return super.toString ();\n')
        jfile.write('    }\n')
        jfile.write('}\n')
        jfile.close()

def jni_generateCmds(ctx, paths):
    genDebug = True
    genTreeFilename = None
    projects = [DEFAULTPROJECTNAME]

    if not os.path.exists (paths.JNI_DIR):
        os.makedirs (paths.JNI_DIR)
    if not os.path.exists (paths.JNIC_DIR):
        os.makedirs (paths.JNIC_DIR)

    allFeatures = ctx.features

    # Check types used
    for ftr in allFeatures:
        for msg in ftr.getMsgs():
            for arg in msg.args:
                if isinstance(arg.argType, ArEnum):
                    hasArgOfType[ArArgType.ENUM] = True
                elif isinstance(arg.argType, ArBitfield):
                    hasArgOfType[ArArgType.BITFIELD] = True
                    hasArgOfType[arg.argType.btfType] = True
                elif isinstance(arg.argType, ArMultiSetting):
                    hasArgOfType[ArArgType.MULTISETTING] = True
                else:
                    hasArgOfType[arg.argType] = True


    cfile = open (paths.JNI_CFILE, 'w')

    JNI_FUNC_PREFIX='Java_' + JNI_PACKAGE_NAME.replace ('.', '_') + '_'
    JNI_FIRST_ARGS='JNIEnv *env, jobject thizz'
    JNI_FIRST_ARGS_STATIC='JNIEnv *env, jclass clazz'

    cfile.write (LICENCE_HEADER)
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

    cfile.write ('JNIEXPORT jstring JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeToString (' + JNI_FIRST_ARGS + ', jlong jpdata, jint jdataSize)\n')
    cfile.write ('{\n')
    cfile.write ('    jstring ret = NULL;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
    cfile.write ('    char *cstr = calloc (TOSTRING_STRING_SIZE, 1);\n')
    cfile.write ('    if (cstr == NULL)\n')
    cfile.write ('    {\n')
    cfile.write ('        return ret;\n')
    cfile.write ('    }\n')
    cfile.write ('    err = ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DescribeBuffer') + ' ((uint8_t *)(intptr_t)jpdata, jdataSize, cstr, TOSTRING_STRING_SIZE);\n')
    cfile.write ('    if (err == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        ret = (*env)->NewStringUTF(env, cstr);\n')
    cfile.write ('    }\n')
    cfile.write ('    free (cstr);\n')
    cfile.write ('    return ret;\n')
    cfile.write ('}\n')
    cfile.write ('JNIEXPORT jstring JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeStaticToString (' + JNI_FIRST_ARGS_STATIC + ', jlong jpdata, jint jdataSize)\n')
    cfile.write ('{\n')
    cfile.write ('    jstring ret = NULL;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ';\n')
    cfile.write ('    char *cstr = calloc (TOSTRING_STRING_SIZE, 1);\n')
    cfile.write ('    if (cstr == NULL)\n')
    cfile.write ('    {\n')
    cfile.write ('        return ret;\n')
    cfile.write ('    }\n')
    cfile.write ('    err = ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DescribeBuffer') + ' ((uint8_t *)(intptr_t)jpdata, jdataSize, cstr, TOSTRING_STRING_SIZE);\n')
    cfile.write ('    if (err == ' + AREnumValue (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME, 'OK') + ')\n')
    cfile.write ('    {\n')
    cfile.write ('        ret = (*env)->NewStringUTF(env, cstr);\n')
    cfile.write ('    }\n')
    cfile.write ('    free (cstr);\n')
    cfile.write ('    return ret;\n')
    cfile.write ('}\n')
    cfile.write ('\n')

    cfile.write ('/* END OF GENERAED CODE */\n')

    cfile.close ()

    def cCallbackName (ftr, cmd):
        return ARFunctionName (LIB_MODULE, JNI_SUBMODULE, ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'nativeCallback')

    def jmethodeCbName (ftr, cmd):
        return LIB_MODULE+ '_'+JNI_SUBMODULE+ '_'+get_ftr_old_name(ftr).upper()+ '_'+format_cmd_name(cmd).upper() + '_CB'

    cfile = open (paths.JNI_DECODER_CFILE, 'w')

    cfile.write (LICENCE_HEADER)
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

    cfile.write ('typedef struct\n')
    cfile.write ('{\n')
    cfile.write ('    jobject javaDecoder; /**< java decoder */\n')
    cfile.write ('    ARCOMMANDS_Decoder_t *nativeDecoder; /**< native decoder*/\n')
    cfile.write ('} ARCOMMANDS_JNI_Decoder_t;\n')
    cfile.write ('\n')

    cfile.write ('static JavaVM *g_vm = NULL;\n')
    cfile.write ('static jfieldID g_dataSize_id = 0;\n')
    cfile.write ('\n')
    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('static jmethodID ' +jmethodeCbName(ftr, cmd)+ ';\n')
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
    cfile.write ('\n')

    cfile.write ('    return JNI_VERSION_1_6;\n')
    cfile.write ('}\n')

    cfile.write ('JNIEXPORT void JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIDecoderClassName + '_nativeStaticInit (' + JNI_FIRST_ARGS_STATIC + ')\n')
    cfile.write ('{\n')

    cfile.write ('    jclass decoder_clazz = (*env)->FindClass (env, "' + JNI_PACKAGE_NAME.replace ('.', '/') + '/' + JNIDecoderClassName + '");\n')
    cfile.write ('\n')

    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('    '+jmethodeCbName(ftr, cmd)+ ' = (*env)->GetMethodID (env, decoder_clazz, "' + javaCbName (ftr, cmd) + '", "(')
            for arg in cmd.args:
                cfile.write ('' + xmlToJavaSig (ftr, cmd, arg))
            cfile.write (')V");\n')
    cfile.write ('\n')

    cfile.write ('    /* cleanup */\n')
    cfile.write ('    (*env)->DeleteLocalRef (env, decoder_clazz);\n')

    cfile.write ('}\n')

    cfile.write ('JNIEXPORT jint JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIDecoderClassName + '_nativeDecode (' + JNI_FIRST_ARGS + ', jlong jdecoder,  jlong jpdata, jint jdataSize)\n')
    cfile.write ('{\n')
    cfile.write ('    ARCOMMANDS_JNI_Decoder_t *decoder = (ARCOMMANDS_JNI_Decoder_t *) (intptr_t)jdecoder;\n')
    cfile.write ('    uint8_t *pdata = (uint8_t *) (intptr_t)jpdata;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + ' err = ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DecodeCommand') + ' (decoder->nativeDecoder, pdata, jdataSize);\n')
    cfile.write ('    return err;\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('JNIEXPORT jint JNICALL\n')
            cfile.write (JNI_FUNC_PREFIX + JNIClassName + '_nativeSet' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + ' (' + JNI_FIRST_ARGS + ', jlong c_pdata, jint dataLen')
            for arg in cmd.args:
                if isinstance(arg.argType, ArMultiSetting):
                    for multiset_msg in arg.argType.msgs:
                        cfile.write (', jint '+multiset_msg.ftr.name+ multiset_msg.name+'IsSet')
                        for multiset_msg_arg in multiset_msg.args:
                            cfile.write (', ' + xmlToJni (multiset_msg.ftr, multiset_msg, multiset_msg_arg) + ' ' +multiset_msg.ftr.name+ multiset_msg.name+ multiset_msg_arg.name)
                else:
                    cfile.write (', ' + xmlToJni (ftr, cmd, arg) + ' ' + arg.name)
            cfile.write (')\n')
            cfile.write ('{\n')
            cfile.write ('    int32_t c_dataSize = 0;\n')
            cfile.write ('    ' + AREnumName (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + ' err = ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'ERROR') + ';\n');
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
                if ArArgType.STRING == arg.argType:
                    cfile.write ('    const char *c_' + arg.name + ' = (*env)->GetStringUTFChars (env, ' + arg.name + ', NULL);\n')
                elif isinstance(arg.argType, ArMultiSetting):
                    cfile.write ('    ' + xmlToC (LIB_MODULE, ftr, cmd, arg) + ' c_' + arg.name + ' = {\n')
                    for multiset_msg in arg.argType.msgs:
                        cfile.write ('        .'+multiset_msg.name+'.isSet = '+multiset_msg.ftr.name+ multiset_msg.name+'IsSet,\n')
                        for multiset_msg_arg in multiset_msg.args:
                            cfile.write ('        .'+multiset_msg.name+'.'+multiset_msg_arg.name+' = ' +multiset_msg.ftr.name+ multiset_msg.name+ multiset_msg_arg.name+',\n')
                    cfile.write ('};\n')
            cfile.write ('    err = ' + ARFunctionName (LIB_MODULE, GEN_SUBMODULE, 'Generate' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd))) + ' ((uint8_t *) (intptr_t) c_pdata, dataLen, &c_dataSize')
            for arg in cmd.args:
                if ArArgType.STRING == arg.argType:
                    cfile.write (', c_' + arg.name)
                elif isinstance(arg.argType, ArMultiSetting):
                    cfile.write (', &c_' + arg.name)
                else:
                    cfile.write (', (' + xmlToC (LIB_MODULE, ftr, cmd, arg) + ')' + arg.name)
            cfile.write (');\n')
            for arg in cmd.args:
                if ArArgType.STRING == arg.argType:
                    cfile.write ('    (*env)->ReleaseStringUTFChars (env, ' + arg.name + ', c_' + arg.name + ');\n')
            cfile.write ('    if (err == ' + AREnumValue (LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME, 'OK') + ')\n')
            cfile.write ('    {\n')
            cfile.write ('        (*env)->SetIntField (env, thizz, g_dataSize_id, (jint)c_dataSize);\n')
            cfile.write ('    }\n')
            cfile.write ('    return err;\n')
            cfile.write ('}\n')
            cfile.write ('\n')
        cfile.write ('\n')

    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('void ' + cCallbackName (ftr, cmd) + ' (')
            for arg in cmd.args:
                cfile.write (xmlToCcharAreConst (LIB_MODULE, ftr, cmd, arg, True) + ' ' + arg.name + ', ')
            cfile.write ('void *custom)\n')
            cfile.write ('{\n')
            cfile.write ('    ARCOMMANDS_JNI_Decoder_t *decoder = (ARCOMMANDS_JNI_Decoder_t *)custom;\n')
            cfile.write ('    jint res;\n')
            cfile.write ('    JNIEnv *env = NULL;\n')
            cfile.write ('    res = (*g_vm)->GetEnv (g_vm, (void **)&env, JNI_VERSION_1_6);\n')
            cfile.write ('    if (res < 0) { return; }\n')
            cfile.write ('\n')

            for arg in _get_args_multiset(cmd.args):
                cfile.write ('    ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE,'Decode'+ARCapitalize(ftr.name)+ARCapitalize(cmd.name))+' (decoder->nativeDecoder, '+arg.name+');\n')
            for arg in _get_args_without_multiset(cmd.args):
                if ArArgType.STRING == arg.argType:
                    cfile.write ('    jstring j_' + arg.name + ' = (*env)->NewStringUTF (env, ' + arg.name + ');\n')
                elif isinstance(arg.argType, ArEnum):
                    cfile.write ('    jclass j_' + arg.name + '_class = (*env)->FindClass (env, "' + jniEnumClassName (ftr, cmd, arg) + '");\n')
                    cfile.write ('    jmethodID j_' + arg.name + '_mid = (*env)->GetStaticMethodID (env, j_' + arg.name + '_class, "getFromValue", "(I)' + xmlToJavaSig(ftr, cmd, arg) + '");\n')
                    cfile.write ('    jobject j_' + arg.name + '_enum = (*env)->CallStaticObjectMethod (env, j_' + arg.name + '_class, j_' + arg.name + '_mid, ' + arg.name + ');\n')
            if not list(_get_args_multiset(cmd.args)):
                cfile.write ('    (*env)->CallVoidMethod (env, decoder->javaDecoder, '+jmethodeCbName (ftr, cmd))
                for arg in _get_args_without_multiset(cmd.args):
                    if ArArgType.STRING == arg.argType:
                        cfile.write (', j_' + arg.name)
                    elif isinstance(arg.argType, ArEnum):
                        cfile.write (', j_' + arg.name + '_enum')
                    else:
                        cfile.write (', ' + xmlToJniCast(ftr, cmd, arg) + arg.name)
                cfile.write (');\n')
            for arg in _get_args_without_multiset(cmd.args):
                if ArArgType.STRING == arg.argType:
                    cfile.write ('    (*env)->DeleteLocalRef (env, j_' + arg.name + ');\n')
            cfile.write ('}\n')
            cfile.write ('\n')
        cfile.write ('\n')

    cfile.write ('JNIEXPORT jlong JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIDecoderClassName + '_nativeNewDecoder (' + JNI_FIRST_ARGS + ')\n')
    cfile.write ('{\n')
    cfile.write ('    int failed = 0;\n')
    cfile.write ('    ARCOMMANDS_JNI_Decoder_t *decoder = calloc(1, sizeof(ARCOMMANDS_JNI_Decoder_t));\n')
    cfile.write ('    if (decoder == NULL)\n')
    cfile.write ('    {\n')
    cfile.write ('        failed = 1;\n')
    cfile.write ('    }\n')
    cfile.write ('    \n')

    cfile.write ('    if (!failed)\n')
    cfile.write ('    {\n')
    cfile.write ('        decoder->nativeDecoder = ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'NewDecoder') + ' (NULL);\n')
    cfile.write ('        if (decoder->nativeDecoder == NULL)\n')
    cfile.write ('        {\n')
    cfile.write ('            failed = 1;\n')
    cfile.write ('        }\n')
    cfile.write ('    }\n')
    cfile.write ('    \n')

    cfile.write ('    if (!failed)\n')
    cfile.write ('    {\n')
    cfile.write ('        decoder->javaDecoder = (*env)->NewGlobalRef(env, thizz);\n')
    cfile.write ('        if (decoder->javaDecoder == NULL)\n')
    cfile.write ('        {\n')
    cfile.write ('            failed = 1;\n')
    cfile.write ('        }\n')
    cfile.write ('    }\n')
    cfile.write ('    \n')

    cfile.write ('    if (!failed)\n')
    cfile.write ('    {\n')
    for ftr in allFeatures:
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('        ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Cb') + ' (decoder->nativeDecoder, ' + cCallbackName (ftr, cmd) + ', decoder);\n')
            cfile.write ('\n')
    cfile.write ('    }\n')
    cfile.write ('\n')

    cfile.write ('    if ((failed) && (decoder != NULL))\n')
    cfile.write ('    {\n')
    cfile.write ('        ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DeleteDecoder') + ' (&decoder->nativeDecoder);\n')
    cfile.write ('        if (decoder->javaDecoder != NULL)\n')
    cfile.write ('        {\n')
    cfile.write ('            (*env)->DeleteGlobalRef(env, decoder->javaDecoder);\n')
    cfile.write ('        }\n')
    cfile.write ('        free(decoder);\n')
    cfile.write ('        decoder = NULL;\n')
    cfile.write ('    }\n')
    cfile.write ('\n')

    cfile.write ('    return (jlong) (intptr_t) decoder;\n')
    cfile.write ('}\n')
    cfile.write ('\n')

    cfile.write ('JNIEXPORT void JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIDecoderClassName + '_nativeDeleteDecoder (' + JNI_FIRST_ARGS + ', jlong jdecoder)\n')
    cfile.write ('{\n')
    cfile.write ('    ARCOMMANDS_JNI_Decoder_t *decoder = (ARCOMMANDS_JNI_Decoder_t *) (intptr_t)jdecoder;\n')
    cfile.write ('\n')
    cfile.write ('    if (decoder != NULL)\n')
    cfile.write ('    {\n')
    cfile.write ('        ' + ARFunctionName (LIB_MODULE, DEC_SUBMODULE, 'DeleteDecoder') + ' (&decoder->nativeDecoder);\n')
    cfile.write ('        if (decoder->javaDecoder != NULL)\n')
    cfile.write ('        {\n')
    cfile.write ('            (*env)->DeleteGlobalRef(env, decoder->javaDecoder);\n')
    cfile.write ('        }\n')
    cfile.write ('        free(decoder);\n')
    cfile.write ('    }\n')
    cfile.write ('}\n')
    cfile.write ('\n')

    cfile.write ('/* END OF GENERAED CODE */\n')
    cfile.close ()

    cfile = open (paths.JNI_FILTER_CFILE, 'w')

    cfile.write (LICENCE_HEADER)
    cfile.write ('/********************************************\n')
    cfile.write (' *            AUTOGENERATED FILE            *\n')
    cfile.write (' *             DO NOT MODIFY IT             *\n')
    cfile.write (' *                                          *\n')
    cfile.write (' * To add new commands :                    *\n')
    cfile.write (' *  - Modify ../../Xml/commands.xml file    *\n')
    cfile.write (' *  - Re-run generateCommandsList.py script *\n')
    cfile.write (' *                                          *\n')
    cfile.write (' ********************************************/\n')
    cfile.write ('#include <' + COMMANDSFIL_HFILE_NAME + '>\n')
    cfile.write ('#include <jni.h>\n')
    cfile.write ('#include <stdlib.h>\n')
    cfile.write ('\n')
    cfile.write ('JNIEXPORT jlong JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIFilterClassName + '_nativeNewFilter(' + JNI_FIRST_ARGS + ', jint behavior)\n')
    cfile.write ('{\n')
    cfile.write ('    ARCOMMANDS_Filter_t *filter = ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'NewFilter') + ' (behavior, NULL);\n')
    cfile.write ('    return (jlong)(intptr_t)filter;\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    cfile.write ('JNIEXPORT void JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIFilterClassName + '_nativeDeleteFilter(' + JNI_FIRST_ARGS + ', jlong cFilter)\n')
    cfile.write ('{\n')
    cfile.write ('    ARCOMMANDS_Filter_t *filter = (ARCOMMANDS_Filter_t *)(intptr_t)cFilter;\n')
    cfile.write ('    ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'DeleteFilter') + ' (&filter);\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    cfile.write ('JNIEXPORT jint JNICALL\n')
    cfile.write (JNI_FUNC_PREFIX + JNIFilterClassName + '_nativeFilterCommand(' + JNI_FIRST_ARGS + ', jlong cFilter, jlong cCommand, jint len)\n')
    cfile.write ('{\n')
    cfile.write ('    ARCOMMANDS_Filter_t *filter = (ARCOMMANDS_Filter_t *)(intptr_t)cFilter;\n')
    cfile.write ('    uint8_t *command = (uint8_t *)(intptr_t)cCommand;\n')
    cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + ' status = ' + ARFunctionName (LIB_MODULE, FIL_SUBMODULE, 'FilterCommand') + ' (filter, command, len, NULL);\n')
    cfile.write ('    return (jint)status;\n')
    cfile.write ('}\n')
    cfile.write ('\n')
    for ftr in allFeatures:
        cfile.write ('    // Feature ' + get_ftr_old_name(ftr) + '\n')
        cfile.write ('JNIEXPORT jint JNICALL\n')
        cfile.write (JNI_FUNC_PREFIX + JNIFilterClassName + '_nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + 'Behavior (' + JNI_FIRST_ARGS + ', jlong cFilter, jint behavior)\n')
        cfile.write ('{\n')
        cfile.write ('    ARCOMMANDS_Filter_t *filter = (ARCOMMANDS_Filter_t *)(intptr_t)cFilter;\n')
        cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' err = ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + 'Behavior (filter, behavior);\n')
        cfile.write ('    return (jint)err;\n')
        cfile.write ('}\n')
        cfile.write ('\n')
        if ftr.classes:#project only
            for cl in ftr.classes:
                cfile.write ('    // - Class ' + cl.name + '\n')
                cfile.write ('JNIEXPORT jint JNICALL\n')
                cfile.write (JNI_FUNC_PREFIX + JNIFilterClassName + '_nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(cl.name) + 'Behavior (' + JNI_FIRST_ARGS + ', jlong cFilter, jint behavior)\n')
                cfile.write ('{\n')
                cfile.write ('    ARCOMMANDS_Filter_t *filter = (ARCOMMANDS_Filter_t *)(intptr_t)cFilter;\n')
                cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' err = ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (cl.name) + 'Behavior (filter, behavior);\n')
                cfile.write ('    return (jint)err;\n')
                cfile.write ('}\n')
                cfile.write ('\n')
        for cmd in ftr.cmds + ftr.evts:
            cfile.write ('JNIEXPORT jint JNICALL\n')
            cfile.write (JNI_FUNC_PREFIX + JNIFilterClassName + '_nativeSet' + ARCapitalize(get_ftr_old_name(ftr)) + ARCapitalize(format_cmd_name(cmd)) + 'Behavior (' + JNI_FIRST_ARGS + ', jlong cFilter, jint behavior)\n')
            cfile.write ('{\n')
            cfile.write ('    ARCOMMANDS_Filter_t *filter = (ARCOMMANDS_Filter_t *)(intptr_t)cFilter;\n')
            cfile.write ('    ' + AREnumName (LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + ' err = ARCOMMANDS_Filter_Set' + ARCapitalize (get_ftr_old_name(ftr)) + ARCapitalize (format_cmd_name(cmd)) + 'Behavior (filter, behavior);\n')
            cfile.write ('    return (jint)err;\n')
            cfile.write ('}\n')
            cfile.write ('\n')
        cfile.write ('\n')
    cfile.write ('/* END OF GENERAED CODE */\n')



# Functions for tree file generation
# (Wireshark Plugin)
def dump_enum_table(ftr, cl, cmd, arg):
    table = 'static struct arsdk_enum %s_%s_%s_%s_enum_tab[] = {\n' % (get_ftr_old_name(ftr), cl.name, cmd.name, arg.name)
    value = 0
    for enum in arg.enums:
        comment = enum.doc.replace('\n', '\\n')
        table += '  {\n'
        table += '    .name = "%s",\n' % enum.name
        table += '    .value = %s,\n' % AREnumValue(LIB_MODULE,
                                                    get_ftr_old_name(ftr).upper() + '_' +
                                                    cl.name.upper(),
                                                    cmd.name.upper() + '_' +
                                                    arg.name.upper(), enum.name)
        table += '    .comment = "%s"\n' % comment.replace('"', '\\"')
        table += '  },\n'
        value += 1
    table = table + '};\n'
    return table if arg.enums else ''

def dump_arg_table(ftr, cl, cmd):
    table = 'static struct arsdk_arg %s_%s_%s_arg_tab[] = {\n' % (get_ftr_old_name(ftr),
                                                                  cl.name,
                                                                  cmd.name)
    for arg in cmd.args:
        comment = get_arg_doc(arg).replace('\n', '\\n')
        if len(arg.enums) > 0:
            enums = '%s_%s_%s_%s_enum_tab' % (get_ftr_old_name(ftr),
                                              cl.name,
                                              cmd.name,
                                              arg.name)
            nenums = 'ARRAY_SIZE(%s)' % enums
        else:
            enums = 'NULL'
            nenums = '0'
        table += '  {\n'
        table += '    .name = "%s",\n' % arg.name
        if isinstance(arg.argType, ArEnum):
            table += '    .type = ARSDK_ARG_TYPE_ENUM,\n'
        elif isinstance(arg.argType, ArBitfield):
            table += '    .type = ARSDK_ARG_TYPE_%s,\n' % ArArgType.TO_STRING[arg.argType.btfType].upper()
        else:
            table += '    .type = ARSDK_ARG_TYPE_%s,\n' % ArArgType.TO_STRING[arg.argType].upper()
        table += '    .enums = %s,\n' % enums
        table += '    .nenums = %s,\n' % nenums
        table += '    .comment = "%s"\n' % comment.replace('"', '\\"')
        table += '  },\n'
    table = table + '};\n'
    return table if cmd.args else ''

def dump_cmd_table(ftr, cl):
    table = 'static struct arsdk_cmd %s_%s_cmd_tab[] = {\n' % (get_ftr_old_name(ftr),
                                                              cl.name)
    for cmd in cl.cmds:
        comment = cmd.doc.title.replace('\n', '\\n')
        if len(cmd.args) > 0:
            args = '%s_%s_%s_arg_tab' % (get_ftr_old_name(ftr), cl.name, cmd.name)
            nargs = 'ARRAY_SIZE(%s)' % args
        else:
            args = 'NULL'
            nargs = '0'
        table += '  {\n'
        table += '    .name = "%s",\n' % cmd.name
        if cl.name == 'defaultCls':
            enum_val = AREnumValue(LIB_MODULE,
                                   ID_SUBMODULE,
                                   get_ftr_old_name(ftr) +
                                   '_CMD', cmd.name)
        else:
            enum_val = AREnumValue(LIB_MODULE,
                                   ID_SUBMODULE,
                                   get_ftr_old_name(ftr) + '_' + cl.name +
                                   '_CMD', cmd.name)
        table += '    .id = %s,\n' % enum_val
        # ignore fields, .buf, .timeout, .listtype (are they used at all ?)
        table += '    .args = %s,\n' % args
        table += '    .nargs = %s,\n' % nargs
        table += '    .comment = "%s"\n' % comment.replace('"', '\\"')
        table += '  },\n'
    table = table + '};\n'
    return table if cl.cmds else ''

def dump_class_table(ftr):
    table = 'static struct arsdk_class %s_class_tab[] = {\n' % get_ftr_old_name(ftr)
    for cl in ftr.classes:
        comment = cl.doc.replace('\n', '\\n')
        if len(cl.cmds) > 0:
            cmds = get_ftr_old_name(ftr) + '_' + cl.name + '_cmd_tab'
            ncmds = 'ARRAY_SIZE(%s)' % cmds
        else:
            cmds = 'NULL'
            ncmds = '0'
        table += '  {\n'
        table += '    .name = "%s",\n' % cl.name
        if cl.name != 'defaultCls':
            table += '    .ident = %s,\n' % AREnumValue(LIB_MODULE,
                                                        ID_SUBMODULE,
                                                        get_ftr_old_name(ftr) + '_CLASS',
                                                        cl.name)
        else:
            table += '    .ident = 0,\n'
        table += '    .cmds = %s,\n' % cmds
        table += '    .ncmds = %s,\n' % ncmds
        table += '    .comment = "%s"\n' % comment.replace('"', '\\"')
        table += '  },\n'
    table = table + '};\n'
    return table if len(ftr.classes) > 0 else ''

def dump_project_table(projects):
    table = 'static struct arsdk_project arsdk_projects[] = {\n'
    for proj in projects:
        comment = proj.doc.replace('\n', '\\n')
        if proj.classes and len(proj.classes) > 0:
            classes = get_ftr_old_name(proj) + '_class_tab'
            nclasses = 'ARRAY_SIZE(%s)' % classes
        else:
            classes = 'NULL'
            nclasses = '0'
        table += '  {\n'
        table += '    .name = "%s",\n' % get_ftr_old_name(proj)
        table += '    .ident = %s,\n' % AREnumValue(LIB_MODULE,
                                                    ID_SUBMODULE,
                                                    'FEATURE',
                                                    get_ftr_old_name(proj))
        table += '    .classes = %s,\n' % classes
        table += '    .nclasses = %s,\n' % nclasses
        table += '    .comment = "%s"\n' % comment.replace('"', '\\"')
        table += '  },\n'
    table = table + '};\n'
    table += 'static const unsigned int arsdk_nprojects = '
    table += 'ARRAY_SIZE(arsdk_projects);\n'
    return table if len(projects) > 0 else ''

def dump_tree_header(ctx, filename):

    allFeatures = ctx.features

    hfile = open (filename, 'w')
    hfile.write (LICENCE_HEADER)
    hfile.write ('/********************************************\n')
    hfile.write (' *            AUTOGENERATED FILE            *\n')
    hfile.write (' *             DO NOT MODIFY                *\n')
    hfile.write (' ********************************************/\n')
    hfile.write ('\n')
    hfile.write ('#define ARRAY_SIZE(_t) (sizeof(_t)/sizeof((_t)[0]))\n')
    hfile.write ('\n')
    hfile.write ('/**\n')
    hfile.write (' * @brief libARCommands Tree dump.\n')
    hfile.write (' * @note Autogenerated file\n')
    hfile.write (' **/\n')
    hfile.write ('#ifndef _ARSDK_ARCOMMANDS_TREE_H\n')
    hfile.write ('#define _ARSDK_ARCOMMANDS_TREE_H\n')
    hfile.write ('#include <inttypes.h>\n')
    hfile.write ('#include <stdlib.h>\n')
    hfile.write ('#include <' + COMMANDSTYPES_HFILE_NAME + '>\n')
    hfile.write ('#include <' + COMMANDSID_HFILE_NAME + '>\n')
    hfile.write ('\n')
    hfile.write ('\n')
    hfile.write('enum arsdk_arg_type {\n')
    hfile.write('    ARSDK_ARG_TYPE_ENUM,\n')
    hfile.write('    ARSDK_ARG_TYPE_U8,\n')
    hfile.write('    ARSDK_ARG_TYPE_I8,\n')
    hfile.write('    ARSDK_ARG_TYPE_U16,\n')
    hfile.write('    ARSDK_ARG_TYPE_I16,\n')
    hfile.write('    ARSDK_ARG_TYPE_U32,\n')
    hfile.write('    ARSDK_ARG_TYPE_I32,\n')
    hfile.write('    ARSDK_ARG_TYPE_U64,\n')
    hfile.write('    ARSDK_ARG_TYPE_I64,\n')
    hfile.write('    ARSDK_ARG_TYPE_FLOAT,\n')
    hfile.write('    ARSDK_ARG_TYPE_DOUBLE,\n')
    hfile.write('    ARSDK_ARG_TYPE_STRING,\n')
    hfile.write('};\n')
    hfile.write ('\n')
    hfile.write ('struct arsdk_enum {\n')
    hfile.write ('    const char               *name;\n')
    hfile.write ('    unsigned int              value;\n')
    hfile.write ('    const char               *comment;\n')
    hfile.write ('};\n')
    hfile.write ('\n')
    hfile.write ('struct arsdk_arg {\n')
    hfile.write ('    const char               *name;\n')
    hfile.write ('    enum arsdk_arg_type       type;\n')
    hfile.write ('    struct arsdk_enum        *enums;\n')
    hfile.write ('    unsigned int              nenums;\n')
    hfile.write ('    const char               *comment;\n')
    hfile.write ('    void                     *priv;\n')
    hfile.write ('};\n')
    hfile.write ('\n')
    hfile.write ('struct arsdk_cmd {\n')
    hfile.write ('    const char               *name;\n')
    hfile.write ('    unsigned int              id;\n')
    hfile.write ('    struct arsdk_arg         *args;\n')
    hfile.write ('    unsigned int              nargs;\n')
    hfile.write ('    const char               *comment;\n')
    hfile.write ('    void                     *priv;\n')
    hfile.write ('};\n')
    hfile.write ('\n')
    hfile.write ('struct arsdk_class {\n')
    hfile.write ('    const char               *name;\n')
    hfile.write ('    unsigned int              ident;\n')
    hfile.write ('    struct arsdk_cmd         *cmds;\n')
    hfile.write ('    unsigned int              ncmds;\n')
    hfile.write ('    const char               *comment;\n')
    hfile.write ('    void                     *priv;\n')
    hfile.write ('};\n')
    hfile.write ('\n')
    hfile.write ('struct arsdk_project {\n')
    hfile.write ('    const char               *name;\n')
    hfile.write ('    eARCOMMANDS_ID_FEATURE    ident;\n')
    hfile.write ('    struct arsdk_class       *classes;\n')
    hfile.write ('    unsigned int              nclasses;\n')
    hfile.write ('    const char               *comment;\n')
    hfile.write ('    void                     *priv;\n')
    hfile.write ('};\n')
    hfile.write ('\n')

    # walk XML tree and dump C structures
    for ftr in allFeatures:
        defaultCls = ArClass('defaultCls', 0, '')
        for cmd in ftr.cmds + ftr.evts:
            cl = defaultCls if cmd.cls is None else cmd.cls
            for arg in cmd.args:
                hfile.write(dump_enum_table(ftr, cl, cmd, arg))
            hfile.write(dump_arg_table(ftr, cl, cmd))
        defaultCls.cmds = [cmd for cmd in (ftr.cmds + ftr.evts) if cmd.cls is None]
        if ftr.classes is None:
            ftr.classes = []
        ftr.classes.append(defaultCls)
        for cl in ftr.classes:
            hfile.write(dump_cmd_table(ftr, cl))
        hfile.write(dump_class_table(ftr))
    hfile.write(dump_project_table(allFeatures))

    hfile.write('#endif /* _ARSDK_ARCOMMANDS_TREE_H */\n')
    hfile.close()

#===============================================================================
#===============================================================================
def native_list_files(ctx, outdir, paths):
    # print c generated files
    for f in paths.GENERATED_FILES:
        print(os.path.join(outdir, f))

#===============================================================================
#===============================================================================
def android_list_files(ctx, paths):

    # print java enum class files
    for ftr in ctx.features:
        for enum in ftr.enums:
            print(paths.JNIJ_OUT_DIR + ARJavaEnumType(LIB_MODULE, get_ftr_old_name(ftr), enum.name) + '.java')
        for multiset in ftr.multisets:
            print(paths.JNIJ_OUT_DIR + ARJavaMultiSetType(LIB_MODULE, get_ftr_old_name(ftr), multiset.name) + '.java')

    # print java listener class files
    for ftr in ctx.features:
        for cmd in ftr.cmds + ftr.evts:
            print(paths.JNIJ_OUT_DIR + interfaceName(ftr, cmd) + '.java')

    # print java generated files
    for f in paths.GENERATED_JAVA_FILES:
        print(os.path.join(outdir, f))

    # print java enum files generated from enums C
    print(paths.JNIJ_OUT_DIR + ARJavaEnumType(LIB_MODULE, DEC_SUBMODULE, DEC_ERR_ENAME) + '.java')
    print(paths.JNIJ_OUT_DIR + ARJavaEnumType(LIB_MODULE, FIL_SUBMODULE, FIL_ERROR_ENAME) + '.java')
    print(paths.JNIJ_OUT_DIR + ARJavaEnumType(LIB_MODULE, FIL_SUBMODULE, FIL_STATUS_ENAME) + '.java')
    print(paths.JNIJ_OUT_DIR + ARJavaEnumType(LIB_MODULE, GEN_SUBMODULE, GEN_ERR_ENAME) + '.java')

#===============================================================================
#===============================================================================
def jni_list_files(ctx, paths):
    # print c generated files
    for f in paths.GENERATED_JNI_FILES:
        print(os.path.join(outdir, f))

#===============================================================================
#===============================================================================
def list_files(ctx, outdir, extra):
    paths = Paths(outdir)

    if extra == "native":
        native_list_files(ctx, outdir, paths)
    elif extra == "android":
        android_list_files(ctx, paths)
    elif extra == "jni":
        jni_list_files(ctx, paths)
#===============================================================================
#===============================================================================
def generate_files(ctx, outdir, extra):
    paths = Paths(outdir)

    if extra == "native":
        # Generation
        native_generateCmds(ctx, paths)
        PREBUILD_ACTION = PACKAGES_DIR+'/ARSDKBuildUtils/Utils/Python/ARSDK_PrebuildActions.py'
        os.system(PREBUILD_ACTION+' --lib libARCommands --root '+LIBARCOMMANDS_DIR+' --outdir '+outdir + ' --disable-java')
    elif extra == "java":
        # Generation
        java_generateCmds(ctx, paths)
    elif extra == "jni":
        # Generation
        jni_generateCmds(ctx, paths)
    elif extra == "tree":
        dump_tree_header(ctx, './tree.h')

#===============================================================================
#===============================================================================
#if __name__ == "__main__":
#    generateCmds()
