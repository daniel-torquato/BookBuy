cmake_minimum_required(VERSION 3.22.1)

project("booklib")

add_library(${PROJECT_NAME} SHARED
        main.cpp)

target_link_libraries(${PROJECT_NAME}
        android
        log)