package MainClasses;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import CommonUtilityFunctions.MapUtility;

public class ViewServiceProvider extends CommonBaseClass
{
	static ServiceProvider m_ServiceProvider;
	
	public  ViewServiceProvider(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		//super(bAccessRoot, dbName);
		// TODO Auto-generated constructor stub
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_ServiceProvider = new ServiceProvider(CommonBaseClass.m_objDbOperations);
		//super(sToken, bIsVerifyDbDetails, sTkey);
	}
	
	public static  HashMap<Integer, Map<String, Object>> mServiceProvider(int iSoc_id,int iUnitID){		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all Service Provider
		HashMap<Integer, Map<String, Object>> mpServiceProvider = m_ServiceProvider.mfetchServiceProvider(iSoc_id, iUnitID);	
		
		if(mpServiceProvider.size() > 0)
		{
			//add member to map
			 rows2.put("providers",MapUtility.HashMaptoList(mpServiceProvider));
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("providers","No security database selected");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
		System.out.println(rows);
	    return rows;
	}	


	public static HashMap<Integer, Map<String, Object>> mProviderDetails(int iService_prd_reg_id, int iSociety_id, int UnitID) throws ClassNotFoundException, ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpServiceProvider = m_ServiceProvider.mfetchProviderDetails(iService_prd_reg_id, iSociety_id,UnitID);
		System.out.println(iService_prd_reg_id + " " + iSociety_id + " " + UnitID) ;
		HashMap<Integer, Map<String, Object>> mServiceData = m_ServiceProvider.mfetchstaffitem(iService_prd_reg_id, iSociety_id,UnitID);
		
		
		if(mpServiceProvider.size() > 0)
		{
			//add member to map
			 rows2.put("providerDetails",mpServiceProvider);
			 rows2.put("servicedata",mServiceData);
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("providerDetails","");
			 rows2.put("servicedata", "");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}	
		System.out.println(rows);
	    return rows;
	}
	public static HashMap<Integer, Map<String, Object>> mProviderCategoryDetails()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpServiceProvider = m_ServiceProvider.mfetchProviderCategoryDetails();
		if(mpServiceProvider.size() > 0)
		{
			//add member to map
			 rows2.put("providerCategory",MapUtility.HashMaptoList(mpServiceProvider));
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("providerCategory","");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}	  
	    return rows;
	}
		
	public static HashMap<Integer, Map<String, Object>> mProviderDocument()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpServiceProvider = m_ServiceProvider.mfetchProviderDocument();
		if(mpServiceProvider.size() > 0)
		{
			//add member to map
			 rows2.put("document",MapUtility.HashMaptoList(mpServiceProvider));
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("document","");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}	  
	    return rows;
	}	
	
	public static HashMap<Integer, Map<String, Object>> mProviderUnitList(int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mMemberList = m_ServiceProvider.mfetchProviderUnits(iSocietyId);
		if(mMemberList.size() > 0)
		{
			//add member to map
			 rows2.put("member",MapUtility.HashMaptoList(mMemberList));
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("member","");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}	  
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mProviderReport(int iServiceProviderID,int iSocietyId,String StartDate, String EndDate)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mProviderReport = m_ServiceProvider.mfetchProviderReport(iServiceProviderID,iSocietyId, StartDate, EndDate);
	    return mProviderReport;
	}
	/* ------------------------insert service provider--------------------------------*/
	
	public static long mAddServiceProvider(int iSocietyID,String sName,int iCatID,String sDOB ,String sIdentityMark,String WorkingSince,String sEdication,String sMarry,String sPhoto, String sPhoto_thumb)
	{
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();
	long mpServiceProvider = m_ServiceProvider.mSetAddServiceProvider(iSocietyID,sName,iCatID,sDOB,sIdentityMark,WorkingSince,sEdication,sMarry,sPhoto,sPhoto_thumb);
	System.out.println("mAddServiceProvider"+ mpServiceProvider);
	
return mpServiceProvider;
}

