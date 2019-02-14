package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.DepartamentsManager;
import ua.onclinic.model.TelnetClient;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.network.serverpackets.DepartamentList;

/**
 * 
 * @author A. Karpenko
 * @date 15 дек. 2018 г. 17:24:03
 */
public class DepartamentListRequest extends ClientPacket
{
	@Override
	public int getId()
	{
		return DEPARTAMENTLIST_REQUEST;
	}
	
	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		
	}
	
	@Override
	public void runImpl(Socket socket)
	{
		final TelnetClient client = ClientThread.getInstance().getClient(socket.getInetAddress());
		
		if (!client.isAuthorized())
		{
			ClientThread.sendPacket(socket, new ActionFailed());
			return;
		}
		
		ClientThread.sendPacket(socket, new DepartamentList(DepartamentsManager.getInstance().getAll()));
	}
}