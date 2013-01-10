#!/bin/sh

CURDIR=$(pwd)
BUILDDIR=$CURDIR/../../Build
LIBNAME=$(cat $BUILDDIR/configure.ac | grep AC_INIT | sed 's:.*(\[\([^]]*\).*:\1:')
LIBNAME_LOWER=$(echo $LIBNAME | tr [:upper:] [:lower:])

# $1 will always be the --prefix=... arg
# we assume that the prefix is ALSO the install dir of other libs

# $2 is the action (release, debug, clean)

PREFIX=$(echo $1 | perl -pe 's:\x13::g' -) #Strip hexa characters added by XCode
CONFIGURATION=$(echo $2 | perl -pe 's:\x13::g' | tr [:upper:] [:lower:])

echo "Building "$LIBNAME" with prefix : <"$PREFIX">"
echo "And target : <"$CONFIGURATION">"

# If any arg is missing, return
if [ -z $PREFIX ] || [ -z $CONFIGURATION ]; then
	echo "Missing args !"
	exit 1
fi

# If arg 2 is clean, run cleanup, delete installed files (if any) and return
if [ "xclean" = "x$CONFIGURATION" ]; then
	cd $BUILDDIR
	./cleanup
	cd $CURDIR
	if [ -d $PREFIX ]; then
		cd $PREFIX
		LIBS=$(find ./lib -name "lib$LIBNAME_LOWER*")
		for LIBFILE in $LIBS; do
			rm -f $LIBFILE
		done
		if [ -d ./include/lib$LIBNAME/ ]; then
			rm -r ./include/lib$LIBNAME/
		fi
	fi
	exit 0
fi

# Setup configure args
CONF_ARGS="--prefix=$PREFIX --with-libSalInstall=$PREFIX CC=/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/usr/bin/llvm-gcc --host=arm-apple CFLAGS="
CONF_CFLAGS="-arch armv7 -isysroot /Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS6.0.sdk"

CURRINC_FOLDER="lib"$LIBNAME

# Add debug flag if needed
if [ "xdebug" = "x$CONFIGURATION" ]; then
	CONF_DEBUG=" --enable-debug"
	CURRLIB_PATTERN="lib"$LIBNAME_LOWER"_dbg.*"
else
	CONF_DEBUG=""
	CURRLIB_PATTERN="lib"$LIBNAME_LOWER".*"
fi


cd $BUILDDIR
# If configure file does not exist, run bootstrap
if [ ! -f configure ]; then
    # XCode treats some "normal" output of configure as error, so we modify them to avoid false error detection
	./bootstrap 2>&1 | sed 's:configure\.ac\:\([0-9]*\)\::configure.ac[\1]:g' || exit 1
fi

FORCE_MAKE="NO"
FORCE_CLEAN="NO"

# If config.log or Makefile does not exist, run configure
if [ ! -f config.log ] || [ ! -f Makefile ]; then
	CSCRIPT=./tmp_cfscript.sh
	echo "#!/bin/sh
./configure $CONF_ARGS\"$CONF_CFLAGS\"$CONF_DEBUG" > $CSCRIPT
	chmod +x $CSCRIPT
	./$CSCRIPT || exit 1
	rm $CSCRIPT
	FORCE_MAKE="YES"
	FORCE_CLEAN="YES"
else # config.log exist, check if args were good
	PREV_CONF_ARGS=$(cat config.log | grep "\$ \./configure" | sed 's:[\ \t]*$[\ \t]*\./configure ::')
	STRIP_CONF_ARGS=$(echo "$CONF_ARGS""$CONF_CFLAGS""$CONF_DEBUG" | sed 's:"::g')
	# Rerun configure if previous args were not good
	if [ "$STRIP_CONF_ARGS" != "$PREV_CONF_ARGS" ]; then
		CSCRIPT=./tmp_cfscript.sh
		echo "#!/bin/sh
./configure $CONF_ARGS\"$CONF_CFLAGS\"$CONF_DEBUG" > $CSCRIPT
		chmod +x $CSCRIPT
		./$CSCRIPT || exit 1
	    rm $CSCRIPT
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
	FORCE_MAKE="YES"
else
	HAS_MY_LIB=$(ls $PREFIX/lib/$CURRLIB_PATTERN | wc -w | bc)
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
