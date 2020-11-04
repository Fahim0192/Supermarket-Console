/*
 * Author: Jefferson Madrid, Fahim Tahmeed
 * Student Number: s3707189,
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package report;

import java.util.*;

import model.Customer;
import model.product.Product;
import model.product.ProductStockComparator;
import store.Store;
import transactions.TransactionLine;
import transactions.Transaction;

public class Report {

	private List<Transaction> transactions;
	private Store store;
	
	public Report(Store store,List<Transaction> transactions) {
		this.store = store;
		this.transactions = transactions;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void addTransaction(Transaction transaction) {
		transactions.add(0, transaction);
	}

	/**
	 * Generates Sales Report of the Transactions within the Dates specified
	 * in the parameters.
	 *
	 * @return
	 */
	public String generateSalesReport(Date from, Date to) {
		String result = "Transactions between "+ from.toString() + " and "+ to + "\n";

		for(Transaction currentTrans: transactions) // will repeat for every transaction
		{
			if(currentTrans.getTransactionDate().getDate() == from.getDate() &&
					currentTrans.getTransactionDate().getMonth() == from.getMonth() &&
					currentTrans.getTransactionDate().getYear() == from.getYear())
				result+= "\n"+currentTrans.toString();
			else if(currentTrans.getTransactionDate().getDate() == to.getDate() &&
					currentTrans.getTransactionDate().getMonth() == to.getMonth() &&
					currentTrans.getTransactionDate().getYear() == to.getYear())
				result+= "\n"+currentTrans.toString();
			else if(currentTrans.getTransactionDate().after(from) &&
					currentTrans.getTransactionDate().before(to))
				result+= "\n"+currentTrans.toString();
			
		}

		return result;
	}

	/**
	 * Generates Sales Report based only on Transactions with customers
	 * living on the specified postcode.
	 */
	public String generateSalesReport(int postCode) {
		String result = "";
		for(Transaction currentTrans: transactions)
		{
			Customer c = currentTrans.getCustomer();
			if (c.getPostcode() == postCode)
				result += "\n" + currentTrans.toString();
		}
		return result;
	}

	/**
	 * Generates Fast Moving Products Report: these are the products with the
	 * highest amounts of restocking value. Gets data from restockHistory Map
	 * inside Store to see the record of restock amounts of each product.
	 */
	public String generateFastProductsReport() {
		String result = String.format("\n\n____________________%s____________________", 
				"Fast Products Report");
		result += String.format("\n%-17s%-25s%s\n",
				"ProductID", "Product Name", "Amount Restocked");
		result += String.format("------------------------------------------------------------\n");

		Map<String, Integer> history = new HashMap<>(store.getRestockHistory());
		Map<String, Integer> sorted = new LinkedHashMap<>();

		// Loop through copy of restockHistory until its empty
		// Finds and arranges products based on their restock amounts
		while (history.size() > 0) {
			String maxRestockedId = "";
			int maxRestockedAmount = Integer.MIN_VALUE;

			for (Map.Entry<String, Integer> entry : history.entrySet()) {
				if (entry.getValue() > maxRestockedAmount) {
					maxRestockedId = entry.getKey();
					maxRestockedAmount = entry.getValue();
				}
			}
			sorted.put(maxRestockedId, maxRestockedAmount);
			history.remove(maxRestockedId);
		}

		for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
			Product product = store.getProductById(entry.getKey());
			result += String.format("%-17s%-25s%d\n",
					product.getProductId(), product.getProductName(),
					entry.getValue());
		}

		return result;
	}

	/**
	 * Generates a string of all the Products in the Store and list their:
	 * - product IDs
	 * - product names
	 * - current stock level
	 * - supplier ID
	 * - supplier name
	 *
	 * This is for the Manager to keep track who the supplier is for each
	 * individual products.
	 */
	public String generateProductsSupply() {
		String result = String.format("\n\n____________________________%s____________________________", 
				"Products Supply Report");
		List<Product> sortedProducts = new ArrayList<>(store.getProducts());
		result += String.format("\n%-17s%-25s%-10s%-13s%s\n",
				"ProductID", "Product Name", "Quantity",
				"SupplierID", "Supplier Name");
		result += String.format("------------------------------------------------------------------------------\n");

		sortedProducts.sort(new ProductStockComparator());
		Collections.reverse(sortedProducts);

		for(Product p : sortedProducts) {
			result += String.format("%-17s%-25s%-10s%-13s%s\n",
					p.getProductId(), p.getProductName(), p.getStockLevel(),
					p.getSupplier().getSupplierId(), p.getSupplier().getSupplierName());
		}
		return result;
	}

	/**
	 * Simply prints out all the recorded Transactions in the Report
	 */
	public String generateSalesReport() {
		String result = "";
		for(Transaction currentTrans: transactions) {
			result +=  currentTrans.toString() + "\n";
		}
		return result;
	}

	/**
	 * Finds out which products generated the greatest Revenue for the business.
	 * Makes use of HashMaps and LinkedHashMap for an ordered data structure to
	 * store the Products in a meaningful way.
	 * For all products sold show the:
	 * - product ID
	 * - product name
	 * - revenue generated
	 * - quantities sold
	 */
	public String generateRevenueReport() {
		String result = String.format("\n\n____________________________%s____________________________", 
				"Revenue Report");
		result += String.format("\n%-17s%-25s%-15s%s\n",
				"ProductID", "Product Name", "Revenue($)", "QuantitySold");
		result += String.format("----------------------------------------------------------------------\n");

		// Records the revenue and quantity of each product
		Map<String, Double> revenueMap = new HashMap<>();
		Map<String, Integer> quantityMap = new HashMap<>();

		// LinkedHashMap to store properly sorted products based on revenue generated
		Map<String, Double> sortedRevenueMap = new LinkedHashMap<>();

		// Arranges the products based on their produced revenue
		for (Transaction transaction : transactions) {
			for (TransactionLine transactionLine : transaction.getTransactionLines()) {
				String productId = transactionLine.getProductId();
				double price = transactionLine.getTotalPrice();
				int quantity = transactionLine.getQuantity();

				revenueMap.put(productId, revenueMap.getOrDefault(productId, 0.0) + price);
				quantityMap.put(productId, quantityMap.getOrDefault(productId, 0) + quantity);
			}
		}

		while (revenueMap.size() > 0) {
			String maxRevenueId = "";
			double maxRevenueAmount = Double.MIN_VALUE;

			for (Map.Entry<String, Double> entry : revenueMap.entrySet()) {
				if (entry.getValue() > maxRevenueAmount) {
					maxRevenueId = entry.getKey();
					maxRevenueAmount = entry.getValue();
				}
			}
			sortedRevenueMap.put(maxRevenueId, maxRevenueAmount);
			revenueMap.remove(maxRevenueId);
		}

		for (Map.Entry<String, Double> entry : sortedRevenueMap.entrySet()) {
			Product product = store.getProductById(entry.getKey());
			result += String.format("%-17s%-25s%-15.2f%d\n",
					product.getProductId(), product.getProductName(),
					entry.getValue(), quantityMap.get(entry.getKey()));
		}

		return result;
	}
	
}
