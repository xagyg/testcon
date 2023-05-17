package roast;

/**
* A utility log message.  Contains a message string and a message level.
*/
public class UtilityMessage implements LogMessage {

/**
* Constructs a UtilityMessage object with the specified message and level.
*
* @param message
*	the utility message text
* @param level
*	the utility message level
*/
public UtilityMessage(String message, int level)
{
	this.message = message;
	this.level = level;
}

/**
* Returns a string representation of the utility message.  The string
* includes the utility message text and message level formatted
* in a single line.
* 
* @return
*       a string representation of the information in the utility 
*	message object
*/
public String toString()
{
	return message;
}

/**
* Returns the utility message text as a String.
*
* @return
*       the utility message text
*/
public String getMessage()
{
	return message;
}

/**
* Returns the utility message level.
*
* @return
*       the utility message level
*/
public int getLevel()
{
	return level;
}

private String message;
private int level;

}
