package roast;

/**
* <b>Overview</b>
* <dl>
* Comparison and printing functions.
* </dl>
* <p>
* <b>Interface Semantics</b>
* <dl>
* <dt>state variables<dd>
*    none
* <dt>state invariant<dd>
*    none
* <dt>assumptions<dd>
*    none
* </dl>
*/
public class ValueType {

/**
* Returns true if the two specified objects are <i>equal</i> to one another,
* that is, they represent the same object.
* <p>
* @return
*    <code>true</code> if the objects are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(Object actVal, Object expVal)
{
	if (expVal == null) {
		return actVal == null;
	} else {
		return expVal.equals(actVal);
	}
}

static public synchronized void compareAndPrintValue(int lineNumber, Object actVal, Object expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}

static public void printValue(int lineNumber, Object actVal, Object expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}

/**
* Returns true if the two specified strings are <i>equal</i> to one another,
* that is, they represent the same sequence of characters.
* <p>
* @return
*    <code>true</code> if the strings are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(String actVal, String expVal)
{
	if (expVal == null) {
		return actVal == null;
	} else {
		return expVal.equals(actVal);
	}
}

static public synchronized void compareAndPrintValue(int lineNumber, String actVal, String expVal)
{
    if (!compareValue(actVal, expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}


static public void printValue(int lineNumber, String actVal, String expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}

/**
* Returns true if the two specified booleans are <i>equal</i> to one another.
* <p>
* @return
*    <code>true</code> if the booleans are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(boolean actVal, boolean expVal)
{
	return actVal == expVal;
}

static public synchronized void compareAndPrintValue(int lineNumber, boolean actVal, boolean expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}

static public void printValue(int lineNumber, boolean actVal, boolean expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}

/**
* Returns true if the two specified bytes are <i>equal</i> to one another.
* <p>
* @return
*    <code>true</code> if the bytes are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(byte actVal, byte expVal)
{
	return actVal == expVal;
}

static public synchronized void compareAndPrintValue(int lineNumber, byte actVal, byte expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}

static public void printValue(int lineNumber, byte actVal, byte expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}

/**
* Returns true if the two specified characters are <i>equal</i> to one another.
* <p>
* @return
*    <code>true</code> if the characters are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(char actVal, char expVal)
{
	return actVal == expVal;
}

static public synchronized void compareAndPrintValue(int lineNumber, char actVal, char expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}

static public void printValue(int lineNumber, char actVal, char expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}

/**
* Returns true if the two specified doubles are <i>equal</i> to one another.
* Two doubles d1 and d2 are considered equal if:
* <pre>
*                               new Double(d1).equals(new Double(d2))
* </pre>
* Unlike the == operator, this method considers NaN equal to itself,
* and 0.0d unequal to -0.0d.
* <p>
* @return
*    <code>true</code> if the doubles are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(double actVal, double expVal)
{
	return actVal == expVal;
}

static public synchronized void compareAndPrintValue(int lineNumber, double actVal, double expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}

static public void printValue(int lineNumber, double actVal, double expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}

/**
* Returns true if the two specified floats are <i>equal</i> to one another.
* Two floats f1 and f2 are considered equal if:
* <pre>
*                               new Float(d1).equals(new Float(d2))
* </pre>
* Unlike the == operator, this method considers NaN equal to itself,
* and 0.0f unequal to -0.0f.
* <p>
* @return
*    <code>true</code> if the floats are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(float actVal, float expVal)
{
	return actVal == expVal;
}

static public synchronized void compareAndPrintValue(int lineNumber, float actVal, float expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}

static public void printValue(int lineNumber, float actVal, float expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}


/**
* Returns true if the two specified ints are <i>equal</i> to one another.
* <p>
* @return
*    <code>true</code> if the ints are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(int actVal, int expVal)
{
	return actVal == expVal;
}

static public synchronized void compareAndPrintValue(int lineNumber, int actVal, int expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}


static public void printValue(int lineNumber, int actVal, int expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}

/**
* Returns true if the two specified longs are <i>equal</i> to one another.
* <p>
* @return
*    <code>true</code> if the longs are equal<br>
*    <code>false</code> otherwise
*/
static public boolean compareValue(long actVal, long expVal)
{
	return actVal == expVal;
}

static public synchronized void compareAndPrintValue(int lineNumber, long actVal, long expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}

static public void printValue(int lineNumber, long actVal, long expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}

static public boolean compareValue(short actVal, short expVal)
{
	return actVal == expVal;
}

static public synchronized void compareAndPrintValue(int lineNumber, short actVal, short expVal)
{
	if (!compareValue(actVal,expVal)) {
	    printValue (lineNumber, actVal, expVal);
	}
}

/**
* Returns true if the two specified shorts are <i>equal</i> to one another.
* <p>
* @return
*    <code>true</code> if the shorts are equal<br>
*    <code>false</code> otherwise
*/
static public void printValue(int lineNumber, short actVal, short expVal)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+expVal,
		lineNumber);
	Roast.incValueErrorCount();
}


}
