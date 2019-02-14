package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;

public class ActionFailed extends ServerPacket
{
	@Override
	public int getId()
	{
		return ACTION_FAILED;
	}
	
	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		
	}
}