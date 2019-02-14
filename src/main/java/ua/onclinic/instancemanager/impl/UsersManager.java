package ua.onclinic.instancemanager.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ua.onclinic.database.dao.impl.UsersDAO;
import ua.onclinic.instancemanager.IInstanceManager;
import ua.onclinic.model.instance.UserInstance;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 20 трав. 2017 р.
 */
public class UsersManager implements IInstanceManager<UserInstance>
{
	private final CopyOnWriteArrayList<UserInstance> _users = new CopyOnWriteArrayList<>();
	
	private UsersManager()
	{
		_users.addAll(UsersDAO.getInstance().getAll());
		_log.info("Loaded " + _users.size() + " users.");
	}
	
	@Override
	public List<UserInstance> getAll()
	{
		return _users;
	}
	
	@Override
	public UserInstance getById(int id)
	{
		for (UserInstance user : _users)
		{
			if (user.getId() == id)
				return user;
		}
		
		return null;
	}
	
	public UserInstance getByLogin(String login)
	{
		for (UserInstance user : _users)
		{
			if (user.getLogin().equals(login))
				return user;
		}
		
		return null;
	}
	
	@Override
	public synchronized boolean add(UserInstance t, boolean addToDb)
	{
		return addToDb ? _users.add(t) && UsersDAO.getInstance().insert(t) : _users.add(t);
	}
	
	@Override
	public synchronized boolean update(UserInstance t, boolean updateInDb)
	{
		boolean isRemoved = false;
		
		for (UserInstance user : _users)
		{
			if (user.getId() == t.getId())
				isRemoved = _users.remove(user);
		}
		
		if (!isRemoved)
			return false;
		
		return updateInDb ? _users.add(t) && UsersDAO.getInstance().update(t) : _users.add(t);
	}
	
	@Override
	public synchronized boolean delete(UserInstance t, boolean deleteFromBd)
	{
		return deleteFromBd ? _users.remove(t) && UsersDAO.getInstance().delete(t) : _users.remove(t);
	}
	
	@Override
	public synchronized boolean synchronize(List<UserInstance> users, boolean synchronizeDb)
	{
		for (UserInstance user : _users)
		{
			if (!delete(user, synchronizeDb))
				return false;
		}
		
		for (UserInstance user : users)
		{
			if (!add(user, synchronizeDb))
				return false;
		}
		
		return true;
	}
	
	public static final UsersManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		private static final UsersManager _instance = new UsersManager();
	}
}