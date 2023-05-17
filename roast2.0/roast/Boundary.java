package roast;

import java.util.*;
import java.lang.Number.*;

/***** terminology
domain: sequence of values
	in Java: any class that implements Domain interface

domain vector: a sequence of domains
	in Java: a Vector of Domains

tuple: a sequence of values, one per domain

tuple vector: a sequence of tuples, with each tuple of the same length
	in Java: a Vector of Vectors
*/

class AddTuple implements ProcessTuple {
public AddTuple(Vector tupleVector0) { tupleVector = tupleVector0; }

public void processTuple(Vector tuple)
{
	tupleVector.addElement(tuple);
}

Vector tupleVector;
}

public class Boundary {

// ***** cp

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

// cp: tuple vector version
public static Vector cp(Vector domainVector)
{
	Vector tupleVector = new Vector();

	cp(domainVector,new AddTuple(tupleVector));

	return tupleVector;
}

// cp: ProcessTuple version
public static void cp(Vector domainVector,ProcessTuple p)
{
	VInt indices = new VInt(domainVector.size(),0);
	Vector cp = new Vector();

	do {
		Vector tuple = new Vector();
		for (int i = 0; i < domainVector.size(); i++) {
			Domain d = (Domain) domainVector.elementAt(i);
			tuple.addElement(d.elementAt(indices.elementAt(i)));
		}
		p.processTuple(tuple);
	} while (nextcp(indices,domainVector));
}

// ***** extends

// extends each tuple in tv0 an element from d
public static Vector extend(Vector tv0,DependentDomain d)
{
	Vector tv1 = new Vector();

	// for each tuple t in tv0
	for (int i = 0; i < tv0.size(); i++){
		Vector t = (Vector) tv0.elementAt(i);
		d.setDomain(t);
		// for each element in d: add (t,d[i]) to tv1
		for (int j = 0; j < d.size(); j++){
			Vector t0 = (Vector) t.clone();
			t0.addElement(d.elementAt(j));
			tv1.addElement(t0);
		}
	}

	return tv1;
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

// kBdy: tuple vector version
public static Vector kBdy(int k,Vector domainVector)
{
	Vector tupleVector = new Vector();

	kBdy(k,domainVector,new AddTuple(tupleVector));

	return tupleVector;
}

// kBdy: ProcessTuple version
public static void kBdy(int k,Vector domainVector,ProcessTuple p)
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
		p.processTuple(tuple);
	} while (nextbdy(k,indices,domainVector));
}

// kStarBdy: tuple vector version
public static Vector kStarBdy(int k,Vector domainVector)
{
	Vector tupleVector = new Vector();
	kStarBdy(k,domainVector,new AddTuple(tupleVector));
	return tupleVector;
}

// kStarBdy: ProcessTuple version
public static void kStarBdy(int k,Vector domainVector,ProcessTuple p)
{
	for (int i = 1; i <= k; i++) {
		kBdy(i,domainVector,p);
	}
}

// ***** kPer

static boolean nextper(int cur,int k,VInt indices,Vector domainVector)
{
	int u;
	// ***** first try to increment one of the indices that is not cur
	for (int i = 0; i < domainVector.size(); i++) {
		Domain d1 = (Domain) domainVector.elementAt(i);
		u = ubnd(k,d1.size());
		if (cur != i && indices.elementAt(i) < u) {
			indices.setElementAt(indices.elementAt(i)+1,i);
			for (int j = 0; j < i; j++)
				if (cur != j) {
					Domain d2 = (Domain)
						domainVector.elementAt(j);
					indices.setElementAt(
						lbnd(k,d2.size()),j
					);
				}
			return(true);
		}
	}
	// ***** check if index cur can be incremented
	Domain d = (Domain) domainVector.elementAt(cur);
	u = ubnd(k,d.size());
	if (indices.elementAt(cur) != u) {
		indices.setElementAt(u,cur);
		for (int j = 0; j < domainVector.size(); j++)
			if (cur != j) {
				Domain d2 = (Domain) domainVector.elementAt(j);
				indices.setElementAt(lbnd(k,d2.size()),j);
			}
		return(true);
	}
	return(false);
}

// kPer: tuple vector version
public static Vector kPer(int k,Vector domainVector)
{
	Vector tupleVector = new Vector();

	kPer(k,domainVector,new AddTuple(tupleVector));

	return tupleVector;
}

// kPer: ProcessTuple version
public static void kPer(int k,Vector domainVector,ProcessTuple p)
{
	VInt indices = new VInt(domainVector.size(),0);

	// ***** calculate perimeter
	for (int i = 0; i < domainVector.size(); i++) {
		// ***** set initial indices (all lower bounds)
		for (int j = 0; j < domainVector.size(); j++) {
			Domain d = (Domain) domainVector.elementAt(j);
			indices.setElementAt(lbnd(k,d.size()),j);
		}
		do {
			Vector tuple = new Vector();
			for (int j = 0; j < domainVector.size(); j++) {
				Domain d = (Domain) domainVector.elementAt(j);
				tuple.addElement(
					d.elementAt(indices.elementAt(j))
				);
			}
			p.processTuple(tuple);
		} while (nextper(i,k,indices,domainVector));
	}
}

// kStarPer: tuple vector version
public static Vector kStarPer(int k,Vector domainVector)
{
	Vector tupleVector = new Vector();
	kStarPer(k,domainVector,new AddTuple(tupleVector));
	return tupleVector;
}

// kStarPer: ProcessTuple version
public static void kStarPer(int k,Vector domainVector,ProcessTuple p)
{
	for (int i = 1; i <= k; i++) {
		kPer(i,domainVector,p);
	}
}

}
