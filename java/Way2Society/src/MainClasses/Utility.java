package MainClasses;

import java.util.HashMap;
import java.util.List;

import static MainClasses.DbConstants.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Calendar;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import CommonUtilityFunctions.MapUtility;
import CommonUtilityFunctions.CalendarFunctions;
import MainClasses.DbOperations.*;
import MainClasses.EmailUtility;
import ecryptDecryptAlgos.ecryptDecrypt;

public class Utility 
{
	static DbOperations m_dbConn;
	static DbRootOperations m_dbConnRoot;
	private static CommonUtilityFunctions.CalendarFunctions m_CalendarFunctions;
	private  TimeZoneConvertor m_Timezone;
	
	public Utility(DbOperations m_dbConnObj, DbRootOperations m_dbConnRootObj) throws ClassNotFoundException
	{
		m_dbConn = m_dbConnObj;
		m_dbConnRoot = m_dbConnRootObj;
		m_CalendarFunctions = new CalendarFunctions();
		m_Timezone=new TimeZoneConvertor();
	}
	
	
	public static  HashMap<Integer, Map<String, Object>> getProfileDetails(int iLoginID, int iUnit_id)
	{
    	String sSqlProfile = "SELECT * FROM `profile` as prof JOIN `mapping` as mappingtbl on mappingtbl.profile = prof.id where mappingtbl.login_id ="+ iLoginID + " and mappingtbl.unit_id ="+ iUnit_id;   	
    	System.out.println("sSqlProfile : " + sSqlProfile);
    	HashMap<Integer, Map<String, Object>> mpProfile = m_dbConnRoot.Select(sSqlProfile);
        
        //System.out.println("mpProfile" + mpProfile);
/*
        if(mpProfile.size() > 0 )
        {
        	int i = 0;
        	for(Entry<Integer, Map<String, Object>> entry : mpProfile.entrySet()) 
			{
        		//System.out.println("Iteration: " + i);
        		//String unit_id = (entry.getValue().get("unit_id")).toString();
				
				System.out.println("Profile: " + entry);
				
			}
        }
       */
        return mpProfile;
	}

	
	public static  HashMap<Integer, Map<String, Object>> getBlockUnitDetails(int iUnit_id)
	{
		String isBlock = "SELECT block_unit, block_desc from unit where unit_id = '" + iUnit_id + "'";
		//System.out.println("isBlock: " + isBlock);
		HashMap<Integer, Map<String, Object>> mpBlockDetails = m_dbConn.Select(isBlock);
		System.out.println("mpBlockDetails: " + mpBlockDetails);
		
		for(Entry<Integer, Map<String, Object>> entry_1 : mpBlockDetails.entrySet())
		{
			if(entry_1.getValue().get("block_unit") != null)
			{
				String sBlockUnit = entry_1.getValue().get("block_unit").toString();
				String sBlockDesc = entry_1.getValue().get("block_desc").toString();
				System.out.println("Unit blocked: " + entry_1.getValue().get("block_unit").toString());
			}
			
		}
		return mpBlockDetails;
	}
	
	
	public static  HashMap<Integer, Map<String, Object>> getAdvertisements(String sRole, boolean bTitleOnly, int iFetchCount)
	{
		 
		//Role 0=All, 1=Member, 2=Admin Member, 3=Admin, 
		System.out.println("sRole: " + sRole);
		System.out.println("bTitleOnly: " + bTitleOnly);

    	String sSqlAdv = "";
    	if(bTitleOnly == true)
    	{
    		sSqlAdv = "SELECT id, title FROM `adv_repo` where status = 1 ";   		
    	}
    	else
    	{
    		sSqlAdv = "SELECT title, description, attachment, sortOrder, weblink, id, advImage, startDate, endDate, status FROM `adv_repo`";
    	}
    	    	
		if(sRole == "Admin")
		{
			sSqlAdv += " and target < 3 ";
			
		}
		else if(sRole == "Admin Member")
		{
			sSqlAdv += " and target < 2 ";
			
		}
		else if(sRole == "Member")
		{
			sSqlAdv += " and target < 1 ";			
		}
		else
		{
			//do nothing
		}
		
    	sSqlAdv += " order by sortOrder";   	
		if(iFetchCount > 0)
		{
			sSqlAdv += " LIMIT " + iFetchCount;
		}

    	//System.out.println("sSqlAdv: " + sSqlAdv);
		
    	HashMap<Integer, Map<String, Object>> mpAdv= m_dbConnRoot.Select(sSqlAdv);        
//        System.out.println("mpAdv" + mpAdv);
        return mpAdv;
	}
	public static String  getMemberIDs(String Date)
	{
		// fetching memeber which have active ownership status 
		String sSql= "";
		String[] sKeys = {"0"};
		
		HashMap<Integer, Map<String, Object>> mpUnitData = getUnitData(0,Date);
		if(mpUnitData.size() > 0)
		{
			for(Entry<Integer, Map<String, Object>> entry : mpUnitData.entrySet()) 
			{
				sKeys[(sKeys.length) -1] = (String) entry.getValue().get("member_id");
					
			}
		}
		
		String strKeys = String.join(",", sKeys);
		return strKeys;
	}
	
	
	public  static HashMap<Integer, Map<String, Object>> getUnitData(int iUnitID,String Date)
	{
		//fetching unit details
		String sSql = "";
		
		String sqlII = "SELECT member_id, unit, owner_name, ownership_date FROM member_main where ownership_date <= '"+ Date +"' ";
		if(iUnitID > 0)
		{
			sqlII += " and   unit = '"+ iUnitID + "'  ";	
		}
		
		sqlII += "  ORDER BY ownership_date DESC";
		
		sSql = "SELECT member_id, owner_name, ownership_date,unit FROM ("+ sqlII +") AS t1 ";
		if(iUnitID > 0)
		{
			sSql += "where  unit = '"+ iUnitID +"'  ";	
		}
		sSql += "  GROUP BY unit";
		
		HashMap<Integer, Map<String, Object>> mpUnitData =  m_dbConn.Select(sSql);
		
		return mpUnitData;
	}
	/* -------------------------------------  Vehicle Renewal for society id 288 -------------------- */
	public static long VehicleRenewal(int iMemberParkingID,int iSocietyID,int VehicleType) throws ClassNotFoundException
	{
		String sUpdateQuery = "";
		if(VehicleType == 1)
		{	
			sUpdateQuery = "Update `mem_car_parking` set Renew_Registration='1',Renew_Registration_Date ='" +java.time.LocalDate.now()+"' Where 	mem_car_parking_id = '"+iMemberParkingID+"'";
		}
		else if(VehicleType == 2)
		{
			 sUpdateQuery = "Update `mem_bike_parking` set Renew_Registration='1', Renew_Registration_Date ='" +java.time.LocalDate.now()+"' Where mem_bike_parking_id = '"+iMemberParkingID+"'";
		}
		System.out.println(sUpdateQuery);
		long updateEntry = m_dbConn.Update(sUpdateQuery);
		
		if(updateEntry > 0)
		{
			return 1;
		}
		else
		{
		return  0;
		}
		
	}

	
	public static String  getSocietyBeginingDate()
	{
		String sSql= "";
		int iSocietyCreationYearID = 0;
		String sSqlSociety = "Select society_creation_yearid from society";
		HashMap<Integer, Map<String, Object>> mpSociety=  m_dbConn.Select(sSqlSociety);
		
		Map.Entry<Integer, Map<String, Object>> entrySociety = mpSociety.entrySet().iterator().next();
		iSocietyCreationYearID = (int) entrySociety.getValue().get("society_creation_yearid");
		
		sSql = "Select  DATE_FORMAT(BeginingDate,'%d %b, %Y') as BeginingDate from year where YearID = '"+ iSocietyCreationYearID + "' ";
		HashMap<Integer, Map<String, Object>> mpDates=  m_dbConn.Select(sSql);
		
		Map.Entry<Integer, Map<String, Object>> entry = mpDates.entrySet().iterator().next();
		if(entry.getValue().get("BeginingDate") != null)
		{
			return (String) entry.getValue().get("BeginingDate");
		}
		
		
		return "00-00-0000";
		
							
		
	}
	
	
	
