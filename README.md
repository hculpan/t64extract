t64extract
==========

Java utility to extract PRG from a T64 file

This simple utility was just something I threw together to extract programs from T64 files because SD2IEC doesn't load T64 files.  I also tried c1541 that comes with VICE, but that didn't seem to work for me either.  Perhaps I was just doing something wrong, but it kept giving me a "Drive not ready" error.  And since I'm on a Mac, the Windows utilities weren't really an option either.

So I wrote a small Java utility to do what I needed.  It's pretty basic at the moment.  You can give it a T64 file and it will extract all the files.  This utility does not support data files, however; it assumes all files are PRG's.


REQUIREMENTS

This utility requires Java 7 or above.


INSTALLING

This is released in binary form as a zip file with only two files, a Java jar files and a shell script to launch it.  The jar file is an executable file (i.e., it is launchable with "java -jar" or by double-clicking on it if your OS supports that), so the launcher shell script is not specifically necessary.


USING

Usage: t64extract [OPTIONS]... <T64 filename>

Options:
  -d, --display    This option will only display information about the
                   tape archive.  It will give basic information on
                   the tape as well as listing all the entries, including
                   the empty ones.
  -o, --output     This sets the directory you want to output your file(s)
                   to.  If the directory does not exist, it will attempt
                   to create it.


BUILDING

The source code for this project can be found at https://github.com/hculpan/t64extract (though you probably know that already if you're reading this).  It is distributed under an MIT license, so you are free to use it and modify it (see LICENSE).

To build, execute the following command:
	On Mac/Linux: ./gradlew build
	On Windows:   gradlew build

To build the redistributable:
	On Mac/Linux: ./gradlew distZip
	On Windows:   gradlew distZip

There are some jar dependencies, so you will need a connection to the Internet, but otherwise they should be downloaded for you.