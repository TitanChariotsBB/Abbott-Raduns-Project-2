import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HTTPServer {
    public static final int PORT = 80;
    public static final String IP = "127.0.0.1";
    public static final String CRLF = "\r\n";
    public static final String EOH = CRLF + CRLF;

    public static void main(String[] args){

        System.out.println("server is listening to port 80");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("get connection from IP: " + socket.getRemoteSocketAddress());

                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                // good to handle strings from stream
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(dataInputStream));
                String line = bufferedReader.readLine();
//                while(!line.isEmpty()){
//                    System.out.println(line);
//                    line  = bufferedReader.readLine();
//                }

                String fileName = line.replace("GET /","").replace(" HTTP/1.1","");
                if (fileName.isEmpty()) fileName = "index.html";
                System.out.println(fileName);

                System.out.println("receive cmd: " + line);

                // send back a response
                // generate a HTTP request a print writer, handy to handle output stream
                String response = "Hello World HTTP!";
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dataOutputStream, StandardCharsets.ISO_8859_1));
                printWriter.print("HTTP/1.1 200 OK" + CRLF);
                printWriter.print("Date: Sun, 10 Apr 2022 21:44:57 GMT " + CRLF);
                printWriter.print("Cache-Control: private, max-age=0" + CRLF);
                printWriter.print("Content-Type: text/html; charset=ISO-8859-1" + CRLF);
//                printWriter.print("Content-Length: " + response.length() + EOH);
//                printWriter.print(response);
//                printWriter.flush();

                // get the file
                File file = new File("./server_folder/" + fileName);
                if (!file.exists()) file = new File("./server_folder/404.html");
                response = new String(Files.readAllBytes(file.toPath()), StandardCharsets.ISO_8859_1);
                printWriter.print("Content-Length: " + response.length() + EOH);
                printWriter.print(response + EOH);
                printWriter.flush();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
