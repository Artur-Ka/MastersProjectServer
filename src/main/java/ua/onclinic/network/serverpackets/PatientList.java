package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import ua.onclinic.model.instance.PatientInstance;

public class PatientList extends ServerPacket
{
	private List<PatientInstance> _patients;
	
	public PatientList(List<PatientInstance> patients)
	{
		_patients = patients;
	}
	
	@Override
	public int getId()
	{
		return PATIENT_LIST;
	}

	@Override
	public void writeImpl(DataOutputStream dos) throws IOException
	{
		for (PatientInstance pat : _patients)
		{
			dos.writeInt(pat.getId());
			dos.writeUTF(pat.getName());
			dos.writeUTF(pat.getDateOfBirth());
			dos.writeUTF(pat.getSex());
			dos.writeUTF(pat.getPhone());
			dos.writeUTF(pat.getAddPhone());
			dos.writeUTF(pat.getMail());
			dos.writeUTF(pat.getCountry());
			dos.writeUTF(pat.getCity());
			dos.writeUTF(pat.getAddress());
			dos.writeUTF(pat.getNote());
		}
	}
}