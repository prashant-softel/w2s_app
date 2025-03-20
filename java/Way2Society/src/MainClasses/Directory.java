package MainClasses;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import MainClasses.DbOperations.*;

public class Directory
{
	static DbOperations m_dbConn;
	static Utility m_Utility;
	
	public Directory(DbOperations m_dbConnObj) throws ClassNotFoundException
	{
		m_dbConn = m_dbConnObj;
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	

	//1= Professional listing
	//2=Blood group
	//else all
	public static HashMap<Integer, Map<String, Object>> mfetchDirectory(int iType)
	{
		String sSqlDirectory = "";
		
		
		//Fetching directory
		if(iType == 1)
		{
			//Professional listing
			//sSqlDirectory = "select other_name, other_mobile, other_profile from mem_other_family where length(other_profile) > 0";		
		
			//sSqlDirectory = "SELECT mm.other_name,desg,other_profile,mm.relation  as rel,other_publish_contact,mm.other_mobile as mobile,mm.other_email as email FROM mem_other_family as mm  JOIN desg as dsg on  mm.other_desg=dsg.desg_id where mm.status='Y' AND length(other_profile) > 0";
			
			sSqlDirectory = "SELECT unit.unit_id, unit.unit_no, mm.owner_name as flat_name, unit.intercom_no, mof.other_name, mof.coowner, mof.relation as rel, mm.publish_contact as mobile, mof.other_profile, mof.other_publish_contact, mof.other_mobile as mobile_o, mof.other_email FROM unit JOIN member_main as mm ON unit.unit_id = mm.unit JOIN mem_other_family as mof ON mm.member_id = mof.member_id where mm.ownership_status=1 AND length(mof.other_profile) > 0 ORDER BY unit.sort_order ASC";
			//sSqlDirectory = "SELECT unit.unit_id, unit.unit_no, mm.owner_name as flat_name, unit.intercom_no, mof.other_name, mof.coowner, mof.relation as rel, mof.other_publish_contact, mof.other_profile, mof.other_mobile as mobile_o, mof.other_email FROM unit JOIN member_main as mm ON unit.unit_id = mm.unit JOIN mem_other_family as mof ON mm.member_id = mof.member_id where mm.ownership_status=1 ORDER BY unit.sort_order ASC";
				//System.out.println(sSqlDirectory);
		}
		else if (iType == 2)
		{
			//Blood group listing
			sSqlDirectory = "SELECT unit.unit_id, unit.unit_no, mm.owner_name as flat_name, unit.intercom_no, mof.other_name, mof.coowner, mof.relation as rel, mof.other_publish_contact, mof.other_mobile as mobile_o , mof.other_email, bgg.bg as booldGroup FROM unit JOIN member_main as mm ON unit.unit_id = mm.unit JOIN mem_other_family as mof ON mm.member_id = mof.member_id JOIN bg as bgg ON mof.child_bg = bgg.bg_id where mof.child_bg != 9 ORDER BY unit.sort_order ASC";		
			//System.out.println(sSqlDirectory);
		}
		
		else if (iType == 4)
		{
			//Emergency numbers listing
			sSqlDirectory = "SELECT * FROM `helpline` ORDER BY `sort_order` ASC";		
			//sSqlDirectory = "SELECT * FROM helpline ORDER BY category ASC";		
			//System.out.println(sSqlDirectory);
		}
		else if (iType == 5)
		{
			//Emergency numbers listing
			sSqlDirectory = "SELECT * FROM helpline ORDER BY sort_order ASC";		
			//sSqlDirectory = "SELECT category FROM helpline";		
			//System.out.println(sSqlDirectory);
		}
		else
		{
			//General Intercom and Mobile number
			//this is same prof listing
			//sSqlDirectory = "SELECT unit.unit_id, unit.unit_no, mm.owner_name as flat_name, unit.intercom_no, mof.other_name, mof.coowner, mof.relation as rel, mm.publish_contact as mobile, mof.other_publish_contact, mof.other_mobile as mobile_o, mof.other_email FROM unit JOIN member_main as mm ON unit.unit_id = mm.unit JOIN mem_other_family as mof ON mm.member_id = mof.member_id where mm.ownership_status=1 AND length(mof.other_profile) > 0 ORDER BY unit.sort_order ASC";
			//sSqlDirectory = "SELECT unit.unit_id, unit.unit_no, mm.owner_name as flat_name, unit.intercom_no, mof.other_name, mof.coowner, mof.relation as rel, mm.publish_contact as mobile, mof.other_publish_contact, mof.other_mobile as mobile_o, mof.other_email FROM unit JOIN member_main as mm ON unit.unit_id = mm.unit JOIN mem_other_family as mof ON mm.member_id = mof.member_id where mm.ownership_status=1 and mof.coowner = 1 ORDER BY unit.sort_order ASC";
			sSqlDirectory = "SELECT unit.unit_id, unit.unit_no, mm.owner_name as flat_name, unit.intercom_no, mof.other_name, mof.coowner, mof.relation as rel, mof.other_publish_contact, mof.other_profile, mof.other_mobile as mobile_o, mof.other_email FROM unit JOIN member_main as mm ON unit.unit_id = mm.unit JOIN mem_other_family as mof ON mm.member_id = mof.member_id where mm.ownership_status=1 ORDER BY unit.sort_order ASC";
			//System.out.println(sSqlDirectory);
		}

		//System.out.println(sSqlDirectory);
		HashMap<Integer, Map<String, Object>> mpDirectory =  m_dbConn.Select(sSqlDirectory);
				
		return mpDirectory;
	}
	
}
