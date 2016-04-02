package database;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class StringParser {
	
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
}
