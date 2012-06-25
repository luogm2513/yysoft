/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-11
 */
package net.caiban.persist.product.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.domain.product.ProductCategory;
import net.caiban.exception.PersistLayerException;
import net.caiban.persist.product.ProductCategoryDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("productCategoryDao")
public class ProductCategoryDaoImpl extends SqlMapClientDaoSupport implements ProductCategoryDao{

	@Autowired
	MessageSource messageSource;
	
	public Integer createProductCategory(ProductCategory category) {
		Assert.notNull(category, messageSource.getMessage("assert.notnull", new String[]{"category"}, null));
		Assert.notNull(category.getEid(), messageSource.getMessage("assert.notnull", new String[]{"category.eid"}, null));
		Assert.notNull(category.getUid(), messageSource.getMessage("assert.notnull", new String[]{"category.uid"}, null));
		if(category.getDel()==null){
			category.setDel(DEFAULT_DEL);
		}
		
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
			ProductCategory parent=(ProductCategory) getSqlMapClientTemplate().queryForObject("productCategory.listOneProductCategoryViaId",category.getParentId());
			
			if(parent==null){
				Integer maxnum=(Integer) getSqlMapClientTemplate().queryForObject("productCategory.queryMaxRight",category.getEid());
				if(maxnum==null){
					maxnum=0;
				}
				category.setL(maxnum+1);
				category.setR(maxnum+2);
			}else{
				category.setL(parent.getR());
				category.setR(parent.getR()+1);
				
				getSqlMapClientTemplate().update("productCategory.updateLeftForCreate",parent.getR());
				getSqlMapClientTemplate().update("productCategory.updateRightForCreate",parent.getR());
			}
			impact=(Integer) getSqlMapClientTemplate().insert("productCategory.createProductCategory", category);
			
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer deleteProductCategory(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
			ProductCategory parent=(ProductCategory) getSqlMapClientTemplate().queryForObject("productCategory.listOneProductCategoryViaId",id);

			Map<String, Integer> root=new HashMap<String, Integer>();
			root.put("left", parent.getL());
			root.put("right", parent.getR());
			
			impact+=getSqlMapClientTemplate().delete("productCategory.deleteProductCategoryByLR", root);
			impact+=getSqlMapClientTemplate().update("productCategory.updateLeftForDelete", root);
			impact+=getSqlMapClientTemplate().update("productCategory.updateRightForDelete", root);
			
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public ProductCategory listOneProductCategoryViaId(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return (ProductCategory) getSqlMapClientTemplate().queryForObject("productCategory.listOneProductCategoryViaId",id);
	}

	@SuppressWarnings("unchecked")
	public List<ProductCategory> listProductCategoryViaLR(Integer l, Integer r, Integer eid) {
		Assert.notNull(l, messageSource.getMessage("assert.notnull", new String[]{"l"}, null));
		Assert.notNull(r, messageSource.getMessage("assert.notnull", new String[]{"r"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		
		Map root = new HashMap();
		root.put("left",l);
		root.put("right",r);
		root.put("eid",eid);
		
		return getSqlMapClientTemplate().queryForList("productCategory.listProductCategoryViaLR",root);
	}

	@SuppressWarnings("unchecked")
	public List<ProductCategory> listProductCategoryViaParent(Integer parentId,
			Integer eid) {
		Assert.notNull(parentId, messageSource.getMessage("assert.notnull", new String[]{"parentId"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		
		Map root=new HashMap();
		root.put("parentid", parentId);
		root.put("eid", eid);
		
		return  getSqlMapClientTemplate().queryForList("productCategory.listProductCategoryViaParent",root);
	}

	public void updateProductCategoryViaId(ProductCategory category) {
		Assert.notNull(category, messageSource.getMessage("assert.notnull", new String[]{"category"}, null));
		Assert.notNull(category.getId(), messageSource.getMessage("assert.notnull", new String[]{"category.id"}, null));
		getSqlMapClientTemplate().update("productCategory.updateProductCategoryViaId",category);
	}

}
