package model.product;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProductTest {
	private Product product;
	
	@Before
	public void setUpClass() {
		product = new Product("014", "Doritos", 4.5, 10);
		product.setUnitPrice(5.5);
		product.setStockLevel(50);
	}
	
	@Test
	public void price() {
		assertEquals(5.5, product.getPrice(1),0.0001);
		
	}
	@Test
	public void stockLevel() {
		assertEquals(60, product.getStockLevel());
	
	}
	@Test
	public void getPriceWithDiscount() {
		assertEquals(27.5, product.getPrice(5),0.0001);
	}

}
