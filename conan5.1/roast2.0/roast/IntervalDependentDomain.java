package roast;

import java.util.*;

public abstract class IntervalDependentDomain implements DependentDomain {

// must initialize low and high, with low <= high
public abstract void setLowHigh(Vector tuple);

public void setDomain(Vector tuple)
{
	setLowHigh(tuple);
	intervalDomain = new IntervalDomain(low,high);
}

public int size()
{
	return intervalDomain.size();
}

public Object elementAt(int i)
{
	return intervalDomain.elementAt(i);
}

public String toString()
{
	return intervalDomain.toString();
}

public static void main(String[] args)
{
	IntervalDependentDomain rdd =
		new IntervalDependentDomain() {
			public void setLowHigh(Vector tuple)
			{
				low = 0;
				Integer i = (Integer)tuple.elementAt(0);
				high = i.intValue()-1;
			}
		};

	Vector v = new Vector();
	v.addElement(new Integer(5));
	rdd.setDomain(v);
	System.out.println("rdd!"+rdd.size()+"!"+rdd);
}

protected int high,low;
private IntervalDomain intervalDomain = new IntervalDomain(0);

}
