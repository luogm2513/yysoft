/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-30
 */
package net.caiban.persist.orders.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.domain.orders.Orders;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.OrdersDto;
import net.caiban.exception.PersistLayerException;
import net.caiban.persist.orders.OrdersDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("ordersDao")
public class OrdersDaoImpl extends SqlMapClientDaoSupport implements OrdersDao {
	
	@Autowired
	private MessageSource messageSource;

	public Integer batchDeleteOrders(Integer[] ordersArray) {
		Assert.notNull(ordersArray, messageSource.getMessage("assert.notnull", new String[]{"idArray"}, null));
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
				for(Integer i:ordersArray){
					Map<String, Object> root = new HashMap<String, Object>();
					root.put("id", i);
					root.put("status", ORDER_STATUS_DELETE_MAX);
					if(getSqlMapClientTemplate().delete("orders.deleteOrdersById",root)>0){
						impact+=1;
						getSqlMapClientTemplate().delete("ordersProduct.clearOrdersProductByOrdersId",i);
					}
					
				}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer countPageOrders(OrdersDto ordersDto) {
		Assert.notNull(ordersDto, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		Assert.notNull(ordersDto.getOrders(), messageSource.getMessage("assert.notnull", new String[]{"ordersDto.orders"}, null));
		Assert.notNull(ordersDto.getOrders().getEid(), messageSource.getMessage("assert.notnull", new String[]{"ordersDto.orders.eid"}, null));
		Map<String,Object> root=new HashMap<String, Object>();
		root.put("dto",ordersDto);
		return (Integer) getSqlMapClientTemplate().queryForObject("orders.countPageOrders",root);
	}

	public Integer createSimpleOrders(Orders orders) {
		Assert.notNull(orders, messageSource.getMessage("assert.notnull", new String[]{"orders"}, null));
		Assert.notNull(orders.getEid(), messageSource.getMessage("assert.notnull", new String[]{"orders.eid"}, null));
		Assert.notNull(orders.getUid(), messageSource.getMessage("assert.notnull", new String[]{"orders.uid"}, null));
		return (Integer) getSqlMapClientTemplate().insert("orders.createSimpleOrders",orders);
	}

	public OrdersDto listOneOrdersById(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return (OrdersDto) getSqlMapClientTemplate().queryForObject("orders.listOneOrdersById",id);
	}

	public OrdersDto listOneOrdersByOrderNo(String orderNo) {
		Assert.notNull(orderNo, messageSource.getMessage("assert.notnull", new String[]{"orderNo"}, null));
		return (OrdersDto) getSqlMapClientTemplate().queryForObject("orders.listOneOrdersByOrderNo",orderNo);
	}

	@SuppressWarnings("unchecked")
	public List<OrdersDto> pageOrdersDto(OrdersDto ordersDto, Paging page) {
		Assert.notNull(ordersDto, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		Assert.notNull(ordersDto.getOrders(), messageSource.getMessage("assert.notnull", new String[]{"ordersDto.orders"}, null));
		Assert.notNull(ordersDto.getOrders().getEid(), messageSource.getMessage("assert.notnull", new String[]{"ordersDto.orders.eid"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("dto",ordersDto);
		root.put("page",page);
		return getSqlMapClientTemplate().queryForList("orders.pageOrdersDto",root);
	}

	public Integer updateOrderStatusById(Short status, Integer id, Integer eid) {
		Assert.notNull(status, messageSource.getMessage("assert.notnull", new String[]{"status"}, null));
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("status", status);
		root.put("id", id);
		root.put("eid", eid);
		return getSqlMapClientTemplate().update("orders.updateOrderStatusById",root);
	}

	public void updateSimpleOrders(Orders orders) {
		Assert.notNull(orders, messageSource.getMessage("assert.notnull", new String[]{"orders"}, null));
		Assert.notNull(orders.getId(), messageSource.getMessage("assert.notnull", new String[]{"orders.id"}, null));
		getSqlMapClientTemplate().update("orders.updateSimpleOrders",orders);
	}

}
