package roast;

import java.util.*;
import java.text.ParseException;

/**
* A <tt>CommandLine</tt> object parses an array of Unix-style
* command-line arguments according to a user-supplied specification.
* <p>
* define terms: flag, flag argument, suffix argument
* <p>
* State variables<br>
* errorMessage: String<br>
* flagArgs: set of (flag: String, arg: String)<br>
* suffixArgs: array of String
* <p>
*/
public class CommandLine {

/**
* Parses an array of command line flags and arguments.
* <dl>
* <dt>Precondition<dd>
* ArgType does not throw an exception
* <dt>Exceptions<dd>
* if flagSpec or args contains a null entry <br>
* or args has an entry that is ""<br> 
* or flagSpec has two entries with the same flag name then <br>
* throw ParameterException
* <dt>Transitions<dd>
* <pre>
* i = 0
* errorMessage = ""
* while i < 0..args.length && errorMessage == ""
* 	if args[i] has a leading "-" // flags
* 		if flagSpec has an entry F such that F.name == args[i] 
* 			if flagArgs has an entry with name args[i]
* 				errorMessage = DUPLICATEFLAG+args[i]
* 			else if F.argRequired
* 				if i == args.length-1
* 					errorMessage = MISSINGFLAGARG+args[i]
* 				else if !F.argType.isValid(args[i+1])
* 					errorMessage = INVALIDFLAGARG+args[i+1]
* 				else
* 					add (args[i],args[i+1]) to flagArgs
* 					i = i+2
* 			else
* 				add (args[i],null) to flagArgs
* 				i = i+1
* 		else
* 			errorMessage = INVALIDFLAG+args[i]
* 	else // begin suffix arguments
* 		if there is a required flag F in flagSpec not in flagArgs
* 			errorMessage = REQUIREDFLAGMISSING+F.name
* 		else
* 			copy args[i..args.length-1] to suffixArgs
* </pre>
* </dl>
*/
public CommandLine(FlagSpec[] flagSpec, String[] args) 
	throws ParameterException
{
	if (flagSpec == null || args == null) {
		throw new ParameterException("null CommandLine parameter");
	}

	argsMap = new HashMap(args.length);
	HashMap flagSpecMap = new HashMap(flagSpec.length);

	for (int i = 0; i < flagSpec.length; i++) {
		if (flagSpec[i] == null) {
			throw new ParameterException("null FlagSpec element");
		}

		// put required flags into argsMap with null values
		if (flagSpec[i].isRequired) {
			argsMap.put(flagSpec[i].flagName,null);
		}

		// put flagSpec array elements into a map
		// throw exception if there are duplicate flagSpecs 
		if (flagSpecMap.put(flagSpec[i].flagName,flagSpec[i]) != null) {
			throw new ParameterException("Duplicate FlagSpecs");
		}
	}

	// Put args array elements in a Vector
	Vector argsVector = new Vector();
	for (int i = 0; i < args.length; i++) {
		if (args[i] == null || args[i].equals("")) {
			throw new ParameterException("null args element");
		}
		argsVector.addElement(args[i]);
	}

	try {
		while (parseNextFlag(flagSpecMap,argsVector)) {
			;
		}
		if (argsVector.size() > 0) { //args contains suffix args
			//copy remainder of args to suffixArgs
			suffixArgs = new String[argsVector.size()];
			for (int i = 0; i < suffixArgs.length; i++) {
				suffixArgs[i] = (String)argsVector.remove(0);
			}
		}

		// Null values in argsMap after parsing indicates missing
		// required flag. 
		if (argsMap.containsValue(null)) {
			Iterator argsIter = argsMap.keySet().iterator();
			String exceptionString = REQUIREDFLAGMISSING;
			while (argsIter.hasNext()) {
				String f = (String)argsIter.next();
				if (argsMap.get(f) == null) {
					exceptionString += f;
					break;
				}
			}
			throw new ParseException(exceptionString,0);
		}
	} catch (ParseException p) {
		errorMessage = p.getMessage();
	}
}

/**
* Gets the error message if the command line was not successfully
* parsed.
* <dl>
* <dt>Output<dd>
* errorMessage or null if no error occurred
* </dl>
*/
public String getErrorMessage() 
{
	return errorMessage;
}

/**
* Determines whether the named flag is present in args.
* <dl>
* <dt>Output<dd>
* exists (flagName,x) in flagArgs, where x is any value (may be null)
* </dl>
*/
public boolean isArgPresent(String flagName) 
{
	if (errorMessage != null) {
		return false;
	}
	return argsMap.containsKey(flagName);
}

/**
* Gets value of named flag.
* <dl>
* <dt>Output<dd>
* arg such that (flagName, arg) is in flagArgs<br>
* null if an error occurred during parsing
* </dl>
*/
public String getFlagArg(String flagName) 
{
	if (errorMessage != null) {
		return null;
	}
	return (String)argsMap.get(flagName);
}

/**
* Gets suffix arguments arguments present in args.
* <dl>
* <dt>Output<dd>
* suffixArgs, may contain zero elements<br>
* null if an error occurred during parsing
* </dl>
*/
public String[] getSuffixArgs()
{
	if (errorMessage != null) {
		return null;
	}
	return suffixArgs;
}

/*
* <dl>
* <dt>Transition<dd>
* if another flag remains, place it in argsMap
* <dt>Output<dd>
* return true if end of flag args reached
* <dt>Exceptions<dd>
* throw ParseException if an invalid flag is found
* </dl>
*/
private boolean parseNextFlag(HashMap flagSpecMap, Vector args) 
	throws ParseException
{
	if (args.size() == 0) {
		return false;
	}
	String nextArg = (String)args.elementAt(0);
	FlagSpec fspec = (FlagSpec)flagSpecMap.get(nextArg);
	if (fspec != null) { //curArg is a flag
		String curArg = (String)args.remove(0);
		String value;
		if (fspec.argRequired) {
			if (args.size() > 0) { //args has another element
				curArg = (String)args.remove(0);
				if (fspec.argType.isValid(curArg)) {
					value = curArg;
				} else {
					throw new ParseException(INVALIDFLAGARG+
						fspec.flagName+", "+curArg,0);
				}
			} else {
				throw new ParseException(MISSINGFLAGARG+
					fspec.flagName,0);
			}
		} else {
			value = "";
		}
		if (argsMap.get(fspec.flagName) == null) {
			argsMap.put(fspec.flagName,value);
		} else {
			throw new ParseException(DUPLICATEFLAG+fspec.flagName,0);
		}
		return true;
	} else if (nextArg.startsWith("-")) { 
		throw new ParseException(INVALIDFLAG+nextArg,0);
	} else {
		return false;
	}
}

// store flag/arg pairs found in args
// use "" for flags without an arg
private HashMap argsMap;

private String[] suffixArgs = new String[0];
private String errorMessage = null;

public static final String DUPLICATEFLAG = "Duplicate flag: ";
public static final String MISSINGFLAGARG = "Missing argument for flag: ";
public static final String INVALIDFLAGARG = "Invalid flag argument: ";
public static final String INVALIDFLAG = "Invalid flag: ";
public static final String REQUIREDFLAGMISSING = "Missing required flag: ";

}
