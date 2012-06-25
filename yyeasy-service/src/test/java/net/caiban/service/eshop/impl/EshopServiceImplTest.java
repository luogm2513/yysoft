package net.caiban.service.eshop.impl;

import java.sql.SQLException;

import net.caiban.service.BaseServiceTestCase;

import org.junit.Test;

public class EshopServiceImplTest extends BaseServiceTestCase{

//	@Test
//	public void testListOneEshopByUid() {
//		fail("Not yet implemented");
//	}

	private void clean() throws SQLException{
		connection.prepareStatement("delete from eshop").execute();
		connection.prepareStatement("delete from eshop_account").execute();
		connection.prepareStatement("delete from auth_user").execute();
	}
}
