package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;

public class CloseClient extends ServerPacket
{
	@Override
	public int getId()
	{
		return CLOSE_CLIENT;
	}
	
	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		
	}
}