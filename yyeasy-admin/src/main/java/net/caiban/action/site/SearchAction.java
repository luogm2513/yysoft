/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-2
 */
package net.caiban.action.site;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import net.caiban.action.BaseAction;
import net.caiban.domain.orders.Logistics;
import net.caiban.dto.ExtResult;
import net.caiban.dto.Paging;
import net.caiban.service.eshop.EshopService;
import net.caiban.service.orders.LogisticsService;
import net.caiban.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.sun.tools.javac.resources.javac;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Scope("prototype")
@Service
public class SearchAction extends BaseAction {

	@Autowired
	private LogisticsService logisticsService;
	@Autowired
	private EshopService eshopService;
	
	private Logistics logistics;
	private String q;
	private ExtResult result;
	private Paging page;
	
	public SearchAction(){
		logistics = new Logistics();
		page = new Paging();
		result=new ExtResult();
	}
	
	public String logistics() throws UnsupportedEncodingException{
		System.out.println("here is search logistics!");
		if(StringUtil.isNotEmpty(q)){
//			q=URLEscape.unescape(q);
			q=URLDecoder.decode(q, "uUTF-8");
		}
		page=logisticsService.search(logistics, q, page);
		
		return "logistics";
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
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
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
}
