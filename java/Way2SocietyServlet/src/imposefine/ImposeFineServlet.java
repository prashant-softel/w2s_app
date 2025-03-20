package imposefine;

import java.io.IOException;  	
import java.io.PrintWriter;  	
import java.text.SimpleDateFormat;  	
import java.util.Date;  	
import java.util.HashMap;  	
import java.util.Map;  	
  	
import javax.servlet.ServletException;  	
import javax.servlet.annotation.WebServlet;  	
import javax.servlet.http.HttpServlet;  	
import javax.servlet.http.HttpServletRequest;  	
import javax.servlet.http.HttpServletResponse;  	
  	
import org.json.JSONObject;  	
  	
import com.google.gson.Gson;  	
import com.google.gson.JsonObject;  	

import MainClasses.DashBoard;
import MainClasses.ViewImposeFine;
import MainClasses.Fine;
import MainClasses.ViewClassifieds;
import Utility.UtilityClass;  	
import Utility.VerifyToken;

/**
 * Servlet implementation class ImposeFineServlet
 */
@WebServlet("/ImposeFineServlet")
public class ImposeFineServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImposeFineServlet() {
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
		//response.setContentType("text/html");
		String sURI = request.getRequestURI();
		try {
			
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			ViewImposeFine objImposeFine = new ViewImposeFine(sToken, true, sTkey);
			if(objImposeFine.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objImposeFine.getDecryptedTokenMap();				
				//out.print(DecryptedTokenMap);
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{	
					 String message=new String();
					 if(request.getParameterMap().containsKey("set"))
					   	{  	
						 if(!request.getParameterMap().containsKey("period_id") || request.getParameter("period_id") == "")
						 	{  	
					    	  message = "Period is not avalable please Create new financial year ";  	
						 	}  	
					      
					      else if(!request.getParameterMap().containsKey("Ledger_id") || request.getParameter("Ledger_id") == "")
					      {  	
					    	  message = "Please Set Fine Ledger from App default";  	
					      }  	
					     
					      else if(!request.getParameterMap().containsKey("unit_id") || request.getParameter("unit_id") == "")
							{
					    	  message = "Please Select Unit No";
							}
					      else if(!request.getParameterMap().containsKey("amount") || request.getParameter("amount") == "")
							{
					    	  message = "Please enter fine Amount";
							}
					      else if(!request.getParameterMap().containsKey("desc") || request.getParameter("desc") == "")
							{
					    	  message = "Please enter a valid details";
							}
					      else 
			 				{
								 
								HashMap objHash=new HashMap();
					 			//String exp_date1=new String();
					 			objHash =  objImposeFine.mAddImposeFine(Integer.parseInt((String)(DecryptedTokenMap.get("society_id"))),Long.parseLong((String) request.getParameter("map")),(String)DecryptedTokenMap.get("name"), (String)DecryptedTokenMap.get("member_id"),Integer.parseInt(request.getParameter("Ledger_id")),Integer.parseInt(request.getParameter("period_id")),Integer.parseInt(request.getParameter("unit_id")),Integer.parseInt(request.getParameter("amount")),request.getParameter("desc"),request.getParameter("img"),request.getParameter("periodDate"),Integer.parseInt(request.getParameter("sendEmail")));
					 			//out.println("Test1");
					 			//out.println(objHash);
					 			Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
			 				}
						}
					 else  if(request.getParameterMap().containsKey("fetch"))
					 {
						 Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
						 if(request.getParameter("fetch").equals("Fetchperiod"))
						 {
							
								//out.print("Valid Token");
								HashMap objHash=new HashMap();
								objHash = objImposeFine.mfetchPeriod();
								//out.println(objHash);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
						 }
						 else if(request.getParameter("fetch").equals("FetchLedger"))
						 {
							 HashMap objHash=new HashMap();
								objHash = objImposeFine.mFineLedger();
								//out.println(objHash);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
						 }
						 else if(request.getParameter("fetch").equals("FetchUnits"))
						 {
							 HashMap objHash=new HashMap();
								objHash = objImposeFine.mUnitDetails(iSocietyID);
								//out.println(objHash);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
						 }
						 else if(request.getParameter("fetch").equals("imposelist"))
						 {
							 HashMap objHash=new HashMap();
								objHash = objImposeFine.mImposeFineList(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")));
								out.println(objHash);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
						 }
						 else if(request.getParameter("fetch").equals("imposelistall"))
						 {//System.out.println("test1");
							 HashMap objHash=new HashMap();
								objHash = objImposeFine.mImposeFineList(0);
								System.out.println(objHash);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
						 }
						 else if(request.getParameter("fetch").equals("imposehistory"))
				 			{
				 			HashMap objHash=new HashMap(); 
				 			objHash= objImposeFine.mImposeHistory(Integer.parseInt(request.getParameter("rev_id")));
				 			Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
							}
						 else if(request.getParameter("fetch").equals("deleteFine"))
				 			{
				 			HashMap objHash=new HashMap(); 
				 			objHash= objImposeFine.mDeleteImposeFine(Integer.parseInt(request.getParameter("rev_id")),Integer.parseInt(request.getParameter("UnitID")),Integer.parseInt(request.getParameter("Amount")),request.getParameter("desc"),Integer.parseInt(request.getParameter("sendEmail")));
				 			Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
							}
						 else if(request.getParameter("fetch").equals("updatefine"))
			 				{
								HashMap objHash=new HashMap();
					 			//String exp_date1=new String();
								objHash =  objImposeFine.mUpdateImposeFine(Integer.parseInt(request.getParameter("rev_id")),Integer.parseInt(request.getParameter("period_id")),Integer.parseInt(request.getParameter("Ledger_id")),Integer.parseInt(request.getParameter("unit_id")),Integer.parseInt(request.getParameter("amount")),request.getParameter("desc"),request.getParameter("member_name"),Integer.parseInt(request.getParameter("sendEmail")));
								//objHash =  objImposeFine.mUpdateImposeFine(Integer.parseInt(request.getParameter("rev_id")),request.getParameter("Dates"),Integer.parseInt(request.getParameter("")),Integer.parseInt(request.getParameter("Amount")),request.getParameter("Comment"),Integer.parseInt( request.getParameter("PeriodId")),request.getParameter("Img_attachment"));
					 			//out.println("Test1");
					 			//out.println(objHash);
					 			
					 			Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
			 				}
						 
					 }
					 if(!message.equals(""))
					    {  	
					      out.println("{\"success\":0,\"response\":{\"message\":\""+message+"\"}}   ");  	
					    }  		 
				}
					
				else
				{
					out.println("{\"success\":0,\"response\":{\"message\": \""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+")\"}}   ");
				}
				
			}
			
			else
			{
				out.println("{\"success\":0,\"response\":{\"message\":\" isTokenValid ("+objImposeFine.IsTokenValid()+")\"}}   ");
			}
		}
		
		catch(Exception ex)
		{
			out.println("{\"success\":0,\"response\":{\"message\":\""+ex+"}}   ");
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
