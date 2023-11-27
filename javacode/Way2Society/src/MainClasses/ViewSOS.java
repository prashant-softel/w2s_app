package MainClasses;

import static MainClasses.ProjectConstants.ENCRYPT_INIT_VECTOR;
import static MainClasses.ProjectConstants.ENCRYPT_SPKEY;
import java.lang.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import CommonUtilityFunctions.MapUtility;
import MainClasses.Bill;
import MainClasses.Payment;
import MainClasses.Receipt;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;
public class ViewSOS extends CommonBaseClass {
	static SOSAlert m_SOSAlert;
	private static Utility m_Utility;
	public ViewSOS(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken, bIsVerifyDbDetails, sTkey);
		// TODO Auto-generated constructor stub
		m_SOSAlert = new SOSAlert(CommonBaseClass.m_objDbOperations);
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	public static  HashMap<Integer, Map<String, Object>> mSetMedicalAlert(int iSocietyID,String sMemeberName,String sAlertType,String UnitNo,int iAlertStatus,int isosType,String OwnerContact,String sWing,String sFloorNo)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		 long  mpMedical = m_SOSAlert.mSetMedicalEmergency(iSocietyID,sMemeberName,sAlertType,UnitNo,iAlertStatus,isosType,OwnerContact,sWing,sFloorNo);
		
		
		 
		if(mpMedical > 0)
		{
			//add bill to map
			 rows2.put("MedicalAlert",mpMedical);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("MedicalAlert","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mSetFireAlert(int iSocietyID,String sMemeberName,String sAlertType,String UnitNo,int iAlertStatus,int isosType,String OwnerContact,String sWing,String sFloorNo)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		 long  mpFire = m_SOSAlert.mSetFireEmergency(iSocietyID,sMemeberName,sAlertType,UnitNo,iAlertStatus,isosType,OwnerContact,sWing,sFloorNo);
		
		
		 
		if(mpFire > 0)
		{
			//add bill to map
			 rows2.put("FireAlert",mpFire);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("FireAlert","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mSetLiftAlert(int iSocietyID,String sMemeberName,String sAlertType,String UnitNo,int iAlertStatus,int isosType,String OwnerContact,String sWing,String sFloorNo)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		 long  mpLift = m_SOSAlert.mSetLiftEmergency(iSocietyID,sMemeberName,sAlertType,UnitNo,iAlertStatus,isosType,OwnerContact,sWing,sFloorNo);
		
		
		 
		if(mpLift > 0)
		{
			//add bill to map
			 rows2.put("LiftAlert",mpLift);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("LiftAlert","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mSetOtherAlert(int iSocietyID,String sMemeberName,String sAlertType,String UnitNo,int iAlertStatus,int isosType,String OwnerContact,String sWing,String sFloorNo)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		 long  mpOther = m_SOSAlert.mSetOtherEmergency(iSocietyID,sMemeberName,sAlertType,UnitNo,iAlertStatus,isosType,OwnerContact,sWing,sFloorNo);
		
		
		 
		if(mpOther > 0)
		{
			//add bill to map
			 rows2.put("OtherAlert",mpOther);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("OtherAlert","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mResolveByMe(int iSocietyID,int iAlertId,String sMemeberName,String sRole,int iAlertStatus)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		 long  mpResolved = m_SOSAlert.mSetResovleByMe(iSocietyID,iAlertId,sMemeberName,sRole,iAlertStatus);
		
		
		 
		if(mpResolved > 0)
		{
			//add bill to map
			 rows2.put("SOSUpdated",mpResolved);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("SOSUpdated","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mGetStatus(int iSocietyID,int iAlertID) throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpResolved = m_SOSAlert.mGetAlertSatatus(iSocietyID,iAlertID);
		System.out.println("mpResolved" +mpResolved);
		if(mpResolved.size() > 0)
		{
			//add member to map
			 rows2.put("AlertStatus",mpResolved);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("AlertStatus","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mFetchMember(int iUnitID) throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpMember = m_SOSAlert.mfetchMemberDetails(iUnitID);
		
		
		 
		if(mpMember.size() > 0)
		{
			//add member to map
			 rows2.put("memberdetails",mpMember);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("memberdetails","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mGetSMDatabase(int iSocietyID) throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		String sSecurityDB = m_Utility.getDBNames(iSocietyID);
		if(sSecurityDB != "")
		{
			//add member to map
			 rows2.put("smDatabse",sSecurityDB);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("smDatabse","DatabaseNotExist");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mSOSAlert(int iSocietyID) throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpCommetee = m_SOSAlert.mFetchSOSEmergency(iSocietyID);
		//fetching all bills
		
		if(mpCommetee.size() > 0)
		{
			//add member to map
			 rows2.put("SOSCommetee",mpCommetee);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("SOSCommetee","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	/* Get Data In Background Visitor Entry */
	public static  HashMap<Integer, Map<String, Object>> mfetchVisitor(int iSocietyID, int iUnitId) throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mFetchIncommingVisitor = m_SOSAlert.mFetchIncommingVisitor(iSocietyID,iUnitId);
		 //long  mFetchIncommingVisitor = m_SOSAlert.mFetchIncommingVisitor(iSocietyID,iUnitId);
		
		
		 
		if(mFetchIncommingVisitor.size() > 0)
		{
			//add bill to map
			 rows2.put("IncommingVisitor",mFetchIncommingVisitor);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("IncommingVisitor","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static void main(String[] args) throws Exception
	{
		String sToken ="wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQKFcsyFFuDnfF5jHFl0oGVsBP7_Gxu6S1vCjKxUP43II5jfx-O3ETIIW4b3XJ6BY1dFuj0hidsu8qX3n2PrV7gTlcMtKXq_wpr03xJqQn5Xtg";
		//sToken ="wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQI6Yg08ysk-HQELgJW36dHO_HTmUfWF_jp3PfcNder1x1UQ-5M-KAQuKT2A6DsZyhbXbokv6lkoTBHloHnGuIOANbQAKLrxKnNL7dnfNymNLw";
		String sTkey = "3HsMjKzOIfQGI-61QBpFmitTPV-QfZzrdQ8pfsxRnAELmbuSu7yszC4CopTaZFOcj6WGBlmJEFknFxreNj34sxYjqmHBKMuN6n4CmA4iNgaJwbQ6VOCMRj4YLTPBQCLrQExYq4NVFLgZIN8Ywc2HKVMJIFhvKPz07WDL-hgPuBw";
		
		//sTkey = "nqP7yCbTzmntnfeAa7tzVjUYqDqaTgnAA6aN5AUQBqx_0frfaUBOTdsf63nFb3se0BUWPOahJySq45usYomP3-7z1KHt19eSFCKSJtJT7qe60K5dnKMtCQ0ZhJGpNKv0-dN7D_EdUzf3KCgva7Xi8A";//501 member
	
		
		ViewSOS obj = new ViewSOS(sToken, true, sTkey);		
		HashMap rows =  obj.mResolveByMe(59,88,"Sujit Kumar","Self",2);
		//HashMap rows =  obj.mfetchMemberDetails(27);
		//System.out.println(obj.convert(rows));
	System.out.println(rows);	
	}

}
