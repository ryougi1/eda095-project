package database;

import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		try {
			Database db = new Database();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
//		Client client = new Client(db);
//		GUI gui = new GUI(client);

	}

}
