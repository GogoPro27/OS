package main.java.molecule;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class C2H2O4 {
    static Semaphore cForm;
    static Semaphore hForm;
    static Semaphore oForm;

//    static Semaphore cHere; // SOLUTION 2
//    static Semaphore hHere; // SOLUTION 2
//    static int totalOxygen; // SOLUTION 2
    static Semaphore ready;
    static Semaphore finished;

    static int totalAtoms;
    static Lock lock;


    static State state = new State();

    static void init() {

        oForm = new Semaphore(4);
        hForm = new Semaphore(2);
        cForm = new Semaphore(2);

//        cHere = new Semaphore(0); // SOLUTION 2
//        hHere = new Semaphore(0); // SOLUTION 2
//        totalOxygen = 0; // SOLUTION 2

        ready = new Semaphore(0);
        finished = new Semaphore(0);

        totalAtoms = 0;
        lock = new ReentrantLock();

    }

    static class O extends Thread {

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void execute() throws InterruptedException {
            // SOLUTION 2
//            oForm.acquire();
//
//            lock.lock();
//            totalOxygen++;
//            if (totalOxygen == 4) {
//                hHere.acquire(2);
//                cHere.acquire(2);
//                ready.release(8);
//            }
//            lock.unlock();
//            ready.acquire();
//            state.bond("Oxygen");
//
//            finished.release();
//
//            lock.lock();
//            totalOxygen--;
//            if (totalOxygen == 0) {
//                finished.acquire(8);
//                state.validate();
//                oForm.release(4);
//                cForm.release(2);
//                hForm.release(2);
//            }
//            lock.unlock();


            // SOLUTION 1 - EASIER
            oForm.acquire(); // 4
            lock.lock();
            totalAtoms++;
            if(totalAtoms == 8) {
                // notify other atoms that are waiting to bond
                ready.release(8);
            }
            lock.unlock();

            ready.acquire();
            state.bond("Oxygen");

            lock.lock();
            totalAtoms--;
            if(totalAtoms == 0) {
                state.validate();
                finished.release(8);

            }
            lock.unlock();

            finished.acquire();
            oForm.release();
        }

    }

    static class C extends Thread {
        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void execute() throws InterruptedException {
            // SOLUTION 2
//            cForm.acquire();
//            cHere.release();
//            ready.acquire();
//            state.bond("Carbon");
//            finished.release();

            // SOLUTION 1 - EASIER
            cForm.acquire();
            lock.lock();
            totalAtoms++;
            if(totalAtoms == 8) {
                // notify other atoms that are waiting to bond
                ready.release(8);

            }
            lock.unlock();

            ready.acquire();
            state.bond("Carbon");

            lock.lock();
            totalAtoms--;
            if(totalAtoms == 0) {
                state.validate();
                finished.release(8);

            }
            lock.unlock();

            finished.acquire();
            cForm.release();
        }
    }

    static class H extends Thread {

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void execute() throws InterruptedException {
            // SOLUTION 2
//            hForm.acquire();
//            hHere.release();
//            ready.acquire();
//            state.bond("Hydrogen");
//            finished.release();


            // SOLUTION 1 - EASIER
            hForm.acquire();
            lock.lock();
            totalAtoms++;
            if(totalAtoms == 8) {
                // notify other atoms that are waiting to bond
                ready.release(8);

            }
            lock.unlock();

            ready.acquire();
            state.bond("Hydrogen");

            lock.lock();
            totalAtoms--;
            if(totalAtoms == 0) {
                state.validate();
                finished.release(8);

            }
            lock.unlock();

            finished.acquire();
            hForm.release();
        }

    }

    static class State {

        public void bond(String type) {
            System.out.println("Bond from " + type);
        }

        public void validate() {
            if (
                    oForm.availablePermits() != 0 ||
                    hForm.availablePermits() != 0 ||
                    cForm.availablePermits() != 0 ||
                    finished.availablePermits() != 0 ||
                    totalAtoms != 0
            ) {
                throw new RuntimeException("Invalid molecule");
            }
            System.out.println("Validate");
        }
    }

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new O());
            threads.add(new O());
            threads.add(new H());
            threads.add(new C());
        }

        init();

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
