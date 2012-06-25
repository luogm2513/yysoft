/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-10
 */
package net.caiban.service.product.impl;

import java.util.ArrayList;
import java.util.List;

import net.caiban.domain.product.ProductCategory;
import net.caiban.dto.ExtTreeNode;
import net.caiban.persist.product.ProductCategoryDao;
import net.caiban.service.product.ProductCategoryService;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("productCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private MessageSource messageSource;
	
	public List<ExtTreeNode> changeListToTreeNode(List<ProductCategory> categoryList) {
		Assert.notNull(categoryList, 
				messageSource.getMessage("assert.notnull", new String[]{"categoryList"}, null));
		List<ExtTreeNode> nodeList= new ArrayList<ExtTreeNode>();
		for(ProductCategory category:categoryList){
			ExtTreeNode node=new ExtTreeNode();
			node.setData(category.getId());
			node.setText(category.getName());
			node.setLeaf((category.getR()-category.getL()-1)/2 == 0);
			
			nodeList.add(node);
		}
		return nodeList;
	}

	public Integer createProductCategory(ProductCategory category) {
		Assert.notNull(category, messageSource.getMessage("assert.notnull", new String[]{"category"}, null));
		Assert.notNull(category.getEid(), messageSource.getMessage("assert.notnull", new String[]{"category.eid"}, null));
		Assert.notNull(category.getUid(), messageSource.getMessage("assert.notnull", new String[]{"category.uid"}, null));
		if(category.getParentId()==null){
			category.setParentId(productCategoryDao.DEFAULT_ROOT);
		}
		return productCategoryDao.createProductCategory(category);
	}

	public ProductCategory listOneProductCategoryViaId(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return productCategoryDao.listOneProductCategoryViaId(id);
	}

	public List<ProductCategory> listProductCategoryViaLR(Integer l, Integer r, Integer eid) {
		Assert.notNull(l, messageSource.getMessage("assert.notnull", new String[]{"l"}, null));
		Assert.notNull(r, messageSource.getMessage("assert.notnull", new String[]{"r"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		return productCategoryDao.listProductCategoryViaLR(l, r, eid);
	}

	public List<ProductCategory> listProductCategoryViaParent(Integer parentId, Integer eid) {
		Assert.notNull(parentId, messageSource.getMessage("assert.notnull", new String[]{"parentId"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		return productCategoryDao.listProductCategoryViaParent(parentId, eid);
	}

	public void updateProductCategoryViaId(ProductCategory category) {
		Assert.notNull(category, messageSource.getMessage("assert.notnull", new String[]{"category"}, null));
		Assert.notNull(category.getId(), messageSource.getMessage("assert.notnull", new String[]{"category.id"}, null));
		productCategoryDao.updateProductCategoryViaId(category);
	}

	public Integer deleteProductCategory(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return productCategoryDao.deleteProductCategory(id);
	}

}
