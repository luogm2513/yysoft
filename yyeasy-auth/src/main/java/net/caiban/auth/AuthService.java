/**
 * Copyright 2010 ASTO.
 * All right reserved.
 * Created on 2010-3-11
 */
package net.caiban.auth;

import java.util.List;

import net.caiban.auth.domain.AuthRight;
import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.exception.AuthException;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface AuthService {

	/**
	 * 通过用户名查询用户权限
	 * @param username:用户账户,不可以为null或"",否则抛出异常
	 * @return
	 */
	public List<AuthRight> listUserRight(String username);

	/**
	 * 查询某个用户的某个权限的子权限,或查询某个权限下的子权限
	 * @param parentRight:父节点编号,为null时表示根节点
	 * @param username:用户名为null或""时,返回父权限ID等于parentRight的所有子权限
	 * @return
	 */
	public List<AuthRight> listRight(Integer parentRight, String username);

	/**
	 * 更改密码
	 * @param username:要更改密码的用户账户,不可以为null或""
	 * @param originalPassword:原始密码,不可以为null或""
	 * @param newPassword:符合密码规则的新密码,不可以为null或""
	 * @param verifyPassword:确认新密码,与newPassword一样
	 */
	public void changePassword(String username, String originalPassword, String newPassword, String verifyPassword)
		throws AuthException;

	/**
	 * 检查用户登录信息是否正确
	 * @param user:待检查的登录信息,不可以为null
	 * @return 验证通过时返回true,否则返回false
	 */
	public AuthUser validateLogin(AuthUser user) throws AuthException;
	
	/**
	 * 通过用户名查询用户权限,返回所有权限内容
	 * @param username:用户名,不能为null
	 * @return
	 */
	public List<String> listUserRightContent(String username);
	
	/**
	 * 列出某个用户的某个权限下的所有子权限
	 * @param username:用户名,不能为null
	 * @param parentId:父节点ID
	 * @return
	 */
	public List<AuthRight> listUserRightByParent(String username, Integer parentId);
}
