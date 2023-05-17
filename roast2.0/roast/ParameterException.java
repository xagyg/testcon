package roast;

/**
* Signals that an improper parameter was passed to the <tt>CommandLine</tt>
* constructor.
*
* @see CommandLine
*/
public class ParameterException extends Exception {

/**
* Constructs a ParameterException with no detail message.
* A detail message is a String that describes this particular exception.
*/
public ParameterException()
{
	super();
}

/**
* Constructs a ParameterException with the specified detail message.
* A detail message is a String that describes this particular exception.
*
* @param message 
*	the detail message
*/
public ParameterException(String message)
{
	super(message);
}

}
