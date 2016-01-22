LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libARCommands
LOCAL_DESCRIPTION := ARSDK Piloting and User Commands
LOCAL_CATEGORY_PATH := dragon/libs

LOCAL_MODULE_FILENAME := libarcommands.so

LOCAL_LIBRARIES := libARSAL

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/Includes \
	$(LOCAL_PATH)/gen/Includes \
	$(LOCAL_PATH)/Sources

LOCAL_SRC_FILES := \
	gen/Sources/ARCOMMANDS_Generator.c \
	gen/Sources/ARCOMMANDS_Decoder.c \
	gen/Sources/ARCOMMANDS_Filter.c \
	gen/Sources/ARCOMMANDS_ReadWrite.c \
	Sources/ARCOMMANDS_Version.c

LOCAL_INSTALL_HEADERS := \
	gen/Includes/libARCommands/ARCOMMANDS_Generator.h:usr/include/libARCommands/ \
	gen/Includes/libARCommands/ARCOMMANDS_Decoder.h:usr/include/libARCommands/ \
	gen/Includes/libARCommands/ARCOMMANDS_Types.h:usr/include/libARCommands/ \
	gen/Includes/libARCommands/ARCOMMANDS_Filter.h:usr/include/libARCommands/ \
	Includes/libARCommands/ARCOMMANDS_Version.h:usr/include/libARCommands/ \
	gen/Includes/libARCommands/ARCOMMANDS_Ids.h:usr/include/libARCommands/ \
	Includes/libARCommands/ARCommands.h:usr/include/libARCommands/

include $(BUILD_LIBRARY)
