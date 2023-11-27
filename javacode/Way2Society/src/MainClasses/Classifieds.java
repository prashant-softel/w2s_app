package MainClasses;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
public class Classifieds 
	{
	static DbOperations m_dbConn;
	static DbRootOperations m_dbConnRoot;
	
	public Classifieds(DbOperations m_dbConnObj, DbRootOperations  m_dbConnRootObj)
	{
		m_dbConn = m_dbConnObj;
		m_dbConnRoot = m_dbConnRootObj;
	}
	//public static HashMap<Integer, Map<String, Object>> mfetchClassifieds(int iSocietyID)   //On server	
	public static HashMap<Integer, Map<String, Object>> mfetchClassifieds(int iSocietyID, int iClassifiedID)
		{
			String sSqlClassifieds = "";
//			sSqlClassifieds = "SELECT c.id,c.`ad_title`,c.`desp`,c.`post_date`,c.`exp_date`,if(c.`exp_date` < now(), 1, 0) as IsClassifiedExpired,c.`img`,c.`location`,c.`email`,c.`phone`,c.`cat_id`, ct.`name` as category_name from classified as c JOIN classified_cate as ct ON c.cat_id=ct.cat_id WHERE c.`society_id` = " + iSocietyID;
			if(iClassifiedID == 0)
			{
				sSqlClassifieds = "SELECT c.id,c.`ad_title`,c.`desp`,c.`post_date`,c.`exp_date`,if(c.`exp_date` < now(), 1, 0) as IsClassifiedExpired,c.`img`,c.`location`,c.`email`,c.`phone`,c.`cat_id`, ct.`name` as category_name, c.`active` from classified as c JOIN classified_cate as ct ON c.cat_id=ct.cat_id WHERE c.`society_id` = '" + iSocietyID + "' AND c.`status` =  'Y'";
			}
			else
			{
				sSqlClassifieds = "SELECT c.id,c.`ad_title`,c.`desp`,c.`post_date`,c.`exp_date`,if(c.`exp_date` < now(), 1, 0) as IsClassifiedExpired,c.`img`,c.`location`,c.`email`,c.`phone`,c.`cat_id`, ct.`name` as category_name, c.`active` from classified as c JOIN classified_cate as ct ON c.cat_id=ct.cat_id WHERE c.`id`='"+iClassifiedID+"' AND  c.`society_id` = '" + iSocietyID + "' AND c.`status` =  'Y'";
			}
				//System.out.println(sSqlClassifieds);
			HashMap<Integer, Map<String, Object>> mpClassifieds =  m_dbConnRoot.Select(sSqlClassifieds);
			return mpClassifieds;
		}
	
	public static long mAddClassifieds(int login_id,int society_id,String ad_title,String desp,String exp_date,String img,String location,String email,String phone,int cat_id)
		{
		//m_dbConn.BeginTransaction();
		String sInsertQuery = "";
		
		try {
			//Get Max RequestNo
			//int id = GetNextid();
			String postdate= GetTodayDate();
			String act_date=postdate;
			sInsertQuery = "insert into classified (`login_id`,`society_id`,`ad_title`,`desp`,`post_date`,`exp_date`,`img`,`location`,`email`,`phone`,`cat_id`,`act_date`) values ";
			sInsertQuery = sInsertQuery + "(" + login_id + "," + society_id + ",'" + ad_title + "','" + desp + "','" + postdate +  "','" + exp_date +  "','" + img +  "','" + location +"','" + email +"','" + phone +  "','" + cat_id +  "','" + act_date +"')";
			
			//System.out.println(sInsertQuery);	
			long ClassifiedID = m_dbConnRoot.Insert(sInsertQuery);
			//System.out.println("ClassifiedID<" + ClassifiedID  + ">");	
			
			if(ClassifiedID  == 0)
			{
				//m_dbConn.RollbackTransaction();
				return 0;
			}		
			//m_dbConn.EndTransaction();
			return ClassifiedID;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//m_dbConn.RollbackTransaction();
			}
		
		return 0;
	}	
	
	/*public static int GetNextid()
		{
		String sSelectQuery = "select Max(id) from classified";
		HashMap<Integer, Map<String, Object>> id =  m_dbConn.Select(sSelectQuery);
		Map.Entry<Integer, Map<String, Object>> itrRequestNo = id.entrySet().iterator().next();
		int iMaxid = (int) itrRequestNo.getValue().get("Max(id)");
		System.out.println("MaxCLNo <" + iMaxid + ">");	
		return iMaxid + 1;
		}*/
	public static String GetTodayDate() throws ParseException
	{
		DateFormat output = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		return output.format(date);
		
	}

	public static HashMap<Integer, Map<String, Object>> mfetchClassifiedsCategory()
		{
		String sSelectQuery = "";
		
		sSelectQuery = "select cat_id,`name` from classified_cate";
		HashMap<Integer, Map<String, Object>> resultCategoryList = m_dbConnRoot.Select(sSelectQuery);
	    return resultCategoryList;
		}
	
	public static HashMap<Integer, Map<String, Object>> mFetchAllClassified(int iSocietyId,int iFetchCount,boolean bFetchExpired,int iServiceRequest)
	{
		
		String societydbName="";
		HashMap<Integer, Map<String, Object>> mpClassifieds = null;
		try
		{
			//m_dbConnRoot = new DbOperations(true,"");
			String todayDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String sSqlClassified = "";
			
			//sSqlEvents = "select events_id, DATE_FORMAT(events_date,'%d %b, %Y') as events_date, DATE_FORMAT(end_date,'%d %b, %Y') as end_date, events_title, events, TIME_FORMAT(event_time,'%h:%i:%s %p') as event_time,IF(end_date >= '"+ todayDate +"',0,1) as IsEventExpired,IF(Uploaded_file <> '',concat('http://way2society.com/Events/',Uploaded_file),'') as Link,events_url,event_type,event_charges from events where society_id=" +iSocietyId+"";
			sSqlClassified = "select id, DATE_FORMAT(post_date,'%d %b, %Y') as post_date, DATE_FORMAT(act_date,'%d %b, %Y') as act_date,DATE_FORMAT(exp_date,'%d %b, %Y') as exp_date, ad_title, location, email,IF(exp_date >= '"+todayDate+"',0,1) as IsClassifiedeExpired,IF(img <> '',concat('http://way2society.com/Events/',img),'') as img from classified where  society_id= '"+iSocietyId+"' and status='Y' and active = '1'";
			//System.out.println("\nsql:"+sSqlEvents);
			

			if(!bFetchExpired)
			{
				//fetch events which are not expired
				sSqlClassified += " and exp_date >= '"+ todayDate +"' ";
			}
			
			//sSqlEvents += " ORDER BY events_date ASC";
			if(iServiceRequest > 0)
			{
				//System.out.println(iEventID);
				sSqlClassified += " and id >= '"+ iServiceRequest +"' ";
			}
			//sSqlEvents += " ORDER BY events_date ASC";
			sSqlClassified += " ORDER BY DATE(exp_date) DESC, id DESC";
			
			if(iFetchCount > 0)
			{
				sSqlClassified += " LIMIT "+iFetchCount;
			}
			
			//System.out.println("\n\nSql:"+sSqlClassified);
			mpClassifieds =  m_dbConnRoot.Select(sSqlClassified);
			//System.out.println("\n\n mpClassifieds :"+mpClassifieds);
		}
		catch(Exception e)
		{
			System.out.println("\nException occured: "+e.getStackTrace());
			HashMap rows = new HashMap<>();
			rows.put("message", e.getMessage());
			mpClassifieds.put(0,rows);
		}
		
		return mpClassifieds;
	}
		
}

