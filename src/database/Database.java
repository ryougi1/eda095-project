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
		String sql = "select * from Pallets where barcode = ? ";
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
		String start = remainder.substring(0, j);
		String end = remainder.substring(j+1, remainder.length());
		
		String sql = "select * from Pallets where cookieName = ? and timeProduced >= ? "
				+ "and timeProduced <= ? order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookieName);
			ps.setString(2, start);
			ps.setString(3, end);
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
	
	public String[][] getAllPallets(){
		String sql = "select * from Pallets order by timeProduced asc";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
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
	
	public ArrayList<Integer> getBlockedPallets() {
		ArrayList<Integer> blockedPallets = new ArrayList<Integer>();
		String sql = "SELECT barcode FROM blockedpallets";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				blockedPallets.add(rs.getInt("barcode"));
			}
			return blockedPallets;
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
	
	public int blockPalletByBarcode(int barcode) {
		String sql = "INSERT INTO blockedpallets VALUES (?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, barcode);
			return ps.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e2) {
			}
		}
		return 0;
	}
	
	public int blockPalletByTime(String startDatetime, String endDatetime) {
		String sql = "INSERT INTO blockedpallets "
				+ "SELECT barcode FROM pallets "
				+ "WHERE timeProduced >= ? "
				+ "and timeProduced <= ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, startDatetime);
			ps.setString(2, endDatetime);
			return ps.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e2) {
			}
		}
		return 0;
	}
	
	public int unblockPalletByBarcode(int barcode) {
		String sql = "DELETE FROM blockedpallets WHERE barcode = ?";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, barcode);
			return ps.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e2) {
			}
		}
		return 0;
	}
	
	//Returns 
	// Positive number on successful pallet creation, otherwise
	// -1 if cookie is unknown
	// -2 if not enough ingredients
	// -3 if it somehow manages to get to the last line.
	public int createPallet(String cookie, String location) {
		if (!cookies.contains(cookie)) {
			return -1;
		} 
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT ingredientName, amount, amountinstorage "
				+ "FROM recipes NATURAL JOIN ingredients "
				+ "WHERE cookieName = ? ";
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookie);
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt("amount") > rs.getInt("amountinstorage")) {
					return -2;
				} 
				sql = "UPDATE ingredients SET amountinstorage = amountinstorage - ? WHERE ingredientName = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, rs.getInt("amount"));
				ps.setString(2, rs.getString("ingredientName"));
				ps.executeUpdate();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
				
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		String datetime = LocalDateTime.now().format(formatter);	
		sql = "INSERT INTO pallets values(null, ?, ?, ?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, datetime);
			ps.setString(2, cookie);
			ps.setString(3, location);
			conn.commit();
			return ps.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
				ps.close();
			} catch (SQLException e2) {
			}
		}
		return -3;
	}

    public String getCookieList(){
        return cookies.toString();
    }
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Database db = new Database();
		
//		System.out.println(db.createPallet("Nut ring", "abstract freezer"));
//		System.out.println(db.createPallet("dettabordeintefunka", "abstract freezer"));
		
//		ResultSet test = db.test("Amneris");
//		while (test.next()) {
//			if (test.getInt("amount") < test.getInt("amountinstorage")) {
//				System.out.println(test.getString("ingredientName"));
//				System.out.println(test.getInt("amount"));
//				System.out.println(test.getInt("amountinstorage"));
//			}
//		}
		

		
//		db.blockPalletByTime("2016-04-03 11:00:00", "2016-04-03 11:15:00");
//		ArrayList<Integer> blocked = db.getBlockedPallets();
//		for (int i : blocked) {
//			System.out.println("Pallet with barcode " + String.valueOf(i) + " is blocked");
//		}
//		for (int i = 1; i <= 3; i++) {
//			db.unblockPalletByBarcode(i);
//		}
		
		String[][] test = db.getAllPallets();
		for(int j = 0; j < test.length; j++) {
			for(int i = 0; i < 4; i++) {
				System.out.print(test[j][i] + " | ");
			}			
			System.out.println("");
		}		
		
//		ArrayList<Integer> blocked = db.getBlockedPallets();
//		System.out.println("1 and 2 should be blocked:");
//		for (int i : blocked) {
//			System.out.println("Pallet with barcode " + String.valueOf(i) + " is blocked");
//		}
//		System.out.println("\n");
//		db.unblockPalletByBarcode(1);
//		blocked = db.getBlockedPallets();
//		System.out.println("2 should be blocked:");
//		for (int i : blocked) {
//			System.out.println("Pallet with barcode " + String.valueOf(i) + " is blocked");
//		}
//		System.out.println("\n");
//		db.blockPalletByBarcode(1);
//		blocked = db.getBlockedPallets();
//		System.out.println("1 and 2 should be blocked:");
//		for (int i : blocked) {
//			System.out.println("Pallet with barcode " + String.valueOf(i) + " is blocked");
//		}
		
		db.terminateConnection();
	}
	
	
}
