LOCAL_PATH := $(call my-dir)

# JNI Wrapper
include $(CLEAR_VARS)

# directory containing generated files
GEN_OUT = $(NDK_OUT)/libarcommand-jni-gen/

LOCAL_CFLAGS := -g
LOCAL_SHARED_LIBRARIES := libARCommands
LOCAL_MODULE := libarcommands_android

LOCAL_GEN_FILES := \
	$(GEN_OUT)/JNI/c/ARCOMMANDS_JNIFilter.c \
	$(GEN_OUT)/JNI/c/ARCOMMANDS_JNIDecoder.c \
	$(GEN_OUT)/JNI/c/ARCOMMANDS_JNI.c

LOCAL_SRC_FILES := \
	JNI/c/ARCOMMANDS_JNIVersion.c \
	$(LOCAL_GEN_FILES)

LOCAL_LDLIBS := -llog -lz
include $(BUILD_SHARED_LIBRARY)

# Add rule to run jni commands generator.
# ----------------------------------------

# avoid defining the rule multiple time as this Android.mk is included for each eabi
ifndef ARSDK_XML_ROOT

# arsdkgen in current arch product sdk
ARSDK_XML_ROOT := $(PRODUCT_OUT_DIR)/$(TARGET_ARCH_ABI)/sdk/host/usr/lib/arsdkgen/
LIBARCMDS_DIR := $(PACKAGES_DIR)/libARCommands/

# LOCAL_GEN_FILES depend on parser, generator, and all xml files
$(LOCAL_GEN_FILES): $(ARSDK_XML_ROOT)/arsdkparser.py $(LIBARCMDS_DIR)/Tools/libARCommandsgen.py $(wildcard $(ARSDK_XML_ROOT)/xml/*.xml)
	$(ARSDK_XML_ROOT)/arsdkgen.py $(LIBARCMDS_DIR)/Tools/libARCommandsgen.py -o $(GEN_OUT) jni

clean-arsdk-jni-$(TARGET_ARCH_ABI):: clean-arsdk-generated

clean-arsdk-generated:
	rm $(GEN_OUT)/*

endif
