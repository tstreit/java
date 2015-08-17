package net.minecrunch.gwc;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class Makeconn {
	
	// Variable defined
	static Socket socket;
	static DataInputStream in;
	String test = null;
	Properties prop = new Properties();
	InputStream input = null;
	
	// Make connection to server
	public String conn() {
		try {
			// Get server.properties file and load the properties
			input = new FileInputStream("options/server.properties");
			prop.load(input);
			
			// Create a string for the server property
			String getServer = prop.getProperty("server");
			// Create a int for the port property 
			String getPort = prop.getProperty("port");
			int getPort2 = Integer.parseInt(getPort);
			
			// Assign the string and int to the socket to get the server connection
			socket = new Socket(getServer,getPort2);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		try {
			// Get data from server
			in = new DataInputStream(socket.getInputStream());
			test = in.readUTF();
		} catch (IOException e) {
			System.out.println("No data recieved.");
			e.printStackTrace();
		}
		// Pass test to the GalaxyWars class
		return test;
	}
}