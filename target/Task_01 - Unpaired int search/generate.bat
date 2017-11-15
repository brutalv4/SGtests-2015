@echo off
%JAVA_HOME%\bin\java -jar ./unpairedIntSearcher.jar
@echo on
%JAVA_HOME%\bin\java -jar ./unpairedIntSearcher.jar --mode gen --file ./arrayfile.yml --length 15 --range 50
pause