package RentalAgreement;

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
import MainClasses.ProjectConstants;
import MainClasses.ServiceProvider;
import MainClasses.ViewOwner;
import MainClasses.ViewRentalAgreement;
import MainClasses.ViewServiceProvider;
import MainClasses.ViewTenant;
import Utility.UtilityClass;
import Utility.VerifyToken;

/**
 * Servlet implementation class RentalAgreementServlet
 */
@WebServlet("/RentalAgreementServlet/*")
public class RentalAgreementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RentalAgreementServlet() {
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
			    		System.out.println("getAgreementDetailsByUnitId");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int unitId = Integer.parseInt(request.getParameter("unitId").toString());
						System.out.println("Unit Id :"+unitId);
						
						
						HashMap rows = ViewRentalAgreement.fetchAllRentalAgreementByUnitId(unitId, iSocietyId);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						System.out.println("AgreementDetails: "+objStr);
						out.println(objStr);
						break;
			    	}
			    	case 2:
			    	{
			    		System.out.println("InsertAgreement");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						int iunitId = Integer.parseInt(request.getParameter("unitId").toString());
						System.out.println("Unit Id :"+iunitId);
						
						int iPropertyUse = Integer.parseInt(request.getParameter("PropertyUse").toString());
						System.out.println("PropertyUse  :"+iPropertyUse);
						
						String sPropertyAdd = request.getParameter("PropertyAdd");
						System.out.println("sPropertyAdd :"+sPropertyAdd);

						String sPinCode = request.getParameter("PinCode");
						System.out.println("sPinCode  :"+sPinCode);
						
						String scity = request.getParameter("city");
						System.out.println("city  :"+scity);
						
						String sRegion = request.getParameter("Region");
						System.out.println("sRegion  :"+sRegion);
						
						String sDeposit = request.getParameter("Deposit");
						System.out.println("sDeposit  :"+sDeposit);
						
						int iRentType = Integer.parseInt(request.getParameter("RentType").toString());
						System.out.println("RentType :"+iRentType);
						
						String sMiscClauses = request.getParameter("MiscClauses").replaceAll("%20", " ");
						System.out.println("sMiscClauses :"+sMiscClauses);
						
						String sPOAName = request.getParameter("POAName").replaceAll("%20", " ");
						System.out.println("sPOAName :"+sPOAName);
						
						String sPOAAdharNo = request.getParameter("POAAdharNo").replaceAll("%20", " ");
						System.out.println("sPOAAdharNo :"+sPOAAdharNo);
						
						//HashMap rows = ViewRentalAgreement.mInsertNewAgreement(iunitId, iPropertyUse, sPropertyAdd, sPinCode, scity, sRegion, sDeposit, iRentType, sMiscClauses, sPOAName, sPOAAdharNo, iSocietyId);
						Gson objGson = new Gson();
						//String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						//System.out.println("tenantId: "+objStr);
						//out.println(objStr);
			    		break;
			    	}
			    	case 3:
			    	{
			    		System.out.println("fetchAllAgreements");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId").toString());
						System.out.println("Society Id :"+iSocietyId);
						
						HashMap rows = ViewRentalAgreement.fetchAllRentalAgreement(iSocietyId);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						System.out.println("All Agreement Details: "+objStr);
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
					Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
					if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
					{
						//System.out.println("" + Utility.ProjectConstants.WAR_FILE_NAME + "/ServiceProvider/selectprovider");
						//out.print("token Valid : " + sURI);
						//out.print("Society ID : " + iSocietyID);
						switch (ch)
					    {
						    case 1:
					    	{
					    		System.out.println("getAgreementDetailsByUnitId");
								
								System.out.println("Society Id :"+iSocietyID);
								int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
								//int unitId = Integer.parseInt(request.getParameter("unitId").toString());
								//System.out.println("Unit Id :"+unitId);
								
								
								HashMap rows = ViewRentalAgreement.fetchAllRentalAgreementByUnitId(iUnitId, iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("AgreementDetails: "+objStr);
								out.println(objStr);
								break;
					    	}
					    	case 2:
					    	{
					    		System.out.println("InsertAgreement");
								
					    		int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
								int iTenantID = Integer.parseInt(request.getParameter("TanantID").toString());
								int iPropertyType = Integer.parseInt(request.getParameter("PropertyType").toString());
								int iInRural = Integer.parseInt(request.getParameter("P_In_rural").toString());
								int iUnitOutOfSociety = Integer.parseInt(request.getParameter("UnitOutOfSociety").toString());
								int iPropertyUse = Integer.parseInt(request.getParameter("PropertyUse").toString());
								String sPropertyAdd = request.getParameter("PropertyAdd");
								String sPinCode = request.getParameter("Pincode");
								String scity = request.getParameter("city");
								String sRegion = request.getParameter("Region");
								String sStartDate = request.getParameter("LeaseStart");
								String sEndDate = request.getParameter("LeaseEnd");
								int iRentType = Integer.parseInt(request.getParameter("RentType").toString());
								String sPOAName = request.getParameter("POAName");
								String sPOAAdharNo = request.getParameter("POAAdharNo");
								String sMonthlyRent = request.getParameter("MonthlyRent");
								String VaryingMonth = request.getParameter("VaryingMonth");
								String sDeposit = request.getParameter("RefundAmount");
								String sRentVaring = request.getParameter("TenantFamilyJSON");
								
								String sMiscClauses="";
								String sRentDetails = "";
								//sMiscClauses= request.getParameter("MiscClauses").replaceAll("%20", " ");
								if(iRentType == 1)
					    		{
					    			sMonthlyRent = request.getParameter("MonthlyRent").toString();
					    			System.out.println("sMonthlyRent : "+sMonthlyRent);
					    		}
					    		else
					    		{
					    			sRentDetails = request.getParameter("VeringRentJSON");
					    			///String sTenantFamilyDetails = request.getParameter("TenantFamilyJSON");
					    			sRentDetails = java.net.URLDecoder.decode(sRentDetails, "UTF-8");
					    		}
								//String sPOAName = request.getParameter("POAName").replaceAll("%20", " ");
								//String sPOAAdharNo = request.getParameter("POAAdharNo").replaceAll("%20", " ");
								//HashMap rows = ViewRentalAgreement.mInsertNewAgreement(iUnitId, iPropertyType, sPropertyAdd, sPincode, sCity, sRegion, sDeposit, iRentType, sMiscClause, sPOAName, sPOAadhaar, iSocietyId, sRentDetails, sMonthlyRent, sStartDate, sEndDate)
								try
								{
									HashMap rows = ViewRentalAgreement.mInsertNewAgreement(iUnitId, iPropertyUse,iPropertyType, sPropertyAdd, sPinCode, scity, sRegion, sDeposit, iRentType,sMiscClauses, sPOAName, sPOAAdharNo, iSocietyID,sRentDetails,sMonthlyRent,sStartDate,sEndDate,iTenantID,iInRural,iUnitOutOfSociety);
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
					    	
					    	case 3:
					    	{
					    		System.out.println("fetchAllAgreements");
								//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
								
								System.out.println("Society Id :"+iSocietyID);
								
								HashMap rows = ViewRentalAgreement.fetchAllRentalAgreement(iSocietyID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("All Agreement Details: "+objStr);
								out.println(objStr);
					    		break;
					    	}
					    	
					    	//Add New Function in fetch specific Agreement data 
					    	case 4:
					    	{
					    		System.out.println("fetchAgreementsUsing TanentID");
								//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
								
								System.out.println("Society Id :"+iSocietyID);
								int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
								int iTenantID = Integer.parseInt(request.getParameter("tenantId").toString());
								HashMap rows = ViewRentalAgreement.fetchRentalAgreementByTenantID(iSocietyID,iUnitId,iTenantID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("All Agreement Details: "+objStr);
								out.println(objStr);
					    		break;
					    	}
					    	case 5:
					    	{
					    		System.out.println("Update Clauses");
								//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
								
								System.out.println("Society Id :"+iSocietyID);
								int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
								int iTenantID = Integer.parseInt(request.getParameter("tenantId").toString());
								int iAgreementID = Integer.parseInt(request.getParameter("AgreementID").toString());
								String sMiscClauses = request.getParameter("Comment");
								String sAppoinmentDate = request.getParameter("aDate");
								String sAppoinmentTime = request.getParameter("aTime");
								HashMap rows = ViewRentalAgreement.UpdateAgreementComment(iSocietyID,iUnitId,iAgreementID,sMiscClauses,iTenantID,sAppoinmentDate,sAppoinmentTime);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("All Agreement Details: "+objStr);
								out.println(objStr);
					    		break;
					    	}
					    	case 6:
					    	{
					    		
					    		System.out.println("Update Agreement");
								int iRentalID = Integer.parseInt(request.getParameter("RID").toString());
								int iTenantID = Integer.parseInt(request.getParameter("TID").toString());
								int iPropertyType = Integer.parseInt(request.getParameter("PropertyType").toString());
								int iPropertyUse = Integer.parseInt(request.getParameter("PropertyUse").toString());
								int iRentType = Integer.parseInt(request.getParameter("RentType").toString());
								String sMonthlyRent = request.getParameter("MonthlyRent");
								String sDeposit = request.getParameter("RdepositeAmount");
								String sRentDetails = "";
								if(iRentType == 1)
					    		{
					    			sMonthlyRent = request.getParameter("MonthlyRent").toString();
					    			System.out.println("sMonthlyRent : "+sMonthlyRent);
					    		}
					    		else
					    		{
					    			sRentDetails = request.getParameter("VaringTypeJSON");
					    			sRentDetails = java.net.URLDecoder.decode(sRentDetails, "UTF-8");
					    		}
								try
								{
									HashMap rows = ViewRentalAgreement.mUpadateAgreemnet(iSocietyID,iRentalID, iTenantID, iPropertyType, iPropertyUse, iRentType, sMonthlyRent, sDeposit, sRentDetails);
									Gson objGson = new Gson();
									String objStr = objGson.toJson(rows);
									response.setContentType("application/json");
								//System.out.println("tenantId: "+objStr);
									out.println(objStr);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
					    		break;
					    	}
					    	/*case 7:
					    	{
					    		System.out.println("Update Clauses");
								//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
								
								System.out.println("Society Id :"+iSocietyID);
								int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
								//int iTenantID = Integer.parseInt(request.getParameter("tenantId").toString());
								int iAgreementID = Integer.parseInt(request.getParameter("AgreementID").toString());
								String sMiscClauses = request.getParameter("Comment");
								HashMap rows = ViewRentalAgreement.UpdateAgreementComment1(iSocietyID,iUnitId,iAgreementID,sMiscClauses);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(rows);
								response.setContentType("application/json");
								System.out.println("All Agreement Details: "+objStr);
								out.println(objStr);
					    		break;
					    	}*/
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
