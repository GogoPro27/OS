package udp;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class UDPClient extends Thread{
    private byte[] buffer = new byte[256];
    private DatagramSocket socket;
    private int serverPort;
    private InetAddress inetAddress;
    private boolean isFirst;
    public static final List<String> WORD_LIST = List.of(
            "apple", "banana", "cherry", "date", "elderberry",
            "fig", "grape", "honeydew", "kiwi", "lemon", "mango",
            "strawberry", "tangerine", "watermelon", "logout");

    public UDPClient(int serverPort, String serverName) {
        this.serverPort = serverPort;
        isFirst = true;
        try {
            this.inetAddress = InetAddress.getByName(serverName);
            socket = new DatagramSocket();
            System.out.println("Client Socket is created...");
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Log in...");

//        while ((line = scanner.nextLine())!=null){
        while (true){
            String to_send="";
            Random random = new Random();
            int num = random.nextInt(WORD_LIST.size());
            if (isFirst){
                int chance = random.nextInt(10);
                to_send = chance<8?"login":WORD_LIST.get(num);
                isFirst=false;
            }else {
                to_send = WORD_LIST.get(num);
            }

            buffer = to_send.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,inetAddress,serverPort);
            try {
                socket.send(packet);
                buffer = new byte[256];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String received =new String( packet.getData(),0, buffer.length).trim();
                System.out.println("TCP_CLIENT RECEIVED: "+ received);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (to_send.equals("logout")){
                socket.close();
                break;
            }
            try {
                Thread.sleep(random.nextInt(2000)+500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
//        List<UDP_Client> clients = IntStream.range(0,10).mapToObj(i->new UDP_Client(4445,"localhost")).toList();
//        clients.forEach(Thread::start);
        String serverName = System.getenv("SERVER_NAME");
        int serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        UDPClient client = new UDPClient(serverPort,serverName);
        client.start();
    }
}
