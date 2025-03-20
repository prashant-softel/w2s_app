package classifieds;  	
  	
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
import MainClasses.ViewClassifieds;  	
import MainClasses.Classifieds;  	
import Utility.UtilityClass;  	
import Utility.VerifyToken;  	
  	
/**  	
 * Servlet implementation class ClassifiedsServlet  	
 */  	
@WebServlet("/Classifieds")  	
public class ClassifiedsServlet extends HttpServlet { 

private static final long serialVersionUID = 1L;  	
         	
    /**  	
     * @see HttpServlet#HttpServlet()  	
     */  	
    public ClassifiedsServlet()   	
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
	   ViewClassifieds objClassifieds = new ViewClassifieds(sToken, true, sTkey); 
	   
	   if(objClassifieds.IsTokenValid() == true){  	
		   HashMap DecryptedTokenMap = objClassifieds.getDecryptedTokenMap();  	
		   //out.print(DecryptedTokenMap);  	
		   if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid"){  	
			   String message=new String();
			   
			   if(request.getParameterMap().containsKey("set"))
			   	{  	
				  Date exp_date = (Date) UtilityClass.getDBFormatDate(request.getParameter("exp_date"));  	
				   //out.println("test1");  	
			      if(!request.getParameterMap().containsKey("ad_title") || request.getParameter("ad_title") == ""){  	
			    	  message = "Please enter a Title";  	
			       }  	
			      else if(!request.getParameterMap().containsKey("exp_date") || !UtilityClass.isValidDate(exp_date)){  	
			    	  message = "Please enter a valid Expiry Date";  	
			       }  	
			      else if(!request.getParameterMap().containsKey("location") || request.getParameter("location") == ""){  	
			    	  message = "Please enter a valid location";  	
			       }  	
			      else if(!request.getParameterMap().containsKey("email") || !UtilityClass.isValidEmailID(request.getParameter("email")))
			      	{  	
			    	  message = "Please enter a valid email";  	
			        }  	
			      else if(!request.getParameterMap().containsKey("cat_id") || request.getParameter("cat_id") == "")
					{
			    	  message = "Please enter a valid category";
					}
			      else if(!request.getParameterMap().containsKey("desp") || request.getParameter("desp") == "")
					{
			    	  message = "Please enter a valid details";
					}
			      else if(!request.getParameterMap().containsKey("phone") || !UtilityClass.isValidNumber(request.getParameter("phone")))
					{
			    	  message = "Please enter a valid phone number";
					}
			      else 
	 				{
						HashMap objHash=new HashMap();
			 			String exp_date1=new String();
			 			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
			 			exp_date1=output.format(exp_date);
			 			objHash = ViewClassifieds.mAddClassifieds(Integer.parseInt((String)(DecryptedTokenMap.get("id"))),Integer.parseInt((String)(DecryptedTokenMap.get("society_id"))),request.getParameter("ad_title"), request.getParameter("desp"), exp_date1, request.getParameter("img"),request.getParameter("location"),request.getParameter("email"),request.getParameter("phone"),Integer.parseInt(request.getParameter("cat_id")));
			 			//out.println("Test1");
			 			//out.println(objHash);
			 			Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
	 				}
				}
			else if(request.getParameterMap().containsKey("fetch"))
			 {
				if(request.getParameter("fetch").equals("classifieds"))
				 {
					Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
					//out.print("Valid Token");
					HashMap objHash=new HashMap();
					int iClassifiedID = 0;
					objHash = objClassifieds.mfetchClassifieds(iSocietyID,iClassifiedID);
					//out.println(objHash);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				 }
				else if(request.getParameter("fetch").equals("classifiedscategory"))
				 {
					HashMap objHash=new HashMap();
					objHash = ViewClassifieds.mfetchClassifiedsCategory();
					//out.println(objHash);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				 }
				else
				 {
					out.println("{\"success\":0,\"response\":{\"message\":\"Cannot Display Categories\"}}   ");
			     }
			 }
		   if(!message.equals(""))
		    {  	
		      out.println("{\"success\":0,\"response\":{\"message\":\""+message+"\"}}   ");  	
		    }  	
		   }
		   else
		   	{  	
			   out.println("{\"success\":0,\"response\":{\"message\":\"Cannot Display\"}}   ");  	
		    }  	
	    }  	
	   else{  	
		   out.println("{\"success\":0,\"response\":{\"message\":\"Cannot Display\"}}   ");  	
	    }  	
  }  	
    	
  catch(Exception ex)  	
  {  	
	  ex.printStackTrace();  	
	  out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}} Exception : "+ex);  	
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