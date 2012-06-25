/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-9
 */
package net.caiban.service.eshop.impl;

import java.util.List;

import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.dto.Paging;
import net.caiban.persist.eshop.EshopBuyerDao;
import net.caiban.service.eshop.EshopBuyerService;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("eshopBuyerService")
public class EshopBuyerServiceImpl implements EshopBuyerService {
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private EshopBuyerDao eshopBuyerDao;

	public Integer batchDeleteEshopBuyerBuyIdArray(Integer[] idArray) {
		Assert.notNull(idArray, messageSource.getMessage("assert.notnull", new String[]{"idArray"}, null));
		
		return eshopBuyerDao.batchDeleteEshopBuyerBuyIdArray(idArray);
	}

	public Integer createEshopBuyer(EshopBuyer buyer) {
		Assert.notNull(buyer, messageSource.getMessage("assert.notnull", new String[]{"buyer"}, null));
		Assert.notNull(buyer.getEid(), messageSource.getMessage("assert.notnull", new String[]{"buyer.eid"}, null));
		Assert.notNull(buyer.getUid(), messageSource.getMessage("assert.notnull", new String[]{"buyer.uid"}, null));
		return eshopBuyerDao.createEshopBuyer(buyer);
	}

	public EshopBuyer listOneEshopBuyerById(Integer id, Integer eid) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		return eshopBuyerDao.listOneEshopBuyerById(id, eid);
	}

	public Paging pageEshopBuyer(EshopBuyer buyer, Paging page) {
		Assert.notNull(buyer, messageSource.getMessage("assert.notnull", new String[]{"buyer"}, null));
		Assert.notNull(buyer.getEid(), messageSource.getMessage("assert.notnull", new String[]{"buyer.eid"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		
		page.setRecords(eshopBuyerDao.pageEshopBuyer(buyer, page));
		page.setTotals(eshopBuyerDao.countPageEshopBuyer(buyer));
		return page;
	}

	public void updateEshopBuyer(EshopBuyer buyer) {
		Assert.notNull(buyer, messageSource.getMessage("assert.notnull", new String[]{"buyer"}, null));
		Assert.notNull(buyer.getId(), messageSource.getMessage("assert.notnull", new String[]{"buyer.id"}, null));
		eshopBuyerDao.updateEshopBuyer(buyer);
	}

	public List<EshopBuyer> listEshopBuyerByName(String name, Integer maxNum,
			Integer eid, Integer uid) {
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Assert.notNull(name, messageSource.getMessage("assert.notnull", new String[]{"name"}, null));
		if(maxNum==null || maxNum.intValue() == 0) {
			maxNum = eshopBuyerDao.DEFAULT_MAX_BUYER;
		}
		return eshopBuyerDao.listEshopBuyerByName(name, maxNum, eid, uid);
	}

}
