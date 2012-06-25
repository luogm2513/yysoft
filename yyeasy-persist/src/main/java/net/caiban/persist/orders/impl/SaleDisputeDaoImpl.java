/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-8-29
 */
package net.caiban.persist.orders.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.domain.orders.SaleDispute;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.SaleDisputeDto;
import net.caiban.exception.PersistLayerException;
import net.caiban.persist.orders.SaleDisputeDao;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("SaleDisputeDao")
public class SaleDisputeDaoImpl extends SqlMapClientDaoSupport implements
		SaleDisputeDao {
	
	@Autowired
	MessageSource messageSource;

	public Integer createSaleDispute(SaleDispute saleDispute) {
		Assert.notNull(saleDispute, messageSource.getMessage("assert.notnull", new String[]{"saleDispute"}, null));
		Assert.notNull(saleDispute.getOrderId(), messageSource.getMessage("assert.notnull", new String[]{"saleDispute.orderId"}, null));
		
		return (Integer) getSqlMapClientTemplate().insert("saleDispute.createSaleDispute", saleDispute);
	}

	public Integer countPageSaleDispute(SaleDisputeDto saleDisputeDto) {
		Assert.notNull(saleDisputeDto, messageSource.getMessage("assert.notnull", new String[]{"saleDisputeDto"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("dto", saleDisputeDto);
		return (Integer) getSqlMapClientTemplate().queryForObject("saleDispute.countPageSaleDispute",root);
	}

	public SaleDispute listOneSaleDisputeById(Integer id, Integer eid) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", id);
		root.put("eid", eid);
		return (SaleDispute) getSqlMapClientTemplate().queryForObject("saleDispute.listOneSaleDisputeById", root);
	}

	public List<SaleDisputeDto> pageSaleDispute(SaleDisputeDto saleDisputeDto,
			Paging page) {
		Assert.notNull(saleDisputeDto, messageSource.getMessage("assert.notnull", new String[]{"saleDisputeDto"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("dto", saleDisputeDto);
		root.put("page", page);
		return getSqlMapClientTemplate().queryForList("saleDispute.pageSaleDispute", root);
	}

	public Integer updateResolvedStatus(Short status, Integer resolvedUid,
			Integer eid, Integer id) {
		Assert.notNull(status, messageSource.getMessage("assert.notnull", new String[]{"status"}, null));
		Assert.notNull(resolvedUid, messageSource.getMessage("assert.notnull", new String[]{"resolvedUid"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("status", status);
		root.put("resolvedUid", resolvedUid);
		root.put("eid", eid);
		root.put("id",id);
		return getSqlMapClientTemplate().update("saleDispute.updateResolvedStatus", root);
	}

	public Integer updateSaleDisputeSimple(SaleDispute saleDispute) {
		Assert.notNull(saleDispute, messageSource.getMessage("assert.notnull", new String[]{"saleDispute"}, null));
		return getSqlMapClientTemplate().update("saleDispute.updateSaleDisputeSimple", saleDispute);
	}

	public Integer batchUpdateResolvedUid(Integer uid, Integer[] ids,
			Integer eid) {
		//TODO undevelope
		return null;
	}

	public Integer batchUpdateResolved(Short resolved, Integer[] ids, Integer eid) {
		Assert.notNull(ids, messageSource.getMessage("assert.notnull", new String[]{"ids"}, null));
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
				for(Integer i:ids){
					Map<String, Object> root = new HashMap<String, Object>();
					root.put("id", i);
					root.put("eid", eid);
					root.put("resolved", resolved);
					Integer j= getSqlMapClientTemplate().delete("saleDispute.updateResolvedById",root);
					if(j>0){
						impact++;
					}
//					if(){
//						impact+=1;
//					}
				}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer batchDeleteSaleDispute(Integer[] ids, Integer eid) {
		Assert.notNull(ids, messageSource.getMessage("assert.notnull", new String[]{"ids"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
				for(Integer i:ids){
					Map<String, Object> root = new HashMap<String, Object>();
					root.put("id", i);
					root.put("eid", eid);
					Integer j= getSqlMapClientTemplate().delete("saleDispute.deleteSaleDisputeById",root);
					if(j>0){
						impact++;
					}
				}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new PersistLayerException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

}
