package database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
				// ... can do nothing if things go wrong here.
			}
		}
	}
	
	/**
	 * Decides what type of search should be conducted.
	 * @param input, Search input.
	 * @return ?
	 */
	public ResultSet search(String input) {
		if(input.isEmpty()) {
			return null;
		}
		char first = input.charAt(0);
		if(!Character.isDigit(first)) {
			for(char c : input.toCharArray()) {
				if(c == ',') {
					return searchByTime(input);
				}
			}
			return searchByCookie(input);
		} else {
			return searchByBarcode(input);
		}
	}
	
	/**
	 * Search by cookie name.
	 * @param input, Search input.
	 * @return
	 */
	private ResultSet searchByCookie(String input) {
		String sql = "select * from Pallets where cookieName = ? order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps.setString(1, input);
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			return rs;
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e2) {
				// ... can do nothing if things go wrong here.
			}
		}
		return null;
	}
	
	/**
	 * Search by pallet bar code.
	 * @param input, Search input.
	 * @return
	 */
	private ResultSet searchByBarcode(String input) {
		String sql = "select * from Pallets where barcode = ? order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps.setString(1, input);
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			return rs;
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e2) {
				// ... can do nothing if things go wrong here.
			}
		}
		return null;
	}
	
	/**
	 * Search by cookie name, date and time.
	 * @param input, Search input.
	 * @return
	 */
	private ResultSet searchByTime(String input) {
		int i = input.indexOf(",");
		String cookieName = input.substring(0, i-1);
		int j = input.lastIndexOf(input);
		String startDate = input.substring(i+1, j-1);
		String endDate = input.substring(j+1, input.length()-1);
		
		String sql = "select * from Pallets where cookieName = ? and timeProduced > ? "
				+ "and timeProduced < ? order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps.setString(1, cookieName);
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			return rs;
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e2) {
				// ... can do nothing if things go wrong here.
			}
		}
		return null;
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
