package ua.onclinic.database.pool.impl;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import ua.onclinic.Config;
import ua.onclinic.database.pool.IConnectionFactory;

public enum C3P0ConnectionFactory implements IConnectionFactory
{
	_instance;
	
	private final ComboPooledDataSource _dataSource;
	
	C3P0ConnectionFactory()
	{
		if (Config.DB_MAX_CONNECTIONS < 2)
		{
			Config.DB_MAX_CONNECTIONS = 2;
			_log.log(Level.WARNING, "A minimum of database connections are required by ", Config.DB_MAX_CONNECTIONS);
		}
		
		_dataSource = new ComboPooledDataSource();
		_dataSource.setAutoCommitOnClose(true);
		
		_dataSource.setInitialPoolSize(10);
		_dataSource.setMinPoolSize(10);
		_dataSource.setMaxPoolSize(Math.max(10, Config.DB_MAX_CONNECTIONS));
		
		_dataSource.setAcquireRetryAttempts(0); // Попытка получить соединение на неопределенный срок (0 = не закрывать)
		_dataSource.setAcquireRetryDelay(500); // 500 миллисекунд ожидания перед попыткой подключиться снова
		_dataSource.setCheckoutTimeout(0); // 0 = ждать неопределенной время перед попыткой подключиться, если пул исчерпан
		_dataSource.setAcquireIncrement(5); // если пул исчерпан, получить еще 5 соединений за раз
		// Причина в том, что при подключении возникает «длинная» задержка
		// так что одновременное подключение нескольких соединений приведет к объединению пулов
		// более эффективно.
		
		// "connection_test_table" будет автоматически создана, если ее не существует
		_dataSource.setAutomaticTestTable("connection_test_table");
		_dataSource.setTestConnectionOnCheckin(false);
		
		// Тестирование OnCheckin, используемого с IdleConnectionTestPeriod выполняется быстрее чем тестирование при проверке
		
		_dataSource.setIdleConnectionTestPeriod(3600); // теситрование неиспользуемого соединения каждые 60 сек.
		_dataSource.setMaxIdleTime(0); // 0 = неиспользуемые соединения никогда не истекают
		// *THANKS* to connection testing configured above
		// but I prefer to disconnect all connections not used
		// for more than 1 hour
		
		_dataSource.setMaxStatementsPerConnection(100);
		_dataSource.setBreakAfterAcquireFailure(false);
		
		try
		{
			_dataSource.setDriverClass(Config.DB_DRIVER);
		}
		catch (PropertyVetoException e)
		{
			_log.log(Level.WARNING, "There has been a problem setting the driver class!", e);
		}
		_dataSource.setJdbcUrl(Config.DB_URL);
		_dataSource.setUser(Config.DB_LOGIN);
		_dataSource.setPassword(Config.DB_PASSWORD);
		
		// Тестирование подключения
		try
		{
			_dataSource.getConnection().close();
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, "There has been a problem closing the test connection!", e);
		}
		
		if (Config.DEBUG)
			_log.info("Database connection working.");
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