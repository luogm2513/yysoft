/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-8
 */
package net.caiban.service.product;

import java.util.List;

import net.caiban.domain.product.ProductImage;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface ProductImageService {

	/**
	 * 列出产品的所有图片
	 * @param productId:产品ID,不能为null
	 * @return
	 */
	public List<ProductImage> listImageByProductId(Integer productId);
	
	/**
	 * 批量删除产品图片
	 * @param idArray:待删除的产品ID集合,不能为null
	 * @return
	 */
	public Integer batchDeleteImageByIdArray(Integer[] idArray);
	
	/**
	 * 删除产品图片
	 * @param productId:产品ID,不能为null
	 * @return
	 */
	public Integer deleteImageByProductId(Integer productId);
	
	/**
	 * 新增产品图片信息
	 * @param image:图片基本信息,不能为null<br/>
	 * 		image.productId:图片所属的产品信息,不能为null<br/>
	 * 		image.eid:登录用户的eshop信息<br/>
	 * 		image.uid:登录用户的账户ID
	 * @return
	 */
	public Integer createImage(ProductImage image);
	
}
