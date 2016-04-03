package database;

import java.sql.*;
import java.util.ArrayList;

import database.Database;

public class Database {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://puccini.cs.lth.se:3306/db105";
	private static final String USER = "db105";
	private static final String PASS = "nli423ww";

	private Connection conn = null;
	private String sql;
	private static ArrayList<String> cookies;

	public Database() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER); //Register JDBC drivers
		cookies = new ArrayList<String>();
	}
	
	/**
	 * Establishes connection.
	 * @throws SQLException
	 */
	public void establishConnection() throws SQLException {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		if(conn != null) {
			System.out.println("Connected.");			
		}
	}
	
	/**
	 * Terminates connection.
	 * @throws SQLException
	 */
	public void terminateConnection() throws SQLException {
		conn.close();
		System.out.println("Connection terminated.");
	}
	
	/**
	 * Fetch list of cookie names from SQL database.
	 */
	public void getCookies() {
		String sql = "select cookieName from Cookies";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				cookies.add(rs.getString("cookieName"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e2) {
				// ... can do nothing if things go wrong here
			}
		}
	}
	
	/**
	 * Decides what type of search should be conducted.
	 * @param input Search input.
	 * @return ?
	 */
	public boolean search(String input) {
		if(input.isEmpty()) {
			return false;
		}
		char c = input.charAt(0);
		if(!Character.isDigit(c)) {
			return searchByCookie(input);
		} else {
			return searchByBarcode(input);
		}
	}
	
	/**
	 * Search by cookie name.
	 * @param input Search input.
	 * @return
	 */
	private boolean searchByCookie(String input) {
		return false;
	}
	
	/**
	 * Search by pallet bar code.
	 * @param input Search input.
	 * @return
	 */
	private boolean searchByBarcode(String input) {
		return false;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Database db = new Database();
		db.establishConnection();
		db.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			System.out.println(cookies.get(i));
		}
//		db.terminateConnection();
	}
	
	
}
