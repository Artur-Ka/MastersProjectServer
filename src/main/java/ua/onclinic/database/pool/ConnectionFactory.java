package ua.onclinic.database.pool;

import java.util.logging.Level;
import java.util.logging.Logger;

import ua.onclinic.Config;
import ua.onclinic.database.pool.impl.BoneCPConnectionFactory;
import ua.onclinic.database.pool.impl.C3P0ConnectionFactory;
import ua.onclinic.database.pool.impl.HikariCPConnectionFactory;

public class ConnectionFactory
{
	private ConnectionFactory()
	{
		// Скрываем конструктор
	}
	
	public static final IConnectionFactory getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static final class SingletonHolder
	{
		private static final Logger _log = Logger.getLogger(ConnectionFactory.class.getName());
		
		private static final IConnectionFactory _instance;
		
		static
		{
			switch (Config.DB_CONNECTION_POOL)
			{
				default:
				case "HikariCP":
				{
					_instance = HikariCPConnectionFactory._instance;
					break;
				}
				case "C3P0":
				{
					_instance = C3P0ConnectionFactory._instance;
					break;
				}
				case "BoneCP":
				{
					_instance = BoneCPConnectionFactory._instance;
					break;
				}
				
			}
			
			_log.log(Level.INFO, "Using connection pool: " + _instance.getClass().getSimpleName().replace("ConnectionFactory", ""));
		}
	}
}