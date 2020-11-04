/*
 * Author: Wing Cheang Mok
 * Student Number: s3697904
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package model;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CustomerTest {
	
	private Customer customer1;
	private Customer customer2;
	private Customer customer3;
	private Customer customer4;
	
	@Before
	public void setUp() throws IntegerException{
		// set points
		customer1 = new Customer("c0001", "Jeff", 3001);
		customer1.setPoints(400);
		
		// set and reduce
		customer2 = new Customer("c0002", "Mok", 3000);
		customer2.setPoints(500);
		
		// set and reduce, error expected
		customer3 = new Customer("c0003", "Sohee", 3002);
		customer3.setPoints(10);
		
		customer4 = new Customer("c0004", "Fahim", 3051);
		customer4.setPoints(50);
	}
	
	//Normal get method
	@Test
	public void setGet() {
		assertEquals(40, customer1.getPoints());
	}
	
	//Reduce points method
	@Test
	public void setReduceGet() throws IntegerException {
		customer2.reducePoints(20);
		assertEquals(30, customer2.getPoints());
	}
	
	//Reduce negative method
	@Test (expected = IntegerException.class)
	public void setReduceGetException() throws IntegerException{
		customer3.reducePoints(-20);
	}
	
	//Reduce 0 point method
	@Test (expected = IntegerException.class)
	public void setReduceGet0Exception() throws IntegerException{
		customer4.reducePoints(0);
	}
	
	//Calculation for setPoints method using totalPrice
	@Test
	public void setPoints() throws IntegerException{
		customer4.setPoints(100.0);
		assertEquals(15, customer4.getPoints());
	}
	
	@After
	public void tearDownClass() throws Exception {
		
	}
}
