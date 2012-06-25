/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-10
 */
package net.caiban.service.product.impl;

import java.util.List;

import net.caiban.domain.product.Product;
import net.caiban.domain.product.ProductImage;
import net.caiban.dto.Paging;
import net.caiban.dto.product.ProductDto;
import net.caiban.persist.product.ProductCategoryDao;
import net.caiban.persist.product.ProductDao;
import net.caiban.persist.product.ProductImageDao;
import net.caiban.service.product.ProductService;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImageDao productImageDao;
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	public Integer batchDeleteProductByIdArray(Integer[] idArray) {
		Assert.notNull(idArray, messageSource.getMessage("assert.notnull", new String[]{"idArray"}, null));
		Integer i=productDao.batchDeleteProductByIdArray(idArray);
		for(Integer pid:idArray){
			productImageDao.deleteImageByProductId(pid);
			//TODO 从磁盘上删除没有用的图片
		}
		return i;
	}

	public Integer createProduct(ProductDto productDto) {
		Assert.notNull(productDto, messageSource.getMessage("assert.notnull", new String[]{"productDto"}, null));
		Assert.notNull(productDto.getProduct(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product"}, null));
		Assert.notNull(productDto.getProduct().getEid(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product.eid"}, null));
		Assert.notNull(productDto.getProduct().getUid(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product.uid"}, null));
	
		Product p=productDto.getProduct();
		Integer id=productDao.createProduct(p);
		List<ProductImage> imgList=productDto.getImageList();
		if(imgList!=null){
			if(id.intValue()>0){
				for(ProductImage image:imgList){
					image.setProductId(id);
					image.setEid(p.getEid());
					image.setUid(p.getUid());
				}
			}
			productImageDao.batchCreateImage(imgList);
		}
		return id;
	}

	public Integer deleteProductByCategoryId(Integer categoryId) {
		Assert.notNull(categoryId, messageSource.getMessage("assert.notnull", new String[]{"categoryId"}, null));
		return productDao.deleteProductByCategoryId(categoryId);
	}

	public ProductDto listOneProductDtoById(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Product product = productDao.listOneProductById(id);
		if(product == null){
			return null;
		}
		ProductDto dto = new ProductDto();
		dto.setProduct(product);
		dto.setProductCategory(productCategoryDao.listOneProductCategoryViaId(product.getProductCategoryId()));
		dto.setImageList(productImageDao.listImageByProductId(product.getId()));
		return dto;
	}

	public Paging pageProductDto(ProductDto productDto, Paging page) {
		Assert.notNull(productDto, messageSource.getMessage("assert.notnull", new String[]{"productDto"}, null));
		Assert.notNull(productDto.getProduct(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product"}, null));
		Assert.notNull(productDto.getProduct().getEid(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product.eid"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		
		page.setRecords(productDao.pageProductDto(productDto, page));
		page.setTotals(productDao.countPageProductDto(productDto));
		
		return page;
	}

	public void updateProduct(Product product) {
		Assert.notNull(product, messageSource.getMessage("assert.notnull", new String[]{"product"}, null));
		Assert.notNull(product.getId(), messageSource.getMessage("assert.notnull", new String[]{"product.id"}, null));
		productDao.updateProduct(product);
	}

}
