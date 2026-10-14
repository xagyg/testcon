#!/bin/sh
# Compiles Roast into $ROAST_HOME/classes and ConAn into $CONAN_HOME/classes.
# Run this after changing any Roast or ConAn source file.
if [ -z "$CONAN_HOME" ] || [ -z "$ROAST_HOME" ]; then
    echo "CONAN_HOME and ROAST_HOME must be set. Run setenv first." >&2
    exit 1
fi

# Roast (old code: warnings suppressed)
rm -rf "$ROAST_HOME/classes"
mkdir -p "$ROAST_HOME/classes"
javac -nowarn -XDsuppressNotes -d "$ROAST_HOME/classes" -cp "$ROAST_HOME/classes" \
    "$ROAST_HOME"/roast/*.java "$ROAST_HOME"/roast/viewer/*.java || exit 1
cp "$ROAST_HOME"/roast/viewer/*.gif "$ROAST_HOME/classes/roast/viewer/" || exit 1
echo "Roast built into $ROAST_HOME/classes"

# ConAn: Conan.java pulls in the translator via -sourcepath; runtime classes are listed explicitly
rm -rf "$CONAN_HOME/classes"
mkdir -p "$CONAN_HOME/classes"
javac -d "$CONAN_HOME/classes" -cp "$CONAN_HOME/classes" -sourcepath "$CONAN_HOME" \
    "$CONAN_HOME/conan/Conan.java" "$CONAN_HOME"/conan/runtime/*.java || exit 1
echo "ConAn built into $CONAN_HOME/classes"
