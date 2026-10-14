@echo off
REM Compiles Roast into %ROAST_HOME%\classes and ConAn into %CONAN_HOME%\classes.
REM Run this after changing any Roast or ConAn source file.
REM Works from each home directory with relative paths, so javac expands the
REM wildcards and paths containing spaces or a trailing \ are safe.
setlocal
if "%CONAN_HOME%"=="" goto noenv
if "%ROAST_HOME%"=="" goto noenv

REM ---- Roast (old code: warnings suppressed)
pushd "%ROAST_HOME%" || goto fail
if exist classes rmdir /s /q classes
mkdir classes
javac -nowarn -XDsuppressNotes -d classes -cp classes roast\*.java roast\viewer\*.java
if errorlevel 1 goto fail
copy /y roast\viewer\*.gif classes\roast\viewer\ >nul
if errorlevel 1 goto fail
popd
echo Roast built into %ROAST_HOME%\classes

REM ---- ConAn: Conan.java pulls in the translator via -sourcepath;
REM      runtime classes are listed explicitly
pushd "%CONAN_HOME%" || goto fail
if exist classes rmdir /s /q classes
mkdir classes
javac -d classes -cp classes -sourcepath . conan\Conan.java conan\runtime\*.java
if errorlevel 1 goto fail
popd
echo ConAn built into %CONAN_HOME%\classes
exit /b 0

:noenv
echo CONAN_HOME and ROAST_HOME must be set. Run setenv first.
exit /b 1

:fail
popd
echo Build failed.
exit /b 1
