package lab1part2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEchoServer {
    static final int BUFSIZE=1024;

    static public void main(String args[]) throws SocketException
    {

        if (args.length != 1) {
            throw new IllegalArgumentException("Must specify a port!");

        }

        int port = Integer.parseInt(args[0]);
        DatagramSocket s = new DatagramSocket(port);
        DatagramPacket dp = new DatagramPacket(new byte[BUFSIZE],BUFSIZE);

        String my_name="Maliha Rahman ";
        byte[] name_buff=my_name.getBytes();

        int total_buff_size=my_name.length()+BUFSIZE;

        byte[] total=new byte[total_buff_size];

        //total has the name : MAliha...:
        System.arraycopy(name_buff,0,total,0,name_buff.length);

        try {
            while (true)
            {
                s.receive(dp);
                byte[] result=dp.getData();

                System.arraycopy(result,0,total,name_buff.length,result.length);
               // DatagramPacket dp2 = new DatagramPacket(result,result.length);
                // print out client's address
                System.out.println("Message from " + dp.getAddress().getHostAddress());
                dp.setData(total);

                // Send it right back
                s.send(dp);
                dp.setLength(total_buff_size);// avoid shrinking the packet buffer
            }
        } catch (IOException e) {
            System.out.println("Fatal I/O Error !");
            System.exit(0);

        }

    }
}