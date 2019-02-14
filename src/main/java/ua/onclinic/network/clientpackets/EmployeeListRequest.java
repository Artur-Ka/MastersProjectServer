package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.EmployeesManager;
import ua.onclinic.model.TelnetClient;
import ua.onclinic.model.enums.EmployeeType;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.network.serverpackets.EmployeeList;

public class EmployeeListRequest extends ClientPacket
{
	private final EmployeeType _type;
	
	public EmployeeListRequest()
	{
		_type = null;
	}
	
	public EmployeeListRequest(EmployeeType type)
	{
		_type = type;
	}
	
	@Override
	public int getId()
	{
		return EMPLOYEELIST_REQUEST;
	}
	
	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		
	}
	
	@Override
	public void runImpl(Socket socket)
	{
		final TelnetClient client = ClientThread.getInstance().getClient(socket.getInetAddress());
		
		if (!client.isAuthorized())
		{
			ClientThread.sendPacket(socket, new ActionFailed());
			return;
		}
		
		if (_type == null)
			ClientThread.sendPacket(socket, new EmployeeList(EmployeesManager.getInstance().getAll()));
		else if (_type == EmployeeType.DOCTOR)
			ClientThread.sendPacket(socket, new EmployeeList(EmployeesManager.getInstance().getDoctors()));
	}
}