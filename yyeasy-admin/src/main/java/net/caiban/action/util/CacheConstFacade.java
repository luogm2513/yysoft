/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-5-2
 */
package net.caiban.action.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class CacheConstFacade {
	
	private static Map<String, Object> cachedMap = new HashMap<String, Object>();
	
	public void startup(){
		ResourceBundle bundle = ResourceBundle.getBundle("web");
		for(String key:bundle.keySet()){
			cachedMap.put(key, bundle.getString(key));
		}
	}
	
	public static Object getValue(String key){
		return cachedMap.get(key);
	}
	
	public static String getStringValue(String key){
		return (String) cachedMap.get(key);
	}
	
}
