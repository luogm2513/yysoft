/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-23
 */
package net.caiban.dto.orders;

import java.io.Serializable;

import net.caiban.domain.orders.OrdersProduct;
import net.caiban.domain.product.Product;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class OrdersProductDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6891848830893335808L;

	private OrdersProduct ordersProduct;
	private Product product;
	/**
	 * @return the ordersProduct
	 */
	public OrdersProduct getOrdersProduct() {
		return ordersProduct;
	}
	/**
	 * @param ordersProduct the ordersProduct to set
	 */
	public void setOrdersProduct(OrdersProduct ordersProduct) {
		this.ordersProduct = ordersProduct;
	}
	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
