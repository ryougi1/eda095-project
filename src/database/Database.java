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
	private ArrayList<String> cookies;

	public Database() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER); //Register JDBC drivers
		cookies = new ArrayList<String>();
		establishConnection();
		getCookies();
	}
	
	/**
	 * Establishes connection.
	 * @throws SQLException
	 */
	private void establishConnection() throws SQLException {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		if(conn != null) {
			System.out.println("Connected.");			
		}
	}
	
	/**
	 * Terminates connection.
	 * @throws SQLException
	 */
	private void terminateConnection() throws SQLException {
		conn.close();
		System.out.println("Connection terminated.");
	}
	
	/**
	 * Fetch list of cookie names from SQL database.
	 */
	private void getCookies() {
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
	public String[][] search(String input) {
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
	private String[][] searchByCookie(String input) {
		String cookie = null;
		for (String cookieName : cookies) {
			if(cookieName.toLowerCase().contains(input.toLowerCase())) {
				cookie = cookieName;
				break;
			}
		}
		if(cookie == null) {
			return null;
		}
		String sql = "select * from Pallets where cookieName = ? order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookie);
			ResultSet rs = ps.executeQuery();
			rs.last();
			int rows = rs.getRow();
			rs.beforeFirst();
			String[][] result = new String[rows][4];
			int j = 0;
			while(rs.next()) {
				for(int i = 1; i < 5; i++) {
					result[j][i-1] = rs.getString(i);	
				}
				j++;
			}
			return result;
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
	private String[][] searchByBarcode(String input) {
		String[][] result = new String[1][4];
		String sql = "select * from Pallets where barcode = ? order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, input);
			ResultSet rs = ps.executeQuery();
			rs.next();
			for(int i = 1; i < 5; i++) {
				result[0][i-1] = rs.getString(i);	
			}
			return result;
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
	private String[][] searchByTime(String input) {
		int i = input.indexOf(",");
		String cookieName = input.substring(0, i);
		String remainder = input.substring(i+1, input.length());
		int j = remainder.indexOf(",");
		String startDate = remainder.substring(0, j);
		String endDate = remainder.substring(j+1, remainder.length());
		
		String sql = "select * from Pallets where cookieName = ? and timeProduced >= ? "
				+ "and timeProduced <= ? order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookieName);
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ResultSet rs = ps.executeQuery();
			
			rs.last();
			int rows = rs.getRow();
			rs.beforeFirst();
			String[][] result = new String[rows][4];
			j = 0;
			while(rs.next()) {
				for(i = 1; i < 5; i++) {
					result[j][i-1] = rs.getString(i);	
				}
				j++;
			}
			return result;
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e2) {
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Database db = new Database();
		
		String[][] test = db.search("Nut ring,2016-04-03 11:00:00,2016-04-03 11:16:00");

		for(int j = 0; j < test.length; j++) {
			for(int i = 0; i < 4; i++) {
				System.out.print(test[j][i] + " | ");
			}			
			System.out.println("");
		}		
		
		db.terminateConnection();
	}
	
	
}
