package ua.onclinic.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ua.onclinic.ClientThread;
import ua.onclinic.Config;
import ua.onclinic.model.enums.AccessLevel;
import ua.onclinic.model.instance.UserInstance;
import ua.onclinic.network.clientpackets.ClientPacket;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.network.serverpackets.CloseClient;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 11 квіт. 2017 р.
 */
public class TelnetClient extends Thread
{
	private static final Logger _log = Logger.getLogger(TelnetClient.class.getName());
	
	private final Socket _socket;
	
	private UserInstance _user = null;
	
	public TelnetClient(Socket socket)
	{
		_socket = socket;
		start();
	}
	
	public Socket getSocket()
	{
		return _socket;
	}
	
	public UserInstance getUser()
	{
		return _user;
	}
	
	public AccessLevel getAccessLevel()
	{
		return _user != null ? _user.getAccessLevel() : AccessLevel.NONE;
	}
	
	public boolean authorize(UserInstance user)
	{
		if (_user != null)
			return false;
		
		_user = user;
		
		return true;
	}
	
	public boolean logout()
	{
		if (_user == null)
			return false;
		
		_user = null;
		
		return true;
	}
	
	public boolean isAuthorized()
	{
		return _user != null;
	}
	
	@Override
	public void run()
	{
		DataInputStream dis;
		
		while (!isInterrupted())
		{
			if (_socket.isClosed())
				continue;
			
			try
			{
				dis = new DataInputStream(_socket.getInputStream());
				
				if (dis.available() <= 0)
				{
					try
					{
						Thread.sleep(Config.THREADS_SLEEP);
					}
					catch (InterruptedException ie)
					{}
					
					continue;
				}
				
				readData(_socket, dis);
			}
			catch (IOException | InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static void readData(Socket socket, DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException
	{
		final int packetId = dis.readInt();
		
		final ClientPacket packet = ClientPacket.getPacket(packetId);
		
		if (packet == null)
		{
			_log.log(Level.WARNING, "Received unknown packet ("+packetId+") from "+ socket.getRemoteSocketAddress());
			
			ClientThread.sendPacket(socket, new ActionFailed());
			ClientThread.sendPacket(socket, new CloseClient());
			ClientThread.getInstance().invalidate(socket); // отключаем клиент, надо бы еще и банить
			return;
		}
		
		if (Config.DEBUG)
			_log.log(Level.INFO, "Received packet: "+packetId+" ("+packet.getClass().getSimpleName()+") from "+socket.getRemoteSocketAddress());
		
		packet.readImpl(dis);
		packet.runImpl(socket);
	}
}