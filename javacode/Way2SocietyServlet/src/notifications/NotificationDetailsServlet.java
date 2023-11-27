package notifications;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bills.BillServlet;

import org.json.JSONObject;

import com.google.gson.Gson;

import MainClasses.Fine;
import MainClasses.TakePoll;
import MainClasses.ViewBills;
import MainClasses.ViewEvents;
import MainClasses.ViewImposeFine;
import MainClasses.ViewNotices;
import MainClasses.ViewServiceProvider;
import MainClasses.ViewClassifieds;
import MainClasses.ViewServiceRequests;
import MainClasses. ViewDirectory;
import Utility.VerifyToken;

/**
 * Servlet implementation class Login
 */
@WebServlet("/NotificationDetails")
public class NotificationDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotificationDetailsServlet()  {
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
	    response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,DELETE,PUT");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "86400");
	    
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();
	
		try
		{
			String sToken = request.getParameter("token").trim();
			String sTkey = request.getParameter("tkey").trim();
			String sPageRef = request.getParameter("page_ref").trim();
			String sDetails = request.getParameter("details").trim();
			String[] sAryDetails = sDetails.split("/");
			//System.out.println("Page Ref : "+sPageRef);
			String sRequestServletURL = "";
			
			switch(sPageRef)
			{
				case "1" : //Bill
						
					String sPeriodID = sAryDetails[0]; 
					String sBT = sAryDetails[1]; 
					//sRequestServletURL = "Bill?PeriodID="+sPeriodID+"&BT="+sBT;
					
					ViewBills objBill = new ViewBills(sToken, true, sTkey);
					
					if(objBill.IsTokenValid() == true)
					{
						HashMap DecryptedTokenMap = objBill.getDecryptedTokenMap();
						
						if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid")
						{
							Integer iUnitID = Integer.parseInt((String)DecryptedTokenMap.get("unit_id"));
													
							if(sPeriodID.isEmpty() && sBT.isEmpty())
							{
								HashMap objHash = objBill.mfetchAllBills(iUnitID);
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
							}
							else
							{
								HashMap objHash = objBill.mfetchMemberBill(iUnitID, Integer.parseInt(sPeriodID), Integer.parseInt(sBT));
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
							}
						}
						else			
						{
							out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
					break;
				case "2" : //Notice
					
					String sNoticeID = sAryDetails[0];

					//System.out.println("Received Data : Notice ID: " + sNoticeID );
					ViewNotices objNotices = new ViewNotices(sToken, true, sTkey);
					
					if(objNotices.IsTokenValid() == true)
					{
						HashMap DecryptedTokenMap = objNotices.getDecryptedTokenMap();
						if(VerifyToken.VerifyToken(DecryptedTokenMap, request) == "valid")
						{
							Integer sUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							Integer sSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							HashMap objHash = objNotices.mfetchAllNotices(sUnitID, sSocietyID, Integer.parseInt(sNoticeID), true);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else
						{
							out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
					
					break;
              case "3" : //Event
					
					String sEventID = sAryDetails[0];
					//System.out.println("Received Data : eventID: " + sEventID );
					ViewEvents objEvents = new ViewEvents(sToken, true, sTkey);
					
					if(objEvents.IsTokenValid() == true)
					{
						HashMap DecryptedTokenMap = objEvents.getDecryptedTokenMap();
						//out.print(DecryptedTokenMap);
						if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
						{
							Integer iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));				
							HashMap objHash = objEvents.mfetchAllEvents(iUnitID, iSocietyID, Integer.parseInt(sEventID), true);

							//System.out.println("Received Data : event data: " + objHash );
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else
						{
						out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
					else
					{
						out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
					}
					break;
					
              case "4" : //Fine
					
					String sRevId = sAryDetails[0];
					
					ViewImposeFine objFine = new ViewImposeFine(sToken, true, sTkey);
					
					if(objFine.IsTokenValid() == true)
					{
						HashMap DecryptedTokenMap = objFine.getDecryptedTokenMap();
						//out.print(DecryptedTokenMap);
						if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
						{
							out.println("{\"success\":1,\"response\":{\"ID\":\"" + sRevId + "\"}}   ");
							//response.setContentType("application/json");
							//out.println(objStr);
						}
						else
						{
							out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
					
					break;
             case "5" : //Poll
					
					String iPollId = sAryDetails[0];
					String iGroupID = sAryDetails[1];
				//	System.out.println("Received Data : PollID: " + iPollId );
					TakePoll objTakePoll = new TakePoll(sToken, true, sTkey);
					if(objTakePoll.IsTokenValid() == true)
					{
						HashMap DecryptedTokenMap = objTakePoll.getDecryptedTokenMap();
						//out.print(DecryptedTokenMap);
						if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
						{
							
							//iUnitId, iGroupId, iSocietyID, iPollId, iOptionId
							Integer iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
							//Integer iGroupId =  Integer.parseInt((String) DecryptedTokenMap.get("group_id"));
							//Integer iOptionId = Integer.parseInt((String) DecryptedTokenMap.get("option_id"));
							//HashMap objHash = objTakePoll.mfetchUserVote(iUnitId,iGroupId,iSocietyID,Integer.parseInt(iPollId));
							//first call to 
							HashMap objHash = objTakePoll.mfetchUserVote(iUnitId,Integer.parseInt(iGroupID),iSocietyID,Integer.parseInt(iPollId));
							out.println("{\"success\":1,\"response\":{\"society_id\":\"" + iSocietyID + "\",\"group_id\":\""+sAryDetails[0]+"\",\"poll_id\":\""+sAryDetails[1]+"\",\"active\":\"true\"}}   ");
							//out.print(DecryptedTokenMap);
							///System.out.println("Result :"+objHash);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
							
							
							//out.println(objStr.response);
							//out.println("{\"success\":1,\"response\":{\"society_id\":\"" + iSocietyID + "\",\"group_id\":\""+sAryDetails[1]+"\",\"poll_id\":\""+sAryDetails[0]+"\",\"active\":\"true\"}}   ");

						}
						else
						{
						out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
					else
					{
						out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
					}
					break;
             case "6" : //Classified
					
					String sClassifieID = sAryDetails[0];
					System.out.println("Received Data");
					
					ViewClassifieds objClassifieds = new ViewClassifieds(sToken, true, sTkey); 
					
					if(objClassifieds.IsTokenValid() == true)
					{
						HashMap DecryptedTokenMap = objClassifieds.getDecryptedTokenMap();
						System.out.println("DecryptedTokenMap"+DecryptedTokenMap);
						//out.print(DecryptedTokenMap);
						if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
						{
							int iClassifiedID = 12;
							//Integer iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));				
							HashMap objHash = objClassifieds.mfetchClassifieds(iSocietyID,iClassifiedID);
							
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							
							response.setContentType("application/json");
							
							out.println(objStr);
							//out.println("{\"success\":1,\"response\":{\"ad_title\":\"" + iSocietyID + "\",\"service_prd_reg_id\":\""+sProviderID+"\",\"pending\":\"society\",\"active\":\"true\"}}   ");
						}
						else
						{
						out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
					else
					{
						out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
					}
					break;
             case "7" : //Service provider
					
					String sProviderID = sAryDetails[0];
					//System.out.println("Received Data : eventID: " + sEventID );
					ViewServiceProvider objProvider = new ViewServiceProvider(sToken, true, sTkey);
					
					if(objProvider.IsTokenValid() == true)
					{
						HashMap DecryptedTokenMap = objProvider.getDecryptedTokenMap();
						//out.print(DecryptedTokenMap);
						if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
						{
							Integer iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));				
							//HashMap objHash = objProvider.mProviderDetails(Integer.parseInt(sProviderID), iSocietyID, iUnitID);
							out.println("{\"success\":1,\"response\":{\"society_id\":\"" + iSocietyID + "\",\"service_prd_reg_id\":\""+sProviderID+"\",\"pending\":\"society\",\"active\":\"true\"}}   ");
							//System.out.println("Received Data : event data: " + objHash );
							//Gson objGson = new Gson();
							//String objStr = objGson.toJson(objHash);
							//response.setContentType("application/json");
							//out.println(objStr);
						}
						else
						{
						out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
					else
					{
						out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
					}
					break;
             case "8" : //Service Request
					
					String sRequestNo = sAryDetails[0];
					//System.out.println("Received Data : eventID: " + sEventID );
					ViewServiceRequests objServiceRequest = new ViewServiceRequests(sToken, true, sTkey);
					
					if(objServiceRequest.IsTokenValid() == true)
					{
						HashMap DecryptedTokenMap = objServiceRequest.getDecryptedTokenMap();
						//out.print(DecryptedTokenMap);
						if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
						{
							Integer iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id"));
							Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));				
							HashMap objHash = objServiceRequest.mfetchServiceRequestHistory(Integer.parseInt(sRequestNo));

							//System.out.println("Received Data : event data: " + objHash );
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
							
						}
						else
						{
						out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						}
					}
					else
					{
						out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
					}
					break;
             case "9" : //GeneralSmS
					
					String sSms = sAryDetails[0];
					//System.out.println("Received Data : eventID: " + sEventID );
					//String sVisitor = sAryDetails[0];
					
//ViewImposeFine objFine = new ViewImposeFine(sToken, true, sTkey);
					
					//if(objFine.IsTokenValid() == true)
					//{
						//HashMap DecryptedTokenMap = objFine.getDecryptedTokenMap();
						//out.print(DecryptedTokenMap);
						//if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
						//{
							out.println("{\"success\":1,\"response\":{\"Content\":\"" + sSms + "\"}}   ");
							//response.setContentType("application/json");
							//out.println(objStr);
						//}
						//else
						//{
							//out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						//}
					//}
						//}
						
				
					break;
             case "10" : //Visitor Notification coming from  Manager apk 
					
					String sVisitor = sAryDetails[0];
					
					
							out.println("{\"success\":1,\"response\":{\"ID\":\"" + sVisitor + "\",\"fetch\":\"6\"}}   ");
//							
					break;
             case "11" : //Staff Notification coming from  Manager apk 
					
					String sStaffId = sAryDetails[0];
					String sSocietyId = sAryDetails[1];
					//String sStaffId = 10;
					
					ViewServiceProvider objProviderStaff = new ViewServiceProvider(sToken, true, sTkey);
					
					//if(objProviderStaff.IsTokenValid() == true)
					//{
						//HashMap DecryptedTokenMap = objProviderStaff.getDecryptedTokenMap();
						//out.print(DecryptedTokenMap);
						//if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
						//{
							//Integer iUnitId = Integer.parseInt((String) DecryptedTokenMap.get("service_prd_reg_id"));
							//Integer iSocietyID = Integer.parseInt((String) DecryptedTokenMap.get("society_id"));
						//}
							
					//}
					out.println("{\"success\":1,\"response\":{\"society_id\":\"" + sSocietyId + "\",\"service_prd_reg_id\":\""+sStaffId+"\",\"pending\":\"society\",\"active\":\"true\"}}");
							//out.println("{\"success\":1,\"response\":{\"society_id\":\"" + iSocietyID + "\",\"service_prd_reg_id\":\""+sProviderID+"\",\"pending\":\"society\",\"active\":\"true\"}}   ");
							//out.println("{\"success\":1,\"response\":{\"society_id\":\"" + iSocietyID + "\",\"service_prd_reg_id\":\""+sProviderID+"\",\"pending\":\"society\",\"active\":\"true\"}}   ");
							//response.setContentType("application/json");
							//out.println(objStr);
						//}
						//else
						//{
							//out.println("{\"success\":0,\"response\":{\"message\":\""+VerifyToken.VerifyToken(DecryptedTokenMap,request)+"\"}}   ");
						//}
					//}
					break;
          
			
				default :
					break;
			}
			
			if(!sRequestServletURL.isEmpty())
			{
				sRequestServletURL += "&token=" + sToken + "&tkey=" + sTkey;
				
				RequestDispatcher rd = request.getRequestDispatcher(sRequestServletURL);
				rd.forward(request,response);
			}
		}
		catch(Exception ex)
		{
			//out.println(ex.getMessage());
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
