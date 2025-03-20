package agreementTermServlet;

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
import com.sun.xml.internal.txw2.Document;

import MainClasses.CommonBaseClass;
import MainClasses.ProjectConstants;
//import MainClasses.Register;
import MainClasses.ServiceProvider;
import MainClasses.ViewAgreementTerm;
import MainClasses.ViewOwner;
import MainClasses.ViewRentalAgreement;
import MainClasses.ViewServiceProvider;
import MainClasses.ViewTenant;
import Utility.UtilityClass;
import Utility.VerifyToken;

/**
 * Servlet implementation class Login
 */
//@WebServlet("/ServiceProvider")
@WebServlet(urlPatterns = {"/AgreementTermServlet/*" })
public class AgreementTermServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AgreementTermServlet() {
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
		
		System.out.println("sURI :"+sURI);
		
		int ch=Integer.parseInt(request.getParameter("mode"));
	    System.out.println("Mode: "+ch);
		
		try {
			//ViewServiceProvider objServPro =  new ViewServiceProvider(true, "hostmjbt_societydb");
			String sToken = request.getParameter("token").trim();
			if(sToken.equals(""))
			{
				//System.out.println("in if");
				//System.out.println("Url :"+sURI);
				//System.out.println("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/OwnerServlet/GetOwnerDetails");
				switch (ch)
			    {
			    	case 1:
			    	{
			    		System.out.println("SendDetailsToDR");
			   
			    		int iSocietyID = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("iSocietyId :"+iSocietyID);
						String iTenantStatus ="0";
						String propertyType = request.getParameter("propertyType");
						System.out.println("propertyType :"+propertyType);
						
						String propertyUse = request.getParameter("propertyUse");
						System.out.println("propertyUse :"+propertyUse);
						
						String pAddress1 = request.getParameter("pAddress1");
						System.out.println("pAddress1 :"+pAddress1);
						
						String pAddress2 = request.getParameter("pAddress2");
						System.out.println("pAddress2 :"+pAddress2);
						
						String pPincode = request.getParameter("pPincode");
						System.out.println("pPincode :"+pPincode);
						
						String pcity = request.getParameter("pcity");
						System.out.println("pcity :"+pcity);
						
						String pregion = request.getParameter("pregion");
						System.out.println("pregion :"+pregion);
						
						String propertyArea = request.getParameter("propertyArea");
						System.out.println("propertyArea :"+propertyArea);
						
						String rentType = request.getParameter("rentType").replaceAll("%20", " ");
						System.out.println("rentType :"+rentType);
						
						System.out.println("request.getParameter(tenantModuleId) :"+request.getParameter("tenantModuleId"));
						
						
						int itenantModuleId = Integer.parseInt(request.getParameter("tenantModuleId").toString());
			    		System.out.println("itenantModuleId :"+itenantModuleId);
			    		
						String deposit = request.getParameter("deposit");
						System.out.println("deposit :"+deposit);
						String monthlyRent = "",var1="",var2="",var3="",rent1="",rent2="",rent3="";
						if(rentType == "Fixed Rent")
						{
							monthlyRent = request.getParameter("monthlyRent").toString();
							System.out.println("monthlyRent :"+monthlyRent);
							//HashMap rows = ViewAgreementTerm.sendDetailsToDR(itenantModuleId,propertyType,propertyUse,pAddress1,pAddress2,pPincode,pcity,pregion,propertyArea,rentType,deposit,rentCount);
						}
						else
						{
							var1 = request.getParameter("var1").toString();
							var2 = request.getParameter("var2").toString();
							var3 = request.getParameter("var3").toString();
							rent1 = request.getParameter("rent1").toString();
							rent2 = request.getParameter("rent2").toString();
							rent3 = request.getParameter("rent3").toString();
						}
						//HashMap rows = ViewAgreementTerm.msendDetailsToDR(itenantModuleId,propertyType,propertyUse,pAddress1,pAddress2,pPincode,pcity,pregion,propertyArea,rentType,deposit,iSocietyID,monthlyRent,iTenantStatus);
					///	Gson objGson = new Gson();
						//String objStr = objGson.toJson(rows);
						//response.setContentType("application/json");
						//System.out.println("Dr Details: "+objStr);
						//out.println(objStr);
						break;
			    	}
			    	case 2:
			    	{
			    		System.out.println("getAgreementTermDetails");
			   
			    		int iSocietyID = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("iSocietyId :"+iSocietyID);
					
						int itenantModuleId = Integer.parseInt(request.getParameter("tenantModuleId").toString());
						System.out.println("itenantModuleId :"+itenantModuleId);
						
						HashMap rows = ViewAgreementTerm.fetchAgreementTermDetails(itenantModuleId, iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						System.out.println("AgreementTermDetails :"+objStr);
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
				String sTkey = request.getParameter("tkey").trim();
				ViewServiceProvider objServiceProvider = new ViewServiceProvider(sToken, true, sTkey);
				if(objServiceProvider.IsTokenValid() == true)
				{
					HashMap DecryptedTokenMap = objServiceProvider.getDecryptedTokenMap();
					if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
					{
						Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
						//out.print("Society ID : " + iSocietyID);
						switch (ch)
					    {
					    	case 1:
					    	{
					    		System.out.println("SendDetailsToDR");
					    		
					    		//int itenantModuleId = Integer.parseInt(request.getParameter("tenantModuleId")); /// get in update tenant
					    		int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
					    		int itenantModuleId = Integer.parseInt(request.getParameter("TanantID").toString());
					    		String iTenantStatus ="0";
					    		iTenantStatus = request.getParameter("tenantStatus");
					    		System.out.println("itenantModuleId :"+itenantModuleId);
							
								String propertyType = request.getParameter("PropertyType");
								String propertyUse = request.getParameter("PropertyUse");
								String pAddress1 = request.getParameter("PropertyAdd");
								String pAddress2 = request.getParameter("city");
								String pPincode = request.getParameter("Pincode");
								String pcity = request.getParameter("pcity");
								String pRural = request.getParameter("P_In_rural");
								
								String pregion = request.getParameter("pregion");
								System.out.println("pregion :"+pregion);
								String LeaseStartDate = request.getParameter("LeaseStart");
								String LeaseEndDate = request.getParameter("LeaseEnd");
								String rentType = request.getParameter("RentType");//.replaceAll("%20", " ");
								String deposit = request.getParameter("RefundAmount");
								String MonthlyRent = request.getParameter("MonthlyRent");
								String VaryingMonth = request.getParameter("VaryingMonth");
								String propertyArea= "";
								String monthlyRent = "";//,var1="",var2="",var3="",rent1="",rent2="",rent3="";
								/*if(rentType == "Fixed Rent")
								{
									monthlyRent = request.getParameter("monthlyRent");
									System.out.println("monthlyRent :"+monthlyRent);
									//HashMap rows = ViewAgreementTerm.sendDetailsToDR(itenantModuleId,propertyType,propertyUse,pAddress1,pAddress2,pPincode,pcity,pregion,propertyArea,rentType,deposit,rentCount);
								}
								else
								{
									var1 = request.getParameter("var1").toString();
									var2 = request.getParameter("var2").toString();
									var3 = request.getParameter("var3").toString();
									rent1 = request.getParameter("rent1").toString();
									rent2 = request.getParameter("rent2").toString();
									rent3 = request.getParameter("rent3").toString();
								}*/
								//HashMap rows = ViewAgreementTerm.msendDetailsToDR(ienantModuleId, propertyType, propertyUse, pAddress1, pAddress2, pPincode, pcity, pregion, propertyArea, rentType, deposit, isocietyId, monthlyRent)
								//HashMap rows = ViewAgreementTerm.msendDetailsToDR(itenantModuleId,propertyType,propertyUse,pAddress1,pAddress2,pPincode,pcity,pregion,propertyArea,rentType,deposit,iSocietyID,monthlyRent);
								int iAgreementID = Integer.parseInt(request.getParameter("AgreementID").toString());
								String sMiscClauses = request.getParameter("Comment");
								String sAppoinmentDate = request.getParameter("aDate");
								String sAppoinmentTime = request.getParameter("aTime");
								System.out.println("Test : " + iAgreementID + " " +  sMiscClauses);
								HashMap rows1 = ViewRentalAgreement.UpdateAgreementComment(iSocietyID,iUnitId,iAgreementID,sMiscClauses,itenantModuleId,sAppoinmentDate,sAppoinmentTime);
								
								
								HashMap rows = ViewAgreementTerm.msendDetailsToDR(itenantModuleId,iUnitId,iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								//System.out.println("Dr Details: "+objStr);
								out.println(objStr);
								break;
					    	}
					    	case 2:
					    	{
					    		System.out.println("getAgreementTermDetails");
					   
					    		//int iSocietyID = Integer.parseInt(request.getParameter("societyId").toString());
								System.out.println("iSocietyId :"+iSocietyID);
							
								int itenantModuleId = Integer.parseInt(request.getParameter("TanantID").toString());
								System.out.println("itenantModuleId :"+itenantModuleId);
								
								HashMap rows = ViewAgreementTerm.fetchAgreementTermDetails(itenantModuleId, iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("AgreementTermDetails :"+objStr);
								out.println(objStr);
								break;
					    	}
					    	case 3:
					    	{
					    		System.out.println("getAgreementTermDetails");
					   
					    		//int iSocietyID = Integer.parseInt(request.getParameter("societyId").toString());
								System.out.println("iSocietyId :"+iSocietyID);
								int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
								int itenantModuleId = Integer.parseInt(request.getParameter("tenantId").toString());
								String propertyType = "1";
								String propertyUse = "1";
								String pAddress1 = "GOkuldham market";
								String pAddress2 = "Mumbai";
								String pPincode = "400063";
								String pcity = "Maharastra";
								String pRural = "Yes";
								
								String pregion = "Goregaon";
								String LeaseStartDate = "2018-01-01";
								String LeaseEndDate = "2019-01-01";
								String rentType = "1";//.replaceAll("%20", " ");
								String deposit = "200000";
								String MonthlyRent ="10000";
								String propertyArea= "";
								String monthlyRent = "";
								//HashMap rows = ViewAgreementTerm.msendDetailsToDR(itenantModuleId,propertyType,propertyUse,pAddress1,pAddress2,pPincode,pcity,pregion,propertyArea,rentType,deposit,iSocietyID,monthlyRent);
							
								int iAgreementID = Integer.parseInt(request.getParameter("AgreementID").toString());
								String sMiscClauses = request.getParameter("Comment");
								String sAppoinmentDate = request.getParameter("aDate");
								String sAppoinmentTime = request.getParameter("aTime");
								HashMap rows1 = ViewRentalAgreement.UpdateAgreementComment(iSocietyID,iUnitId,iAgreementID,sMiscClauses,itenantModuleId,sAppoinmentDate,sAppoinmentTime);
								
								HashMap rows = ViewAgreementTerm.msendDetailsToDR(itenantModuleId,iUnitId,iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("AgreementTermDetails :"+objStr);
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
