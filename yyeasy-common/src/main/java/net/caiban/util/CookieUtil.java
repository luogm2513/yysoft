package net.caiban.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	/**
	 * desc:得到cookie
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getLocalInfo(HttpServletRequest request, String key){
		Cookie cookies[]=request.getCookies();
		if(cookies == null)
			return null;

		String value = null;
		for(Cookie cookie : cookies){
			String cname = cookie.getName();
			if(cname.equals(key)){
				value = cookie.getValue();
			}
		}

		return value;
	}
	
	/**
	 * desc:设置cookie
	 * @param response
	 * @param domain
	 * @param key
	 * @param value
	 * @param cookieAge
	 */
	public static void setLocalInfo(HttpServletResponse response, String domain, String key, String value, int cookieAge){
		Cookie cookieToken= new Cookie(key, value);
		cookieToken.setDomain(domain);
		cookieToken.setPath("/");
		if(cookieAge!=0){  //允许不设置时限
			cookieToken.setMaxAge(cookieAge);
		}
		response.addCookie(cookieToken);
	}
	
	/**
	 * @param response
	 * @param domain
	 * @param key
	 */
	public static void removeLocalInfo(HttpServletResponse response, String domain, String key){
		Cookie cookieToken= new Cookie(key, "");
		cookieToken.setDomain(domain);
		cookieToken.setPath("/");
		cookieToken.setMaxAge(0);
		response.addCookie(cookieToken);
	}
}
