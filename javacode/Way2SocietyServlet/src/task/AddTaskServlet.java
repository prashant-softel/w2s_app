package task;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import MainClasses.ViewTasks;
import MainClasses.ViewClassifieds;
import Utility.UtilityClass;
import Utility.VerifyToken;

/**
 * Servlet implementation class AddTask
 */
@WebServlet("/AddTaskServlet")
public class AddTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTaskServlet() {
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
		
		try {
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			int flag = Integer.parseInt(request.getParameter("flag"));
			ViewTasks objTask = new ViewTasks(sToken, true, sTkey);
			if(objTask.IsTokenValid() == true)
			{
				String message = new String();
				HashMap DecryptedTokenMap = objTask.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap.get("dbname"));
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					if(flag==1) // get AssignedTo list
					{
						int iSocietyID = Integer.parseInt(((String)DecryptedTokenMap.get("society_id")));
						HashMap objHash = objTask.mfetchAssignMember(iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					else if (flag==2) // call AddTask
					{
						
						int iLoginID = Integer.parseInt(((String)DecryptedTokenMap.get("id")));
			
						  Date dueDate = (Date) UtilityClass.getDBFormatDate(request.getParameter("duedate"));  	
						   //out.println("test1");  	
					      if(!request.getParameterMap().containsKey("title") || request.getParameter("title") == ""){  	
					    	  message = "Please enter a Title";  	
					       }
					      else if(!request.getParameterMap().containsKey("mapping_id") || Long.parseLong(request.getParameter("mapping_id").trim())==0){
					    	  message = "Please assign Member";
							}
					      else if(!request.getParameterMap().containsKey("duedate") || !UtilityClass.isValidDate(dueDate)){  	
					    	  message = "Please enter a valid Due Date";  	
					       }  	
					      else if(!request.getParameterMap().containsKey("priority") || Integer.parseInt(request.getParameter("priority").trim())==0){  	
					    	  message = "Please select Priority";  	
					        }  	
					      else if(!request.getParameterMap().containsKey("status") || Integer.parseInt(request.getParameter("status").trim())==0){
					    	  message = "Please select Status";
							}
					      else if(!request.getParameterMap().containsKey("percentcomplete") || Integer.parseInt(request.getParameter("status").trim())==0){
					    	  message = "Please enter Percent Completed";
							}
					      else{
							HashMap objHash=new HashMap();
				 			String dueDate1=new String();
				 			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
				 			dueDate1=output.format(dueDate);
				 			
							long lMappingID = Long.parseLong(request.getParameter("mapping_id").trim());								
							String sTitle = request.getParameter("title").trim();
							String sTask_desc = request.getParameter("desc").trim();
							String sAttachment = request.getParameter("attachment").trim();
							int iPriority = Integer.parseInt(request.getParameter("priority").trim());
							int iStatus =  Integer.parseInt(request.getParameter("status").trim());;
							int iPercentCompleted =  Integer.parseInt(request.getParameter("percentcomplete").trim());
							Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							//AddTask(long lMappingID, int iLoginID, String sTitle, String sTask_desc, String sAttachment, String sDueDate, int iPriority , int iStatus, int iPercentCompleted)
							objHash = objTask.AddTask(lMappingID, iLoginID, sTitle, sTask_desc, sAttachment, dueDate1, iPriority, iStatus, iPercentCompleted);
				 			Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
		 				}						
						//http://localhost:10317/Unichem_web/AddTaskServlet?flag=2&mapping_id=11910&title=title54&desc=desc4455&attachment=fdfdsf.png&duedate=2018-06-29&priority=1&status=1&percentcomplete=23&token=wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQICbULqM-4VDZZZYAK98igxHW66I42kLjFIEIRlMElsdCkczc_KZAMEiedzlQ8F-L8bTltAvAYoWBOtsXW0nT_sDNPmoYCbXNR-fRlVIPJ6OA&tkey=qzkitHqSkz1PV2Jm1beHa-u0YvhIRLBphLBCP5gO_y88ohYSsa_WhydauPB5i976Nce5afNNAX7WFPT7JADSIAh01q_cd0ei-dZGA1GC50e2NaufyuVlxzdeS0cKWzik6jP2Mh9RaT3EA2f1A7jFsBh4GAkzfwxxjG-nCnB9HQY
					}
					else{
						out.println("{\"success\":0,\"response\":{\"message\":\"Servlet error, give flag\"}}");
					}
					if(!message.equals(""))
				    {  	
				      out.println("{\"success\":0,\"response\":{\"message\":\""+message+"\"}}   ");  	
				    }
				}
				else{
					out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
					}
				}
			else {
				out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			out.println("{\"success\":0,\"response\":{\"message\":\""+e+"\"}}   ");
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
