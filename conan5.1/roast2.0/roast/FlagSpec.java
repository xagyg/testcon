package roast;

/**
* A FlagSpec object is a description of a command-line flag.
*/
public class FlagSpec {

/**
* Initialize flag specification.
* <dl>
* <dt>Exceptions<dd>
* if flagName or argType is null or flagName does not contain a leading
* '-' then throw ParameterException
* </dl>
*/
public FlagSpec(String flagName, boolean isRequired,
	boolean argRequired, ArgType argType) throws ParameterException
{
	if (flagName == null || !flagName.startsWith("-")) {
		throw new ParameterException();
	}
	if (argRequired && argType == null) {
		throw new ParameterException();
	}
	this.flagName = flagName;
	this.isRequired = isRequired;
	this.argRequired = argRequired;
	this.argType = argType;
}

/**
* The flag string with leading flag delimiter.
*/
public final String flagName;

/**
* The flag must be present.
*/
public final boolean isRequired;

/**
* The flag must have an argument.
*/
public final boolean argRequired;

/**
* The type of the flag's argument.
*/
public final ArgType argType;

}
