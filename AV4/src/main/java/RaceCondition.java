package main.java;

import java.util.ArrayList;
import java.util.List;

class GeoObject {
    private int height;
    private int width;

    public GeoObject(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void incrementHight() {
        this.height++;
    }

    public void incrementWidth() {
        this.width++;
    }

    public String toString() {
        return String.format("GeoObject: {height: %d, width: %d}", this.height, this.width);
    }
}

class ObjectMutation {
    private static String staticField; // can be shared
  
    private String classField; // can be shared
  
    public static void example(String threadName) {
        GeoObject localVariable= new GeoObject(5, 3); // never shared 
        System.out.println("Initial value local: " + localVariable + " thread: " + threadName);
        GeoObject result = mutate(localVariable);
        System.out.println("Local value after mutation: " + result + " thread: " + threadName);
    }
  
    public static void example(String threadName, GeoObject mayBeShared) {
        System.out.println("Initial value shared: " + mayBeShared + " thread: " + threadName);
        GeoObject result = mutate(mayBeShared);
        System.out.println("Shared value after mutation: " + result + " thread: " + threadName);
    }

    private static GeoObject mutate(GeoObject initialValue) {
        initialValue.incrementHight();
        initialValue.incrementWidth();
        return initialValue;
    }
}

public class RaceCondition {

    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<Thread>();
        GeoObject objectShared = new GeoObject(0, 0);
        for (int i = 0; i < 10; i++) {
            threadList.add(new MutationThread("T" + i, objectShared));
        }

        threadList.forEach(t -> t.start());
        threadList.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}

class MutationThread extends Thread {

    private String threadName;
    private GeoObject sharedObject;

    public MutationThread(String threadName, GeoObject sharedObject) {
        this.threadName = threadName;
        this.sharedObject = sharedObject;
    }

    public void run() {
        ObjectMutation.example(threadName);
        ObjectMutation.example(threadName, sharedObject);
    }
}