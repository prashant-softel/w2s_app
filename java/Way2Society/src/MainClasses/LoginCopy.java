package MainClasses;

import static MainClasses.ProjectConstants.*;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.codec.binary.Base64;

import com.google.common.collect.ArrayListMultimap;

import CommonUtilityFunctions.MapUtility;
import ecryptDecryptAlgos.ecryptDecrypt;

public class LoginCopy extends DbOperations
{
	ProjectConstants m_objProjectConstants;
	private static CommonUtilityFunctions.MapUtility m_objMapUtility;

	public LoginCopy(Boolean bAccessRoot, String dbName) throws ClassNotFoundException 
	{
		super(bAccessRoot, dbName);
		m_objProjectConstants = new ProjectConstants();
		m_objMapUtility = new MapUtility();
		
		// TODO Auto-generated constructor stub
	}
	
	
	public static  HashMap<Integer, Map<String, Object>> fetchLoginDetails(String sEmail,String sPassword,String sFbId)
	{
		int iLoginID = 0;
		String iLoginName="";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sSqlLogin = "SELECT * FROM `login` where `member_id` ='" + sEmail +"' and `password`='" + sPassword + "'";
		System.out.println("first:" + sSqlLogin); 
		HashMap<Integer, Map<String, Object>> mpLogin = Select(sSqlLogin);
		
		/*for (Entry<Integer, Map<String, Object>> entry : mpLogin.entrySet()) {
		    Integer key2 = entry.getKey();
		    Map<String, Object> value2 = entry.getValue();
		   
		}*/
		
		
		for(Entry<Integer, Map<String, Object>> entry2 : mpLogin.entrySet()) 
		{
			  System.out.println(entry2.getKey()+": "+entry2.getValue());
			  iLoginID = (int) entry2.getValue().get("login_id");
			  iLoginName = (String) entry2.getValue().get("name");
			  
		}
		
		System.out.println("map:" + mpLogin.size()); 
	    if(mpLogin.size() > 0)
	    {
	    	//login success
	    	String sSqlMap = "SELECT map.id as map,`society_name` as society, map.`desc`, `dbname` as tkey,`dbname` as sTimeStampAsKey,`unit_id`,`role`,societytbl.society_id as society_id  FROM `mapping` as map JOIN `society` as societytbl on societytbl.society_id = map.society_id  where societytbl.status = 'Y' and `login_id`="+ iLoginID + " order by societytbl.society_name";
			System.out.println("map:" + sSqlMap); 
			HashMap<Integer, Map<String, Object>> mpMapping = Select(sSqlMap);
			System.out.println("mpMapping size:" + mpMapping.size()); 
			// mpMapping.put("test",mpLogin.get("name"));
			//String enPath = Base64.class.getProtectionDomain().getCodeSource().getLocation().toString();
			
			Map<String, Object> encrptionMap = new HashMap<String, Object>();
		 	encrptionMap.put("name", iLoginName);
		 	encrptionMap.put("id", iLoginID);
		 	
	        
	        Calendar calendar = Calendar.getInstance();
	        Timestamp ourJavaTimestampObject = new Timestamp(System.currentTimeMillis());
	        
	        encrptionMap.put("tt",ourJavaTimestampObject);
	        String sToken = ecryptDecrypt.encrypt(ENCRYPT_SPKEY,ENCRYPT_INIT_VECTOR,encrptionMap.toString());	
	        
	        if(sToken.length() > 0)
	        {	
	        	rows2.put("token",sToken);
	        }
	        else
	        {
	        	rows2.put("token","no token");
	        }
	        
	        if(mpMapping.size() > 0 )
	        {
	        
				for(Entry<Integer, Map<String, Object>> entry : mpMapping.entrySet()) 
				{
				  //System.out.println(entry.getKey()+": "+entry.getValue());
				  //System.out.println("fetch val:" + entry.getValue().get("role"));
					Map<String, Object> tmpMap = new HashMap<String, Object>();
					tmpMap.put("tkey", entry.getValue().get("tkey"));
					tmpMap.put("unit_id", entry.getValue().get("unit_id"));
					tmpMap.put("society_id", entry.getValue().get("society_id"));
					tmpMap.put("map_id", entry.getValue().get("map_id"));
					tmpMap.put("role", entry.getValue().get("role"));
					tmpMap.put("tt", ourJavaTimestampObject);
				 	
					
					String sData = tmpMap.toString();
					SecretKeySpec sTimeStampAsKey = ecryptDecrypt.generate128BitAesKey(ourJavaTimestampObject.toString());
					String sEncryptedDbname = ecryptDecrypt.encrypt(sTimeStampAsKey,ENCRYPT_INIT_VECTOR,sData);	
					entry.getValue().put("tkey",sEncryptedDbname);
					entry.getValue().put("sTimeStampAsKey",sTimeStampAsKey.toString());
					
					entry.getValue().put("society", entry.getValue().get("society") + " - [ " + entry.getValue().get("desc") + " ]");
					
					//System.out.println(entry.getKey()+": "+entry.getValue());
					entry.getValue().remove("unit_id");
					entry.getValue().remove("desc");
					
					System.out.println(entry.getKey()+": "+entry.getValue());
					
					 
				}
				 rows.put("success","1");
				 //rows2.put("token","qwesdfsdkfs324jsjkddfgdgks");
				 rows2.put("name",iLoginName);
				 //rows2.put("url",enPath);
				 rows.put("response",rows2);
				 rows2.put("map",MapUtility.HashMaptoList(mpMapping));
	        }
	        else
	        {
	        	 rows.put("success","0");
				 rows2.put("message","No mapping Found");
				 rows2.put("map","");
				 rows.put("response",rows2);
				
	        }
		 	
	    }	
	    else
	    {
	    	rows.put("success","0");
			rows2.put("message","Invalid username or password.");
			rows2.put("map","");
			rows.put("response",rows2);
		}
	  
	    return rows;
	}
	
	
	public static  HashMap<Integer, Map<String, Object>> generateTokenMap(int iLoginID,String sLoginName)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		if(iLoginID > 0 && sLoginName != "")
	    {
	    	String sSqlMap = "SELECT map.id as map,`society_name` as society, map.`desc`, `dbname` as tkey,`dbname` as sTimeStampAsKey,`unit_id`,`role`,societytbl.society_id as society_id  FROM `mapping` as map JOIN `society` as societytbl on societytbl.society_id = map.society_id  where societytbl.status = 'Y' and `login_id`="+ iLoginID + " order by societytbl.society_name";
			System.out.println("map:" + sSqlMap); 
			HashMap<Integer, Map<String, Object>> mpMapping = Select(sSqlMap);
			System.out.println("mpMapping size:" + mpMapping.size()); 
			
			Map<String, Object> encrptionMap = new HashMap<String, Object>();
		 	encrptionMap.put("name", sLoginName);
		 	encrptionMap.put("id", iLoginID);
		 	
	        
	        Calendar calendar = Calendar.getInstance();
	        Timestamp ourJavaTimestampObject = new Timestamp(System.currentTimeMillis());
	        
	        encrptionMap.put("tt",ourJavaTimestampObject);
	        String sToken = ecryptDecrypt.encrypt(ENCRYPT_SPKEY,ENCRYPT_INIT_VECTOR,encrptionMap.toString());	
	        
	        if(sToken.length() > 0)
	        {	
	        	rows2.put("token",sToken);
	        }
	        else
	        {
	        	rows2.put("token","no token");
	        }
	        
	        if(mpMapping.size() > 0 )
	        {
	        
				for(Entry<Integer, Map<String, Object>> entry : mpMapping.entrySet()) 
				{
				  //System.out.println(entry.getKey()+": "+entry.getValue());
				  //System.out.println("fetch val:" + entry.getValue().get("role"));
					Map<String, Object> tmpMap = new HashMap<String, Object>();
					tmpMap.put("tkey", entry.getValue().get("tkey"));
					tmpMap.put("unit_id", entry.getValue().get("unit_id"));
					tmpMap.put("society_id", entry.getValue().get("society_id"));
					tmpMap.put("map_id", entry.getValue().get("map_id"));
					tmpMap.put("role", entry.getValue().get("role"));
					tmpMap.put("tt", ourJavaTimestampObject);
				 	
					
					String sData = tmpMap.toString();
					SecretKeySpec sTimeStampAsKey = ecryptDecrypt.generate128BitAesKey(ourJavaTimestampObject.toString());
					String sEncryptedDbname = ecryptDecrypt.encrypt(sTimeStampAsKey,ENCRYPT_INIT_VECTOR,sData);	
					entry.getValue().put("tkey",sEncryptedDbname);
					entry.getValue().put("sTimeStampAsKey",sTimeStampAsKey.toString());
					
					entry.getValue().put("society", entry.getValue().get("society") + " - [ " + entry.getValue().get("desc") + " ]");
					
					//System.out.println(entry.getKey()+": "+entry.getValue());
					entry.getValue().remove("unit_id");
					entry.getValue().remove("desc");
					
					System.out.println(entry.getKey()+": "+entry.getValue());
					
					 
				}
				 rows.put("success","1");
				 rows2.put("name",sLoginName);
				 rows.put("response",rows2);
				 rows2.put("map",mpMapping);
	        }
	        else
	        {
	        	 rows.put("success","0");
				 rows2.put("message","No mapping Found");
				 rows2.put("map","");
				 rows.put("response",rows2);
				
	        }
		 	
	    }	
	    else
	    {
	    	rows.put("success","0");
			rows2.put("message","Invalid username or password.");
			rows2.put("map","");
			rows.put("response",rows2);
		}
	  
