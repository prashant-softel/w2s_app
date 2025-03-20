package user;

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
import com.google.gson.GsonBuilder;

import MainClasses.NewUser;

/**
 * Servlet implementation class NewUser
 */
@WebServlet("/NewUser")
public class NewUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewUserServlet() {
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
			NewUser objNewUser =  new NewUser(true, "hostmjbt_societydb");
			}
		catch (ClassNotFoundException e) 
			{
			out.println("{\"success\":0,\"response\":{\"message\":\"Not a vaild Society User!Try Again\"}}   ");
			//e.printStackTrace();
			}
		boolean b=true;
		try
			{
			/*String token=request.getParameter("token").trim();
			//String tkey=request.getParameter("Tkey").trim();
			HashMap objHash = NewUser.InsertLoginDetails("rohit1122@gmail.com", "111", "1c0584901b649f2f1e52bd9890b23a14", "Rohit", "", "");
			Gson objGson = new GsonBuilder().disableInnerClassSerialization() .create();
			String objStr = objGson.toJson(objHash);
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			out.println(objStr);*/
			}
		catch(Exception e)
			{
			b=false;
			}
		if(b==true)
			{
			try
			{
				String sEmail = "";
				String sPassword = "";
				String sAccess = "";
				String sName = "";
				String sMobileNo = "";
				String sFBCode = "";
				sEmail = request.getParameter("Email").trim();
				//out.println(sEmail);
				sPassword = request.getParameter("Password").trim();
				sAccess = request.getParameter("AccessCode").trim();
				sName = request.getParameter("Name").trim();
				sMobileNo = request.getParameter("MobileNo").trim();
				sFBCode = request.getParameter("FBCode").trim();
				/*sEmail = "rohit1122@gmail.com";
				sPassword = "111";
				sAccess ="1c0584901b649f2f1e52bd9890b23a14"; 
				sName = "Rohit";
				sFBCode ="";
				sMobileNo = "";*/
				HashMap objHash = NewUser.InsertLoginDetails(sEmail, sPassword, sAccess, sName, sFBCode, sMobileNo);
				if(sEmail.equals("")|| sPassword.equals(""))
				{
				//http://localhost:8089/W2s/Login?email=a@gmail.com&password=
				out.println("{\"success\":0,\"response\":{\"message\":\"Vacant Field\"}}   ");
				}
				else
				{
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
			}
			catch(Exception e)
				{
				out.println("{\"success\":0,\"response\":{\"message\":\"Username or password not provided\"}} Exception : "+e);
				}
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
