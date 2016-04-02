package database;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Client {
	private Database db;
	
	public Client(Database db) {
		this.db = db;
	}
	
	public boolean search(String input) {
		if(input.isEmpty()) {
			return false;
		}
		char c = input.charAt(0);
		if(!Character.isDigit(c)) {
			//SearchByCookie
		} else {
			//SearchByBarcode
		}
		return true;
	}
	
	public boolean block(LocalDateTime start, LocalDateTime end, String cookieName){
		return false;
	}
}
