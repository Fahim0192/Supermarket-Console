package model.product;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Product")
public class Product {

	public static final int ONE_QTY = 1;

	@DatabaseField(id = true)
	private String productId;
	
	@DatabaseField(canBeNull = false)
	private String productName;

	@DatabaseField(canBeNull = false)
	private int stockLevel;
	
	@DatabaseField(canBeNull = false)
	private double unitPrice;
	
	@DatabaseField(canBeNull = false)
	private int discountPercent; 	// bulk/wholesale

	@DatabaseField(canBeNull = false)
	private int discountThreshold;
	
	@DatabaseField(canBeNull = false)
	private int promotionPercent;
	
	@DatabaseField(canBeNull = false, foreign = true, columnName = "supplierId")
	private Supplier supplier;
	
	public Product() { }
	
	// the MINIMAL constructor needed to create the product object
	public Product(String productId, String productName, double unitPrice, int stockLevel) {
		this.productId = productId;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.stockLevel = stockLevel;
	}
	
	public Product(String productId, String productName, double unitPrice, int stockLevel, 
			Supplier supplier) {
		this.productId = productId;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.stockLevel = stockLevel;
		this.supplier = supplier;
	}

	// the COMPLETE constructor, preferably call this
	public Product(String productId, String productName, double unitPrice, int stockLevel, Supplier supplier,
		   	int discountPercent, int discountThreshold, int promotionPercent) {
		this.productId = productId;
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.stockLevel = stockLevel;
		this.supplier = supplier;
		this.discountThreshold = discountThreshold;
		this.discountPercent = discountPercent;
		this.promotionPercent = promotionPercent;
	}
	
	public String getProductId() { return productId; }
	public String getProductName() { return productName; }
	public int getStockLevel() { return stockLevel; }
	public Supplier getSupplier( ) { return supplier; }

	/**
	 * Calculation of this Product's price based on the quantity provided.
	 * If the requested quantity is greater than discount threshold then
	 * both promotion and discount percent is applied.
	 * Else if it is lower than threshold, then only apply only the promotion
	 * percentage if there is.
	 *
	 * @param quantity
	 * @return
	 */
	public double getPrice(int quantity) {
		double price = 0;
		if (quantity >= discountThreshold && discountThreshold != 0) {
			price = (unitPrice * 
					((100-discountPercent-promotionPercent)/100.0)) *
					quantity;
		} else {
			price = unitPrice * ((100-promotionPercent)/100.0) * quantity;
		}
		return price;
	}

	// Sets the unit price of this product
	public boolean setUnitPrice(double price) {
		if (price == 0.0) {
			return false;
		}
		unitPrice = price;
		return true;
	}


	// Subtract or increment the quantity of the stock of this product
	public boolean setStockLevel(int quantity) {
		// Don't deduct the quantity if quantity > current stock level
		if (quantity < 0 && this.stockLevel < Math.abs(quantity))
			return false;
		this.stockLevel +=quantity;
		return true;
	}

	// Set discount for this product
	public void setDiscount(int discountPercent, int discountThreshold) {
		this.discountPercent = discountPercent;
		this.discountThreshold = discountThreshold;
	}

	// Set promotion for this product
	public void setPromotion(int promotionPercent) {
		this.promotionPercent = promotionPercent;
		this.unitPrice = unitPrice * (100 - promotionPercent) / 100 ;
	}

	// Set supplier for this product: used for database connection
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String toString() {
		String product = String.format("%-15s %-25s%-7.2f%-7d%-20d%-20d%-20d\n",
				productId, productName, unitPrice, stockLevel, discountPercent,
				discountThreshold, promotionPercent);
		return product;
	}

}


