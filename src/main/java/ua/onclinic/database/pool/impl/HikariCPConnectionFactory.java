package ua.onclinic.database.pool.impl;

import java.util.logging.Level;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import ua.onclinic.Config;
import ua.onclinic.database.pool.IConnectionFactory;

public enum HikariCPConnectionFactory implements IConnectionFactory
{
	_instance;
	
	private final HikariDataSource _dataSource;
	
	HikariCPConnectionFactory()
	{
		_dataSource = new HikariDataSource();
		_dataSource.setJdbcUrl(Config.DB_URL);
		_dataSource.setUsername(Config.DB_LOGIN);
		_dataSource.setPassword(Config.DB_PASSWORD);
		_dataSource.setMaximumPoolSize(Config.DB_MAX_CONNECTIONS);
		_dataSource.setIdleTimeout(0); // 0 = неиспользуемые соединения никогда не истекают
	}
	
	@Override
	public DataSource getDataSource()
	{
		return _dataSource;
	}
	
	@Override
	public void close()
	{
		try
		{
			_dataSource.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "There has been a problem closing the data source!", e);
		}
	}
}