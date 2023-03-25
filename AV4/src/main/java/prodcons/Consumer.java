package main.java.prodcons;

class Consumer extends Thread {

    private Buffer buffer;
    private int consumerId;

    public Consumer(int consumerId, Buffer buffer) {
        this.buffer = buffer;
        this.consumerId = consumerId;
    }

    public void execute() throws InterruptedException {
        // TO DO: Implement item taking logic
        ProducerConsumer.items[consumerId].acquire();
        this.buffer.getItem(consumerId);

        ProducerConsumer.lock.lock();
        buffer.decrementNumberOfItemsLeft(); // items--;
        if (buffer.isBufferEmpty()) { // items == 0;
            ProducerConsumer.isBufferEmpty.release();
        }
        ProducerConsumer.lock.unlock();
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
