/*
 * Author: Wing Cheang Mok
 * Student Number: s3697904
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Customer")
public class Customer {
	
	@DatabaseField(id = true)
	private String customerID;
	
	@DatabaseField(canBeNull = false)
	private String name;
	
	@DatabaseField(canBeNull = false)
	private int points;
	
	@DatabaseField(canBeNull = false)
	private int postcode;

	public Customer() { }
	
	public Customer(String customerID, String name, int postcode){
		this.name = name;
		this.customerID = customerID;
		this.postcode = postcode;
		points = 0;
	}

	public String getCustomerId(){
		return customerID;
	}

	public String getCustomerName(){
		return name;
	}

	public int getPoints(){
		return points;
	}

	public int getPostcode(){
		return postcode;
	}
	
	/**
	 * Method to set points for the customers
	 * If the price is less than or equal to 0
	 * - exception will be thrown
	 * else
	 * - it will set the points based on the total price
	 */
	public void setPoints(double totalPrice) throws IntegerException{
		if(totalPrice <= 0){
			throw new IntegerException("Total Price should not be less than or equal to 0!");
		}
		else{
			int price = (int) totalPrice;
			int amount = price / 10;
			points += amount;
		}
	}

	/**
	 * Method to reduce points for the customers 
	 * If the amount of points to be reduced is less than or equal to 0
	 * - exception will be thrown
	 * If the customer's point is less than the amount of points to be reduced
	 * - exception will be thrown
	 * else
	 * - reduce the customer's point based on the amount of points to be reduced
	 */
	public void reducePoints(int amount) throws IntegerException{	
		if(amount <= 0){
			throw new IntegerException("Points should not be less than or equal to 0!");
		}
		else if(amount > points){
			throw new IntegerException("Customer's points should more than the input points");
		}
		else {
			points -= amount;
		}
	}
}
