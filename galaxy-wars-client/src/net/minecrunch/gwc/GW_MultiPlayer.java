package net.minecrunch.gwc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.net.Socket;
import java.sql.*;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTable;

import java.awt.Color;
import javax.swing.ListSelectionModel;

public class GW_MultiPlayer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static final Logger logger = Logger.getLogger(GW_MultiPlayer.class.getName());
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_CONNECTION = "jdbc:h2:./data/gws_data;";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";
	static Socket socket;
	static DataInputStream in;
	private JTable table;

	/**
	 * Create the frame.
	 */
	public GW_MultiPlayer() {
		setTitle("Galaxy Wars");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
        DefaultTableModel model = new DefaultTableModel() { 
			private static final long serialVersionUID = 1L;
			String[] rowName = {"Name", "Server", "Port"}; 

            @Override 
            public int getColumnCount() { 
                return rowName.length; 
            } 

            @Override 
            public String getColumnName(int index) { 
                return rowName[index]; 
            } 
        };
        
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(false);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setForeground(Color.GREEN);
		table.setBounds(6, 200, 438, -193);
		contentPane.add(table);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(6, 214, 117, 29);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnBack);

		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(135, 214, 117, 29);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GetConnection();
			}
		});
		contentPane.add(btnConnect);

		JButton btnExit = new JButton("Quit");
		btnExit.setBounds(264, 214, 117, 29);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		contentPane.add(btnExit);
				
						// Make starfield background
						JLabel label = new JLabel("");
						label.setBounds(0, 0, 450, 278);
						contentPane.add(label);
						label.setIcon(new ImageIcon("resources/starfield.jpg"));

		// Center window to middle of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
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

	public void GetConnection() {
		try {
			Connection connection = getDBConnection();
			PreparedStatement selectPreparedStatement = null;
			String SelectQuery = "SELECT * FROM GWS_GAME";

			// Get database list of tables
			DatabaseMetaData dbm = connection.getMetaData();
			ResultSet rsm = dbm.getTables(null, "PUBLIC", "GWS_GAME", null);

			if (rsm.next()) {
				// Results from select query
				logger.info("Database table exist, you're safe....for now.");
				try {
					connection.setAutoCommit(false);
					selectPreparedStatement = connection.prepareStatement(SelectQuery);
					ResultSet rs = selectPreparedStatement.executeQuery();
					while (rs.next()) {
						logger.info("Nickname: " + rs.getString("nickname") + " Server Address: " + rs.getString("host")
								+ " Port Number: " + rs.getInt("port"));

						logger.info("Connecting....");
						socket = new Socket(rs.getString("host"), rs.getInt("port"));
						logger.info("Connection successful.");
						in = new DataInputStream(socket.getInputStream());
						logger.info("Receiving information.");
						String test = in.readUTF();
						logger.info("Message from server: " + test);
						Component frame = null;
						JOptionPane.showMessageDialog(frame, test);
					}
					selectPreparedStatement.close();
					connection.commit();
				} catch (SQLException e1) {
					System.out.println("Exception Message " + e1.getLocalizedMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					connection.close();
				}
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} finally {
		}
	}
}
