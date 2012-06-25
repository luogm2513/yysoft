/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-18
 */
package net.caiban.action.auth;

import net.caiban.action.BaseAction;
import net.caiban.auth.RightService;
import net.caiban.auth.RoleService;
import net.caiban.auth.UserService;
import net.caiban.auth.domain.AuthRole;
import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.dto.Paging;
import net.caiban.dto.ExtResult;
import net.caiban.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Scope("prototype")
@Service
public class RoleAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -933480005627967415L;

	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private RightService rightService;
	
	private AuthRole role;
	private ExtResult result;
	private Paging page;
	private String rightArray;
	private String userArray;
	private AuthUser user;
	
	public RoleAction(){
		role = new AuthRole();
		result = new ExtResult();
		page = new Paging();
		user = new AuthUser();
	}
	
	public String index(){
		return "index";
	}
	
	public String listRole(){
		page.setRecords(roleService.listRole());
		return "extpage";
	}
	
	public String createRole(){
		if(roleService.createRole(role)>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	public String updateRole(){
		roleService.updateRole(role);
		result.setSuccess(true);
		return "extresult";
	}
	
	public String deleteRole(){
		if(roleService.deleteRole(role.getId())>0){
			result.setSuccess(true);
		}
		return "extresult";
	}
	
	public String listRightInRole(){
		page.setRecords(rightService.listRightInRole(role.getId(),null));
		return "extpage";
	}
	
	public String listRightNotInRole(){
		page.setRecords(rightService.listRightNotInRole(role.getId(),null));
		return "extpage";
	}
	
	public String createRoleRight(){
		roleService.createRoleRight(role.getId(), StringUtil.str2intArray(rightArray));
		result.setSuccess(true);
		return "extresult";
	}
	
	public String deleteRoleRight(){
		roleService.deleteRoleRight(role.getId(), StringUtil.str2intArray(rightArray));
		result.setSuccess(true);
		return "extresult";
	}
	
	public String listUserInRole(){
		page = userService.pageUserInRole(role.getId(), user, page);
		return "extpage";
	}
	
	public String listUserNotInRole(){
		page = userService.pageUserNotInRole(role.getId(), user, page);
		return "extpage";
	}
	
	public String createUserRole(){
		userService.createUserRole(role.getId(), StringUtil.str2intArray(userArray));
		result.setSuccess(true);
		return "extresult";
	}
	
	public String deleteUserRole(){
		userService.deleteUserRole(role.getId(), StringUtil.str2intArray(userArray));
		result.setSuccess(true);
		return "extresult";
	}
	
	/**
	 * @return the role
	 */
	public AuthRole getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(AuthRole role) {
		this.role = role;
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
	 * @return the rightArray
	 */
	public String getRightArray() {
		return rightArray;
	}

	/**
	 * @param rightArray the rightArray to set
	 */
	public void setRightArray(String rightArray) {
		this.rightArray = rightArray;
	}

	/**
	 * @return the userArray
	 */
	public String getUserArray() {
		return userArray;
	}

	/**
	 * @param userArray the userArray to set
	 */
	public void setUserArray(String userArray) {
		this.userArray = userArray;
	}
	
	
}
