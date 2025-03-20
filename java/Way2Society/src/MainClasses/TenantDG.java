package MainClasses;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TenantDG
{
	static DbOperations m_dbConnRoot;
	static DbOperations m_dbConn;
	public TenantDG(DbOperations m_dbConnObj) 
	{
		m_dbConn = m_dbConnObj;	
	}
	public static HashMap<Integer, Map<String, Object>> fetchTenantDetails(int UnitId,int iTenantId,int SocietyId)
	{
		HashMap<Integer, Map<String, Object>> TenantDetails = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>> TenantMemberDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			String societyDbName = "",tenantName = "",FirstName="",LastName="",dob="";
			int index;
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+SocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			m_dbConn = new DbOperations(false, societyDbName);
			String sqlForTenantsDetails = "SELECT *,DATE_FORMAT(start_date, '%Y-%m-%d') as StartDate,DATE_FORMAT(end_date, '%Y-%m-%d') as EndDate FROM `tenant_module` where unit_id = '"+UnitId+"' and tenant_id = '"+iTenantId+"' and status = 'Y'";
			//System.out.println("Sql: "+sqlForTenantsDetails);
			TenantDetails =  m_dbConn.Select(sqlForTenantsDetails);
			//String sqlForTenantMembersDetails = "SELECT * FROM `tenant_member` where tenant_id = '"+iTenantId+"' and status = 'Y'";
			String sqlForTenantMembersDetails = "SELECT * ,DATE_FORMAT(mem_dob, '%Y-%m-%d') as DOB FROM `tenant_member` where tenant_id = '"+iTenantId+"' and status = 'Y'";
			//System.out.println("Sql: "+sqlForTenantMembersDetails);
			TenantMemberDetails =  m_dbConn.Select(sqlForTenantMembersDetails);
			for(Entry<Integer, Map<String, Object>> entry : TenantDetails.entrySet()) 
			{
			entry.getValue().put("TenantMemberDetails", TenantMemberDetails);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return TenantDetails;
	}
	/* *************************FUNCTION CREATED BEFORE************************************/
	/*public static long addTenantModule(int iUnitId, String sFName, String sMName,String sLName, String sMobileNo, String sEmail, int iProfessionId,String sDob, String sAdd1,String sAdd2,String sCity,String sPincode,int imemCount,String sCardNo,int iSocietyId)
	{
		String societyDbName = "",tenantName = "",FirstName="",LastName="",dob="",todayDate="";
		int index;
		long lTenantId = 0;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			todayDate = DateFunctionality.getCurrentDate();
			m_dbConn = new DbOperations(false, societyDbName);
			//Insert into tenant Module
			String sInsertQuery = "";
			sInsertQuery = "insert into tenant_module (`doc_id`,`unit_id`,`tenant_name`,`tenant_MName`,`tenant_LName`,`mobile_no`,`email`,`Profession`,`dob`,`Address1`,`Address2`,`City`,`Pincode`,`members`,`create_date`,`AdhaarCard_CINNo`,`tenantStatus`) values ";
			sInsertQuery = sInsertQuery + "('0','" + iUnitId + "','" + sFName + "','" + sMName + "','" + sLName +  "','" + sMobileNo +  "','" + sEmail +  "','" + iProfessionId +  "','"+sDob+"','" + sAdd1 + "','" + sAdd2 + "','"+ sCity + "','" + sPincode + "','" + imemCount + "','" + todayDate + "','"+sCardNo+"','1')";
			
			System.out.println(sInsertQuery);	
			lTenantId = m_dbConn.Insert(sInsertQuery);
		}*/
		public static long addTenantModule(int iUnitId, String sFName, String sMName,String sLName, String sMobileNo, String sEmail, int iProfessionId, String sDob,String sAdhaarCard, String sTenantAddress, String sCity, String sPincode,String sAgentName, String sAgentNumber, String sCIN_No, int iMemCount, String sNote,String sRelation,int iTenantType, String sCompanyName, String sCompanyPanNo, int iSocietyId)//Correct
		{
			String societyDbName = "",todayDate="";
			int index;
			long lTenantId = 0,result = 0;
			try
			{
				m_dbConnRoot = new DbOperations(true,"");
				String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
				HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
				for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
				{
					societyDbName = entry.getValue().get("dbname").toString();
				}
				todayDate = DateFunctionality.getCurrentDate();
				m_dbConn = new DbOperations(false, societyDbName);
				//Insert into tenant Module
				String sInsertQuery = "";
				sInsertQuery = "INSERT INTO `tenant_module`( `doc_id`, `unit_id`, `tenant_name`, `tenant_MName`, `tenant_LName`, `mobile_no`, `email`, `Profession`, `dob`, `Address`, `City`, `Pincode`, `agent_name`, `agent_no`, `members`, `create_date`, `p_varification`, `note`, `active`, `tenantType`,`AdhaarCard`, `CompanyName`, `CIN_NO`, `CompanyPanNo`, `tenantStatus`, `status`)";
				sInsertQuery += "VALUES ('0', '"+iUnitId+"', '"+sFName+"', '"+sMName+"', '"+sLName+"', '"+sMobileNo+"', '"+sEmail+"', '"+iProfessionId+"', '"+sDob+"', '"+sTenantAddress+"','"+sCity+"', '"+sPincode+"','"+sAgentName+"','"+sAgentNumber+"', '"+iMemCount+"', '"+todayDate+"','1','"+sNote+"','0','"+iTenantType+"','"+sAdhaarCard+"','"+sCompanyName+"','"+sCIN_No+"','"+sCompanyPanNo+"','0', 'Y')";
				
				//System.out.println(sInsertQuery);	
				lTenantId = m_dbConn.Insert(sInsertQuery);
				String sInsertIntoTenantMember = "";
				sInsertIntoTenantMember = "INSERT INTO `tenant_member`( `tenant_id`, `mem_name`, `relation`, `mem_dob`, `contact_no`, `email`, `AdhaarCard`, `CoSignStatus`)";
				sInsertIntoTenantMember += "VALUES ('"+lTenantId+"', '"+sFName+" "+sMName+" "+sLName+"', '"+sRelation+"', '"+sDob+"', '"+sMobileNo+"', '"+sEmail+" ', '"+sAdhaarCard+"', '1')";
				long lTenantMemberId = m_dbConn.Insert(sInsertIntoTenantMember);
				//System.out.println("lTenantMemberId : "+lTenantMemberId);
				//System.out.println("lTenantId : "+lTenantId);
			}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lTenantId;
	}
	public static long addTenantMembers(int iTenantModuleId, String sMemName, String sRelation,String sDOB, String sMobileNo, String sEmail, String sAdhaarCard, int sCoSignStatus, int iSocietyId)
	{
		String societyDbName = "",tenantName = "",FirstName="",LastName="",dob="",todayDate="";
		int index;
		long lTenantId = 0;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			todayDate = DateFunctionality.getCurrentDate();
			m_dbConn = new DbOperations(false, societyDbName);
			//Insert into tenant Module
			String sInsertQuery = "";
			sInsertQuery = "insert into tenant_member (`tenant_id`,`mem_name`,`relation`,`mem_dob`,`contact_no`,`email`,`AdhaarCard`, `CoSignStatus`) values ";
			sInsertQuery = sInsertQuery + "('" + iTenantModuleId + "','" + sMemName + "','" + sRelation + "','" + sDOB +  "','" + sMobileNo +  "','" + sEmail + "','"+sAdhaarCard+"','"+sCoSignStatus+"')";
			//System.out.println(sInsertQuery);	
			lTenantId = m_dbConn.Insert(sInsertQuery);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lTenantId;
	}
	public static long updateTenantDetails(int societyId,int tenantId, String sStartDate, String sEndDate)
	{
		long lUpdateResult = 0;
		String societydbName="";
		try 
		{
			m_dbConnRoot = new DbOperations(true, "");
			String sqlForSocietyDetails = "Select `dbname` from society where society_id = '"+societyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> societyDetails = m_dbConnRoot.Select(sqlForSocietyDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet())
			{	
				societydbName = entry1.getValue().get("dbname").toString();
			}
			String sDateOfBirth="";
			m_dbConn = new DbOperations(false,societydbName);
			//sStartDate = DateFunctionality.convertToStandardFormat(sStartDate);
			//sEndDate = DateFunctionality.convertToStandardFormat(sEndDate);
			//System.out.println("sStartDate :"+sStartDate);
			//System.out.println("sEndDate :"+sEndDate);
			//update into member_main
			String sUpdateQuery = "";
			sUpdateQuery = "UPDATE  `tenant_module` SET  `start_date` =  '"+sStartDate+"',`end_date` =  '"+sEndDate+"' WHERE `tenant_id` ='"+tenantId+"' and status = 'Y'";	
			//System.out.println(sUpdateQuery);	
			lUpdateResult = m_dbConn.Update(sUpdateQuery);
			//System.out.println("lUpdateID<" + lUpdateResult + ">");	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    return lUpdateResult;	
	}
	public static long updateTenantStatus(int isocietyId,int itenantId, String sstatus)
	{
		long lUpdateResult = 0;
		String societydbName="";
		try 
		{
			m_dbConnRoot = new DbOperations(true, "");
			String sqlForSocietyDetails = "Select `dbname` from society where society_id = '"+isocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> societyDetails = m_dbConnRoot.Select(sqlForSocietyDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet())
			{	
				societydbName = entry1.getValue().get("dbname").toString();
			}
			String sDateOfBirth="";
			m_dbConn = new DbOperations(false,societydbName);
			//sStartDate = DateFunctionality.convertToStandardFormat(sStartDate);
			//sEndDate = DateFunctionality.convertToStandardFormat(sEndDate);
			//System.out.println("tenantId :"+itenantId);
			//System.out.println("status :"+sstatus);
			//update into member_main
			String sUpdateQuery = "";
			sUpdateQuery = "UPDATE  `tenant_module` SET  `tenantStatus` =  '"+sstatus+"'  WHERE `tenant_id` ='"+itenantId+"' and status = 'Y'";	
			//System.out.println(sUpdateQuery);	
			lUpdateResult = m_dbConn.Update(sUpdateQuery);
			//System.out.println("lUpdateID<" + lUpdateResult + ">");	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    return lUpdateResult;	
	}
	public static HashMap<Integer, Map<String, Object>> fetchTenantMemberDetails(int iTenantId,int SocietyId)
	{
		HashMap<Integer, Map<String, Object>> TenantDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			String societyDbName = "",tenantName = "",FirstName="",LastName="",dob="";
			int index;
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+SocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			m_dbConn = new DbOperations(false, societyDbName);
			String sqlForOwnersDetails = "SELECT * FROM `tenant_member` where tenant_id = '"+iTenantId+"' and status = 'Y'";
			//System.out.println("Sql: "+sqlForOwnersDetails);
			TenantDetails =  m_dbConn.Select(sqlForOwnersDetails);
			//System.out.println("TenantDetails :"+TenantDetails);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return TenantDetails;
	}
	public static long editTenantModule(int iTenantModuleId,int iUnitId, String sFName, String sMName,String sLName, String sMobileNo, String sEmail, int iProfessionId, String sDob,String sAdhaarCard, String sTenantAddress, String sCity, String sPincode,String sAgentName, String sAgentNumber, String sCIN_No, int iMemCount, String sNote,String sRelation,int iTenantType, String sCompanyName, String sCompanyPanNo, int iSocietyId,String LeaseStartDate,String LeaseEndDate)//Correct
	{
		String societyDbName = "",tenantName = "",FirstName="",LastName="",dob="",todayDate="";
		int index;
		long lTenantId = 0;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			todayDate = DateFunctionality.getCurrentDate();
			m_dbConn = new DbOperations(false, societyDbName);
			//Update into tenant Module
			String sUpdateQuery = "";
																						
			//sUpdateQuery = "Update tenant_module SET `doc_id`= '0',`unit_id` = '"+iUnitId+"',`tenant_name` = '"+sFName+"',`tenant_MName`='"+sMName+"',`tenant_LName`='"+sLName+"',`mobile_no` = '"+sMobileNo+"',`email`='"+sEmail+"',`Profession`='"+iProfessionId+"',`dob`='"+sDob+"',`Address`='"+sTenantAddress+"',`City`='"+sCity+"',`Pincode`='"+sPincode+"',`agent_name`='"+sAgentName+"',`agent_no`='"+sAgentNumber+"',`members`='"+iMemCount+"',`note`='"+sNote+"',`AdhaarCard`='"+sAdhaarCard+"',`CompanyName`='"+sCompanyName+"', `CIN_NO`='"+sCIN_No+"', `CompanyPanNo`='"+sCompanyPanNo+"' where tenant_id = '"+iTenantModuleId+"' and `status` = 'Y'";
			sUpdateQuery = "Update tenant_module SET `doc_id`= '0',`unit_id` = '"+iUnitId+"',`tenant_name` = '"+sFName+"',`tenant_MName`='"+sMName+"',`tenant_LName`='"+sLName+"',`mobile_no` = '"+sMobileNo+"',`email`='"+sEmail+"',`Profession`='"+iProfessionId+"',`Address`='"+sTenantAddress+"',`City`='"+sCity+"',`Pincode`='"+sPincode+"',`agent_name`='"+sAgentName+"',`agent_no`='"+sAgentNumber+"',`members`='"+iMemCount+"',`note`='"+sNote+"',`AdhaarCard`='"+sAdhaarCard+"',`CompanyName`='"+sCompanyName+"', `CIN_NO`='"+sCIN_No+"', `CompanyPanNo`='"+sCompanyPanNo+"',`start_date`='"+LeaseStartDate+"',`end_date`='"+LeaseEndDate+"' where tenant_id = '"+iTenantModuleId+"' and `status` = 'Y'";
			System.out.println(sUpdateQuery);	
			lTenantId = m_dbConn.Update(sUpdateQuery);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lTenantId;
	}
	public static long editTenantMember(int itenantMemberId,int iTenantId, String sMemName, String sRelation,String sDOB, String sMobileNo, String sEmail,int iSocietyId,String sTMemAdhaarCard, int sCoSignAgreement)
	{
		String societyDbName = "",tenantName = "",FirstName="",LastName="",dob="",todayDate="";
		int index;
		long lTenantId = 0;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			todayDate = DateFunctionality.getCurrentDate();
			m_dbConn = new DbOperations(false, societyDbName);
			//Insert into tenant Module
			String sUpdateQuery = "";
			if(itenantMemberId != 0)
			{
				sUpdateQuery = "Update tenant_member SET `mem_name` = '"+sMemName+"',`relation` = '"+sRelation+"',`mem_dob` = '"+sDOB+"',`contact_no` = '"+sMobileNo+"',`email` = '"+sEmail+"' ,`AdhaarCard`='"+sTMemAdhaarCard+"',`CoSignStatus`='"+sCoSignAgreement+"' where tmember_id = '"+itenantMemberId+"' and `status` = 'Y'";
			}
			else
			{
				sUpdateQuery = "Update tenant_member SET `mem_name` = '"+sMemName+"',`relation` = '"+sRelation+"',`mem_dob` = '"+sDOB+"',`contact_no` = '"+sMobileNo+"',`email` = '"+sEmail+"' where tenant_id = '"+iTenantId+"' and `status` = 'Y'";
				
			}
			//System.out.println(sUpdateQuery);	
			lTenantId = m_dbConn.Update(sUpdateQuery);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lTenantId;
	}
	public static long DeleteTenant(int iTenantModuleId,int iSocietyId)
	{
		String societyDbName = "",tenantName = "",FirstName="",LastName="",dob="",todayDate="";
		int index;
		long lTenantId = 0;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			todayDate = DateFunctionality.getCurrentDate();
			m_dbConn = new DbOperations(false, societyDbName);
			//Update into tenant Module
			String sUpdateQuery = "";
			sUpdateQuery = "Update tenant_module SET `Status`= 'N' where tenant_id = '"+iTenantModuleId+"' ";
			//System.out.println(sUpdateQuery);	
			lTenantId = m_dbConn.Update(sUpdateQuery);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lTenantId;
	}
	
	public static void main(String[] args) 
	{
	
	}
}
