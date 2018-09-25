package Lab1Part3;

import java.io.*;
import java.net.Socket;

public class ClientSocket extends Thread{

    clientGuiForm obj=new clientGuiForm();
    String adrs, cmd;
    int port = 9090;

    public ClientSocket(String cmd, String uri)
    {
        this.adrs=uri;
        this.cmd=cmd;
    }

    @Override
    public void run()
    {
        String str="",output="";
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(adrs, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            PrintWriter out=new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            out.write(cmd+"\n");
            out.flush();

            System.out.println("Command sent: " + cmd);

            BufferedReader in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while((str=in.readLine())!=null) {
                output = output + str + "\n";
                Data.sth=output;
                System.out.println("received in client " + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}