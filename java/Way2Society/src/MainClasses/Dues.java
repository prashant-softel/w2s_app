package MainClasses;

import java.util.HashMap;
import java.util.Map;

import MainClasses.DbOperations.*;

public class Dues 
{
	static DbOperations m_dbConn;
	
	public Dues(DbOperations m_dbConnObj)
	{
		m_dbConn = m_dbConnObj;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchDues(int iUnitId, int iPeriodID, boolean bIsFetchLatest)
	{
		String sSqlQuery = "";
		
		if(bIsFetchLatest)
		{
			//fetch latest dues
			sSqlQuery =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,Amount,PaidBy,ChequeNumber from chequeentrydetails where PaidBy='" + iUnitId + "' order by VoucherDate DESC limit 0,1";
		}
		else if(iPeriodID == 0 && !bIsFetchLatest)
		{
			//fetch all dues
			sSqlQuery =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,Amount,PaidBy,ChequeNumber from chequeentrydetails where PaidBy='" + iUnitId + "' order by VoucherDate DESC";
		}
		else
		{
			//sSqlQuery =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,Amount,PaidBy,ChequeNumber from chequeentrydetails where PaidBy='" + iUnitId + "' order by ChequeDate DESC limit 0,1";
		}
		
		HashMap<Integer, Map<String, Object>> mPayments = CommonBaseClass.m_objDbOperations.Select(sSqlQuery);
		
			   	  
	    return mPayments;
	}

}
