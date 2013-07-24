package dayoo.ClickStat.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {
	private static Log log = LogFactory.getLog(Config.class);
	/**
	 * IP与访问访问情况(稿件与末次访问时间)
	 * 存储形式:
	 * <ip,Map<artid,lastAccessTime>>
	 */
	private static Map ipAccessInfo = new HashMap();
	
	public static int validNum = 17;
	public static long interval = 3;
	public static long timeRange = 60*1000;
	public static String getCSHome(){
		return ConfigManager.getCSHome();
	}
	
	/**
	 * 是否有此ip记录
	 * @param ip
	 * @return
	 */
	public static boolean isHaveIP(String ip){
		return ipAccessInfo.containsKey(ip);
	}
	
	private static Map getIPInfo(String ip){
		return (Map)ipAccessInfo.get(ip);
	}
	
	/**
	 * 此IP是否有此稿件访问记录
	 * @param ip
	 * @param artid
	 * @return
	 */
	private static boolean isHaveArtid(String ip,String artid){
		boolean result = false;
		if(isHaveIP(ip))
			if(getIPInfo(ip).containsKey(artid))
				result = true;
		return result;
	}
	/**
	 * 得到最后IP对于一篇稿件的最后一次点击时间
	 * @param ip
	 * @param artid
	 * @return long型，如果没有此IP记录，则返回0
	 */
	private static long getLastAccessTime(String ip,String artid){
		long result = 0;
		if(isHaveIP(ip))
			if(isHaveArtid(ip, artid))
				result = ((Long)getIPInfo(ip).get(artid)).longValue();
		return result;
	}
	
	/**
	 * 判断此ip对此视频的 这次与上次访问 是否在规定的时间间隔内
	 * @param ip
	 * @param artid
	 * @param curAccessTime
	 * @return true 两次访问在时间间隔内，可判为恶意点击<br>false 超过了时间间隔的点击，正常
	 */
	public static boolean isInTimeRange(String ip,String artid,long curAccessTime){
		boolean result = true;
		if(isHaveIP(ip)){
			long lastAccTime = getLastAccessTime(ip, artid);
			if(curAccessTime-lastAccTime<timeRange){
				result = true;
				log.warn("IP "+ip+" 在规定时间间隔 "+timeRange+" 秒内重复访问");
			}else
				result = false;
		}else
			result = false;
		return result;
	}
	
	/**
	 * 增加一次IP访问视频的时间记录,包括了是否在间隔时间内的判断
	 * @param ip
	 * @param artid
	 * @param curAccessTime
	 * @return true增加成功<br>false增加失败
	 */
	public static boolean addIPOneClick(String ip,String artid,long curAccessTime){
		boolean result = false;
		if(isHaveIP(ip)){
			if(isHaveArtid(ip, artid)){
				if(!isInTimeRange(ip, artid, curAccessTime)){
					getIPInfo(ip).put(artid, new Long(curAccessTime));//jdk1.5 Long.valueOf(curAccessTime));
					result = true;
				}
			}else{
				getIPInfo(ip).put(artid, new Long(curAccessTime));//jdk1.5 Long.valueOf(curAccessTime));
				result = true;
			}
		}else{
			ipAccessInfo.put(ip, new HashMap());
			result = addIPOneClick(ip, artid, curAccessTime);
		}
		return result;
	}
	

	public static class DestDB{
		public static String driver = null;
		public static String url = null;
		public static String username = null;
		public static String password = null;
	}
	public static class ConnectionPool{
		public static int initialSize = 0;
		public static int minIdle = 0;
		public static int maxIdle = 0;
		public static int maxActive = 0;
		public static int maxWait = 0;
		public static boolean logAbandoned = false;
		public static boolean removeAbandoned = false;
		public static int removeAbandonedTimeout = 0;
	}
}