	public static HashMap  getReceiptBeginingAndEndingDateByPeriodID(int iPeriodID)
	{
		//fetching Receipt Begining And EndingDate By PeriodID
		String sSql= "";
		HashMap result = new HashMap<>();
		
		sSql = "SELECT DATE_FORMAT(BillDate - INTERVAL 1 DAY,'%d %b, %Y')  as Date,`PeriodID` FROM `billregister` where `PeriodID` ='"+ iPeriodID + "' UNION SELECT DATE_FORMAT(BillDate,'%d %b, %Y') as Date,`PeriodID` FROM `billregister` as bilreg JOIN `period` as periodtbl on bilreg.PeriodID = periodtbl.PrevPeriodID where periodtbl.ID ='"+ iPeriodID + "' ";
		HashMap<Integer, Map<String, Object>> mpDates =  m_dbConn.Select(sSql);
		
		//Map.Entry<Integer, Map<String, Object>> entry = mpDates.entrySet().iterator().next();
		
		
		
		for(Entry<Integer, Map<String, Object>> entry : mpDates.entrySet()) 
		{
			if((int) entry.getValue().get("PeriodID") == iPeriodID)
			{
				//if iPeriodID and records fetch from database match then only add end date to map
				//return (String) entry.getValue().get("BeginingDate")
						result.put("end_date", entry.getValue().get("Date"));
				//result.put("end_date",(Date)m_CalendarFunctions.subtractDay((Date)entry.getValue().get("Date"),1));
			}
			else
			{
				result.put("start_date", entry.getValue().get("Date"));
			}
		}
		
		return result;
}
	//Function to get number of active and inactive units
	public static HashMap<Integer, Map<String, Object>> mfetchUnitsStatistics(int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();

		try 
		{
			
			String selectActiveUnit= "select count(*) as UnitCount, status from mapping where society_id = " + iSocietyId + " group by status";
			//System.out.println("Unit query :" +selectActiveUnit);
			HashMap<Integer, Map<String, Object>>  mpUnitsStatus = m_dbConnRoot.Select(selectActiveUnit);
//			System.out.println("Unit mpUnitsStatus:" + mpUnitsStatus);
		 	for(Entry<Integer, Map<String, Object>> Review : mpUnitsStatus.entrySet()) 
		 	{
		 		Integer iStatus = (Integer) Review.getValue().get("status");
//		 		Integer iCount = (Integer) Review.getValue().get("UnitCount");
//		 		System.out.println("iStatus:" + iStatus + "  and Count :" + iCount);
		 		switch (iStatus)
		 		{
			 		case 0:
			 		{
						rows2.put("UndefinedUnits", Review.getValue().get("UnitCount"));
			 			break;
			 		}
			 		case 1:
			 		{
				 		//rows.put("InactiveUnits", iCount);
				 		rows2.put("InactiveCodes", Review.getValue().get("UnitCount"));
			 			break;
			 		}
			 		case 2:
			 		{
						//rows.put("ActiveUnits1", iCount);
						rows2.put("ActiveUsers", Review.getValue().get("UnitCount")); //Done
			 			break;
			 		}
			 		case 3:
			 		{
						//rows.put("DeletedeUnits", iCount);
						rows2.put("DeletedeUnits", Review.getValue().get("UnitCount"));
			 			break;
			 		}
			 		default:
			 		{
						//rows.put("ErrorUnits", iCount);
						rows2.put("ErrorUnits", Review.getValue().get("UnitCount"));
			 			break;
			 		}
		 		
		 		}
			}
		 	 

		 	//To get totoal number of units
			String sSelectUnit= "select count(distinct(unit_id)) as ActiveUnitCount from mapping where society_id = '" + iSocietyId + "' and  status = 2 and unit_id not in (0)";
//			System.out.println("sSelectUnit query:" + sSelectUnit);
			HashMap<Integer, Map<String, Object>>  mpActiveUnits = m_dbConnRoot.Select(sSelectUnit);
//			System.out.println("mpActiveUnits status:" + mpActiveUnits);
			
			int iActiveUnitCount = 0;
		 	for(Entry<Integer, Map<String, Object>> ActiveUnits : mpActiveUnits.entrySet()) 
		 	{
		 		String sActiveUnitCount = ActiveUnits.getValue().get("ActiveUnitCount").toString();
				iActiveUnitCount = Integer.parseInt(sActiveUnitCount);
				//rows2.put("ActiveUnitCount", ActiveUnits.getValue().get("ActiveUnitCount"));
				rows2.put("ActiveUnitCount", iActiveUnitCount);
				break;
		 	}			
		 			
		 	//To get totoal number of units
			sSelectUnit= "Select count(*) as TotalUnits from unit where society_id='" + iSocietyId + "'";
			HashMap<Integer, Map<String, Object>>  mpUnits = m_dbConn.Select(sSelectUnit);			
			int iTotalUnits = 0;
			for(Entry<Integer, Map<String, Object>> TotalUnits : mpUnits.entrySet()) 
		 	{
				String sTotalUnits = TotalUnits.getValue().get("TotalUnits").toString();
				iTotalUnits = Integer.parseInt(sTotalUnits);
				//rows2.put("TotalUnits", TotalUnits.getValue().get("TotalUnits"));
				rows2.put("TotalUnits", iTotalUnits);
				break;
		 	}		
			int iInactiveUnits = iTotalUnits - iActiveUnitCount;
			rows2.put("InactiveUnits", iInactiveUnits);
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			
		}
		return rows2;
	}
	
	

	public static String convertToCurrentTimeZone(String Date) 
	{
        String converted_date = "";
        try {

            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = utcFormat.parse(Date);

            DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            //currentTFormat.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));
            currentTFormat.setTimeZone(TimeZone.getTimeZone("IST"));
           // DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		//pstFormat.setTimeZone(TimeZone.getTimeZone("IST"));
    		//
            converted_date =  currentTFormat.format(date);
        }catch (Exception e){ e.printStackTrace();}
        //System.out.println(converted_date);
        return converted_date;
	}

