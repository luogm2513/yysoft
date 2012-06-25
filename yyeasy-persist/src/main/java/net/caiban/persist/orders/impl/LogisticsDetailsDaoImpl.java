/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-7-11
 */
package net.caiban.persist.orders.impl;

import net.caiban.domain.orders.LogisticsDetails;
import net.caiban.persist.orders.LogisticsDetailsDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("logisticsDetailsDao")
public class LogisticsDetailsDaoImpl extends SqlMapClientDaoSupport implements
		LogisticsDetailsDao {
	
	@Autowired
	private MessageSource messageSource;

	public Integer createLogisticsDetails(LogisticsDetails details) {
		Assert.notNull(details, messageSource.getMessage("assert.notnull", new String[]{"details"}, null));
		Assert.notNull(details.getUid(), messageSource.getMessage("assert.notnull", new String[]{"details.uid"}, null));
		Assert.notNull(details.getEid(), messageSource.getMessage("assert.notnull", new String[]{"details.eid"}, null));
		Assert.notNull(details.getGmtSend(), messageSource.getMessage("assert.notnull", new String[]{"details.gmtSend"}, null));
		return (Integer) getSqlMapClientTemplate().insert("logisticsDetails.createLogisticsDetails", details);
	}

	public Integer updateLogisticsDetailsByOrdersId(LogisticsDetails details) {
		Assert.notNull(details.getEid(), messageSource.getMessage("assert.notnull", new String[]{"details.eid"}, null));
		Assert.notNull(details.getOrderId(), messageSource.getMessage("assert.notnull", new String[]{"details.gmtSend"}, null));
		return getSqlMapClientTemplate().update("logisticsDetails.updateLogisticsDetailsByOrdersId", details);
	}

}
