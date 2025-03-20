package MainClasses;

import static MainClasses.ProjectConstants.*;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.commons.codec.binary.Base64;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;

import CommonUtilityFunctions.MapUtility;
import ecryptDecryptAlgos.ecryptDecrypt;

public class Login extends DbOperations
{
	ProjectConstants m_objProjectConstants;
	private static CommonUtilityFunctions.MapUtility m_objMapUtility;
//	static DbOperations dbConn;
//	static DbOperations dbConnRoot;

	public Login(Boolean bAccessRoot, String dbName) throws ClassNotFoundException 
	{
		super(bAccessRoot, dbName);
		m_objProjectConstants = new ProjectConstants();
		m_objMapUtility = new MapUtility();
		
	}

	public static  HashMap<Integer, Map<String, Object>> fetchLoginDetails_2(String sEmail,String sPassword,String sFbId, String sDeviceID)
	{
		String deviceOS = "";
		
		return fetchLoginDetails(sEmail, sPassword, sFbId,  sDeviceID, deviceOS);
		//return fetchLoginDetails_new(sEmail, sPassword, sFbId,  sDeviceID, deviceOS);
	}
	
	public static  HashMap<Integer, Map<String, Object>> fetchLoginDetails(String sEmail,String sPassword,String sFbId, String sDeviceID, String deviceOS)
	{
		//System.out.println("Inside Login");
		int iLoginID = 0;
		String sLoginName ="";
		String sMemberID = "";
		if(deviceOS.equals("Android"))
		{
			deviceOS = "1";
		}
		else
		{
		
			deviceOS = "0";
		}
		//Check for login details
		//String sSqlLogin = "SELECT * FROM `login` where `member_id` ='" + sEmail +"' and `password`="+sPassword;
		String sSqlLogin = "SELECT * FROM `login` where `member_id` ='" + sEmail +"' and `password`='"+sPassword+"'";
		System.out.println("sSqlLogin :"+sSqlLogin);
		HashMap<Integer, Map<String, Object>> mpLogin = Select(sSqlLogin);
		//System.out.println(mpLogin);
		for(Entry<Integer, Map<String, Object>> entry2 : mpLogin.entrySet()) 
		{
			  //System.out.println(entry2.getKey()+": "+entry2.getValue());
			  iLoginID = (int) entry2.getValue().get("login_id");
			  sLoginName = (String) entry2.getValue().get("name");
			  sMemberID = (String) entry2.getValue().get("member_id");
		}
		HashMap<Integer, Map<String, Object>> rows = generateTokenMap(iLoginID,sLoginName, sMemberID);
		
		if(iLoginID>0)
		{			
			String sSelectQuery = "SELECT * FROM `device_details` where `device_id` = '" + sDeviceID + "' AND `login_id` =" + iLoginID;
			//System.out.println("Device Query <" + sSelectQuery + ">");
			HashMap<Integer, Map<String, Object>> mpDevice = Select(sSelectQuery);
			int iDeviceCount= mpDevice.size();
			//System.out.println("Device size " + iDeviceCount);
			if(iDeviceCount == 1)
			{
				//System.out.println("Device Count 1. Device already registered");
				//if(mpDevice.containsKey("device_OS"))
				{
					Map.Entry<Integer, Map<String, Object>> entryDevice = mpDevice.entrySet().iterator().next();
					String existing_device_OS = (String) entryDevice.getValue().get("device_OS");
					
					//String device_OS = (String) mpDevice.get("device_OS");
					//System.out.println("device_OS : " + existing_device_OS);
					if(deviceOS != existing_device_OS)
					{
						//Update OSType
						String sUpdateQuery = "UPDATE `device_details` Set `device_OS` = '" + deviceOS + "' where `device_id` = '" + sDeviceID + "' AND `login_id` =" + iLoginID;
						//System.out.println("Updating Device sUpdateQuery <" + sUpdateQuery  + ">");
						long iUpdateCount = Delete(sUpdateQuery);
						//System.out.println("Updated <" + iUpdateCount  + "> devices");
					}
				}
			}
			else
			{
				if(iDeviceCount > 1)
				{
					//cleanup
					String sDeleteQuery = "DELETE FROM `device_details` where `device_id` = '" + sDeviceID + "' AND `login_id` =" + iLoginID;
					//System.out.println("Deleting Device Delete Query <" + sDeleteQuery  + ">");
					long iDeleteCount = Delete(sDeleteQuery);
					//System.out.println("Deleted <" + iDeleteCount  + "> devices");
				}
				
				String sInsertQuery = "INSERT INTO `device_details`(`device_id`, `login_id`, `device_OS`) VALUES ('"+sDeviceID+"','"+iLoginID+"','"+deviceOS+"')";
				//System.out.println("Device Query <" + sInsertQuery + ">");
				long device_id = Insert(sInsertQuery);
				///System.out.println("DeviceReqID <"+device_id+">");
				if(device_id==0)
				{
					System.out.println("msg : device id : "+sDeviceID+" not inserted");
				}
				else
				{
					System.out.println("msg : device id : "+sDeviceID+" inserted" + device_id);
				}
			}
		}
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> fetchLoginDetails_new(String sEmail,String sPassword,String sFbId, String sDeviceID, String deviceOS)
	{
		//System.out.println("Inside Login");
		int iLoginID = 0;
		String sLoginName ="";
		String sMemberID = "";
		if(deviceOS.equals("Android"))
		{
			deviceOS = "1";
		}
		else
		{
		
			deviceOS = "0";
		}
		//Check for login details
		String sSqlLogin = "SELECT * FROM `login` where `member_id` ='" + sEmail +"' and `password`="+sPassword;
		//String sSqlLogin = "SELECT * FROM `login` where `member_id` ='" + sEmail +"' and `password`='"+sPassword+"'";
		System.out.println("sSqlLogin :"+sSqlLogin);
		HashMap<Integer, Map<String, Object>> mpLogin = Select(sSqlLogin);
		//System.out.println(mpLogin);
		for(Entry<Integer, Map<String, Object>> entry2 : mpLogin.entrySet()) 
		{
			  //System.out.println(entry2.getKey()+": "+entry2.getValue());
			  iLoginID = (int) entry2.getValue().get("login_id");
			  sLoginName = (String) entry2.getValue().get("name");
			  sMemberID = (String) entry2.getValue().get("member_id");
		}
		HashMap<Integer, Map<String, Object>> rows = generateTokenMap(iLoginID,sLoginName, sMemberID);
		
		if(iLoginID>0)
		{			
			String sSelectQuery = "SELECT * FROM `device_details` where `device_id` = '" + sDeviceID + "' AND `login_id` =" + iLoginID;
			//System.out.println("Device Query <" + sSelectQuery + ">");
			HashMap<Integer, Map<String, Object>> mpDevice = Select(sSelectQuery);
			int iDeviceCount= mpDevice.size();
			//System.out.println("Device size " + iDeviceCount);
			if(iDeviceCount == 1)
			{
				//System.out.println("Device Count 1. Device already registered");
				//if(mpDevice.containsKey("device_OS"))
				{
					Map.Entry<Integer, Map<String, Object>> entryDevice = mpDevice.entrySet().iterator().next();
					String existing_device_OS = (String) entryDevice.getValue().get("device_OS");
					
					//String device_OS = (String) mpDevice.get("device_OS");
					//System.out.println("device_OS : " + existing_device_OS);
					if(deviceOS != existing_device_OS)
					{
						//Update OSType
						String sUpdateQuery = "UPDATE `device_details` Set `device_OS` = '" + deviceOS + "' where `device_id` = '" + sDeviceID + "' AND `login_id` =" + iLoginID;
						//System.out.println("Updating Device sUpdateQuery <" + sUpdateQuery  + ">");
						long iUpdateCount = Delete(sUpdateQuery);
						//System.out.println("Updated <" + iUpdateCount  + "> devices");
					}
				}
			}
			else
			{
				if(iDeviceCount > 1)
				{
					//cleanup
					String sDeleteQuery = "DELETE FROM `device_details` where `device_id` = '" + sDeviceID + "' AND `login_id` =" + iLoginID;
					//System.out.println("Deleting Device Delete Query <" + sDeleteQuery  + ">");
					long iDeleteCount = Delete(sDeleteQuery);
					//System.out.println("Deleted <" + iDeleteCount  + "> devices");
				}
				
				String sInsertQuery = "INSERT INTO `device_details`(`device_id`, `login_id`, `device_OS`) VALUES ('"+sDeviceID+"','"+iLoginID+"','"+deviceOS+"')";
				//System.out.println("Device Query <" + sInsertQuery + ">");
				long device_id = Insert(sInsertQuery);
				///System.out.println("DeviceReqID <"+device_id+">");
				if(device_id==0)
				{
					System.out.println("msg : device id : "+sDeviceID+" not inserted");
				}
				else
				{
					System.out.println("msg : device id : "+sDeviceID+" inserted" + device_id);
				}
			}
		}
	    return rows;
	}

	public static  HashMap<Integer, Map<String, Object>> generateTokenMap(int iLoginID,String sLoginName, String sMemberID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		if(iLoginID > 0 && sLoginName != "")
	    {
			//add login id and name to encrptionMap
			Map<String, Object> encrptionMap = new HashMap<String, Object>();
		 	encrptionMap.put("name", sLoginName);
		 	encrptionMap.put("id", iLoginID);
		 	encrptionMap.put("member_id", sMemberID);
		 	
	        //get current timestamp
	        Calendar calendar = Calendar.getInstance();
	        Timestamp ourJavaTimestampObject = new Timestamp(System.currentTimeMillis());
	        
	        encrptionMap.put("tt",ourJavaTimestampObject);
	        
	        //generate encrypted token
	        String sToken = ecryptDecrypt.encrypt(ENCRYPT_SPKEY,ENCRYPT_INIT_VECTOR,encrptionMap.toString());	
	        //System.out.println("sToken" + sToken);
	        if(sToken.length() > 0)
	        {	
	        	rows2.put("token",sToken);
	        }
	        else
	        {
	        	rows2.put("token","no token");
	        }
	        
	       // Map<Object, Object> mpMapping1=mpMapping.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue()));
	        //System.out.println("Test" + mpMapping1);
	    	String sSqlMap = "SELECT map.id as map,`society_name` as society, map.`desc`, `dbname` as tkey,`dbname` as sTimeStampAsKey,`unit_id`,`role`,societytbl.society_id as society_id  FROM `mapping` as map JOIN `society` as societytbl on societytbl.society_id = map.society_id  where societytbl.status = 'Y' and `login_id`="+ iLoginID + " order by societytbl.society_name";
			HashMap<Integer, Map<String, Object>> mpMapping = Select(sSqlMap);			
	        if(mpMapping.size() > 0 )
	        {
	        	int i = 0;
	        	for(Entry<Integer, Map<String, Object>> entry : mpMapping.entrySet()) 
				{
	        		//System.out.println("Iteration: " + i);
	        		String unit_id = (entry.getValue().get("unit_id")).toString();
					
					//System.out.println("Unit: " + unit_id);

	        		//creating tkey map for each mapped society
					Map<String, Object> tmpMap = new HashMap<String, Object>();
					
					tmpMap.put("tkey", entry.getValue().get("tkey"));
					tmpMap.put("unit_id", entry.getValue().get("unit_id"));
					//tmpMap.put("unit_no", entry.getValue().get("decs"));
					tmpMap.put("society_id", entry.getValue().get("society_id"));
					tmpMap.put("map_id", entry.getValue().get("map_id"));
					tmpMap.put("role", entry.getValue().get("role"));
					tmpMap.put("tt", ourJavaTimestampObject);
					//tmpMap.put("isblock", 0);
					//tmpMap.put("block_reason", "");	
					//System.out.println("tmpMap" + tmpMap);
					//convert each tkey map to string  
					String sData = tmpMap.toString();
					SecretKeySpec sTimeStampAsKey = ecryptDecrypt.generate128BitAesKey(ourJavaTimestampObject.toString());
					
					//encrypt tkey using sTimeStampAsKey
					String sEncryptedDbname = ecryptDecrypt.encrypt(sTimeStampAsKey,ENCRYPT_INIT_VECTOR,sData);	
					
					//add encrypted tkey
					entry.getValue().put("tkey",sEncryptedDbname);
					entry.getValue().put("sTimeStampAsKey",sTimeStampAsKey.toString());
					//entry.getValue().put("society", entry.getValue().get("society") + " - [ " + entry.getValue().get("desc") + " ]");
					String role = entry.getValue().get("role").toString();
					if(role.equals("Admin") || role.equals("Super Admin") || role.equals("Contractor"))
					{
						//System.out.println("Admin");
						entry.getValue().put("society", entry.getValue().get("society") );
						
					}
					else
					{
						//System.out.println("non-Admin");
						entry.getValue().put("society", entry.getValue().get("society") + " - [ " + entry.getValue().get("desc") + " ]");
					}
					//System.out.println("society:"+entry.getValue().put("society", entry.getValue().get("society") ));
					//System.out.println("tkey: " + sEncryptedDbname);
					//System.out.println("Role:"+role);
					entry.getValue().put("unit_no", entry.getValue().get("desc"));
					entry.getValue().put("isblock", "0");
					entry.getValue().put("block_reason", "");
					
				}
				rows.put("success","1");
				rows2.put("name",sLoginName);
				rows.put("version", DbConstants.APP_VERSION);
				rows.put("app_link", "https://way2society.com");
				rows.put("login_id", iLoginID);
				rows.put("response",rows2);
				rows2.put("map",MapUtility.HashMaptoList (mpMapping));
	        }
	        else
	        {
	        	//mapped society found
	        	rows.put("success","0");
	        	rows.put("version", DbConstants.APP_VERSION);
	        	rows.put("app_link", "https://way2society.com");
	        	rows.put("login_id", "0");
				rows2.put("message","No mapping Found");
				rows2.put("map","");
				rows.put("response",rows2);
	        }
	    }	
	    else
	    {
	    	//invalid username or password
	    	rows.put("success","0");
	    	rows.put("version", DbConstants.APP_VERSION);
        	rows.put("app_link", "https://way2society.com");
			rows2.put("message","Invalid username or password.");
			rows2.put("map","");
			rows.put("response",rows2);
		}
	  
//		Gson objGson = new Gson();
//		System.out.println("GSON : " + objGson.toJson(rows));
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> refreshToken(String sToken)
	{
		//refreshing token process
		HashMap map = new HashMap<>();
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		if(sToken.length() > 0 )
		{
			//decryting old token 
			String decrptedKey = ecryptDecrypt.decrypt(ENCRYPT_SPKEY,ENCRYPT_INIT_VECTOR,sToken);
			map = m_objMapUtility.stringToHashMap(decrptedKey);
			
			if(map.size() > 0)
			{
				//old token decrypted and fetching login details from old token passed
				int iLoginID = 0;
				String sLoginName = "";
				iLoginID = Integer.parseInt((String)map.get("id"));
				sLoginName = (String)map.get("name");
			 	String sMemberID = (String)map.get("member_id");
			 	
				//generating new token 
				rows = generateTokenMap(iLoginID,sLoginName, sMemberID);
			}
			else
			{
				//Decoded Token Empty.
				rows.put("success","0");
				rows2.put("message","Decoded Token Empty.");
				rows2.put("map","");
				rows.put("response",rows2);
			}
		}
		else
		{
			//Given Token Is Empty.
			rows.put("success","0");
			rows2.put("message","Given Token Is Empty.");
			rows2.put("map","");
			rows.put("response",rows2);
		}
		
		return rows;
	}
	
	public static void main(String[] args) throws Exception
	{
		String test;
		Login obj = new Login(true, "hostmjbt_societydb");
		//HashMap rows = obj.fetchLoginDetails("prsh3006@yahoo.com","acmeamay123","1","0","3321.24324.21324","null");
		HashMap rows = obj.fetchLoginDetails("raji380@yahoo.co.in","107851","0","3321.24324.21324","null");
		//HashMap rows = obj.fetchLoginDetails("developer@way2society.com","dev","0","3321.24324.21324","null");
		//HashMap rows = obj.refreshToken("developer@way2society.com","dev","0","3321.24324.21324");
		//HashMap rows = obj.refreshToken("0cEbYQ-ICO2-LhD6WiuUtIth44jGRtz8CtGdfCvGzpF_Wf40wUViI8CXOYnxvkPCDTIIR2uKaw_-W3KoqRBwGa5iTlnfytY88wRvb8Gf36ZYXClgWRBk299mAuLuq17J");
		//MapUtility objComm = new MapUtility(true, "hostmjbt_societydb");
		//test = objComm.convert(rows);
		System.out.println(rows);
		//System.out.println(test);
	}
	

}
