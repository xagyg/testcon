import roast.*;
import java.util.*;

public class Templates {

public static void main(String[] args)
{
	String s;

	// ***** value-checking templates
	s = new String("abc");

	// builtin types

{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 15.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!ValueType.compareValue(s.length() ,3 )) {
			ValueType.printValue(15,s.length() ,3 );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(15,RoastActExc,null);
}
} // ***** test case end
// case V1: pass

{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 17.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!ValueType.compareValue(s.length() ,0 )) {
			ValueType.printValue(17,s.length() ,0 );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(17,RoastActExc,null);
}
} // ***** test case end
// case V2: fail

{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 19.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!ValueType.compareValue(s ,"abc" )) {
			ValueType.printValue(19,s ,"abc" );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(19,RoastActExc,null);
}
} // ***** test case end
// case V3: pass

	// custom type: case-insensitive string comparison

{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 23.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!ValueType.compareValue(s ,"abc" )) {
			ValueType.printValue(23,s ,"abc" );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(23,RoastActExc,null);
}
} // ***** test case end
// case V4: pass
{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 24.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!CIString.compareValue(s ,"abc" )) {
			CIString.printValue(24,s ,"abc" );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(24,RoastActExc,null);
}
} // ***** test case end
// case V5: pass

{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 26.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!ValueType.compareValue(s ,"aBc" )) {
			ValueType.printValue(26,s ,"aBc" );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(26,RoastActExc,null);
}
} // ***** test case end
// case V6: fail
{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 27.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!CIString.compareValue(s ,"aBc" )) {
			CIString.printValue(27,s ,"aBc" );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(27,RoastActExc,null);
}
} // ***** test case end
// case V7: pass

	// exception during comparison
{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 30.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!ValueType.compareValue(s.charAt(-1) ,'a' )) {
			ValueType.printValue(30,s.charAt(-1) ,'a' );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(30,RoastActExc,null);
}
} // ***** test case end
// case V8: fail

	// ***** exception-monitoring templates

	// default comparison
	char c;

{ // ***** Test case start *******************************
  // ***** Case type: #excMonitor. Script line number 37.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
c = s.charAt(0); 
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(37,RoastActExc,null);
}
} // ***** test case end
// case E1: pass
{ // ***** Test case start *******************************
  // ***** Case type: #excMonitor. Script line number 38.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
c = s.charAt(-1); 
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(38,RoastActExc,null);
}
} // ***** test case end
// case E2: fail

{ // ***** Test case start *******************************
  // ***** Case type: #excMonitor. Script line number 40.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
c = s.charAt(0); 
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,		new StringIndexOutOfBoundsException() )) {
	ExceptionType.printExc(40,RoastActExc,		new StringIndexOutOfBoundsException() );
}
} // ***** test case end
// case E3: fail
{ // ***** Test case start *******************************
  // ***** Case type: #excMonitor. Script line number 42.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
c = s.charAt(-1); 
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,		new StringIndexOutOfBoundsException() )) {
	ExceptionType.printExc(42,RoastActExc,		new StringIndexOutOfBoundsException() );
}
} // ***** test case end
// case E4: pass

	// custom comparison

{ // ***** Test case start *******************************
  // ***** Case type: #excMonitor. Script line number 47.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
throw new E(1); 
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,new E(2) )) {
	ExceptionType.printExc(47,RoastActExc,new E(2) );
}
} // ***** test case end
// E5: pass

{ // ***** Test case start *******************************
  // ***** Case type: #excMonitor. Script line number 49.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
throw new E(1); 
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!MyExcCompare .compareExc(RoastActExc,new E(2) )) {
	MyExcCompare .printExc(49,RoastActExc,new E(2) );
}
} // ***** test case end
// E6: fail

	Roast.printCounts();
}

}

// ***** case-insensitive string for testing custom value comparison
class CIString extends ValueType {

static public boolean compareValue(String actVal,String expVal)
{ return actVal.equalsIgnoreCase(expVal); }

static public void printValue(int lineNumber,String actVal,String expVal)
{ ValueType.printValue(lineNumber,actVal,expVal); }

}

// ***** classes for testing custom exception comparison

class E extends Exception {
	public E(int x) { this.x = x; }
	public int x;
}

class MyExcCompare {

static public boolean compareExc(Object actExc,E expExc)
{
	if (!(actExc instanceof E)) {
		return false;
	} else if (expExc == null) {
		return actExc == null;
	} else {
		return expExc.x == ((E)actExc).x;
	}
}

static public void printExc(int lineNumber,Object actExc,E expExc)
{
	String expExc0,actExc0;

	// test here: actExc instanceOf E; else print class name
	actExc0 = actExc == null ? "<none>" : "E(" + ((E)actExc).x + ")";
	expExc0 = expExc == null ? "<none>" : "E(" + expExc.x + ")";

	Roast.logFailureMessage("Exception error at line number: " +
		lineNumber +
		".  Actual exception: "+actExc0 +
		"  Expected exception: "+expExc0,
		lineNumber);
	Roast.excErrorCount++;
}

}
