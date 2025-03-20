package MainClasses;

import java.util.HashMap;
import java.util.Map;

import MainClasses.DbOperations.*;

public class Receipt 
{
	static DbOperations m_dbConn;
	
	public Receipt(DbOperations m_dbConnObj)
	{
		m_dbConn = m_dbConnObj;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchReceipts(int iUnitId, int iPeriodID, boolean bIsFetchLatest)
	{
		String sSqlReceipt = "";
		
		if(iPeriodID == 0 && bIsFetchLatest)
		{
			//old query
			//fetch latest receipts
			/*sSqlReceipt =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,DATE_FORMAT(ChequeDate,'%Y-%m-%d') as sdate,Amount,PaidBy,ChequeNumber,BillType,periodtable.ID as PeriodID,PayerBank,PayerChequeBranch ";
			sSqlReceipt += " ,IF(DepositID = -3, 'CASH',IF(DepositID = -2, 'NEFT', IF(DepositID > 0, 'CHEQUE','OTHER'))) AS mode,Comments";
			sSqlReceipt += " from chequeentrydetails JOIN `period` as periodtable on `ChequeDate` between periodtable.BeginingDate and periodtable.EndingDate where PaidBy='" + iUnitId + "' order by VoucherDate DESC limit 0,1";
			*/
			sSqlReceipt =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,DATE_FORMAT(ChequeDate,'%Y-%m-%d') as sdate,Amount,PaidBy,ChequeNumber,BillType,periodtable.ID as PeriodID,PayerBank,PayerChequeBranch ";
			sSqlReceipt += " ,IF(DepositID = -3, 'CASH',IF(DepositID = -2, 'NEFT', IF(DepositID > 0, 'CHEQUE','PAYTM'))) AS mode,Comments";
			sSqlReceipt += " from chequeentrydetails JOIN `period` as periodtable on `ChequeDate` between periodtable.BeginingDate and periodtable.EndingDate where PaidBy='" + iUnitId + "' order by VoucherDate DESC limit 0,1";
		}
		else if(iPeriodID == 0 && !bIsFetchLatest)
		{
			// old query
			/*sSqlReceipt =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,DATE_FORMAT(ChequeDate,'%Y-%m-%d') as sdate,Amount,PaidBy,ChequeNumber,BillType,periodtable.ID as PeriodID,PayerBank,PayerChequeBranch ";
			sSqlReceipt += " ,IF(DepositID = -3, 'CASH',IF(DepositID = -2, 'NEFT', IF(DepositID > 0, 'CHEQUE','OTHER'))) AS mode,Comments";
			sSqlReceipt += " from chequeentrydetails JOIN `period` as periodtable on `ChequeDate` between periodtable.BeginingDate and periodtable.EndingDate where PaidBy='" + iUnitId + "' order by VoucherDate DESC";
		   */
			/*sSqlReceipt =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,DATE_FORMAT(ChequeDate,'%Y-%m-%d') as sdate,Amount,PaidBy,ChequeNumber,BillType,periodtable.ID as PeriodID,PayerBank,PayerChequeBranch ";
			sSqlReceipt += " ,IF(DepositID = -3, 'CASH',IF(DepositID = -2, 'NEFT', IF(DepositID > 0, 'CHEQUE','PAYTM'))) AS mode,Comments";
			sSqlReceipt += " from chequeentrydetails JOIN `period` as periodtable on `ChequeDate` between periodtable.BeginingDate and periodtable.EndingDate where PaidBy='" + iUnitId + "' order by VoucherDate DESC";
			*/
			 // Chndeg by sujit 
			sSqlReceipt =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,DATE_FORMAT(ChequeDate,'%Y-%m-%d') as sdate,Amount,PaidBy,ChequeNumber,BillType,periodtable.ID as PeriodID,PayerBank,PayerChequeBranch ";
			sSqlReceipt += " ,IF(DepositID = -3, 'CASH',IF(DepositID = -2, 'NEFT', IF(DepositID > 0, 'CHEQUE','PAYTM'))) AS mode,Comments";
			sSqlReceipt += " from chequeentrydetails JOIN `period` as periodtable on `ChequeDate` between periodtable.BeginingDate and periodtable.EndingDate JOIN `voucher` as vch on chequeentrydetails.ID = vch.RefNo where PaidBy='" + iUnitId + "' and vch.RefTableID = 2 group by vch.VoucherNo order by VoucherDate DESC";
			
		}
		else if(iPeriodID > 0)
		{
			//old query
			/*sSqlReceipt =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,DATE_FORMAT(ChequeDate,'%Y-%m-%d') as sdate,Amount,PaidBy,ChequeNumber,BillType,periodtable.ID as PeriodID,PayerBank,PayerChequeBranch ";
			sSqlReceipt += " ,IF(DepositID = -3, 'CASH',IF(DepositID = -2, 'NEFT', IF(DepositID > 0, 'CHEQUE','OTHER'))) AS mode,Comments";
			sSqlReceipt += " from chequeentrydetails JOIN `period` as periodtable on `ChequeDate` between periodtable.BeginingDate and periodtable.EndingDate where PaidBy='" + iUnitId + "' and periodtable.ID ='" + iPeriodID + "' order by VoucherDate DESC";
			*/
			sSqlReceipt =  "select DATE_FORMAT(ChequeDate,'%d %b, %Y') as ChequeDate,DATE_FORMAT(ChequeDate,'%Y-%m-%d') as sdate,Amount,PaidBy,ChequeNumber,BillType,periodtable.ID as PeriodID,PayerBank,PayerChequeBranch ";
			sSqlReceipt += " ,IF(DepositID = -3, 'CASH',IF(DepositID = -2, 'NEFT', IF(DepositID > 0, 'CHEQUE','PAYTM'))) AS mode,Comments";
			sSqlReceipt += " from chequeentrydetails JOIN `period` as periodtable on `ChequeDate` between periodtable.BeginingDate and periodtable.EndingDate where PaidBy='" + iUnitId + "' and periodtable.ID ='" + iPeriodID + "' order by VoucherDate DESC";
		}
		//System.out.println("Select Query : "+sSqlReceipt);
		
		HashMap<Integer, Map<String, Object>> mReceipts = CommonBaseClass.m_objDbOperations.Select(sSqlReceipt);
		
			   	  
	    return mReceipts;
	}
	
	
	
	public static HashMap<Integer, Map<String, Object>> getDueAmount(int iUnitId)
	{
		//fetching memeber due amount
		String sSqlDues = "SELECT SUM(`Debit`) as Debit , SUM(`Credit`) as Credit, (SUM(Debit) - SUM(Credit)) as Total FROM `assetregister` WHERE `LedgerID` = '"+iUnitId+"' GROUP BY LedgerID ";
		
		HashMap<Integer, Map<String, Object>> mReceipts = CommonBaseClass.m_objDbOperations.Select(sSqlDues);
		
		return mReceipts;
	}
	
	
	public static HashMap<Integer, Map<String, Object>> getBeginEndReceiptDate(int iUnitId,int iPeriodID,int BillType)
	{
		//fetching recipt start and end date
		String sSqlDues = "SELECT SUM(`Debit`) as Debit , SUM(`Credit`) as Credit, (SUM(Debit) - SUM(Credit)) as Total FROM `assetregister` WHERE `LedgerID` = '"+iUnitId+"' GROUP BY LedgerID ";
		
		HashMap<Integer, Map<String, Object>> mReceipts = CommonBaseClass.m_objDbOperations.Select(sSqlDues);
		
		return mReceipts;
	}

	
	public static HashMap<Integer, Map<String, Object>> mfetchcreditdebit(String iUnitId, String mode,String cdid)
	{
		String sSqlcdnote = "";
	
		sSqlcdnote = "select * from credit_debit_note where  ID  =  '"+cdid + "'"; 
	
		HashMap<Integer, Map<String, Object>> mcdnote = CommonBaseClass.m_objDbOperations.Select(sSqlcdnote);
		System.out.println("**************************************************");
		System.out.println("Query : " +sSqlcdnote);
		System.out.println("Output : " + mcdnote );
		System.out.println("**************************************************");
		
		return mcdnote;
	}
	public static HashMap<Integer, Map<String, Object>> mfetchcreditdebitvoucher(String iUnitId, String mode,String cdid)
	{
		String sSqlcdnote = "";
		
		String ToORBy = "To";
		String DebitOrCredit = "Credit";
		if(mode.equals("M-CNote") || mode.equals("S-CNote"))
		{
			ToORBy = "By";
			DebitOrCredit = "Debit";
		}
		
		sSqlcdnote = "SELECT voucher_table.id as VcrId, voucher_table."+ToORBy+ " as key1, voucher_table."+DebitOrCredit +"  As Credit, ledger_table.ledger_name FROM `voucher` as `voucher_table` join `ledger` as `ledger_table` on voucher_table."+ToORBy+" = ledger_table.id WHERE voucher_table.RefNo= '"+cdid+"' and voucher_table.RefTableID = '9' order by voucher_table.SrNo";
	
		HashMap<Integer, Map<String, Object>> mcdnote = CommonBaseClass.m_objDbOperations.Select(sSqlcdnote);
		
		System.out.println("**************************************************");
		System.out.println("Query : " +sSqlcdnote);
		System.out.println("Output : " + mcdnote );
		System.out.println("**************************************************");
		   	  
	    return mcdnote;
	}
	
	
	public static HashMap<Integer, Map<String, Object>> mfetchinv_number(String iUnitId, String mode,String inv_number,String inv_id)
	{
		String sSqlinv_number = "";
	
		sSqlinv_number = "select * from sale_invoice where  Inv_Number= '"+inv_number+"' and UnitID='"+iUnitId+"'"; 
	
		HashMap<Integer, Map<String, Object>> minv_number = CommonBaseClass.m_objDbOperations.Select(sSqlinv_number);
		System.out.println("**************************************************");
		System.out.println("Query : " +sSqlinv_number);
		System.out.println("Output : " + minv_number );
		System.out.println("**************************************************");
		
		return minv_number;
	}
	public static HashMap<Integer, Map<String, Object>> mfetchinv_numbervoucher(String iUnitId, String mode,String inv_number,String inv_id)
	{
		String sSqlinv_number = "";
		
		
		sSqlinv_number = "SELECT voucher_table.id as VcrId, voucher_table.To as key1, voucher_table.Credit, ledger_table.ledger_name, ledger_table.ledger_name  FROM `voucher` as `voucher_table` join `ledger` as `ledger_table` on voucher_table.To = ledger_table.id WHERE voucher_table.RefNo= '"+inv_id+"' and voucher_table.RefTableID = '8' order by voucher_table.SrNo";
	
		HashMap<Integer, Map<String, Object>> minv_number = CommonBaseClass.m_objDbOperations.Select(sSqlinv_number);
		
		System.out.println("**************************************************");
		System.out.println("Query : " +sSqlinv_number);
		System.out.println("Output : " + minv_number );
		System.out.println("**************************************************");
		   	  
	    return minv_number;
	}
	
}
