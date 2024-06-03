package tcp_moj_nacin;

import java.util.concurrent.Semaphore;

public class Main {
    public static final int NUM_CLIENTS = 1;
    public static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {
        Server server = new Server(7001);
        server.start();
//        List<Client> clients = IntStream.range(0,NUM_CLIENTS).
//                mapToObj(i->new Client(7001,i))
//                .toList();
//        clients.forEach(Thread::start);
        Client client = new Client(7001,1);
        client.start();

    }
}
