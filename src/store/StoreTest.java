package store;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.employee.Employee;
import model.product.Product;

public class StoreTest {
	private Store store = new Store();
	private List<Product> products;
	private List<Employee> employees;
	private Product p1;
	private Product p2;
	private Employee e1;
	private Employee e2;

	@Before
	public void setUp() throws Exception {
		p1 = new Product("111", "product 1", 4.50, 111, null);
		p2 = new Product("222", "product 2", 7.50, 222, null);

		store.getProducts().add(p1);
		store.getProducts().add(p2);

//		products = store.products;
//		employees = store.employees;
//		e1 = new Employee("empid1", "employee 1", "role1");
//		e2 = new Employee("empid2", "employee 2", "role2");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void isEmployeeExistTest() {
		employees.add(e1);
		employees.add(e2);
//		assertTrue(store.isEmployeeExist("empid1"));
//		assertTrue(store.isEmployeeExist("empid2"));
//		assertFalse(store.isEmployeeExist("empid3"));
	}

	@Test
	public void getEmployeeByIdTest() {
		employees.add(e1);
		employees.add(e2);
//		assertEquals(store.getEmployeeById("empid1"), e1);
//		assertEquals(store.getEmployeeById("empid2"), e2);
//		assertEquals(store.getEmployeeById("empid3"), null);
	}

	@Test
	public void getProductByIdTest() {
		assertEquals(store.getProductById("111"), p1);
		assertEquals(store.getProductById("222"), p2);
		assertEquals(store.getProductById("333"), null);

	}

	@Test
	public void checkDateTest() {
		assertEquals(store.getDate().toString(), new Date().toString());
	}


	@Test
	public void completeTransactionTest() {
		store.completeTransaction();
		assertNull(store.getTransaction());
	}
}
