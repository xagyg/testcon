package conan;

public class MonitorClock {
                              
	private int time, lastTick;
		  
	public MonitorClock () {
	    time = 0;
	    lastTick = 0;
	} 	                                               
      
	public synchronized void await(int when) {
       
	    // keep waiting until it's time for process to continue	              
        while (time != when) {  
		    try {      
			    wait();  
		    } catch (InterruptedException e) {}
	    }       
        lastTick = time;	        
    }                

    public synchronized int time() {
        return time;
    }           
    
    public synchronized int lastTick() {
        return lastTick;
    }

	public synchronized void tick() {
		// Increment the time and notify all processes that are waiting
		time++;      
		notifyAll();
	}
}
