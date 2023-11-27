package MainClasses;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import CommonUtilityFunctions.MapUtility;

public class ViewClassifieds extends CommonBaseClass 
{
	static Classifieds m_Classifieds;
	private static Utility m_Utility;
	
	public ViewClassifieds(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_Classifieds = new Classifieds(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		//System.out.println(m_Classifieds);
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchClassifieds(int iSocietyID, int iClassifiedID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpClassifieds = m_Classifieds.mfetchClassifieds(iSocietyID,iClassifiedID);
		 
		if(mpClassifieds.size() > 0)
		{
			 rows2.put("classified",MapUtility.HashMaptoList(mpClassifieds));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("classified","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}

	public static HashMap<Integer, Map<String, Object>> mAddClassifieds(int login_id,int society_id,String ad_title,String desp,String exp_date,String img,String location,String email,String phone,int cat_id)
		{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		long mpClassifieds = m_Classifieds.mAddClassifieds(login_id,society_id,ad_title,desp,exp_date,img,location,email,phone,cat_id);
		System.out.println(mpClassifieds);
		if(mpClassifieds > 0)
			{
			rows2.put("new_c_id",mpClassifieds);
			rows2.put("message","Classified Successfully Created");
			rows.put("response",rows2);
			rows.put("success",1);
			}
		else
			{
			rows2.put("new_c_id","");
			rows2.put("message","Unable To Create Classified. Please try again");
			rows.put("response",rows2);
			rows.put("success",0);
			}
		return rows;
		}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchClassifiedsCategory()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all Categories
		HashMap<Integer, Map<String, Object>> mpClassifieds = m_Classifieds.mfetchClassifiedsCategory();
		
		if(mpClassifieds.size() > 0)
		{
			//add category to map
			 rows2.put("cat_id",MapUtility.HashMaptoList(mpClassifieds));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//category not found
			 rows2.put("cat_id","Empty");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
		public static  HashMap<Integer, Map<String, Object>> mFetchAllClassifieds(int iSocietyId,int iFetchCount,boolean bFetchExpired,int iServiceRequest)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all Categories
		HashMap<Integer, Map<String, Object>> mpClassifieds = m_Classifieds.mFetchAllClassified(iSocietyId, iFetchCount, bFetchExpired, iServiceRequest);
		
		if(mpClassifieds.size() > 0)
		{
			//add category to map
			 rows.put("success",1);
			 rows2.put("cat_id",MapUtility.HashMaptoList(mpClassifieds));
			 rows.put("response",rows2);
			 
		}
		else
		{
			//category not found
			 rows2.put("cat_id","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static void main(String[] args) throws Exception
	{
		String tkey = "4qbv_xos4DepIqmHWVcepC4RNbXYyOs0Ct9J5Z29_b5JUOkp3bEmYn8eBMSsblBdWRIUQpUjkQxGPRsQp4NoDWk9FuH0gEyGJINXy1BCL5PUjg0hj822EEPHrOs0gU8NFbXQv7yLAiaDejUF4ZaAhw";
		String token = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQJ-DGHaFiGlp2Rk_X2ezeBaQsxprp4Zp1GMhRQG4BBIPC93oFYwOExWe7PuvXOiCzbb3b6I43bQt-kgFPggsPSuTNnmm--iYa9p1opyszoAcQ";
		ViewClassifieds obj = new ViewClassifieds(token, true, tkey);		
		//HashMap rows =  obj.mfetchClassifieds(156);
		//HashMap rows1 =  obj.mAddClassifieds(17,15,"prjjrs", "hrthhy", "2017-07-01", "gfs.jpg","","","",1);
	//	System.out.println(rows1);
		System.out.println(getDecryptedTokenMap());		
		//System.out.println(rows);
	}

}