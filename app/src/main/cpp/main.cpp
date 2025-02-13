//
// Created by torquato on 07/02/25.
//

#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <curl/curl.h>

#define jni_prefix(func) Java_xyz_torquato_bookbuy_data_books_BookDataSource_ ## func

size_t WriteCallback(void *contents, size_t size, size_t nmemb, std::string *userp);
void queryBook(const std::string &query, int maxResults, int startIndex, std::string *result);

void setJSONCallback(JNIEnv *env, jobject dataSource, jobject jsonData, jobject error);
jobject createJSON(JNIEnv *env, jobject rawJSON);
jobject createJSONException(JNIEnv *env, jstring message);
jobject stringToJString(JNIEnv *env, const std::string &str);

extern "C"
JNIEXPORT void JNICALL
jni_prefix(Search)(
        JNIEnv *env,
        jobject _this,
        jstring query,
        jint startIndex,
        jint maxResults
) {
    __android_log_write(ANDROID_LOG_ERROR,
                        "DEBUG", "JNI call");

    jobject bookItem = nullptr;
    jobject error = nullptr;
    try {
        std::string result;
        std::string queryString = env->GetStringUTFChars(query, nullptr);
        queryBook(queryString, maxResults, startIndex, &result);
        jobject _result = stringToJString(env, result);
        bookItem = createJSON(env, _result);
    } catch (const std::exception& e) {
        error = createJSONException(env, env->NewStringUTF(e.what()));
    }
    setJSONCallback(env, _this, bookItem, error);
}

void
setJSONCallback(
        JNIEnv *env,
        jobject dataSource,
        jobject jsonData,
        jobject error
) {
    jclass clsDataSource = env->FindClass("xyz/torquato/bookbuy/data/books/BookDataSource");

    jmethodID setId = env->GetMethodID(clsDataSource, "setResult",
                                       "(Lorg/json/JSONObject;Lorg/json/JSONException;)V");

    env->CallVoidMethod(dataSource, setId, jsonData, error);
}


jobject
createJSON(
        JNIEnv *env,
        jobject rawJSON
) {
    jclass clsJSON = env->FindClass("org/json/JSONObject");

    jmethodID initId = env->GetMethodID(clsJSON, "<init>", "(Ljava/lang/String;)V");

    jobject result = env->NewObject(clsJSON, initId, rawJSON);

    return result;
}

jobject
createJSONException(
        JNIEnv *env,
        jstring message
) {
    jclass clsJSONException = env->FindClass("org/json/JSONException");

    jmethodID initId = env->GetMethodID(clsJSONException, "<init>", "(Ljava/lang/String;)V");

    jobject result = env->NewObject(clsJSONException, initId, message);

    return result;
}

void
queryBook(
        const std::string &query,
        int maxResults,
        int startIndex,
        std::string *result
) {

    CURL *curl = curl_easy_init();
    if (curl) {
        std::string url = std::string("https://www.googleapis.com/books/v1/volumes?q=") +
                          query +
                          std::string("&maxResults=") +
                          std::to_string(maxResults) +
                          std::string("&startIndex=") + std::to_string(startIndex);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, result);

        CURLcode res = curl_easy_perform(curl);

        if (res != CURLE_OK)
            *result = std::string(R"({"error":"Failed to fetch data"})");

        curl_easy_cleanup(curl);
    }
}

jobject stringToJString(JNIEnv *env, const std::string &str) {
    int state = 0;
    jstring encoding = nullptr;
    jclass stringClass = nullptr;
    jmethodID methodId = nullptr;
    jobject result = nullptr;


    auto longStr = std::vector<char>(str.length(), '\0');
    copy(str.begin(), str.end(), longStr.begin());

    auto byteArray = env->NewByteArray((jsize) str.length());
    if (byteArray == nullptr)
        state = -1;

    if (state == 0)
        env->SetByteArrayRegion(byteArray, 0, (jsize) str.length(),
                                reinterpret_cast<jbyte *>(longStr.data()));

    if (state == 0) {
        encoding = env->NewStringUTF("UTF-8");
        if (encoding == nullptr) {
            state = 3;
        }
    }

    if (state == 0) {
        stringClass = env->FindClass("java/lang/String");
        if (stringClass == nullptr)
            state = 2;
    }

    if (state == 0) {
        methodId = env->GetMethodID(stringClass, "<init>", "([BLjava/lang/String;)V");
        if (methodId == nullptr)
            state = 1;
    }

    if (state == 0) {
        result = env->NewObject(stringClass, methodId, byteArray, encoding);
    }

    switch (state) {
        case 0:
        case 1:
            env->DeleteLocalRef(stringClass);
        case 2:
            env->DeleteLocalRef(encoding);
        case 3:
            env->DeleteLocalRef(byteArray);
        default:
            break;
    }

    return result;
}

size_t WriteCallback(void *contents, size_t size, size_t nmemb, std::string *userp) {
    const size_t realsize{size * nmemb};

    if (!userp)
        return 0;

    userp->append((char *) contents, realsize);

    return realsize;
}