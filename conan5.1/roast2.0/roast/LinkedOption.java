package roast;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
* <b>Overview</b>
* <dl>
* Comparison and printing functions for options.
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
public class LinkedOption {

    private static ThreadLocal option = new ThreadLocal();
    
    private static ThreadLocal map = new ThreadLocal();
    private static ThreadLocal reversedMap = new ThreadLocal();
    
    private static String selected = "";
 
    public LinkedOption() {
    }

    static public synchronized void setSelected(String s) {
        selected = s;
    }
    
    static public synchronized boolean compareSelected(String s) {
        
        if (selected.equals("")) selected = s;
     
        return selected.equals(s);   
    }
    
    static public synchronized void clear() {
        map.set(new HashMap());
        reversedMap.set(new HashMap());
    }

    static public synchronized void reset() {
        clear();
        option.set(null);
    }
    
    static public synchronized void add(char c, String s) {
        Map m = (Map)map.get();
        Map r = (Map)reversedMap.get();
        m.put(s, new Character(c));
        r.put(new Character(c), s);
    }

    static public synchronized void add(int i, String s) {     
        Map m = (Map)map.get();
        Map r = (Map)reversedMap.get();
        m.put(s, new Integer(i));
        r.put(new Integer(i), s);
    }

    static public synchronized void add(byte c, String s) {
        Map m = (Map)map.get();
        Map r = (Map)reversedMap.get();        
        m.put(s, new Byte(c));
        r.put(new Byte(c), s);
    }

    static public synchronized void add(long i, String s) {
        Map m = (Map)map.get();
        Map r = (Map)reversedMap.get();        
        m.put(s, new Long(i));
        r.put(new Long(i), s);
    }
    
    static public synchronized void add(short c, String s) {
        Map m = (Map)map.get();
        Map r = (Map)reversedMap.get();        
        m.put(s, new Short(c));
        r.put(new Short(c), s);
    }

    static public synchronized void add(double i, String s) {
        Map m = (Map)map.get();
        Map r = (Map)reversedMap.get();        
        m.put(s, new Double(i));
        r.put(new Double(i), s);
    }
    
    static public synchronized void add(float i, String s) {
        Map m = (Map)map.get();
        Map r = (Map)reversedMap.get();        
        m.put(s, new Float(i));
        r.put(new Float(i), s);
    }


static public synchronized void compareAndPrintValue(int lineNumber, String actVal)
{                                                 
    Map m = (Map)map.get();
    Map r = (Map)reversedMap.get();
    String id = (String)r.get(actVal);
    
    String opt = (String)option.get();

    if (id == null) { 
        if (opt==null) printValue(lineNumber, actVal, m);
        else printValue(lineNumber, actVal, opt, m);
        return; 
    }
    
    if (opt==null) { 
        if (!selected.equals("") && !selected.equals(id)) {
	        Roast.logFailureMessage("Value error at line number: " +
		        lineNumber +
		        ".  Actual value: " +actVal +
		        " matched trace option: "+id +
		        "  Expected trace option: "+selected,
		        lineNumber);
	        Roast.incValueErrorCount();
	        return;
	    }
        option.set(id);
        selected = id;
        return; 
    }
    
    if (!id.equals(opt)) { printValue(lineNumber, actVal, opt, m); return; }
}

static public void printValue(int lineNumber, String actVal, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+ map.toString(),
		lineNumber);
	Roast.incValueErrorCount();
}

static public void printValue(int lineNumber, String actVal, String id, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+(String)map.get(id),
		lineNumber);
	Roast.incValueErrorCount();
}


static public synchronized void compareAndPrintValue(int lineNumber, byte actVal)
{                                                                               
    Map m = (Map)map.get();
    Map r = (Map)reversedMap.get();
    
    String opt = (String)option.get();
    String id = (String)r.get(new Byte(actVal));

    if (id == null) { 
        if (opt==null) printValue(lineNumber, actVal, m);
        else printValue(lineNumber, actVal, opt, m);
        return; 
    }
    
    if (opt==null) { 
        if (!selected.equals("") && !selected.equals(id)) {
	        Roast.logFailureMessage("Value error at line number: " +
		        lineNumber +
		        ".  Actual value: " +actVal +
		        " matched trace option: "+id +
		        "  Expected trace option: "+selected,
		        lineNumber);
	        Roast.incValueErrorCount();
	        return;
	    }
        option.set(id);
        selected = id;
        return; 
    }
      
    if (!id.equals(opt)) { printValue(lineNumber, actVal, opt, m); return; }
}

