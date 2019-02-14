package ua.onclinic.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ua.onclinic.database.dao.AbstractModelDAO;
import ua.onclinic.database.pool.ConnectionFactory;
import ua.onclinic.model.enums.AccessLevel;
import ua.onclinic.model.instance.UserInstance;

public class UsersDAO implements AbstractModelDAO<UserInstance>
{
	private static final String SELECT = "SELECT id,login,password,name,access_level FROM Users";
	private static final String INSERT = "INSERT INTO Users (id,login,password,name,access_level) VALUES (?,?,?,?,?)";
	private static final String UPDATE = "UPDATE Users SET login=?,password=?,name=?,access_level=? WHERE id=?";
	private static final String DELETE = "DELETE FROM Users WHERE id=?";
	
	// Нельзя создать несколько екземпляров
	private UsersDAO(){}
	
	@Override
	public synchronized List<UserInstance> getAll()
	{
		final List<UserInstance> users = new ArrayList<>();
		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				UserInstance user;
				while (rs.next())
				{
					user = new UserInstance();
					user.setId(rs.getInt("id"));
					user.setLogin(rs.getString("login"));
					user.setPassword(rs.getString("password"));
					user.setName(rs.getString("name"));
					
					// Устанавливаем уровень доступа только если такой существует
					int access = rs.getInt("access_level");
					if (access >= AccessLevel.values().length)
					{
						access = 0;
						_log.warning("Invalid access level for user " + user.getId() + ". Set access level 0.");
					}
					
					user.setAccessLevel(AccessLevel.values()[access]);
					
					users.add(user);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not load users from DB", e);
		}
		
		return users;
	}
	
	@Override
	public synchronized boolean insert(UserInstance user)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT))
		{
			ps.setInt(1, user.getId());
			ps.setString(2, user.getLogin());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getName());
			ps.setInt(5, user.getAccessLevel().ordinal());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not insert user into DB " + user.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean update(UserInstance user)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE))
		{
			ps.setInt(1, user.getId());
			ps.setString(2, user.getLogin());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getName());
			ps.setInt(5, user.getAccessLevel().ordinal());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not update user in DB " + user.getId(), e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public synchronized boolean delete(UserInstance user)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(DELETE))
		{
			ps.setInt(1, user.getId());
			ps.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not remove user from DB " + user.getId(), e);
			return false;
		}
		
		return true;
	}
	
	public static final UsersDAO getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		private static final UsersDAO _instance = new UsersDAO();
	}
}