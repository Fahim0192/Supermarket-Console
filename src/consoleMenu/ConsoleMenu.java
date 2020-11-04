/*
 * Author: Wing Cheang Mok
 * Student Number: s3697904
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package consoleMenu;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import data.DatabaseManager;
import model.IntegerException;
import model.employee.ManagerEmp;
import model.employee.SalesEmp;
import model.employee.WarehouseEmp;
import store.LoginManager;
import store.Store;
import transactions.Transaction;

public class ConsoleMenu {

	private Store store;
	private LoginManager loginManager;
	private Scanner scanner = new Scanner(System.in);
	private String access = "";
	private DatabaseManager dbManager;

	public ConsoleMenu() {
		dbManager = new DatabaseManager();
		loginManager = new LoginManager(dbManager.getEmployees(), dbManager.getCustomers());
		store = new Store(dbManager.getProducts(), dbManager.getSuppliers(), dbManager.getTransactions());
	}

	/**
	 * Basic menu for the supermarket support system
	 * The system will prompt the user to select the type of users
	 * The type of users:
	 * - Customer
	 * - Staff
	 * and exit option
	 */
	public void printMenu() throws SQLException, IntegerException{
		System.out.println("__________Supermarket System Menu__________");
		System.out.println("Please select one of list!");
		System.out.println("1. Customer");
		System.out.println("2. Staff");
		System.out.println("3. Exit");
		System.out.print("Your choice: ");
		int selection = 0;
		try {
			selection = scanner.nextInt();
			}
		catch (InputMismatchException e) {
			System.err.println("Please input a valid number");
			scanner.nextLine();
			System.out.println();printMenu();
		}
		switch(selection) {
		
		case 1:
			logInCustomerMenu();
			break;
		
		case 2: 
			logInEmployeeMenu();
			break;
			
		case 3:
			safeExit();
			break;
			
		default:
			System.out.println("Error! Please input a valid number!");
			break;
		}
		printMenu();
	}

	/**
	 * Method to prompt the details of customers 
	 * validate check the customer to login
	 * and set the customer of the transaction to that logged in customer 
	 */
	public void logInCustomerMenu() throws SQLException, IntegerException{
		System.out.println("-------------------------");
		System.out.print("Please enter Customer ID: ");
		String id = scanner.next();
		if(loginManager.loginCustomer(id) == true){
			store.getTransaction().setCustomer(loginManager.getLoggedInCustomer());
			System.out.println("-------------------------");
			System.out.println("Welcome back " + loginManager.getLoggedInCustomer().getCustomerName() + "!");
			System.out.println("Access right: Customer");
			customerMenu();
		}
		else{
			System.out.println("Customer ID is not found! Please try again!");
			logInCustomerMenu();
		}
	}

	/**
	 * Method to handle the customer menu
	 * The cutomers can:
	 * - add products to transaction
	 * - remove products from transaction
	 * - print all products of store
	 * - print products in transaction
	 * - complete transaction
	 * - request help from sales employee
	 * - exit the program
	 */
	public void customerMenu() throws SQLException, IntegerException{
		System.out.println("-------------------------");
		System.out.println("1. Add Product to Transaction");
		System.out.println("2. Remove Product from Transaction");
		System.out.println("3. Print All Products of Store");
		System.out.println("4. Print Products in Transaction");
		System.out.println("5. Complete Transaction");
		System.out.println("6. Request Help from Sales Employee");
		System.out.println("7. Exit");
		System.out.print("Your choice: ");
		int selection = 0;
		try {
			selection = scanner.nextInt();
			scanner.nextLine();
			}
		catch (InputMismatchException e) {
			System.err.println("Please input a valid number");
			scanner.nextLine();
			System.out.println();
			customerMenu();
		}

		switch(selection) {
		
		case 1: 
			printProducts();
			System.out.print("Please input the product ID: ");
			String productId = scanner.nextLine();
			if(store.getProductById(productId) == null){
				System.out.println("Product ID not found... Please try again!");
			}
			else {
				boolean done = store.addItemOnTransaction(productId, 1);
				if(done == true){
					System.out.println("Successfully added the product!");
				}
				else{
					System.out.println("Problem occurred while adding the product... Please try again!");
				}
			}
			break;
			
		case 2:
			System.out.println(store.getTransaction().toString());
			System.out.print("Please input the product ID to remove product: ");
			String product = scanner.nextLine();
			if(store.getProductById(product) == null){
				System.out.println("Product ID not found... Please try again!");
			}
			else{
				boolean done = store.removeItemOnTransaction(product);
				if(done == true){
					System.out.println("Successfully removed the product!");
				} else {
					System.out.println("Problem occurred while removing the products... Please try again!");
				}
			}
			break;
			
		case 3:
			printProducts();
			System.out.println("Proceeding back to the Customer Menu...");
			break;
			
		case 4:
			System.out.println(store.getTransaction().toString());
			break;
		
		case 5:
			if (store.completeTransaction() != 0) {
				System.out.println("Completing transaction...");
				System.out.println(store.printMostRecentTransaction());
			} else {
				System.out.println("No item in the transaction...");
			}
			System.out.println("Logging out..\n\n");
			printMenu();
			break;
			
		case 6:
			logInEmployeeMenu();
			break;
			
		case 7:
			safeExit();
			break;
			
		default:
			System.out.println("Please input a valid number!");
			break;
		}
		customerMenu();
	}

	/**
	 * Method to prompt the details of employees 
	 * validate check the employees to login
	 */
	public void logInEmployeeMenu() throws SQLException, IntegerException{
		System.out.println("-------------------------");
		System.out.print("Please enter Employee ID: ");
		String id = scanner.next();

		if(loginManager.loginEmployee(id) == true){
			if(loginManager.getLoggedInEmployee() instanceof SalesEmp){
				access = "Sales Employee";
			}
			else if(loginManager.getLoggedInEmployee() instanceof WarehouseEmp){
				access = "Warehouse Employee";
			}
			else if(loginManager.getLoggedInEmployee() instanceof ManagerEmp){
				access = "Manager";
			}

			System.out.println("-------------------------");
			System.out.println("Welcome back " + loginManager.getLoggedInEmployee().getName() + "!");
			System.out.println("Access right: " + access);
			if(access.equals("Sales Employee")){
				salesEmployeeMenu();
			}
			else if(access.equals("Warehouse Employee")){
				warehouseEmployeeMenu();
			}
			else if(access.equals("Manager")){
				managerMenu();
			}
			else{
				System.out.println("Invalid access right! Please try again");
				logInEmployeeMenu();
			}
		}
		else{
			System.out.println("Staff ID is not found! Please try again!");
			logInEmployeeMenu();
		}
	}

	/**
	 * Method to handle the sales employee menu
	 * The sales employee can:
	 * - remove product from transaction
	 * - print all products of store
	 * - print products in transaction
	 * - exit the program
	 */
	public void salesEmployeeMenu() throws SQLException, IntegerException{
		System.out.println("-----------------------");
		System.out.println("1. Remove Product from Transaction");
		System.out.println("2. Print All Products of Store");
		System.out.println("3. Print Products in Transaction");
		System.out.println("4. Exit");
		System.out.print("Your choice: ");
		int selection = 0;
		try {
			selection = scanner.nextInt();
			}
		catch (InputMismatchException e) {
			System.err.println("Please input a valid number");
			scanner.nextLine();
			System.out.println();
			salesEmployeeMenu();
		}

		switch(selection){
			case 1:
				if(loginManager.getLoggedInCustomer() != null){
					System.out.println(store.getTransaction().toString());
					System.out.print("Please input the product ID to remove product: ");
					scanner.nextLine();
					String product = scanner.nextLine();
					if(store.getProductById(product) == null){
						System.out.println("Product ID not found... Please try again!");
					}
					else{
						boolean done = store.removeItemOnTransaction(product);
						if(done == true){
							System.out.println("Successfully removing the product!");
						}
						else{
							System.out.println("Problems occur while removing the products... Please try again!");
						}
					}
				}
				else {
					System.out.println("Customer is not logged in...");
				}
				break;

			case 2:
				printProducts();
				System.out.println("Proceeding back to the Sales Employee Menu...");
				break;
				
			case 3:
				if(loginManager.getLoggedInCustomer() != null) {
					System.out.println(store.getTransaction().toString());
				}
				else {
					System.out.println("Customer is not logged in...");
				}
				break;
				
			case 4:
				if(loginManager.getLoggedInCustomer() != null) {
					System.out.println("Exiting Sales Employee Menu! Proceeding back to the Customer Menu...");
					customerMenu();
				}
				else {
					System.out.println("Exiting the application...");
					safeExit();
				}
				break;

			default:
				System.out.println("Please input a valid number!");
				break;
		}
		salesEmployeeMenu();
	}

	/**
	 * Method to handle the warehouse employee menu
	 * The warehouse employee can:
	 * - replenish stock
	 * - print all products of store
	 * - exit the program
	 */
	public void warehouseEmployeeMenu() throws SQLException, IntegerException{
		System.out.println("-----------------------");
		System.out.println("1. Replenish Stock");
		System.out.println("2. Print All Products of Store");
		System.out.println("3. Exit");
		System.out.print("Your choice: ");
		int selection = 0;
		try {
			selection = scanner.nextInt();
			}
		catch (InputMismatchException e) {
			System.err.println("Please input a valid number");
			scanner.nextLine();
			System.out.println();
			warehouseEmployeeMenu();
		}

		switch(selection){
			case 1:
				printProducts();
				System.out.print("Please input productId of that product to replenish stock: ");
				scanner.nextLine();
				String stock = scanner.nextLine();
				if(store.getProductById(stock) == null){
					System.out.println("Product is not found... Please try again!");
				}
				else{
					System.out.print("Please input the quantity: ");
					int stockLevel = 0;
					try {
						stockLevel = scanner.nextInt();
						boolean checkQuantity = errorIntegerMessage(stockLevel);
						if(checkQuantity == true){
							boolean done = store.restockProduct(stock, stockLevel);
							if(done == true){
								System.out.println("Success Replenish Stock");
							}
							else{
								System.out.println("Problems occur while replenish stocks... Please try again!");
							}
						}
					}
					catch (IntegerException e) {
						System.err.println(e.getMessage());
						scanner.nextLine();
						warehouseEmployeeMenu();
					}
					catch (InputMismatchException e) {
						System.err.println("Please input a valid number!");
						scanner.nextLine();
						warehouseEmployeeMenu();
					}
				}
				break;
				
			case 2:
				printProducts();
				System.out.println("Proceeding back to the Warehouse Employee Menu...");
				break;

			case 3:
				safeExit();
				break;

			default:
				System.out.println("Please input a valid number!");
				break;
		}
		warehouseEmployeeMenu();
	}

	/**
	 * Method to handle the manager menu
	 * The manager can:
	 * - set promotion
	 * - set discount for bulk products
	 * - print all products of store
	 * - generate revenue report
	 * - generate sales report: all transactions in report
	 * - generate sales report (postcode)
	 * - generate sales report (date)
	 * - generate fast-moving products report
	 * - generate product supply report
	 * - exit the program
	 */
	public void managerMenu() throws SQLException, IntegerException{
		System.out.println("-----------------------");
		System.out.println("1. Set Promotion");
		System.out.println("2. Set Discount (Bulk)");
		System.out.println("3. Print All Products of Store");
		System.out.println("4. Generate Revenue Report");
		System.out.println("5. Generate Sales Report: All Transactions in Report");
		System.out.println("6. Generate Sales Report (Postcode)");
		System.out.println("7. Generate Sales Report (Date)");
		System.out.println("8. Generate Fast-Moving Products Report");
		System.out.println("9. Generate Product Supply Report");
		System.out.println("10. Exit");
		System.out.print("Your choice: ");
		int selection = 0;
		try {
			selection = scanner.nextInt();
			}
		catch (InputMismatchException e) {
			System.err.println("Please input a valid number!");
			System.out.println();
			scanner.nextLine();
			managerMenu();
		}
		
		switch(selection){
			case 1:
				printProducts();
				System.out.print("Please input the product ID: ");
				scanner.nextLine();
				String productId = scanner.nextLine();
				if(store.getProductById(productId) == null){
					System.out.println("Product is not found... Please try again!");
				}
				else{
					System.out.print("Please input the percentage for promotion: ");
					int promotionPercentage = 0;
					try {
						promotionPercentage = scanner.nextInt();
						boolean checkPercentage = errorIntegerMessage(promotionPercentage);
						if(checkPercentage == true){
							store.getProductById(productId).setPromotion(promotionPercentage);
							System.out.println("Successfully set the promotion!");
						}
					}
					catch (IntegerException e) {
						System.err.println(e.getMessage());
						scanner.nextLine();
						managerMenu();
					}
					catch (InputMismatchException e) {
						System.err.println("Please input a valid number!");
						scanner.nextLine();
						managerMenu();
					}
				}
				break;

			case 2:
				printProducts();
				System.out.print("Please input the product ID: ");
				scanner.nextLine();
				String product = scanner.nextLine();
				if(store.getProductById(product) == null){
					System.out.println("Product is not found... Please try again!");
				}
				else{
					System.out.print("Please input the percentage for bulk discount: ");
					int discountPercentage = 0;
					
					try {
						discountPercentage = scanner.nextInt();
						boolean checkPercentage = errorIntegerMessage(discountPercentage);
						if(checkPercentage == true)
						{
							System.out.print("Please input the threshold for bulk discount: ");
						}
					}
					catch (IntegerException e) {
						System.err.println("The number should not be less than or equal to 0!");
						System.out.println();
						scanner.nextLine();
						managerMenu();
					}
					catch (InputMismatchException e) {
						System.err.println("Please input a valid number!");
						System.out.println();
						scanner.nextLine();
						managerMenu();
					}
					
					int discountThreshold = 0;
					try {
							discountThreshold = scanner.nextInt();
							boolean checkThreshold = errorIntegerMessage(discountThreshold);
							if(checkThreshold == true){
								System.out.println();
								store.getProductById(product).setDiscount(discountPercentage, discountThreshold);
								System.out.println("Successfully set the discount!");	
							}
						}
					catch (IntegerException e) {
						System.err.println("The number should not be less than or equal to 0!");
						System.out.println();
						scanner.nextLine();
						managerMenu();
					}
					catch (InputMismatchException e) {
						System.err.println("Please input a valid number!");
						System.out.println();
						scanner.nextLine();
						managerMenu();
					}
				}
				break;

			case 3:
				printProducts();
				break;

			case 4:
				System.out.println("Generating Revenue Report...");
				System.out.println(store.getReport().generateRevenueReport());
				break;
				
			case 5:
				System.out.println("Generating Sales Report: All Transactions in Report...");
				System.out.println(store.getReport().generateSalesReport());
				break;
			
			case 6:
				System.out.print("Please insert postcode: ");
				int postcode = scanner.nextInt();
				System.out.println("Generating Sales Report (Postcode)");
				System.out.println(store.getReport().generateSalesReport(postcode));
				break;
				
			case 7:
				System.out.print("Please insert start date (dd/mm/yyyy): ");
				scanner.nextLine();
				String start = scanner.nextLine();
				Date startDate = dateFormat(start);
				System.out.println();
				System.out.print("Please insert end date (dd/mm/yyyy): ");
				String end = scanner.nextLine();
				Date endDate = dateFormat(end);
				System.out.println("Generating Sales Report (Date)...");
				System.out.println(store.getReport().generateSalesReport(startDate, endDate));
				break;
				
			case 8:
				System.out.println("Generating Fast-Moving Products Report...");
				System.out.println(store.getReport().generateFastProductsReport());
				break;
				
			case 9:
				System.out.println("Generating Product Supply Report...");
				System.out.println(store.getReport().generateProductsSupply());
				break;

			case 10:
				safeExit();
				break;

			default:
				System.out.println("Please input a valid number!");
				break;
		}
		managerMenu();
	}

	/**
	 * Method to print all products of the store
	 * - set the alignment and header 
	 * - print the products from the store
	 */
	public void printProducts(){
		System.out.println();
		System.out.println("______________________________________________" +
				"List of Products_________________________________________________");
		System.out.printf("%-15s %-25s%-7s%-7s%-20s%-20s%-20s\n", "Product ID",
				"Product Name", "Price", "Qty", "Bulk Discount (%)", "Bulk Threshold", "Promotion (%)");
		System.out.println("-----------------------------------------------" +
				"----------------------------------------------------------------");
		System.out.println(store.printAllProducts());
	}


	/**
	 * Method to throw an integer message 
	 * If the input is less than or equal to 0
	 * - will throw exception
	 */
	public boolean errorIntegerMessage(int input) throws IntegerException{
		if(input <= 0){
			throw new IntegerException("The number should not be less than or equal to 0!");
		}
		return true;
	}
	
	/**
	 * Method to return the date from string to Date format
	 */
	public Date dateFormat(String dateString) {
		String[] dateArray = dateString.split("/");
		int[] dateInt = new int[3];
		for(int i = 0; i < dateArray.length; i++) {
			dateInt[i] = Integer.parseInt(dateArray[i]);	
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, dateInt[2]);
		cal.set(Calendar.MONTH, dateInt[1]-1);
		cal.set(Calendar.DAY_OF_MONTH, dateInt[0]);
		Date date = cal.getTime();
		return date;
	}


	/**
	 * Method to update all the tables in the database when exiting the program
	 * The order to update is:
	 * - update the customers' details
	 * - update the employees' details
	 * - update the suppliers' details
	 * - update the products' details
	 * - update the transaction's line details
	 * - update the transactions' details
	 */
	public void safeExit() throws SQLException{
		dbManager.deleteAllTables();
		dbManager.updateData(loginManager.getCustomers());
		dbManager.updateData(loginManager.getEmployees());
		dbManager.updateData(store.getSuppliers());
		dbManager.updateData(store.getProducts());
		for (Transaction t : store.getReport().getTransactions())
			dbManager.updateData(t.getTransactionLines());
		dbManager.updateData(store.getReport().getTransactions());
		System.out.println("Exiting the application...");
		System.exit(0);
	}
}
