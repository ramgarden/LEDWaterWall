setlocal
set PATH=%PATH%;C:\arduino-1.0\
javac -cp C:\arduino-1.0\lib\RXTXcomm.jar;. MIDIPlayer.java
java -cp C:\arduino-1.0\lib\RXTXcomm.jar;. MIDIPlayer

REM javac -cp C:\arduino-1.0\lib\RXTXcomm.jar SerialTest.java
REM java -cp C:\arduino-1.0\lib\RXTXcomm.jar;. SerialTest
 