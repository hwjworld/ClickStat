/**
 * 
 */
package dayoo.ClickStat.config;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dayoo.ClickStat.util.StringValueUtils;
import dayoo.ClickStat.util.XMLProperties;



/**
 * @author hwj
 *
 */
public class ConfigManager{
	

	private static Log log = LogFactory.getLog(ConfigManager.class);
	
	private static String CONFIG_FILENAME = "statConfig.xml";
	
	private static String dataFile = "stat.xml";
	
	private static String CSHome = null;
		
	public static XMLProperties properties = null;
	
	private synchronized static void loadProperties(){
		if(properties == null){
			properties = new XMLProperties(getConfigFile());
			log.info("加载成功配置文件"+CONFIG_FILENAME);
		}
		try{
			Config.validNum = Integer.parseInt(properties.getProperty("validNum"));
			Config.interval = Long.parseLong(properties.getProperty("interval"));
			Config.timeRange = Long.parseLong(properties.getProperty("timeRange"))*1000;
			//dest db
			Config.DestDB.driver = getConfigProperty("DestDB.driver");
			Config.DestDB.url = getConfigProperty("DestDB.url");
			Config.DestDB.username = getConfigProperty("DestDB.username");
			Config.DestDB.password = getConfigProperty("DestDB.password");
			//pool
			Config.ConnectionPool.initialSize = StringValueUtils.getInt(getConfigProperty("ConnectionPool.initialSize"));
			Config.ConnectionPool.logAbandoned = StringValueUtils.getBoolean(getConfigProperty("ConnectionPool.logAbandoned"));
			Config.ConnectionPool.maxActive = StringValueUtils.getInt(getConfigProperty("ConnectionPool.maxActive"));
			Config.ConnectionPool.maxIdle = StringValueUtils.getInt(getConfigProperty("ConnectionPool.maxIdle"));
			Config.ConnectionPool.maxWait = StringValueUtils.getInt(getConfigProperty("ConnectionPool.maxWait"));
			Config.ConnectionPool.minIdle = StringValueUtils.getInt(getConfigProperty("ConnectionPool.minIdle"));
			Config.ConnectionPool.removeAbandoned = StringValueUtils.getBoolean(getConfigProperty("ConnectionPool.removeAbandoned"));
			Config.ConnectionPool.removeAbandonedTimeout = StringValueUtils.getInt(getConfigProperty("ConnectionPool.removeAbandonedTimeout"));
			
		}catch (Exception e) {
			log.error(" 请查看配置文件是否配置正常,不正确项将采用默认配置 ");
		}
	}
	
	public static String getConfigProperty(String name) {
		if (properties == null)
			loadProperties();
		String tmp = properties.getProperty(name);
		return tmp==null?"":tmp;
	}
	
	public static void initConfig() {
		loadProperties();
	}
	
	public static String getCSHome(){
		try{
			if(CSHome == null){
				CSHome = ResourceBundle.getBundle("init").getString("clickstatHome");
				if(CSHome == null){
		            log.error("There is no 'clickstatHome' property in the 'init.properties'!");
		            System.exit(1);
				}
			}
		}catch (Exception e)
        {
            log.error("Can not found 'init.properties' in classpath!", e);
            System.exit(1);
        }
		return CSHome;
	}
	
	public static void updateConfig(){
		properties = null;
		loadProperties();
	}
	
	public static String getDataFile(){
		return getCSHome()+File.separator+dataFile;
	}
	
	public static String getConfigFile(){
		return getCSHome()+File.separator+CONFIG_FILENAME;
	}
	
	public static String getProperty(String prop){
		return properties.getProperty(prop);
		
	}
	
	public static void main(String[] args){
//		loadProperties();
	}

}
