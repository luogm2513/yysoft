package net.caiban.auth;

import java.util.List;

import net.caiban.auth.domain.AuthRight;
import net.caiban.auth.dto.ExtTreeNode;


public interface RightService {

	/**
	 * 根据父权限列出子权限信息,不包括孙子节点
	 * @param parentId:父权限ID号
	 * @return
	 */
	public List<AuthRight> listRightViaParent(Integer parentId);

	/**
	 * 通过左右值获取该节点下的所有节点信息
	 * @param l:节点左值,不可以为null
	 * @param r:节点右值,不可以为null
	 * @return
	 */
	public List<AuthRight> listRightViaLR(Integer l, Integer r);

	/**
	 * 通过权限ID获取单个权限
	 * @param id:权限ID,不可以为null
	 * @return
	 */
	public AuthRight listOneRightViaId(Integer id);

	/**
	 * 更新权限基本信息
	 * @param right:被更新的权限对象,不可以为null<br/>
	 * 		right.id是被更新的权限ID号,不可以为null
	 * 		只更新以下属性:name,content,menu,menuUrl,menuCss
	 */
	public void updateRightBaseInfo(AuthRight right);

	/**
	 * 创建权限信息
	 * @param right:待创建的权限对象,不可以为null<br/>
	 * @return 新创建的权限ID
	 */
	public Integer createRight(AuthRight right);

	/**
	 * 删除权限信息<br/>
	 * 同时删除子权限信息,同时删除与权限表关联的角色权限和部门权限
	 * @param rightId:待删除的权限编号,不可以为null和0,否则抛出异常
	 * @return 非负数,表示影响行数
	 */
	public Integer deleteRight(Integer rightId);

	/**
	 * 列出某个角色的所拥有权限信息,并按照左侧排序(树结构)
	 * @param roleId:角色编号,不可以为null
	 * @param parentId:父权限ID号,当parentRight为null时默认父节点编号为{@link com.ast.ast1949.persist.auth.RightDao#DEFAULT_RIGHT_ROOT}
	 *
	 * @return
	 */
	public List<AuthRight> listRightInRole(Integer roleId, Integer parentId);

	/**
	 * 列出非某角色所拥有的权限信息,并按左侧排序
	 * @param roleId:角色编号,不可以为null
	 * @param parentId:父权限ID号,当parentRight为null时默认父节点编号为@see {@link #DEFAULT_RIGHT_ROOT}
	 * @return
	 */
	public List<AuthRight> listRightNotInRole(Integer roleId, Integer parentId);
	
	/**
	 * 指定默认是否使用带复选框的树节点对象
	 */
	final static boolean DEFAULT_CHECKED_USE	= false;
	
	/**
	 * 将一组权限信息列表转换成Ext树结构节点列表
	 * @param rightList:待转换的权限列表,不可以为null
	 * @param check:是否带复选框,为null时使用默认值{@link #DEFAULT_CHECKED_USE}
	 * @return
	 */
	public List<ExtTreeNode> changeRightListToTreeNode(List<AuthRight> rightList, Boolean check);
	
}
