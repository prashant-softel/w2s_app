package MainClasses;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Owner
{
	static DbOperations m_dbConnRoot;
	static DbOperations m_dbConn;
	public Owner(DbOperations m_dbConnObj) 
	{
		m_dbConn = m_dbConnObj;	
	}
	public static HashMap<Integer, Map<String, Object>> fetchOwnersDetails(int UnitId,int SocietyId)
	{
		HashMap<Integer, Map<String, Object>> OwnersDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			String societyDbName = "", ownerName = "",FirstName="",LastName="",dob="",MiddleName="";
			int index,age;
			m_dbConnRoot = new DbOperations(true,"");
			String sqlForDatabaseDetails = "SELECT * FROM `society` where society_id = '"+SocietyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> DatabaseDetails =  m_dbConnRoot.Select(sqlForDatabaseDetails);
			for(Entry<Integer, Map<String, Object>> entry : DatabaseDetails.entrySet()) 
			{
				societyDbName = entry.getValue().get("dbname").toString();
			}
			//System.out.println("societyDbName :"+societyDbName);
			m_dbConn = new DbOperations(false, societyDbName);
			//String sqlForOwnersDetails = "SELECT m.member_id,m.primary_owner_name,m.dob,m.gender,u.unit_no,u.floor_no,w.wing,m.`alt_address` FROM `member_main` as m,`unit` as u, `wing` as w where m.unit = '"+UnitId+"' and m.society_id = '"+SocietyId+"' and m.ownership_status= '1' and m.unit = u.unit_id and u.wing_id = w.wing_id";
			String sqlForOwnersDetails = "SELECT m.member_id,m.primary_owner_name,m.dob,m.gender,u.unit_no, m.`alt_address`,mof.`other_adhaar`,d.`desg`,mof.`Name_Of_POA`, mof.`POA_Adhaar_No`,w.wing FROM `member_main` as m,`unit` as u, `desg` as d, `mem_other_family` as mof,`wing` as w  where m.unit = '"+UnitId+"' and m.society_id = '"+SocietyId+"' and m.ownership_status= '1' and m.unit = u.unit_id and d.`desg_id` = m.`desg` and mof.`member_id` = m.`member_id` and mof.`coowner` = '1'and w.`wing_id`=u.`wing_id`";
			OwnersDetails =  m_dbConn.Select(sqlForOwnersDetails);
			String sqlForSocietyAddress = "select * from society where society_id ='"+SocietyId+"'";
			HashMap<Integer, Map<String, Object>> societyDetails = m_dbConn.Select(sqlForSocietyAddress);
			//System.out.println("Owner Details :"+OwnersDetails);
			for(Entry<Integer, Map<String, Object>> entry : OwnersDetails.entrySet()) 
			{
				int ownerId = Integer.parseInt(entry.getValue().get("member_id").toString());
				ownerName = entry.getValue().get("primary_owner_name").toString();
				index = ownerName.lastIndexOf(" ");
				if(index < 0)
				{
					entry.getValue().put("FirstName", ownerName);
					entry.getValue().put("LastName", "");
				}
				else
				{
					//System.out.println("Last Index :"+index);
					FirstName = ownerName.substring(0, index);
					MiddleName = "";
					//System.out.println("First Name :"+FirstName);
					LastName = ownerName.substring(index+1, ownerName.length());
					//System.out.println("Last Name :"+LastName);
					entry.getValue().put("FirstName", FirstName);
					entry.getValue().put("MiddleName", MiddleName);
					entry.getValue().put("LastName", LastName);
				}
				if(entry.getValue().get("dob")== null)
				{
					entry.getValue().put("dob", "00-00-0000");
				}
				if(entry.getValue().get("gender").equals(""))
				{
					entry.getValue().put("gender","Not Specified");
				}
				if(entry.getValue().get("other_adhaar") == null)
				{
					entry.getValue().put("other_adhaar", "--");
				}
				if(entry.getValue().get("alt_address").equals(""))
				{
					entry.getValue().put("alt_address", "--");
				}
				if(entry.getValue().get("Name_Of_POA")== null)
				{
					entry.getValue().put("Name_Of_POA", "");
				}
				if(entry.getValue().get("POA_Adhaar_No")== null)
				{
					entry.getValue().put("POA_Adhaar_No","");
				}
				String sqlForCoOwner = "Select mof.`other_name`,mof.`other_dob`,mof.`other_adhaar`, d.`desg` FROM `desg` as d, `mem_other_family` as mof where d.`desg_id` = mof.`other_desg` and mof.`member_id` = '"+ownerId+"' and mof.`coowner` = '2' ";
				HashMap<Integer, Map<String, Object>> coownerDetails = m_dbConn.Select(sqlForCoOwner);
				System.out.println("coownerDetails :"+coownerDetails);
				for(Entry<Integer, Map<String, Object>> entry1 : coownerDetails.entrySet()) 
				{
					if(entry1.getValue().get("other_dob").equals("") || entry1.getValue().get("other_dob") == null)
					{
						entry1.getValue().put("other_dob", "00-00-0000");
					}
					if(entry1.getValue().get("other_adhaar").equals("") || entry1.getValue().get("other_adhaar") == null)
					{
						entry1.getValue().put("other_adhaar", "--");
					}
					if(entry1.getValue().get("desg").equals("") || entry1.getValue().get("desg") == null )
					{
						entry1.getValue().put("desg", "Not specified");
					}
					String coownerName  = entry1.getValue().get("other_name").toString();
					String coownerDob  = DateFunctionality.convertToSqlFormat(entry1.getValue().get("other_dob").toString());
					String coownerAdhaar = entry1.getValue().get("other_adhaar").toString();
					String coownerDesg = entry1.getValue().get("desg").toString();
					entry.getValue().put("coowner_name", coownerName);
					entry.getValue().put("coowner_dob", coownerDob);
					entry.getValue().put("coowner_adhaar", coownerAdhaar);
					entry.getValue().put("coowner_profession", coownerDesg);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//System.out.println("OwnersDetails : "+OwnersDetails);
		return OwnersDetails;
	}
	public static long updateOwner(int societyId,int imemberId,int iOtherMemberId, String sNameOfPOA, String sPOAAdhaar)
	{
		long lUpdateResult = 0;
		String societydbName="";
		try 
		{
			m_dbConnRoot = new DbOperations(true, "");
			String sqlForSocietyDetails = "Select `dbname` from society where society_id = '"+societyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> societyDetails = m_dbConnRoot.Select(sqlForSocietyDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet())
			{	
				societydbName = entry1.getValue().get("dbname").toString();
			}
			String sDateOfBirth="";
			m_dbConn = new DbOperations(false,societydbName);
			
			//update into member_main
			String sUpdateQuery = "";
			
			//sUpdateQuery = "UPDATE  `member_main` SET  `Name_Of_POA` =  '"+sNameOfPOA+"',`POA_Adhaar_No` =  '"+sPOAAdhaar+"' WHERE `member_id` ='"+imemberId+"'";
			sUpdateQuery = "UPDATE  `mem_other_family` SET  `Name_Of_POA` =  '"+sNameOfPOA+"',`POA_Adhaar_No` =  '"+sPOAAdhaar+"' WHERE `member_id` ='"+imemberId+"' AND `mem_other_family_id`='"+iOtherMemberId+"'";	
			//System.out.println(sUpdateQuery);	
			lUpdateResult = m_dbConn.Update(sUpdateQuery);
			//System.out.println("lUpdateID<" + lUpdateResult + ">");	
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    return lUpdateResult;	
	}
	// vaishali function ----------------
	/*public static long updateOwner(int societyId,int imemberId, String sdob, String sgender)
	{
		long lUpdateResult = 0;
		String societydbName="";
		try 
		{
			m_dbConnRoot = new DbOperations(true, "");
			String sqlForSocietyDetails = "Select `dbname` from society where society_id = '"+societyId+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> societyDetails = m_dbConnRoot.Select(sqlForSocietyDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet())
			{	
				societydbName = entry1.getValue().get("dbname").toString();
			}
			String sDateOfBirth="";
			m_dbConn = new DbOperations(false,societydbName);
			if(sdob == "")
			{
				sDateOfBirth = "0000-00-00";
			}
			else
			{
				sDateOfBirth = DateFunctionality.convertToStandardFormat(sdob);
			}
			System.out.println("SDateOfBirth :"+sDateOfBirth);
			//update into member_main
			String sUpdateQuery = "";
			sUpdateQuery = "UPDATE  `member_main` SET  `dob` =  '"+sDateOfBirth+"',`gender` =  '"+sgender+"' WHERE `member_id` ='"+imemberId+"'";	
			System.out.println(sUpdateQuery);	
			lUpdateResult = m_dbConn.Update(sUpdateQuery);
			System.out.println("lUpdateID<" + lUpdateResult + ">");	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    return lUpdateResult;	
	}*/
	public static int calculateAge(LocalDate birthDate, LocalDate currentDate)
	{
        if ((birthDate != null) && (currentDate != null))
        {
            return Period.between(birthDate, currentDate).getYears();
        } 
        else 
        {
            return 0;
        }
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
