#                                               -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.
AC_INIT([libARCommands], [1.0], [ardrone@ardrone.org])
AC_CONFIG_HEADERS([config.h])
AC_CONFIG_AUX_DIR(config)
AC_CONFIG_MACRO_DIR(m4)

# automake initialisation and check for minimal automake API version 1.9
AM_INIT_AUTOMAKE ([foreign -Wall -Werror])

# use the C compiler for the following checks
AC_LANG([C])

##########################################################################
# Debug compilation support
##########################################################################
AC_MSG_CHECKING([whether to build with debug information])
AC_ARG_ENABLE([debug],
    [AS_HELP_STRING([--enable-debug],
    [enable debug data generation (def=no)])],
    [debugit="$enableval"],
    [debugit=no])
AC_MSG_RESULT([$debugit])

if test x"$debugit" = x"yes"; then
    AC_DEFINE([DEBUG],[],[Debug Mode])
    CFLAGS+=" -g -Wall -Werror -Wno-uninitialized -O0 -fno-inline"
else
    AC_DEFINE([NDEBUG],[],[No-debug Mode])
    CFLAGS+=" -O3"
fi
AM_CONDITIONAL([DEBUG_MODE], [test "$debugit" = "yes"])

##########################################################################
# Define libraries directories
##########################################################################

# get the directories of libSal
AC_ARG_WITH([libSalInstall],
            AS_HELP_STRING([--with-libSalInstall=DIR],[directory of the libSal installation]),
            [if test x$withval = xno; then
              libSalInstall=""
            else
              libSalInstall="$withval"
            fi], libSalInstall="")

AC_ARG_WITH([libSalHeader],
            AS_HELP_STRING([--with-libSalHeader=DIR],[directory of the libSal headers]),
            [if test x$withval = xno; then
              libSalHeader="$libSalInstall/include"
            else
              libSalHeader="$withval"
            fi], libSalHeader="$libSalInstall/include")

AC_ARG_WITH([libSalBin],
            AS_HELP_STRING([--with-libSalBin=DIR],[directory of the libSal binaries]),
            [if test x$withval = xno; then
              libSalBin="$libSalInstall/lib"
            else
              libSalBin="$withval"
            fi], libSalBin="$libSalInstall/lib")

##########################################################################
# Define includes directories
##########################################################################

# include local headers first
CFLAGS+=" -I ../Includes -I../Sources"
# add the headers directory of libSal
CFLAGS+=" -I$libSalHeader"

CPPFLAGS="$CFLAGS"
##########################################################################
# Define binaries directories
##########################################################################

# add the binaries directory of libSal
LDFLAGS+=" -L$libSalBin"

##########################################################################
# Check headers
##########################################################################

# Checks for libSAL.h
AC_CHECK_HEADERS([libSAL/print.h libSAL/thread.h libSAL/mutex.h \
libSAL/socket.h libSAL/time.h],,
	AC_MSG_ERROR(The libSAL header is required in order to build the library ! 
- Use INCLUDEDIR for indicate a specific include path.))

##########################################################################
# Checks for programs.
AC_PROG_CC
AC_PROG_LIBTOOL

AC_CHECK_PROGS([DOXYGEN], [doxygen])
m4_include(doxygen.ac)
DX_HTML_FEATURE(ON)
DX_CHM_FEATURE(OFF)
DX_CHI_FEATURE(OFF)
DX_MAN_FEATURE(OFF)
DX_RTF_FEATURE(OFF)
DX_XML_FEATURE(OFF)
DX_PDF_FEATURE(OFF)
DX_PS_FEATURE(OFF)
DX_INIT_DOXYGEN([libARCommands], [doxygen.cfg], [../Doc])
AM_CONDITIONAL([HAVE_DOXYGEN], [test -n "$DOXYGEN"])

AC_CHECK_PROGS([PYTHON], [python])
AM_CONDITIONAL([HAVE_PYTHON], [test -n "$PYTHON"])
AM_COND_IF([HAVE_PYTHON], [$PYTHON ../Xml/generateLibARCommands.py], AC_MSG_ERROR(python is required to generate source files for this library))

# Checks for header files.
AC_HEADER_STDC

# Checks for typedefs, structures, and compiler characteristics.
AC_TYPE_SIZE_T

# distribute additional compiler and linker flags
# --> set these variables instead of CFLAGS or LDFLAGS
AC_SUBST([CFLAGS])
AC_SUBST([LDFLAGS])

# Checks for library functions.


# Generates Makefile
AC_CONFIG_FILES([Makefile])
AC_OUTPUT

echo "
  ($PACKAGE_NAME) version $PACKAGE_VERSION
  Prefix.........: $prefix
  Debug Build....: $debugit
  C Compiler.....: $CC $CFLAGS
  Linker.........: $LD $LDFLAGS $LIBS
  Doxygen........: ${DOXYGEN:-NONE}
"
