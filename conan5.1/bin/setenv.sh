#!/bin/sh
# Sets environment variables for ConAn. Source it so the variables stay set:
#     . bin/setenv.sh
# CONAN_HOME is worked out from this file's location (bash and zsh). In other
# shells, set CONAN_HOME yourself before sourcing this file.
if [ -n "${BASH_SOURCE:-}" ]; then _setenv_file=$BASH_SOURCE; else _setenv_file=$0; fi
_setenv_home=$(cd "$(dirname "$_setenv_file")/.." 2>/dev/null && pwd)
if [ -n "$_setenv_home" ] && [ -f "$_setenv_home/bin/setenv.sh" ]; then
    CONAN_HOME=$_setenv_home
elif [ -z "$CONAN_HOME" ]; then
    echo "Could not locate ConAn; set CONAN_HOME to the conan5.1 folder and source this again." >&2
fi
unset _setenv_file _setenv_home
if [ -n "$CONAN_HOME" ]; then
    ROAST_HOME=$CONAN_HOME/roast2.0
    PATH=$PATH:$CONAN_HOME/bin
    CLASSPATH=${CLASSPATH:+$CLASSPATH:}$CONAN_HOME/classes:$ROAST_HOME/classes:.
    export CONAN_HOME ROAST_HOME CLASSPATH PATH
    echo "CONAN_HOME=$CONAN_HOME"
fi
