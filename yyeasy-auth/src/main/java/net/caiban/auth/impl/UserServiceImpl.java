/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-23
 */
package net.caiban.auth.impl;

import java.util.List;

import net.caiban.auth.UserDao;
import net.caiban.auth.UserService;
import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.dto.Paging;
import net.caiban.auth.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private MessageSource messageSource;
	
	public Integer createUserRole(Integer roleId, Integer[] userArray) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Assert.notNull(userArray, messageSource.getMessage("assert.notnull", new String[]{"userArray"}, null));
		return userDao.createUserRole(roleId, userArray);
	}

	public Integer deleteUserRole(Integer roleId, Integer[] userArray) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Assert.notNull(userArray, messageSource.getMessage("assert.notnull", new String[]{"userArray"}, null));
		return userDao.deleteUserRole(roleId, userArray);
	}

	public Paging pageUserInRole(Integer roleId, AuthUser user, Paging page) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleId"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		List<AuthUser> userList = userDao.listUserInRole(roleId, user, page);
		page.setRecords(userList);
		page.setTotals(userDao.countUserInRole(roleId, user));
		return page;
	}

	public Paging pageUserNotInRole(Integer roleId, AuthUser user, Paging page) {
		Assert.notNull(roleId, messageSource.getMessage("assert.notnull", new String[]{"roleid"}, null));
		Assert.notNull(user, messageSource.getMessage("assert.notnull", new String[]{"user"}, null));
		Assert.notNull(page, messageSource.getMessage("assert.notnull", new String[]{"page"}, null));
		
		List<AuthUser> userList = userDao.listUserNotInRole(roleId, user, page);
		page.setRecords(userList);
		page.setTotals(userDao.countUserNotInRole(roleId, user));
		return page;
	}

}
