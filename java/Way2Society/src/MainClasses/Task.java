package MainClasses;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.google.common.collect.HashMultimap;

import CommonUtilityFunctions.MapUtility;

public class Task
{
	static DbOperations m_dbConn; 		//society46 by default
	static DbRootOperations m_dbConnRoot;
	static Utility m_Utility;
	//needed 2 DB, 'hostmjbt_society46' for tasklist & 'hostmjbt_societydb' for login (taskowner)

	
	public Task(DbOperations  m_dbConnObj, DbRootOperations  m_dbConnRootObj)
	{
		try 
		{
			m_dbConn = m_dbConnObj;
			m_dbConnRoot = m_dbConnRootObj;
			m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//public static HashMap<Integer, Map<String, Object>> addTask(int loginID){}
	
	public static HashMap<Integer, Map<String, Object>> mfetchTasksForMe(int loginID, int iFetchCount, boolean bFetchOpenOnly)
	{		
		HashMap<Integer, Map<String, Object>> mTasksForMe;

		String fetchQuery = "SELECT `current_mapping` FROM `login` where `login_id` = '"+loginID+"'";
//		System.out.println(fetchQuery);
		HashMap<Integer, Map<String, Object>> fetchResult = m_dbConnRoot.Select(fetchQuery);
		int current_mapping = 0;
		for(Entry<Integer, Map<String, Object>> entry1 : fetchResult.entrySet())
		{
			current_mapping =  (Integer)entry1.getValue().get("current_mapping");
			//System.out.println("fetchResult.Curr_mapping" + current_mapping);
		}
		
		//System.out.println("current_mapping" + current_mapping);
		if(current_mapping > 0)
		{
				//current_mapping = Integer.parseInt((fetchResult.get("0")).get("currentMapping").toString());
//				m_dbConn = new DbOperations(false,"hostmjbt_society46");
			
			String fetchTasks = "SELECT * FROM `tasklist` where `Task_Owner` = '"+current_mapping+"'";
			if(bFetchOpenOnly == true)
			{
				fetchTasks = fetchTasks + " and `Status` <=3";	
			}
			fetchTasks = fetchTasks + " ORDER BY  `TimeStamp` DESC ";
			if(iFetchCount > 0)
			{
				fetchTasks += " LIMIT " + iFetchCount;
			}

//			System.out.println(fetchTasks);
			mTasksForMe =  m_dbConn.Select(fetchTasks);
			//System.out.println("mTasksForMe : " + mTasksForMe);
			if(mTasksForMe.size() > 0)
			{
				//get raised_by name from societydb
				//m_dbConnRoot = new DbOperations(false,"hostmjbt_societydb");
				for(Entry<Integer, Map<String, Object>> entry1 : mTasksForMe.entrySet())
				{	
					//get raised_by name from societydb
					int raised_by = (Integer)entry1.getValue().get("RaisedBy");
					String sqlQuery = "SELECT `name` FROM `login` where `login_id` = '"+raised_by+"'";
					//System.out.println(sqlQuery);
					HashMap<Integer, Map<String, Object>> raisedByName = m_dbConnRoot.Select(sqlQuery);
					for(Entry<Integer, Map<String, Object>> entry2 : raisedByName.entrySet())
					{
						String name = entry2.getValue().get("name").toString();
						entry1.getValue().put("raised_by_name", name);
					}						
				}
			}
		}
		else
		{
			mTasksForMe =  new HashMap<Integer, Map<String, Object>> ();

			//ystem.out.println("In else part");
			
		}
		return mTasksForMe;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchTasksByMe(int loginID, int iFetchCount, boolean bFetchOpenOnly)
	{
		String fetchTasks = "SELECT * FROM `tasklist` where `RaisedBy` = '"+loginID+"'";
		if(bFetchOpenOnly == true)
		{
			fetchTasks = fetchTasks + " and `Status` <=3";	
		}
		fetchTasks = fetchTasks + " ORDER BY `TimeStamp` DESC ";
		
		if(iFetchCount > 0)
		{
			fetchTasks += " LIMIT " + iFetchCount;
		}
		System.out.println(fetchTasks);
		HashMap<Integer, Map<String, Object>> mTasksByMe =  m_dbConn.Select(fetchTasks);
		
		if(mTasksByMe.size() > 0)
		{
			//get task_owner name from societydb
			String sqlQuery = "SELECT `name`,`current_mapping` FROM `login` where `current_mapping` IN (";
			int sqlCnt= 0;
			for(Entry<Integer, Map<String, Object>> entry1 : mTasksByMe.entrySet())
			{
				sqlCnt++;
				int task_owner = (Integer)entry1.getValue().get("Task_Owner");
				sqlQuery += "'"+task_owner+"'";	
				if(sqlCnt < mTasksByMe.size()){
					sqlQuery += ",";
				}
			}	
			sqlQuery += ")";
			System.out.println(sqlQuery);
			HashMap<Integer, Map<String, Object>> taskOwnerName = m_dbConnRoot.Select(sqlQuery);
			//System.out.println(taskOwnerName);
			if(taskOwnerName.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> entry1 : mTasksByMe.entrySet())
				{
					for(Entry<Integer, Map<String, Object>> entry2 : taskOwnerName.entrySet())
					{
						int entry1_map_ip =  (Integer)entry1.getValue().get("Task_Owner");
						int entry2_map_id =  (Integer)entry2.getValue().get("current_mapping");
						if(entry1_map_ip==entry2_map_id)
						{ 
							entry1.getValue().put("task_owner_name", entry2.getValue().get("name").toString());
						}
					}
				}				
			}
		}
		return mTasksByMe;
	}
	// Fetch All Task in Society
	public static HashMap<Integer, Map<String, Object>> mfetchAllTask(int loginID, int iSocietyID)
	{
		String fetchTasks = "SELECT * FROM `tasklist`";
		
		System.out.println(fetchTasks);
		HashMap<Integer, Map<String, Object>> SelectAllTask =  m_dbConn.Select(fetchTasks);
		
		if(SelectAllTask.size() > 0)
		{
			//get task_owner name from societydb
			String sqlQuery = "SELECT `name`,`current_mapping` FROM `login` where `current_mapping` IN (";
			int sqlCnt= 0;
			for(Entry<Integer, Map<String, Object>> entry1 : SelectAllTask.entrySet())
			{
				sqlCnt++;
				int task_owner = (Integer)entry1.getValue().get("Task_Owner");
				sqlQuery += "'"+task_owner+"'";	
				if(sqlCnt < SelectAllTask.size()){
					sqlQuery += ",";
				}
			}	
			sqlQuery += ")";
			System.out.println(sqlQuery);
			HashMap<Integer, Map<String, Object>> taskOwnerName = m_dbConnRoot.Select(sqlQuery);
			//System.out.println(taskOwnerName);
			if(taskOwnerName.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> entry1 : SelectAllTask.entrySet())
				{
					for(Entry<Integer, Map<String, Object>> entry2 : taskOwnerName.entrySet())
					{
						int entry1_map_ip =  (Integer)entry1.getValue().get("Task_Owner");
						int entry2_map_id =  (Integer)entry2.getValue().get("current_mapping");
						if(entry1_map_ip==entry2_map_id)
						{ 
							entry1.getValue().put("task_owner_name", entry2.getValue().get("name").toString());
						}
					}
				}				
			}
		}
		return SelectAllTask;
	}
	/**
	 * @param lMappingID = assigned to
	 * @throws ClassNotFoundException 
	 */
	public static long AddTask(long lMappingID, int iLoginID, String sTitle, String sTask_desc, String sAttachment, String sDueDate, int iPriority , int iStatus, int iPercentCompleted) throws AddressException, MessagingException, ClassNotFoundException
	{
		//Insert into tasklist
		String sInsertQuery = "";
		sInsertQuery = "insert into tasklist (`Task_Owner`,`RaisedBy`,`Title`,`Description`,`Attachment`,`Priority`,`DueDate`,`Status`,`PercentCompleted`,`TaskType`,`TypeID`,`Role`,`Reminder`) values ";
		sInsertQuery = sInsertQuery + "('" + lMappingID + "','" + iLoginID + "','" + sTitle + "','" + sTask_desc +  "','" + sAttachment +  "','" + iPriority +  "','" + sDueDate +  "','" + iStatus + "','" + iPercentCompleted + "','1','1','1','1')";
		
		//System.out.println(sInsertQuery);	
		long lTaskID = m_dbConn.Insert(sInsertQuery);
		if(lTaskID > 0)
		{
			String societyDbName="";
			String sqlTogetAssignee="SELECT login.name,login.`member_id` FROM `mapping` join login on mapping.login_id=login.login_id where login.login_id='"+iLoginID+"'";
			//System.out.println("sqlTogetAssignee :"+sqlTogetAssignee);
			String sqltoGetTaskOwner="SELECT login.`name`,login.`member_id` FROM `mapping` join login on mapping.login_id=login.login_id where mapping.id='"+lMappingID+"'";
			//System.out.println("sqltoGetTaskOwner :"+sqltoGetTaskOwner);
			HashMap<Integer, Map<String, Object>> mAssignDetails = m_dbConnRoot.Select(sqlTogetAssignee);
			System.out.println("mAssignDetails :"+mAssignDetails);
			HashMap<Integer, Map<String, Object>> mTaskOwnerDetails = m_dbConnRoot.Select(sqltoGetTaskOwner);
			System.out.println("mTaskOwnerDetails :"+mTaskOwnerDetails);
			
			String sqlForDatabaseDetails = "SELECT * FROM `society` where society_id = '"+CommonBaseClass.getSocietyId()+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			//System.out.println("societyDbName :"+societyDbName);
			m_dbConn = new DbOperations(false, societyDbName);
			
			String sqlToGetSocietyDetails="select `email` from society";
			HashMap<Integer, Map<String, Object>> mSocietyDetails = m_dbConn.Select(sqlToGetSocietyDetails);
			String sSocietyEmail = "";
			String sTaskOwnerEmail = "",sAssigneeEmail="",sTaskOwnerName="",sAssigneeName="";
			for(Entry<Integer, Map<String, Object>> entry1 : mAssignDetails.entrySet())
			{
				sAssigneeEmail = entry1.getValue().get("member_id").toString();
				sAssigneeName = entry1.getValue().get("name").toString();
			}
			for(Entry<Integer, Map<String, Object>> entry1 : mTaskOwnerDetails.entrySet())
			{
				sTaskOwnerName = entry1.getValue().get("name").toString();
				sTaskOwnerEmail = entry1.getValue().get("member_id").toString();
			}
			for(Entry<Integer, Map<String, Object>> entry1 :mSocietyDetails.entrySet())
			{
				sSocietyEmail = entry1.getValue().get("email").toString();
			}
			String sPriority="";
			if(iPriority == 1)
			{
				sPriority = "Low";
			}
			else if(iPriority == 2)
			{
				sPriority = "Medium";
			}
			else if(iPriority == 3)
			{
				sPriority = "High";
			}
			else if(iPriority == 4)
			{
				sPriority = "Critical";
			}
			String strSubject="New Task : "+sTitle;
			String strMessage = "<table  style='border-collapse:collapse'><tr>"
        		+ "<tr><td >Dear Member,</td></tr>"
        		+ "<tr><td>This is to inform you that new task with the title <b>"+sTitle+"</b> has been created by <b>"+sAssigneeName+" assigned to "+sTaskOwnerName+"</b> </td></tr>"
        		+ "<tr><td><br></td></tr>"
        		+ "<tr><td>Description of the task : "+sTask_desc+"</td></tr>"
        		+ "<tr><td>Priority of the task : "+sPriority+"</td></tr>"
        		+ "<tr><td>Completion Percentage : "+iPercentCompleted+"% </td></tr>"
        		+ "<tr><td>Due date of the task : "+DateFunctionality.convertToStandardFormat(sDueDate)+"</td></tr>"
        		+ "<tr><td><br><br></td></tr>"
 //       		+ "<tr><td> If you have any questions, please contact society Manager or Secretary.</td></tr>"
        		+ "<tr><td><br></td></tr>"
         		+ "</tr></table>";
			//System.out.println("strMessage : "+strMessage);
			String result = EmailUtility.sendGenericMail(sAssigneeEmail, strSubject, strMessage);
			System.out.println("REsult :"+result);
			String result2 = EmailUtility.sendGenericMail(sTaskOwnerEmail, strSubject, strMessage);
			System.out.println("REsult :"+result2);
			String result3 = EmailUtility.sendGenericMail(sSocietyEmail, strSubject, strMessage);
			System.out.println("REsult :"+result3);
		}
		return lTaskID;
	}

	//	public static HashMap<Integer, Map<String, Object>>  UpdateTask(int iTaskID, String sTask_desc, String sAttachment, String sDueDate, int iStatus, int iPercentCompleted)
	public static long UpdateTask(int iTaskID, int iStatus, int iPercentCompleted,String sComment) throws AddressException, MessagingException
	{
		String societyDbName="";
		//update into tasklist
		String sUpdateQuery = "";
		sUpdateQuery = "UPDATE  `tasklist` SET  `Status` =  '"+iStatus+"',`PercentCompleted` =  '"+iPercentCompleted+"' WHERE  `tasklist`.`id` ='"+iTaskID+"'";
		
		// update with other parameters too
//			sUpdateQuery = "UPDATE  `tasklist` SET  `Description` =  '"+sTask_desc+"',`Attachment` =  '"+sAttachment+"',`DueDate` =  '"+sDueDate+"',`Status` =  '"+iStatus+"',`PercentCompleted` =  '"+iPercentCompleted+"' WHERE  `tasklist`.`id` =  '"+iPercentCompleted+"'";
		
		System.out.println(sUpdateQuery);	
		long lUpdateID = m_dbConn.Update(sUpdateQuery);
		System.out.println("lUpdateID<" + lUpdateID + ">");	
		if(lUpdateID > 0)
		{
			String sqlForTask = "Select * from tasklist where id='"+iTaskID+"'";
			HashMap<Integer, Map<String, Object>> mTaskDetails = m_dbConn.Select(sqlForTask);
			String sTitle="",sDes="",sDueDate="";
			int iTaskOwner=0,iRaisedBy=0,iPriority=0,iPercentage=0;
			for(Entry<Integer, Map<String, Object>> entry1 : mTaskDetails.entrySet())
			{
				sTitle = entry1.getValue().get("Title").toString();
				sDes = entry1.getValue().get("Description").toString();
				sDueDate = entry1.getValue().get("DueDate").toString();
				iTaskOwner = Integer.parseInt(entry1.getValue().get("Task_Owner").toString());
				iRaisedBy = Integer.parseInt(entry1.getValue().get("RaisedBy").toString());
				iPriority = Integer.parseInt(entry1.getValue().get("Priority").toString());
				iPercentage = Integer.parseInt(entry1.getValue().get("PercentCompleted").toString());
			}
			String sqlTogetAssignee="SELECT login.name,login.`member_id` FROM `mapping` join login on mapping.login_id=login.login_id where login.login_id='"+iRaisedBy+"'";
			//System.out.println("sqlTogetAssignee :"+sqlTogetAssignee);
			String sqltoGetTaskOwner="SELECT login.`name`,login.`member_id` FROM `mapping` join login on mapping.login_id=login.login_id where mapping.id='"+iTaskOwner+"'";
			//System.out.println("sqltoGetTaskOwner :"+sqltoGetTaskOwner);
			HashMap<Integer, Map<String, Object>> mAssignDetails = m_dbConnRoot.Select(sqlTogetAssignee);
			//System.out.println("mAssignDetails :"+mAssignDetails);
			HashMap<Integer, Map<String, Object>> mTaskOwnerDetails = m_dbConnRoot.Select(sqltoGetTaskOwner);
			//System.out.println("mTaskOwnerDetails :"+mTaskOwnerDetails);
			String sqlForDatabaseDetails = "SELECT * FROM `society` where society_id = '"+CommonBaseClass.getSocietyId()+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			String sTaskOwnerEmail = "",sAssigneeEmail="",sTaskOwnerName="",sAssigneeName="",sSocietyEmail="";
			for(Entry<Integer, Map<String, Object>> entry1 : mAssignDetails.entrySet())
			{
				sAssigneeEmail = entry1.getValue().get("member_id").toString();
				sAssigneeName = entry1.getValue().get("name").toString();
			}
			for(Entry<Integer, Map<String, Object>> entry1 : mTaskOwnerDetails.entrySet())
			{
				sTaskOwnerName = entry1.getValue().get("name").toString();
				sTaskOwnerEmail = entry1.getValue().get("member_id").toString();
			}
			String sqlToGetSocietyDetails="select `email` from society";
			HashMap<Integer, Map<String, Object>> mSocietyDetails = m_dbConn.Select(sqlToGetSocietyDetails);
			for(Entry<Integer, Map<String, Object>> entry1 :mSocietyDetails.entrySet())
			{
				sSocietyEmail = entry1.getValue().get("email").toString();
			}
			String sPriority="";
			if(iPriority == 1)
			{
				sPriority = "Low";
			}
			else if(iPriority == 2)
			{
				sPriority = "Medium";
			}
			else if(iPriority == 3)
			{
				sPriority = "High";
			}
			else if(iPriority == 4)
			{
				sPriority = "Critical";
			}
			String strSubject="Task Updated : "+sTitle;
			String strMessage = "<table  style='border-collapse:collapse'><tr>"
        		+ "<tr><td >Dear Member,</td></tr>"
        		+ "<tr><td>This is to inform you that task with the title <b>"+sTitle+"</b> has been updated by <b>"+sAssigneeName+"</b> assigned to <b>"+sTaskOwnerName+"</b></td></tr>"
        		+ "<tr><td><br></td></tr>"
        		+ "<tr><td>Description of the task : "+sDes+"</td></tr>"
        		+ "<tr><td>Priority of the task : "+sPriority+"</td></tr>"
        		+ "<tr><td>Completion Percentage : "+iPercentCompleted+"% </td></tr>"
        		+ "<tr><td>Due date of the task : "+DateFunctionality.convertToStandardFormat(sDueDate)+"</td></tr>"
        		+ "<tr><td><br>Update : "+sComment+" </td></tr>"
        		+ "<tr><td><br><br></td></tr>"
        		//+ "<tr><td> If you have any questions, please contact society Manager or Secretary.</td></tr>"
        		//+ "<tr><td><br></td></tr>"
        		//+ "<tr><td>From </td></tr>"
        		//+ "<tr><td>Managing Committee.</td></tr>"
        		+ "</table>";
			//System.out.println("strMessage : "+strMessage);
			String result = EmailUtility.sendGenericMail(sAssigneeEmail, strSubject, strMessage);
			System.out.println("REsult :"+result);
			String result2 = EmailUtility.sendGenericMail(sTaskOwnerEmail, strSubject, strMessage);
			System.out.println("REsult :"+result2);
			String result3 = EmailUtility.sendGenericMail(sSocietyEmail, strSubject, strMessage);
			System.out.println("REsult :"+result3);
			//System.out.println("REsult :"+result);
		}	
		return lUpdateID;
	}
	
	public static long UpdateTaskComment(int iTaskID, int iLoginID, String sComment)
	{
		//update into tasklist
		String sUpdateQuery = "";
		//sUpdateQuery = "UPDATE  `tasklist` SET  `Status` =  '"+iStatus+"',`PercentCompleted` =  '"+iPercentCompleted+"' WHERE  `tasklist`.`id` ='"+iTaskID+"'";
		
		// update with other parameters too
//			sUpdateQuery = "UPDATE  `tasklist` SET  `Description` =  '"+sTask_desc+"',`Attachment` =  '"+sAttachment+"',`DueDate` =  '"+sDueDate+"',`Status` =  '"+iStatus+"',`PercentCompleted` =  '"+iPercentCompleted+"' WHERE  `tasklist`.`id` =  '"+iPercentCompleted+"'";
		
		System.out.println(sUpdateQuery);	
		long lUpdateID = 0;
		//lUpdateID = m_dbConn.Update(sUpdateQuery);
		System.out.println("lUpdateID<" + lUpdateID + ">");	
			
		return lUpdateID;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchTaskComments (int iTaskID)
	{
		//update into tasklist
		String sFetchQuery = "";
		//sFetchQuery =  = "SELECT login.current_mapping as mapping_id, CONCAT( CONCAT( mapping.desc,  ' - ' ) , login.name ) AS mapping_name FROM `mapping` JOIN login ON mapping.login_id = login.login_id WHERE mapping.society_id =  '"+iSocietyID+"' AND mapping.role!='Member' AND login.`current_mapping` > 0";
		System.out.println(sFetchQuery);
		HashMap<Integer, Map<String, Object>> mTaskComments =  m_dbConnRoot.Select(sFetchQuery);
		//removing rows of mapping_id as 0
		return mTaskComments;
	}
	
	
	public static HashMap<Integer, Map<String, Object>> mfetchAssignMember(int iSocietyID)
	{
//			String sFetchQuery = "SELECT mapping.id as mapping_id, CONCAT( CONCAT( mapping.desc,  ' - ' ) , login.name ) AS mapping_name FROM `mapping` JOIN login ON mapping.login_id = login.login_id WHERE mapping.society_id =  '"+iSocietyID+"'";
		String sFetchQuery = "SELECT login.current_mapping as mapping_id, CONCAT( CONCAT( mapping.desc,  ' - ' ) , login.name ) AS mapping_name FROM `mapping` JOIN login ON mapping.login_id = login.login_id WHERE mapping.society_id =  '"+iSocietyID+"' AND mapping.role!='Member' AND login.`current_mapping` > 0";
		System.out.println(sFetchQuery);
		HashMap<Integer, Map<String, Object>> mAssignedMembers =  m_dbConnRoot.Select(sFetchQuery);
		//removing rows of mapping_id as 0
		if(mAssignedMembers.size() > 0 )
		{
			for(Iterator<HashMap.Entry<Integer, Map<String, Object>>>it = mAssignedMembers.entrySet().iterator();it.hasNext();){
				HashMap.Entry<Integer, Map<String, Object>> entry = it.next();
			     if ((Integer)entry.getValue().get("mapping_id") == 0) {
			          it.remove();
			     }
			 }
		}
		return mAssignedMembers;
	}
	

}