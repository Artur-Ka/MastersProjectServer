package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import ua.onclinic.ClientThread;

public class CloseConnection extends ClientPacket
{
	@Override
	public int getId()
	{
		return CLOSE_CONNECTION;
	}
	
	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		
	}
	
	@Override
	public void runImpl(Socket socket)
	{
		ClientThread.getInstance().invalidate(socket);
	}
}