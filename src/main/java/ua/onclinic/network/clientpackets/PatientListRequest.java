package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.PatientsManager;
import ua.onclinic.model.TelnetClient;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.network.serverpackets.PatientList;

public class PatientListRequest extends ClientPacket
{
	@Override
	public int getId()
	{
		return PATIENTLIST_REQUEST;
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
		
		ClientThread.sendPacket(socket, new PatientList(PatientsManager.getInstance().getAll()));
	}
}