/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-9
 */
package net.caiban.action.util;

import net.caiban.facade.ParamFacade;
import net.caiban.service.auth.ParamService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class InitSystem {

	@Autowired
	private ParamService paramService;
	
	public void startup(){
		ParamFacade.getInstance().initCache(paramService.listAllParam((short)0));
	}
	
	public void shutdown(){
		
	}
}
