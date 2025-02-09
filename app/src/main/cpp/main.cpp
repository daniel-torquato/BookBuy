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

    BookData() = default;
};


jboolean appendItemToBookList(JNIEnv *env, jobject bookList, jobject data);

jboolean appendItemToArrayList(JNIEnv *env, jobject list, jobject data);

jobject createEmptyBookList(JNIEnv *env);

jobject mapToBookList(JNIEnv *env, const Json::Value &items);

jobject createBookData(JNIEnv *env, const BookData &bookData);

Json::Value queryBook(const std::string &query, int maxResults, int startIndex);

jobject stringToJString(JNIEnv *env, const std::string &str);

extern "C"
JNIEXPORT jstring JNICALL
jni_prefix(example)(JNIEnv *env, jobject) {
    __android_log_write(ANDROID_LOG_ERROR,
                        "DEBUG", "JNI call");

    return env->NewStringUTF("Example");

}

extern "C"
JNIEXPORT jobject JNICALL
jni_prefix(getBooks)(JNIEnv *env, jobject ) {
    __android_log_write(ANDROID_LOG_ERROR,
                        "DEBUG", "JNI call");

    Json::Value items = queryBook("ios", 20, 0);
    jobject bookItem = mapToBookList(env, items);
    return bookItem;
}

jobject
mapToBookList(
        JNIEnv *env,
        const Json::Value& items
) {
    jobject result = createEmptyBookList(env);
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

       if (!appendItemToBookList(env, result, createBookData(env, bookData))) {
           __android_log_write(ANDROID_LOG_ERROR,
                               "DEBUG", "Could not add item");
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

jobject createEmptyBookList(JNIEnv *env) {
    jclass clsBookList = env->FindClass("xyz/torquato/bookbuy/data/model/BookList");
    jclass clsArrayList = env->FindClass("java/util/ArrayList");

    jmethodID methodId = env->GetMethodID(clsBookList, "<init>", "()V");
    jmethodID arrayListInitId = env->GetMethodID(clsArrayList, "<init>", "()V");

    jobject bookList = env->NewObject(clsBookList, methodId);
    jfieldID itemsId = env->GetFieldID(clsBookList, "items", "Ljava/util/ArrayList;");

    jobject arrayList = env->NewObject(clsArrayList, arrayListInitId);

    env->SetObjectField(bookList, itemsId, arrayList);

    return bookList;
}

jboolean
appendItemToBookList(
        JNIEnv *env,
        jobject bookList,
        jobject data
) {
    jclass clsBookList = env->FindClass("xyz/torquato/bookbuy/data/model/BookList");

    jfieldID itemsId = env->GetFieldID(clsBookList, "items", "Ljava/util/ArrayList;");
    jobject bookItems = env->GetObjectField(bookList, itemsId);

    return appendItemToArrayList(env, bookItems, data);
}

jboolean
appendItemToArrayList(
        JNIEnv *env,
        jobject list,
        jobject data
) {
    jclass clsArrayList = env->FindClass("java/util/ArrayList");

    jmethodID addId = env->GetMethodID(clsArrayList, "add", "(Ljava/lang/Object;)Z");

    return env->CallBooleanMethod(list, addId, data);
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
    }

    return result;
}
