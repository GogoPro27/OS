package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class Client extends Thread {

    private int serverPort;

    public Client(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket;
        Random random = new Random();

        try {
            socket = new Socket(InetAddress.getLocalHost(), serverPort);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bw.write((random.nextInt(10) % 2 == 0 ? "GET" : "POST") +
                    " /movies/" + random.nextInt(100) + " HTTP/1.1\n");
            bw.write("User: FINKI\n");
            bw.write("\n");
            bw.flush();

            String line = "/";
            while ((line = br.readLine()) != null)
                System.out.println("Client received: " + line);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client(7000);
        client.start();
    }
}
