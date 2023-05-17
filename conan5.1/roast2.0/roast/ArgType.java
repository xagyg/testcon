package roast;

/**
* Abstract class for validating strings.
* <p>
* Note: by definition, for integers i and j
* <br>
*	[i,j] = { x | x >= i && x <= j }
*/
abstract class ArgType {

/**
* Determines if s is valid.
*/
abstract boolean isValid(String s);

}
