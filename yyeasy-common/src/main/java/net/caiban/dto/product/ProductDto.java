/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-7
 */
package net.caiban.dto.product;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.caiban.domain.product.Product;
import net.caiban.domain.product.ProductCategory;
import net.caiban.domain.product.ProductImage;
import net.caiban.domain.product.ProductStock;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class ProductDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8896261432927876137L;

	private Product product;
	private ProductCategory productCategory;
	private List<ProductImage> imageList;
	private List<ProductStock> stockList;
	
	private Float pricePurchaseMin;
	private Float pricePurchaseMax;
	private Float priceSaleMin;
	private Float priceSaleMax;
	
	private Date gmtcreateMin;
	private Date gmtcreateMax;
	
	private String uname;
	
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
	/**
	 * @return the productCategory
	 */
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	/**
	 * @param productCategory the productCategory to set
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	/**
	 * @return the imageList
	 */
	public List<ProductImage> getImageList() {
		return imageList;
	}
	/**
	 * @param imageList the imageList to set
	 */
	public void setImageList(List<ProductImage> imageList) {
		this.imageList = imageList;
	}
	/**
	 * @return the stockList
	 */
	public List<ProductStock> getStockList() {
		return stockList;
	}
	/**
	 * @param stockList the stockList to set
	 */
	public void setStockList(List<ProductStock> stockList) {
		this.stockList = stockList;
	}
	/**
	 * @return the pricePurchaseMin
	 */
	public Float getPricePurchaseMin() {
		return pricePurchaseMin;
	}
	/**
	 * @param pricePurchaseMin the pricePurchaseMin to set
	 */
	public void setPricePurchaseMin(Float pricePurchaseMin) {
		this.pricePurchaseMin = pricePurchaseMin;
	}
	/**
	 * @return the pricePurchaseMax
	 */
	public Float getPricePurchaseMax() {
		return pricePurchaseMax;
	}
	/**
	 * @param pricePurchaseMax the pricePurchaseMax to set
	 */
	public void setPricePurchaseMax(Float pricePurchaseMax) {
		this.pricePurchaseMax = pricePurchaseMax;
	}
	/**
	 * @return the priceSaleMin
	 */
	public Float getPriceSaleMin() {
		return priceSaleMin;
	}
	/**
	 * @param priceSaleMin the priceSaleMin to set
	 */
	public void setPriceSaleMin(Float priceSaleMin) {
		this.priceSaleMin = priceSaleMin;
	}
	/**
	 * @return the priceSaleMax
	 */
	public Float getPriceSaleMax() {
		return priceSaleMax;
	}
	/**
	 * @param priceSaleMax the priceSaleMax to set
	 */
	public void setPriceSaleMax(Float priceSaleMax) {
		this.priceSaleMax = priceSaleMax;
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
	 * @return the uname
	 */
	public String getUname() {
		return uname;
	}
	/**
	 * @param uname the uname to set
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	
	
}
