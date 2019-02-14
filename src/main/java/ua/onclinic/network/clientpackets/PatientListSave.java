package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ua.onclinic.ClientThread;
import ua.onclinic.instancemanager.impl.PatientsManager;
import ua.onclinic.model.instance.PatientInstance;
import ua.onclinic.network.serverpackets.ActionFailed;
import ua.onclinic.utils.IdFactory;

public class PatientListSave extends ClientPacket
{
	private List<PatientInstance> _patients;
	
	@Override
	public int getId()
	{
		return PATIENTLIST_SAVE;
	}

	@Override
	public void readImpl(DataInputStream dis) throws IOException
	{
		_patients = new ArrayList<>();
		
		while (dis.available() > 0)
		{
			final PatientInstance patient = new PatientInstance();
			
			int id = dis.readInt();
			if (id == 0 || PatientsManager.getInstance().getById(id) == null)
				id = IdFactory.getInstance().getNextId();
			
			patient.setId(id);
			
			final String name = dis.readUTF();
			if (name == null || name.equals(""))
				continue;
			
			patient.setName(name);
			
			patient.setDateOfBirth(dis.readUTF());
			patient.setSex(dis.readUTF());
			patient.setPhone(dis.readUTF());
			patient.setAddPhone(dis.readUTF());
			patient.setMail(dis.readUTF());
			patient.setCountry(dis.readUTF());
			patient.setCity(dis.readUTF());
			patient.setAddress(dis.readUTF());
			patient.setNote(dis.readUTF());
			
			_patients.add(patient);
		}
	}

	@Override
	public void runImpl(Socket socket)
	{
		PatientsManager.getInstance().synchronize(_patients, true);
		
		// Отправляем пустой пакет, чтобы клиент перестал ожидать ответа
		ClientThread.sendPacket(socket, new ActionFailed());
	}
}