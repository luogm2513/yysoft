/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-9
 */
package net.caiban.action.eshop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.caiban.action.BaseAction;
import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.dto.ExtResult;
import net.caiban.dto.Paging;
import net.caiban.facade.ParamFacade;
import net.caiban.service.eshop.EshopBuyerService;
import net.caiban.util.StringUtil;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Scope("prototype")
@Service
public class EshopBuyerAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2111973348731674568L;
	
	@Autowired
	private EshopBuyerService eshopBuyerService;
	
	final static String COMBO_VALUABLE_KEY = "combo_eshop_buyer_valuable";
	
	private EshopBuyer eshopBuyer;
	private ExtResult result;
	private Paging page;
	
	private String idArray;
	
	public EshopBuyerAction(){
		eshopBuyer = new EshopBuyer();
		result = new ExtResult();
		page = new Paging();
	}
	
	public String index(){
		//初始化,让前台可以判断是否允许查看全部联系信息,还是只允许查看自己的
		eshopBuyer.setUid(getSessionAuthUser().getId());
		return "index";
	}
	
	public String listBuyer(){
		eshopBuyer.setEid(getSessionEshop().getId());
		page = eshopBuyerService.pageEshopBuyer(eshopBuyer, page);
		return "extpage";
	}
	
	public String listOneBuyer(){
		List<EshopBuyer> list = new ArrayList<EshopBuyer>(); 
		list.add(eshopBuyerService.listOneEshopBuyerById(eshopBuyer.getId(), getSessionEshop().getId()));
		page.setRecords(list);
		return "extpage";
	}

	public String createBuyer(){
		eshopBuyer.setEid(getSessionEshop().getId());
		eshopBuyer.setUid(getSessionAuthUser().getId());
		
		if(eshopBuyerService.createEshopBuyer(eshopBuyer)>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	public String updateBuyer(){
		eshopBuyerService.updateEshopBuyer(eshopBuyer);
		result.setSuccess(true);
		return "extresult";
	}
	
	public String deleteBuyer(){
		eshopBuyerService.batchDeleteEshopBuyerBuyIdArray(StringUtil.str2intArray(idArray));
		result.setSuccess(true);
		return "extresult";
	}
	
	public String valuableCombo(){
		page.setRecords(ParamFacade.getInstance().getParamListByType(COMBO_VALUABLE_KEY));
		return "extpage";
	}
	
	/**********************************************/
	/**
	 * @return the eshopBuyer
	 */
	public EshopBuyer getEshopBuyer() {
		return eshopBuyer;
	}

	/**
	 * @param eshopBuyer the eshopBuyer to set
	 */
	public void setEshopBuyer(EshopBuyer eshopBuyer) {
		this.eshopBuyer = eshopBuyer;
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
	 * @return the idArray
	 */
	public String getIdArray() {
		return idArray;
	}

	/**
	 * @param idArray the idArray to set
	 */
	public void setIdArray(String idArray) {
		this.idArray = idArray;
	}
	
	
}
