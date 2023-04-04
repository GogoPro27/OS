package main.java;

import java.util.concurrent.Semaphore;

public class Pushachi {

    static Semaphore accessTable;
    static Semaphore emptyTable;
    static Semaphore[] wait;
    static boolean[] waiting;

    static void init() {
        accessTable = new Semaphore(0);
        emptyTable = new Semaphore(1);
        wait = new Semaphore[3];
        waiting = new boolean[3];
        for (int i = 0; i < 3; i++) {
            wait[i] = new Semaphore(0);
        }
    }

    public static class Table{

        private int type = -1;

        public void putItems() {
            this.type = (int)Math.round(Math.random()*2);
            System.out.printf(
                    "Agent added items, smoker with id %d can smoke...%n",
                    type
            );
        }

        public void consume(int type) {
            System.out.printf("Smoker with id %d consuming...\n", type);
        }

        public boolean hasMyItems(int type) {
            return (this.type==type);
        }

    }

    public static class Agent extends Thread{

        private Table table = null;
        private boolean firstTime = true;
        public Agent(Table table) {
            this.table = table;
        }

        public void execute() throws InterruptedException {
            emptyTable.acquire();
            this.table.putItems();
            for (int i = 0; i < 3; i++) {
                if (waiting[i]) {
                    waiting[i] = false;
                    wait[i].release();
                }
            }
            accessTable.release();
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 100; i++) {
                    execute();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Pushac extends Thread{

        private final int type;
        private final Table table;
        public Pushac(int type, Table table) {
            this.type = type;
            this.table = table;
        }

        public void execute() throws InterruptedException {
            accessTable.acquire();
            if (this.table.hasMyItems(type)) {
                this.table.consume(type);
                emptyTable.release();
            } else {
                waiting[type]=true;
                accessTable.release();
                wait[type].acquire();
            }   
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 100; i++) {
                    execute();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        init();

        Table sharedTable = new Table();
        Thread pushachTutun = new Pushac(0, sharedTable);
        Thread pushachRizla = new Pushac(1,sharedTable);
        Thread pushachKibrit = new Pushac(2, sharedTable);
        Agent agent = new Agent(sharedTable);
        
        pushachTutun.start();
        pushachRizla.start();
        pushachKibrit.start();
        agent.start();
    }
}

