package owner;

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
import MainClasses.DateFunctionality;
import MainClasses.ProjectConstants;
//import MainClasses.Register;
import MainClasses.ServiceProvider;
import MainClasses.ViewOwner;
import MainClasses.ViewServiceProvider;
import Utility.UtilityClass;
import Utility.VerifyToken;

/**
 * Servlet implementation class Login
 */
//@WebServlet("/ServiceProvider")
@WebServlet(urlPatterns = {"/OwnerServlet/*" })
public class OwnerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OwnerServlet() {
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
		
		int ch=Integer.parseInt(request.getParameter("mode"));
	    System.out.println("Mode: "+ch);
	    
		try {
			//ViewServiceProvider objServPro =  new ViewServiceProvider(true, "hostmjbt_societydb");
			String sToken = request.getParameter("token").trim();
			if(sToken.equals(""))
			{
				// Vaishali code 
				//System.out.println("in if");
				//System.out.println("Url :"+sURI);
				//System.out.println("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/OwnerServlet/GetOwnerDetails");
				switch (ch)
			    {  
			    	case 1:
			    	{
			    		int iSocietyId = Integer.parseInt(request.getParameter("societyId"));
						System.out.println("Society Id :"+iSocietyId);
						int unitId = Integer.parseInt(request.getParameter("unitId"));
						System.out.println("Unit Id :"+unitId);
						HashMap rows = ViewOwner.fetchOwnersAllDetails(unitId,iSocietyId);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						System.out.println("OwnerDetails: "+objStr);
						out.println(objStr);
			    		break;
			    	}
			    	case 2:
			    	{
			    		System.out.println("in if2");
						//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
						int iSocietyId = Integer.parseInt(request.getParameter("societyId"));
						System.out.println("Society Id :"+iSocietyId);
						int imemberId = Integer.parseInt(request.getParameter("memberId"));
						System.out.println("memberId :"+imemberId);
						String sdob = request.getParameter("dob").toString();
						System.out.println("sdob :"+sdob);
						String sgender = request.getParameter("gender").toString();
						System.out.println("sgender :"+sgender);
						//HashMap rows = ViewOwner.updateOwner(iSocietyId, imemberId, sdob, sgender);
						Gson objGson = new Gson();
						//String objStr = objGson.toJson(rows);
						response.setContentType("application/json");
						//System.out.println("OwnerDetails: "+objStr);
						//out.println(objStr);
			    		break;
			    	}
				/*if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/OwnerServlet/GetOwnerDetails"))
				{	
					//System.out.println("in if2");
					//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
					int iSocietyId = Integer.parseInt(request.getParameter("societyId"));
					System.out.println("Society Id :"+iSocietyId);
					int unitId = Integer.parseInt(request.getParameter("unitId"));
					System.out.println("Unit Id :"+unitId);
					HashMap rows = ViewOwner.fetchOwnersAllDetails(unitId,iSocietyId);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(rows);
					response.setContentType("application/json");
					System.out.println("OwnerDetails: "+objStr);
					out.println(objStr);
				 }
				 else if(sURI.equals("/" + Utility.ProjectConstants.WAR_FILE_NAME + "/OwnerServlet/updateOwnerDetails"))
				 {	
					System.out.println("in if2");
					//int iSoc_id = Integer.valueOf(request.getParameter("society_id"));
					int iSocietyId = Integer.parseInt(request.getParameter("societyId"));
					System.out.println("Society Id :"+iSocietyId);
					int imemberId = Integer.parseInt(request.getParameter("memberId"));
					System.out.println("memberId :"+imemberId);
					String sdob = request.getParameter("dob").toString();
					System.out.println("sdob :"+sdob);
					String sgender = request.getParameter("gender").toString();
					System.out.println("sgender :"+sgender);
					HashMap rows = ViewOwner.updateOwner(iSocietyId, imemberId, sdob, sgender);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(rows);
					response.setContentType("application/json");
					System.out.println("OwnerDetails: "+objStr);
					out.println(objStr);
				 }*/
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
				HashMap DecryptedTokenMap = objServiceProvider.getDecryptedTokenMap();
				if(objServiceProvider.IsTokenValid() == true)
				{				
					Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
					switch (ch)
				    {
				    	case 1:
				    	{
				    		System.out.println("in if2");
							System.out.println("Society Id :"+iSocietyID);
							int unitId = Integer.parseInt(request.getParameter("unitId"));
							System.out.println("Unit Id :"+unitId);
							HashMap rows = ViewOwner.fetchOwnersAllDetails(unitId,iSocietyID);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(rows);
							response.setContentType("application/json");
							out.println(objStr);
				    		break;
				    	}
				    	case 2:
				    	{
				    		int imemberId =Integer.parseInt(request.getParameter("MemberID"));
				    		int iOtherMemberId =Integer.parseInt(request.getParameter("OtherMemID"));
				    		//int iAddPOA =Integer.parseInt(request.getParameter("AddPOA"));
							/*String sOwnerdob = request.getParameter("OwnerDob").toString();
							String OwnerDOB = DateFunctionality.convertToStandardFormat(sOwnerdob);
							String sgender = request.getParameter("OwnerGender").toString();
							String sOwnerProfession = request.getParameter("OwnerProfession").toString();
							String aOwnerAdd = request.getParameter("OwnerAdd").toString();
							String sOwnerCity = request.getParameter("OwnerCity").toString();
							String sPinCode = request.getParameter("OwnerPinCode").toString();
							String sCoOwnerName = request.getParameter("CoOwnerName").toString();
							String sCoOwnerPro = request.getParameter("CoOwnerPro").toString();
							String sCoOwnerdob = request.getParameter("CoOwnerDob").toString();
							String CoOwnerDOB = DateFunctionality.convertToStandardFormat(sCoOwnerdob);*/
							String sNameOfPOA = request.getParameter("NamePOA").toString();
							String sPOAAdhaar = request.getParameter("POAAdhaar").toString();
							HashMap rows = ViewOwner.updateOwner(iSocietyID, imemberId,iOtherMemberId, sNameOfPOA, sPOAAdhaar);
							//HashMap rows = ViewOwner.updateOwner(iSocietyID, imemberId, OwnerDOB, sgender);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(rows);
							response.setContentType("application/json");
							System.out.println("OwnerDetails: "+objStr);
							out.println(objStr);
				    		break;
				    	}
					
				    }
				}
				else
				{
					out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
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
