package task;

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

import MainClasses.ViewTasks;
import MainClasses.ViewSociety;
import Utility.VerifyToken;

/**
 * Servlet implementation class TaskServlet
 */

@WebServlet("/TaskServlet")

//@WebServlet(urlPatterns = {"/TaskServlet/fetchForMe", "/TaskServlet/fetchByMe"})
public class TaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TaskServlet() {
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
		
		/* flag = 1,2,3
		 * 1=fetchByMe,		2=fetchForMe, 	3=add
		 * */
		try {
			String str = request.getRequestURI();;
			//out.print(str+"\t"+str2+"\t"+str3);
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			int flag = Integer.parseInt(request.getParameter("flag"));
			ViewTasks objTask = new ViewTasks(sToken, true, sTkey);
			if(objTask.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objTask.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap.get("dbname"));
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
					int iLoginID = Integer.parseInt(((String)DecryptedTokenMap.get("id")));
					if(flag==1) // /Unichem_web/TaskServlet/fetchByMe
					{
						HashMap objHash = objTask.mfetchTasksByMe(iLoginID, iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					else if (flag==2) ///Unichem_web/TaskServlet/fetchForMe
					{
						HashMap objHash = objTask.mfetchTasksForMe(iLoginID,iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					else if (flag==3) ///Unichem_web/TaskServlet/updateTask
					{
						int iTaskID = Integer.parseInt(request.getParameter("taskid"));
						int iStatus = Integer.parseInt(request.getParameter("status"));
						String sComments = request.getParameter("summary");
						int iPercentCompleted = Integer.parseInt(request.getParameter("percentCompleted"));
						//Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
						HashMap objHash = objTask.UpdateTask(iTaskID, iStatus, iPercentCompleted,sComments);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					else if (flag==4) ///Unichem_web/TaskServlet/fetchForMe
					{
						HashMap objHash = objTask.mfetchAllTask(iLoginID,iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					else
					{
					out.println("{\"success\":0,\"response\":{\"message\":\"ServletURI error, Add flag\tflag = 1,2,3 \n1=fetchByMe,\t2=fetchForMe,\t3=update\"}}");
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
