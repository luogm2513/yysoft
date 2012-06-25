/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-3-13
 */
package net.caiban.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.caiban.facade.ParamFacade;
import net.caiban.service.auth.ParamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class BaseServiceTestCase extends AbstractDependencyInjectionSpringContextTests{

	protected java.sql.Connection   connection;
	
	@Autowired
	private SqlMapClientFactoryBean sqlMapClient;
	@Autowired
	public ParamService paramService;
	
	protected String[] getConfigLocations() {
        return new String[] { "spring-persist.xml","spring-service.xml" };
    }
	
	public void onSetUp() {
        try {
            connection = ((SqlMapClient) sqlMapClient.getObject()).getDataSource().getConnection();
        } catch (SQLException e) {
        }
    }
	
	public void onTearDown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }
	
	protected int executeCountSQL(String sql) throws SQLException {
        ResultSet resultSet;
        resultSet = connection.prepareStatement(sql).executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    protected ResultSet executeQuerySQL(String sql) throws SQLException {
        ResultSet resultSet;
        resultSet = connection.prepareStatement(sql).executeQuery();
        return resultSet;
    }

    protected int executeUpdateSQL(String sql) throws SQLException {
        return connection.prepareStatement(sql).executeUpdate();
    }
    
    public void test_demo(){
    	assertTrue(true);
    }
    
    public void initParam() throws SQLException{
    	connection.prepareStatement("delete from param_type").execute();
		connection.prepareStatement("delete from param").execute();
		
		// earlier
		connection.prepareStatement("insert into param_type (`type`,names,gmtcreate) values ('combo_eshop_buyer_valuable','客户等级',now())").execute();
		connection.prepareStatement("insert into param_type (`type`,names,gmtcreate) values ('orders_payment','订单支付方式',now())").execute();
		connection.prepareStatement("insert into param_type (`type`,names,gmtcreate) values ('orders_status','订单状态',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('combo_eshop_buyer_valuable','',0,'潜在客户',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('combo_eshop_buyer_valuable','',1,'一般客户',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('combo_eshop_buyer_valuable','',2,'重要客户',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('combo_eshop_buyer_valuable','',3,'纠纷客户',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_payment','','alipay','支付宝',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_payment','','bank','银行',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_payment','','remittance','邮局汇款',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_payment','','cash','现金',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_payment','','orther','其他',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_status','',0,'新订单',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_status','',10,'备货',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_status','',30,'已发货',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_status','',40,'售后纠纷',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('orders_status','',50,'交易完成',now())").execute();
		
		// 2010.8.28 ADD
		connection.prepareStatement("insert into param_type (`type`,names,gmtcreate) values ('sale_dispute_severity','纠纷严重程度',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',0,'轻微',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',10,'一般',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',20,'严重',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,gmtcreate) values('sale_dispute_severity','',30,'紧急',now())").execute();
		
		// 2010.9.4 ADD
		connection.prepareStatement("insert into param_type(`type`,names,gmtcreate) values('sale_dispute_resolved','纠纷处理状态',now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','0','新建',0,0,now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','10','进行中',0,0,now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','20','已解决',0,0,now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','30','已闭关',0,0,now())").execute();
		connection.prepareStatement("insert into param(`type`,names,`key`,value,sort,`used`,gmtcreate) values('sale_dispute_resolved','','40','已拒绝',0,0,now())").execute();
		
    	ParamFacade.getInstance().initCache(paramService.listAllParam((short)0));
    }
	
}
