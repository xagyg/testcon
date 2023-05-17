package roast;

/**
* Executes a test case represented by <tt>tuple</tt>.  Tuple elements
* are first used to construct a test case, which is then executed on
* the CUT.
*
* @see Roast
*/
public abstract class ExecuteUnitOp {

/**
* Executes the test case represented by <tt>tuple</tt>.
* The returned tuple may be the input tuple unchanged, the input
* tuple with updated elements, or a new tuple altogether.
*
* @param tuple
*	an abstract representation of the test case to be executed
* @return
*	the input tuple unchanged, the input tuple with updated elements, 
*	or a new tuple altogether
*/
public abstract AbstractTuple executeTuple(AbstractTuple tuple);

}
