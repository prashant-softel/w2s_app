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
import MainClasses.Receipt;
import MainClasses.DebitCrediNote;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;

public class MemberLedger  extends CommonBaseClass{

	static Bill m_Bill;
	static Receipt m_Receipt;
	static DebitCrediNote m_DebitNotes;
	
	public  MemberLedger(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		
		m_Bill = new Bill(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Receipt = new Receipt(CommonBaseClass.m_objDbOperations);	
	}
	/*  --------------------------------------    Old App Code  ------------------------------------    */
	public static  HashMap<Integer, Map<String, Object>> mfetchBillNReceiptsDetails(int iUnitId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		/*********** Fetching all bill of given unit id ****************/
		HashMap<Integer, Map<String, Object>> mpBills = m_Bill.mfetchBills(iUnitId,0,false);

		/*if(mpBills.size() > 0)
		{
			/// update bill description ///
			for(Entry<Integer, Map<String, Object>> entry : mpBills.entrySet()) 
			{
				/// check if bill is opening bill ///
				if(entry.getValue().get("PeriodID") != null)
				{
					if(m_Bill.getIsOpeningBill((int)entry.getValue().get("PeriodID")) == true)
					{
						entry.getValue().put("BillFor","Opening Balance");
						entry.getValue().put("mode","Opening Balance");
					}
					else
					{
						entry.getValue().put("BillFor",m_Bill.GetBillFor((int)entry.getValue().get("PeriodID")));
						
					}
				}	
			}
		}*/
		
		/************ fetching receipts of given unit *******************/
		HashMap<Integer, Map<String, Object>> mpReceipts = m_Receipt.mfetchReceipts(iUnitId, 0, false);
		
		HashMap<Integer, Map<String, Object>> map3 = new HashMap <Integer, Map<String, Object>>();
		
		if(mpBills.size() > 0)
		{	
			//map3.putAll(mpBills);
			/********merge recipts to new map**********/
			int mVal = 0;
			for(Entry<Integer, Map<String, Object>> entryBills : mpBills.entrySet()) 
			{
				
				//map3.put("map_id", entry.getValue().get("map_id"));
				//System.out.println("iVal"+iVal);
				
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("Mode", "Bill");
				tmpMap.put("Date", entryBills.getValue().get("bill_date"));
				tmpMap.put("Credit",0.00);
				tmpMap.put("Debit",entryBills.getValue().get("CurrentBillAmount"));
				tmpMap.put("BillFor", entryBills.getValue().get("BillFor"));
				tmpMap.put("PeriodID", entryBills.getValue().get("PeriodID"));
				tmpMap.put("sdate", entryBills.getValue().get("sdate"));
				tmpMap.put("BalanceAmount",0.00);
				tmpMap.put("IsOpeningBill",entryBills.getValue().get("IsOpeningBill"));
				tmpMap.put("BillType",entryBills.getValue().get("BillType"));
				tmpMap.put("UnitID",entryBills.getValue().get("BillUnitID"));
				tmpMap.put("SocietyCode",entryBills.getValue().get("society_code"));
				tmpMap.put("BillYear",entryBills.getValue().get("YEAR"));
				//System.out.println("CurrentBillAmount" + entryBills.getValue().get("CurrentBillAmount")+"F");
				map3.put(mVal,tmpMap);
				mVal++;
				
				
			}
		}
		
		int iVal = 0;
		if(map3.size() > 0)
		{
			iVal = map3.size();
		}
		
	
		
		
		if(mpReceipts.size() > 0)
		{	
			/********merge recipts to new map**********/
			for(Entry<Integer, Map<String, Object>> entryPayments : mpReceipts.entrySet()) 
			{
				
				//map3.put("map_id", entry.getValue().get("map_id"));
				//System.out.println("iVal"+iVal);
				//map3.put(iVal, entryPayments.getValue());
				
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("Mode", "Receipt");
				tmpMap.put("ChequeNumber", entryPayments.getValue().get("ChequeNumber"));
				tmpMap.put("Date", entryPayments.getValue().get("ChequeDate"));
				tmpMap.put("Credit",entryPayments.getValue().get("Amount"));
				tmpMap.put("Debit",0.00);
				tmpMap.put("PeriodID", entryPayments.getValue().get("PeriodID"));
				tmpMap.put("sdate", entryPayments.getValue().get("sdate"));
				tmpMap.put("BalanceAmount",0.00);
				tmpMap.put("BillType",entryPayments.getValue().get("BillType"));
				tmpMap.put("UnitID",entryPayments.getValue().get("PaidBy"));
				
				map3.put(iVal,tmpMap);
				iVal++;
				
				
			}
		}
		
		
		//if(map3.size() > 0 )
		//{	
		// rows2.put("BillnReceipts",MapUtility.HashMaptoList(map3));
		//}
		
		/********merge inDebit/Credit Note to new map**********/
		HashMap<Integer, Map<String, Object>> mpCreditDebit = m_DebitNotes.mfetchDebitNoteold(iUnitId, false);
		if(mpCreditDebit.size() >0)
		{
			for(Entry<Integer, Map<String, Object>> entryCreditNotes : mpCreditDebit.entrySet()) 
			{
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("Mode",  "Receipt");   /// Reflact Entry In Note 
				tmpMap.put("Date", entryCreditNotes.getValue().get("bill_date"));
				tmpMap.put("ChequeNumber", "0");
				if(entryCreditNotes.getValue().get("mode").equals("CNote"))
				{
					tmpMap.put("Credit",entryCreditNotes.getValue().get("totalPayable"));
					tmpMap.put("Debit",0.00);
				}
				
				else if(entryCreditNotes.getValue().get("mode").equals("DNote") || entryCreditNotes.getValue().get("mode").equals("SInvoice"))
				{
					tmpMap.put("Debit",entryCreditNotes.getValue().get("totalPayable"));
					tmpMap.put("Credit",0.00);
				}
				
				//tmpMap.put("Credit",entryCreditNotes.getValue().get("Amount"));
				//tmpMap.put("Debit",0.00);
				tmpMap.put("PeriodID",95);
				tmpMap.put("sdate", entryCreditNotes.getValue().get("bill_date"));
				tmpMap.put("BalanceAmount",0.00);
				tmpMap.put("BillType","false");
				tmpMap.put("UnitID",28);
				map3.put(iVal,tmpMap);
				iVal++;
			}
			
		}
		if(map3.size() > 0 )
		{	
		 rows2.put("BillnReceipts",MapUtility.HashMaptoList(map3));
		}
		if(rows2.size() > 0)
		{	
		 rows.put("response",rows2);
		 rows.put("success",1);
		}
	  
	    return rows;
	}
	
	
	
	/*  --------------------------------------    New App Code  ------------------------------------    */
	
	public static  HashMap<Integer, Map<String, Object>> mfetchBillNReceiptsDetailsNew(int iUnitId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		/*********** Fetching all bill of given unit id ****************/
		HashMap<Integer, Map<String, Object>> mpBills = m_Bill.mfetchBills(iUnitId,114,false);

		
		/************ fetching receipts of given unit *******************/
		HashMap<Integer, Map<String, Object>> mpReceipts = m_Receipt.mfetchReceipts(iUnitId, 0, false);
		
		HashMap<Integer, Map<String, Object>> mpCreditDebit = m_DebitNotes.mfetchDebitNote(iUnitId, false);
		
		HashMap<Integer, Map<String, Object>> mpSaleInvoice = m_DebitNotes.mfetchSaleInvoice(iUnitId, false);
		HashMap<Integer, Map<String, Object>> map3 = new HashMap <Integer, Map<String, Object>>();
		

		if(mpBills.size() > 0)
		{	
			//map3.putAll(mpBills);
			/********merge recipts to new map**********/
			int mVal = 0;

			for(Entry<Integer, Map<String, Object>> entryBills : mpBills.entrySet()) 
			{
				

				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("Mode", "Bill");
				tmpMap.put("Date", entryBills.getValue().get("bill_date"));
				tmpMap.put("Credit",0.00);
				tmpMap.put("Debit",entryBills.getValue().get("CurrentBillAmount"));
				tmpMap.put("BillFor", entryBills.getValue().get("BillFor"));
				tmpMap.put("PeriodID", entryBills.getValue().get("PeriodID"));
				tmpMap.put("sdate", entryBills.getValue().get("sdate"));
				tmpMap.put("BalanceAmount",0.00);
				tmpMap.put("IsOpeningBill",entryBills.getValue().get("IsOpeningBill"));
				tmpMap.put("BillType",entryBills.getValue().get("BillType"));
				tmpMap.put("UnitID",entryBills.getValue().get("BillUnitID"));
				tmpMap.put("Type",0);
				tmpMap.put("SocietyCode",entryBills.getValue().get("society_code"));
				tmpMap.put("BillYear",entryBills.getValue().get("year"));
				

				//System.out.println("CurrentBillAmount" + entryBills.getValue().get("CurrentBillAmount")+"F");
				map3.put(mVal,tmpMap);
				mVal++;
				
				
			}
		}
		
		int iVal = 0;
		if(map3.size() > 0)
		{
			iVal = map3.size();
		}
		
	

		/********merge inDebit/Credit Note to new map**********/
		
		if(mpCreditDebit.size() >0)
		{
			System.out.println("====================================================");
			System.out.println("mpCreditDebit : " + mpCreditDebit);
			System.out.println("====================================================");
			for(Entry<Integer, Map<String, Object>> entryCreditNotes : mpCreditDebit.entrySet()) 
			{
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("Mode",  entryCreditNotes.getValue().get("mode"));   /// Reflact Entry In Note 
				tmpMap.put("Date", entryCreditNotes.getValue().get("bill_date"));

				tmpMap.put("Cd_id",entryCreditNotes.getValue().get("ID"));
				if(entryCreditNotes.getValue().get("mode").equals("CNote"))
				{
					tmpMap.put("Credit",entryCreditNotes.getValue().get("totalPayable"));
					tmpMap.put("Debit",0.00);
				}
				
				else if(entryCreditNotes.getValue().get("mode").equals("DNote"))
				{
					tmpMap.put("Debit",entryCreditNotes.getValue().get("totalPayable"));
					tmpMap.put("Credit",0.00);
				}
				tmpMap.put("BalanceAmount",0.00);
				tmpMap.put("BillType",entryCreditNotes.getValue().get("BillType"));
				tmpMap.put("UnitID",iUnitId);
				tmpMap.put("sdate", entryCreditNotes.getValue().get("sdate"));
				tmpMap.put("Type",0);
				map3.put(iVal,tmpMap);
				iVal++;
			}
			
		}
		/* -------------------- Sale Invoice --------------------   */
		
		if(mpSaleInvoice.size() >0)
		{
			for(Entry<Integer, Map<String, Object>> entrySaleInvoice : mpSaleInvoice.entrySet()) 
			{
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("Mode",  "SInvoice");   /// Reflact Entry In Note 
				tmpMap.put("Inv_Number",entrySaleInvoice.getValue().get("Inv_Number"));
				tmpMap.put("Inv_id",entrySaleInvoice.getValue().get("ID"));
				tmpMap.put("Date", entrySaleInvoice.getValue().get("bill_date"));
				//tmpMap.put("Credit",entryCreditNotes.getValue().get("totalPayable"));
				//tmpMap.put("Debit",0.00);
				tmpMap.put("Debit",entrySaleInvoice.getValue().get("totalPayable"));
				tmpMap.put("Credit",0.00);
				tmpMap.put("BalanceAmount",0.00);
				tmpMap.put("BillType",false);
				tmpMap.put("UnitID",iUnitId);
				tmpMap.put("sdate", entrySaleInvoice.getValue().get("sdate"));
				tmpMap.put("Type",1);
				map3.put(iVal,tmpMap);
				iVal++;
			}
			
		}
		
		if(mpReceipts.size() > 0)
		{	
			/********merge recipts to new map**********/
			for(Entry<Integer, Map<String, Object>> entryPayments : mpReceipts.entrySet()) 
			{
				
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("Mode", "Receipt");
				tmpMap.put("ChequeNumber", entryPayments.getValue().get("ChequeNumber"));
				tmpMap.put("Date", entryPayments.getValue().get("ChequeDate"));
				tmpMap.put("Credit",entryPayments.getValue().get("Amount"));
				tmpMap.put("Debit",0.00);
				tmpMap.put("PeriodID", entryPayments.getValue().get("PeriodID"));
				tmpMap.put("sdate", entryPayments.getValue().get("sdate"));
				tmpMap.put("BalanceAmount",0.00);
				tmpMap.put("BillType",entryPayments.getValue().get("BillType"));
				tmpMap.put("UnitID",entryPayments.getValue().get("PaidBy"));
				tmpMap.put("Type",0);
				map3.put(iVal,tmpMap);
				iVal++;
				
				
			}
		}
		
		
		
		
		if(map3.size() > 0 )
		{	
		 rows2.put("BillnReceipts",MapUtility.HashMaptoList(map3));
		}
		if(rows2.size() > 0)
		{	
		 rows.put("response",rows2);
		 rows.put("success",1);
		}
	  
	    return rows;
	}
	public static void main(String[] args) throws Exception
	{
		String tkey="l0sJ8gVHcNWuuRLtae6usyProdK5bNbNXIJJjW8ndhndasc5O5HL4-8ZB7ZCPTzBCfSm6LLAVSfVFa4JvvLPYeZVQEY5ywuFkLLJLOgW1sFMwAQkPhVVXwbk4h_i6BmfQSmTk0ZvfTnYRrSX_cTM6g";
		String token ="OFCea7Iz2XW2_z-UXCQetaK3ZDfY_NqDOi5TFlZ5U4wErYWsoi0t0ioDT0RjLYzTj2GAC6N35SI58xkRTRTtzHBwKY8LS8HyFbLtSwy4lBgwrQdW-JIuRhNKL2FLNnDN";
		MemberLedger obj = new MemberLedger(token,true,tkey);
		HashMap rows =  obj.mfetchBillNReceiptsDetailsNew(56);
		System.out.println("mfetchBills Details : " + rows);	
		
		
		
	}
	
	
	

}