/*		
		if(mMemberList.size() > 0)
		{
			//add member to map
			 rows2.put("member",MapUtility.HashMaptoList(mMemberList));
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("member","");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
		*/			
			/*if(iServiceProvider  == 0)
			{
				//m_dbConn.RollbackTransaction();
				return 0;
			}		
		
			//m_dbConn.EndTransaction();
			
			return iServiceProvider;
		}
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
		}
			
		return 0;
	}
	
	private long mUpdateServiceProvider(long lServiceProviderID, String name, int cat_id, String dateOfBirth,
			String identity_mark, String working_since, String education, String married, String photo, String photo_thumb)
	{
		long lRecordUpdatedID = m_ServiceProvider.mUpdateServiceProvider(lServiceProviderID, name,cat_id,dateOfBirth, identity_mark,working_since,education,married, photo, photo_thumb);
		System.out.println(lRecordUpdatedID);
		return lRecordUpdatedID;

	}
*/
	public static long mUpdateServiceProvider_Document(long lServiceProvider, String documentID, String file_name) {
		// TODO Auto-generated method stub
	
		long lRecordUpdatedID = m_ServiceProvider.mUpdateServiceProvider_Document(lServiceProvider, documentID, file_name);
		System.out.println(lRecordUpdatedID);
		return lRecordUpdatedID;
	}
	
	public static long mUpdateServiceProvider_FamilyDetails(long lServiceProvider, String father_name, String father_occu,
			String mother_name, String mother_occu, String hus_wife_name, String hus_wife_occ, String son_dou_name,
			String son_dou_occ, String other_name, String other_occ) {
		// TODO Auto-generated method stub
		long lRecordUpdatedID = m_ServiceProvider.mUpdateServiceProvider_FamilyDetails(lServiceProvider, father_name,  father_occu,  mother_name,  mother_occu,  hus_wife_name,  hus_wife_occ,  son_dou_name,  son_dou_occ,  other_name,  other_occ);
		System.out.println(lRecordUpdatedID);
		return lRecordUpdatedID;
	}	
	
	public static long mUpdateServiceProvider_ContactDetails(long lServiceProvider, String cur_resd_add, String cur_con_1,
			String cur_con_2, String native_add, String native_con_1, String native_con_2, String ref_name,
			String ref_add, String ref_con_1, String ref_con_2) 
	{
		// TODO Auto-generated method stub
		long lRecordUpdatedID = m_ServiceProvider.mUpdateServiceProvider_ContactDetails(lServiceProvider, cur_resd_add, cur_con_1, cur_con_2, native_add,  native_con_1,  native_con_2,  ref_name,  ref_add,  ref_con_1,  ref_con_2);
		System.out.println(lRecordUpdatedID);
		return lRecordUpdatedID;
	}
	
	//public static long mAddServiceProvider_Partial(int society_id,String name,int cat_id,String DateOfBirth,String identity_mark,String working_since,String education,String married, String photo, String photo_thumb)
	
public static HashMap<Integer, Map<String, Object>> mAddServiceProvider_Partial(int society_id,String name,String cat_id,String DateOfBirth,String identity_mark,String working_since,String education,String married, String photo, String photo_thumb)
	
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	//	long lServiceProviderID = 0;
		try
		{																																																			
			
			long lServiceProviderID = m_ServiceProvider.mSetAddServiceProvider_Partial(society_id,name,cat_id,DateOfBirth,identity_mark,working_since,education,married, photo, photo_thumb);
			System.out.println(lServiceProviderID);
			 
	if(lServiceProviderID > 0)
		{
			//add member to map
			 rows2.put("ServiceProID",lServiceProviderID);
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("ServiceProID","");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
	//return rows;
	}
	
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
		}
		System.out.println(rows);
		return rows;	
	}
	
	// New Version 
	public static HashMap<Integer, Map<String, Object>> mAddServiceProvider_Partial1(int society_id,String name,String cat_id,String DateOfBirth,String identity_mark,String working_since,String education,String married, String photo, String photo_thumb,String sCurrent_ContNo,String soc_staff_id)
	
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	//	long lServiceProviderID = 0;
		try
		{																																																			
			
			String lServiceProviderID = m_ServiceProvider.mSetAddServiceProvider_Partial1(society_id,name,cat_id,DateOfBirth,identity_mark,working_since,education,married, photo, photo_thumb,sCurrent_ContNo,soc_staff_id);
			System.out.println(lServiceProviderID);
			 
	if(!lServiceProviderID.equals("0"))
		{
			//add member to map
			 rows2.put("ServiceProID",lServiceProviderID);
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("ServiceProID","");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
	//return rows;
	}
	
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
		}
		System.out.println(rows);
		return rows;	
	}
	
	
	public static long mUpdateServiceProvider_UpdatoOkUnit(long lServiceProvider, int society_id, String unit_id,
			boolean bAdd) {
		// TODO Auto-generated method stub
//		public static long mUpdateServiceProvider_UpdateWorkUnit(long lServiceProviderID, int society_id, int unit_id, boolean bAdd)
		long lRecordUpdatedID = m_ServiceProvider.mUpdateServiceProvider_UpdateWorkUnit(lServiceProvider, society_id, unit_id, bAdd);
		return lRecordUpdatedID ;
	}

