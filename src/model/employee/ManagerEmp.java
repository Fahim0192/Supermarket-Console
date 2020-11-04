package model.employee;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ManagerEmp")
public class ManagerEmp extends Employee{

	public ManagerEmp() { }
	
	public ManagerEmp(String employeeId, String name) 
	{
		super(employeeId, name);
	}

}
