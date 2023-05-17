#!/bin/sh
# Example script to set environment variables for ConAn
CONAN_HOME=<conan root directory>
ROAST_HOME=<roast root directory>
PATH=$PATH:${CONAN_HOME}/bin
CLASSPATH=$CLASSPATH:$CONAN_HOME:$ROAST_HOME:.
export CONAN_HOME ROAST_HOME CLASSPATH PATH
