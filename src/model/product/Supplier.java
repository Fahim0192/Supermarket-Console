package model.product;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Supplier")
public class Supplier {
	
	@DatabaseField(id = true)
	private String supplierId;
	
	@DatabaseField(canBeNull = false)
	private String supplierName;
	
	public Supplier() { }
	
	public Supplier(String supplierId, String supplierName) {
		this.supplierId = supplierId;
		this.supplierName = supplierName;
	}

	public String getSupplierId() {
		return supplierId;
	}
	
	public String getSupplierName() {
		return supplierName;
	}
}
