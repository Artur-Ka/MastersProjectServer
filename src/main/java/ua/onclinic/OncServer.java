package ua.onclinic;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import ua.onclinic.database.pool.ConnectionFactory;
import ua.onclinic.instancemanager.impl.DepartamentsManager;
import ua.onclinic.instancemanager.impl.EmployeesManager;
import ua.onclinic.instancemanager.impl.PatientsManager;
import ua.onclinic.instancemanager.impl.UsersManager;
import ua.onclinic.servlets.Registration;
import ua.onclinic.utils.IdFactory;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 14 квіт. 2017 р.
 */
public class OncServer
{
	private static final Logger _log = Logger.getLogger(OncServer.class.getName());
	
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		LogManager.getLogManager().readConfiguration();
		
		Server webServer = new Server(Config.WEB_PORT);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(new ServletHolder(new Registration()), "/registration");
		
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] {"index.html"});
		resourceHandler.setResourceBase("WebContent");
		
		NCSARequestLog requestLog = new NCSARequestLog(System.getProperty("user.dir") + "/request_log.log");
		requestLog.setRetainDays(3);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogTimeZone("EST");
		
		RequestLogHandler logHandler = new RequestLogHandler();
		logHandler.setRequestLog(requestLog);
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {resourceHandler, context, logHandler});
		webServer.setHandler(handlers);   		
		
		printSection("WEB Server");
		webServer.start();
		_log.info("WEB Server started on port: " + Config.WEB_PORT);
		
		printSection("Telnet Server");
		ClientThread.getInstance().start();
		_log.info("Telnet Server started on port: " + Config.TELNET_PORT);
		_log.info("Max telnet connections: " + Config.TELNET_MAX_CONNECTIONS);
		
		printSection("Database");
		ConnectionFactory.getInstance();
		
		printSection("ID Factory");
		IdFactory.getInstance();
		
		printSection("Instances");
		DepartamentsManager.getInstance();
		EmployeesManager.getInstance();
		PatientsManager.getInstance();
		UsersManager.getInstance();
		
		printSection("Server");
		_log.info("Server started in " + (System.currentTimeMillis() - startTime) + " milliseconds.");
	}
	
	public static void printSection(String s)
	{
		s = "=[ " + s + " ]";
		while (s.length() < 51)
		{
			s = "-" + s;
		}
		
		_log.info(s);
	}
}