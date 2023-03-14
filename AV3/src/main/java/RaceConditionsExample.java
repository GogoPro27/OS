import java.util.ArrayList;
import java.util.List;

public class RaceConditionsExample {
    public static void main(String[] args) throws InterruptedException {

        UnsafeSequence sequence =  new UnsafeSequence();
        List<Thread> threads = new ArrayList<Thread>();

        for(int i = 0; i < 150; i++) {
            Thread incrementExecutor = new IncrementExecutor("p" + i, sequence);
            threads.add(incrementExecutor);

            incrementExecutor.start();
        }

        for (Thread t : threads) {
            t.join();
        }
        
        System.out.println("Main done");

    }

}

class IncrementExecutor extends Thread {

    private String name;
    private UnsafeSequence sequence;

    public IncrementExecutor(String name, UnsafeSequence sequence) {
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

class UnsafeSequence {
    private int value;

    public int getNext() {
        // Not synchronized, race condition
        value++;
        System.out.println("Accessed by thread: " + Thread.currentThread().getName() + " Current value: " + value);
        return value;
    }

    public int getValue() {
        return value;
    }
}