package MainClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.Iterator;
import com.google.common.collect.HashMultimap;


import CommonUtilityFunctions.MapUtility;
import MainClasses.Task;

public class ViewTasks extends CommonBaseClass 
{
	static Task m_Task;
	
	public ViewTasks(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_Task = new Task(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	
	public static HashMap<Integer, Map<String, Object>> mfetchTasksForMe(int loginID, int iFetchCount)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		try 
		{	
			boolean bFetchOpenOnly = false;
			HashMap<Integer, Map<String, Object>> mTasksByMe = m_Task.mfetchTasksForMe(loginID, iFetchCount, bFetchOpenOnly);

			if(mTasksByMe.size() > 0)
			{
				rows.put("success",1);
				 rows2.put("task",MapUtility.HashMaptoList(mTasksByMe));
				 rows.put("response",rows2);
			}
			else 
			{
				rows.put("success",0); 
				rows2.put("task","Tasks empty");
				rows.put("response",rows2);
			}
			//String fetchTasks = "SELECT * FROM `tasklist` where `Task_Owner ` = '"+current_mapping+"'";
		} 
		catch (Exception e) {
			 rows.put("success",0);
			 rows2.put("message", "Exception:" + e);
			 rows.put("response",rows2);
		}

		return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchTasksByMe(int loginID, int iFetchCount)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			boolean bFetchOpenOnly = false;
			HashMap<Integer, Map<String, Object>> mTasksByMe = m_Task.mfetchTasksByMe(loginID, iFetchCount, bFetchOpenOnly);

			if(mTasksByMe.size() > 0)
			{
				rows.put("success",1);
				rows2.put("task",MapUtility.HashMaptoList(mTasksByMe));
				rows.put("response",rows2);
			}
			else 
			{
				rows.put("success",0); 
				rows2.put("task","Empty");
				rows.put("response",rows2);
			}
		}
		catch (Exception e)
		{
			 rows.put("success",0);
			 rows2.put("message", e.getMessage());
			 rows.put("response",rows2);
		}		
		return rows;
	}
	// Fetch All Task using SOcietyId And Login ID 
	public static HashMap<Integer, Map<String, Object>> mfetchAllTask(int loginID, int iSocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			boolean bFetchOpenOnly = false;
			HashMap<Integer, Map<String, Object>> mTasksByMe = m_Task.mfetchAllTask(loginID, iSocietyID);

			if(mTasksByMe.size() > 0)
			{
				rows.put("success",1);
				rows2.put("task",MapUtility.HashMaptoList(mTasksByMe));
				rows.put("response",rows2);
			}
			else 
			{
				rows.put("success",0); 
				rows2.put("task","Empty");
				rows.put("response",rows2);
			}
		}
		catch (Exception e)
		{
			 rows.put("success",0);
			 rows2.put("message", e.getMessage());
			 rows.put("response",rows2);
		}		
		return rows;
	}
	public static  HashMap<Integer, Map<String, Object>>  AddTask(long lMappingID, int iLoginID, String sTitle, String sTask_desc, String sAttachment, String sDueDate, int iPriority , int iStatus, int iPercentCompleted)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try {
			
			long lTaskID =  m_Task.AddTask(lMappingID, iLoginID, sTitle, sTask_desc, sAttachment, sDueDate, iPriority , iStatus, iPercentCompleted);
			if(lTaskID > 0){
				//add task
				 rows2.put("new_id",lTaskID);
				 rows2.put("message","Task Created Successfully");
				 rows.put("response",rows2);
				 rows.put("success",1);
			}
			else{
				 rows2.put("new_id",lTaskID);
				 rows2.put("message","Unable To Create Task. Please try again");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
		}
		catch (Exception e) {
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
	    return rows;	
	}
//	public static HashMap<Integer, Map<String, Object>>  UpdateTask(int iTaskID, String sTask_desc, String sAttachment, String sDueDate, int iStatus, int iPercentCompleted)
	public static HashMap<Integer, Map<String, Object>>  UpdateTask(int iTaskID, int iStatus, int iPercentCompleted,String sComment)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		try {
			long lUpdateID =  m_Task.UpdateTask(iTaskID, iStatus, iPercentCompleted,sComment);
			System.out.println("lUpdateID<" + lUpdateID + ">");	
			
			if(lUpdateID == 0){
				//m_dbConn.RollbackTransaction();
				 rows2.put("message","Unable To update Task. Please try again");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
			else{
				//update task
				 rows2.put("message","Task Updated Successfully");
				 rows.put("response",rows2);
				 rows.put("success",1);
				
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
	    return rows;	
	}
	
	public static HashMap<Integer, Map<String, Object>>  UpdateTaskComment(int iTaskID, int iLoginID, String sComment)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		try {
			long lUpdateID =  m_Task.UpdateTaskComment(iTaskID, iLoginID, sComment);
			System.out.println("lUpdateID<" + lUpdateID + ">");	
			
			if(lUpdateID == 0){
				//m_dbConn.RollbackTransaction();
				 rows2.put("message","Unable To update Task Comment. Please try again");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
			else{
				//update task
				 rows2.put("message","Task Comment Updated Successfully");
				 rows.put("response",rows2);
				 rows.put("success",1);
				
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//m_dbConn.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
	    return rows;	
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchAssignMember(int iSocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			HashMap<Integer, Map<String, Object>> mAssignedMembers =  m_Task.mfetchAssignMember(iSocietyID);
			System.out.println(mAssignedMembers);

			if(mAssignedMembers.size() > 0)
			{
				rows.put("success",1);
				rows2.put("assignedMembers",MapUtility.HashMaptoList(mAssignedMembers));
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success",0); 
				rows2.put("message","Members not found in society : "+iSocietyID);
				rows.put("response",rows2);
			}
		}
		catch(Exception e){
			rows.put("success",0);
			rows2.put("message", e);
			rows.put("response",rows2);
		}
		return rows;
	}
	
	
	public static void main(String[] args) throws Exception
	{

		String sToken ="LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM7GOAgQUY2TkjnMXxAAi6d21jJI4DEbyxzK4P9EBjtdVbLTrIRIqo3XroOm0IuVNj93IFiERM4zCMY61OOsCSM6";
		String sTkey = "JMF-utA68iGKoKeLb98wJrzPQHkrZANI68928KZaJX0tFBD9A6a1hkLTfz_5KXHIWgnheoUK2tkpH3GbtoPLexwOWIVar6NcCabKEJgKrfhfqAjzOaZAC-rM5bYgpDJb7wV18qfJGbejg62AWwuCz7BAbzCJ8UHccnTg9NgNd2k";
		sTkey = "C8FQiS6zBoO2HW5X_1VksPQLRDY-Odc8AcoC_nTvZCUNiIef1drBC4X4aDQrG3MMcAlVxIwnjnNQQjTG2v6Ja7ToEQC7xuU9CQ9yRixnDOdLlR69tzr_6KGJ4s6HKJZU_ZKxEZvn0zkm5-mjF5Mc5PAxA_DGMl-fd--ZKQDIWSQ";
		sToken = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQI6Yg08ysk-HQELgJW36dHO2n-MfMt3QeOeD9bQwoszP9KIU5bxuJGF2WcNEnWyVGEiWkzSIYaMG1NMvCn0lg5XI1qni_6KbtjinhneUKfyHw";
		
		ViewTasks obj = new ViewTasks(sToken, true, sTkey);
		int iLoginID = 169;
		HashMap rows;
		 //rows =  obj.mfetchTasksByMe(iLoginID);
		 //rows =  obj.mfetchTasksForMe(iLoginID);
		 
		 
		//rows =  obj.mfetchTasksForMe(169);
		//System.out.println(obj.convert(rows));
//		rows = obj.mfetchAssignMemb`er(Integer.parseInt(((String) getDecryptedTokenMap().get("society_id"))));
//		rows = obj.AddTask(11910, 169, "Task232", "task232 Desc", "dsdsd.png", "2018-06-09", 1, 2, 45);
//		rows = obj.UpdateTask(24, 3, 64);
		
		
	//	System.out.println("\n"+rows);	
	}
}


