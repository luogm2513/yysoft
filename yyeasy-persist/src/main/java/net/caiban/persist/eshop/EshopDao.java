/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-1
 */
package net.caiban.persist.eshop;

import net.caiban.domain.eshop.Eshop;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface EshopDao {
	
	/**
	 * 通过UID查找该用户的eshop信息
	 * @param uid:登录用户的UID,不能为null
	 * @return
	 */
	public Eshop listOneEshopByUid(Integer uid);
}
