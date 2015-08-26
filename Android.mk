LOCAL_PATH := $(call my-dir)

# JNI Wrapper
include $(CLEAR_VARS)

LOCAL_CFLAGS := -g
ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
    LOCAL_CFLAGS += -mfloat-abi=softfp -mfpu=neon
endif
LOCAL_SHARED_LIBRARIES := libARCommands-prebuilt
LOCAL_MODULE := libarcommands_android
LOCAL_SRC_FILES := gen/JNI/c/ARCOMMANDS_JNIFilter.c JNI/c/ARCOMMANDS_JNIVersion.c gen/JNI/c/ARCOMMANDS_JNI.c
LOCAL_LDLIBS := -llog -lz
include $(BUILD_SHARED_LIBRARY)
