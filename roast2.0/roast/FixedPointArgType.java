package roast;

/**
* A FixedPointArgType determines whether a given string contains only
* digits and one decimal (optional).  The number of digits before and 
* after the decimal must be in given ranges.
* <dl>
* <dt>State variables<dd>
* leftMaxLength: maximum possible number of digits before the decimal
* rightMaxLength: maximum possible number of digits after the decimal
*/
public class FixedPointArgType extends ArgType {

/**
* Creates a new instance.
* <dl>
* <dt>Transitions<dd>
* leftMaxLength = leftMaxLength0
* rightMaxLength = rightMaxLength0
* </dl>
*/
public FixedPointArgType(int leftMaxLength0,int rightMaxLength0)
{
	leftMaxLength = leftMaxLength0;
	rightMaxLength = rightMaxLength0;
}

/**
* Determines whether s contains only digits and one decimal (optional) and 
* that the number of digits before and after the decimal are in range.
* <dl>
* <dt>Output<dd>
* Let <br>
*   i = index of decimal in s || if no decimal i = s.length<br>
*   s1 = s[0..i-1]<br>
*   s2 = s[i+1..|s|] || if no decimal s = ""<br>
* <br>
* i != s.length-1 &&
* s1 is a string of numeric characters && |s1| in [1..leftMaxLength] &&
* s2 is a string of numeric characters && |s2| in [0..rightMaxLength]
* </dl>
*/
public boolean isValid(String s)
{
	int n = s.length();
	int dotPos = s.indexOf('.');

	if (dotPos == -1) { //no decimal point
		if (n < 1 || n > leftMaxLength) {
			return false;
		} else {
			for (int i = 0; i < n; i++) {
				if (!Character.isDigit(s.charAt(i))) {
					return false;
				}
			}
			return true;
		}
	} else { //decimal point present
		if (
			dotPos < 1 || dotPos > leftMaxLength
			||
			n-dotPos == 1 || n-dotPos-1 > rightMaxLength
		) {
			return false;
		} else {
			for (int i = 0; i < dotPos; i++) {
				if (!Character.isDigit(s.charAt(i))) {
					return false;
				}
			}
			for (int i = dotPos+1; i < n; i++) {
				if (!Character.isDigit(s.charAt(i))) {
					return false;
				}
			}
			return true;
		}
	}
}

int leftMaxLength;
int rightMaxLength;

}

