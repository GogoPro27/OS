package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TCPClient extends Thread{
    private String serverName;
    private int serverPort;
    private boolean isFirst;
    public static final List<String> WORD_LIST = List.of(
            "apple", "banana", "cherry", "date", "elderberry",
            "fig", "grape", "honeydew", "kiwi", "lemon", "mango",
            "strawberry", "tangerine", "watermelon", "logout");

    public TCPClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        isFirst = true;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(serverName),serverPort);
            System.out.println("You are connected, log in!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        Scanner scanner = new Scanner(System.in);

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            while (true){
//                String to_send = scanner.nextLine();
                String to_send;
                Random random = new Random();
                int num = random.nextInt(WORD_LIST.size());
                if (isFirst){
                    int chance = random.nextInt(10);
                    to_send = chance<8?"login":WORD_LIST.get(num);
                    isFirst=false;
                }else {
                    to_send = WORD_LIST.get(num);
                }
                writer.write(to_send+"\n");
                writer.flush();
                String received = reader.readLine();
                System.out.println("RECIEVED: " + received);
                if (received.equals("logged out!") || received.contains("disconnected"))break;
                Thread.sleep(random.nextInt(2000)+500);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
//        List<TCPClient> clients = IntStream.range(0,10).mapToObj(i->new TCPClient("localhost",7100)).toList();
//        clients.forEach(Thread::start);
        String serverName = System.getenv("SERVER_NAME");
        int serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        TCPClient client = new TCPClient(serverName,serverPort);
        client.start();
    }
}

