package ua.onclinic.instancemanager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ua.onclinic.database.dao.impl.EmployeesDAO;
import ua.onclinic.instancemanager.IInstanceManager;
import ua.onclinic.model.enums.EmployeeType;
import ua.onclinic.model.instance.EmployeeInstance;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 20 трав. 2017 р.
 */
public class EmployeesManager implements IInstanceManager<EmployeeInstance>
{
	private final CopyOnWriteArrayList<EmployeeInstance> _employees = new CopyOnWriteArrayList<>();
	
	private EmployeesManager()
	{
		_employees.addAll(EmployeesDAO.getInstance().getAll());
		_log.info("Loaded " + _employees.size() + " employees of them " + EmployeesDAO.getInstance().getDoctorsCount() + " are doctors.");
	}
	
	@Override
	public List<EmployeeInstance> getAll()
	{
		return _employees;
	}
	
	@Override
	public EmployeeInstance getById(int id)
	{
		for (EmployeeInstance empl : _employees)
		{
			if (empl.getId() == id)
				return empl;
		}
		
		return null;
	}
	
	public List<EmployeeInstance> getDoctors()
	{
		List<EmployeeInstance> doctors = new ArrayList<>();
		
		for (EmployeeInstance empl : _employees)
		{
			if (empl.getEmployeeType() == EmployeeType.DOCTOR)
				doctors.add(empl);
		}
		
		return doctors;
	}
	
	@Override
	public synchronized boolean add(EmployeeInstance t, boolean addToDb)
	{
		return addToDb ? _employees.add(t) && EmployeesDAO.getInstance().insert(t) : _employees.add(t);
	}
	
	@Override
	public synchronized boolean update(EmployeeInstance t, boolean updateInDb)
	{
		boolean isRemoved = false;
		
		for (EmployeeInstance doctor : _employees)
		{
			if (doctor.getId() == t.getId())
				isRemoved = _employees.remove(doctor);
		}
		
		if (!isRemoved)
			return false;
		
		return updateInDb ? _employees.add(t) && EmployeesDAO.getInstance().update(t) : _employees.add(t);
	}
	
	@Override
	public synchronized boolean delete(EmployeeInstance t, boolean deleteFromBd)
	{
		return deleteFromBd ? _employees.remove(t) && EmployeesDAO.getInstance().delete(t) : _employees.remove(t);
	}
	
	@Override
	public synchronized boolean synchronize(List<EmployeeInstance> doctors, boolean synchronizeDb)
	{
		for (EmployeeInstance doc : _employees)
		{
			if (!delete(doc, synchronizeDb))
				return false;
		}
		
		for (EmployeeInstance doc : doctors)
		{
			if (!add(doc, synchronizeDb))
				return false;
		}
		
		return true;
	}
	
	public static final EmployeesManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		private static final EmployeesManager _instance = new EmployeesManager();
	}
}