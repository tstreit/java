package net.minecrunch.gwc;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Options extends JFrame {

	/**
	 * Global Variables
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Options.class.getName());
	private JPanel contentPane;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField;
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_CONNECTION = "jdbc:h2:./data/gws_data;";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";

	/**
	 * Create the frame.
	 */
	public Options() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 235);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		// Center windows to middle of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

		textField1 = new JTextField();
		textField1.setBounds(115, 56, 247, 28);
		contentPane.add(textField1);
		textField1.setColumns(10);

		// Output logging to database.log in logs folder
		Handler fh = new FileHandler("logs/database.log", true);
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		logger.setLevel(Level.FINE);

		// Action for Add Server button
		JButton btnAddServer = new JButton("Add Server");
		btnAddServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get text from textfields
				String writeServer = textField1.getText();
				String writePort = textField2.getText();
				int i = Integer.parseInt(writePort);
				String writeNick = textField.getText();

				// Check if the database exists, if not create it and build
				// tables
				Connection connection = getDBConnection();
				PreparedStatement selectPreparedStatement = null;
				PreparedStatement insertPreparedStatement = null;
				String InsertTable = "INSERT INTO GWS_GAME VALUES('"+ writeNick +"', '"+ writeServer +"', "+ i +")";
				String SelectQuery = "SELECT * FROM GWS_GAME";

				// Results return that database doesn't exists, creates
				// database, inserts some data, and runs select query
				logger.info("Just insert some real data already.");
				try {
					connection.setAutoCommit(false);

					insertPreparedStatement = connection.prepareStatement(InsertTable);
					insertPreparedStatement.execute();

					selectPreparedStatement = connection.prepareStatement(SelectQuery);
					ResultSet rs = selectPreparedStatement.executeQuery();
					while (rs.next()) {
						logger.info("Nickname: " + rs.getString("nickname") + " Server Address: " + rs.getString("host") + " Port Number: " + rs.getInt("port"));
					}
					insertPreparedStatement.close();
					selectPreparedStatement.close();

					connection.commit();
				} catch (SQLException e1) {
					System.out.println("Exception Message " + e1.getLocalizedMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					try {
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnAddServer.setBounds(167, 124, 117, 29);
		contentPane.add(btnAddServer);

		// Action for Close button
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Close window
				dispose();
			}
		});
		btnClose.setBounds(167, 165, 117, 29);
		contentPane.add(btnClose);

		JLabel lblServerPort = new JLabel("Server Port:");
		lblServerPort.setForeground(Color.GREEN);
		lblServerPort.setBounds(6, 90, 97, 16);
		contentPane.add(lblServerPort);

		JLabel label_1 = new JLabel("Server Address:");
		label_1.setForeground(Color.GREEN);
		label_1.setBounds(6, 62, 97, 16);
		contentPane.add(label_1);

		JLabel lblServerInformation = new JLabel("Server Information");
		lblServerInformation.setForeground(Color.GREEN);
		lblServerInformation.setBounds(6, 6, 117, 16);
		contentPane.add(lblServerInformation);

		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setForeground(Color.GREEN);
		lblNickname.setBounds(6, 34, 97, 16);
		contentPane.add(lblNickname);

		textField2 = new JTextField();
		textField2.setText("6669");
		textField2.setColumns(10);
		textField2.setBounds(115, 84, 247, 28);
		contentPane.add(textField2);

		textField = new JTextField();
		textField.setBounds(115, 28, 247, 28);
		contentPane.add(textField);
		textField.setColumns(10);

		// Make starfield background
		JLabel label = new JLabel("");
		label.setForeground(Color.BLACK);
		label.setIcon(new ImageIcon("resources/starfield.jpg"));
		label.setBounds(0, 0, 450, 389);
		contentPane.add(label);

	}

	private static Connection getDBConnection() {
		// Make database connection
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
}
