/*
 * Author: Jefferson Madrid
 * Student Number: s3707189
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */
package transactions;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "TransactionLine")
public class TransactionLine {

	// Artificial ID required to store to the database
	@DatabaseField(generatedId = true)
	private int uniqueId;
	
	@DatabaseField(canBeNull = false, uniqueCombo = true)
	private int transactionId;
	
	@DatabaseField(canBeNull = false, uniqueCombo = true)
	private String productId;

	@DatabaseField(canBeNull = false)
	private String productName;

	@DatabaseField(canBeNull = false)
	private int quantity;

	@DatabaseField(canBeNull = false)
	private double totalPrice;

	// Parameterless Constructor needed for creation of this Class through ORM
	public TransactionLine() { }
	
	public TransactionLine (int transactionId, String productId, String productName,
							int quantity, double totalPrice) {
		this.transactionId = transactionId;
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}
	
	public int getTransactionId() {
		return transactionId;
	}

	public String getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return String.format("%-17s %-20s %-10d\t %.2f\t\n",
				productId, productName, quantity, totalPrice);
	}
}
