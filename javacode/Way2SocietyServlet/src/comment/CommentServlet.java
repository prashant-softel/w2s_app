package comment;

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

import MainClasses.Comments;
import MainClasses.ViewSociety;
import Utility.VerifyToken;

/**
 * Servlet implementation class TaskServlet
 */

@WebServlet("/CommentServlet")

public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
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
		 * 1=fetchComments,		2=addComments, 	3=deleteComments
		 * */
		try {
//			String str = request.getRequestURI();;
			//out.print(str+"\t"+str2+"\t"+str3);
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			int flag = Integer.parseInt(request.getParameter("flag").trim());
			Comments objCom = new Comments(sToken, true, sTkey);
			if(objCom.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objCom.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap.get("dbname"));
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					if(flag==1) //fetch comments
					{
						int commentType = Integer.parseInt(request.getParameter("commentType").trim());
						int refID = Integer.parseInt(request.getParameter("refID").trim());
						HashMap objHash = objCom.mfetchComments(commentType, refID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					else if (flag==2) //add comments
					{
						int iCtype = Integer.parseInt(request.getParameter("commentType").trim());
						int iRefID = Integer.parseInt(request.getParameter("refID").trim());
						String sComment = request.getParameter("comments");
					
						HashMap objHash = objCom.AddComment(iCtype, iRefID, sComment, Integer.parseInt((String) DecryptedTokenMap.get("id")));
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					else if (flag==3) //delete comments
					{
						int iCommentID = Integer.parseInt(request.getParameter("commentID"));
						
						HashMap objHash = objCom.DeleteComment(Integer.parseInt((String) DecryptedTokenMap.get("id")), iCommentID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					else
					{
					out.println("{\"success\":0,\"response\":{\"message\":\"ServletURI error, Add flag\t1=fetchComments,\t2=addComments,\t3=deleteComments\"}}");
					}

				}
				else
					{
					out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");}
					}
				}
		catch(Exception ex)
		{
			out.println("{\"success\":0,\"response\":{\"message from servlet\":\""+ex+"\"}}   ");
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
