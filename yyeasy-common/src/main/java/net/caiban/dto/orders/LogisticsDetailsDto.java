/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-23
 */
package net.caiban.dto.orders;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.caiban.domain.orders.Logistics;
import net.caiban.domain.orders.LogisticsDetails;
import net.caiban.domain.product.ProductStock;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class LogisticsDetailsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6078656056639622001L;
	private Logistics logistics;
	private LogisticsDetails logisticsDetails;
	private List<ProductStock> productStockList;
	
	private Date gmtcreateMin;
	private Date gmtcreateMax;
	/**
	 * @return the logistics
	 */
	public Logistics getLogistics() {
		return logistics;
	}
	/**
	 * @param logistics the logistics to set
	 */
	public void setLogistics(Logistics logistics) {
		this.logistics = logistics;
	}
	/**
	 * @return the logisticsDetails
	 */
	public LogisticsDetails getLogisticsDetails() {
		return logisticsDetails;
	}
	/**
	 * @param logisticsDetails the logisticsDetails to set
	 */
	public void setLogisticsDetails(LogisticsDetails logisticsDetails) {
		this.logisticsDetails = logisticsDetails;
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
	 * @return the productStockList
	 */
	public List<ProductStock> getProductStockList() {
		return productStockList;
	}
	/**
	 * @param productStockList the productStockList to set
	 */
	public void setProductStockList(List<ProductStock> productStockList) {
		this.productStockList = productStockList;
	}
	
}
