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

import MainClasses.Login;

/**
 * Servlet implementation class NewLogin  is new version app login
 */
@WebServlet("/AppLogin")
public class AppLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppLogin() {
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
			Login objLogin =  new Login(true, "hostmjbt_societydb");
			}
		catch (ClassNotFoundException e) 
			{
			out.println("{\"success\":0,\"response\":{\"message\":\"Not a vaild Society User!Try Again\"}}   ");
			//e.printStackTrace();
			}
		boolean b=true;
		try
			{
			String token=request.getParameter("token").trim();
			//String tkey=request.getParameter("Tkey").trim();
			HashMap objHash = Login.refreshToken(token);
			Gson objGson = new GsonBuilder().disableInnerClassSerialization() .create();
			String objStr = objGson.toJson(objHash);
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			out.println(objStr);
			
			}
		catch(Exception e)
			{
			b=false;
			}
		if(b==false)
			{
			try
			{
				
				System.out.println("login");
				String Devicemodel="";
				String DevicePlateform="";
				String DeviceOs = "";
				String DeviceManufactures="";
				String decodeLogin = "0";
				//int  newLogin = 0;
				String email = request.getParameter("Email").trim();
				//String password = request.getParameter("Password").trim();
				String password = "";
				
					//password = request.getParameter("Password").trim();
					password = request.getParameter("PasswordNew").trim();
					password = java.net.URLDecoder.decode(password, "UTF-8");
				System.out.println("Password"+password);
				String deviceID = request.getParameter("deviceID").trim();
				
				if(!request.getParameter("model").equals(null) || !request.getParameter("platform").equals(null))
				{
					//System.out.println("Inside if");
					Devicemodel = request.getParameter("model").trim();
					DevicePlateform = request.getParameter("platform").trim();
				}
				else
				{
					//System.out.println("Inside Else");
					Devicemodel="";
					DevicePlateform="";
				}
				if(!request.getParameter("osVersion").equals(null) || !request.getParameter("manufacture").equals(null))
				{
					//System.out.println("Inside if2");
					DeviceOs = request.getParameter("osVersion").trim();
					DeviceManufactures = request.getParameter("manufacture").trim();
				}
				else
				{
					//System.out.println("Inside Else2");
					DeviceOs = "";
					DeviceManufactures="";
				}
				//HashMap objHash = Login.fetchLoginDetails_2(email, password, "", deviceID);
				//old call 
				//System.out.println("befor function");
				HashMap objHash = Login.fetchLoginDetails_new(email, password, "", deviceID,DevicePlateform);
				if(email.equals("")||password.equals(""))
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
