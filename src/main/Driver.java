package main;

import java.sql.SQLException;
import consoleMenu.ConsoleMenu;
import model.IntegerException;

public class Driver {

	public static void main(String[] args) throws SQLException, IntegerException {

		ConsoleMenu console = new ConsoleMenu();
		console.printMenu();
	}

}
