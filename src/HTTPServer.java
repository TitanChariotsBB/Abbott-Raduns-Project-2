/** Authors: Christian Abbott, Ben Raduns
 *  Course: COMP 342
 *  Date: May 7th, 2023
 *  Description: Server side of the socket-based HTTP protocol
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HTTPServer {
    public static final int PORT = 80;
    public static final String IP = "127.0.0.1"; //For Reference
    public static final String CRLF = "\r\n";
    public static final String EOH = CRLF + CRLF;

    public static void main(String[] args){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
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

                String fileName = line.replace("GET /","").replace(" HTTP/1.1","");
                if (fileName.isEmpty()) fileName = "index.html";
                System.out.println(fileName);

                System.out.println("receive cmd: " + line);

                // send back a response
                // generate a HTTP request a print writer, handy to handle output stream
                String response = "Hello World HTTP!";
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dataOutputStream, StandardCharsets.ISO_8859_1));
                printWriter.print("HTTP/1.1 200 OK" + CRLF);
                printWriter.print("Date: " + dateFormat.format(date) + " GMT" + CRLF);
                printWriter.print("Cache-Control: private, max-age=0" + CRLF);
                printWriter.print("Content-Type: text/html; charset=ISO-8859-1" + CRLF);

                // get the file
                File file = new File("./server_folder/" + fileName);
                if (!file.exists()) {
                    file = new File("./server_folder/404.html");
                }
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
