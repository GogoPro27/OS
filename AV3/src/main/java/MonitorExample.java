import java.util.ArrayList;
import java.util.List;

public class MonitorExample {
    
    public static void main(String[] args) throws InterruptedException {

        SafeSequence sequence =  new SafeSequence();
        List<Thread> threads = new ArrayList<Thread>();

        for(int i = 0; i < 50; i++) {
            Thread incrementExecutor = new SafeIncrementExecutor("p" + i, sequence);
            threads.add(incrementExecutor);
        }

        threads.forEach(t -> t.start());

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        System.out.println("Main done");
        
    }

}

class SafeIncrementExecutor extends Thread {

    private String name;
    private SafeSequence sequence;

    public SafeIncrementExecutor(String name, SafeSequence sequence) {
        super(name);
        this.name = name;
        this.sequence = sequence;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            this.sequence.getNext();
        }
    }
}

class SafeSequence {
    private int value;

    public synchronized int getNext() {
        value++;
        System.out.println("Accessed by thread: " + Thread.currentThread().getName() + " Current value: " + value);
        return value;
    }

    public int getValue() {
        return value;
    }
}