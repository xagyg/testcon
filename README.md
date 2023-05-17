# testcon
ConAn - concurrency analysis testing tool

A subset of the TestCon project, ConAn builds on Roast to provide a testing framework for concurrent Java components. For more details on the development of ConAn and TestCon/Crom, see http://longbrothers.net/brad/papers/thesis.pdf

SIX STEPS TO SET UP CONAN
=========================

1. Ensure JAVA is installed on your system.
   You can check this by typing "java -version" at the command prompt.

2. Ensure Perl is installed on your system.
   You can check this by typing "perl -version" at the command prompt.

3. Go to the "bin" directory and customize "setenv.bat"
   (or .sh equivalent as applicable).

4. Open a command prompt and execute "setenv" (or already have these
   environment variables set).
   If you set these environment variables permanently you will not have to
   execute "setenv" each time you open a command shell to use ConAn.

5. To generate a driver for the TestDriver.conan test script, open
   a command prompt. Go to the ConAn home directory (i.e. where ConAn was
   installed):

   $ cd samples
   $ conan TestDriver

6. To execute the driver:

   $ java TestDriver
