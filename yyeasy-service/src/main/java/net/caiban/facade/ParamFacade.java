/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-9
 */
package net.caiban.facade;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.caiban.domain.auth.Param;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class ParamFacade {
	
	private static ParamFacade _instance = null;
	
//	private static List<Param> paramList;
	private static Map<String, List<Param>> paramMap = null;
	
	synchronized public static ParamFacade getInstance(){
		if(_instance == null){
			_instance = new ParamFacade();
		}
		return _instance;
	}
	
	public void initCache(List<Param> list){
		if(list!=null){
			paramMap = new LinkedHashMap<String, List<Param>>();
			
			for(Param p:list){
				if(paramMap.get(p.getType())==null){
					paramMap.put(p.getType(), new ArrayList<Param>());
				}
				paramMap.get(p.getType()).add(p);
			}
		}
	}
	
	public Map<String, String> getParamByType(String type){
		if(type==null){
			return null;
		}
		
		Map<String, String> map=new LinkedHashMap<String, String>();
		
		List<Param> list = paramMap.get(type);
		
		if(list != null){
			for(Param p:list){
				if(type.equals(p.getType())){
					map.put(p.getKey(), p.getValue());
				}
			}
		}
		
		return map;
	}
	
	public List<Param> getParamListByType(String type){
		if(type==null){
			return null;
		}
		
		return paramMap.get(type);
	}
	
	public String getValue(String type, String key){
		Map<String, String> map= getParamByType(type);
		return map.get(key);
	}
}
