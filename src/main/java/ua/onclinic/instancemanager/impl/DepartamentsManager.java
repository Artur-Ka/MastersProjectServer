package ua.onclinic.instancemanager.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ua.onclinic.database.dao.impl.DepartamentsDAO;
import ua.onclinic.instancemanager.IInstanceManager;
import ua.onclinic.model.Departament;

/**
 * 
 * @author A. Karpenko
 * @date 15 дек. 2018 г. 17:25:20
 */
public class DepartamentsManager implements IInstanceManager<Departament>
{
	private final CopyOnWriteArrayList<Departament> _departaments = new CopyOnWriteArrayList<>();
	
	private DepartamentsManager()
	{
		_departaments.addAll(DepartamentsDAO.getInstance().getAll());
		_log.info("Loaded " + _departaments.size() + " departaments.");
	}
	
	@Override
	public List<Departament> getAll()
	{
		return _departaments;
	}
	
	@Override
	public Departament getById(int id)
	{
		for (Departament dep : _departaments)
		{
			if (dep.getId() == id)
				return dep;
		}
		
		return null;
	}
	
	public Departament getByName(String name)
	{
		for (Departament dep : _departaments)
		{
			if (dep.getName().equalsIgnoreCase(name))
				return dep;
		}
		
		return null;
	}
	
	@Override
	public synchronized boolean add(Departament t, boolean addToDb)
	{
		return addToDb ? _departaments.add(t) && DepartamentsDAO.getInstance().insert(t) : _departaments.add(t);
	}
	
	@Override
	public synchronized boolean update(Departament t, boolean updateInDb)
	{
		boolean isRemoved = false;
		
		for (Departament dep : _departaments)
		{
			if (dep.getId() == t.getId())
				isRemoved = _departaments.remove(dep);
		}
		
		if (!isRemoved)
			return false;
		
		return updateInDb ? _departaments.add(t) && DepartamentsDAO.getInstance().update(t) : _departaments.add(t);
	}
	
	@Override
	public synchronized boolean delete(Departament t, boolean deleteFromBd)
	{
		return deleteFromBd ? _departaments.remove(t) && DepartamentsDAO.getInstance().delete(t) : _departaments.remove(t);
	}
	
	@Override
	public synchronized boolean synchronize(List<Departament> departaments, boolean synchronizeDb)
	{
		for (Departament dep : _departaments)
		{
			if (!delete(dep, synchronizeDb))
				return false;
		}
		
		for (Departament dep : departaments)
		{
			if (!add(dep, synchronizeDb))
				return false;
		}
		
		return true;
	}
	
	public static final DepartamentsManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		private static final DepartamentsManager _instance = new DepartamentsManager();
	}
}