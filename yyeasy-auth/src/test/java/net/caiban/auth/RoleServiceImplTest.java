package net.caiban.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.caiban.auth.domain.AuthRole;

import org.springframework.beans.factory.annotation.Autowired;


public class RoleServiceImplTest extends BaseAuthTestCase{

	@Autowired
	RoleService roleService;

	public void test_listRole() throws SQLException{
		clean();
		prepareData();

		List<AuthRole> list=roleService.listRole();
		assertTrue(list.size()==3);
	}

	public void test_createRole_no_attribute(){
		try {
			roleService.createRole(null);
			fail();
		} catch (IllegalArgumentException e) {
		}

		try {
			roleService.createRole(new AuthRole());
			fail();
		} catch (IllegalArgumentException e) {
		}

		try {
			roleService.createRole(role(null, "", ""));
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_createRole_has_attribute() throws SQLException{
		clean();
		prepareData();
		Integer id=roleService.createRole(role(null, "junitRunTime", ""));
		assertTrue(id>0);
	}

	public void test_updateRole_no_attribute(){
		try {
			roleService.updateRole(null);
			fail();
		} catch (IllegalArgumentException e) {
		}

		try {
			roleService.updateRole(new AuthRole());
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_updateRole_has_attribute() throws SQLException{
		clean();
		prepareData();

		AuthRole role=queryRoleViaName("dev");
		role.setName("devRuntime");
		roleService.updateRole(role);

		AuthRole newRole=queryRoleViaName("devRuntime");
		assertEquals(role.getId(), newRole.getId());
	}

	public void test_deleteRole_no_attribute(){
		try {
			roleService.deleteRole(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_deleteRole_has_attribute() throws SQLException{
		clean();
		prepareData();

		AuthRole role=queryRoleViaName("dev");

		Integer impact = roleService.deleteRole(role.getId());
		assertTrue(impact==1);

		AuthRole oldrole=queryRoleViaName("dev");
		assertNull(oldrole);
	}

	public void test_createRoleRight_no_attribute(){
		try {
			roleService.createRoleRight(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_createRoleRight_has_attribute() throws SQLException{
		clean();
		prepareData();

		Integer[] right={};
		AuthRole role=queryRoleViaName("dev");
		roleService.createRoleRight(role.getId(),right);
	}

	public void test_deleteRoleRight_no_attribute() throws SQLException{
		clean();
		try {
			roleService.deleteRoleRight(null, null);
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_deleteRoleRight_has_attribute() throws SQLException{
		clean();
		Integer roleid=createOneRole(role(null, "junitTest", "just for test"));
		createRoleRight(roleid,1);
		createRoleRight(roleid,2);
		createRoleRight(roleid,3);

		Integer[] rightIds={1,2};
		Integer impact=roleService.deleteRoleRight(roleid, rightIds);
		assertTrue(impact==2);
	}

	public void test_deleteRoleRight_has_attribute_no_rights() throws SQLException{
		clean();
		Integer roleid=createOneRole(role(null, "junitTest", "just for test"));
		createRoleRight(roleid,1);
		createRoleRight(roleid,2);
		createRoleRight(roleid,3);

		Integer impact=roleService.deleteRoleRight(roleid, null);
		assertTrue(impact==3);
	}

	/************prepare data*********/
	private void clean() throws SQLException{
		connection.prepareStatement("delete from auth_user_role").execute();
		connection.prepareStatement("delete from auth_role_right").execute();
		connection.prepareStatement("delete from auth_role").execute();
	}

	private void prepareData() throws SQLException{
		createOneRole(role(null, "dev", "for junit test!"));
		createOneRole(role(null, "junit", "for junit test!"));
		createOneRole(role(null, "all", "for junit test!"));
	}

	private Integer createOneRole(AuthRole role) throws SQLException{
		String sql="";
		sql="insert into auth_role(name,remark) " +
				"values('"+role.getName()+"','"+role.getRemark()+"')";
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		return 0;
	}

	private AuthRole role(Integer id, String name, String remark){
		AuthRole role=new AuthRole();
		role.setId(id);
		role.setName(name);
		role.setRemark(remark);
		return role;
	}

	private AuthRole queryRoleViaName(String name) throws SQLException{
		ResultSet rs=connection.createStatement().executeQuery("select * from auth_role where name='"+name+"'");
		if(rs.next()){
			return role(rs.getInt("id"), rs.getString("name"), rs.getString("remark"));
		}
		return null;
	}

	private void createRoleRight(Integer roleid,Integer right) throws SQLException{
		String sql="";
		sql="insert into auth_role_right(role_id,right_id) " +
			"values("+roleid+","+right+")";
		connection.prepareStatement(sql).execute();
	}

}
