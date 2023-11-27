package directory;

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
import com.google.gson.JsonObject;

import MainClasses.DashBoard;
import MainClasses.ViewDirectory;

import Utility.*;

/**
 * Servlet implementation class ViewDirectoryServlet
 */
@WebServlet("/Directory")
public class ViewDirectoryServlet extends HttpServlet 
	{
	private static final long serialVersionUID = 1L;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewDirectoryServlet() 
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
			ViewDirectory objDirectory = new ViewDirectory(sToken, true, sTkey);
			System.out.println("DATA CALLL");
			if(objDirectory.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objDirectory.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap);
				if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid" )
				{
					//out.print(DecryptedTokenMap);
					if(request.getParameterMap().containsKey("fetch"))
				 	{
				 		if(Integer.parseInt(request.getParameter("fetch")) == 0)
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			HashMap objHash;
				 			if(iSocietyID==253)
				 			{
				 				objHash=new HashMap<>();
				 				
				 			}
				 			else
				 			{
				 			 objHash = objDirectory.mfetchDirectory();	
				 			}
				 			//System.out.println("Directorydata:"+objHash);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 1)
				 		{
				 			HashMap objHash = objDirectory.mfetchProfessionalDirectory();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 2)
				 		{
							HashMap objHash = objDirectory.mfetchBloodGroupDirectory();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		/*else if(Integer.parseInt(request.getParameter("fetch")) == 2)
				 		{
							HashMap objHash = objDirectory.mfetchBloodGroupDirectory();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}*/
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 4)
				 		{
							HashMap objHash = objDirectory.mfetchEmergencyDirectory();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 5)
				 		{
				 			//System.out.println("Call Directory");
							HashMap objHash = objDirectory.mfetchCategoryDirectory();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 6)
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			//int visitorEntryId =191;
				 			int visitorEntryId = Integer.valueOf(request.getParameter("VisitorID"));
							HashMap objHash = objDirectory.mfetchVisitorDetails(visitorEntryId,iSocietyID);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 7)
				 		{
				 			//Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			//int visitorEntryId =191;
							HashMap objHash = objDirectory.mfetchLetestFature();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 8 )
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			//int visitorEntryId =191;
							HashMap objHash = objDirectory.UnitsStatistics();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 9 )
				 		{
				 			String sUserid = (String)DecryptedTokenMap.get("member_id");
				 			//String sActivationCode = request.getParameter("code");
				 			String sActivationCode = request.getParameter("Activationcodecode");    //main code 
				 			//System.out.println("Email" +sUserid); 
				 			//System.out.println("sActivationCode" +sActivationCode);
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			int iLoginID=Integer.parseInt((String) DecryptedTokenMap.get("id"));
				 			//System.out.println("iLoginID" +iLoginID);
							HashMap objHash = objDirectory.LinkLoginIDToActivationCode(sUserid,iLoginID,sActivationCode);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 10 )
				 		{
				 			int iUnitID = Integer.valueOf(request.getParameter("unit_id"));
				 			//int visitorEntryId =191;
							HashMap objHash = objDirectory.mFetchDuesAmount(iUnitID);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 11 )
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			int iUnitID = Integer.valueOf(request.getParameter("UnitID"));
				 			int iMemOtherID = Integer.valueOf(request.getParameter("Member_OtherID"));
				 			
							HashMap objHash = objDirectory.mSendActivationCode(iUnitID,iMemOtherID);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 12 )
				 		{
				 			//Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							HashMap objHash = objDirectory.mFetchProperty();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 13 )
				 		{
				 			//Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							HashMap objHash = objDirectory.mFetchAgreementText();			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 14 )
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));

							int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							HashMap objHash = objDirectory.mVisitorinside(iSocietyID,iUnitId);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 15 )
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));

							int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							HashMap objHash = objDirectory.mVisitoroutside(iSocietyID,iUnitId);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 16 )
				 		{
				 			//int iSocietyID,int visitorID,int iUnitId
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			int VisitorId = Integer.valueOf(request.getParameter("VisitorExistID"));
							int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							HashMap objHash = objDirectory.mfetchReport(iSocietyID,VisitorId,iUnitId);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 17 )
				 		{
				 			//int iSocietyID,int visitorID,int iUnitId
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							HashMap objHash = objDirectory.mfetchexpected(iSocietyID,iUnitId);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 18 )
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							//String sFullName = request.getParameter("FullName");
							String sFName = request.getParameter("fName");
							String sLName = request.getParameter("lName");
							String sContact = request.getParameter("ContactNo");
							String sExpectedDate = request.getParameter("Expdate");
							String sExpectedTime = request.getParameter("ExpTime");
							int iPurposeID =Integer.valueOf(request.getParameter("ExpPurpose_id"));
							String sNote = request.getParameter("vNote");
							HashMap objHash = objDirectory.minsertVisitor(iSocietyID,sFName,sLName,sContact,sExpectedDate,sExpectedTime,iUnitId,iPurposeID,sNote);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 19 )
				 		{
				 			Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							
							HashMap objHash = objDirectory.mFetchPurpose(iSocietyID,iUnitId);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 20 )
				 		{
				 			int iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							String l_id=(String) DecryptedTokenMap.get("id");
							String l_name=(String) DecryptedTokenMap.get("name");
							String VisitorID= request.getParameter("VisitorID").trim();
							String approvalstatus = request.getParameter("approvalstatus").trim();
							String note = request.getParameter("note").trim();
							System.out.println(iSocietyID + " " + iUnitId + " "+ l_id + " "+l_name + " "+VisitorID + " "+ approvalstatus + " "+ note );
							HashMap objHash = objDirectory.minsertapproval(iSocietyID,iUnitId,l_id,l_name,VisitorID,approvalstatus,note);			
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 21 )
				 		{
				 			//System.out.println("Testing");
				 			String checkoutnote= request.getParameter("checkoutnote");
				 			//System.out.println("checkoutnote : " + checkoutnote);
				 			String imagestring = "";
				 			if(!request.getParameter("imagestring").equals(""))
				 			{
							imagestring = request.getParameter("imagestring");
				 			}
							//System.out.println("imagestring : " + imagestring );
							String id = request.getParameter("id");
							//System.out.println("id : " + id);
							//System.out.println( id  + " "+checkoutnote + " "+ imagestring  );
							
				 			int iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			
				 			int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							String l_id=(String) DecryptedTokenMap.get("id");
							String l_name=(String) DecryptedTokenMap.get("name");
							//System.out.println(iSocietyID + " " + id +" "+ iUnitId + " "+ l_id + " "+l_name + " "+checkoutnote + " "+ imagestring  );
							HashMap objHash = objDirectory.minsertitemvisitor(iSocietyID,iUnitId,l_id,l_name,checkoutnote,id,imagestring);			
							
							Gson objGson = new Gson();
						    String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 		else if(Integer.parseInt(request.getParameter("fetch")) == 22 )
				 		{
				 			//System.out.println("Testing");
				 			String checkoutnote= request.getParameter("checkoutnote");
				 			//System.out.println("checkoutnote : " + checkoutnote);
				 			
							String id = request.getParameter("id");
							//System.out.println("id : " + id);
							//System.out.println( id  + " "+checkoutnote + " "+ imagestring  );
								
				 			int iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
				 			long fetchstaffid = objDirectory.mfetchstaffattend(iSocietyID,id);
				 			HashMap objHash = new HashMap<>();
				 			if(String.valueOf(fetchstaffid).equals("0"))
				 			{
				 				System.out.println("Staff entry : " + fetchstaffid);
					 			
				 				objHash.put("staffid",fetchstaffid);	
								
				 			}
				 			else
				 			{
				 			
							
				 			int iUnitId = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
							String l_id=(String) DecryptedTokenMap.get("id");
							String l_name=(String) DecryptedTokenMap.get("name");
							
							System.out.println(iSocietyID + " " + id +" "+ iUnitId + " "+ l_id + " "+l_name + " "+checkoutnote  + " " +  fetchstaffid);
							 objHash = objDirectory.minsertitemstaff(iSocietyID,iUnitId,l_id,l_name,checkoutnote,id,String.valueOf(fetchstaffid));			
							
				 			}

							Gson objGson = new Gson();
						    String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							System.out.println("objstr : " + objStr);
							out.println(objStr);
				 		}
				 		
					else if(Integer.parseInt(request.getParameter("fetch")) == 23 )
			 		{
			 			String id= request.getParameter("id");
			 			String jobprofile = request.getParameter("jobprofile");
						int iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
						 HashMap objHash = objDirectory.minsertstaffmarkentry(iSocietyID,id,jobprofile);			
							
						Gson objGson = new Gson();
					    String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						
						out.println(objStr);
			 		}
					else if(Integer.parseInt(request.getParameter("fetch")) == 24 )
			 		{
						int iUnitID= Integer.parseInt(request.getParameter("UnitId"));
						int iPeriodID= Integer.parseInt(request.getParameter("PeriodId"));
						int iBillType= Integer.parseInt(request.getParameter("BillType"));
						String BillFor = request.getParameter("BillFor");
						int iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
						HashMap objHash = objDirectory.SendBillEMail(iUnitID,iPeriodID,iBillType,BillFor);			
							
						Gson objGson = new Gson();
					    String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						
						out.println(objStr);
			 		}
					else if(Integer.parseInt(request.getParameter("fetch")) == 25)
			 		{
						int iMemberPakingID= Integer.parseInt(request.getParameter("memberPakingID"));
						int VehicleType= Integer.parseInt(request.getParameter("VehicleType"));
						int iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
						//System.out.println("MemberId" + iMemberPakingID);
						//System.out.println("VehicleType" + VehicleType);
						//System.out.println("iSocietyID" + iSocietyID);
						HashMap objHash = objDirectory.VehicleRenew(iMemberPakingID,iSocietyID,VehicleType);			
						Gson objGson = new Gson();
					    String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						
						out.println(objStr);
			 		}
				}
				}
				else
				{
					out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
				}
				
			}
			else
			{
				out.println("{\"success\":0,\"response\":{\"message\":\"Invalid\"}}   ");
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
