package profile;

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

import MainClasses.ViewMemberDetails;
import Utility.*;

/**
 * Servlet implementation class ProfileServlet
 */
@WebServlet("/Profile")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
/**
* @see HttpServlet#HttpServlet()
*/
    public ProfileServlet() {
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
		String sURI = request.getRequestURI();
		System.out.println("URI :"+ sURI);
		
		try
		{
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			ViewMemberDetails objMember = new ViewMemberDetails(sToken, true, sTkey);
			//System.out.println("Inside Fuction ");
			if(objMember.IsTokenValid() == true)
				{
				HashMap DecryptedTokenMap = objMember.getDecryptedTokenMap();
				if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid")
					{
					
					if(request.getParameterMap().containsKey("fetch"))
					{
						if(request.getParameter("fetch").equals("profileData"))
						{
							 int iUnitId= Integer.parseInt(request.getParameter("uID"));
								
							 if(iUnitId == 0)
							 {
								 
								 iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
								
							 }
							 else
							 {
								 //sUnitID= Integer.parseInt(request.getParameter("uID"));
								// System.out.println("Inside else" +iUnitId );
							 }
							 System.out.println("Inside else" +iUnitId );
							HashMap objHash = objMember.mfetchMemberDetailsFromUnitID(iUnitId);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else if(request.getParameter("fetch").equals("memberNo"))
						{
							int iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							int ilogin =0;
							String Role =request.getParameter("role");
							if(Role == "Tenant")
							{
								ilogin = 1;
							}
							else
							{
								ilogin= 0;
							}
							HashMap objHash = objMember.mfetchMemberContactNo(iUnitId,ilogin);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else if(request.getParameter("fetch").equals("updateContact"))
						{
			
							int iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							int iOtherFemilyID =  Integer.parseInt(request.getParameter("otherID"));
							
							HashMap objHash = objMember.mUpdateMemberContactNo(iUnitId,iOtherFemilyID);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else if(request.getParameter("fetch").equals("notify"))
						{
			
							//int iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							int notify = Integer.parseInt(request.getParameter("notify"));
							int iMemOtherID =  Integer.parseInt(request.getParameter("MemOtherID"));
							HashMap objHash = objMember.mUpdateMemberNotify(iMemOtherID,notify);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						//**   Renting Registration function -----  */
						else if(request.getParameter("fetch").equals("MemberData"))
						{
			
							 int  iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							//int notify = Integer.parseInt(request.getParameter("notify"));
							//int iMemOtherID =  Integer.parseInt(request.getParameter("MemOtherID"));
							HashMap objHash = objMember.mfetchMemberDetailsForRentingRegistration(iUnitId);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						       /* DND section start */
						else if(request.getParameter("fetch").equals("UpdateDndData"))
						{
							System.out.println("Inside Fuction ");

							 int iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							 int unitno = Integer.parseInt(request.getParameter("unit_no"));
							 int dndtype = Integer.parseInt(request.getParameter("dnd_type"));
				
							 String dndmsg = request.getParameter("dnd_msg").toString();

							 int society_id = Integer.parseInt(request.getParameter("iSocietyID"));
							 

							HashMap objHash = objMember.mUpdateDND(society_id, iUnitId, unitno, dndtype,  dndmsg);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else if(request.getParameter("fetch").equals("fetchDndData"))
						{
							//int iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unitId"));
							//int dndtype = Integer.parseInt(request.getParameter("dnd_type"));
							//String dndmsg = request.getParameter("dnd_msg").toString();
                            int society_id = Integer.parseInt(request.getParameter("iSocietyID"));
                            int iUnitId = Integer.parseInt(request.getParameter("unitId"));

							HashMap objhash = objMember.mfetchDND(society_id, iUnitId);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objhash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else if(request.getParameter("fetch").equals("UpdateAddress"))
						{
			
							 //int  iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							 int iMemerID =  Integer.parseInt(request.getParameter("memberID"));
							 String sAddress = request.getParameter("ownerAdd").toString();
							HashMap objHash = objMember.mUpdateOwnerAdd(iMemerID,sAddress);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else if(request.getParameter("fetch").equals("TenantProfile"))
						{
			                 ///////     Tenant Profile section //////////////////
							int  iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							HashMap objHash = objMember.mfetchTenantDetailsFromUnitID(iUnitId);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");		
							out.println(objStr);
						}
						else if(request.getParameter("fetch").equals("RenewMSG"))
						{
							//System.out.println("Renew Vehicle");
							//int iSocietyID=288;
							//String EndDate ="31-12-2019";
							//String ButtonText ="Renew Details";
			                 ///////     Tenant Profile section //////////////////
							//int  iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							HashMap objHash = objMember.mfetchRenewDetails();
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else
						{
							Integer sUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							HashMap objHash = objMember.mfetchMemberDetailsFromUnitID(sUnitID);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						//out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
						
					}
					
				}
				else
				{
					out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
				}
		}
			catch(Exception ex)
				{
				out.println("{\"success\":0,\"response\":{\"message\":"+ex+"}}   ");
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
