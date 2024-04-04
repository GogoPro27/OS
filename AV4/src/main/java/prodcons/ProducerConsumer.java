package main.java.prodcons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ProducerConsumer {
    static final int NUM_RUNS = 100;
    static final int NUM_CONSUMERS = 50;

    // TO DO: Define synchronization elements

    static Semaphore[] semaphores;
    static Semaphore isEmpty;
    static Lock lock;


    public static void init(int numConsumers) {
        semaphores=new Semaphore[NUM_CONSUMERS];
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            semaphores[i] = new Semaphore(1);
        }
        isEmpty = new Semaphore(1);
        lock = new ReentrantLock();

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
