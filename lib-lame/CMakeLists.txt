# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

add_library(
             lamemp3
             SHARED
             src/main/cpp/lamemp3.cpp )

find_library(
              log-lib
              log )

set(distribution_DIR ${CMAKE_SOURCE_DIR}/../../../../libs)

add_library( mp3lame
             SHARED
             IMPORTED)
set_target_properties( mp3lame
                       PROPERTIES IMPORTED_LOCATION
                       ../../../../libs/armeabi-v7a/libmp3lame.so )

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

include_directories(libs/include)

target_link_libraries( lamemp3 mp3lame ${log-lib} )
