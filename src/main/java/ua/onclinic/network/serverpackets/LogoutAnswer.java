package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;

public class LogoutAnswer extends ServerPacket
{
	@Override
	public int getId()
	{
		return LOGOUT_ANSWER;
	}

	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		
	}
}