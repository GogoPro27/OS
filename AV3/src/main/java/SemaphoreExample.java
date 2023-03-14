import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LoginQueueSemaphore {

    private Semaphore semaphore;

    public LoginQueueSemaphore(int slotLimit) {
        semaphore = new Semaphore(slotLimit);
    }

    public void tryLogin() throws InterruptedException { 
        semaphore.acquire();
    }

    public void logout() {
        semaphore.release();
    }

    public int availableSlots() {
        return semaphore.availablePermits();
    }

}

class UserThread extends Thread {
    private String username;
    private LoginQueueSemaphore queue;
    private Lock lock = new ReentrantLock();

    public UserThread(String username, LoginQueueSemaphore queue) {
        this.username = username;
        this.queue = queue;
    }

    public void run() {
        lock.lock();
        System.out.println("Available slots: " + this.queue.availableSlots() + 
        " User: " + this.username);
        lock.unlock();
        
        try {
            this.queue.tryLogin();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("I have successfully logged in " + this.username);
        this.queue.logout();
        System.out.println("I have successfully logged out " + this.username);
    }
}

public class SemaphoreExample {
    public static void main(String[] args) {
        LoginQueueSemaphore queue = new LoginQueueSemaphore(8);
        List<UserThread> users = new ArrayList<UserThread>();

        for (int i = 0; i < 1000; i++) {
            users.add(new UserThread("user"+i, queue));
        }

        users.forEach(t -> t.start());

        users.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}