package net.caiban.service.eshop.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.domain.product.Product;
import net.caiban.domain.product.ProductCategory;
import net.caiban.dto.Paging;
import net.caiban.dto.product.ProductDto;
import net.caiban.service.BaseServiceTestCase;
import net.caiban.service.eshop.EshopBuyerService;
import net.caiban.util.StringUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EshopBuyerServiceImplTest extends BaseServiceTestCase{
	
	@Autowired
	EshopBuyerService eshopBuyerService;

	private int TEST_DATA = 10; 
	@Test
	public void testBatchDeleteEshopBuyerBuyIdArray() throws SQLException {
		clean();
		List<Integer> idArray = prepareEshopBuyer();
		
		Integer impact = eshopBuyerService.batchDeleteEshopBuyerBuyIdArray(
				StringUtil.str2intArray(idArray.toString().replace("[","").replace("]","").replace(" ", ""))
		);
		
		assertEquals(idArray.size(), impact.intValue());
	}

	@Test
	public void testCreateEshopBuyer() throws SQLException {
		clean();
		EshopBuyer buyer = new EshopBuyer(null,"testbuyer","phone","email","im",(short)3,1,1,null,"test remark");
		
		Integer id = eshopBuyerService.createEshopBuyer(buyer);
		
		assertTrue(id>0);
		
		buyer = new EshopBuyer(null,null,null,null,null,null,1,1,null,null);
		id = eshopBuyerService.createEshopBuyer(buyer);
		assertTrue(id>0);
	}

	@Test
	public void testListOneEshopBuyerById() throws SQLException {
		clean();
		Integer id = createbuyer(new EshopBuyer(null,"testbuyer","phone","email","im",(short)3,1,1,null,"test remark"));
		
		EshopBuyer buyer = eshopBuyerService.listOneEshopBuyerById(id, 1);
		assertNotNull(buyer);
		assertEquals("testbuyer", buyer.getName());
	}

	@Test
	public void testPageEshopBuyer() throws SQLException {
		clean();
		TEST_DATA = 12;
		prepareEshopBuyer();
		TEST_DATA=10;
		
		Paging page=new Paging();
		page.setLimit(10);

		EshopBuyer b = new EshopBuyer();
		b.setEid(1);
		
		page.setStart(0);
		page=eshopBuyerService.pageEshopBuyer(b, page);
		assertEquals(page.getRecords().size(),10);
		assertEquals(page.getTotals().intValue(),12);
		
		page.setStart(10);
		page=eshopBuyerService.pageEshopBuyer(b, page);
		assertEquals(page.getRecords().size(),2);
		assertEquals(page.getTotals().intValue(),12);
	}

	@Test
	public void testUpdateEshopBuyer() throws SQLException {
		clean();
		Integer id = createbuyer(new EshopBuyer(null,"testbuyer","phone","email","im",(short)3,1,1,null,"test remark"));
		
		EshopBuyer buyer = eshopBuyerService.listOneEshopBuyerById(id, 1);
		buyer.setName("testbuyer_update");
		
		eshopBuyerService.updateEshopBuyer(buyer);
		
		EshopBuyer buyer2 = eshopBuyerService.listOneEshopBuyerById(id, 1);
		assertEquals("testbuyer_update", buyer2.getName());
	}
	
	@Test
	public void testListEshopBuyerByName() throws SQLException{
		clean();
		EshopBuyer buyer = new EshopBuyer(null,"客户甲","phone","email","im",(short)3,1,1,null,"test remark");
		eshopBuyerService.createEshopBuyer(buyer);
		buyer = new EshopBuyer(null,"客户乙","phone","email","im",(short)3,1,1,null,"test remark");
		eshopBuyerService.createEshopBuyer(buyer);
		buyer = new EshopBuyer(null,"客户丙","phone","email","im",(short)3,1,1,null,"test remark");
		eshopBuyerService.createEshopBuyer(buyer);
		buyer = new EshopBuyer(null,"客户丁","phone","email","im",(short)3,1,1,null,"test remark");
		eshopBuyerService.createEshopBuyer(buyer);
		
		buyer = new EshopBuyer(null,"buyerone","phone","email","im",(short)3,1,1,null,"test remark");
		eshopBuyerService.createEshopBuyer(buyer);
		buyer = new EshopBuyer(null,"buyertwo","phone","email","im",(short)3,1,1,null,"test remark");
		eshopBuyerService.createEshopBuyer(buyer);
		buyer = new EshopBuyer(null,"buyerthree","phone","email","im",(short)3,1,1,null,"test remark");
		eshopBuyerService.createEshopBuyer(buyer);
		
		List<EshopBuyer> list1 = eshopBuyerService.listEshopBuyerByName("客户", null, 1, 1);
		assertEquals(list1.size(), 4);
		
		List<EshopBuyer> list2 = eshopBuyerService.listEshopBuyerByName("buyert", null, 1, 1);
		assertEquals(list2.size(), 2);
	}
	
	private void clean() throws SQLException{
		connection.prepareStatement("delete from eshop").execute();
		connection.prepareStatement("delete from eshop_buyer").execute();
		connection.prepareStatement("delete from auth_user").execute();
	}
	
	private List<Integer> prepareEshopBuyer() throws SQLException{
		List<Integer> impact=new ArrayList<Integer>();
		for(int i=0;i<TEST_DATA;i++){
			impact.add(createbuyer(
				new EshopBuyer(null,"buyer"+i,"phone"+i,"email"+i,"im"+i,(short)1,1,1,null,"remark"+i)
			));
		}
		return impact;
	}
	
	private Integer createbuyer(EshopBuyer buyer) throws SQLException{
		String sql="";
		sql="insert into eshop_buyer(name,phone,im,email,valuable,remark,eid,uid,gmtcreate) ";
		sql=sql+" values('"+buyer.getName()+"','"+buyer.getPhone()+"','"+buyer.getIm()+"','"+
			buyer.getEmail()+"',"+buyer.getValuable()+",'"+buyer.getRemark()+"',"+buyer.getEid()+","+buyer.getUid()+",now())";
		
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		
		return 0;
	}

}
