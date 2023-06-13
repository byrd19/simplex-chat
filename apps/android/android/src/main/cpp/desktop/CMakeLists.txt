# Manually downloaded headers because `find_package(JNI REQUIRED)` doesn't work while cross-compiling:

# https://raw.githubusercontent.com/openjdk/jdk/master/src/java.base/share/native/include/jni.h
include_directories(${CMAKE_SOURCE_DIR}/include)
if(UNIX)
    # https://raw.githubusercontent.com/openjdk/jdk/master/src/java.base/unix/native/include/jni_md.h
    include_directories(${CMAKE_SOURCE_DIR}/include/unix)
elseif(WIN32)
    # https://raw.githubusercontent.com/openjdk/jdk/master/src/java.base/windows/native/include/jni_md.h
    include_directories(${CMAKE_SOURCE_DIR}/include/windows)
endif()

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.
project("app")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
    app-lib

    # Sets the library as a shared library.
    SHARED

    # Provides a relative path to your source file(s).
    simplex-api.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

#find_library( # Sets the name of the path variable.
#    c-lib

    # Specifies the name of the NDK library that
    # you want CMake to locate.
#    c
#    NAMES libc.so
#    REQUIRED)

add_library( simplex SHARED IMPORTED )
set_target_properties( simplex PROPERTIES IMPORTED_LOCATION
    ${CMAKE_SOURCE_DIR}/libs/linux-x86_64/libsimplex.a)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
    app-lib

    simplex
)