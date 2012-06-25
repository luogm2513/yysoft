/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-18
 */
package net.caiban.auth;

import java.util.List;

import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.dto.Paging;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface UserDao {
	
	final static Integer DEFAULT_BLOCKED = 0;
	
	/**
	 * 创建用户角色关联
	 * @param roleId:角色ID,不能为null
	 * @param userArray:一组待创建关联的用户,不可以为null
	 * @return
	 */
	public Integer createUserRole(Integer roleId, Integer[] userArray);
	
	/**
	 * 删除用户角色关联
	 * @param roleId:角色ID,不能为null
	 * @param userArray:一组待删除关联的用户,不可以为null
	 * @return
	 */
	public Integer deleteUserRole(Integer roleId, Integer[] userArray);
	
	/**
	 * 计算某个角色下面所有用户的数量
	 * @param roleId:角色ID,不能为null
	 * @param user:用户查询条件,只有username会作为查询条件起作用<br/>
	 * 		user.username:可做为查询条件,不为空时按照前匹配过滤用户信息<br/>
	 * 		user.blocked:默认为{@link #DEFAULT_BLOCKED},用户也可以自己设置
	 * @return
	 */
	public Integer countUserInRole(Integer roleId, AuthUser user);
	
	/**
	 * 列表某个角色下面的某一页用户
	 * @param roleId:角色编号,不可以为null
	 * @param user:用户查询条件,只有username会作为查询条件起作用<br/>
	 * 		user.username:可做为查询条件,不为空时按照前匹配过滤用户信息<br/>
	 * 		user.blocked:默认为{@link #DEFAULT_BLOCKED},用户也可以自己设置
	 * @param page:分页参数,不能为null<br/>
	 * 		page.start:表示起始记录<br/>
	 * 		page.limit:表示记录数量<br/>
	 * 		page.sort:表示排序的字段<br/>
	 * 		page.dir:表示排序的规则,只允许"ASC"或"DESC"
	 * @return
	 */
	public List<AuthUser> listUserInRole(Integer roleId, AuthUser user, Paging page);
	
	/**
	 * 计算非某个角色下所有用户的数量
	 * @param roleId:角色ID,不能为null
	 * @param user:用户查询条件,只有username会作为查询条件起作用<br/>
	 * 		user.username:可做为查询条件,不为空时按照前匹配过滤用户信息<br/>
	 * 		user.blocked:默认为{@link #DEFAULT_BLOCKED},用户也可以自己设置
	 * @return
	 */
	public Integer countUserNotInRole(Integer roleId, AuthUser user);
	
	/**
	 * @param roleId:角色ID,不能为null
	 * @param user:用户查询条件,只有username会作为查询条件起作用<br/>
	 * 		user.username:可做为查询条件,不为空时按照前匹配过滤用户信息<br/>
	 * 		user.blocked:默认为{@link #DEFAULT_BLOCKED},用户也可以自己设置
	 * @param page:分页参数,不能为null<br/>
	 * 		page.start:表示起始记录<br/>
	 * 		page.limit:表示记录数量<br/>
	 * 		page.sort:表示排序的字段<br/>
	 * 		page.dir:表示排序的规则,只允许"ASC"或"DESC"
	 * @return
	 */
	public List<AuthUser> listUserNotInRole(Integer roleId, AuthUser user, Paging page);
	
	/**
	 * 通过用户名查找账户信息
	 * @param username:用户名,不能为null
	 * @return
	 */
	public AuthUser listOneUserByUsername(String username);
	
	/**
	 * 通过email查找账户信息
	 * @param email:邮件地址,不能为null,且必需是邮件格式
	 * @return
	 */
	public AuthUser listOneUserByEmail(String email);
	
	/**
	 * 通过账户ID查找账户信息
	 * @param id:账户ID,不能为null
	 * @return
	 */
	public AuthUser listOneUserById(Integer id);
	
	/**
	 * 更新用户密码
	 * @param id:待更新的用户ID,不可以为null
	 * @param password:新的用户密码,已加密,不可以为null
	 */
	public void updatePasswordById(Integer id, String password);
}
