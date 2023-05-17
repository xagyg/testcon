package roast;

import java.util.*;

public class ExtendIterator implements Iterator {
Iterator baseIter;
Factory extendFact;
Vector baseVal;
Iterator extendIter = null;

public ExtendIterator(Iterator iter,Factory fact)
{
	baseIter = iter;
	extendFact = fact;
	if (baseIter.hasNext()) {
		baseVal = (Vector)baseIter.next();
		extendIter = extendFact.create(baseVal);
		while (!extendIter.hasNext() && baseIter.hasNext()) {
			baseVal = (Vector)baseIter.next();
			extendIter = extendFact.create(baseVal);
		}
	}
}

public Object next() throws NoSuchElementException
{
	if (!hasNext()) {
		throw new NoSuchElementException();
	}
	Vector v = new Vector(baseVal);
	v.addAll((Vector)extendIter.next());
	while (!extendIter.hasNext() && baseIter.hasNext()) {
		baseVal = (Vector)baseIter.next();
		extendIter = extendFact.create(baseVal);
	}
	return (v);
}

public boolean hasNext()
{
	return (baseIter.hasNext() ||
		(extendIter != null && extendIter.hasNext()));
}

public void remove() throws UnsupportedOperationException
{
	throw new UnsupportedOperationException();
}
}
