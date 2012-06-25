/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-23
 */
package net.caiban.dto.orders;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.domain.orders.Orders;
import net.caiban.domain.orders.SaleDispute;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class OrdersDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1521372417480555241L;
	private Orders orders;
	private EshopBuyer buyer;
	private SaleDispute saleDispute;
	
	private List<OrdersProductDto> ordersProductList;
	private List<LogisticsDetailsDto> logisticsDetailsList;
	
	private Date gmtcreateMin;
	private Date gmtcreateMax;
	private Date gmtOrderMin;
	private Date gmtOrderMax;
	private Float totalOrderCashMin;
	private Float totalOrderCashMax;
	private Float totalProductCashMin;
	private Float totalProductCashMax;
	
	private String orderStatusStr;
	private String paymentStr;
	
	private Short[] orderStatusArray;
	
	/**
	 * @return the orders
	 */
	public Orders getOrders() {
		return orders;
	}
	/**
	 * @param orders the orders to set
	 */
	public void setOrders(Orders orders) {
		this.orders = orders;
	}
	/**
	 * @return the buyer
	 */
	public EshopBuyer getBuyer() {
		return buyer;
	}
	/**
	 * @param buyer the buyer to set
	 */
	public void setBuyer(EshopBuyer buyer) {
		this.buyer = buyer;
	}
	/**
	 * @return the saleDispute
	 */
	public SaleDispute getSaleDispute() {
		return saleDispute;
	}
	/**
	 * @param saleDispute the saleDispute to set
	 */
	public void setSaleDispute(SaleDispute saleDispute) {
		this.saleDispute = saleDispute;
	}
	/**
	 * @return the ordersProductList
	 */
	public List<OrdersProductDto> getOrdersProductList() {
		return ordersProductList;
	}
	/**
	 * @param ordersProductList the ordersProductList to set
	 */
	public void setOrdersProductList(List<OrdersProductDto> ordersProductList) {
		this.ordersProductList = ordersProductList;
	}
	/**
	 * @return the logisticsDetailsList
	 */
	public List<LogisticsDetailsDto> getLogisticsDetailsList() {
		return logisticsDetailsList;
	}
	/**
	 * @param logisticsDetailsList the logisticsDetailsList to set
	 */
	public void setLogisticsDetailsList(
			List<LogisticsDetailsDto> logisticsDetailsList) {
		this.logisticsDetailsList = logisticsDetailsList;
	}
	/**
	 * @return the gmtcreateMin
	 */
	public Date getGmtcreateMin() {
		return gmtcreateMin;
	}
	/**
	 * @param gmtcreateMin the gmtcreateMin to set
	 */
	public void setGmtcreateMin(Date gmtcreateMin) {
		this.gmtcreateMin = gmtcreateMin;
	}
	/**
	 * @return the gmtcreateMax
	 */
	public Date getGmtcreateMax() {
		return gmtcreateMax;
	}
	/**
	 * @param gmtcreateMax the gmtcreateMax to set
	 */
	public void setGmtcreateMax(Date gmtcreateMax) {
		this.gmtcreateMax = gmtcreateMax;
	}
	/**
	 * @return the totalOrderCashMin
	 */
	public Float getTotalOrderCashMin() {
		return totalOrderCashMin;
	}
	/**
	 * @param totalOrderCashMin the totalOrderCashMin to set
	 */
	public void setTotalOrderCashMin(Float totalOrderCashMin) {
		this.totalOrderCashMin = totalOrderCashMin;
	}
	/**
	 * @return the totalOrderCashMax
	 */
	public Float getTotalOrderCashMax() {
		return totalOrderCashMax;
	}
	/**
	 * @param totalOrderCashMax the totalOrderCashMax to set
	 */
	public void setTotalOrderCashMax(Float totalOrderCashMax) {
		this.totalOrderCashMax = totalOrderCashMax;
	}
	/**
	 * @return the totalProductCashMin
	 */
	public Float getTotalProductCashMin() {
		return totalProductCashMin;
	}
	/**
	 * @param totalProductCashMin the totalProductCashMin to set
	 */
	public void setTotalProductCashMin(Float totalProductCashMin) {
		this.totalProductCashMin = totalProductCashMin;
	}
	/**
	 * @return the totalProductCashMax
	 */
	public Float getTotalProductCashMax() {
		return totalProductCashMax;
	}
	/**
	 * @param totalProductCashMax the totalProductCashMax to set
	 */
	public void setTotalProductCashMax(Float totalProductCashMax) {
		this.totalProductCashMax = totalProductCashMax;
	}
	/**
	 * @return the orderStatusStr
	 */
	public String getOrderStatusStr() {
		return orderStatusStr;
	}
	/**
	 * @param orderStatusStr the orderStatusStr to set
	 */
	public void setOrderStatusStr(String orderStatusStr) {
		this.orderStatusStr = orderStatusStr;
	}
	/**
	 * @return the paymentStr
	 */
	public String getPaymentStr() {
		return paymentStr;
	}
	/**
	 * @param paymentStr the paymentStr to set
	 */
	public void setPaymentStr(String paymentStr) {
		this.paymentStr = paymentStr;
	}
	/**
	 * @return the gmtOrderMin
	 */
	public Date getGmtOrderMin() {
		return gmtOrderMin;
	}
	/**
	 * @param gmtOrderMin the gmtOrderMin to set
	 */
	public void setGmtOrderMin(Date gmtOrderMin) {
		this.gmtOrderMin = gmtOrderMin;
	}
	/**
	 * @return the gmtOrderMax
	 */
	public Date getGmtOrderMax() {
		return gmtOrderMax;
	}
	/**
	 * @param gmtOrderMax the gmtOrderMax to set
	 */
	public void setGmtOrderMax(Date gmtOrderMax) {
		this.gmtOrderMax = gmtOrderMax;
	}
	/**
	 * @return the orderStatusArray
	 */
	public Short[] getOrderStatusArray() {
		return orderStatusArray;
	}
	/**
	 * @param orderStatusArray the orderStatusArray to set
	 */
	public void setOrderStatusArray(Short[] orderStatusArray) {
		this.orderStatusArray = orderStatusArray;
	}
	
}
