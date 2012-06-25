package net.caiban.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.caiban.auth.domain.AuthRight;

import org.springframework.beans.factory.annotation.Autowired;

public class RightServiceImplTest extends BaseAuthTestCase{

	@Autowired
	RightService rightService;

	public void test_listRightViaParent_no_attribute() throws SQLException{
		clean();
		prepareData();
		List<AuthRight> list=rightService.listRightViaParent(null);

		AuthRight right=list.get(0);
		assertEquals("肉", right.getName());
	}

	public void test_listRightViaParent_has_attribute() throws SQLException{
		clean();
		prepareData();
		int id=queryIdViaName("肉");
		List<AuthRight> list=rightService.listRightViaParent(id);
		assertTrue(list.size()==3);

		List<AuthRight> nolist=rightService.listRightViaParent(-1);
		assertTrue(nolist.size()==0);
	}

	public void test_listRightViaLR_no_attribute() throws SQLException{
		clean();
		prepareData();
		try {
			rightService.listRightViaLR(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}

	}

	public void test_listRightViaLR_has_attribute() throws SQLException{
		clean();
		prepareData();
		List<AuthRight> list0_0=rightService.listRightViaLR(0, 0);
		assertTrue(list0_0.size()==0);

		List<AuthRight> list0_9=rightService.listRightViaLR(2, 7);
		assertTrue(list0_9.size()==2);

		List<AuthRight> list0_13=rightService.listRightViaLR(0, 13);
		assertTrue(list0_13.size()==6);
	}

	public void test_listOneRightViaId_no_attribute(){
		try {
			rightService.listOneRightViaId(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_listOneRightViaId_has_attribute() throws SQLException{
		clean();
		prepareData();

		AuthRight right0=rightService.listOneRightViaId(0);
		assertNull(right0);

		AuthRight rightX=rightService.listOneRightViaId(queryIdViaName("猪肉"));
		assertEquals("猪肉", rightX.getName());
	}

	public void test_updateRightBaseInfo_no_attribute() throws SQLException{
		try {
			rightService.updateRightBaseInfo(null);
			fail();
		} catch (IllegalArgumentException e) {
		}

		try {
			rightService.updateRightBaseInfo(new AuthRight());
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_updateRightBaseInfo_has_attribute() throws SQLException{
		clean();
		prepareData();

		int id=queryIdViaName("羊肉");
		AuthRight right= new AuthRight();
		right.setId(id);
		right.setName("鸡肉");
		right.setContent("");

		rightService.updateRightBaseInfo(right);

		int id2=queryIdViaName("鸡肉");

		assertEquals(id, id2);
	}

	public void test_createRight_no_attribute(){
		try {
			rightService.createRight(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_createRight_has_attribute() throws SQLException{
		clean();

		Integer newi=rightService.createRight(right(null, null, "肉", "", 1, 2, "", "", ""));
		assertTrue(newi>0);

		clean();
		prepareData();
		AuthRight r=queryRightViaName("羊肉");

		AuthRight right=right(null, r.getId(), "羊头肉", "^.*$",
				r.getR(), (r.getR()+1), "", "", "");

		Integer i=rightService.createRight(right);
		assertTrue(i>0);

		AuthRight r2=queryRightViaName("羊肉");
		assertTrue(r2.getR()==(r.getR()+2));

	}

	public void test_deleteRight_no_attribute(){
		try {
			rightService.deleteRight(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_deleteRight_has_attribute() throws SQLException{
		clean();
		prepareData();

		int id=queryIdViaName("猪肉");

		Integer i=rightService.deleteRight(id);
		assertTrue(i>0);

		AuthRight right=queryRightViaName("肉");
		assertTrue(right.getR()==6);

	}

	public void test_listRightInRole_no_attribute(){
		try {
			rightService.listRightInRole(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_listRightInRole_has_attribute() throws SQLException{
		clean();
		prepareData();
		int roleid=createRole();
		createRoleRight(roleid);

		List<AuthRight> list=rightService.listRightInRole(roleid, 0);
		assertTrue(list.size()==3);

		List<AuthRight> listbig=rightService.listRightInRole(roleid, queryIdViaName("猪肉"));
		assertTrue(listbig.size()==2);
	}

	public void test_listRightNotInRole_no_attribute(){
		try {
			rightService.listRightNotInRole(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	public void test_listRightNotInRole_has_attrbute() throws SQLException{
		clean();
		prepareData();
		int roleid=createRole();
		createRoleRight(roleid);

		List<AuthRight> list=rightService.listRightNotInRole(roleid, 0);
		assertTrue(list.size()==3);

		List<AuthRight> listX=rightService.listRightNotInRole(roleid, queryIdViaName("肉"));
		assertTrue(listX.size()==2);

	}


	/*************prepare***************/

	/**
	 * @throws SQLException
	 */
	private void clean() throws SQLException{
		connection.prepareStatement("delete from auth_role_right").execute();
		connection.prepareStatement("delete from auth_right").execute();
	}

	private int create(AuthRight right) throws SQLException{
		String sql="";
		sql="insert into auth_right(parent_id,name,content,l,r,menu,menu_url,menu_css) ";
		sql=sql+" values("+right.getParentId()
			+",'"+right.getName()
			+"','"+right.getContent()
			+"',"+right.getL()
			+","+right.getR()
			+",'"+right.getMenu()
			+"','"+right.getMenuUrl()
			+"','"+right.getMenuCss()
			+"')";
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		return 0;
	}

	private Integer queryIdViaName(String name) throws SQLException{
		ResultSet rs=connection.createStatement().executeQuery("select id from auth_right where name='"+name+"'");
		if(rs.next()){
			return rs.getInt("id");
		}
		return 0;
	}

	private AuthRight queryRightViaName(String name) throws SQLException{
		ResultSet rs=connection.createStatement().executeQuery("select * from auth_right where name='"+name+"'");
		if(rs.next()){
			return right(
					rs.getInt("id"),
					rs.getInt("parent_id"),
					rs.getString("name"),
					rs.getString("content"),
					rs.getInt("l"),
					rs.getInt("r"),
					rs.getString("menu"),
					rs.getString("menu_url"),
					rs.getString("menu_css"));
		}
		return null;
	}

	private void prepareData() throws SQLException{
		int deep1=create(right(null, 0, "肉", "", 1, 12, "", "", ""));
		int deep2=create(right(null, deep1, "猪肉", "", 2, 7, "", "", ""));
		create(right(null, deep2, "猪头肉", "", 3, 4, "", "", ""));
		create(right(null, deep2, "猪后腿肉", "", 5, 6, "", "", ""));
		create(right(null, deep1, "羊肉", "", 8, 9, "", "", ""));
		create(right(null, deep1, "牛肉", "", 10, 11, "", "", ""));
	}

	private AuthRight right(Integer id, Integer parent, String name, String content,
			Integer l, Integer r, String menu, String menuUrl, String menuCss ){
		AuthRight right=new AuthRight();
		right.setId(id);
		right.setParentId(parent);
		right.setName(name);
		right.setContent(content);
		right.setL(l);
		right.setR(r);
		right.setMenu(menu);
		right.setMenuUrl(menuUrl);
		right.setMenuCss(menuCss);
		return right;
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

	private void createRoleRight(int roleId) throws SQLException{
		createOneRoleRight(roleId, "猪肉");
		createOneRoleRight(roleId, "猪头肉");
		createOneRoleRight(roleId, "猪后腿肉");

	}

	private void createOneRoleRight(int roleId, String rightName) throws SQLException{
		String sql="";
		AuthRight right=queryRightViaName(rightName);
		sql="insert into auth_role_right(role_id,right_id)"+
			" values("+roleId+","+right.getId()+")";
		connection.prepareStatement(sql).execute();
	}


//	private AuthRole role(String name, String remark){
//		AuthRole role=new AuthRole();
//		role.setName(name);
//		role.setRemark(remark);
//		return role;
//	}
}
