package serviceprovider;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import MainClasses.CommonBaseClass;
import MainClasses.Login;
import MainClasses.ProjectConstants;
//import MainClasses.Register;
import MainClasses.ServiceProvider;
import MainClasses.ViewClassifieds;
import MainClasses.ViewEvents;
import MainClasses.ViewServiceProvider;
import Utility.UtilityClass;
import Utility.VerifyToken;

/**
 * Servlet implementation class Login
 */
//@WebServlet("/ServiceProvider")
@WebServlet(urlPatterns = {"/ServiceProvider/*" })
public class ServiceProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceProviderServlet() {
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
		System.out.println("URI :"+ sURI);
		try {
			//ViewServiceProvider objServPro =  new ViewServiceProvider(true, "hostmjbt_societydb");
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			ViewServiceProvider objServiceProvider = new ViewServiceProvider(sToken, true, sTkey);
			
			if(objServiceProvider.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objServiceProvider.getDecryptedTokenMap();
				
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					
					Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
					System.out.println("SocietyID :"+ iSocietyID);
					System.out.println("URI :"+ sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/selectmy"));
					if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/selectall"))
					{	
						
						HashMap rows = ViewServiceProvider.mServiceProvider(iSocietyID,0);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						out.println(objStr);
						
					 }
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/selectmy"))
					{	
						
						HashMap rows = ViewServiceProvider.mServiceProvider(iSocietyID,Integer.parseInt((String)DecryptedTokenMap.get("unit_id")));
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						out.println(objStr);
						
					 }
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/pending"))
					{	
						
						HashMap rows = ViewServiceProvider.mServiceProvider(iSocietyID,-1);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						out.println(objStr);
						
					 }
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/selectprovider"))
					{
						int iService_prd_reg_id = Integer.valueOf(request.getParameter("providerid"));
						
						HashMap rows = ViewServiceProvider.mProviderDetails(iService_prd_reg_id, iSocietyID,Integer.parseInt((String)DecryptedTokenMap.get("unit_id")));
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						out.println(objStr);
					 } 
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/fetchCategory"))
					{
					
						HashMap rows = ViewServiceProvider.mProviderCategoryDetails();
						//out.println(objHash);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						out.println(objStr);
					 }
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/fetchDocuments"))
					{
					
						HashMap rows = ViewServiceProvider.mProviderDocument();
						//out.println(objHash);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						out.println(objStr);
					 }
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/fetchUnits"))
					{
						System.out.println("Call Function");
					
						HashMap rows = ViewServiceProvider.mProviderUnitList(iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						out.println(objStr);
					 }
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/addServiceProvider"))
					{    // Old Version Code
						 Date DateOfBirth = (Date) UtilityClass.getDBFormatDate(request.getParameter("dob"));
						 Date WorkingSince = (Date) UtilityClass.getDBFormatDate(request.getParameter("working_since"));
						 String message=new String();
						  if(!request.getParameterMap().containsKey("name") || request.getParameter("name") == "")
						  {  	
					    	  message = "Please enter Provider Name";  	
					      }  
						  else if(!request.getParameterMap().containsKey("cat_id") || request.getParameter("cat_id") == "")
						  {  	
					    	  message = "Please Select Category";  	
					      }  
						  else if(!request.getParameterMap().containsKey("dob") || request.getParameter("dob") == ""){  	
					    	  message = "Please Select Date Date of Birth ";  	
					       }  
		
						  else
						  {
							
							  String iCatID = request.getParameter("cat_id");
							  String sName = request.getParameter("name");
							  String sDate_Of_Birth = request.getParameter("dob");
							  String sWorking_since= request.getParameter("working_since");
							  String sIdentityMark = request.getParameter("identity_mark");
							  String sEducation = request.getParameter("education");
							  String sMarried = request.getParameter("married");
							  String sPhoto = request.getParameter("img");
							  String sPhoto_thumb = request.getParameter("img"); 
							  String sCurrentAdd = request.getParameter("cur_add");
							  String sCurrent_ContNo = request.getParameter("cont_no");
							  String sCureentAltNo = request.getParameter("alt_cont_no");
							  String sNativeAddress = request.getParameter("per_add");
							  String sNativeContact= request.getParameter("per_cont_no");
							  String sNativeAltCont = request.getParameter("per_alt_cont_no");
							  String sRefName = request.getParameter("ref_name");
							  String sRefAdd = request.getParameter("ref_add");
							  String sRefContact = request.getParameter("ref_cont_no");
							  String sRefAltCont = request.getParameter("ref_alt_cont_no");  
							  String sFatherName = request.getParameter("f_name");
							  String sFatherOccu = request.getParameter("f_occu");
							  String sMotherNam = request.getParameter("m_name");
							  String sMotherOccu = request.getParameter("m_occu");
							  String sHusWifeName = request.getParameter("hus_wife_name");
							  String sHusWifeOccu = request.getParameter("hus_wife_occu");
							  String sSonDotName = request.getParameter("son_dot_name");
							  String sSonDotOccu = request.getParameter("son_dot_occu");
							  String sOtherName = request.getParameter("other_name");
							  String sOtherOccu = request.getParameter("other_occu");
							  String iUnitID =request.getParameter("unit_id");
							  String sDoc_id=request.getParameter("document_id");
							  String sDoc_img=request.getParameter("Doc_img");
							
							  	HashMap<Integer, Map<String, Object>> ServiceReqID = ViewServiceProvider.mAddServiceProvider_Partial(iSocietyID,sName,iCatID,sDate_Of_Birth,sIdentityMark,sWorking_since,sEducation,sMarried,sPhoto,sPhoto_thumb);
							  	System.out.println("return:"+ServiceReqID);
							  	//  HashMap<Integer, Map<String, Object>> ServiceReqID = ViewServiceProvider.mAddServiceProvider_Partial(iSocietyID,sName,iCatID,sDate_Of_Birth,sIdentityMark,sWorking_since,sEducation,sMarried,sPhoto,sPhoto_thumb);
								 long  ServiceProRegID=0;
								 Map<String, Object> mapResponse = ServiceReqID.get("response");
								 System.out.println("id:"+mapResponse.get("ServiceProID").toString());
								 String sServiceProRegID= mapResponse.get("ServiceProID").toString();
								 ServiceProRegID = Long.parseLong(sServiceProRegID);
								
									  ViewServiceProvider.mUpdateServiceProvider_ContactDetails(ServiceProRegID,sCurrentAdd,sCurrent_ContNo,sCureentAltNo,sNativeAddress,sNativeContact,sNativeAltCont,sRefName,sRefAdd,sRefContact,sRefAltCont);
								
									  ViewServiceProvider.mUpdateServiceProvider_FamilyDetails(ServiceProRegID,sFatherName,sFatherOccu,sMotherNam,sMotherOccu,sHusWifeName,sHusWifeOccu,sSonDotName,sSonDotOccu,sOtherName,sOtherOccu);

									  ViewServiceProvider.mUpdateServiceProvider_UpdatoOkUnit(ServiceProRegID,iSocietyID,iUnitID,true);
							
									  ViewServiceProvider.mUpdateServiceProvider_Document(ServiceProRegID,sDoc_id,sDoc_img);
									  Gson objGson = new Gson();
							  String objStr = objGson.toJson(ServiceReqID);
							  response.setContentType("application/json");
							  out.println(objStr);
						  }
						 
					
						  if(!message.equals(""))
						  {  	
						      out.println("{\"success\":0,\"response\":{\"message\":\""+message+"\"}}   ");  	
						  } 
						  	
					 }
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/addServiceProvider1"))
					{ // New  Version Code
						 Date DateOfBirth = (Date) UtilityClass.getDBFormatDate(request.getParameter("dob"));
						 Date WorkingSince = (Date) UtilityClass.getDBFormatDate(request.getParameter("working_since"));
						 String message=new String();
						 if(!request.getParameterMap().containsKey("staffid") || request.getParameter("staffid") == ""){  	
					    	  message = "Please Select Society's Staff Id ";  	
					       }
						 else if(!request.getParameterMap().containsKey("name") || request.getParameter("name") == "")
						  {  	
					    	  message = "Please enter Provider Name";  	
					      }  
						  else if(!request.getParameterMap().containsKey("cat_id") || request.getParameter("cat_id") == "")
						  {  	
					    	  message = "Please Select Category";  	
					      }  
						  else if(!request.getParameterMap().containsKey("dob") || request.getParameter("dob") == ""){  	
					    	  message = "Please Select Date Date of Birth ";  	
					       }  
						  
						  else
						  {
							  String msg = ServiceProvider.fetchstaffidstatus(iSocietyID,request.getParameter("staffid"));
							  System.out.println("message : "+msg);
							  if(msg.equals("Exist"))
							  {
								message = "Please Enter Unique Society Staff Id";  
							  }
							  else
							  {
							  String soc_staff_id = request.getParameter("staffid");
							  String iCatID = request.getParameter("cat_id");
							  String sName = request.getParameter("name");
							  String sDate_Of_Birth = request.getParameter("dob");
							  String sWorking_since= request.getParameter("working_since");
							  String sIdentityMark = request.getParameter("identity_mark");
							  String sEducation = request.getParameter("education");
							  String sMarried = request.getParameter("married");
							  String sPhoto = request.getParameter("img");
							  String sPhoto_thumb = request.getParameter("img"); 
							  String sCurrentAdd = request.getParameter("cur_add");
							  String sCurrent_ContNo = request.getParameter("cont_no");
							  String sCureentAltNo = request.getParameter("alt_cont_no");
							  String sNativeAddress = request.getParameter("per_add");
							  String sNativeContact= request.getParameter("per_cont_no");
							  String sNativeAltCont = request.getParameter("per_alt_cont_no");
							  String sRefName = request.getParameter("ref_name");
							  String sRefAdd = request.getParameter("ref_add");
							  String sRefContact = request.getParameter("ref_cont_no");
							  String sRefAltCont = request.getParameter("ref_alt_cont_no");  
							  String sFatherName = request.getParameter("f_name");
							  String sFatherOccu = request.getParameter("f_occu");
							  String sMotherNam = request.getParameter("m_name");
							  String sMotherOccu = request.getParameter("m_occu");
							  String sHusWifeName = request.getParameter("hus_wife_name");
							  String sHusWifeOccu = request.getParameter("hus_wife_occu");
							  String sSonDotName = request.getParameter("son_dot_name");
							  String sSonDotOccu = request.getParameter("son_dot_occu");
							  String sOtherName = request.getParameter("other_name");
							  String sOtherOccu = request.getParameter("other_occu");
							  String iUnitID = request.getParameter("unit_id");
							  String sDoc_id=request.getParameter("document_id");
							  String sDoc_img=request.getParameter("Doc_img");
							
							  	HashMap<Integer, Map<String, Object>> ServiceReqID = ViewServiceProvider.mAddServiceProvider_Partial1(iSocietyID,sName,iCatID,sDate_Of_Birth,sIdentityMark,sWorking_since,sEducation,sMarried,sPhoto,sPhoto_thumb,sCurrent_ContNo,soc_staff_id);
							  	System.out.println("return:"+ServiceReqID);
							  	//  HashMap<Integer, Map<String, Object>> ServiceReqID = ViewServiceProvider.mAddServiceProvider_Partial(iSocietyID,sName,iCatID,sDate_Of_Birth,sIdentityMark,sWorking_since,sEducation,sMarried,sPhoto,sPhoto_thumb);
								 long  ServiceProRegID=0;
								 Map<String, Object> mapResponse = ServiceReqID.get("response");
								 System.out.println("id:"+mapResponse.get("ServiceProID").toString());
								 String sServiceProRegID= mapResponse.get("ServiceProID").toString();
								 String[] staff = sServiceProRegID.split(";");
								 String staffid = staff[0];
								 String staff_test = staff[1];
								 ServiceProRegID = Long.parseLong(staffid);
								if(staff_test.equals("1"))
								{
									ViewServiceProvider.mUpdateServiceProvider_ContactDetails(ServiceProRegID,sCurrentAdd,sCurrent_ContNo,sCureentAltNo,sNativeAddress,sNativeContact,sNativeAltCont,sRefName,sRefAdd,sRefContact,sRefAltCont);
									
									ViewServiceProvider.mUpdateServiceProvider_FamilyDetails(ServiceProRegID,sFatherName,sFatherOccu,sMotherNam,sMotherOccu,sHusWifeName,sHusWifeOccu,sSonDotName,sSonDotOccu,sOtherName,sOtherOccu);

									ViewServiceProvider.mUpdateServiceProvider_UpdatoOkUnit(ServiceProRegID,iSocietyID,iUnitID,true);
							
									ViewServiceProvider.mUpdateServiceProvider_Document(ServiceProRegID,sDoc_id,sDoc_img);
	
								}
								else
								{
									ViewServiceProvider.mUpdateServiceProvider_UpdatoOkUnit(ServiceProRegID,iSocietyID,iUnitID,true);
									
								}
							 Gson objGson = new Gson();
							  String objStr = objGson.toJson(ServiceReqID);
							  response.setContentType("application/json");
							  out.println(objStr);
							  }
						  }
						 
					
						  if(!message.equals(""))
						  {  	
						      out.println("{\"success\":0,\"response\":{\"message\":\""+message+"\"}}   ");  	
						  } 
						  	
					 }
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/updatePersonal"))
					{ 
						  int iServiceProviderID = Integer.valueOf(request.getParameter("service_prd_id"));
						  String sProviderName = request.getParameter("provider_name");
						  String sProviderDOB = request.getParameter("DateOfBirth");
						  String sProviderIdentymark= request.getParameter("IdentyMark");
						  String sProviderW_since = request.getParameter("WorkingSince");
						  String sMarritalStatus = request.getParameter("MarritalStatus");
						  String sMobile = request.getParameter("MobileNo");
						  String sAltMobile = request.getParameter("AltMobileNo");
						
						  HashMap<Integer, Map<String, Object>> UpdateServiceReq = ViewServiceProvider.mUpdatePersonalDetails(iSocietyID,iServiceProviderID,sProviderName,sProviderDOB,sProviderIdentymark,sProviderW_since,sMarritalStatus,sMobile,sAltMobile);
						  Gson objGson = new Gson();
						  String objStr = objGson.toJson(UpdateServiceReq);
						  response.setContentType("application/json");
						  out.println(objStr);
					}
						
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/updateContact"))
					{
						int iServiceProviderID = Integer.valueOf(request.getParameter("service_prd_id"));
						  String sCurrentAdd = request.getParameter("CurrentAdd");
						  String sCurrent_ContNo = request.getParameter("NativeContactNo");
						  String sCureentAltNo= request.getParameter("AltContact");
						  String sNativeAddress = request.getParameter("PermanentAdd");
						  String sNativeContact = request.getParameter("Permanet_contact");
						  String sNativeAltCont = request.getParameter("Permanet_Alt_contact");
						  String sRefName = request.getParameter("RefName");
						  String sRefAdd = request.getParameter("RefAdd");
						  String sRefContact = request.getParameter("RefContact");
						  String sRefAltCont = request.getParameter("RefAltContact");
						  long UpdateContactDetails = ViewServiceProvider.mUpdateServiceProvider_ContactDetails(iServiceProviderID,sCurrentAdd,sCurrent_ContNo,sCureentAltNo,sNativeAddress,sNativeContact,sNativeAltCont,sRefName,sRefAdd,sRefContact,sRefAltCont);
						  Gson objGson = new Gson();
						  out.println("{\"response\":{\"message\":\"Contact Details Update Successfully\",\"UpdateContact\":\""+UpdateContactDetails+"\"},\"success\":1}");
							
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/updateFamily"))
					{
						int iServiceProviderID = Integer.valueOf(request.getParameter("service_prd_id"));
						  String sFatherName = request.getParameter("FatherName");
						  String sFatherOccu = request.getParameter("FatherOccu");
						  String sMotherNam= request.getParameter("MotherName");
						  String sMotherOccu = request.getParameter("MotherOccu");
						  String sHusWifeName = request.getParameter("HusWifeName");
						  String sHusWifeOccu = request.getParameter("HusWifeOccu");
						  String sSonDotName = request.getParameter("SonDouName");
						  String sSonDotOccu = request.getParameter("SonDouOccu");
						  String sOtherName = request.getParameter("OtherName");
						  String sOtherOccu = request.getParameter("OtherName");
						  long UpdateFamilyDetails =ViewServiceProvider.mUpdateServiceProvider_FamilyDetails(iServiceProviderID,sFatherName,sFatherOccu,sMotherNam,sMotherOccu,sHusWifeName,sHusWifeOccu,sSonDotName,sSonDotOccu,sOtherName,sOtherOccu);
						  Gson objGson = new Gson();
						  out.println("{\"response\":{\"message\":\"Family Details Update Successfully\",\"UpdateFamily\":\""+UpdateFamilyDetails+"\"},\"success\":1}");
							
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/assign"))
					{
						int iServiceProviderID = Integer.valueOf(request.getParameter("providerid"));
						HashMap<Integer, Map<String, Object>> UpdateAssign =ViewServiceProvider.mAssignTome(iSocietyID,iServiceProviderID,Integer.parseInt((String)DecryptedTokenMap.get("unit_id")));
						Gson objGson = new Gson();
						String objStr = objGson.toJson(UpdateAssign);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/remove"))
					{
						int iServiceProviderID = Integer.valueOf(request.getParameter("providerid"));
						HashMap<Integer, Map<String, Object>> RemoveUnit =ViewServiceProvider.mRemoveUnit(iSocietyID,iServiceProviderID,Integer.parseInt((String)DecryptedTokenMap.get("unit_id")));
						Gson objGson = new Gson();
						String objStr = objGson.toJson(RemoveUnit);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/submitComment"))
					{
						Integer iMemberID = Integer.parseInt((String) DecryptedTokenMap.get("id"));
						int iServiceProviderID = Integer.valueOf(request.getParameter("serviceProvideID"));
						String sComments = request.getParameter("comment");
						int iRating = Integer.valueOf(request.getParameter("rating"));
						HashMap<Integer, Map<String, Object>> SubmitComment =ViewServiceProvider.mAddComments(iServiceProviderID,iMemberID,(String)DecryptedTokenMap.get("name"),sComments,iRating,0);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(SubmitComment);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/Approved"))
					{
						int iServiceProviderID = Integer.valueOf(request.getParameter("providerid"));
						HashMap<Integer, Map<String, Object>> ApprovedProviders =ViewServiceProvider.mProviderApproved(iServiceProviderID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(ApprovedProviders);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/Removeit"))
					{
						
						int iServiceProviderID = Integer.valueOf(request.getParameter("providerid"));
						HashMap<Integer, Map<String, Object>> RemovedProviders =ViewServiceProvider.mProviderRemoved(iServiceProviderID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(RemovedProviders);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/BlockProvider"))
					{
						
						int iServiceProviderID = Integer.valueOf(request.getParameter("serviceProviderID"));
						String sComments = request.getParameter("Comments");
						HashMap<Integer, Map<String, Object>> RemovedProviders =ViewServiceProvider.mProviderBlocked(iSocietyID,iServiceProviderID,sComments);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(RemovedProviders);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/updateDocument"))
					{
						int iServiceProviderID = Integer.valueOf(request.getParameter("service_prd_id"));
						String sDoc_id=request.getParameter("document_id");
						String sDoc_img ="";
						HashMap<Integer, Map<String, Object>> UpadateDocument =ViewServiceProvider.mAddMore_Document(iServiceProviderID,sDoc_id,sDoc_img);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(UpadateDocument);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/ProviderReport"))
					{
						String StartDate=request.getParameter("StartDate").trim();
						String EndDate=request.getParameter("EndDate").trim();
						int iServiceProviderID = Integer.valueOf(request.getParameter("providerid"));
						HashMap<Integer, Map<String, Object>> UpadateDocument =ViewServiceProvider.mProviderReport(iServiceProviderID,iSocietyID,StartDate,EndDate);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(UpadateDocument);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/addMoreInfo"))
					{
						String sComment=request.getParameter("comment").trim();
						int iServiceProviderID = Integer.valueOf(request.getParameter("providerid"));
						HashMap<Integer, Map<String, Object>> UpadateDocument =ViewServiceProvider.mProviderSetMoreDetailsForApproval(iServiceProviderID,sComment,iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(UpadateDocument);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/GetStaffIdStatus"))
					{
					HashMap objHash = ViewServiceProvider.getStaffStatus(iSocietyID);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
					}
					
					/*else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/unitstatigy"))
					{
						
						HashMap<Integer, Map<String, Object>> UnitStatigy =ViewServiceProvider.mSocietyUnitStats(iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(UnitStatigy);
						response.setContentType("application/json");
						out.println(objStr);
					}*/
					else
					{
						out.println("{\"success\":0,\"response\":{\"message\":\"URI error\" : "+sURI+"}} ");
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
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			out.println("{\"success\":0,\"response\":{\"message\":\"Exception Caught\" : "+e+"}} ");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
