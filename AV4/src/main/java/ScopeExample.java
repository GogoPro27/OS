package main.java;

import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class IntegerWrapper {
    private int value = 0;
    public synchronized void increment() { this.value++;}
    public int getVal() { return value;}
}

class ExampleThread extends Thread {
    // would get a reference to some object which becomes shared
    private IntegerWrapper wrapper;
    // visible by this thread only and is not shared
    // no need for protection
    private int threadLocalField = 0;
    // can be access from other threads and should be protected when used
    public int threadPublishedField = 0;

    public ExampleThread(int init, IntegerWrapper iw) {
        // init is a primitive variable and thus it is not shared
        threadLocalField = init;
        // this object can be shared since iw is a reference
        wrapper = iw;
    }

    public void run() {
        privateFieldIncrement();
        publicFieldIncrement();
        wrapperIncrement();
    }

    private void forceSwitch(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) { /* DO NOTHING */}
    }

    private void privateFieldIncrement() {
        // only this thread can access this field
        threadLocalField++;
        // this variable is only visible in this method (not shared)
        int localVar = threadLocalField;
        forceSwitch(3);
        // check for race condition, will it ever occur?
        if (localVar != threadLocalField) {
            System.err.println("private-mismatch-%d" + getId());
        } else {
            System.out.println(String.format("[private-%d] %d", getId(), threadLocalField));
        }
    }

    private Lock lock = new ReentrantLock(); // does static make a difference?

    public void publicFieldIncrement() {
        // increment the public field and store it to localVar
        int localVar = ++threadPublishedField;
        forceSwitch(10);
        // check for race condition, will it ever occur?
        if (localVar != threadPublishedField) {
            System.err.println("public-mismatch-%d" + getId());
        } else {
            System.out.println(String.format("[public-%d] %d", getId(), threadPublishedField));
        }
    }

    public void wrapperIncrement() {
        // increment the shared variable
        wrapper.increment();
        int localVar = wrapper.getVal();
        forceSwitch(3);
        // check for race condition, it will be common
        if (localVar != wrapper.getVal()) {
            System.err.println("wrapper-mismatch-%d" + getId());
        } else {
            System.out.println(String.format("[wrapper-%d] %d", getId(), wrapper.getVal()));
        }
    }

    public void publicFieldSafeIncrement() {
        lock.lock();
        publicFieldIncrement();
        lock.unlock();
    }

    public void wrapperSafeIncrement() {
        synchronized(wrapper) {
            wrapperIncrement();
        }
    }
}

public class ScopeExample {
    public static void main(String[] args) {
        HashSet<ExampleThread> threads = new HashSet<ExampleThread>();
        IntegerWrapper sharedWrapper = new IntegerWrapper();
        
        // Shuffle the threads using HashSet

        for (int i = 0; i < 100; i++) {
            ExampleThread t = new ExampleThread(0, sharedWrapper);
            threads.add(t);
        }

        for(Thread t : threads) {
            t.start();
        }

        for(ExampleThread t : threads) { // modify thread variables
            /* The private fields are not accessible,
             and thus protected by design */
            t.publicFieldIncrement();
            t.wrapperIncrement();
        }
    }
}
