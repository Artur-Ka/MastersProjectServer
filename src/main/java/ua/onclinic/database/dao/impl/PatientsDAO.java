package ua.onclinic.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ua.onclinic.database.dao.AbstractModelDAO;
import ua.onclinic.database.pool.ConnectionFactory;
import ua.onclinic.model.instance.PatientInstance;

public class PatientsDAO implements AbstractModelDAO<PatientInstance>
{
	private static final String SELECT = "SELECT id,name,dob,sex,phone,add_phone,mail,country,city,address,note FROM Patients";
	private static final String INSERT = "INSERT INTO Patients (id,name,dob,sex,phone,add_phone,mail,country,city,address,note) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE = "UPDATE Patients SET name=?,dob=?,sex=?,phone=?,add_phone=?,mail=?,country=?,city=?,address=?,note=? WHERE id=?";
	private static final String DELETE = "DELETE FROM Patients WHERE id=?";
	
	// Нельзя создать несколько екземпляров
	private PatientsDAO(){}
	
	@Override
	public synchronized List<PatientInstance> getAll()
	{
		final List<PatientInstance> patients = new ArrayList<>();
		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				PatientInstance patient;
				
				while (rs.next())
				{
					patient = new PatientInstance();
					patient.setId(rs.getInt("id"));
					patient.setName(rs.getString("name"));
					patient.setDateOfBirth(rs.getString("dob"));
					patient.setSex(rs.getString("sex"));
					patient.setPhone(rs.getString("phone"));
					patient.setAddPhone(rs.getString("add_phone"));
					patient.setMail(rs.getString("mail"));
					patient.setCountry(rs.getString("country"));
					patient.setCity(rs.getString("city"));
					patient.setAddress(rs.getString("address"));
					patient.setNote(rs.getString("note"));
					
					patients.add(patient);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not load patients from DB", e);
		}
		
		return patients;
	}
	
	@Override
	public synchronized boolean insert(PatientInstance patient)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT))
		{
			ps.setInt(1, patient.getId());
			ps.setString(2, patient.getName());
			ps.setString(3, patient.getDateOfBirth());
			ps.setString(4, patient.getSex());
			ps.setString(5, patient.getPhone());
			ps.setString(6, patient.getAddPhone());
			ps.setString(7, patient.getMail());
			ps.setString(8, patient.getCountry());
			ps.setString(9, patient.getCity());
			ps.setString(10, patient.getAddress());
			ps.setString(11, patient.getNote());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not insert patient into DB " + patient.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean update(PatientInstance patient)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE))
		{
			ps.setString(1, patient.getName());
			ps.setString(2, patient.getDateOfBirth());
			ps.setString(3, patient.getSex());
			ps.setString(4, patient.getPhone());
			ps.setString(5, patient.getAddPhone());
			ps.setString(6, patient.getMail());
			ps.setString(7, patient.getCountry());
			ps.setString(8, patient.getCity());
			ps.setString(9, patient.getAddress());
			ps.setString(10, patient.getNote());
			ps.setInt(11, patient.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not update patient in DB " + patient.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean delete(PatientInstance patient)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(DELETE))
		{
			ps.setInt(1, patient.getId());
			ps.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not remove patient from DB " + patient.getId(), e);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static final PatientsDAO getInstance()
	{
		return SingletonHolder._isntance;
	}
	
	private static final class SingletonHolder
	{
		private static final PatientsDAO _isntance = new PatientsDAO();
	}
}