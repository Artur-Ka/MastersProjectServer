package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;

public class AuthorizationFail extends ServerPacket
{
	public enum Reason
	{
		USER_NOT_FOUND,
		WRONG_PASSWORD,
		USER_ALREADY_EXIST
	}
	
	private final Reason _reason;
	
	public AuthorizationFail(Reason reason)
	{
		_reason = reason;
	}
	
	@Override
	public int getId()
	{
		return AUTHORIZATION_FAIL;
	}
	
	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		dos.writeInt(_reason.ordinal());
	}
}