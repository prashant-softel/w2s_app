package MainClasses;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import CommonUtilityFunctions.MapUtility;

public class ViewOwner
{
	static Owner m_Owner;
	CommonBaseClass b1;
	public ViewOwner(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		//super(sToken,bIsVerifyDbDetails,sTkey);
		b1 = new CommonBaseClass(sToken,bIsVerifyDbDetails,sTkey);
		m_Owner = new Owner(b1.m_objDbOperations);
	}
	public ViewOwner() throws ClassNotFoundException 
	{
		//super(sToken,bIsVerifyDbDetails,sTkey);
		m_Owner = new Owner(b1.m_objDbOperations);
	}
	public static HashMap<Integer, Map<String, Object>> fetchOwnersAllDetails(int UnitId,int SocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> OwnersDetails = m_Owner.fetchOwnersDetails(UnitId, SocietyId);
		
		if(OwnersDetails.size() > 0)
		{	
			rows.put("success",1);
			rows2.put("OwnerDetails", MapUtility.HashMaptoList(OwnersDetails));
			rows.put("response", rows2);
		}
		else
		{
			rows2.put("OwnerDetails","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	//public static HashMap<Integer, Map<String, Object>> updateOwner(int societyId,int imemberId, String sdob, String sgender)
	public static HashMap<Integer, Map<String, Object>> updateOwner(int societyId,int imemberId,int iOtherMemberId, String sNameOfPOA, String sPOAAdhaar)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		long lUpdateResult = m_Owner.updateOwner(societyId, imemberId,iOtherMemberId, sNameOfPOA, sPOAAdhaar);
		
		if(lUpdateResult == 0)
		{
			//m_dbConn.RollbackTransaction();
			 rows2.put("message","Unable to update owner");
			 rows2.put("MemberID",lUpdateResult);
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
		else
		{
			//update task
			 rows2.put("message","Owner Updated Successfully");
			 rows2.put("MemberID",lUpdateResult);
			 rows.put("response",rows2);
			 rows.put("success",1);
			
		}
		System.out.println(rows);
	    return rows;
	}
	public static void main(String[] args) throws Exception
	{
		String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4yShq3gS2XOamTB-D4umDzi2TYQfnG5nBEn_d3K5HEbfKlAGWg8f-FjOl6u9vV4NJvFoxg2ZtUMth9Bdt6aVKx";
		String sTkey = "nXTYH6gXb5NrNS7bD-xf-7p-DLueHDMHV63rrzV8xdR5QxtyKNUQph89PNpuyjn5qq1PT2kOpx3iSYamT24xEmrftFMt-IfABEKDKRDSmEhS0-n02MO74v-qHylcskOL79mjDGY4VS4R1dqZybT3Mg";
		//ViewOwner obj = new ViewOwner(sToken, true, sTkey);
		ViewOwner obj = new ViewOwner();
		//HashMap rows =  obj.updateOwner(59, 26, "03-09-1996", "Male");
		HashMap rows =  obj.fetchOwnersAllDetails(26, 59);
		Gson objGson = new Gson();
		String objStr = objGson.toJson(rows);
		System.out.println(objStr);
	}

}
