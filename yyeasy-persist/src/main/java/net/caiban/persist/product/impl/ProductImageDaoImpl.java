/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-13
 */
package net.caiban.persist.product.impl;

import java.sql.SQLException;
import java.util.List;

import net.caiban.domain.product.ProductImage;
import net.caiban.exception.PersistLayerException;
import net.caiban.persist.product.ProductImageDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("productImageDao")
public class ProductImageDaoImpl extends SqlMapClientDaoSupport implements
		ProductImageDao {
	
	@Autowired
	private MessageSource messageSource;

	public Integer batchDeleteImageByIdArray(Integer[] idArray) {
		Assert.notNull(idArray, messageSource.getMessage("assert.notnull", new String[]{"logisticsArray"}, null));
		Integer impact = 0;
		try {
			getSqlMapClient().startBatch();
			for(Integer i:idArray){
				getSqlMapClientTemplate().delete("productImage.deleteImageById", i);
				impact++;
			}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer createImage(ProductImage image) {
		Assert.notNull(image, messageSource.getMessage("assert.notnull", new String[]{"image"}, null));
		Assert.notNull(image.getProductId(), messageSource.getMessage("assert.notnull", new String[]{"image.productId"}, null));
		Assert.notNull(image.getUid(), messageSource.getMessage("assert.notnull", new String[]{"image.uid"}, null));
		Assert.notNull(image.getEid(), messageSource.getMessage("assert.notnull", new String[]{"image.eid"}, null));
		return (Integer) getSqlMapClientTemplate().insert("productImage.createImage", image);
	}

	public Integer deleteImageByProductId(Integer productId) {
		Assert.notNull(productId, messageSource.getMessage("assert.notnull", new String[]{"productId"}, null));
		return getSqlMapClientTemplate().delete("productImage.deleteImageByProductId", productId);
	}

	@SuppressWarnings("unchecked")
	public List<ProductImage> listImageByProductId(Integer productId) {
		Assert.notNull(productId, messageSource.getMessage("assert.notnull", new String[]{"productId"}, null));
		return getSqlMapClientTemplate().queryForList("productImage.listImageByProductId",productId);
	}

	public Integer batchCreateImage(List<ProductImage> imgList) {
		Integer impact=0;
		Integer id=0;
		try {
			getSqlMapClient().startBatch();
			for(ProductImage image:imgList){
				id=(Integer) getSqlMapClientTemplate().insert("productImage.createImage", image);
				if(id>0){
					impact++;
				}
			}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

}
