package main.java;

import java.util.concurrent.Semaphore;

public class SiO2 {

    public static int NUM_RUN = 50;
    // define necessary synchronization structures
    static Semaphore siForm;
    static Semaphore oForm;
    static Semaphore siHere;
    static Semaphore oHere;
    static Semaphore ready;

    public static void init() {
        // initialize synchronization structures
        siForm = new Semaphore(1);
        oForm = new Semaphore(2);
        siHere = new Semaphore(0);
        oHere = new Semaphore(0);
        ready = new Semaphore(0);
    }

    public static class Si extends Thread {

        public void bond() {
            System.out.println("Si is bonding now.");
        }

        @Override
        public void run() {
            for (int i=0;i<NUM_RUN;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void execute() throws InterruptedException {
            // synchronize call to bond() from Si
            siForm.acquire();
            siHere.release(2);
            oHere.acquire(2);
            ready.release(2);
            bond();
            siForm.release();
        }

    }

    public static class O extends Thread {

        public void execute() throws InterruptedException {
            // synchronize call to bond() from Si
            oForm.acquire();
            siHere.acquire();
            oHere.release();
            ready.acquire();
            bond();
            oForm.release();
        }

        public void bond() {
            System.out.println("O is bonding now.");
        }


        @Override
        public void run() {
            for (int i=0;i<NUM_RUN;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

