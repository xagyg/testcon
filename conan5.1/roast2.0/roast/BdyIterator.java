package roast;

import java.util.*;

public class BdyIterator implements Iterator {

static final int BUFSIZ = 5;	// buffer size
Buffer buf;			// buffer used for communication
Vector nextTuple;		// next vector to be returned by next()

public BdyIterator(int k,Vector domainVector)
{
	buf = new Buffer(BUFSIZ);

	BdyProducer prod = new BdyProducer(buf,k,domainVector);
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

	BdyIterator iter = new BdyIterator(1,v);
	while (iter.hasNext()) {
		System.out.println("output vector (k=1): "+iter.next());
	}
	iter = new BdyIterator(2,v);
	while (iter.hasNext()) {
		System.out.println("output vector (k=2): "+iter.next());
	}
	iter = new BdyIterator(3,v);
	while (iter.hasNext()) {
		System.out.println("output vector (k=3): "+iter.next());
	}
}
}

class BdyProducer extends Thread {
Buffer buf;
int k;
Vector domainVector;

BdyProducer(Buffer b,int k,Vector dV)
{
	buf = b;
	this.k = k;
	domainVector = dV;
}

// ***** kBdy, kPer utilities

static int lbnd(int k,int siz)
{
	if (siz % 2 == 0)
		if (k < siz/2) return(k-1);
		else return(siz/2 - 1);
	else
		if (k < (siz+1)/2) return(k-1);
		else return((siz-1)/2);
}

static int ubnd(int k,int siz)
{
	if (siz % 2 == 0)
		if (k < siz/2) return(siz-k);
		else return(siz/2);
	else
		if (k < (siz+1)/2) return(siz-k);
		else return((siz-1)/2);
}

// ***** kBdy

static boolean nextbdy(int k,VInt indices,Vector domainVector)
{
	for (int i = 0; i < domainVector.size(); i++) {
		Domain d1 = (Domain) domainVector.elementAt(i);
		int u = ubnd(k,d1.size());
		if (indices.elementAt(i) != u) {
			indices.setElementAt(u,i);
			for (int j = 0; j < i; j++) {
				Domain d2 = (Domain) domainVector.elementAt(j);
				indices.setElementAt(lbnd(k,d2.size()),j);
			}
			return(true);
		}
	}
	return(false);
}

public void run()
{
	VInt indices = new VInt();

	// ***** calculate initial indices (all lower bounds)
	for (int i = 0; i < domainVector.size(); i++) {
		Domain d = (Domain) domainVector.elementAt(i);
		indices.addElement(lbnd(k,d.size()));
	}

	// ***** calculate boundary
	do {
		Vector tuple = new Vector();
		for (int i = 0; i < domainVector.size(); i++) {
			Domain d = (Domain) domainVector.elementAt(i);
			tuple.addElement(d.elementAt(indices.elementAt(i)));
		}
		buf.put(tuple);
	} while (nextbdy(k,indices,domainVector));
	buf.put(null);
}
}
