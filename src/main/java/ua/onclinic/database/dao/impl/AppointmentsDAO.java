package ua.onclinic.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ua.onclinic.database.dao.AbstractModelDAO;
import ua.onclinic.database.pool.ConnectionFactory;
import ua.onclinic.model.instance.AppointmentInstance;

public class AppointmentsDAO implements AbstractModelDAO<AppointmentInstance>
{
	private static final String SELECT = "SELECT * FROM Appointment_history";
	private static final String INSERT = "INSERT INTO Appointment_history () VALUES ()";
	private static final String UPDATE = "UPDATE Appointment_history SET WHERE id=?";
	private static final String DELETE = "DELETE FROM Appointment_history WHERE id=?";
	
	// Нельзя создать несколько екземпляров
	private AppointmentsDAO(){}
	
	@Override
	public List<AppointmentInstance> getAll()
	{
		List<AppointmentInstance> appointments = new ArrayList<>();
		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					appointments.add(new AppointmentInstance());
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not load appointments from DB", e);
		}
		
		return appointments;
	}
	
	@Override
	public boolean insert(AppointmentInstance appointment)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT))
		{
//			ps.setInt(1, appointment.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
//			_log.log(Level.SEVERE, "Could not insert appointment into DB " + appointment.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean update(AppointmentInstance appointment)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE))
		{
//			ps.setInt(1, appointment.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
//			_log.log(Level.SEVERE, "Could not update appointment in DB " + appointment.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean delete(AppointmentInstance appointment)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(DELETE))
		{
//			ps.setInt(1, appointment.getId());
			ps.execute();
		}
		catch (Exception e)
		{
//			_log.log(Level.SEVERE, "Could not remove appointment from DB " + appointment.getId(), e);
			return false;
		}
		
		return true;
	}
	
	public static final AppointmentsDAO getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static final class SingletonHolder
	{
		private static final AppointmentsDAO _instance = new AppointmentsDAO();
	}
}