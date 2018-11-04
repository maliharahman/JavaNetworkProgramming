package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MultithreadedServer implements Runnable {

	Socket csocket;
	public static int noOfConnections = 0;

	private final static Logger LOGGER = Logger.getLogger(Logger.class.getName());

	final static int socket = 5678;

	MultithreadedServer(Socket csocket) {
		this.csocket = csocket;
	}

	public static void main(String args[]) throws Exception {
		ServerSocket ssock = new ServerSocket(socket);
		LOGGER.info("Server Listening to port: 5678");
		while (true) {
			Socket sock = ssock.accept();
			LOGGER.info("New tcp connection established");
			noOfConnections++;
			LOGGER.info("New tcp connection established - Connection Number: " + noOfConnections);
			new Thread(new MultithreadedServer(sock)).start();
		}
	}

	public void run() {
		try {
			String line;
			BufferedReader clientInputReader = new BufferedReader(new InputStreamReader(csocket.getInputStream()));

			PrintStream pstream = new PrintStream(csocket.getOutputStream());

			ArrayList<Double> fibo;
			while ((line = clientInputReader.readLine()) != null) { 
				fibo = new ArrayList<>();
				for (long i = Long.parseLong(line); i >= 0; i--)
					fibo.add(fibbonaciGenerator(i));
				pstream.println(fibo);
				pstream.flush();

			}
			csocket.close();
			LOGGER.info("Connection Closed!");
		} catch (IOException e) {
			LOGGER.severe("IO interruption detected!");
		}
	}

	 public double fibbonaciGenerator(long n){
		 double prev=0d, next=1d, result=0d;
		 for (int i = 0; i < n; i++) {
			 result=prev+next;
			 prev=next;
			 next=result;
		 }
		 return result;
	 }
}
