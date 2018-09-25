package lab1part2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPEchoSErverWithBuffer {

    static public void main(String args[]) {
        if (args.length != 1){
            throw new IllegalArgumentException("Must specify a port!");
        }

        int port = Integer.parseInt(args[0]);
        try {
            // Create Server Socket (passive socket)
            ServerSocket ss = new ServerSocket(port);

            while (true) {
                Socket s = ss.accept();
                handleClient(s);
            }

        } catch (IOException e) {
            System.out.println("Fatal I/O Error !");
            System.exit(0);
        }
    }

    //this method handles one client
    // declared as throwing IOException - this means it throws
    // up to the calling method (who must handle it!)
    //try taking out the "throws IOException" and compiling,
    // the compiler will tell us we need to deal with this!

    static void handleClient(Socket s) throws IOException
    {
        //print out client's address
        System.out.println("Connection from " + s.getInetAddress().getHostAddress());

        BufferedReader buf=new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter outp = new PrintWriter(s.getOutputStream(), true);

        int data=0;
        String str;
        while((str=buf.readLine())!=null)
        {
            outp.print("Maliha " + str + "\n");
            outp.flush();
        }

        System.out.println("Client has left\n");
        s.close();
    }
}