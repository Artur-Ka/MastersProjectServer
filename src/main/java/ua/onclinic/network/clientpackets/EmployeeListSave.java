package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.EmployeesManager;
import ua.onclinic.instancemanager.impl.PatientsManager;
import ua.onclinic.model.enums.EmployeeType;
import ua.onclinic.model.enums.Post;
import ua.onclinic.model.instance.EmployeeInstance;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.utils.IdFactory;

public class EmployeeListSave extends ClientPacket
{
	private List<EmployeeInstance> _employees;
	
	@Override
	public int getId()
	{
		return EMPLOYEELIST_SAVE;
	}

	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		_employees = new ArrayList<>();
		
		while (dis.available() > 0)
		{
			final EmployeeInstance empl = new EmployeeInstance();
			
			int id = dis.readInt();
			if (id == 0 || PatientsManager.getInstance().getById(id) == null)
				id = IdFactory.getInstance().getNextId();
			
			empl.setId(id);
			
			final String name = dis.readUTF();
			if (name == null || name.equals(""))
				continue;
			
			empl.setName(name);
			
			empl.setDateOfBirth(dis.readUTF());
			empl.setPhone(dis.readUTF());
			empl.setAddPhone(dis.readUTF());
			empl.setMail(dis.readUTF());
			empl.setAddress(dis.readUTF());
			empl.setEmployeeType(EmployeeType.values()[dis.readInt()]);
			empl.setPost(Post.values()[dis.readInt()]);
			empl.setSchedule(dis.readUTF());
			empl.setNote(dis.readUTF());
			
			_employees.add(empl);
		}
	}

	@Override
	public void runImpl(Socket socket)
	{
		EmployeesManager.getInstance().synchronize(_employees, true);
		
		// Отправляем пустой пакет, чтобы клиент перестал ожидать ответа
		ClientThread.sendPacket(socket, new ActionFailed());
	}
}