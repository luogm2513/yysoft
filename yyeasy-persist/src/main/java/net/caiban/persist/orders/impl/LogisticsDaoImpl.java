/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-28
 */
package net.caiban.persist.orders.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.domain.orders.Logistics;
import net.caiban.dto.Paging;
import net.caiban.exception.PersistLayerException;
import net.caiban.persist.orders.LogisticsDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("logisticsDao")
public class LogisticsDaoImpl extends SqlMapClientDaoSupport implements LogisticsDao {

	@Autowired
	private MessageSource messageSource;
	
	public void batchDeleteLogisticsById(Integer[] logisticsArray) {
		Assert.notNull(logisticsArray, messageSource.getMessage("assert.notnull", new String[]{"logisticsArray"}, null));
		try {
			getSqlMapClient().startBatch();
			for(Integer i:logisticsArray){
				getSqlMapClientTemplate().delete("logistics.deleteLogisticsById", i);
			}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
	}

	public Integer createLogisticsWithEid(Logistics logistics) {
		Assert.notNull(logistics, messageSource.getMessage("assert.notnull", new String[]{"logistics"}, null));
		return (Integer) getSqlMapClientTemplate().insert("logistics.createLogistics", logistics);
	}

	public Logistics listOneLogisticsById(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return (Logistics) getSqlMapClientTemplate().queryForObject("logistics.listOneLogisticsById", id);
	}

	@SuppressWarnings("unchecked")
	public List<Logistics> pageLogisticsWithEid(Logistics logistics, Paging page) {
		Assert.notNull(logistics, messageSource.getMessage("assert.notnull", new String[]{"logistics"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("logistics", logistics);
		root.put("page", page);
		return getSqlMapClientTemplate().queryForList("logistics.listLogisticsWithEid",root);
	}

	public Integer countLogisticsWithEid(Logistics logistics) {
		Assert.notNull(logistics, messageSource.getMessage("assert.notnull", new String[]{"logistics"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("logistics", logistics);
		return (Integer) getSqlMapClientTemplate().queryForObject("logistics.countLogisticsWithEid",root);
	}

	public void updateLogisticsById(Logistics logistics) {
		Assert.notNull(logistics, messageSource.getMessage("assert.notnull", new String[]{"logistics"}, null));
		getSqlMapClientTemplate().update("logistics.updateLogisticsById", logistics);
	}

	public Integer countSearch(Logistics logistics, String q) {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("logistics", logistics);
		root.put("q", q);
		return (Integer) getSqlMapClientTemplate().queryForObject("logistics.countSearch", root);
	}

	@SuppressWarnings("unchecked")
	public List<Logistics> pageSearch(Logistics logistics, String q, Paging page) {
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("logistics", logistics);
		root.put("q", q);
		root.put("page", page);
		return getSqlMapClientTemplate().queryForList("logistics.search", root);
	}
}
