package roast;

/**
* An abstract representation of a test case passed between <i>Roast</i>
* unit operations.  Each tuple element is taken from a domain: a set of 
* elements, often ordered, of the same type.  The tester extends 
* AbstractTuple to add the necessary tuple elements.
*
* @see Roast
*/
public abstract class AbstractTuple {

/**
* Stores the unique tuple number automatically assigned to each tuple.
*/
public long tupleNumber;
}
