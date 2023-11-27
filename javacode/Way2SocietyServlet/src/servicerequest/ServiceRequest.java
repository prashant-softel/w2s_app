package servicerequest;
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

import MainClasses.ViewServiceRequests;
import Utility.*;

/**
 * Servlet implementation class ServiceRequest
 */
@WebServlet("/ServiceRequest")
public class ServiceRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceRequest() {
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
			
			ViewServiceRequests objServiceRequest = new ViewServiceRequests(sToken, true, sTkey);
			
			if(objServiceRequest.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objServiceRequest.getDecryptedTokenMap();
				System.out.print(DecryptedTokenMap);
			if(VerifyToken.VerifyToken(DecryptedTokenMap,request) =="valid")
				{
				String message=new String();
				if(request.getParameterMap().containsKey("set"))
					{
					if(request.getParameter("set").equals("sr"))
						{
						int iUnitid=0;
						if(!request.getParameterMap().containsKey("title") || request.getParameter("title") == "")
							{
							message = "Please enter a valid title for Service Request";
							}
						else if(!request.getParameterMap().containsKey("category") || request.getParameter("category") == "")
							{
							message = "Please enter a valid Category for Service Request";
							}
						//else if(!request.getParameterMap().containsKey("details") || request.getParameter("details") == "")
							//{
							//message = "Please enter Details for Service Request";
							//}
					
				 			else 
				 			{
				 				if(!request.getParameterMap().containsKey("unit_id"))
				 				{
				 					iUnitid =Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
				 				}
				 				else
				 				{
				 					
				 					if(Integer.parseInt((String)request.getParameter("unit_id")) == 0)
				 					{
				 						iUnitid =Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
				 					}
				 					else
				 					{
				 						iUnitid=Integer.parseInt((String)request.getParameter("unit_id"));
				 					}
				 			}
				 		HashMap objHash=new HashMap();
					 		objHash = objServiceRequest.mAddServiceRequests(iUnitid,Integer.parseInt((String)DecryptedTokenMap.get("society_id")), Long.parseLong((String)request.getParameter("map")), (String)DecryptedTokenMap.get("name"), (String)DecryptedTokenMap.get("member_id"), "", request.getParameter("title"), "Raised", 1,  request.getParameter("category"), request.getParameter("priority"), "", request.getParameter("details"));
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr); 	
					 		}
						}
					else if(request.getParameter("set").equals("comment"))
						{
						if(!request.getParameterMap().containsKey("status") || request.getParameter("status") == "")
							{
							message = "Please enter a valid Status for Service Request";
							}
						else if(!request.getParameterMap().containsKey("request_no") || request.getParameter("request_no") == "")
							{
							message = "Invalid Service Request";
							}
					 	else 
					 		{
					 		int iUnitid=0;
					 		if(!request.getParameterMap().containsKey("unit_id"))
			 				{
			 					iUnitid =Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
			 				}
			 				else
			 				{
			 					
			 					if(Integer.parseInt((String)request.getParameter("unit_id")) == 0)
			 					{
			 						iUnitid =Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
			 					}
			 					else
			 					{
			 						iUnitid=Integer.parseInt((String)request.getParameter("unit_id"));
			 					}
			 				}
					 		HashMap objHash=new HashMap();
					 		
					 		//int iUnit=Integer.parseInt((String)request.getParameter("unit_id"));
					 		objHash = objServiceRequest.mUpdateServiceRequestHistory(iUnitid,Integer.parseInt((String)DecryptedTokenMap.get("society_id")),Long.parseLong((String) request.getParameter("map")), Integer.parseInt((String)request.getParameter("request_no")), (String)DecryptedTokenMap.get("name"), (String)DecryptedTokenMap.get("member_id"), "", (String)request.getParameter("summary"), request.getParameter("status"), 1,  request.getParameter("category"), request.getParameter("priority"),"");	
					 		Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
					 		}
						}
					}
				else if(request.getParameterMap().containsKey("fetch"))
			 		{
					Integer iLoginID = Integer.parseInt((String) DecryptedTokenMap.get("id"));
					if(request.getParameter("fetch").equals("srlist"))
			 			{
						HashMap objHash=new HashMap();
						objHash = objServiceRequest.mfetchServiceRequests(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")), iLoginID,Integer.parseInt((String)DecryptedTokenMap.get("society_id")),false);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
						}
					else if(request.getParameter("fetch").equals("srlistcontractor"))
		 			{
					HashMap objHash=new HashMap();
					objHash = objServiceRequest.mfetchServiceContractor(Integer.parseInt((String)DecryptedTokenMap.get("society_id")),iLoginID);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
					}
				
					else if(request.getParameter("fetch").equals("srlistall"))
		 			{
					HashMap objHash=new HashMap();
					objHash = objServiceRequest.mfetchServiceRequests(0, iLoginID,Integer.parseInt((String)DecryptedTokenMap.get("society_id")), false);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
					}
					else if(request.getParameter("fetch").equals("assignme"))
		 			{
					HashMap objHash=new HashMap();
					
					//System.out.println("member_id :" + (String)DecryptedTokenMap.get("member_id"));
					objHash = objServiceRequest.mfetchServiceRequests(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")), iLoginID,Integer.parseInt((String)DecryptedTokenMap.get("society_id")),true);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
					}
			 		else if(request.getParameter("fetch").equals("srhistory"))
			 			{
			 			HashMap objHash=new HashMap(); 
			 			objHash= objServiceRequest.mfetchServiceRequestHistory(Integer.parseInt(request.getParameter("request_no")));
			 			Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
						}	
			 		else if(request.getParameter("fetch").equals("srstatuslist"))
			 			{
			 			HashMap objHash=new HashMap();
			 			objHash = objServiceRequest.mfetchSRStatusList();
			 			Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
						}
			 		else if(request.getParameter("fetch").equals("srcategory"))
			 			{
			 			HashMap objHash=new HashMap();
			 			objHash = objServiceRequest.mfetchSRCategory();
			 			Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
			 			}	
			 		}
					if((!(message.equals(""))))
						{
						out.println("{\"success\":0,\"response\":{\"message\":\""+message+"\"}}   ");
						}
				}
			else
				{
				out.println("{\"success\":0,\"response\":{\"message\":\"Invalid\"}}   ");
				}
			}
		}	
	catch (ClassNotFoundException e) 
		{
		//e.printStackTrace();
		out.println("{\"success\":0,\"response\":{\"message\":\"Cannot Find Results\"}}   ");
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