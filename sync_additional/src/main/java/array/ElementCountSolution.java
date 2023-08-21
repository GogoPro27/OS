import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ElementCountSolution {

    public static long count = 0;

    static final BoundedRandomGenerator random = new BoundedRandomGenerator();

    private static final int ARRAY_LENGTH = 1000000;
    private static final int NUM_THREADS = 10;

    private static final int SEARCH_TARGET = random.nextInt();  // This is the target element we are looking for

    // TODO: Define sychronization elements and initialize
    public static Lock lock = new ReentrantLock();
    
    // DO NOT CHANGE
    public static int[] getSubArray(int[] array, int start, int end) {
        return Arrays.copyOfRange(array, start, end);
    }

    public static void main(String[] args) {

        int[] arr = ArrayGenerator.generate(ARRAY_LENGTH, SEARCH_TARGET);

        // TODO: Make the SearchThread class a thread and start 10 instances
        // Each instance should take a subarray from the original array with equal length

        
        List<CountThread> threadList = new ArrayList<>();
        int elementsPerThread = arr.length / NUM_THREADS;

        for (int i=0; i<NUM_THREADS; i++) {
            int start = i * elementsPerThread;
            int end = Math.min(arr.length, (i + 1) * elementsPerThread);
            int[] subarray = getSubArray(arr, start, end);

            threadList.add(new CountThread(subarray, SEARCH_TARGET));
        }


        // TODO: Start the 10 threads

        threadList.forEach(t -> t.start());

        // wait for all threads to finish before printing the result

        threadList.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });


        // TODO: Check if the threads have finished and interrupt any running thread

        for (Thread t: threadList) {
            if (t.isAlive()) {
                System.out.println("Interrupting thread");
                t.interrupt();
            }
        }

        

        // DO NOT CHANGE

        System.out.println("The number of total counted elements is: " + count);
        System.out.println("The generated number of elements is: " + ArrayGenerator.elementCount);

        // TODO: Print the percentage of counted elements for each thread
        // ATTEMPT TO IMPLEMENT THIS YOURSELF, IT'S A BIT TRICKY

    }

// TO DO: Make the SearchThread class a thread
// You can add methods or attributes if necessary 

static class CountThread extends Thread{

    private int[] arr; 
    private int target;
    private int localCount;

    public CountThread(int[] arr, int target) {
        this.arr = arr;
        this.target = target;
    }

    public void countElements() {
        for (int num: this.arr) {
            if (num == target) {
                count++;
            }
        }
    }

    
    public void countElementsParallel() {
        // TO DO: Implement and run the paralel counting method from the thread
        for (int num: this.arr) {
            if (num == target) {
                localCount++;
            }
        }

        lock.lock();
        count += localCount;
        lock.unlock();
    }

    public void run() {
        countElementsParallel();
    }
}

}

