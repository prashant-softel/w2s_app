package MainClasses;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import CommonUtilityFunctions.MapUtility;

public class ViewRentalAgreement
{
	static RentalAgreement m_Agreement;
	CommonBaseClass b1;
	public ViewRentalAgreement(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		//super(sToken,bIsVerifyDbDetails,sTkey);
		b1 = new CommonBaseClass(sToken,bIsVerifyDbDetails,sTkey);
		m_Agreement = new RentalAgreement(b1.m_objDbOperations);
	}
	public ViewRentalAgreement() throws ClassNotFoundException 
	{
		//super(sToken,bIsVerifyDbDetails,sTkey);
		m_Agreement = new RentalAgreement(b1.m_objDbOperations);
	}
	public static HashMap<Integer, Map<String, Object>> fetchAllRentalAgreementByUnitId(int UnitId,int SocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> Result = m_Agreement.fetchRentalAgreementByUnitId(UnitId, SocietyId);
		
		if(Result.size() > 0)
		{	
			rows.put("success",1);
			rows2.put("AgreementDetails", Result);
			rows.put("response", rows2);
		}
		else
		{
			rows2.put("AgreementDetails","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static HashMap<Integer, Map<String, Object>> fetchAllRentalAgreement(int SocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> Result = m_Agreement.fetchRentalAgreement(SocietyId);
		
		if(Result.size() > 0)
		{	
			rows.put("success",1);
			rows2.put("AllAgreementDetails", Result);
			rows.put("response", rows2);
		}
		else
		{
			rows2.put("AllAgreementDetails","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	
	// Add new Function Fetch Specific tenant Agrrement Data
	public static HashMap<Integer, Map<String, Object>> fetchRentalAgreementByTenantID(int SocietyId,int iUnitId,int iTenantID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> Result = m_Agreement.fetchRentalAgreementByTanent(SocietyId,iUnitId,iTenantID);
		
		if(Result.size() > 0)
		{	
			rows.put("success",1);
			rows2.put("AgreementDetails", Result);
			rows.put("response", rows2);
		}
		else
		{
			rows2.put("AgreementDetails","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mInsertNewAgreement(int iUnitId,int iPropertyUse,int iPropertyType, String sPropertyAdd, String sPincode, String sCity,String sRegion, String sDeposit,int iRentType,String sMiscClause,String sPOAName,String sPOAadhaar,int iSocietyId,String sRentDetails,String sMonthlyRent,String sStartDate,String sEndDate,int iTenantId,int iInRural,int iUnitOutOfSociety) throws JSONException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		int iFirstIndex = 0,iRentalAgreementId=0;
		int iLastIndex = 1,iMember = 0;
		int iFrom[] = new int[3];
		int iTo[] = new int[3];
		String sRent[] = new String[3];
		//long lAgreementId = m_Agreement.addRentalAgreement(iUnitId, iPropertyType, sPropertyAdd, sPincode, sCity, sRegion, sDeposit, iRentType, sMiscClause, sPOAName, sPOAadhaar, iSocietyId, iTenantID, sStartdate, sEnddate)
		long lAgreementId = m_Agreement.addRentalAgreement(iUnitId, iPropertyUse,iPropertyType, sPropertyAdd, sPincode, sCity, sRegion, sDeposit, iRentType, sMiscClause, sPOAName, sPOAadhaar, iSocietyId,iTenantId,sStartDate, sEndDate,iInRural,iUnitOutOfSociety);	
		if(lAgreementId > 0)
		{	
			if(sRentDetails != "")
			{
					JSONObject jsonObj;
					int iCount = ViewTenant.getCharCount(sRentDetails, '}');
					for(int i = 0; i < iCount;i++)
					{
						iFirstIndex = iLastIndex;
						System.out.println("First Index :"+iFirstIndex);
						if(iFirstIndex > 1)
						{
							iFirstIndex = iFirstIndex + 2;
						}
						String sNewString = sRentDetails.substring(iFirstIndex, sRentDetails.length());
						System.out.println("sNewString :"+sNewString);
						iLastIndex = sNewString.indexOf("}", 0);
						String sCurrentString = sNewString.substring(0, iLastIndex+1);
						jsonObj = new JSONObject(sCurrentString);
						System.out.println("jsonObj :"+jsonObj);
						sRentDetails = sNewString;
						iFrom[i] = jsonObj.getInt("FromMonth");
						iTo[i] = jsonObj.getInt("ToMonth");
						sRent[i] = jsonObj.getString("Rent");
						System.out.println("sRent : "+sRent[i]);
						iRentalAgreementId = (int)lAgreementId;
					}
					long lTenantMemberId = AgreementTerm.addRentDetails(iRentalAgreementId, iRentType, sStartDate, sEndDate, sMonthlyRent, iFrom, iTo, sRent, iSocietyId);
					
				}
				else
				{
					iRentalAgreementId = (int)lAgreementId;
					long lTenantMemberId = AgreementTerm.addRentDetails(iRentalAgreementId, iRentType, sStartDate, sEndDate, sMonthlyRent, iFrom, iTo, sRent, iSocietyId);
					
				}
			rows.put("success",1);
			rows2.put("AgreementId",lAgreementId);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("AgreementId","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> UpdateAgreementComment(int SocietyId,int iUnitId,int iAgreementID, String sMiscClauses,int iTenantID,String sAppoinmentDate, String sAppoinmentTime)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//HashMap<Integer, Map<String, Object>> Result = m_Agreement.UpdateAgreementClauses(SocietyId,iUnitId,iAgreementID,sMiscClauses);
		long Result = m_Agreement.UpdateAgreementClauses(SocietyId,iUnitId,iAgreementID,sMiscClauses,iTenantID,sAppoinmentDate,sAppoinmentTime);
		if(Result > 0)
		{	
			rows.put("success",1);
			rows2.put("Update", Result);
			rows.put("response", rows2);
		}
		else
		{
			rows2.put("Update","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	
	
	public static  HashMap<Integer, Map<String, Object>> mUpadateAgreemnet(int iSocietyID,int iRentalID,int iTenantID,int iPropertyType,int iPropertyUse,int iRentType,String sMonthlyRent,String sDeposit,String sRentDetails) throws JSONException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		int iFirstIndex = 0,iRentalAgreementId=0;
		int iLastIndex = 1,iMember = 0;
		int iFrom[] = new int[3];
		int iTo[] = new int[3];
		int sRent[] = new int[3];
		int iRentDetailsID[] =new int[3];
		long Result=0;
		if(iRentType==1)
		{
		Result = m_Agreement.UpdateAgreement(iSocietyID,iRentalID, iTenantID, iPropertyType, iPropertyUse, sMonthlyRent, sDeposit);
		}
		else
		{
			JSONObject jsonObj;
			int iCount = ViewTenant.getCharCount(sRentDetails, '}');
			for(int i = 0; i < iCount;i++)
			{
				iFirstIndex = iLastIndex;
				System.out.println("First Index :"+iFirstIndex);
				if(iFirstIndex > 1)
				{
					iFirstIndex = iFirstIndex + 2;
				}
				String sNewString = sRentDetails.substring(iFirstIndex, sRentDetails.length());
				///System.out.println("sNewString :"+sNewString);
				iLastIndex = sNewString.indexOf("}", 0);
				String sCurrentString = sNewString.substring(0, iLastIndex+1);
				jsonObj = new JSONObject(sCurrentString);
				//System.out.println("jsonObj :"+jsonObj);
				sRentDetails = sNewString;
				iFrom[i] = jsonObj.getInt("FromMonth");
				iTo[i] = jsonObj.getInt("ToMonth");
				sRent[i] = jsonObj.getInt("Rent");
				iRentDetailsID[i] = jsonObj.getInt("ID");
			   System.out.println("sRent : "+sRent[i]);
			}
			Result = m_Agreement.UpdateAgreement2(iSocietyID,iRentalID,iTenantID, iPropertyType, iPropertyUse, sDeposit ,iFrom, iTo, sRent,iRentDetailsID);
		
		}
		if(Result > 0)
		{	
			rows.put("success",1);
			rows2.put("Update", Result);
			rows.put("response", rows2);
		}
		else
		{
			rows2.put("Update","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
		
	}
	
	public static void main(String[] args) throws Exception
	{
		String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4yShq3gS2XOamTB-D4umDzi2TYQfnG5nBEn_d3K5HEbfKlAGWg8f-FjOl6u9vV4NJvFoxg2ZtUMth9Bdt6aVKx";
		String sTkey = "nXTYH6gXb5NrNS7bD-xf-7p-DLueHDMHV63rrzV8xdR5QxtyKNUQph89PNpuyjn5qq1PT2kOpx3iSYamT24xEmrftFMt-IfABEKDKRDSmEhS0-n02MO74v-qHylcskOL79mjDGY4VS4R1dqZybT3Mg";
		//ViewOwner obj = new ViewOwner(sToken, true, sTkey);
		ViewRentalAgreement obj = new ViewRentalAgreement();
		//HashMap rows =  obj.updateOwner(59, 26, "03-09-1996", "Male");
		HashMap rows =  obj.fetchAllRentalAgreementByUnitId(45, 59);
		Gson objGson = new Gson();
		String objStr = objGson.toJson(rows);
		System.out.println(objStr);
	}

}
