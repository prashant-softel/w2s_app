package MainClasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import CommonUtilityFunctions.MapUtility;

import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;;

public class ViewTenant
{
	static TenantDG m_Tenant;
	
	public ViewTenant(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		//super(sToken,bIsVerifyDbDetails,sTkey);
		CommonBaseClass b1 = new CommonBaseClass(sToken, bIsVerifyDbDetails, sTkey);
		m_Tenant = new TenantDG(b1.m_objDbOperations);
	}
	public ViewTenant() throws ClassNotFoundException 
	{
		//super(sToken,bIsVerifyDbDetails,sTkey);
		m_Tenant = new TenantDG(CommonBaseClass.m_objDbOperations);
	}
	public static  HashMap<Integer, Map<String, Object>> mfetchTenantDetails(int iUnitId,int itenantId,int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpTenant = m_Tenant.fetchTenantDetails(iUnitId, itenantId,iSocietyId);
		
		if(mpTenant.size() > 0)
		{	
			rows.put("success",1);
			rows2.put("Tenant",MapUtility.HashMaptoList(mpTenant));
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("Tenant","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mInsertNewTenant(int iUnitId, String sFName, String sMName,String sLName, String sMobileNo, String sEmail, int iProfessionId, String sDob,String sAdhaarCard, String sTenantAddress, String sCity, String sPincode,String sAgentName, String sAgentNumber, String sCIN_No, int iMemCount, String sNote, String sRelation,int iTenantType,String sCompanyName, String sCompanyPanNo, int iSocietyId,String sTenantFamilyDetails) throws JSONException
	{
		String sTMemName = "", sTRelation = "", sTMemDob = "", sTMemContactNo = "", sTMemEmail = "", sTMemAdhaarCard = "" ; 
		//boolean bCoSignAgreement = false;
		int sCoSignAgreement=0;
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		int iFirstIndex = 0;
		int iLastIndex = 1;
		int iMember = 0;
		if(sTenantFamilyDetails != "")
		{
		iMember = getCharCount(sTenantFamilyDetails, '}');
		System.out.println("iCount : "+iMember);
		iMemCount = iMember+1;
		}
		else
		{

		}
		long lTenantId = m_Tenant.addTenantModule(iUnitId, sFName, sMName, sLName, sMobileNo, sEmail, iProfessionId, sDob, sAdhaarCard, sTenantAddress, sCity, sPincode, sAgentName, sAgentNumber, sCIN_No,iMemCount, sNote, sRelation,iTenantType,sCompanyName,sCompanyPanNo,iSocietyId);
		System.out.println("lTenantId : "+lTenantId);
		//String sFamilyDetails = sTenantFamilyDetails.replaceAll("'", "\"");
		//JSONObject jsonObj;
		//int iCount = getCharCount(sTenantFamilyDetails, '}');
		if(lTenantId > 0)
		{
			if(sTenantFamilyDetails != "")
			{
				JSONObject jsonObj;
				int iCount = getCharCount(sTenantFamilyDetails, '}');
				for(int i = 0; i < iCount;i++)
				{
					iFirstIndex = iLastIndex;
					System.out.println("First Index :"+iFirstIndex);
					if(iFirstIndex > 1)
					{
						iFirstIndex = iFirstIndex + 2;
					}
					String sNewString = sTenantFamilyDetails.substring(iFirstIndex, sTenantFamilyDetails.length());
					System.out.println("sNewString :"+sNewString);
					iLastIndex = sNewString.indexOf("}", 0);
					String sCurrentString = sNewString.substring(0, iLastIndex+1);
					jsonObj = new JSONObject(sCurrentString);
					System.out.println("jsonObj :"+jsonObj);
					sTenantFamilyDetails = sNewString;
					sTMemName = jsonObj.getString("Name");
					sTRelation = jsonObj.getString("Relation");
					sTMemDob = jsonObj.getString("DOB");
					sTMemContactNo = jsonObj.getString("Contact");
					sTMemEmail = jsonObj.getString("Email");
					sTMemAdhaarCard = jsonObj.getString("Adhaar");
				sCoSignAgreement = 0;
				if(jsonObj.getBoolean("CoSingAgreement") == true) {
					sCoSignAgreement = 1;
				}
					System.out.println("sCoSignStatus :"+sCoSignAgreement);
					int iTenantId = (int)lTenantId;
					long lTenantMemberId = m_Tenant.addTenantMembers(iTenantId, sTMemName, sTRelation, sTMemDob, sTMemContactNo, sTMemEmail, sTMemAdhaarCard,sCoSignAgreement,iSocietyId);
				}
			}
			else
			{
			}
			rows.put("success",1);
			rows2.put("TenantId",lTenantId);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("TenantId","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mInsertNewTenantMember(int iTenantModuleId, String sMemName, String sRelation,String sDOB, String sMobileNo, String sEmail, String sAdhaarCard, int sCoSignStatus, int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		long lTenantId = m_Tenant.addTenantMembers(iTenantModuleId, sMemName, sRelation, sDOB, sMobileNo, sEmail,sAdhaarCard,sCoSignStatus, iSocietyId);
		//List sFamilyMem = Arrays.asList(sFamilyDetails);
		//System.out.println(sFamilyMem);
		
		if(lTenantId > 0)
		{	
			rows.put("success",1);
			rows2.put("TenantMemberId",lTenantId);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("TenantMemberId","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mUpdateTenantModule(int iSocietyId,int iTenantModuleId, String sStartDate, String sEndDate)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		long updateResult = m_Tenant.updateTenantDetails(iSocietyId, iTenantModuleId,sStartDate,sEndDate);
		
		if(updateResult > 0)
		{	
			rows.put("success",1);
			rows2.put("Tenant","Updated Successfully");
			rows.put("response",updateResult);
		}
		else
		{
			rows2.put("Tenant","");
			rows.put("response","Error");
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mUpdateTenantStatus(int iSocietyId,int iTenantModuleId, String sStatus)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		long updateResult = m_Tenant.updateTenantStatus(iSocietyId, iTenantModuleId, sStatus);
		
		if(updateResult > 0)
		{	
			rows.put("success",1);
			rows2.put("Tenant","Updated Successfully");
			rows.put("response",updateResult);
		}
		else
		{
			rows2.put("Tenant","");
			rows.put("response","Error");
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mFetchTenantMemberDetails(int itenantId,int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpTenant = m_Tenant.fetchTenantMemberDetails(itenantId,iSocietyId);
		
		if(mpTenant.size() > 0)
		{	
			rows.put("success",1);
			rows2.put("Tenant",MapUtility.HashMaptoList(mpTenant));
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("Tenant","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mEditTenantDetails(int iTenantModuleId,int iUnitId, String sFName, String sMName,String sLName, String sMobileNo, String sEmail, int iProfessionId, String sDob,String sAdhaarCard, String sTenantAddress, String sCity, String sPincode,String sAgentName, String sAgentNumber, String sCIN_No, int iMemCount, String sNote,String sRelation,int iTenantType, String sCompanyName, String sCompanyPanNo, int iSocietyId,String sTenantFamilyDetails,String LeaseStartDate,String LeaseEndDate) throws JSONException//Correct
	{
		String sTMemName = "", sTRelation = "", sTMemDob = "", sTMemContactNo = "", sTMemEmail = "", sTMemAdhaarCard = ""; 
		int sCoSignAgreement=0;
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		int iFirstIndex = 0,iTenantMemberId=0;
		int iLastIndex = 1,iMember = 0;
		
		if(sTenantFamilyDetails != "")
		{
			iMember = getCharCount(sTenantFamilyDetails, '}');
			System.out.println("iCount : "+iMember);
			iMemCount = iMember;
		}
		else
		{
		
		}
		//long lTenantId = m_Tenant.editTenantModule(iTenantModuleId, iUnitId, sFName, sMName, sLName, sMobileNo, sEmail, iProfessionId, sDob, sAdhaarCard, sTenantAddress, sCity, sPincode, sAgentName, sAgentNumber, sCIN_No, iMemCount, sNote, sRelation, iTenantType, sCompanyName, sCompanyPanNo, iSocietyId)
		long lTenantId = m_Tenant.editTenantModule(iTenantModuleId,iUnitId, sFName, sMName, sLName, sMobileNo, sEmail, iProfessionId, sDob, sAdhaarCard, sTenantAddress, sCity, sPincode, sAgentName, sAgentNumber, sCIN_No, iMemCount, sNote, sRelation, iTenantType, sCompanyName, sCompanyPanNo, iSocietyId,LeaseStartDate,LeaseEndDate);
		if(lTenantId > 0)
		{
			if(sTenantFamilyDetails != "")
			{
				JSONObject jsonObj;
				int iCount = getCharCount(sTenantFamilyDetails, '}');
				for(int i = 0; i < iCount;i++)
				{
					iFirstIndex = iLastIndex;
					System.out.println("First Index :"+iFirstIndex);
					if(iFirstIndex > 1)
					{
						iFirstIndex = iFirstIndex + 2;
					}
					String sNewString = sTenantFamilyDetails.substring(iFirstIndex, sTenantFamilyDetails.length());
					System.out.println("sNewString :"+sNewString);
					iLastIndex = sNewString.indexOf("}", 0);
					String sCurrentString = sNewString.substring(0, iLastIndex+1);
					jsonObj = new JSONObject(sCurrentString);
					System.out.println("jsonObj :"+jsonObj);
					sTenantFamilyDetails = sNewString;
					iTenantMemberId = jsonObj.getInt("tmember_id");
					sTMemName = jsonObj.getString("mem_name");
					sTRelation = jsonObj.getString("relation");
					sTMemDob = jsonObj.getString("DOB");
					sTMemContactNo = jsonObj.getString("contact_no");
					sTMemEmail = jsonObj.getString("email");
					sTMemAdhaarCard = jsonObj.getString("AdhaarCard");
					//if(jsonObj.getBoolean("CoSignStatus") == true ||  jsonObj.getInt("CoSignStatus") != 0 && jsonObj.getInt("CoSignStatus") != 1 ) {
					//	sCoSignAgreement = 1;
					//}
					sCoSignAgreement = jsonObj.getInt("CoSignStatus");
					System.out.println("sCoSignStatus :"+sCoSignAgreement);
					//int iTenantId = (int)lTenantId;
					long lTenantMemberId = m_Tenant.editTenantMember(iTenantMemberId, iTenantModuleId,sTMemName, sTRelation, sTMemDob, sTMemContactNo, sTMemEmail, iSocietyId,sTMemAdhaarCard,sCoSignAgreement);//if you dont have tenant member id send it as 0 and send itenat Id of it
					
				}
			}
			else
			{
			}
		}
		if(lTenantId > 0)
		{	
			rows.put("success",1);
			rows2.put("TenantId",lTenantId);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("TenantId","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mEditTenantMemberDetails(int itenantMemberId, String sMemName, String sRelation,String sDOB, String sMobileNo, String sEmail,int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		int iTenantId = 0;
		String sTMemAdhaarCard = "";
		int sCoSignAgreement =0;
		long lTenantId = m_Tenant.editTenantMember(itenantMemberId,iTenantId,sMemName, sRelation, sDOB, sMobileNo, sEmail, iSocietyId,sTMemAdhaarCard,sCoSignAgreement);
		
		if(lTenantId > 0)
		{	
			rows.put("success",1);
			rows2.put("TenantMemberId",lTenantId);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("TenantMemberId","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mDeleteTenant(int itenantId,int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		long lTenantId = m_Tenant.DeleteTenant(itenantId, iSocietyId);
		
		if(lTenantId > 0)
		{	
			rows.put("success",1);
			rows2.put("TenantId",lTenantId);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("TenantMemberId","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static int getCharCount(String s, char c) 
    { 
        int res = 0; 
  
        for (int i=0; i<s.length(); i++) 
        { 
            // checking character in string 
            if (s.charAt(i) == c) 
            res++; 
        }  
        return res; 
    } 
	public static void main(String[] args) throws Exception
	{
		String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4yShq3gS2XOamTB-D4umDzi2TYQfnG5nBEn_d3K5HEbfKlAGWg8f-FjOl6u9vV4NJvFoxg2ZtUMth9Bdt6aVKx";
		String sTkey = "nXTYH6gXb5NrNS7bD-xf-7p-DLueHDMHV63rrzV8xdR5QxtyKNUQph89PNpuyjn5qq1PT2kOpx3iSYamT24xEmrftFMt-IfABEKDKRDSmEhS0-n02MO74v-qHylcskOL79mjDGY4VS4R1dqZybT3Mg";
		//ViewTenant obj = new ViewTenant(sToken, true, sTkey);
		//ViewServiceProvider objServiceProvider = new ViewServiceProvider(sToken, true, sTkey);
		ViewTenant obj = new ViewTenant();
		int indexOfChar = 1, firstIndex = 0;
		String familyDate = "[{'Name':'ajay kumar','Relation':'friend','DOB':'1970-12-10','Contact':'23456788','Email':'sadad@gmail.com','Adhaar':'4356656','CoSingAgreement':'1'},{'Name':'amit singh','Relation':'friends','DOB':'2003-12-10','Contact':'','Email':'','Adhaar':'','CoSingAgreement':'0'},{'Name':'amit singh','Relation':'friends','DOB':'2003-12-10','Contact':'','Email':'','Adhaar':'','CoSingAgreement':'0'},{'Name':'amit singh','Relation':'friends','DOB':'2003-12-10','Contact':'','Email':'','Adhaar':'','CoSingAgreement':'0'}]";
		/*//String familyDetails = "\"[{"Name":"ajay kumar","Relation":"friend","DOB":"1970-12-10","Contact":"23456788","Email":"sadad@gmail.com","Adhaar":"4356656","CoSingAgreement":true},{"Name":"amit singh","Relation":"friends","DOB":"2003-12-10","Contact":"","Email":"","Adhaar":"","CoSingAgreement":false}]/";
		String sFamilyDetails = familyDate.replaceAll("'", "\"");
		int iCount = getCharCount(familyDate, '}');
		for(int i = 0; i < iCount;i++)
		{
			firstIndex = indexOfChar;
			System.out.println("First Index :"+firstIndex);
			if(firstIndex > 1)
			{
				firstIndex = firstIndex + 2;
			}
			String sNewString = familyDate.substring(firstIndex, familyDate.length());
			//System.out.println("sNewString :"+sNewString);
			indexOfChar = sNewString.indexOf("}", 0);
			//System.out.println("last Index :"+indexOfChar);
			String sCurrentString = sNewString.substring(0, indexOfChar+1);
			//System.out.println("sCurrentString :"+sCurrentString);
			JSONObject jsonObj = new JSONObject(sCurrentString);
			//System.out.println("jsonObj:\n"+jsonObj);
			familyDate = sNewString;
			//System.out.println("Value of i :"+i);
		}
		
		*/
		//HashMap rows = mInsertNewTenant(iUnitId, sFName, sMName, sLName, sMobileNo, sEmail, iProfessionId, sDob, sAdhaarCard, sTenantAddress, sCity, sPincode, sAgentName, sAgentNumber, sCIN_No, iMemCount, sNote, sCompanyName, sCompanyPanNo, iSocietyId, sTenantFamilyDetails)
		//HashMap rows = mInsertNewTenant(26, "Arnav", "", "Wagh", "8108019872", "arnav@gmail.com", 2, "1996-09-03", "1783392", "Mahim", "Mumbai", "400016", "Vaishali", "38737283", "", 4, "","","", 59, familyDate);
		//Gson objGson = new Gson();
		//String objStr = objGson.toJson(rows);
		//System.out.println("result :"+objStr);
	}
}


