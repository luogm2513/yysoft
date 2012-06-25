/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-8-29
 */
package net.caiban.service.orders.impl;

import java.util.List;

import net.caiban.domain.orders.SaleDispute;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.SaleDisputeDto;
import net.caiban.facade.ParamFacade;
import net.caiban.persist.orders.SaleDisputeDao;
import net.caiban.service.orders.SaleDisputeService;
import net.caiban.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("saleDisputeService")
public class SaleDisputeServiceImpl implements SaleDisputeService {
	
	@Autowired
	private SaleDisputeDao saleDisputeDao;
	@Autowired
	private MessageSource messageSource;
	
	final static String SEVERITY_KEY = "sale_dispute_severity";
	final static String RESOLVED_KEY = "sale_dispute_resolved";

	public SaleDispute listOneSaleDisputeById(Integer id, Integer eid) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		return saleDisputeDao.listOneSaleDisputeById(id, eid);
	}

	public Paging pageSaleDispute(SaleDisputeDto saleDisputeDto, Paging page) {
		Assert.notNull(saleDisputeDto, messageSource.getMessage("assert.notnull", new String[]{"saleDisputeDto"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		page.setTotals(saleDisputeDao.countPageSaleDispute(saleDisputeDto));
		List<SaleDisputeDto> list = saleDisputeDao.pageSaleDispute(saleDisputeDto, page);
		for(SaleDisputeDto o:list){
			if(o.getSaleDispute().getSeverity()!=null){
				o.setSeverityStr(ParamFacade.getInstance().getValue(SEVERITY_KEY, 
					String.valueOf(o.getSaleDispute().getSeverity())
				));
			}
			if(o.getSaleDispute().getResolved()!=null){
				o.setResolvedStr(ParamFacade.getInstance().getValue(RESOLVED_KEY, 
					String.valueOf(o.getSaleDispute().getResolved())
				));
			}
			//TODO 设置用户信息
		}
		page.setRecords(list);
		return page;
	}

	public Integer updateResolvedStatus(Short status, Integer resolvedUid,
			Integer eid, Integer id) {
		Assert.notNull(status, messageSource.getMessage("assert.notnull", new String[]{"status"}, null));
		Assert.notNull(resolvedUid, messageSource.getMessage("assert.notnull", new String[]{"resolvedUid"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return saleDisputeDao.updateResolvedStatus(status, resolvedUid, eid, id);
	}

	public Integer updateSaleDisputeSimple(SaleDispute saleDispute) {
		Assert.notNull(saleDispute, messageSource.getMessage("assert.notnull", new String[]{"saleDispute"}, null));
		return saleDisputeDao.updateSaleDisputeSimple(saleDispute);
	}

	public Integer batchUpdateResolvedUid(Integer uid, Integer[] ids,
			Integer eid) {
		return null;
	}

	public Integer batchUpdateResolved(Short status, Integer[] ids, Integer eid) {
		Assert.notNull(ids, messageSource.getMessage("assert.notnull", new String[]{"ids"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
		Assert.notNull(status, messageSource.getMessage("assert.notnull", new String[]{"status"}, null));
		return saleDisputeDao.batchUpdateResolved(status, ids, eid);
	}

	public Integer batchDeleteSaleDispute(Integer[] ids, Integer eid) {
		Assert.notNull(ids, messageSource.getMessage("assert.notnull", new String[]{"ids"}, null));
		Assert.notNull(eid, messageSource.getMessage("assert.notnull", new String[]{"eid"}, null));
//		ordersDao.updateOrderStatusById(ordersDao., id, eid)  暂只删除，不更改订单状态，视最终的需求而定
		return saleDisputeDao.batchDeleteSaleDispute(ids, eid);
	}

}
