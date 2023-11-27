package MainClasses;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import CommonUtilityFunctions.MapUtility;

public class ViewLease extends CommonBaseClass  {
	static Lease m_Lease;
	public ViewLease(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken, bIsVerifyDbDetails, sTkey);
		// TODO Auto-generated constructor stub
		m_Lease = new Lease(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}

	public static  HashMap<Integer, Map<String, Object>> mfetchProfesstion(int iSocietyID) throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpProfession = m_Lease.mfetchProfesstionList(iSocietyID);
		
		
		 
		if(mpProfession.size() > 0)
		{
			//add member to map
			 rows2.put("Profession",mpProfession);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("Profession","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> minsertlease(int iSocietyID,int iUnitId,String TFname,String TLname,String TMname,String Tdob,String TContact,String TEmail,String Tprofession_id,String TLeaseStart,String TLeaseEnd,String TAddress,String TCity,String TPincode,String TNote,String TAgentName,String TAgentContact,int membercount,String sTenantVaring) throws ParseException, ClassNotFoundException, JSONException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		long mpLeaseID = m_Lease.minsertleased(iSocietyID,iUnitId,TFname,TLname,TMname,Tdob,TContact,TEmail,Tprofession_id,TLeaseStart,TLeaseEnd,TAddress,TCity,TPincode,TNote,TAgentName,TAgentContact,membercount,sTenantVaring);
		
		
		 
		if(mpLeaseID != 0)
		{
			//add member to map
			 rows.put("LeaseID",mpLeaseID);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows.put("LeaseID",mpLeaseID);
			 rows.put("success",0);
		}
	  
	    return rows;
	}

}
