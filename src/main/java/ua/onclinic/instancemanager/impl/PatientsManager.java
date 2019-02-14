package ua.onclinic.instancemanager.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ua.onclinic.database.dao.impl.PatientsDAO;
import ua.onclinic.instancemanager.IInstanceManager;
import ua.onclinic.model.instance.PatientInstance;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 20 трав. 2017 р.
 */
public class PatientsManager implements IInstanceManager<PatientInstance>
{
	private final CopyOnWriteArrayList<PatientInstance> _patients = new CopyOnWriteArrayList<>();
	
	private PatientsManager()
	{
		_patients.addAll(PatientsDAO.getInstance().getAll());
		_log.info("Loaded " + _patients.size() + " patients.");
	}
	
	@Override
	public List<PatientInstance> getAll()
	{
		return _patients;
	}
	
	@Override
	public PatientInstance getById(int id)
	{
		for (PatientInstance patient : _patients)
		{
			if (patient.getId() == id)
				return patient;
		}
		
		return null;
	}
	
	@Override
	public synchronized boolean add(PatientInstance t, boolean addToDb)
	{
		return addToDb ? _patients.add(t) && PatientsDAO.getInstance().insert(t) : _patients.add(t);
	}
	
	@Override
	public synchronized boolean update(PatientInstance t, boolean updateInDb)
	{
		boolean isRemoved = false;
		
		for (PatientInstance patient : _patients)
		{
			if (patient.getId() == t.getId())
				isRemoved = _patients.remove(patient);
		}
		
		if (!isRemoved)
			return false;
		
		return updateInDb ? _patients.add(t) && PatientsDAO.getInstance().update(t) : _patients.add(t);
	}
	
	@Override
	public synchronized boolean delete(PatientInstance t, boolean deleteFromBd)
	{
		return deleteFromBd ? _patients.remove(t) && PatientsDAO.getInstance().delete(t) : _patients.remove(t);
	}
	
	@Override
	public synchronized boolean synchronize(List<PatientInstance> patients, boolean synchronizeDb)
	{
		for (PatientInstance patient : _patients)
		{
			if (!delete(patient, synchronizeDb))
				return false;
		}
		
		for (PatientInstance patient : patients)
		{
			if (!add(patient, synchronizeDb))
				return false;
		}
		
		return true;
	}
	
	public static final PatientsManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		private static final PatientsManager _instance = new PatientsManager();
	}
}