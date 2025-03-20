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

public class NewUser  extends DbOperations{
	ProjectConstants m_objProjectConstants;
	private static CommonUtilityFunctions.MapUtility m_objMapUtility;
	
	public NewUser(Boolean bAccessRoot, String dbName) throws ClassNotFoundException 
	{
		super(bAccessRoot, dbName);
		m_objProjectConstants = new ProjectConstants();
		m_objMapUtility = new MapUtility();
	}
	public static  HashMap<Integer, Map<String, Object>> InsertLoginDetails(String sEmail,String sPassword, String sAccessCode, String sName, String sFBCode, String sMobileNo)
	{
		long lLoginID = 0;
		String sLoginName ="";
		String sMemberID = "";
		String sMessage = "";
		long lSuccessCode = 0;
		long lErrorCode = 0;
		long lMappedSocietyCode = 0;
		String sMappedSocietyname = "";
		long lCount = 0;
		//Check for login details
		String sSqlLogin = "SELECT count(member_id) as cnt from login where member_id  ='" + sEmail +"'";
		//System.out.println("qry:"+sSqlLogin);
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mLogin = Select(sSqlLogin);
		//System.out.println(mLogin);
		try
		{
			if(sAccessCode.length() == 4)
			{
				sAccessCode = sEmail + sAccessCode;
				//System.out.println("new code:"+sAccessCode);
			}
			for(Entry<Integer, Map<String, Object>> entry2 : mLogin.entrySet()) 
			{
				lCount = (long) entry2.getValue().get("cnt");
				//System.out.println("lCount:"+lCount);
			}
			if(lCount > 0)
			{
				sMessage = "Login Already Exist. Please sign in";
				//System.out.println(sMessage);
			}
			else
			{
				String sqlMap = "Select maptbl.id,maptbl.society_id,maptbl.`desc`,unit_id,societytbl.society_name,maptbl.`status` from mapping as maptbl JOIN `society` as societytbl on maptbl.society_id = societytbl.society_id where code = '" + sAccessCode + "' ";
				//System.out.println("sql3:"+sqlMap);
				HashMap<Integer, Map<String, Object>> mMapping = Select(sqlMap);
				//System.out.println("mappings found:"+ mMapping);
				if(mMapping.size() > 0)
				{
					//System.out.println("mappings found:"+ mMapping);
					int lMappingID = 0;
					int lSocietyID = 0;
					String sSocietyName = "";
					int iStatus = 0;
					for(Entry<Integer, Map<String, Object>> entry2 : mMapping.entrySet()) 
					{
						//  System.out.println(entry2.getKey()+": "+entry2.getValue());
						  lMappingID = (int) entry2.getValue().get("id");
						  lSocietyID = (int) entry2.getValue().get("society_id");
						  sSocietyName = entry2.getValue().get("society_name").toString();
						  
						  iStatus = (int)entry2.getValue().get("status");
					}
					if(iStatus == 1)
					{
						int iSocietyID = 0;
						String sql = "INSERT INTO `login`(`member_id`,`source`,`society_id`, `password`, `name`, `fbcode`,`mobile_number`,`com_id`,`authority`) VALUES ('" + sEmail + "', 1,'"+ iSocietyID +"','" + sPassword + "', '" + sName + "', '" + sFBCode + "', '" +  sMobileNo + "','0','')";
						//System.out.println("sql2:"+sql);
						
						long lNewLoginID = Insert(sql);
						//System.out.println("New Login ID:"+ lNewLoginID);
						if(lNewLoginID > 0)
						{
							lLoginID = lNewLoginID;
							
							String sqlUpdateMap = "Update mapping SET login_id = '" + lNewLoginID + "', status = 2 where id = '" + lMappingID +"'";
							//System.out.println("sql4:"+sqlUpdateMap);
							long lMapID = Update(sqlUpdateMap);
							if(lMapID > 0)
							{
								sMessage = "Login Created and Mapping upadated for society " + sSocietyName;
								//System.out.println("mapping done:"+ sMessage);
								lSuccessCode = 2;
								lErrorCode = 0;
								lMappedSocietyCode = lSocietyID;
								sMappedSocietyname = sSocietyName;
							}
							else
							{
								sMessage = "Login Created but unable to connect society : " + sSocietyName;
								//System.out.println("mapping done:"+ sMessage);
								lSuccessCode = 1;
								lErrorCode = 1;
							}
						}
						else
						{
							sMessage = "Unable to create Login. Please try again";
							//System.out.println("mapping done:"+ sMessage);
							lErrorCode = 2;
						}
					}
					else if(iStatus == 2)
					{
						sMessage = "Access Code already in use.";
						//System.out.println("No mapping found."+ sMessage);
						lErrorCode = 3;
					}
				}
				else
				{
					sMessage = "Invalid access code";
					//System.out.println("No mapping found."+ sMessage);
					lErrorCode = 4;
				}
			}
		}
		catch(Exception ex)
		{
			sMessage = "unexpected Error while adding new user";
			System.out.println("Error."+ sMessage);
			lErrorCode = -1;
		}
		 rows.put("success", lSuccessCode);
		 rows2.put("loginID", lLoginID);
		 rows.put("ErrorCode", lErrorCode);
		 rows.put("message", sMessage);
		 rows.put("SocietyID", lMappedSocietyCode);
		 rows.put("SocietyName", sMappedSocietyname);
		 rows.put("version", DbConstants.APP_VERSION);
		 rows.put("app_link", "https://way2society.com");
		 rows.put("response",rows2);
		return rows;
	}
	public static void main(String[] args) throws Exception
	{
		String test;
		NewUser obj = new NewUser(true, "hostmjbt_societydb");
		//HashMap rows = obj.InsertLoginDetails("rohit112243@gmail.com", "111", "56f2", "Rohit", "", "");
		HashMap rows = obj.InsertLoginDetails("sujitprajapati0304@yahoo.com", "111", "eb65", "Rohit", "", "");
		//HashMap rows = obj.refreshToken("0cEbYQ-ICO2-LhD6WiuUtIth44jGRtz8CtGdfCvGzpF_Wf40wUViI8CXOYnxvkPCDTIIR2uKaw_-W3KoqRBwGa5iTlnfytY88wRvb8Gf36ZYXClgWRBk299mAuLuq17J");
		//MapUtility objComm = new MapUtility(true, "hostmjbt_societydb");
		//test = objComm.convert(rows);
		System.out.println(rows);
		//System.out.println(test);
	}
}
