package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.UsersManager;
import ua.onclinic.model.TelnetClient;
import ua.onclinic.model.instance.UserInstance;
import ua.onclinic.network.serverpackets.AuthorizationFail;
import ua.onclinic.network.serverpackets.AuthorizationFail.Reason;
import ua.onclinic.network.serverpackets.AuthorizationOk;

public class AuthorizationRequest extends ClientPacket
{
	private String _login;
	private String _password;
	
	@Override
	public int getId()
	{
		return AUTHORIZATION_REQUEST;
	}
	
	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		_login = dis.readUTF();
		_password = "";
		
		while(dis.available() > 0)
		{
			_password = _password.concat(String.valueOf(dis.readChar()));
		}
	}
	
	@Override
	public void runImpl(Socket socket)
	{
		final TelnetClient client = ClientThread.getInstance().getClient(socket.getInetAddress());
		
		if (client.isAuthorized())
		{
			ClientThread.sendPacket(socket, new AuthorizationFail(Reason.USER_ALREADY_EXIST));
			return;
		}
		
		final UserInstance user = UsersManager.getInstance().getByLogin(_login);
		
		if (user == null)
		{
			ClientThread.sendPacket(socket, new AuthorizationFail(Reason.USER_NOT_FOUND));
			return;
		}
		
		if (!comparePasswords(user.getPassword(), _password))
		{
			ClientThread.sendPacket(socket, new AuthorizationFail(Reason.WRONG_PASSWORD));
			return;
		}
		
		if (client.authorize(user))
			ClientThread.sendPacket(socket, new AuthorizationOk(_login, user.getAccessLevel()));
		
		// Занулим перенные, чтобы не хранились в памяти
		_login = null;
		_password = null;
	}
	
	private static boolean comparePasswords(String pass1, String pass2)
	{
		if (pass1.length() != pass2.length())
			return false;
		
		final char[] p1 = pass1.toCharArray();
		final char[] p2 = pass2.toCharArray();
		
		for (int i = 0; i < pass1.length(); i++)
		{
			if (p1[i] != p2[i])
				return false;
		}
		
		return true;
	}
}