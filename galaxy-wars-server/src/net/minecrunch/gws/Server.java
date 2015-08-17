package net.minecrunch.gws;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {
	
	// Create logger for the server class
	private static final Logger logger = Logger.getLogger(Server.class.getName());
	
	public static void main(String[] args) throws Exception {
		//Make base directories
		try {
			MakeDir();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Starting server
		new Server().startServer();
		new BuildDatabase().startDB();
		new ChatServer().startChat();
	}
	
	public static void MakeDir() throws IOException {
		// Make logs directory if it doesn't exist
		File logs = new File("logs");
		if (logs.exists()) {
			// Output logging to server.log in logs folder
			Handler fh = new FileHandler("logs/server.log", true);
		    fh.setFormatter(new SimpleFormatter());
		    logger.addHandler(fh);
		    logger.setLevel(Level.FINE);
			logger.info("The directory logs exists. Yay! Don't do anything.");
		} else {
			logs.mkdirs();
			// Output logging to server.log in logs folder
			Handler fh = new FileHandler("logs/server.log", true);
		    fh.setFormatter(new SimpleFormatter());
		    logger.addHandler(fh);
		    logger.setLevel(Level.FINE);
			logger.info("The directory logs doesn't exist. What have you been doing???");
		}
		
		File data = new File("data");
		if (data.exists()) {
			logger.info("The directory data exists. Yay! Don't do anything.");
		} else {
			data.mkdirs();
			logger.info("The directory data doesn't exist. What have you been doing???");
		}
	}

	public void startServer() {
		
		//Create Thread for server to keep socket open and listening for connections
		final ExecutorService clientProcessingPool = Executors
				.newFixedThreadPool(10);

		Runnable serverTask = new Runnable() {
			private ServerSocket serverSocket;

			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(6669);
					logger.info("Waiting for clients to connect...");
					while (true) {
						Socket clientSocket = serverSocket.accept();
						clientProcessingPool
								.submit(new ClientTask(clientSocket));
					}
				} catch (IOException e) {
					logger.severe("Unable to process client request");
					e.printStackTrace();
				}
			}
		};
		
		// Start thread
		Thread serverThread = new Thread(serverTask);
		serverThread.start();
	}

	private class ClientTask implements Runnable {
		
		/* ClientTask thread is were the work of the server gets done
		   Create socket for the client to connect to and output the data stream */
		private final Socket clientSocket;
		DataOutputStream out;

		private ClientTask(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			
			/* Once client connects this is were the work data gets pass back and forth */
			logger.info("Got a client !");
			logger.info("Connection from: " + clientSocket.getInetAddress());

			try {
				out = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			try {
				out.writeUTF("This is a test of Java sockets. Your IP address is: "
						+ clientSocket.getInetAddress());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.info("Data has been sent.");
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
}