/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-28
 */
package net.caiban.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.dto.Paging;
import net.caiban.auth.util.MD5;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class UserServiceImplTest extends BaseAuthTestCase{
	
	@Autowired
	private UserService userService;
	
	final static short DEFAULT_BLOCK = 0;

	/**
	 * Test method for {@link net.caiban.service.auth.impl.UserServiceImpl#createUserRole(java.lang.Integer, java.lang.Integer[])}.
	 * @throws SQLException 
	 */
	@Test
	public void testCreateUserRole_no_attribute() throws SQLException {
		try {
			userService.createUserRole(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			clean();
			Integer i=createRole();
			userService.createUserRole(i, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testCreateUserRole_has_attribute() throws SQLException {
		clean();
		Integer role = createRole();
		String userArray = createUser(user("test", "test", "email", DEFAULT_BLOCK))+","+
			createUser(user("test", "test", "email", DEFAULT_BLOCK));
		
		Integer i=userService.createUserRole(role, str2intArray(userArray));
		assertEquals(i.intValue(), 2);
		
		//合着测试删除功能
		Integer di=userService.deleteUserRole(role, str2intArray(userArray));
		assertEquals(di.intValue(), 2);
	}

	/**
	 * Test method for {@link net.caiban.service.auth.impl.UserServiceImpl#deleteUserRole(java.lang.Integer, java.lang.Integer[])}.
	 */
	@Test
	public void testDeleteUserRole_no_attribute() {
		try {
			userService.deleteUserRole(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			userService.deleteUserRole(0, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Test method for {@link net.caiban.service.auth.impl.UserServiceImpl#pageUserInRole(java.lang.Integer, net.caiban.domain.auth.AuthUser, net.caiban.dto.Paging)}.
	 */
	@Test
	public void testPageUserInRole_no_attribute() {
		try {
			userService.pageUserInRole(null, null, null);
		} catch (IllegalArgumentException e) {
		}
		
		try {
			userService.pageUserInRole(0, null, null);
		} catch (IllegalArgumentException e) {
		}
		
		try {
			userService.pageUserInRole(0, new AuthUser(), null);
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testPageUserInRole_has_attribute() throws SQLException {
		clean();
		Integer role = createRole(); 
		List<Integer> userList= new ArrayList<Integer>(); 
		for(int i=0;i<22;i++){
			userList.add(createUser(user("test"+i, "test"+i, i+"email@caiban.net", DEFAULT_BLOCK)));
		}
		Integer[] userArray = new Integer[userList.size()];
		for(int i=0;i<userList.size();i++){
			userArray[i] = userList.get(i);
		}
		Paging page1=new Paging();
		//未关联的
		page1=userService.pageUserNotInRole(role, user(null, null, null, null), page1);
		assertNotNull(page1);
		assertEquals(page1.getRecords().size(), 20);
		
		Paging page2=new Paging();
		page2.setLimit(20);
		page2.setStart(20);
		page2=userService.pageUserNotInRole(role, user(null, null, null, null), page2);
		assertEquals(page2.getRecords().size(), 2);
		
		//关联的
		userService.createUserRole(role,userArray);
		Paging page3 = new Paging();
		page3=userService.pageUserInRole(role, user(null, null, null, null), page3);
		assertNotNull(page3);
		assertEquals(page3.getRecords().size(), 20);
		
		Paging page4 = new Paging();
		page4.setLimit(20);
		page4.setStart(20);
		page4 = userService.pageUserInRole(role, new AuthUser(), page4);
		assertEquals(page4.getRecords().size(), 2);
	}
	
	/**
	 * Test method for {@link net.caiban.service.auth.impl.UserServiceImpl#pageUserNotInRole(java.lang.Integer, net.caiban.domain.auth.AuthUser, net.caiban.dto.Paging)}.
	 */
	@Test
	public void testPageUserNotInRole_no_attribute() {
		try {
			userService.pageUserNotInRole(null, null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			userService.pageUserNotInRole(0, null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			userService.pageUserNotInRole(0, new AuthUser(), null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	/************prepare data*********/
	private void clean() throws SQLException{
		connection.prepareStatement("delete from auth_user_role").execute();
		connection.prepareStatement("delete from auth_user").execute();
		connection.prepareStatement("delete from auth_role").execute();
	}
	
	private Integer createRole() throws SQLException{
		String sql="";
		sql="insert into auth_role(name,remark) "+
			"values('dev','for junit test!')";
		connection.prepareStatement(sql).execute();

		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		return 0;
	}
	
	private Integer createUser(AuthUser user) throws SQLException{
		String sql="";
		sql="insert into auth_user(username,password,email) "+
			"values('"+user.getUsername()+"','"+MD5.encode(user.getPassword())+"','"+user.getEmail()+"')";
		connection.prepareStatement(sql).execute();

		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		return 0;
	}
	
	private AuthUser user(String username, String password, String email, Short blocked){
		AuthUser user=new AuthUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setBlocked(blocked);
		return user;
	}
}