	public static String getSocietyDBNames(int iSocietyID)
	{
		String m_sSocietyDB = "";
		try
		{
			//dbop = CommonBaseClass.m_objDbOperations;
			//DbOperations dbop_root = new DbOperations(DB_DATABASE_ROOT);
			String sql3 = "Select * from society where society_id = '"+ iSocietyID +"' and status = 'Y';";
			System.out.println("sql3:"+sql3);
			HashMap<Integer, Map<String, Object>> societyDetails = m_dbConnRoot.Select(sql3);
			System.out.println("society:"+societyDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet()) 
			{	m_sSocietyDB = (String)entry1.getValue().get("dbname");
				if( (String)entry1.getValue().get("security_dbname")!=null)
				{
					m_sSocietyDB =(String)entry1.getValue().get("dbname");
				}
				else
				{
					m_sSocietyDB="";
				}
			}
			System.out.println("sDB:"+ m_sSocietyDB);
			if(m_sSocietyDB.length() > 0)
			{
				//m_bHasSecurityDB = true;
				//dbop_security = new DbOperations(m_sSecurityDB);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			
		}
		System.out.println("sDB4 " + m_sSocietyDB);
		return m_sSocietyDB;
	}
	
	
	public static String getDBNames(int iSocietyID)
	{
		String sSecurityDB = "";
		try
		{
			//dbop = CommonBaseClass.m_objDbOperations;
			//DbOperations dbop_root = new DbOperations(DB_DATABASE_ROOT);
			String sql3 = "Select * from society where society_id = '"+ iSocietyID +"' and status = 'Y';";
			System.out.println("sql3:"+sql3);
			HashMap<Integer, Map<String, Object>> societyDetails = m_dbConnRoot.Select(sql3);
			System.out.println("society:"+societyDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet()) 
			{
				String sSocietyName = (String)entry1.getValue().get("society_name");
				String sSocietyCode = (String)entry1.getValue().get("society_code");
				//m_sSocietyDB = (String)entry1.getValue().get("dbname");
				if( (String)entry1.getValue().get("security_dbname")!=null)
				{
					sSecurityDB =(String)entry1.getValue().get("security_dbname");
				}
				else
				{
					sSecurityDB="";
				}
			}
			System.out.println("sSecurityDB:"+ sSecurityDB);
			if(sSecurityDB.length() > 0)
			{
				//m_bHasSecurityDB = true;
				//dbop_security = new DbOperations(m_sSecurityDB);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			
		}
		System.out.println("sSecurityDB4 " + sSecurityDB);
		return sSecurityDB;
	}
	public static HashMap<Integer, Map<String, Object>> mfetchAllVisitorsDetails(int VisitorID, int iSocietyID)
	{
		String sSecurityDB = getDBNames(iSocietyID);
		HashMap<Integer, Map<String, Object>>  visitorEntryDetails = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>>  mpVisitors = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "",sql2="";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String StatusBy="inside",sql1="",MonthDate="";
		String dateOnly="",timeOnly="";
		String OutMonthDate ="";
		String flag="";
		//if(m_bHasSecurityDB)
		{
			try
			{
				System.out.println("sSecurityDB "+ sSecurityDB);
				DbOperations dbop_security = new DbOperations(false,sSecurityDB);
				System.out.println("Database Security :" +dbop_security.toString());
				//sql1="Select id,verified,visitor_ID,visitorMobile,unit_id,purpose_id,vehicle,otpGenerated,otpGtimestamp,otpStatus,status,outTimeStamp from visitorentry as v join `purpose` as p.purpose_id=v.purpose_id where id='"+VisitorID+"'";
				sql1="Select v.id,v.verified,v.visitor_ID,v.visitorMobile,v.purpose_id,v.company,v.vehicle,v.otpGenerated,v.otpGtimestamp,v.otpStatus,v.status,v.outTimeStamp,p.purpose_name,v.Entry_Gate,v.Exit_Gate,va.unit_id,v.visitor_note from visitorentry as v inner join visit_approval as va on v.id=va.v_id inner join `purpose` as p on p.purpose_id=v.purpose_id where v.id='"+VisitorID+"'";
				System.out.println("SM Table query : "+sql1);
				visitorEntryDetails = dbop_security.Select(sql1);
				System.out.println("VisitorDetails size:"+visitorEntryDetails.size());
				System.out.println("VisitorDetails:"+visitorEntryDetails);
				String sqlflag;
				HashMap<Integer, Map<String, Object>>  flagDetails = new HashMap<Integer, Map<String, Object>>();
//				System.out.println(mpVisitors);
				if(visitorEntryDetails.size() > 0)
				{
					for(Entry<Integer, Map<String, Object>> entry : visitorEntryDetails .entrySet()) 
					{
						 dbop_security = new DbOperations(false,sSecurityDB);
						int unit_id=Integer.parseInt(entry.getValue().get("unit_id").toString());
						int id=Integer.parseInt(entry.getValue().get("id").toString());
						
						System.out.println("unit_id : " + unit_id);
						System.out.println("id : " + id);
						
						sqlflag="select Entry_flag from visit_approval where unit_id='"+unit_id+"' and v_id='"+id+"'";
						flagDetails=dbop_security.Select(sqlflag);
						for(Entry<Integer, Map<String, Object>> entryflag : flagDetails.entrySet())
						{
							flag = entryflag.getValue().get("Entry_flag").toString();
							
						}
						
						System.out.println("Inside For Loop");
						DbOperations sm_dbRoot = new DbOperations(false,"security_rootdb");
						System.out.println("Database Security Root :" + sm_dbRoot);
						//sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.Doc_img,v.Doc_No,v.Doc_Type_ID,d.document,v.img,v.Company from visitors as v, documents as d where v.Doc_Type_ID = d.id and v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
						sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.img from visitors as v where v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
						System.out.println("Security Root Query :" +sSelectQuery);
						mpVisitors = sm_dbRoot.Select(sSelectQuery);
						System.out.println("VisitorDetails12 size:"+mpVisitors.size());
						System.out.println("VisitorDetails2:"+mpVisitors);
						HashMap rows1 = new HashMap<>();
						if(entry.getValue().get("otpGtimestamp") != null)
						{
						MonthDate =entry.getValue().get("otpGtimestamp").toString();
						String Time = convertToCurrentTimeZone(MonthDate); //pending
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);
						entry.getValue().put("VisitorDetails", mpVisitors);
						Date dt = df.parse(Time);
	
						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
						timeOnly = tdf.format(dt);
						dateOnly = dfmt.format(dt);
						//System.out.println("InTime:"+timeOnly);
						//System.out.println("date :"+dateOnly + "<br>");
						
						entry.getValue().put("InTime", timeOnly);
						entry.getValue().put("Date", dateOnly);
						System.out.println("entry : " + entry);
						}
					
					if(entry.getValue().get("outTimeStamp") != null)
					{
						OutMonthDate =entry.getValue().get("outTimeStamp").toString();
						String OutTime = convertToCurrentTimeZone(OutMonthDate); //pending
						
						
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);

						Date dt = df.parse(OutTime);

						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");


						String OuttimeOnly = tdf.format(dt);
						String OutdateOnly = dfmt.format(dt);
						
				 		entry.getValue().put("OutTime", OuttimeOnly);
						entry.getValue().put("OutDate", OutdateOnly);
				 		
					}
					else
					{
						String OuttimeOnly = "00:00";
						String OutdateOnly = "00-00-00";
						entry.getValue().put("OutTime", OuttimeOnly);
						entry.getValue().put("OutDate", OutdateOnly);
				 		
					}
						//dbop = new DbOperations(DB_SOCIETY);
					String db=getSocietyDBNames(iSocietyID);
					dbop_security=new DbOperations(false,db);
						System.out.println("Databse Society : "+ dbop_security);
						//HashMap<Integer, Map<String, Object>>  unitDetails = new HashMap<Integer, Map<String, Object>>();
						sql1="Select u.`unit_no`, mm.`primary_owner_name` from unit u, member_main mm where u.`unit_id` = '"+ entry.getValue().get("unit_id") + "' and u.unit_id = mm.`unit` and mm.`status` = 'Y'";
						System.out.println("Society query " +sql1);
						HashMap<Integer, Map<String, Object>> unitDetails = dbop_security.Select(sql1);
						System.out.println("Society query " +unitDetails);
						if(unitDetails.size() < 0)
						{
							entry.getValue().put("UnitNo","");
							entry.getValue().put("OwnerName","");
						}
						else
						{
							
							for(Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet())
							{
								entry.getValue().put("UnitNo",entry1.getValue().get("unit_no"));
								entry.getValue().put("OwnerName",entry1.getValue().get("primary_owner_name"));
							}
						}

						entry.getValue().put("FlagStatus", flag);
						
					}
					//add document to map
						
					// rows2.put("visitors",MapUtility.HashMaptoList(visitorEntryDetails));
					 rows.put("success",1);
					 rows.put("visitors",MapUtility.HashMaptoList(visitorEntryDetails));			 
				}
				else
				{
					//documents not found
					 rows2.put("visitors","");
					 rows2.put("message ","No Visitor found" );
					 rows.put("success",0);
					 rows.put("response",rows2);
				}
			}
			catch(Exception e) 
			{
				e.printStackTrace();
				rows2.put("exception", e);
				rows.put("success",0);
				rows.put("response",rows2);
			}
		}
/*		else
		{
			// rows2.put("visitors","");
			 rows2.put("message ","No security database" );
			 rows.put("success",0);
			 rows.put("response",rows2);					
		}*/
		
		System.out.println(rows);
		 return  rows;
		
	}
	
