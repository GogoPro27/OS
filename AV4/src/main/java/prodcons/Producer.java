package main.java.prodcons;

import java.util.concurrent.Semaphore;

public class Producer extends Thread {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void execute() throws InterruptedException {
        ProducerConsumer.isEmpty.acquire();
        ProducerConsumer.lock.lock();
        buffer.fillBuffer();
        ProducerConsumer.lock.unlock();
        for (int i = 0; i < ProducerConsumer.semaphores.length; i++) {
            ProducerConsumer.semaphores[i].release();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < ProducerConsumer.NUM_RUNS; i++) {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
