package net.caiban.service.demo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.caiban.persist.demo.DemoDao;
import net.caiban.service.demo.DemoService;
import net.caiban.util.Assert;

@Component("demoService")
public class DemoServiceImpl implements DemoService {

	@Autowired
	private DemoDao demoDao;
	
	public String queryProjectName(String name) {
		Assert.notNull(name, "name is not null");
		
		return "service :"+demoDao.queryProjectName(name);
	}

}
