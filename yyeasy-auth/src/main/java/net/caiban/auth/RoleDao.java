/**
 * Copyright 2010 ASTO.
 * All right reserved.
 * Created on 2010-3-11
 */
package net.caiban.auth;

import java.util.List;

import net.caiban.auth.domain.AuthRole;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface RoleDao {

	/**
	 * 查询所有角色信息
	 * @return
	 */
	public List<AuthRole> listRole();

	/**
	 * 创建角色信息
	 * @param role:角色对象,不可以为null
	 * @return
	 */
	public Integer createRole(AuthRole role);

	/**
	 * 更新角色信息
	 * @param role:角色对象,不可以为null
	 */
	public void updateRole(AuthRole role);

	/**
	 * 删除角色信息
	 * @param roleId:角色编号,不可以为null
	 * @return
	 */
	public Integer deleteRole(Integer roleId);

	/**
	 * 批量创建角色与权限关联
	 * @param roleId:要关联的角色ID,不可以为null
	 * @param rightIds:要关联的权限编号数组,不可以为null
	 */
	public void batchCreateRoleRight(Integer roleId,Integer[] rightIds);

	/**
	 * 批量删除角色与权限的关联
	 * @param roleId:要删除关联的角色ID,不可以为null
	 * @param rightIds:要删除关联的权限编号数组,不可以为null
	 * @return
	 */
	public Integer batchDeleteRoleRight(Integer roleId, Integer[] rightIds);

	/**
	 * 删除某角色的角色权限关联
	 * @param roleId:角色ID,不可以为null
	 * @return
	 */
	public Integer deleteRoleRightByRole(Integer roleId);
}
