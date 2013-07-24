/**
 * ConnectionManager.java
 *
 * Gracefully 
 * Student Team
 * 2008
 */
package dayoo.ClickStat.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��Ӧ�÷������л�ȡ���ӳص�����
 * @author Canni
 */
public class ConnectionManager {
	private static Log log = LogFactory.getLog(ConnectionManager.class);

    private DataSource _dataSource = null;

    private static ConnectionManager _connectionManager = null;

    private ConnectionManager() throws SQLException
    {
        initDataSource();
    }

    private void initDataSource() throws SQLException
    {
        try
        {
        	Context ctx = new InitialContext();
        	_dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/Cs");//jdbc/Cs");
        	log.info("��ʼ������Դ�ɹ�");
        }
        catch (Exception ex)
        {
            log.warn("Init DataSource Error : ",ex);
            throw new SQLException("Init DataSource Error : " + ex);
        }
    }

    public static ConnectionManager getInstance() throws SQLException
    {
        if (_connectionManager == null) {
            _connectionManager = new ConnectionManager();
        }
        return _connectionManager;
    }

    public Connection getConnection() throws SQLException
    {
        Connection conn = null;
        try
        {
            conn = _dataSource.getConnection();
        }
        catch (Exception ex)
        {
            log.warn("�޷�ȡ�����ݿ�����",ex);
            throw new SQLException("�޷�ȡ�����ݿ�����");
        }
        return conn;
    }

    public DataSource getDataSource() throws SQLException{
        if(_dataSource == null) {
            initDataSource();
        }
        return _dataSource;
    }    
}
