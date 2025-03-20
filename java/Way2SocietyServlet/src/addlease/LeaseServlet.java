package addlease;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import MainClasses.ViewLease;
import MainClasses.ViewMemberDetails;
import Utility.*;
/**
 * Servlet implementation class LeaseServlet
 */
@WebServlet("/LeaseServlet")
public class LeaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LeaseServlet() {
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
			ViewLease objLease = new ViewLease(sToken, true, sTkey);
			if(objLease.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objLease.getDecryptedTokenMap();
				if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid")
				{
					Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
					if(request.getParameterMap().containsKey("fetch"))
					{
						if(request.getParameter("fetch").equals("Profession"))
						{
							System.out.println("iSocietyID" + iSocietyID);
							HashMap objHash = objLease.mfetchProfesstion(iSocietyID);
							
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							System.out.println("objstr : " + objStr);
							response.setContentType("application/json");
							out.println(objStr);
						}
					
					else if(request.getParameter("fetch").equals("addTenant"))
					{
						System.out.println("Inside else");
					int iUnitId = 0;
					iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
					String TFname ="",TLname="",TMname="",Tdob="",TContact="",TEmail="",Tprofession_id="",TLeaseStart="",TLeaseEnd="",TAddress="",TCity="",TPincode="",TNote="",TAgentName="",TAgentContact="";
					TFname = request.getParameter("TFirstName").toString().trim();
					TMname =  request.getParameter("TMiddleName").toString().trim();
					TLname =  request.getParameter("TLastName").toString().trim();
					Tdob =  request.getParameter("TDob").toString().trim();
					TContact =  request.getParameter("TContact").toString().trim();
					TEmail =  request.getParameter("TEmail").toString().trim();
					Tprofession_id =  request.getParameter("Tprofession").toString().trim();
					TLeaseStart =  request.getParameter("LeaseStart").toString().trim();
					TLeaseEnd =  request.getParameter("LeaseEnd").toString().trim();
					TAddress =  request.getParameter("TAddress").toString().trim();
					TCity =  request.getParameter("TCity").toString().trim();
					TPincode =  request.getParameter("TPincode").toString().trim();
					TNote =  request.getParameter("TNote").toString().trim();
					TAgentName =  request.getParameter("TAgentName").toString().trim();
					TAgentContact =  request.getParameter("TAgentNumber").toString().trim();
					String sTenantVaring = request.getParameter("TenantFamilyJSON");
					System.out.println("TenantFamilyJSON : " +sTenantVaring);
					int membercount = 0;
					if(sTenantVaring != null)
					{
						JSONArray jsonArray = new JSONArray(sTenantVaring);
						membercount = jsonArray.length() + 1;
					}
					HashMap objHash = objLease.minsertlease(iSocietyID,iUnitId,TFname,TLname,TMname,Tdob,TContact,TEmail,Tprofession_id,TLeaseStart,TLeaseEnd,TAddress,TCity,TPincode,TNote,TAgentName,TAgentContact,membercount,sTenantVaring);
					
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					System.out.println("objstr : " + objStr);
					response.setContentType("application/json");
					out.println(objStr);
					}
				}
				}
				else
				{
					out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
				}
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
