package MainClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import CommonUtilityFunctions.MapUtility;
import MainClasses.DbOperations.*;
public class SOSAlert {
	static DbOperations m_dbConn;
	static Utility m_Utility;
	
	public SOSAlert(DbOperations m_dbConnObj) throws ClassNotFoundException {
		// TODO Auto-generated constructor stub
		m_dbConn = m_dbConnObj;
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchMemberDetails(int iUnitID) throws ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>>  resultMemberMain = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>>  resultMemberNo = new HashMap<Integer, Map<String, Object>>();
		String sSqlMemberMain = "",sSqlMemberContac="";
		
		sSqlMemberMain = "select m.member_id, m.owner_name,m.unit, u.unit_no, u.area,u.floor_no, u.intercom_no, u.share_certificate, u.share_certificate_from, u.share_certificate_to, u.nomination, w.wing, m.alt_address from member_main as m JOIN unit as u on m.unit = u.unit_id JOIN wing as w on w.wing_id = u.wing_id where m.unit = " + iUnitID + " AND u.status = 'Y' AND ownership_status = 1";	

		/******* fetching MemberID for the UnitID **********/
		 resultMemberMain =  m_dbConn.Select(sSqlMemberMain);
		 for(Entry<Integer, Map<String, Object>> entry : resultMemberMain .entrySet()) 
			{	
			 sSqlMemberContac ="SELECT * FROM `mem_other_family` where member_id='"+entry.getValue().get("member_id")+"' and coowner='1'";
			 resultMemberNo=  m_dbConn.Select(sSqlMemberContac);
			 for(Entry<Integer, Map<String, Object>> entry1 : resultMemberNo.entrySet()) 
				{
				 if(!entry1.getValue().get("other_mobile").equals(""))
				 {
					 entry.getValue().put("MemberContact", entry1.getValue().get("other_mobile"));
				 }
				 else
				 {
					 entry.getValue().put("MemberContact","0");
				 }
				}

			}
		
		return resultMemberMain;
	}
	public static long mSetMedicalEmergency(int iSocietyID,String sMemeberName,String sAlertType,String UnitNo,int iAlertStatus,int isosType,String OwnerContact,String sWing,String sFloorNo)
	{
		String sInsertQuery=""; 
		long iInsertID=0;
		try {
				String sSecurityDB = m_Utility.getDBNames(iSocietyID);
				//System.out.println("sSecurityDB "+ sSecurityDB);
				if(sSecurityDB != "")
				{
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
					//System.out.println("Database Security :" +dbop_security.toString());
					sInsertQuery = "insert into sos_alert(`AlertType`,`RaisedBy`,`UnitNo`,`R_FloorNo`,`R_Wing`,`ContactNo`,`AlertStatus`)values('"+isosType+"','"+sMemeberName + "','"+UnitNo+"','"+sFloorNo+"','"+sWing+"','"+OwnerContact+"','"+iAlertStatus+"')";
					iInsertID = dbop_security.Insert(sInsertQuery);
					//System.out.println(sInsertQuery);	
				}
				else
				{
					return 0;
				}		
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}

		
		return iInsertID;
	}
	
