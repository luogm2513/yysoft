/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-23
 */
package net.caiban.auth.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.caiban.auth.UserDao;
import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.dto.Paging;
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
@Component("userDao")
public class UserDaoImpl extends SqlMapClientDaoSupport implements UserDao {

	@Autowired
	private MessageSource messageSource;
	
	public Integer countUserInRole(Integer roleId, AuthUser user) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("roleid", roleId);
		root.put("user", user);
		return (Integer) getSqlMapClientTemplate().queryForObject("authUser.countUserInRole",root);
	}

	public Integer countUserNotInRole(Integer roleId, AuthUser user) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("roleid", roleId);
		root.put("user", user);
		return (Integer) getSqlMapClientTemplate().queryForObject("authUser.countUserNotInRole", root);
	}

	public Integer createUserRole(Integer roleId, Integer[] userArray) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Assert.notNull(userArray, messageSource.getMessage("assert.notnull", new String[]{"userArray"}, null));
		Integer impact = 0;
		try {
			getSqlMapClient().startBatch();
			for(Integer i:userArray){
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("roleid", roleId);
				root.put("userid", i);
				getSqlMapClientTemplate().insert("authUser.createUserRole", root);
				impact++;
			}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new AuthException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	public Integer deleteUserRole(Integer roleId, Integer[] userArray) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Assert.notNull(userArray, messageSource.getMessage("assert.notnull", new String[]{"userArray"}, null));
		Integer impact = 0;
		try {
			getSqlMapClient().startBatch();
			for(Integer i:userArray){
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("roleid", roleId);
				root.put("userid", i);
				impact += (Integer) getSqlMapClientTemplate().delete("authUser.deleteUserRole", root);
			}
			getSqlMapClient().executeBatch();
		} catch (SQLException e) {
			throw new AuthException(messageSource.getMessage("persist.sqlexception", 
					new String[]{e.getMessage(),e.getSQLState()}, null),e);
		}
		return impact;
	}

	@SuppressWarnings("unchecked")
	public List<AuthUser> listUserInRole(Integer roleId, AuthUser user,
			Paging page) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("user", user);
		root.put("page", page);
		root.put("roleid", roleId);
		
		return getSqlMapClientTemplate().queryForList("authUser.listUserInRole", root);
	}

	@SuppressWarnings("unchecked")
	public List<AuthUser> listUserNotInRole(Integer roleId, AuthUser user,
			Paging page) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("user", user);
		root.put("page", page);
		root.put("roleid", roleId);
		
		return getSqlMapClientTemplate().queryForList("authUser.listUserNotInRole", root);
	}

	public AuthUser listOneUserByEmail(String email) {
		Assert.notNull(email, messageSource.getMessage("assert.notnull", new String[]{"email"}, null));
		return (AuthUser) getSqlMapClientTemplate().queryForObject("authUser.listOneUserByEmail",email);
	}

	public AuthUser listOneUserById(Integer id) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		return (AuthUser) getSqlMapClientTemplate().queryForObject("authUser.listOneUserById",id);
	}

	public AuthUser listOneUserByUsername(String username) {
		Assert.notNull(username, messageSource.getMessage("assert.notnull", new String[]{"username"}, null));
		return (AuthUser) getSqlMapClientTemplate().queryForObject("authUser.listOneUserByUsername",username);
	}

	public void updatePasswordById(Integer id, String password) {
		Assert.notNull(id, messageSource.getMessage("assert.notnull", new String[]{"id"}, null));
		Assert.notNull(password, messageSource.getMessage("assert.notnull", new String[]{"password"}, null));
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", id);
		root.put("password", password);
		getSqlMapClientTemplate().update("authUser.updatePasswordById",root);
	}
}
