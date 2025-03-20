package MainClasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import CommonUtilityFunctions.CalendarFunctions;
import MainClasses.DbOperations.*;


public class Polls
{
	//static DbOperations m_dbConnRoot;
	static DbOperations m_dbConn;
	//private static HashMap<Integer, Map<String, Object>> Optionlist;
	static DbRootOperations m_dbConnRoot;
	private static CommonUtilityFunctions.CalendarFunctions m_CalendarFunctions;
	
	public Polls(DbOperations m_dbConnObj, DbRootOperations m_dbConnRootObj)
	{
		m_dbConn = m_dbConnObj;
		m_dbConnRoot = m_dbConnRootObj;
		m_CalendarFunctions = new CalendarFunctions();
	}
	
	
	
		public static  HashMap<Integer, Map<String, Object>> mfetchActivePolls(int iUnitId,int iSocietyID)
			{
			
			String sSqlServey = "";
			//and a.end_date<=CURDATE()
			//sSqlServey="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,a.poll_status,a.allow_vote,b.society_id,a.additional_content FROM poll_question as a join soc_group as b on a.group_id=b.group_id WHERE a.group_id = b.group_id and b.society_id='"+iSocietyID+"' and CURDATE() between a.start_date and a.end_date";
			sSqlServey="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,a.poll_status,a.allow_vote,b.society_id,a.additional_content,a.comment_flag,a.comment_question FROM poll_question as a join soc_group as b on a.group_id=b.group_id WHERE a.group_id = b.group_id and b.society_id='"+iSocietyID+"' and CURDATE() between a.start_date and a.end_date ORDER BY DATE(a.end_date) DESC, a.poll_id DESC";
			//System.out.println("123"+sSqlServey);
			HashMap<Integer, Map<String, Object>> resultsociety =  m_dbConnRoot.Select(sSqlServey);
			
			return resultsociety;
			}
		
		public static  HashMap<Integer, Map<String, Object>> mfetchInactivePolls(int iUnitId,int iSocietyID)
			{
			String sSqlServey = "";
			//and a.end_date>CURDATE()
			sSqlServey="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,a.poll_status,a.allow_vote,b.society_id,a.additional_content,a.comment_flag,a.comment_question FROM poll_question as a join soc_group as b on a.group_id=b.group_id WHERE a.group_id = b.group_id and b.society_id='"+iSocietyID+"' and CURDATE() > a.end_date ORDER BY DATE(a.end_date) DESC, a.poll_id DESC";
			//sSqlServey="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,a.poll_status,a.allow_vote,b.society_id,a.additional_content FROM poll_question as a join soc_group as b on a.group_id=b.group_id WHERE a.group_id = b.group_id and b.society_id='"+iSocietyID+"' and CURDATE() between a.start_date and a.end_date ORDER BY DATE(a.end_date) DESC, a.poll_id DESC";
			//System.out.println("Expire"+sSqlServey);
			HashMap<Integer, Map<String, Object>> resultsociety =  m_dbConnRoot.Select(sSqlServey);
			return resultsociety;
			}
		
		public static  HashMap<Integer, Map<String, Object>> mfetchUserVote(int iUnitId,int iGroupId,int iSocietyID,int iPollId)
			{
			String sSqlServey = "";
			//sSqlServey="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,a.poll_status,a.allow_vote,a.additional_content,b.society_id,c.option_id,c.vote_id FROM poll_question as a join soc_group as b left join poll_vote as c on a.group_id=b.group_id WHERE a.group_id = b.group_id and b.society_id='"+iSocietyID+"' and c.unit_id='"+iUnitId+"' and c.isValid=1 Group by group_id";
			sSqlServey="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,a.poll_status,a.allow_vote,a.additional_content,b.society_id, d.vote_id, d.option_id,c.options as yourvote,d.mem_comment as yourReview FROM poll_question as a left join soc_group as b on a.group_id=b.group_id left join poll_option as c ON c.poll_id = '"+iPollId+"' left join poll_vote as d on d.option_id = c.option_id and d.unit_id='"+iUnitId+"' WHERE a.group_id = b.group_id and b.society_id='"+iSocietyID+"' and d.isValid = 1 and a.poll_id = '"+iPollId+"'";
			//sSqlServey="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,a.poll_status,a.allow_vote,a.additional_content ,b.options,b.counter,b.option_id FROM poll_question as a join poll_option as b on a.poll_id=b.poll_id WHERE a.poll_id = '"+iPollId+"'";
			//System.out.println(sSqlServey);
			HashMap<Integer, Map<String, Object>> resultsociety =  m_dbConnRoot.Select(sSqlServey);
			return resultsociety;
			}
		
