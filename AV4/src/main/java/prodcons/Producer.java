package main.java.prodcons;

public class Producer extends Thread {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void execute() throws InterruptedException {
        // TO DO: Implement buffer filling logic with synchronization
        ProducerConsumer.isBufferEmpty.acquire();
        
        ProducerConsumer.lock.lock();
        buffer.fillBuffer();
        ProducerConsumer.lock.unlock();

        for (int i=0; i<ProducerConsumer.NUM_CONSUMERS; i++) {
            ProducerConsumer.items[i].release();
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
