REM Example script to set environment variables for ConAn
set CONAN_HOME=<conan root directory>
set ROAST_HOME=%CONAN_HOME%\roast2.0
set PATH=%PATH%;%CONAN_HOME%\bin
set CLASSPATH=%CLASSPATH%;%CONAN_HOME%;%ROAST_HOME%;.
