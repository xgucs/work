LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

# Only compile source java files in this apk.
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_JAVA_LIBRARIES := droidlogic

LOCAL_PACKAGE_NAME := BoxFactoryTest
#different sdk may be can't build ok.
#LOCAL_SDK_VERSION := current

#LOCAL_PROGUARD_ENABLED := full
#LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_CERTIFICATE := platform

LOCAL_DEX_PREOPT := false

include $(BUILD_PACKAGE)

