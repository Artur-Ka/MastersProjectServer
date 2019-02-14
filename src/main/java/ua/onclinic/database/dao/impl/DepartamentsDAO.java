package ua.onclinic.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ua.onclinic.database.dao.AbstractModelDAO;
import ua.onclinic.database.pool.ConnectionFactory;
import ua.onclinic.instancemanager.impl.EmployeesManager;
import ua.onclinic.model.Departament;
import ua.onclinic.model.instance.EmployeeInstance;

public class DepartamentsDAO implements AbstractModelDAO<Departament>
{
	private static final String SELECT = "SELECT id,name,employees FROM Departaments";
	private static final String INSERT = "INSERT INTO Departaments (id,name,employees) VALUES (?,?,?)";
	private static final String UPDATE = "UPDATE Departaments SET name=?,employees=? WHERE id=?";
	private static final String DELETE = "DELETE FROM Departaments WHERE id=?";
	
	// Нельзя создать несколько екземпляров
	private DepartamentsDAO(){}
	
	@Override
	public synchronized List<Departament> getAll()
	{
		final List<Departament> deps = new ArrayList<>();
		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				Departament dep;
				
				while (rs.next())
				{
					dep = new Departament();
					dep.setId(rs.getInt("id"));
					dep.setName(rs.getString("name"));
					
					final String employees = rs.getString("employees");
					if (employees != null && !employees.equals(""))
					{
						final String[] emplIds = employees.split(";");
						for (String s : emplIds)
						{
							dep.addEmployee(EmployeesManager.getInstance().getById(Integer.parseInt(s)));
						}
					}
					
					deps.add(dep);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not load departaments from DB", e);
			e.printStackTrace();
		}
		
		return deps;
	}
	
	@Override
	public synchronized boolean insert(Departament dep)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT))
		{
			ps.setInt(1, dep.getId());
			ps.setString(2, dep.getName());
			
			final StringBuilder sb = new StringBuilder();
			for (EmployeeInstance empl : dep.getEmployees())
			{
				sb.append(empl.getId());
				sb.append(";");
			}
			ps.setString(3, sb.toString());
			
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not insert departament into DB " + dep.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean update(Departament dep)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE))
		{
			ps.setString(1, dep.getName());
			
			final StringBuilder sb = new StringBuilder();
			for (EmployeeInstance empl : dep.getEmployees())
			{
				sb.append(empl.getId());
				sb.append(";");
			}
			ps.setString(2, sb.toString());
			
			ps.setInt(3, dep.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not update departament in DB " + dep.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean delete(Departament dep)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(DELETE))
		{
			ps.setInt(1, dep.getId());
			ps.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not remove departament from DB " + dep.getId(), e);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static final DepartamentsDAO getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static final class SingletonHolder
	{
		private static final DepartamentsDAO _instance = new DepartamentsDAO();
	}
}