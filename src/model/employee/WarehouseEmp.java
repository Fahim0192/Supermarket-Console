package model.employee;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "WarehouseEmp")
public class WarehouseEmp extends Employee{

	public WarehouseEmp() { }
	
	public WarehouseEmp(String employeeId, String name) 
	{
		super(employeeId, name);
	}

}
