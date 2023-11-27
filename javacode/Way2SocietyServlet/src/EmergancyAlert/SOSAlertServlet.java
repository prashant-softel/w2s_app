package EmergancyAlert;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import MainClasses.ViewSOS;
import Utility.VerifyToken;

/**
 * Servlet implementation class SOSAlert
 */
@WebServlet("/SOSAlert")
public class SOSAlertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SOSAlertServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "GET");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "86400");
	    JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();
		
		try
		{
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			ViewSOS objSOSAlert = new ViewSOS(sToken, true, sTkey);
			//System.out.println("DATA CALLL");
			if(objSOSAlert.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objSOSAlert.getDecryptedTokenMap();
				
				if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid" )
				{
					
					if(request.getParameterMap().containsKey("fetch"))
				 	{
				 		if(Integer.parseInt(request.getParameter("fetch")) == 1)
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			//String sMemeberName = (String) DecryptedTokenMap.get("name");
				 			String sMemeberName = request.getParameter("OwnerName");
				 			String sAlertType = request.getParameter("AlertType"); 
				 			int iAlertStatus =Integer.parseInt((String) request.getParameter("Alertstatus"));
				 			int iSoSType =Integer.parseInt((String) request.getParameter("sosType"));
				 			String UnitNo=request.getParameter("UnitNo"); 
				 			String OwnerContact=request.getParameter("OwneContact"); 
				 			String sWing=request.getParameter("Wing"); 
				 			String sFloorNo=request.getParameter("FloorNo"); 
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mSetMedicalAlert(iSocietyID,sMemeberName,sAlertType,UnitNo,iAlertStatus,iSoSType,OwnerContact,sWing,sFloorNo);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 2)
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			//String sMemeberName = (String) DecryptedTokenMap.get("name");
				 			String sMemeberName = request.getParameter("OwnerName");
				 			String sAlertType = request.getParameter("AlertType"); 
				 			int iSoSType =Integer.parseInt((String) request.getParameter("sosType"));
				 			int iAlertStatus =Integer.parseInt((String) request.getParameter("Alertstatus"));
				 			String UnitNo=request.getParameter("UnitNo"); 
				 			String OwnerContact=request.getParameter("OwneContact"); 
				 			String sWing=request.getParameter("Wing"); 
				 			String sFloorNo=request.getParameter("FloorNo"); 
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mSetFireAlert(iSocietyID,sMemeberName,sAlertType,UnitNo,iAlertStatus,iSoSType,OwnerContact,sWing,sFloorNo);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 3)
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			//String sMemeberName = (String) DecryptedTokenMap.get("name");
				 			String sMemeberName = request.getParameter("OwnerName");
				 			String sAlertType = request.getParameter("AlertType"); 
				 			int iSoSType =Integer.parseInt((String) request.getParameter("sosType"));
				 			int iAlertStatus =Integer.parseInt((String) request.getParameter("Alertstatus"));
				 			String UnitNo=request.getParameter("UnitNo"); 
				 			String OwnerContact=request.getParameter("OwneContact"); 
				 			String sWing=request.getParameter("Wing"); 
				 			String sFloorNo=request.getParameter("FloorNo"); 
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mSetLiftAlert(iSocietyID,sMemeberName,sAlertType,UnitNo,iAlertStatus,iSoSType,OwnerContact,sWing,sFloorNo);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 4)
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			//String sMemeberName = (String) DecryptedTokenMap.get("name");
				 			String sMemeberName = request.getParameter("OwnerName");
				 			String sAlertType = request.getParameter("AlertType"); 
				 			int iAlertStatus =Integer.parseInt((String) request.getParameter("Alertstatus"));
				 			int iSoSType =Integer.parseInt((String) request.getParameter("sosType"));
				 			String UnitNo=request.getParameter("UnitNo"); 
				 			String OwnerContact=request.getParameter("OwneContact"); 
				 			String sWing=request.getParameter("Wing"); 
				 			String sFloorNo=request.getParameter("FloorNo"); 
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mSetOtherAlert(iSocietyID,sMemeberName,sAlertType,UnitNo,iAlertStatus,iSoSType,OwnerContact,sWing,sFloorNo);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 5)
				 		{
				 			
				 			int UnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
				 			String sAlertType = request.getParameter("AlertType"); 
				 			
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mFetchMember(UnitId);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 6)
				 		{
				 			
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			int iAlertID=Integer.parseInt((String) request.getParameter("AlertID"));
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mGetStatus(iSocietyID,iAlertID);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 7)
				 		{
				 			
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mGetSMDatabase(iSocietyID);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 8)
				 		{
				 			
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			int iAlertId = Integer.parseInt((String)request.getParameter("AlertID"));
				 			int iAlertStatus = Integer.parseInt((String)request.getParameter("AlertStatus"));
				 			String sMemeberName = request.getParameter("MemberName");
				 			String sRole = request.getParameter("Role");
				 			
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mResolveByMe(iSocietyID,iAlertId,sMemeberName,sRole,iAlertStatus);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}
				 	/*	else if(Integer.parseInt(request.getParameter("fetch")) == 9)
				 		{
				 			
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			
				 			//String sMemeberName = (String) DecryptedTokenMap.get("name");
				 		//	String sRole = (String) DecryptedTokenMap.get("role");
				 			HashMap objHash;
				 			
				 			 objHash = objSOSAlert.mSOSAlert(iSocietyID);	
				 			 Gson objGson = new Gson();
				 			 String objStr = objGson.toJson(objHash);
				 			 response.setContentType("application/json");
				 			 out.println(objStr);
				 		}*/
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 10 )
				 		{
				 			//int iSocietyID,int visitorID,int iUnitId
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							HashMap objHash = objSOSAlert.mfetchVisitor(iSocietyID,iUnitId);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 11 )
				 		{
				 			int iMemberID= Integer.parseInt(request.getParameter("memberID"));
							
							int VehicleType= Integer.parseInt(request.getParameter("VehicleType"));
							
							int iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							System.out.println("MemberId" + iMemberID);
							System.out.println("VehicleType" + VehicleType);
							System.out.println("iSocietyID" + iSocietyID);
							//HashMap objHash = objSOSAlert.mfetchVisitor(iSocietyID,iUnitId);			
							//Gson objGson = new Gson();
							//String objStr = objGson.toJson(objHash);
							//response.setContentType("application/json");
							//out.println(objStr);
				 		}
				 	}
					
				}
				else
				{
					out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
				}
			}
			else
			{
				out.println("{\"success\":0,\"response\":{\"message\":\"Invalid\"}}   ");
			}
			
		}
		catch(Exception ex)
		{
			out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
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
