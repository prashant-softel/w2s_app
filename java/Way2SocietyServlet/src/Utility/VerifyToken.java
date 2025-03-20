package Utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class VerifyToken {

	static public String VerifyToken(HashMap DecryptedTokenMap,HttpServletRequest request)
	{
		if( DecryptedTokenMap.containsKey("unit_id") &&  DecryptedTokenMap.containsKey("society_id"))
		{
			String ScriptName = getScriptName(request);
			//System.out.println(ScriptName);
			if(Integer.parseInt((String)DecryptedTokenMap.get("society_id")) == 0)
			{
				return "missing society details";
			}
			else if(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")) == 0 &&  (String)DecryptedTokenMap.get("role") == "Member" || (String)DecryptedTokenMap.get("role") == "Admin Member")
			{
				
				return "missing unit details";
			}
			else if(ScriptName == "")
			{
				return "unable to decode script name";
			}
			else if(hasPageAccessRoleWise((String)DecryptedTokenMap.get("role"), ScriptName) == 1)
			{
				return "valid";
			}
			else
			{	
				return "Your are not authorised to use this page.";
			}
		}
		else
		{
			return "missing required parameters";
		}
	}	

	static public String getScriptName(HttpServletRequest request)
	{
		String protocol = (request.getProtocol().toLowerCase().equals("https")==true) ? "https" :"http";
		String host     = request.getRemoteHost();
		String script   = (String) request.getRequestURI();
		//$_SERVER['SCRIPT_NAME'];
		/*String params   = $_SERVER['QUERY_STRING'];
		String referer  =  $_SERVER['HTTP_REFERER'];*/
		int pos = script.indexOf("/");
		String scriptName = script.substring(pos + 1);
		int pos1 = scriptName.indexOf("/");
		String scriptName1 = scriptName.substring(pos1+1);
		return scriptName1;
	}

	static public int hasPageAccessRoleWise(String role, String page)
		{
		//System.out.println(role);
		//System.out.println(page);
		int bHasAccess = 1;
		 // System.
		try {
			//Cominations of various roles.
			String[] ary_All = {"Super Admin", "Admin", "Admin Member", "Member","Contractor","Tenant" ,"Manager"};
			String[] ary_SuperAdmin = {"Super Admin"};
			String[] ary_Admin = {"Admin"};
			String[] ary_Member = {"Member"};
			String[] ary_Admin_Member = {"Admin Member"};
			String[] ary_AdminMember_Member = {"Admin Member", "Member"};
			String[] ary_MasterAdmin = {"Master Admin"};
			String[] ary_Master_SuperAdmin = {"Master Admin","Super Admin"};
			String[] ary_SuperAdmin_Admin = {"Super Admin", "Admin"};
			String[] ary_SuperAdmin_Admin_AdminMember = {"Super Admin", "Admin", "Admin Member"};
			
			HashMap<String,String[]> arrayPages =new HashMap<String,String[]>();
			arrayPages.put("Dashboard", ary_All);
			//arrayPages.put("Bill", ary_AdminMember_Member);
			arrayPages.put("Bill", ary_All);
			arrayPages.put("Notices" ,ary_All);
			arrayPages.put("Events", ary_All);
			arrayPages.put("MemberLedger", ary_All);
			arrayPages.put("Receipts",ary_All);
			arrayPages.put("MaintenanceBill",ary_All);
			//arrayPages.put("Receipts",ary_AdminMember_Member);
			//arrayPages.put("MaintenanceBill",ary_AdminMember_Member);
			arrayPages.put("Bank",ary_AdminMember_Member);
			arrayPages.put("Profile",ary_All);
			arrayPages.put("Directory",ary_All);
			arrayPages.put("MemberDetails",ary_All);   //ary_AdminMember_Member
			arrayPages.put("ServiceRequest",ary_All);
			arrayPages.put("Classifieds",ary_All);
			arrayPages.put("Commitee",ary_All);
			arrayPages.put("NotificationDetails", ary_AdminMember_Member);
			arrayPages.put("ServiceProvider/selectall", ary_All);
			arrayPages.put("ServiceProvider/selectprovider", ary_All);
			String[] aryRole = arrayPages.get(page);
			//System.out.println(aryRole);
			try{
			if((aryRole.length > 0) && ((Arrays.asList(aryRole).contains(role))==false))
			{
				System.out.println((Arrays.asList(aryRole).contains(role)));
				bHasAccess = 0;
			}
			}
			catch(Exception e)
			{}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bHasAccess = 0;
		}
		return bHasAccess;
	}
	
}
