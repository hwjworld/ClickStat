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
	private static String initialSize = null;			//��ʼ��������
	private static String minIdle = null;				//��С��������
	private static String maxIdle = null;				//����������
	private static String maxWait = null;				//��ʱ����ʱ��(�Ժ���Ϊ��λ)
	private static String maxActive = null;				//���������
	private static boolean logAbandoned = false;		//�Ƿ����Զ����ճ�ʱ���ӵ�ʱ���ӡ���ӵĳ�ʱ����
	private static boolean removeAbandoned = false;		//�Ƿ��Զ����ճ�ʱ����
	private static int removeAbandonedTimeout = 0;		//��ʱʱ��(������Ϊ��λ)
	
	
	private static BasicDataSource dataSource = null;
	
	//��־
	public static Log logger = LogFactory.getLog(SourcePoolManager.class.getName());
//	public static Logger logger = Tools.getLogger(SourcePoolManager.class);
	
	public SourcePoolManager(){
	}
	
	/**
	 * ���������ļ�
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
	 * ��ʼ������Դ
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
	 * �������ӳ�
	 * 
	 * */
	public static void startPool(){
		loadProperties();
		initDataSource();
		logger.info("������������ݿ����ӳأ�POOL");
	}
	
	
	/**
	 * �ͷ����ӳ�
	 * 
	 * */
	public static void ShutdownPool(){
		try{
			if(dataSource!=null){
				dataSource.close();
				logger.info("�رյ�������ݿ����ӳأ�POOL");
			}
		}catch(Exception e){
			logger.error("",e);
		} 
	}
	
	
	/**
	 * ȡ�����ӳ��е�����
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
	 * �ͷ�����
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
