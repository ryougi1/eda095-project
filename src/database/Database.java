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
			//return searchByCookie(input);
			return null;
		} else {
//			return searchByBarcode(input);
			return null;
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
//			rs.last();
//			int rows = rs.getRow();
//			rs.beforeFirst();
			String[][] result = new String[4][4];
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
	private ResultSet searchByTime(String input) {
		int i = input.indexOf(",");
		String cookieName = input.substring(0, i-1);
		String remainder = input.substring(i+1, input.length()-1);
		int j = remainder.indexOf(",");
		String startDate = input.substring(i+1, j-1);
		String endDate = input.substring(j+1, input.length()-1);
		
		String sql = "select * from Pallets where cookieName = ? and timeProduced >= ? "
				+ "and timeProduced <= ? order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookieName);
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ResultSet rs = ps.executeQuery();
			return rs;
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
//			try {
//				ps.close();
//			} catch (SQLException e2) {
//			}
		}
		return null;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Database db = new Database();
		db.establishConnection();
		db.getCookies();
		String[][] test = db.searchByCookie("Nut ring");
		if(test == null) {
			System.out.println("!sad");
		}
		for(int j = 0; j < 4; j++) {
			for(int i = 0; i < 4; i++) {
				System.out.print(test[j][i] + " | ");
			}			
			System.out.println("");
		}
		
//		ResultSet rs = db.searchByTime("Nut Ring,2016-04-03 11:00:00,2016-04-03 11:00:00");
//		ArrayList<String> test = new ArrayList<String>();
//	
//		while(rs.next()) {
//			test.add(rs.getString(1));
//		}
//		if (rs == null) {
//			System.out.println("set is null");
//		}
//		rs.next();
//		System.out.println(rs.getString(1));
//			
		
		db.terminateConnection();
	}
	
	
}
