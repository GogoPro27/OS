package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Client extends Thread{
    private String serverName;
    private int port;

    public Client(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            socket = new Socket(InetAddress.getByName(this.serverName),this.port);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            List<String> shortNames = Arrays.asList("ADS", "OS", "DBMS", "CN", "AI","wrong");
            Random random = new Random();
            String reqSubject = "/"+shortNames.get(random.nextInt(6));

            bufferedWriter.write(String.format("GET %s HTTP/1.1\n",reqSubject));
            bufferedWriter.write(String.format("User: Goc\n"));
            bufferedWriter.write("\n");
            bufferedWriter.flush();

            String line ;
            while ((line = bufferedReader.readLine())!=null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
        try {
            if (bufferedWriter != null) bufferedWriter.close();
            if (bufferedReader != null) bufferedReader.close();
            try {
                assert socket != null;
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }

    public static void main(String[] args) {
        String serverName = System.getenv("SERVER_NAME");
        int serverPort= Integer.parseInt(System.getenv("SERVER_PORT"));
        Client client = new Client(serverName,serverPort);
        client.start();
    }
}
