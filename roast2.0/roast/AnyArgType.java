package roast;

/**
* An AnyArgType determines whether a given string has a length in 
* a given range.
* <dl>
* <dt>State variables<dd>
* maxLength: maximum possible length of string
*/
public class AnyArgType extends ArgType {

/**
* Creates a new instance.
* <dl>
* <dt>Transitions<dd>
* maxLength = maxLength0
* </dl>
*/
public AnyArgType(int maxLength0)
{
	maxLength = maxLength0;
}

/**
* Determines whether s has a valid length.
* <dl>
* <dt>Output<dd>
* |s| in [1..maxLength]
* </dl>
*/
public boolean isValid(String s)
{
	int n = s.length();

	if (n < 1 || n > maxLength) {
		return false;
	} else {
		return true;
	}
}

int maxLength;

}

