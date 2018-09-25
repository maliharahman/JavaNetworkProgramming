package lab1part2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPEchoServer {
    // define a constant used as size of buffer
    static final int BUFSIZE=1024;
    // main starts things rolling
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
        byte[] input = new byte[BUFSIZE];
        int bytesread = 0;

        //print out client's address
        System.out.println("Connection from " + s.getInetAddress().getHostAddress());

        //Set up streams
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream();

        String my_name="Maliha Rahman ";
        byte[] name_buff=my_name.getBytes();
        byte[] total_byte=new byte[name_buff.length+BUFSIZE];

        System.arraycopy(name_buff,0,total_byte,0,my_name.length());

        while ((bytesread = in.read(input)) != -1)
        {
            System.arraycopy(input,0,total_byte,my_name.length(),input.length);
            int total=total_byte.length;
            out.write(total_byte,0,total);
        }

        System.out.println("Client has left\n");
        s.close();

    }
}