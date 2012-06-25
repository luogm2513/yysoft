/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-8-29
 */
package net.caiban.service.orders.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.domain.orders.Orders;
import net.caiban.domain.orders.SaleDispute;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.SaleDisputeDto;
import net.caiban.facade.ParamFacade;
import net.caiban.service.BaseServiceTestCase;
import net.caiban.service.orders.SaleDisputeService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class SaleDisputeServiceImplTest extends BaseServiceTestCase{
	
	@Autowired
	private SaleDisputeService saleDisputeService;

	/**
	 * Test method for {@link net.caiban.service.orders.impl.SaleDisputeServiceImpl#listOneSaleDisputeById(java.lang.Integer, java.lang.Integer)}.
	 * @throws SQLException 
	 */
	@Test
	public void testListOneSaleDisputeById() throws SQLException {
		clean();
		Integer i = createSaleDispute(new SaleDispute(null,1,(short)0,(float)100.6,"test sale dispute",new Date(),(short)0,(short)0,0,1,1,null));
		
		SaleDispute saleDispute = saleDisputeService.listOneSaleDisputeById(i, 1);
		assertNotNull(saleDispute);
		assertEquals("test sale dispute", saleDispute.getRemark());
	}

	final static String SALEDISPUTE_RESOLVED_KEY = "sale_dispute_resolved";
	/**
	 * Test method for {@link net.caiban.service.orders.impl.SaleDisputeServiceImpl#pageSaleDispute(net.caiban.dto.orders.SaleDisputeDto, net.caiban.dto.Paging)}.
	 * @throws SQLException 
	 */
	@Test
	public void testPageSaleDispute() throws SQLException {
		clean();
		
		initParam();
		
		Integer buyerId=createbuyer(new EshopBuyer(null,"test buyer","test phone","test@email.com","",(short)1,1,1,null,null));
		Integer oid = createOrders(new Orders(null,"orders-"+new Date(),"12345660",buyerId,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		
		Integer[] ids = new Integer[18];
		for(int i=0;i<18;i++){
			ids[i] = createSaleDispute(new SaleDispute(null,oid,(short)0,(float)100.6,"test sale dispute"+i,new Date(),(short)0,(short)0,0,1,1,null));;
		}
		
		Paging page = new Paging();
		page.setStart(10);
		page.setLimit(10);
		page.setSort("id");
		Orders o = new Orders();
		o.setEid(1);
		
		SaleDisputeDto saleDisputeDto = new SaleDisputeDto();
		saleDisputeDto.setSaleDispute(new SaleDispute());
		
		page = saleDisputeService.pageSaleDispute(saleDisputeDto, page);
		
		assertNotNull(page);
		assertEquals(page.getTotals(), new Integer(18) );
		assertEquals(page.getRecords().size(), 8);
	}
	
	@Test
	public void testPageSaleDispute_resolvedArray() throws SQLException {
		clean();
		initParam();
		
		Integer buyerId=createbuyer(new EshopBuyer(null,"test buyer","test phone","test@email.com","",(short)1,1,1,null,null));
		Integer oid = createOrders(new Orders(null,"orders-"+new Date(),"12345660",buyerId,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		
		Integer[] ids = new Integer[18];
		
		Map<String, String> resolvedMap = ParamFacade.getInstance().getParamByType(SALEDISPUTE_RESOLVED_KEY);
		
		for(String m:resolvedMap.keySet()){
			for(int i=0;i<5;i++){
				ids[i] = createSaleDispute(
					new SaleDispute(null,oid,(short)0,(float)100.6,
						"test sale dispute"+i,new Date(),
						(short)0, Short.valueOf(m),0,1,1,null)
				);
			}
		}
		
		Paging page = new Paging();
		page.setStart(10);
		page.setLimit(10);
		page.setSort("id");
		Orders o = new Orders();
		o.setEid(1);
		
		SaleDisputeDto saleDisputeDto = new SaleDisputeDto();
		saleDisputeDto.setSaleDispute(new SaleDispute());
		
		Integer[] searchArray = {0,10,20};
		saleDisputeDto.setResolvedArray(searchArray);
		
		page = saleDisputeService.pageSaleDispute(saleDisputeDto, page);
		
		assertNotNull(page);
		assertEquals(new Integer(searchArray.length*5), page.getTotals());
		assertEquals(5, page.getRecords().size());
		
	}

	/**
	 * Test method for {@link net.caiban.service.orders.impl.SaleDisputeServiceImpl#updateResolvedStatus(java.lang.Short, java.lang.Integer, java.lang.Integer)}.
	 * @throws SQLException 
	 */
	@Test
	public void testUpdateResolvedStatus() throws SQLException {
		clean();
		Integer i = createSaleDispute(new SaleDispute(null,1,(short)0,(float)100.6,"test sale dispute",new Date(),(short)0,(short)0,0,1,1,null));

		Integer j= saleDisputeService.updateResolvedStatus((short)2	, 1, 1, i);
		assertTrue(j>0);
		
		SaleDispute saleDispute = saleDisputeService.listOneSaleDisputeById(i, 1);
		assertEquals(saleDispute.getResolved().shortValue(),(short)2);
	}

	/**
	 * Test method for {@link net.caiban.service.orders.impl.SaleDisputeServiceImpl#updateSaleDisputeSimple(net.caiban.domain.orders.SaleDispute)}.
	 * @throws SQLException 
	 */
	@Test
	public void testUpdateSaleDisputeSimple() throws SQLException {
		clean();
		SaleDispute sale = new SaleDispute(null,1,(short)0,(float)100.6,"test sale dispute",new Date(),(short)0,(short)0,0,1,1,null);
		Integer i = createSaleDispute(sale);
		
		sale.setRemark("new sale dispute");
		sale.setReparation((float)200.8);
		sale.setId(i);
		
		Integer j =saleDisputeService.updateSaleDisputeSimple(sale);
		assertNotNull(j);
		assertTrue(j>0);
		
		SaleDispute ns = saleDisputeService.listOneSaleDisputeById(i, 1);
		
		assertNotNull(ns);
		assertEquals("new sale dispute", ns.getRemark());
		assertEquals((float)200.8, ns.getReparation().floatValue());
	}
	
	@Test
	public void testBatchUpdateResolved() throws SQLException{
		clean();
		
		Integer buyerId=createbuyer(new EshopBuyer(null,"test buyer","test phone","test@email.com","",(short)1,1,1,null,null));
		Integer oid = createOrders(new Orders(null,"orders-"+new Date(),"12345660",buyerId,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		
		Integer[] ids = new Integer[10];
		for(int i=0;i<10;i++){
			ids[i] = createSaleDispute(new SaleDispute(null,oid,(short)0,(float)100.6,"test sale dispute"+i,new Date(),(short)0,(short)0,0,1,1,null));;
		}
		
		Integer i = saleDisputeService.batchUpdateResolved((short)10, ids, 1);
		assertNotNull(i);
		assertEquals(10, i.intValue());
		
		SaleDispute s = saleDisputeService.listOneSaleDisputeById(ids[0], 1);
		assertEquals((short)10, s.getResolved().shortValue());
	}
	
	@Test
	public void testBatchDeleteSaleDispute() throws SQLException{
		clean();
		
		Integer buyerId=createbuyer(new EshopBuyer(null,"test buyer","test phone","test@email.com","",(short)1,1,1,null,null));
		Integer oid = createOrders(new Orders(null,"orders-"+new Date(),"12345660",buyerId,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		
		Integer[] ids = new Integer[10];
		for(int i=0;i<10;i++){
			ids[i] = createSaleDispute(new SaleDispute(null,oid,(short)0,(float)100.6,"test sale dispute"+i,new Date(),(short)0,(short)0,0,1,1,null));;
		}
		
		Integer i = saleDisputeService.batchDeleteSaleDispute(ids, 1);
		
		assertEquals(10, i.intValue());
		
	}
	
	/*******************************************************/
	private void clean() throws SQLException{
		connection.prepareStatement("delete from sale_dispute").execute();
	}
	
//	Integer i = createSaleDispute(new SaleDispute(null,1,(short)0,(float)100.6,"test sale dispute",new Date(),(short)0,(short)0,0,1,1,null));
	private Integer createSaleDispute(SaleDispute saleDispute) throws SQLException{
		String sql="";
		sql="insert into sale_dispute(order_id,return_goods,reparation,remark,period,severity,resolved,uid,eid,gmtcreate) ";
		sql=sql+" values("+
			saleDispute.getOrderId()+","+
			saleDispute.getReturnGoods()+","+
			saleDispute.getReparation()+",'"+
			saleDispute.getRemark()+"',now(),"+
			saleDispute.getSeverity()+","+
			saleDispute.getResolved()+",1,1,now())";
		System.out.println(sql);

		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		
		return 0;
	}
	
	
	//Integer buyerId=createbuyer(new EshopBuyer(null,"test buyer","test phone","test@email.com","",(short)1,1,1,null,null));
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
	
	// Integer oid = createOrders(new Orders(null,"orders-"+new Date(),"12345660",buyerId,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
	private Integer createOrders(Orders orders) throws SQLException{
		String sql="";
		sql="insert into orders(order_no,order_no_taobao,buyer_id,payment,total_order_cash,total_product_cash,paid,gmt_order,order_status,uid,eid,gmtcreate) ";
		sql=sql+" values('"+orders.getOrderNo()+"','"+orders.getOrderNoTaobao()+"',"+orders.getBuyerId()+",'"+orders.getPayment()
			+"',"+orders.getTotalOrderCash()+","+orders.getTotalProductCash()+","+orders.getPaid()+",now(),0,1,1,now())";
		
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		
		return 0;
	}
}
