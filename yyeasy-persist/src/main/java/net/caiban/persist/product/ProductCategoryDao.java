/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-7
 */
package net.caiban.persist.product;

import java.util.List;

import net.caiban.domain.product.ProductCategory;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface ProductCategoryDao {

	final static int DEFAULT_ROOT = 0;
	final static short DEFAULT_DEL = 0;
	
	/**
	 * 通过父节点的左右值获取所有子孙节点的类别信息
	 * @param l:左值,不能为null
	 * @param r:右值,不能为null
	 * @param eid:读取该类别信息的eshop,不可以为null
	 * @return
	 * 		按照左值正序排列的产品类别信息
	 */
	public List<ProductCategory> listProductCategoryViaLR(Integer l, Integer r, Integer eid);
	
	/**
	 * 通过父节点的ID获取子节点的类别信息,不包括孙节点
	 * @param parentId:父节点ID,不能为null
	 * @param eid:读取该类别信息的eshop,不可以为null
	 * @return
	 */
	public List<ProductCategory> listProductCategoryViaParent(Integer parentId, Integer eid);
	
	/**
	 * 通过类别ID查找类别信息
	 * @param id:类别ID,不能为null
	 * @return
	 */
	public ProductCategory listOneProductCategoryViaId(Integer id);
	
	/**
	 * 创建类别信息
	 * @param category:类别信息,不能为nulll<br/>
	 * 			category.eid:表示创建该类别的eshop,不可以为nulll<br/>
	 * 			category.uid:表示创建该类别信息的用户,不可以为nulll<br/>
	 * 			category.parentId:该节点的父节点ID,为null时默认为 {@link ProductCategoryDao#DEFAULT_ROOT}
	 * @return
	 */
	public Integer createProductCategory(ProductCategory category);
	
	/**
	 * 按照类别ID更新类别信息
	 * @param category:待更新的类别,不能为null<br/>
	 * 		仅更新基本信息category.name<br/>
	 * 		category.id:待更新的类别ID,不能为null
	 */
	public void updateProductCategoryViaId(ProductCategory category) ;
	
	/**
	 * 删除类别及子类别
	 * @param id:被删除的类别ID
	 * @return
	 */
	public Integer deleteProductCategory(Integer id);
}
