/**
 * Copyright  2010 YYSoft
 * All right reserved.
 * Created on 2010-7-3
 */
package net.caiban.service.orders.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.caiban.domain.orders.OrdersProduct;
import net.caiban.dto.orders.OrdersProductDto;
import net.caiban.service.BaseServiceTestCase;
import net.caiban.service.orders.OrdersProductService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class OrdersProductServiceImplTest extends BaseServiceTestCase{
	
	@Autowired
	private OrdersProductService ordersProductService;
	
	/**
	 * Test method for {@link net.caiban.service.orders.impl.OrdersProductServiceImpl#listOrdersProductByOrdersId(java.lang.Integer)}.
	 * @throws SQLException 
	 */
	@Test
	public void testListOrdersProductByOrdersId() throws SQLException {
		clean();
		
		prepareOrdersProduct(1);
		
		List<OrdersProductDto> l= ordersProductService.listOrdersProductByOrdersId(1);
		assertEquals(l.size(), 5);
		
	}
	
	/**
	 * Test method for {@link net.caiban.service.orders.impl.OrdersProductServiceImpl#clearOrdersProductByOrdersId(java.lang.Integer)}.
	 * @throws SQLException 
	 */
	@Test
	public void testClearOrdersProductByOrdersId() throws SQLException {
		clean();
		prepareOrdersProduct(1);
		ordersProductService.clearOrdersProductByOrdersId(1);
		
		List<OrdersProductDto> l = ordersProductService.listOrdersProductByOrdersId(1);
		assertEquals(l.size(), 0);
		
	}

	/**
	 * Test method for {@link net.caiban.service.orders.impl.OrdersProductServiceImpl#createOrdersProduct(net.caiban.domain.orders.OrdersProduct)}.
	 * @throws SQLException 
	 */
	@Test
	public void testCreateOrdersProduct() throws SQLException {
		clean();
		ordersProductService.createOrdersProduct(new OrdersProduct(null,1,1,"100",2));
		
		List<OrdersProductDto> l = ordersProductService.listOrdersProductByOrdersId(1);
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getOrdersProduct().getPriceDiscount(), "100");
	}

	/***************data************/
	private void clean() throws SQLException{
		connection.prepareStatement("delete from orders_product").execute();
	}
	
	final static int TEST_DATA = 5 ;
	private List<Integer> prepareOrdersProduct(Integer ordersid) throws SQLException{
		if(ordersid == null || ordersid == 0){
			ordersid = 1;
		}
		List<Integer> impact=new ArrayList<Integer>();
		for(int i=0;i<TEST_DATA;i++){
			impact.add(createordersproduct(new OrdersProduct(null,1,ordersid,"100",1)));
		}
		return impact;
	}
	
	private Integer createordersproduct(OrdersProduct ordersProduct) throws SQLException{
		String sql="";
		sql="insert into orders_product(product_id,orders_id,price_discount,quantity)";
		sql=sql+" values("+ordersProduct.getProductId()+","+ordersProduct.getOrdersId()+",'"+ordersProduct.getPriceDiscount()+"',"+ordersProduct.getQuantity()+")";
		
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		
		return 0;
	}

}
