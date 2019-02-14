package ua.onclinic.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import ua.onclinic.database.pool.ConnectionFactory;

public class IdFactory
{
	private static final Logger _log = Logger.getLogger(IdFactory.class.getSimpleName());
	
	private static final int PATIENT_ID_LENGTH = 10;
	private static final int FIRST_ID = 0x10000000;
	
	private static final CopyOnWriteArrayList<Integer> EXISTING_IDS = new CopyOnWriteArrayList<>();
	private static final CopyOnWriteArrayList<Integer> HOLE_IDS = new CopyOnWriteArrayList<>();
	
	private static AtomicInteger A_INT;
	private static int NEXT_ID;
	
	private static final String[][] ID_EXTRACTS =
	{
			{"users","id"},
			{"departaments","id"},
			{"employees","id"},
			{"patients","id"}
	};

	public IdFactory()
	{
		try
		{
			final Integer[] tmpIds = extractUsedObjectIDTable();
			
			int curId;
			int tmpId = FIRST_ID;
			for (int i = 0; i < tmpIds.length; i++)
			{
				curId = tmpIds[i];
				if (curId - tmpId > 1)
				{
					for (int j = ++tmpId; j < curId; j++)
					{
						HOLE_IDS.add(j);
					}
				}
				
				EXISTING_IDS.add(curId);
				
				tmpId = curId;
			}
			
			A_INT = new AtomicInteger((EXISTING_IDS.get(EXISTING_IDS.size() - 1)));
			
			Collections.sort(EXISTING_IDS);
			
			if (!HOLE_IDS.isEmpty())
			{
				Collections.sort(HOLE_IDS);
				NEXT_ID = HOLE_IDS.get(0);
				HOLE_IDS.remove(0);
			}
			else
			{
				NEXT_ID = A_INT.incrementAndGet();
			}
			
			_log.info("Count of vacant IDs in list: " + HOLE_IDS.size());
			_log.info("Next usable Object ID is: " + NEXT_ID);
		}
		catch (Exception e)
		{
			_log.severe(getClass().getSimpleName() + ": Could not be initialized properly:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public synchronized int getNextId()
	{
		final int tmpId = NEXT_ID;
		
		if (!HOLE_IDS.isEmpty())
		{
			NEXT_ID = HOLE_IDS.get(0);
			HOLE_IDS.remove(0);
		}
		else
		{
			NEXT_ID = A_INT.incrementAndGet();
		}
		
		return tmpId;
	}
	
	public synchronized int getNextPatientId()
	{
		return 0;
	}
	
	/**
	 * Возвращает номер карты пациента, приведенный к заданной длинне.<BR><BR>
	 * @param id
	 * @return
	 */
	public static String getFormattedPatientId(int id)
	{
		String sid = String.valueOf(id);
		
		if (sid.length() == PATIENT_ID_LENGTH)
			return sid;
		
		while (sid.length() < PATIENT_ID_LENGTH)
		{
			sid = "0".concat(sid);
		}
		
		return sid;
	}
	
	private final Integer[] extractUsedObjectIDTable() throws Exception
	{
		final List<Integer> temp = new ArrayList<>();
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			 Statement s = con.createStatement())
		{

			String extractUsedObjectIdsQuery = "";

			for (String[] tblClmn : ID_EXTRACTS)
			{
				extractUsedObjectIdsQuery += "SELECT " + tblClmn[1] + " FROM " + tblClmn[0] + " UNION ";
			}

			extractUsedObjectIdsQuery = extractUsedObjectIdsQuery.substring(0, extractUsedObjectIdsQuery.length() - 7); // Remove the last " UNION "
			try (ResultSet rs = s.executeQuery(extractUsedObjectIdsQuery))
			{
				while (rs.next())
				{
					temp.add(rs.getInt(1));
				}
			}
		}
		Collections.sort(temp);

		return temp.toArray(new Integer[temp.size()]);
	}
	
	public static final IdFactory getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static final class SingletonHolder
	{
		private static final IdFactory _instance = new IdFactory();
	}
	
	public static void main(String[] args)
	{
		System.out.println(IdFactory.getInstance().getNextId());
	}
}