package MainClasses;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CommonUtilityFunctions.MapUtility;
import MainClasses.DbOperations.*;

public class Lease {
	public Lease(DbOperations m_objDbOperations, DbRootOperations m_objDbRootOperations) {
		// TODO Auto-generated constructor stub
	}
	static DbOperations m_dbConn;
	static DbOperations m_dbConnRoot;
	static Utility m_Utility;
	
	public static HashMap<Integer, Map<String, Object>> mfetchProfesstionList(int iSocietyID)
	{
		String sSelectQuery = "select desg_id, desg from desg where status = 'Y'";	
		/******* fetching designations **********/
		HashMap<Integer, Map<String, Object>> resultDesignations =  m_dbConn.Select(sSelectQuery);
		return resultDesignations;
	}
	
	public static  long minsertleased(int iSocietyID,int iUnitId,String TFname,String TLname,String TMname,String Tdob,String TContact,String TEmail,String Tprofession_id,String TLeaseStart,String TLeaseEnd,String TAddress,String TCity,String TPincode,String TNote,String TAgentName,String TAgentContact,int membercount,String sTenantVaring) throws ParseException, ClassNotFoundException, JSONException
	{
		m_dbConnRoot = new DbOperations(true,"");
		Timestamp updateTimeStamp = new Timestamp(System.currentTimeMillis());
		String d=new SimpleDateFormat("yyyy-MM-dd").format(updateTimeStamp);
		String societyDbName = "";
		String sqlForDatabaseDetails = "SELECT `dbname` FROM `society` where society_id = '"+iSocietyID+"' and status = 'Y'";
		HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
		for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
		{
			societyDbName = entry.getValue().get("dbname").toString();
		}
		m_dbConn = new DbOperations(false, societyDbName);
		//Insert into tenant Module
		String sInsertQuery = "",sInsertQuery1="";
		sInsertQuery = "insert into tenant_module (`doc_id`, `unit_id`, `tenant_name`, `tenant_MName`, `tenant_LName`, `mobile_no`, `email`, `Profession`, `dob`, `Address`, `City`, `Pincode`, `agent_name`, `agent_no`, `members`, `create_date`, `start_date`, `end_date`, `note`) values ";
		sInsertQuery = sInsertQuery + "('0','" +iUnitId+ "','" + TFname + "','" + TMname +  "','" + TLname +  "','" + TContact +  "','" + TEmail +  "','" + Tprofession_id +  "','" + Tdob +  "','" + TAddress +  "','" + TCity +  "','" + TPincode +  "','" + TAgentName +  "','" + TAgentContact +  "','" + membercount +  "','" + d +  "','" + TLeaseStart +  "','" + TLeaseEnd +  "','" + TNote +  "')";
		System.out.println(sInsertQuery);	
		long lTenantId = m_dbConn.Insert(sInsertQuery);
		JSONArray jsonArray = new JSONArray(sTenantVaring);
		String name = TFname + " " + TLname;
		sInsertQuery1 = "insert into tenant_member(`tenant_id`, `mem_name`, `relation`, `mem_dob`, `contact_no`, `email`) values ";
		sInsertQuery1 = sInsertQuery1 + "('"+lTenantId+"','"+name +"','Self','"+Tdob+"','"+TContact+"','" + TEmail +  "')";
		System.out.println(sInsertQuery1);	
		long lTenantId1 = m_dbConn.Insert(sInsertQuery1);
		for(int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonobj = jsonArray.getJSONObject(i);
			String tenant_name = jsonobj.getString("Name"); 
			String relation = jsonobj.getString("Relation");
			String tenant_dob = "0000-00-00";
			if(jsonobj.has("DOB") && (!(jsonobj.getString("DOB").equals(""))))
			{
				tenant_dob = jsonobj.getString("DOB");
			}
			String tenant_contact = jsonobj.getString("Contact");
			String tenant_email = jsonobj.getString("Email");
			sInsertQuery1 = "insert into tenant_member(`tenant_id`, `mem_name`, `relation`, `mem_dob`, `contact_no`, `email`) values ";
			sInsertQuery1 = sInsertQuery1 + "('"+lTenantId+"','"+tenant_name +"','"+relation+"','"+tenant_dob+"','"+tenant_contact+"','" + tenant_email +  "')";
			System.out.println(sInsertQuery1);	
			lTenantId1 = m_dbConn.Insert(sInsertQuery1);
		}
		
		return lTenantId;
	}
		
}
