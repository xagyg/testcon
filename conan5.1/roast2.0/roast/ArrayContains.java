package roast;

import java.util.Arrays;

/**
* <b>Overview</b>
* <dl>
* Comparison and printing functions for elements in arrays.
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
public class ArrayContains {
/**
* Returns true if the integer <code>actVal</code>
* exists in the array <code>expVal</code>.
* <p>                     
* @param expVal An array of integers
* @param actVal The integer to be found in the array 
* @return
*    <code>true</code> if the integer exists in the array<br>
*    <code>false</code> otherwise
*/         
static public boolean compareValue(int actVal, int[] expVal)
{ 
    Arrays.sort(expVal);
    return Arrays.binarySearch(expVal,actVal) >= 0;
} 

static public synchronized void compareAndPrintValue(int lineNumber, int actVal, int[] expVal)
{ 
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber,actVal,expVal);
    }
} 
/**
* Returns true if the char <code>actVal</code>
* exists in the char array <code>expVal</code>.
* <p>                     
* @param expVal An array of chars
* @param actVal The char to be found in the array 
* @return
*    <code>true</code> if the char exists in the array<br>
*    <code>false</code> otherwise
*/     
static public boolean compareValue(char actVal, char[] expVal)
{ 
    Arrays.sort(expVal);
    return Arrays.binarySearch(expVal,actVal) >= 0;
} 

static public synchronized void compareAndPrintValue(int lineNumber, char actVal, char[] expVal)
{ 
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber,actVal,expVal);
    }
} 
/**
* Returns true if the floating point value <code>actVal</code>
* exists in the array <code>expVal</code>.
* <p>                     
* @param expVal An array of floating point numbers
* @param actVal The value to be found in the array 
* @return
*    <code>true</code> if the value exists in the array<br>
*    <code>false</code> otherwise
*/     
static public boolean compareValue(float actVal, float[] expVal)
{ 
    Arrays.sort(expVal);
    return Arrays.binarySearch(expVal,actVal) >= 0;
} 

static public synchronized void compareAndPrintValue(int lineNumber, float actVal, float[] expVal)
{ 
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber,actVal,expVal);
    }
} 
/**
* Returns true if the byte <code>actVal</code>
* exists in the byte array <code>expVal</code>.
* <p>                     
* @param expVal An array of bytes
* @param actVal The byte to be found in the array 
* @return
*    <code>true</code> if the byte exists in the array<br>
*    <code>false</code> otherwise
*/      
static public boolean compareValue(byte actVal, byte[] expVal)
{ 
    Arrays.sort(expVal);
    return Arrays.binarySearch(expVal,actVal) >= 0;
} 

static public synchronized void compareAndPrintValue(int lineNumber, byte actVal, byte[] expVal)
{ 
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber,actVal,expVal);
    }
} 
/**
* Returns true if the double <code>actVal</code>
* exists in the double array <code>expVal</code>.
* <p>                     
* @param expVal An array of doubles
* @param actVal The value to be found in the array 
* @return
*    <code>true</code> if the value exists in the array<br>
*    <code>false</code> otherwise
*/                   
static public boolean compareValue(double actVal, double[] expVal)
{ 
    Arrays.sort(expVal);
    return Arrays.binarySearch(expVal,actVal) >= 0;
} 

static public synchronized void compareAndPrintValue(int lineNumber, double actVal, double[] expVal)
{ 
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber,actVal,expVal);
    }
} 
/**
* Returns true if the short integer <code>actVal</code>
* exists in the array <code>expVal</code>.
* <p>                     
* @param expVal An array of short integers
* @param actVal The value to be found in the array 
* @return
*    <code>true</code> if the integer exists in the array<br>
*    <code>false</code> otherwise
*/         
static public boolean compareValue(short actVal, short[] expVal)
{ 
    Arrays.sort(expVal);
    return Arrays.binarySearch(expVal,actVal) >= 0;
} 

static public synchronized void compareAndPrintValue(int lineNumber, short actVal, short[] expVal)
{ 
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber,actVal,expVal);
    }
} 
/**
* Returns true if the long integer <code>actVal</code>
* exists in the array <code>expVal</code>.
* <p>                     
* @param expVal An array of long integers
* @param actVal The value to be found in the array 
* @return
*    <code>true</code> if the integer exists in the array<br>
*    <code>false</code> otherwise
*/        
static public boolean compareValue(long actVal, long[] expVal)
{ 
    Arrays.sort(expVal);
    return Arrays.binarySearch(expVal,actVal) >= 0;
} 

static public synchronized void compareAndPrintValue(int lineNumber, long actVal, long[] expVal)
{ 
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber,actVal,expVal);
    }
} 
/**
* Returns true if the Object <code>actVal</code>
* exists in the Object array <code>expVal</code>.
* <p>                     
* @param expVal An array of Objects
* @param actVal The Object to be found in the array 
* @return
*    <code>true</code> if the Object exists in the array<br>
*    <code>false</code> otherwise
*/     
static public boolean compareValue(Object actVal, Object[] expVal)
{ 
    Arrays.sort(expVal);
    return Arrays.binarySearch(expVal,actVal) >= 0;
}

static public synchronized void compareAndPrintValue(int lineNumber, Object actVal, Object[] expVal)
{ 
    if (!compareValue(actVal, expVal)) {
        printValue(lineNumber,actVal,expVal);
    }
}  

static public void printValue(int lineNumber,int actVal,int[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); }
static public void printValue(int lineNumber,char actVal,char[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); }
static public void printValue(int lineNumber,float actVal,float[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); }
static public void printValue(int lineNumber,byte actVal,byte[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); }
static public void printValue(int lineNumber,boolean actVal,boolean[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); } 
static public void printValue(int lineNumber,double actVal,double[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); }
static public void printValue(int lineNumber,short actVal,short[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); }
static public void printValue(int lineNumber,long actVal,long[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); }
static public void printValue(int lineNumber,Object actVal,Object[] expVal)
{ ValueType.printValue(lineNumber,String.valueOf(actVal), expVal.toString()); }
}