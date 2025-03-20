package MainClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import CommonUtilityFunctions.MapUtility;
import MainClasses.DbOperations.*;

public class Bill
{
	static DbOperations m_dbConn;
	static DbRootOperations m_dbConnRoot;
	static Utility m_Utility;
	
	public Bill(DbOperations m_dbConnObj, DbRootOperations  m_dbConnRootObj) throws ClassNotFoundException
	{
		m_dbConn = m_dbConnObj;
		m_dbConnRoot = m_dbConnRootObj;
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	
	public static HashMap<Integer, Map<String, Object>> mfetchBills(int iUnitId, int iPeriodID, boolean bIsFetchLatest)
	{
		
		String sSqlBill = "";
		
		if(iPeriodID == 0 && bIsFetchLatest)
		{
			//System.out.println("Inside IF");
			//fetch latest bill only
			//bill.BillSubTotal+bill.AdjustmentCredit+bill.BillTax+bill.IGST+bill.CGST+bill.SGST + bill.CESS+bill.BillInterest as CurrentBillAmount
			sSqlBill =  "Select DATE_FORMAT(billreg.BillDate,'%d %b, %Y') as bill_date, billreg.BillType as bill_type, DATE_FORMAT(billreg.BillDate,'%Y-%m-%d') as sdate,DATE_FORMAT(billreg.DueDate,'%d %b, %Y') as due_date,period.Type as Month, yr.YearDescription as Year,bill.UnitID as BillUnitID,bill.PeriodID as BillPeriodID,bill.BillSubTotal+bill.AdjustmentCredit+bill.BillTax+bill.IGST+bill.CGST+bill.SGST + bill.CESS+bill.BillInterest as CurrentBillAmount , bill.`TotalBillPayable`, bill.BillType, society.society_code,YEAR(period.BeginingDate) AS year from billdetails as bill JOIN period as period ON bill.PeriodID = period.id JOIN year as yr ON yr.YearID=period.YearID JOIN billregister as billreg ON bill.PeriodID = billreg.PeriodID JOIN society ON society.society_id = billreg.SocietyID WHERE bill.UnitID= '" + iUnitId + "' AND bill.PeriodID= '" + iPeriodID + "' group by bill.PeriodID order by billreg.BillDate DESC limit 0,1";
			System.out.println(sSqlBill);		
		}
		else if(iPeriodID == 0 && !bIsFetchLatest)
		{
			//fetch all bills
			//changes on 17082019 issue in m-bill not show 
			sSqlBill =  "Select  bill.PeriodID,DATE_FORMAT(billreg.BillDate,'%d %b, %Y') as bill_date, billreg.BillType as bill_type, DATE_FORMAT(billreg.BillDate,'%Y-%m-%d') as sdate,DATE_FORMAT(billreg.DueDate,'%d %b, %Y') as due_date,period.Type as Month, yr.YearDescription as Year,bill.UnitID as BillUnitID,bill.PeriodID as BillPeriodID,bill.BillSubTotal+bill.AdjustmentCredit+bill.BillTax+bill.IGST+bill.CGST+bill.SGST + bill.CESS+bill.BillInterest as CurrentBillAmount, bill.`TotalBillPayable`, bill.BillType, society.society_code, YEAR(period.BeginingDate) AS year from billdetails as bill JOIN period as period ON bill.PeriodID = period.id JOIN year as yr ON yr.YearID=period.YearID JOIN billregister as billreg ON bill.PeriodID = billreg.PeriodID JOIN society ON society.society_id = billreg.SocietyID where bill.UnitID= '" + iUnitId + "' group by bill.PeriodID order by billreg.BillDate DESC";
			sSqlBill =  "Select  bill.PeriodID,DATE_FORMAT(billreg.BillDate,'%d %b, %Y') as bill_date, billreg.BillType as bill_type, DATE_FORMAT(billreg.BillDate,'%Y-%m-%d') as sdate,DATE_FORMAT(billreg.DueDate,'%d %b, %Y') as due_date,period.Type as Month, yr.YearDescription as Year,bill.UnitID as BillUnitID,bill.PeriodID as BillPeriodID,bill.BillSubTotal+bill.AdjustmentCredit+bill.BillTax+bill.IGST+bill.CGST+bill.SGST + bill.CESS+bill.BillInterest as CurrentBillAmount, bill.`TotalBillPayable`, bill.BillType, society.society_code, YEAR(period.BeginingDate) AS year from billdetails as bill JOIN period as period ON bill.PeriodID = period.id JOIN year as yr ON yr.YearID=period.YearID JOIN billregister as billreg ON bill.PeriodID = billreg.PeriodID JOIN society ON society.society_id = billreg.SocietyID where bill.UnitID= '" + iUnitId + "' group by bill.BillRegisterID order by billreg.BillDate DESC";
			//System.out.println("Inside Else IF");
		}
		else
		{
			
			//sSqlBill =  "Select  DATE_FORMAT(billreg.BillDate,'%d %b, %Y') as bill_date, billreg.BillType as bill_type, DATE_FORMAT(billreg.BillDate,'%Y-%m-%d') as sdate,DATE_FORMAT(billreg.DueDate,'%d %b, %Y') as due_date, period.Type as Month, yr.YearDescription as Year,bill.UnitID as BillUnitID,bill.PeriodID as BillPeriodID,bill.BillSubTotal+bill.AdjustmentCredit+bill.BillTax+bill.IGST+bill.CGST+bill.SGST + bill.CESS+bill.BillInterest as CurrentBillAmount, bill.`TotalBillPayable`, bill.BillType from billdetails as bill JOIN period as period ON bill.PeriodID = period.id JOIN year as yr ON yr.YearID=period.YearID JOIN billregister as billreg ON bill.PeriodID = billreg.PeriodID where bill.UnitID= '" + iUnitId + "' and bill.PeriodID= '" + iPeriodID + "' group by bill.PeriodID order by billreg.BillDate DESC";
			sSqlBill =  "Select  DATE_FORMAT(billreg.BillDate,'%d %b, %Y') as bill_date, billreg.BillType as bill_type, DATE_FORMAT(billreg.BillDate,'%Y-%m-%d') as sdate,DATE_FORMAT(billreg.DueDate,'%d %b, %Y') as due_date, period.Type as Month, yr.YearDescription as Year,bill.UnitID as BillUnitID,bill.PeriodID as BillPeriodID,bill.BillSubTotal+bill.AdjustmentCredit+bill.BillTax+bill.IGST+bill.CGST+bill.SGST + bill.CESS+bill.BillInterest as CurrentBillAmount, bill.`TotalBillPayable`, bill.BillType, society.society_code, YEAR(period.BeginingDate) AS year from billdetails as bill JOIN period as period ON bill.PeriodID = period.id JOIN year as yr ON yr.YearID=period.YearID JOIN billregister as billreg ON bill.PeriodID = billreg.PeriodID JOIN society ON society.society_id = billreg.SocietyID where bill.UnitID= '" + iUnitId + "' and bill.PeriodID= '" + iPeriodID + "' group by bill.BillRegisterID order by billreg.BillDate DESC";
			System.out.println(sSqlBill);
			//System.out.println("Inside Else");
		}
		
		System.out.println(sSqlBill);
		HashMap<Integer, Map<String, Object>> mpBills =  m_dbConn.Select(sSqlBill);
		
		if(mpBills.size() > 0 && !bIsFetchLatest)
		{
			/************* update bill description ***************/
			for(Entry<Integer, Map<String, Object>> entry : mpBills.entrySet()) 
			{
				/************* check if bill is opening bill ************/
				if(entry.getValue().get("PeriodID") != null)
				{
					if(getIsOpeningBill((int)entry.getValue().get("PeriodID")) == true)
					{
						entry.getValue().put("BillFor","Opening Balance");
						entry.getValue().put("IsOpeningBill",1);
						entry.getValue().put("bill_date",m_Utility.getSocietyBeginingDate());
						
						
						
					}
					else
					{
						entry.getValue().put("BillFor",GetBillFor((int)entry.getValue().get("PeriodID")));
						entry.getValue().put("IsOpeningBill",0);
						
					}
				}	
			}
		}
				
		return mpBills;
	}
	
	public static boolean  getIsOpeningBill(int periodID)
	{
		/**** check if bill is opening bill *****/
		String sSql= "";
		int iSocietyCreationYearID = 0;
		
		//fetch society creation year id
		String sSqlSociety = "Select society_creation_yearid from society";
		HashMap<Integer, Map<String, Object>> mpSociety=  m_dbConn.Select(sSqlSociety);
		
		
		Map.Entry<Integer, Map<String, Object>> entrySociety = mpSociety.entrySet().iterator().next();
		iSocietyCreationYearID = (int) entrySociety.getValue().get("society_creation_yearid");
		
		sSql = "Select YearID, IsYearEnd from period where ID = '"+ periodID + "' ";
		HashMap<Integer, Map<String, Object>> mpPeriod=  m_dbConn.Select(sSql);
		
		for(Entry<Integer, Map<String, Object>> entry : mpPeriod.entrySet()) 
		{
			if(iSocietyCreationYearID > 0)
			{
				if((boolean) entry.getValue().get("IsYearEnd") == true && (int) entry.getValue().get("YearID") == iSocietyCreationYearID -1)
				{
					return true;
				}
			}	
		}
						
		return false;
	}
	
	public static HashMap<Integer, Map<String, Object>>  getBillRegisterDetailsByID(Object ID)
	{
		String sSql= "";
		
		sSql = "Select * from billregister where ID  = '"+ ID + "' ";
		HashMap<Integer, Map<String, Object>> mpBillReg=  m_dbConn.Select(sSql);
		
		return mpBillReg;
	}
	
	public static String GetBillFor(int periodID)
	{
		//fetching bill description text
		String sSql= "";
		String RetrunVal = "";
		
		sSql = "SELECT periodtable.Type, yeartable.YearDescription FROM period AS periodtable JOIN year AS yeartable ON periodtable.YearID = yeartable.YearID WHERE periodtable.id ='"+ periodID + "' ";
		HashMap<Integer, Map<String, Object>> mpBillFor=  m_dbConn.Select(sSql);
		
		if(mpBillFor.size() > 0)
		{
			Map.Entry<Integer, Map<String, Object>> entry = mpBillFor.entrySet().iterator().next();
			RetrunVal = (String) entry.getValue().get("Type");
			RetrunVal += " " + (String) entry.getValue().get("YearDescription");
		}
		else
		{
			RetrunVal =  "FetchData::GetBillFor - No Data Found from Period table of societies database.";
		}
		return RetrunVal;
	}

	public static HashMap<Integer, Map<String, Object>> getCollectionOfDetails(int iUnitId, int iPeriodID,int SupplementaryBill,boolean bIsFetchSignleBill)
	{
		
		String sqlQuery = "";
		String memberIDS = "";
		int InterestOnPrincipleDue = 0;
		int AdjustCredits = 0;
		int IGST = 0;
		int CGST = 0;
		int SGST = 0;
		int CESS = 0;
		String BillDate ="";
		
		/******* fetching app defaults **********/
		String sqlDefaults = "SELECT * FROM `appdefault`";
		HashMap<Integer, Map<String, Object>> resultDefaults =  m_dbConn.Select(sqlDefaults);
		
		if(resultDefaults.size() > 0)
		{
			Map.Entry<Integer, Map<String, Object>> entry = resultDefaults.entrySet().iterator().next();
			InterestOnPrincipleDue = (Integer)  entry.getValue().get("APP_DEFAULT_INTEREST_ON_PRINCIPLE_DUE");
			AdjustCredits = (Integer)  entry.getValue().get("APP_DEFAULT_ADJUSTMENT_CREDIT");
			IGST = (Integer)  entry.getValue().get("APP_DEFAULT_IGST");
			CGST = (Integer)  entry.getValue().get("APP_DEFAULT_CGST");
			SGST = (Integer)  entry.getValue().get("APP_DEFAULT_SGST");
			CESS = (Integer)  entry.getValue().get("APP_DEFAULT_CESS");
		}
		
		/******* fetching bill register details **********/
		String sqlBillReg = "SELECT max(id),BillType,PeriodID,DATE_FORMAT(BillDate,'%Y-%m-%d') as BillDate,DATE_FORMAT(DueDate,'%Y-%m-%d') as DueDate FROM `billregister` where `PeriodID` = '"+ iPeriodID +"' and BillType='"+ SupplementaryBill +"'";
		HashMap<Integer, Map<String, Object>> resultBillReg =  m_dbConn.Select(sqlBillReg);
		
		if(resultBillReg.size() > 0)
		{	
			Map.Entry<Integer, Map<String, Object>> entryBillReg = resultBillReg.entrySet().iterator().next();
			BillDate = (String) entryBillReg.getValue().get("BillDate");
		}		
		
		/**********fetching ledgers *********/
		String sqlLedgers = "SELECT `id`,`ledger_name` FROM `ledger`  WHERE `id` IN(SELECT vch.`To` from `voucher` as vch JOIN `billdetails` as bill on bill.ID = vch.`RefNo` where `To` <> '' and `Credit` <> 0 and `RefTableID`=1 and bill.`PeriodID` = '"+ iPeriodID +"' ) "
				+ "and `id` NOT IN("+ InterestOnPrincipleDue +" ,"+ AdjustCredits +" ,"+ IGST +" ,"+ CGST +" ,"+ SGST +" ,"+ CESS +")";
		HashMap<Integer, Map<String, Object>> resultLedgers =  m_dbConn.Select(sqlLedgers);
		
		
		/********** fetch bill amount by ledgers id ********/
		sqlQuery= "SELECT vch.`RefNo`, uni.unit_no as UNIT_NO, mem.owner_name as OWNER_NAME, bill.BillNumber, ";
		System.out.println("select query"+sqlQuery);
		
		if(resultLedgers.size() > 0)
		{	
			/********ledger amount in voucher as column **********/
			int iCount = 0;
			for(Entry<Integer, Map<String, Object>> entryLedgers : resultLedgers.entrySet()) 
			{
				
				sqlQuery += " MAX( IF( vch.`To` =  '"+ entryLedgers.getValue().get("id") +"', vch.`Credit` , 0.00 ) ) AS '"+ entryLedgers.getValue().get("ledger_name")+"' ";
				
				if(iCount < (resultLedgers.size()))
				{
					sqlQuery += " , ";
				}
				
				iCount ++;
			}
			
		}	
		
		
		sqlQuery += " bill.`BillSubTotal` ,bill.`BillInterest` as InterestOnArrears," +
					" bill.`AdjustmentCredit` , bill.`PrincipalArrears` as PreviousPrincipalArrears , bill.`InterestArrears` as PreviousInterestArrears,"  +
					" bill.`IGST`, bill.`CGST`, bill.`SGST`, bill.`CESS`, " +
					" bill.`TotalBillPayable` as Payable" +
					" FROM voucher AS vch JOIN billdetails AS bill ON vch.RefNo = bill.ID JOIN member_main AS mem ON bill.UnitID = mem.unit" +
					" JOIN unit AS uni ON uni.unit_id = bill.UnitID LEFT JOIN chequeentrydetails as chq on bill.UnitID = chq.PaidBy WHERE bill.PeriodID ='" + iPeriodID +"' ";					
							
		if(iUnitId == 0)
		{
			memberIDS = m_Utility.getMemberIDs(BillDate);
			sqlQuery +="	and mem.member_id IN ("+ memberIDS +") ";
		}
		else
		{
			sqlQuery += "   and uni.unit_id = '"+ iUnitId + "'";
		}
		
		sqlQuery += "    and bill.BillType='"+ SupplementaryBill +"' ";
		sqlQuery += "	GROUP BY vch.`RefNo`  ORDER BY uni.sort_order ASC";
		
		HashMap<Integer, Map<String, Object>> mpBills =  m_dbConn.Select(sqlQuery);
		
		if(bIsFetchSignleBill == true)
		{
			if(mpBills.size() > 0)
			{
				Map<String, Object> mpSingleBillsDetails = new HashMap<String, Object>();
				Map<String, Object> mpSingleBillsParticulars = new HashMap<String, Object>();
				
				for(Entry<Integer, Map<String, Object>> entryBills : mpBills.entrySet()) 
				{
					if(entryBills.getValue().get("Date") == "UNIT_NO")
					{
						mpSingleBillsDetails.put("sdate", entryBills.getValue().get("sdate"));
					}	
				}
				
				
			}
		}
				
		return mpBills;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchBillDetails(int iUnitId, int iPeriodID,int SupplementaryBill)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap billdetailsPart2 = new HashMap<>();
		
		String sqlQuery = "";
		String memberIDS = "";
		int InterestOnPrincipleDue = 0;
		int AdjustCredits = 0;
		int IGST = 0;
		int CGST = 0;
		int SGST = 0;
		int CESS = 0;
		
		String BillDate ="";
		
		/******* fetching app defaults **********/
		String sqlDefaults = "SELECT * FROM `appdefault`";
		HashMap<Integer, Map<String, Object>> resultDefaults =  m_dbConn.Select(sqlDefaults);
		
		if(resultDefaults.size() > 0)
		{
			Map.Entry<Integer, Map<String, Object>> entry = resultDefaults.entrySet().iterator().next();
			InterestOnPrincipleDue = (Integer)  entry.getValue().get("APP_DEFAULT_INTEREST_ON_PRINCIPLE_DUE");
			AdjustCredits = (Integer)  entry.getValue().get("APP_DEFAULT_ADJUSTMENT_CREDIT");
			IGST = (Integer)  entry.getValue().get("APP_DEFAULT_IGST");
			CGST = (Integer)  entry.getValue().get("APP_DEFAULT_CGST");
			SGST = (Integer)  entry.getValue().get("APP_DEFAULT_SGST");
			CESS = (Integer)  entry.getValue().get("APP_DEFAULT_CESS");
		}
		
		/******* fetching bill register details **********/
		String sqlBillReg = "SELECT max(id),BillType,PeriodID,DATE_FORMAT(BillDate,'%Y-%m-%d') as BillDate,DATE_FORMAT(DueDate,'%Y-%m-%d') as DueDate,Notes FROM `billregister` where `PeriodID` = '"+ iPeriodID +"' and BillType='"+ SupplementaryBill +"'";
		HashMap<Integer, Map<String, Object>> resultBillReg =  m_dbConn.Select(sqlBillReg);
//		System.out.println("sqlbillreg"+sqlBillReg);
//		System.out.println("sqlbillreg"+resultBillReg);

		
		if(resultBillReg.size() > 0)
		{	
			Map.Entry<Integer, Map<String, Object>> entryBillReg = resultBillReg.entrySet().iterator().next();
			BillDate = (String) entryBillReg.getValue().get("BillDate");
		}		
		
		/**********fetching ledgers *********/
		String sqlLedgers = "SELECT `id`,`ledger_name` FROM `ledger`  WHERE `id` IN(SELECT vch.`To` from `voucher` as vch JOIN `billdetails` as bill on bill.ID = vch.`RefNo` where `To` <> '' and `Credit` <> 0 and `RefTableID`=1 and bill.`PeriodID` = '"+ iPeriodID +"' ) "
				+ "and `id` NOT IN("+ InterestOnPrincipleDue +" ,"+ AdjustCredits +" ,"+ IGST +" ,"+ CGST +" ,"+ SGST +" ,"+ CESS +")";
		HashMap<Integer, Map<String, Object>> resultLedgers =  m_dbConn.Select(sqlLedgers);
//		System.out.println("resultledger"+resultLedgers);
//		System.out.println("sqlledger"+sqlLedgers);

		/********** fetch bill amount by ledgers id ********/
		sqlQuery= "SELECT ";
		
		
		if(resultLedgers.size() > 0)
		{	
			/********ledger amount in voucher as column **********/
			int iCount = 0;
			for(Entry<Integer, Map<String, Object>> entryLedgers : resultLedgers.entrySet()) 
			{
				
				sqlQuery += " MAX( IF( vch.`To` =  '"+ entryLedgers.getValue().get("id") +"', vch.`Credit` , 0.00 ) ) AS '"+ entryLedgers.getValue().get("ledger_name")+"' ";
//				System.out.println("ledger amount"+sqlQuery);
				if(iCount < (resultLedgers.size()))
				{
					sqlQuery += " , ";
				}
				
				iCount ++;
			}
			
		}	
		
		
		sqlQuery += " bill.`BillSubTotal` ,bill.`BillInterest` as InterestOnArrears," +
					" bill.`AdjustmentCredit` , bill.`PrincipalArrears` as PreviousPrincipalArrears , bill.`InterestArrears` as PreviousInterestArrears,"  +
					" bill.`IGST`, bill.`CGST`, bill.`SGST`, bill.`CESS`, " +
					" bill.`TotalBillPayable` as Payable, (bill.`InterestArrears` + bill.`PrincipalArrears`) as PreviousArrears" +
					" FROM voucher AS vch JOIN billdetails AS bill ON vch.RefNo = bill.ID JOIN member_main AS mem ON bill.UnitID = mem.unit" +
					" JOIN unit AS uni ON uni.unit_id = bill.UnitID  WHERE bill.PeriodID ='" + iPeriodID +"' ";					
//	    System.out.println(sqlQuery);		
		if(iUnitId == 0)
		{
			memberIDS = m_Utility.getMemberIDs(BillDate);
			sqlQuery +="	and mem.member_id IN ("+ memberIDS +") ";
		}
		else
		{
			sqlQuery += "   and uni.unit_id = '"+ iUnitId + "'";
		}
		
		sqlQuery += "    and bill.BillType='"+ SupplementaryBill +"' ";
		sqlQuery += "	GROUP BY vch.`RefNo`  ORDER BY uni.sort_order ASC";
		System.out.println(sqlQuery);
		HashMap<Integer, Map<String, Object>> mpBills =  m_dbConn.Select(sqlQuery);
//		System.out.println(mpBills);
		/*if(bIsFetchSignleBill == true)
		{
			if(mpBills.size() > 0)
			{
				Map<String, Object> mpSingleBillsDetails = new HashMap<String, Object>();
				Map<String, Object> mpSingleBillsParticulars = new HashMap<String, Object>();
				
				for(Entry<Integer, Map<String, Object>> entryBills : mpBills.entrySet()) 
				{
					if(entryBills.getValue().get("Date") == "UNIT_NO")
					{
						mpSingleBillsDetails.put("sdate", entryBills.getValue().get("sdate"));
					}	
				}
				
				
			}
		}*/
		
		if(mpBills.size() > 0)
		{
			rows2.put("particulars",mpBills);
			
			Map.Entry<Integer, Map<String, Object>> entryBillReg = resultBillReg.entrySet().iterator().next();
			entryBillReg.getValue().put("BillFor",GetBillFor(iPeriodID));
			
			//billdetailsPart2.put("BillSubTotal",entryBillReg.getValue().get("BillSubTotal"));
			
			rows2.put("details",resultBillReg);
			rows.put("success",1);
			rows.put("response",rows2);
			
			Map.Entry<Integer, Map<String, Object>> entryBills = mpBills.entrySet().iterator().next();
			
			billdetailsPart2.put("BillSubTotal",entryBills.getValue().get("BillSubTotal"));
			entryBills.getValue().remove("BillSubTotal");
			
			billdetailsPart2.put("InterestOnArrears",entryBills.getValue().get("InterestOnArrears"));
			entryBills.getValue().remove("InterestOnArrears");
			
			billdetailsPart2.put("IGST",entryBills.getValue().get("IGST"));
			entryBills.getValue().remove("IGST");
			
			billdetailsPart2.put("CGST",entryBills.getValue().get("CGST"));
			entryBills.getValue().remove("CGST");
			
			billdetailsPart2.put("SGST",entryBills.getValue().get("SGST"));
			entryBills.getValue().remove("SGST");
			
			billdetailsPart2.put("CESS",entryBills.getValue().get("CESS"));
			entryBills.getValue().remove("CESS");
			
			billdetailsPart2.put("PreviousPrincipalArrears",entryBills.getValue().get("PreviousPrincipalArrears"));
			entryBills.getValue().remove("PreviousPrincipalArrears");
			
			billdetailsPart2.put("PreviousInterestArrears",entryBills.getValue().get("PreviousInterestArrears"));
			entryBills.getValue().remove("PreviousInterestArrears");
			
			billdetailsPart2.put("AdjustmentCredit",entryBills.getValue().get("AdjustmentCredit"));
			entryBills.getValue().remove("AdjustmentCredit");
			
			billdetailsPart2.put("Payable",entryBills.getValue().get("Payable"));
			entryBills.getValue().remove("Payable");
			
			billdetailsPart2.put("PreviousArrears",entryBills.getValue().get("PreviousArrears"));
			entryBills.getValue().remove("PreviousArrears");
			
			rows2.put("particulars2",billdetailsPart2);
			rows2.put("UnitId",iUnitId);
			
			
		}
		else
		{
			rows.put("success",0);
		}
						
		return rows;
	}
}
