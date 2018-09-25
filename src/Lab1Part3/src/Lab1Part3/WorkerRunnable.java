package Lab1Part3;

import java.io.*;
import java.net.Socket;

public class WorkerRunnable extends Thread {

    private Socket socket=null;
    private String serverText;

    public WorkerRunnable(Socket clientSocket, String textFromServer) {
        this.socket=clientSocket;
        this.serverText=textFromServer;
    }

    @Override
    public void run() {
        try{

            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //BufferedWriter out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            PrintWriter out=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String str="";
            while((str=in.readLine())!=null) {
                System.out.println("Received commands are: " + str);

                String receiveoutput = commandFunc(str);
                System.out.println("receiveoutput are: " + receiveoutput);

                out.write(receiveoutput);
                out.flush();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }catch(Exception e2)
        {
            System.out.println(e2.toString());
        }
    }

    public String commandFunc(String inputCmd) throws IOException
    {
        String consoleOutput="";
        try {

            String[] cmd={"/bin/sh", "-c",inputCmd};
            Process process=Runtime.getRuntime().exec(cmd);

            BufferedReader input=new BufferedReader(new InputStreamReader(process.getInputStream()));
            String readInput;
            BufferedReader error_input=new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String error;

            while((readInput=input.readLine())!=null)
            {
                consoleOutput=consoleOutput+readInput+"\n";
            }

            while((error=error_input.readLine())!=null)
            {
                consoleOutput=consoleOutput+error+"\n";
            }

        } catch (IOException e)
            {
              System.out.println(e.toString());
            }

        return consoleOutput;
    }
    }