/*
 * Author: Jefferson Madrid, Wing Cheang Mok
 * Student Number: s3707189, s3697904
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */
package data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import model.Customer;
import model.employee.Employee;
import model.employee.ManagerEmp;
import model.employee.SalesEmp;
import model.employee.WarehouseEmp;
import model.product.Product;
import model.product.Supplier;
import transactions.TransactionLine;
import transactions.Transaction;

public class DatabaseManager {
	private static ConnectionSource connSource;

	//These are the connection between the tables on the DB and the Java classes, note: Dao objects are iterable
	private static Dao<Customer, String> customerDao;
	private static Dao<SalesEmp, String> salesEmpDao;
	private static Dao<WarehouseEmp, String> warehouseEmpDao;
	private static Dao<ManagerEmp, String> managerEmpDao;
	private static Dao<Supplier, String> supplierDao;
	private static Dao<Product, Integer> productDao;
	private static Dao<Transaction, Integer> transactionDao;
	private static Dao<TransactionLine, Void> transactionLineDao;

	/**
	 * Main Constructor which initialises the Dao objects:
	 * Database Access Objects - direct connection with the tables.
	 */
	public DatabaseManager() {
		// Line below disables debugging log in console
		System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");

		System.out.println("Loading driver...");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}

		// Connection strings + username + password (for both localhost and remote DB)
//		String hostname = "jdbc:mysql://127.0.0.1:3306/sefdb?useUnicode=true&u" +
//				"seJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		String hostname = "jdbc:mysql://mydbinstance.caqv3m11ydtb.ap-southeast-2.rds.amazonaws.com:3306/sefdb";
		String username = "student"; //root for local database, student for aws rds
		String password = "abcde12345";
		
		try {
			connSource = new JdbcConnectionSource(hostname, username, password);

			customerDao = DaoManager.createDao(connSource, Customer.class);
			salesEmpDao = DaoManager.createDao(connSource, SalesEmp.class);
			warehouseEmpDao = DaoManager.createDao(connSource, WarehouseEmp.class);
			managerEmpDao = DaoManager.createDao(connSource, ManagerEmp.class);
			productDao = DaoManager.createDao(connSource, Product.class);
			supplierDao = DaoManager.createDao(connSource, Supplier.class);
			transactionDao = DaoManager.createDao(connSource, Transaction.class);
			transactionLineDao = DaoManager.createDao(connSource, TransactionLine.class);
			
			createAllTables();
//			deleteAllTables();
//			seedData();
			
			connSource.close();
		} catch (SQLException e) {
			System.err.println("SQLException -> caught in DatabaseManager constructor");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException -> caught in DatabaseManager constructor");
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Creates Tables on the database with the specified schema by the Java model classes
	 */
	public void createAllTables() throws SQLException {
		TableUtils.createTableIfNotExists(connSource, Customer.class);
		TableUtils.createTableIfNotExists(connSource, SalesEmp.class);
		TableUtils.createTableIfNotExists(connSource, WarehouseEmp.class);
		TableUtils.createTableIfNotExists(connSource, ManagerEmp.class);
		TableUtils.createTableIfNotExists(connSource, Supplier.class);
		TableUtils.createTableIfNotExists(connSource, Product.class);
		TableUtils.createTableIfNotExists(connSource, TransactionLine.class);
		TableUtils.createTableIfNotExists(connSource, Transaction.class);
	}
	
	/**
	 * Reads the Customer table from database and creates a
	 * List of Customer objects with it
	 * @return the list of Customers
	 */
	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList<>();
		for (Customer customer: customerDao) 
			customers.add(customer);
		return customers;
	}

