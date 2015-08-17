package net.minecrunch.gws;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ChatServer {

	private static final int PORT = 6670;
	private static HashSet<String> names = new HashSet<String>();
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	private static final Logger logger = Logger.getLogger(ChatServer.class.getName());

	public void startChat() throws Exception {
		// Make base directories
		try {
			MakeDir();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Starting server

		logger.info("The chat server is running.");
		ServerSocket listener = new ServerSocket(PORT);

		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}
	
	private static void MakeDir() throws IOException {
		// Make logs directory if it doesn't exist
		File logs = new File("logs");
		if (logs.exists()) {
			// Output logging to server.log in logs folder
			FileHandler fh = new FileHandler("logs/chat.log", true);
		    fh.setFormatter(new SimpleFormatter());
		    logger.addHandler(fh);
		    logger.setLevel(Level.FINE);
			logger.info("The directory logs exists. Yay! Don't do anything.");
		} else {
			logs.mkdirs();
			// Output logging to server.log in logs folder
			FileHandler fh = new FileHandler("logs/chat.log", true);
		    fh.setFormatter(new SimpleFormatter());
		    logger.addHandler(fh);
		    logger.setLevel(Level.FINE);
			logger.info("The directory logs doesn't exist. What have you been doing???");
		}
	}

	private static class Handler extends Thread {
		
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			
			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					String sname = name;
					logger.info(sname + " has connected.");
					if (name == null) {
						return;
					}
					synchronized (names) {
						if (!names.contains(name)) {
							names.add(name);
							break;
						}
					}
				}

				out.println("NAMEACCEPTED");
				writers.add(out);

				while (true) {
					String input = in.readLine();
					logger.info(name + ": " + input);
					if (input == null) {
						return;
					}
					for (PrintWriter writer : writers) {
						writer.println("MESSAGE " + name + ": " + input);
						
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (name != null) {
					names.remove(name);
				}
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}