		public static HashMap<Integer, Map<String, Object>> InsertUserVote(int iOptionId, int iLoginId,int iSocietyId, int iUnitId,int iPollId,String MemReview)
		{
			String sSqlServey = "";
			sSqlServey="INSERT into poll_vote(option_id,login_id,society_id,unit_id,member_id,mem_comment) VALUES ('"+iOptionId+"','"+iLoginId+"','"+iSocietyId+"','"+iUnitId+"','"+0+"','"+MemReview+"')";
			long resultsociety =  m_dbConnRoot.Insert(sSqlServey);
			String sSqlServey1 = "UPDATE poll_option set counter=counter+1 where option_id='"+iOptionId+"' and poll_id='"+iPollId+"'";
			long resultsociety1 =  m_dbConnRoot.Update(sSqlServey1);
			Map<String, Object> m1=new HashMap<>();
			m1.put((resultsociety+resultsociety1)+"",(resultsociety+resultsociety1)+"");
			HashMap<Integer, Map<String, Object>> result = new HashMap<>();
			result.put(1,m1);
			return result;
		}
		
		public static HashMap<Integer, Map<String, Object>> UpdateUserVote(int iOldOptionId,int iOptionId, int iLoginId,int iSocietyId, int iUnitId,int iPollId ,String MemReview)
			{
			String sSqlServey = "";
			sSqlServey="UPDATE poll_vote set Isvalid='0' where option_id='"+iOldOptionId+"' and unit_id='"+iUnitId+"' and society_id='"+iSocietyId+"' ";
			long resultsociety =  m_dbConnRoot.Update(sSqlServey);
			
			String sSqlServey1="INSERT into poll_vote(option_id,login_id,society_id,unit_id,member_id,mem_comment) VALUES ('"+iOptionId+"','"+iLoginId+"','"+iSocietyId+"','"+iUnitId+"','"+0+"','"+MemReview+"')";
			long resultsociety1 =  m_dbConnRoot.Insert(sSqlServey1);
			
			String sqlquery = "select counter from poll_option where option_id='"+iOldOptionId+"' and poll_id='"+iPollId+"'";
			HashMap<Integer, Map<String, Object>> resultcounter =  m_dbConnRoot.Select(sqlquery);
			int counter = Integer.valueOf(resultcounter.get(0).get("counter").toString());
			if(counter != 0)
			{
			String sSqlServey2 = "UPDATE poll_option set counter=(counter-1) where option_id='"+iOldOptionId+"' and poll_id='"+iPollId+"'";
			long resultsociety2 =  m_dbConnRoot.Update(sSqlServey2);
			}
			//System.out.println(sSqlServey2);
			String sSqlServey3 = "UPDATE poll_option set counter=(counter+1) where option_id='"+iOptionId+"' and poll_id='"+iPollId+"'";
			long resultsociety3 =  m_dbConnRoot.Update(sSqlServey3);
			//System.out.println(sSqlServey3);
			Map<String, Object> m1=new HashMap<>();
			m1.put((resultsociety+resultsociety1+resultsociety3)+"",(resultsociety+resultsociety1+resultsociety3)+"");
			HashMap<Integer, Map<String, Object>> result = new HashMap<>();
			result.put(1,m1);
			return result;
			}
		
