package MainClasses;
import java.util.HashMap;
import java.util.Map;

import MainClasses.DbOperations.*;

public class DebitCrediNote {
	static DbOperations m_dbConn;
	static DbRootOperations m_dbConnRoot;
	static Utility m_Utility;
	public DebitCrediNote(DbOperations m_dbConnObj)
	{
		m_dbConn = m_dbConnObj;
	}
	public static HashMap<Integer, Map<String, Object>> mfetchDebitNote(int iUnitId, boolean bIsFetchLatest)
	{
		String sSqlDebitNote = "";
		
		//sSqlDebitNote="select DATE_FORMAT(asset.Date,'%d %b, %Y') as bill_date,IF(v.RefTableID = 8, 'SaleInvoice',IF(v.VoucherTypeID = 8, 'CreditNote','DebitNote')) as mode,IF(v.VoucherTypeID = 8, asset.Credit,asset.Debit) as totalPayable from assetregister as asset join voucher as v on v.id=asset.VoucherId where asset.LedgerID = '"+iUnitId+"' and v.RefTableID IN(8,9)";
		
		//notetype,billtype.unitid,cdnoteid
		sSqlDebitNote="select cdn.ID,DATE_FORMAT(asset.Date,'%d %b, %Y') as bill_date,DATE_FORMAT(v.Date,'%Y-%m-%d') as sdate,IF(v.VoucherTypeID = 8, 'CNote','DNote') as mode,IF(v.VoucherTypeID = 8, asset.Credit,asset.Debit) as totalPayable,cdn.BillType from assetregister as asset join voucher as v on v.id=asset.VoucherId join `credit_debit_note` as cdn on cdn.ID=v.RefNo where asset.LedgerID = '"+iUnitId+"' and v.RefTableID =9";
		HashMap<Integer, Map<String, Object>> mDebitNotes = CommonBaseClass.m_objDbOperations.Select(sSqlDebitNote);
		
	   	  
	    return mDebitNotes;
	}
	public static HashMap<Integer, Map<String, Object>> mfetchSaleInvoice(int iUnitId, boolean bIsFetchLatest)
	{
		String sSqlSale="";
		sSqlSale="select si.ID,si.Inv_Number,DATE_FORMAT(asset.Date,'%d %b, %Y') as bill_date,DATE_FORMAT(v.Date,'%Y-%m-%d') as sdate,IF(v.VoucherTypeID = 7, asset.Credit,asset.Debit) as totalPayable from assetregister as asset join voucher as v on v.id=asset.VoucherId join sale_invoice as si on si.id = v.RefNo where asset.LedgerID = '"+iUnitId+"' and v.RefTableID =8";
		HashMap<Integer, Map<String, Object>> mSaleInvoice = CommonBaseClass.m_objDbOperations.Select(sSqlSale);
		return mSaleInvoice;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchDebitNoteold(int iUnitId, boolean bIsFetchLatest)
	{
		String sSqlDebitNote = "";
		
		sSqlDebitNote="select DATE_FORMAT(asset.Date,'%d %b, %Y') as bill_date,IF(v.RefTableID = 8, 'SInvoice',IF(v.VoucherTypeID = 8, 'CNote','DNote')) as mode,IF(v.VoucherTypeID = 8, asset.Credit,asset.Debit) as totalPayable from assetregister as asset join voucher as v on v.id=asset.VoucherId where asset.LedgerID = '"+iUnitId+"' and v.RefTableID IN(8,9)";
		
		//sSqlDebitNote="select DATE_FORMAT(asset.Date,'%d %b, %Y') as bill_date,DATE_FORMAT(v.Date,'%Y-%m-%d') as sdate,IF(v.VoucherTypeID = 8, 'CreditNote','DebitNote') as mode,IF(v.VoucherTypeID = 8, asset.Credit,asset.Debit) as totalPayable,cdn.BillType from assetregister as asset join voucher as v on v.id=asset.VoucherId join `credit_debit_note` as cdn on cdn.ID=v.RefNo where asset.LedgerID = '"+iUnitId+"' and v.RefTableID =9";
		HashMap<Integer, Map<String, Object>> mDebitNotes = CommonBaseClass.m_objDbOperations.Select(sSqlDebitNote);
		
	   	  
	    return mDebitNotes;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchNotes(int iUnitId, int iBillType )
	{
		String sSqlDebitNote = "";
		
		sSqlDebitNote="select DATE_FORMAT(asset.Date,'%d %b, %Y') as bill_date,IF(v.RefTableID = 8, 'SInvoice',IF(v.VoucherTypeID = 8, 'CNote','DNote')) as mode,IF(v.VoucherTypeID = 8, asset.Credit,asset.Debit) as totalPayable from assetregister as asset join voucher as v on v.id=asset.VoucherId where asset.LedgerID = '"+iUnitId+"' and v.RefTableID IN(8,9)";
		
		//sSqlDebitNote="select DATE_FORMAT(asset.Date,'%d %b, %Y') as bill_date,DATE_FORMAT(v.Date,'%Y-%m-%d') as sdate,IF(v.VoucherTypeID = 8, 'CreditNote','DebitNote') as mode,IF(v.VoucherTypeID = 8, asset.Credit,asset.Debit) as totalPayable,cdn.BillType from assetregister as asset join voucher as v on v.id=asset.VoucherId join `credit_debit_note` as cdn on cdn.ID=v.RefNo where asset.LedgerID = '"+iUnitId+"' and v.RefTableID =9";
		HashMap<Integer, Map<String, Object>> mDebitNotes = CommonBaseClass.m_objDbOperations.Select(sSqlDebitNote);
		
	   	  
	    return mDebitNotes;
	}
}
