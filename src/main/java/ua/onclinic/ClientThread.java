package ua.onclinic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ua.onclinic.model.TelnetClient;
import ua.onclinic.network.serverpackets.ServerPacket;

public class ClientThread extends Thread
{
	private static final Logger _log = Logger.getLogger(ClientThread.class.getName());
	
	private ServerSocket _serverSocket;
	private final Map<InetAddress, TelnetClient> _clients = new HashMap<>();
	
	public ClientThread()
	{
		try
		{
			_serverSocket = new ServerSocket(Config.TELNET_PORT, Config.TELNET_MAX_CONNECTIONS);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Map<InetAddress, TelnetClient> getClients()
	{
		return _clients;
	}
	
	public TelnetClient getClient(InetAddress address)
	{
		return _clients.get(address);
	}
	
	public void invalidate(Socket socket)
	{
		TelnetClient client = _clients.remove(socket.getInetAddress());
		
		if (client != null)
		{
			client.interrupt();
			_log.log(Level.INFO, "Client " + socket.getRemoteSocketAddress() + " is disconnected");
		}
	}
	
	public static void sendPacket(Socket socket, ServerPacket packet)
	{
		if (socket.isClosed())
			return;
		
		try
		{
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeInt(packet.getId());
			packet.writeImpl(dos);
			dos.flush();
			
			if (Config.DEBUG)
				_log.log(Level.INFO, "Sended packet: "+packet.getId()+" (" + packet.getClass().getSimpleName()+") to "+socket.getRemoteSocketAddress());
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public static void broadcastPacket(ServerPacket packet)
	{
		for (TelnetClient client : getInstance()._clients.values())
		{
			sendPacket(client.getSocket(), packet);
		}
	}
	
	@Override
	public void run()
	{
		while (!isInterrupted())
		{
			try
			{
				Socket client = _serverSocket.accept();
				InetAddress address = client.getInetAddress();
				
				if (!_clients.containsKey(address))
				{
					_clients.put(address, new TelnetClient(client));
					_log.log(Level.INFO, "Connected new client - " + client.getRemoteSocketAddress());
				}
				else
				{
					_clients.remove(address);
					client.close();
					
					_log.log(Level.WARNING, address + ": Trying to connect already existing client!");
				}
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			try
			{
				Thread.sleep(Config.THREADS_SLEEP);
			}
			catch (InterruptedException e)
			{}
		}
	}
	
	public static final ClientThread getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static final class SingletonHolder
	{
		private static final ClientThread _instance = new ClientThread();
	}
}