	public static long mSetFireEmergency(int iSocietyID,String sMemeberName,String sAlertType,String UnitNo,int iAlertStatus,int isosType,String OwnerContact,String sWing,String sFloorNo)
	{
		String sInsertQuery=""; 
		long iInsertID=0;
		try {
				String sSecurityDB = m_Utility.getDBNames(iSocietyID);
				//System.out.println("sSecurityDB "+ sSecurityDB);
				if(sSecurityDB != "")
				{
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
				//	System.out.println("Database Security :" +dbop_security.toString());
					sInsertQuery = "insert into sos_alert(`AlertType`,`RaisedBy`,`UnitNo`,`R_FloorNo`,`R_Wing`,`ContactNo`,`AlertStatus`)values('"+isosType+"','"+sMemeberName + "','"+UnitNo+"','"+sFloorNo+"','"+sWing+"','"+OwnerContact+"','"+iAlertStatus+"')";
					iInsertID = dbop_security.Insert(sInsertQuery);
					//System.out.println(sInsertQuery);	
				}
				else
				{
					return 0;
				}		
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}

		
		return iInsertID;
	}
	public static long mSetLiftEmergency(int iSocietyID,String sMemeberName,String sAlertType,String UnitNo,int iAlertStatus,int isosType,String OwnerContact,String sWing,String sFloorNo)
	{
		String sInsertQuery=""; 
		long iInsertID=0;
		try {
				String sSecurityDB = m_Utility.getDBNames(iSocietyID);
				//System.out.println("sSecurityDB "+ sSecurityDB);
				if(sSecurityDB != "")
				{
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
					//System.out.println("Database Security :" +dbop_security.toString());
					//sInsertQuery = "insert into sosalert(`AlertType`,`RaiseName`,`UnitNo`,`AlertStatus`,`sosType`,`ContactNo`)values('"+sAlertType+"','"+sMemeberName + "','"+UnitNo+"','"+iAlertStatus+"','"+isosType+"','"+OwnerContact+"')";
					sInsertQuery = "insert into sos_alert(`AlertType`,`RaisedBy`,`UnitNo`,`R_FloorNo`,`R_Wing`,`ContactNo`,`AlertStatus`)values('"+isosType+"','"+sMemeberName + "','"+UnitNo+"','"+sFloorNo+"','"+sWing+"','"+OwnerContact+"','"+iAlertStatus+"')";
					iInsertID = dbop_security.Insert(sInsertQuery);
					//System.out.println(sInsertQuery);	
				}
				else
				{
					return 0;
				}		
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}

		
		return iInsertID;
	}
	public static long mSetOtherEmergency(int iSocietyID,String sMemeberName,String sAlertType,String UnitNo,int iAlertStatus,int isosType,String OwnerContact,String sWing,String sFloorNo)
	{
		String sInsertQuery=""; 
		long iInsertID=0;
		try {
				String sSecurityDB = m_Utility.getDBNames(iSocietyID);
				//System.out.println("sSecurityDB "+ sSecurityDB);
				if(sSecurityDB != "")
				{
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
					//System.out.println("Database Security :" +dbop_security.toString());
					sInsertQuery = "insert into sos_alert(`AlertType`,`RaisedBy`,`UnitNo`,`R_FloorNo`,`R_Wing`,`ContactNo`,`AlertStatus`)values('"+isosType+"','"+sMemeberName + "','"+UnitNo+"','"+sFloorNo+"','"+sWing+"','"+OwnerContact+"','"+iAlertStatus+"')";
					//sInsertQuery = "insert into sosalert(`AlertType`,`RaiseName`,`UnitNo`,`AlertStatus`,`sosType`,`ContactNo`)values('"+sAlertType+"','"+sMemeberName + "','"+UnitNo+"','"+iAlertStatus+"','"+isosType+"','"+OwnerContact+"')";
					iInsertID = dbop_security.Insert(sInsertQuery);
					//System.out.println(sInsertQuery);	
				}
				else
				{
					return 0;
				}		
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}

		
		return iInsertID;
	}
	public static long mSetResovleByMe(int iSocietyID,int iAlertId,String sMemeberName,String sRole,int iAlertStatus)
	{
		String sUpdateQuery="",sInsertQuery=""; 
		long iUpdatedResult=0,InsertResult=0;
		try {
				String sSecurityDB = m_Utility.getDBNames(iSocietyID);
				//System.out.println("sSecurityDB "+ sSecurityDB);
				if(sSecurityDB != "")
				{
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
					//System.out.println("Database Security :" +dbop_security.toString());
					sUpdateQuery = "update `sos_alert`  set AlertStatus='"+iAlertStatus+"',ClosedTimeStamp=now() where  sosID='"+iAlertId+"'";
					iUpdatedResult = dbop_security.Update(sUpdateQuery);
					//System.out.println("sUpdateQuery" +sUpdateQuery);	
					sInsertQuery = "Insert into sos_Acknowledge (`sos_id`,`sos_acked_by`,`ack_Role`,`gate_no`) value ('"+iAlertId+"','"+sMemeberName+"','"+sRole+"','0')";
					InsertResult = dbop_security.Insert(sInsertQuery);
					
					//System.out.println("sInsertQuery" +sInsertQuery);	
				}
				else
				{
					return 0;
				}		
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}

		
		return iUpdatedResult;
	}
	
