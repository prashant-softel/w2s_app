package poll;  	
  	
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
import MainClasses.TakePoll;  	
import Utility.VerifyToken;  	
  	
/**  	
 * Servlet implementation class PollsServlet  	
 */  	
@WebServlet("/Polls")  	
public class PollsServlet extends HttpServlet {  	
 private static final long serialVersionUID = 1L;  	
         	
    /**  	
     * @see HttpServlet#HttpServlet()  	
     */  	
    public PollsServlet() {  	
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
     	
	  TakePoll objTakePoll = new TakePoll(sToken, true, sTkey);  	
	  if(objTakePoll.IsTokenValid() == true)  	
   		{  	
	   	HashMap DecryptedTokenMap = objTakePoll.getDecryptedTokenMap();  	
	   	//System.out.print(DecryptedTokenMap);  	
	   	if(VerifyToken.VerifyToken(DecryptedTokenMap,request) =="valid")  	
	   		{  	
	   		String message=new String();  	
	   		if(request.getParameterMap().containsKey("set"))  	
	   			{  	
	   			if(Integer.parseInt((String)request.getParameter("set")) == 1)  	
	   				{  	
	   				if(!request.getParameterMap().containsKey("option_id") || request.getParameter("option_id") == "")  	
	   					{  	
	   					message = "Please enter an option";  	
	   					}  	
	   				else  	
	   					{  	
	   					HashMap objHash=new HashMap();  	
	   					objHash = objTakePoll.InsertUserVote(Integer.parseInt((String)request.getParameter("option_id")),Integer.parseInt((String)DecryptedTokenMap.get("id")),Integer.parseInt((String)request.getParameter("society_id")),Integer.parseInt((String)DecryptedTokenMap.get("unit_id")),Integer.parseInt((String)request.getParameter("poll_id")),request.getParameter("mem_review").trim());  	
	   					Gson objGson = new Gson();  	
	   					String objStr = objGson.toJson(objHash);  	
	   					response.setContentType("application/json");  	
	   					out.println(objStr);  	
	   					}
	   				}  	
	   			else if(Integer.parseInt((String)request.getParameter("set")) == 2)  	
	   				{  	
	   				if(!request.getParameterMap().containsKey("option_id") || request.getParameter("option_id") == "")  	
	   					{  	
	   					message = "Please enter an option";  	
	   					}  	
	   				else  	
	   					{  	
	   					HashMap objHash=new HashMap();  	
	   					objHash = objTakePoll.UpdateUserVote(Integer.parseInt((String)request.getParameter("old_option_id")),Integer.parseInt((String)request.getParameter("option_id")),Integer.parseInt((String)DecryptedTokenMap.get("id")),Integer.parseInt((String)request.getParameter("society_id")),Integer.parseInt((String)DecryptedTokenMap.get("unit_id")),Integer.parseInt((String)request.getParameter("poll_id")),request.getParameter("mem_review").trim());  	
	   					Gson objGson = new Gson();  	
	   					String objStr = objGson.toJson(objHash);  	
	   					response.setContentType("application/json");  	
	   					out.println(objStr);  	
	   					}  	
	   				}  	
	   			}  	
	   		else if(request.getParameterMap().containsKey("fetch"))  	
	   			{  	
	   			if(Integer.parseInt((String)request.getParameter("fetch")) == 0)  	
	   				{
	   				//out.println("fecth0");
	   				HashMap objHash=new HashMap();  	
	   				objHash = objTakePoll.mfetchActivePolls(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")), Integer.parseInt((String)DecryptedTokenMap.get("society_id")));  	
	   				Gson objGson = new Gson();  	
	   				String objStr = objGson.toJson(objHash);  	
	   				response.setContentType("application/json");  	
	   				out.println(objStr);  	
	   				}  	
	   			else if(Integer.parseInt((String)request.getParameter("fetch")) == 1)  	
	   				{  	
	   				HashMap objHash=new HashMap();  	
	   				objHash = objTakePoll.mfetchInactivePolls(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")), Integer.parseInt((String)DecryptedTokenMap.get("society_id")));  	
	   				Gson objGson = new Gson();  	
	   				String objStr = objGson.toJson(objHash);  	
	   				response.setContentType("application/json");  	
	   				out.println(objStr);
	   				}  	
	   			else if(Integer.parseInt((String)request.getParameter("fetch")) == 2)  	
	   				{  	
	   				HashMap objHash=new HashMap();  	
	   				objHash = TakePoll.mfetchUserVote(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")),Integer.parseInt((String)request.getParameter("group_id")), 	
	   				Integer.parseInt((String)DecryptedTokenMap.get("society_id")),Integer.parseInt((String)request.getParameter("poll_id")));  	
	   				System.out.print(Integer.parseInt((String)DecryptedTokenMap.get("unit_id")));  	
	   				Gson objGson = new Gson();  	
	   				String objStr = objGson.toJson(objHash);  	
	   				response.setContentType("application/json");  	
	   				out.println(objStr);  	
	   				}  	
	   			}  	
	   		if(!(message.equals("")))  	
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
}
  	
 /**  	
  * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)  	
  */