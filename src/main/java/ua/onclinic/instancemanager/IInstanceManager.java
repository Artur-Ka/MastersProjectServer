package ua.onclinic.instancemanager;

import java.util.List;
import java.util.logging.Logger;

public interface IInstanceManager<T>
{
	static final Logger _log = Logger.getLogger(IInstanceManager.class.getName());
	
	public List<T> getAll();
	
	public T getById(int id);
	
	boolean add(T t, boolean addToDb);
	
	boolean update(T t, boolean updateInDb);
	
	boolean delete(T t, boolean deleteFromDb);
	
	boolean synchronize(List<T> t, boolean synchronizeDb);
}