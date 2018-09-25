package Lab1Part3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MultiThreadedServer
{

    public static Logger logger;
    static FileHandler fh;
    static String fileName="myLogFile";

    public static void Log() throws IOException {
        File f=new File(fileName);
        if(!f.exists())
        {
            f.createNewFile();
        }

        fh =new FileHandler(fileName,true);
        logger=Logger.getLogger("test");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    public static void main(String[] args) throws IOException {
        try
        {
            Log();
            Socket clientSocket;
            ServerSocket serverSocket = new ServerSocket(9090);
            boolean stop = false;
            while(!stop)
            {
                System.out.println("Waiting for clients...");
                clientSocket = serverSocket.accept();
                System.out.println("Client is connected.");
                WorkerRunnable clientThread = new WorkerRunnable(clientSocket,"Msg from server..");
                clientThread.start();
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
}