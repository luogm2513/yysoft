/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-7-3
 */
package net.caiban.persist.orders;

import java.util.List;

import net.caiban.domain.orders.OrdersProduct;
import net.caiban.dto.orders.OrdersProductDto;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface OrdersProductDao {
	
	/**
	 * 为订单增加产品信息
	 * @param ordersProduct:待增加的产品信息,不能为null<br/>
	 * 		ordersProduct.productId:产品ID,不能为null<br/>
	 * 		ordersProduct.ordersId:订单ID,不能为null<br/>
	 * 		ordersProduct.priceDiscount:折扣<br/>
	 * 		ordersProduct.quantity:数量
	 * @return
	 */
	public Integer createOrdersProduct(OrdersProduct ordersProduct);
	
	/**
	 * 清除与订单关联的产品
	 * @param ordersid:订单ID号
	 * @return
	 */
	public Integer clearOrdersProductByOrdersId(Integer ordersid);
	
	/**
	 * 通过订单ID号获取该订单的所有产品信息
	 * @param ordersid:订单ID号，不能为null
	 * @return
	 */
	public List<OrdersProductDto> listOrdersProductByOrdersId(Integer ordersid);
}
