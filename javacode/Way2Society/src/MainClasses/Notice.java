package MainClasses;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import CommonUtilityFunctions.MapUtility;

public class Notice 
{
	static DbOperations m_dbConn;	
	static DbRootOperations m_dbConnRoot;
	
	public Notice(DbOperations m_dbConnObj, DbRootOperations m_dbConnRootObj) 
	{
		m_dbConn = m_dbConnObj;
		m_dbConnRoot = m_dbConnRootObj;
	}
	
	
	public static HashMap<Integer, Map<String, Object>> mfetchNotices(int iUnitId, int iSocietyId, int iFetchCount, boolean bFetchExpired, int iNoticeID)
	{
		HashMap<Integer, Map<String, Object>> mpNotice=null;
		String todayDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String sSqlNotice = "";
		try
		{
			String societyDbName = "", ownerName = "",FirstName="",LastName="",dob="";
			int index,age;
			//m_dbConnRoot = new DbOperations(true,"");
/*			
			String sqlForDatabaseDetails = "SELECT * FROM `society` where society_id = '"+iSocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			m_dbConn = new DbOperations(false, societyDbName);
			*/
			//System.out.println("iUnitId :"+iUnitId);
			if(iUnitId > 0)
			{
				//fetching notices
				//server
				if(iFetchCount == 0)
				{					
					sSqlNotice = "select DISTINCT noticetbl.id, noticetbl.society_id,noticetbl.notice_type_id,subject,description,note,DATE_FORMAT(creation_date,'%d %b, %Y') as creation_date,DATE_FORMAT(exp_date,'%d %b, %Y') as exp_date,IF(noticetbl.exp_date >= '"+ todayDate +"',0,1) as IsNoticeExpired,IF(noticetbl.note <> '',concat('https://way2society.com/Notices/',noticetbl.note),'') as Link,notice_version,attachment_gdrive_id,doc_template_id,doc_id FROM notices AS noticetbl,display_notices AS displaynoticetbl WHERE noticetbl.id=displaynoticetbl.notice_id and  noticetbl.society_id="+iSocietyId+" and noticetbl.status = 'Y' and  displaynoticetbl.unit_id IN ("+ iUnitId +",0)";
				}
				else
				{
					sSqlNotice = "select DISTINCT noticetbl.id, noticetbl.society_id,noticetbl.notice_type_id,subject,note,DATE_FORMAT(creation_date,'%d %b, %Y') as creation_date,DATE_FORMAT(exp_date,'%d %b, %Y') as exp_date,IF(noticetbl.exp_date >= '"+ todayDate +"',0,1) as IsNoticeExpired,IF(noticetbl.note <> '',concat('https://way2society.com/Notices/',noticetbl.note),'') as Link,notice_version,attachment_gdrive_id,doc_template_id,doc_id FROM notices AS noticetbl,display_notices AS displaynoticetbl WHERE noticetbl.id=displaynoticetbl.notice_id and  noticetbl.society_id="+iSocietyId+" and noticetbl.status = 'Y' and  displaynoticetbl.unit_id IN ("+ iUnitId +",0)";
					
				}
				//local
				//sSqlNotice = "select DISTINCT noticetbl.id, noticetbl.society_id,noticetbl.notice_type_id,subject,description,note,DATE_FORMAT(creation_date,'%d %b, %Y') as creation_date,DATE_FORMAT(exp_date,'%d %b, %Y') as exp_date,IF(noticetbl.exp_date >= '"+ todayDate +"',0,1) as IsNoticeExpired,IF(noticetbl.note <> '',concat('http://localhost/beta_aws_2/Notices/',noticetbl.note),'') as Link FROM notices AS noticetbl,display_notices AS displaynoticetbl WHERE noticetbl.id=displaynoticetbl.notice_id and  noticetbl.society_id="+iSocietyId+" and noticetbl.status = 'Y' and  displaynoticetbl.unit_id IN ("+ iUnitId +",0)";	
				if(!bFetchExpired)
				{
					//fetch expired notices also
					sSqlNotice += " and noticetbl.exp_date >= '"+ todayDate +"' ";
				}
				//sSqlNotice += " ORDER BY creation_date ASC";
				
				if(iNoticeID > 0)
				{
					sSqlNotice += " and noticetbl.id >= '"+ iNoticeID +"' ";
				}
				
				sSqlNotice += " ORDER BY noticetbl.id DESC";
				//sSqlNotice += " ORDER BY noticetbl.creation_date ASC";
				
				if(iFetchCount > 0)
				{
					sSqlNotice += " LIMIT "+iFetchCount;
				}
			}
			else
			{
				//sSqlNotice = "select DISTINCT id,society_id,notice_type_id,subject,description,note,DATE_FORMAT(creation_date,'%d %b, %Y') as creation_date,DATE_FORMAT(exp_date,'%d %b, %Y') as exp_date,IF(exp_date >= '"+todayDate+"',0,1) as IsNoticeExpired,IF(note <> '',concat('https://way2society.com/Notices/',note),'') as Link FROM notices where society_id='"+iSocietyId+"' and status = 'Y' ";
				if(iFetchCount == 0)
				{
					sSqlNotice = "select DISTINCT id,society_id,notice_type_id,subject,description,note,DATE_FORMAT(creation_date,'%d %b, %Y') as creation_date,DATE_FORMAT(exp_date,'%d %b, %Y') as exp_date,IF(exp_date >= '"+todayDate+"',0,1) as IsNoticeExpired,IF(note <> '',concat('https://way2society.com/Notices/',note),'') as Link,notice_version,attachment_gdrive_id,doc_template_id,doc_id FROM notices where society_id='"+iSocietyId+"' and status = 'Y' ";
				}
				else
				{
					sSqlNotice = "select DISTINCT id,society_id,notice_type_id,subject,note,DATE_FORMAT(creation_date,'%d %b, %Y') as creation_date,DATE_FORMAT(exp_date,'%d %b, %Y') as exp_date,IF(exp_date >= '"+todayDate+"',0,1) as IsNoticeExpired,IF(note <> '',concat('https://way2society.com/Notices/',note),'') as Link,notice_version,attachment_gdrive_id,doc_template_id,doc_id FROM notices where society_id='"+iSocietyId+"' and status = 'Y' ";
				}
				if(!bFetchExpired)
				{
					//fetch expired notices also
					sSqlNotice += " and exp_date >= '"+ todayDate +"' ";
				}
				
				if(iNoticeID > 0)
				{
					sSqlNotice += " and id >= '"+ iNoticeID +"' ";
				}
				
				sSqlNotice += " ORDER BY id DESC";
				
				if(iFetchCount > 0)
				{
					sSqlNotice += " LIMIT "+iFetchCount;
				}
//				and exp_date >= '"+todayDate+"' ORDER BY IsNoticeExpired dsc LIMIT "+iFetchCount
			
			}
			//System.out.println(sSqlNotice);	
			mpNotice =  m_dbConn.Select(sSqlNotice);
			//System.out.println("mpNotice :"+mpNotice);
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return mpNotice;
	}

}
