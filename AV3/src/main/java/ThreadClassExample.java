public class ThreadClassExample {
  public static void main(String[] args) throws InterruptedException {
	  T obj = new T();
    obj.start();
    //obj runs in parallel​
    //with the main thread​
    System.out.println("Hello from main!");
  }
}

class T extends Thread {
  public void run() {
	// thread's main logic​
    System.out.println("Hello from thread!");
  }
}