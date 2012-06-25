/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-8-29
 */
package net.caiban.dto.orders;

import java.util.Date;

import net.caiban.domain.orders.Orders;
import net.caiban.domain.orders.SaleDispute;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class SaleDisputeDto  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SaleDispute saleDispute;
	private Orders orders;
	
	private Date periodMin;
	private Date periodMax;
	private Float reparationMin;
	private Float reparationMax;
	private String username;
	private String resolvedStr;
	private String severityStr; 
	private Integer[] resolvedArray;
	
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
	 * @return the periodMin
	 */
	public Date getPeriodMin() {
		return periodMin;
	}
	/**
	 * @param periodMin the periodMin to set
	 */
	public void setPeriodMin(Date periodMin) {
		this.periodMin = periodMin;
	}
	/**
	 * @return the periodMax
	 */
	public Date getPeriodMax() {
		return periodMax;
	}
	/**
	 * @param periodMax the periodMax to set
	 */
	public void setPeriodMax(Date periodMax) {
		this.periodMax = periodMax;
	}
	/**
	 * @return the reparationMin
	 */
	public Float getReparationMin() {
		return reparationMin;
	}
	/**
	 * @param reparationMin the reparationMin to set
	 */
	public void setReparationMin(Float reparationMin) {
		this.reparationMin = reparationMin;
	}
	/**
	 * @return the reparationMax
	 */
	public Float getReparationMax() {
		return reparationMax;
	}
	/**
	 * @param reparationMax the reparationMax to set
	 */
	public void setReparationMax(Float reparationMax) {
		this.reparationMax = reparationMax;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the resolvedStr
	 */
	public String getResolvedStr() {
		return resolvedStr;
	}
	/**
	 * @param resolvedStr the resolvedStr to set
	 */
	public void setResolvedStr(String resolvedStr) {
		this.resolvedStr = resolvedStr;
	}
	/**
	 * @return the severityStr
	 */
	public String getSeverityStr() {
		return severityStr;
	}
	/**
	 * @param severityStr the severityStr to set
	 */
	public void setSeverityStr(String severityStr) {
		this.severityStr = severityStr;
	}
	/**
	 * @return the resolvedArray
	 */
	public Integer[] getResolvedArray() {
		return resolvedArray;
	}
	/**
	 * @param resolvedArray the resolvedArray to set
	 */
	public void setResolvedArray(Integer[] resolvedArray) {
		this.resolvedArray = resolvedArray;
	}

	
}
