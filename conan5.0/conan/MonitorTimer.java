package conan;

public class MonitorTimer extends Thread {    

//	final static int MAX_TICKS = 9999;

    private MonitorClock clock;
    private int ticks, ticktime;
/*                       
	public MonitorTimer(MonitorClock c) {
		clock = c;  
		ticks = MAX_TICKS;
	} 
	 
	public MonitorTimer(MonitorClock c, int t) {
		clock = c;  
		ticks = t;
	}
*/	
    public MonitorTimer(MonitorClock c, int t, int ticktime) {
        clock = c;
        ticks = t;
        this.ticktime = ticktime;
    }       
		  	
    public void run() {
        try{
            while(clock.time() < ticks+1) {
                // Sleep for ticktime milliseconds
                sleep(ticktime);
                yield();
                clock.tick();
            }
        } catch (InterruptedException e) {}
    }
}
