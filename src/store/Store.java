/*
 * Author: Sohee Kim
 * Student Number: s3720307
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package store;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.IntegerException;
import model.product.Product;
import model.product.Supplier;
import report.Report;
import transactions.Transaction;

public class Store {

	public static final int INITIAL_STOCK = 100;

	private List<Product> products;
	private List<Supplier> suppliers;

	// string = product id, integer = increase level
	private Map<String, Integer> restockHistory = new HashMap<>();
	private Report report;
	private Transaction transaction;
	private Date simulatedDate;

	// Constructor used for unit testing
	public Store() {
		report = new Report(this,null);
		simulatedDate = new Date();
		transaction = new Transaction();
	}

	// Constructor that should be called to initialise the products + transactions inside report
	public Store(List<Product> products, List<Supplier> suppliers, List<Transaction> transactions) {
		this.products = products;
		this.suppliers = suppliers;

		report = new Report(this,transactions);
		simulatedDate = new Date();
		initialiseTransaction();

		// Initialise the restock history so it can have stuff inside without actual restocking product
		for (Product product : products)
			restockHistory.put(product.getProductId(), INITIAL_STOCK - product.getStockLevel());
	}

	// Initialises transaction in complete transaction
	private void initialiseTransaction() {
		simulatedDate = new Date();
		int numberOfTransaction = report.getTransactions().size();
		transaction = new Transaction(numberOfTransaction, simulatedDate);
	}

	public Report getReport() {
		return report;
	}

	public List<Product> getProducts() {
		return products;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public Map<String, Integer> getRestockHistory() {
		return restockHistory;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public Date getDate() {
		return simulatedDate;
	}

	// Add product to the list of products
	public void addProduct(String productId, String productName, int stockLevel,
			double unitPrice) {
		products.add(new Product(productId, productName,
				unitPrice, stockLevel));
	}

	// Checks if productId exist on the Store, before adding the product in the Transaction
	public boolean addItemOnTransaction(String productId, int quantity) {
		if (getProductById(productId) == null) {
			return false;
		} else {
			return transaction.addProduct(getProductById(productId), quantity);
		}
	}

	// Remove item on transaction - calls Transaction.removeProduct(..) method
	public boolean removeItemOnTransaction (String productId) {
		return transaction.removeProduct(productId);
	}

	// Complete current transaction and initialise new transaction
	public double completeTransaction() {
		double totalCost = 0;
		if (transaction.getTransactionLines().size() != 0) {
			// Get total cost
			try {
				totalCost = transaction.finaliseTransaction();
			} catch (IntegerException e) {
			}
			// Adds the completed transaction to the Report object
			report.addTransaction(transaction);
			// Initialise a new transaction object
			initialiseTransaction();
		}
		return totalCost;
	}

	public boolean restockProduct(String productId, int stockLevel) {
		if (getProductById(productId) == null) {
			return false;
		} else {
			restockHistory.put(productId, stockLevel);
			return getProductById(productId).setStockLevel(stockLevel);
		}
	}

	public boolean setProductPrice(String productId, double unitPrice) {
		Product product = getProductById(productId);
		if (product != null) {
			return product.setUnitPrice(unitPrice);
		}
		return false;
	}

	public Product getProductById(String productId) {
		for (Product product : products) {
			if (productId.equals(product.getProductId())) {
				return product;
			}
		}
		return null;
	}

	public String printAllProducts() {
		String output = "";
		for (Product product: products) {
			output += product.toString();
		}
		return output;
	}

	public String printMostRecentTransaction() {
		return report.getTransactions().size() > 0 ?
				report.getTransactions().get(0).toString() :
				"Store has no previous transaction.";
	}

}
