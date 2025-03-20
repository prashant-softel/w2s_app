package events;


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

import MainClasses.ViewEvents;
import Utility.VerifyToken;

/**
 * Servlet implementation class EventsServlet
 */
@WebServlet("/Events")
public class EventsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventsServlet() {
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
			ViewEvents objEvents = new ViewEvents(sToken, true, sTkey);
			
			if(objEvents.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objEvents.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap);
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					Integer iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
					Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));				
					HashMap objHash = objEvents.mfetchAllEvents(iUnitID, iSocietyID, 0, false);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else
				{
				out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
				}
			}
			else
			{
				out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
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
