package MainClasses;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import MainClasses.DbOperations;

public class Event
{
	static DbOperations m_dbConn;
	static DbRootOperations m_dbConnRoot;
	
	public Event(DbOperations m_dbConnObj, DbRootOperations m_dbConnRootObj) 
	{
		m_dbConn = m_dbConnObj;
		m_dbConnRoot = m_dbConnRootObj;
/*		try 
		{
			m_dbConnRoot = new DbOperations(true,"hostmjbt_societydb");
		}
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	
	public static HashMap<Integer, Map<String, Object>> mfetchEvents(int iUnitId,int iSocietyId,int iFetchCount,boolean bFetchExpired,int iEventID)
	{
		HashMap<Integer, Map<String, Object>> mpBills = null;
		try
		{
			String todayDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String sSqlEvents = "";
			
			//sSqlEvents = "select events_id, DATE_FORMAT(events_date,'%d %b, %Y') as events_date, DATE_FORMAT(end_date,'%d %b, %Y') as end_date, events_title, events, TIME_FORMAT(event_time,'%h:%i:%s %p') as event_time,IF(end_date >= '"+ todayDate +"',0,1) as IsEventExpired,IF(Uploaded_file <> '',concat('http://way2society.com/Events/',Uploaded_file),'') as Link,events_url,event_type,event_charges from events where society_id=" +iSocietyId+"";
			sSqlEvents = "select events_id, DATE_FORMAT(events_date,'%d %b, %Y') as events_date, DATE_FORMAT(end_date,'%d %b, %Y') as end_date, events_title, events, event_time,IF(end_date >= '"+ todayDate +"',0,1) as IsEventExpired,IF(Uploaded_file <> '',concat('https://way2society.com/Events/',Uploaded_file),'') as Link,events_url,event_type,event_charges,event_version,attachment_gdrive_id from events where society_id=" +iSocietyId+"";

			if(!bFetchExpired)
			{
				//fetch events which are not expired
				sSqlEvents += " and end_date >= '"+ todayDate +"' ";
			}
			
			//sSqlEvents += " ORDER BY events_date ASC";
			if(iEventID > 0)
			{
				//System.out.println(iEventID);
				sSqlEvents += " and events_id >= '"+ iEventID +"' ";
			}
			//sSqlEvents += " ORDER BY events_date ASC";
			sSqlEvents += " ORDER BY DATE(end_date) DESC, events_id DESC";
			
			if(iFetchCount > 0)
			{
				sSqlEvents += " LIMIT "+iFetchCount;
			}
			
			//System.out.println(sSqlEvents);
			mpBills =  m_dbConnRoot.Select(sSqlEvents);
		}
		catch(Exception e)
		{
			HashMap rows = new HashMap<>();
			rows.put("message", e.getMessage());
			mpBills.put(0,rows);
		}
		
		return mpBills;
	}

}
