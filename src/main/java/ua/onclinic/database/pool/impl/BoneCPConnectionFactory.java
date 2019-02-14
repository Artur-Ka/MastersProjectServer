package ua.onclinic.database.pool.impl;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPDataSource;

import ua.onclinic.Config;
import ua.onclinic.database.pool.IConnectionFactory;

public enum BoneCPConnectionFactory implements IConnectionFactory
{
	_instance;
	
	private static final int PARTITION_COUNT = 5;
	
	private final BoneCPDataSource _dataSource;
	
	BoneCPConnectionFactory()
	{
		_dataSource = new BoneCPDataSource();
		_dataSource.setJdbcUrl(Config.DB_URL);
		_dataSource.setUsername(Config.DB_LOGIN);
		_dataSource.setPassword(Config.DB_PASSWORD);
		_dataSource.setPartitionCount(PARTITION_COUNT);
		_dataSource.setMaxConnectionsPerPartition(Config.DB_MAX_CONNECTIONS);
		_dataSource.setIdleConnectionTestPeriod(0, TimeUnit.SECONDS); // 0 = неиспользуемые соединения никогда не истекают
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