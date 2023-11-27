package tenant;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import MainClasses.CommonBaseClass;
import MainClasses.DateFunctionality;
import MainClasses.ProjectConstants;
//import MainClasses.Register;
import MainClasses.ServiceProvider;
import MainClasses.ViewOwner;
import MainClasses.ViewServiceProvider;
import MainClasses.ViewTenant;
import Utility.UtilityClass;
import Utility.VerifyToken;
import jdk.nashorn.internal.parser.JSONParser;


/**
 * Servlet implementation class Login
 */
//@WebServlet("/ServiceProvider")
@WebServlet(urlPatterns = {"/TenantServlet/*" })
public class TenantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TenantServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

  
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "GET");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "86400");
	    
	    
	    
	    JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();
		response.setContentType("text/html"); 
		
		
	
		String sURI = request.getRequestURI();
		
		int ch=Integer.parseInt(request.getParameter("mode").toString());
	    System.out.println("Mode: "+ch);
	    
	try {
			//ViewServiceProvider objServPro =  new ViewServiceProvider(true, "hostmjbt_societydb");
			String sToken = request.getParameter("token").trim();
			if(sToken.equals(""))
			{
				System.out.println("in if");
				System.out.println("Url :"+sURI);
				switch (ch)
			    {
			    	case 1:
			    	{
			    		System.out.println("getTenantDetails");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int unitId = Integer.parseInt(request.getParameter("unitId").toString());
						System.out.println("Unit Id :"+unitId);
						
						int iTenantId = Integer.parseInt(request.getParameter("tenantId").toString());
						System.out.println("iTenantId :"+iTenantId);
						
						HashMap rows = ViewTenant.mfetchTenantDetails(unitId,iTenantId,iSocietyId);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						System.out.println("TenantDetails: "+objStr);
						out.println(objStr);
						break;
			    	}
			    	case 2:
			    	{
			    		System.out.println("InsertTenant");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int iunitId = Integer.parseInt(request.getParameter("unitId").toString());
						System.out.println("Unit Id :"+iunitId);
						
						String sfName = request.getParameter("fName");
						System.out.println("fName  :"+sfName);
						
						String smName = request.getParameter("mName");
						System.out.println("sMName :"+smName);

						String slName = request.getParameter("lName");
						System.out.println("slName  :"+slName);
						
						int imemCount = Integer.parseInt(request.getParameter("memCount").toString());
						System.out.println("imemCount :"+imemCount);
						
						int iprofession = Integer.parseInt(request.getParameter("profession").toString());
						System.out.println("profession"+iprofession);
						
						String sDob = request.getParameter("dob").replaceAll("%20", " ");
						System.out.println("sDob :"+sDob);
						
						String sAdd1 = request.getParameter("add1").replaceAll("%20", " ");
						System.out.println("sAdd1 :"+sAdd1);
						
						String sAdd2 = request.getParameter("add2").replaceAll("%20", " ");
						System.out.println("sAdd2 :"+sAdd2);
						
						String scity = request.getParameter("city");
						System.out.println("city  :"+scity);
						
						String spincode = request.getParameter("pincode");
						System.out.println("pincode :"+spincode);
						
						String scardNo = request.getParameter("cardNo");
						System.out.println("cardNo  :"+scardNo);
						
						String semail = request.getParameter("email");
						System.out.println("pincode :"+semail);
						
						String smobileNo = request.getParameter("mobileNo");
						System.out.println("cardNo  :"+smobileNo);
						
						//HashMap rows = ViewTenant.mInsertNewTenant(iunitId, sfName, smName, slName, smobileNo, semail, iprofession, sDob, sAdd1, sAdd2, scity, spincode, imemCount, scardNo, iSocietyId);
						Gson objGson = new Gson();
						//String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						//System.out.println("tenantId: "+objStr);
						//out.println(objStr);
			    		break;
			    	}
			    	case 3:
			    	{
			    		System.out.println("InsertTenantMember");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int itenantModuleId = Integer.parseInt(request.getParameter("tenantModuleId").toString());
						System.out.println("itenantModuleId :"+itenantModuleId);
						
						String smemberName = request.getParameter("memberName");
						System.out.println("memberName  :"+smemberName);
						
						String sRelation = request.getParameter("relation");
						System.out.println("sRelation :"+sRelation);

						String smemDob = request.getParameter("memDob");
						if(smemDob == "")
						{
							smemDob = "0000-00-00";
						}
						System.out.println("smemDob  :"+smemDob);
						String semail = request.getParameter("email");
						System.out.println("pincode :"+semail);
						
						String smobileNo = request.getParameter("mobileNo");
						System.out.println("mobileNo  :"+smobileNo);
						
						//HashMap rows = ViewTenant.mInsertNewTenantMember(itenantModuleId, smemberName, sRelation, smemDob, smobileNo, semail,iSocietyId);
						Gson objGson = new Gson();
						//response.setContentType("application/json");
						//System.out.println("tenantMember Id: "+objStr);
						//out.println(objStr);
			    		break;
			    	}
			    	case 4:
			    	{
			    		System.out.println("updateTenant");
			    		
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int itenantModuleId = Integer.parseInt(request.getParameter("tenantModuleId").toString());
						System.out.println("itenantModuleId :"+itenantModuleId);
						
						String sFromDate = request.getParameter("pFromDate");
						System.out.println("sFromDate  :"+sFromDate);
						
						String sToDate = request.getParameter("pToDate");
						System.out.println("sToDate :"+sToDate);
						
						HashMap rows = ViewTenant.mUpdateTenantModule(iSocietyId, itenantModuleId, sFromDate, sToDate);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						System.out.println("Update Tenant Result : "+objStr);
						out.println(objStr);
			    		break;
			    	}
			    	case 5:
			    	{
			    		System.out.println("getTenantMemberDetails");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int iTenantId = Integer.parseInt(request.getParameter("tenantId").toString());
						System.out.println("iTenantId :"+iTenantId);
						
						HashMap rows = ViewTenant.mFetchTenantMemberDetails(iTenantId,iSocietyId);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						System.out.println("TenantDetails: "+objStr);
						out.println(objStr);
						break;
			    	}
			    	case 6:
			    	{
			    		System.out.println("editTenantDetails");
			    		
			    		int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int iunitId = Integer.parseInt(request.getParameter("unitId").toString());
						System.out.println("Unit Id :"+iunitId);
						
						int iTenantModuleId = Integer.parseInt(request.getParameter("tenantModuleId").toString());
						System.out.println("tenantModuleId :"+iTenantModuleId);
						
						String sfName = request.getParameter("fName");
						System.out.println("fName  :"+sfName);
						
						String smName = request.getParameter("mName");
						System.out.println("sMName :"+smName);

						String slName = request.getParameter("lName");
						System.out.println("slName  :"+slName);
						
						int imemCount = Integer.parseInt(request.getParameter("memCount").toString());
						System.out.println("imemCount :"+imemCount);
						
						int iprofession = Integer.parseInt(request.getParameter("profession").toString());
						System.out.println("profession"+iprofession);
						
						String sAdd1 = request.getParameter("add1").toString();
						System.out.println("sAdd1 :"+sAdd1);
						
						String sDob = request.getParameter("dob").replaceAll("%20", " ");
						System.out.println("sDob"+sDob);
						
						String sAdd2 = request.getParameter("add2").replaceAll("%20", " ");
						System.out.println("sAdd2 :"+sAdd2);
						
						String scity = request.getParameter("city");
						System.out.println("city  :"+scity);
						
						String spincode = request.getParameter("pincode");
						System.out.println("pincode :"+spincode);
						
						String scardNo = request.getParameter("cardNo");
						System.out.println("cardNo  :"+scardNo);
						
						String semail = request.getParameter("email");
						System.out.println("pincode :"+semail);
						
						String smobileNo = request.getParameter("mobileNo");
						System.out.println("cardNo  :"+smobileNo);
						
						//HashMap rows = ViewTenant.mEditTenantDetails(iTenantModuleId,iunitId, sfName, smName, slName, smobileNo, semail, iprofession, sDob,sAdd1, sAdd2, scity, spincode, imemCount, scardNo, iSocietyId);
						//Gson objGson = new Gson();
						//String objStr = objGson.toJson(rows);
						///response.setContentType("application/json");
						//System.out.println("tenantId: "+objStr);
						//out.println(objStr);
			    		break;
			    	}
			    	case 7:
			    	{
			    		System.out.println("EditTenantMember");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
			    		int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int itenantMemberId = Integer.parseInt(request.getParameter("tenantMemberId").toString());
						System.out.println("tenantMemberId :"+itenantMemberId);
						
						String smemberName = request.getParameter("memberName");
						System.out.println("memberName  :"+smemberName);
						
						String sRelation = request.getParameter("relation");
						System.out.println("sRelation :"+sRelation);

						String smemDob = request.getParameter("memDob");
						if(smemDob == "")
						{
							smemDob = "0000-00-00";
						}
						System.out.println("smemDob  :"+smemDob);
						String semail = request.getParameter("email");
						System.out.println("pincode :"+semail);
						
						String smobileNo = request.getParameter("mobileNo");
						System.out.println("mobileNo  :"+smobileNo);
						
						HashMap rows = ViewTenant.mEditTenantMemberDetails(itenantMemberId, smemberName, sRelation, smemDob, smobileNo, semail,iSocietyId);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						System.out.println("tenantMember Id: "+objStr);
						out.println(objStr);
			    		break;
			    	}
			    	default:
			    	{
			    		break;
			    	}
			    }
				
				//System.out.println("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/TenantServlet/GetOwnerDetails");
			}
			else
			{
				String sTkey = request.getParameter("tkey").trim();
				ViewServiceProvider objServiceProvider = new ViewServiceProvider(sToken, true, sTkey);
				if(objServiceProvider.IsTokenValid() == true)
				{
					HashMap DecryptedTokenMap = objServiceProvider.getDecryptedTokenMap();
					int iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
					if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
					{
						switch (ch)
					    {
					    	case 1:
					    	{
					    		System.out.println("getTenantDetails");
					    		System.out.println("Society Id :"+iSocietyID);
					    		
								//int unitId = Integer.parseInt(request.getParameter("unitId").toString());
								int unitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
								System.out.println("Unit Id :"+unitId);
								
								int tenantId = Integer.parseInt(request.getParameter("tenantId").toString());
								System.out.println("tenantId :"+tenantId);
								
								HashMap rows = ViewTenant.mfetchTenantDetails(unitId,tenantId,iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("TenantDetails: "+objStr);
								out.println(objStr);
								break;
					    	}
					    	case 2:
					    	{
					    		String sCompanyName="",sCompanyPanNo= "";
					    		int iTcompanyNI=0;
					    		System.out.println("InsertTenant");
								System.out.println("Society Id :"+iSocietyID);
								int iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
								//iTenantType = Integer.parseInt((String) DecryptedTokenMap.get("tenantType"));//1-Individual and 2=company
								//String sMoreThanTenant = request.getParameter("TMorethan");    //true/false
								String sfName = request.getParameter("TFirstName");
								String smName = request.getParameter("TMiddleName");
								String slName = request.getParameter("TLastName");
								String sTContact = request.getParameter("TContact");
								String sTEmail = request.getParameter("TEmail");
								String sRelation="";
								int iprofession = Integer.parseInt(request.getParameter("Tprofession").toString());
								String sTDOB ="0000-00-00"; 
								if(request.getParameter("TDob") == "")
								{
									sTDOB ="0000-00-00";
								}
								else
								{
								sTDOB =request.getParameter("TDob");
								sTDOB = DateFunctionality.convertToSqlFormat(sTDOB);
								}
								 iTcompanyNI = Integer.parseInt(request.getParameter("TcompanyNI").toString());
								// iTcompanyNI = 1 means Tenant is an Individual.
								// iTcompanyNI = 2 means Tenant is Company.
								String sAdhaarNo = "",sCIN_NO="";
								if(iTcompanyNI == 0)
								{
									sAdhaarNo = request.getParameter("TAdhaarNo");
								}
								else
								{
									sCIN_NO = request.getParameter("TCIN_No");
								}
								int imemCount = 0;
								String sTAddress = "", scity = "", sPincode = "", sTAgentName = "", sTAgentNumber = "", sNote = "";
								sTAddress = request.getParameter("TAddress").toString();
								scity = request.getParameter("TCity");
								sPincode = request.getParameter("TPincode");
								sTAgentName = request.getParameter("TAgentName");
								sTAgentNumber = request.getParameter("TAgentNumber");
								sNote = request.getParameter("TNote");
								sCompanyName = request.getParameter("TCompanyName");
								sCompanyPanNo = request.getParameter("AuthPan");
								String sTenantFamilyDetails = request.getParameter("TenantFamilyJSON");
								sTenantFamilyDetails = java.net.URLDecoder.decode(sTenantFamilyDetails, "UTF-8");
								HashMap rows;
								try 
								{
									//rows = ViewTenant.mInsertNewTenant(iUnitId, sFName, sMName, sLName, sMobileNo, sEmail, iProfessionId, sDob, sAdhaarCard, sTenantAddress, sCity, sPincode, sAgentName, sAgentNumber, sCIN_No, iMemCount, sNote, sRelation, iTenantType, sCompanyName, sCompanyPanNo, iSocietyId, sTenantFamilyDetails)
									rows = ViewTenant.mInsertNewTenant(iUnitId, sfName, smName, slName, sTContact, sTEmail, iprofession, sTDOB, sAdhaarNo, sTAddress, scity, sPincode, sTAgentName, sTAgentNumber, sCIN_NO, imemCount, sNote, sRelation,iTcompanyNI,sCompanyName, sCompanyPanNo, iSocietyID, sTenantFamilyDetails);
									Gson objGson = new Gson();
									String objStr = objGson.toJson(rows);
									response.setContentType("application/json");
									//System.out.println("tenantId: "+objStr);
									out.println(objStr);
								} 
								catch (JSONException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//JSONObj
								
					    		break;
					    	}
					    	case 3:
					    	{
					    		System.out.println("InsertTenantMember");
								//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
								//int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
								System.out.println("Society Id :"+iSocietyID);
								
								int itenantModuleId = Integer.parseInt(request.getParameter("tenantModuleId").toString());
								System.out.println("itenantModuleId :"+itenantModuleId);
								String smemberName = "",sRelation="",smemDob="",semail="",smobileNo="",sAdhaarCard ="";
								int sCoSignStatus=Integer.parseInt(request.getParameter("CoSingAgreement"));
								smemberName = request.getParameter("memberName");
								System.out.println("memberName  :"+smemberName);
								
								sRelation = request.getParameter("relation");
								System.out.println("sRelation :"+sRelation);
								smemDob = request.getParameter("DOB").toString();
								String smemDOB = DateFunctionality.convertToStandardFormat(smemDob);
								//smemDob = request.getParameter("DOB");
								if(smemDOB == "")
								{
									smemDOB = "0000-00-00";
								}
								System.out.println("smemDob  :"+smemDOB);
								semail = request.getParameter("email");
								System.out.println("pincode :"+semail);
								
								smobileNo = request.getParameter("mobileNo");
								System.out.println("mobileNo  :"+smobileNo);
								
								
								HashMap rows = ViewTenant.mInsertNewTenantMember(itenantModuleId, smemberName, sRelation, smemDOB, smobileNo, semail,sAdhaarCard,sCoSignStatus,iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("tenantMember Id: "+objStr);
								out.println(objStr);
					    		break;
					    	}
					    	case 4:
					    	{
					    		System.out.println("updateTenant");
					    		
								int itenantModuleId = Integer.parseInt(request.getParameter("tenantModuleId").toString());
								System.out.println("itenantModuleId :"+itenantModuleId);
								
								String sFromDate = request.getParameter("pFromDate");
								System.out.println("sFromDate  :"+sFromDate);
								
								String sToDate = request.getParameter("pToDate");
								System.out.println("sToDate :"+sToDate);
								
								HashMap rows = ViewTenant.mUpdateTenantModule(iSocietyID, itenantModuleId, sFromDate, sToDate);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("Update Tenant Result : "+objStr);
								out.println(objStr);
					    		break;
					    	}
					    	case 5:
					    	{
					    		System.out.println("getTenantMemberDetails");
								//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
								//int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
								System.out.println("Society Id :"+iSocietyID);
								
								int iTenantId = Integer.parseInt(request.getParameter("tenantId").toString());
								System.out.println("iTenantId :"+iTenantId);
								
								HashMap rows = ViewTenant.mFetchTenantMemberDetails(iTenantId,iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("TenantDetails: "+objStr);
								out.println(objStr);
								break;
					    	}
					    	case 6:
					    	{
					    		int iTcompanyNI=0;
					    		System.out.println("editTenantDetails");
								System.out.println("Society Id :"+iSocietyID);
								
								//int iunitId = Integer.parseInt(request.getParameter("unitId").toString());
								int iunitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
								System.out.println("Unit Id :"+iunitId);
								
								int iTenantModuleId = Integer.parseInt(request.getParameter("TtenantID").toString());
								System.out.println("tenantModuleId :"+iTenantModuleId);
								
								String sfName = request.getParameter("TFirstName");
								String smName = request.getParameter("TMiddleName");
								String slName = request.getParameter("TLastName");
								String sTContact = request.getParameter("TContact");
								String sTEmail = request.getParameter("TEmail");
								String sRelation="",sCompanyName="",sCompanyPanNo="";
								int iprofession = Integer.parseInt(request.getParameter("Tprofession").toString());
								String sTDOB ="0000-00-00"; 
								//sTDOB =request.getParameter("TDob");
								//sTDOB = DateFunctionality.convertToSqlFormat(sTDOB);
								 iTcompanyNI = Integer.parseInt(request.getParameter("TcompanyNI").toString());
								// iTcompanyNI = 1 means Tenant is an Individual.
								// iTcompanyNI = 2 means Tenant is Company.
								String sAdhaarNo = "",sCIN_NO="";
								if(iTcompanyNI == 0)
								{
									sAdhaarNo = request.getParameter("TAdhaarNo");
								}
								else
								{
									sCIN_NO = request.getParameter("TCIN_No");
								}
								int imemCount = 0;
								String sTAddress = "", scity = "", sPincode = "", sTAgentName = "", sTAgentNumber = "", sNote = "";
								sTAddress = request.getParameter("TAddress").toString();
								scity = request.getParameter("TCity");
								sPincode = request.getParameter("TPincode");
								sTAgentName = request.getParameter("TAgentName");
								sTAgentNumber = request.getParameter("TAgentNumber");
								sNote = request.getParameter("TNote");
								sCompanyName = request.getParameter("TCompanyName");
								sCompanyPanNo = request.getParameter("AuthPan");
								String sLeaseStartDate = request.getParameter("StartDate");
								String sLeaseEndDate = request.getParameter("EndDate");
								String sTenantFamilyDetails = request.getParameter("TenantFamilyJSON");
								String sAdhaarCard="";
								HashMap rows;
								try 
								{
									//HashMap rows = ViewTenant.mEditTenantDetails(iTenantModuleId, iUnitId, sFName, sMName, sLName, sMobileNo, sEmail, iProfessionId, sDob, sAdhaarCard, sTenantAddress, sCity, sPincode, sAgentName, sAgentNumber, sCIN_No, iMemCount, sNote, sRelation, iTenantType, sCompanyName, sCompanyPanNo, iSocietyId, sTenantFamilyDetails);
								
									rows = ViewTenant.mEditTenantDetails(iTenantModuleId,iunitId, sfName, smName, slName, sTContact, sTEmail, iprofession, sTDOB,sAdhaarCard, sTAddress, scity, sPincode,  sTAgentName, sTAgentNumber, sCIN_NO, imemCount, sNote, sRelation, iTcompanyNI, sCompanyName, sCompanyPanNo, iSocietyID, sTenantFamilyDetails,sLeaseStartDate,sLeaseEndDate);
									Gson objGson = new Gson();
									String objStr = objGson.toJson(rows);
									response.setContentType("application/json");
									System.out.println("tenantId: "+objStr);
									out.println(objStr);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
					    		break;
					    	}
					    	case 7:
					    	{
					    		System.out.println("EditTenantMember");
								//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
								//int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
								System.out.println("Society Id :"+iSocietyID);
								
								int itenantMemberId = Integer.parseInt(request.getParameter("tenantMemberId").toString());
								System.out.println("tenantMemberId :"+itenantMemberId);
								
								String smemberName = request.getParameter("memberName");
								System.out.println("memberName  :"+smemberName);
								
								String sRelation = request.getParameter("relation");
								System.out.println("sRelation :"+sRelation);

								String smemDob = request.getParameter("memDob");
								if(smemDob == "")
								{
									smemDob = "0000-00-00";
								}
								System.out.println("smemDob  :"+smemDob);
								String semail = request.getParameter("email");
								System.out.println("pincode :"+semail);
								
								String smobileNo = request.getParameter("mobileNo");
								System.out.println("mobileNo  :"+smobileNo);
								
								HashMap rows = ViewTenant.mEditTenantMemberDetails(itenantMemberId, smemberName, sRelation, smemDob, smobileNo, semail,iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("tenantMember Id: "+objStr);
								out.println(objStr);
					    		break;
					    	}
					    	case 8:
					    	{
					    		System.out.println("DeleteTenant");
								//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
								//int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
								System.out.println("Society Id :"+iSocietyID);
								
								int iTenantID = Integer.parseInt(request.getParameter("tenantId").toString());
								System.out.println("iTenantID : "+iTenantID);
								HashMap rows = ViewTenant.mDeleteTenant(iTenantID,iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("tenantMember Id: "+objStr);
								out.println(objStr);
					    		break;
					    	}
					    	default:
					    	{
					    		break;
					    	}
					    }
					}
					else
					{
						out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
					}
				}
				else
				{
					out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}");
				}
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			out.println("{\"success\":0,\"response\":{\"message\":\"Exception Caught\" : "+e+"}} ");
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
