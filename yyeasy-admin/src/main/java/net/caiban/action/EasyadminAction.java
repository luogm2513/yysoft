/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-14
 */
package net.caiban.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.action.util.AdminConst;
import net.caiban.auth.AuthService;
import net.caiban.auth.RightService;
import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.dto.ExtTreeNode;
import net.caiban.auth.dto.SessionUserDto;
import net.caiban.auth.exception.AuthException;
import net.caiban.dto.ExtResult;
import net.caiban.dto.Paging;
import net.caiban.exception.ServiceLayerException;
import net.caiban.service.eshop.EshopService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Scope("prototype")
@Service
public class EasyadminAction extends BaseAction {
	
	@Autowired
	private AuthService authService;
	@Autowired
	private RightService rightService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private EshopService eshopService;
	
	private AuthUser user;
	private Integer parentRightId;
	private ExtResult result;
	private Paging page;
	private List<ExtTreeNode> exttree;
	private String originalPassword;
	private String newPassword;
	private String verifyPassword;
	
	public EasyadminAction(){
		user = new AuthUser();
		result = new ExtResult();
		page = new Paging();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 607273693089251096L;

	/**
	 * 跳转到用户登录页面
	 * @return
	 */
	public String login(){
		return "login";
	}
	
	/**
	 * 登出系统,清除登录信息
	 * @return
	 */
	public String logout(){
		cleanCachedSession(AdminConst.SESSION_AUTH);
		cleanCachedSession(AdminConst.SESSION_USER);
		return "login";
	}
	
	/**
	 * 登录用户检查
	 * @return
	 */
	public String checkuser(){
		try {
			AuthUser u = authService.validateLogin(user);
			if(u.getId()!=null && u.getId().intValue()>0){
				
				SessionUserDto userDto = new SessionUserDto();
				Map<String, Object> profileMap = new HashMap<String, Object>();
				userDto.setUser(u);
				userDto.setLoginIP(getLoginIP());
//				userDto.setEshop(eshopService.listOneEshopByUid(u.getId()));
				profileMap.put(AdminConst.SESSION_USER_KEY_ESHOP, eshopService.listOneEshopByUid(u.getId()));
				userDto.setProfileMap(profileMap);
				
				String sessionid = getCookiedSessionId();
				setCachedSession(AdminConst.SESSION_USER, userDto, sessionid);
				
				List<String> authList = authService.listUserRightContent(user.getUsername());
				setCachedSession(AdminConst.SESSION_AUTH, authList, sessionid);
				
				result.setSuccess(true);
			}
		} catch (AuthException e) {
			result.setData(messageSource.getMessage("auth.validate.login", 
					new String[]{user.getUsername(),e.getMessage()}, null));
		}
		return "extresult";
	}

	/**
	 * 后台管理首页
	 * @return
	 */
	public String index(){
		return "index";
	}
	
	/**
	 * 发生错误时跳转的页面
	 * @return
	 */
	public String error(){
		throw new ServiceLayerException("here is an test error for ajax");
//		return "error";
	}
	
	/**
	 * 进入系统后的首页面
	 * @return
	 */
	public String welcome(){
		return "welcome";
	}
	
	/**
	 * 权限被禁止时转向的地址
	 * @return
	 */
	public String forbiden(){
		return "forbiden";
	}
	
	/**
	 * 登录用户的菜单信息
	 * @return
	 */
	public String mymenu(){
		SessionUserDto userDto = getSessionUser();
		exttree = rightService.changeRightListToTreeNode(authService
				.listUserRightByParent(
						userDto.getUser().getUsername(),
						parentRightId), false);
		return "exttree";
	}
	
	public String changePassword(){
		try {
			authService.changePassword(getSessionAuthUser().getUsername(), 
					originalPassword, newPassword, verifyPassword);
			result.setSuccess(true);
		} catch (AuthException e) {
			result.setData(e.getMessage());
		}
		return "extresult";
	}

	/**
	 * @return the result
	 */
	public ExtResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(ExtResult result) {
		this.result = result;
	}

	/**
	 * @return the page
	 */
	public Paging getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Paging page) {
		this.page = page;
	}

	/**
	 * @return the exttree
	 */
	public List<ExtTreeNode> getExttree() {
		return exttree;
	}

	/**
	 * @param exttree the exttree to set
	 */
	public void setExttree(List<ExtTreeNode> exttree) {
		this.exttree = exttree;
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
	 * @return the parentRightId
	 */
	public Integer getParentRightId() {
		return parentRightId;
	}

	/**
	 * @param parentRightId the parentRightId to set
	 */
	public void setParentRightId(Integer parentRightId) {
		this.parentRightId = parentRightId;
	}
	
	/**
	 * @return the originalPassword
	 */
	public String getOriginalPassword() {
		return originalPassword;
	}

	/**
	 * @param originalPassword the originalPassword to set
	 */
	public void setOriginalPassword(String originalPassword) {
		this.originalPassword = originalPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the verifyPassword
	 */
	public String getVerifyPassword() {
		return verifyPassword;
	}

	/**
	 * @param verifyPassword the verifyPassword to set
	 */
	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	
}
