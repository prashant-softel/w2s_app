package MainClasses;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import MainClasses.AndroidPush;
import MainClasses.EmailUtility;
import CommonUtilityFunctions.MapUtility;
import MainClasses.DbOperations.*;
import java.lang.NullPointerException;
public class Fine{
	
	static DbOperations m_dbConn;
	static DbOperations m_dbConnRoot;
	static Utility m_Utility;
	//static Fine  m_Fine;
	public Fine(DbOperations  m_dbConnObj) throws ClassNotFoundException
	{
		m_dbConn = m_dbConnObj;
		//System.out.println(m_dbConn);
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		
	}	  	
	public static HashMap<Integer, Map<String, Object>> mfetchBillPeriod()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		boolean bFound = false;
		String Type="";
	    String PeriodDate="";
	    String PrevPeriodID="";
	    String PrevPeriodDate="";
	    String PeriodID= "";
		
		try
		{
		String sSelectPeriod = "";
		sSelectPeriod = "SELECT periodtbl.ID,periodtbl.Type,periodtbl.BeginingDate,periodtbl.EndingDate,periodtbl.PrevPeriodID, yeartbl.YearDescription,yeartbl.YearID FROM year as yeartbl JOIN period as periodtbl on periodtbl.YearID = yeartbl.YearID JOIN society as societytbl on periodtbl.PrevPeriodID =societytbl.M_PeriodID";

		HashMap<Integer, Map<String, Object>> resultYearID =  m_dbConn.Select(sSelectPeriod);
		//System.out.println("qry result:"+resultYearID);
		for(Entry<Integer, Map<String, Object>> entry : resultYearID.entrySet()) 
		{
			if(entry.getValue().get("ID") != null)
			{
				PeriodID =entry.getValue().get("ID").toString();
			}
		}
		//System.out.println("period:"+PeriodID);
		if(resultYearID.size() > 0 && PeriodID != null)
		{
		    for(Entry<Integer, Map<String, Object>> entry : resultYearID.entrySet()) 
			{
		    	if(entry.getValue().get("ID") != null)
				{
		    		PeriodID =entry.getValue().get("ID").toString();
		    		PrevPeriodID =entry.getValue().get("PrevPeriodID").toString();
		    		Type=entry.getValue().get("Type").toString();
		    		PeriodDate=entry.getValue().get("BeginingDate").toString();
		    		//PrevPeriodDate=entry.getValue().get("BeginingDate").toString();
		    		
		    		String sSelectPrePeriod = "SELECT periodtbl.ID,periodtbl.Type,periodtbl.BeginingDate,periodtbl.EndingDate from period as periodtbl where ID='"+ PrevPeriodID +"'";

		    		HashMap<Integer, Map<String, Object>> resultPrevID =  m_dbConn.Select(sSelectPrePeriod);
		    		//System.out.println("qry result:"+resultPrevID);
		    		for(Entry<Integer, Map<String, Object>> entry1 : resultPrevID.entrySet()) 
		    		{
		    			if(entry1.getValue().get("ID") != null)
		    			{
		    				PrevPeriodDate=entry1.getValue().get("BeginingDate").toString();
		    			}
		    		}
		    		bFound = true;
		    		//System.out.println("Type:"+Type);
    				break; 
				}
		    	
		    	if(bFound)
		    	{
		    		break;
		    	}
		    }

	    	//System.out.println("Prev date:"+PrevPeriodDate);
	 		rows.put("PrevPeriodDate", PrevPeriodDate);
		    //System.out.println("PeriodID :"+PeriodID);
		    //rows.put("Selected Period", PeriodID);
		    //rows.put("Selected Period", MapUtility.HashMaptoList(PeriodID));
		 }
		if(!bFound)
	    {
	    	PeriodID = "-1";
	    	Type = "Period not available";
	    	PeriodDate="0000-00-00";
	    }
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Exception:"+e.getMessage());
			e.printStackTrace();
		}
	    rows.put("PeriodID", PeriodID);
	    rows.put("PrvePeriodID", PrevPeriodID);
	    rows.put("Type", Type);
	    rows.put("PeriodDate", PeriodDate);
		    
