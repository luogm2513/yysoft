/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-22
 */
package net.caiban.auth;

import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.dto.Paging;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface UserService {

	/**
	 * 分页列出某个角色下面的用户信息
	 * @param roleId:角色编号,不可以为null
	 * @param user:查询条件,不可以为null<br/>
	 * 		user.username:可作为查询条件查询,过滤用户名信息
	 * @param page:分页信息,不可以为null,最后会被返回,返回时设置了totals的值
	 * @return
	 */
	public Paging pageUserInRole(Integer roleId, AuthUser user, Paging page);
	
	/**
	 * 分页列出蜚某个角色下面的用户信息
	 * @param roleId:角色编号,不可以为null
	 * @param user:查询条件,不可以为null<br/>
	 * 		user.username:可作为查询条件,过滤用户名信息
	 * @param page:分页信息,不可以为null,最后会被返回,返回时设置了totals的值
	 * @return
	 */
	public Paging pageUserNotInRole(Integer roleId, AuthUser user, Paging page);
	
	/**
	 * 创建用户角色关联
	 * @param roleId:角色ID,不可以为null
	 * @param userArray:用户列表,不可以为null
	 * @return
	 */
	public Integer createUserRole(Integer roleId, Integer[] userArray);
	
	/**
	 * 删除用户角色关联
	 * @param roleId:角色ID,不可以为null
	 * @param userArray:用户列表,不可以为null
	 * @return
	 */
	public Integer deleteUserRole(Integer roleId, Integer[] userArray);
	
}
