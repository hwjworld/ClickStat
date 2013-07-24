/**
 * 
 */
package dayoo.ClickStat.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author Canni
 *
 */
public class CSUtil {
	private static String selectNumSql = "select NUM FROM CLICKSTAT WHERE ID=?";
	private static String updateNumSql = "update clickstat set num=num+1 where id=?";
	private static String insertNumSql = "insert into clickstat(id,num) values(?,0);";
	private static String countNumSql = "select count(*) from clickstat where id=?";
	private static Log log = LogFactory.getLog(CSUtil.class);
	
	/*//no conn
	public static String getNumFromDB(String id) {
		if("0".equals(getCountNum(id))){
			inserNewtNum(id);
		}
		return selectNum(id, selectNumSql);
	}
	
	private static String getCountNum(String id) {
		return selectNum(id, countNumSql);
	}
	
	private static void inserNewtNum(String id) {
		updateNum(id,insertNumSql);
	}
	
	public static void updateNum(String id) {
		if("0".equals(getCountNum(id))){
			inserNewtNum(id);
		}
		updateNum(id,updateNumSql);
	}
	*/
	
	//conn
	public static String getNumFromDB(String id,Connection conn) {
		if("0".equals(getCountNum(id,conn))){
			inserNewtNum(id,conn);
		}
		return selectNum(id, selectNumSql,conn);
	}
	
	private static String getCountNum(String id,Connection conn) {
		return selectNum(id, countNumSql,conn);
	}
	
	private static void inserNewtNum(String id,Connection conn) {
		updateNum(id,insertNumSql,conn);
	}
	
	public static void updateNum(String id,Connection conn) {
		if("0".equals(getCountNum(id,conn))){
			inserNewtNum(id,conn);
		}
		updateNum(id,updateNumSql,conn);
	}
	
	
	
	/*
	private static void updateNum(String id,String sql){
		Connection conn = null;
		try {
			conn = SourcePoolManager.getConnection();
			updateNum(id, sql, conn);
		} finally {
			SourcePoolManager.freeConnection(conn);
		}
	}
	
	private static String selectNum(String id,String sql){
		Connection conn = null;
		String ret = "0";
		try {
			conn = SourcePoolManager.getConnection();
			ret = selectNum(id, sql, conn);
		} finally {
			SourcePoolManager.freeConnection(conn);
		}
		return ret;
	}*/
	

	private static void updateNum(String id,String sql,Connection conn){
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("更新点击数记录错误 , id:"+id,e);
		}finally {
			SqlHandler.close(ps);
		}
	}
	
	private static String selectNum(String id,String sql,Connection conn){
		PreparedStatement ps = null;
		ResultSet rs = null;
		String ret = "0";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				ret = String.valueOf(rs.getInt(1));
			}
		} catch (SQLException e) {
			log.error("从数据库中查询点击数出错,ID: "+id,e);
		}finally {
			SqlHandler.close(rs, ps);
		}
		return ret;
	}
		
	public static int revoverFromFile(Iterator iter){
		int rv = 0;
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (!item.isFormField()) {
					String fieldName = item.getFieldName();
					if ("stat".equalsIgnoreCase(fieldName)) {
						InputStream uploadedStream = item.getInputStream();
						SAXReader reader = new SAXReader();
						Document doc = reader.read(uploadedStream);
						List eles = doc.getRootElement().elements();
						Element ele = null;		
						for(int i=0;i<eles.size();i++){
							ele = (Element)eles.get(i);
							String id = ele.element("id").getText();
							String num = ele.element("num").getText();
							if(StringValueUtils.getInt(selectNum(id, selectNumSql, conn))<StringValueUtils.getInt(num)){
								SqlHandler.excuteSQL(conn,"delete from clickstat where id='"+id+"'");
								SqlHandler.excuteSQL(conn,"insert into clickstat(id,num) values('"+id+"',"+num+")");
								rv++;
							}
							
						}
						log.info("恢复数据成功");
						uploadedStream.close();
					}
				}
			}
		}  catch (Exception e) {
			log.error("", e);
		} finally {
			SqlHandler.close(conn);
		}
		return rv;
	}
}
