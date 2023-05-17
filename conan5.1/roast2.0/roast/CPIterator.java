package roast;

import java.util.*;

public class CPIterator implements Iterator {

static final int BUFSIZ = 5;	// buffer size
Buffer buf;			// buffer used for communication
Vector nextTuple;		// next vector to be returned by next()

public CPIterator(Vector domainVector)
{
	buf = new Buffer(BUFSIZ);

	CPProducer prod = new CPProducer(buf,domainVector);
	prod.start();

	nextTuple = (Vector)buf.get();
}

public Object next() throws NoSuchElementException
{
	if (!hasNext()) {
		throw new NoSuchElementException();
	}
	Vector oldTuple = nextTuple;
	nextTuple = (Vector)buf.get();
	return (oldTuple);
}

public boolean hasNext()
{
	return (nextTuple != null);
}

public void remove() throws UnsupportedOperationException
{
	throw new UnsupportedOperationException();
}

public static void main (String args[])
{
	Vector v = new Vector();
	v.addElement(new IntervalDomain(2));
	v.addElement(new IntervalDomain(3,5));
	v.addElement(new IntervalDomain(6,6));

	CPIterator cpIter = new CPIterator(v);

	while (cpIter.hasNext()) {
		System.out.println("output vector: "+cpIter.next());
	}
}
}

class CPProducer extends Thread {

Buffer buf;
Vector domainVector;

CPProducer(Buffer b,Vector dV)
{
	buf = b;
	domainVector = dV;
}

static boolean nextcp(VInt indices,Vector domainVector)
{
	for (int i = 0; i < domainVector.size(); i++) {
		Domain d = (Domain) domainVector.elementAt(i);
		if (indices.elementAt(i) != d.size()-1) {
			indices.setElementAt(indices.elementAt(i)+1,i);
			for (int j = 0; j < i; j++)
				indices.setElementAt(0,j);
			return(true);
		}
	}
	return(false);
}

public void run()
{
	VInt indices = new VInt(domainVector.size(),0);
	Vector cp = new Vector();
	do {
		Vector tuple = new Vector();
		for (int i = 0; i < domainVector.size(); i++) {
			Domain d = (Domain) domainVector.elementAt(i);
			tuple.addElement(d.elementAt(indices.elementAt(i)));
		}
		buf.put(tuple);
	} while (nextcp(indices,domainVector));
	buf.put(null);
}
}
