package conan;

public class ConanHelper {
    
    static int deadevents;
        
    public static boolean checkDeadlock (
                         conan.ConanThread[] thread,
                         int deadlock,
                         int casenum,
                         int clocktime) {

      boolean deadlocked = false;

      for (int i=0; i < thread.length; ++i) {
        if (thread[i].isAlive()) {
          if (clocktime != deadlock) {
            System.out.println("Sequence " + casenum + ": liveness error detected at time: " + thread[i].lastTick() + ". Thread: " + thread[i].getName());
            ++deadevents;
          }
          thread[i].interrupt();
          deadlocked = true;
        }
      }
      return deadlocked;
    }      
    
    public static int deadlockEvents() {
        return deadevents;
    }


    public static void checkExpectedDeadlock (int deadlock, int casenum) {
        if (deadlock > 0) {
            System.out.println("case " + casenum +": expected deadlock at time " + deadlock + " did not occur");
            ++deadevents;
        }
    }
}