/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-4-1
 */
package net.caiban.auth.dto;

import java.util.Map;

import net.caiban.auth.domain.AuthUser;

/**
 * 用来将登录用户的信息保存到SESSION中
 * @author Mays (x03570227@gmail.com)
 *
 */
public class SessionUserDto implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AuthUser user;
//	private Eshop eshop;
//	private EshopAccount eshopAccount;
	private String loginIP;
	private Map<String, Object> profileMap;
	
	/**
	 * @return the loginIP
	 */
	public String getLoginIP() {
		return loginIP;
	}
	/**
	 * @param loginIP the loginIP to set
	 */
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}
	/**
	 * @return the user
	 */
	public AuthUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(AuthUser user) {
		this.user = user;
	}
	/**
	 * @return the profileMap
	 */
	public Map<String, Object> getProfileMap() {
		return profileMap;
	}
	/**
	 * @param profileMap the profileMap to set
	 */
	public void setProfileMap(Map<String, Object> profileMap) {
		this.profileMap = profileMap;
	}
	
	
//	/**
//	 * @return the eshop
//	 */
//	public Eshop getEshop() {
//		return eshop;
//	}
//	/**
//	 * @param eshop the eshop to set
//	 */
//	public void setEshop(Eshop eshop) {
//		this.eshop = eshop;
//	}
//	/**
//	 * @return the eshopAccount
//	 */
//	public EshopAccount getEshopAccount() {
//		return eshopAccount;
//	}
//	/**
//	 * @param eshopAccount the eshopAccount to set
//	 */
//	public void setEshopAccount(EshopAccount eshopAccount) {
//		this.eshopAccount = eshopAccount;
//	}
}
