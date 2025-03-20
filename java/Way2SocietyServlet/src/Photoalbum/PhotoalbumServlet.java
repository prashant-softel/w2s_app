package Photoalbum;  	
  	
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
  	
import MainClasses.ViewPhotoAlbum;  	
import Utility.VerifyToken;  	
  	
/**  	
 * Servlet implementation class Photoalbum  	
 */  	
@WebServlet("/Photoalbum")  	
public class PhotoalbumServlet extends HttpServlet {  	
 private static final long serialVersionUID = 1L;  	
         	
    /**  	
     * @see HttpServlet#HttpServlet()  	
     */  	
    public PhotoalbumServlet() {  	
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
  
//  String str = request.getRequestURI();
    	
  try  	
   {  	
	   String sToken = request.getParameter("token").trim();  	
	   String sTkey = request.getParameter("tkey").trim();  	
	   ViewPhotoAlbum objPhotoAlbum = new ViewPhotoAlbum(sToken, true, sTkey);  	
	   if(objPhotoAlbum.IsTokenValid() == true)  	
	   {  	 
		    HashMap DecryptedTokenMap = objPhotoAlbum.getDecryptedTokenMap();  	
		    //out.print(DecryptedTokenMap);  	
		    if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid")  	
		    {  	
		    	//System.out.println("Society_id" +Integer.parseInt((String)(DecryptedTokenMap.get("society_id"))));
			     String message=new String();  	
			     if(request.getParameter("fetch").equals("photoalbum"))    	 
			      {  	
				      HashMap objHash=new HashMap();  
				      //Old Code
				      objHash = ViewPhotoAlbum.mfetchPhotoalbum(Integer.parseInt((String)(DecryptedTokenMap.get("society_id")))); 
				     // New Code 
				      //objHash = ViewPhotoAlbum.mGetPhotoAlbum(Integer.parseInt((String)(DecryptedTokenMap.get("society_id")))); 
				  	
				      Gson objGson = new Gson();  	
				      String objStr = objGson.toJson(objHash);  	
				      response.setContentType("application/json");  	
				      out.println(objStr);  	
			      } 
			     
			     else if(request.getParameter("fetch").equals("reportedphotoalbum"))    	 
			      {  	
				      HashMap objHash=new HashMap();  	
				      objHash = ViewPhotoAlbum.mfetchReportedPhotoalbum(Integer.parseInt((String)(DecryptedTokenMap.get("society_id")))); 
				  	
				      Gson objGson = new Gson();  	
				      String objStr = objGson.toJson(objHash);  	
				      response.setContentType("application/json");  	
				      out.println(objStr);  	
			      }
			     
			     else if(request.getParameter("fetch").equals("disapprovedphotoalbum"))    	 
			      {  	
				      HashMap objHash=new HashMap();  	
				      objHash = ViewPhotoAlbum.mfetchDisapprovedPhotoalbum(Integer.parseInt((String)(DecryptedTokenMap.get("society_id")))); 
				  	
				      Gson objGson = new Gson();  	
				      String objStr = objGson.toJson(objHash);  	
				      response.setContentType("application/json");  	
				      out.println(objStr);  	
			      }
			     
			     else if (request.getParameter("fetch").equals("id"))
			     {
			    	 String name = request.getParameter("name");
			    	 HashMap objHash=new HashMap();  	
			         objHash = ViewPhotoAlbum.getAlbumID(name); 
			           	
			         Gson objGson = new Gson();  	
			         String objStr = objGson.toJson(objHash);  	
			         response.setContentType("application/json");  	
			         out.println(objStr);  	
			     }
			     
			     else if (request.getParameter("fetch").equals("approval"))
			     {
			    	 int imageId = Integer.parseInt(request.getParameter("image_id"));
			    	 HashMap objHash=new HashMap();  	
			         objHash = ViewPhotoAlbum.approveImage(imageId);
			           	
			         Gson objGson = new Gson();  	
			         String objStr = objGson.toJson(objHash);  	
			         response.setContentType("application/json");  	
			         out.println(objStr);  	
			     }
			     else if (request.getParameter("fetch").equals("disapproval"))
			     {
			    	 int imageId = Integer.parseInt(request.getParameter("image_id"));
			    	 HashMap objHash=new HashMap();  	
			         objHash = ViewPhotoAlbum.disApproveImage(imageId); 
			           	
			         Gson objGson = new Gson();  	
			         String objStr = objGson.toJson(objHash);  	
			         response.setContentType("application/json");  	
			         out.println(objStr);  	
			     }

			     else if (request.getParameter("fetch").equals("savemember_id"))
			     {
			    	 int memberId = Integer.parseInt(request.getParameter("member_id"));
			    	 int imageId = Integer.parseInt(request.getParameter("image_id"));
			    	 HashMap objHash=new HashMap();  	
			         objHash = ViewPhotoAlbum.saveMemberId(imageId , memberId); 
			           	
			         Gson objGson = new Gson();  	
			         String objStr = objGson.toJson(objHash);  	
			         response.setContentType("application/json");  	
			         out.println(objStr);  	
			     }
			     
			     else if (request.getParameter("fetch").equals("timestamp"))
			     {
			    	 String timestamp = request.getParameter("timestamp");
			      	 int imageId = Integer.parseInt(request.getParameter("image_id"));
			    	 HashMap objHash=new HashMap();  	
			         objHash = ViewPhotoAlbum.saveTimestamp(imageId, timestamp); 
			           	
			         Gson objGson = new Gson();  	
			         String objStr = objGson.toJson(objHash);  	
			         response.setContentType("application/json");  	
			         out.println(objStr);  	
			     }
			     
			     else if (request.getParameter("fetch").equals("report"))
			     {
			    	 int imageId = Integer.parseInt(request.getParameter("image_id"));
			    	 String comment = request.getParameter("comment").toString();
			    	 HashMap objHash=new HashMap();  	
			         objHash = ViewPhotoAlbum.reportImage(imageId, comment); 
			           	
			         Gson objGson = new Gson();  	
			         String objStr = objGson.toJson(objHash);  	
			         response.setContentType("application/json");  	
			         out.println(objStr);  	
			     }
			     // New Code for album
			     else if (request.getParameter("fetch").equals("Allphotoalbum"))
			     {
			    	// System.out.println("Society_id" +Integer.parseInt((String)(DecryptedTokenMap.get("society_id"))));
			    	 HashMap objHash=new HashMap();  
				      //Old Code
				     // objHash = ViewPhotoAlbum.mfetchPhotoalbum(Integer.parseInt((String)(DecryptedTokenMap.get("society_id")))); 
				     // New Code 
				      objHash = ViewPhotoAlbum.mGetPhotoAlbum(Integer.parseInt((String)(DecryptedTokenMap.get("society_id")))); 
				  	
				      Gson objGson = new Gson();  	
				      String objStr = objGson.toJson(objHash);  	
				      response.setContentType("application/json");  	
				      out.println(objStr);  	
			     }
			     else if (request.getParameter("fetch").equals("photoByAlbumID"))
			     {
			    	 int iAlbumID = Integer.parseInt(request.getParameter("AlbumID"));
			    	 HashMap objHash=new HashMap();  
				      //Old Code
				     // objHash = ViewPhotoAlbum.mfetchPhotoalbum(Integer.parseInt((String)(DecryptedTokenMap.get("society_id")))); 
				     // New Code 
				      objHash = ViewPhotoAlbum.mFetchAllPhotos(iAlbumID,Integer.parseInt((String)(DecryptedTokenMap.get("society_id")))); 
				  	
				      Gson objGson = new Gson();  	
				      String objStr = objGson.toJson(objHash);  	
				      response.setContentType("application/json");  	
				      out.println(objStr);  	
			     }
			     
			     else
			      {  	
			    	 out.println("{\"success\":0,\"response\":{\"message\":\"Cannot Display Photoalbum\"}}   ");  	
			      }  	
		     }  	
	   }  	
	   else  	
	   {  	
	    out.println("{\"success\":0,\"response\":{\"message\":\"Cannot Display Photoalbum\"}}   ");  	
	   }  	
   }  	
  	catch(Exception e)  	
    {  	
	    e.printStackTrace();  	
	    out.println("{\"success\":0,\"response\":{\"message\":\"Cannot Display Photoalbum\"}}   ");  	
    }  	
 }  
}
  	
 /**  	
  * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)  	
  */  	 		