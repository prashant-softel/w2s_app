package MainClasses;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import CommonUtilityFunctions.MapUtility;
import MainClasses.DbOperations.*;

public class Member
{
	static DbOperations m_dbConn;
	static DbRootOperations m_dbConnRoot;
	static Utility m_Utility;
	
	public Member(DbOperations m_dbConnObj, DbRootOperations m_dbConnRootObj) throws ClassNotFoundException
	{
		m_dbConn = m_dbConnObj;
		m_dbConnRoot = m_dbConnRootObj;

		m_Utility = new Utility(m_dbConn, m_dbConnRoot);
	}	  	

	public static HashMap<Integer, Map<String, Object>> mfetchMemberDetailsFromUnitID(int iUnitID) throws ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		String sSqlMemberMain = "";
		
		sSqlMemberMain = "select m.member_id, m.owner_name,m.unit, u.unit_no, u.area, u.intercom_no, u.share_certificate, u.share_certificate_from, u.share_certificate_to, u.nomination, w.wing, m.alt_address from member_main as m JOIN unit as u on m.unit = u.unit_id JOIN wing as w on w.wing_id = u.wing_id where m.unit = " + iUnitID + " AND u.status = 'Y' AND ownership_status = 1";	

		/******* fetching MemberID for the UnitID **********/
		HashMap<Integer, Map<String, Object>> resultMemberMain =  m_dbConn.Select(sSqlMemberMain);
		
		//System.out.println(resultMemberMain);	
		int iMember_main_ID = 0;
		int iTenantID = 0;
		if(resultMemberMain.size() > 0)
		{	
			Map.Entry<Integer, Map<String, Object>> entryMemberMain = resultMemberMain.entrySet().iterator().next();
			iMember_main_ID = (Integer) entryMemberMain.getValue().get("member_id");
		}		
		
		rows.put("unit", MapUtility.HashMaptoList(resultMemberMain));
		
		//members for above memberid from mem_other_family table
		String sSqlOtherFamily = "select * from mem_other_family where status = 'Y' and member_id = " + iMember_main_ID;	
//		System.out.println("sSqlOtherFamily: " + sSqlOtherFamily);
		
		//All members related to iMemberID_from_member_main
		HashMap<Integer, Map<String, Object>> mpMember =  m_dbConn.Select(sSqlOtherFamily);
		//System.out.println("sSqlOtherFamily: " + mpMember);
//		rows.put("members", MapUtility.HashMaptoList(mpMember));

		for(Entry<Integer, Map<String, Object>> entry : mpMember.entrySet())
		{
			boolean bHasActiveAccount = false;
			String other_name = (String) entry.getValue().get("other_name");
			String other_email = (String) entry.getValue().get("other_email");
//			System.out.println("other_name:"+ other_name);
//			System.out.println("other_email:"+ other_email);
			if(other_email.length() > 0)
			{
				//Get Loginid
				String sqlLogin = "Select * from login where member_id= '"+ other_email +"' and status = 'Y'";
				//System.out.println("sqlLogin:"+ sqlLogin);
				HashMap<Integer, Map<String, Object>> LoginDetails = m_dbConnRoot.Select(sqlLogin);
				//System.out.println("LoginDetails size:"+ LoginDetails.size());
				//System.out.println("LoginDetails:"+ LoginDetails);
					
				if(LoginDetails.size() > 0)
				{
					for(Entry<Integer, Map<String, Object>> entry1 : LoginDetails.entrySet()) 
					{
						int iLoginID = Integer.parseInt(entry1.getValue().get("login_id").toString());
						String sStatus = entry1.getValue().get("status").toString();
						String sMember_id = entry1.getValue().get("member_id").toString();
						//System.out.println("ioginID :"+ iLoginID);
						//System.out.println("sStatus :"+ sStatus);
						//System.out.println("sMember_id : "+ sMember_id);
						
						if(iLoginID > 0)
						{
							String sqlMapping = "Select * from mapping where login_id = '"+ iLoginID +"'";
							//System.out.println("sqlMapping:"+ sqlMapping);
							HashMap<Integer, Map<String, Object>> MappingDetails = m_dbConnRoot.Select(sqlMapping);
							//System.out.println("ActivationDetails size:"+ MappingDetails.size());
							//System.out.println("ActivationDetails:"+ MappingDetails);
							if(MappingDetails.size() > 0)
							{
								//System.out.println("Mapping exist for " + other_email );
								bHasActiveAccount = true;
								
							}
							else
							{
								System.out.println("No Mapping");
								//No mapping
							}
						}
					}
				}
				else
				{
					//No login details
					System.out.println("No Login Details for " + other_email );
				}
				 
			}
			 else
			 {
					System.out.println("other_name:"+ other_name +" no email exist");
			 
			 }
			if (bHasActiveAccount == true)
			{
				entry.getValue().put("ActiveLogin", 1);
			}
			else
			{
				entry.getValue().put("ActiveLogin", 0);				
			}
		}

