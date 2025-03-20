package MainClasses;

import static MainClasses.ProjectConstants.ENCRYPT_INIT_VECTOR;
import static MainClasses.ProjectConstants.ENCRYPT_SPKEY;

import java.lang.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import CommonUtilityFunctions.MapUtility;
import MainClasses.Bill;
import MainClasses.Payment;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;

public class ViewServiceRequests extends CommonBaseClass 
{
	static ServiceRequest m_ServiceRequest;
	public  ViewServiceRequests(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
		m_ServiceRequest = new ServiceRequest(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);		
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchServiceContractor(int iSocietyID,int iLoginID)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpServcieRequest = m_ServiceRequest.fetchContractor(iSocietyID,iLoginID);
				 System.out.println(mpServcieRequest);
		if(mpServcieRequest.size() > 0)
		{
			 rows2.put("sr",MapUtility.HashMaptoList(mpServcieRequest));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("sr","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	
	public static  HashMap<Integer, Map<String, Object>> mfetchServiceRequests(int iUnitId, int iLoginID,int iSocietyID, boolean bAssignedToMe)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all Service Requests
		HashMap<Integer, Map<String, Object>> mpServcieRequest = m_ServiceRequest.mfetchServiceRequests(iUnitId, 5, false, bAssignedToMe, iLoginID, iSocietyID);
				 
		if(mpServcieRequest.size() > 0)
		{
			//add member to map
			 rows2.put("sr",MapUtility.HashMaptoList(mpServcieRequest));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("sr","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchServiceRequestHistory(int iSR_No)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all Service Requests
		HashMap<Integer, Map<String, Object>> mpServcieRequest = m_ServiceRequest.mfetchServiceRequestHistory(iSR_No);
		 
		if(mpServcieRequest.size() > 0)
		{
			//add member to map
			 rows2.put("sr",MapUtility.HashMaptoList(mpServcieRequest));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("sr","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
		
	public static  HashMap<Integer, Map<String, Object>> mAddServiceRequests(int iUnitID, int iSocietyID, long iMappingID, String sReportedby, String sEmail, String sPhone, String sSummary, String sStatus, int iVisibility, String sCategory, String sPriority, String sImageFileName, String sDetails)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all Service Requests
		//HashMap<Integer, Map<String, Object>> mpServcieRequest = 
		long lNewDR_ID = m_ServiceRequest.AddServiceRequest(iUnitID, iSocietyID, iMappingID, sReportedby, sEmail, sPhone, sSummary, sStatus, iVisibility, sCategory, sPriority, sImageFileName, sDetails);

		//if(mpServcieRequest.size() > 0)
		if(lNewDR_ID > 0)
		{
			//add member to map
			 rows2.put("new_sr_id",lNewDR_ID);
			 rows2.put("message","Request Created Successfully");
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("new_sr_id","");
			 rows2.put("message","Unable To Create Request. Please try again");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mUpdateServiceRequestHistory(int iUnitID, int iSocietyID, long iMappingID, int iSR_ID, String sReportedby, String sEmail, String sPhone, String sSummary, String sStatus, int iVisibility, String sCategory, String sPriority, String sNewImageFile)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all Service Requests
		long lNewDR_ID = m_ServiceRequest.UpdateServiceRequestHistory(iUnitID, iSocietyID, iMappingID, iSR_ID, sReportedby, sEmail, sPhone, sSummary, sStatus, iVisibility, sCategory, sPriority, sNewImageFile);

		//if(mpServcieRequest.size() > 0)
		if(lNewDR_ID > 0)
		{
			//add member to map
			 rows2.put("new_sr_id",lNewDR_ID);
			 rows2.put("message","Request Updated Successfully");
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("new_sr_id","");
			 rows2.put("message","Unable To Update Request. Please try again");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchSRStatusList()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all designations
		HashMap<Integer, Map<String, Object>> mpStatusList = m_ServiceRequest.GetStatusList();		
		 
		if(mpStatusList.size() > 0)
		{
			//add member to map
			 rows2.put("status",MapUtility.HashMaptoList(mpStatusList));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("status","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchSRCategory()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all designations
		HashMap<Integer, Map<String, Object>> mpStatusList = m_ServiceRequest.mfetchSRCategory();		
		 
		if(mpStatusList.size() > 0)
		{
			//add member to map
			 rows2.put("category",MapUtility.HashMaptoList(mpStatusList));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("category","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mFetchAllServiceRequest(int iSocietyId, int iFetchCount, boolean bFetchExpired,int iUnitId)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all designations
		HashMap<Integer, Map<String, Object>> mpServiceRequest = m_ServiceRequest.mFetchAllServiceRequest(iSocietyId, iFetchCount, bFetchExpired, iUnitId);		
		 
		if(mpServiceRequest.size() > 0)
		{
			//add member to map
			rows.put("success",1);
			 rows2.put("category",MapUtility.HashMaptoList(mpServiceRequest));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("category","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		
		ViewServiceRequests obj = new ViewServiceRequests("wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQKhI38wWkZxx8CPJXkD-jd9es3OlFudcBGYLv8wdEgIeyCJkD1VMZY11w1jAtUNdeDQ0r4MQ4D5WxuHMSeymokPGLrEFk0qZyF4XYRgpBWZFA",true,"g71eq5a5fa1DlpGZM8heB4f28VW2Na8FUZ_G7-6fCIfRgXP-NGHHxtQHUZmaD99AsAD77k-URWObTDEka8FrRCumI7UJCjvJC2ZhysfUrWJn1QjnI5KvFtOq6nhLYpFePTwFHwhh2D9lKxIfaL69VccNQpk_y1gzmVQx6Ugl_l4");
		
		HashMap rows =  obj.mfetchServiceRequestHistory(72);
//		HashMap rows =  obj.mfetchServiceRequests(14);
		//HashMap rows =  obj.mAddServiceRequests(16, 156 , 21321 , "Gajanan Shetye", "gd_shetye@yahoo.com", "9900009990", "My SR Comment : Status", "Raised", 1, "370848551.jpeg", "My SR test details", "Medium", "Plumbing");
		//Why Society ID required
		//Need to make separate History table
		
		//System.out.println(obj.convert(rows));
		System.out.println(rows);	
	}

}


