package roast;

import java.util.*;

public class DomainIterator implements Iterator {

public DomainIterator(Domain d0)
{
	d = d0;
}

public Object next() throws NoSuchElementException
{
	if (!hasNext()) {
		throw new NoSuchElementException();
	}
	Vector v = new Vector();
	v.addElement(d.elementAt(count++));
	return v;
}

public boolean hasNext()
{
	return count < d.size();
}

public void remove() throws UnsupportedOperationException
{
	throw new UnsupportedOperationException();
}

private Domain d;
private int count = 0; // number of elements returned so far

}
