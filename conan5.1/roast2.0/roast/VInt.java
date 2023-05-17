package roast;

import java.util.*;
import java.lang.Number.*;

public class VInt {

public VInt()
{
	v = new Vector();
}

public VInt(int size,int x0)
{
	v = new Vector();
	for (int i = 0; i < size; i++) {
		v.addElement(new Integer(x0));
	}
}

public int size()
{
	return v.size();
}

public int elementAt(int i)
{
	Integer j = (Integer)v.elementAt(i);
	return j.intValue();
}

public void setElementAt(int x,int i)
{
	v.setElementAt(new Integer(x),i);
}

public void addElement(int x)
{
	v.addElement(new Integer(x));
}

public String toString()
{
	String s = new String();
	for (int i = 0; i < size(); i++) {
		s = s+elementAt(i)+" ";
	}
	return s;
}

public static void main(String[] args)
{
	VInt v = new VInt(5,46);
	v.setElementAt(35,2);
	System.out.println("VInt:"+v.size()+": "+v);
}

private Vector v;
}
