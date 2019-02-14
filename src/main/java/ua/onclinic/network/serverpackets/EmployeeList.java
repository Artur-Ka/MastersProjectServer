package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import ua.onclinic.model.instance.EmployeeInstance;

public class EmployeeList extends ServerPacket
{
	private List<EmployeeInstance> _employees;
	
	public EmployeeList(List<EmployeeInstance> employees)
	{
		_employees = employees;
	}
	
	@Override
	public int getId()
	{
		return EMPLOYEE_LIST;
	}

	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		for (EmployeeInstance empl : _employees)
		{
			dos.writeInt(empl.getId());
			dos.writeUTF(empl.getName());
			dos.writeUTF(empl.getDateOfBirth());
			dos.writeUTF(empl.getPhone());
			dos.writeUTF(empl.getAddPhone());
			dos.writeUTF(empl.getMail());
			dos.writeUTF(empl.getAddress());
			dos.writeInt(empl.getEmployeeType().ordinal());
			dos.writeInt(empl.getPost().ordinal());
			dos.writeUTF(empl.getSchedule());
			dos.writeUTF(empl.getNote());
		}
	}
}