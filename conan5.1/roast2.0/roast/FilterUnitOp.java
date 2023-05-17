package roast;

/**
* Removes tuples from the test tuple set.  The test tuple set created
* by the <i>generate</i> unit operation often contains a very large
* number of tuples.  Many of these may be invalid or undesired and 
* need to be filtered out.
*
* @see Roast
*/
public abstract class FilterUnitOp {

/**
* Determines whether or not the input <tt>tuple</tt> is valid.  
* Invalid tuples are discarded.
*
* @param tuple
*	a tuple from the test tuple set
* @return
*	<tt>true</tt> if the tuple is valid; <tt>false</tt> otherwise
*/
public abstract boolean isValidTuple(AbstractTuple tuple);

}
