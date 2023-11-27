package MainClasses;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.google.gson.Gson;

import CommonUtilityFunctions.MapUtility;

public class AgreementTerm
{
	static DbOperations m_dbConnRoot;
	static DbOperations m_dbConn;
	public AgreementTerm(DbOperations m_dbConnObj) 
	{
		m_dbConn = m_dbConnObj;	
	}
	public static HashMap<Integer, Map<String, Object>> fetchAgreementTermDetails(int itenantId,int SocietyId)
	{
		HashMap<Integer, Map<String, Object>> societyDetails = new HashMap<Integer, Map<String, Object>>();
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
			String sqlForOwnersDetails = "SELECT `city`,`region`,`postal_code` FROM `society` where society_id = '"+SocietyId+"'";
			//System.out.println("Sql: "+sqlForOwnersDetails);
			societyDetails =  m_dbConn.Select(sqlForOwnersDetails);
			//System.out.println("societyDetails :"+societyDetails);
			String sqlForLeaseDetails = "SELECT `start_date`,`end_date` FROM `tenant_module` where tenant_id = '"+itenantId+"' and status = 'Y'";
			//System.out.println("Sql: "+sqlForLeaseDetails);
			HashMap<Integer, Map<String, Object>> LeaseDetails = m_dbConn.Select(sqlForLeaseDetails);
			//System.out.println("LeaseDetails :"+LeaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : societyDetails.entrySet()) 
			{
				for(Entry<Integer, Map<String, Object>> entry1 : LeaseDetails.entrySet()) 
				{
					String StartDate = entry1.getValue().get("start_date").toString();
					String EndDate = entry1.getValue().get("end_date").toString();
					//System.out.println("StartDate :"+StartDate );
					//System.out.println("EndDate :"+EndDate);
					int Months = 0;
							//getMonthBetweenTwoDates(DateFunctionality.convertToStandardFormat(StartDate),DateFunctionality.convertToStandardFormat(EndDate));
					
					//System.out.println("Months :"+Months);
					entry.getValue().put("StartDate", DateFunctionality.convertToStandardFormat(StartDate));
					entry.getValue().put("EndDate", DateFunctionality.convertToStandardFormat(EndDate));
					entry.getValue().put("Months", Months);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return societyDetails;
	}
	public static HashMap<Integer, Map<String, Object>> fetchPropertyAddress(int iUnitId,int SocietyId)
	{
		HashMap<Integer, Map<String, Object>> societyDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			String societyDbName = "";
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+SocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet())
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			m_dbConn = new DbOperations(false, societyDbName);
			String sqlForOwnersDetails = "SELECT `society_add`,`city`,`region`,`postal_code` FROM `society` where society_id = '"+SocietyId+"'";
			//System.out.println("Sql: "+sqlForOwnersDetails);
			societyDetails =  m_dbConn.Select(sqlForOwnersDetails);
			//System.out.println("societyDetails :"+societyDetails);
			String sqlForUnit = "Select u.`unit_no`, u.`floor_no`,w.`wing` from `unit` as u,wing as `w` where u.`wing_id` = w.`wing_id` and u.`unit_id` = '"+iUnitId+"' and u.`society_id` ='"+SocietyId+"'";
			//System.out.println("sqlForUnit: "+sqlForUnit);
			HashMap<Integer, Map<String, Object>> unitDetails = m_dbConn.Select(sqlForUnit);
			for(Entry<Integer, Map<String, Object>> entry : societyDetails.entrySet()) 
			{
				for(Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet()) 
				{
					String floor = entry1.getValue().get("floor_no").toString();
					int iLast = floor.length();
					char cLast = floor.charAt(iLast-1);
					String sAppend = "th";
					if(cLast == '1')
					{
						sAppend = "st";
					}
					if(cLast == '2')
					{
						sAppend = "nd";
					}
					if(cLast == '3')
					{
						sAppend = "rd";
					}
					entry.getValue().put("UnitNo", entry1.getValue().get("unit_no"));
					entry.getValue().put("floorNo", entry1.getValue().get("floor_no")+sAppend);
					entry.getValue().put("Wing", entry1.getValue().get("wing"));
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return societyDetails;
	}
	public static int getMonthBetweenTwoDates(String StartDate, String EndDate)//yyyy-mm-dd format
	{
		//System.out.println("StartDate :"+StartDate+"\nEndDate :"+EndDate);
		String[] d1 = StartDate.split("-");
		String[] d2 = EndDate.split("-");
		LocalDate S1 = LocalDate.of(Integer.parseInt(d1[0]),Integer.parseInt(d1[1]),Integer.parseInt(d1[2])); 
		LocalDate E1 = LocalDate.of(Integer.parseInt(d2[0]),Integer.parseInt(d2[1]),Integer.parseInt(d2[2])); 
		//System.out.println("S1 :"+S1);
		//System.out.println("E1 :"+E1);
		Period p1 = Period.between(S1, E1);
		int year = p1.getYears();
		int months = p1.getMonths();
		if(year > 0)
		{
			months = months+year*12;
		}
		//System.out.println("months : "+months);
		return months;
	}
	public static long sendDetailsToDr(int itenantModuleId,String propertyType,String propertyUse,String pAddress1,String pAddress2,String pPincode,String pcity,String pregion,String propertyArea,String rentType,String deposit,int isocietyId, String monthlyRent)
	//public static long sendDetailsToDr(int itenantModuleId,int isocietyId, int iUnitID )
	{
		String societyDbName = "",todayDate="",FName="",mName="",lName="",contactO="",emailO="",houseO="",locationO="",cityO="",pincodeO="",tFName="",tMName="",tLName="",contactT="",emailT="",houseT="",locationT="",cityT="",pincodeT="",fromDate="",toDate="";
		boolean rentT = true;
		int index,age=0,tAge=0,rentAt=0, unitId=0,month=0;
		long result = 0;
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+isocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			todayDate = DateFunctionality.getCurrentDate();
			m_dbConn = new DbOperations(false, societyDbName);
			//Insert into tenant Module
			String sSelectQueryForTenantModule = "";
			
			sSelectQueryForTenantModule = "Select * from tenant_module where tenant_id = '"+itenantModuleId+"';";
			HashMap<Integer, Map<String, Object>> tenantModuleDetails = m_dbConn.Select(sSelectQueryForTenantModule);
			//System.out.println("tenantModuleDetails :"+tenantModuleDetails);
			for(Entry<Integer, Map<String, Object>> entry : tenantModuleDetails.entrySet()) 
			{
				tFName = entry.getValue().get("tenant_name").toString();
				tMName = entry.getValue().get("tenant_MName").toString();
				tLName = entry.getValue().get("tenant_LName").toString();
				contactT = entry.getValue().get("mobile_no").toString();
				emailT = entry.getValue().get("email").toString();
				houseT = entry.getValue().get("Address").toString();
				//locationT = entry.getValue().get("Address2").toString();
				cityT = entry.getValue().get("City").toString();
				pincodeT = entry.getValue().get("Pincode").toString();
				unitId = Integer.parseInt(entry.getValue().get("unit_id").toString());
				tAge = 0;
				
				fromDate = entry.getValue().get("start_date").toString();
				//System.out.println("fromDate :"+fromDate);
				toDate = entry.getValue().get("end_date").toString();
				//System.out.println("toDate :"+toDate);
				month = AgreementTerm.getMonthBetweenTwoDates(fromDate, toDate);
			}
			HashMap<Integer, Map<String, Object>> ownerDeatils = Owner.fetchOwnersDetails(unitId,isocietyId);
			//System.out.println("ownerDeatils : "+ownerDeatils);
			for(Entry<Integer, Map<String, Object>> entry: ownerDeatils.entrySet()) 
			{
				FName = entry.getValue().get("FirstName").toString();
				mName = entry.getValue().get("MiddleName").toString();
				lName = entry.getValue().get("LastName").toString();
				age = 0;
				houseO = entry.getValue().get("alt_address").toString();
				//locationO = entry.getValue().get("landmark").toString();
				//pincodeO = entry.getValue().get("postalCode").toString();
				//cityO = entry.getValue().get("city").toString();
			}
			EmailUtility em = new EmailUtility();
			String toEmail = "techsupport@way2society.com";
			String subject = "Digital Renting Details";
			//String subject = "New Renting ";
			String message = "<table  style='border-collapse:collapse'><tr>"
        		+ "<tr><td>Owner<br>================<br>First Name : "+FName+"<br>Middle Name : "+mName+"<br>Last Name : "+lName+"<br>Email : "+emailO+"<br>Contact : "+contactO+"<br>Age : "+age+" <br>House : "+houseO+"<br>Location : "+locationO+"<br>City : "+cityO+"<br>Pincode : "+pincodeO+"</td></tr>"
        		+ "<tr><td><br>Tenant<br>================<br>First Name : "+tFName+"<br>Middle Name : "+tMName+"<br>Last Name : "+tLName+"<br>Email : "+emailT+"<br>Contact : "+contactT+"<br>Age : "+tAge+" <br>House : "+houseT+"<br>Location : "+locationT+"<br>City : "+cityT+"<br>Pincode : "+pincodeT+"</td></tr>"
        		+ "<tr><td><br>Agreement Terms<br>==================<br>From Date : "+fromDate+" <br> To Date : "+toDate+"<br>House : "+pAddress1+"<br>Location : "+pAddress2+"<br>City : "+pcity+"<br>Pincode : "+pPincode+"<br>Property Type : "+propertyType+"<br>Area Type : "+propertyArea+"<br>No of months : "+month+"<br>Deposit : "+deposit+"</td></tr>";
			//	if(var1 == "")
				//{
				///	message += "<tr><td>Rent Type : true<br>Rent = "+monthlyRent+"<br></td></tr>";
				//}
				//else
				//{
				//	message += "<tr><td>Rent Type : false<br>";
				//	message += "var1 :"+var1+"<br>var2 : "+var2+"<br>var3 : "+var3+"<br>rent0 : "+rent1+"<br>rent1 : "+rent2+"<br>rent2 : "+rent3+"</td></tr>";
				//}
        		message += "<tr><td><br>doc_status<br>======================<br>Ref_Id :"+itenantModuleId+"<br>merchant_id : Way2Society</td></tr>"
        		+ "<tr><td><br><br></td></tr>"
        		+ "<tr><td>From </td></tr>"
        		+ "<tr><td>Way2Society</td>"
        		+ "</tr></table>";
        		//System.out.println(message);	
			result = Integer.parseInt(em.sendGenericMail(toEmail, subject, message));
			if(result == 1)
			{
				String sStatus = "1";
				ViewTenant vt = new ViewTenant();
				HashMap rows = vt.mUpdateTenantStatus(isocietyId, itenantModuleId, sStatus);
			}
			//lTenantId = m_dbConn.Insert(sInsertQuery);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public static long sendMailToDr(int itenantModuleId,int iUnitId,int isocietyId)
	{
		String societyDbName = "",todayDate="",FName="",mName="",lName="",contactO="",emailO="",houseO="",locationO="",cityO="",pincodeO="",tFName="",tMName="",tLName="",contactT="",emailT="",houseT="",locationT="",cityT="",pincodeT="",fromDate="",toDate="",pAddress1="",pcity="",propertyArea="",propertyType="",pPincode="",deposit="",societyName="";
		boolean rentT = true;
		int index,age=0,tAge=0,rentAt=0, unitId=0,month=0,iRentType=0,iRentalAgreementId=0;
		long result = 0;
		String unitNo = "",wing="";
		try
		{
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT `dbname`,`society_name` FROM `society` where society_id = '"+isocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
				societyName = entry.getValue().get("society_name").toString();
			}
			todayDate = DateFunctionality.getCurrentDate();
			m_dbConn = new DbOperations(false, societyDbName);
			//Insert into tenant Module
			String sSelectQueryForTenantModule = "";
			
			sSelectQueryForTenantModule = "Select * from tenant_module where tenant_id = '"+itenantModuleId+"';";
			HashMap<Integer, Map<String, Object>> tenantModuleDetails = m_dbConn.Select(sSelectQueryForTenantModule);
			//System.out.println("tenantModuleDetails :"+tenantModuleDetails);
			for(Entry<Integer, Map<String, Object>> entry : tenantModuleDetails.entrySet()) 
			{
				tFName = entry.getValue().get("tenant_name").toString();
				tMName = entry.getValue().get("tenant_MName").toString();
				tLName = entry.getValue().get("tenant_LName").toString();
				contactT = entry.getValue().get("mobile_no").toString();
				emailT = entry.getValue().get("email").toString();
				houseT = entry.getValue().get("Address").toString();
				//locationT = entry.getValue().get("Address2").toString();
				cityT = entry.getValue().get("City").toString();
				pincodeT = entry.getValue().get("Pincode").toString();
				unitId = Integer.parseInt(entry.getValue().get("unit_id").toString());
				tAge = 0;
				fromDate = entry.getValue().get("start_date").toString();
				//System.out.println("fromDate :"+fromDate);
				toDate = entry.getValue().get("end_date").toString();
				//System.out.println("toDate :"+toDate);
				month = AgreementTerm.getMonthBetweenTwoDates(fromDate, toDate);
			}
			HashMap<Integer, Map<String, Object>> ownerDeatils = Owner.fetchOwnersDetails(unitId,isocietyId);
			//System.out.println("ownerDeatils : "+ownerDeatils);
			for(Entry<Integer, Map<String, Object>> entry: ownerDeatils.entrySet()) 
			{
				FName = entry.getValue().get("FirstName").toString();
				mName = entry.getValue().get("MiddleName").toString();
				lName = entry.getValue().get("LastName").toString();
				age = 0;
				houseO = entry.getValue().get("alt_address").toString();
				wing =  entry.getValue().get("wing").toString();
				unitNo = entry.getValue().get("unit_no").toString();
				//locationO = entry.getValue().get("landmark").toString();
				//pincodeO = entry.getValue().get("postalCode").toString();
				//cityO = entry.getValue().get("city").toString();
			}
			String monthlyRent="",propertyUse="",appointment_date="",appointment_time="",appointment_comment="";
			String [] var = new String[3];
			String [] rent = new String[3];
			HashMap<Integer, Map<String, Object>> RentDeatils =RentalAgreement.fetchRentalAgreementByTanent2(isocietyId, iUnitId, itenantModuleId);
			//System.out.println("ownerDeatils : "+RentDeatils);
			Gson objGson1 = new Gson();
			String objStr1=objGson1.toJson(RentDeatils);
			//System.out.println(objStr1);
			
			for(Entry<Integer, Map<String, Object>> entry: RentDeatils.entrySet()) 
			{
				pAddress1 = entry.getValue().get("Property_Address").toString();
				pcity = entry.getValue().get("City").toString();
				pPincode = entry.getValue().get("PinCode").toString();
				propertyType = entry.getValue().get("PropertyType").toString();
				propertyArea = "Urban";
				propertyUse = entry.getValue().get("PropertyUse").toString();
				deposit = entry.getValue().get("Deposit").toString();
				iRentType = Integer.parseInt(entry.getValue().get("RentType").toString());
				iRentalAgreementId = Integer.parseInt(entry.getValue().get("ID").toString());
				appointment_date =  entry.getValue().get("AppoimentDate").toString();
				appointment_time =  entry.getValue().get("AppoinmentTime").toString();
				appointment_comment =  entry.getValue().get("MiscClauses").toString();
				String rentSql = "Select * from rentdetails where `RentalAgreement_Id` = '"+iRentalAgreementId+"' and status = 'Y'";
				HashMap<Integer, Map<String, Object>> details = m_dbConn.Select(rentSql);
				//System.out.println("Details :"+details);
				if(iRentType == 1)
				{
					for(Entry<Integer, Map<String, Object>> entry1: details.entrySet()) 
					{
						monthlyRent = entry1.getValue().get("Rent").toString();
					}
				}
				else
				{
					int i=0;
					for(Entry<Integer, Map<String, Object>> entry1: details.entrySet()) 
					{
						var[i] = entry1.getValue().get("FromMonth").toString();
						rent[i] = entry1.getValue().get("Rent").toString();
						i=i+1;
					}
				}
			}
			//System.out.println("monthly rent :"+monthlyRent);
			EmailUtility em = new EmailUtility();
			String toEmail = "techsupport@way2society.com";
			//String toEmail = "vaishali.manjrekar3@gmail.com";
			String subject = "New Rental : "+wing+unitNo+" "+societyName;
			String message = "<table  style='border-collapse:collapse'><tr>"
        		+ "<tr><td>Owner<br>================<br>First Name : "+FName+"<br>Middle Name : "+mName+"<br>Last Name : "+lName+"<br>Email : "+emailO+"<br>Contact : "+contactO+"<br>Age : "+age+" <br>Owner Address : "+houseO+"<br>Owner Location : "+locationO+"<br>City : "+cityO+"<br>Pincode : "+pincodeO+"</td></tr>"
        		+ "<tr><td><br>Tenant<br>================<br>First Name : "+tFName+"<br>Middle Name : "+tMName+"<br>Last Name : "+tLName+"<br>Email : "+emailT+"<br>Contact : "+contactT+"<br>Age : "+tAge+" <br>Tenant Address : "+houseT+"<br>Tenant Location : "+locationT+"<br>City : "+cityT+"<br>Pincode : "+pincodeT+"</td></tr>"
        		+ "<tr><td><br>Agreement Terms<br>==================<br>From Date : "+fromDate+" <br> To Date : "+toDate+"<br>Property Address : "+pAddress1+"<br>City : "+pcity+"<br>Pincode : "+pPincode+"<br>Property Type : "+propertyType+"<br>Area Type : "+propertyArea+"<br>Property Use : "+propertyUse+"<br>No of months : "+month+"<br>Deposit : "+deposit+"</td></tr>";
				if(iRentType == 1)
				{
					message += "<tr><td>Rent Type : true<br>Rent = "+monthlyRent+"<br></td></tr>";
				}
				else
				{
					message += "<tr><td>Rent Type : false<br>Varying : <br>";
					message += "var1 :"+var[0]+"<br>var2 : "+var[1]+"<br>var3 : "+var[2]+"<br>rent0 : "+rent[0]+"<br>rent1 : "+rent[1]+"<br>rent2 : "+rent[2]+"</td></tr>";
				}
        		message += "<tr><td><br>doc_status<br>======================<br>Ref_Id :"+itenantModuleId+"<br>merchant_id : Way2Society</td></tr>"
        		+ "<tr><td><br><br></td></tr>"
        		+ "<tr><td>From </td></tr>"
        		+ "<tr><td>Way2Society</td>"
        		+ "</tr></table>";
        		//System.out.println(message);	
        		result = Integer.parseInt(em.sendGenericMail(toEmail, subject, message));
			//lTenantId = m_dbConn.Insert(sInsertQuery);
        		if(result == 1)
    			{
    				String sStatus = "1";
    				ViewTenant vt = new ViewTenant();
    				HashMap rows = vt.mUpdateTenantStatus(isocietyId, itenantModuleId, sStatus);
    			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public static long addAgreementDetails(int iPropertyType,int iPropertyUse,String sPAddress,String sPPincode,String sPCity,String sPRegion,String sDeposit,int iRentType,int iUnitId, int iSocietyId)
	{
		String societyDbName = "";
		
		long lRentalAgreementId = 0;
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
			sInsertQuery = "insert into rentalagreements (`UnitId`,`PropertyUse`,`PropertyType`,`Property_Address`,`PinCode`,`City`,`Region`, `Deposit`,`RentType`) values ";
			sInsertQuery = sInsertQuery + "('" + iUnitId + "','" + iPropertyUse + "','" + iPropertyType + "','" + sPAddress +  "','" + sPPincode +  "','" + sPCity + "','"+sPRegion+"','"+sDeposit+"','"+iRentType+"')";
			//System.out.println(sInsertQuery);	
			lRentalAgreementId = m_dbConn.Insert(sInsertQuery);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lRentalAgreementId;
	}
	public static long addRentDetails(int iRentalAgreementId,int iRentType,String sStartDate,String sEndDate,String sMonthlyRent,int iFrom[],int iTo[],String sRent[],int iSocietyId)
	{
		String societyDbName = "";
		
		long lRentDetailsId = 0;
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
			int iToMonth = 0,iFromMonth = 1;
			if(iRentType == 1)
			{
				//sStartDate = DateFunctionality.convertToStandardFormat(sStartDate);
				//sEndDate = DateFunctionality.convertToStandardFormat(sEndDate);
				iToMonth = getMonthBetweenTwoDates(sStartDate, sEndDate);
				sInsertQuery = "insert into rentdetails (`RentalAgreement_Id`,`FromMonth`,`ToMonth`,`Rent`) values ";
				sInsertQuery = sInsertQuery + "('" + iRentalAgreementId + "','" +iFromMonth+ "','" + iToMonth + "','" + sMonthlyRent +  "')";
				//System.out.println(sInsertQuery);	
				lRentDetailsId = m_dbConn.Insert(sInsertQuery);
			}
			else
			{
				sStartDate = DateFunctionality.convertToSqlFormat(sStartDate);
				sEndDate = DateFunctionality.convertToSqlFormat(sEndDate);
				for(int i = 0;i < 3; i++)
				{
					sInsertQuery = "insert into rentdetails (`RentalAgreement_Id`,`FromMonth`,`ToMonth`,`Rent`) values ";
					sInsertQuery = sInsertQuery + "('" + iRentalAgreementId + "','" +iFrom[i]+ "','" + iTo[i]+ "','" + sRent[i]+  "')";
					//System.out.println(sInsertQuery);	
					lRentDetailsId = m_dbConn.Insert(sInsertQuery);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lRentDetailsId;
	}
	public static void main(String[] args) 
	{
		String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4yShq3gS2XOamTB-D4umDzi2TYQfnG5nBEn_d3K5HEbfKlAGWg8f-FjOl6u9vV4NJvFoxg2ZtUMth9Bdt6aVKx";
		String sTkey = "nXTYH6gXb5NrNS7bD-xf-7p-DLueHDMHV63rrzV8xdR5QxtyKNUQph89PNpuyjn5qq1PT2kOpx3iSYamT24xEmrftFMt-IfABEKDKRDSmEhS0-n02MO74v-qHylcskOL79mjDGY4VS4R1dqZybT3Mg";
		//ViewOwner obj = new ViewOwner(sToken, true, sTkey);
		int month =  getMonthBetweenTwoDates("2018-01-01", "2018-12-01");
		//Gson objGson = new Gson();
		//String objStr = objGson.toJson(rows);
		System.out.println("month :"+month);
	}

}
