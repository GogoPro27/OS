package main.java;

import java.util.ArrayList;
import java.util.List;

class Composite {

    private Composite c = null; int x = 0; 
  
    public Composite(int b) { x=b; }
  
    public Composite(Composite a, int b) { c=a; x=b; }
  
    public void move() { if(c!=null) c.move(); x++; }

    public int getValue() {
        return x;
    }
}

public class CompositeExample {
    private static final Composite shared=new Composite(0);
    public static void main(String[] args) {

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threadList.add(new Thread() {
  
                public void run() {
          
                    Composite local = new Composite(shared, 0);   
                    local.move(); 
                }
            });
        }
        threadList.forEach(t -> t.start());
        threadList.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(shared.getValue());
    }
  
}
  
  