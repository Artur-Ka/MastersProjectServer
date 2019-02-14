package ua.onclinic.network.serverpackets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 
 * @author Карпенко А. В.
 *
 * @date 11 квіт. 2017 р.
 */
public abstract class ServerPacket
{
	protected static final int AUTHORIZATION_OK = 0x02;
	protected static final int AUTHORIZATION_FAIL = 0x03;
	protected static final int LOGOUT_ANSWER = 0x05;
	protected static final int ACTION_FAILED = 0x07;
	protected static final int CLOSE_CLIENT = 0x08;
	
	protected static final int USER_LIST = 0x0b;
	protected static final int DEPARTAMENT_LIST = 0x10;
	protected static final int EMPLOYEE_LIST = 0x15;
	protected static final int PATIENT_LIST = 0x1a;
	
	public abstract int getId();
	
	public abstract void writeImpl(DataOutputStream dos) throws IOException;
}