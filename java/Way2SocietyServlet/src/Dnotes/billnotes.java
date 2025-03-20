package Dnotes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

//import MainClasses.MemberLedger;
import MainClasses.ViewNotes;
import Utility.VerifyToken;

/**
 * Servlet implementation class billnotes
 */
@WebServlet("/billnotes")
public class billnotes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public billnotes() {
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
			ViewNotes objViewNotes = new ViewNotes(sToken, true, sTkey);
			if(objViewNotes.IsTokenValid() == true)
			{
				HashMap DecryptedTokenMap = objViewNotes.getDecryptedTokenMap();
				if(VerifyToken.VerifyToken(DecryptedTokenMap,request) == "valid")
				{
					if(request.getParameterMap().containsKey("fetch"))    /// New Version Call To Inside If
				 	{
						if(Integer.parseInt(request.getParameter("fetch")) == 1)
				 		{
							int iUnitID = Integer.parseInt((String) DecryptedTokenMap.get("unit_id")); 
							int iBillType = Integer.parseInt((String) DecryptedTokenMap.get("BillType")); 
							HashMap objHash=new HashMap();
							objHash = objViewNotes.mfetchNoteDetails(iUnitID,iBillType);
							HashMap objHash1=new HashMap();
							objHash1=(HashMap) objHash.get("response");
							ArrayList objHash2=new ArrayList();
							objHash2=(ArrayList) objHash1.get("BillnReceipts");
							ArrayList tmpResult = new ArrayList();
							objHash1.put("BillnReceipts",tmpResult);
							objHash.put("response", objHash1);
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
				 		}
				 	}
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

}
