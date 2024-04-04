package main.java;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UpisiFinki_myTry {

    static int NUM_STUDENTS = 10;


    static Semaphore enter_as_clen;
    static Semaphore enter;
    static Semaphore poveli;
    static Semaphore done;
    public static void init() {
        enter_as_clen = new Semaphore(4);
        enter = new Semaphore(0);
        poveli = new Semaphore(0);
        done = new Semaphore(0);
    }

    public static class Clen extends Thread{

        public void execute() throws InterruptedException {
            enter_as_clen.acquire();

            for (int i = 0; i < 10; i++) {
                enter.release();
                poveli.acquire();
                zapisi();
                done.release();
            }

            enter_as_clen.release();
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
            enter.acquire();
            ostaviDokumenti();
            poveli.release();
            done.acquire();
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

    public static void main(String[] args) {
        init();
        ArrayList<Clen> clenovi =
                IntStream.range(0,50)
                        .mapToObj(i->new Clen())
                        .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Student> studenti =
                IntStream.range(0,200)
                        .mapToObj(i->new Student())
                        .collect(Collectors.toCollection(ArrayList::new));
        clenovi.forEach(Thread::start);
        studenti.forEach(Thread::start);
    }
}

