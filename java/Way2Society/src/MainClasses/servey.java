package MainClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import MainClasses.DbOperations.*;


public class servey
{
	static DbOperations m_dbConnRoot;
	static DbOperations m_dbConn;
	//private static HashMap<Integer, Map<String, Object>> Optionlist;
	
	public servey(DbOperations  m_dbConnObj)
	{
			m_dbConn = m_dbConnObj;
			try 
			{
				m_dbConnRoot = new DbOperations(true,"hostmjbt_societydb");
			}
			catch (ClassNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				 			for(Entry<Integer, Map<String, Object>> selectValue : selected.entrySet()) 
							{
				 				rows2.put("selected", selectValue.getValue().get("option_id"));
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
			 //System.out.println(selectOption);
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
		
		/*public static HashMap<Integer, Map<String, Object>> SelectEdit(int iOptionId,int iPollId, boolean bEditVoteList)
		{
		String sEdit="";
		String sOption="";
		sEdit="select `poll_id`,`group_id`,`question`,`start_date`,`end_date`,`created_by`,poll_ from `poll_question` where `poll_id`='57'";
		HashMap<Integer, Map<String, Object>> selectList =  m_dbConnRoot.Select(sEdit);
		//System.out.println(selectList);
		iPollId=57;
		if(iPollId > 0)
		{
			sOption="select `poll_id`,`options` from `poll_option` where `poll_id`='"+iPollId+"'";
			HashMap<Integer, Map<String, Object>> selectOption =  m_dbConnRoot.Select(sOption);
			System.out.println(selectOption);
		}
		return selectList;
		}*/

}		
