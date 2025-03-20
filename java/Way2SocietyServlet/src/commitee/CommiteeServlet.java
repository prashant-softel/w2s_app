package commitee;

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

import MainClasses.ViewCommitee;
import MainClasses.ViewMemberDetails;
import Utility.*;
import Utility.VerifyToken;

/**
 * Servlet implementation class MemberDetailsServlet
 */
@WebServlet("/Commitee")
public class CommiteeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       
/**
* @see HttpServlet#HttpServlet()
*/
    public CommiteeServlet() {
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

//			String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM6vcKZ7uAvmKFwVT0AnjLMG0nUkxJFGGjyaBGk6naq4Bu177lcSPz3xkMvf_a1HPHOBZzcOXhVC2S5uaWuDVlz1";
//			String sTkey = "lneybn9-fyKNMyrxK8wZ45jaj6H4Qp82jbnpHfuhbOOyScdY1f0mFHO0NSqoFR7RlKQPqANJBOHmGrvJTtEguM2MUEdS0NV8svtlmG6q9K28ZB_YR4A66vH5uzcYbFEk7Syr6U03jUzTkjr-eoBUIheE8VynoQlyjQ4gAB-UXrE";
//			
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			ViewCommitee objCommitee = new ViewCommitee(sToken, true, sTkey);
			
			if(objCommitee.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objCommitee.getDecryptedTokenMap();				
				//out.print(DecryptedTokenMap);
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{					
					HashMap objHash = objCommitee.mfetchCommitee();						
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else{
					out.println("{\"success\":0,\"response\":{\"message\": \""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+")\"}}   ");
				}
			}
			else
			{
				out.println("{\"success\":0,\"response\":{\"message\":\" isTokenValid ("+objCommitee.IsTokenValid()+")\"}}   ");
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
