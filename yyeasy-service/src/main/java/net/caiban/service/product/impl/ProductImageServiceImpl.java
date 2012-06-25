/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-10
 */
package net.caiban.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import net.caiban.domain.product.ProductImage;
import net.caiban.persist.product.ProductImageDao;
import net.caiban.service.product.ProductImageService;
import net.caiban.util.Assert;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("productImageService")
public class ProductImageServiceImpl implements ProductImageService {
	
	@Autowired
	ProductImageDao productImageDao;
	@Autowired
	MessageSource messageSource;

	public Integer batchDeleteImageByIdArray(Integer[] idArray) {
		Assert.notNull(idArray, messageSource.getMessage("assert.notnull", new String[]{"idArray"}, null));
		Integer i = productImageDao.batchDeleteImageByIdArray(idArray);
		//TODO 删除磁盘上的图片
		
		return i;
	}

	public Integer createImage(ProductImage image) {
		Assert.notNull(image, messageSource.getMessage("assert.notnull", new String[]{"image"}, null));
		Assert.notNull(image.getEid(), messageSource.getMessage("assert.notnull", new String[]{"image.eid"}, null));
		Assert.notNull(image.getUid(), messageSource.getMessage("assert.notnull", new String[]{"image.uid"}, null));
		Assert.notNull(image.getProductId(), messageSource.getMessage("assert.notnull", new String[]{"image.productId"}, null));
		return productImageDao.createImage(image);
	}

	public Integer deleteImageByProductId(Integer productId) {
		Assert.notNull(productId, messageSource.getMessage("assert.notnull", new String[]{"productId"}, null));
		return productImageDao.deleteImageByProductId(productId);
	}

	public List<ProductImage> listImageByProductId(Integer productId) {
		Assert.notNull(productId, messageSource.getMessage("assert.notnull", new String[]{"productId"}, null));
		return productImageDao.listImageByProductId(productId);
	}

}
