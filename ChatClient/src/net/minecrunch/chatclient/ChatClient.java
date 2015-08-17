package net.minecrunch.chatclient;

import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ChatClient {
	
	BufferedReader in;
	PrintWriter out;
	JFrame frame = new JFrame("Chat");
	JTextField textField = new JTextField(40);
	JTextArea messageArea = new JTextArea(8, 40);
	DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
	
	public ChatClient() {
		
		textField.setEditable(false);
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		frame.getContentPane().add(textField, "South");
		frame.getContentPane().add(new JScrollPane(messageArea), "Center");
		frame.pack();
		
		textField.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				out.println(textField.getText());
				textField.setText("");
			}
		});
	}
	
	private String getServerAddress() {
		
		return JOptionPane.showInputDialog(
				frame,
				"Enter address of the Chat Server:",
				"Welcome to the Chat Server",
				JOptionPane.QUESTION_MESSAGE);
	}
	
	private String getName() {
		
		return JOptionPane.showInputDialog(
				frame,
				"Choose a screen name:",
				"Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	private void run() throws IOException {
		
		String serverAddress = getServerAddress();
		@SuppressWarnings("resource")
		Socket socket = new Socket(serverAddress, 6670);
		in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		
		while (true) {
			String line = in.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out.println(getName());
			} else if (line.startsWith("NAMEACCEPTED")) {
				textField.setEditable(true);
			} else if (line.startsWith("MESSAGE")) {
				messageArea.append(line.substring(8) + "\n");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		ChatClient client = new ChatClient();
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		client.run();
	}
}