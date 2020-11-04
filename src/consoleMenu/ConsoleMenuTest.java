/*
 * Author: Wing Cheang Mok
 * Student Number: s3697904
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package consoleMenu;


import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.DatabaseManager;
import model.IntegerException;
import store.LoginManager;

public class ConsoleMenuTest {
	
	private ConsoleMenu menu;
	private LoginManager loginManager;
	private DatabaseManager dbManager;

	@Before
	public void setUp() throws IntegerException{
		menu = new ConsoleMenu();
		dbManager = new DatabaseManager();
		loginManager = new LoginManager(dbManager.getEmployees(), dbManager.getCustomers());
	}
	
	/**
	 * Warehouse Staff - Replenish Stock
	 * Manager - Set Promotion
	 * Manager - Set Discount (Bulk)
	 */
	@Test (expected = IntegerException.class)
	public void negativeQuantity() throws IntegerException, SQLException{
		menu.printMenu();
	}
	
	@After
	public void tearDownClass() throws Exception{
		
	}

}
