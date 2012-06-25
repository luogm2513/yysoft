package net.caiban.persist.demo.impl;

import org.springframework.stereotype.Component;
import net.caiban.persist.demo.DemoDao;
import net.caiban.util.Assert;

@Component("demoDao")
public class DemoDaoImpl implements DemoDao {

	public String queryProjectName(String name) {
		Assert.notNull(name, "name is not null");
		return name;
	}

}
