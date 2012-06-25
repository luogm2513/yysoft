/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-9-4
 */
package net.caiban.action.orders;

import java.util.ArrayList;
import java.util.List;

import net.caiban.action.BaseAction;
import net.caiban.domain.auth.Param;
import net.caiban.domain.orders.SaleDispute;
import net.caiban.dto.ExtResult;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.SaleDisputeDto;
import net.caiban.facade.ParamFacade;
import net.caiban.service.orders.SaleDisputeService;
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
public class SaleDisputeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private SaleDisputeService saleDisputeService;
	
	private String onlyme;  //用来标识是否只显示自己的信息
	private String resolvedArray;
	private String idArray;
	
	private Paging page;
	private SaleDispute saleDispute;
	private ExtResult result;
	private List<Param> paramsList;
	
	final static String COMBO_SEVERITY_KEY = "sale_dispute_severity";
	final static String COMBO_RESOLVED_KEY = "sale_dispute_resolved";
	
	public SaleDisputeAction(){
		page = new Paging();
		saleDispute = new SaleDispute();
		result = new ExtResult();
	}
	
	public String index(){
		//TODO 获取自己公司的员工信息
		paramsList = ParamFacade.getInstance().getParamListByType(COMBO_RESOLVED_KEY);
		return INDEX;
	}
	
	public String list(){
		SaleDisputeDto dto = new SaleDisputeDto();
		saleDispute.setEid(getSessionEshop().getId());
		if("Y".equals(onlyme)){
			saleDispute.setResolvedUid(getSessionAuthUser().getId());
		}
		if(StringUtil.isNotEmpty(resolvedArray)){
			dto.setResolvedArray(StringUtil.str2intArray(resolvedArray));
		}
		dto.setSaleDispute(saleDispute);
		page = saleDisputeService.pageSaleDispute(dto, page);
		return EXTPAGE;
	}
	
	public String listOneSaleDispute(){
		saleDispute = saleDisputeService.listOneSaleDisputeById(saleDispute.getId(), getSessionEshop().getId());
		List<SaleDispute> list = new ArrayList<SaleDispute>();
		list.add(saleDispute);
		page.setRecords(list);
		return EXTPAGE;
	}
	
	public String updateSaleDispute(){
		saleDispute.setEid(getSessionEshop().getId());
		if(saleDisputeService.updateSaleDisputeSimple(saleDispute)>0){
			result.setSuccess(true);
		}
		return EXTRESULT;
	}
	
	public String updateSaleDisputeResolved(){
		if(saleDisputeService.batchUpdateResolved(saleDispute.getResolved(), 
				StringUtil.str2intArray(idArray), getSessionEshop().getId())>0){
			result.setSuccess(true);
		}
		return EXTRESULT;
	}
	
	public String deleteSaleDispute(){
		if(saleDisputeService.batchDeleteSaleDispute(StringUtil.str2intArray(idArray), getSessionEshop().getId())>0){
			result.setSuccess(true);
		}
		return EXTRESULT;
	}
	
	public String severityCombo(){
		page.setRecords(ParamFacade.getInstance().getParamListByType(COMBO_SEVERITY_KEY));
		return EXTPAGE;
	}
	
	public String resolvedCombo(){
		page.setRecords(ParamFacade.getInstance().getParamListByType(COMBO_RESOLVED_KEY));
		return EXTPAGE;
	}
	
	/*******************************************************************/

	/**
	 * @return the onlyme
	 */
	public String getOnlyme() {
		return onlyme;
	}

	/**
	 * @param onlyme the onlyme to set
	 */
	public void setOnlyme(String onlyme) {
		this.onlyme = onlyme;
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
	 * @return the saleDispute
	 */
	public SaleDispute getSaleDispute() {
		return saleDispute;
	}

	/**
	 * @param saleDispute the saleDispute to set
	 */
	public void setSaleDispute(SaleDispute saleDispute) {
		this.saleDispute = saleDispute;
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
	 * @return the resolvedArray
	 */
	public String getResolvedArray() {
		return resolvedArray;
	}

	/**
	 * @param resolvedArray the resolvedArray to set
	 */
	public void setResolvedArray(String resolvedArray) {
		this.resolvedArray = resolvedArray;
	}

	/**
	 * @return the paramsList
	 */
	public List<Param> getParamsList() {
		return paramsList;
	}

	/**
	 * @param paramsList the paramsList to set
	 */
	public void setParamsList(List<Param> paramsList) {
		this.paramsList = paramsList;
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
