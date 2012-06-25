package net.caiban.service.orders.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.caiban.domain.eshop.EshopBuyer;
import net.caiban.domain.orders.Orders;
import net.caiban.domain.orders.SaleDispute;
import net.caiban.dto.Paging;
import net.caiban.dto.orders.OrdersDto;
import net.caiban.facade.ParamFacade;
import net.caiban.service.BaseServiceTestCase;
import net.caiban.service.auth.ParamService;
import net.caiban.service.orders.OrdersService;
import net.caiban.util.StringUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrdersServiceImplTest extends BaseServiceTestCase{

	@Autowired
	private OrdersService ordersService;
	@Autowired
	private ParamService paramService;
	
	@Test
	public void testBatchDeleteOrders() throws SQLException {
		clean();
		List<Integer> orderList = prepareOrders();
		Integer impact = ordersService.batchDeleteOrders(StringUtil.str2intArray(orderList.toString().replace("[","").replace("]","").replace(" ", "")));
		assertEquals(impact.intValue(), orderList.size());
	}
	
	@Test
	public void testCreateSimpleOrders() throws SQLException {
		clean();
		Integer buyerId=createbuyer(new EshopBuyer(null,"test buyer","test phone","test@email.com","",(short)1,1,1,null,null));
		Orders orders = new Orders(null,null,null,buyerId,"taobao",null,null,null,new Date(),null,1,1,null);
		Integer i = ordersService.createSimpleOrders(orders);
		assertTrue(i.intValue()>0);
		
		orders = new Orders(null,"orders-"+new Date(),"12345660",buyerId,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null);
		Integer j = ordersService.createSimpleOrders(orders);
		assertTrue(j.intValue()>0);
	}

	@Test
	public void testListOneOrdersById() throws SQLException {
		clean();
		Integer oid = createOrders(new Orders(null,"orderstest","12345660",0,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		
		OrdersDto orders = ordersService.listOneOrdersById(oid);
		assertNotNull(orders);
		assertNotNull(orders.getOrders());
		assertEquals(orders.getOrders().getOrderNo(), "orderstest");
		
	}

	@Test
	public void testListOneOrdersByOrderNo() throws SQLException {
		clean();
		createOrders(new Orders(null,"orderstest","12345660",0,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		
		OrdersDto orders = ordersService.listOneOrdersByOrderNo("orderstest");
		assertNotNull(orders);
		assertNotNull(orders.getOrders());
		assertEquals(orders.getOrders().getOrderNo(), "orderstest");
	}

	@Test
	public void testPageOrdersDto() throws SQLException {
		clean();
		Integer[] ids = new Integer[18];
		for(int i=0;i<18;i++){
			ids[i] = createOrders(new Orders(null,"orderno-"+i,"taobaono-"+i,0,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		}
		
		ParamFacade.getInstance().initCache(paramService.listAllParam((short)0));
		
		//page=2 pagesize=10
		Paging page = new Paging();
		page.setStart(10);
		page.setLimit(10);
		page.setSort("id");
		Orders o = new Orders();
		o.setEid(1);
		OrdersDto ordersDto = new OrdersDto();
		ordersDto.setOrders(o);
		page = ordersService.pageOrdersDto(ordersDto, page);
		
		assertNotNull(page);
		assertEquals(page.getTotals(), new Integer(18) );
		assertEquals(page.getRecords().size(), 8);
	}

	@Test
	public void testUpdateOrderStatusById() throws SQLException {
		clean();
		Integer oid = createOrders(new Orders(null,"orderstest","12345660",0,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		
		ordersService.updateOrderStatusById((short)20, oid, 1);
		
		OrdersDto o = ordersService.listOneOrdersById(oid);
		assertEquals(o.getOrders().getOrderStatus().shortValue(), (short)20);
	}

	@Test
	public void testUpdateSimpleOrders() throws SQLException {
		clean();
		Integer oid = createOrders(new Orders(null,"orderstest","12345660",0,"taobao",new Float(100.5),new Float(180.7),new Float(100),new Date(),null,1,1,null));
		
		OrdersDto o = ordersService.listOneOrdersById(oid);
		
		Orders orders = o.getOrders();
		orders.setOrderNo("newordersno-noupdate");
		orders.setOrderNoTaobao("newtaobaono");
		orders.setBuyerId(1);
		orders.setPayment("onlineblank");
		orders.setTotalOrderCash(new Float(200.8));
		orders.setTotalProductCash(new Float(200.5));
		orders.setPaid(new Float(120.3));
		orders.setOrderStatus((short)10);
		ordersService.updateSimpleOrders(orders);
		
		OrdersDto onew = ordersService.listOneOrdersById(oid);
		assertEquals(onew.getOrders().getOrderNo(), "orderstest");
		assertEquals(onew.getOrders().getOrderNoTaobao(), "newtaobaono");
		assertEquals(onew.getOrders().getBuyerId().intValue(),1);
		assertEquals(onew.getOrders().getPayment(), "onlineblank");
		assertEquals(onew.getOrders().getTotalOrderCash().floatValue(), new Float(200.8).floatValue());
		assertEquals(onew.getOrders().getTotalProductCash().floatValue(), new Float(200.5).floatValue());
		assertEquals(onew.getOrders().getPaid().floatValue(), new Float(120.3).floatValue());
		assertEquals(onew.getOrders().getOrderStatus().shortValue(), (short)0);
	}
	
	@Test
	public void test_failureOrders() throws SQLException{
		clean();
		Integer buyerId=createbuyer(new EshopBuyer(null,"test buyer","test phone","test@email.com","",(short)1,1,1,null,null));
		Integer oid = createOrders(new Orders(null,"orders-"+new Date(),"12345660",buyerId,"taobao",new Float(100.5),null,new Float(100),new Date(),null,1,1,null));
		
		SaleDispute saleDispute = new SaleDispute();
		saleDispute.setOrderId(oid);
		saleDispute.setEid(1);
		Integer i = ordersService.failureOrders(saleDispute);
		
		assertEquals(i>0, true);
		
		OrdersDto o = ordersService.listOneOrdersById(oid);
		assertEquals(o.getOrders().getOrderStatus().shortValue(), (short)40);
		
	}
	
	/***************data************/
	private void clean() throws SQLException{
		connection.prepareStatement("delete from orders").execute();
		connection.prepareStatement("delete from eshop_buyer").execute();
		connection.prepareStatement("delete from sale_dispute").execute();
	}
	final static int TEST_DATA = 5 ;
	private List<Integer> prepareOrders() throws SQLException{
		Integer buyerId=createbuyer(new EshopBuyer(null,"test buyer","test phone","test@email.com","",(short)1,1,1,null,null));
		
		List<Integer> impact=new ArrayList<Integer>();
		for(int i=0;i<TEST_DATA;i++){
			impact.add(createOrders(new Orders(
					null,"order-"+i,null,buyerId,"taobao",new Float(100.5),new Float(120.6),new Float(100),new Date(),(short)10,1,1,null
			)));
		}
		return impact;
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
	
	
}
