package MainClasses;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import static MainClasses.ProjectConstants.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import CommonUtilityFunctions.MapUtility;
import ecryptDecryptAlgos.KeyAuthentication;

public class TokenAuthentication extends KeyAuthentication
{
	/*ProjectConstants m_objProjectConstants;

	public TokenAuthentication()
	{
		m_objProjectConstants = new ProjectConstants();
	}
	*/
	static KeyAuthentication m_objKeyAuthentication;
	public TokenAuthentication()
	{
		m_objKeyAuthentication = new KeyAuthentication();
	}
	public HashMap<String,String> verifyToken(String sToken,Boolean bIsVerifyDbDetails,String sTkey)
	{
		HashMap<String,String> map = new HashMap<>();
		//
		map = m_objKeyAuthentication.ValidateToken(sToken, true, null);
		
		//System.out.print("TokenMap : " + map);
		
		if(map.size() > 0 && map.get("status") == "Valid" && bIsVerifyDbDetails && sTkey != null)
		{
			//verifyDbDetails(sTkey);
			
			HashMap<String,String> map2 = new HashMap<>();
			
			SecretKeySpec sPkey = generate128BitAesKey(map.get("tt"));
			map2 = verifyDbDetails(sTkey,sPkey);
			map.put("sPkey",sPkey.toString());
			if(map2.size() > 0 && map2.get("status") == "Valid")
			{
				map.put("dbname",map2.get("tkey"));
				map.put("unit_id",map2.get("unit_id"));
				map.put("society_id",map2.get("society_id"));
				map.put("role",map2.get("role"));
			}
//			System.out.print("\nMap2 : " + map);
		}
				
		return map;
	}
	
	
	
	/*public HashMap<String,String> refreshToken(String sToken)
	{
		HashMap<String,String> map = new HashMap<>();
		map = m_objKeyAuthentication.ValidateToken(sToken, true, null);
		if(map.size() > 0 && map.get("status") == "Valid" && bIsVerifyDbDetails && sTkey != null)
		{
			//verifyDbDetails(sTkey);
			
			HashMap<String,String> map2 = new HashMap<>();
			
			SecretKeySpec sPkey = generate128BitAesKey(map.get("tt"));
			map2 = verifyDbDetails(sTkey,sPkey);
			map.put("sPkey",sPkey.toString());
			if(map2.size() > 0 && map2.get("status") == "Valid")
			{
				map.put("dbname",map2.get("tkey"));
				map.put("unit_id",map2.get("unit_id"));
				map.put("society_id",map2.get("society_id"));
				map.put("role",map2.get("role"));
			}
		}
		
		return map;
	}*/
	
	
	
	public HashMap<String,String> verifyDbDetails(String sToken,SecretKeySpec sPkey)
	{
		HashMap<String,String> map = new HashMap<>();
		map = m_objKeyAuthentication.ValidateToken(sToken, false, sPkey);
		
		return map;
	}
	
	
	
	
}
