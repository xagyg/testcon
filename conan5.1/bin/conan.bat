@echo off
REM Example script for building a ConAn driver.
REM A ConAn script is passed as the argument.
REM The output is a compiled test driver.
REM Execute the resultant driver using the "java" command.
perl %ROAST_HOME%\bin\roast -nocheck < %1.conan > %1.script
java conan.Conan %1 < %1.script
javac %1.java
