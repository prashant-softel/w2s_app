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
import MainClasses.Event;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;

public class ViewEvents extends CommonBaseClass 
{
	private static Event m_Event;
	
	public  ViewEvents(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_Event = new Event(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	

	
	public static  HashMap<Integer, Map<String, Object>> mfetchAllEvents(int iUnitId,int iSocietyId, int iEventID, boolean bIsNotification)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpEvents = m_Event.mfetchEvents(iUnitId, iSocietyId, 0, true, iEventID);
			
		 
		if(mpEvents.size() > 0)
		{
			if(bIsNotification)
			{
				rows.put("response", mpEvents.get(0));
			}
		else{
			 rows2.put("events",MapUtility.HashMaptoList(mpEvents)); 
			 rows.put("response",rows2);
		    }
			 rows.put("success",1);
		}
		else
		{
			rows2.put("events","");
			rows.put("response",rows2);
			rows.put("success",0);
		}
		  //System.out.println(rows);

	    return rows;
	}
	
	
	
	public static void main(String[] args) throws Exception
	{
		//ViewEvents obj = new ViewEvents("LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4rN4UvW36NPHGaIgIoQdhRHazieK-x57rsPgmCHlTFQUroo7KcWQ2Pj_MRe0CJYlLFw_aOUq6_zH58af3hJwYX", true, "g6HoIfwqvn-e9HKlLi1XmHLLGpDoDVXDCNmzM3w9QNNIvEChod9HTBjp5gtkgJrMX59Ok3ciPvbV2luYx4Yde55FKR217XkQbsRab1FCMR-vKczR8jYQQnDz8t4EJhsU2NyJ7nEsMQfjZ-02XDoKIw");
		String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4rN4UvW36NPHGaIgIoQdhRHazieK-x57rsPgmCHlTFQUroo7KcWQ2Pj_MRe0CJYlLFw_aOUq6_zH58af3hJwYX";
		String sTkey = "g6HoIfwqvn-e9HKlLi1XmHLLGpDoDVXDCNmzM3w9QNNIvEChod9HTBjp5gtkgJrMX59Ok3ciPvbV2luYx4Yde55FKR217XkQbsRab1FCMR-vKczR8jYQQnDz8t4EJhsU2NyJ7nEsMQfjZ-02XDoKIw";
		ViewEvents obj = new ViewEvents(sToken, true, sTkey);
		HashMap rows =  obj.mfetchAllEvents(45,7,45,true);
		//System.out.println(obj.convert(rows));
		System.out.println(rows);	
	}


}


