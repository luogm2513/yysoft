/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-28
 */
package net.caiban.service.orders.impl;

import java.util.List;

import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.domain.orders.LogisticsDetails;
import net.caiban.domain.orders.Orders;
import net.caiban.domain.orders.SaleDispute;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.OrdersDto;
import net.caiban.facade.ParamFacade;
import net.caiban.persist.orders.LogisticsDetailsDao;
import net.caiban.persist.orders.OrdersDao;
import net.caiban.persist.orders.SaleDisputeDao;
import net.caiban.service.orders.OrdersService;
import net.caiban.service.orders.SaleDisputeService;
import net.caiban.util.Assert;
import net.caiban.util.DateUtil;
import net.caiban.util.IdCreateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("ordersService")
public class OrdersServiceImpl implements OrdersService {
	
	@Autowired
	private OrdersDao ordersDao;
	@Autowired
	private LogisticsDetailsDao logisticsDetailsDao;
	@Autowired
	private SaleDisputeDao saleDisputeDao;
	
	private final static short ORDER_STATUS_DELIVERY = 30;
	private final static short ORDER_STATUS_ARRIVAL = 50;
	
	private final static short ORDER_STATUS_DISPUTE = 40;
	
	@Autowired
	private MessageSource messageSource;
	
	private final static int ORDER_NO_FREE_NUM = 5;

	public Integer batchDeleteOrders(Integer[] ordersArray) {
		Assert.notNull(ordersArray, messageSource.getMessage("assert.notnull", new String[]{"idArray"}, null));
		return ordersDao.batchDeleteOrders(ordersArray);
	}

	public Integer createSimpleOrders(Orders orders) {
		Assert.notNull(orders, messageSource.getMessage("assert.notnull", new String[]{"orders"}, null));
		Assert.notNull(orders.getEid(), messageSource.getMessage("assert.notnull", new String[]{"orders.eid"}, null));
		Assert.notNull(orders.getUid(), messageSource.getMessage("assert.notnull", new String[]{"orders.uid"}, null));
		if(orders.getOrderNo() == null){
			orders.setOrderNo(IdCreateUtil.createId(ORDER_NO_FREE_NUM)+DateUtil.getSecTimeMillis()); 
		}
		if(orders.getOrderStatus()==null){
			orders.setOrderStatus(ordersDao.DEFAULT_ORDER_STATUS);
		}
		if(orders.getPayment()==null){
			orders.setPayment(ordersDao.DEFAULT_PAYMENT);
		}
		if(orders.getBuyerId()==null){
			orders.setBuyerId(ordersDao.DEFAULT_BUYER);
		}
		return ordersDao.createSimpleOrders(orders);
	}

	public OrdersDto listOneOrdersById(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return ordersDao.listOneOrdersById(id);
	}

	public OrdersDto listOneOrdersByOrderNo(String orderNo) {
		Assert.notNull(orderNo, messageSource.getMessage("assert.notnull", new String[]{"orderNo"}, null));
		return ordersDao.listOneOrdersByOrderNo(orderNo);
	}

	final static String ORDERS_STATUS = "orders_status";
	final static String ORDERS_PAYMENT = "orders_payment";
	