	public static HashMap<Integer, Map<String, Object>> mGetAlertSatatus(int iSocietyId,int iAlertID) throws ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> resultAlertStatus = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>> resultAckStatus = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "",sSelectQuery1="";
		try {
				String sSecurityDB = m_Utility.getDBNames(iSocietyId);
				//System.out.println("sSecurityDB "+ sSecurityDB);
				if(sSecurityDB != "")
				{
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
					sSelectQuery = "select * from `sos_alert` where `AlertStatus` IN('1','2') and sosID='"+iAlertID+"'";
					//System.out.println("sSelectQuery : "+ sSelectQuery);
					resultAlertStatus=  dbop_security.Select(sSelectQuery);
					if(resultAlertStatus.size() > 0)
					{
						rows.put("AlertDetails", MapUtility.HashMaptoList(resultAlertStatus));
					}
					
					sSelectQuery1 = "select * from `sos_Acknowledge` where sos_id='"+iAlertID+"'";
					resultAckStatus=  dbop_security.Select(sSelectQuery1);
					if(resultAckStatus.size() > 0)
					{
						rows.put("AckdDeatils", MapUtility.HashMaptoList(resultAckStatus));
					}
				}
				
			}
			catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}
		return rows;
	}
	
	
	public static HashMap<Integer, Map<String, Object>> mFetchSOSEmergency(int iSocietyId) throws ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> resultCommetee = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>> resultAlert = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "",sSelectQuery1="";
		try {
			String sSocietyDB = m_Utility.getSocietyDBNames(iSocietyId);
			DbOperations dbop_Society = new DbOperations(false,sSocietyDB);
			
			sSelectQuery= "SELECT c.position,c.member_id,mof.other_name,mm.unit FROM `commitee` as c Join `mem_other_family` as mof on mof.mem_other_family_id =c.member_id  join member_main as mm on mm.member_id = mof.member_id where c.member_id NOT IN(0)"; 
			resultCommetee=  dbop_Society.Select(sSelectQuery);
			
		//	System.out.println("sSelectQuery"+sSelectQuery);
			int UnitId=0;
			for(Entry<Integer, Map<String, Object>> entry : resultCommetee.entrySet())
			{
				rows2.put("UnitID", entry.getValue().get("unit"));
				rows2.put("Position", entry.getValue().get("position"));
				rows2.put("AckdName", entry.getValue().get("position"));
				//UnitId = (int) entry.getValue().get("unit");
				//String position = (String) entry.getValue().get("position");
				//String AckdName = (String) entry.getValue().get("other_name");
				
				
					String sSecurityDB = m_Utility.getDBNames(iSocietyId);
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
					sSelectQuery = "select * from `sos_alert` where `AlertStatus`= '0' and (`RaisedTimestamp` > DATE_SUB(now(), INTERVAL 1 DAY))";
					resultAlert=  dbop_security.Select(sSelectQuery);
					rows2.put("object", resultAlert);
				
			}
			
			}
			catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}
		return rows2;
	}
	/* Fetch In Background Incomming Visitor */
	public static HashMap<Integer, Map<String, Object>> mFetchIncommingVisitor(int iSocietyId,int iUnitId) throws ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> resultIncomming = new HashMap<Integer, Map<String, Object>>();
		
		String sSelectQuery = "";
		try {
				String sSecurityDB = m_Utility.getDBNames(iSocietyId);
				//System.out.println("sSecurityDB "+ sSecurityDB);
				if(sSecurityDB != "")
				{
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
					sSelectQuery = "SELECT va.v_id,va.unit_id,va.Entry_flag,ve.visitor_ID FROM `visit_approval` as va join visitorentry as ve on ve.id=va.v_id where va.unit_id='"+iUnitId+"' and va.Entry_flag='0' and status='inside' and (ve.`otpGtimestamp` > DATE_SUB(now(), INTERVAL 1 DAY))";
				//	System.out.println("sSelectQuery : "+ sSelectQuery);
					resultIncomming=  dbop_security.Select(sSelectQuery);
					
				}
				
			}
			catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}
		return resultIncomming;
	}
	
	
	public static HashMap<Integer, Map<String, Object>> FetaureSetting(int iSocietyId) throws ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mFetaureSetting = new HashMap<Integer, Map<String, Object>>();
		
		String sSelectQuery = "";
		try {
				String sSecurityDB = m_Utility.getDBNames(iSocietyId);
				System.out.println("sSecurityDB "+ sSecurityDB);
				if(sSecurityDB != "")
				{
					DbOperations dbop_security = new DbOperations(false,sSecurityDB);
					sSelectQuery = "SELECT * from feature_setting";
					System.out.println("sSelectQuery : "+ sSelectQuery);
					mFetaureSetting=  dbop_security.Select(sSelectQuery);
					System.out.println("Data : "+ mFetaureSetting);
					
				}
				
			}
			catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}
		return mFetaureSetting;
	}
}
