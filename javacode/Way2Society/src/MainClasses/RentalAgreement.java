package MainClasses;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import CommonUtilityFunctions.MapUtility;

public class RentalAgreement
{
	static DbOperations m_dbConnRoot;
	static DbOperations m_dbConn;
	public RentalAgreement(DbOperations m_dbConnObj) 
	{
		m_dbConn = m_dbConnObj;	
	}
	public static HashMap<Integer, Map<String, Object>> fetchRentalAgreementByUnitId(int UnitId,int SocietyId)
	{
		//HashMap rows = new HashMap<>();
		//HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> AgreementDetails = new HashMap<Integer, Map<String, Object>>();
		//HashMap<Integer, Map<String, Object>> AgreementDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			String societyDbName = "";
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT * FROM `society` where society_id = '"+SocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			//System.out.println("societyDbName :"+societyDbName);
			m_dbConn = new DbOperations(false, societyDbName);
			//String sqlForAgreement = "SELECT * from rentalagreements where UnitId = '"+UnitId+"' and Status = 'Y'";
			String sqlForAgreement = "SELECT r.*,tm.tenant_id, tm.tenant_name,tm.tenant_MName,tm.tenant_LName,tm.mobile_no,tm.email,tm.start_date,tm.end_date,tm.active,tm.tenantStatus,tm.tenantType,tm.CompanyName from `rentalagreements` as r join `tenant_module` as tm on r.UnitId=tm.unit_id where r.UnitId = '"+UnitId+"' and tm.Status = 'Y' group by tm.tenant_id";
			AgreementDetails =  m_dbConn.Select(sqlForAgreement);
			///int iUnitid = 0;
			//int iSociety_ID = 0;
			//rows.put("RegistrationData", MapUtility.HashMaptoList(AgreementDetails));
			//if(AgreementDetails.size() > 0)
			//{	
			///	Map.Entry<Integer, Map<String, Object>> entryMemberMain = AgreementDetails.entrySet().iterator().next();
				//iUnitid = (Integer) entryMemberMain.getValue().get("UnitId");
				//iSociety_ID = (Integer) entryMemberMain.getValue().get("society_id");
			//}
			//String sSqlTenant = "SELECT * FROM `tenant_module` WHERE `unit_id`= '"+iUnitid+"' ";
			//System.out.println("sSqlOtherFamily: " + sSqlTenant);
			//HashMap<Integer, Map<String, Object>> mpTanent =  m_dbConn.Select(sSqlTenant);
			//rows.put("Tenant", MapUtility.HashMaptoList(mpTanent));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return AgreementDetails;
	}
	public static long addRentalAgreement(int iUnitId,int iPropertyUse,int iPropertyType, String sPropertyAdd, String sPincode, String sCity,String sRegion, String sDeposit,int iRentType,String sMiscClause,String sPOAName,String sPOAadhaar,int iSocietyId,int iTenantID, String sStartdate , String sEnddate,int iInRural,int iUnitOutOfSociety)
	{
		String societyDbName = "";
		int index;
		long lAgreementId = 0;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			m_dbConn = new DbOperations(false, societyDbName);
			//Insert into tenant Module
			String sInsertQuery = "";
			sInsertQuery = "INSERT INTO `rentalagreements` (`UnitId`,`TenantID`, `PropertyUse`,`PropertyType`,`Property_Address`, `PinCode`, `City`, `Region`, `Deposit`, `RentType`, `MiscClauses`, `POA_Name`, `POA_AdharNo`,`Is_rular`,`Unit_OutOfSociety`) VALUES ('"+iUnitId+"','"+iTenantID+"', '"+iPropertyUse+"','"+iPropertyType+"', '"+sPropertyAdd+"', '"+sPincode+"', '"+sCity+"', '"+sRegion+"', '"+sDeposit+"', '"+iRentType+"', '"+sMiscClause+"', '"+sPOAName+"', '"+sPOAadhaar+"','"+iInRural+"','"+iUnitOutOfSociety+"');";	
			//System.out.println(sInsertQuery);	
			lAgreementId = m_dbConn.Insert(sInsertQuery);
			String sUpdateQuery = "UPDATE  `tenant_module` SET  `start_date` =  '"+sStartdate+"',`end_date` =  '"+sEnddate+"' WHERE `tenant_id` ='"+iTenantID+"'";	
			//System.out.println(sUpdateQuery);	
			long lUpdateResult = m_dbConn.Update(sUpdateQuery);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lAgreementId;
	}
	public static HashMap<Integer, Map<String, Object>> fetchRentalAgreement(int SocietyId)
	{
		HashMap<Integer, Map<String, Object>> AgreementDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			String societyDbName = "";
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT * FROM `society` where society_id = '"+SocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			//System.out.println("societyDbName :"+societyDbName);
			m_dbConn = new DbOperations(false, societyDbName);
			String sqlForAgreement = "SELECT * from rentalagreements where Status = 'Y'";
			AgreementDetails =  m_dbConn.Select(sqlForAgreement);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return AgreementDetails;
	}
	// Add New Function Fetch Agreement by Tenant ID
	public static HashMap<Integer, Map<String, Object>> fetchRentalAgreementByTanent(int SocietyId,int iUnitId,int iTenantID)
	{
		//HashMap<Integer, Map<String, Object>> AgreementDetails = new HashMap<Integer, Map<String, Object>>();
		//HashMap<Integer, Map<String, Object>> VeringDetails = new HashMap<Integer, Map<String, Object>>();
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			String societyDbName = "";
			//int iAgreementID = 0;
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT * FROM `society` where society_id = '"+SocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			//System.out.println("societyDbName :"+societyDbName);
			m_dbConn = new DbOperations(false, societyDbName);
			//String sqlForAgreement = "SELECT * from rentalagreements where `TenantID` ='"+iTenantID+"' and UnitId='"+iUnitId+"' and Status = 'Y'";
			String sqlForAgreement = "SELECT r.*,u.description from rentalagreements as r join unit_type as u on u.id=r.PropertyType where `TenantID` ='"+iTenantID+"' and UnitId='"+iUnitId+"' and Status = 'Y'";
			HashMap<Integer, Map<String, Object>> AgreementDetails =  m_dbConn.Select(sqlForAgreement);
			
			rows.put("AgreementDetails", MapUtility.HashMaptoList(AgreementDetails));
			if(AgreementDetails.size() > 0)
			{
				int iAgreementID = 0;
				for(Entry<Integer, Map<String, Object>> entry1 : AgreementDetails.entrySet()) 
				{
					 iAgreementID = Integer.parseInt(entry1.getValue().get("ID").toString());
				}
				m_dbConn = new DbOperations(false, societyDbName);
				String sSqlVaringRent = "SELECT * FROM `rentdetails` where RentalAgreement_Id = " +  iAgreementID;
				HashMap<Integer, Map<String, Object>> VeringDetails =  m_dbConn.Select(sSqlVaringRent);
				rows.put("VaringDetails", MapUtility.HashMaptoList(VeringDetails));
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rows;
	}
	public static HashMap<Integer, Map<String, Object>> fetchRentalAgreementByTanent2(int SocietyId,int iUnitId,int iTenantID)
	{
		//HashMap<Integer, Map<String, Object>> AgreementDetails = new HashMap<Integer, Map<String, Object>>();
		//HashMap<Integer, Map<String, Object>> VeringDetails = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>> rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			String societyDbName = "";
			//int iAgreementID = 0;
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT * FROM `society` where society_id = '"+SocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			//System.out.println("societyDbName :"+societyDbName);
			m_dbConn = new DbOperations(false, societyDbName);
			//String sqlForAgreement = "SELECT * from rentalagreements where `TenantID` ='"+iTenantID+"' and UnitId='"+iUnitId+"' and Status = 'Y'";
			String sqlForAgreement = "SELECT r.*,u.description from rentalagreements as r join unit_type as u on u.id=r.PropertyType where `TenantID` ='"+iTenantID+"' and UnitId='"+iUnitId+"' and Status = 'Y'";
			rows =  m_dbConn.Select(sqlForAgreement);
			
			//rows.put("AgreementDetails",AgreementDetails);
			if(rows.size() > 0)
			{
				int iAgreementID = 0;
				for(Entry<Integer, Map<String, Object>> entry1 : rows.entrySet()) 
				{
					 iAgreementID = Integer.parseInt(entry1.getValue().get("ID").toString());
				}
				m_dbConn = new DbOperations(false, societyDbName);
				String sSqlVaringRent = "SELECT * FROM `rentdetails` where RentalAgreement_Id = " +  iAgreementID;
				HashMap<Integer, Map<String, Object>> VeringDetails =  m_dbConn.Select(sSqlVaringRent);
				//rows.put("VaringDetails", MapUtility.HashMaptoList(VeringDetails));
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rows;
	}
	
	public static long UpdateAgreementClauses(int iSocietyId,int iUnitId,int iAgreementID, String sMiscClauses,int iTenantID,String sAppoinmentDate, String sAppoinmentTime )
	{
		String societyDbName = "";
		int index;
		long lUpdateResult = 0, lUpdateTenatResult=0;;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			m_dbConn = new DbOperations(false, societyDbName);
			//Insert into tenant Module
			
			String sUpdateQuery = "UPDATE  `rentalagreements` SET  `MiscClauses` =  '"+sMiscClauses+"',`AppoimentDate`='"+sAppoinmentDate+"',`AppoinmentTime`='"+sAppoinmentTime+"' WHERE `ID` ='"+iAgreementID+"'";	
			//System.out.println(sUpdateQuery);	
			lUpdateResult = m_dbConn.Update(sUpdateQuery);
			
			//String UpdateTenantStatus = "UPDATE  `tenant_module` SET  `tenantStatus` =  '1' WHERE `tenant_id` ='"+iTenantID+"'";
			//lUpdateTenatResult = m_dbConn.Update(UpdateTenantStatus);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lUpdateResult;
	}
	
	public static long UpdateAgreement(int iSocietyID,int iRentalID,int iTenantID,int iPropertyType,int iPropertyUse,String sMonthlyRent,String sDeposit)
	{
		String societyDbName = "";
		int index;
		long lUpdateResult = 0, lUpdateResult1=0;;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyID+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			m_dbConn = new DbOperations(false, societyDbName);
			//Insert into tenant Module
			
			String sUpdateQuery = "UPDATE  `rentalagreements` SET `PropertyUse`='"+iPropertyUse+"',`PropertyType` = '"+iPropertyType+"',`Deposit`='"+sDeposit+"' where `TenantID`='"+iTenantID+"' and `ID`='"+iRentalID+"'";	
			//System.out.println(sUpdateQuery);	
			lUpdateResult = m_dbConn.Update(sUpdateQuery);
			
			String updateagreement = "UPDATE `rentdetails` SET `Rent`='"+sMonthlyRent+"' where `RentalAgreement_Id`='"+iRentalID+"'";
			lUpdateResult1 = m_dbConn.Update(updateagreement);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lUpdateResult;
	}
	public static long UpdateAgreement2(int iSocietyID,int iRentalID,int iTenantID,int iPropertyType,int iPropertyUse,String sDeposit,int iFrom[],int iTo[],int sRent[],int iRentDetailsID[])
	{
		String societyDbName = "";
		int index;
		long lUpdateResult = 0, lUpdateResult1=0;;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyID+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			m_dbConn = new DbOperations(false, societyDbName);
			
			String sUpdateQuery = "UPDATE  `rentalagreements` SET `PropertyUse`='"+iPropertyUse+"',`PropertyType` = '"+iPropertyType+"',`Deposit`='"+sDeposit+"' where `TenantID`='"+iTenantID+"' and `ID`='"+iRentalID+"'";	
				
			lUpdateResult = m_dbConn.Update(sUpdateQuery);
			for(int i = 0;i < 3; i++)
			{
				String Updateagreement = "UPDATE `rentdetails` SET `FromMonth`='" +iFrom[i]+ "',`ToMonth`= '" +iTo[i]+ "',`Rent` = '"+sRent[i]+"' where `RentalAgreement_Id`='"+iRentalID+"' and `ID`='"+iRentDetailsID[i]+"' ";
				lUpdateResult1 = m_dbConn.Update(Updateagreement);
				//System.out.println(Updateagreement);
				//System.out.println(lUpdateResult1);
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lUpdateResult;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
