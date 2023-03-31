package main.java;

import java.util.concurrent.Semaphore;

public class UpisiFinki {

    static int NUM_STUDENTS = 10;
    static Semaphore slobodnoMestoKomisija;
    static Semaphore studentVlezi;
    static Semaphore ostavivDokumenti;
    static Semaphore odiSiDoma;


    public static void init() {
        slobodnoMestoKomisija = new Semaphore(4);
        studentVlezi = new Semaphore(0);
        ostavivDokumenti = new Semaphore(0);
        odiSiDoma = new Semaphore(0);
    }

    public static class Clen extends Thread{

        public void execute() throws InterruptedException {
            slobodnoMestoKomisija.acquire(); // 4
            int brStudenti = 10;
            for(int i = 0; i < brStudenti; i++) {
                studentVlezi.release();
                ostavivDokumenti.acquire();
                zapisi();
                odiSiDoma.release();
            }
            slobodnoMestoKomisija.release();
        }

        public void zapisi() {
            System.out.println("Zapisuvam student...");
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Student extends Thread{

        public void execute() throws InterruptedException {
            studentVlezi.acquire();
            ostaviDokumenti();
            ostavivDokumenti.release();
            odiSiDoma.acquire();
        }

        public void ostaviDokumenti() {
            System.out.println("Ostavam dokumenti...");
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

