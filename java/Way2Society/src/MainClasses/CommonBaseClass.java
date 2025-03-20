package MainClasses;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import MainClasses.DbOperations;
import MainClasses.ProjectConstants;
import MainClasses.TokenAuthentication;

public class CommonBaseClass 
{
	static ProjectConstants m_objProjectConstants;
	static DbOperations m_objDbOperations;
	static DbRootOperations m_objDbRootOperations;
	static TokenAuthentication m_objTokenAuthentication;
	private static boolean m_bIsTokenValid = false;
	private static HashMap m_DecryptedTokenMap = new HashMap<>();

	public CommonBaseClass(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		HashMap<String, String> map = new HashMap<>();
		m_objProjectConstants = new ProjectConstants();
		m_objTokenAuthentication = new TokenAuthentication();
		
		//verify token
		 map = m_objTokenAuthentication.verifyToken(sToken, bIsVerifyDbDetails, sTkey);
		 if(map.get("status") == "Valid")
		 {
			 //connect to database given tkey
			System.out.println("Context : " + map);
			 m_bIsTokenValid = true;
			 m_objDbOperations = new DbOperations(false, map.get("dbname"));
			 m_objDbRootOperations = new DbRootOperations();
		}
		 else
		 {
			 //token invalid
			 m_bIsTokenValid = false;
		 }
		 
		 m_DecryptedTokenMap = map;
	}
	
	public static boolean IsTokenValid()
	{
		//return boolean value if IsTokenValid
		return m_bIsTokenValid;
	}
	
	public static HashMap<String,String> getDecryptedTokenMap()
	{
		//return decrypted token map 
		return m_DecryptedTokenMap;
	}
	
	public static int getSocietyId()
	{
//		System.out.println("m_DecryptedTokenMap : " + m_DecryptedTokenMap);
		int retvalue = Integer.parseInt((String) getDecryptedTokenMap().get("society_id"));
		return retvalue;
	}
	
	public static int getUnitId()
	{
		
		int iRetVal = Integer.parseInt((String) m_DecryptedTokenMap.get("unit_id"));
		//System.out.println("mapping_id : " + iRetVal);
		int retvalue = Integer.parseInt((String) getDecryptedTokenMap().get("unit_id"));
		return retvalue;
		
	}
	
	public static int getLoginId()
	{
		int retvalue = Integer.parseInt((String) getDecryptedTokenMap().get("id"));
		return retvalue;
		//return 0;//Integer.parseInt(((String) getDecryptedTokenMap.get("id")));
		//map.put("unit_id",map2.get("unit_id"));
		//map.put("society_id",map2.get("society_id"));

	}

	public static String getUserID()
	{
		return (String) getDecryptedTokenMap().get("member_id");
	}
	public static String getRole()
	{
		return (String) getDecryptedTokenMap().get("role");
	}
	public static String getDBname()
	{
		return (String) getDecryptedTokenMap().get("dbname");
	}
	public static String getUserName()
	{
		return (String) getDecryptedTokenMap().get("name");
	}
	public static String getStatus()
	{
		return (String) getDecryptedTokenMap().get("status");
	}

}