/* -------------------------------------Update Functions ------------------------------------------------------------------*/
	
	public static HashMap<Integer, Map<String, Object>> mUpdatePersonalDetails(int iSocietyID,int iServiceProviderID, String sProviderName,String sProviderDOB,String sProviderIdentymark,String sProviderW_since,String sMarritalStatus,String sMobile,String sAltMobile )
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_ServiceProvider.mUpdateProfileDetails(iSocietyID, iServiceProviderID, sProviderName, sProviderDOB, sProviderIdentymark, sProviderW_since, sMarritalStatus,sMobile,sAltMobile);

	System.out.println("update:"+ lResult);
	HashMap rows2 = new HashMap<>();
	if(lResult > 0)
	{
		 rows2.put("Update_id",lResult);
		 rows2.put("message","Profile Update Successfully");
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows2.put("Update_id","0");
		rows2.put("message","Profile Not Update");
		rows.put("response","");
		rows.put("success",0);
	}
	//System.out.println(rows);
	return rows;
	}
	/*public static HashMap<Integer, Map<String, Object>> mfetchUnitDetails(int iSocietyId,int iServiceProviderID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mUnitList = m_ServiceProvider.mfetchUnitsList(iSocietyId,iServiceProviderID);
		if(mUnitList.size() > 0)
		{
			//add member to map
			 rows2.put("units",MapUtility.HashMaptoList(mUnitList));
			 rows.put("success",1);
			 rows.put("response",rows2);			 
		}
		else
		{
			//member not found
			 rows2.put("units","");
			 rows.put("success",0);
			 rows.put("response",rows2);
		}	  
	    return rows;
	}*/
	
	public static HashMap<Integer, Map<String, Object>> mAddMore_Document(int iServiceProviderID,String sDoc_id, String sDoc_img )
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_ServiceProvider.mAddMoreDocument(iServiceProviderID,sDoc_id,sDoc_img);

	System.out.println("update:"+ lResult);
	HashMap rows2 = new HashMap<>();
	if(lResult > 0)
	{
		 rows2.put("Update_id",lResult);
		 rows2.put("message","Add Document Successfully");
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows2.put("Update_id","0");
		rows2.put("message","Add Document not Successfully");
		rows.put("response","");
		rows.put("success",0);
	}
	//System.out.println(rows);
	return rows;
	}
	public static HashMap<Integer, Map<String, Object>> mAssignTome(int iSocietyID,int iServiceProviderID,int iUnitID)
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_ServiceProvider.mAddAssignUnit(iSocietyID, iServiceProviderID,iUnitID);

	System.out.println("update:"+ lResult);
	HashMap rows2 = new HashMap<>();
	if(lResult > 0)
	{
		 rows2.put("UpdateUnit",lResult);
		 rows2.put("message","Assign Successfully");
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows2.put("UpdateUnit","0");
		rows2.put("message","Assign not Successfully");
		rows.put("response","");
		rows.put("success",0);
	}
	//System.out.println(rows);
	return rows;
	}
	public static HashMap<Integer, Map<String, Object>> mRemoveUnit(int iSocietyID,int iServiceProviderID,int iUnitID)
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_ServiceProvider.mRemoveUnit(iSocietyID, iServiceProviderID,iUnitID);

	System.out.println("update:"+ lResult);
	HashMap rows2 = new HashMap<>();
	if(lResult > 0)
	{
		 rows2.put("RemoveID",lResult);
		 rows2.put("message","Unit Remove Successfully");
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows2.put("RemoveID","0");
		rows2.put("message","Unit not Remove");
		rows.put("response","");
		rows.put("success",0);
	}
	//System.out.println(rows);
	return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mAddComments(int iServiceProviderID,int iMemberID,String sMemberName,String sComment, int iRating,int iHideName)
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_ServiceProvider.mAddCommentReview(iServiceProviderID,iMemberID, sMemberName,sComment,iRating,iHideName);

	System.out.println("ReviewID:"+ lResult);
	HashMap rows2 = new HashMap<>();
	if(lResult > 0)
	{
		 rows2.put("ReviewID",lResult);
		 rows2.put("message","Review Added Successfully");
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows2.put("ReviewID","0");
		rows2.put("message","Review not Added");
		rows.put("response","");
		rows.put("success",0);
	}
	//System.out.println(rows);
	return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mProviderApproved(int iServiceProviderID)
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_ServiceProvider.mAppved(iServiceProviderID);

	System.out.println("ReviewID:"+ lResult);
	HashMap rows2 = new HashMap<>();
	if(lResult > 0)
	{
		 rows2.put("Approved",lResult);
		 rows2.put("message","Provider Approve Successfully");
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows2.put("Approved","0");
		rows2.put("message","Provider not Approve");
		rows.put("response","");
		rows.put("success",0);
	}
	//System.out.println(rows);
	return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mProviderRemoved(int iServiceProviderID)
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_ServiceProvider.mRemoved(iServiceProviderID);

	System.out.println("ReviewID:"+ lResult);
	HashMap rows2 = new HashMap<>();
	if(lResult > 0)
	{
		 rows2.put("Approved",lResult);
		 rows2.put("message","Provider Removed Successfully");
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows2.put("Approved","0");
		rows2.put("message","Provider not Removed");
		rows.put("response","");
		rows.put("success",0);
	}
	//System.out.println(rows);
	return rows;
	}
	public static HashMap<Integer, Map<String, Object>> mProviderBlocked(int iSocietyID,int iServiceProviderID, String sComments)
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_ServiceProvider.mProviderBlock(iSocietyID,iServiceProviderID,sComments);

	System.out.println("ReviewID:"+ lResult);
	HashMap rows2 = new HashMap<>();
	if(lResult > 0)
	{
		 rows2.put("Approved",lResult);
		 rows2.put("message","Provider Removed Successfully");
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows2.put("Approved","0");
		rows2.put("message","Provider not Removed");
		rows.put("response","");
		rows.put("success",0);
	}
	//System.out.println(rows);
	return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> getStaffStatus(int iSocietyID)
	{
	HashMap rows = new HashMap<>();
	
	HashMap<Integer, Map<String, Object>> lResult = m_ServiceProvider.mgetStaffStatus(iSocietyID);

	System.out.println("ReviewID:"+ lResult);
	
	return lResult;
	}
	

	
	public static HashMap<Integer, Map<String, Object>> mProviderSetMoreDetailsForApproval(int iServiceProviderID, String sComment, int societyId)
	{
		HashMap rows = new HashMap<>();
		
		long lResult = m_ServiceProvider.mSetMoreDetailsForApproval(iServiceProviderID, sComment,societyId);
	
		System.out.println("mGetMoreDetails Result:"+ lResult);
		HashMap rows2 = new HashMap<>();
		if(lResult > 0)
		{
			 rows2.put("GetMoreDetails",lResult);
			 rows2.put("message","Provider GetMoreDetails called Successfully");
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			rows2.put("GetMoreDetails","0");
			rows2.put("message","Provider GetMoreDetails call failed");
			rows.put("response",rows2);
			rows.put("success",0);
		}
		//System.out.println(rows);
		return rows;
	}

	public static void main(String[] args) throws Exception
	{
		String sToken="LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4yIdc50iQM01cP_FVR5Y6pn0J-eVRwxttv5OHgjOp0jjX79a4Ol_G0DpHvx2CmW3xksT3ToVSj3pipWFXjLJFG";

		
		String sTkey = "Aeoqvo-byugNot7FVjO_8y8RxZFb7Sz9QOVqaJuaf8Bx42N6QplPkM5WvxiZs9fmtKGAE9OKGop1qGWeTG2XNTxYPoT0kLvvVWQeU18A6ducON83ugRc00fuiKH15joMwSsxGN9-DrMeZaF92q30sFbCjPHqMp2VkyU2pCScxUc";
		
		ViewServiceProvider obj = new ViewServiceProvider(sToken, true, sTkey);	
		int login_id = 4;
		int society_id = 156;
		String name = "Samir Driver";
		String cat_id = "3";
		String DateOfBirth = "2014-04-12";
		String identity_mark = "Scar";
		String Working_since = "04-01-2011";
		String education = "B.Sc";
		String married = "Yes";
		String Photo = "Samir.jpg";

		String photo_thumb = "";
		//int age = 23;

		HashMap rows = obj.mProviderUnitList(288);
		//HashMap rows = obj.mUpdatePersonalDetails(59,44,"Sujit Kumar","12-09-1970","No","15-01-2017","Yes","9869854582", "15241251");
		//System.out.println(ID);
		//mProviderReport(int iServiceProviderID,int iSocietyId,String StartDate, String EndDate)
		//HashMap rows = obj.mServiceProvider(59);
		Gson objGson = new Gson();
		String objStr = objGson.toJson(rows);
		System.out.println("Result:"+objStr);
	
		// TODO Auto-generated method stub
		
	}
}


//}


