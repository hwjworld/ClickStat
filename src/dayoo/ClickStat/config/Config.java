package dayoo.ClickStat.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {
	private static Log log = LogFactory.getLog(Config.class);
	/**
	 * IP����ʷ������(�����ĩ�η���ʱ��)
	 * �洢��ʽ:
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
	 * �Ƿ��д�ip��¼
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
	 * ��IP�Ƿ��д˸�����ʼ�¼
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
	 * �õ����IP����һƪ��������һ�ε��ʱ��
	 * @param ip
	 * @param artid
	 * @return long�ͣ����û�д�IP��¼���򷵻�0
	 */
	private static long getLastAccessTime(String ip,String artid){
		long result = 0;
		if(isHaveIP(ip))
			if(isHaveArtid(ip, artid))
				result = ((Long)getIPInfo(ip).get(artid)).longValue();
		return result;
	}
	
	/**
	 * �жϴ�ip�Դ���Ƶ�� ������ϴη��� �Ƿ��ڹ涨��ʱ������
	 * @param ip
	 * @param artid
	 * @param curAccessTime
	 * @return true ���η�����ʱ�����ڣ�����Ϊ������<br>false ������ʱ�����ĵ��������
	 */
	public static boolean isInTimeRange(String ip,String artid,long curAccessTime){
		boolean result = true;
		if(isHaveIP(ip)){
			long lastAccTime = getLastAccessTime(ip, artid);
			if(curAccessTime-lastAccTime<timeRange){
				result = true;
				log.warn("IP "+ip+" �ڹ涨ʱ���� "+timeRange+" �����ظ�����");
			}else
				result = false;
		}else
			result = false;
		return result;
	}
	
	/**
	 * ����һ��IP������Ƶ��ʱ���¼,�������Ƿ��ڼ��ʱ���ڵ��ж�
	 * @param ip
	 * @param artid
	 * @param curAccessTime
	 * @return true���ӳɹ�<br>false����ʧ��
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
