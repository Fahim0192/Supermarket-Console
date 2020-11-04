/*
 * Author: Jefferson Madrid
 * Student Number: s3707189
 * Course: ISYS1118 Software Engineering Fundamentals
 * Assignment: Supermarket Support System
 */

package model.product;

import java.util.Comparator;

/**
 * Comparator used to sort the Products based on their stock level
 */
public class ProductStockComparator implements Comparator<Product> {
	@Override
	public int compare(Product o1, Product o2) {
		return o1.getStockLevel()- o2.getStockLevel();
	}
}
