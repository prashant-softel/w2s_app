package MainClasses;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import CommonUtilityFunctions.MapUtility;

public class Comments extends CommonBaseClass
{
	static DbOperations m_dbConn; 		//society46 by default
	static DbOperations m_dbConnRoot;
	static Utility m_Utility;

	public Comments(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		// TODO Auto-generated constructor stub
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_dbConn = CommonBaseClass.m_objDbOperations;
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	//public static HashMap<Integer, Map<String, Object>> addTask(int loginID){}
	/*
	 * commentType = {1=task; 2=approval; 3=meeting}
	 * */
	public static HashMap<Integer, Map<String, Object>> mfetchComments(int commentType, int refID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			String fetchComments = "SELECT c.Id, c.Comment, c.PostedBy, c.TimeStamp from comments as c where CType = '"+commentType+"' and CRefId = '"+refID+"' and Status = 'Y' ORDER BY c.`TimeStamp` DESC";
			//System.out.println(fetchComments);
			HashMap<Integer, Map<String, Object>> mCommentsList =  m_dbConn.Select(fetchComments);
			//System.out.println(mCommentsList);
			
			if(mCommentsList.size() > 0){
				//get postedBy name from societydb
				m_dbConnRoot = new DbOperations(false,"hostmjbt_societydb");
				String sqlQuery = "SELECT `name`,`login_id` FROM `login` where `login_id` IN (";
				int sqlCnt= 0;
				for(Entry<Integer, Map<String, Object>> entry1 : mCommentsList.entrySet()){
					sqlCnt++;
					int postedBy = (Integer)entry1.getValue().get("PostedBy");
					sqlQuery += "'"+postedBy+"'";	
					if(sqlCnt < mCommentsList.size()){
						sqlQuery += ",";
					}
				}	
				sqlQuery += ")";
				//System.out.println(sqlQuery);
				HashMap<Integer, Map<String, Object>> postedByName = m_dbConnRoot.Select(sqlQuery);
				//System.out.println(taskOwnerName);
				if(postedByName.size() > 0){
					for(Entry<Integer, Map<String, Object>> entry1 : mCommentsList.entrySet()){
						for(Entry<Integer, Map<String, Object>> entry2 : postedByName.entrySet()){
							int entry1_map_ip =  (Integer)entry1.getValue().get("PostedBy");
							int entry2_map_id =  (Integer)entry2.getValue().get("login_id");
							if(entry1_map_ip==entry2_map_id){ 
								entry1.getValue().put("posted_by_name", entry2.getValue().get("name").toString());
							}
						}
					}
					rows.put("success",1);
					rows2.put("comments",MapUtility.HashMaptoList(mCommentsList));
					rows.put("response",rows2);
				}
				else{
					rows.put("success",0); 
					rows2.put("comments","Comment Owner could not be found");
					rows.put("response",rows2);
				}	
			}
			else {
				rows.put("success",0); 
				rows2.put("comments","Empty");
				rows.put("response",rows2);
			}
		}
		catch(ClassNotFoundException e)
		{
			 rows.put("success",0);
			 rows2.put("message", e.getMessage());
			 rows.put("response",rows2);
		}		
		return rows;
	}
	/**
	 * @param iCtype = comment type
	 */
	public static  HashMap<Integer, Map<String, Object>>  AddComment(int iCtype, int iRefID, String sComment, int iLoginID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try {
			//Insert into comments
			String sInsertQuery = "";
			//sInsertQuery = "INSERT INTO `comments` (`CType`, `CRefId`, `Comment`, `PostedBy`, `Status`) VALUES ";
			//sInsertQuery = sInsertQuery + "('" + iCtype + "','" + iRefID + "','" + sComment + "','" + iLoginID +  "','Y')";
			//System.out.println("before add Comment");
			sInsertQuery = "INSERT INTO `comments` (`CType`, `CRefId`, `Comment`, `PostedBy`, `Status`) VALUES ('" + iCtype + "','" + iRefID + "','" + sComment + "','" + iLoginID +  "','Y')";
			//System.out.println(sInsertQuery);	
			long lServiceReqID = m_dbConn.Insert(sInsertQuery);
			//System.out.println("ServiceReqID<" + lServiceReqID  + ">");	
			if(lServiceReqID > 0){
				//add task
				 rows2.put("new_id",lServiceReqID);
				 rows2.put("message","Comment Created Successfully");
				 rows.put("response",rows2);
				 rows.put("success",1);
			}
			else{
				//m_dbConn.RollbackTransaction();
//				 rows2.put("new_id",lServiceReqID);
				 rows2.put("message","Unable To Create Comment. Please try again");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
		//System.out.println("rows" +rows);
	    return rows;	
	}
//	delete comment within 5 mins
	public static HashMap<Integer, Map<String, Object>>  DeleteComment(int iLoginID, int iCommentID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		final int setTimeDiff = 5;
		
		try {			
//			m_dbConn.BeginTransaction();	
			//check time diff & delete if timediff <5 (mins)
			String fetchTime = "SELECT TIMESTAMPDIFF( MINUTE , ( SELECT  `TimeStamp` FROM  `comments` WHERE  `Id` =  '"+iCommentID+"' AND  `status` =  'Y'), NOW( ) ) AS timeDiff";
//System.out.println("\n"+fetchTime);
			HashMap<Integer, Map<String, Object>> commentTimeDiff =  m_dbConn.Select(fetchTime); 		//returns time in mins
			int timeDiff = 99;
			for(Entry<Integer, Map<String, Object>> entry1 : commentTimeDiff.entrySet()){
				if(entry1.getValue().get("timeDiff") != null)
					timeDiff = Integer.parseInt(entry1.getValue().get("timeDiff").toString());
				else 
					timeDiff = 88; 
			}
	//			System.out.println("temp :\n" +temp+"\n"+temp.toLocalDateTime()+"\n"+temp.);
				//System.out.println("tempiff : "+timeDiff );
				if(timeDiff <= setTimeDiff)
				{
					String sUpdateQuery = "";
					//Timestamp time = new Timestamp(System.currentTimeMillis());
					sUpdateQuery = "UPDATE comments SET Status = 'N', DeletedBy = '"+iLoginID+"', DeleteTimeStamp = NOW() where Id = '"+iCommentID+"'";
					//System.out.println(sUpdateQuery);	
					long lUpdateID = m_dbConn.Update(sUpdateQuery);
					//System.out.println("lUpdateID<" + lUpdateID + ">");	
					
					if(lUpdateID == 0){
						//m_dbConn.RollbackTransaction();
						 rows2.put("message","Unable to delete comment.");
						 rows.put("response",rows2);
						 rows.put("success",0);
					}
					else{
						 rows2.put("message","Comment deleted Successfully");
						 rows.put("response",rows2);
						 rows.put("success",1);				
					} 
				}
				else{
					 rows2.put("message","Comment deletion TimeOut");
					 rows.put("response",rows2);
					 rows.put("success",0);				
				}
		}
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
		return rows2;	    	
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String token = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQI1By2w6EZabMQYAzmQGezdBP7CV8cUW-EfZhb4jZmFy0iVon2Pwq7GjXbeyRum6jJMl0bALii9RN5JxhAwuZcvIukLS04hM_2iIRgcfSFJzQ";
		String tkey = "cRfDdp8rhum53ppy8b6uIJKx1_sCITmnCJqqfocSOwu-_sYwI9nbeveBcpsjBjFcS2xWu6LaMenMXQlKhIXNA7b0YIgE6JgZ0RqahKbEDzsdC2GpAln_c0hRqrM1uLT0jEQ_llo6rficXCbnjAi8Kea1jEDnIztd_GMTvrbkhQE";
		Comments obj = new Comments(token,true, tkey);
		HashMap rows;
		rows = mfetchComments(1, 3);
		//rows = DeleteComment(Integer.parseInt(getDecryptedTokenMap().get("id")),18);
		//rows = AddComment(1, 3, "Status updated :In progress | Progress updated : 21% | Comment : test.", Integer.parseInt(getDecryptedTokenMap().get("id")));
		//get current timestamp
//        Calendar calendar = Calendar.getInstance();
//        Timestamp ourJavaTimestampObject = new Timestamp(System.currentTimeMillis());
//        System.out.println("\n"+calendar);
//		Time time = new Time(System.currentTimeMillis());
//        System.out.println(time);
		System.out.println(rows);	
	}

}