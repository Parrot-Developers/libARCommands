#!/bin/sh

CURDIR=$(pwd)
BUILDDIR=$CURDIR/../Build
LIBNAME=$(cat $BUILDDIR/configure.ac | grep AC_INIT | sed 's:.*(\[\([^]]*\).*:\1:')
LIBNAME_LOWER=$(echo $LIBNAME | tr [:upper:] [:lower:])

# $1 will always be the --prefix=... arg
# we assume that the prefix is ALSO the install dir of other libs

# $2 is the action (release, debug, clean)

PREFIX=$1
FRAMEWORK_PATH=$PREFIX/Frameworks/
CONFIGURATION=$(echo $2 | tr [:upper:] [:lower:])

echo "Building "$LIBNAME" with prefix : <"$PREFIX">"
echo "And target : <"$CONFIGURATION">"

# If any arg is missing, return
if [ -z $PREFIX ] || [ -z $CONFIGURATION ]; then
	echo "Missing args !"
	echo " usage:"
	echo $0 "install/dir action"
	echo ""
	echo "Valid actions:"
	echo " - release > Build the lib in release mode"
	echo " - debug   > Build the lib in debug mode"
	echo " - clean   > Remove all debug/release products"
	exit 1
fi

# If arg 2 is clean, run cleanup, delete installed files (if any) and return
if [ "xclean" = "x$CONFIGURATION" ]; then
	cd $BUILDDIR
	./cleanup
	cd $CURDIR
	if [ -d $PREFIX ]; then
		cd $PREFIX
		find ./lib/ -name $LIBNAME_LOWER'.'* -delete 2>/dev/null
		find ./lib/ -name $LIBNAME_LOWER'_dbg.'* -delete 2>/dev/null
		find ./lib/ -name $LIBNAME_LOWER'-'*'.'* -delete 2>/dev/null
		find ./lib/ -name $LIBNAME_LOWER'_dbg-'*'.'* -delete 2>/dev/null
		rm -r $FRAMEWORK_PATH/$LIBNAME'.framework' 2>/dev/null
		rm -r $FRAMEWORK_PATH/$LIBNAME'_dbg.framework' 2>/dev/null
	fi
	exit 0
fi

# Get latest SDK Available to Xcode
SDKLIST=$(ls /Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/ | grep iPhoneOS)
LATEST_SDK=""
LATEST_SDK_MAJ="0"
LATEST_SDK_MIN="0"
for SDK in $SDKLIST; do
	SDKNUM_MAJOR=$(echo $SDK | sed 's:iPhoneOS\([0-9]*\)\..*:\1:')
	SDKNUM_MINOR=$(echo $SDK | sed 's:iPhoneOS[0-9]*\.\([0-9]*\).*:\1:')
	if [ $SDKNUM_MAJOR -gt $LATEST_SDK_MAJ ]; then
		LATEST_SDK=$SDK
	elif [ $SDKNUM_MAJOR -eq $LATEST_SDK_MAJ ] && [ $SDKNUM_MINOR -gt $LATEST_SDK_MIN ]; then
		LATEST_SDK=$SDK
	fi
done

if [ -z $LATEST_SDK ]; then
	echo "Unable to find a suitable SDK for Xcode"
	exit 1
fi

# Setup configure args
CONF_ARGS="--prefix=$PREFIX CC=/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/usr/bin/llvm-gcc --host=arm-apple CFLAGS="
CONF_CFLAGS="-arch armv7 -isysroot /Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/$LATEST_SDK"
CURRINC_FOLDER=$LIBNAME

# Add debug flag if needed
if [ "xdebug" = "x$CONFIGURATION" ]; then
	CONF_DEBUG=" --enable-debug"
	CURRLIB_PATTERN=$LIBNAME_LOWER"_dbg.*"
	STATIC_LIB=$LIBNAME_LOWER"_dbg.a"
	FRAMEWORK=$FRAMEWORK_PATH"/"$LIBNAME"_dbg.framework"
	FRAMEWORK_LIBNAME=$LIBNAME"_dbg"
else
	CONF_DEBUG=""
	CURRLIB_PATTERN=$LIBNAME_LOWER".*"
	STATIC_LIB=$LIBNAME_LOWER".a"
	FRAMEWORK=$FRAMEWORK_PATH"/"$LIBNAME".framework"
	FRAMEWORK_LIBNAME=$LIBNAME
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

# Create framework in current dir
F_HDIR=$FRAMEWORK/Headers
F_LDIR=$FRAMEWORK/
mkdir -p $F_HDIR # Sufficient to create all other dirs

# Copy headers
cp -r $PREFIX/include/$CURRINC_FOLDER/*.h $F_HDIR

# Lipo lib files
lipo $PREFIX/lib/$STATIC_LIB -create -output $F_LDIR/$FRAMEWORK_LIBNAME

exit 0
