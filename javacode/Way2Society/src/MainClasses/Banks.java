package MainClasses;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import MainClasses.CommonBaseClass;

public class Banks
{
	static DbOperations m_dbConn;
	static Utility m_Utility;
	
	public Banks(DbOperations m_dbConnObj) throws ClassNotFoundException
	{
		m_dbConn = m_dbConnObj;
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	
	public static int mSetNEFTTransaction(int iLoginID, int iUnitID, int iBankid, int iBillType, double lAmount, String sTransactionID, String sTxnDate, String sNote, String sPayerBank, String sPayerBranch) throws SQLException
	{
		String sInsertQuery = "";
		String sSelectQuery = "";
		String sUpdateQuery = "";
		
		int iSocietyId = CommonBaseClass.getSocietyId();
		
		m_dbConn.BeginTransaction();
		
		try {
			//Insert into ChequeentryDetails
			sInsertQuery = "insert into chequeentrydetails (`VoucherDate`,`ChequeDate`,`ChequeNumber`,`Amount`,`PaidBy`,`BankID`,`PayerBank`,`PayerChequeBranch`,`DepositID`,`EnteredBy`,`Comments`,`isEnteredByMember`,`BillType`,`TransactionType`) values "
							+ "('" + sTxnDate + "','" + sTxnDate + "','" + sTransactionID + "','" + lAmount + "','" + iUnitID + "','" + iBankid + "','" + sPayerBank + "','" + sPayerBranch + "','-2','" + iLoginID + "','" + sNote + "','1','" + iBillType + "', '0')";
			
			long lChequeDetailID = m_dbConn.Insert(sInsertQuery);
			
			if(lChequeDetailID == 0)
			{
				return 0;
			}
			
			//Get latest voucher no
			sSelectQuery = "Select voucher_no from counter where society_id = '"+iSocietyId+"'";
			HashMap<Integer, Map<String, Object>> selectedVoucher =  m_dbConn.Select(sSelectQuery);
			 
			long lVoucherNo = 0;
			if(selectedVoucher.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> valueVoucherNo : selectedVoucher.entrySet()) 
				{
					lVoucherNo = (long) valueVoucherNo.getValue().get("voucher_no");
					
					//lVoucherNo = lVoucherNo + 1;
					//Increment voucher no in DB
					long lNewVoucherNo = lVoucherNo + 1;
					sUpdateQuery = "Update counter SET `voucher_no` = '" + lNewVoucherNo + "' where society_id = '"+iSocietyId+"'";
					
					m_dbConn.Update(sUpdateQuery);
				} 
			}
			
			//Insert into voucher
			sInsertQuery = "INSERT INTO `voucher`(`Date`, `RefNo`, `RefTableID`, `VoucherNo`, `SrNo`, `VoucherTypeID`, `By`, `To`, `Debit`, `Credit`, `Note`) VALUES "
					+ "('" + sTxnDate + "', '" + lChequeDetailID +  "', '2', '" + lVoucherNo + "', '1', '3' , '" + iUnitID +  "', '', '" + lAmount + "', '0', '" + sNote + "')";
			
			long lVoucherID = m_dbConn.Insert(sInsertQuery);
			
			String sCategoryID ="";
			String sSubCategoryID = "";
			
			//Fetch Category and Sub Category
			sSelectQuery = "select categorytbl.group_id, ledgertbl.categoryid from ledger As ledgertbl JOIN account_category As categorytbl ON ledgertbl.categoryid = categorytbl.category_id where ledgertbl.id = '" + iUnitID + "'";
			HashMap<Integer, Map<String, Object>> selectedCategory =  m_dbConn.Select(sSelectQuery);
			 
			if(selectedCategory.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> valueCategory : selectedCategory.entrySet()) 
				{
					sCategoryID = (String) valueCategory.getValue().get("group_id");
					sSubCategoryID = (String) valueCategory.getValue().get("categoryid");
				}
			}
			
			//insert into asset register
			sInsertQuery = "INSERT INTO `assetregister`(`Date`, `CategoryID`, `SubCategoryID`, `LedgerID`, `VoucherID`, `VoucherTypeID`, `Debit`, `Credit`, `Is_Opening_Balance`) VALUES "
					+ "('" + sTxnDate + "', '" + sCategoryID + "', '" + sSubCategoryID + "', '" + iUnitID +   "', '" + lVoucherID + "',  '3', '0', '" + lAmount + "', '0')";
			
			long lAssetRegisterID = m_dbConn.Insert(sInsertQuery);
			
			if(lAssetRegisterID == 0)
			{
				return 0;
			}
			//Insert into voucher
			sInsertQuery = "INSERT INTO `voucher`(`Date`, `RefNo`, `RefTableID`, `VoucherNo`, `SrNo`, `VoucherTypeID`, `By`, `To`, `Debit`, `Credit`,`Note`) VALUES "
					+ "('" + sTxnDate + "', '" + lChequeDetailID +  "', '2', '" + lVoucherNo + "', '2', '3' , '', '" + iBankid +  "', '0', '" + lAmount + "', '" + sNote + "')";
			
			lVoucherID = m_dbConn.Insert(sInsertQuery);
			
			if(lVoucherID == 0)
			{
				return 0;
			}
			
			//Insert into Bank Register - Find replacement for date
			sInsertQuery = "INSERT INTO `bankregister`(`Date`, `LedgerID`, `VoucherID`, `VoucherTypeID`, `ReceivedAmount`, `PaidAmount`, `DepositGrp`, `ChkDetailID`, `Is_Opening_Balance`, `Cheque Date`, `Ref`, `Reconcile Date`, `ReconcileStatus`, `Reconcile`, `Return`,`statement_id`) VALUES "
					+ "('" + sTxnDate + "', '" + iBankid +  "', '" + lVoucherID + "', '3', '" + lAmount + "', '0', '-2', '" + lChequeDetailID + "', '0', '" + sTxnDate + "', '0', '0000-00-00', '0', '0', '0','0')";
			
			long lBankRegisterID = m_dbConn.Insert(sInsertQuery);
			
			if(lBankRegisterID == 0)
			{
				return 0;
			}
				
			//Update Member PayerBank and PayerBranch
			if(!sPayerBank.isEmpty() && !sPayerBranch.isEmpty())
			{
				sUpdateQuery = "UPDATE `unit` SET `Payer_Bank`='" + sPayerBank  + "',`Payer_Cheque_Branch`= '" + sPayerBranch + "' WHERE `unit_id` = '" + iUnitID + "'";
				long lUpdateBank = m_dbConn.Update(sUpdateQuery);
			}
			
			m_dbConn.EndTransaction();
			
			return 1;
		}
		catch (Exception e) {
			// TODO: handle exception
			m_dbConn.RollbackTransaction();
		}
		
		return 0;
	}
	  	
	public static HashMap<Integer, Map<String, Object>> mfetchNEFTBanks()
	{
		String sSqlNEFTBanks = "";
		
		//Fetching NEFT bank
		sSqlNEFTBanks = "select BankID, AcNumber, IFSC_Code, BankName from bank_master where AllowNEFT = 1";		
		
		HashMap<Integer, Map<String, Object>> mpBills =  m_dbConn.Select(sSqlNEFTBanks);
				
		return mpBills;
	}
	
	public static HashMap<Integer, Map<String, Object>> fetchPayerBanks(int iUnitID)
	{
		String sSqlPayerBanks = "";
		
		sSqlPayerBanks = "select Payer_Bank, Payer_Cheque_Branch from unit where unit_id = " + iUnitID;
		
		HashMap<Integer, Map<String,Object>> mpBills = m_dbConn.Select(sSqlPayerBanks);
		
		return mpBills;
		
	}
	
	}
