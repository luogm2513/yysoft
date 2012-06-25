/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-9
 */
package net.caiban.service.auth.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.caiban.domain.auth.Param;
import net.caiban.persist.auth.ParamDao;
import net.caiban.service.auth.ParamService;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("paramService")
public class ParamServiceImpl implements ParamService {

	@Autowired
	private ParamDao paramDao;
	
	public List<Param> listAllParam(Short used) {
		return paramDao.listAllParam(used);
	}

}
