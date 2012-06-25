/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-1
 */
package net.caiban.persist.eshop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import net.caiban.domain.eshop.Eshop;
import net.caiban.persist.eshop.EshopDao;
import net.caiban.util.Assert;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("eshopDao")
public class EshopDaoImpl extends SqlMapClientDaoSupport implements EshopDao {

	@Autowired
	private MessageSource messageSource;
	
	public Eshop listOneEshopByUid(Integer uid) {
		Assert.notNull(uid, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		return (Eshop) getSqlMapClientTemplate().queryForObject("eshop.listOneEshopByUid", uid);
	}

}
