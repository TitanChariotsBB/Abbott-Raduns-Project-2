import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HTTPClient {
    public static final int PORT = 80;
    public static final String SERVER_ADDR = "127.0.0.1" ; // ""www.google.com";//"142.250.190.36";
    public static final String CRLF = "\r\n";
    public static final String EOH = CRLF + CRLF;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Address: ");
        String addr = in.nextLine();
        String file = "index.html";

        System.out.println("client is requesting ... ");
        try {
            Socket socket = new Socket(addr, PORT);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            // generate a HTTP request a print writer, handy to handle output stream
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dataOutputStream, StandardCharsets.ISO_8859_1));
            printWriter.print("GET / HTTP/1.1" + CRLF);
            printWriter.print("Host: " + SERVER_ADDR + CRLF);
            printWriter.print("Connection: close" + CRLF);
            printWriter.print("Accept: */*" + EOH);
            printWriter.flush();

            // try to receive the feedback
            System.out.println("After sending the request, wait for response: ");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
            String line = bufferedReader.readLine();
            while (!line.isEmpty()) {
                System.out.println(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
