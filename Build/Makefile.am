###############################################################################
# THE LIBRARIES TO BUILD
###############################################################################
ACLOCAL_AMFLAGS = -I m4

# The list of header files that belong to the library (to be installed later)
HEADER_FILES	=	../Includes/libARCommands/commandsTypes.h	\
					../Includes/libARCommands/commandsList.h	\
					../Includes/libARCommands/commands.h

# The sources to add to the library and to add to the source distribution
SOURCE_FILES	=	$(HEADER_FILES)		\
					../Sources/commands.c	\
					../Sources/commandsList.c

# The library names to build (note we are building static and shared libs)
if DEBUG_MODE
lib_LTLIBRARIES				=	libarcommands_dbg.la
libarcommands_dbg_ladir		=	$(includedir)/libARCommands
libarcommands_dbg_la_HEADERS	=	$(HEADER_FILES)
libarcommands_dbg_la_SOURCES	=	$(SOURCE_FILES)
else
lib_LTLIBRARIES				=	libarcommands.la
libarcommands_ladir			=	$(includedir)/libARCommands
libarcommands_la_HEADERS		=	$(HEADER_FILES)
libarcommands_la_SOURCES		=	$(SOURCE_FILES)
endif



check_PROGRAMS	=					../TestBench/test1

___TestBench_test1_SOURCES		=	../TestBench/test1.c
if DEBUG_MODE
___TestBench_test1_LDADD		=	-lsal_dbg  \
									libarcommands_dbg.la
else
___TestBench_test1_LDADD		=	-lsal \
									libarcommands.la
endif

CLEAN_FILES = libarcommands.la libarcommands_dbg.la

EXTRA_DIST = bootstrap

# Ensure the distribution of the doxygen configuration file
# Doxygen support
if HAVE_DOXYGEN
include doxygen.am
EXTRA_DIST += doxygen.ac doxygen.am $(DX_CONFIG)
endif
