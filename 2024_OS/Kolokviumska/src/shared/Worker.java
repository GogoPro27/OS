package shared;

import java.io.*;
import java.net.Socket;

public class Worker extends Thread{
    private File logger;
    private File counter;
    private Socket socket;

    public Worker(Socket socket,File logger, File counter) {
        this.logger = logger;
        this.counter = counter;
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logger,true)));

            incrementCounterFile();

            String line = null;
            while ((line = bufferedReader.readLine())!=null) {
                bufferedWriter.append(line).append("\n");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (bufferedWriter != null) bufferedWriter.close();
                if (bufferedReader != null) bufferedReader.close();
                try {
                    this.socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void incrementCounterFile() throws InterruptedException, IOException {
        RandomAccessFile raf = new RandomAccessFile(counter,"rw");
        ServerSharedResources.semaphore.acquire();

        Integer ctr = 0;
        try {
            ctr = raf.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        raf.seek(0);
        raf.writeInt(++ctr);
        System.out.println("Total clients by now: "+ctr);
        raf.close();
        ServerSharedResources.semaphore.release();
    }
}
