//
// Created by torquato on 07/02/25.
//

#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <curl/curl.h>
#include <json/json.h>

#define jni_prefix(func) Java_xyz_torquato_bookbuy_data_BookDataSource_ ## func

size_t WriteCallback(void* contents, size_t size, size_t nmemb, std::string* userp) {
    const size_t realsize{ size * nmemb };

    if (!userp)
        return 0;

    userp->append((char*)contents, realsize);

    return realsize;
}


void parse_json(const std::string &rawJson);

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

        parse_json(result);

        curl_easy_cleanup(curl);
    }

    return env->NewStringUTF("Example");

}

void parse_json(const std::string &rawJson) {
    Json::Value root;

    Json::Reader reader;
    reader.parse(rawJson, root);

    auto items = root["items"];
    unsigned int length = items.size();

    for (const auto &item: root["items"]) {
        auto itemInfo = item["volumeInfo"];
        __android_log_write(ANDROID_LOG_ERROR,
                            "DEBUG:TITLE", itemInfo["title"].asCString());
        for (const auto &author: itemInfo["authors"]) {
            __android_log_write(ANDROID_LOG_ERROR,
                                "DEBUG:AUTH", author.asCString());
        }
        __android_log_write(ANDROID_LOG_ERROR,
                            "DEBUG:DESC", itemInfo.get("description", "empty").asCString());
    }
}
