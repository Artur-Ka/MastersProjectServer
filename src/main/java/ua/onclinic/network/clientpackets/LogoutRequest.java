package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import ua.onclinic.ClientThread;
import ua.onclinic.model.TelnetClient;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.network.serverpackets.LogoutAnswer;

public class LogoutRequest extends ClientPacket
{
	@Override
	public int getId()
	{
		return LOGOUT_REQUEST;
	}

	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		
	}

	@Override
	public void runImpl(Socket socket)
	{
		TelnetClient client = ClientThread.getInstance().getClient(socket.getInetAddress());
		
		if (client == null)
			return;
		
		if (!client.isAuthorized())
			ClientThread.sendPacket(socket, new ActionFailed());
		
		if (client.logout())
			ClientThread.sendPacket(socket, new LogoutAnswer());
	}
}