package main.java;

import java.util.concurrent.Semaphore;

public class Pushachi_myTry {


    static Semaphore releaseTutun;
    static Semaphore releaseRizla;
    static Semaphore releaseKibrit;
    static Semaphore goAhead;

    static void init() {
        goAhead = new Semaphore(1);
        releaseKibrit = new Semaphore(0);
        releaseRizla = new Semaphore(0);
        releaseTutun = new Semaphore(0);
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
            goAhead.acquire();
            table.putItems();
            int itemType = table.type;
            switch (itemType){
                case 0: {
                    releaseKibrit.release();
                    break;
                }
                case 1: {
                    releaseRizla.release();
                    break;
                }
                case 2: {
                    releaseTutun.release();
                    break;
                }
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

    public static class Pushac extends Thread{

        private final int type;
        private final Table table;
        public Pushac(int type, Table table) {
            this.type = type;
            this.table = table;
        }

        public void execute() throws InterruptedException {
            switch (type){
                case 0:{
                    releaseKibrit.acquire();
                    break;
                }
                case 1:{
                    releaseRizla.acquire();
                    break;
                }
                case 2:{
                    releaseTutun.acquire();
                    break;
                }
            }
            table.consume(type);
            goAhead.release();
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

