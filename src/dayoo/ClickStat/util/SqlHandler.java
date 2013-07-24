package dayoo.ClickStat.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SqlHandler {

	private static Log log = LogFactory.getLog(SqlHandler.class);

	/**
	 * 取sequence的值 
	 */
	public static long getSequeenceValue(Connection con, String seqName) {
		long value = -1;
		Statement st = null;
		ResultSet rs = null;
		String sSQL = "values next value for " + seqName;
		try {
			st = con.createStatement();
			rs = st.executeQuery(sSQL);
			while (rs.next()) {
				value = rs.getLong(1);
			}
		} catch (Exception ex) {
			log.error("no Such Sequeence:" + seqName);
		} finally {
			close(rs,st);
		}
		return value;
	}

	/**
	 * 返回更新的record count
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int excuteSQL(String sql) throws SQLException {
		int count = -1;
		Connection con = null;
		try {
			con = ConnectionManager.getInstance().getConnection();
			count = excuteSQL(con, sql);
		} catch (Exception ex) {
			log.error("excuteUpdate Error:" + sql,ex);
		} finally {
			close(con);
		}
		return count;
	}

	/**
	 * 返回更新的record count
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int excuteSQL(Connection con, String sSQL) {
		int count = -1;
		Statement st = null;
		try {
			st = con.createStatement();
			count = st.executeUpdate(sSQL);
		} catch (Exception ex) {
			log.error("excuteUpdate Error:" + sSQL,ex);
		} finally {
			close(st,null);
		}
		return count;
	}

	/**
	 * 从数据库中获取一个字符串结果
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static String getStringBySql(String sql) throws SQLException {
		Connection conn = null;
		String ret = null;

		try {
			conn = ConnectionManager.getInstance().getConnection();
			ret = getStringBySql(conn, sql);
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			close(conn);
		}
		return ret;
	}

	/**
	 * 从数据库中获取一个字符串结果
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static String getStringBySql(Connection conn, String sql)
			throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		String res = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				res = rs.getString(1);
			}
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			close(rs, st);
		}

		return res;
	}

	/**
	 * 从数据库中获取一个Date结果
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static Date getDateBySql(String sql) throws SQLException {
		Connection conn = null;
		Date res = null;

		try {
			conn = ConnectionManager.getInstance().getConnection();
			res = getDateBySql(conn, sql);
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			close(conn);
		}
		return res;
	}

	/**
	 * 从数据库中获取一个Date结果
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static Date getDateBySql(Connection conn, String sql)
			throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		Date res = null;

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				res = rs.getTimestamp(1);
			}
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			close(rs, st);
		}

		return res;
	}

	/**
	 * 从数据库中方便的获取一个整型值，通常象select count(*) from xxx语句
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int getIntBySql(String sql) throws SQLException {
		int res = 0;
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			res = getIntBySql(conn, sql);
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			close(conn);
		}
		return res;
	}

	/**
	 * 从数据库中方便的获取一个整型值，通常象select count(*) from xxx语句
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int getIntBySql(Connection conn, String sql)
			throws SQLException {
		int res = 0;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			close(rs, pst);
			rs = null;
			pst = null;
		}
		return res;
	}

	/**
	 * 关闭数据库连接所使用的资源
	 * @param rs
	 * @param st
	 * @param con
	 */
	public static void close(ResultSet rs, Statement st, Connection con) {
		try {
			rs.close();
		} catch (Exception e) {
		}

		try {
			st.close();
		} catch (Exception e) {
		}

		try {
			con.close();
		} catch (Exception e) {
		}
	}

	public static void close(ResultSet rs, PreparedStatement st, Connection conn) {
		try {
			rs.close();
		} catch (Exception e) {
		}

		try {
			st.close();
		} catch (Exception e) {
		}

		try {
			conn.close();
		} catch (Exception e) {
		}
	}

	public static void close(Statement st, Connection conn) {
		close(null, st, conn);
	}

	public static void close(Connection conn) {
		close(null, null, conn);
	}
	
	public static void close(PreparedStatement ps ) {
		close(null, ps, null);
	}

	public static void close(ResultSet rs, Statement st) {
		close(rs, st, null);
	}

	public static void close(ResultSet rs) {
		close(rs, null, null);
	}

	public static void rollback(Connection conn) {
		try {
			conn.rollback();
		} catch (Exception e) {

		}
	}
}
