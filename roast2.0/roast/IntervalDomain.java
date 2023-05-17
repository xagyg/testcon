package roast;

import java.util.*;

// Domain implementing a consecutive sequence of values
public class IntervalDomain implements Domain {

// NEEDED: exception check for size < 0
public IntervalDomain(int size)
{
	low = 0;
	high = size-1;
}

// NEEDED: exception check for high < low (ok if high or low negative??)
public IntervalDomain(int low0,int high0)
{
	low = low0;
	high = high0;
}

public int size()
{
	return high-low+1;
}

// NEEDED: exception check for i not in [0..high-low]
public Object elementAt(int i)
{
	return new Integer(i+low);
}

public String toString()
{
	String s = new String();
	for (int i = low; i <= high; i++) {
		s = s + i;
		if (i != high) {
			s = s + ",";
		}
	}
	return "[" + s + "]";
}

public static void main(String[] args)
{
	// ***** two trivial tests
	IntervalDomain d0 = new IntervalDomain(3,5);
	System.out.println("d0!"+d0.size()+"!"+d0);
	System.out.println();

	IntervalDomain d1 = new IntervalDomain(3);
	System.out.println("d1!"+d1.size()+"!"+d1);
	System.out.println();
}

private int low,high;
}
