/*
 * Author: Wing Cheang Mok
 * Student Number: s3697904
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package store;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Customer;
import model.employee.Employee;
import model.employee.ManagerEmp;
import model.employee.SalesEmp;
import model.employee.WarehouseEmp;

public class LoginManagerTest {
	
	private LoginManager loginManager;
	private Customer customer1;
	private Customer customer2;
	private Employee salesStaff1;
	private Employee salesStaff2;
	private Employee warehouseStaff1;
	private Employee warehouseStaff2;
	private Employee manager1;
	private Employee manager2;
	private List<Employee> employees;
	private List<Customer> customers;
	
	@Before
	public void setUp() {
		
		customer1 = new Customer("c001", "Dave", 3000);
		customer2 = new Customer("c002", "Trevor", 3051);
		
		customers = new ArrayList<Customer>();
		customers.add(customer1);
		customers.add(customer2);
		
		salesStaff1 = new SalesEmp("s001", "Jackson");
		salesStaff2 = new SalesEmp("s002", "Ben");
		
		warehouseStaff1 = new WarehouseEmp("w005", "Jean");
		warehouseStaff2 = new WarehouseEmp("w006", "Daniel");
		
		manager1 = new ManagerEmp("m005", "Alvin");
		manager2 = new ManagerEmp("m010", "Dale");
		
		employees = new ArrayList<Employee>();
		employees.add(salesStaff1);
		employees.add(salesStaff2);
		employees.add(warehouseStaff1);
		employees.add(warehouseStaff2);
		employees.add(manager1);
		employees.add(manager2);
		
		loginManager = new LoginManager(employees, customers);
	}
	
	//Check is there valid customer
	@Test
	public void checkCustomer() {
		assertTrue(loginManager.loginCustomer("c001"));
		assertFalse(loginManager.loginCustomer("c010"));
	}
	
	//Check is there valid sales employee
	@Test
	public void checkSalesEmployee() {
		assertTrue(loginManager.loginEmployee("s001"));
		assertFalse(loginManager.loginEmployee("s009"));
		assertFalse(loginManager.loginEmployee("s011"));
	}
	
	//Check is there valid warehouse employee
	@Test
	public void checkWarehouseEmployee() {
		assertTrue(loginManager.loginEmployee("w005"));
		assertFalse(loginManager.loginEmployee("w001"));
	}
	
	//Check is there valid manager
	@Test
	public void checkManagerEmployee() {
		assertTrue(loginManager.loginEmployee("m010"));
		assertTrue(loginManager.loginEmployee("m005"));
		assertFalse(loginManager.loginEmployee("m003"));
	}
	
	//Check all the get methods
	@Test
	public void getMethod() {
		assertTrue(loginManager.loginCustomer("c001"));
		assertEquals(customer1, loginManager.getLoggedInCustomer());
		assertNotSame(customer2, loginManager.getLoggedInCustomer());
		
		assertEquals(customers, loginManager.getCustomers());
		
		assertTrue(loginManager.loginEmployee("s002"));
		assertEquals(salesStaff2, loginManager.getLoggedInEmployee());
		assertNotSame(salesStaff1, loginManager.getLoggedInCustomer());
		
		assertTrue(loginManager.loginEmployee("w005"));
		assertEquals(warehouseStaff1, loginManager.getLoggedInEmployee());
		assertNotSame(warehouseStaff2, loginManager.getLoggedInCustomer());
		
		assertTrue(loginManager.loginEmployee("m010"));
		assertEquals(manager2, loginManager.getLoggedInEmployee());
		assertNotSame(manager1, loginManager.getLoggedInCustomer());
		
		assertEquals(employees, loginManager.getEmployees());	
	}
	
	@After 
	public void tearDownClass() throws Exception{
		
	}
}
