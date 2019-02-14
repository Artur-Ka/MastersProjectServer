package ua.onclinic.model.instance;

import ua.onclinic.model.AbstractInstance;
import ua.onclinic.model.enums.EmployeeType;
import ua.onclinic.model.enums.Post;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 26 квіт. 2017 р.
 */
public class EmployeeInstance extends AbstractInstance
{
	private String _dateOfBirth;
	private String _phone;
	private String _addPhone;
	private String _mail;
	private String _address;
	private EmployeeType _type;
	private Post _post;
	private String _schedule;
	private String _note;
	
	
	public String getDateOfBirth()
	{
		return _dateOfBirth;
	}
	
	public String getPhone()
	{
		return _phone;
	}
	
	public String getAddPhone()
	{
		return _addPhone;
	}
	
	public String getMail()
	{
		return _mail;
	}
	
	public String getAddress()
	{
		return _address;
	}
	
	public EmployeeType getEmployeeType()
	{
		return _type;
	}
	
	public Post getPost()
	{
		return _post;
	}
	
	public String getSchedule()
	{
		return _schedule;
	}
	
	public String getNote()
	{
		return _note;
	}
	
	public void setDateOfBirth(String dateOfBirth)
	{
		_dateOfBirth = dateOfBirth;
	}
	
	public void setPhone(String phone)
	{
		_phone = phone;
	}
	
	public void setAddPhone(String addPhone)
	{
		_addPhone = addPhone;
	}
	
	public void setMail(String mail)
	{
		_mail = mail;
	}
	
	public void setAddress(String address)
	{
		_address = address;
	}
	
	public void setEmployeeType(EmployeeType type)
	{
		_type = type;
	}
	
	public void setPost(Post post)
	{
		_post = post;
	}
	
	public void setSchedule(String schedule)
	{
		_schedule = schedule;
	}
	
	public void setNote(String note)
	{
		_note = note;
	}
}