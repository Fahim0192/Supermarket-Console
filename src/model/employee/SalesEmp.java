package model.employee;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "SalesEmp")
public class SalesEmp extends Employee {

	public SalesEmp() { }
	
	public SalesEmp(String employeeId, String name) 
	{
		super(employeeId, name);
	}

}
