package database;

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

import MainClasses.DashBoard;
import MainClasses.ViewBills;
import Utility.VerifyToken;

/**
 * Servlet implementation class Database
 */
@WebServlet(urlPatterns = {"/Database/get"})
public class DatabaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatabaseServlet() {
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
		try
		{
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			//String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM7sT_rWiDrEygawTu4HgwS7bu7_OUxtHrRloGaFPR8Tnrb7J7g2xQLwVFCd6q0iWt7jd9cpnA-TQv3EA6vznzl3";
			//String sTkey = "MLUPYihVZbxvSfS2fPFJuIN0MpzkkUHKBSY0hcAqtv_-2nyhp9rMQU2k5-rastIULY5cv5JmJi6iUhBito2La5s2EGgJtDZjZ02lYyypnCiG0XqR0OXN24u05ot30taayjSrxCMxta0dl2cCVEZv9A";
			
			DashBoard objDashboard = new DashBoard(sToken, true, sTkey);
			
			if(objDashboard.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objDashboard.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap.get("dbname"));
				out.println("{\"success\":1,\"response\":{\"dbname\": \"" + DecryptedTokenMap.get("dbname") + "\"}}");
			}
			else
			{
				out.println("{\"success\":0,\"response\":{\"dbname\": \"Invalid Token\"}}");
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
