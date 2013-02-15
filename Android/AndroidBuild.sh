#!/bin/sh

# Android build for libARCommands

CURDIR=$(pwd)

DIRNAME=$(echo $0 | sed 's:\(.*\)/.*:\1/:')

BUILDDIR=$DIRNAME/../Build

LIBNAME=$(cat $BUILDDIR/configure.ac | grep AC_INIT | sed 's:.*(\[\([^]]*\).*:\1:')
LIBNAME_LOWER=$(echo $LIBNAME | tr [:upper:] [:lower:])

# $1 will always be the --prefix=... arg

# $2 will always be the --with-libARSALInstallDir=... arg

# $3 is the action (release, debug, clean)

PREFIX=$1
LIBARSALINSTALLDIR=$2
CONFIGURATION=$(echo $3  | tr [:upper:] [:lower:])

echo "Building "$LIBNAME" with prefix : <"$PREFIX">"
echo "And target : <"$CONFIGURATION">"

# If any arg is missing, return
if [ -z $PREFIX ] || [ -z $LIBARSALINSTALLDIR ] || [ -z $CONFIGURATION ]; then
	echo "Missing args !"
	exit 1
fi

# If arg 3 is clean, run cleanup, delete installed files (if any) and return
if [ "xclean" = "x$CONFIGURATION" ]; then
	cd $BUILDDIR
	./cleanup
	cd $CURDIR
	if [ -d $PREFIX ]; then
		cd $PREFIX
		LIBS=$(find ./lib -name "$LIBNAME_LOWER*" 2>/dev/null)
		for LIBFILE in $LIBS; do
			rm -f $LIBFILE
		done
		if [ -d ./include/$LIBNAME/ ]; then
			rm -r ./include/$LIBNAME/
		fi
	fi
	exit 0
fi

# Setup configure args
CONF_ARGS="--prefix=$PREFIX --with-libARSALInstallDir=$LIBARSALINSTALLDIR --host=arm-linux-androideabi"

CURRINC_FOLDER=$LIBNAME

# Add debug flag if needed
if [ "xdebug" = "x$CONFIGURATION" ]; then
	CONF_DEBUG=" --enable-debug"
	CURRLIB_PATTERN=$LIBNAME_LOWER"_dbg.*"
else
	CONF_DEBUG=""
	CURRLIB_PATTERN=$LIBNAME_LOWER".*"
fi


cd $BUILDDIR
# If configure file does not exist, run bootstrap
if [ ! -f configure ]; then
	./bootstrap || exit 1
fi

FORCE_MAKE="NO"
FORCE_CLEAN="NO"

# If config.log or Makefile does not exist, run configure
if [ ! -f config.log ] || [ ! -f Makefile ]; then
	./configure $CONF_ARGS $CONF_DEBUG || exit 1
	FORCE_MAKE="YES"
	FORCE_CLEAN="YES"
else # config.log exist, check if args were good
	PREV_CONF_ARGS=$(cat config.log | grep "\$ \./configure" | sed 's:[\ \t]*$[\ \t]*\./configure ::')
	STRIP_CONF_ARGS=$(echo "$CONF_ARGS""$CONF_CFLAGS""$CONF_DEBUG" | sed 's:"::g')
	# Rerun configure if previous args were not good
	if [ "$STRIP_CONF_ARGS" != "$PREV_CONF_ARGS" ]; then
		./configure $CONF_ARGS $CONF_DEBUG || exit 1
		FORCE_MAKE="YES"
		FORCE_CLEAN="YES"
	fi
fi

# Run make clean only if we did a reconfigure of the project (and the Makefile file exists)
if [ -f Makefile ] && [ "YES" = $FORCE_CLEAN ]; then
	make clean || exit 1
fi

# Run make//make install only if one of the following is true (and the Makefile file exists) :
# - We had to run configure again
# - We can't find lib<our_lib>[_dbg].* ($CURRLIB_PATTERN variable) in $PREFIX/lib/
# - We can't find lib<Our_Lib>/ folder ($CURRINC_FOLDER variable) in $PREFIX/include/
if [ ! -d $PREFIX/lib/ ] || [ ! -d $PREFIX/include/$CURRINC_FOLDER ]; then
	echo "Force make because directory does not exist :"
	[ -d $PREFIX/lib/ ] || echo $PREFIX/lib
	[ -d $PREFIX/include/$CURRINC_FOLDER ] || echo $PREFIX/include/$CURRINC_FOLDER
	FORCE_MAKE="YES"
else
	HAS_MY_LIB=$(ls $PREFIX/lib/$CURRLIB_PATTERN 2>/dev/null | wc -w | bc)
	if [ 0 -eq $HAS_MY_LIB ]; then
		FORCE_MAKE="YES"
	fi
fi

if [ -f Makefile ] && [ "YES" = $FORCE_MAKE ]; then
	make || exit 1
	make install || exit 1 
fi

cd $CURDIR
exit 0
