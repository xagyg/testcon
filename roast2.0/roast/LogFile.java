package roast;

import java.io.*;

/**
* The <tt>LogFile</tt> class controls output to the log file.
*/
public class LogFile {

/**
* Constructs a <tt>LogFile</tt> object which writes output to
* <tt>System.out</tt> in text format.
*/
public LogFile()
{
	try {
		setOutputFile(null,false);
	} catch (ParameterException p) {
	} catch (IOException i) {
	}
}

/**
* Constructs a <tt>LogFile</tt> object with the specified file
* <tt>fileName</tt> and output mode.  If <tt>fileName</tt> is
* <tt>null</tt>, output is written to <tt>System.out</tt>.
* If <tt>serializeOutput</tt> is true then any messages written
* to the log file are serialized message objects; otherwise,
* messages are written as plain text.
*
* @param fileName
*	the file name of the log file;
*	<tt>null</tt> if output should be written to <tt>System.out</tt>
* @param serializeOutput
*	<tt>true</tt> writes serialized message objects to the log file;
*	<tt>false</tt> writes message text to the log file
* @exception ParameterException
*	if <tt>fileName</tt> is <tt>null</tt> and 
*	serializeOutput is <tt>true</tt>
*/
public LogFile(String fileName, boolean serializeOutput) 
	throws ParameterException, IOException
{
	setOutputFile(fileName,serializeOutput);
}

private void setOutputFile(String fileName, boolean serializeOutput) 
	throws ParameterException, IOException
{
	if (serializeOutput) {
		// illegal: fileName == null && serializeOutput
		if (fileName == null) {
			throw new ParameterException();
		} else {
			serializeOut = new ObjectOutputStream(
					new FileOutputStream(fileName.trim()));
		}
	} else {
		if (fileName == null) {
			textOut = new PrintStream(System.out);
		} else {
			textOut = new PrintStream(
					new FileOutputStream(fileName.trim()));
		}
	}
	this.fileName = fileName;
	this.serializeOutput = serializeOutput;
}

/**
* Write a utility message to the log file.
*
* @param uMessage
*	the message to be written to the log file
* @exception IOException
*	if an error occurs writing to the log file
*/
public void writeMessage(UtilityMessage uMessage) throws IOException
{
	if (serializeOutput) {
		serializeOut.writeObject(uMessage);
	} else {
		for (int i = 0; i < uMessage.getLevel(); i++) {
			textOut.print("\t");
		}
		textOut.println(uMessage);
	}
}

/**
* Write a failure message to the log file.
*
* @param fMessage
*	the message to be written to the log file
* @exception IOException
*	if an error occurs writing to the log file
*/
public void writeMessage(FailureMessage fMessage) throws IOException
{
	if (serializeOutput) {
		serializeOut.writeObject(fMessage);
	} else {
		textOut.println(fMessage);
	}
}

/**
* Closes log file stream.
*/
public void finalize()
{
	if (textOut != null) {
		textOut.close();
	}
	try {
		if (serializeOut != null) {
			serializeOut.close();
		}
	} catch (IOException e) {}
}

/**
* Converts a serialized log file to a plain text log file.  If
* <tt>output</tt> is <tt>null</tt>, the output is written 
* to <tt>System.out</tt>.
*
* @param input
*	the name of the input file
* @param output
*	the name of the output file; <tt>null</tt> indicates that
*	output will be written to <tt>System.out</tt>
* @return
* 	<tt>true</tt> if conversion is successful;
*	<tt>false</tt> otherwise
*/
public static boolean convertSerializeToText(String input, String output)
{
	try {
		ObjectInputStream serializeIn = new ObjectInputStream(
			new FileInputStream(new File(input)));
		LogFile logFile = new LogFile(output,false);
		LogMessage m;
		try {
			while (true) {
				m = (LogMessage)serializeIn.readObject();
				if (m instanceof FailureMessage) {
					logFile.writeMessage(
						(FailureMessage)m);
				} else if (m instanceof UtilityMessage) {
					logFile.writeMessage(
						(UtilityMessage)m);
				}
			}
		} catch (EOFException eof) {}
		serializeIn.close();
		return true;
	} catch (Exception e) {
		return false;
	}
}

public static void main(String[] args)
{
	String usage = "usage: java roast.LogFile <serialized log file>";
	if (args == null) {
		System.out.println(usage);
		System.exit(1);
	}
	if (! convertSerializeToText(args[0], null)) {
		System.out.println("Error reading " + args[0] + ".");
	}
}

private String fileName = null; //stdout
private boolean serializeOutput = false;
private PrintStream textOut;
private ObjectOutputStream serializeOut;

}
