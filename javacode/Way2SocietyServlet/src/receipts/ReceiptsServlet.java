package receipts;

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

import MainClasses.ViewReceipts;
import Utility.VerifyToken;

/**
 * Servlet implementation class ReceiptsServlet
 */
@WebServlet("/Receipts")
public class ReceiptsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReceiptsServlet() {
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
			 ViewReceipts objReceipts = new ViewReceipts(sToken, true, sTkey);
			
			if(objReceipts.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objReceipts.getDecryptedTokenMap();
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					if(request.getParameterMap().containsKey("fetch"))    /// New Version Call To Inside If
				 	{
						if(Integer.parseInt(request.getParameter("fetch")) == 1)
				 		{
							if(request.getParameterMap().containsKey("PeriodID") && (request.getParameter("PeriodID") != "" ))
							{
								int iUnitID= Integer.parseInt(request.getParameter("UnitID"));
								if(iUnitID == 0)
								{
									iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
								}
								else
								{
									//sUnitID= Integer.parseInt(request.getParameter("uID"));
									// System.out.println("Inside else" +iUnitId );
								}
								HashMap objHash = objReceipts.mfetchAllReceipts(iUnitID, Integer.parseInt(request.getParameter("PeriodID")));
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr); 
								//fetch detailed receipts according to period id
							}
							else
							{
								//fetch all receipts
								int iUnitID= Integer.parseInt(request.getParameter("UnitID"));
								if(iUnitID == 0)
								{
									iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
								}
								else
								{
									//sUnitID= Integer.parseInt(request.getParameter("uID"));
									// System.out.println("Inside else" +iUnitId );
								}
								HashMap objHash = objReceipts.mfetchAllReceipts(iUnitID,0);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr); 
							}
				 		}
						
						else if(Integer.parseInt(request.getParameter("fetch")) == 2)
				 		{
							String iUnitID= request.getParameter("unit");
							String mode = request.getParameter("mode");
							String cdid = request.getParameter("cdnid");
							System.out.println("iUnitID : " + iUnitID + " mode : " + mode + " cdid : " + cdid ) ;
							HashMap objHash = objReceipts.mfetchcreditdebit(iUnitID,mode,cdid);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr); 
							
				 		}

						else if(Integer.parseInt(request.getParameter("fetch")) == 3)
				 		{
							String iUnitID= request.getParameter("unit");
							String mode = request.getParameter("mode");
							String inv_number = request.getParameter("inv_number");
							String inv_id = request.getParameter("inv_id");
							
							System.out.println("iUnitID : " + iUnitID + " mode : " + mode + " inv_number : " + inv_number + " inv_id : " + inv_id ) ;
							HashMap objHash = objReceipts.mfetchinvoicenumber(iUnitID,mode,inv_number,inv_id);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr); 
							
				 		}
				 	}
					else
					{
						if(request.getParameterMap().containsKey("PeriodID") && (request.getParameter("PeriodID") != "" ))
						{
							int iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							HashMap objHash = objReceipts.mfetchAllReceipts(iUnitID, Integer.parseInt(request.getParameter("PeriodID")));
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr); 
						}
						else
						{
							int iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							HashMap objHash = objReceipts.mfetchAllReceipts(iUnitID,0);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr); 
						}
					}
				}
			else
				{
				out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");}
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
