@echo off
REM Sets environment variables for ConAn in the current cmd window.
REM CONAN_HOME is worked out from this file's location (the folder above bin),
REM so no editing is needed.
REM From PowerShell, use setenv.ps1 instead: variables set by a .bat file do
REM not carry back into PowerShell.
for %%I in ("%~dp0..") do set "CONAN_HOME=%%~fI"
set "ROAST_HOME=%CONAN_HOME%\roast2.0"
set "PATH=%PATH%;%CONAN_HOME%\bin"
set "CLASSPATH=%CLASSPATH%;%CONAN_HOME%\classes;%ROAST_HOME%\classes;."
echo CONAN_HOME=%CONAN_HOME%
