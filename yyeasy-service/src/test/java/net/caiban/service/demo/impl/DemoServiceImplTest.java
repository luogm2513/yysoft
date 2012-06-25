package net.caiban.service.demo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import net.caiban.service.BaseServiceTestCase;
import net.caiban.service.demo.DemoService;


public class DemoServiceImplTest extends BaseServiceTestCase{

	@Autowired
	DemoService demoService;
	
	public void test_demo(){
		System.out.println(demoService.queryProjectName("yyeasy"));
	}
}
