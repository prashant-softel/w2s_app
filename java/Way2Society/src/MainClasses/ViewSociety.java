package MainClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import CommonUtilityFunctions.MapUtility;
import MainClasses.DbOperations.*;

public class ViewSociety extends CommonBaseClass {
	static DbOperations m_dbConn;
	static Utility m_Utility;
	
	public ViewSociety(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_dbConn = CommonBaseClass.m_objDbOperations;
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchSocietyDetails(int iSocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		String sSocietyDetail =  "SELECT * FROM `society` where society_id='"+iSocietyID+"'";
		
		HashMap<Integer, Map<String, Object>> mSocietyDetail =  m_dbConn.Select(sSocietyDetail);
	
		if(mSocietyDetail.size() > 0)
		{
			//add society details to map
			 rows.put("success",1);
			 rows2.put("society",MapUtility.HashMaptoList(mSocietyDetail));
			 rows.put("response",rows2);
			 
		}
		else
		{
			//society not found not found
			rows.put("success",0); 
			rows2.put("society","");
			 rows.put("response",rows2);
			 
		}
		return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> fetchPaymentGateway(int iSocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		String sPaymentGateway =  "SELECT `PaymentGateway`,`Paytm_Link`,`Record_NEFT` FROM `society` where society_id='"+iSocietyID+"'";
		
		HashMap<Integer, Map<String, Object>> mPaymentGateway =  m_dbConn.Select(sPaymentGateway);
	
		if(mPaymentGateway.size() > 0)
		{
			//add society details to map
			 rows2.put("PaymentGateway",MapUtility.HashMaptoList(mPaymentGateway));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//paymentGateway not found not found
			 rows2.put("PaymentGateway","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
		return rows;
	}

	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String token = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQJ-DGHaFiGlp2Rk_X2ezeBa0PXY03aG1I7FHYrEAi67VjdgHWvMAtUMIzOBLyUOr4u5IKiLAdA0WSev2WLpwEJHfD5hx7pFuSoiF001JOdG4Q";
		String tkey = "RD_92lLXmmu1JoRV8A2YgyeA4BKVd4TW_DcMzxZIUIRv5KVdDhZJIeCDv0qQe5WLFecyhnkO2eIf6meBiW49yyaFTHH7hm7j1936d0pPXv-DgvxrtGVPrTCGWIBMPZmR77PnU18hHMR1pxLGvEYUZw";
		ViewSociety obj = new ViewSociety(token,true, tkey);
		int iSocietyID = Integer.parseInt(((String)obj.getDecryptedTokenMap().get("society_id")));
		HashMap rows =  obj.fetchPaymentGateway(iSocietyID);
		//System.out.println(obj.convert(rows));
		System.out.println(rows);	
	}

}