	public Paging pageOrdersDto(OrdersDto ordersDto, Paging page) {
		Assert.notNull(ordersDto, messageSource.getMessage("assert.notnull", new String[]{"ordersDto"}, null));
		Assert.notNull(ordersDto.getOrders(), messageSource.getMessage("assert.notnull", new String[]{"ordersDto.orders"}, null));
		Assert.notNull(ordersDto.getOrders().getEid(), messageSource.getMessage("assert.notnull", new String[]{"ordersDto.orders.eid"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		List<OrdersDto> list = ordersDao.pageOrdersDto(ordersDto, page);
		for(OrdersDto o:list){
			if(o.getBuyer() == null){
				o.setBuyer(new EshopBuyer());
			}
			if(o.getOrders().getPayment()!=null){
				o.setPaymentStr(ParamFacade.getInstance().getValue(ORDERS_PAYMENT, o.getOrders().getPayment()));
			}
			if(o.getOrders().getOrderStatus()!=null){
				o.setOrderStatusStr(ParamFacade.getInstance().getValue(ORDERS_STATUS, String.valueOf(o.getOrders().getOrderStatus())));
			}
		}
		page.setRecords(list);
		page.setTotals(ordersDao.countPageOrders(ordersDto));
		return page;
	}

	public void updateOrderStatusById(Short status, Integer id, Integer eid) {
		Assert.notNull(status, messageSource.getMessage("assert.notnull", new String[]{"status"}, null));
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		ordersDao.updateOrderStatusById(status, id, eid);
	}

	public void updateSimpleOrders(Orders orders) {
		Assert.notNull(orders, messageSource.getMessage("assert.notnull", new String[]{"orders"}, null));
		Assert.notNull(orders.getId(), messageSource.getMessage("assert.notnull", new String[]{"orders.id"}, null));
		ordersDao.updateSimpleOrders(orders);
	}

	public Integer arrivalConfirm(LogisticsDetails logisticsDetails) {
		Assert.notNull(logisticsDetails, messageSource.getMessage("assert.notnull", new String[]{"logisticsDetails"}, null));
		Assert.notNull(logisticsDetails.getOrderId(), messageSource.getMessage("assert.notnull", new String[]{"logisticsDetails.orderId"}, null));		
		Assert.notNull(logisticsDetails.getEid(), messageSource.getMessage("assert.notnull", new String[]{"logisticsDetails.eid"}, null));
		
		OrdersDto o = ordersDao.listOneOrdersById(logisticsDetails.getOrderId());
		if(o.getOrders().getOrderStatus().shortValue()>ORDER_STATUS_DISPUTE 
				|| o.getOrders().getOrderStatus().shortValue()<ORDER_STATUS_DELIVERY){
			return null;
		}
		
		logisticsDetailsDao.updateLogisticsDetailsByOrdersId(logisticsDetails);
		return  ordersDao.updateOrderStatusById(ORDER_STATUS_ARRIVAL, logisticsDetails.getOrderId(), logisticsDetails.getEid());
	}

	public Integer deliveryOrders(LogisticsDetails logisticsDetails) {
		Assert.notNull(logisticsDetails, messageSource.getMessage("assert.notnull", new String[]{"logisticsDetails"}, null));
		Assert.notNull(logisticsDetails.getOrderId(), messageSource.getMessage("assert.notnull", new String[]{"logisticsDetails.orderId"}, null));		
		Assert.notNull(logisticsDetails.getEid(), messageSource.getMessage("assert.notnull", new String[]{"logisticsDetails.eid"}, null));		
		Assert.notNull(logisticsDetails.getUid(), messageSource.getMessage("assert.notnull", new String[]{"logisticsDetails.uid"}, null));
		
		OrdersDto o = ordersDao.listOneOrdersById(logisticsDetails.getOrderId());
		if(o.getOrders().getOrderStatus().shortValue()>=ORDER_STATUS_DELIVERY){
			return null;
		}
		
		Integer i = logisticsDetailsDao.createLogisticsDetails(logisticsDetails);
		ordersDao.updateOrderStatusById(ORDER_STATUS_DELIVERY, logisticsDetails.getOrderId(), logisticsDetails.getEid());
		return i;
	}

	public Integer failureOrders(SaleDispute saleDispute) {
		Assert.notNull(saleDispute, messageSource.getMessage("assert.notnull", new String[]{"saleDispute"}, null));
		Assert.notNull(saleDispute.getOrderId(), messageSource.getMessage("assert.notnull", new String[]{"saleDispute.orderId"}, null));
		Assert.notNull(saleDispute.getEid(), messageSource.getMessage("assert.notnull", new String[]{"saleDispute.eid"}, null));
		OrdersDto o = ordersDao.listOneOrdersById(saleDispute.getOrderId());
		if(ORDER_STATUS_DISPUTE == o.getOrders().getOrderStatus().shortValue()){
			return null;
		}
		ordersDao.updateOrderStatusById(ORDER_STATUS_DISPUTE, saleDispute.getOrderId(), saleDispute.getEid());
		return saleDisputeDao.createSaleDispute(saleDispute);
	}

}
