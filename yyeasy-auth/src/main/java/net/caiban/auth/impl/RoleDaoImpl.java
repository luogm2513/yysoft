/**
 * Copyright 2010 ASTO.
 * All right reserved.
 * Created on 2010-3-11
 */
package net.caiban.auth.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.auth.RoleDao;
import net.caiban.auth.domain.AuthRole;
import net.caiban.auth.exception.AuthException;
import net.caiban.auth.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("roleDao")
public class RoleDaoImpl extends SqlMapClientDaoSupport implements RoleDao {
	
	@Autowired
	private MessageSource messageSource;

	public void batchCreateRoleRight(Integer roleId, Integer[] rightIds) {
		try {
			getSqlMapClient().startBatch();
			for(Integer i:rightIds){
				Map<String, Integer> root=new HashMap<String, Integer>();
				root.put("roleid", roleId);
				root.put("rightid", i);
				getSqlMapClientTemplate().insert("authRole.createRoleRight", root);
			}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new AuthException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
	}

	public Integer batchDeleteRoleRight(Integer roleId, Integer[] rightIds) {
		Integer impact=0;
		try {
			getSqlMapClient().startBatch();
			for(Integer i:rightIds){
				Map<String, Integer> root=new HashMap<String, Integer>();
				root.put("rightid", i);
				root.put("roleid", roleId);
				impact+=getSqlMapClientTemplate().delete("authRole.deleteRoleRight", root);
			}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new AuthException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer createRole(AuthRole role) {
		return (Integer) getSqlMapClientTemplate().insert("authRole.createRole", role);
	}

	public Integer deleteRole(Integer roleId) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		return getSqlMapClientTemplate().delete("authRole.deleteRoleById", roleId);
	}

	@SuppressWarnings("unchecked")
	public List<AuthRole> listRole() {
		return getSqlMapClientTemplate().queryForList("authRole.listRole");
	}

	public void updateRole(AuthRole role) {
		getSqlMapClientTemplate().update("authRole.updateRoleById", role);
	}

	public Integer deleteRoleRightByRole(Integer roleId) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		return getSqlMapClientTemplate().delete("authRole.deleteRoleRightByRoleId", roleId);
	}

}
