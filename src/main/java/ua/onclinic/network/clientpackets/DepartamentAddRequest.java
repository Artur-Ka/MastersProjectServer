package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.DepartamentsManager;
import ua.onclinic.model.Departament;
import ua.onclinic.network.serverpackets.DepartamentList;
import ua.onclinic.utils.IdFactory;

/**
 * 
 * @author A. Karpenko
 * @date 16 дек. 2018 г. 17:29:23
 */
public class DepartamentAddRequest extends ClientPacket
{
	private String _name;
	
	@Override
	public int getId()
	{
		return DEPARTAMENT_ADD_REQUEST;
	}
	
	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		_name = dis.readUTF();
	}
	
	@Override
	public void runImpl(Socket socket)
	{
		final Departament dep = new Departament();
		dep.setId(IdFactory.getInstance().getNextId());
		dep.setName(_name);
		
		DepartamentsManager.getInstance().add(dep, true);
		ClientThread.broadcastPacket(new DepartamentList(DepartamentsManager.getInstance().getAll()));
	}
}