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

class BookData {
public:
    std::string title;
    std::string author;
    std::string description;

    BookData() {}
};

jobject mapToBooList(JNIEnv *env,const Json::Value& items);
jobject createBookData(JNIEnv *env, const BookData &bookData);
Json::Value queryBook(const std::string& query,int maxResults,int startIndex);
jobject stringToJString(JNIEnv* env, const std::string& str);
void parse_json(const std::string &rawJson);

extern "C"
JNIEXPORT jstring JNICALL
jni_prefix(example)(JNIEnv *env, jobject) {
    __android_log_write(ANDROID_LOG_ERROR,
                        "DEBUG", "JNI call");

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


extern "C"
JNIEXPORT jobject JNICALL
jni_prefix(getBooks)(JNIEnv *env, jobject ) {
    __android_log_write(ANDROID_LOG_ERROR,
                        "DEBUG", "JNI call");

    Json::Value items = queryBook("ios", 20, 0);
    jobject bookItem = mapToBooList(env, items);
    return bookItem;
}

jobject
mapToBooList(
        JNIEnv *env,
        const Json::Value& items
) {
    jobject result;
    int idx = 0;
    for (const auto &item: items) {
        BookData bookData;
        auto itemInfo = item["volumeInfo"];

        if (!itemInfo.isNull()) {
            if (!itemInfo["title"].isNull())
                bookData.title = itemInfo["title"].asCString();
            __android_log_write(ANDROID_LOG_ERROR,
                                "DEBUG:TITLE", itemInfo["title"].asCString());
        }

        if (!itemInfo["authors"].isNull()) {
            if (itemInfo["authors"].isArray()) {
                for (const auto &author: itemInfo["authors"]) {
                    __android_log_write(ANDROID_LOG_ERROR,
                                        "DEBUG:AUTH", author.asCString());
                }
                bookData.author = itemInfo["authors"].get(Json::ArrayIndex(0), "example").asCString();
            }
        }

        if (!itemInfo["description"].isNull()) {
            __android_log_write(ANDROID_LOG_ERROR,
                                "DEBUG:DESC", itemInfo.get("description", "empty").asCString());
            bookData.description = itemInfo["description"].asCString();
        } else {
            bookData.description = "Not Found";
        }
        if (idx++ == 0) {
            result = createBookData(env, bookData);
        }
    }
    return result;
}


jobject createBookData(JNIEnv *env, const BookData &bookData) {

    jclass cls = env->FindClass("xyz/torquato/bookbuy/data/model/BookData");

    jmethodID methodId = env->GetMethodID(cls, "<init>", "()V");
    jfieldID titleId = env->GetFieldID(cls, "title", "Ljava/lang/String;");
    jfieldID authorId = env->GetFieldID(cls, "author", "Ljava/lang/String;");
    jfieldID descriptionId = env->GetFieldID(cls, "description", "Ljava/lang/String;");

    jobject newTitle = stringToJString(env, bookData.title);
    jobject newAuthor = stringToJString(env, bookData.author);
    jobject newDescription = stringToJString(env, bookData.description);

    jobject bookItem = env->NewObject(cls, methodId);

    env->SetObjectField(bookItem, titleId, newTitle);
    env->SetObjectField(bookItem, authorId, newAuthor);
    env->SetObjectField(bookItem, descriptionId, newDescription);

    return bookItem;
}

Json::Value
queryBook(
        const std::string& query,
        int maxResults,
        int startIndex
) {
    Json::Value items;
    CURL* curl = curl_easy_init();
    if (curl) {
        std::string url = std::string("https://www.googleapis.com/books/v1/volumes?q=") +
                          query +
                          std::string("&maxResults=") +
                          std::to_string(maxResults) +
                          std::string("&startIndex=") + std::to_string(startIndex);
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

        Json::Value root;
        Json::Reader reader;
        reader.parse(result, root);

        items = root["items"];

        curl_easy_cleanup(curl);
    }
    return items;
}

jobject stringToJString(JNIEnv* env, const std::string& str)
{
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
    };

    return result;
}