		rows.put("members", MapUtility.HashMaptoList(mpMember));
		
		//Fetch Car Details
		String sSqlCar = "Select * from mem_car_parking where member_id = '" + iMember_main_ID + "' AND status = 'Y'";
//		System.out.println("sSqlCar : "+sSqlCar);
		HashMap<Integer, Map<String, Object>> mpMemberCar =  m_dbConn.Select(sSqlCar);
		
		for(Entry<Integer, Map<String, Object>> entry1 : mpMemberCar.entrySet()){
			 int parking_type = Integer.parseInt((String) entry1.getValue().get("ParkingType"));
			if(parking_type != 0){
				String sSqlParkingType = "SELECT ParkingType, Rate, Description FROM `parking_type` where Id = '"+parking_type+"'";
				HashMap<Integer, Map<String, Object>> mpParkingType =  m_dbConn.Select(sSqlParkingType);
				for(Entry<Integer, Map<String, Object>> entry2 : mpParkingType.entrySet()){
					entry1.getValue().put("parking_type_name", entry2.getValue().get("ParkingType"));
					entry1.getValue().put("parking_type_desc", entry2.getValue().get("Description"));
					entry1.getValue().put("parking_type_rate", entry2.getValue().get("Rate"));
				}				
			}
		}
	
		rows.put("car", MapUtility.HashMaptoList(mpMemberCar));
		
		//Fetch Bike Details
		String sSqlBike = "Select * from mem_bike_parking where member_id = '" + iMember_main_ID + "' AND status = 'Y'";
		HashMap<Integer, Map<String, Object>> mpMemberBike =  m_dbConn.Select(sSqlBike);
		
		for(Entry<Integer, Map<String, Object>> entry1 : mpMemberBike.entrySet()){
			int parking_type = Integer.parseInt((String) entry1.getValue().get("ParkingType"));
			if(parking_type != 0){
				String sSqlParkingType = "SELECT ParkingType, Rate, Description FROM `parking_type` where Id = '"+parking_type+"'";
				HashMap<Integer, Map<String, Object>> mpParkingType =  m_dbConn.Select(sSqlParkingType);
				for(Entry<Integer, Map<String, Object>> entry2 : mpParkingType.entrySet()){
					entry1.getValue().put("parking_type_name", entry2.getValue().get("ParkingType"));
					entry1.getValue().put("parking_type_desc", entry2.getValue().get("Description"));
					entry1.getValue().put("parking_type_rate", entry2.getValue().get("Rate"));
				}				
			}
		}
		
		rows.put("bike",MapUtility.HashMaptoList(mpMemberBike));
		
		//Fetch Bike Details
		String sSqlLien = "Select * from mortgage_details where member_id = '" + iMember_main_ID + "' AND status = 'Y'";
		HashMap<Integer, Map<String, Object>> mpLien =  m_dbConn.Select(sSqlLien);
		//System.out.println("mpLien : "+ mpLien +"");
		rows.put("lien",MapUtility.HashMaptoList(mpLien));
		
		//Fetch Tenent Details 
		String TodayDate= GetTodayDate();
		String sSqlTenant ="select tenant_id,unit_id, tenant_name,start_date, end_date, agent_name, agent_no, note,members from `tenant_module` where unit_id = '"+iUnitID+"' AND end_date >= '"+TodayDate+"'";
		HashMap<Integer, Map<String, Object>> mpMemberTenant =  m_dbConn.Select(sSqlTenant);
		
