package net.minecrunch.gwc;

import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BuildDatabase {
	// Create logger for the database class
	private static final Logger logger = Logger.getLogger(BuildDatabase.class.getName());
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_CONNECTION = "jdbc:h2:./data/gws_data;";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";

	public void startDB() throws SecurityException, IOException {
		// Output logging to database.log in logs folder
		Handler fh = new FileHandler("logs/database.log", true);
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		logger.setLevel(Level.FINE);
		try {
			selectWithPreparedStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}

	private static void selectWithPreparedStatement() throws SQLException {
		// Check if the database exists, if not create it and build tables
		Connection connection = getDBConnection();
		PreparedStatement createPreparedStatement = null;
		PreparedStatement selectPreparedStatement = null;
		String CreateTable = "CREATE TABLE IF NOT EXISTS GWS_GAME(NICKNAME VARCHAR(255), HOST VARCHAR(255), PORT INT)";
		String SelectQuery = "SELECT * FROM GWS_GAME";

		// Get database list of tables
		DatabaseMetaData dbm = connection.getMetaData();
		ResultSet rsm = dbm.getTables(null, "PUBLIC", "GWS_GAME", null);

		if (rsm.next()) {
			// Results return that database exists and runs select query
			logger.info("Database table exist, you're safe....for now.");
			try {
				connection.setAutoCommit(false);
				selectPreparedStatement = connection
						.prepareStatement(SelectQuery);
				ResultSet rs = selectPreparedStatement.executeQuery();
				while (rs.next()) {
					logger.info("Nickname: " + rs.getString("nickname") + " Server Address: " + rs.getString("host") + " Port Number: " + rs.getInt("port"));
				}
				selectPreparedStatement.close();
				connection.commit();
			} catch (SQLException e) {
				System.out.println("Exception Message "
						+ e.getLocalizedMessage());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				connection.close();
			}
		} else {
			// Results return that database doesn't exists, creates database, inserts some data, and runs select query
			logger.info("The database doesn't exist!!! The world will now burn.....just kidding.");
			try {
				connection.setAutoCommit(false);
				
				createPreparedStatement = connection
						.prepareStatement(CreateTable);
				createPreparedStatement.execute();

				logger.info("I guess I will go ahead and create it now...save the world and all.");
				
				selectPreparedStatement = connection
						.prepareStatement(SelectQuery);
				ResultSet rs = selectPreparedStatement.executeQuery();
				while (rs.next()) {
					logger.info("Nickname: " + rs.getString("nickname") + " Server Address: " + rs.getString("host") + " Port Number: " + rs.getInt("port"));
				}				
				createPreparedStatement.close();
				selectPreparedStatement.close();
				
				connection.commit();
			} catch (SQLException e) {
				System.out.println("Exception Message "
						+ e.getLocalizedMessage());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				connection.close();
			}
		}
	}
}