		public static HashMap<Integer, Map<String, Object>> mfetchVotes(int iUnitId,int iGroupId, int iSocietyID,int iPollId, int iOptionId, boolean bIsFetchLatest)
		{
		//	System.out.println("helllo");
		//HashMap<Integer, Map<String, Object>> mServey = null;
		String sSqlServey = "";
		String result="";
		HashMap rows = new HashMap<>();
		
		sSqlServey="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,a.poll_status,a.allow_vote,b.society_id FROM poll_question as a join soc_group as b on a.group_id=b.group_id WHERE a.group_id = b.group_id and b.society_id='"+iSocietyID+"' Group by group_id";
		
		HashMap<Integer, Map<String, Object>> resultsociety =  m_dbConnRoot.Select(sSqlServey);
		
		if(resultsociety.size() > 0)
		{
			int iCount = 0;
			for(Entry<Integer, Map<String, Object>> polllist : resultsociety.entrySet()) 
			{	
				sSqlServey ="SELECT a.poll_id, a.group_id,a.question,a.start_date,a.end_date,a.created_by,b.options,b.option_id,b.counter FROM poll_question as a join poll_option as b on a.poll_id=b.poll_id WHERE a.poll_id = '"+ polllist.getValue().get("poll_id")+"'";
				
				HashMap<Integer, Map<String, Object>> Optionlist =  m_dbConnRoot.Select(sSqlServey);
				if(Optionlist.size() > 0)
				{
					//System.out.println("hi");
				sSqlServey ="SELECT a.poll_id ,a.options ,a.option_id ,b.option_id ,b.unit_id,b.isValid FROM poll_vote as b JOIN poll_option as a ON b.option_id = a.option_id WHERE a.poll_id = '"+ polllist.getValue().get("poll_id")+"' and b.unit_id='"+iUnitId+"' and b.society_id='"+iSocietyID+"' and b.isValid='1'";
				HashMap<Integer, Map<String, Object>> selected =  m_dbConnRoot.Select(sSqlServey);
				
				//System.out.println(selected);
				JsonObject jsonOption = new JsonObject();
			 	if(Optionlist.size() > 0)
				{	
			 		HashMap rows1 = new HashMap<>();
			 		polllist.getValue().put("option", rows1);
			 		Integer iCnt = 1;
					for(Entry<Integer, Map<String, Object>> options : Optionlist.entrySet()) 
					{
						HashMap rows2 = new HashMap<>();
				 		rows1.put(iCnt, rows2);
						rows2.put("id", options.getValue().get("option_id"));
				 		rows2.put("option", options.getValue().get("options"));
				 		rows2.put("counter", options.getValue().get("counter"));
				 		//rows2.put("revote",options.getValue().get("allow_vote"));
				 		if(selected.size() > 0)
				 		{
				 			for(Entry<Integer, Map<String, Object>> selelctValue : selected.entrySet()) 
							{
				 				rows2.put("selected", selelctValue.getValue().get("option_id"));
							}
				 		//rows2.put("count", 0);
				 		}
				 		else
				 		{
				 			rows2.put("selected","0");
				 		}
				 		iCnt++;
					}
				}
			}
			//System.out.print(polllist);
		}
	}
	return rows;
}
		
