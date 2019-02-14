package ua.onclinic.network.clientpackets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 11 квіт. 2017 р.
 */
public abstract class ClientPacket
{
	private static final Map<Integer, Class<? extends ClientPacket>> _packets = new HashMap<>();
	
	protected static final int AUTHORIZATION_REQUEST = 0x01;
	protected static final int LOGOUT_REQUEST = 0x04;
	protected static final int CLOSE_CONNECTION = 0x06;
	
	protected static final int USERLIST_REQUEST = 0x0a;
	protected static final int USERLIST_SAVE = 0x0c;
	protected static final int DEPARTAMENT_ADD_REQUEST = 0x0d;
	protected static final int DEPARTAMENT_REMOVE_REQUEST = 0x0e;
	protected static final int DEPARTAMENTLIST_REQUEST = 0x0f;
	protected static final int DEPARTAMENTLIST_SAVE = 0x11;
	protected static final int EMPLOYEE_ADD_REQUEST = 0x12;
	protected static final int EMPLOYEE_REMOVE_REQUEST = 0x13;
	protected static final int EMPLOYEELIST_REQUEST = 0x014;
	protected static final int EMPLOYEELIST_SAVE = 0x16;
	protected static final int PATIENT_ADD_REQUEST = 0x17;
	protected static final int PATIENT_REMOVE_REQUEST = 0x18;
	protected static final int PATIENTLIST_REQUEST = 0x19;
	protected static final int PATIENTLIST_SAVE = 0x1b;
	
	static
	{
		_packets.put(AUTHORIZATION_REQUEST, AuthorizationRequest.class);
		_packets.put(LOGOUT_REQUEST, LogoutRequest.class);
		_packets.put(CLOSE_CONNECTION, CloseConnection.class);
		_packets.put(USERLIST_REQUEST, UserListRequest.class);
		_packets.put(USERLIST_SAVE, UserListSave.class);
		_packets.put(DEPARTAMENT_ADD_REQUEST, DepartamentAddRequest.class);
		_packets.put(DEPARTAMENTLIST_REQUEST, DepartamentListRequest.class);
		_packets.put(DEPARTAMENTLIST_SAVE, DepartamentListSave.class);
		_packets.put(EMPLOYEELIST_REQUEST, EmployeeListRequest.class);
		_packets.put(EMPLOYEELIST_SAVE, EmployeeListSave.class);
		_packets.put(PATIENTLIST_REQUEST, PatientListRequest.class);
		_packets.put(PATIENTLIST_SAVE, PatientListSave.class);
	}
	
	public static ClientPacket getPacket(int id) throws InstantiationException, IllegalAccessException
	{
		Class<? extends ClientPacket> packet = _packets.get(id);
		
		return packet != null ? packet.newInstance() : null;
	}
	
	public abstract int getId();
	
	public abstract void readImpl(DataInputStream dis) throws IOException;
	
	public abstract void runImpl(Socket socket);
}