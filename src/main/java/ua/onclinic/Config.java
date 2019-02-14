package ua.onclinic;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 14 квіт. 2017 р.
 */
public class Config
{
	private static final Logger _log = Logger.getLogger(Config.class.getName());
	
	public static final String CONFIGURATION_FILE = "./Config/Server.properties";
	
	// WEB
	public static int WEB_PORT;
	
	// Телнет
	public static String TELNET_IP;
	public static int TELNET_PORT;
	public static int TELNET_MAX_CONNECTIONS;
	
	// БД
	public static String DB_DRIVER;
	public static String DB_URL;
	public static String DB_LOGIN;
	public static String DB_PASSWORD;
	public static int DB_MAX_CONNECTIONS;
	public static String DB_CONNECTION_POOL;
	
	public static long THREADS_SLEEP;
	
	public static boolean DEBUG;
	
	static
	{
		try
		{
			final Properties serverSettings = new Properties();
			final InputStream is = new FileInputStream(new File(CONFIGURATION_FILE));
			serverSettings.load(is);
			
			WEB_PORT = Integer.parseInt(serverSettings.getProperty("WebPort", "8080"));
			TELNET_PORT = Integer.parseInt(serverSettings.getProperty("TelnetPort", "8070"));
			TELNET_MAX_CONNECTIONS = Integer.parseInt(serverSettings.getProperty("TelnetMaxConnections", "10"));
			
			DB_DRIVER = serverSettings.getProperty("Driver", "org.h2.Driver");
			DB_URL = serverSettings.getProperty("URL", "jdbc:h2:tcp://localhost/~/test");
			DB_LOGIN = serverSettings.getProperty("Login", "sa");
			DB_PASSWORD = serverSettings.getProperty("Password", "");
			DB_MAX_CONNECTIONS = Integer.parseInt(serverSettings.getProperty("MaxConnections", "100"));
			DB_CONNECTION_POOL = serverSettings.getProperty("ConnectionPool", "C3P0");
			
			THREADS_SLEEP = Long.parseLong(serverSettings.getProperty("ThreadsSleep", "100"));
			DEBUG = Boolean.parseBoolean(serverSettings.getProperty("Debug", "false"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			_log.log(Level.SEVERE, "Failed to Load " + CONFIGURATION_FILE + " File.", e);
		}
	}
}