static public void printValue(int lineNumber, byte actVal, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+ map.toString(),
		lineNumber);
	Roast.incValueErrorCount();
}

static public void printValue(int lineNumber, byte actVal, String id, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+(String)map.get(id),
		lineNumber);
	Roast.incValueErrorCount();
}


static public synchronized void compareAndPrintValue(int lineNumber, char actVal)
{                                                                               
    Map m = (Map)map.get();
    Map r = (Map)reversedMap.get();
    String id = (String)r.get(new Character(actVal));
    
    String opt = (String)option.get();

    if (id == null) {
        if (opt==null) printValue(lineNumber, actVal, m);
        else printValue(lineNumber, actVal, opt, m);
        return; 
    }
    
    if (opt==null) { 
        if (!selected.equals("") && !selected.equals(id)) {
	        Roast.logFailureMessage("Value error at line number: " +
		        lineNumber +
		        ".  Actual value: " +actVal +
		        " matched trace option: "+id +
		        "  Expected trace option: "+selected,
		        lineNumber);
	        Roast.incValueErrorCount();
	        return;
	    }
        option.set(id);
        selected = id;
        return; 
    }
        
    if (!id.equals(opt)) { printValue(lineNumber, actVal, opt, m); return; }
}

static public void printValue(int lineNumber, char actVal, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+ map.toString(),
		lineNumber);
	Roast.incValueErrorCount();
}

static public void printValue(int lineNumber, char actVal, String id, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+(String)map.get(id),
		lineNumber);
	Roast.incValueErrorCount();
}


static public synchronized void compareAndPrintValue(int lineNumber, double actVal)
{                                                                                 
    Map m = (Map)map.get();
    Map r = (Map)reversedMap.get();
    
    String opt = (String)option.get();
    String id = (String)r.get(new Double(actVal));

    if (id == null) { 
        if (opt==null) printValue(lineNumber, actVal, m);
        else printValue(lineNumber, actVal, opt, m);
        return; 
    }
    
    if (opt==null) { 
        if (!selected.equals("") && !selected.equals(id)) {
	        Roast.logFailureMessage("Value error at line number: " +
		        lineNumber +
		        ".  Actual value: " +actVal +
		        " matched trace option: "+id +
		        "  Expected trace option: "+selected,
		        lineNumber);
	        Roast.incValueErrorCount();
	        return;
	    }
        option.set(id);
        selected = id;
        return; 
    }
        
    if (!id.equals(opt)) { printValue(lineNumber, actVal, opt, m); return; }
}

static public void printValue(int lineNumber, double actVal, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+ map.toString(),
		lineNumber);
	Roast.incValueErrorCount();
}

static public void printValue(int lineNumber, double actVal, String id, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+(String)map.get(id),
		lineNumber);
	Roast.incValueErrorCount();
}


static public synchronized void compareAndPrintValue(int lineNumber, float actVal)
{   
    Map m = (Map)map.get();
    Map r = (Map)reversedMap.get();
    
    String opt = (String)option.get();
    String id = (String)r.get(new Float(actVal));

    if (id == null) { 
        if (opt==null) printValue(lineNumber, actVal, m);
        else printValue(lineNumber, actVal, opt, m);
        return; 
    }
    
    if (opt==null) { 
        if (!selected.equals("") && !selected.equals(id)) {
	        Roast.logFailureMessage("Value error at line number: " +
		        lineNumber +
		        ".  Actual value: " +actVal +
		        " matched trace option: "+id +
		        "  Expected trace option: "+selected,
		        lineNumber);
	        Roast.incValueErrorCount();
	        return;
	    }
        option.set(id);
        selected = id;
        return; 
    }
        
    if (!id.equals(opt)) { printValue(lineNumber, actVal, opt, m); return; }
}