		//System.out.println(resultMemberMain);
		return rows;
	}

	public static HashMap<Integer, Map<String, Object>> mfetchFineLedger()
	{
		//HashMap rows = new HashMap<>();
		//HashMap rows2 = new HashMap<>();
		String sSelectLedger = "";
		sSelectLedger = "SELECT `appdefault`.APP_DEFAULT_IMPOSE_FINE as FineID,ledgertbl.ledger_name FROM `appdefault` join ledger as ledgertbl on appdefault.APP_DEFAULT_IMPOSE_FINE=ledgertbl.id ";
		
		HashMap<Integer, Map<String, Object>> resultFineLedger =  m_dbConn.Select(sSelectLedger);
		
		return resultFineLedger;
	}
	public static HashMap<Integer, Map<String, Object>> mfetchUnitDetais(int iSocietyID)
	{
		//HashMap rows = new HashMap<>();
		//HashMap rows2 = new HashMap<>();
		String sSelectLedger = "";
		
		
		sSelectLedger = "select u.unit_id, CONCAT(CONCAT(u.unit_no,' '), mm.owner_name) AS 'unit_name',u.unit_no,mm.owner_name, mm.email from unit AS u JOIN `member_main` AS mm ON u.unit_id = mm.unit where u.society_id = '"+iSocietyID+"' AND mm.ownership_status='1' ORDER BY u.sort_order ";
		
		HashMap<Integer, Map<String, Object>> resultUnitList =  m_dbConn.Select(sSelectLedger);
		
		return resultUnitList;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchImposeFineList(int iUnitId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sSelectFineList = "";
		int IMPOSE_FINE = 0;
		String BillDate ="";
		/******* fetching app defaults **********/
		String sqlDefaults = "SELECT * FROM `appdefault`";
		HashMap<Integer, Map<String, Object>> resultDefaults =  m_dbConn.Select(sqlDefaults);
		 boolean bFound = false;
		if(resultDefaults.size() > 0)
		{
			//Map.Entry<Integer, Map<String, Object>> entry = resultDefaults.entrySet().iterator().next();
			
			//IMPOSE_FINE = (Integer)  entry.getValue().get("APP_DEFAULT_IMPOSE_FINE");
			//System.out.println("inside:"+IMPOSE_FINE);
			//System.out.println("app default:"+resultDefaults);
			
			 for(Entry<Integer, Map<String, Object>> entry : resultDefaults.entrySet()) 
				{    // System.out.println("begai app default Value:"+entry.getValue().get("APP_DEFAULT_IMPOSE_FINE"));
				    	if(!entry.getValue().get("APP_DEFAULT_IMPOSE_FINE").toString().equals("0") && !entry.getValue().get("APP_DEFAULT_IMPOSE_FINE").toString().equals(""))
						{
				    		IMPOSE_FINE = (Integer)  entry.getValue().get("APP_DEFAULT_IMPOSE_FINE");
				    		//System.out.println("app default Value:"+IMPOSE_FINE);
				    		//IMPOSE_FINE =entry.getValue().get("APP_DEFAULT_IMPOSE_FINE").toString();
				    		bFound = true;
		    				break;   
						}
				    	
				    	 
				}
				 if(!bFound)
				    {
					 //int intArray[];    //declaring array
						//intArray = new int[-1]; 
					 
					 //IMPOSE_FINE = intArray[-1];
					 IMPOSE_FINE = -1;
				    	
				    }
			    	 
				 
		}
		//int intArray[];    //declaring array
		//intArray = new int[1];
		//intArray[0] = 1;
		//HashMap<Integer, Map<String, Object>> result = m_dbConn.Select("select " + IMPOSE_FINE);
		//rows.put("Fine", "{"+0+"}");
		rows.put("Fine", IMPOSE_FINE);
		if(bFound)
		{
		/******* fetching bill register details **********/
		String sqlBillDate="Select BeginingDate from period where PrevPeriodID = (Select Max(PeriodID)-1 from billregister)";
		//String sqlBillDate="Select BeginingDate from period where ID = (Select Max(PeriodID)-1 from billregister)";
		HashMap<Integer, Map<String, Object>> resultBillReg =  m_dbConn.Select(sqlBillDate);
		//System.out.println(resultBillReg);
		if(resultBillReg.size() > 0)
			
		{	
			Map.Entry<Integer, Map<String, Object>> entryBillDate = resultBillReg.entrySet().iterator().next();
			BillDate = entryBillDate.getValue().get("BeginingDate").toString();	
			//System.out.println(BillDate);
		}
		
		if(iUnitId == 0)
		{
			
			sSelectFineList = "select rec.ID,u.unit_type,u.unit_no,mm.owner_name,rec.`Date`,rec.Amount as amount,rec.Comments,rec.PeriodID,rec.LedgerID,rec.ReportedBy,rec.UnitID,DATE_FORMAT( CONVERT_TZ( rec.UpdateTime,  '+00:00',  '+0:00' ) , '%d %b, %Y %h:%i %p' ) AS UpdateTime,p.Type,p.EndingDate,p.BeginingDate,IF( rec.`Date`>= '"+BillDate+"' ,0,1) as IsPreviousFine from unit AS u JOIN `member_main` AS mm ON u.unit_id = mm.unit join `reversal_credits` as rec on mm.unit=rec.UnitID join `period` as p on p.`ID`=rec.`PeriodID` where rec.Status='1'and mm.ownership_status='1' ORDER BY DATE(rec.`Date`) DESC, u.sort_order";
		
		}
		else
		{
			
			sSelectFineList = "select rec.ID,u.unit_no,u.unit_type,mm.owner_name,rec.`Date`,rec.Amount as amount,rec.Comments,rec.PeriodID,rec.LedgerID,rec.ReportedBy,rec.UnitID,DATE_FORMAT( CONVERT_TZ( rec.UpdateTime,  '+00:00',  '+0:00' ) , '%d %b, %Y %h:%i %p' ) AS UpdateTime,p.Type,p.EndingDate,p.BeginingDate,IF( rec.`Date`>= '"+BillDate+"' ,0,1) as IsPreviousFine from unit AS u JOIN `member_main` AS mm ON u.unit_id = mm.unit join `reversal_credits` as rec on mm.unit=rec.UnitID join `period` as p on p.`ID`=rec.`PeriodID` where u.unit_id='"+iUnitId+"' and  rec.Status='1' and mm.ownership_status='1' ORDER BY DATE(rec.`Date`) DESC, u.sort_order";
		
		}
		//System.out.println("Fine List : "+sSelectFineList);
		rows =  m_dbConn.Select(sSelectFineList);
		}
		//System.out.println("Fine  : "+rows);
		return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> getVehicles(int iUnitId)
	{
		HashMap rows = new HashMap<>();

//		HashMap<Integer, Map<String, Object>> rows1 = null;
		
		String query = "";
//		int member_id = 0;
		
		query = "select mem_bike_parking.bike_reg_no from member_main JOIN mem_bike_parking ON member_main.member_id = mem_bike_parking.member_id WHERE unit = '" + iUnitId + "'";
		
		rows = m_dbConn.Select(query);
		
		//System.out.println("Rows: " + rows );
//		if(rows.size() > 0)
//		{	
//			rows1 = rows;
//		}
		
//		else 
//		{
//			query = "select mem_car_parking.car_reg_no from member_main JOIN mem_car_parking ON member_main.member_id = mem_car_parking.member_id WHERE unit = '" + iUnitId + "'";
//			
//			rows = m_dbConn.Select(query);
//			
//			System.out.println("Rowss2: " + rows);
//			
//			if(rows.size() > 0)
//			{	
//				rows1 = rows;
//			}
//		}
//		
//		System.out.println("Rows1: " + rows1);
//		
		return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchImposeHistory(int iRevID)
	{
		String sSelectFineList = "";
		// Server path
	sSelectFineList = "select u.unit_no,u.unit_type,mm.owner_name,mm.email,rev.UnitID,rev.PeriodID,rev.Date,rev.Amount,rev.Comments,rev.img_attachment,rev.ReportedBy,IF(rev.img_attachment <> '',concat('https://way2society.com/impose_Img/',rev.img_attachment),'') as Link,DATE_FORMAT( CONVERT_TZ( rev.UpdateTime,  '+00:00',  '+0:00' ) , '%d %b, %Y %h:%i %p' ) AS UpdateTime,p.Type,rev.ID,rev.LedgerID from unit AS u JOIN `member_main` AS mm ON u.unit_id = mm.unit join `reversal_credits` as rev on mm.unit=rev.UnitID join `period` as p on p.ID=rev.PeriodID where rev.ID='"+iRevID+"' and mm.ownership_status='1' ORDER BY u.sort_order";
		
		//localhost
		//sSelectFineList = "select u.unit_no,u.unit_type,mm.owner_name,mm.email,rev.UnitID,rev.PeriodID,rev.Date,rev.Amount,rev.Comments,rev.img_attachment,rev.ReportedBy,IF(rev.img_attachment <> '',concat('http://localhost/beta_aws_2/impose_Img/',rev.img_attachment),'') as Link,DATE_FORMAT( CONVERT_TZ( rev.UpdateTime,  '+00:00',  '+0:00' ) , '%d %b, %Y %h:%i %p' ) AS UpdateTime,p.Type,rev.ID,rev.LedgerID from unit AS u JOIN `member_main` AS mm ON u.unit_id = mm.unit join `reversal_credits` as rev on mm.unit=rev.UnitID join `period` as p on p.ID=rev.PeriodID where rev.ID='"+iRevID+"' and mm.ownership_status='1' ORDER BY u.sort_order";
		
		//System.out.println("Impose History : "+sSelectFineList);
		HashMap<Integer, Map<String, Object>> resultFineList =  m_dbConn.Select(sSelectFineList);
		
		return resultFineList;
	}
	public static long mSetAddImposeFine(int society_id,long lMappingID, String sReportedby,String sEmail,int ledger_id,int period_id,int unit_id,int amount,String desc,String img,String iPeriodDate,int isMail)
	{
	String sInsertQuery = "";
	String sSelectQuery="";
	Timestamp updateTimeStamp = new Timestamp(System.currentTimeMillis());
	
	try {
		//Get Max RequestNo
		//int id = GetNextid();
		 String sTitle="Add New Fine"  ;  
		 String sPageName= "ImposedetailPage";
		String FineDate= GetTodayDate();
		//System.out.println("Date :"+FineDate);
				
				sInsertQuery = "insert into reversal_credits (`Date`,`VoucherID`,`UnitID`,`Amount`,`LedgerID`,`Comments`,`BillType`,`ChargeType`,`ReportedBy`,`img_attachment`,`PeriodID`,`UpdateTime`) values ";
				sInsertQuery = sInsertQuery + "('" + iPeriodDate + "','0'," + unit_id + ",'" + amount + "','" + ledger_id + "','" + desc +  "','0','2','"+sReportedby+"','"+img+"','"+period_id+"','"+updateTimeStamp+"' )";
				//System.out.println("Reversal :"+sInsertQuery);	
				long RevesalId = m_dbConn.Insert(sInsertQuery);
				//System.out.println(RevesalId);	
				
				sSelectQuery="select * from `member_main` where society_id='"+society_id+"' and unit='"+unit_id+"' ";
				HashMap<Integer, Map<String, Object>> resultMember_list =  m_dbConn.Select(sSelectQuery);
				//System.out.println(resultMember_list);
				String sUserEmail="";
				if(resultMember_list.size() > 0)
				{
					Map.Entry<Integer, Map<String, Object>> entry = resultMember_list.entrySet().iterator().next();
					
					sUserEmail = entry.getValue().get("email").toString();	
					//System.out.println(sUserEmail);
				}
				if(!sUserEmail.equals(""))
				{
					//System.out.println("member email: "+sUserEmail);
					m_dbConnRoot = new DbOperations(true,"hostmjbt_societydb");
					
					sSelectQuery="SELECT d.login_id, l.login_id, l.member_id, d.device_id, m.id as 'map_id' from `device_details` as d JOIN `login` as l on l.login_id = d.login_id JOIN `mapping` as m on m.login_id = d.login_id WHERE m.unit_id = '" +unit_id+  "' AND m.society_id = '"+society_id+ "' AND l.member_id = '"+sUserEmail+ "' AND d.device_id <> '' ORDER BY d.id DESC LIMIT 0,1";
					HashMap<Integer, Map<String, Object>> selectLogin =  m_dbConnRoot.Select(sSelectQuery);
					//System.out.println(selectLogin);
					String sUserID="";
					String sMapID="";
					String sDevice="";
					if(selectLogin.size() > 0)
					{
						Map.Entry<Integer, Map<String, Object>> entry = selectLogin.entrySet().iterator().next();
						
						sUserID = entry.getValue().get("member_id").toString();	
						sMapID = entry.getValue().get("map_id").toString();
						sDevice = entry.getValue().get("device_id").toString();
						//sendPushNotification
						AndroidPush pushNotification = new AndroidPush();
						// public static String sendPushNotification(String sTitle, String sMessage, String sDeviceToken, String sMapID, String sPageRef, String sPageName, String sDetails) throws Exception {
						String sResponse = pushNotification.sendPushNotification(sTitle, desc, sDevice, sMapID, "4", sPageName, Long.toString(RevesalId));
		
					}
					
					// String host="smtp.gmail.com";
		    	    // String port="587";
		    	    // String userName="sujitkumar0304@gmail.com";
		    	    // String password="ayushprajapati";
		    	     //String toEmail="sujit10.arnav@gmail.com";
					if(isMail== 1)
					{
		    	   //  int Amount=200;
		    	     String message;
		     	   	String subject = "Notice Fine of Rs. "+amount+".00";
		     	   //message = "Fine for vahicle parking ";
		     	     EmailUtility obj = new EmailUtility();
		     	   	 String sResonseEmail= EmailUtility.sendEmail(sUserEmail, subject, amount ,desc);
					}
				}
		if(RevesalId  == 0)
		{
			return 0;
		}		
		return RevesalId;
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		System.out.println("exception in fine:"+e.getMessage());
		//m_dbConn.RollbackTransaction();
		}
	
	return 0;
}
	/*-------------------------------------------Update Fine Function -------------------------------------------*/
	
		
	public static HashMap<Integer, Map<String, Object>> mUpdateImposeFine(int iRevID,int iPeriodID, int iLedgerID,int iUnitID,int iAmount,String SComment,String sDate,int isMail)
	{
	String sSelectQuery = "";
	String updatedID="";
	String sSqlServey="";
	HashMap<Integer, Map<String, Object>> result = new HashMap<>();
	try {
	Timestamp updateTimeStamp = new Timestamp(System.currentTimeMillis());
	//sSqlServey="update reversal_credits set Status= '0', UpdateTime='"+updateTimeStamp+"' where id='"+ID+"' ";
	sSqlServey ="update reversal_credits set Amount='"+iAmount+"',Comments='"+SComment+"',UpdateTime='"+updateTimeStamp+"' where ID="+ iRevID+" ";
	
	long  UpdateId =  m_dbConn.Update(sSqlServey);
	//result.put(UpdateId);
	sSelectQuery="select * from `member_main` where unit='"+iUnitID+"' ";
	HashMap<Integer, Map<String, Object>> resultMember_list =  m_dbConn.Select(sSelectQuery);
	//System.out.println(resultMember_list);
	String sUserEmail="";
	if(resultMember_list.size() > 0)
	{
		Map.Entry<Integer, Map<String, Object>> entry = resultMember_list.entrySet().iterator().next();
		
		sUserEmail = entry.getValue().get("email").toString();	
		//System.out.println(sUserEmail);
	}
	if(isMail== 1)
	{
	   	String subject = "Notice: Update Fine of Rs. "+iAmount+".00";
	   	String message = SComment;
	   	int amount=iAmount;
	    // EmailUtility obj = new EmailUtility();
	   	 String sResonseEmail= EmailUtility.sendEmail(sUserEmail, subject, amount,message);
	}
	Map<String, Object> m1=new HashMap<>();
	m1.put("ID", Long.toString(UpdateId));
	
	result.put(1, m1);
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//m_dbConn.RollbackTransaction();
		}
	return  result ;
	}
	
	/*------------------------------------------------Delete Fine Function -------------------------------------*/
	public static HashMap<Integer, Map<String, Object>> mDeleteImposeFine(int ID,int iUnitId,int iAmount, String sDesc,int isMail)
	{
	String sSqlServey = "";
	String updatedID="";
	String sSelectQuery="";
	HashMap<Integer, Map<String, Object>> result = new HashMap<>();
	
	try
	{
	Timestamp updateTimeStamp = new Timestamp(System.currentTimeMillis());
	sSqlServey="update reversal_credits set Status= '0', UpdateTime='"+updateTimeStamp+"' where id='"+ID+"' ";
	long  UpdateId =  m_dbConn.Update(sSqlServey);
	//result.put(UpdateId);
	sSelectQuery="select * from `member_main` where unit='"+iUnitId+"' ";
	HashMap<Integer, Map<String, Object>> resultMember_list =  m_dbConn.Select(sSelectQuery);
	//System.out.println(resultMember_list);
	String sUserEmail="";
	if(resultMember_list.size() > 0)
	{
		Map.Entry<Integer, Map<String, Object>> entry = resultMember_list.entrySet().iterator().next();
		
		sUserEmail = entry.getValue().get("email").toString();	
		//System.out.println(sUserEmail);
	}
	if(isMail== 1)
	{
	   	String subject = "Notice: Delete Fine of Rs. "+iAmount+".00";
	   	String message = sDesc;
	   	int amount=iAmount;
	    // EmailUtility obj = new EmailUtility();
	   	 String sResonseEmail= EmailUtility.sendEmail(sUserEmail, subject, amount,message);
	}
	Map<String, Object> m1=new HashMap<>();
	m1.put("ID", Long.toString(UpdateId));
	result.put(1, m1);
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//m_dbConn.RollbackTransaction();
		}
	return  result ;
	}
	
	public static String GetTodayDate() throws ParseException
	{
		DateFormat output = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		return output.format(date);
		
	}


}
