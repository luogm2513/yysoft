/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-28
 */
package net.caiban.service.orders.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import net.caiban.domain.orders.Logistics;
import net.caiban.dto.Paging;
import net.caiban.persist.orders.LogisticsDao;
import net.caiban.service.orders.LogisticsService;
import net.caiban.util.Assert;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("logisticsService")
public class LogisticsServiceImpl implements LogisticsService {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LogisticsDao logisticsDao;
	
	public void batchDeleteLogisticsByEid(Integer[] logisticsArray) {
		Assert.notNull(logisticsArray, messageSource.getMessage("assert.notnull", new String[]{"logisticsArray"}, null));
		logisticsDao.batchDeleteLogisticsById(logisticsArray);
	}

	public Integer createLogisticsByEid(Logistics logistics) {
		Assert.notNull(logistics, messageSource.getMessage("assert.notnull", new String[]{"logisticsArray"}, null));
		Assert.notNull(logistics.getEid(), messageSource.getMessage("assert.notnull", new String[]{"logisticsArray.eid"}, null));
		Assert.notNull(logistics.getUid(), messageSource.getMessage("assert.notnull", new String[]{"logisticsArray.uid"}, null));
		return logisticsDao.createLogisticsWithEid(logistics);
	}

	public Logistics listOneLogisticsById(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return logisticsDao.listOneLogisticsById(id);
	}

	public Paging pageLogisticsWithEid(Logistics logistics, Paging page) {
		Assert.notNull(logistics, messageSource.getMessage("assert.notnull", new String[]{"logistics"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Assert.notNull(logistics.getEid(), messageSource.getMessage("assert.notnull", new String[]{"logistics.eid"}, null));
		page.setRecords(logisticsDao.pageLogisticsWithEid(logistics, page));
		page.setTotals(logisticsDao.countLogisticsWithEid(logistics));
		return page;
	}

	public void updateLogisticsById(Logistics logistics) {
		Assert.notNull(logistics, messageSource.getMessage("assert.notnull", new String[]{"logistics"}, null));
		logisticsDao.updateLogisticsById(logistics);
	}

	public Paging search(Logistics logistics, String q, Paging page) {
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		if(logistics==null){
			logistics = new Logistics();
		}
		page.setRecords(logisticsDao.pageSearch(logistics, q, page));
		page.setTotals(logisticsDao.countSearch(logistics, q));
		return page;
	}
	
}
