package main.java.prodcons;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProducerConsumer {
    static final int NUM_RUNS = 100;
    static final int NUM_CONSUMERS = 50;

    // TO DO: Define synchronization elements
    public static Semaphore isBufferEmpty;
    public static Semaphore[] items;
    public static Lock lock;

    public static void init(int numConsumers) {
        // TO DO: Initialize synchronization elements
        isBufferEmpty = new Semaphore(1);
        lock = new ReentrantLock();
        items = new Semaphore[NUM_CONSUMERS];
        for (int i = 0; i < NUM_CONSUMERS; i ++) {
            items[i] = new Semaphore(0);
        }
    }

    public static void main(String[] args) {
        init(NUM_CONSUMERS);

        Buffer sharedBuffer = new Buffer(NUM_CONSUMERS);
        Producer p = new Producer(sharedBuffer);
        p.start();

        List<Consumer> consumers = new ArrayList<>();
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers.add(new Consumer(i,sharedBuffer));
        }

        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers.get(i).start();
        }
    }
}
