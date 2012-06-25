/**
 * Copyright 2010 ASTO.
 * All right reserved.
 * Created on 2010-3-11
 */
package net.caiban.auth.impl;

import java.util.List;

import net.caiban.auth.RoleDao;
import net.caiban.auth.RoleService;
import net.caiban.auth.domain.AuthRole;
import net.caiban.auth.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MessageSource messageSource;

	public Integer createRole(AuthRole role) {
		Assert.notNull(role, messageSource.getMessage("assert.notnull", new String[]{"role"}, null));
		Assert.hasText(role.getName(), messageSource.getMessage("assert.hastext", new String[]{"role.name"}, null));
		return roleDao.createRole(role);
	}

	public void createRoleRight(Integer roleId, Integer[] rightIds) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Assert.notNull(rightIds, messageSource.getMessage("assert.notnull", new String[]{"rightIds"}, null));
		roleDao.batchCreateRoleRight(roleId, rightIds);
	}

	public Integer deleteRole(Integer roleId) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		return roleDao.deleteRole(roleId);
	}

	public Integer deleteRoleRight(Integer roleId, Integer[] rightIds) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		if(rightIds!=null){
			return roleDao.batchDeleteRoleRight(roleId, rightIds);
		}else{
			return roleDao.deleteRoleRightByRole(roleId);
		}
	}

	public List<AuthRole> listRole() {
		return roleDao.listRole();
	}

	public void updateRole(AuthRole role) {
		Assert.notNull(role, messageSource.getMessage("assert.notnull", new String[]{"role"}, null));
		Assert.notNull(role.getId(), messageSource.getMessage("assert.notnull", new String[]{"role.id"}, null));
		roleDao.updateRole(role);
	}


}
