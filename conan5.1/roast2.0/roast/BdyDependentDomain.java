package roast;

import java.util.*;

public abstract class BdyDependentDomain implements DependentDomain {

public BdyDependentDomain(int bdyType0,int k0)
{
	k = k0;
	bdyType = bdyType0;
}

// must initialize low and high, with low <= high
public abstract void setLowHigh(Vector tuple);

public void setDomain(Vector tuple)
{
	// extract low, high from tuple
	setLowHigh(tuple);

	// compute the k*-bdy of [low..high]
	IntervalDomain d = new IntervalDomain(low,high);
	Vector domainVector = new Vector();
	domainVector.addElement(d);

	Vector tupleVector = null;
	if (bdyType == KBDY) {
		tupleVector = Boundary.kBdy(k,domainVector);
	} else {
		tupleVector = Boundary.kStarBdy(k,domainVector);
	}

	// convert tupleVector into a plain Vector
	kBdy = new Vector();
	for (int i = 0; i < tupleVector.size(); i++) {
		Vector tuple0 = (Vector) tupleVector.elementAt(i);
		kBdy.addElement(tuple0.elementAt(0));
	}
}

public int size()
{
	return kBdy.size();
}

public Object elementAt(int i)
{
	return kBdy.elementAt(i);
}

public String toString()
{
	return kBdy.toString();
}

public static void main(String[] args)
{
	{
	BdyDependentDomain bdd =
		new BdyDependentDomain(KBDY,2) {
			public void setLowHigh(Vector tuple)
			{
				Integer i;
				i = (Integer)tuple.elementAt(0);
				low = i.intValue();
				i = (Integer)tuple.elementAt(1);
				high = i.intValue();
			}
		};

	Vector tuple = new Vector();
	tuple.addElement(new Integer(1));
	tuple.addElement(new Integer(10));
	bdd.setDomain(tuple);
	System.out.println("bdd!"+bdd.size()+"!"+bdd);
	}

	{
	BdyDependentDomain bdd =
		new BdyDependentDomain(KSTARBDY,2) {
			public void setLowHigh(Vector tuple)
			{
				Integer i;
				i = (Integer)tuple.elementAt(0);
				low = i.intValue();
				i = (Integer)tuple.elementAt(1);
				high = i.intValue();
			}
		};

	Vector tuple = new Vector();
	tuple.addElement(new Integer(1));
	tuple.addElement(new Integer(10));
	bdd.setDomain(tuple);
	System.out.println("bdd!"+bdd.size()+"!"+bdd);
	}
}

public static final int KBDY = 0;
public static final int KSTARBDY = 1;
private int bdyType;

protected int low,high;
private int k;
private Vector kBdy = new Vector();
}