	/**
	 * Reads the 3 tables (SalesEmp, WarehouseEmp, ManagerEmp)
	 * for different employee roles and creates objects with it
	 * @return the list of employees from the database
	 */
	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<>();
		for (SalesEmp salesEmp: salesEmpDao)
			employees.add(salesEmp);
		for (WarehouseEmp warehouseEmp: warehouseEmpDao)
			employees.add(warehouseEmp);
		for (ManagerEmp managerEmp: managerEmpDao)
			employees.add(managerEmp);
		return employees;
	}
	
	/**
	 * Reads the Supplier table from the database and creates a List of Suppliers with it
	 * @return the list of Suppliers
	 */
	public List<Supplier> getSuppliers() {
		List<Supplier> suppliers = new ArrayList<>();
		for (Supplier supplier: supplierDao) 
			suppliers.add(supplier);
		return suppliers;
	}
	
	/**
	 * Reads the Product table and since it only saves the String supplierId
	 * of its supplier. The supplierId is used to query over the Supplier table
	 * to get the correct corresponding Supplier object. This Supplier object is then
	 * set to the Product object.
	 * @return the list of products from the database
	 * @throws SQLException
	 */
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<>();
		
		// For each of the Product row, find the Supplier associated with it
		for (Product product : productDao) {
			try {
				Supplier supplier = supplierDao.queryForId(product.getSupplier().getSupplierId());
				product.setSupplier(supplier);
			} catch (SQLException e) {
				System.err.println("Error: Could not find Supplier in the database.");
			}
			products.add(product);
		}
		return products;
	}

	/**
	 * Reads the Transaction table directly and queries TransactionLine
	 * and Customer tables to produce the Transaction object.
	 * @return the list of Transactions from database
	 * @throws SQLException
	 */
	public List<Transaction> getTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		
		for (Transaction transaction : transactionDao) {
			try {
				transaction.setTransactionLines(transactionLineDao.queryForEq("transactionId", transaction.getTransactionId()));
				transaction.setCustomer(customerDao.queryForId(transaction.getCustomer().getCustomerId()));
			} catch (SQLException e) {
				System.err.println("Error: Could not find Transaction in the database.");
			}
			
			transactions.add(transaction);
		}
		return transactions;
	}

	/**
	 * Public method to delete all row from all Tables on the database
	 * @throws SQLException
	 */
	public void deleteAllTables() throws SQLException {
		for (Customer customer: customerDao) 						{ customerDao.delete(customer); }
		for (SalesEmp salesEmp: salesEmpDao) 						{ salesEmpDao.delete(salesEmp); }
		for (WarehouseEmp warehouseEmp: warehouseEmpDao) 			{ warehouseEmpDao.delete(warehouseEmp); }
		for (ManagerEmp managerEmp: managerEmpDao) 					{ managerEmpDao.delete(managerEmp); }
		for (Supplier supplier: supplierDao) 						{ supplierDao.delete(supplier); }
		for (Product product: productDao) 							{ productDao.delete(product); }
		for (TransactionLine transactionLine: transactionLineDao) 	{ transactionLineDao.delete(transactionLine); }
		for (Transaction transaction: transactionDao) 				{ transactionDao.delete(transaction); }
	}

	/**
	 * Public method to update the tables on the database.
	 * Uses generic types to reduce code duplication.
	 * @param datas the list of objects to be saved
	 * @param <E> classes of Data in the system
	 * @throws SQLException
	 */
	public <E> void updateData(List<E> datas) throws SQLException {
		for (E data: datas) {
			if (data instanceof Customer)
				customerDao.createOrUpdate((Customer) data);
			else if (data instanceof SalesEmp)
				salesEmpDao.createOrUpdate((SalesEmp) data);
			else if (data instanceof WarehouseEmp)
				warehouseEmpDao.createOrUpdate((WarehouseEmp) data);
			else if (data instanceof ManagerEmp)
				managerEmpDao.createOrUpdate((ManagerEmp) data);
			else if (data instanceof Supplier)
				supplierDao.createOrUpdate((Supplier) data);
			else if (data instanceof Product)
				productDao.createOrUpdate((Product) data);
			else if (data instanceof TransactionLine)
				transactionLineDao.createOrUpdate((TransactionLine) data);
			else if (data instanceof Transaction)
				transactionDao.createOrUpdate((Transaction) data);
		}
	}

	/**
	 * Method to seed the data/objects/rows. Used primarily for testing, debugging
	 * and lazy initialising big data.
	 */
	private void seedData() {
		Customer c1 = new Customer("c001", "James", 3000);
		Customer c2 = new Customer("c002", "Jake", 3000);
		Customer c3 = new Customer("c003", "Amy", 3000);
		Customer c4 = new Customer("c004", "Julie", 3000);
		Customer c5 = new Customer("c005", "Yona", 3000);
		Customer c6 = new Customer("c006", "Jean", 3000);
		Customer c7 = new Customer("c007", "Fourth", 3000);
		Customer c8 = new Customer("c008", "Michael", 3000);
		List<Customer> customers = new ArrayList<Customer>() {{
			add(c1); add(c2); add(c3); add(c4); add(c5); add(c6);
			add(c7); add(c8);
		}};
		
		List<Employee> employees = new ArrayList<Employee>() {{
			add(new SalesEmp("e008", "Nina"));
			add(new WarehouseEmp("e005", "John"));
			add(new ManagerEmp("e001", "Big Boss"));
		}};
		
		Supplier s1 = new Supplier("s001", "Heinz");
		Supplier s2 = new Supplier("s002", "Mondelez");
		Supplier s3 = new Supplier("s003", "Nescafe");
		Supplier s4 = new Supplier("s004", "Coles");
		Supplier s5 = new Supplier("s005", "Coca-Cola");
		Supplier s6 = new Supplier("s006", "BBQ Sauce");
		Supplier s7 = new Supplier("s007", "a2 Milk");
		Supplier s8 = new Supplier("s008", "Fonterra");
		Supplier s9 = new Supplier("s009", "Lion Nathan");
		Supplier s10 = new Supplier("s010", "Woolworths");
		Supplier s11 = new Supplier("s011", "Arnott's");
		Supplier s12 = new Supplier("s012", "Uncle Tobys");
		Supplier s13 = new Supplier("s013", "Twinings");
		Supplier s14 = new Supplier("s014", "PepsiCo");
		Supplier s15 = new Supplier("s015", "Nestle");
		Supplier s16 = new Supplier("s016", "Cadbury");
		List<Supplier> suppliers = new ArrayList<Supplier>() {{
			add(s1); add(s2); add(s3); add(s4); add(s5); add(s6); add(s7); add(s8); add(s9); add(s10);
			add(s11); add(s12); add(s13); add(s14); add(s15); add(s16);
		}};
		
		Product p1 = new Product("9557725908001", "Tomato Ketchup", 3.10, 25, s1, 0, 0, 0);
		Product p2 = new Product("9314057002049", "Oreo", 2.0, 30, s2, 10, 20, 0);
		Product p3 = new Product("9300650028944", "Coffee", 3.5, 30, s3, 20, 5, 0);
		Product p4 = new Product("9300633592721", "Full Cream Milk", 2.0, 30, s4, 0, 0, 0);
		Product p5 = new Product("8410076601285", "Coke", 3.20, 30, s5, 10, 24, 5);
		Product p6 = new Product("9323015016332", "BBQ Sauce", 1, 30, s6, 0, 0, 0);
		Product p7 = new Product("9310055598447", "Chicken Fillet", 3.10, 25, s10, 0, 0, 0);
		Product p8 = new Product("9310988014724", "Tim Tam", 2.0, 30, s4, 0, 0, 0);
		Product p9 = new Product("9310434001612", "a2 Milk", 3.5, 30, s7, 10, 5, 0);
		Product p10 = new Product("9310179006712", "Ice Cream", 2.0, 30, s10, 0, 0, 0);
		Product p11 = new Product("9310072029597", "Biscuits", 5.00, 20, s11, 15, 10, 0);
		Product p12 = new Product("9300605048522", "Roll-Ups", 2.80, 20, s12, 0, 0, 0);
		Product p13 = new Product("9310060406942", "Muesli Bar", 2.00, 10, s12, 0, 0, 10);
		Product p14 = new Product("070177077389", "Orange Tea", 2.00, 10, s13, 10, 5, 0);
		Product p15 = new Product("070177077372", "Apple Tea", 2.00, 10, s13, 0, 0, 5);
		Product p16 = new Product("070177078959", "Lemon Tea", 2.00, 10, s13, 0, 0, 0);
		Product p17 = new Product("070177287269", "Strawberry Tea", 2.00, 10, s13, 10, 5, 0);
		Product p18 = new Product("070177287306", "Camomile Tea", 2.00, 10, s13, 0, 0, 0);
		Product p19 = new Product("070177225667", "Watermelon Infuse", 2.00, 10, s13, 0, 0, 10);
		Product p20 = new Product("9313820001784", "Mountain Dew", 2.00, 10, s14, 0, 0, 5);
		Product p21 = new Product("0228341005861", "Chicken drumsticks", 5.86, 20, s4, 0, 0, 10);
		Product p22 = new Product("9300605042544", "KitKat", 4.50, 10, s15, 0, 0, 0);
		Product p23 = new Product("9300617075509", "Dairy Milk", 3.00, 15, s16, 30, 20, 0);
		Product p24 = new Product("9310015243783", "Twisties", 2.00, 20, s14, 0, 0, 0);
		List<Product> products = new ArrayList<Product>() {{
			add(p1); add(p2); add(p3); add(p4); add(p5); add(p6); add(p7); add(p8); add(p9); add(p10);
			add(p11); add(p12); add(p13); add(p14); add(p15); add(p16); add(p17); add(p18); add(p19);
			add(p20); add(p21); add(p22); add(p23); add(p24);
		}};

		// Seed it to the database
		try {
			updateData(customers);
			updateData(employees);
			updateData(suppliers);
			updateData(products);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	
	
}
