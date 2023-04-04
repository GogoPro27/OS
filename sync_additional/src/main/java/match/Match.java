package main.java.match;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Match {
    public static void main(String[] args) {
        MatchTest u = new MatchTest();
        try {
            u.simulate();
        } catch (InterruptedException e) {
            System.out.println("NOT OK");
        }
    }

}

class MatchTest {

    private static int counterCPB = 0;
    private static int counterMZT = 0;

    private static Lock lock = new ReentrantLock();

    private static Semaphore fanCPBCanEnter = new Semaphore(0);
    private static Semaphore fanMZTCanEnter = new Semaphore(0);

    private static Semaphore isSeated = new Semaphore(0);
    private static Semaphore canExit = new Semaphore(0);

    private static class Taxi {

        void seatFanCPB() throws InterruptedException {
            boolean isLast = false;
            // passengerCount
            lock.lock();
            counterCPB++;
            personHere();
            if(counterCPB == 2 && counterMZT >= 2) {
                // notify waiting threads to get into the taxi
                isLast = true;
                counterCPB -= 2;
                counterMZT -= 2;
                fanCPBCanEnter.release();
                fanMZTCanEnter.release(2);
            } else if(counterCPB == 4) {
                // notify waiting threads to get into the taxi
                isLast = true;
                counterCPB -=4;
                fanCPBCanEnter.release(3);
            }
            else {
                lock.unlock();
                fanCPBCanEnter.acquire();
            }

            seated();

            if(isLast) {
                isSeated.acquire(3);
                drive();
                canExit.release(4);
                lock.unlock();
            }
            else {
                isSeated.release();
            }
            canExit.acquire();
            exit();
        }

        void seatFanMZT() throws InterruptedException {
            boolean isLast = false;
            lock.lock();
            counterMZT++;
            personHere();
            if(counterCPB >= 2 && counterMZT == 2) {
                // notify waiting threads to get into the taxi
                isLast = true;
                counterCPB -= 2;
                counterMZT -= 2;
                fanMZTCanEnter.release();
                fanCPBCanEnter.release(2);
            }else if(counterMZT == 4) {
                // notify waiting threads to get into the taxi
                isLast = true;
                counterMZT -=4;
                fanMZTCanEnter.release(3);
            }
            else {
                lock.unlock();
                fanMZTCanEnter.acquire();
            }

            seated();

            if(isLast) {
                isSeated.acquire(3);
                drive();
                canExit.release(4);
                lock.unlock();
            }
            else {
                isSeated.release();
            }
            canExit.acquire();
            exit();
        }

        void personHere() {
            System.out.println("====HERE: " + Thread.currentThread().getName());
            System.out.flush();
        }

        void seated() {
            System.out.println("====SEATED: " + Thread.currentThread().getName());
            System.out.flush();
        }

        void drive() {
            System.out.println("====DRIVE: " + Thread.currentThread().getName());
        }

        void exit() {
            System.out.println("====EXITING: " + Thread.currentThread().getName());
        }

    }

    private static class FanCPB extends Thread {
        private final Taxi taxi;

        public FanCPB(Taxi taxi) {
            this.taxi = taxi;
        }

        @Override
        public void run() {
            try {
                taxi.seatFanCPB();
            }catch (InterruptedException e) {
                System.out.println("===ERROR====");
            }

        }
    }

    private static class FanMZT extends Thread {
        private final Taxi taxi;

        public FanMZT(Taxi taxi) {
            this.taxi = taxi;
        }

        @Override
        public void run() {
            try {
                taxi.seatFanMZT();
            }catch (InterruptedException e) {
                System.out.println("===ERROR====");
            }
        }
    }

    void simulate() throws InterruptedException {
        Taxi taxi = new Taxi();

        HashSet<Thread> threads = new HashSet<>();

        for(int i = 0; i < 14; i++) {
            FanCPB fanCPB = new FanCPB(taxi);
            fanCPB.setName("FanCPB_" + i);
            threads.add(fanCPB);
        }

        for(int i = 0; i < 10; i++) {
            FanMZT fanMZT = new FanMZT(taxi);
            fanMZT.setName("FanMZT_" + i);
            threads.add(fanMZT);
        }

        for(Thread t: threads) {
            t.start();
        }
        for(Thread t: threads) {
            t.join(1000);
        }

        boolean hasDeadlock = false;
        for(Thread t: threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("Deadlock");
                hasDeadlock = true;
            }
        }

        if(!hasDeadlock)
            System.out.println("Synced successfully");
    }

}
