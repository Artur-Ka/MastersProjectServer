package ua.onclinic.model.instance;

import ua.onclinic.model.AbstractInstance;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 22 квіт. 2017 р.
 */
public class PatientInstance extends AbstractInstance
{
	private String _dateOfBirth;
	private String _sex;
	private String _phone;
	private String _addPhone;
	private String _mail;
	private String _country;
	private String _city;
	private String _address;
	private String _note;
	
	
	public String getDateOfBirth()
	{
		return _dateOfBirth;
	}
	
	public String getSex()
	{
		return _sex;
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
	
	public String getCountry()
	{
		return _country;
	}
	
	public String getCity()
	{
		return _city;
	}
	
	public String getAddress()
	{
		return _address;
	}
	
	public String getNote()
	{
		return _note;
	}
	
	public void setDateOfBirth(String dateOfBirth)
	{
		_dateOfBirth = dateOfBirth;
	}
	
	public void setSex(String sex)
	{
		_sex = sex;
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
	
	public void setCountry(String country)
	{
		_country = country;
	}
	
	public void setCity(String city)
	{
		_city = city;
	}
	
	public void setAddress(String address)
	{
		_address = address;
	}
	
	public void setNote(String note)
	{
		_note = note;
	}
}