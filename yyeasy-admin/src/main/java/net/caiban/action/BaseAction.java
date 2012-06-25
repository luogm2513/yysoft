package net.caiban.action;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.caiban.action.util.AdminConst;
import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.dto.SessionUserDto;
import net.caiban.domain.eshop.Eshop;
import net.caiban.domain.eshop.EshopAccount;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements ServletRequestAware{

	HttpServletRequest request;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5174509438064309965L;
	
	public final static String EXTRESULT="extresult";
	public final static String EXTPAGE="extpage";
	public final static String INDEX = "index";
	
	/**
	 * IOC注入request
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String getCookiedSessionId(){
		// TODO 得到cookie里的sessionid
		return null;
	}

	public void setCachedSession(String key, Object object, String cookiedSessionId){
		request.getSession().setAttribute(key, object);
	}
	
	public Object getCachedSession(String key){
		return request.getSession().getAttribute(key);
	}
	
	public void cleanCachedSession(String key){
		request.getSession().removeAttribute(key);
	}
	
	public SessionUserDto getSessionUser(){
		SessionUserDto user = (SessionUserDto) getCachedSession(AdminConst.SESSION_USER);
		return user;
	}
	
	public AuthUser getSessionAuthUser(){
		return getSessionUser().getUser();
	}
	
	public Eshop getSessionEshop(){
		if(getSessionUser().getProfileMap()!=null){
			return (Eshop) getSessionUser().getProfileMap().get(AdminConst.SESSION_USER_KEY_ESHOP);
		}
		return null;
	}
	
	public EshopAccount getSessionEshopAccount(){
		if(getSessionUser().getProfileMap()!=null){
			return (EshopAccount) getSessionUser().getProfileMap().get(AdminConst.SESSION_USER_KEY_ACCOUNT);
		}
		return null;
	}
	
	public String getLoginIP(){
		return "";
	}
	
	@Deprecated
	public String getResource(String key){
		ResourceBundle bundle = ResourceBundle.getBundle("web");
		return bundle.getString(key);
	}
	
}
