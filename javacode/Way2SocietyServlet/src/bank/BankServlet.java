package bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import MainClasses.ViewBanks;
import Utility.VerifyToken;
import Utility.UtilityClass;;

/**
 * Servlet implementation class BankServlet
 */
@WebServlet("/Bank")
public class BankServlet extends HttpServlet 
	{
	private static final long serialVersionUID = 1L;   
    /* @see HttpServlet#HttpServlet()
     */
    public BankServlet() 	
    	{
        super();
        // TODO Auto-generated constructor stub
    	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{
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
			ViewBanks objBanks = new ViewBanks(sToken, true, sTkey);
			String	message = "";
			
			if(objBanks.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objBanks.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap);
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					if(request.getParameterMap().containsKey("fetch"))
					{
						Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
						//if(iSocietyID == 225)
						//{
							
						//}
						//else
						//{
							HashMap objHash = objBanks.mfetchNEFTBanks();
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						//}
					}
					else if(request.getParameterMap().containsKey("fetch_payer"))
					{
						HashMap objHash = objBanks.fetchPayerBanks(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")));
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(request.getParameterMap().containsKey("set"))
				 	{
				 		Date trnx_date = (Date) UtilityClass.getDBFormatDate(request.getParameter("trnx_date"));
				 		
				 		if(!request.getParameterMap().containsKey("bank_id") || !request.getParameterMap().containsKey("bill_type") || !request.getParameterMap().containsKey("amount") || !request.getParameterMap().containsKey("trnx_id"))
				 			{
				 			message = "Please enter values for all * fields";
				 			}
				 		else if(!UtilityClass.isValidNumber(request.getParameter("amount")))
				 		{
				 			message = "Please enter valid amount";
				 		}
				 		else if(request.getParameter("trnx_id") == "")
				 		{
				 			message = "Please enter valid Transaction ID";
				 		}
				 		else if(!UtilityClass.isValidDate(trnx_date))
				 		{
				 			message = "Please enter valid Transaction date";
				 		}
				 		else if(!(Integer.parseInt((String)(request.getParameter("bank_id"))) > 0))
				 		{
				 		message = "Please select Society Bank name where payment is deposited";
				 		}
				 		else
				 		{
				 			String trnx_date1=new String();
				 			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
				 			trnx_date1=output.format(trnx_date);
				 			HashMap objHash = objBanks.mSetNEFTTransaction(Integer.parseInt((String)DecryptedTokenMap.get("id")), Integer.parseInt((String)DecryptedTokenMap.get("unit_id")), Integer.parseInt((String)request.getParameter("bank_id")), Integer.parseInt((String)request.getParameter("bill_type")), Double.parseDouble((String)request.getParameter("amount")), request.getParameter("trnx_id"), trnx_date1, request.getParameter("note"), request.getParameter("payer_bank"), request.getParameter("payer_branch"));
				 			Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 	}
					else
					{
						message = "Invalid input received";
					}
				if(message!="")
					{
					out.println("{\"success\":0,\"response\":{\"message\":\""+message+"\"}}   ");
					}
				}
				else
					{
					out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Login\"\"}}   ");
					}				
			}
			else
			{
				out.println("{\"success\":0,\"response\":{\"message\":\"Invalid\"}}   ");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			out.println("{\"success\":0,\"response\":{\"message\":\"Invalid\"}}   ");
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