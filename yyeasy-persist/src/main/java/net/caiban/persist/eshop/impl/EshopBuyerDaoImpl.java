/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-9
 */
package net.caiban.persist.eshop.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.dto.Paging;
import net.caiban.exception.PersistLayerException;
import net.caiban.persist.eshop.EshopBuyerDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("eshopBuyerDao")
public class EshopBuyerDaoImpl extends SqlMapClientDaoSupport implements EshopBuyerDao {

	@Autowired
	private MessageSource messageSource;
	
	public Integer batchDeleteEshopBuyerBuyIdArray(Integer[] idArray) {
		Assert.notNull(idArray, messageSource.getMessage("assert.notnull", new String[]{"idArray"}, null));
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
				for(Integer i:idArray){
					impact+=getSqlMapClientTemplate().delete("eshopBuyer.deleteEshopBuyerById",i);
				}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer countPageEshopBuyer(EshopBuyer buyer) {
		Assert.notNull(buyer, messageSource.getMessage("assert.notnull", new String[]{"buyer"}, null));
		Assert.notNull(buyer.getEid(), messageSource.getMessage("assert.notnull", new String[]{"buyer.eid"}, null));
		Map map = new HashMap();
		map.put("buyer", buyer);
		return (Integer) getSqlMapClientTemplate().queryForObject("eshopBuyer.countPageEshopBuyer",map);
	}

	public Integer createEshopBuyer(EshopBuyer buyer) {
		Assert.notNull(buyer, messageSource.getMessage("assert.notnull", new String[]{"buyer"}, null));
		Assert.notNull(buyer.getEid(), messageSource.getMessage("assert.notnull", new String[]{"buyer.eid"}, null));
		Assert.notNull(buyer.getUid(), messageSource.getMessage("assert.notnull", new String[]{"buyer.uid"}, null));
		return (Integer) getSqlMapClientTemplate().insert("eshopBuyer.createEshopBuyer",buyer);
	}

	public EshopBuyer listOneEshopBuyerById(Integer id, Integer eid) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Map root = new HashMap();
		root.put("id", id);
		root.put("eid", eid);
		return (EshopBuyer) getSqlMapClientTemplate().queryForObject("eshopBuyer.listOneEshopBuyerById",root);
	}

	public List<EshopBuyer> pageEshopBuyer(EshopBuyer buyer, Paging page) {
		Assert.notNull(buyer, messageSource.getMessage("assert.notnull", new String[]{"buyer"}, null));
		Assert.notNull(buyer.getEid(), messageSource.getMessage("assert.notnull", new String[]{"buyer.eid"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("buyer",buyer);
		root.put("page",page);
		return getSqlMapClientTemplate().queryForList("eshopBuyer.pageEshopBuyer",root);
	}

	public void updateEshopBuyer(EshopBuyer buyer) {
		Assert.notNull(buyer, messageSource.getMessage("assert.notnull", new String[]{"buyer"}, null));
		Assert.notNull(buyer.getId(), messageSource.getMessage("assert.notnull", new String[]{"buyer.id"}, null));
		getSqlMapClientTemplate().update("eshopBuyer.updateEshopBuyer",buyer);
	}

	public List<EshopBuyer> listEshopBuyerByName(String name, Integer maxNum,
			Integer eid, Integer uid) {
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Assert.notNull(name, messageSource.getMessage("assert.notnull", new String[]{"name"}, null));
		Map root = new HashMap();
		root.put("name", name);
		root.put("eid", eid);
		root.put("uid",uid);
		root.put("maxNum", maxNum);
		return getSqlMapClientTemplate().queryForList("eshopBuyer.listEshopBuyerByName",root);
	}

}
