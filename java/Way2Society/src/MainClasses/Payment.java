package MainClasses;

import java.util.HashMap;
import java.util.Map;

import MainClasses.DbOperations.*;

public class Payment 
{
	static DbOperations m_dbConn;
	public Payment(DbOperations m_dbConnObj)
	{
		m_dbConn = m_dbConnObj;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchPaymentsReceived(int iUnitId,int iFetchCount)
	{
		//fetching  Payments Received
		
		String sSqlPayment =  "select ChequeDate,Amount,PaidBy,ChequeNumber from chequeentrydetails where PaidBy='"+ iUnitId +"' order by ChequeDate DESC";
		
		if(iFetchCount > 0)
		{
			sSqlPayment += " LIMIT "+iFetchCount;
		}
		
		//System.out.println(sSqlPayment);
		
		HashMap<Integer, Map<String, Object>> mPayments = CommonBaseClass.m_objDbOperations.Select(sSqlPayment);
		
	    return mPayments;
	}

}
