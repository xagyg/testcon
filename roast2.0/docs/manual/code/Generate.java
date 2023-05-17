import roast.*;
import java.util.*;

public class Generate {

public static void main(String[] args)
{
	Iterator i;

	System.out.println("create domainVector...");
	// create alphaDomain: ('a','b','c')
	VectorDomain alphaDomain = new VectorDomain();
	alphaDomain.addElement(new Character('a'));
	alphaDomain.addElement(new Character('b'));
	alphaDomain.addElement(new Character('c'));
	// create intervalDomain: (0,1,2)
	IntervalDomain intervalDomain = new IntervalDomain(3);
	// combine alphaDomain and intervalDomain
	Vector domainVector = new Vector();
	domainVector.addElement(alphaDomain);
	domainVector.addElement(intervalDomain);
	System.out.println("domainVector: "+domainVector);

	System.out.println("compute cartesian product...");
	i = new CPIterator(domainVector);
	while (i.hasNext()) {
		System.out.println(i.next().toString());
	}

	System.out.println("compute 1-boundary...");
	i = new BdyIterator(1,domainVector);
	while (i.hasNext()) {
		System.out.println(i.next().toString());
	}

	System.out.println("compute 2-boundary...");
	i = new BdyIterator(2,domainVector);
	while (i.hasNext()) {
		System.out.println(i.next().toString());
	}

	System.out.println("generate length/index pairs...");
	i = new ExtendIterator(
		new DomainIterator(new IntervalDomain(1,5)),
		new IndexFactory()
	);
	while (i.hasNext()) {
		System.out.println(i.next().toString());
	}
}
}

class IndexIterator implements Iterator {
private Vector v = new Vector();

public IndexIterator(Vector v1)
{
	int n = ((Integer)v1.elementAt(0)).intValue();
	v.addElement(new Integer(0));
	if (n > 1)
		v.addElement(new Integer(n-1));
}

public Object next()
{
	Vector v1 = new Vector();
	v1.addElement(v.elementAt(0));
	v.removeElementAt(0);
	return v1;
}

public boolean hasNext()
{
	return v.size() > 0;
}

public void remove() throws UnsupportedOperationException
{
	throw new UnsupportedOperationException();
}
}

class IndexFactory implements Factory {
public Iterator create(Vector v) { return new IndexIterator(v); }
}
