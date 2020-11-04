/*
 * Author: Wing Cheang Mok
 * Student Number: s3697904
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package store;

import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.employee.Employee;

public class LoginManager {
	
	private List<Employee> employees = new ArrayList<Employee>();
	private List<Customer> customers = new ArrayList<Customer>();
	private Employee loggedInEmployee;
	private Customer loggedInCustomer;
	
	public LoginManager(List<Employee> employees, List<Customer> customers) {
		this.employees = employees;
		this.customers = customers;
	}
	
	/**
	 * Method to check is there valid customer ID 
	 * Finds out the customer from the input 
	 * exists inside the customer list
	 */
	public boolean loginCustomer(String customerId){
		for(int i = 0; i < customers.size(); i++){
			if(customers.get(i).getCustomerId().equals(customerId)){
				loggedInCustomer = customers.get(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method to check is there valid employee ID 
	 * Finds out if the employee from the input
	 * exists inside the employee list
	 */
	public boolean loginEmployee(String employeeId){
		for(int i = 0; i < employees.size(); i++){
			if(employees.get(i).getEmployeeId().equals(employeeId)){
				loggedInEmployee = employees.get(i);
				return true;
			}
		}
		return false;
	}

	public Customer getLoggedInCustomer(){
		return loggedInCustomer;
	}
	
	public Employee getLoggedInEmployee(){
		return loggedInEmployee;
	}
	
	public List<Employee> getEmployees() {
		return employees;
	}

	public List<Customer> getCustomers() {
		return customers;
	}
	
}
