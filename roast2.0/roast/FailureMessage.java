package roast;

/**
* A failure log message.  Contains a message string, a message level,
* and the test script line number of the failure.
*/
public class FailureMessage implements LogMessage {

/**
* Constructs a FailureMessage object with the specified message and level.
*
* @param message
*       the failure message text
* @param level
*       the failure message level
*/
public FailureMessage(String message, int lineNumber)
{
	this.message = message;
	this.lineNumber = lineNumber;
}

/**
* Returns a string representation of the failure message.  The string
* includes the failure message text, message level, and line number
* formatted in a single line.
*
* @return
*       a string representation of the information in the failure
*       message object
*/
public String toString()
{
	return message;
}

/**
* Returns the failure message text as a String.
*
* @return
*       the failure message text
*/
public String getMessage()
{
        return message;
}

/**
* Returns the failure message level.
*
* @return
*       the failure message level
*/
public int getLevel()
{
        return 0;
}

/**
* Returns the test script line number of the failure.
*
* @return
*       the test script line number of the failure
*/
public int getLineNumber()
{
        return lineNumber;
}

private String message;
private int lineNumber;

}
