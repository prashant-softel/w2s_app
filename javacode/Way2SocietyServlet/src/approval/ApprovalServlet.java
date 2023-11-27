package approval;

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

import MainClasses.Tenant;
import MainClasses.Task;
import MainClasses.ViewClassifieds;
import Utility.UtilityClass;
import Utility.VerifyToken;

/**
 * Servlet implementation class Approval
 */
@WebServlet("/TenantServlet")
public class ApprovalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApprovalServlet() {
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
			Tenant objApproval = new Tenant(sToken, true, sTkey);
//			out.print(objApproval.getDecryptedTokenMap());
			if(objApproval.IsTokenValid() == true)
			{
				String message = new String();
				HashMap DecryptedTokenMap = objApproval.getDecryptedTokenMap();
//				out.print(DecryptedTokenMap.get("dbname"));
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					HashMap objHash = new HashMap();
					switch (flag) {
					case 1:	//fetch Tenants
					{					
						objHash = objApproval.mFetchTenants(); 
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
						break;
					}
					case 2: //approve Tenants
					{
						if(!request.getParameterMap().containsKey("tenant_id") || Integer.parseInt(request.getParameter("tenant_id").trim())==0){  	
							message = "Assign Tenant ID";  	
						}
						else
						{
							int iTenantsID =  Integer.parseInt(request.getParameter("tenant_id").trim());
							objHash = objApproval.mApproveTenants(iTenantsID);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						break;
					}
					case 3:	//fetch Classifieds
					{	
						int iSocietyID = Integer.parseInt((String)objApproval.getDecryptedTokenMap().get("society_id"));
						objHash = objApproval.mFetchClassified(iSocietyID); 
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
						break;
					}
					case 4: //approve Classifieds
					{
						if(!request.getParameterMap().containsKey("id") || Integer.parseInt(request.getParameter("id").trim())==0){  	
							message = "Assign classified ID";  	
						}
						else
						{
							int iClassifiedID =  Integer.parseInt(request.getParameter("id").trim());
							objHash = objApproval.mApproveClassified(iClassifiedID);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						break;
					}
					case 5:	//fetch service provider
					{					
						int iSocietyID = Integer.parseInt((String)objApproval.getDecryptedTokenMap().get("society_id"));
						objHash = objApproval.mFetchServiceProvider(iSocietyID) ;
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
						break;
					}
					case 6: //approve service providers
					{
						if(!request.getParameterMap().containsKey("service_prd_reg_id") || Integer.parseInt(request.getParameter("service_prd_reg_id").trim())==0){  	
							message = "Assign Service Provider ID";  	
						}
						else
						{
							int iServPrdID =  Integer.parseInt(request.getParameter("service_prd_reg_id").trim());
							objHash = objApproval.mApproveServiceProvider(iServPrdID);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						break;
					}
					case 7: //comment Tenants
					{
						if(!request.getParameterMap().containsKey("tenant_id") || Integer.parseInt(request.getParameter("tenant_id").trim())==0){  	
							message = "Tenant ID Not Found";  	
						}
						else
						{
							int iTenantsID =  Integer.parseInt(request.getParameter("tenant_id").trim());
							//int sComment =  Integer.parseInt(request.getParameter("comment").trim());
							String sComment = request.getParameter("comment");
							objHash = objApproval.mGetMoreDetailsOfTenantsForApproval(iTenantsID,sComment);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						break;
					}
					case 8: //Remove Classified
					{
						if(!request.getParameterMap().containsKey("id") || Integer.parseInt(request.getParameter("id").trim())==0){  	
							message = "Remove classified ID";  	
						}
						else
						{
							int iClassifiedID =  Integer.parseInt(request.getParameter("id").trim());
							objHash = objApproval.mRemoveClassified(iClassifiedID);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						break;
					}
					
					default:
						out.println("{\"success\":0,\"response\":{\"message\":\"Servlet FLAG error\"}}");
						break;
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
