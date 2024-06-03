package tcp_messaging_plus_stuff;

import java.io.*;
import java.net.Socket;

public class Worker extends Thread{
    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bw.write("Log in sir...\n");
            bw.flush();
            String line;
            while ((line = br.readLine())!=null){
                bw.write("echo: "+line+"\n");
                bw.flush();
            }
            bw.write("Goodbye!");
            bw.flush();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                socket.close();
                if (br != null) br.close();
                if (bw != null) bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
