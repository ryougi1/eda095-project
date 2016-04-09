package database;

import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		try {
			Database db = new Database();
//			GUI gui = new GUI(db);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

}