		public static HashMap<Integer, Map<String, Object>> InsertVotes(int iOptionId, int iLoginId,int iSocietyId, int iUnitId,int iPollId, boolean bInsertLatestVote)
		{
			String sInsertServey = "";
			String sUpdateServey="";
			String sSqlServey="";
			
			HashMap insert = new HashMap<>();
			
			 sSqlServey="SELECT poll_id,allow_vote FROM poll_question WHERE poll_id = '"+iPollId+"'";
			 HashMap<Integer, Map<String, Object>> selectOption =  m_dbConnRoot.Select(sSqlServey);
			// System.out.println(selectOption);
			 if(selectOption.size()>0)
			 {
				 for(Entry<Integer, Map<String, Object>> VoteValue : selectOption.entrySet()) 
				 {
					 String voteAllow="";
					 voteAllow = (String) VoteValue.getValue().get("allow_vote").toString();
					// System.out.println("Allow Vote : " +  voteAllow);
					 if(voteAllow.equals("1"))
					 {
						 sSqlServey ="SELECT a.option_id from poll_vote as a JOIN poll_option as b on a.option_id = b.option_id WHERE b.poll_id = '"+iPollId+"' and a.unit_id='"+iUnitId+"' and a.society_id='"+iSocietyId+"' and a.IsValid = '1'";
						 HashMap<Integer, Map<String, Object>> selected =  m_dbConnRoot.Select(sSqlServey);
						 
						 //System.out.println("Size : " + selected.size() + " Query : " + sSqlServey);
						 if(selected.size() > 0)
						 {
							for(Entry<Integer, Map<String, Object>> options : selected.entrySet()) 
							{
								sSqlServey="Update poll_vote set IsValid='0' where option_id='"+ options.getValue().get("option_id") +"' and unit_id='"+iUnitId+"' and society_id='"+iSocietyId+"'";
								long IsValid =  m_dbConnRoot.Update(sSqlServey);
								
								//System.out.println("Update IsValid : " + sSqlServey);
								sSqlServey="update poll_option set counter=counter -1 where option_id='"+ options.getValue().get("option_id") +"' and poll_id='"+ iPollId +"'";
								long NewCounter =  m_dbConnRoot.Update(sSqlServey);
								
								//System.out.println("Update Counter : " + sSqlServey);
							}
						 }
					 }
				}
			}
				
		
		sInsertServey ="insert into poll_vote(option_id,login_id,society_id,unit_id,member_id)" + " VALUES ('"+iOptionId+"','"+iLoginId+"','"+iSocietyId+"','"+iUnitId+"','0')";	
		long result = m_dbConnRoot.Insert(sInsertServey);
		
		if(iOptionId > 0)
		{
			sUpdateServey = "update poll_option"+ " set counter=counter +1 where option_id='"+iOptionId+"' and poll_id='"+iPollId+"'";
			long counter = m_dbConnRoot.Update(sUpdateServey);	
		}
		 
		return insert;
	}
		
