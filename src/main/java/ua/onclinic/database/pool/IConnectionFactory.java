package ua.onclinic.database.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

public interface IConnectionFactory
{
	static final Logger _log = Logger.getLogger(IConnectionFactory.class.getName());
	
	
	DataSource getDataSource();
	
	void close();
	
	/**
	 * Получить соединение из пула.<BR><BR>
	 * @return
	 */
	default Connection getConnection()
	{
		Connection con = null;
		while (con == null)
		{
			try
			{
				con = getDataSource().getConnection();
			}
			catch (SQLException e)
			{
				_log.log(Level.WARNING, "Unable to get a connection " + getClass().getSimpleName(), e);
			}
		}
		
		return con;
	}
}