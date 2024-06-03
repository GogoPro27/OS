package tcp_moj_nacin;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class Client extends Thread{
    private int serverPort;
    private int id;

    public Client(int serverPort, int id) {
        this.serverPort = serverPort;
        this.id = id;
    }

    @Override
    public void run() {
        Socket socket = null;
        Random random = new Random();
        try {
            Main.semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            socket = new Socket(InetAddress.getLocalHost(),serverPort);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            bw.write((random.nextInt(10) % 2 == 0 ? "GET" : "POST") +
                    " /movies/" + random.nextInt(100) + " HTTP/1.1\n");
            bw.write("User: FINKI_"+id+"\n");
            bw.write("\n");
            bw.flush(); //todo ne zaboravaj



            System.out.println("Client "+id+" recieved:");
            String line;
            while (!(line=br.readLine()).isEmpty()) {
                System.out.print(line);
            }
            bw.close();
            br.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
