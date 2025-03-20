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
import MainClasses.Receipt;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;
import java.sql.Connection;
import java.sql.Timestamp;
import MainClasses.CommonBaseClass.*;

public class TakePoll  extends CommonBaseClass 
{ 
	static Polls t_servey;
	static Utility m_Utility;

	public TakePoll(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken, bIsVerifyDbDetails, sTkey);
		// TODO Auto-generated constructor stub
		t_servey = new Polls(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);	
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchActivePolls(int iUnitId,int iSocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> t_votes= t_servey.mfetchActivePolls(iUnitId,iSocietyID);

		if(t_votes.size() > 0)
		{
			 rows2.put("vote",MapUtility.HashMaptoList(t_votes)); 
			 rows2.put("active",1);
			 rows.put("response",rows2);
			 rows.put("success",1);
			
		}
		else
		{
			rows2.put("vote","");
			rows2.put("active",1);
			rows.put("response",rows2);
			rows.put("success",0);
			
		}
		
		return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchInactivePolls(int iUnitId,int iSocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> t_votes= t_servey.mfetchInactivePolls(iUnitId,iSocietyID);

		if(t_votes.size() > 0)
		{
			 rows2.put("vote",MapUtility.HashMaptoList(t_votes)); 
			 rows.put("response",rows2);
			 rows.put("success",1);
			 rows.put("active",0);
		}
		else
		{
			rows2.put("vote","");
			rows.put("response",rows2);
			rows.put("success",0);
			rows.put("active",0);
		}
		
		return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchUserVote(int iUnitId,int iGroupId,int iSocietyID,int iPollId)
		{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> t_votes= t_servey.mfetchUserVote(iUnitId, iGroupId, iSocietyID, iPollId);
		HashMap<Integer, Map<String, Object>> options=t_servey.mfetchAllOptions(iPollId);
		if(t_votes.size() > 0||options.size()>0)
		{
			 rows2.put("vote",MapUtility.HashMaptoList(t_votes)); 
			 rows2.put("options",MapUtility.HashMaptoList(options)); 
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			rows2.put("vote","");
			rows.put("response",rows2);
			rows.put("success",0);
		}
	  
	    return rows;
		}
	
	public static  HashMap<Integer, Map<String, Object>> InsertAllVotes(int iOptionId, int iLoginId,int iSocietyId, int iUnitId,int iPollId)
	{
		HashMap<Integer, Map<String, Object>> rows= t_servey.InsertVotes( iOptionId, iLoginId, iSocietyId, iUnitId,iPollId, false);
		
		return rows;
		 
	}
	
	/*public static  HashMap<Integer, Map<String, Object>> SelectAllEdit(int iOptionId,int iPollId)
	{
		HashMap<Integer, Map<String, Object>> rows= t_servey.SelectEdit( iOptionId,iPollId, false);
		
		return rows;
		 
	}*/
	
	public static  HashMap<Integer, Map<String, Object>> InsertUserVote(int iOptionId, int iLoginId,int iSocietyId, int iUnitId,int iPollId,String MemReview)
		{
		HashMap<Integer, Map<String, Object>> rows2= t_servey.InsertUserVote(iOptionId, iLoginId, iSocietyId, iUnitId, iPollId,MemReview);
		
		HashMap rows = new HashMap<>();
		if(rows2.size() > 0)
		{
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			rows.put("response","");
			rows.put("success",0);
		}
		
		return rows;
		}
	
	public static  HashMap<Integer, Map<String, Object>> UpdateUserVote(int iOldOptionId,int iOptionId, int iLoginId,int iSocietyId, int iUnitId,int iPollId,String MemReview)
		{
		HashMap rows2= t_servey.UpdateUserVote(iOldOptionId, iOptionId, iLoginId, iSocietyId, iUnitId, iPollId,MemReview);
		
		HashMap rows = new HashMap<>();
		if(rows2.size() > 0)
		{
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			rows.put("response","");
			rows.put("success",0);
		}
		
		return rows;
		}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchAllVotes(int iUnitId,int iSocietyID,int iGroupId, int iOptionId, int iPollId)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		
		HashMap<Integer, Map<String, Object>> t_votes= t_servey.mfetchVotes(iUnitId, iGroupId, iSocietyID, iPollId, iOptionId, false);
		 
		if(t_votes.size() > 0)
		{
			 rows2.put("vote",MapUtility.HashMaptoList(t_votes)); 
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			rows2.put("vote","");
			rows.put("response",rows2);
			rows.put("success",0);
		}
	 System.out.println(rows);
	    return rows;
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TakePoll obj;
		try {
			obj = new TakePoll("wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQKhI38wWkZxx8CPJXkD-jd9PXq2IKxUy-LVXvlb-MZWOclr6NJeKqggO7ku4eUyDnC4h33aPqtViQHpUdJsPvSf0ukoWqrTZ2aHwnakhK08nA",true,"tDC-PQDnxMCtyDirc3pLNKEfKmWs4wLXcinv2c-39rap_qYRLeHZk5dB8jvE-RmMZjqaFvCXPPiWrn7NhIf7-AvsgvdRbzeVXC1zHT4WCfOIlC4iG9Cm2-8QzVxuEuZOVBODmQa6TtHMUkLu6KfShmUrxFls5D-4oHpxv8Hr-Oo");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//HashMap t_votes = new HashMap<>();
		HashMap rows = new HashMap<>();
		//iUnitId, iGroupId, iSocietyID, iPollId, iOptionId, false
		rows=mfetchAllVotes(12,59,241,0,0);
		//rows=mfetchUserVote(17,145,156,229);
		//rows=mfetchInactivePolls(16,156);
		//t_votes=mfetchAllVotes(46,7,0,0,0);
		System.out.println(rows);
		
		//HashMap VoteInsert = new HashMap<>();
		//rows=InsertAllVotes(78,1,7,46,57);
		
		//System.out.println(rows);
		
		//HashMap SelectEdit = new HashMap<>();
		//rows=SelectAllEdit(57,77);
		
		//System.out.println(rows);
	}

}