		public static  HashMap<Integer, Map<String, Object>> mfetchAllOptions(int iPollId)
			{
			String sSqlServey = "";
			sSqlServey="SELECT a.poll_id,a.option_id,a.options,a.counter,a.status from poll_option as a WHERE a.poll_id='"+iPollId+"' ";
			HashMap<Integer, Map<String, Object>> resultsociety =  m_dbConnRoot.Select(sSqlServey);
			return resultsociety;
			}
public static HashMap<Integer, Map<String, Object>> mFetchPolls(int iSocietyId,int iFetchCount,boolean bFetchExpired,int iPollId)
{
	
	String societydbName="";
	HashMap<Integer, Map<String, Object>> mpPolls = null;
	try
	{
/*		
		//m_dbConnRoot = new DbOperations(true,"");
		String sqlForSocietyDetails = "Select `dbname` from society where society_id = '"+iSocietyId+"' and status = 'Y'";
		HashMap<Integer, Map<String, Object>> societyDetails = m_dbConnRoot.Select(sqlForSocietyDetails);
		for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet())
		{	
			societydbName = entry1.getValue().get("dbname").toString();
		}
		System.out.println("dbName:"+societydbName);
	*/
		String todayDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String sSqlPolls = "";
		
		//sSqlEvents = "select events_id, DATE_FORMAT(events_date,'%d %b, %Y') as events_date, DATE_FORMAT(end_date,'%d %b, %Y') as end_date, events_title, events, TIME_FORMAT(event_time,'%h:%i:%s %p') as event_time,IF(end_date >= '"+ todayDate +"',0,1) as IsEventExpired,IF(Uploaded_file <> '',concat('http://way2society.com/Events/',Uploaded_file),'') as Link,events_url,event_type,event_charges from events where society_id=" +iSocietyId+"";
		sSqlPolls = "select p.poll_id, DATE_FORMAT(p.start_date,'%d %b, %Y') as start_date, DATE_FORMAT(p.end_date,'%d %b, %Y') as end_date,DATE_FORMAT(p.create_date,'%d %b, %Y') as create_date,p.question, l.name, p.poll_status,IF(end_date >= '"+todayDate+"',0,1) as IsEventExpired,p.group_id,p.created_by from poll_question as p, login as l where p.created_by = l.login_id";
		//System.out.println("\nsql:"+sSqlEvents);
		

		if(!bFetchExpired)
		{
			//fetch events which are not expired
			sSqlPolls += " and end_date >= '"+ todayDate +"' ";
		}
		
		//sSqlEvents += " ORDER BY events_date ASC";
		if(iPollId > 0)
		{
			//System.out.println(iEventID);
			sSqlPolls += " and poll_id >= '"+ iPollId +"' ";
		}
		//sSqlEvents += " ORDER BY events_date ASC";
		sSqlPolls += " ORDER BY DATE(end_date) DESC, poll_id DESC";
		
		if(iFetchCount > 0)
		{
			sSqlPolls += " LIMIT "+iFetchCount;
		}
		
		//System.out.println("\n\nSql:"+sSqlPolls);
		mpPolls =  m_dbConnRoot.Select(sSqlPolls);
		//System.out.println("\n\n mpPolls: "+mpPolls);
		//m_dbConnRoot = new DbOperations(true, "");
		for(Entry<Integer, Map<String, Object>> entry1 : mpPolls.entrySet())
		{	
			String createdBy = entry1.getValue().get("created_by").toString();
			String sqlForCreatedByName = "select name from login where login_id = '"+createdBy+"';";
			//System.out.println("sqlForCreatedByName :"+sqlForCreatedByName);
			HashMap<Integer, Map<String, Object>> mpCreatedByName = m_dbConnRoot.Select(sqlForCreatedByName);
			//System.out.println("mpCreatedByName :"+mpCreatedByName);
			for(Entry<Integer, Map<String, Object>> entry2 : mpCreatedByName.entrySet())
			{
				String name = entry2.getValue().get("name").toString();
				//System.out.println("Name :"+name);
				entry1.getValue().put("createdByName",name);
			}
		}
		//System.out.println("\n\nmpPolls :"+mpPolls);
	}
	catch(Exception e)
	{
		System.out.println("\nException occured: "+e.getLocalizedMessage());
		HashMap rows = new HashMap<>();
		rows.put("message", e.getMessage());
		mpPolls = new HashMap<Integer, Map<String, Object>> ();
		mpPolls.put(0,rows);
	}
	
	return mpPolls;
}
public static void main(String[] args) throws Exception {
	// TODO Auto-generated method stub
	String token = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQI1By2w6EZabMQYAzmQGezd33qtufUfj-SMa0fhbCWM84fHKxZCb9_nTszcJN3OdGPDlO6cb7zYzjydLoHnta5NvBjXgFG7jgGYWpukXScicg";
	String tkey = "ltvk9RmgxmrAnSl7k8TQ_fzXwGFuQwQ0q__3ec81iEOBdPhybb-Vtjn-ETgoVDDhv2VuBYI5A_uTTktwFP8ytv3Kp_c0bOEFvt5Ip9bkQyTVEGBrq2FemzWuXJRvbqq8bASt1gvZt_IaP2adzvyFtQW6CMUrbxvs04tm8dk_gG0";
//	DbOperations m_dbConnRoot = new DbOperations(true, "");
//	Polls obj = new Polls(m_dbConnRoot);
//	int iSocietyID = 156;
	HashMap rows;
	 //rows =  obj.mfetchTasksByMe(59,169);
	//rows =  obj.mfetchTasksForMe(169);
	//System.out.println(obj.convert(rows));
//	rows = obj.mfetchAssignMember(Integer.parseInt(((String) getDecryptedTokenMap().get("society_id"))));
	//rows = obj.mFetchPolls(59,3,true,0);
//	rows = obj.UpdateTask(24, 3, 64);
	//System.out.println("\n"+rows);	
}
}
		