	    return rows;
	}
	
	
	public static HashMap<Integer, Map<String, Object>> refreshToken(String sToken)
	{
		HashMap<String,String> map = new HashMap<>();
		HashMap rows = new HashMap<>();
		
		
		if(sToken.length() > 0 )
		{
			String decrptedKey = ecryptDecrypt.decrypt(ENCRYPT_SPKEY,ENCRYPT_INIT_VECTOR,sToken);
			map = m_objMapUtility.stringToHashMap(decrptedKey);
			
			if(map.size() > 0)
			{
				
			}
		}
		else
		{
			
		}
		
		return rows;
	}
	
	public static void main(String[] args) throws Exception
	{
		String test;
		LoginCopy obj = new LoginCopy(true, "hostmjbt_societydb");
		//HashMap rows = obj.fetchLoginDetails("sadmin","1","0");	
		HashMap rows = obj.refreshToken("7Esvz6Qd2mf0Ui_bMPFV6VVZOLkpYRsOeTc6Q_VinQrKgTpGZ6jFlFoqnGCRpGquUCV2ygVwb1587-Wca1j2qzSKOGsaKU9-sLmiBw1BNuc");
		//MapUtility objComm = new MapUtility(true, "hostmjbt_societydb");
		//test = objComm.convert(rows);
		System.out.println(rows);
		//System.out.println(test);
		
	}
	

}
