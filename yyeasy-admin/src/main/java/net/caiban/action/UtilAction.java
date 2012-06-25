/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-17
 */
package net.caiban.action;

import java.util.ArrayList;
import java.util.List;

import net.caiban.domain.product.ProductCategory;
import net.caiban.dto.ExtResult;
import net.caiban.dto.ExtTreeNode;
import net.caiban.service.product.ProductCategoryService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class UtilAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7156498328252300695L;

	@Autowired
	private ProductCategoryService productCategoryService;
	
	private ExtResult result;
	private List<ExtTreeNode> treeNodeList;
	private ProductCategory productCategory;
	
	public UtilAction(){
		result = new ExtResult();
		treeNodeList = new ArrayList<ExtTreeNode>();
		productCategory = new ProductCategory();
	}
	
	public String comboProductCategoryByParent(){
		treeNodeList=productCategoryService.changeListToTreeNode(
				productCategoryService.listProductCategoryViaParent(
						productCategory.getParentId(),getSessionEshop().getId()));
		return "extresult";
	}

	/**
	 * @return the result
	 */
	public ExtResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ExtResult result) {
		this.result = result;
	}

	/**
	 * @return the treeNodeList
	 */
	public List<ExtTreeNode> getTreeNodeList() {
		return treeNodeList;
	}

	/**
	 * @param treeNodeList the treeNodeList to set
	 */
	public void setTreeNodeList(List<ExtTreeNode> treeNodeList) {
		this.treeNodeList = treeNodeList;
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
	
	
}
