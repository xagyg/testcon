package roast;

import java.util.Collection;
/**
* <b>Overview</b>
* <dl>
* Comparison, removal and printing functions for elements in collections.
* These functions check existence and remove the element from
* the collection if it exists.
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
public class CollectionRemove extends CollectionType {
/**
* Returns true if the integer <code>actVal</code>
* exists in the collection <code>expVal</code>. The integer is
* removed from the collection.
* <p>                     
* @param expVal A collection of integers
* @param actVal The integer to be found in the collection 
* @return
*    <code>true</code> if the integer exists in the collection<br>
*    <code>false</code> otherwise
*/         
static public boolean compareValue(int actVal,Collection expVal)
{ return expVal.remove(new Integer(actVal)); }

/**
* Returns true if the char <code>actVal</code>
* exists in the char collection <code>expVal</code>.
* The value is removed from the collection.
* <p>                     
* @param expVal A collection of chars
* @param actVal The char to be found in the collection 
* @return
*    <code>true</code> if the char exists in the collection<br>
*    <code>false</code> otherwise
*/   
static public boolean compareValue(char actVal,Collection expVal)
{ return expVal.remove(new Character(actVal)); }

/**
* Returns true if the floating point value <code>actVal</code>
* exists in the collection <code>expVal</code>. The value is removed
* from the collection.
* <p>                     
* @param expVal A collection of floating point numbers
* @param actVal The value to be found in the collection 
* @return
*    <code>true</code> if the value exists in the collection<br>
*    <code>false</code> otherwise
*/     
static public boolean compareValue(float actVal,Collection expVal)
{ return expVal.remove(new Float(actVal)); } 

/**
* Returns true if the byte <code>actVal</code>
* exists in the byte collection <code>expVal</code>. 
* The value is removed from the collection.
* <p>                     
* @param expVal A collection of bytes
* @param actVal The byte to be found in the collection 
* @return
*    <code>true</code> if the byte exists in the collection<br>
*    <code>false</code> otherwise
*/    
static public boolean compareValue(byte actVal,Collection expVal)
{ return expVal.remove(new Byte(actVal)); }

/**
* Returns true if the boolean <code>actVal</code>
* exists in the boolean collection <code>expVal</code>.
* The value is removed from the collection.
* <p>                     
* @param expVal A collection of boolean values
* @param actVal The value to be found in the collection 
* @return
*    <code>true</code> if the value exists in the collection<br>
*    <code>false</code> otherwise
*/    
static public boolean compareValue(boolean actVal,Collection expVal)
{ return expVal.remove(new Boolean(actVal)); } 

/**
* Returns true if the double <code>actVal</code>
* exists in the double collection <code>expVal</code>.
* The value is removed from the collection.
* <p>                     
* @param expVal A collection of doubles
* @param actVal The value to be found in the collection 
* @return
*    <code>true</code> if the value exists in the collection<br>
*    <code>false</code> otherwise
*/    
static public boolean compareValue(double actVal,Collection expVal)
{ return expVal.remove(new Double(actVal)); }

/**
* Returns true if the short integer <code>actVal</code>
* exists in the collection <code>expVal</code>.
* The value is removed from the collection.
* <p>                     
* @param expVal A collection of short integers
* @param actVal The value to be found in the collection 
* @return
*    <code>true</code> if the integer exists in the collection<br>
*    <code>false</code> otherwise
*/    
static public boolean compareValue(short actVal,Collection expVal)
{ return expVal.remove(new Short(actVal)); }  

/**
* Returns true if the long integer <code>actVal</code>
* exists in the collection <code>expVal</code>.
* The value is removed from the collection.
* <p>                     
* @param expVal A collection of long integers
* @param actVal The value to be found in the collection 
* @return
*    <code>true</code> if the integer exists in the collection<br>
*    <code>false</code> otherwise
*/       
static public boolean compareValue(long actVal,Collection expVal)
{ return expVal.remove(new Long(actVal)); }

/**
* Returns true if the Object <code>actVal</code>
* exists in the Object collection <code>expVal</code>.
* The value is removed from the collection.
* <p>                     
* @param expVal A collection of Objects
* @param actVal The Object to be found in the collection 
* @return
*    <code>true</code> if the Object exists in the collection<br>
*    <code>false</code> otherwise
*/     
static public boolean compareValue(Object actVal,Collection expVal)
{ return expVal.remove(actVal); }    

}

