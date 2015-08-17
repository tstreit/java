package net.minecrunch.gwc;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GalaxyWars extends JFrame {

	/**
	 * Global Variables
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(GalaxyWars.class.getName());
	static String osvendor = System.getProperty("os.name");
	static String osarch = System.getProperty("os.arch");
	static String javaversion = System.getProperty("java.version");
	static String javavendor = System.getProperty("java.vendor");
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			private FileOutputStream fos;

			public void run() {
				try {
					MakeDir();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					GalaxyWars frame = new GalaxyWars();
					frame.setVisible(true);
					// Output logging to server.log in logs folder
					Handler fh = new FileHandler("logs/client.log", true);
					fh.setFormatter(new SimpleFormatter());
					logger.addHandler(fh);
					logger.setLevel(Level.FINE);
					logger.info("Operating System: " + osvendor + " " + osarch);
					logger.info("Java Version: " + javavendor + " " + javaversion);

				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					new BuildDatabase().startDB();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			/* Setting up folders and getting necessary files */
			private void MakeDir() throws IOException {
				File logs = new File("logs");
				if (logs.exists()) {
					logger.info("The directory logs exists. Yay! Don't do anything.");
				} else {
					logs.mkdirs();
					logger.info("The directory logs doesn't exist. What have you been doing???");
				}

				File options = new File("data");
				if (options.exists()) {
					logger.info("The directory data exists. Yay! Don't do anything.");
				} else {
					options.mkdirs();
					logger.info("The directory data doesn't exist. What have you been doing???");
				}

				File chat = new File("chat");
				if (chat.exists()) {
					logger.info("The directory chat exists. Yay! Don't do anything.");
				} else {
					chat.mkdirs();
					logger.info("The directory chat doesn't exist. What have you been doing???");
				}

				File resources = new File("resources");
				if (resources.exists()) {
					logger.info("The directory resources exists. Yay! Don't do anything.");
				} else {
					resources.mkdirs();
					logger.info("The directory resources doesn't exist. What have you been doing???");
				}

				File bg = new File("resources/starfield.jpg");
				if (bg.exists()) {
					logger.info("The background file exists. Yay! Don't do anything.");
				} else {
					URL website = new URL("https://raw.githubusercontent.com/tstreit/java/master/galaxy-wars-client/starfield.jpg");
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					fos = new FileOutputStream("resources/starfield.jpg");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					logger.info("Do I have to do everything?? Downloaded the background image now.");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GalaxyWars() throws IOException {
		setTitle("Galaxy Wars");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Center window to middle of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

		// Action for Single Player button
		JButton btnSinglePlayer = new JButton("Single Player");
		btnSinglePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component frame = null;
				JOptionPane.showMessageDialog(frame, "This will be for a single player game.");
			}
		});
		btnSinglePlayer.setBounds(176, 95, 117, 29);
		contentPane.add(btnSinglePlayer);

		// Action for Multiplayer button
		JButton btnMultiplayer = new JButton("Multiplayer");
		btnMultiplayer.setBounds(176, 136, 117, 29);
		btnMultiplayer.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				GW_MultiPlayer gwm = new GW_MultiPlayer();
				gwm.show();
			}
		});
		contentPane.add(btnMultiplayer);

		// Action for Options button
		JButton btnOptions = new JButton("Options");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Calls Options class
				Options op = null;
				try {
					op = new Options();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				op.setVisible(true);
			}
		});
		btnOptions.setBounds(176, 177, 117, 29);
		contentPane.add(btnOptions);

		// Action for Exit button
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Ends program
				System.exit(0);
			}
		});
		btnExit.setBounds(176, 218, 117, 29);
		contentPane.add(btnExit);

		// Make starfield background
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("resources/starfield.jpg"));
		label.setBounds(0, 0, 450, 278);
		contentPane.add(label);
	}
}