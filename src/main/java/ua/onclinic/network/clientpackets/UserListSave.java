package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.UsersManager;
import ua.onclinic.model.enums.AccessLevel;
import ua.onclinic.model.instance.UserInstance;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.utils.IdFactory;

public class UserListSave extends ClientPacket
{
	private List<UserInstance> _users;
	
	@Override
	public int getId()
	{
		return USERLIST_SAVE;
	}

	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		_users = new ArrayList<>();
		
		while (dis.available() > 0)
		{
			final UserInstance user = new UserInstance();
			
			int id = dis.readInt();
			if (id == 0 || UsersManager.getInstance().getById(id) == null)
				id = IdFactory.getInstance().getNextId();
			
			user.setId(id);
			user.setLogin(dis.readUTF());
			user.setPassword(dis.readUTF());
			user.setName(dis.readUTF());
			
			int access = dis.readInt();
			if (access >= AccessLevel.values().length)
				access = 0;
			user.setAccessLevel(AccessLevel.values()[access]);
			
			_users.add(user);
		}
	}

	@Override
	public void runImpl(Socket socket)
	{
		UsersManager.getInstance().synchronize(_users, true);
		
		// Отправляем пустой пакет, чтобы клиент перестал ожидать ответа
		ClientThread.sendPacket(socket, new ActionFailed());
	}
}