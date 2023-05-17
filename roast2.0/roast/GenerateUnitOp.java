package roast;

/**
* Constructs a test tuple set and provides an iterator over the set.
* Various classes and methods for automatically generating test tuple
* sets are provides in the <i>Roast</i> package.
*
* @see Roast
* @see CPIterator
* @see BdyIterator
* @see ExtendIterator
*/
public abstract class GenerateUnitOp {

/**
* Determines whether or not there are more tuples in the test tuple set.
*
* @return
*	<tt>true</tt> if more tuple exist in the test tuple set; 
*	<tt>false</tt> otherwise
*/
public abstract boolean hasMoreTuples();

/**
* Returns the next test tuple.
*
* @return
*	the next tuple in the test tuple set
*/
public abstract AbstractTuple nextTuple();

}
