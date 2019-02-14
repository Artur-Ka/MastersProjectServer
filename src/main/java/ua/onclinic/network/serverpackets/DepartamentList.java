package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import ua.onclinic.model.Departament;
import ua.onclinic.model.instance.EmployeeInstance;

/**
 * 
 * @author A. Karpenko
 * @date 15 дек. 2018 г. 17:23:53
 */
public class DepartamentList extends ServerPacket
{
	private List<Departament> _deps;
	
	public DepartamentList(List<Departament> deps)
	{
		_deps = deps;
	}
	
	@Override
	public int getId()
	{
		return DEPARTAMENT_LIST;
	}

	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		for (Departament dep : _deps)
		{
			dos.writeInt(dep.getId());
			dos.writeUTF(dep.getName());
			
			final List<EmployeeInstance> empls = dep.getEmployees();
			dos.writeInt(empls.size());
			for (EmployeeInstance empl : empls)
			{
				dos.writeInt(empl.getId());
				dos.writeUTF(empl.getName());
			}
		}
	}
}