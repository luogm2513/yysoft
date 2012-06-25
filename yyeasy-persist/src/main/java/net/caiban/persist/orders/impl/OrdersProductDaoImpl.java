/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-7-3
 */
package net.caiban.persist.orders.impl;

import java.util.List;

import net.caiban.domain.orders.OrdersProduct;
import net.caiban.dto.orders.OrdersProductDto;
import net.caiban.persist.orders.OrdersProductDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("ordersProductDao")
public class OrdersProductDaoImpl extends SqlMapClientDaoSupport implements
		OrdersProductDao {
	
	@Autowired
	private MessageSource messageSource;

	public Integer clearOrdersProductByOrdersId(Integer ordersid) {
		Assert.notNull(ordersid, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		return getSqlMapClientTemplate().delete("ordersProduct.clearOrdersProductByOrdersId", ordersid);
	}

	public Integer createOrdersProduct(OrdersProduct ordersProduct) {
		Assert.notNull(ordersProduct, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		Assert.hasText(String.valueOf(ordersProduct.getOrdersId()), messageSource.getMessage("assert.hasText", new String[]{"ordersProduct.ordersId"}, null));
		Assert.hasText(String.valueOf(ordersProduct.getProductId()), messageSource.getMessage("assert.hasText", new String[]{"ordersProduct.productId"}, null));
		return (Integer) getSqlMapClientTemplate().insert("ordersProduct.createOrdersProduct", ordersProduct);
	}

	public List<OrdersProductDto> listOrdersProductByOrdersId(Integer ordersid) {
		Assert.notNull(ordersid, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		return getSqlMapClientTemplate().queryForList("ordersProduct.listOrdersProductByOrdersId", ordersid);
	}

}
