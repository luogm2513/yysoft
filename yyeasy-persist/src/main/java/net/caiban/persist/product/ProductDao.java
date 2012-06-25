/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-9
 */
package net.caiban.persist.product;

import java.util.List;

import net.caiban.domain.product.Product;
import net.caiban.dto.Paging;
import net.caiban.dto.product.ProductDto;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface ProductDao {

	/**
	 *  分页查询产品信息
	 * @param productDto:查询条件,不能为null<br/>
	 * 			productDto.product.eid:登录用户的eshop id,不能为null<br/>
	 * @param page:分页信息,不能为null
	 */
	public List<ProductDto> pageProductDto(ProductDto productDto, Paging page);
	
	/**
	 * 统计记录总数
	 * @param productDto:查询条件,不能为null<br/>
	 * 		productDto.product.eid:登录用户的eshop id,不能为null
	 * @return
	 */
	public Integer countPageProductDto(ProductDto productDto);
	
	/**
	 * 查询产品基本信息
	 * @param id:产品ID,不能为null
	 * @return
	 */
	public Product listOneProductById(Integer id);
	
	/**
	 * 创建产品信息
	 * @param product:待创建的产品信息,不能为null
	 * @return
	 */
	public Integer createProduct(Product product);
	
	/**
	 * 更新产品信息,只更新产品基本信息
	 * @param product:待更新的产品信息,不能为null<br />
	 * 			product.id:待更新产品的ID,不能为null
	 */
	public void updateProduct(Product product);
	
	/**
	 * 批量删除产品信息
	 * @param idArray:待删除的产品ID数组,不能为empty
	 * @return
	 */
	public Integer batchDeleteProductByIdArray(Integer[] idArray);
	
	/**
	 * 删除某个类别的产品信息
	 * @param categoryId:类别ID,不能为null
	 * @return
	 */
	public Integer deleteProductByCategoryId(Integer categoryId);
}
