package members;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import MainClasses.DashBoard;
import MainClasses.MemberLedger;
import Utility.VerifyToken;

/**
 * Servlet implementation class MemberLedgerServlet
 */
@WebServlet("/MemberLedger")
public class MemberLedgerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberLedgerServlet() {
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
			MemberLedger objMemberLedger = new MemberLedger(sToken, true, sTkey);
			
			if(objMemberLedger.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objMemberLedger.getDecryptedTokenMap();
				//out.print(DecryptedTokenMap);
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					if(request.getParameterMap().containsKey("fetch"))    /// New Version Call To Inside If
				 	{
						if(Integer.parseInt(request.getParameter("fetch")) == 1)
				 		{
							int iUnitID= Integer.parseInt(request.getParameter("UnitID"));
							
							 if(iUnitID == 0)
							 {
								 
								 iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
								
							 }
							 else
							 {
								 //sUnitID= Integer.parseInt(request.getParameter("uID"));
								// System.out.println("Inside else" +iUnitId );
							 }
							
							HashMap objHash=new HashMap();
							objHash = objMemberLedger.mfetchBillNReceiptsDetailsNew(iUnitID);
							HashMap objHash1=new HashMap();
							objHash1=(HashMap) objHash.get("response");
							ArrayList objHash2=new ArrayList();
							objHash2=(ArrayList) objHash1.get("BillnReceipts");
							ArrayList tmpResult = new ArrayList();
							tmpResult= sortArray(objHash2,"sdate");
							objHash1.put("BillnReceipts",tmpResult);
							objHash.put("response", objHash1);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 	}
					else
					{
						int iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
						HashMap objHash=new HashMap();
						objHash = objMemberLedger.mfetchBillNReceiptsDetails(iUnitID);
						HashMap objHash1=new HashMap();
						objHash1=(HashMap) objHash.get("response");
						ArrayList objHash2=new ArrayList();
						objHash2=(ArrayList) objHash1.get("BillnReceipts");
						ArrayList tmpResult = new ArrayList();
						tmpResult= sortArray(objHash2,"sdate");
						objHash1.put("BillnReceipts",tmpResult);
						objHash.put("response", objHash1);
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
			else
			{
				out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			//out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}   ");
			
			
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	
	protected ArrayList sortArray(ArrayList array,String inputKey) throws ParseException
		{
		
		if (array == null || array.size() <= 1) {
	        return array;
	    }

	    for (int i = 0; i < array.size(); i++) 
	    	{
	        for (int j = 1; j < (array.size()-i); j++) 
	        	{
	        	HashMap arrayobject1=new HashMap();
	        	HashMap arrayobject2=new HashMap();
	        	arrayobject1=(HashMap) array.get(j);
	        	arrayobject2=(HashMap) array.get(j-1);
	        	String date1=(String) arrayobject1.get(inputKey);
	        	String date2=(String) arrayobject2.get(inputKey);
	        	try{
	        	Date d1=new SimpleDateFormat("yyyy-MM-dd").parse(date1);
	        	Date d2=new SimpleDateFormat("yyyy-MM-dd").parse(date2);  
	        	if(!(d1.compareTo(d2)>0))
	        		{
	        		HashMap arrayobject3=new HashMap();
	        		arrayobject3=arrayobject2;
	        		arrayobject2=arrayobject1;
	        		arrayobject1=arrayobject3;
	        		}
	        	array.set(j, arrayobject1);
	        	array.set(j-1, arrayobject2);
	        	}
	        	catch(Exception e)
	        		{return array;}
	        	}
	        	
	    	}
	    
	    return array;
		}
	
	
	
}
