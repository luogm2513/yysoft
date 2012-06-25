package net.caiban.service.auth.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.caiban.domain.auth.Param;
import net.caiban.domain.auth.ParamType;
import net.caiban.service.BaseServiceTestCase;
import net.caiban.service.auth.ParamService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ParamServiceImplTest extends BaseServiceTestCase{
	
	@Autowired
	ParamService paramService;

	@Test
	public void testListAllParam() throws SQLException {
		clean();
		createParams(createParamType(new ParamType("test_type1")));
		createParams(createParamType(new ParamType("test_type2")));
		
		List<Param> paramList = paramService.listAllParam(null);
		assertNotNull(paramList);
		assertEquals(10, paramList.size());
		
		createParam(new Param(null,createParamType(new ParamType("test_type3"))
				,"param","key","value_",null,(short)0,null));
		
		paramList = paramService.listAllParam((short)1);
		assertNotNull(paramList);
		assertEquals(10, paramList.size());
		
		paramList = paramService.listAllParam((short)0);
		assertNotNull(paramList);
		assertEquals(1, paramList.size());
	}
	
	/**
	 * @throws SQLException
	 */
	private void clean() throws SQLException{
		connection.prepareStatement("delete from param_type").execute();
		connection.prepareStatement("delete from param").execute();
	}
	
	private int createParam(Param param) throws SQLException{
		String sql="";
		sql="insert into param(type,names,`key`,value,sort,used,gmtcreate) ";
		sql=sql+" values('"+param.getType()
			+"','"+param.getNames()
			+"','"+param.getKey()
			+"','"+param.getValue()
			+"',0,"+param.getUsed()
			+",now())";
			
		connection.prepareStatement(sql).execute();
		ResultSet rs=connection.createStatement().executeQuery("select last_insert_id()");
		if(rs.next()){
			return rs.getInt(1);
		}
		return 0;
	}
	
	private String createParamType(ParamType type) throws SQLException{
		String sql="";
		sql="insert into param_type(type,names,gmtcreate) ";
		sql = sql+ "values('"+type.getType()+"','"+type.getNames()+"',now())";
		
		connection.prepareStatement(sql).execute();
		return type.getType();
	}
	
	private void createParams(String type) throws SQLException{
		
		for(int i=0;i<5;i++){
			createParam(new Param(null,type,"param"+i,"key_"+i,"value_"+i,null,(short)1,null));
		}
		
	}
}
