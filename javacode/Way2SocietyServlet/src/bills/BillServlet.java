package bills;

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
import com.google.gson.JsonObject;

import MainClasses.ViewBills;
import Utility.VerifyToken;
/**
 * Servlet implementation class BillServlet
 */
@WebServlet("/Bill")
public class BillServlet extends HttpServlet 
	{
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BillServlet() 
    	{
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
			ViewBills objBill = new ViewBills(sToken, true, sTkey);
			
			if(objBill.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objBill.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap);
				
				if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid")
				{
					if(request.getParameterMap().containsKey("fetch"))    /// New Version Call To Inside If
				 	{
						if(Integer.parseInt(request.getParameter("fetch")) == 1)
				 		{
							int iUnitID= Integer.parseInt(request.getParameter("UnitId"));
							if(iUnitID == 0)
							{
						 
								iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							}
							else
							{
								//sUnitID= Integer.parseInt(request.getParameter("uID"));
								// System.out.println("Inside else" +iUnitId );
							}
							//Integer iUnitID = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							String IPeriodID="";
							String BT="";
							try
							{
								IPeriodID= request.getParameter("PeriodID").trim(); 
								BT=request.getParameter("BT").trim();
							}
							catch(Exception e)
							{
						
							}	
					
							if(IPeriodID.isEmpty() && BT.isEmpty())
							{
								HashMap objHash = objBill.mfetchAllBills(iUnitID);
								//out.println(objHash);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
							}
							
							else
							{
								HashMap objHash = objBill.mfetchMemberBill(iUnitID, Integer.parseInt(request.getParameter("PeriodID").trim()), Integer.parseInt(request.getParameter("BT").trim()));
								//out.println(objHash);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
							}
				 		}
					}
					else
					{
						int iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
						String IPeriodID="";
						String BT="";
						try
						{
							IPeriodID= request.getParameter("PeriodID").trim(); 
							BT=request.getParameter("BT").trim();
						}
						catch(Exception e)
						{
					
						}	
				
						if(IPeriodID.isEmpty() && BT.isEmpty())
						{
							HashMap objHash = objBill.mfetchAllBills(iUnitID);
							//out.println(objHash);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						
						else
						{
							HashMap objHash = objBill.mfetchMemberBill(iUnitID, Integer.parseInt(request.getParameter("PeriodID").trim()), Integer.parseInt(request.getParameter("BT").trim()));
							//out.println(objHash);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
					}
				}
				else			
				{
					out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
				}
			}
		}
		catch(Exception ex)
		{
			out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}");
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
