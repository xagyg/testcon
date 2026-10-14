@echo off
REM Builds a ConAn test driver.
REM Usage: conan ScriptName   (reads ScriptName.conan, writes and compiles ScriptName.java)
REM Execute the resultant driver using the "java" command.
if not exist "%CONAN_HOME%\classes\conan\Conan.class" goto build
if not exist "%ROAST_HOME%\classes\roast\Roast.class" goto build
goto run
:build
call "%CONAN_HOME%\bin\build.bat"
if errorlevel 1 exit /b 1
:run
perl "%ROAST_HOME%\bin\roast" -nocheck < %1.conan > %1.script
if errorlevel 1 exit /b 1
java conan.Conan %1 < %1.script
if errorlevel 1 exit /b 1
javac %1.java
