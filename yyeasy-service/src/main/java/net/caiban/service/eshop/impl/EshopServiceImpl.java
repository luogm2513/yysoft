/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-1
 */
package net.caiban.service.eshop.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import net.caiban.domain.eshop.Eshop;
import net.caiban.persist.eshop.EshopDao;
import net.caiban.service.eshop.EshopService;
import net.caiban.util.Assert;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("eshopService")
public class EshopServiceImpl implements EshopService {
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private EshopDao eshopDao;

	public Eshop listOneEshopByUid(Integer uid) {
		Assert.notNull(uid, messageSource.getMessage("assert.notnull", new String[]{"user"}, null));
		return eshopDao.listOneEshopByUid(uid);
	}

}
