cmake_minimum_required(VERSION 3.22.1)

project("booklib")

add_library(${PROJECT_NAME} SHARED
        main.cpp)


find_package(openssl REQUIRED CONFIG)
find_package(curl REQUIRED CONFIG)
find_package(jsoncpp REQUIRED CONFIG)


target_link_libraries(${PROJECT_NAME}
        curl::curl
        jsoncpp::jsoncpp
        android
        log)