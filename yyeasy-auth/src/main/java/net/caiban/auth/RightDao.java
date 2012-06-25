/**
 * Copyright 2010 ASTO.
 * All right reserved.
 * Created on 2010-3-11
 */
package net.caiban.auth;

import java.util.List;

import net.caiban.auth.domain.AuthRight;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public interface RightDao {

	/**
	 * 默认根权限ID
	 */
	final static Integer DEFAULT_RIGHT_ROOT = 0;

	/**
	 * 通过权限ID获取该权限下的子权限信息,不包括孙子
	 * @param parentId:父节点ID
	 * @return
	 */
	public List<AuthRight> listRightViaParent(Integer parentId);

	/**
	 * 通过父节点的左右值获取所有子节点信息
	 * @param left:父权限左值
	 * @param right:父权限右值
	 * @return
	 */
	public List<AuthRight> listRightViaLR(Integer left, Integer right);

	/**
	 * 通过权限ID获取权限信息
	 * @param id:权限ID,不可以为null
	 * @return
	 */
	public AuthRight listOneRightViaId(Integer id);

	/**
	 * 更新权限基本信息
	 * @param right:权限对象,不能为null<br/>
	 * parentId不更新,只更新以下属性:name,content,menu,menuUrl,menuCss
	 */
	public void updateRightBaseInfo(AuthRight right);

	/**
	 * 创建一个权限信息,创建时需要更新其他权限信息的左右值
	 * @param right:权限对象,不可以为null<br/>
	 * 		属性parentId为null时默认为@see {@link #DEFAULT_RIGHT_ROOT}
	 * @return
	 */
	public Integer createRight(AuthRight right);

	/**
	 * 删除本权限及子权限,同时删除角色权限的关联,部门权限的关联信息
	 * @param rightId:待删除的权限编号,不可以为null
	 * @return
	 */
	public Integer deleteRight(Integer rightId);

	/**
	 * 列出某角色关联或不关联的,且以某个权限为根权限的所有子权限信息
	 * @param roleId:角色ID
	 * @param parentId:父权限ID,为null时使用默认值 {@link #DEFAULT_RIGHT_ROOT};
	 * @param inrole:true表示角色关联,false表示非角色关联
	 * @return
	 */
	public List<AuthRight> listRightByRole(Integer roleId, Integer parentId, boolean inrole);

	/**
	 * 通过用户名,查找该用户的某一权限下的子权限
	 * @param username:用户名,不能为null
	 * @param parentId:权限父节点编号,不能为null
	 * @return
	 */
	public List<AuthRight> listUserRightByParent(String username, Integer parentId);
	
	/**
	 * 通过用户名,查找该用户所拥有的权限
	 * @param username:用户名,不能为null
	 * @return
	 */
	public List<AuthRight> listUserRightByUsername(String username);
}
