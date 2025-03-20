package society;

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
import MainClasses.ViewSociety;
import Utility.VerifyToken;

/**
 * Servlet implementation class Society
 */
@WebServlet("/Society")

//@WebServlet(urlPatterns = {"/Society", "/Society/paymentGateway"})
public class SocietyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SocietyServlet() {
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
			String str = request.getRequestURI();
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			ViewSociety objSociety = new ViewSociety(sToken, true, sTkey);
			if(objSociety.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objSociety.getDecryptedTokenMap();
//				out.println(DecryptedTokenMap);
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
//					if(str.equals("/Society/paymentGateway"))
//					{
						//fetch paymentGateway according to society id
						HashMap objHash = objSociety.fetchPaymentGateway(Integer.parseInt(((String)DecryptedTokenMap.get("society_id"))));
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
//					}
//					else
//					{
//					out.println("{\"success\":0,\"response\":{\"message\":\"ServletURI error\"}}   ");
//					}

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
