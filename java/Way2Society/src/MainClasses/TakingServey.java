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

import MainClasses.Receipt;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;
import java.sql.Connection;
import java.sql.Timestamp;
import MainClasses.CommonBaseClass.*;

public class TakingServey  extends CommonBaseClass 
{ 
	static servey t_servey;
	static Utility m_Utility;

	public TakingServey(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken, bIsVerifyDbDetails, sTkey);
		// TODO Auto-generated constructor stub
		t_servey = new servey(CommonBaseClass.m_objDbOperations);	
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchAllVotes(int iUnitId,int iSocietyID,int iGroupId, int iOptionId, int iPollId)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		
		HashMap<Integer, Map<String, Object>> t_votes= t_servey.mfetchVotes(iUnitId, iGroupId, iSocietyID, iPollId, iOptionId, false);
		 
		if(t_votes.size() > 0)
		{
			 rows2.put("vote",t_votes); 
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
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TakingServey obj;
		try {
			obj = new TakingServey("uvBiLie2SRayt8cETO-_xmugWNcI8u-2Alb1-pxhVuJzNn7BgrPaVab63iEP5jD7zTksTuCkNQmVWbhYYMyiLwHuTY9OFFvNnUysCLO2-Hc",true,"Tlqd5aX-0C9SzNh7GHjZJkb-AgV3FsHVxRlQYo8oE2HzDRNcn_WIHgmoKUJDQSsmAyKAJL4IzxsD1CAjVQWcqEcCHrUIs83_PZAP7c8sqa-BwNa-U2n9ougHryyEz9x9-eUy_UhF2bduGBjWeh2JOg");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//HashMap t_votes = new HashMap<>();
		HashMap rows = new HashMap<>();
		rows=mfetchAllVotes(46,7,0,0,0);
	    
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

