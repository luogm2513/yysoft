/**
 * Copyright 2010 ASTO.
 * All right reserved.
 * Created on 2010-3-11
 */
package net.caiban.auth.impl;

import java.util.ArrayList;
import java.util.List;

import net.caiban.auth.AuthService;
import net.caiban.auth.RightDao;
import net.caiban.auth.UserDao;
import net.caiban.auth.domain.AuthRight;
import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.exception.AuthException;
import net.caiban.auth.util.Assert;
import net.caiban.auth.util.MD5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
@Component("authService")
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RightDao rightDao;
	@Autowired
	private MessageSource messageSource;
	
	public void changePassword(String username, String originalPassword,
			String newPassword, String verifyPassword) throws AuthException{
		Assert.notNull(username, messageSource.getMessage("assert.notnull", new String[]{"username"}, null));
		Assert.notNull(originalPassword, messageSource.getMessage("assert.notnull", new String[]{"originalPassword"}, null));
		Assert.notNull(newPassword, messageSource.getMessage("assert.notnull", new String[]{"newPassword"}, null));
		Assert.notNull(verifyPassword, messageSource.getMessage("assert.notnull", new String[]{"verifyPassword"}, null));
		if(!newPassword.equals(verifyPassword)){
			throw new AuthException(messageSource.getMessage("auth.validate.errorverifypassword", new String[]{},null));
		}
		AuthUser user = userDao.listOneUserByUsername(username);
		if(user==null){
			throw new AuthException(messageSource.getMessage("auth.validate.nousername", new String[]{username},null));
		}
		if(!user.getPassword().equals(MD5.encode(originalPassword))){
			throw new AuthException(messageSource.getMessage("auth.validate.errorpassword", new String[]{},null));
		}
		userDao.updatePasswordById(user.getId(),MD5.encode(newPassword));
	}

	public List<AuthRight> listRight(Integer parentRight, String username) {
		return null;
	}

	public List<AuthRight> listUserRight(String username) {
		return rightDao.listUserRightByUsername(username);
	}

	public AuthUser validateLogin(AuthUser user) throws AuthException {
		Assert.notNull(user, messageSource.getMessage("assert.notnull", new String[]{"user"}, null));
		Assert.notNull(user.getPassword(), messageSource.getMessage("assert.notnull", new String[]{"user.password"}, null));
		AuthUser u = userDao.listOneUserByUsername(user.getUsername());
		if(u==null){
			throw new AuthException(messageSource.getMessage("auth.validate.nousername", new String[]{user.getUsername()}, null));
		}
		if(!u.getPassword().equals(MD5.encode(user.getPassword()))){
			throw new AuthException(messageSource.getMessage("auth.validate.errorpassword", new String[]{}, null));
		}
		return u;
	}

	public List<String> listUserRightContent(String username) {
		Assert.notNull(username, messageSource.getMessage("assert.notnull", new String[]{"username"}, null));
		List<AuthRight> rightList = rightDao.listUserRightByUsername(username);
		List<String> rightContentList = new ArrayList<String>();
		for(AuthRight right:rightList){
			if(right.getContent()!=null && right.getContent().length()>0){
				String[] str = right.getContent().split("\\|");
				for(String s:str){
					rightContentList.add(s);
				}
			}
		}
		return rightContentList;
	}

	public List<AuthRight> listUserRightByParent(String username,
			Integer parentId) {
		Assert.notNull(username, messageSource.getMessage("assert.notnull", new String[]{"username"}, null));
		if(parentId == null){
			parentId = RightDao.DEFAULT_RIGHT_ROOT;
		}
		return rightDao.listUserRightByParent(username, parentId);
	}


}
