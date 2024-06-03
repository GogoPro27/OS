package first;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Worker extends Thread{
    private Socket socket;
    private File file;

    public Worker(Socket socket, File file) {
        this.socket = socket;
        this.file = file;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            int counter = 0;
            bw.write("HELLO "+socket.getInetAddress().getHostAddress()+ "\n");
            bw.flush();

            while (true){
                String received = br.readLine();
                if (counter==0){
                    String[] parts = received.split(" ");
                    if(parts.length!=2)throw new IOException();
                    if (!parts[0].equals("HELLO"))throw new IOException();
                    int port = Integer.parseInt(parts[1]);
                    //ova kje throwne exception ako ne e brojka tkda taman
//                    if (port!=socket.getPort())throw new IOException();
                    bw.write("SEND DAILY DATA\n");
                    bw.flush();

                }else if(counter==1){
                    String[] parts = received.split(",");
                    if(parts.length!=4)throw new IOException();
                    String[] date = parts[0].split("/");

                    Arrays.stream(parts).skip(1).forEach(i-> {
                        try {
                            verify(i);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    Arrays.stream(date).forEach(i-> {
                        try {
                            verify(i);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    Server.logInFile(received);
                    bw.write("OK\n");
                    bw.flush();
                }else {
                    if(!received.equals("QUIT"))throw new RuntimeException();
                    bw.write("");
                    break;
                }

                counter++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (br != null) br.close();
                if (bw != null) bw.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean verify(String numberString) throws IOException,NumberFormatException {
        int parsed = Integer.parseInt(numberString);
        if(parsed<0) throw new IOException();
        return true;
    }
}
