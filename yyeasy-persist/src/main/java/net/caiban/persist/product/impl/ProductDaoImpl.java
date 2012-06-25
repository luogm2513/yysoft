/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-13
 */
package net.caiban.persist.product.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.domain.product.Product;
import net.caiban.dto.Paging;
import net.caiban.dto.product.ProductDto;
import net.caiban.exception.PersistLayerException;
import net.caiban.persist.product.ProductDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("productDao")
public class ProductDaoImpl extends SqlMapClientDaoSupport implements
		ProductDao {

	@Autowired
	private MessageSource messageSource;
	
	public Integer batchDeleteProductByIdArray(Integer[] idArray) {
		Assert.notNull(idArray, messageSource.getMessage("assert.notnull", new String[]{"idArray"}, null));
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
				for(Integer i:idArray){
					impact+=getSqlMapClientTemplate().delete("product.deleteProductById",i);
				}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer countPageProductDto(ProductDto productDto) {
		Assert.notNull(productDto, messageSource.getMessage("assert.notnull", new String[]{"productDto"}, null));
		Assert.notNull(productDto.getProduct(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product"}, null));
		Assert.notNull(productDto.getProduct().getEid(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product.eid"}, null));
		Map<String,Object> root=new HashMap<String, Object>();
		root.put("dto",productDto);
		return (Integer) getSqlMapClientTemplate().queryForObject("product.countPageProductDto",root);
	}

	public Integer createProduct(Product product) {
		Assert.notNull(product, messageSource.getMessage("assert.notnull", new String[]{"product"}, null));
		Assert.notNull(product.getEid(), messageSource.getMessage("assert.notnull", new String[]{"product.eid"}, null));
		Assert.notNull(product.getUid(), messageSource.getMessage("assert.notnull", new String[]{"product.uid"}, null));
		return (Integer) getSqlMapClientTemplate().insert("product.createProduct",product);
	}

	public Integer deleteProductByCategoryId(Integer categoryId) {
		Assert.notNull(categoryId, messageSource.getMessage("assert.notnull", new String[]{"categoryId"}, null));
		return getSqlMapClientTemplate().delete("product.deleteProductByCategoryId",categoryId);
	}

	public Product listOneProductById(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return (Product) getSqlMapClientTemplate().queryForObject("product.listOneProductById",id);
	}

	@SuppressWarnings("unchecked")
	public List<ProductDto> pageProductDto(ProductDto productDto, Paging page) {
		Assert.notNull(productDto, messageSource.getMessage("assert.notnull", new String[]{"productDto"}, null));
		Assert.notNull(productDto.getProduct(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product"}, null));
		Assert.notNull(productDto.getProduct().getEid(), messageSource.getMessage("assert.notnull", new String[]{"productDto.product.eid"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("dto",productDto);
		root.put("page",page);
		return getSqlMapClientTemplate().queryForList("product.pageProductDto",root);
	}

	public void updateProduct(Product product) {
		Assert.notNull(product, messageSource.getMessage("assert.notnull", new String[]{"product"}, null));
		Assert.notNull(product.getId(), messageSource.getMessage("assert.notnull", new String[]{"product.id"}, null));
		getSqlMapClientTemplate().update("product.updateProduct",product);
	}

}
