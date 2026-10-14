# testcon
ConAn - concurrency analysis testing tool

A subset of the TestCon project, ConAn builds on Roast to provide a testing framework for concurrent Java components. For more details on the development of ConAn and TestCon/Crom, see http://longbrothers.net/brad/papers/thesis.pdf

SIX STEPS TO SET UP CONAN
=========================

1. Ensure JAVA is installed on your system.
   You can check this by typing "java -version" at the command prompt.

2. Ensure Perl is installed on your system.
   You can check this by typing "perl -version" at the command prompt.

3. setenv works out where ConAn is installed, so it needs no editing.
   It sets CONAN_HOME, ROAST_HOME, PATH and CLASSPATH for the current window.

4. From the ConAn home directory (conan5.1), run the setenv for your shell
   (or already have these environment variables set):

     cmd:          bin\setenv
     PowerShell:   .\bin\setenv.ps1
     bash:         . bin/setenv.sh

   If you set these environment variables permanently you will not have to
   execute "setenv" each time you open a command shell to use ConAn.

   ConAn and Roast are compiled into "classes" directories the first time you
   run "conan". If you change any ConAn or Roast source file, run "build" to
   recompile.

5. To generate a driver for the TestDriver.conan test script, open
   a command prompt. Go to the ConAn home directory (i.e. where ConAn was
   installed):

   $ cd samples<br>
   $ conan TestDriver

6. To execute the driver:

   $ java TestDriver
