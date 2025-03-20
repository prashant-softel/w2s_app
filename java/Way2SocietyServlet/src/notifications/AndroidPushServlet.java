package notifications;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import MainClasses.AndroidPush;
import Utility.VerifyToken;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Notification")
public class AndroidPushServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidPushServlet() {
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
	    response.addHeader("Access-Control-Allow-Methods", "GET,POST");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "86400");
	    
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();
	
		AndroidPush obj = new AndroidPush();
		try{
			String sTitle = request.getParameter("title").trim();//"Test Title";
			String sMessage = request.getParameter("message").trim();//"Test Message"; 
			String sDeviceID = request.getParameter("deviceID").trim();
			
			String sMapID = "0";
			String sPageRef = "";
			String sDetails = "";
			String sPageName = "";
			
			if((request.getParameter("map_id") != null) && !request.getParameter("map_id").isEmpty())
			{
				sMapID = request.getParameter("map_id").trim();
			}
			
			if((request.getParameter("page_ref") != null) && !request.getParameter("page_ref").isEmpty())
			{
				sPageRef = request.getParameter("page_ref").trim();
			}
			
			if((request.getParameter("details") != null) && !request.getParameter("details").isEmpty())
			{
				sDetails = request.getParameter("details").trim();
			}
			//System.out.println("MApping" + sMapID+ "Page Ref"+sPageRef+ "Details"+sDetails);
			switch(sPageRef)
			{
				case "1" : //Bill
					sPageName = "ViewbillPage";
					break;
				case "2" : //Notice
					sPageName = "ViewnoticePage";
					break;
				case "3" : //Event
					sPageName = "VieweventPage";
					break;
				case "4" : //fine
					sPageName = "ViewimposefinePage";
					break;
				case "5" : //Poll
					sPageName = "TakepollPage";
					break;
				case "6" : //Classified
					sPageName = "ClassifiedsdetailsPage";
					break;
				case "7" : //Service Provider
					sPageName = "ProviderDetailsPage";
					break;
				case "8" : //service request
					sPageName = "ViewServiceRequestPage";
					break;
				case "9" : //Poll
					sPageName = "GeneralSmS";
					break;
				case "10" : //Manager Apk send to notification
					sPageName = "ProviderDetailsPage";
					break;
				case "11" : //Manager Apk send to notification
					sPageName = "VisitorInPage";
					break;
				default : //
					sPageName = "SocietyPage";
					break;
					
			}
			///System.out.println("Received Data : Page name : " + sPageName);			
			//System.out.println("Received Data : Page Ref : " + sPageRef + " Details : " + sDetails);
			
			String sResponse = AndroidPush.sendPushNotification(sTitle, sMessage, sDeviceID, sMapID, sPageRef, sPageName, sDetails);
			
			String sResult = "";
			if(sResponse.equals("200"))
			{
				sResult = "{\"success\":1, \"response\":{\"message\":\""+ sResponse +"\"}}";
			}
			else
			{
				sResult = "{\"success\":0, \"response\":{\"message\":\""+ sResponse +"\"}}";
			}
			
			response.setContentType("application/json");
			out.println(sResult);
			
		}	
		catch(Exception e){
			e.printStackTrace();
			out.print(e.getMessage());
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
