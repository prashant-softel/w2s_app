package MainClasses;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import CommonUtilityFunctions.MapUtility;

public class ViewBanks extends CommonBaseClass 
{
	static Banks m_Banks;
	//static Payment m_Payment;
	public  ViewBanks(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
		m_Banks = new Banks(CommonBaseClass.m_objDbOperations);
		//m_Payment = new Payment(CommonBaseClass.m_objDbOperations);
		
	}
		
	public static  HashMap<Integer, Map<String, Object>> mfetchNEFTBanks()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpBanks = m_Banks.mfetchNEFTBanks();
		
		if(mpBanks.size() > 0)
		{
			//add bill to map
			 rows2.put("banks",MapUtility.HashMaptoList (mpBanks));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("banks","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> fetchPayerBanks(int iUnitId)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpBanks = m_Banks. fetchPayerBanks(iUnitId);
		
		if(mpBanks.size() > 0)
		{
			//add bill to map
			 rows2.put("banks",MapUtility.HashMaptoList (mpBanks));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("banks","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mSetNEFTTransaction(int iLoginID, int iUnitID, int iBankid, int iBillType, double lAmount, String sTransactionID, String sTxnDate, String sNote, String sPayerBank, String sPayerBranch) throws SQLException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//Insert NEFT transaction 
		int iRetValue = m_Banks.mSetNEFTTransaction(iLoginID, iUnitID, iBankid, iBillType, lAmount, sTransactionID, sTxnDate, sNote, sPayerBank, sPayerBranch);
	  
		if(iRetValue == 1)
		{
			//add bill to map
			 rows.put("success",1);
			 rows2.put("message","NEFT payment recorded successfully");
			 rows.put("response",rows2);
		}
		else
		{
			//bills not found
			rows.put("success",0);
			rows2.put("message","Unable to record NEFT Payment. Please try again.");
			 rows.put("response",rows2);
		}
	  
	    return rows;
	}
	
	public static void main(String[] args) throws Exception
	{
		String sToken = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQJYoY9FIePV9HZk2oayQ05RzYXQVr4gFSUpnEFB4NUR8b9slofmvFxSSzNLfHxq4csyTMst4cwvyTaAkVzmVH055jdwnlVF1A7xcqzEPzoriw";
		String sTkey = "OxzMyuL-qLwVOU-5FECvFKLl_jzsNDYSub4_EmTn1KjODkErEqLvxcXojWGV_vMJtG9RUEDEORAGW0Pov1mkm35aVanANUvnov24tq51YP4442HfuoTktXVwpjz3yCvA0uA9fvtWMU-gcLaK66Mwx5fteRrHKHfFRpRTC9Y7Sqs";
		
		ViewBanks obj = new ViewBanks(sToken,true,sTkey);
		//HashMap rows =  obj.mfetchNEFTBanks();
		//HashMap rows = obj.mSetNEFTTransaction(822, 14, 92, 0, 5000, "TRNXTEST123", "2017-04-30", "Java NEFT", "ICICI", "Borivali Branch");
		HashMap rows = fetchPayerBanks(46);
		//System.out.println(obj.convert(rows));
		System.out.println(rows);	
		
	}
}


