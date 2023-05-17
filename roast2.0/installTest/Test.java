import roast.*;

// ********** Driver **********

public class Test {

public static void main(String[] args)
{
	try {
		Roast.parseArgs(null,args);
	} catch (ParameterException pe) {
		Roast.logUtilityMessage("Test failed: no exception expected",0);
	}

	int x = 0;
	Roast.logUtilityMessage("Level 0 message: pass",0);
	Roast.logUtilityMessage("Level 1 message: pass",1);
	Roast.logUtilityMessage("Level 2 message: pass",2);
	Roast.logUtilityMessage("Level 3 message: pass",3);
	Roast.logUtilityMessage("Level 4 message: pass",4);
	Roast.logUtilityMessage("Level 5 message: fail",5);
{ // ***** Test case start *******************************
  // ***** Case type: #valueCheck. Script line number 22.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
		if (!ValueType.compareValue(x ,0 )) {
			ValueType.printValue(22,x ,0 );
		}
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(22,RoastActExc,null);
}
} // ***** test case end
{ // ***** Test case start *******************************
  // ***** Case type: #excMonitor. Script line number 23.
  // *****************************************************
Roast.totalCaseCount++;
Object RoastActExc = null;
try {
x = 1; 
} catch (Throwable RoastExc) {
	RoastActExc = RoastExc;
}
if (!ExceptionType.compareExc(RoastActExc,null)) {
	ExceptionType.printExc(23,RoastActExc,null);
}
} // ***** test case end

	Roast.printCounts();
}

}