		for(Entry<Integer, Map<String, Object>> entry3 : mpMemberTenant.entrySet()){
			//int tenantID = Integer.parseInt((String) entry3.getValue().get("tenant_id"));
			iTenantID = (Integer) entry3.getValue().get("tenant_id");
			//iMember_main_ID = (Integer) entryMemberMain.getValue().get("member_id");
			if(iTenantID != 0){
				String sSqlParkingType = "SELECT tmember_id,mem_name,relation,DATE_FORMAT(mem_dob,'%d %b, %Y') as mem_dob,contact_no,email FROM `tenant_member` where tenant_id='"+iTenantID+"'";
				HashMap<Integer, Map<String, Object>> mpTenantType =  m_dbConn.Select(sSqlParkingType);
				//System.out.println("mpTenantType"+mpTenantType);
				HashMap rows3 = new HashMap<>();
				/*for(Entry<Integer, Map<String, Object>> entry4 : mpTenantType.entrySet()){
					rows3.put("OccupyName", entry4.getValue().get("mem_name"));
					rows3.put("OccupyDob", entry4.getValue().get("mem_dob"));
					rows3.put("Relation", entry4.getValue().get("relation"));
					rows3.put("ContanctNo", entry4.getValue().get("contact_no"));
					rows3.put("EMail", entry4.getValue().get("email"));
					System.out.println("rows3 "+rows3);
					entry3.getValue().put("OccupyDetails",rows3);
				}*/
				
				entry3.getValue().put("OccupyDetails",mpTenantType);
				//entry3.put("OccupyDetails",rows3);
			}
			
		}
		rows.put("Tenant",MapUtility.HashMaptoList(mpMemberTenant));
		
