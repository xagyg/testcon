// The monitor class
class ProducerConsumer {
	String contents;
	int theLength = 0;
	int totalLength;

	// The get routine
    public synchronized char receive() {
		char y;
		while (theLength == 0){
			try{
				wait(); 
			}
			catch (InterruptedException e){
			}
		}
		// totalLength - theLength to get the right order
		y = contents.charAt(totalLength - theLength);
		theLength = theLength - 1;
		notifyAll();
		return y;
    }

	// the put routine
    public synchronized void send(String x) {
		while (theLength > 0){
			try{
				wait();
			}
			catch (InterruptedException e){
			}
		}
		contents = x;
		theLength = x.length();
		totalLength = theLength;
		notifyAll();
    }
}