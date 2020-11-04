package model.employee;

import com.j256.ormlite.field.DatabaseField;

public class Employee {
	
	@DatabaseField(id = true)
	protected String employeeId;
	
	@DatabaseField(canBeNull = false)
	protected String name;

	
	public Employee() { }
	
	public Employee(String employeeId, String name) {
		this.employeeId = employeeId;
		this.name = name;
	}
	
	public String getEmployeeId() { return employeeId; }
	public String getName() { return name; }

}
