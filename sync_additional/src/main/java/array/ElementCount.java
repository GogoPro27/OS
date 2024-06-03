package main.java.array;

import main.java.array.BoundedRandomGenerator;

import java.util.Arrays;

public class ElementCount {

    public static long count = 0;

    static final BoundedRandomGenerator random = new BoundedRandomGenerator();

    private static final int ARRAY_LENGTH = 1000000;

    private static final int SEARCH_TARGET = random.nextInt();  // This is the target element we are looking for

    // TODO: Define and initialize sychronization elements

        // DO NOT CHANGE
    public static int[] getSubArray(int[] array, int start, int end) {
        return Arrays.copyOfRange(array, start, end);
    }

    public static void main(String[] args) {
        
        int[] arr = ArrayGenerator.generate(ARRAY_LENGTH, SEARCH_TARGET);

        // TODO: Make the CountThread class a thread and start 10 instances
        // Each instance should take a subarray from the original array with equal length

        CountThread searchThread = new CountThread(arr, SEARCH_TARGET);

        // TODO: Start the 10 threads

        searchThread.countElements();

        // TODO: Check if the threads have finished and interrupt any running thread


        // TODO: Print the percentage of counted elements for each thread


        // DO NOT CHANGE

        System.out.println("The number of total counted elements is: " + count);
        System.out.println("The generated number of elements is: " + ArrayGenerator.elementCount);

    }
    
// TO DO: Make the SearchThread class a thread
static class CountThread {

    private int[] arr; 
    private int target;

    public CountThread(int[] arr, int target) {
        this.arr = arr;
        this.target = target;
    }

    public void countElements() {
        for (int num: this.arr) {
            if (num == this.target) {
                count++;
            }
        }
    }

    public void countElementsParallel() {
        // TO DO: Implement and run this method from the thread
    }
}

}

