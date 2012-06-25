/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-18
 */
package net.caiban.action.orders;

import java.util.ArrayList;
import java.util.List;

import net.caiban.action.BaseAction;
import net.caiban.domain.orders.Logistics;
import net.caiban.dto.ExtResult;
import net.caiban.dto.Paging;
import net.caiban.service.orders.LogisticsService;
import net.caiban.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Scope("prototype")
@Service
public class LogisticsAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9145700532449341118L;
	@Autowired
	private LogisticsService logisticsService;
	
	private Logistics logistics;
	private Paging page;
	private ExtResult result;
	
	private String logisticsArray;
	private Integer eshopid;
	
	public LogisticsAction(){
		logistics = new Logistics();
		page = new Paging();
		result = new ExtResult();
	}

	/**
	 * 进入物流信息管理首页面
	 * @return
	 */
	public String index(){
		eshopid = getSessionEshop().getId();
		return "index";
	}
	
	public String listLogistics(){
		logistics.setEid(getSessionEshop().getId());
		page = logisticsService.pageLogisticsWithEid(logistics, page);
		return "extpage";
	}
	
	public String createLogistics(){
		logistics.setEid(getSessionEshop().getId());
		logistics.setUid(getSessionAuthUser().getId());
		
		if(logisticsService.createLogisticsByEid(logistics)>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	public String updateLogistics(){
		logisticsService.updateLogisticsById(logistics);
		result.setSuccess(true);
		return "extresult";
	}
	
	public String deleteLogistics(){
		logisticsService.batchDeleteLogisticsByEid(StringUtil.str2intArray(logisticsArray));
		result.setSuccess(true);
		return "extresult";
	}
	
	public String listOneLogistics(){
		List list = new ArrayList();
		list.add(logisticsService.listOneLogisticsById(logistics.getId()));
		page.setRecords(list);
		return "extpage";
	}
	
	/**
	 * @return the logistics
	 */
	public Logistics getLogistics() {
		return logistics;
	}

	/**
	 * @param logistics the logistics to set
	 */
	public void setLogistics(Logistics logistics) {
		this.logistics = logistics;
	}

	/**
	 * @return the page
	 */
	public Paging getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Paging page) {
		this.page = page;
	}

	/**
	 * @return the result
	 */
	public ExtResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ExtResult result) {
		this.result = result;
	}

	/**
	 * @return the logisticsArray
	 */
	public String getLogisticsArray() {
		return logisticsArray;
	}

	/**
	 * @param logisticsArray the logisticsArray to set
	 */
	public void setLogisticsArray(String logisticsArray) {
		this.logisticsArray = logisticsArray;
	}

	/**
	 * @return the eshopid
	 */
	public Integer getEshopid() {
		return eshopid;
	}

	/**
	 * @param eshopid the eshopid to set
	 */
	public void setEshopid(Integer eshopid) {
		this.eshopid = eshopid;
	}
	
	
}
