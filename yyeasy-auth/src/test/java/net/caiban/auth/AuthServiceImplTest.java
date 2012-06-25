package net.caiban.auth;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.caiban.auth.domain.AuthUser;
import net.caiban.auth.exception.AuthException;
import net.caiban.auth.util.MD5;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceImplTest extends BaseAuthTestCase{
	
	@Autowired
	private AuthService authService;

	final static short DEFAULT_BLOCK = 0;
	
	@Test
	public void testChangePassword_validate_error() throws SQLException {
		clean();
		createUser(user("test", "test", "test@email.com", DEFAULT_BLOCK));
		try {
			authService.changePassword("", "", "newpwd", "newpwd-v-error");
			fail();
		} catch (AuthException e) {
		}
		
		try {
			authService.changePassword("test-error", "", "", "");
			fail();
		} catch (AuthException e) {
		}
		
		try {
			authService.changePassword("test", "password-error", "", "");
			fail();
		} catch (AuthException e) {
		}
		
		try {
			authService.changePassword("test", "test", "newpassword", "newpassword");
			AuthUser user = getUserByUsername("test");
//			System.out.println(MD5.encode("test"));
//			System.out.println(MD5.encode("newpassword"));
			assertEquals(user.getPassword(), MD5.encode("newpassword"));
		} catch (AuthException e) {
			fail();
		}
	}

//	@Test
//	public void testListRight() {
//		fail("Not yet implemented");
//	}

//	@Test
//	public void testListUserRight() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testValidateLogin_no_attribute() {
		try {
			authService.validateLogin(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (AuthException e) {
			
		}
		
		try {
			authService.validateLogin(new AuthUser());
			fail();
		} catch (IllegalArgumentException e) {
		} catch (AuthException e) {
		}
	}
	
	@Test
	public void testValidateLogin_has_attribute() throws SQLException {
		clean();
		createUser(user("test", "test", "test@email.com", DEFAULT_BLOCK));
		try {
			authService.validateLogin(user("testnousername", "", "", DEFAULT_BLOCK));
			fail();
		} catch (AuthException e) {
			
		}
		
		try {
			authService.validateLogin(user("test", "", "testnoemail", DEFAULT_BLOCK));
			fail();
		} catch (AuthException e) {
			
		}
		
		try {
			authService.validateLogin(user("test", "testnopassword", "test@email.com", DEFAULT_BLOCK));
			fail();
		} catch (AuthException e) {
		}
		
		try {
			AuthUser user =authService.validateLogin(user("test", "test", "test@email.com", DEFAULT_BLOCK));
			assertNotNull(user);
			assertTrue(user.getId().intValue()>0);
		} catch (AuthException e) {
		}
	}
	
	
	/************prepare data*********/
	private void clean() throws SQLException{
		connection.prepareStatement("delete from auth_user_role").execute();
		connection.prepareStatement("delete from auth_user").execute();
		connection.prepareStatement("delete from auth_role").execute();
		connection.prepareStatement("delete from auth_role_right").execute();
		connection.prepareStatement("delete from auth_right").execute();
	}
	
//	private Integer createRole() throws SQLException{
//		String sql="";
//		sql="insert into auth_role(name,remark) "+
//			"values('dev','for junit test!')";
//		connection.prepareStatement(sql).execute();
//
//		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
//		if(rs.next()){
//			return rs.getInt(1);
//		}
//		return 0;
//	}
	
	private AuthUser getUserByUsername(String username) throws SQLException{
		String sql="select * from auth_user where username='"+username+"'";
		ResultSet rs = connection.createStatement().executeQuery(sql);
		AuthUser u = new AuthUser();
		if(rs.next()){
			u.setId(rs.getInt("id"));
			u.setUsername(rs.getString("username"));
			u.setEmail(rs.getString("email"));
			u.setPassword(rs.getString("password"));
		}
		return u;
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
