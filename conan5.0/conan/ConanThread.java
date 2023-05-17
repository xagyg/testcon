
package conan;

import java.util.*;

public class ConanThread extends Thread {
    
    public ConanThread(String name) {
        super(name);             
    }    

    protected static Map threads = new HashMap();

    protected int conan_LastTick = 0;

    public MonitorClock clock;
    public int conan_Id;  
    
    protected int time() { return clock.time(); }
    public int lastTick() { return conan_LastTick; } 
    
    public static void add(String id, ConanThread thread) {
            threads.put(id, thread);
    }
    
    protected static Thread getThread(String id) {
        return (Thread) threads.get(id);
    }
    
    public static void clear() {
        threads.clear();
    }
}
