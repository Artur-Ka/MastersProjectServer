package ua.onclinic.model;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 12 квіт. 2017 р.
 */
public abstract class AbstractInstance
{
	private int _id;
	private String _name;
	
	public int getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setId(int id)
	{
		_id = id;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
}
