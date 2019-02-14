package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;

import ua.onclinic.model.enums.AccessLevel;

public class AuthorizationOk extends ServerPacket
{
	public static final int USER = 0;
	public static final int ADMIN = 1;
	
	private final String _login;
	private final AccessLevel _accessLevel;
	
	public AuthorizationOk(String login, AccessLevel accessLevel)
	{
		_login = login;
		_accessLevel = accessLevel;
	}
	
	@Override
	public int getId()
	{
		return AUTHORIZATION_OK;
	}
	
	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		dos.writeUTF(_login);
		dos.writeInt(_accessLevel.ordinal());
	}
}