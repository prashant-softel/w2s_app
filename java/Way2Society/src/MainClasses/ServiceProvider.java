package MainClasses;

import static MainClasses.DbConstants.DB_PASSWORD;
import static MainClasses.DbConstants.DB_USER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import MainClasses.DbOperations;

import CommonUtilityFunctions.MapUtility;
import CommonUtilityFunctions.CalendarFunctions;

public class ServiceProvider
{
	private static CalendarFunctions m_Timezone;
	ProjectConstants m_objProjectConstants;
	private static CommonUtilityFunctions.MapUtility m_objMapUtility;
	static DbOperations m_dbConn;
	static DbOperations m_dbConnRoot;
	static DbOperations m_dbConnSecurity;

	public ServiceProvider(DbOperations m_dbConnObj) 
	{
		//super(bAccessRoot, dbName);
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
		m_objProjectConstants = new ProjectConstants();
		m_objMapUtility = new MapUtility();		
	}	
	
	/**
	 * @param iSoc_id (int : returns records of society)
	 */
	public static HashMap<Integer, Map<String, Object>> mfetchServiceProvider(int iSoc_id,int iUnitID)
	{
		String sFetchProvider ="";
		String sSelectReview = "";
		HashMap rows = new HashMap<>();
		if(iUnitID == -1)
		{
			// Local
			//sFetchProvider ="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` ,  sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo`, 4)),'') as 'Main',sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND sp.`active`= 0 AND sp.`society_id` = '"+iSoc_id+"' GROUP BY sc.`spr_cat_id`  ORDER BY sc.`cat_id`";
		
			//server  old
			//sFetchProvider ="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo`, 4)),'') as 'Main',sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND sp.`active` IN (0,2) AND sp.`society_id` = '"+iSoc_id+"' GROUP BY sc.`spr_cat_id`";	
			// Modifi Query
			sFetchProvider="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo`, 4)),'') as 'Main',sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status,sps.`society_staff_id` FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c, `service_prd_society` AS sps WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND sp.`active` IN (0,2) AND sps.provider_id=sp.service_prd_reg_id AND sp.`society_id` = '"+iSoc_id+"' GROUP BY sc.`spr_cat_id`";
		}
		else if(iUnitID == 0)
		{
			// Local
			//sFetchProvider ="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo`, 4)),'') as 'Main',sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND sp.`active`= 1 AND sp.`society_id` = '"+iSoc_id+"' GROUP BY sc.`spr_cat_id`  ORDER BY sc.`cat_id`";
		
			//server
			//sFetchProvider ="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo`, 4)),'') as 'Main',sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND sp.`active`= 1 AND sp.`society_id` = '"+iSoc_id+"' GROUP BY sc.`spr_cat_id`";
			// Modifi Query
			sFetchProvider ="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo`, 4)),'') as 'Main',sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status,sps.`society_staff_id` FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c,`service_prd_society` AS sps WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND sps.provider_id=sp.service_prd_reg_id AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND sp.`active`= 1 AND sp.`society_id` = '"+iSoc_id+"' GROUP BY sc.`spr_cat_id`";
		}
		else
		{
			// Local
			//sFetchProvider ="SELECT sp.`service_prd_reg_id`, sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`age`, sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('http://localhost/beta_aws_2/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('http://localhost/beta_aws_2/',SUBSTR(sp.`photo`, 4)),'') as 'Main' , spu.unit_no,sp.security_status FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c, `service_prd_units` AS spu  WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sp.`service_prd_reg_id`=spu.`service_prd_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND sp.`active`= 1 AND sp.`society_id` = '"+iSoc_id+"' AND spu.unit_id='"+iUnitID+"' ORDER BY sc.`cat_id`";
			//new
			//sFetchProvider ="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo`, 4)),'') as 'Main' , spu.unit_no,sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c, `service_prd_units` AS spu  WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sp.`service_prd_reg_id`=spu.`service_prd_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND  sp.`society_id` = '"+iSoc_id+"' AND spu.unit_id='"+iUnitID+"' GROUP BY sc.`spr_cat_id` ORDER BY sc.`cat_id`";
			
			//server
			
		//sFetchProvider ="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo`, 4)),'') as 'Main' , spu.unit_no,sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c, `service_prd_units` AS spu  WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sp.`service_prd_reg_id`=spu.`service_prd_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND  sp.`society_id` = '"+iSoc_id+"' AND spu.unit_id='"+iUnitID+"' GROUP BY sc.`spr_cat_id`";
		//modify Query
		sFetchProvider ="SELECT distinct(sp.`service_prd_reg_id`), sp.`full_name`, sp.`photo`, sp.`photo_thumb` , sp.`active`, s.`society_id`, s.`society_name`, sc.`spr_cat_id`, sc.`cat_id` , c.`cat`,IF(sp.`photo_thumb` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo`, 4)),'') as 'Main' , spu.unit_no,sp.IsBlock,sp.Block_desc,sp.dob,sp.security_status,sps.`society_staff_id` FROM `service_prd_reg` AS sp, `society` AS s, `spr_cat` AS sc, `cat` AS c, `service_prd_units` AS spu,`service_prd_society` AS sps WHERE sp.`society_id`=s.`society_id` AND sp.`service_prd_reg_id`=sc.`service_prd_reg_id` AND sp.`service_prd_reg_id`=spu.`service_prd_id` AND sc.`cat_id`=c.`cat_id` AND sp.`status`='Y' AND sps.provider_id=sp.service_prd_reg_id AND s.`status`='Y' AND sc.`status`='Y' AND c.`status`='Y' AND sp.`society_id` = '"+iSoc_id+"' AND spu.unit_id='"+iUnitID+"' GROUP BY sc.`spr_cat_id`";
		}
		//System.out.println(sFetchProvider);
		HashMap<Integer, Map<String, Object>> mpServiceProvider =  m_dbConnRoot.Select(sFetchProvider);	
		String sDateofBirth="";  
	    Date now = new Date();
	    int age = 0;
		for(Entry<Integer, Map<String, Object>> Providers : mpServiceProvider.entrySet()) 
		{
			if(Providers.getValue().get("service_prd_reg_id") != null)
			{
				sDateofBirth =Providers.getValue().get("dob").toString();
				
			
			try 
			{
				Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(sDateofBirth);
				Calendar today = Calendar.getInstance();
				Calendar birthDate = Calendar.getInstance();
				birthDate.setTime(date1);
				age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
		   // System.out.println("Age : "+age);

				Providers.getValue().put("age", age);
			}
			catch (ParseException e)
			{
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		if(mpServiceProvider.size() > 0)
		{
			int iCount = 0;
			int ProviderID= 0;
			for(Entry<Integer, Map<String, Object>> Providers : mpServiceProvider.entrySet()) 
			{
				if(Providers.getValue().get("service_prd_reg_id") != null)
				{
					ProviderID =(Integer)  Providers.getValue().get("service_prd_reg_id");
				}
				//System.out.println("ProviderId"+ProviderID);
				if(mpServiceProvider.size() > 0 && ProviderID != 0)
				{
					//sSelectReview="select MAX(rating) as 'Rate',comment,name,add_comment_id from add_comment where service_prd_reg_id = '"+ProviderID+"'";
					sSelectReview="select ROUND( AVG(rating),1 ) as 'Rate',comment,name,add_comment_id from add_comment where service_prd_reg_id ='"+ProviderID+"'";
					HashMap<Integer, Map<String, Object>> Reviewlist =  m_dbConnRoot.Select(sSelectReview);
					 //System.out.println("QUery :"+Reviewlist);
					if(mpServiceProvider.size() > 0)
					{
						 HashMap rows1 = new HashMap<>();
						 Providers.getValue().put("Review", rows1);
					 	 Integer iCnt = 1;
					 	 for(Entry<Integer, Map<String, Object>> Review : Reviewlist.entrySet()) 
					 	 {
								HashMap rows2 = new HashMap<>();
						 		rows1.put("0", rows2);
								rows2.put("Comments", Review.getValue().get("comment"));
						 		rows2.put("Name", Review.getValue().get("name"));
						 		rows2.put("Rating", Review.getValue().get("Rate"));
						}
					 	iCount++;
					}	 
				}
				
				
			}
		}
		//return rows;
	    return mpServiceProvider;		
	}
	/**
	 * @param iService_prd_reg_id = Provider ID
	 * @param iSociety_id = Society ID
	 * @throws ClassNotFoundException 
	 */
	public static HashMap<Integer, Map<String, Object>> mfetchProviderDetails(int iService_prd_reg_id, int iSociety_id,int UnitID)
	{
		HashMap rows = new HashMap<>();
		String sUnitNo = "",security_dbName= "";
		System.out.println("==========================================================");
		System.out.println("UnitID : " + UnitID);
		
			String dbname = "SELECT dbname,security_dbname from society where society_id = '" + iSociety_id + "'";
			System.out.println("Query db : " + dbname);
			HashMap<Integer, Map<String, Object>> mpMapping_2 = m_dbConnRoot.Select(dbname);

			String sBlockUnit = "";
			String sBlockDesc = "";
			for(Entry<Integer, Map<String, Object>> entry_2 : mpMapping_2.entrySet())
			{
				if(entry_2.getValue().get("dbname").toString() != null && !entry_2.getValue().get("dbname").toString().equals(""))
				{
					try							
					{
						String db_name = entry_2.getValue().get("dbname").toString();
						if(entry_2.getValue().get("security_dbname") !=null )
						{
							security_dbName = entry_2.getValue().get("security_dbname").toString();
						}
						m_dbConn = new DbOperations(false,db_name);
						String sSelectUnit="select unit_no from unit where unit_id='"+ UnitID+"'";
						//System.out.println(sSelectUnit);
						HashMap<Integer, Map<String, Object>> Reviewlist =  m_dbConn.Select(sSelectUnit);
						//System.out.println(Reviewlist);
						for(Entry<Integer, Map<String, Object>> Review : Reviewlist.entrySet()) 
						 {
							if(Review.getValue().get("unit_no") != null)
							{
								sUnitNo = Review.getValue().get("unit_no").toString();
							}
						 }
					} 
					catch (ClassNotFoundException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}						
				}
			}
			
		
		String sFetchProvider="";
		if(sUnitNo.equals(""))
		{
			//Local
			//sFetchProvider = "SELECT sp.*,GROUP_CONCAT(su.`unit_id`) as UnitID,GROUP_CONCAT(' ', SUBSTR( su.`unit_no` , 1, LOCATE( '[', su.`unit_no` ) -1 ) ) as Unit,IF(sp.`photo_thumb` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo`, 4)),'') as 'Main', DATE_FORMAT( sp.since, '%d %b, %Y' ) AS 'workingDate',sc.cat_id,c.`cat` FROM `service_prd_reg` as sp JOIN `service_prd_units` AS su ON sp.`service_prd_reg_id`=su.`service_prd_id` JOIN `spr_cat` AS sc ON sc.service_prd_reg_id=sp.service_prd_reg_id JOIN `cat` AS c ON sc.`cat_id`=c.`cat_id` WHERE sp.`service_prd_reg_id`='"+iService_prd_reg_id+"' AND sp.`society_id`='"+iSociety_id+"' AND sp.`status`='Y' AND su.`society_id`='"+iSociety_id+"' GROUP BY sp.`service_prd_reg_id`";
			//Server
			sFetchProvider = "SELECT sp.*,GROUP_CONCAT(su.`unit_id`) as UnitID,GROUP_CONCAT(' ', SUBSTR( su.`unit_no` , 1, LOCATE( '[', su.`unit_no` ) -1 ) ) as Unit,IF(sp.`photo_thumb` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo`, 4)),'') as 'Main', DATE_FORMAT( sp.since, '%d %b, %Y' ) AS 'workingDate',sc.cat_id,c.`cat` FROM `service_prd_reg` as sp JOIN `service_prd_units` AS su ON sp.`service_prd_reg_id`=su.`service_prd_id` JOIN `spr_cat` AS sc ON sc.service_prd_reg_id=sp.service_prd_reg_id JOIN `cat` AS c ON sc.`cat_id`=c.`cat_id` WHERE sp.`service_prd_reg_id`='"+iService_prd_reg_id+"' AND sp.`society_id`='"+iSociety_id+"' AND sp.`status`='Y' AND su.`society_id`='"+iSociety_id+"' GROUP BY sp.`service_prd_reg_id`";
		}
		else
		{
			//Local
			//sFetchProvider = "SELECT "+sUnitNo+" as UnitNo,sp.*,GROUP_CONCAT(su.`unit_id`) as UnitID,GROUP_CONCAT(' ', SUBSTR( su.`unit_no` , 1, LOCATE( '[', su.`unit_no` ) -1 ) ) as Unit,IF(sp.`photo_thumb` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('http://localhost/beta_aws_14_gdrive/',SUBSTR(sp.`photo`, 4)),'') as 'Main', DATE_FORMAT( sp.since, '%d %b, %Y' ) AS 'workingDate',sc.cat_id,c.`cat` FROM `service_prd_reg` as sp JOIN `service_prd_units` AS su ON sp.`service_prd_reg_id`=su.`service_prd_id` JOIN `spr_cat` AS sc ON sc.service_prd_reg_id=sp.service_prd_reg_id JOIN `cat` AS c ON sc.`cat_id`=c.`cat_id` WHERE sp.`service_prd_reg_id`='"+iService_prd_reg_id+"' AND sp.`society_id`='"+iSociety_id+"' AND sp.`status`='Y' AND su.`society_id`='"+iSociety_id+"' GROUP BY sp.`service_prd_reg_id`";
			//Server
		sFetchProvider = "SELECT '"+sUnitNo+"' as UnitNo,sp.*,GROUP_CONCAT(su.`unit_id`) as UnitID,GROUP_CONCAT(' ', SUBSTR( su.`unit_no` , 1, LOCATE( '[', su.`unit_no` ) -1 ) ) as Unit,IF(sp.`photo_thumb` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo_thumb`, 4)),'') as Link, IF(sp.`photo` <> '',concat('https://way2society.com/',SUBSTR(sp.`photo`, 4)),'') as 'Main', DATE_FORMAT( sp.since, '%d %b, %Y' ) AS 'workingDate',sc.cat_id,c.`cat` FROM `service_prd_reg` as sp JOIN `service_prd_units` AS su ON sp.`service_prd_reg_id`=su.`service_prd_id` JOIN `spr_cat` AS sc ON sc.service_prd_reg_id=sp.service_prd_reg_id JOIN `cat` AS c ON sc.`cat_id`=c.`cat_id` WHERE sp.`service_prd_reg_id`='"+iService_prd_reg_id+"' AND sp.`society_id`='"+iSociety_id+"' AND sp.`status`='Y' AND su.`society_id`='"+iSociety_id+"' GROUP BY sp.`service_prd_reg_id`";
		}
		//System.out.println("provider:"+sFetchProvider);
		try
		{				
			m_dbConnRoot = new DbOperations(true,"hostmjbt_societydb");
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(sFetchProvider);
		HashMap<Integer, Map<String, Object>> mpServiceProvider =  m_dbConnRoot.Select(sFetchProvider);	
		//System.out.println(mpServiceProvider);
		String sDateofBirth="";  
	    Date now = new Date();
	    int age = 0;
		for(Entry<Integer, Map<String, Object>> Providers : mpServiceProvider.entrySet()) 
		{
			if(Providers.getValue().get("service_prd_reg_id") != null)
			{
				sDateofBirth =Providers.getValue().get("dob").toString();
			}
			try
			{
				Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(sDateofBirth);
				Calendar today = Calendar.getInstance();
			    Calendar birthDate = Calendar.getInstance();
			    birthDate.setTime(date1);
			    age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
			   // System.out.println("Age : "+age);
			    Providers.getValue().put("Age", age);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		
		if(mpServiceProvider.size() > 0)
		{
			int iCount = 0;
			
				if(mpServiceProvider.size() > 0)
				 {
					for(Entry<Integer, Map<String, Object>> Providers : mpServiceProvider.entrySet()) 
					{	
				
					//String sSelectReview="select * from add_comment where service_prd_reg_id = '"+iService_prd_reg_id+"' Order by timestamp Desc";
					String sSelectReview="select *, (select ROUND( AVG(rating),1 ) as 'Rate' from add_comment where service_prd_reg_id = '"+iService_prd_reg_id+"') as 'Rate' from add_comment where service_prd_reg_id = '"+iService_prd_reg_id+"' Order by timestamp Desc";
					HashMap<Integer, Map<String, Object>> Reviewlist =  m_dbConnRoot.Select(sSelectReview);
					//System.out.println("Query :"+Reviewlist);
					//System.out.println("REVIEW Query :"+sSelectReview);
					 boolean bFound = false;
					 if(mpServiceProvider.size() > 0)
					 {   
						
						 HashMap rows1 = new HashMap<>();
						 Providers.getValue().put("Review", rows1);
					 	 iCount = 0;
					 	
					 	 
					 		 for(Entry<Integer, Map<String, Object>> Review : Reviewlist.entrySet()) 
					 		 {
								HashMap rows2 = new HashMap<>();
						 		rows1.put(iCount, rows2);
								rows2.put("Comments", Review.getValue().get("comment"));
						 		rows2.put("Name", Review.getValue().get("name"));
						 		rows2.put("Rating", Review.getValue().get("rating"));
						 		rows2.put("Rate", Review.getValue().get("Rate"));
						 		iCount++;
						 		bFound = true;
			    				
							}
					 	
					 		if(!bFound)
						    {
					 			HashMap rows2 = new HashMap<>();
						 		rows1.put(iCount, rows2);
								rows2.put("Comments", "");
						 		rows2.put("Name", "");
						 		rows2.put("Rating", "0");
						 		rows2.put("Rate", "0");

						    }
//Get documents
						
						String sSelectDocument="SELECT spr_document_id, service_prd_reg_id, spr_document.document_id, spr_document.status, attached_doc, doc1.document FROM `spr_document` join document as doc1 on doc1.document_id=spr_document.document_id where spr_document.service_prd_reg_id = '"+iService_prd_reg_id+"' and spr_document.status='Y' ";
						HashMap<Integer, Map<String, Object>> Documentlist =  m_dbConnRoot.Select(sSelectDocument);
						//System.out.println("Query :"+sSelectDocument);
						//System.out.println("Query :"+Documentlist);
						iCount = 0;
						 if(mpServiceProvider.size() > 0)
						 {
							 HashMap rows11 = new HashMap<>();
							 Providers.getValue().put("Documents", rows11);
						 	// Integer iCnt = 1;
						 	for(Entry<Integer, Map<String, Object>> Document : Documentlist.entrySet()) 
							{
								HashMap rows12 = new HashMap<>();
						 		rows11.put(iCount, rows12);
						 		rows12.put("DocumentID", Document.getValue().get("document_id"));
								rows12.put("DocumentType", Document.getValue().get("document"));
						 		rows12.put("attached_doc", Document.getValue().get("attached_doc"));
						 		iCount++;
							}
						 	

					 
					 }
						 String sSelectUnitDetails="SELECT distinct(`unit_id`),`unit_no`,`service_prd_id`,`society_id` from service_prd_units where service_prd_id = '"+iService_prd_reg_id+"'";
							HashMap<Integer, Map<String, Object>> UnitDetails =  m_dbConnRoot.Select(sSelectUnitDetails);
							//System.out.println("Query :"+sSelectUnitDetails);
							//System.out.println("Query :"+UnitDetails);
							iCount = 0;
							 if(mpServiceProvider.size() > 0)
							 {
								 HashMap rows13 = new HashMap<>();
								 Providers.getValue().put("UnitsDetails", rows13);
							 	// Integer iCnt = 1;
							 	for(Entry<Integer, Map<String, Object>> Units : UnitDetails.entrySet()) 
								{
									HashMap rows14 = new HashMap<>();
							 		rows13.put(iCount, rows14);
									rows14.put("UnitID", Units.getValue().get("unit_id"));
							 		rows14.put("UnitNo", Units.getValue().get("unit_no"));
							 		iCount++;
								}
							 	
						 }
						 
						 }
					 System.out.println("==================================================");
					 System.out.println("security_dbName : " + security_dbName );
					if(security_dbName != "")
					{
						Providers.getValue().put("SecurityDBStatus", "Connected");
					}
					else
					{
						Providers.getValue().put("SecurityDBStatus", "NotConnected");
					}
				}
					
			}
		}
			return mpServiceProvider;		
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchProviderCategoryDetails()
	{
	String sSelectQuery = "";
	
	sSelectQuery = "select `cat_id`,`cat` from `cat`";
	//System.out.println("Category list" +sSelectQuery);
	HashMap<Integer, Map<String, Object>>  mpServiceProvider = m_dbConnRoot.Select(sSelectQuery);
    return  mpServiceProvider;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchProviderDocument()
	{
	String sSelectQuery = "";
	
	sSelectQuery = "select `document_id`,`document` from `document` where status='Y' ORDER BY document_id";
	//System.out.println("document list" +sSelectQuery);
	HashMap<Integer, Map<String, Object>>  mpServiceProvider = m_dbConnRoot.Select(sSelectQuery);
    return  mpServiceProvider;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchProviderUnits(int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	String sSelectUnit = "";
	sSelectUnit = "select `dbname` from `dbname`  where society_id= '"+iSocietyId+"'";
	HashMap<Integer, Map<String, Object>>  mpServiceProvider = m_dbConnRoot.Select(sSelectUnit);
	String dbName= "";
	//System.out.println("unit list" +mpServiceProvider);
	for(Entry<Integer, Map<String, Object>> entry : mpServiceProvider.entrySet()) 
	{
		/************* check if bill is opening bill ************/
		if(entry.getValue().get("dbname") != null)
		{
			dbName =entry.getValue().get("dbname").toString();
		}
	}
	//System.out.println("dbName:" +dbName);
	//mpServiceProvider["0"]["dbname"];
	if(mpServiceProvider.size() > 0)
	{
		//String DB_HOST = "localhost";
	   // String DB_USER = "root";
		//String DB_PASSWORD = "";
		//public Connection mMysqli;
		try 
		{
			//mMysqli = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName+"?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC",DB_USER,DB_PASSWORD);
			m_dbConn = new DbOperations(false, dbName);
			//System.out.println("dbName status:" + m_dbConn);
			String sSqlMemberMain = "";
			sSqlMemberMain = "select unit.`unit_no`,unit.`unit_id`,mem.owner_name from `unit` JOIN `member_main` as mem on unit.unit_id=mem.unit  where mem.ownership_status= 1 and unit.society_id='"+iSocietyId+"' order by unit.sort_order ASC ";
			//System.out.println("dbName status:" + sSelectUnit);
			HashMap<Integer, Map<String, Object>>  mpServiceProviderUnit = m_dbConn.Select(sSqlMemberMain);
			rows.put("member", MapUtility.HashMaptoList(mpServiceProviderUnit));
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			
		}
		
	}
	
    return  rows;
    
	}
	
	//			

	public static long mUpdateServiceProvider(long lServiceProviderID, String name, int cat_id,String DateOfBirth,String identity_mark,String Working_since,String education,String married, String photo, String photo_thumb)
	{
		long lUpdatedRecords = 0;
		try 
		{
//			m_dbConn.BeginTransaction();
			String sUpdateQuery = "update service_prd_reg set `full_name`='" + name + "',`photo`='"+ photo +"',`dob`='" + DateOfBirth + "',`identy_mark`='" + identity_mark + "',`since`='" + Working_since + "',`education`='" + education + "',`marry`='" + married + "'";
			sUpdateQuery = sUpdateQuery + " where service_prd_reg_id='" + lServiceProviderID + "'";
			//System.out.println(sUpdateQuery);	
			
			lUpdatedRecords = m_dbConnRoot.Update(sUpdateQuery);
			
			if(lUpdatedRecords  == 0)
			{
				//System.out.println("Update failed for ServiceReqID<" + lServiceProviderID  + ">");	
				//m_dbConn.RollbackTransaction();
				//record not updated
				return 0;
			}		
	
			//System.out.println("Update ServiceReqID<" + lServiceProviderID  + ">");	
//			m_dbConn.EndTransaction();
		
			return lUpdatedRecords;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			m_dbConn.RollbackTransaction();
		}
	
		return lUpdatedRecords;
	}

	//public static long mSetAddServiceProvider(int login_id,int society_id,String name,int cat_id,String DateOfBirth,String identity_mark,String Working_since,String education,String married, String photo,  String photo_thumb, int age, String cur_resd_add,String cur_con_1,String cur_con_2,String native_add,String native_con_1,String native_con_2,String ref_name,String ref_add,String ref_con_1,String ref_con_2,String father_name,String father_occu,String mother_name,String mother_occu,String hus_wife_name,String hus_wife_occ,String son_dou_name,String son_dou_occ,String other_name,String other_occ)
	public static long mSetAddServiceProvider(int iSocietyID,String sName,int iCatID,String sDOB ,String sIdentityMark,String WorkingSince,String sEdication,String sMarry,String sPhoto, String sPhoto_thumb)
	
	{
/*
	//m_dbConn.BeginTransaction();
	String sInsertQuery = "";
	String sPhotoUrl="../upload/main/"+sPhoto;
	String sThumbUrl="../upload/thumb/"+sPhoto_thumb;
	String cur_resd_add = "";
	String cur_con_1 = "";
	String cur_con_2 = "";
	String native_add = "";
	String native_con_1 = "";
	String native_con_2 = "";
	String ref_name = "";
	String ref_add = "";
	String ref_con_1 = "";
	String ref_con_2 = "";
	String father_name = "";
	String father_occu = "";
	String mother_name = "";
	String mother_occu = "";
	String hus_wife_name = "";
	String hus_wife_occ = "";
	String son_dou_name = "";
	String son_dou_occ = "";
	String other_name = "";
	String other_occ = "";

	int age = 0;
	try {
		
		//sInsertQuery = "insert into service_prd_reg (`society_id`,`full_name`,`photo`,`photo_thumb`,`age`,`dob`,`identy_mark`,`since`,`education`,`marry`) values ";
		sInsertQuery = "insert into service_prd_reg (`society_id`,`full_name`,`dob`,`identy_mark`,`since`,`education`,`marry`,`photo`,`photo_thumb`, `age`,`cur_resd_add`,`cur_con_1`, `cur_con_2`, native_add, native_con_1, native_con_2, ref_name, ref_add, ref_con_1, ref_con_2, father_name, father_occ, mother_name, mother_occ, hus_wife_name, hus_wife_occ, son_dou_name, son_dou_occ, other_name, other_occ) values ";
		sInsertQuery = sInsertQuery + "(" + iSocietyID + ",'" + sName + "','" + sDOB + "','" + sIdentityMark +  "','" + WorkingSince +  "','" + sEdication +  "','" + sMarry + "','" + sPhotoUrl + "','" + sThumbUrl + "','" + age +  "','" + cur_resd_add + "','" + cur_con_1 + "','" + cur_con_2 +"','" + native_add +"','" + native_con_1 +"','" + native_con_2 +"','" + ref_name +"','" + ref_add +"','" + ref_con_1 +"','" + ref_con_2 +"','" + father_name +"','" + father_occu +"','" + mother_name +"','" + mother_occu +"','" + hus_wife_name +"','" + hus_wife_occ +"','" + son_dou_name +"','" + son_dou_occ +"','" + other_name +"','" + other_occ +"')";

		
		//sInsertQuery = sInsertQuery + "(" + iSocietyID + ",'" + sName + "','" + sPhotoUrl + "','" + sThumbUrl +  "','"+ age +"','" + sDOB +  "','" + sIdentityMark +  "','" + WorkingSince + "','" + sEdication + "','" + sMarry + "')";

		//Sir
		//sInsertQuery = "insert into service_prd_reg (`society_id`,`full_name`,`dob`,`identy_mark`,`since`,`education`,`marry`,`photo`,`photo_thumb`, `age`,`cur_resd_add`,`cur_con_1`, `cur_con_2`, native_add, native_con_1, native_con_2, ref_name, ref_add, ref_con_1, ref_con_2, father_name, father_occ, mother_name, mother_occ, hus_wife_name, hus_wife_occ, son_dou_name, son_dou_occ, other_name, other_occ) values ";

		//sInsertQuery = sInsertQuery + "(" + society_id + ",'" + name + "','" + DateOfBirth + "','" + identity_mark +  "','" + Working_since +  "','" + education +  "','" + married + "','" + photo + "','" + photo_thumb + "','" + age +  "','" + photo + "','" + photo + "','" + photo +"','" + native_add +"','" + native_con_1 +"','" + native_con_1 +"','" + ref_name +"','" + ref_add +"','" + ref_con_1 +"','" + ref_con_2 +"','" + father_name +"','" + father_occu +"','" + mother_name +"','" + mother_occu +"','" + hus_wife_name +"','" + hus_wife_occ +"','" + son_dou_name +"','" + son_dou_occ +"','" + other_name +"','" + other_occ +"')";

		
		
		System.out.println(sInsertQuery);	
		
		long lServiceProviderID = m_dbConn.Insert(sInsertQuery);
		System.out.println("ServiceReqID<" + lServiceProviderID  + ">");	
		
		if(lServiceProviderID  == 0)
		{
			//m_dbConn.RollbackTransaction();
			return 0;
		}		
	
		String sInsert_Cat_Query  = "insert into spr_cat(`service_prd_reg_id`,`cat_id`)values('"+ lServiceProviderID + "','" + iCatID + "')";
		System.out.println(sInsert_Cat_Query);	
		
		long lCatID = m_dbConnRoot.Insert(sInsert_Cat_Query);
		//System.out.println("lCatID<" + lCatID  + ">");	
		
		//m_dbConn.EndTransaction();
		
		return lServiceProviderID;
		
		
		
		//long Service_prd_id = m_dbConnRoot.Insert(sInsertQuery);
		//System.out.println("Service_prd_id<" + Service_prd_id  + ">");
		}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//m_dbConn.RollbackTransaction();
		}

	*/
	return 0;
	}

	public static long mUpdateServiceProvider_ContactDetails(long lServiceProviderID, String cur_resd_add,String cur_con_1, String cur_con_2, String native_add,String native_con_1,String native_con_2,String ref_name,String ref_add,String ref_con_1,String ref_con_2)
	{
		long lUpdatedRecords = 0;
		try 
		{
			
//			m_dbConn.BeginTransaction();
			String sUpdateQuery = "update service_prd_reg set `cur_resd_add`='"  + cur_resd_add + "',`cur_con_1`='" + cur_con_1 + "',`cur_con_2`='" + cur_con_2 + "',`native_add`='" + native_add + "',`native_con_1`='"  + native_con_1 + "',`native_con_2`='" + native_con_2 + "',`ref_name`='" + ref_name + "',`ref_add`='" + ref_add + "',`ref_con_1`='" + ref_con_1 + "',`ref_con_2`='" + ref_con_2 + "'";

//					+ "//`ref_add`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['ref_add'])))."',`ref_con_1`='".$_POST['ref_con_1']."',`ref_con_2`='".$_POST['ref_con_2']."',`since`='".$_POST['since']."',`education`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['education'])))."',`marry`='".$_POST['marry']."',`father_name`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['father_name'])))."',`father_occ`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['father_occ'])))."',`mother_name`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['mother_name'])))."',`mother_occ`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['mother_occ'])))."',`hus_wife_name`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['hus_wife_name'])))."',`hus_wife_occ`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['hus_wife_occ'])))."',`son_dou_name`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['son_dou_name'])))."',`son_dou_occ`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['son_dou_occ'])))."',`other_name`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['other_name'])))."',`other_occ`='".$this->m_dbConn->escapeString(ucwords(trim($_POST['other_occ'])))."' where service_prd_reg_id='".$_POST['id']."'";
			
			sUpdateQuery = sUpdateQuery + " where service_prd_reg_id='" + lServiceProviderID + "'";
			//System.out.println(sUpdateQuery);	
			
			lUpdatedRecords = m_dbConnRoot.Update(sUpdateQuery);
			
			if(lUpdatedRecords  == 0)
			{
				//System.out.println("Update failed for ServiceReqID<" + lServiceProviderID  + ">");	
				///m_dbConn.RollbackTransaction();
				//record not updated
				return 0;
			}		
	
			//System.out.println("Update ServiceReqID<" + lServiceProviderID  + ">");	
//			m_dbConn.EndTransaction();
		
			return lUpdatedRecords;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			m_dbConn.RollbackTransaction();
		}
	
		return lUpdatedRecords;
	}
	
	
	
	public static long mUpdateServiceProvider_Document(long lServiceProviderID, String documentID, String file_name)
	{
		long lUpdatedRecords = 0;
		try 
		{
			
			List<String> Documents = Arrays.asList(documentID.split(","));
			List<String> FileName = Arrays.asList(file_name.split(","));
			for (int i = 0; i < Documents.size(); i++)
			{
//			m_dbConn.BeginTransaction();
			String sSelectQuery = "select * from spr_document where `service_prd_reg_id` = '" + lServiceProviderID + "' and status='Y' and `document_id`='" + Documents.get(i) + "'";

			HashMap<Integer, Map<String, Object>> mpDocument =  m_dbConnRoot.Select(sSelectQuery);	
			//System.out.println("Record Selected " + sSelectQuery + " Result " + lUpdatedRecords);	
			//System.out.println("mpDocument <" + mpDocument + "> Result " + lUpdatedRecords);	
				
				if(mpDocument.size() > 0)
				{
					String sUpdateQuery = "update spr_document set `attached_doc`='" + FileName.get(i) + "'"; 
					sUpdateQuery = sUpdateQuery + " where  `document_id`='" + Documents.get(i) +  "' and service_prd_reg_id='" + lServiceProviderID + "'";
					//System.out.println(sUpdateQuery);	
				
					lUpdatedRecords = m_dbConnRoot.Update(sUpdateQuery);
					//System.out.println("Record Updated " + lUpdatedRecords);	
				}
				else
				{
					String sInsertQuery = "insert into spr_document(`service_prd_reg_id`,`document_id`, `attached_doc`)values('"+lServiceProviderID+"','"+ Documents.get(i) + "', '" + FileName.get(i) + "')";
					lUpdatedRecords = m_dbConnRoot.Insert(sInsertQuery);
					//System.out.println("Record Inserted " + sInsertQuery + " Result " + lUpdatedRecords);	
				}
			}
				
			if(lUpdatedRecords  == 0)
			{
				//System.out.println("Update failed for ServiceReqID<" + lServiceProviderID  + ">");	
				//m_dbConn.RollbackTransaction();
				//record not updated
				return 0;
			}		
	
			//System.out.println("Update ServiceReqID<" + lServiceProviderID  + ">");	
//			m_dbConn.EndTransaction();
		
			return lUpdatedRecords;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			m_dbConn.RollbackTransaction();
		}
	
		return lUpdatedRecords;
	}

	public static long mAddCommentReview(int iServiceProviderID,int iMemberID,String sMemberName,String sComment, int iRating,int iHideName)
	{
		String sInsertQuery=""; 
		try {
			
			sInsertQuery = "insert into add_comment (`service_prd_reg_id`,`commenter_id`,`hide_name`,`comment`,`name`,`rating`) values ";
			sInsertQuery = sInsertQuery + "(" + iServiceProviderID + ",'" + iMemberID + "','" + iHideName + "','" + sComment +  "','" + sMemberName +  "','" + iRating + "')";

			
			
			//System.out.println(sInsertQuery);	
			
			long lCommentID = m_dbConn.Insert(sInsertQuery);
			//System.out.println("ServiceReqID<" + lCommentID  + ">");	
			
			if(lCommentID  == 0)
			{
				return 0;
			}		
		
			
			return lCommentID;
			
			}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//m_dbConn.RollbackTransaction();
			}

		
		return 0;
		}
	
	
	
	
	public static long mUpdateServiceProvider_UpdateWorkUnit(long lServiceProviderID, int society_id, String unit_id, boolean bAdd)
	{
		long lUpdatedRecords = 0;
		
		try 
		{
			List<String> UnitIDs = Arrays.asList(unit_id.split(","));
			String sSelectUnit = "select `dbname` from `dbname`  where society_id= '"+society_id+"'";
			HashMap<Integer, Map<String, Object>>  mpServiceProvider = m_dbConnRoot.Select(sSelectUnit);
			String dbName= "";
			//System.out.println("unit list" +mpServiceProvider);
			for(Entry<Integer, Map<String, Object>> entry : mpServiceProvider.entrySet()) 
			{
				/************* check if bill is opening bill ************/
				if(entry.getValue().get("dbname") != null)
				{
					dbName =entry.getValue().get("dbname").toString();
					//System.out.println(dbName);
				}
			}
						//System.out.println("dbName status:" + m_dbConn);
			
			
			if(bAdd)
			{
				for (int i = 0; i < UnitIDs.size(); i++)
				{
					   //element = items.get(i);
					m_dbConn = new DbOperations(false, dbName);

					String sSqlMemberMain = "select unit.`unit_no`,unit.`unit_id`,mem.owner_name from `unit` JOIN `member_main` as mem on unit.unit_id=mem.unit where unit.unit_id='" + UnitIDs.get(i) +"' AND mem.`ownership_status`='1'";
					HashMap<Integer, Map<String, Object>>  mpServiceProviderUnit = m_dbConn.Select(sSqlMemberMain);
					//System.out.println(mpServiceProviderUnit);
					
					String sUnitNo="";
					String sOwnerName="";
					 for(Entry<Integer, Map<String, Object>> entryresult : mpServiceProviderUnit.entrySet()) 
					    {
					     if(entryresult.getValue().get("unit_id") != null)
						 {
					    	 sUnitNo=entryresult.getValue().get("unit_no").toString(); 
					    	 sOwnerName=entryresult.getValue().get("owner_name").toString(); 
						 }
					    }
					 	String UnitDetails=sUnitNo+"["+sOwnerName+"]";
					 //	System.out.println(UnitDetails);
					 	if( UnitDetails !=null)
					 	{
					 		//Add Column Status and Added, Deleted
					 		m_dbConnRoot = new DbOperations(true,"hostmjbt_societydb");
					 		///String sInsertQuery = "insert into service_prd_units(`service_prd_id`,`society_id`, `unit_id`,`unit_no`, status )values('"+lServiceProviderID+"','"+ society_id + "', '" + unit_id + "', '" + unit_no + "', 'Y')";
					 		String sInsertQuery = "insert into service_prd_units(`service_prd_id`,`society_id`, `unit_id`,`unit_no` )values('"+lServiceProviderID+"','"+ society_id + "', '" + UnitIDs.get(i) + "', '"+UnitDetails+"')";
				
					 		lUpdatedRecords = m_dbConnRoot.Insert(sInsertQuery);
					 		//System.out.println("Added unit " + sInsertQuery + " Result " + lUpdatedRecords);
					 	}
				}
			}
			else
			{
				//Remove means update status to N
//				String sUpdateQuery = "update spr_document set `attached_doc`='" + file_name + "'"; 
				String sUpdateQuery = "update service_prd_units set `status`= 'N'"; 
				sUpdateQuery = sUpdateQuery + " where `service_prd_id` = '" + lServiceProviderID + "' and society_id= '" + society_id + "' and `unit_id`='" + unit_id + "'";
				//System.out.println(sUpdateQuery);	
				
				lUpdatedRecords = m_dbConnRoot.Update(sUpdateQuery);
				//System.out.println("Set Unit status to N " + lUpdatedRecords);	
			}
				
			if(lUpdatedRecords  == 0)
			{
				//System.out.println("Update failed for ServiceReqID<" + lServiceProviderID  + ">");	
				//m_dbConn.RollbackTransaction();
				//record not updated
				return 0;
			}		
	
			//System.out.println("Update ServiceReqID<" + lServiceProviderID  + ">");	
//			m_dbConn.EndTransaction();
		
			return lUpdatedRecords;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			m_dbConn.RollbackTransaction();
		}
	
		return lUpdatedRecords;
	}

	public static long mUpdateServiceProvider_RemoveWorkUnit(long lServiceProviderID, int society_id, int UnitID)
	{
		long lUpdatedRecords = 0;
		try 
		{
			//String sSelectUnit = "";
			//sSelectUnit = "select `dbname` from `dbname`  where society_id= '"+iSocietyId+"'";
			//HashMap<Integer, Map<String, Object>>  mpServiceProvider = m_dbConnRoot.Select(sSelectUnit);
			//String dbName= "";
//			m_dbConn.BeginTransaction();
//			String sDeleteQuery = "delete from service_prd_units where `service_prd_id` = '" + lServiceProviderID + "' and society_id= '" + society_id + "' and `unit_id`='" + UnitID + "'";
			String sUpdateQuery = "update service_prd_units set `status`= 'N'"; 
			sUpdateQuery = sUpdateQuery + " where `service_prd_id` = '" + lServiceProviderID + "' and society_id= '" + society_id + "' and `unit_id`='" + UnitID + "'";
			//System.out.println(sUpdateQuery);	
			
			lUpdatedRecords = m_dbConnRoot.Update(sUpdateQuery);
			//System.out.println("Record deleted " + lUpdatedRecords);	

			if(lUpdatedRecords  == 0)
			{
				//System.out.println("Update failed for ServiceReqID<" + lServiceProviderID  + "> UnitID<" + UnitID  + ">");	
				//m_dbConn.RollbackTransaction();
				//record not updated
				return 0;
			}		
//			m_dbConn.EndTransaction();
		
			return lUpdatedRecords;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			m_dbConn.RollbackTransaction();
		}
	
		return lUpdatedRecords;
	}
	
	
	public static long mUpdateServiceProvider_FamilyDetails(long lServiceProviderID, String father_name,String father_occu,String mother_name,String mother_occu,String hus_wife_name,String hus_wife_occ,String son_dou_name,String son_dou_occ,String other_name,String other_occ)
	{
		long lUpdatedRecords = 0;
		try 
		{
			
//			m_dbConn.BeginTransaction();
			String sUpdateQuery = "update service_prd_reg set `father_name`='" + father_name + "',`father_occ`='" +  father_occu + "',`mother_name`='" + mother_name + "',`mother_occ`='" + mother_occu + "',`hus_wife_name`='" + hus_wife_name + "',`hus_wife_occ`='" + hus_wife_occ + "',`son_dou_name`='" + son_dou_name  + "',`son_dou_occ`='" +  son_dou_occ + "',`other_name`='" + other_name + "',`other_occ`='" + other_occ + "'";
			sUpdateQuery = sUpdateQuery + " where service_prd_reg_id='" + lServiceProviderID + "'";
			//System.out.println(sUpdateQuery);	
			
			lUpdatedRecords = m_dbConnRoot.Update(sUpdateQuery);
			
			if(lUpdatedRecords  == 0)
			{
				//System.out.println("Update failed for ServiceReqID<" + lServiceProviderID  + ">");	
				//m_dbConn.RollbackTransaction();
				//record not updated
				return 0;
			}		
	
			//System.out.println("Update ServiceReqID<" + lServiceProviderID  + ">");	
//			m_dbConn.EndTransaction();
		
			return lUpdatedRecords;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			m_dbConn.RollbackTransaction();
		}
	
		return lUpdatedRecords;
	}
/*	
	public long mSetAddServiceProvider_Partial(int login_id, int society_id, String name, int cat_id,
			String dateOfBirth, String identity_mark, String working_since, String education, String married,
			String photo, String photo_thumb) {
		// TODO Auto-generated method stub
		return 0;
	}
	*/
	/*-------------   Olde Version Code ----------------- */
	public static long mSetAddServiceProvider_Partial(int society_id,String name,String cat_id,String DateOfBirth,String identity_mark,String Working_since,String education,String married, String photo,  String photo_thumb)
	{
		
		String sPhotoUrl="";
		String sThumbUrl="";
		//m_dbConn.BeginTransaction();
		int age = 0;
		String cur_resd_add = "";
		String cur_con_1 = "";
		String cur_con_2 = "";
		String native_add = "";
		String native_con_1 = "";
		String native_con_2 = "" ;
		String ref_name = "";
		String ref_add = "";
		String ref_con_1 = "";
		String ref_con_2 = "";
		String father_name = "";
		String father_occu = "";
		String mother_name = "";
		String mother_occu = "";
		String hus_wife_name = "";
		String hus_wife_occ = "";
		String son_dou_name = "";
		String son_dou_occ = "";
		String other_name = "";
		String other_occ = "";
		String Block_Desc = "";

	String sInsertQuery = "";
	String NewDOB = "";
	try {
		
		System.out.println("Date of Birth : "+DateOfBirth);
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		Date date = formatter.parse(DateOfBirth);
		DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy"); 
		System.out.println("new Date of Birth : "+date);
		NewDOB = formatter1.format(date);
		System.out.println("string Date of Birth : "+NewDOB);
	  
		sInsertQuery = "insert into service_prd_reg (`society_id`,`full_name`,`dob`,`identy_mark`,`since`,`education`,`marry`,`photo`,`photo_thumb`, `age`,`cur_resd_add`,`cur_con_1`, `cur_con_2`, native_add, native_con_1, native_con_2, ref_name, ref_add, ref_con_1, ref_con_2, father_name, father_occ, mother_name, mother_occ, hus_wife_name, hus_wife_occ, son_dou_name, son_dou_occ, other_name, other_occ, Block_desc) values ";

		sInsertQuery = sInsertQuery + "(" + society_id + ",'" + name + "','" + NewDOB + "','" + identity_mark +  "','" + Working_since +  "','" + education +  "','" + married + "','" + sPhotoUrl + "','" + sThumbUrl + "','" + age +  "','" + cur_resd_add + "','" + cur_con_1 + "','" + cur_con_2 +"','" + native_add +"','" + native_con_1 +"','" + native_con_2 +"','" + ref_name +"','" + ref_add +"','" + ref_con_1 +"','" + ref_con_2 +"','" + father_name +"','" + father_occu +"','" + mother_name +"','" + mother_occu +"','" + hus_wife_name +"','" + hus_wife_occ +"','" + son_dou_name +"','" + son_dou_occ +"','" + other_name +"','" + other_occ +"','"+Block_Desc+"')";

		
		
		System.out.println(sInsertQuery);	
		
		long lServiceProviderID = m_dbConnRoot.Insert(sInsertQuery);
		System.out.println("mSetAddServiceProvider_Partial : ServiceReqID<" + lServiceProviderID  + ">");	
		
		if(lServiceProviderID  == 0)
		{
			//m_dbConn.RollbackTransaction();
			return 0;
		}		
		List<String> CategoryId = Arrays.asList(cat_id.split(","));
		for (int i = 0; i < CategoryId.size(); i++)
		{
		String sInsert_Cat_Query  = "insert into spr_cat(`service_prd_reg_id`,`cat_id`)values('"+ lServiceProviderID + "','" + CategoryId.get(i) + "')";
		System.out.println(sInsert_Cat_Query);	
		
		long lCatID = m_dbConnRoot.Insert(sInsert_Cat_Query);
		System.out.println("lCatID<" + lCatID  + ">");	
		}
		
		//m_dbConn.EndTransaction();
		
		return lServiceProviderID;
		
		
		
		//long Service_prd_id = m_dbConnRoot.Insert(sInsertQuery);
		//System.out.println("Service_prd_id<" + Service_prd_id  + ">");
		}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//m_dbConn.RollbackTransaction();
		}

	
	return 0;
	}
	
	/* ---  New Version Code */
	public static String mSetAddServiceProvider_Partial1(int society_id,String name,String cat_id,String DateOfBirth,String identity_mark,String Working_since,String education,String married, String photo,  String photo_thumb,String sCurrent_ContNo,String soc_staff_id)
	{
		String lServiceProviderID = "";
		String sPhotoUrl="";
		String sThumbUrl="";
		//m_dbConn.BeginTransaction();
		int age = 0;
		String cur_resd_add = "";
		String cur_con_1 = "";
		String cur_con_2 = "";
		String native_add = "";
		String native_con_1 = "";
		String native_con_2 = "" ;
		String ref_name = "";
		String ref_add = "";
		String ref_con_1 = "";
		String ref_con_2 = "";
		String father_name = "";
		String father_occu = "";
		String mother_name = "";
		String mother_occu = "";
		String hus_wife_name = "";
		String hus_wife_occ = "";
		String son_dou_name = "";
		String son_dou_occ = "";
		String other_name = "";
		String other_occ = "";
		String Block_Desc = "";

	String sInsertQuery = "";
	String NewDOB = "";
	try {
		
		
		String sqlForServiceProviderDetails = "Select service_prd_reg_id from service_prd_reg where cur_con_1 = '"+sCurrent_ContNo+"' and status = 'Y'";
		HashMap<Integer, Map<String, Object>> servicePrdDetails =  m_dbConnRoot.Select(sqlForServiceProviderDetails);
		if(servicePrdDetails.size() > 0)
		{
			for(Entry<Integer, Map<String, Object>> entry1 : servicePrdDetails.entrySet()) 
			{
				lServiceProviderID = entry1.getValue().get("service_prd_reg_id").toString();
				//System.out.println("service_prd_reg_id : " + lServiceProviderID);
				lServiceProviderID += ";0";
			}
		}
		else
		{

			//System.out.println("Date of Birth : "+DateOfBirth);
			String servicePrdId="";
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = formatter.parse(DateOfBirth);
			DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy"); 
			//System.out.println("new Date of Birth : "+date);
			NewDOB = formatter1.format(date);
			//System.out.println("string Date of Birth : "+NewDOB);
			sInsertQuery = "insert into service_prd_reg (`society_id`,`full_name`,`dob`,`identy_mark`,`since`,`education`,`marry`,`photo`,`photo_thumb`, `age`,`cur_resd_add`,`cur_con_1`, `cur_con_2`, native_add, native_con_1, native_con_2, ref_name, ref_add, ref_con_1, ref_con_2, father_name, father_occ, mother_name, mother_occ, hus_wife_name, hus_wife_occ, son_dou_name, son_dou_occ, other_name, other_occ, Block_desc) values ";
	/*
			$insert_query = "insert into service_prd_reg
					(`society_id`,`full_name`,`photo`,`photo_thumb`,`age`,`dob`,`identy_mark`,`cur_resd_add`,`cur_con_1`,`cur_con_2`,`native_add`,
					`native_con_1`,`native_con_2`,`ref_name`,`ref_add`,`ref_con_1`,`ref_con_2`,`since`,`education`,`marry`,`father_name`,
					`father_occ`,`mother_name`,`mother_occ`,`hus_wife_name`,`hus_wife_occ`,`son_dou_name`,`son_dou_occ`,`other_name`,`other_occ`)  values 
	*/
			sInsertQuery = sInsertQuery + "(" + society_id + ",'" + name + "','" + NewDOB + "','" + identity_mark +  "','" + Working_since +  "','" + education +  "','" + married + "','" + sPhotoUrl + "','" + sThumbUrl + "','" + age +  "','" + cur_resd_add + "','" + cur_con_1 + "','" + cur_con_2 +"','" + native_add +"','" + native_con_1 +"','" + native_con_2 +"','" + ref_name +"','" + ref_add +"','" + ref_con_1 +"','" + ref_con_2 +"','" + father_name +"','" + father_occu +"','" + mother_name +"','" + mother_occu +"','" + hus_wife_name +"','" + hus_wife_occ +"','" + son_dou_name +"','" + son_dou_occ +"','" + other_name +"','" + other_occ +"','"+Block_Desc+"')";
	
			
			
			//System.out.println(sInsertQuery);	
			
			long lServiceProviderID1 = m_dbConnRoot.Insert(sInsertQuery);
			lServiceProviderID = String.valueOf(lServiceProviderID1) + ";1";
			//System.out.println("mSetAddServiceProvider_Partial : ServiceReqID<" + lServiceProviderID1  + ">");	
			List<String> CategoryId = Arrays.asList(cat_id.split(","));
			for (int i = 0; i < CategoryId.size(); i++)
			{
			String sInsert_Cat_Query  = "insert into spr_cat(`service_prd_reg_id`,`cat_id`)values('"+ lServiceProviderID1 + "','" + CategoryId.get(i) + "')";
			//System.out.println(sInsert_Cat_Query);	
			
			long lCatID = m_dbConnRoot.Insert(sInsert_Cat_Query);
			//System.out.println("lCatID<" + lCatID  + ">");	
			}
			
			if(lServiceProviderID1  == 0)
			{
				//m_dbConn.RollbackTransaction();
				return "0";
			}
		}
		
		String[] arr = lServiceProviderID.split(";");
		String staffprovid = arr[0];
		String insertQuery = "insert into service_prd_society(`provider_id`, `society_id`, `society_staff_id`) values ('"+staffprovid+"','"+society_id+"','"+soc_staff_id+"')";
		long lstaffid = m_dbConnRoot.Insert(insertQuery);
		//m_dbConn.EndTransaction();
		
		return lServiceProviderID;
		
		
		
		//long Service_prd_id = m_dbConnRoot.Insert(sInsertQuery);
		//System.out.println("Service_prd_id<" + Service_prd_id  + ">");
		}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//m_dbConn.RollbackTransaction();
		}

	
	return "0";
	}

	
/* -----------------------------------------------------Upsade Personal Detials --------------------------------------*/
	public static long mUpdateProfileDetails(int iSocietyID,int iServiceProviderID, String sProviderName,String sProviderDOB,String sProviderIdentymark,String sProviderW_since,String sMarritalStatus,String sMobile,String sAltMobile )
	{
	
	String updatedID="";
	String sSqlQuery="";
	long  UpdateId = 0;
	HashMap<Integer, Map<String, Object>> result = new HashMap<>();
	try {
	String sDate1=sProviderDOB;  
	    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);  
	    Date now = new Date();
	    int age = 0;
	    Calendar today = Calendar.getInstance();
	    Calendar birthDate = Calendar.getInstance();
	    birthDate.setTime(date1);
	    age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
	   // System.out.println("Age :"+age);
	sSqlQuery ="update service_prd_reg set full_name='"+sProviderName+"',age='"+age+"',dob='"+sProviderDOB+"',identy_mark='"+sProviderIdentymark+"',since='"+sProviderW_since+"',marry='"+sMarritalStatus+"',cur_con_1='"+sMobile+"',	cur_con_2='"+sAltMobile+"' where service_prd_reg_id	="+ iServiceProviderID+" and society_id	="+ iSocietyID+" ";
	
	  UpdateId =  m_dbConnRoot.Update(sSqlQuery);
	  
	//Map<String, Object> m1=new HashMap<>();
	//m1.put("ID", Long.toString(UpdateId));
	
	//result.put(1, m1);	
	
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//m_dbConn.RollbackTransaction();
		}
	return iServiceProviderID;
	}
	
	
	public static long mAddAssignUnit(int iSocietyID,int iServiceProviderID,int iUnitID )
	{
	
	String sInsertQuery="";
	String sSqlQuery="";
	long  UpdateUnit = 0;
	HashMap<Integer, Map<String, Object>> result = new HashMap<>();
	try {
			String sSelectUnit = "select `dbname` from `dbname`  where society_id= '"+iSocietyID+"'";
			HashMap<Integer, Map<String, Object>>  mpServiceProvider = m_dbConnRoot.Select(sSelectUnit);
			String dbName= "";
			//System.out.println("unit list" +mpServiceProvider);
			for(Entry<Integer, Map<String, Object>> entry : mpServiceProvider.entrySet()) 
			{
				/************* check if bill is opening bill ************/
				if(entry.getValue().get("dbname") != null)
				{
					dbName =entry.getValue().get("dbname").toString();
					//System.out.println(dbName);
				}
			}
						//System.out.println("dbName status:" + m_dbConn);
			
			
			
					m_dbConn = new DbOperations(false, dbName);

					String sSqlMemberMain = "select unit.`unit_no`,unit.`unit_id`,mem.owner_name from `unit` JOIN `member_main` as mem on unit.unit_id=mem.unit where unit.unit_id='" + iUnitID +"' AND mem.`ownership_status`='1'";
					HashMap<Integer, Map<String, Object>>  mpServiceProviderUnit = m_dbConn.Select(sSqlMemberMain);
					//System.out.println(mpServiceProviderUnit);
					
					String sUnitNo="";
					String sOwnerName="";
					 for(Entry<Integer, Map<String, Object>> entryresult : mpServiceProviderUnit.entrySet()) 
					    {
					     if(entryresult.getValue().get("unit_id") != null)
						 {
					    	 sUnitNo=entryresult.getValue().get("unit_no").toString(); 
					    	 sOwnerName=entryresult.getValue().get("owner_name").toString(); 
						 }
					    }
					 	String UnitDetails=sUnitNo+"["+sOwnerName+"]";
					 	if( UnitDetails !=null)
					 	{
					 		m_dbConnRoot = new DbOperations(true,"hostmjbt_societydb");
					 		sInsertQuery = "insert into service_prd_units (`service_prd_id`,`unit_id`,`unit_no`,`society_id`) values ";
					 		sInsertQuery = sInsertQuery + "(" + iServiceProviderID + ",'" + iUnitID + "','"+UnitDetails+"','" + iSocietyID + "')";
					 		//System.out.println(	"Insert : "+sInsertQuery);
					 		UpdateUnit =  m_dbConnRoot.Insert(sInsertQuery);
	  
					 	}
	
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//m_dbConn.RollbackTransaction();
		}
	return UpdateUnit;
	}
	public static long mRemoveUnit(int iSocietyID,int iServiceProviderID,int iUnitID)
	{
	
	String updatedID="";
	String sSqlQuery="";
	long  DeletedID = 0;
	HashMap<Integer, Map<String, Object>> result = new HashMap<>();
	try {
			//sSqlQuery ="Delete  service_prd_units set full_name='"+sProviderName+"',age='"+age+"',dob='"+sProviderDOB+"',identy_mark='"+sProviderIdentymark+"',since='"+sProviderW_since+"',marry='"+sMarritalStatus+"',cur_con_1='"+sMobile+"',	cur_con_2='"+sAltMobile+"' where service_prd_reg_id	="+ iServiceProviderID+" and society_id	="+ iSocietyID+" ";
			
			sSqlQuery ="delete from `service_prd_units` where `service_prd_id`='"+iServiceProviderID+"'AND `unit_id`='"+iUnitID+"' AND `society_id`='"+iSocietyID+"' ";
			DeletedID =  m_dbConnRoot.Delete(sSqlQuery);
		}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		//m_dbConn.RollbackTransaction();
		}
	return DeletedID;
	}
	public static long mAppved(int iServiceProviderID)
	{
		String sSqlQuery=""; 
		//String UpdateId="";
		long UpdateId = 0;
		HashMap<Integer, Map<String, Object>> result = new HashMap<>();
		try
		{
			sSqlQuery ="update `service_prd_reg` set active='1' where service_prd_reg_id	="+ iServiceProviderID+" ";
			 UpdateId =  m_dbConnRoot.Update(sSqlQuery);
		}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
//				m_dbConn.RollbackTransaction();
			}
		
			return UpdateId;
	}	
		
	public static long mSetMoreDetailsForApproval(int iServiceProviderID, String sComment, int societyId)
	{
		String sSqlQuery="",servicePrdName="",title="",pushNotificationMsg=""; 
		//String UpdateId="";
		long UpdateId = 0;
		int unitId = 0;
		HashMap<Integer, Map<String, Object>> result = new HashMap<>();
		try
		{
			sSqlQuery ="update `service_prd_reg` set active='2', `Block_desc` ='"+ sComment +"' where service_prd_reg_id	="+ iServiceProviderID+" ";
			//System.out.println(sSqlQuery);	
			UpdateId =  m_dbConnRoot.Update(sSqlQuery);
			String sqlForServiceProviderDetails = "Select full_name from service_prd_reg where service_prd_reg_id = '"+iServiceProviderID+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> servicePrdDetails =  m_dbConnRoot.Select(sqlForServiceProviderDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : servicePrdDetails.entrySet()) 
			{
				servicePrdName = entry1.getValue().get("full_name").toString();
			}
			String sqlForMemberDetails = "SELECT * FROM `service_prd_units`  where service_prd_id = '"+iServiceProviderID+"' and society_id = '"+societyId+"'";
			HashMap<Integer, Map<String, Object>> memberDetails = m_dbConn.Select(sqlForMemberDetails);
			for(Entry<Integer, Map<String, Object>> entryresult : memberDetails.entrySet()) 
			{
				String memberName = entryresult.getValue().get("unit_no").toString();
				int index = memberName.indexOf("[");
				String unitNo = memberName.substring(0, index);
				//System.out.println("unit No :"+unitNo);
				String sqlForMemberEmail = "select m.email,u.unit_id from member_main as m, unit as u where m.unit = u.unit_id and u.unit_no = '"+unitNo+"' and m.ownership_status = '1' and m.status = 'Y'";
				HashMap<Integer, Map<String, Object>> emailDetails = m_dbConn.Select(sqlForMemberEmail);
				EmailUtility obj_email = new EmailUtility();
				for(Entry<Integer, Map<String, Object>> entry1 : emailDetails.entrySet()) 
				{
					unitId = Integer.parseInt(entry1.getValue().get("unit_id").toString());
					String emailId = entry1.getValue().get("email").toString();
					String subject = "Service Provider Approval";
					String messgae = "<table  style='border-collapse:collapse'><tr>"
	        		+ "<tr><td >Dear Member,</td></tr>"
	        		+ "<tr><td>This notice is being send to inform you that we need more information about service provider  <b>"+servicePrdName+"</b></td></tr>"
	        		+ "<tr><td><br></td></tr>"
	        		+ "<tr><td> Note: "+sComment+" </td></tr>"
	        		+ "<tr><td><br><br></td></tr>"
	        		+ "<tr><td> If you have any questions, please contact society Manager or Secretary.</td></tr>"
	        		+ "<tr><td><br></td></tr>"
	        		+ "<tr><td>From </td></tr>"
	        		+ "<tr><td>Managing Committee.</td>"
	        		+ "</tr></table>";
					String emailResponse = obj_email.sendGenericMail(emailId, subject, messgae);
					System.out.println("Email response: "+emailResponse);
				}
				String sqlForDeviceDetails = "Select d.device_id from device_details as d,mapping as m where d.login_id = m.login_id and m.unit_id = '"+unitId+"'";
				HashMap<Integer, Map<String, Object>> deviceDetails = m_dbConnRoot.Select(sqlForDeviceDetails);
				String deviceId = "";
				for(Entry<Integer, Map<String, Object>> entry2 : deviceDetails.entrySet()) 
				{
					deviceId = entry2.getValue().get("device_id").toString().trim();
				}
				AndroidPush ap = new AndroidPush();
				//System.out.println("Device Id: "+deviceId);
				title = "Visitor Notification";
				pushNotificationMsg = "This notification is being send to inform you that we need more information about service provider "+servicePrdName+".\n Note: "+sComment;
				if(deviceId == "")
				{
				
				}
				else
				{
					ap.sendPushNotification(title,pushNotificationMsg,deviceId, "1", "3", "ViewEventPage", "12"); 
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//				m_dbConn.RollbackTransaction();
		}
		
		
		return UpdateId;
	}	
		
	
	public static long mRemoved(int iServiceProviderID)
	{
		String sSqlQuery=""; 
		//String UpdateId="";
		long UpdateId = 0;
		HashMap<Integer, Map<String, Object>> result = new HashMap<>();
		try
		{
			sSqlQuery ="update `service_prd_reg` set status='N' where service_prd_reg_id	="+ iServiceProviderID+" ";
			 UpdateId =  m_dbConnRoot.Update(sSqlQuery);
		}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
//				m_dbConn.RollbackTransaction();
			}
		
			return UpdateId;
	}	
	public static long mProviderBlock(int iSocietyID,int iServiceProviderID, String sComments)
	{
		String sSqlQuery=""; 
		//String UpdateId="";
		long UpdateId = 0;
		HashMap<Integer, Map<String, Object>> result = new HashMap<>();
		try
		{
			sSqlQuery ="update `service_prd_reg` set `IsBlock` ='1', `Block_desc` ='"+sComments+"' where service_prd_reg_id	="+ iServiceProviderID+" ";
			 UpdateId =  m_dbConnRoot.Update(sSqlQuery);
		}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
//				m_dbConn.RollbackTransaction();
			}
		
			return UpdateId;
	}	
	
	public static long mAddMoreDocument(int iServiceProviderID,String sDoc_id, String sDoc_img)
	{
		String sInsertQuery=""; 
		//String InsertRecords ="";
		try {
			//System.out.println("Doc ID :" +sDoc_id);
			List<String> Documents = Arrays.asList(sDoc_id.split(","));
			//System.out.println("Size  :" +Documents.size());
			for (int i = 0; i < Documents.size(); i++)
			{
				sInsertQuery = "insert into spr_document(`service_prd_reg_id`,`document_id`)values('"+iServiceProviderID+"','"+ Documents.get(i) + "')";
				long iInsertID = m_dbConn.Insert(sInsertQuery);
				//System.out.println(sInsertQuery);	
				//System.out.println("Record Inserted " + sInsertQuery + " Result " +iInsertID);	
			}
			
			
			
		
			//System.out.println("ServiceReqID<" + lCommentID  + ">");	
			
			
			}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Size  :" +e.getMessage());
			//m_dbConn.RollbackTransaction();
			}

		
		return iServiceProviderID;
	}
	public static String getFormatedDate(String date)//input date in (yyyy-mm-dd) format returns date in (dd-MMM-yyyy) format
	{
		//System.out.println("DateIn format:"+date);
		String uDate;
		String[] dboArray=date.split("-");
		String[] monthName = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		uDate = dboArray[2]+"-"+monthName[Integer.parseInt(dboArray[1])-1]+"-"+dboArray[0];
		return uDate;
	}
	public static ArrayList<String> getDatesBetweenTwoDates(String str_date,String end_date)
	{
		List<Date> dates = new ArrayList<Date>();
		ArrayList<String> allDates = new ArrayList<String>();
		try
		{
			DateFormat formatter ; 
			formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date  startDate = (Date)formatter.parse(str_date); 
			Date  endDate = (Date)formatter.parse(end_date);
			long interval = 24*1000 * 60 * 60; // 1 hour in millis
			long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
			long curTime = startDate.getTime();
			while (curTime <= endTime) {
			    dates.add(new Date(curTime));
			    curTime += interval;
			}
			for(int i=0;i<dates.size();i++)
			{
			    Date lDate =(Date)dates.get(i);
			    String ds = formatter.format(lDate);
			    allDates.add(ds);
			    //System.out.println(" Date is ..." + ds);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return allDates;
	}
	/* ------------------------------Add New Function For Provider Report For Security manager --------------------------*/
	public static HashMap<Integer, Map<String, Object>> mfetchProviderReport(int iProvider_id,int iSocietyId,String StartDate,String EndDate)
	{
		String sFetchProvider ="";
		String sSelectReview = "";
		String sss=StartDate;
		String eee = EndDate;
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> StaffReportDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			String sSelectSecurityDb = "select `dbname`,`security_dbname` from `society`  where society_id= '"+iSocietyId+"'";
			HashMap<Integer, Map<String, Object>>  mpSecuirityDb = m_dbConnRoot.Select(sSelectSecurityDb);
			String SocietydbName= "",sql2="",dbo="",uDbo="",sSelectQuery="";
			String SecuritydbName= "";
			//System.out.println("unit list" +mpServicedbProvider);
			for(Entry<Integer, Map<String, Object>> entry : mpSecuirityDb.entrySet()) 
			{
				/************* check if bill is opening bill ************/
				if(entry.getValue().get("security_dbname") != null )
				{
					SocietydbName =entry.getValue().get("dbname").toString();
					SecuritydbName =entry.getValue().get("security_dbname").toString();
					//System.out.println("security Database:" +SecuritydbName);
					///System.out.println("societyDatabase Database:" +SocietydbName);
				}
			}
			String StDate="00:01:01";
			String EnDate="23:59:59";
			if(StartDate== "" && EndDate=="" )
			{
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				StartDate= dateFormat.format(date) +" "+ StDate;
				EndDate = dateFormat.format(date)+ " " + EnDate;
				
				
			}
			else
			{
				StartDate= StartDate +" "+ StDate;
				EndDate = EndDate + " " + EnDate;
			}
			m_dbConnSecurity = new DbOperations(false, SecuritydbName);
			sSelectQuery = "SELECT * FROM `staffattendance` where staff_id = '"+iProvider_id+"' AND `inTimeStamp` between '"+StartDate+"' AND '"+EndDate+"'";
			StaffReportDetails =m_dbConnSecurity.Select(sSelectQuery);
			//System.out.println("Staff Details:"+StaffReportDetails);
			String MonthDate ="";
			String OutMonthDate ="";
			String attendanceDate [] = new String[StaffReportDetails.size()];
			String inTimeDetails [] = new String[StaffReportDetails.size()];
			String timeDiffDetails [] = new String[StaffReportDetails.size()];
			String outTimeDetails [] = new String[StaffReportDetails.size()];
			String dateOnly="",timeOnly="";
			String[] monthName = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
			int i=0;
			for(Entry<Integer, Map<String, Object>> entry : StaffReportDetails.entrySet()) 
			{
				HashMap rows1 = new HashMap<>();
			 		//Integer iCnt = 1;
					if(entry.getValue().get("inTimeStamp") != null)
					{
						
						MonthDate =entry.getValue().get("inTimeStamp").toString();
						String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);
						Date dt = df.parse(Time);
						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yyyy");
						timeOnly = tdf.format(dt);
						dateOnly = dfmt.format(dt);
						rows1.put("InTime", timeOnly);
				 		rows1.put("Date", dateOnly);	
				 		attendanceDate[i] = dateOnly;
				 		inTimeDetails[i] = timeOnly;
					}
					
					if(entry.getValue().get("outTimeStamp") != null)
					{
						OutMonthDate =entry.getValue().get("outTimeStamp").toString();
						String OutTime = m_Timezone.convertToCurrentTimeZone(OutMonthDate);
						//System.out.println("out Time :"+OutTime);
						
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);
	
						Date dt = df.parse(OutTime);
	
						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yyyy");
	
	
						String OuttimeOnly = tdf.format(dt);
						String OutdateOnly = dfmt.format(dt);
						
						rows1.put("OutTime", OuttimeOnly);
				 		rows1.put("OutDate", OutdateOnly);
				 		outTimeDetails[i] = OuttimeOnly;
					//}
					    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    Date d1 = null;
					    Date d2 = null;
					    try
					    {
					    	
					        d1 = format.parse(entry.getValue().get("inTimeStamp").toString());
					        d2 = format.parse(entry.getValue().get("outTimeStamp").toString());
					    } 
					    catch (ParseException e) 
					    {
					        e.printStackTrace();
					    }
					    long diff = d2.getTime() - d1.getTime();
					    long diffMinutes = diff / (60 * 1000) % 60;
					    long diffHours = diff / (60 * 60 * 1000);
					    String diffStr = diffHours+":"+diffMinutes;
					    rows1.put("TimeDiff",diffStr);
					    timeDiffDetails[i] = diffStr;
						entry.getValue().put("Timelist", rows1);
						i++;
						//System.out.println("\nTTTT:"+entry.getValue().get("Timelist")+"\n");
						//System.out.println("\nSSSS:"+StaffReportDetails+"\n");
					}
					else
					{
						String OuttimeOnly = "00:00";
						String OutdateOnly = "00-00-00";
						rows1.put("OutTime", OuttimeOnly);
				 		rows1.put("OutDate", OutdateOnly);
				 		String diffStr = "00";
				 		rows1.put("TimeDiff",diffStr);
				 		timeDiffDetails[i] = diffStr;
				 		outTimeDetails[i] = OuttimeOnly;
				 		entry.getValue().put("Timelist", rows1);
					}
				}
				int MonthDays[] = {31,28,31,30,31,30,31,31,30,31,30,31};
				String startDate2 = getFormatedDate(sss);
				//System.out.println("InTime:"+inTimeDetails[2]);
				//System.out.println("TimeDiff:"+timeDiffDetails[2]);
				
				String endDate2 =  getFormatedDate(eee);
				//System.out.println(endDate2);
				ArrayList<String> allDates = getDatesBetweenTwoDates(startDate2, endDate2); 
				String attReport[] = new String[allDates.size()];
				String inTimeReport[] = new String[allDates.size()];
				String timeDiffReport[] = new String[allDates.size()];
				String outTimeReport[] = new String[allDates.size()];
				//System.out.println(allDates.size());
				for(i=0;i<attReport.length;i++)
				{
					attReport[i]="A";
					inTimeReport[i] = "--";
					timeDiffReport[i] = "--";
					outTimeReport[i] = "--";
				}
				int m = 0;
				for(int j = 0;j<allDates.size();j++)
				{
					for(i=0;i<attendanceDate.length;i++)
					{
						//System.out.println("i:"+i+"j:"+j+"\nAtt:"+attendanceDate[i]+"\nAD:"+allDates.get(j));	
						//entry1.getValue().put("Date", allDates.get(j));
						if(allDates.get(j).equalsIgnoreCase(attendanceDate[i]))
						{
							attReport[j] = "P";//entry1.getValue().put("Attendance", "P");
							//System.out.println("InTime:"+inTimeDetails[m]+"m:"+m);
							//System.out.println("TimeDiff:"+timeDiffDetails[m]+"m:"+m);
							inTimeReport[j] = inTimeDetails[m];
							timeDiffReport[j] = timeDiffDetails[m];
							outTimeReport[j] = outTimeDetails[m];
							m++;
						}
					}
				}
				//ArrayList<String><String> attReportFinal = new ArrayList<String><String>();
				HashMap tempAtt = new HashMap();
				String attReportFinal[][] = new String[allDates.size()][5];
				int k=0;
				for(i = 0; i < allDates.size(); i++)
				{
					for(int j = 0; j < 5; j++)
					{
						if(j == 0)
						{
							attReportFinal[i][j] = allDates.get(i);
							//tempAtt.put("Date", allDates.get(i));
						}
						else if(j == 1)
						{
							attReportFinal[i][j] =  attReport[i];
						}
						else if(j == 2)
						{
							attReportFinal[i][j] = inTimeReport[i];
						}
						else if(j == 3)
						{
							attReportFinal[i][j] =  timeDiffReport[i];
						}
						else 
						{
							attReportFinal[i][j] =  outTimeReport[i];
						}
					}
				}
				//System.out.println("TempATT:"+tempAtt);
				if(StaffReportDetails.size() > 0)
				{
					//rows2.put("StaffReport",MapUtility.HashMaptoList(StaffReportDetails) );
					rows2.put("AttendanceReport",attReportFinal);
					rows.put("success",1);
					rows.put("response",rows2);	
					
				}
				else
				{
					rows2.put("message ","");
					rows.put("success",0);
					rows.put("response",rows2);
					//rows.put("InDate", dateOnly);
					//rows.put("InTime",timeOnly);
				}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			rows2.put("exception ",e );
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println("Rows:"+rows);
		return rows;
	}
	
	
	
	public static HashMap<Integer, Map<String, Object>> mgetStaffStatus(int iSocietyId)
	{
		HashMap<Integer, Map<String, Object>>  mpStaffID = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	
		try
		{
			sSelectQuery = "SELECT society_staff_id as StaffIDL FROM `service_prd_society` where status ='Y' and society_id = '"+iSocietyId+"' order by sp_id desc LIMIT 1";
			mpStaffID = m_dbConnRoot.Select(sSelectQuery);
			//System.out.println(mpStaffDocument);
			if(mpStaffID.size() > 0)
			{
				//add document to map
				 rows2.put("StaffID",MapUtility.HashMaptoList(mpStaffID));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("StaffID","");
			
			}
	}
		catch(Exception e) 
		{
			e.printStackTrace();
			rows2.put("exception ",e );
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println("Rows:"+rows);
		return rows;
	}

	public static String fetchstaffidstatus(int iSocietyID,String soc_staffid)
	{
		String msg = "";
		HashMap<Integer, Map<String, Object>>  mpStaffID = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	
		try
		{
			
			sSelectQuery = "SELECT * FROM `service_prd_society` where `status`='Y' and society_id = '"+iSocietyID+"' and society_staff_id = '"+soc_staffid+"'";
			mpStaffID = m_dbConnRoot.Select(sSelectQuery);
			//System.out.println(mpStaffDocument);
			if(mpStaffID.size() > 0)
			{
				//add document to map
				 msg = "Exist";			 
			}
			else
			{
				msg = "NotExist";	
			}
		}
		catch(Exception e) 
		{
			msg = e.toString();
		}
		
		//System.out.println(rows);
		 return  msg;
	}
	
	
	public static HashMap<Integer, Map<String, Object>> mfetchstaffitem(int iService_prd_reg_id, int iSociety_id,int UnitID) throws ClassNotFoundException, ParseException
	{
		HashMap rows = new HashMap<>();
		String sUnitNo = "",security_dbName= "";
		System.out.println("IUnit Id : " + UnitID);
			String dbname = "SELECT dbname,security_dbname from society where society_id = '" + iSociety_id + "'";
			HashMap<Integer, Map<String, Object>> mpMapping_2 = m_dbConnRoot.Select(dbname);

			for(Entry<Integer, Map<String, Object>> entry_2 : mpMapping_2.entrySet())
			{
				if(entry_2.getValue().get("dbname").toString() != null && !entry_2.getValue().get("dbname").toString().equals(""))
				{
					
						String db_name = entry_2.getValue().get("dbname").toString();
						if(entry_2.getValue().get("security_dbname") !=null )
						{
							security_dbName = entry_2.getValue().get("security_dbname").toString();
						}
				}
			}
			System.out.println("security_dbName : " +security_dbName);
			if(security_dbName != "")
			{
				DbOperations dbop_security = new DbOperations(false,security_dbName);
				String sqlQuery = "SELECT * FROM `item_lended` where staff_id = '"+iService_prd_reg_id+"' and unit_id = '"+UnitID+"' order by timestamp desc";
			    System.out.println("sqlQuery : " + sqlQuery );
				HashMap<Integer, Map<String, Object>>  staffitem_lended = dbop_security.Select(sqlQuery);
				if(staffitem_lended.size() > 0)
				{
					for(Entry<Integer, Map<String, Object>> entry :staffitem_lended.entrySet()) 
					{
						String time = entry.getValue().get("timestamp").toString();
						System.out.println("time : " + time);
						String[] split = time.split(" ");
						String timestamp = split[0];
						System.out.println("Timestamp : "  + timestamp);
						entry.getValue().put("timestamp", timestamp);
					}
					rows.put("Staff_Lended",MapUtility.HashMaptoList(staffitem_lended));
				}
					
				else
				{
					rows.put("Staff_Lended", "");
				}
			    	
			}
		
		return rows;
	}
					
	
}

