package ua.onclinic.database.dao;

import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 11 квіт. 2017 р.
 */
public interface AbstractModelDAO<T>
{
	static final Logger _log = Logger.getLogger(AbstractModelDAO.class.getName());
	
	List<T> getAll();
	
	boolean insert(T t);
	
	boolean update(T t);
	
	boolean delete(T t);
}