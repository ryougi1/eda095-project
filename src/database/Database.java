package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; 

import database.Database;

public class Database {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://puccini.cs.lth.se:3306/db105";
	private static final String USER = "db105";
	private static final String PASS = "nli423ww";

	private Connection conn = null;
	private String sql;

	public Database() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER); //Register JDBC drivers
	}
	
	/*
	 * Connection Establishment
	 */
	public void establishConnection() throws SQLException {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		if(conn != null) {
			System.out.println("Connected.");			
		}
	}
	
	/*
	 * Connection Termination
	 */
	public void terminateConnection() throws SQLException {
		conn.close();
		System.out.println("Connection terminated.");
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Database db = new Database();
		db.establishConnection();
		db.terminateConnection();
	}
}
