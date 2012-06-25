/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-7-3
 */
package net.caiban.service.orders.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import net.caiban.domain.orders.OrdersProduct;
import net.caiban.dto.orders.OrdersProductDto;
import net.caiban.persist.orders.OrdersProductDao;
import net.caiban.service.orders.OrdersProductService;
import net.caiban.util.Assert;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("ordersProductService")
public class OrdersProductServiceImpl implements OrdersProductService {
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private OrdersProductDao ordersProductDao;

	public Integer clearOrdersProductByOrdersId(Integer ordersid) {
		Assert.notNull(ordersid, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		return ordersProductDao.clearOrdersProductByOrdersId(ordersid);
	}

	public Integer createOrdersProduct(OrdersProduct ordersProduct) {
		Assert.notNull(ordersProduct, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		Assert.hasText(String.valueOf(ordersProduct.getOrdersId()), messageSource.getMessage("assert.hasText", new String[]{"ordersProduct.ordersId"}, null));
		Assert.hasText(String.valueOf(ordersProduct.getProductId()), messageSource.getMessage("assert.hasText", new String[]{"ordersProduct.productId"}, null));
		return ordersProductDao.createOrdersProduct(ordersProduct);
	}

	public List<OrdersProductDto> listOrdersProductByOrdersId(Integer ordersid) {
		Assert.notNull(ordersid, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		return ordersProductDao.listOrdersProductByOrdersId(ordersid);
	}

}
