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
import MainClasses.Bill;
import MainClasses.Payment;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;

public class ViewBills extends CommonBaseClass 
{
	static Bill m_Bill;
	static Payment m_Payment;
	public  ViewBills(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
		m_Bill = new Bill(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Payment = new Payment(CommonBaseClass.m_objDbOperations);
		
	}
	
	
	public static  HashMap<Integer, Map<String, Object>> mfetchAllBills(int iUnitId)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpBills = m_Bill.mfetchBills(iUnitId, 0, false);
		
		
		 
		if(mpBills.size() > 0)
		{
			//add bill to map
			 rows2.put("bill",MapUtility.HashMaptoList(mpBills));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("bill","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}

	
	public static  HashMap<Integer, Map<String, Object>> mfetchMemberBill(int iUnitId,int iPeriodID,int SupplementaryBill)
	{
		//fetch complete bill details for member according to period
		HashMap<Integer, Map<String, Object>> rows = m_Bill.mfetchBillDetails(iUnitId,iPeriodID,SupplementaryBill);
		return rows;
	}
	
	public static void main(String[] args) throws Exception
	{
		ViewBills obj = new ViewBills("OFCea7Iz2XW2_z-UXCQetaK3ZDfY_NqDOi5TFlZ5U4wErYWsoi0t0ioDT0RjLYzTFnuBurIuWd0g2HgY-6yc6h01HN572NGCBS-Z2WQQDqnaLfbQGP8rZOsxWgrJ9SIb", true, "pELEtTUPXrjGbvf5gY2MN8_0foV7q2poBw5IoyKGJui1I6Jg5KRPKPCirlo7dxNULicuT3n_QN104lv5JedjQYpcBaSsAr_nkgNSXzYaS7K9wvu7lD4zPfZra7HlicSQTMPT-7l3tuI3J_zsnopnHg");
		HashMap rows =  obj.mfetchMemberBill(16,114,0);
//		System.out.println(obj.convert(rows));
		System.out.println(rows);	
	}

}


