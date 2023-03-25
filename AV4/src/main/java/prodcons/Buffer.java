package main.java.prodcons;

public class Buffer {

    // how many items are in the buffer currently
    private int numItems = 0;

    // total capacity of the buffer
    private final int numConsumers;

    public Buffer(int numConsumers) {
        this.numConsumers = numConsumers;
    }

    public int getBufferCapacity() {
        return numConsumers;
    }

    public void fillBuffer() {
        if (numItems !=0 ) {
            throw new RuntimeException("The buffer is not empty!");
        }
        numItems=numConsumers;
        System.out.println("The buffer is full.");
    }

    public void decrementNumberOfItemsLeft() {
        if (numItems <= 0) {
            throw new RuntimeException("Can't get item, no items left in the buffer!");
        }
        numItems--;
    }

    public boolean isBufferEmpty() {
        return numItems==0;
    }

    public void getItem(int consumerId) {
        System.out.println(String.format("Get item for consumer with id: %d.", consumerId));
    }
}
