package tcp_myTry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static final int NUM_CLIENTS = 50;
    public static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {
        Server server = new Server(7001);
        server.start();
        List<Client> clients = IntStream.range(0,NUM_CLIENTS).
                mapToObj(i->new Client(7001,i))
                .toList();
        clients.forEach(Thread::start);

    }
}
