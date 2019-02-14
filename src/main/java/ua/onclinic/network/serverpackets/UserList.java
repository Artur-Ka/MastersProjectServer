package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import ua.onclinic.model.instance.UserInstance;

public class UserList extends ServerPacket
{
	private List<UserInstance> _users;
	
	public UserList(List<UserInstance> users)
	{
		_users = users;
	}
	
	@Override
	public int getId()
	{
		return USER_LIST;
	}

	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		for (UserInstance user : _users)
		{
			dos.writeInt(user.getId());
			dos.writeUTF(user.getLogin());
			dos.writeUTF(user.getPassword());
			dos.writeUTF(user.getName());
			dos.writeInt(user.getAccessLevel().ordinal());
		}
	}
}