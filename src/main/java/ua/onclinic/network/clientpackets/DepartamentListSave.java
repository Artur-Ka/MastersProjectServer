package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.DepartamentsManager;
import ua.onclinic.model.Departament;
import ua.onclinic.model.instance.EmployeeInstance;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.utils.IdFactory;

/**
 * 
 * @author A. Karpenko
 * @date 15 дек. 2018 г. 17:53:29
 */
public class DepartamentListSave extends ClientPacket
{
	private List<Departament> _departaments;
	
	@Override
	public int getId()
	{
		return DEPARTAMENTLIST_SAVE;
	}

	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		_departaments = new ArrayList<>();
		
		while (dis.available() > 0)
		{
			final Departament dep = new Departament();
			
			int id = dis.readInt();
			if (id == 0 || DepartamentsManager.getInstance().getById(id) == null)
				id = IdFactory.getInstance().getNextId();
			
			dep.setId(id);
			dep.setName(dis.readUTF());
			
			final int count = dis.readInt();
			EmployeeInstance empl;
			for (int i = 0; i < count; i++)
			{
				empl = new EmployeeInstance();
				empl.setId(dis.readInt());
				empl.setName(dis.readUTF());
				dep.addEmployee(empl);
			}
			
			_departaments.add(dep);
		}
	}

	@Override
	public void runImpl(Socket socket)
	{
		DepartamentsManager.getInstance().synchronize(_departaments, true);
		
		// Отправляем пустой пакет, чтобы клиент перестал ожидать ответа
		ClientThread.sendPacket(socket, new ActionFailed());
	}
}