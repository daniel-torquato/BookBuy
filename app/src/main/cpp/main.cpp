//
// Created by torquato on 07/02/25.
//

#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <curl/curl.h>


#define jni_prefix(func) Java_xyz_torquato_bookbuy_data_BookDataSource_ ## func

size_t WriteCallback(void* contents, size_t size, size_t nmemb, std::string* userp) {
    const size_t realsize{ size * nmemb };

    if (!userp)
        return 0;

    userp->append((char*)contents, realsize);

    return realsize;
}


extern "C"
JNIEXPORT jstring JNICALL
jni_prefix(example)(JNIEnv *env, jobject) {
    __android_log_write(ANDROID_LOG_ERROR,
                        "DEBUG", "JNI call");

    CURL* curl = curl_easy_init();
    if (curl) {
        std::string url = "https://www.googleapis.com/books/v1/volumes?q=ios&maxResults=20&startIndex=0";
        std::string result;
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &result);

        CURLcode res = curl_easy_perform(curl);

        if (res != CURLE_OK)
            result = R"({"error":"Failed to fetch data"})";

        __android_log_write(ANDROID_LOG_ERROR,
                            "DEBUG", std::to_string(res).c_str());
        __android_log_write(ANDROID_LOG_ERROR,
                            "DEBUG", result.c_str());


        curl_easy_cleanup(curl);
    }

    return env->NewStringUTF("Example");

}