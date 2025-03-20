package bills;

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

import MainClasses.MaintenanceBill2;
import MainClasses.ViewBills;
import Utility.VerifyToken;

/**
 * Servlet implementation class MaintenenceBill
 */
@WebServlet("/MaintenenceBill")
public class MaintenenceBill extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MaintenenceBill() {
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
			MaintenanceBill2 mainBill = new MaintenanceBill2(sToken, true, sTkey);
			
			
			HashMap objHash = mainBill.mfetchMaintenanceBillList(14);
			//out.println(objHash);
			Gson objGson = new Gson();
			String objStr = objGson.toJson(objHash);
			response.setContentType("application/json");
			out.println(objStr);
			
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


	/*

	<?php 
	header("Access-Control-Allow-Origin : *");
	header("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept");
	header('Access-Control-Allow-Methods: GET, POST, PUT');

	require_once("java/Java.inc");
	$javaobj = new Java("javatest.MaintenanceBill",true, "hostmjbt_society7");
	
	
	
	$result = $javaobj->mfetchMaintenanceBillList(14);
	
	$result2 = $javaobj->stringTest();
	echo "result:".$result2;
	$res = $javaobj->convert($result);
	echo $res;
	echo "sizeof:".sizeof(java_values($result));
	$array = java_values($result);
	foreach ($array as $k => $v) 
	{
	echo "$k=>$v";
	  echo "<br/>\n";
	  	foreach ($v as $k2 => $v2)
		{
			echo "$k2=>$v2";
	  echo "<br/>\n";
		}
	}

	$test = array(array("success" => 0),  array("data" => array("0" => "end")));
	//echo json_encode($test);
	?>
	*/
