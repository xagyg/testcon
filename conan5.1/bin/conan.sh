#!/bin/sh
# Builds a ConAn test driver.
# Usage: conan <ScriptName>   (reads ScriptName.conan, writes and compiles ScriptName.java)
# Execute the resultant driver using the "java" command.
if [ ! -f "$CONAN_HOME/classes/conan/Conan.class" ] || [ ! -f "$ROAST_HOME/classes/roast/Roast.class" ]; then
    "$CONAN_HOME/bin/build.sh" || exit 1
fi
perl "$ROAST_HOME/bin/roast" -nocheck < "$1.conan" > "$1.script" || exit 1
java conan.Conan "$1" < "$1.script" || exit 1
javac "$1.java"
