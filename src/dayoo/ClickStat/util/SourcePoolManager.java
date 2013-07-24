package dayoo.ClickStat.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dayoo.ClickStat.config.Config;

public class SourcePoolManager {
	
	private static String driver = null;
	private static String url = null;
	private static String userName = null;
	private static String passWord = null;
	private static String initialSize = null;			//初始化连接数
	private static String minIdle = null;				//最小空闲连接
	private static String maxIdle = null;				//最大空闲连接
	private static String maxWait = null;				//超时回收时间(以毫秒为单位)
	private static String maxActive = null;				//最大连接数
	private static boolean logAbandoned = false;		//是否在自动回收超时连接的时候打印连接的超时错误
	private static boolean removeAbandoned = false;		//是否自动回收超时连接
	private static int removeAbandonedTimeout = 0;		//超时时间(以秒数为单位)
	
	
	private static BasicDataSource dataSource = null;
	
	//日志
	public static Log logger = LogFactory.getLog(SourcePoolManager.class.getName());
//	public static Logger logger = Tools.getLogger(SourcePoolManager.class);
	
	public SourcePoolManager(){
	}
	
	/**
	 * 加载配置文件
	 * */
	private static void loadProperties(){
		driver = Config.DestDB.driver;
		url = Config.DestDB.url;
		userName = Config.DestDB.username;
		passWord = Config.DestDB.password;
		initialSize = ""+Config.ConnectionPool.initialSize;
		minIdle = ""+Config.ConnectionPool.minIdle;
		maxIdle = ""+Config.ConnectionPool.maxIdle;
		maxWait = ""+Config.ConnectionPool.maxWait;
		maxActive = ""+Config.ConnectionPool.maxActive;
		logAbandoned = Config.ConnectionPool.logAbandoned;
		removeAbandoned = Config.ConnectionPool.removeAbandoned;
		removeAbandonedTimeout = Config.ConnectionPool.removeAbandonedTimeout;
	}
	
	/**
	 * 初始化数据源
	 * 
	 * */
	private static synchronized void initDataSource(){
		if(dataSource==null){
			loadProperties();
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(driver);
	        dataSource.setUrl(url);
	        dataSource.setUsername(userName);
	        dataSource.setPassword(passWord);
	        dataSource.setInitialSize(Integer.parseInt(initialSize));
	        dataSource.setMinIdle(Integer.parseInt(minIdle));
	        dataSource.setMaxIdle(Integer.parseInt(maxIdle));
	        dataSource.setMaxWait(Long.parseLong(maxWait));
	        dataSource.setMaxActive(Integer.parseInt(maxActive));
	        dataSource.setLogAbandoned(logAbandoned);
	        dataSource.setRemoveAbandoned(removeAbandoned);
	        dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
	        
		}
	}
	
	/**
	 * 启动连接池
	 * 
	 * */
	public static void startPool(){
		loadProperties();
		initDataSource();
		logger.info("启动点击量数据库连接池：POOL");
	}
	
	
	/**
	 * 释放连接池
	 * 
	 * */
	public static void ShutdownPool(){
		try{
			if(dataSource!=null){
				dataSource.close();
				logger.info("关闭点击量数据库连接池：POOL");
			}
		}catch(Exception e){
			logger.error("",e);
		} 
	}
	
	
	/**
	 * 取得连接池中的连接
	 * 
	 * */
	public static synchronized Connection getConnection(){
		Connection conn = null;
		if(dataSource == null){
			startPool();
		}
		try{
			conn = dataSource.getConnection();
		}catch(Exception e){
			logger.error("",e);
		}
		return conn;
	}
	
	/**
	 * 释放连接
	 * 
	 * */
	public static void freeConnection(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("",e);
			}
		}
	}


}
