package tcp_myTry;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

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

            //TODO implement Http protocol
            WebRequest webRequest = WebRequest.WebRequestBuilder(bufferedReader);

            System.out.println("Server recieved : "+webRequest.method+ " " + webRequest.path+ " "+ webRequest.protocol);

            String htmlContent = "<html><body><h1>Hello World!</h1></body></html>";
            bufferedWriter.write("HTTP/1.1 200 OK\n");
            bufferedWriter.write("Content-Type: text/html\n");
            bufferedWriter.write("Content-Length: " + htmlContent.length() + "\n");
            bufferedWriter.write("\n"); // Separate headers from the body
            bufferedWriter.write(htmlContent);
            bufferedWriter.write("\n"); // End the response with a new line

            //TODO ne zaboravaj
            bufferedWriter.flush();
            //TODO ne zaboravaj

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                this.socket.close();
                assert bufferedWriter != null;
                bufferedWriter.close();
                bufferedReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class WebRequest{
        private String method;
        private String path;
        private String protocol;
        private Map<String,String> headers;

        private WebRequest(String method, String path, String protocol, Map<String, String> headers) {
            this.method = method;
            this.path = path;
            this.protocol = protocol;
            this.headers = headers;
        }

        public static WebRequest WebRequestBuilder(BufferedReader bufferedReader) throws IOException {
            List<String> lines = new ArrayList<>();
            String line;
            while (!(line = bufferedReader.readLine()).isEmpty()) {
               lines.add(line);
            }

            String[] first_line_split = lines.getFirst().split("\\s+");


            String method = first_line_split[0];
            String path = first_line_split[1];
            String protocol = first_line_split[2];

            HashMap<String,String> headers = (HashMap<String, String>) lines.stream().skip(1)
                    .map(l->l.contains(": ")?l.split(": ",2):l.split(";",2))
                    .filter(l->l.length==2)
                    .collect(Collectors.toMap(
                            l->l[0],
                            l->l[1]
                    ));
            return new WebRequest(method,path,protocol,headers);
        }
    }
}
