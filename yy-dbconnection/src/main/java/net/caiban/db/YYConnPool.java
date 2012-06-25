/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-10-20
 */
package net.caiban.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;


/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class YYConnPool {
	
	
	private static YYConnPool _instance =null;
	
	public static synchronized YYConnPool getInstance(){
		if(_instance==null){
			_instance = new YYConnPool();
		}
		return _instance;
	}
	
	private Logger LOG = Logger.getLogger(YYConnPool.class);
//	private String DEFAULT_DB = "defaultdb";
	private String DEFAULT_URL = "jdbc:mysql://127.0.0.1/default?useUnicode=true&characterEncoding=UTF-8";
	private String dEFAULT_DRIVER = "org.gjt.mm.mysql.Driver";
	private String DEFAULT_MAXCONN = "10";
	
	/**
	 * 
	 */
	private YYConnPool(){
		initConnPools();
	}
	
	/**
	 * 读取配置文件,并初始化数据库连接池
	 */
	public  void initConnPools(){
		//TODO 初始化数据库连接池，读取配置文件，并分别注册数据库
		
		LOG.debug("正在读取配置文件...");
		//test
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("db.properties");
		Properties p = new Properties();
		try {
			p.load(is);
		} catch (IOException e) {
		}
		
//		this.DEFAULT_DB = p.getProperty("dbdefault");
		String[] dblist = p.getProperty("dblist").split(",");
		for(String s:dblist){
//			LOG.debug("正在注册连接... DB："+s);
			registerConnection(s, 
					p.getProperty(s+".url", DEFAULT_URL),
					p.getProperty(s+".username"), 
					p.getProperty(s+".password"),
					Integer.valueOf(p.getProperty(s+".maxconn",DEFAULT_MAXCONN)),
					p.getProperty(s+".driver", dEFAULT_DRIVER));
		}
		
	}
	
	/**
	 * 注册数据库连接池
	 * @param db:数据库名称
	 * @param url:连接用的url
	 * @param username:用户名
	 * @param password:密码
	 * @param maxconn:最大连接数
	 * @param driver:驱动
	 */
	private void registerConnection(String db, String url, String username, String password, int maxconn, String driver){
		//TODO 注册数据库连接池
		LOG.debug("正在准备注册连接...DB:"+db);
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
		} catch (ClassNotFoundException e) {
			LOG.error("无法载入ProxoolDriver",e);
		}
		String proxoolURL = "proxool." + db + ":" + driver + ":" + url;
		Properties info = new Properties();
		info.setProperty("proxool.maximun-connection-count", String.valueOf(maxconn));
		info.setProperty("user", username);
		info.setProperty("password", password);
		info.setProperty("proxool", "true");
		
		try {
			LOG.debug("正在注册连接...  DB:"+db);
			ProxoolFacade.registerConnectionPool(proxoolURL, info);
		} catch (ProxoolException e) {
			LOG.error("注册连接发生错误.DB:"+db,e);
		}
		
	}
	
	/**
	 * 得到一个数据库连接
	 * @param db:数据库名称
	 * @return
	 */
	public Connection getConnection(String db){
		LOG.debug("正在获取数据库连接... DB:"+db);
		Connection connection = null;
		try {
			if(db!=null){
				connection = DriverManager.getConnection("proxool."+db);
			}
		} catch (SQLException e) {
			LOG.error("没有正确获取数据库连接 DB:"+db,e);
		}
		return connection;
	}
	
	final static int DESTORY_DELAY = 5000;
	
	/**
	 * 关闭数据库连接
	 */
	public void destoryConnectionPools(){
		LOG.debug("连接将在"+DESTORY_DELAY+"ms后销毁...");
		ProxoolFacade.shutdown(DESTORY_DELAY);
	}
	
}
