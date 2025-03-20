package MainClasses;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import CommonUtilityFunctions.MapUtility;

public class ViewCommitee extends CommonBaseClass 
{
	static Member m_Member;
	public  ViewCommitee(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
		m_Member = new Member(CommonBaseClass.m_objDbOperations,CommonBaseClass.m_objDbRootOperations);		
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchCommitee(){		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching Commitee Details
		HashMap<Integer, Map<String, Object>> mCommitee = m_Member.fetchCommitee();		
		 
		if(mCommitee.size() > 0)
		{
			for(Entry<Integer,Map<String, Object>> entry1 : mCommitee.entrySet()){
				if((int)entry1.getValue().get("coowner")==1){
					entry1.getValue().put("ownerStatus", "Owner");
				}
				else if((int)entry1.getValue().get("coowner")==2){
					entry1.getValue().put("ownerStatus", "Co-Owner");
				}
				else{
					entry1.getValue().put("ownerStatus", "");
				}
			}
			 rows2.put("commiteedetails",mCommitee);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("commiteedetails","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
		
	public static void main(String[] args) throws Exception
	{
		String tkey = "s_vcmYCVoFyDqDJOdmTFGdrJRGJNoZn8NsV-TfRyu-4ax6se5dTUiQvDPsKhtB2Bj1sq86pT7_yUskKeX7w6DMu0-ZA-ZbVWdve_0R8TH0m6mAY0a0VXaGYMJIePlCaBdA_fCK_tklxavFhvP9mHq6FcPvo4mHYLNJGiDJQehgU";
		String token = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4QbxX8E332W2ghFj7NYdPn3yvOs1wZFscO1bGwxFPEXW-62W42dAF4LjcLaDBpqB4n8mFhyQ08oLEgMSuOwubp";
		ViewCommitee obj = new ViewCommitee(token, true, tkey);
		
		HashMap rows =  obj.mfetchCommitee();
		Gson objGson = new Gson();
		String str = objGson.toJson(rows);
		//System.out.println(obj.convert(rows));
		System.out.println(str);	
	}

}


