package ua.onclinic.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ua.onclinic.database.dao.AbstractModelDAO;
import ua.onclinic.database.pool.ConnectionFactory;
import ua.onclinic.model.enums.EmployeeType;
import ua.onclinic.model.enums.Post;
import ua.onclinic.model.instance.EmployeeInstance;

public class EmployeesDAO implements AbstractModelDAO<EmployeeInstance>
{
	private static final String SELECT = "SELECT id,name,dob,phone,add_phone,mail,address,employee_type,post,schedule,note FROM Employees";
	private static final String INSERT = "INSERT INTO Employees (id,name,dob,phone,add_phone,mail,address,employee_type,post,schedule,note) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE = "UPDATE Employees SET name=?,dob=?,phone=?,add_phone=?,mail=?,address=?,employee_type=?,post=?,schedule=?,note=? WHERE id=?";
	private static final String DELETE = "DELETE FROM Employees WHERE id=?";
	
	private int _doctorsCount = 0;
	
	// Нельзя создать несколько екземпляров
	private EmployeesDAO(){}
	
	public int getDoctorsCount()
	{
		return _doctorsCount;
	}
	
	@Override
	public synchronized List<EmployeeInstance> getAll()
	{
		final List<EmployeeInstance> employees = new ArrayList<>();
		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				EmployeeInstance empl; 
				
				while (rs.next())
				{
					empl = new EmployeeInstance();
					empl.setId(rs.getInt("id"));
					empl.setName(rs.getString("name"));
					empl.setDateOfBirth(rs.getString("dob"));
					empl.setPhone(rs.getString("phone"));
					empl.setAddPhone(rs.getString("add_phone"));
					empl.setMail(rs.getString("mail"));
					empl.setAddress(rs.getString("address"));
					empl.setEmployeeType(EmployeeType.valueOf(rs.getString("employee_type")));
					empl.setPost(Post.valueOf(rs.getString("post")));
					empl.setSchedule(rs.getString("schedule"));
					empl.setNote(rs.getString("note"));
					
					if (empl.getEmployeeType() == EmployeeType.DOCTOR)
						_doctorsCount++;
					
					employees.add(empl);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not load clinic employee from DB", e);
			e.printStackTrace();
		}
		
		return employees;
	}
	
	@Override
	public synchronized boolean insert(EmployeeInstance empl)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT))
		{
			ps.setInt(1, empl.getId());
			ps.setString(2, empl.getName());
			ps.setString(3, empl.getDateOfBirth());
			ps.setString(4, empl.getPhone());
			ps.setString(5, empl.getAddPhone());
			ps.setString(6, empl.getMail());
			ps.setString(7, empl.getAddress());
			ps.setString(8, empl.getEmployeeType().toString());
			ps.setString(9, empl.getPost().toString());
			ps.setString(10, empl.getSchedule());
			ps.setString(11, empl.getNote());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not insert clinic employee into DB " + empl.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean update(EmployeeInstance empl)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE))
		{
			ps.setString(1, empl.getName());
			ps.setString(2, empl.getDateOfBirth());
			ps.setString(3, empl.getPhone());
			ps.setString(4, empl.getAddPhone());
			ps.setString(5, empl.getMail());
			ps.setString(6, empl.getAddress());
			ps.setString(7, empl.getEmployeeType().toString());
			ps.setString(8, empl.getPost().toString());
			ps.setString(9, empl.getSchedule());
			ps.setString(10, empl.getNote());
			ps.setInt(11, empl.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			_log.log(Level.SEVERE, "Could not update clinic employee in DB " + empl.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean delete(EmployeeInstance empl)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(DELETE))
		{
			ps.setInt(1, empl.getId());
			ps.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not remove clinic employee from DB " + empl.getId(), e);
			return false;
		}
		
		return true;
	}
	
	public static final EmployeesDAO getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		private static final EmployeesDAO _instance = new EmployeesDAO();
	}
}