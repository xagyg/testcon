#!/bin/sh
# Example script for building a ConAn driver.
# A ConAn script is passed as the argument.
# The output is a compiled test driver.
# Execute the resultant driver using the "java" command.
perl $ROAST_HOME/bin/roast -nocheck < $1.conan > $1.script
java conan.Conan $1 < $1.script
javac $1.java
