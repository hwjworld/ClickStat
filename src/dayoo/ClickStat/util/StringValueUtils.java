/**
 * StringValueUtils.java
 *
 * Gracefully 
 * Student Team
 * 2008
 */
package dayoo.ClickStat.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��String�ַ����л�ȡ��Ӧֵ
 * 
 * @author Canni
 */
public class StringValueUtils {
	private static Log log = LogFactory.getLog(StringValueUtils.class);

	/**
	 * �����򷵻�Ĭ��ֵ
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static int getInt(String value, int defaultValue) {
		int result = defaultValue;
		try {
			result = Integer.parseInt(value);
		} catch (Exception ex) {
		}
		return result;
	}

	public static int getInt(String value) {
		return getInt(value, 0);
	}

	/**
	 * ȡ�ַ����е�longֵ
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static long getLong(String value, long defaultValue) {
		long result = defaultValue;
		try {
			result = Long.parseLong(value);
		} catch (Exception ex) {
		}
		return result;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static long getLong(String value) {
		return getLong(value, 0L);
	}

	// float,double

	/**
	 * <p>
	 * ��ȡ�ַ����е�booleanֵ, �����ת�������г����κδ����򷵻�ȱʡֵ
	 * </p>
	 * <p>
	 * ����:
	 * <ul>
	 * <li>true</li>
	 * <li>y</li>
	 * <li>yes</li>
	 * <li>1</li>
	 * <li>on</li>
	 * </ul>
	 * ��Ӧ���ַ����᷵��true
	 * <ul>
	 * <li>false</li>
	 * <li>n</li>
	 * <li>no</li>
	 * <li>0</li>
	 * <li>off</li>
	 * </ul>
	 * ��Ӧ���ַ����᷵��false
	 * </p>
	 * 
	 * @param value
	 *            ������ַ���
	 * @param defaultValue
	 *            ȱʡֵ
	 * @return ��Ӧ��booleanֵ
	 */
	public static boolean getBoolean(String value, boolean defaultValue) {
		boolean result = defaultValue;
		if ("true".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value)
				|| "yes".equalsIgnoreCase(value) || "1".equals(value)
				|| "on".equalsIgnoreCase(value)) {
			result = true;
		} else if ("false".equals(value) || "n".equals(value)
				|| "no".equalsIgnoreCase(value) || "0".equals(value)
				|| "off".equalsIgnoreCase(value)) {
			result = false;
		}
		return result;
	}

	/**
	 * <p>
	 * ��ȡ�ַ����е�booleanֵ, �����ת�������г����κδ����򷵻�ȱʡֵ<b>false</b>
	 * </p>
	 * <p>
	 * ����:
	 * <ul>
	 * <li>true</li>
	 * <li>y</li>
	 * <li>yes</li>
	 * <li>1</li>
	 * <li>on</li>
	 * </ul>
	 * ��Ӧ���ַ����᷵��true
	 * <ul>
	 * <li>false</li>
	 * <li>n</li>
	 * <li>no</li>
	 * <li>0</li>
	 * <li>off</li>
	 * </ul>
	 * ��Ӧ���ַ����᷵��false
	 * </p>
	 * 
	 * @param value
	 *            ������ַ���
	 * @return ��Ӧ��booleanֵ
	 */
	public static boolean getBoolean(String value) {
		return getBoolean(value, false);
	}

	/**
	 * ��������"2003-12-11 00:00:00"���ַ���ת��Ϊ"2003-12-11"
	 * 
	 * @param str
	 * @return
	 */
	public static String getDateFromStr(String str) {
		String date = null;
		char[] date1 = str.toCharArray();
		char[] date2 = new char[date1.length];
		for (int i = 0; i < date1.length - 9; i++) {
			date2[i] = date1[i];
		}
		date = String.copyValueOf(date2).trim();
		return date;
	}

	/**
	 * ��������ַ�����������ת��Ϊint���� *
	 * 
	 * @param str
	 * @return
	 */
	public static int getIntFromString(String str) {
		int result = 0;
		try {
			result = Integer.parseInt(str, 2);
		} catch (Exception ex) {
		}
		return result;
	}

	/**
	 * ��������ַ�����������ת��Ϊlong����ֵ
	 * 
	 * @param str
	 * @return
	 */
	public static long getLongFromString(String str) {
		long result = 0;
		try {
			result = Long.parseLong(str, 2);
		} catch (Exception ex) {
		}
		return result;
	}

//	/**
//	 * ȥ��str�����Ķ���
//	 * 
//	 * @param str
//	 * @author
//	 */
//	public static String clean(String str) {
//		String s = str;
//		if (str == null)
//			return str;
//		if (str.endsWith(",")) {
//			s = str.substring(0, str.length()-1);
//		}
//		return s;
//	}
	
	/**
	 * ��ΪnullתΪ""
	 */
	public static String getString(String str){
		if(str == null)
			return "";
		else
			return str;
	}

}
