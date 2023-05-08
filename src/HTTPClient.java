/** Authors: Christian Abbott, Ben Raduns
 *  Course: COMP 342
 *  Date: May 7th, 2023
 *  Description: Client side of the socket-based HHTP protocol
 */

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HTTPClient {
    public static final int PORT = 80;
    //kept for reference
    public static final String SERVER_ADDR = "127.0.0.1" ; // ""www.google.com";//"142.250.190.36";
    public static final String CRLF = "\r\n";
    public static final String EOH = CRLF + CRLF;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Address: (hit enter for HTTPServer address): ");
        String fn1 = in.nextLine();
        String addr;
        if (fn1.isEmpty()) addr = SERVER_ADDR;
        else addr = fn1;
        System.out.print("File (hit enter for default): ");
        String fn2 = in.nextLine();
        String file;
        if (fn2.isEmpty()) file = "index.html";
        else file = fn2;

        System.out.println("client is requesting ... ");
        try {
            //Create socket and dataStreams
            Socket socket = new Socket(addr, PORT);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            // generate a HTTP request a print writer, handy to handle output stream
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dataOutputStream, StandardCharsets.ISO_8859_1));
            printWriter.print("GET /" + file + " HTTP/1.1" + CRLF);
            printWriter.print("Host: " + addr + CRLF);
            printWriter.print("Connection: close" + CRLF);
            printWriter.print("Accept: */*" + EOH);
            printWriter.flush();

            // try to receive the feedback
            System.out.println("After sending the request, wait for response: ");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
            String line = bufferedReader.readLine();
            //parse the HEAD section of the feedback
            while (!line.isEmpty()) {
                System.out.println(line);
                line = bufferedReader.readLine();
            }
            //parse the BODY section of the feedback
            StringBuilder sb = new StringBuilder();
            line = bufferedReader.readLine();
            sb.append(line);
            //loop until the page ends
            while (line != null && !line.isEmpty()) {
                System.out.println(line);
                sb.append(line + "\n");
                line = bufferedReader.readLine();
            }
            System.out.println(sb);
            //Write
            File newFile = new File("client_folder/" + file);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }

            try (PrintWriter out = new PrintWriter("client_folder/" + file)) {
                out.println(sb);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
