//
// Created by torquato on 07/02/25.
//

#include <jni.h>
#include <android/log.h>

#define jni_prefix(func) Java_xyz_torquato_bookbuy_data_BookDataSource_ ## func


extern "C"
JNIEXPORT jstring JNICALL
jni_prefix(example)(JNIEnv *env, jobject) {
    __android_log_write(ANDROID_LOG_ERROR,
                        "DEBUG", "JNI call");

    return env->NewStringUTF("Example");

}
