package members;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import Utility.VerifyToken;

/**
 * Servlet implementation class MemberDetailsServlet
 */
@WebServlet("/MemberDetails")
public class MemberDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
/**
* @see HttpServlet#HttpServlet()
*/
    public MemberDetailsServlet() {
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
			/*
			if(request.getParameterMap().containsKey("summary"))
			{
				String = request.getParameter("token").trim();
			}*/
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			ViewMemberDetails objMember = new ViewMemberDetails(sToken, true, sTkey);
			System.out.println(request.getParameter("fetch"));
			if(objMember.IsTokenValid() == true)
				{
				HashMap DecryptedTokenMap = objMember.getDecryptedTokenMap();
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) =="valid")
					{
						String message = "";
					//System.out.println("message");
					if(request.getParameterMap().containsKey("member_id") && (request.getParameter("member_id") !="") && Integer.parseInt((String)request.getParameter("member_id")) > 0)
					{
						//System.out.println("reply1");
						if(request.getParameterMap().containsKey("set") && request.getParameter("set").equals("member"))
						{
							//System.out.println("Member");
							if(!request.getParameterMap().containsKey("other_name"))
							{
								message = "Please enter a valid name for the member";
							}
							else if(request.getParameterMap().containsKey("other_email") && !request.getParameter("other_email").equals("") && UtilityClass.isValidEmailID(request.getParameter("other_email")) == false )
							{
								message = "Please enter valid Email ID";
							}
						 	else 
							{
						 		
						 		String sDOB = request.getParameter("other_dob");
						 		String sDOW = "0000-00-00 00:00:00";
						 		
						 		/*out.println(sDOB);
								if(!request.getParameter("other_dob").equals(""))
								{
									out.println("Inside Format : " + request.getParameter("other_dob"));
									sDOB = output.format(sDOB);
								}
								out.println(sDOB);
								out.println(sDOW);*/
								if(!request.getParameter("other_wed").equals(""))
								{
									sDOW = request.getParameter("other_wed");
								}
								

							 	if(request.getParameterMap().containsKey("mem_other_family_id") && (Integer.parseInt((String)request.getParameter("mem_other_family_id") )> 0))
							 	{
							 		//Update Query
							 		Integer sUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
									HashMap objHash = objMember.mUpdateMemberDetails(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")), Integer.parseInt((String)request.getParameter("member_id")), Integer.parseInt((String)request.getParameter("mem_other_family_id")), request.getParameter("other_name"), Integer.parseInt((String)request.getParameter("coowner")), request.getParameter("relation"), Integer.parseInt((String)request.getParameter("other_publish_contact")), request.getParameter("other_mobile"), request.getParameter("other_email"), Integer.parseInt((String)request.getParameter("child_bg")), sDOB, Integer.parseInt((String)request.getParameter("other_publish_profile")), request.getParameter("other_profile") , Integer.parseInt((String)request.getParameter("other_desg")), request.getParameter("ssc"), sDOW,request.getParameter("other_gender"),request.getParameter("other_adhaar"));
									System.out.println("UpdateResponse:"+objHash);
									Gson objGson = new Gson();
									String objStr = objGson.toJson(objHash);
									response.setContentType("application/json");
									out.println(objStr);
									//System.out.println("1stUpdateResponse:"+objStr);
							 	}
							 	else
							 	{
							
									HashMap objHash= objMember.mAddMemberDetails(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")), Integer.parseInt((String)request.getParameter("member_id")), request.getParameter("other_name"),Integer.parseInt((String)request.getParameter("coowner")), request.getParameter("relation"), Integer.parseInt((String)request.getParameter("other_publish_contact")),  request.getParameter("other_mobile"), request.getParameter("other_email"), Integer.parseInt((String)request.getParameter("child_bg")), request.getParameter("other_dob"), Integer.parseInt((String)request.getParameter("other_publish_profile")), request.getParameter("other_profile") , Integer.parseInt((String)request.getParameter("other_desg")), request.getParameter("ssc"), request.getParameter("other_wed"),request.getParameter("other_gender"),request.getParameter("other_adhaar"));
					 				Gson objGson = new Gson();
									String objStr = objGson.toJson(objHash);
									response.setContentType("application/json");
									//System.out.println("2ndAddResponse:"+objStr);
							//Insert query
									out.println(objStr);	
													
							 	}
							}
						}
						else if(request.getParameterMap().containsKey("set") && request.getParameter("set").equals("vehicle"))
						{
							if(!request.getParameterMap().containsKey("regno") || (request.getParameter("regno").equals("")))
							{
								message = "Please enter a valid registration no.";
							}
							else if(!request.getParameterMap().containsKey("owner") || (request.getParameter("owner").equals ("")))
							{
								message = "Please enter a valid owner name";
							}
							else if(!request.getParameterMap().containsKey("make") || (request.getParameter("make").equals("")))
							{
								message = "Please enter a valid vehicle make";
							}
							else if(!request.getParameterMap().containsKey("model") || (request.getParameter("model").equals("")))
							{
								message = "Please enter a valid vehicle model";
							}
							else if(!request.getParameterMap().containsKey("color") || (request.getParameter("color").equals("")))
							{
								message = "Please enter a valid vehicle color";
							}
						 	else 
							{
								if(request.getParameterMap().containsKey("vehicle_id") && (Integer.parseInt((String)request.getParameter("vehicle_id"))> 0))
							 	{
							 		HashMap objHash = objMember.mUpdateVehicleDetails(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")),Integer.parseInt((String) request.getParameter("member_id")), Integer.parseInt((String)request.getParameter("vehicle_id")), Integer.parseInt((String)request.getParameter("type")), request.getParameter("slot"), request.getParameter("sticker"), request.getParameter("regno"),  request.getParameter("owner"), request.getParameter("make"), request.getParameter("model"), request.getParameter("color"));
									Gson objGson = new Gson();
									String objStr = objGson.toJson(objHash);
									response.setContentType("application/json");
									out.println(objStr);
							 		
							 	}
							 	else
							 	{
							 		HashMap objHash = objMember.mAddVehicleDetails(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")), Integer.parseInt(request.getParameter("member_id")), Integer.parseInt(request.getParameter("type")), request.getParameter("slot"), request.getParameter("sticker"), request.getParameter("regno"),  request.getParameter("owner"), request.getParameter("make"), request.getParameter("model"), request.getParameter("color"));
									Gson objGson = new Gson();
									String objStr = objGson.toJson(objHash);
									response.setContentType("application/json");
									out.println(objStr);							 	}
							}
						}
				 	}
					
				 	else if(request.getParameterMap().containsKey("fetch") && request.getParameter("fetch").toString().equals("desg"))
				 	{
				 		System.out.println("desg fetching");
						
				 		HashMap objHash = objMember.mfetchDesignations();
				 		
						
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						//System.out.println("desg:"+objStr);
						response.setContentType("application/json");
						out.println(objStr);	
				 	}
				 	else
				 	{
				 		message = "Invalid input received";
				 	}

				 	if(message !=""&&(VerifyToken.VerifyToken(DecryptedTokenMap,request))!="valid")
			 		{
				 		out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
					}
				}
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
