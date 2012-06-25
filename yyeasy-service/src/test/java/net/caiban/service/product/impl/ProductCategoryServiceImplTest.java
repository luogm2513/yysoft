package net.caiban.service.product.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import net.caiban.domain.product.ProductCategory;
import net.caiban.service.BaseServiceTestCase;
import net.caiban.service.product.ProductCategoryService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductCategoryServiceImplTest extends BaseServiceTestCase{
	
	Integer DEFAULT_EID = 1;
	Integer DEFAULT_UID = 1;
	Short DEFAULT_DEL = 0;
	
	@Autowired
	ProductCategoryService productCategoryService;

	@Test
	public void testChangeListToTreeNode() {
		try {
			productCategoryService.changeListToTreeNode(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testCreateProductCategory_null_argument() {
		try {
			productCategoryService.createProductCategory(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			productCategoryService.createProductCategory(category(null,null,"test",0,0,DEFAULT_DEL,null,null,null));
		} catch (IllegalArgumentException e) {
		}
		
		try {
			productCategoryService.createProductCategory(category(null,null,"test",0,0,DEFAULT_DEL,DEFAULT_UID,null,null));
		} catch (IllegalArgumentException e) {
		};
	}
	
	@Test
	public void testCreateProductCategory() throws SQLException {
		clean();

		Integer newi = productCategoryService.createProductCategory(category(null,0,"肉",1,2,DEFAULT_DEL,DEFAULT_UID,DEFAULT_EID,null));
		assertTrue(newi>0);

		clean();
		prepareCategory();
		ProductCategory c=queryCategoryViaName("羊肉",DEFAULT_EID);
		
		ProductCategory category=category(null,c.getId(),"羊头肉",c.getR(),(c.getR()+1),DEFAULT_DEL,DEFAULT_UID,DEFAULT_EID,null);
		
		Integer i = productCategoryService.createProductCategory(category);
		assertTrue(i>0);

		ProductCategory c2 = queryCategoryViaName("羊肉",DEFAULT_EID);
		assertTrue(c2.getR()==(c.getR()+2));
	}

	@Test
	public void testDeleteProductCategoryViaLR() throws SQLException {
		clean();
		prepareCategory();

		ProductCategory category = queryCategoryViaName("猪肉",DEFAULT_EID);

		Integer i=productCategoryService.deleteProductCategory(category.getId());
		assertTrue(i>0);

		ProductCategory category2=queryCategoryViaName("肉",DEFAULT_EID);
		assertTrue(category2.getR()==6);
	}
	
	@Test
	public void testDeleteProductCategoryViaLR_null_argument() throws SQLException {
		try {
			productCategoryService.deleteProductCategory(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testListOneProductCategoryViaId() throws SQLException {
		clean();
		prepareCategory();
		
		Integer id = queryIdViaName("猪肉",DEFAULT_EID);
		
		ProductCategory category = productCategoryService.listOneProductCategoryViaId(id);
		assertEquals("猪肉",category.getName());
	}
	
	@Test
	public void testListOneProductCategoryViaId_null_argument() {
		try {
			productCategoryService.listOneProductCategoryViaId(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testListProductCategoryViaLR() throws SQLException {
		clean();
		Integer myeid=2;
		prepareCategory(myeid);
		
		List<ProductCategory> list0_0 = productCategoryService.listProductCategoryViaLR(0,0,myeid);
		assertTrue(list0_0.size()==0);

		List<ProductCategory> list0_9=productCategoryService.listProductCategoryViaLR(2, 7,myeid);
		assertTrue(list0_9.size()==2);

		List<ProductCategory> list0_13=productCategoryService.listProductCategoryViaLR(0, 13,myeid);
		assertTrue(list0_13.size()==6);
		
		List<ProductCategory> list0_13_default_eid=productCategoryService.listProductCategoryViaLR(0, 13,DEFAULT_EID);
		assertTrue(list0_13_default_eid.size()==0);
	}
	
	@Test
	public void testListProductCategoryViaLR_null_argument(){
		try {
			productCategoryService.listProductCategoryViaLR(null,null,null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			productCategoryService.listProductCategoryViaLR(0,null,null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			productCategoryService.listProductCategoryViaLR(0,1,null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testListProductCategoryViaParent() throws SQLException {
		clean();
		prepareCategory();
		int id=queryIdViaName("肉",DEFAULT_EID);
		List<ProductCategory> list=productCategoryService.listProductCategoryViaParent(id,DEFAULT_EID);
		assertTrue(list.size()==3);

		List<ProductCategory> nolist=productCategoryService.listProductCategoryViaParent(id,2);
		assertTrue(nolist.size()==0);
	}
	
	@Test
	public void testListProductCategoryViaParent_null_argument() {
		try {
			productCategoryService.listProductCategoryViaParent(null,null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			productCategoryService.listProductCategoryViaParent(1,null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testUpdateProductCategoryViaId() throws SQLException {
		clean();
		prepareCategory();

		int id=queryIdViaName("羊肉",DEFAULT_EID);
		ProductCategory category= new ProductCategory();
		category.setId(id);
		category.setName("鸡肉");

		productCategoryService.updateProductCategoryViaId(category);

		int id2=queryIdViaName("鸡肉",DEFAULT_EID);

		assertEquals(id, id2);
	}
	
	@Test
	public void testUpdateProductCategoryViaId_null_argument(){
		try {
			productCategoryService.updateProductCategoryViaId(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			productCategoryService.updateProductCategoryViaId(new ProductCategory());
			fail();
		} catch (IllegalArgumentException e) {
		}
	}


	/*********************************/
	
	private void clean() throws SQLException{
		connection.prepareStatement("delete from product_category").execute();
	}

	int create(ProductCategory category) throws SQLException{
		String sql="";
		sql="insert into product_category(parent_id,name,l,r,del,gmtcreate,uid,eid) ";
		sql=sql+" values("+category.getParentId()
			+",'"+category.getName()
			+"',"+category.getL()
			+","+category.getR()
			+","+category.getDel()
			+",now(),"+category.getUid()
			+","+category.getEid()+")";
		
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		return 0;
	}

	Integer queryIdViaName(String name,Integer eid) throws SQLException{
		ResultSet rs=connection.createStatement().executeQuery("select id from product_category where name='"+name+"' and eid="+eid);
		if(rs.next()){
			return rs.getInt("id");
		}
		return 0;
	}

	ProductCategory queryCategoryViaName(String name,Integer eid) throws SQLException{
		ResultSet rs=connection.createStatement().executeQuery("select * from product_category where name='"+name+"' and eid="+eid);
		if(rs.next()){
			return category(
					rs.getInt("id"),
					rs.getInt("parent_id"),
					rs.getString("name"),
					rs.getInt("l"),
					rs.getInt("r"),
					rs.getShort("del"),
					rs.getInt("uid"),
					rs.getInt("eid"),
					rs.getDate("gmtcreate")
					);
		}
		return null;
	}

	void prepareCategory() throws SQLException{
		int deep1=create(category(null, 0, "肉", 1, 12, DEFAULT_DEL, DEFAULT_UID, DEFAULT_EID, null));
		int deep2=create(category(null, deep1, "猪肉", 2, 7, DEFAULT_DEL, DEFAULT_UID, DEFAULT_EID, null));
		create(category(null, deep2, "猪头肉", 3, 4, DEFAULT_DEL, DEFAULT_UID, DEFAULT_EID, null));
		create(category(null, deep2, "猪后腿肉", 5, 6, DEFAULT_DEL, DEFAULT_UID, DEFAULT_EID, null));
		create(category(null, deep1, "羊肉", 8, 9, DEFAULT_DEL, DEFAULT_UID, DEFAULT_EID, null));
		create(category(null, deep1, "牛肉", 10, 11, DEFAULT_DEL, DEFAULT_UID, DEFAULT_EID, null));
	}
	
	void prepareCategory(Integer eid) throws SQLException{
		int deep1=create(category(null, 0, "肉", 1, 12, DEFAULT_DEL, DEFAULT_UID, eid, null));
		int deep2=create(category(null, deep1, "猪肉", 2, 7, DEFAULT_DEL, DEFAULT_UID, eid, null));
		create(category(null, deep2, "猪头肉", 3, 4, DEFAULT_DEL, DEFAULT_UID, eid, null));
		create(category(null, deep2, "猪后腿肉", 5, 6, DEFAULT_DEL, DEFAULT_UID, eid, null));
		create(category(null, deep1, "羊肉", 8, 9, DEFAULT_DEL, DEFAULT_UID, eid, null));
		create(category(null, deep1, "牛肉", 10, 11, DEFAULT_DEL, DEFAULT_UID, eid, null));
	}

	private ProductCategory category(Integer id, Integer parent, String name,
			Integer l, Integer r, Short del, Integer uid, Integer eid, Date gmtcreate ){
		ProductCategory category=new ProductCategory();
		category.setId(id);
		category.setParentId(parent);
		category.setName(name);
		category.setL(l);
		category.setR(r);
		category.setDel(del);
		category.setUid(uid);
		category.setEid(eid);
		category.setGmtcreate(gmtcreate);
		return category;
	}

}