		return rows;
	}
	
	/*public static String  UpdateMemberDetails2(int iMemberID, int iMemberOtherTableID, String sName, int iCoOwner, String sRelation, int iPublishContact , String sMobile, String sEmail, int iBloodGroup, String sDOB, int iPublishProfile, String sProfProfile, int iDesignation, String sSSC, String sWeddingDate) throws SQLException
	{
		String sUpdateQuery = "update mem_other_family set other_name ='" + sName + "', coowner='" + iCoOwner + "', relation ='"+ sRelation + "', other_publish_contact='" + iPublishContact+ "', other_mobile='"+ sMobile + "',  other_email='" + sEmail + "', child_bg='" + iBloodGroup + "', other_dob='"+ sDOB + "',  other_publish_profile='" + iPublishProfile + "',  other_profile='" + sProfProfile + "',  other_desg='" + iDesignation + "',  ssc='" + sSSC + "', other_wed='" + sWeddingDate + "' where mem_other_family_id ='" + iMemberOtherTableID + "' and  member_id='"+ iMemberID + "'";
		return sUpdateQuery;
	}*/
	
	public static int UpdateMemberDetails(int iMemberID, int iMemberOtherTableID, String sName, int iCoOwner, String sRelation, int iPublishContact , String sMobile, String sEmail, int iBloodGroup, String sDOB, int iPublishProfile, String sProfProfile, int iDesignation, String sSSC, String sWeddingDate,String sGender,String sAdhaar) throws SQLException
	{
		String sUpdateQuery = "";
		
		m_dbConn.BeginTransaction();	
		
		try {
			sUpdateQuery = "update mem_other_family set other_name ='" + sName + "', coowner='" + iCoOwner + "', relation ='"+ sRelation + "', other_publish_contact='" + iPublishContact+ "', other_mobile='"+ sMobile + "',  other_email='" + sEmail + "', other_gender='" + sGender + "',other_adhaar='" + sAdhaar + "', child_bg='" + iBloodGroup + "', other_dob='"+ sDOB + "',  other_publish_profile='" + iPublishProfile + "',  other_profile='" + sProfProfile + "',  other_desg='" + iDesignation + "',  ssc='" + sSSC + "', other_wed='" + sWeddingDate + "' where mem_other_family_id ='" + iMemberOtherTableID + "' and  member_id='"+ iMemberID + "'";		
		
			//System.out.println(sUpdateQuery);
			//if(!sAddress.equals(""))
			//{
			//	sUpdateQuery = "update member_main set alt_address ='" + sAddress + "' where member_id='"+ iMemberID + "'";
			//}
			long lMemberID = m_dbConn.Update(sUpdateQuery);
			//System.out.println("lMemberID<" + lMemberID + ">");	
			
			if(lMemberID == 0)
			{
				m_dbConn.RollbackTransaction();
				return 0;
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
	
	public static int AddMemberDetails(int iMemberID, String sName, int iCoOwner, String sRelation, int iPublishContact , String sMobile, String sEmail, int iBloodGroup, String sDOB, int iPublishProfile, String sProfProfile, int iDesignation, String sSSC, String sWeddingDate,String sGender,String sAdhaar) throws SQLException
	{
		String sInsertQuery = "";
		
		m_dbConn.BeginTransaction();	
		
		try {
			if(sWeddingDate.isEmpty())
			{
				sWeddingDate= "0000-00-00";
			}
			//Insert into mem_other_family
			sInsertQuery = "insert into mem_other_family (`member_id`,`other_name`,`relation`,`other_dob`,`other_desg`,`ssc`,`child_bg`,`other_wed`,`other_profile`,`other_publish_contact`,`other_publish_profile`, `other_mobile`, `other_email`,`coowner`,`other_gender`,`other_adhaar`) values ";
			sInsertQuery = sInsertQuery + "(" + iMemberID + ",'" + sName +  "','" + sRelation +  "','" + sDOB  +  "','" + iDesignation +  "','" + sSSC  +  "','" + iBloodGroup + "','" + sWeddingDate  +  "','" +  sProfProfile +  "','" + iPublishContact +  "','" +  iPublishProfile +  "','" + sMobile  +  "','" + sEmail  +  "','" + iCoOwner  + "','"+sGender+"','"+sAdhaar+"')";


			//System.out.println(sInsertQuery);	
			long lMemberID = m_dbConn.Insert(sInsertQuery);
			//System.out.println("lMemberID<" + lMemberID + ">");	
			
			if(lMemberID == 0)
			{
				m_dbConn.RollbackTransaction();
				return 0;
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
	

	public static int AddVehicleDetails(int iMemberID, int iVehicleType, String sParkingSlot, String sParkingSticker, String sRegNo, String sOwner, String sVehicleMake, String sVehicleModel, String sVehicleColor) throws SQLException
	{
		String sInsertQuery = "";
		
		m_dbConn.BeginTransaction();
		
		try {
			
			if(iVehicleType == 4)//Car
			{
				//Insert into mem_car_parking
				sInsertQuery = "insert into mem_car_parking (`member_id`,`parking_slot`,`car_reg_no`,`car_owner`,`car_model`,`car_make`,`car_color`,`parking_sticker`) values ";				
				sInsertQuery = sInsertQuery + "(" + iMemberID + ",'" + sParkingSlot +  "','" + sRegNo +  "','" + sOwner  +  "','" + sVehicleModel  +  "','" + sVehicleMake  +  "','" +  sVehicleColor +  "','" + sParkingSticker  + "')";
			}
			else
			{
				sInsertQuery = "insert into mem_bike_parking (`member_id`,`parking_slot`,`bike_reg_no`,`bike_owner`,`bike_model`,`bike_make`,`bike_color`,`parking_sticker`) values ";
				sInsertQuery = sInsertQuery + "(" + iMemberID + ",'" + sParkingSlot +  "','" + sRegNo +  "','" + sOwner  +  "','" + sVehicleModel  +  "','" + sVehicleMake  +  "','" +  sVehicleColor +  "','" + sParkingSticker  + "')";
			}
			//System.out.println(sInsertQuery);	
			long lVehicleID = m_dbConn.Insert(sInsertQuery);
			//System.out.println("Vehicle ID<" + lVehicleID + ">");	
			
			if(lVehicleID == 0)
			{
				m_dbConn.RollbackTransaction();
				return 0;
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
	
	public static int UpdateVehicleDetails(int iMemberID, int iVehicleTableID, int iVehicleType, String sParkingSlot, String sParkingSticker, String sRegNo, String sOwner, String sVehicleMake, String sVehicleModel, String sVehicleColor) throws SQLException
	{
		String sUpdateQuery = "";
		
		m_dbConn.BeginTransaction();
		
		try {
			
			if(iVehicleType == 4)//Car
			{
				//Update into mem_car_parking	
				sUpdateQuery = "update mem_car_parking set parking_slot='" + sParkingSlot + "', car_reg_no='" + sRegNo + "', car_owner='"+ sOwner + "', car_model='" + sVehicleModel+ "', car_make='"+ sVehicleMake + "',  car_color='" + sVehicleColor + "',  parking_sticker='"+ sParkingSticker + "' where mem_car_parking_id='" + iVehicleTableID + "' and  member_id='"+ iMemberID + "'";
				
			}
			else
			{
				sUpdateQuery = "update mem_bike_parking set parking_slot='" + sParkingSlot + "', bike_reg_no='" + sRegNo + "', bike_owner='"+ sOwner + "', bike_model='" + sVehicleModel+ "', bike_make='"+ sVehicleMake + "',  bike_color='" + sVehicleColor + "',  parking_sticker='"+ sParkingSticker + "' where mem_bike_parking_id='" + iVehicleTableID + "' and  member_id='"+ iMemberID + "'";				
			}
			//System.out.println(sUpdateQuery);	
			long lVehicleID = m_dbConn.Update(sUpdateQuery);
			//System.out.println("Vehicle ID<" + lVehicleID + ">");	
			
			if(lVehicleID == 0)
			{
				m_dbConn.RollbackTransaction();
				return 0;
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

	public static int DeleteVehicleDetails(int iMemberID, int iVehicleTableID, int iVehicleType) throws SQLException
	{
		String sUpdateQuery = "";
		
		m_dbConn.BeginTransaction();
		
		try {
			
			if(iVehicleType == 1)//Car
			{
				//Update into mem_car_parking	
				sUpdateQuery = "UPDATE mem_car_parking set status='N' where mem_car_parking_id='" + iVehicleTableID + "' and  member_id='"+ iMemberID + "'";
			}
			else
			{				
				sUpdateQuery = "UPDATE mem_bike_parking set status='N' where mem_bike_parking_id='" + iVehicleTableID + "' and  member_id='"+ iMemberID + "'";
			}
			//System.out.println(sUpdateQuery);	
			long lVehicleID = m_dbConn.Update(sUpdateQuery);
			//System.out.println("Vehicle ID<" + lVehicleID + ">");	
			
			if(lVehicleID == 0)
			{
				m_dbConn.RollbackTransaction();
				return 0;
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
	
	public static HashMap<Integer, Map<String, Object>> GetDesignations()
	{
		String sSelectQuery = "select desg_id, desg from desg where status = 'Y'";	

		//System.out.println(sSelectQuery);	
		/******* fetching designations **********/
		HashMap<Integer, Map<String, Object>> resultDesignations =  m_dbConn.Select(sSelectQuery);
		//System.out.println("Dbname "+m_dbConn);
				
		return resultDesignations;
	}
	
	public static HashMap<Integer, Map<String, Object>> fetchCommitee()
	{
		String sSelectQuery = "SELECT c.`id` AS commiteeid, m1.`mem_other_family_id` AS memID, m1.`other_mobile` as mobile, c.`position`, m1.`other_name` AS name, m1.`coowner`, GROUP_CONCAT(' ',s.`category`) AS category , s.`email`, u.`unit_no` FROM `commitee` AS c JOIN `mem_other_family` AS m1 ON c.`member_id`=m1.`mem_other_family_id` JOIN `member_main` AS m2 ON m1.`member_id`=m2.`member_id` JOIN `unit` AS u ON m2.`unit`=u.`unit_id` LEFT JOIN `servicerequest_category` as s ON c.`member_id`=s.`member_id` GROUP BY m1.`mem_other_family_id` ORDER BY commiteeid";	
		HashMap<Integer, Map<String, Object>> resultDesignations =  m_dbConn.Select(sSelectQuery);
		return resultDesignations;
	}
	
	public static HashMap<Integer, Map<String, Object>> GetContacts(int iUnitId, int iLogin)
	{
		//old QUery 
		String sSelectQuery ="";
		if(iLogin == 0)
		{
			sSelectQuery = "SELECT mof.member_id,mof.mem_other_family_id, mof.other_name,mof.other_mobile, mof.`send_commu_emails`,IF(mm.`security_callback` <> mof.`mem_other_family_id` ,0 ,mm.`security_callback`) as securityCall FROM `member_main` as mm join `mem_other_family` as mof on mof.member_id=mm.member_id where mm.unit='"+iUnitId+"' and mof.`other_mobile` > '0'  and mof.status='Y'";	
		}
		else
		{
			// sSelectQuery = "SELECT mof.member_id,mof.mem_other_family_id, mof.other_name,mof.other_mobile, mof.`send_commu_emails`,IF(mm.`security_callback` <> mof.`mem_other_family_id` ,0 ,mm.`security_callback`) as securityCall FROM `member_main` as mm join `mem_other_family` as mof on mof.member_id=mm.member_id where mm.unit='"+iUnitId+"' and mof.`other_mobile` > '0'  and mof.status='Y'";	
		}
		HashMap<Integer, Map<String, Object>> resultMemberContact =  m_dbConn.Select(sSelectQuery);
		
		return resultMemberContact;
	}
	
	
	
	public static int UpdateContacts(int iUnitId,int iOtherFemilyID) throws SQLException
	{
		String sUpdateQuery = "";
		
		m_dbConn.BeginTransaction();
		
		try {
			
				
			 sUpdateQuery = "UPDATE member_main set security_callback='"+iOtherFemilyID+"' where unit='"+iUnitId+"'";
			 //System.out.println("Member QUery " +sUpdateQuery);
			 long UpdateID = m_dbConn.Update(sUpdateQuery);
			// System.out.println("id " +UpdateID);
			
			if(UpdateID == 0)
			{
				m_dbConn.RollbackTransaction();
				return 0;
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
	public static int UpdateAddress(int iMemberID,String OwnerAdd) throws SQLException
	{
		String sUpdateQuery = "";
		
		m_dbConn.BeginTransaction();
		
		try {
			
				
			 sUpdateQuery = "UPDATE member_main set alt_address='"+OwnerAdd+"' where member_id='"+iMemberID+"'";
			 //System.out.println("Member QUery " +sUpdateQuery);
			 long UpdateID = m_dbConn.Update(sUpdateQuery);
			 //System.out.println("id " +UpdateID);
			
			if(UpdateID == 0)
			{
				m_dbConn.RollbackTransaction();
				return 0;
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
	
	public static int UpdateMemberNofify(int iMemOtherID,int notify) throws SQLException
	{
		String sUpdateQuery = "";
		
		m_dbConn.BeginTransaction();
		
		try {
			
				
			 sUpdateQuery = "UPDATE `mem_other_family` set 	send_commu_emails='"+notify+"' where mem_other_family_id='"+iMemOtherID+"'";
			/// System.out.println("Member QUery " +sUpdateQuery);
			 long UpdateID = m_dbConn.Update(sUpdateQuery);
			 //System.out.println("id " +UpdateID);
			
			if(UpdateID == 0)
			{
				m_dbConn.RollbackTransaction();
				return 0;
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
	public static String GetTodayDate() throws ParseException
	{
		DateFormat output = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		return output.format(date);
		
	}
	
	/*   --------------------------  Add New Function For renting Registration -------------------------  */
	public static HashMap<Integer, Map<String, Object>> mfetchMemberDetailsForRegistration(int iSocietyID,int iUnitID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		String sSqlMemberMain = "";
		
		sSqlMemberMain = "select m.member_id, m.owner_name,m.alt_address, m.unit, u.unit_no, u.area, w.wing from member_main as m JOIN unit as u on m.unit = u.unit_id JOIN wing as w on w.wing_id = u.wing_id where m.unit = " + iUnitID + " AND u.status = 'Y' AND ownership_status = 1";	

		/******* fetching MemberID for the UnitID **********/
		HashMap<Integer, Map<String, Object>> resultMemberMain =  m_dbConn.Select(sSqlMemberMain);
		//System.out.println("resultMemberMain " + resultMemberMain);
		//System.out.println("resultMemberMain " + resultMemberMain.size());

		int iMember_main_ID = 0;
		//int iSociety_ID = 0;
		rows.put("unit", MapUtility.HashMaptoList(resultMemberMain));
		if(resultMemberMain.size() > 0)
		{	
			Map.Entry<Integer, Map<String, Object>> entryMemberMain = resultMemberMain.entrySet().iterator().next();
			iMember_main_ID = (Integer) entryMemberMain.getValue().get("member_id");
			//iSociety_ID = (Integer) entryMemberMain.getValue().get("society_id");
		}
		String sSqlOtherFamily = "select mof.member_id, mof.mem_other_family_id , mof.other_name, mof.other_dob, mof.coowner, d.desg as profession,mof.other_gender, mof.other_address,mof.other_adhaar from mem_other_family as mof JOIN desg as d ON mof.other_desg=d.desg_id where mof.status = 'Y' and mof.coowner in (1,2) and mof.member_id = " + iMember_main_ID;	
		//String sSqlOtherFamily = "select member_id, mem_other_family_id ,other_name, other_dob, coowner from mem_other_family where status = 'Y' and coowner in (1,2) and member_id = " + iMember_main_ID;	
		//System.out.println("sSqlOtherFamily: " + sSqlOtherFamily);
		
		//All members related to iMemberID_from_member_main
		HashMap<Integer, Map<String, Object>> mpMember =  m_dbConn.Select(sSqlOtherFamily);
		rows.put("members", MapUtility.HashMaptoList(mpMember));
		
		String sSqlSociety = "select society_add, city, region, postal_code from society where status = 'Y' and society_id = " + iSocietyID;	
//		System.out.println("sSqlOtherFamily: " + sSqlOtherFamily);
		
		//All members related to iMemberID_from_member_main
		HashMap<Integer, Map<String, Object>> mpSociety =  m_dbConn.Select(sSqlSociety);
		rows.put("society", MapUtility.HashMaptoList(mpSociety));
		//String sSelectProfession = "SELECT * FROM `profession` order by `Id` asc";
		String sSelectProfession = "SELECT * FROM `desg` order by `desg_id` asc";
		HashMap<Integer, Map<String, Object>> mpProfession =  m_dbConn.Select(sSelectProfession);
		rows.put("profession", MapUtility.HashMaptoList(mpProfession));
		return rows;
	}
	public static HashMap<Integer, Map<String, Object>> mfetchTenantDetailsFromUnitID(int iUnitID) throws ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		String sSqlMemberMain = "";
		
		sSqlMemberMain = "select m.member_id,m.unit, u.unit_no, u.area, w.wing from member_main as m JOIN unit as u on m.unit = u.unit_id JOIN wing as w on w.wing_id = u.wing_id where m.unit = "+iUnitID+" AND u.status = 'Y' AND ownership_status = 1";

		/******* fetching MemberID for the UnitID **********/
		HashMap<Integer, Map<String, Object>> resultMemberMain =  m_dbConn.Select(sSqlMemberMain);
		
		//System.out.println(resultMemberMain);	
		int iMember_main_ID = 0;
		int iTenantID = 0;
		if(resultMemberMain.size() > 0)
		{	
			Map.Entry<Integer, Map<String, Object>> entryMemberMain = resultMemberMain.entrySet().iterator().next();
			iMember_main_ID = (Integer) entryMemberMain.getValue().get("member_id");
		}		
		
		rows.put("unit", MapUtility.HashMaptoList(resultMemberMain));
		
		String TodayDate= GetTodayDate();
		String sSqlTenant ="select tenant_id,unit_id, tenant_name,tenant_MName,tenant_LName,mobile_no,start_date, end_date, agent_name, agent_no, note,members,Address,City,PinCode from `tenant_module` where unit_id = '"+iUnitID+"'";
		HashMap<Integer, Map<String, Object>> mpMemberTenant =  m_dbConn.Select(sSqlTenant);
		
		//rows.put("TenantDetals",MapUtility.HashMaptoList(mpMemberTenant));
		HashMap rows3 = new HashMap<>();
		int i =0;
		for(Entry<Integer, Map<String, Object>> entry3 : mpMemberTenant.entrySet()){
			
			iTenantID = (Integer) entry3.getValue().get("tenant_id");
			
			if(iTenantID != 0){
				String sSqlParkingType = "SELECT tmember_id,mem_name,relation,DATE_FORMAT(mem_dob,'%d %b, %Y') as mem_dob,contact_no,email FROM `tenant_member` where tenant_id='"+iTenantID+"'";
				HashMap<Integer, Map<String, Object>> mpTenantType =  m_dbConn.Select(sSqlParkingType);
				//System.out.println("mpTenantType"+mpTenantType);
				
				
				
				rows3.put(i,mpTenantType);
				//entry3.put("OccupyDetails",rows3);
				i++;
			}
			
		}
		rows.put("OccupyDetails",rows3);
		rows.put("Tenant",MapUtility.HashMaptoList(mpMemberTenant));
		
		return rows;
	}
	
	
//	                                  get and set dnd data
	public static long UpdateDNDdetails(int iSocietyID,int unit_id, int unit_no, int dnd_type, String dnd_msg)
	{
	 long iInsertID= 0;
//	 long iUpdateID= 0;
	  try{
		 String sSecurityDB = m_Utility.getDBNames(iSocietyID);

		 String sInsertQuery = "";
		 String sSelectQuery = "";
		 String sUpdateQuery = "";
		 if(sSecurityDB != "")
		 {
			 DbOperations dbop_security = new DbOperations(false, sSecurityDB);
			 sSelectQuery = "SELECT * from dnd_status where unit_id='"+ unit_id +"'";
			
			 System.out.println(sSelectQuery);
			 HashMap<Integer, Map<String, Object>> dndmsecurity =  dbop_security.Select(sSelectQuery);
			 if(dndmsecurity.size() > 0)
			 {
//				'update query
				 sUpdateQuery = "UPDATE dnd_status set dnd_type='" +dnd_type+ "', dnd_msg='" +dnd_msg+  "' where unit_id='" +unit_id+ "'";
//				 iUpdateID = dbop_security.Update(sUpdateQuery);
				 iInsertID = dbop_security.Update(sUpdateQuery);

				 System.out.println(sUpdateQuery);

		     }
		     else
		     {
//				insert query 
				sInsertQuery = "insert into dnd_status (`unit_id`,`unit_no`,`dnd_type`,`dnd_msg`) values ";				
				sInsertQuery = sInsertQuery + "('" + unit_id +  "','" + unit_no +  "','" + dnd_type  +  "' ,'" + dnd_msg +  "')";
			
				iInsertID = dbop_security.Insert(sInsertQuery);
				System.out.println("Database Security :" +dbop_security.toString());
				System.out.println(sInsertQuery);	

		     }

			//System.out.println("Database Security :" +dbop_security.toString());
			
		 }
		 else
		 {
			return 0;
		 }
	   }
	   catch (Exception e) 
	   {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
		}

		return iInsertID;
	}
//	public static  getDNDdetails(int iSocietyID,int unit_id, int dnd_type, String dnd_msg){
	public static HashMap<Integer, Map<String, Object>> getDNDdetails(int iSocietyID,int unit_id) throws ClassNotFoundException
	{
		 String sfetchQuery = "";
		 System.out.println("inside denddetail");
		 String sSecurityDB = m_Utility.getDBNames(iSocietyID);
		 HashMap<Integer, Map<String, Object>> dnd_status =  null;
		 try{
			 String sSelectQuery = "";
			 
			 if(sSecurityDB != "")
			 {
				 DbOperations dbop_security = new DbOperations(false, sSecurityDB);
				 sfetchQuery = "SELECT unit_id, dnd_type, dnd_msg from dnd_status where unit_id='"+ unit_id +"'";
				 System.out.println(sfetchQuery);
				 dnd_status =  dbop_security.Select(sfetchQuery);
			 }
		 }
		 catch (Exception e) 
		   {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("Size  :" +e.getMessage());				
			}
		 return dnd_status;
	}

}
