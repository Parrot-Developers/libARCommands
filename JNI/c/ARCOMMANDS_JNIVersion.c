/*
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

#include <jni.h>
#include <stdlib.h>
#include <libARCommands/ARCOMMANDS_Version.h>

JNIEXPORT jstring JNICALL
Java_com_parrot_arsdk_arcommands_ARCommandsVersion_getVersionCode(JNIEnv *env, jclass clazz)
{
    return (*env)->NewStringUTF(env, ARCOMMANDS_VERSION_STRING);
}


JNIEXPORT jintArray JNICALL
Java_com_parrot_arsdk_arcommands_ARCommandsVersion_getVersionCodeAsInt(JNIEnv *env, jclass clazz)
{
    jintArray ret = (*env)->NewIntArray(env, 4);
    if (ret != NULL)
    {
        jint *data = (*env)->GetIntArrayElements(env, ret, NULL);
        if (data != NULL)
        {
            data[0] = ARCOMMANDS_VERSION_MAJOR;
            data[1] = ARCOMMANDS_VERSION_MINOR;
            data[2] = ARCOMMANDS_VERSION_REVISION;
            data[3] = ARCOMMANDS_VERSION_COMMIT;
        }
        (*env)->ReleaseIntArrayElements(env, ret, data, 0);
    }
    return ret;
}



JNIEXPORT jint JNICALL
Java_com_parrot_arsdk_arcommands_ARCommandsVersion_nativeCompareVersionsCode(JNIEnv *env, jclass clazz, jstring v1, jstring v2)
{
    jint ret = 0;

    const char *v1_c = (*env)->GetStringUTFChars(env, v1, NULL);
    const char *v2_c = (*env)->GetStringUTFChars(env, v2, NULL);

    ret = ARCOMMANDS_Version_CompareVersionCodes(v1_c, v2_c);

    (*env)->ReleaseStringUTFChars(env, v1, v1_c);
    (*env)->ReleaseStringUTFChars(env, v2, v2_c);

    return ret;
}


JNIEXPORT jint JNICALL
Java_com_parrot_arsdk_arcommands_ARCommandsVersion_nativeCompareVersions(JNIEnv *env, jclass clazz,
                                                                         jint v1M, jint v1m, jint v1r, jint v1c,
                                                                         jint v2M, jint v2m, jint v2r, jint v2c)
{
    return ARCOMMANDS_Version_CompareVersions(v1M, v1m, v1r, v1c,
                                              v2M, v2m, v2r, v2c);
}
