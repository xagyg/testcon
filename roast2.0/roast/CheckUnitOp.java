package roast;

/**
* Checks that a test case executed as expected.
*
* @see Roast
*/
public abstract class CheckUnitOp {

/**
* Calls CUT methods to verify that the test case, represented by
* <tt>tuple</tt>, executed successfully.
*
* @param tuple
*	an abstract representation of the test case to be checked
*/
public abstract void checkTuple(AbstractTuple tuple);

}
