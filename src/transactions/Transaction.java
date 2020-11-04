/*
 * Author: Jefferson Madrid
 * Student Number: s3707189
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */
package transactions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import model.Customer;
import model.IntegerException;
import model.product.Product;

@DatabaseTable(tableName = "Transaction")
public class Transaction {

	@DatabaseField(id = true)
	private int transactionId;

	@DatabaseField(canBeNull = false)
	private Date transactionDate;

	@DatabaseField(canBeNull = false, foreign = true, columnName = "customerId")
	private Customer customer;

	private List<TransactionLine> transactionLines = new ArrayList<>();

	@DatabaseField(canBeNull = false)
	private double totalCost;
	
	@DatabaseField(canBeNull = false)
	private double discountedCost;
	
	
	public Transaction() { }

	public Transaction(int transactionId, Date transactionDate) {
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
	}

	public Transaction(int transactionId, Date transactionDate, Customer customer) {
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.customer = customer;
	}
	
	public Date getTransactionDate() { return transactionDate; }
	public int getTransactionId() { return transactionId; }
	public Customer getCustomer() { return customer; }
	public List<TransactionLine> getTransactionLines() { return transactionLines; }
	public double getTotalCost() { return totalCost; }
	public double getDiscountedCost() { return discountedCost; }

	// Sets the customer of this Transaction
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	// Sets the list of transaction line: only used in database connection
	public void setTransactionLines(List<TransactionLine> transactionLines) {
		this.transactionLines = transactionLines;
	}

	/**
	 * Complete/Finalise the transaction: this changes the state of this Transaction
	 * from not complete to completed, and is then transferred to the List of
	 * Transactions inside the Report class.
	 * Also calculates if the Customer has any points:
	 * - if points > 20, gets $5 discount every 20 points
	 * - if points < 20, gain points: 1 point for every $10
	 *
	 * @return the discountedCost, or totalCost if the same
	 */
	public double finaliseTransaction() throws IntegerException {
		totalCost = 0;
		for (TransactionLine order: transactionLines) {
			totalCost += order.getTotalPrice();
		}
		
		if (customer.getPoints() >= 20) {
			int points = customer.getPoints();
			// discounted $5 for every 20 points
			discountedCost = totalCost - (points/20) * 5;
			// reduce used points of the customer
			customer.reducePoints(points/20);
		} else {
			customer.setPoints(totalCost);
			discountedCost = totalCost;
		}

		return discountedCost;
	}

	/**
	 * Adds the product to this transaction. Preliminary checks if product
	 * is allowed to be bought:
	 * - if the requested quantity does not exceed the stock level of the product
	 *
	 * Also updates the amount of TransactionLine as a result of the Product being added.
	 *
	 * @return boolean if successfully added or not
	 */
	public boolean addProduct(Product product, int quantity) {

		//Deduct the quantity first and if it doesn't pass this method
		//then the quantity is greater than current stock level - NOT POSSIBLE
		if (product.setStockLevel(-quantity)) {
			int correctQty = quantity;

			//First checks if the product has been added,
			//checks if quantity is < stock level, if true, increase order quantity
			for (TransactionLine order: transactionLines)
				if (order.getProductId() == product.getProductId()) {
					correctQty += order.getQuantity();
					order.setQuantity(correctQty);
					order.setTotalPrice(product.getPrice(correctQty));
					return true;
				}

			if (correctQty == 1) {
				TransactionLine order = new TransactionLine(transactionId, product.getProductId(),
						product.getProductName(), correctQty, product.getPrice(correctQty));
				transactionLines.add(order);
			}
			return true;
		}
		return false;
	}

	/**
	 * Removes the product in the Transaction
	 * @param productId
	 * @return boolean if successfully removed or not
	 */
	public boolean removeProduct(String productId) {
		for (int i = 0; i < transactionLines.size(); i++) {
			if (transactionLines.get(i).getProductId().equals(productId)) {
				transactionLines.remove(i);
				return true;
			}
		}
		System.err.println("ERROR: Product is not currently in the transaction.");
		return false;
	}

	/**
	 * Method used for presenting the details of the Transaction
	 * including all TransactionLines, Final Cost and any Discount Cost.
	 */
	public String toString()
	{
		String output = "____________________________________________________________\n";
		if (discountedCost == 0) {
			output += "________________________TRANSACTION_________________________\n";
		} else {
			output += "__________________________INVOICE____________________________\n";
		}

		output += String.format("Date: %s\n", transactionDate.toString());
		output += String.format("TransactionID: %s\n", transactionId);
		output += String.format("CustomerID: %s\nName: %s\n", customer.getCustomerId(), customer.getCustomerName());

		output += String.format("\n%-17s %-20s %-10s\t %s\n", "Product ID", "Product Name", "Quantity", "Total");

		if (transactionLines.size() != 0) {
			totalCost = 0; // reset price?? bad

			for (TransactionLine order : transactionLines) {
				output += order.toString();
				totalCost += order.getTotalPrice();
			}

			output += String.format("\nTotal Cost: $%.2f\n", totalCost);
			output += discountedCost == 0 ?
					String.format("Final Cost (discount applied): only applied only after transaction is finalised.\n") :
					String.format("Final Cost (discount applied): $%.2f\n", discountedCost);
		} else
			output += "No items in the transaction/cart.\n\n";
		return output;
	}

}
