package net.caiban.action.demo;

import net.caiban.action.BaseAction;
import net.caiban.service.demo.DemoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
@Scope("prototype")
@Service
public class DemoAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -434695553706139906L;

	@Autowired
	private DemoService demoService;
	
	public String demo(){
//		System.out.println(demoService.queryProjectName("project demo"));
//		System.out.println("here is a struts test");
		demoService.queryProjectName("project demo");
		return "index";
	}
	
	public void initsystem(){
		
	}
	
}
