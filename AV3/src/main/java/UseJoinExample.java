public class UseJoinExample {
    public static void main(String[] args) throws InterruptedException {
        Count c = new Count();
        c.start();
        
        try {
            c.join();
            System.out.println("Result = " + c.getResult());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main done");
    }
}

class Count extends Thread {
    private long result = 0;

    public void run() {
        result = count();
    }

    public long getResult() {
        return result;
    }

    public long count() {
        long tmp = 0;
        for (tmp = 0; tmp < 100000; tmp++) {
            
        }
        return tmp;
    }
}