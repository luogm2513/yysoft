package net.caiban.service.orders.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.caiban.domain.orders.Logistics;
import net.caiban.dto.Paging;
import net.caiban.service.BaseServiceTestCase;
import net.caiban.service.orders.LogisticsService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LogisticsServiceImplTest extends BaseServiceTestCase{
	
	@Autowired
	private LogisticsService logisticsService;
	
	@Test
	public void testBatchDeleteLogisticsByEid_no_attribute() throws SQLException {
		try {
			logisticsService.batchDeleteLogisticsByEid(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testBatchDeleteLogisticsByEid_has_attribute() throws SQLException {
		clean();
		Integer[] ids = new Integer[5];
		for(int i=0;i<5;i++){
			ids[i] = createLogistics(new Logistics(null,"test"+i,"test"+i,"test"+i,"test"+i,"test"+i,"test"+i,0,0,null));
		}
		
		try {
			logisticsService.batchDeleteLogisticsByEid(ids);
		} catch (Exception e) {
		}
	}

	@Test
	public void testCreateLogisticsByEid_no_eid() {
		try {
			logisticsService.createLogisticsByEid(new Logistics());
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		try {
			logisticsService.createLogisticsByEid(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testCreateLogisticsByEid_has_eid() throws SQLException {
		clean();
		Logistics logistics = new Logistics(null,"test","test","test","test","test","test",1,1,null);
		Integer i=logisticsService.createLogisticsByEid(logistics);
		assertTrue(i.intValue()>0);
		
		Logistics obj = logisticsService.listOneLogisticsById(i);
		assertNotNull(obj);
		assertEquals("test", obj.getName());
	}

	@Test
	public void testListOneLogisticsById() {
		try {
			logisticsService.listOneLogisticsById(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPageLogisticsWithEid_no_attribute() throws SQLException {
		try {
			logisticsService.pageLogisticsWithEid(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			logisticsService.pageLogisticsWithEid(new Logistics(), null);
			fail();
		} catch (IllegalArgumentException e) {
		}
		try {
			logisticsService.pageLogisticsWithEid(new Logistics(), new Paging());
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testPageLogisticsWithEid_has_attribute() throws SQLException {
		clean();
		Integer[] ids = new Integer[18];
		for(int i=0;i<18;i++){
			ids[i] = createLogistics(new Logistics(null,"test"+i,"test"+i,"test"+i,"test"+i,"test"+i,"test"+i,1,1,null));
		}
		
		//page=2 pagesize=10
		Logistics logistics = new Logistics(null,null,null,null,null,null,null,1,1,null);
		Paging page = new Paging();
		page.setStart(10);
		page.setLimit(10);
		page.setSort("id");
		
		page = logisticsService.pageLogisticsWithEid(logistics, page);
		assertNotNull(page);
		assertEquals(page.getTotals(), new Integer(18) );
		assertEquals(page.getRecords().size(), 8);
	}

	@Test
	public void testUpdateLogisticsById_no_attribute() {
		try {
			logisticsService.updateLogisticsById(null);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testUpdateLogisticsById_has_attribute() throws SQLException {
		clean();
		Integer id = createLogistics(new Logistics(null,"test","test","test","test","test","test",0,0,null));
		
		Logistics newLogistics = new Logistics(id,"newname","test","test","test","test","test",0,0,null);
		logisticsService.updateLogisticsById(newLogistics);
		
		Logistics obj = logisticsService.listOneLogisticsById(id);
		assertNotNull(obj);
		assertEquals("newname", obj.getName());
	}

	/************prepare data*********/
	private void clean() throws SQLException{
		connection.prepareStatement("delete from logistics").execute();
	}
	
	private Integer createLogistics(Logistics logistics) throws SQLException{
		String sql="";
		sql="insert into logistics(name,phone,address,routes,arrival_area,remark,uid,eid,gmtcreate) "+
			"values('"+logistics.getName()+"','"+logistics.getPhone()+"'," +
					"'"+logistics.getAddress()+"','"+logistics.getRoutes()+"'," +
					"'"+logistics.getArrivalArea()+"','"+logistics.getRemark()+"'," +
					logistics.getUid()+","+logistics.getEid()+",now())";
		connection.prepareStatement(sql).execute();

		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		return 0;
	}
	
}
