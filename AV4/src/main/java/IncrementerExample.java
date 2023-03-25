package main.java;

import java.util.ArrayList;
import java.util.List;

class Incrementer {
    private int x;
    public Incrementer(int i) { x=i; }
    public synchronized void increment() { x++; }
    public int getValue() {
        return x;
    }
  
}

public class IncrementerExample {
    private static Incrementer shared = new Incrementer(0);
    public static void main(String[] args) {
        
        List<Thread> incrementExecutorList = new ArrayList<Thread>();
    
        for(int i = 0; i < 50000; i++) {
            Thread t = new Thread() {
                private int threadLocal=0;
          
                public void run() {

                    threadLocal++;

                    System.out.println("Thread local variable: " + threadLocal);
                    shared.increment();

                }

            };

            incrementExecutorList.add(t);
        }

        incrementExecutorList.forEach(t -> t.start());

        incrementExecutorList.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Shared incrementer value: " + shared.getValue());
    }
}
