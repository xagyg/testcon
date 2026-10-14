package conan.runtime;

import java.util.Vector;


public class ConanVector extends Vector<Object> {

    private static final long serialVersionUID = 1L;

    public ConanVector() {
        super();
    }
    
    public ConanVector(int[] intArray) {
        super();
        add(intArray);
    }     
    
    public ConanVector(char[] charArray) {
        super();
        add(charArray);
    }
    
    public ConanVector(double[] doubleArray) {
        super();
        add(doubleArray);
    }
    
    public ConanVector(float[] floatArray) {
        super();
        add(floatArray);
    }
    
    public ConanVector(byte[] byteArray) {
        super();
        add(byteArray);
    }
    
    public ConanVector(long[] longArray) {
        super();
        add(longArray);
    }
    
    public ConanVector(short[] shortArray) {
        super();
        add(shortArray);
    }
    
    public void add(int i) {
        super.add(Integer.valueOf(i));
    }
    
    public void add(char c) {
        super.add(Character.valueOf(c));
    }
    
    public void add(double d) {
        super.add(Double.valueOf(d));
    }
    
    public void add(float f) {
        super.add(Float.valueOf(f));
    }
    
    public void add(byte b) {
        super.add(Byte.valueOf(b));
    }
    
    public void add(long l) {
        super.add(Long.valueOf(l));
    }
    
    public void add(short s) {
        super.add(Short.valueOf(s));
    }    
    
    public void add(int[] intArray) {
        for (int i=0; i < intArray.length; ++i) {
            add(intArray[i]);
        }
    }    
    
    public void add(char[] charArray) {
        for (int i=0; i < charArray.length; ++i) {
            add(charArray[i]);
        }
    } 
    
    public void add(double[] doubleArray) {
        for (int i=0; i < doubleArray.length; ++i) {
            add(doubleArray[i]);
        }
    } 
    
    public void add(float[] floatArray) {
        for (int i=0; i < floatArray.length; ++i) {
            add(floatArray[i]);
        }
    } 
    
    public void add(byte[] byteArray) {
        for (int i=0; i < byteArray.length; ++i) {
            add(byteArray[i]);
        }
    } 
    
    public void add(long[] longArray) {
        for (int i=0; i < longArray.length; ++i) {
            add(longArray[i]);
        }
    } 
    
        public void add(short[] shortArray) {
        for (int i=0; i < shortArray.length; ++i) {
            add(shortArray[i]);
        }
    }     
}