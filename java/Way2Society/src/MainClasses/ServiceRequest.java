package MainClasses;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public class ServiceRequest
{
	static DbOperations m_dbConn;
	static DbRootOperations m_dbConnRoot;
	//static Utility m_Utility;
	
	public ServiceRequest(DbOperations m_dbConnObj, DbRootOperations  m_dbConnRootObj)
	{
		m_dbConn = m_dbConnObj;
		m_dbConnRoot = m_dbConnRootObj;
		//m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	

	
	public static HashMap<Integer, Map<String, Object>> fetchContractor(int iSocietyId,int iLoginID)
	{
		String sSqlServiceRequest = "";
			
		//Fetching ServiceRequests
		//sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp, u.unit_no from service_request join unit as u ON u.unit_id=service_request.unit_id WHERE service_request.visibility=1 AND request_no=" + iSR_No + " ORDER BY service_request.request_no  DESC";
		sSqlServiceRequest = "SELECT r.request_id,r.request_no,r.`society_id`,r. `reportedby`, DATE_FORMAT(r.dateofrequest,'%d %b, %Y') as dateofrequest,r. `email`, r.`phone`, r.`priority`, r.`category`, r.`summery`, `details`, DATE_FORMAT(CONVERT_TZ(r.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp, r.`unit_id`, r.`visibility`, r.`img` ,s.category,s.contractor_loginid,u.unit_no FROM `servicerequest_category` as s JOIN service_request as r on s.id=r.category join unit as u on u.unit_id=r.unit_id where s.contractor_loginid = '"+iLoginID+"'";		
		//System.out.println(sSqlServiceRequest);
		HashMap<Integer, Map<String, Object>> mpServiceRequest =  m_dbConn.Select(sSqlServiceRequest);
		//System.out.println(mpServiceRequest);
		for(Entry<Integer, Map<String, Object>> entry : mpServiceRequest.entrySet()) 
		{
			int request_no = (int) entry.getValue().get("request_no");
			String sUnitID = entry.getValue().get("unit_id").toString();
			//sSqlServiceRequest = request_no;
			String sSQLSelectStatus = "SELECT `status` from service_request where request_no = '" + request_no + "' ORDER BY request_id DESC LIMIT 0,1";
			HashMap<Integer, Map<String, Object>> mpServiceRequestStatus =  m_dbConn.Select(sSQLSelectStatus);
			
			if(mpServiceRequestStatus.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> valueRequestsStatus : mpServiceRequestStatus.entrySet()) 
				{
					entry.getValue().put("status", valueRequestsStatus.getValue().get("status"));
				}
			}
		}		
		return mpServiceRequest;
	}
	
	
	
	public static HashMap<Integer, Map<String, Object>> mfetchServiceRequestHistory(int iSR_No)
	{
		String sSqlServiceRequest = "";
			
		//Fetching ServiceRequests
		//sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp, u.unit_no from service_request join unit as u ON u.unit_id=service_request.unit_id WHERE service_request.visibility=1 AND request_no=" + iSR_No + " ORDER BY service_request.request_no  DESC";
		sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp, u.unit_no,mem_other.other_name as Supervised_by from service_request LEFT join unit as u ON u.unit_id=service_request.unit_id LEFT JOIN servicerequest_category as serv_cat ON serv_cat.ID = service_request.category LEFT JOIN mem_other_family as mem_other ON serv_cat.member_id = mem_other.mem_other_family_id WHERE service_request.visibility=1 AND request_no='" + iSR_No + "' ORDER BY service_request.request_no DESC";		
		//System.out.println(sSqlServiceRequest);
		HashMap<Integer, Map<String, Object>> mpServiceRequest =  m_dbConn.Select(sSqlServiceRequest);
				
		return mpServiceRequest;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchServiceRequests(int iUnit, int iFetchCount, boolean bFetchClosed, boolean bAssignedToMe, int iLoginID,int iSocietyID)
	{
		String sSqlServiceRequest = "";
			//System.out.println("UNIT ID :" +iUnit+ "Assing me :"+bAssignedToMe); 
		//Fetching ServiceRequests
		HashMap<Integer, Map<String, Object>> mpServiceRequest = new HashMap<Integer, Map<String, Object>>();
		//sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, service_request.dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img from service_request inner join (select request_no, min(timestamp) as ts from service_request group by request_no) maxt on (service_request.request_no = maxt.request_no and service_request.timestamp = maxt.ts)   WHERE service_request.visibility=1 AND unit_id=" + iUnit + " ORDER BY service_request.request_no  DESC";
		if(iUnit == 0)
		{
			sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category as category_id,  servicerequest_category.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp, u.unit_no from service_request inner join (select request_no, min(timestamp) as ts from service_request group by request_no) maxt on (service_request.request_no = maxt.request_no and service_request.timestamp = maxt.ts) JOIN  servicerequest_category ON  servicerequest_category.ID =  service_request.category join unit as u ON u.unit_id=service_request.unit_id  WHERE service_request.visibility=1 ORDER BY service_request.request_no  DESC";
			//System.out.println("");
		}
		
		else if(iUnit > 0 && !bAssignedToMe)
		{
			
			
			//System.out.println("");
			//sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category as category_id,  servicerequest_category.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp ,u.unit_no,mm.owner_name,mof.mem_other_family_id from service_request inner join (select request_no, min(timestamp) as ts from service_request group by request_no) maxt on (service_request.request_no = maxt.request_no and service_request.timestamp = maxt.ts) JOIN  servicerequest_category ON  servicerequest_category.ID =  service_request.category join unit as u ON u.unit_id=service_request.unit_id join member_main as mm on mm.unit=servicerequest_category.unitID Join `mem_other_family` as mof on mof.member_id=mm.member_id WHERE service_request.visibility=1 AND mof. mem_other_family_id="+memberID+"  ORDER BY service_request.request_no  DESC";
			sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category as category_id,  servicerequest_category.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp ,u.unit_no from service_request inner join (select request_no, min(timestamp) as ts from service_request group by request_no) maxt on (service_request.request_no = maxt.request_no and service_request.timestamp = maxt.ts) JOIN  servicerequest_category ON  servicerequest_category.ID =  service_request.category join unit as u ON u.unit_id=service_request.unit_id  WHERE service_request.visibility=1 AND service_request.unit_id=" + iUnit + " ORDER BY service_request.request_no  DESC";
			//System.out.println("if else condition ");
			//System.out.println(sSqlServiceRequest);
			//}
		}
		else
		{
			String SqlLogged="select unit_id,dbname from `mapping` join dbname on dbname.society_id=mapping.society_id where login_id='"+iLoginID+"' and dbname.society_id='"+iSocietyID+"'";
			HashMap<Integer, Map<String, Object>> mpMapping =  m_dbConnRoot.Select(SqlLogged);
			
			if(mpMapping.size() > 0)
			{
				
				
				for(Entry<Integer, Map<String, Object>> entry : mpMapping.entrySet()) 
				{
					String sUnitID = entry.getValue().get("unit_id").toString();
					String dbName =entry.getValue().get("dbname").toString();
				//	System.out.println("memberID : "+sUnitID+" Dbname :"+dbName);
					//String SqlUnit = "select sc.member_id from `servicerequest_category` as sc JOIN mem_other_family as mof ON sc.member_id=mof.member-id where sc.unitID='"+sUnitID+"' JOIN"
					String SqlUnit =  "select mof.mem_other_family_id,mof.other_name,mm.unit from mem_other_family as mof JOIN member_main as mm ON mm.member_id = mof.member_id JOIN `servicerequest_category` as sc ON sc.member_id=mof.mem_other_family_id where mm.unit= '"+sUnitID+"'  ";
					HashMap<Integer, Map<String, Object>> mpMemberDetails =  m_dbConn.Select(SqlUnit);
					//System.out.println("assigned member details : "+mpMemberDetails);
					if(mpMemberDetails.size() > 0)
					{
						
						
						for(Entry<Integer, Map<String, Object>> entry2 : mpMemberDetails.entrySet()) 
						{
							String sMemOtherFamilyID = entry2.getValue().get("mem_other_family_id").toString();
							String sMemOtherName = entry2.getValue().get("other_name").toString();
							String sAssignID = entry2.getValue().get("unit").toString();
							String SqlCategories ="select * from servicerequest_category where member_id='"+sMemOtherFamilyID+"'";
							HashMap<Integer, Map<String, Object>> mpCategories =  m_dbConn.Select(SqlCategories);
							//System.out.println("Categories : "+mpCategories);
							if(mpCategories.size() > 0)
							{
								for(Entry<Integer, Map<String, Object>> entry3 : mpCategories.entrySet()) 
								{
									String sCategoryID = entry3.getValue().get("ID").toString();
									//Rohit Sir Assign querty working
									//String SqlServiceRequests ="select * from service_request where category='"+sCategoryID+"'";
									
									String SqlServiceRequests ="select '"+sAssignID+"' as AssignID,service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category as category_id,  servicerequest_category.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp ,u.unit_no from service_request inner join (select request_no, min(timestamp) as ts from service_request group by request_no) maxt on (service_request.request_no = maxt.request_no and service_request.timestamp = maxt.ts) JOIN  servicerequest_category ON  servicerequest_category.ID =  service_request.category join unit as u ON u.unit_id=service_request.unit_id where service_request.visibility=1 AND  service_request.category='"+sCategoryID+"' ORDER BY service_request.request_no  DESC";
									mpServiceRequest =  m_dbConn.Select(SqlServiceRequests);
									//System.out.println("SRAssignQuery : "+SqlServiceRequests);
									//System.out.println("SRs : "+mpServiceRequest);
									if(mpServiceRequest.size() > 0)
									{
										
										
										for(Entry<Integer, Map<String, Object>> entry4 : mpServiceRequest.entrySet()) 
										{
											//int lRequestNo = (int) valueRequests.getValue().get("request_no");
											int request_no = (int) entry4.getValue().get("request_no");
											//sSqlServiceRequest = request_no;
											String sSQLSelectStatus = "SELECT `status` from service_request where request_no = '" + request_no + "' ORDER BY request_id DESC LIMIT 0,1";
											HashMap<Integer, Map<String, Object>> mpServiceRequestStatus =  m_dbConn.Select(sSQLSelectStatus);
											
											if(mpServiceRequestStatus.size() > 0)
											{
												for(Entry<Integer, Map<String, Object>> valueRequestsStatus : mpServiceRequestStatus.entrySet()) 
												{
													entry4.getValue().put("status", valueRequestsStatus.getValue().get("status"));
												}
											}
										}
									}
								}
							}
						}
					}
					
				}
			}
			
			
			//System.out.println("UnitID : "+iUnit);
			//System.out.println("Category Results : "+SqlSelect);
			//HashMap<Integer, Map<String, Object>> mpRequestCategory =  m_dbConn.Select(SqlSelect);
			/*int memberID= 0;
			if(mpRequestCategory.size() > 0)
			{
				
				for(Entry<Integer, Map<String, Object>> entry : mpRequestCategory.entrySet()) 
				{
					memberID = (Integer)  entry.getValue().get("member_id");
					System.out.println("memberID : "+memberID);
				}
			}*/
			///System.out.println("");
			//sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category as category_id,  servicerequest_category.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp ,u.unit_no,mm.owner_name,mof.mem_other_family_id from service_request inner join (select request_no, min(timestamp) as ts from service_request group by request_no) maxt on (service_request.request_no = maxt.request_no and service_request.timestamp = maxt.ts) JOIN  servicerequest_category ON  servicerequest_category.ID =  service_request.category join unit as u ON u.unit_id=service_request.unit_id join member_main as mm on mm.unit=servicerequest_category.unitID Join `mem_other_family` as mof on mof.member_id=mm.member_id WHERE service_request.visibility=1 AND mof. mem_other_family_id='231'  ORDER BY service_request.request_no  DESC";

			//sSqlServiceRequest = "select service_request.request_id, service_request.request_no, service_request.society_id, service_request.reportedby, DATE_FORMAT(service_request.dateofrequest,'%d %b, %Y') as dateofrequest, service_request.email, service_request.phone, service_request.priority, service_request.category as category_id,  servicerequest_category.category, service_request.summery, service_request.details, service_request.status, service_request.unit_id, service_request.visibility, service_request.img, DATE_FORMAT(CONVERT_TZ(service_request.timestamp,'+00:00','+05:30'),'%d %b, %Y %h:%i %p') as timestamp ,u.unit_no,mm.owner_name from service_request inner join (select request_no, min(timestamp) as ts from service_request group by request_no) maxt on (service_request.request_no = maxt.request_no and service_request.timestamp = maxt.ts) JOIN  servicerequest_category ON  servicerequest_category.ID =  service_request.category join unit as u ON u.unit_id=service_request.unit_id join member_main as mm on mm.unit=servicerequest_category.unitID  WHERE service_request.visibility=1 AND service_request.unit_id=" + iUnit + " ORDER BY service_request.request_no  DESC";
			//System.out.println("else condition ");	
			//System.out.println(sSqlServiceRequest);	
		}
		if(iFetchCount > 0)
		{
			//sSqlServiceRequest += " LIMIT " + iFetchCount;
		}
		
		//System.out.println(sSqlServiceRequest);
		if(!bAssignedToMe)
		{
			//System.out.println("Inside !Assigntome");
			mpServiceRequest =  m_dbConn.Select(sSqlServiceRequest);
			
			if(mpServiceRequest.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> valueRequests : mpServiceRequest.entrySet()) 
				{
					int lRequestNo = (int) valueRequests.getValue().get("request_no");
					
					String sSQLSelectStatus = "SELECT `status` from service_request where request_no = '" + lRequestNo + "' ORDER BY request_id DESC LIMIT 0,1";
					HashMap<Integer, Map<String, Object>> mpServiceRequestStatus =  m_dbConn.Select(sSQLSelectStatus);
					
					if(mpServiceRequestStatus.size() > 0)
					{
						for(Entry<Integer, Map<String, Object>> valueRequestsStatus : mpServiceRequestStatus.entrySet()) 
						{
							valueRequests.getValue().put("status", valueRequestsStatus.getValue().get("status"));
						}
					}
				} 
			}
		}
		else
		{
			
		}
		return mpServiceRequest;
	}

	public static int GetNextRequestNo()
	{
		//Get Max RequestNo
		String sSelectQuery = "select Max(request_no) from service_request";
		HashMap<Integer, Map<String, Object>> mpRequestNo =  m_dbConn.Select(sSelectQuery);		
		
		Map.Entry<Integer, Map<String, Object>> itrRequestNo = mpRequestNo.entrySet().iterator().next();
		//System.out.println("gap");	
		if(itrRequestNo.getValue().get("Max(request_no)") == null)
		{
			return  1;
		}
		else
		{
			int iMaxRequestNo = (int) itrRequestNo.getValue().get("Max(request_no)");
		
			//System.out.println("MaxSRNo <" + iMaxRequestNo + ">");	
		
			return iMaxRequestNo + 1;
		}
	}
	
	public static String GetTodayDate()
	{
        //get current timestamp
        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(System.currentTimeMillis());
        String sDateOfRequest = ourJavaTimestampObject.toString();
        //System.out.println("Date check <" + sDateOfRequest + ">");
        return sDateOfRequest;
	}
	
	public static long AddServiceRequest(int iUnitID, int iSocietyID, long lMappingID, String sReportedby, String sEmail, String sPhone, String sSummary, String sStatus, int iVisibility, String sCategory, String sPriority, String sImageFileName, String sDetails)
	{
		String sInsertQuery = "";
		
		//m_dbConn.BeginTransaction();	
		
		try {
			//Get Max RequestNo
			if(sPhone.length()<=0)
			{
				sPhone = getMobileFromEmail(iUnitID, sEmail);
			//	System.out.println("getMobileFromEmail sMobile <" + sPhone + ">");	
				
			}
			
			//Get Max RequestNo
			int iRequestNo = GetNextRequestNo();
			String sDateOfRequest = GetTodayDate();

			//Insert into mem_other_family
			sInsertQuery = "insert into service_request (`unit_id`,`society_id`,`request_no`,`reportedby`,`email`,`phone`,`summery`,`priority`,`category`,`img`,`details`,`status`,`visibility`, dateofrequest) values ";
			sInsertQuery = sInsertQuery + "(" + iUnitID + "," + iSocietyID + "," + iRequestNo + ",'" + sReportedby +  "','" + sEmail +  "','" + sPhone +  "','" + sSummary +  "','" + sPriority + "','" + sCategory + "','" + sImageFileName + "','" + sDetails  + "','" + sStatus + "'," + iVisibility + ",'" + sDateOfRequest + "')";

			//System.out.println(sInsertQuery);	
			long lServiceReqID = m_dbConn.Insert(sInsertQuery);
			//System.out.println("ServiceReqID<" + lServiceReqID  + ">");	
			
			if(lServiceReqID  == 0)
			{
				//m_dbConn.RollbackTransaction();
				return 0;
			}		
		
			//m_dbConn.EndTransaction();
			
			return lServiceReqID;
		}
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
		}
		
		return 0;
	}
	
	public static String getMobileFromEmail(int iUnitID, String sEmail)
	{
		String sMobile = "";
		String sSelectQuery = "Select * from member_main where Unit=" + iUnitID + " AND ownership_status = 1";
		//System.out.println(sSelectQuery);
		HashMap<Integer, Map<String, Object>> mpMapping = m_dbConn.Select(sSelectQuery);
//		System.out.println("Before mapping size");	
		int lsize = mpMapping.size();
	//	System.out.println("mapping size <" + lsize + ">");	
		Map.Entry<Integer, Map<String, Object>> itrMapping = mpMapping.entrySet().iterator().next();
		//System.out.println("iter");
		
		int member_id = (int) itrMapping.getValue().get("member_id");
		//System.out.println("mapping member_id <" + member_id + ">");	

		
		sSelectQuery = "Select * from mem_other_family where member_id=" + member_id + " AND other_email = '" + sEmail + "'";
		//System.out.println(sSelectQuery);
		HashMap<Integer, Map<String, Object>> mpMem_other_family = m_dbConn.Select(sSelectQuery);
//		System.out.println("Before mapping size");	
		if(mpMem_other_family.size() > 0)
		{
			//System.out.println("mapping size <" + lsize + ">");	
			Map.Entry<Integer, Map<String, Object>> itrMember_Other = mpMem_other_family.entrySet().iterator().next();
	//		System.out.println("iter");
			
			sMobile = (String) itrMember_Other.getValue().get("other_mobile");
			//System.out.println("mapping sMobile <" + sMobile + ">");	
		}
		else
		{
			sMobile = "0";
		}
		return sMobile;
	}
	
	public static long UpdateServiceRequestHistory(int iUnitID, int iSocietyID, long lMappingID, int iSR_ID, String sReportedby, String sEmail, String sPhone, String sSummary, String sStatus, int iVisibility, String sCategory, String sPriority, String sNewImage)
	{
		String sInsertQuery = "";
		
		//m_dbConn.BeginTransaction();	
		
		try {
			
			//Get Max RequestNo
			if(sPhone.length()<=0)
			{
				sPhone = getMobileFromEmail(iUnitID, sEmail);
				//System.out.println("getMobileFromEmail sMobile <" + sPhone + ">");	
				
			}
			String sDateOfRequest = "0000-00-00";
			String sDetails = "";
			//Insert into mem_other_family
			sInsertQuery = "insert into service_request (`unit_id`,`society_id`,`request_no`,`reportedby`,`email`,`phone`,`summery`,`priority`,`category`,`img`,`details`,`status`,`visibility`, dateofrequest) values ";
			sInsertQuery = sInsertQuery + "(" + iUnitID + "," + iSocietyID + "," + iSR_ID + ",'" + sReportedby +  "','" + sEmail +  "','" + sPhone +  "','" + sSummary +  "','" + sPriority + "','" + sCategory + "','" + sNewImage + "','" + sDetails + "','" + sStatus + "'," + iVisibility + ",'" + sDateOfRequest + "')";

			//System.out.println(sInsertQuery);	
			long lServiceReqID = m_dbConn.Insert(sInsertQuery);
			//System.out.println("ServiceReqID<" + lServiceReqID  + ">");	
			
			if(lServiceReqID  == 0)
			{
				//m_dbConn.RollbackTransaction();
				return 0;
			}		
		
			//m_dbConn.EndTransaction();
			
			return lServiceReqID;
		}
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
		}
		
		return 0;
	}

	public static HashMap<Integer, Map<String, Object>> GetStatusList()
	{
		String sSelectQuery = "select status_id, status from sr_status where status = 'Y'";	

		//System.out.println(sSelectQuery);	
		/******* fetching designations **********/
		HashMap<Integer, Map<String, Object>> resultStatusList =  m_dbConn.Select(sSelectQuery);
				
		return resultStatusList;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchSRCategory()
	{
		String sSelectQuery = "select ID, category from servicerequest_category where status = 'Y'";	

		//System.out.println(sSelectQuery);	
		/******* fetching designations **********/
		HashMap<Integer, Map<String, Object>> resultStatusList =  m_dbConn.Select(sSelectQuery);
				
		return resultStatusList;
	}
	public static HashMap<Integer, Map<String, Object>> mFetchAllServiceRequest(int iSocietyId,int iFetchCount,boolean bFetchExpired,int iUnitId)
	{
		
		String societydbName="";
		HashMap<Integer, Map<String, Object>> mpServiceSRequest = null;
		try
		{
			//m_dbConnRoot = new DbOperations(true,"");
			String sqlForSocietyDetails = "Select `dbname` from society where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> societyDetails = m_dbConnRoot.Select(sqlForSocietyDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet())
			{	
				societydbName = entry1.getValue().get("dbname").toString();
			}
			//System.out.println("dbName"+societydbName);
			String todayDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String sSqlServiceRequest = "";
			//m_dbConn = new DbOperations(false, societydbName);
			//sSqlEvents = "select events_id, DATE_FORMAT(events_date,'%d %b, %Y') as events_date, DATE_FORMAT(end_date,'%d %b, %Y') as end_date, events_title, events, TIME_FORMAT(event_time,'%h:%i:%s %p') as event_time,IF(end_date >= '"+ todayDate +"',0,1) as IsEventExpired,IF(Uploaded_file <> '',concat('http://way2society.com/Events/',Uploaded_file),'') as Link,events_url,event_type,event_charges from events where society_id=" +iSocietyId+"";
			sSqlServiceRequest = "select DISTINCT(sr.request_id),DATE_FORMAT(sr.reportedby,'%d %b, %Y') as reportedby,sr.email,sc.category,IF(sr.img <> '',concat('http://way2society.com/Events/',sr.img),'') as image,sr.dateofrequest,sr.phone,sr.priority,sr.summery from service_request sr,servicerequest_category sc where sr.society_id='"+iSocietyId+"' and sc.ID = sr.category ";
			//System.out.println("\nsql:"+sSqlEvents);
			if(iUnitId > 0)
			{
				sSqlServiceRequest += " and sr.unit_id = '"+iUnitId+"'";
			}

			//if(!bFetchExpired)
			//{
				//fetch events which are not expired
				//sSqlServiceRequest += " and dateofrequest >= '"+ todayDate +"' ";
			//}
			
			sSqlServiceRequest += " ORDER BY sr.request_id DESC";
			
			if(iFetchCount > 0)
			{
				sSqlServiceRequest += " LIMIT "+iFetchCount;
			}
			//System.out.println("\n\nSql:"+sSqlServiceRequest);
			mpServiceSRequest =  m_dbConn.Select(sSqlServiceRequest);
			//System.out.println("\n\nmpTasks :"+mpServiceSRequest);
		}
		catch(Exception e)
		{
			System.out.println("\nException occured: "+e.getStackTrace());
			HashMap rows = new HashMap<>();
			rows.put("message", e.getMessage());
			mpServiceSRequest = new HashMap<Integer, Map<String, Object>>();
			mpServiceSRequest.put(0,rows);
		}
		
		return mpServiceSRequest;
	}
}
