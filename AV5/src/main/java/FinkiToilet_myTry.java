package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FinkiToilet_myTry {

    public static class Toilet {

        boolean isManInside = false;
        boolean isWomanInside = false;

        public void enter(String gender) {
            if (gender.equals("man")) {
                isManInside = true;
            }
            else {
                isWomanInside = true;
            }

            if (isWomanInside && isManInside) {
                throw new RuntimeException(
                        "Two different genders inside toilet."
                );
            }

            System.out.printf("Entering %s...%n", gender);
        }

        public void exit(String gender) {

            if (isWomanInside && isManInside) {
                throw new RuntimeException(
                        "Two different genders inside toilet."
                );
            }

            if (gender.equals("man")) {
                if (isWomanInside) {
                    throw new RuntimeException(
                            "Man can't call exit if woman is inside."
                    );
                }
                isManInside = false;
            } else {
                if (isManInside) {
                    throw new RuntimeException(
                            "Woman can't call exit if man is inside."
                    );
                }
                isWomanInside = false;
            }
            System.out.printf("Exiting %s...%n", gender);
        }
    }

    static Semaphore toiletAccess;
    static Lock lock_m;
    static Lock lock_z;

    private static int m_x = 0;
    private static int z_x = 0;

    public static void init() {
        toiletAccess = new Semaphore(1);
        lock_m = new ReentrantLock();
        lock_z = new ReentrantLock();
    }

    public static class Man extends Thread{

        private final Toilet toilet;


        public Man(Toilet toilet) {
            this.toilet = toilet;
        }

        public void manEnter() throws InterruptedException {
            lock_m.lock();
            if(m_x == 0){
                toiletAccess.acquire();
            }
            m_x++;
            toilet.enter("man");
            lock_m.unlock();

        }

        public void manExit() throws InterruptedException {
            lock_m.lock();
            m_x--;
            if(m_x == 0){
                toiletAccess.release();
            }
            toilet.exit("man");
            lock_m.unlock();

        }

        @Override
        public void run() {
            try {
                manEnter();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                manExit();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Woman extends Thread{

        private final Toilet toilet;

        public Woman(Toilet toilet) {
            this.toilet = toilet;
        }

        public void womanEnter() throws InterruptedException {
            lock_z.lock();
            if(z_x == 0){
                toiletAccess.acquire();
            }
            z_x++;
            toilet.enter("woman");
            lock_z.unlock();

        }

        public void womanExit() throws InterruptedException {
            lock_z.lock();
            z_x--;
            if(z_x == 0){
                toiletAccess.release();
            }
            toilet.exit("woman");
            lock_z.unlock();

        }

        @Override
        public void run() {
            try {
                womanEnter();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                womanExit();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        init();
        Toilet sharedToilet = new Toilet();
        List<Thread> men = new ArrayList<>();
        List<Thread> women = new ArrayList<>();

        for (int i = 0; i < 50; i ++) {
            men.add(new Man(sharedToilet));
            women.add(new Woman(sharedToilet));
        }

        men.forEach(Thread::start);
        women.forEach(Thread::start);
    }
}

