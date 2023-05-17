package roast;

public class ExceptionType {

static public boolean compareExc(Object actExc,Object expExc)
{
	if (expExc == null) {
		return actExc == null;
	} else {
		return actExc != null && expExc.getClass() == actExc.getClass();
	}
}

static public void printExc(int lineNumber,Object actExc,Object expExc)
{
	String expExc0,actExc0;

	actExc0 = actExc == null ? "<none>" : actExc.getClass().getName();
	expExc0 = expExc == null ? "<none>" : expExc.getClass().getName();

	Roast.logFailureMessage("Exception error at line number: " +
		lineNumber +
		".  Actual exception: "+actExc0 +
		"  Expected exception: "+expExc0,
		lineNumber);
	Roast.incExcErrorCount();
}

}
