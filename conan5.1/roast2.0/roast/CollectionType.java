package roast;

import java.util.Collection;
/**
* <b>Overview</b>
* <dl>
* Printing functions for elements in collections.
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
public abstract class CollectionType {
    
static public boolean compareValue(int actVal, Collection expVal) { return true; }    

static public synchronized void compareAndPrintValue(int lineNumber,int actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public boolean compareValue(char actVal, Collection expVal) { return true; }    

static public synchronized void compareAndPrintValue(int lineNumber,char actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public boolean compareValue(float actVal, Collection expVal) { return true; }

static public synchronized void compareAndPrintValue(int lineNumber,float actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public boolean compareValue(byte actVal, Collection expVal) { return true; }    

static public synchronized void compareAndPrintValue(int lineNumber,byte actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public boolean compareValue(boolean actVal, Collection expVal) { return true; }

static public synchronized void compareAndPrintValue(int lineNumber,boolean actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public boolean compareValue(double actVal, Collection expVal) { return true; }

static public synchronized void compareAndPrintValue(int lineNumber,double actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public boolean compareValue(short actVal, Collection expVal) { return true; }

static public synchronized void compareAndPrintValue(int lineNumber,short actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public boolean compareValue(long actVal, Collection expVal) { return true; }

static public synchronized void compareAndPrintValue(int lineNumber,long actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public boolean compareValue(Object actVal, Collection expVal) { return true; }

static public synchronized void compareAndPrintValue(int lineNumber,Object actVal,Collection expVal)
{
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber, actVal, expVal);
    }
}

static public void printValue(int lineNumber,int actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); }
static public void printValue(int lineNumber,char actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); }
static public void printValue(int lineNumber,float actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); }
static public void printValue(int lineNumber,byte actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); }
static public void printValue(int lineNumber,boolean actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); } 
static public void printValue(int lineNumber,double actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); }
static public void printValue(int lineNumber,short actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); }
static public void printValue(int lineNumber,long actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); }
static public void printValue(int lineNumber,Object actVal,Collection expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal); }
}