	public long insertexpectedvisitor(int iSocietyID,String sFName,String sLName,String mobile,String ex_date,String ex_time,int unitid,int iPurposeID,String sNote) throws ClassNotFoundException 
	{
		String sSecurityDB = getDBNames(iSocietyID);
		System.out.println("sSecurityDB "+ sSecurityDB);
		//String lname = "";
		if(sSecurityDB != "")
		{
		DbOperations dbop_security = new DbOperations(false,sSecurityDB);
		String sSqlDirectory = "INSERT INTO `expected_visitor`(`fname`, `lname`,`mobile`, `expected_date`, `expected_time`, `unit`,`purpose_id`,`note`) VALUES ('"+sFName+"','"+sLName+"','"+mobile+"','"+ex_date+"','"+ex_time+"','"+unitid+"','"+iPurposeID+"','"+sNote+"') ";
		long mpinsert =  dbop_security.Insert(sSqlDirectory);
		
		return mpinsert;
		}
		else
			return 0;
	}
	public HashMap<Integer, Map<String, Object>> mfetchexpected(int iSocietyID, int iUnitId) throws ClassNotFoundException 
	{
		
		String sSecurityDB = getDBNames(iSocietyID);
		System.out.println("sSecurityDB "+ sSecurityDB);

		HashMap<Integer, Map<String, Object>> mpVisitor ;
		if(sSecurityDB != "")
		{
		DbOperations dbop_security = new DbOperations(false,sSecurityDB);
		//DbOperations dbop_security = new DbOperations(false,"sm_1");
		System.out.println("Database Security :" +dbop_security.toString());
		//String sSqlDirectory = "SELECT fname,lname,mobile,expected_date,expected_time from expected_visitor where unit='"+iUnitId+"'";
		String sSqlDirectory ="SELECT ev.fname,ev.lname,ev.mobile,ev.expected_date,ev.expected_time,ev.note,p.purpose_name from `expected_visitor` as ev join `purpose` as p on p.`purpose_id`=ev.`purpose_id` where ev.unit='"+iUnitId+"' and ev.expected_date >= DATE(NOW()) ORDER BY ev.expected_date ASC ";
		System.out.println(sSqlDirectory);
		mpVisitor =  dbop_security.Select(sSqlDirectory);
		return mpVisitor;
		}
		else
		{
			//HashMap  mpVisitors = new HashMap<>();
			//mpVisitors.put("Visitor", "No Database available");
			//mpVisitors.put("sm", "0");
			return null;
		}
	}

	
	public HashMap<Integer, Map<String, Object>> mfetchvisitor(int iSocietyID, int iUnitId) throws ClassNotFoundException 
	{
		
		DbOperations dbop;		
		String sSecurityDB = getDBNames(iSocietyID);
		System.out.println("sSecurityDB "+ sSecurityDB);
		HashMap<Integer, Map<String, Object>> rows = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpVisitor ;
		if(sSecurityDB != "")
		{
			DbOperations dbop_security = new DbOperations(false,sSecurityDB);
			//DbOperations dbop_security = new DbOperations(false,"sm_1");
			System.out.println("Database Security :" +dbop_security.toString());
		
			String sSqlDirectory = "SELECT v.id as ID,v.visitor_ID,v.company,v.vehicle,v.visitor_note,v.visitorMobile,v.status,va.*,p.`purpose_name`FROM `visitorentry` as v inner join visit_approval as va on v.id=va.v_id inner join  purpose as p on v.`purpose_id` = p.`purpose_id` where status='inside' and  va.unit_id='"+iUnitId+"'";
			System.out.println(sSqlDirectory);
			mpVisitor=  dbop_security.Select(sSqlDirectory);
			String sSelectQuery;
			System.out.println("mpVisitor : " + mpVisitor);
			HashMap<Integer, Map<String, Object>>  mpVisitors = new HashMap<Integer, Map<String, Object>>();
			for(Entry<Integer, Map<String, Object>> entry : mpVisitor .entrySet()) 
			{	
				System.out.println("Id : " + Integer.valueOf(entry.getValue().get("ID").toString()));
				System.out.println("SocId : " + iSocietyID);
				
				rows = fechvisitoritem(Integer.valueOf(entry.getValue().get("ID").toString()),iSocietyID);
				System.out.println("Rows : " + rows);
				if(rows  != null)
				{
					entry.getValue().put("VisitorItem", rows);
				}
				else
				{
					entry.getValue().put("VisitorItem", "");
				}
				
				dbop=new DbOperations(false,"security_rootdb");
				sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.img from visitors as v  where  v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
				mpVisitors = dbop.Select(sSelectQuery);
				//System.out.println(mpVisitors);

				entry.getValue().put("Visitor", mpVisitors);
				
			}
			
		return mpVisitor;
		}
		else
		{

			//HashMap  mpVisitors = new HashMap<>();
			//mpVisitor.put("Visitor", "No Database available");
			//mpVisitor.put("sm", "0");
			return null;
		}
		//return mpVisitor;
	}
	public HashMap<Integer, Map<String, Object>> mfetchvisitoroutside(int iSocietyID, int iUnitId) throws ClassNotFoundException 
	{
		DbOperations dbop;
		String sSecurityDB = getDBNames(iSocietyID);
		System.out.println("sSecurityDB "+ sSecurityDB);
		HashMap<Integer, Map<String, Object>> mpVisitor ;
		if(sSecurityDB != "")
		{
		DbOperations dbop_security = new DbOperations(false,sSecurityDB);
		//DbOperations dbop_security = new DbOperations(false,"sm_1");
		System.out.println("Database Security :" +dbop_security.toString());
		String sSqlDirectory = "SELECT v.id as ID,v.company,v.vehicle,v.visitor_note,v.visitor_ID,v.visitorMobile,v.status,va.*,p.`purpose_name`,v.`otpGtimestamp` FROM `visitorentry` as v inner join visit_approval as va on v.id=va.v_id inner join  purpose as p on v.`purpose_id` = p.`purpose_id` where status='outside' and  va.unit_id='"+iUnitId+"'";
		
		System.out.println(sSqlDirectory);
		mpVisitor =  dbop_security.Select(sSqlDirectory);
		String sSelectQuery;
		HashMap<Integer, Map<String, Object>>  mpVisitors = new HashMap<Integer, Map<String, Object>>();
		
		for(Entry<Integer, Map<String, Object>> entry : mpVisitor .entrySet()) 
		{	
			
			dbop=new DbOperations(false,"security_rootdb");
			sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.img from visitors as v  where  v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
			mpVisitors = dbop.Select(sSelectQuery);
			System.out.println(mpVisitors);

			entry.getValue().put("Visitor", mpVisitors);
		}
			
		
		return mpVisitor;
		}
		else
		{

			//HashMap  mpVisitors = new HashMap<>();
			//mpVisitors.put("Visitor", "No Database available");
			//mpVisitors.put("sm", "0");
			return null;
		}
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchVisitorDetails(int ivisitorId,int isocietyId)
	{ 
		//System.out.print("Test1");
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			//System.out.print("Test1");
			String sSelectSecurityDb = "select `dbname`,`security_dbname` from `society`  where society_id= '"+isocietyId+"'";
			//System.out.print(sSelectSecurityDb);
			HashMap<Integer, Map<String, Object>>  mpSecurityDb = m_dbConnRoot.Select(sSelectSecurityDb);
			//System.out.print("mpSecurityDb1 " + mpSecurityDb);
			String SocietydbName= "";
			String SecuritydbName= "";
			for(Entry<Integer, Map<String, Object>> entry : mpSecurityDb.entrySet()) 
			{
				/************* check if bill is opening bill ************/
				if(entry.getValue().get("security_dbname") != null )
				{
					SocietydbName =entry.getValue().get("dbname").toString();
					SecuritydbName =entry.getValue().get("security_dbname").toString();
					//System.out.println("security Database" +SecuritydbName);
					System.out.println("societyDatabase Database" +SocietydbName);
				}
			}
		//	System.out.print("Test2");
			
			DbOperations m_sm_dbConnRoot = new DbOperations(false, "security_rootdb");
			String sqlForVisitorDetails = "Select CONCAT(v.Lname,' ',v.Fname) as name,v.mobile,v.Company,v.Doc_img,v.Doc_No,d.document from visitors as v, documents as d where v.visitor_id = '"+ivisitorId+"' and v.Doc_Type_Id = d.id and v.status = 'Y' and d.status = 'Y'";
			System.out.print("sqlForVisitorDetails" + sqlForVisitorDetails);
			HashMap<Integer, Map<String, Object>>  mpVisitorDetails = m_sm_dbConnRoot.Select(sqlForVisitorDetails);
			System.out.println("mpVisitorDetails:"+mpVisitorDetails);
			DbOperations m_sm_dbConn = new DbOperations(false,SecuritydbName);
			String sqlForVisitorPurpose = "Select p.purpose_name from visitorentry as v, purpose as p where v.`visitor_ID` = '"+ivisitorId+"' and v.`purpose_id` = p.`purpose_id`";
			HashMap<Integer, Map<String, Object>>  mpPurposeDetails = m_sm_dbConn.Select(sqlForVisitorPurpose);
			System.out.println("\n\nmpPurposeDetails:"+mpPurposeDetails);
			String purposeName[] = new String[mpPurposeDetails.size()];
			int i = 0;
			for(Entry<Integer, Map<String, Object>> entry1 : mpVisitorDetails.entrySet()) 
			{
				if(mpPurposeDetails.size() > 0)
				{
					for(Entry<Integer, Map<String, Object>> entry : mpPurposeDetails.entrySet()) 
					{
						purposeName[i] = entry.getValue().get("purpose_name").toString();
						i++;
					}
					entry1.getValue().put("PurposeDetails", purposeName);
				}
				else
				{
					entry1.getValue().put("PurposeDetails", "");
				}
			}
			if(mpVisitorDetails.size() > 0)
			{
				rows2.put("VisitorDetails",MapUtility.HashMaptoList(mpVisitorDetails));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//member not found
				 rows2.put("VisitorDetails","");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rows;
	}
		
 public static boolean CheckIfMappingAlreadyExist()
    {
    	boolean bRetValue = false;
    	
    	
    	return bRetValue;
    }
    //if return value = -1 : Activation code is already in use
    public static HashMap<Integer, Map<String, Object>>  LinkLoginIDToActivationCode(int iLoginID, String sActivationCode)
    {
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();

		String sqlLogin = "Select * from login where login_id= '"+ iLoginID +"'";
		System.out.println("sqlLogin:"+ sqlLogin);
		HashMap<Integer, Map<String, Object>> LoginDetails = m_dbConnRoot.Select(sqlLogin);
		System.out.println("LoginDetails size:"+ LoginDetails.size());
		System.out.println("LoginDetails:"+ LoginDetails);
		
		if(LoginDetails.size() > 0)
		{
			for(Entry<Integer, Map<String, Object>> entry1 : LoginDetails.entrySet()) 
			{
				int iExistingLoginID = Integer.parseInt(entry1.getValue().get("login_id").toString());
				String sMember_id = entry1.getValue().get("member_id").toString();
				System.out.println("iExistingLoginID :"+ iExistingLoginID);
				System.out.println("sMember_id : "+ sMember_id);
				return LinkLoginIDToActivationCode(sMember_id, iLoginID, sActivationCode);
			}
		}
		//Activation code disabled;
		 rows2.put("message","iLoginID '"+ iLoginID +"' doesn't exist");
		 rows.put("success",0);
		 rows.put("response",rows2);			 					
		
        return rows;
    	
    }
    
    public static String getRandomNumber(int iDigits)
    {
    	return "k83k";
    }
    public static String CreateNewActivationCode(int iSocietyID, String sUnitNo, int iUnit_id, int iOther_Member_id, String mailToEmail)
    {
    	String sRandomNumber = getRandomNumber(4);
    	String sCode = mailToEmail + sRandomNumber;

    	//String sUnitNo = "";
    	String codeType = "1";  
    	int iLogin_id = 0;
    	int iRole = 1;//ROLE_MEMBER
    	String insert_mapping = "INSERT INTO `mapping`(`society_id`, `unit_id`, `desc`, `code`,`code_type`, `role`, `created_by`, `view`,`status`) VALUES ('" + iSocietyID + "', '" + iUnit_id + "', '" + sUnitNo + "', '" + sCode + "','" + codeType +  "', '" + iRole + "', '" + iLogin_id + "', 'MEMBER','1')";
    	long iInsertResult = m_dbConnRoot.Insert(insert_mapping);
    	
    	return sCode ;
    	
    }
    public static HashMap<Integer, Map<String, Object>>  SendActivationCode_2(int iSocietyID, int iUnit_id, int iOther_Member_id)
    {
    	
    	String mailToEmail = "";
    	String mailToName  = "";
    	String sOther_email  = "";
    	String sOther_name  = "";
    	String sUnitNo  = "";
    	//String sSelectSQL = "SELECT mm.unit, mof.* FROM `mem_other_family` as mof JOIN member_main as mm on mof.member_id = mm.member_id where mof.mem_other_family_id = " + iOther_Member_id + " and mm.unit = " + iUnit_id;
    	String sSelectSQL = "SELECT un.unit_no, mm.unit, mof.* FROM `mem_other_family` as mof JOIN member_main as mm on mof.member_id = mm.member_id join unit as un on un.unit_id = mm.unit where mof.mem_other_family_id = " + iOther_Member_id + " and mm.unit = " + iUnit_id;
    	HashMap<Integer, Map<String, Object>> Member_other_family = m_dbConn.Select(sSelectSQL);
		System.out.println("Member_other_family size:"+ Member_other_family.size());

		if(Member_other_family.size() == 1)
		{
			for(Entry<Integer, Map<String, Object>> entry1 : Member_other_family.entrySet()) 
			{
				int iUnitID = Integer.parseInt(entry1.getValue().get("unit").toString());
				int iStatus = Integer.parseInt(entry1.getValue().get("mem_other_family_id").toString());
				sOther_name = entry1.getValue().get("other_name").toString();
				sOther_email = entry1.getValue().get("other_email").toString();
				sUnitNo = entry1.getValue().get("unit_no").toString();

				System.out.println("iUnitID :"+ iUnitID);
				System.out.println("sOther_name : "+ sOther_name);
				System.out.println("sOther_email : "+ sOther_email);
				System.out.println("sUnitNo : "+ sUnitNo);
			}
		}
    	
    	return SendActivationCode(iSocietyID, sUnitNo, iUnit_id, iOther_Member_id, sOther_email, sOther_name);
    }
    public static HashMap<Integer, Map<String, Object>>  SendActivationCode(int iSocietyID, String sUnitNo, int iUnit_id, int iOther_Member_id, String mailToEmail, String mailToName)
    {
    	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			//from iOther_Member_id get member details
			
	    	long lRetValue = 0;
	    	String sCode = "";
	    	boolean bLoginExist = false;
			String sqlActivationCode = "Select * from mapping where society_id = '"+ iSocietyID +"' and unit_id = '"+ iUnit_id +"' and code like '"+ mailToEmail +"%'";
			System.out.println("sqlActivationCode:"+ sqlActivationCode);
			HashMap<Integer, Map<String, Object>> ActivationDetails = m_dbConnRoot.Select(sqlActivationCode);
			System.out.println("ActivationDetails size:"+ ActivationDetails.size());
			System.out.println("ActivationDetails:"+ ActivationDetails);

			boolean bSendActivacationCode = false;
			if(ActivationDetails.size() == 0)
			{
				//generate new code 
				System.out.println("Code not found. Creating new code:");
				sCode = CreateNewActivationCode(iSocietyID, sUnitNo, iUnit_id, iOther_Member_id, mailToEmail);
				bSendActivacationCode = true;
			}
			else if(ActivationDetails.size() == 1)
			{
				for(Entry<Integer, Map<String, Object>> entry1 : ActivationDetails.entrySet()) 
				{
					int iExistingLoginID = Integer.parseInt(entry1.getValue().get("login_id").toString());
					int iStatus = Integer.parseInt(entry1.getValue().get("status").toString());
					sCode = entry1.getValue().get("code").toString();
					System.out.println("iExistingLoginID :"+ iExistingLoginID);
					System.out.println("iStatus : "+ iStatus);
					System.out.println("sCode : "+ sCode);

					if(iStatus == 1)
					{
						//email activation code
						
						if(iExistingLoginID == 0)
						{
							System.out.println("emailing Activation code :"+ sCode);
							//email code to user
							bSendActivacationCode = true;
							//emailCodeToUser(sEmail, sCode)''
						}
						else
						{
							 rows2.put("message","Mapping Status = 1 but Activation code '"+ sCode +"' already linked to "  + iExistingLoginID);
							 rows.put("success",0);
							 rows.put("response",rows2);			 					
							
						}
					}
					else if(iStatus == 2)
					{
						//Activation code is already in use;
						 rows2.put("message","Activation code '"+ sCode +"' already linked to UserID " + iExistingLoginID);
						 rows.put("success",0);
						 rows.put("response",rows2);			 					
					}
					else if(iStatus == 3)
					{
						//Activation code disabled;
						 rows2.put("message","Activation code '"+ sCode +"' disabled");
						 rows.put("success",0);
						 rows.put("response",rows2);			 					
					}
//					if(iStatus == 0)
					else
					{
						//Activation code disabled;
						 rows2.put("message","Activation code '"+ sCode +"' invalid status");
						 rows.put("success",0);
						 rows.put("response",rows2);			 					
					}
					break;
				}
			}
			else
			{
				 rows2.put("message","Error: Multiple Activation code '"+ "Test" +"' exist");
				 rows.put("success",0);
				 rows.put("response",rows2);			 
			}
			if(bSendActivacationCode)
			{
				System.out.println("Sending Emails");
				String EmailStatus = EmailUtility.sendActivationEmail(mailToEmail, mailToName, bLoginExist, sCode);	
				//System.out.println("Sending Emails"+EmailStatus);
				 if(EmailStatus.equals("1"))
				 {
					 rows2.put("message","Activation code '"+ sCode);
					 rows.put("response",rows2);
				 }
				 else
				 {
					// rows2.put("message","Activation code '"+ sCode);
					// rows.put("response",rows2);
				 }
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//member not found
			 rows2.put("message",e.getMessage());
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
		//System.out.println("Response :"+rows);
		return rows;
    }
    //Added New Condition
    public static HashMap<Integer, Map<String, Object>> SendBillEmail(int iSocietyid, int iUnit_id, int iPeriodID,int iBillType, String BillFor) throws AddressException, MessagingException 
	{
	
    	String mEmail="";
		String sSql = "SELECT member_id, unit, email,alt_email FROM member_main where unit='"+iUnit_id+"' ";
		HashMap<Integer, Map<String, Object>> MemberEmail = m_dbConn.Select(sSql);
		System.out.println(sSql);
		
		for(Entry<Integer, Map<String, Object>> entry : MemberEmail.entrySet()) 
	 	{
			 mEmail = entry.getValue().get("email").toString();
			
		}
		String EmailStatus = EmailUtility.sendBillEmail(mEmail, iUnit_id, iPeriodID, iBillType,BillFor);	
		return MemberEmail; 
		
	}
    
    public static HashMap<Integer, Map<String, Object>>  LinkLoginIDToActivationCode(String sUserid, int iLoginID, String sActivationCode)
    {
    	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
	    	long lRetValue = 0;
	    	String sFullActivationCode = ""; 
			System.out.println("sqlActivationCode 1:"+ sActivationCode);
	    	if(sActivationCode.length() == 4)
	    	{
	    		//Mobile Activation code = email + 4digit code
	    		sFullActivationCode = sUserid + sActivationCode;
				System.out.println("sqlActivationCode 2:"+ sFullActivationCode);
	    	}
	    	else
	    	{
	    		//Web activation code
	    		sFullActivationCode = sActivationCode;
				System.out.println("sqlActivationCode 3:"+ sFullActivationCode);
	    	}
	
			String sqlActivationCode = "Select * from mapping where code = '"+ sFullActivationCode +"'";
			System.out.println("sqlActivationCode:"+ sqlActivationCode);
			HashMap<Integer, Map<String, Object>> ActivationDetails = m_dbConnRoot.Select(sqlActivationCode);
			//System.out.println("ActivationDetails size:"+ ActivationDetails.size());
			//System.out.println("ActivationDetails:"+ ActivationDetails);
			
			if(ActivationDetails.size() == 0)
			{
				 rows2.put("message","Activation code '"+ sFullActivationCode +"' doesn't exist");
				 rows.put("success",0);
				 rows.put("response",rows2);			 
			}
			else if(ActivationDetails.size() == 1)
			{
				//System.out.println("inside else if");
				for(Entry<Integer, Map<String, Object>> entry1 : ActivationDetails.entrySet()) 
				{
					//System.out.println("inside for");
					int iExistingLoginID = Integer.parseInt(entry1.getValue().get("login_id").toString());
					System.out.println("iExistingLoginID :"+ iExistingLoginID);
					String Code = entry1.getValue().get("code").toString();
					System.out.println("code  :"+ Code);
					int iStatus = Integer.parseInt(entry1.getValue().get("status").toString());
					
					System.out.println("iStatus : "+ iStatus);

					if(iStatus == 1)
					{
						if(iExistingLoginID == 0)
						{
							//Pending setting up profile
							String sqlMapping = "UPDATE mapping set login_id = " + iLoginID + ", status = 2 where code = '"+ sFullActivationCode +"' and login_id = 0";
							System.out.println("sqlMapping:"+ sqlMapping);
							lRetValue = m_dbConnRoot.Update(sqlMapping);
							if(lRetValue > 0)
							{
								 rows2.put("message","Activation code '"+ sActivationCode +"' linked to "  + sUserid);
								 rows.put("success",1);
								 rows.put("response",rows2);			 					
							}
							else
							{
								 rows2.put("message","Activation code '"+ sActivationCode +"' failed to link to "  + sUserid);
								 rows.put("success",0);
								 rows.put("response",rows2);			 					
								
							}
						}
						else
						{
							 rows2.put("message","Mapping Status = 1 but Activation code '"+ sActivationCode +"' already linked to "  + iExistingLoginID);
							 rows.put("success",0);
							 rows.put("response",rows2);			 					
							
						}
					}
					else if(iStatus == 2)
					{
						//Activation code is already in use;
						 rows2.put("message","Activation code '"+ sFullActivationCode +"' already linked to UserID " + iExistingLoginID);
						 rows.put("success",0);
						 rows.put("response",rows2);			 					
					}
					else if(iStatus == 3)
					{
						//Activation code disabled;
						 rows2.put("message","Activation code '"+ sFullActivationCode +"' disabled");
						 rows.put("success",0);
						 rows.put("response",rows2);			 					
					}
//					if(iStatus == 0)
					else
					{
						//Activation code disabled;
						 rows2.put("message","Activation code '"+ sFullActivationCode +"' invalid status");
						 rows.put("success",0);
						 rows.put("response",rows2);			 					
					}
					break;
				}
			}
			else
			{
				 rows2.put("message","Error: Multiple Activation code '"+ sFullActivationCode +"' exist");
				 rows.put("success",0);
				 rows.put("response",rows2);			 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//member not found
			 rows2.put("message",e.getMessage());
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
		return rows;
    }
		

public static HashMap<Integer, Map<String, Object>>  getPropertyType()
{
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();

	String sqlProperty = "SELECT * FROM `unit_type`";
	HashMap<Integer, Map<String, Object>> PropertyType = m_dbConn.Select(sqlProperty);
    System.out.println("PropertyType"+PropertyType);		
    return PropertyType;
	
}
public static HashMap<Integer, Map<String, Object>>  getAgreementText()
{
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();

	String sqlSelect = "SELECT * FROM `document_templates` where id='55'";
	HashMap<Integer, Map<String, Object>> PropertyAgreementText = m_dbConnRoot.Select(sqlSelect);
    System.out.println("PropertyType"+PropertyAgreementText);		
    return PropertyAgreementText;
	
}

public static HashMap<Integer, Map<String, Object>>  getSubscription(int iSociectyId)
{
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();

	String sqlSelect = "SELECT `software_subscription`,`subscription_alert_notes`,DATE_FORMAT(subscription_alert_date,'%d-%m-%Y') as subscription_alert_date FROM `society` where society_id='"+iSociectyId+"'";
	HashMap<Integer, Map<String, Object>> SocietySubscription = m_dbConnRoot.Select(sqlSelect);
   // System.out.println("PropertyType"+PropertyAgreementText);		
    return SocietySubscription;
	
}
public static HashMap<Integer, Map<String, Object>>  getNewServices()
{
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();

	String sqlSelect = "SELECT * FROM `mobileservices`";
	HashMap<Integer, Map<String, Object>> MobileServices = m_dbConnRoot.Select(sqlSelect);
    //System.out.println("PropertyType"+PropertyAgreementText);		
    return MobileServices;
	
}

public  HashMap<Integer, Map<String, Object>> fetchVisitorsReports(int iSocietyID,int visitorID,int unitID) throws ClassNotFoundException, ParseException
{
	DbOperations dbop;
	
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();
	String sqlflag="";
	String sSelectQuery = "",sql1="";
	JsonObject jsonOption = new JsonObject();
	HashMap<Integer, Map<String, Object>> visitorReportDetails = new HashMap<Integer, Map<String, Object>>();
	HashMap<Integer, Map<String, Object>> visitorDetails = new HashMap<Integer, Map<String, Object>>();
	String sSecurityDB = getDBNames(iSocietyID);
	System.out.println("sSecurityDB "+ sSecurityDB);
	if(sSecurityDB != "")
	{
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	//DbOperations dbop_security = new DbOperations(false,"sm_1");
	System.out.println("Database Security :" +dbop_security.toString());
	//String sSqlDirectory = "SELECT v.id,v.visitor_ID,v.visitorMobile,v.status,va.Entry_flag,p.`purpose_name`,`outTimeStamp`,`otpGtimestamp`,`Entry_Gate`,`Exit_Gate` FROM `visitorentry` as v inner join visit_approval as va on v.id=va.v_id inner join  purpose as p on v.`purpose_id` = p.`purpose_id` and  va.unit_id='"+unitID+"' and v.visitor_ID='"+visitorID+"'";
	String sSqlDirectory = "SELECT v.id,v.visitor_ID,v.visitorMobile,v.status,va.Entry_flag,p.`purpose_name`,v.`outTimeStamp`,v.`otpGtimestamp`,v.`Entry_Gate`,v.`Exit_Gate` FROM `visitorentry` as v inner join visit_approval as va on v.id=va.v_id inner join  purpose as p on v.`purpose_id` = p.`purpose_id` and  va.unit_id='"+unitID+"' and v.visitor_ID='"+visitorID+"'";
	System.out.println("Visitor report : "+sSqlDirectory);
	visitorReportDetails = dbop_security.Select(sSqlDirectory);
	String MonthDate ="";
	String OutMonthDate ="";
	String dateOnly="",timeOnly="";;
	for(Entry<Integer, Map<String, Object>> entry : visitorReportDetails.entrySet()) 
	{	
		if(entry.getValue().get("otpGtimestamp") != null)
		{
		MonthDate =entry.getValue().get("otpGtimestamp").toString();
		
		String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
		String fmt = "yyyy-MM-dd HH:mm a";
		DateFormat df = new SimpleDateFormat(fmt);
		Date dt = df.parse(Time);
		DateFormat tdf = new SimpleDateFormat("hh:mm a");
		DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
		timeOnly = tdf.format(dt);
		dateOnly = dfmt.format(dt);
		entry.getValue().put("InTime", timeOnly);
		entry.getValue().put("InDate", dateOnly);
		}
		if(entry.getValue().get("outTimeStamp") != null)
		{

		OutMonthDate =entry.getValue().get("outTimeStamp").toString();
		String OutTime = m_Timezone.convertToCurrentTimeZone(OutMonthDate);
		String fmt = "yyyy-MM-dd HH:mm a";
		DateFormat df = new SimpleDateFormat(fmt);
		Date dt = df.parse(OutTime);
		DateFormat tdf = new SimpleDateFormat("hh:mm a");
		DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
		String OuttimeOnly = tdf.format(dt);
		String OutdateOnly = dfmt.format(dt);
		entry.getValue().put("OutTime", OuttimeOnly);
		entry.getValue().put("OutDate", OutdateOnly);
 		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date d1 = null;
	    Date d2 = null;
	    try
	    {
	        d1 = format.parse(entry.getValue().get("otpGtimestamp").toString());
	        d2 = format.parse(entry.getValue().get("outTimeStamp").toString());
	        
			
	    } 
	    catch (ParseException e) 
	    {
	        e.printStackTrace();
	    }
	    long diff = d2.getTime() - d1.getTime();
	    long diffMinutes = diff / (60 * 1000) % 60;
	    long diffHours = diff / (60 * 60 * 1000);
	    String diffStr = diffHours+":"+diffMinutes;
	    entry.getValue().put("TimeDiff",diffStr);
		
		
	}
	else
	{
		String OuttimeOnly = "00:00";
		String OutdateOnly = "00-00-00";
		entry.getValue().put("OutTime", OuttimeOnly);
		entry.getValue().put("OutDate", OutdateOnly);
 		String diffStr = "00";
 		entry.getValue().put("TimeDiff",diffStr);
 	}	
	}
			return visitorReportDetails;
	}
	else
	{
		HashMap  mpVisitors = new HashMap<>();
		mpVisitors.put("Visitor", "No Database available");
		return mpVisitors;
	}
}
public  HashMap<Integer, Map<String, Object>> fetchPurpose(int iSocietyID,int unitID) throws ClassNotFoundException, ParseException
{
	DbOperations dbop;
	
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();
	String sqlflag="";
	String sSelectQuery = "",sql1="";
	
	HashMap<Integer, Map<String, Object>> PurposeDetails = new HashMap<Integer, Map<String, Object>>();
	String sSecurityDB = getDBNames(iSocietyID);
	System.out.println("sSecurityDB "+ sSecurityDB);
	if(sSecurityDB != "")
	{
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	System.out.println("Database Security :" +dbop_security.toString());
	String sSqlDirectory = "SELECT * FROM `purpose`";
	System.out.println("Visitor report : "+sSqlDirectory);
	PurposeDetails = dbop_security.Select(sSqlDirectory);
	return PurposeDetails;
	}
	else
	{
		HashMap  mpVisitors = new HashMap<>();
		mpVisitors.put("purpose", "No Database available");
		return mpVisitors;
	}
}

public  HashMap<Integer, Map<String, Object>> fetchApprovalMsg(int iSocietyID) throws ClassNotFoundException, ParseException
{
	DbOperations dbop;
	
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();
	String sqlflag="";
	String sSelectQuery = "",sql1="";
	
	HashMap<Integer, Map<String, Object>> ApprovalMsgDetails = new HashMap<Integer, Map<String, Object>>();
	String sSecurityDB = getDBNames(iSocietyID);
	System.out.println("sSecurityDB "+ sSecurityDB);
	if(sSecurityDB != "")
	{
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	System.out.println("Database Security :" +dbop_security.toString());
	String sSqlDirectory = "SELECT * FROM `approvemsg`";
	System.out.println("Visitor report : "+sSqlDirectory);
	ApprovalMsgDetails = dbop_security.Select(sSqlDirectory);
	return ApprovalMsgDetails;
	}
	else
	{
		HashMap  ApprovalMsgDetails1 = new HashMap<>();
		ApprovalMsgDetails1.put("ApprovalMsgDetails", "No Database available");
		return ApprovalMsgDetails1;
	}
}


public  long pinsertapproval(int iSocietyID,int iUnitId,String l_id,String l_name,String visitorid,String approvalstatus,String note) throws ClassNotFoundException, ParseException
{
	DbOperations dbop;
	
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();
	String sqlflag="";
	String sSelectQuery = "",sql1="";
	
	String sSecurityDB = getDBNames(iSocietyID);
	System.out.println("sSecurityDB "+ sSecurityDB);
	if(sSecurityDB != "")
	{
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	System.out.println("Database Security :" +dbop_security.toString());
	String sSqlDirectory = "Update `visit_approval` set Entry_flag='"+approvalstatus+"',login_id = '"+l_id+"',login_name = '"+l_name+"', approvemsg = '"+note+"' , approvewith = '"+1+"' where v_id = '"+ visitorid + "' and unit_id='"+iUnitId+"'";
	System.out.println("Query : " + sSqlDirectory);
	long mpinsert =  dbop_security.Update(sSqlDirectory);
	
	return mpinsert;

	}
	else
	{
		return 0;
	}
}


public  String minsertVisitorItem(int iSocietyID,int iUnitId,String l_id,String l_name,String checkoutnote,String id,String imagestring) throws ClassNotFoundException, ParseException
{
	DbOperations dbop;
	
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();
	String sqlflag="";
	String unitnumber = "";
	String sSelectQuery = "",sql1="";
	String query = "Select CONCAT(u.unit_no, '(', w.wing, ')') as unitNo , mm.`primary_owner_name`,w.wing from unit u, member_main mm,wing w where u.`unit_id` ='"+iUnitId+"' and u.unit_id = mm.`unit` and w.wing_id  = u.wing_id  and mm.`status` = 'Y' and mm.`ownership_status` = '1'";
	HashMap<Integer, Map<String, Object>> unitDetails = m_dbConn.Select(query);
	System.out.println("query : " + query);
	System.out.println("unitDetails : " + unitDetails.size());
	if(unitDetails.size() <=  0)
	{
		System.out.println("test1");
		unitnumber = "0";
	}
	else
	{
		System.out.println("test2");	
		for(Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet())
		{
			unitnumber = entry1.getValue().get("unitNo").toString();
		}
	}
	System.out.println("testing");
	String sSecurityDB = getDBNames(iSocietyID);
	System.out.println("sSecurityDB "+ sSecurityDB);
	System.out.println("unitnumber "+ unitnumber);
	if(sSecurityDB != "")
	{
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	System.out.println("Database Security :" +dbop_security.toString());
	String sSqlDirectory = "INSERT INTO `item_lended`(`entry_id`, `unit_id`, `member_name`, `unit_no`, `item_desc`, `entry_for`) VALUES ";
	sSqlDirectory += "('"+id+"','"+iUnitId+"','"+l_name+"','"+unitnumber+"','"+checkoutnote+"','0')";
	System.out.println("Query : " + sSqlDirectory);
	long mpinsert =  dbop_security.Insert(sSqlDirectory);
	
	return mpinsert + "/" + sSecurityDB;

	}
	else
	{
		return "";
	}
}



public static HashMap<Integer, Map<String, Object>> fechvisitoritem(int VisitorID, int iSocietyID) throws ClassNotFoundException
{
	String sSecurityDB = getDBNames(iSocietyID);
	HashMap<Integer, Map<String, Object>>  visitorEntryDetails = new HashMap<Integer, Map<String, Object>>();
	
	String sql1="";
	
	System.out.println("sSecurityDB "+ sSecurityDB);
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	System.out.println("Database Security :" +dbop_security.toString());
	//sql1="Select id,verified,visitor_ID,visitorMobile,unit_id,purpose_id,vehicle,otpGenerated,otpGtimestamp,otpStatus,status,outTimeStamp from visitorentry as v join `purpose` as p.purpose_id=v.purpose_id where id='"+VisitorID+"'";
	sql1="Select * from item_lended  where entry_id='"+VisitorID+"'";
	System.out.println("SM Table query : "+sql1);
	visitorEntryDetails = dbop_security.Select(sql1);
	return  visitorEntryDetails;
	
}


public static long mfetchstaffattend(int iSocietyID,String id) throws ClassNotFoundException
{
	String sSecurityDB = getDBNames(iSocietyID);
	HashMap<Integer, Map<String, Object>>  staffid = new HashMap<Integer, Map<String, Object>>();
	
	String sql1="";
	
	System.out.println("sSecurityDB "+ sSecurityDB);
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	System.out.println("Database Security :" +dbop_security.toString());
	//sql1="Select id,verified,visitor_ID,visitorMobile,unit_id,purpose_id,vehicle,otpGenerated,otpGtimestamp,otpStatus,status,outTimeStamp from visitorentry as v join `purpose` as p.purpose_id=v.purpose_id where id='"+VisitorID+"'";
	sql1="SELECT sr_no FROM `staffattendance` where staff_id = '"+id+"' and status = 'inside'";
	System.out.println("SM Table query : "+sql1);
	staffid = dbop_security.Select(sql1);
	long staffid1 = 0;
	if(staffid.size() > 0)
	{
	staffid1 = Long.valueOf(staffid.get(0).get("sr_no").toString());
	}
	
	return  staffid1;
	
}


public  String minsertstaffItem(int iSocietyID,int iUnitId,String l_id,String l_name,String checkoutnote,String id,String fetchstaffid,String dbname1) throws ClassNotFoundException, ParseException
{
	DbOperations dbop_data = new DbOperations(false,dbname1);
	
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();
	String sqlflag="";
	String unitnumber = "";
	String sSelectQuery = "",sql1="";
	String query = "Select CONCAT(u.unit_no, '(', w.wing, ')') as unitNo , mm.`primary_owner_name`,w.wing from unit u, member_main mm,wing w where u.`unit_id` ='"+iUnitId+"' and u.unit_id = mm.`unit` and w.wing_id  = u.wing_id  and mm.`status` = 'Y' and mm.`ownership_status` = '1'";
	HashMap<Integer, Map<String, Object>> unitDetails = dbop_data.Select(query);
	System.out.println("query : " + query);
	System.out.println("unitDetails : " + unitDetails.size());
	if(unitDetails.size() <= 0)
	{
	unitnumber = "0";	
	}
	else
	{
		
		for(Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet())
		{
			unitnumber = entry1.getValue().get("unitNo").toString();
		}
	}
	System.out.println("unitnumber : " + unitnumber);
	String sSecurityDB = getDBNames(iSocietyID);
	System.out.println("sSecurityDB "+ sSecurityDB);
	if(sSecurityDB != "")
	{
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	System.out.println("Database Security :" +dbop_security.toString());
	String sSqlDirectory = "INSERT INTO `item_lended`(`entry_id`, `unit_id`, `member_name`, `unit_no`, `item_desc`, `entry_for`,`staff_id`) VALUES ";
	sSqlDirectory += "('"+fetchstaffid+"','"+iUnitId+"','"+l_name+"','"+unitnumber+"','"+checkoutnote+"','1','"+id+"')";
	System.out.println("Query : " + sSqlDirectory);
	long mpinsert =  dbop_security.Insert(sSqlDirectory);
	
	return mpinsert + "/" + sSecurityDB;

	}
	else
	{
		return "";
	}
}


public static long markentry(int iSocietyID,String id,String jobprofile) throws ClassNotFoundException
{
	String sSecurityDB = getDBNames(iSocietyID);
	HashMap<Integer, Map<String, Object>>  staffid = new HashMap<Integer, Map<String, Object>>();
	
	String sql1="";
	
	System.out.println("sSecurityDB "+ sSecurityDB);
	DbOperations dbop_security = new DbOperations(false,sSecurityDB);
	System.out.println("Database Security :" +dbop_security.toString());
	//sql1="Select id,verified,visitor_ID,visitorMobile,unit_id,purpose_id,vehicle,otpGenerated,otpGtimestamp,otpStatus,status,outTimeStamp from visitorentry as v join `purpose` as p.purpose_id=v.purpose_id where id='"+VisitorID+"'";
	String sInsertQuery = "Insert into staffattendance (staff_id,attendance,status,entry_profile,Entry_Gate,staff_note,staff_entry_with) values('"+id+"', 'p','inside','"+jobprofile+"','0','','2')";
	long attend = dbop_security.Insert(sInsertQuery);
	DbOperations dbop = new DbOperations(false,"hostmjbt_societydb");
	String sUpdateQuery = "Update `service_prd_reg` set security_status='1' Where service_prd_reg_id = '"+id+"'";
	//System.out.println(sUpdateQuery);
	long insideEntry = dbop.Update(sUpdateQuery);
	
	if(attend > 0)
	{
		return attend;
	}
	else
	{
	return  0;
	}
	
}


}