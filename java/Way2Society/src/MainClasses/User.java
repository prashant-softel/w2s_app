package MainClasses;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class User
{
	static DbOperations m_dbConn;
	static Utility m_Utility;
	
	public User(DbOperations m_dbConnObj) throws ClassNotFoundException
	{
		m_dbConn = m_dbConnObj;
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	
	public static int mAddUser() throws SQLException
	{
		return 0;
	}
	  	
	public static int mUpdateUser() throws SQLException
	{
		return 0;
	}
	
	}
