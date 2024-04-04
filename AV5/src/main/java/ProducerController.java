package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerController {

    public static int NUM_RUN = 50;

    static Semaphore canCheck ;
    static Lock lock ;
    static Semaphore accessBuffer;
    static int num_checks = 0;

    public static void init() {
        canCheck = new Semaphore(10);
        lock = new ReentrantLock();
        accessBuffer = new Semaphore(1);
    }

    public static class Buffer {

        private boolean producing = false;
        private int checkingCount = 0;

        public void produce() {
            producing = true;
            if (checkingCount > 0) {
                throw new RuntimeException("Can't produce if controllers checking");
            }

            System.out.println("Producer is producing...");

            producing = false;
        }

        public synchronized void check() {
            checkingCount++;

            if (producing) {
                throw new RuntimeException("Can't check if producer is producing");
            }

            if (checkingCount > 10) {
                throw new RuntimeException(
                        "No more than 10 checks can be in progress simultaneously"
                );
            }

            System.out.println("Controller is checking...");

            checkingCount --;
        }
    }

    public static class Producer extends Thread {
        private final Buffer buffer;

        public Producer(Buffer b) {
            this.buffer = b;
        }

        public void execute() throws InterruptedException {
            accessBuffer.acquire();
            buffer.produce();
            accessBuffer.release();
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Controller extends Thread {

        private final Buffer buffer;
        private static int ctr = 0;

        public Controller(Buffer buffer) {
            this.buffer = buffer;
        }

        public void execute() throws InterruptedException {

            lock.lock(); //ovoj lock e za numchecks !!!
                if (num_checks == 0){
                    accessBuffer.acquire();  // proveruva dali mozhe da vleze kontrolerot ako e prv
                }//koga kje vleze
                num_checks++;
            lock.unlock();

            canCheck.acquire(); // mora da bidat 10 odednash samo

            buffer.check(); //vrsham rabota
            lock.lock();
            num_checks--;

            canCheck.release();

            if(num_checks == 0){
                accessBuffer.release();
            }
            lock.unlock();

        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        Producer p = new Producer(buffer);
        List<Controller> controllers = new ArrayList<>();
        init();
        for (int i = 0; i < 100; i++) {
            controllers.add(new Controller(buffer));
        }

        p.start();
        controllers.forEach(Thread::start);

    }

}
