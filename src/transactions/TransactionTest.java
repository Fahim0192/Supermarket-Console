package transactions;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Customer;
import model.product.Product;

public class TransactionTest {
	
	private Transaction transaction;
	private Customer customer;
	private Product product;

	@Before
	public void setUp() throws Exception {
		Date date = new Date();
		customer = new Customer("s11111", "Name1", 3000);
		product = new Product("0001", "Milk", 1.0, 10);
		transaction = new Transaction(1, date, customer);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addOneOrder() {
		// Test if order is properly addded to the constructor
		assertEquals(0, transaction.getTransactionLines().size());
		transaction.addProduct(product, 5);
		assertEquals(1, transaction.getTransactionLines().size());
	}
	
	@Test
	public void addInvalidOrder() {
		transaction.addProduct(product, 20);
		assertNotEquals(1, transaction.getTransactionLines().size());
	}
	
	@Test
	public void addMultipleOfSameOrder() {
		transaction.addProduct(product, 1);
		transaction.addProduct(product, 5);
		
		assertEquals(1, transaction.getTransactionLines().size());
		assertEquals(0001, transaction.getTransactionLines().get(0).getProductId());
		assertEquals(6, transaction.getTransactionLines().get(0).getQuantity());
	}
	
	@Test
	public void removeOneOrder() {
		transaction.addProduct(product, 2);
		transaction.removeProduct(product.getProductId());
		assertEquals(0, transaction.getTransactionLines().size());
	}

	@Test
	public void transactionDateTest() {
		assertEquals(transaction.getTransactionDate().toString(), new Date().toString());
	}








}
