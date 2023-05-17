package roast;

import java.util.*;
import java.io.*;

/**
* The <tt>Roast</tt> class handles command-line parsing, control and 
* communication between four unit operations, logging utility and
* failure messages, and logging test case summary statistics.
*/
public class Roast {

/**
* Called by driver to start unit operations.
*
* <dl>
* <dt>Transitions<dd>
* Let g, f, e, and c be instances of the tester's
* <i>generate</i>, <i>filter</i>, <i>execute</i>, and <i>check</i> 
* unit operations.  The framework pseudocode for control of the
* unit operations is:
* <pre>
*	while g.hasMoreTuples()
*		t = g.nextTuple()
*		if f.isValidTuple(t)
*			r = e.executeTuple(t)
*			c.checkTuple(r)
* </pre>
* Tuples are requested one at a time until the test tuple set has been 
* exhausted.  The framework filters out test tuples deemed undesirable 
* by the tester.  If a tuple is discarded, the framework requests the 
* next tuple.  The framework invokes <tt>executeTuple(t)</tt> with each valid 
* test tuple and passes the result to <tt>checkTuple</tt>.
* </dl>
*
* @see GenerateUnitOp
* @see FilterUnitOp
* @see ExecuteUnitOp
* @see CheckUnitOp
*/
public static void startUnitOps(GenerateUnitOp generate, 
	FilterUnitOp filter, ExecuteUnitOp execute, CheckUnitOp check) 
{
	int tupleNum = 0;
	AbstractTuple tuple;
	while (generate.hasMoreTuples()) {
		tuple = generate.nextTuple();
		tuple.tupleNumber = ++tupleNum;
		if (filter == null || filter.isValidTuple(tuple)) {
			tuple = execute.executeTuple(tuple);
			if (check != null) {
				check.checkTuple(tuple);
			}
		}
	}
}

/**
* Parse command-line arguments. The default flag arguments are all optional:
* <pre>
*	-logfile <file name>
*	-serialize
*	-level <integer value>
* </pre>
* <tt>-serialize</tt> is valid only if <tt>-logfile</tt> is present.  
* If <tt>-serialize</tt> is present, the logfile will be output as 
* serialized message object which can be display by the 
* <i>Roast Log Message Viewer</i>.
* <p>
* The flagSpecs parameter is an array of FlagSpec objects.  This allows
* client-defined flag arguments to be specified and parsed.
*
* @param flagSpec
*	array of client-defined command-line flag specifications; 
*	<tt>null</tt> if only the default flags are allowed
* @param args
*	the array of command-line arguments
* @return
*	a CommandLine object
* @exception ParameterException
*	if a CommandLine object cannot be constructed
* @see CommandLine
* @see FlagSpec
* @see roast.viewer.LogViewer
*/
public static CommandLine parseArgs(FlagSpec[] flagSpecs, String[] args) 
	throws ParameterException
{
	int numFlagSpecs = 0;
	if (flagSpecs != null)
		numFlagSpecs = flagSpecs.length;

	// create FlagSpec array and populate with Roast flag specs
	FlagSpec[] f = new FlagSpec[3 + numFlagSpecs];
	f[0] = new FlagSpec("-logfile",false,true,new AnyArgType(255));
	f[1] = new FlagSpec("-serialize",false,false,null);
	f[2] = new FlagSpec("-level",false,true,new IntegerArgType(10));

	// add user flag specs to FlagSpec array
	for (int i = 3; i < f.length; i++)
		f[i] = flagSpecs[i - 3];

	// create CommandLine object to parse args
	CommandLine c = new CommandLine(f,args);

	String usage = "usage: java <driver> [-logfile <filename> " +
		"[-serialize] ] [-level <int>]";

	if (c.getErrorMessage() != null)
		throw new ParameterException(c.getErrorMessage()+"\n"+usage);

	// -logfile must be present if -serialize is present
	if (c.isArgPresent("-serialize") && !c.isArgPresent("-logfile"))
		throw new ParameterException("-logfile must be present when " +
			"-serialize is present.\n"+usage);

	// set message level
	if (c.isArgPresent("-level")) {
		// PENDING: catch possible runtime exception
		Integer i = new Integer(c.getFlagArg("-level"));
		setMessageLevel(i.intValue());
	}

	// set logFile
	if (c.isArgPresent("-logfile")) {
		try {
			logFile = new LogFile(c.getFlagArg("-logfile"),
				c.isArgPresent("-serialize"));
		} catch (Exception e) {
			//PENDING
		}
	}

	return c;
}

/**
* Write a utility message to the log file if <tt>level</tt> is less than
* or equal to <tt>currentMessageLevel</tt>.  The input parameters are 
* used to create a UtilityMessage object.
*
* @param message
*	the utility message text
* @param level
*	the utility message level
* @see LogFile
*/
public static synchronized void logUtilityMessage(String message,int level)
{
	if (level <= currentMessageLevel) {
		try {
			logFile.writeMessage(
				new UtilityMessage(message,level));
		} catch (IOException e) {
			System.out.println("Error writing to file: " + e +
				".  Swithcing to stdout.");
			logFile = new LogFile();
		}
	}
}

/**
* Write a failure message to the log file.  The input parameters are
* used to create a FailureMessage object.
*
* @param message
*	the failure message text
* @param lineNumber
*	the test script line number of the failure
* @see LogFile
*/
public static synchronized void logFailureMessage(String message,int lineNumber)
{
	try {
		logFile.writeMessage(
			new FailureMessage(message,lineNumber));
	} catch (IOException e) {
		System.out.println("Error writing to file: " + e +
			".  Swithcing to stdout.");
		logFile = new LogFile();
	}
}

/**
* Write summary statistics to the log file.  The number of test cases,
* the number of #valueCheck failures, and the number of #excMonitor 
* failures are written to the log file as level 0 utility messages.
*/
public static void printCounts()
{
	logUtilityMessage("***** Test cases: "+totalCaseCount,0);
	logUtilityMessage("***** Value errors: "+valueErrorCount,0);
	logUtilityMessage("***** Exception errors: "+excErrorCount,0);
}

/**
* Set the current message level.
*
* @see LogFile
*/
public static void setMessageLevel(int level)
{
	currentMessageLevel = level;
}

/**
* Increment the number of exception errors. This method is synchronized
* to ensure thread-safe access whilst incrementing the count.
*
*/
public static synchronized void incExcErrorCount() {
    excErrorCount++;
}

/**
* Increment the number of value errors. This method is synchronized
* to ensure thread-safe access whilst incrementing the count.
*
*/
public static synchronized void incValueErrorCount() {
    valueErrorCount++;
} 
                    
/**
* Increment the number of total test cases. This method is synchronized
* to ensure thread-safe access whilst incrementing the count.
*
*/
public static synchronized void incTotalCaseCount() {
    totalCaseCount++;
}

private static LogFile logFile = new LogFile();
private static int currentMessageLevel = Integer.MAX_VALUE;

/**
* The number of test cases.
*/
public static int totalCaseCount = 0;

/**
* The number of #valueCheck failures.
*/
public static int valueErrorCount = 0;

/**
* The number of #excMonitor failures.
*/
public static int excErrorCount = 0;

}
