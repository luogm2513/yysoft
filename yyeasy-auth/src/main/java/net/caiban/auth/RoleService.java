package net.caiban.auth;

import java.util.List;

import net.caiban.auth.domain.AuthRole;

public interface RoleService {

	/**
	 * 列出所有角色信息
	 * @return
	 */
	public List<AuthRole> listRole();

	/**
	 * 创建新的角色信息
	 * @param role:不可以为null<br />
	 * 		属性name不可以为null或""
	 * @return
	 */
	public Integer createRole(AuthRole role);

	/**
	 * 更新角色信息
	 * @param role:不可以为null<br/>
	 * 		id不可以为null
	 *
	 */
	public void updateRole(AuthRole role);

	/**
	 * 根据角色编号删除角色信息,同时删除角色和权限的关联信息
	 * @param roleId:角色编号不可以为null
	 * @return
	 */
	public Integer deleteRole(Integer roleId);

	/**
	 * 关联角色与权限信息
	 * @param roleId:要关联的角色ID,不可以为null和0
	 * @param rightIds:要关联的权限ID数组,不可以为null
	 */
	public void createRoleRight(Integer roleId, Integer[] rightIds);

	/**
	 * 删除角色与权限的关联
	 * @param roleId:角色ID,不可以为null
	 * @param rightIds:要删除关联的权限ID数组,不可以为null
	 * @return
	 */
	public Integer deleteRoleRight(Integer roleId, Integer[] rightIds);
}
