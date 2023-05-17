package roast;

/**
* An AlphabeticArgType determines whether a given string contains only
* alphabetic characters and has a length in a given range.
* <dl>
* <dt>State variables<dd>
* maxLength: maximum possible length of string
*/ 
public class AlphabeticArgType extends ArgType {

/**
* Creates a new instance.
* <dl>
* <dt>Transitions<dd>
* maxLength = maxLength0
* </dl>
*/
public AlphabeticArgType(int maxLength0)
{
	maxLength = maxLength0;
}

/**
* Determines whether s is a valid alphabetic string with a valid length.
* <dl>
* <dt>Output<dd>
* s is a string of alphabetic characters && |s| in [1..maxLength]
* </dl>
*/
public boolean isValid(String s)
{
	int n = s.length();

	if (n < 1 || n > maxLength) {
		return false;
	} else {
		for (int i = 0; i < n; i++) {
			if (!Character.isLetter(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}

int maxLength;

}