static public void printValue(int lineNumber, float actVal, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+ map.toString(),
		lineNumber);
	Roast.incValueErrorCount();
}

static public void printValue(int lineNumber, float actVal, String id, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+(String)map.get(id),
		lineNumber);
	Roast.incValueErrorCount();
}


static public synchronized void compareAndPrintValue(int lineNumber, int actVal)
{   
    Map m = (Map)map.get();
    Map r = (Map)reversedMap.get();
    
    String opt = (String)option.get();
    String id = (String)r.get(new Integer(actVal));
    
    if (id == null) { 
        if (opt==null) printValue(lineNumber, actVal, m);
        else printValue(lineNumber, actVal, opt, m);
        return; 
    }
       
    if (opt==null) { 
        if (!selected.equals("") && !selected.equals(id)) {
	        Roast.logFailureMessage("Value error at line number: " +
		        lineNumber +
		        ".  Actual value: " +actVal +
		        " matched trace option: "+id +
		        "  Expected trace option: "+selected,
		        lineNumber);
	        Roast.incValueErrorCount();
	        return;
	    }
        option.set(id);
        selected = id;
        return; 
    }
       
    if (!id.equals(opt)) { printValue(lineNumber, actVal, opt, m); return; }
}

static public void printValue(int lineNumber, int actVal, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+ map.toString(),
		lineNumber);
	Roast.incValueErrorCount();
}

static public void printValue(int lineNumber, int actVal, String id, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+map.get(id),
		lineNumber);
	Roast.incValueErrorCount();
}



static public synchronized void compareAndPrintValue(int lineNumber, long actVal)
{
    Map m = (Map)map.get();
    Map r = (Map)reversedMap.get();
    
    String opt = (String)option.get();
    String id = (String)r.get(new Long(actVal));

    if (id == null) { 
        if (opt==null) printValue(lineNumber, actVal, m);
        else printValue(lineNumber, actVal, opt, m);
        return; 
    }
    
    if (opt==null) { 
        if (!selected.equals("") && !selected.equals(id)) {
	        Roast.logFailureMessage("Value error at line number: " +
		        lineNumber +
		        ".  Actual value: " +actVal +
		        " matched trace option: "+id +
		        "  Expected trace option: "+selected,
		        lineNumber);
	        Roast.incValueErrorCount();
	        return;
	    }
        option.set(id);
        selected = id;
        return; 
    }
        
    if (!id.equals(opt)) { printValue(lineNumber, actVal, opt, m); return; }
}

static public void printValue(int lineNumber, long actVal, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+ map.toString(),
		lineNumber);
	Roast.incValueErrorCount();
}

static public void printValue(int lineNumber, long actVal, String id, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+(String)map.get(id),
		lineNumber);
	Roast.incValueErrorCount();
}


static public synchronized void compareAndPrintValue(int lineNumber, short actVal)
{                                                                                
    Map m = (Map)map.get();
    Map r = (Map)reversedMap.get();
    
    String opt = (String)option.get();
    String id = (String)r.get(new Short(actVal));

    if (id == null) { 
        if (opt==null) printValue(lineNumber, actVal, m);
        else printValue(lineNumber, actVal, opt, m);
        return; 
    }
    
    if (opt==null) { 
        if (!selected.equals("") && !selected.equals(id)) {
	        Roast.logFailureMessage("Value error at line number: " +
		        lineNumber +
		        ".  Actual value: " +actVal +
		        " matched trace option: "+id +
		        "  Expected trace option: "+selected,
		        lineNumber);
	        Roast.incValueErrorCount();
	        return;
	    }
        option.set(id);
        selected = id;
        return; 
    }
        
    if (!id.equals(opt)) { printValue(lineNumber, actVal, opt, m); return; }
}

static public void printValue(int lineNumber, short actVal, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+ map.toString(),
		lineNumber);
	Roast.incValueErrorCount();
}

static public void printValue(int lineNumber, short actVal, String id, Map map)
{
	Roast.logFailureMessage("Value error at line number: " +
		lineNumber +
		".  Actual value: "+actVal +
		"  Expected value: "+(String)map.get(id),
		lineNumber);
	Roast.incValueErrorCount();
}


}
