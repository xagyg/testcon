package roast;

public class Buffer {

protected Object[] buf;
protected int in = 0;
protected int out= 0;
protected int count= 0;
protected int size;

public Buffer(int size)
{
	this.size = size;
	buf = new Object[size];
}

public synchronized void put(Object o)
{
	while (count == size)
		try {
			wait();
		} catch (InterruptedException e) {}
	buf[in] = o;
	++count;
	in = (in+1) % size;
        notify();
}

public synchronized Object get()
{
	while (count==0)
		try {
			wait();
		} catch (InterruptedException e) {}
	Object o = buf[out];
	buf[out] = null;
	--count;
	out = (out+1) % size;
	notify();
	return (o);
}
}
