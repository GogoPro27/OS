package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Worker extends Thread{
    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            WebRequest request = WebRequest.webRequestBuilder(bufferedReader);

            List<String> toLog = new ArrayList<>();
            toLog.add(String.format("Client with address %s:%d requested for: %s",socket.getInetAddress().getHostAddress(),socket.getPort(),request.path));

            String reply="";
            Subject subject = request.searchedFor();
            if (subject == null){
                reply = "HTTP/1.1 404 Not Found";
                bufferedWriter.write(reply+"\n");
                bufferedWriter.write("Content-type: text/html\n");
                bufferedWriter.write("\n");
                bufferedWriter.write("<h1>Couldn't find the subject you are looking for!<h1>\n");
                bufferedWriter.write("\n");
            }else {
                reply = "HTTP/1.1 200 OK";
                bufferedWriter.write(reply+"\n");
                bufferedWriter.write("Content-type: text/html\n");
                bufferedWriter.write("\n");
                bufferedWriter.write(String.format("<h1>%s<h1>\n",subject.getName()));
                bufferedWriter.write(String.format("<h3>Num students : %s<h3>\n",subject.getNumberOfStudents()));
                bufferedWriter.write("<h3>Prerequisite Subjects: <h3>\n");
                for(String s : subject.getPrerequisiteSubjects()){
                    bufferedWriter.write(String.format("<h4>    -%s<h4>\n",s));
                }
                bufferedWriter.write("\n");
            }
            bufferedWriter.flush();
            toLog.add("Server replied with: "+reply);
            log_info(toLog);


        } catch (IOException e) {
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
    public void log_info(List<String> logs) throws IOException {
        Socket sharedServerSocket = new Socket(InetAddress.getByName(Server.sharedServerName),Server.sharedServerPort);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sharedServerSocket.getOutputStream()));
        System.out.println("Sending the info to the logger...");
        for(String log : logs){
            bw.write(log+"\n");
        }
        bw.flush();
        socket.close();
        bw.close();
    }
    static class WebRequest{
        private String method;
        private String path;
        private String protocol;
        private Map<String,String> headers;

        public WebRequest(String method, String path, String protocol, Map<String, String> headers) {
            this.method = method;
            this.path = path;
            this.protocol = protocol;
            this.headers = headers;
        }

        public static WebRequest webRequestBuilder(BufferedReader br) throws IOException {
            List<String> lines = new ArrayList<>();
            String line;
            while (!(line = br.readLine()).isEmpty()){
                lines.add(line);
            }
            HashMap<String,String> map = new HashMap<>();
            String [] parts = lines.get(0).split("\\s+");
            lines.stream().skip(1).forEach(l->{
                String [] p = l.split(":");
                map.put(p[0].trim(),p[1].trim());
            });
            return new WebRequest(parts[0],parts[1].replace("/",""),parts[2],map);
        }

        public Subject searchedFor(){
            return Server.subjectsMap.getOrDefault(this.path,null);
        }

    }
}
