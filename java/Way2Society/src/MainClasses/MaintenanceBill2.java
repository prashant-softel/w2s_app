package MainClasses;

import java.lang.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import MainClasses.CommonBaseClass.*;

public class MaintenanceBill2  extends CommonBaseClass{

	public  MaintenanceBill2(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * @param args
	 */
	
	 public static String convert(HashMap<Integer,Map<String,Object>> rows) {
	        Gson gson = new Gson();
	        String json = gson.toJson(rows);
	        return json;
	    }
	
	public static  HashMap<Integer, Map<String, Object>> mfetchMaintenanceBillList(int iUnitId)
	{
		
		String Sql = "SELECT * FROM `unit`";
		 //System.out.println("first:" + Sql); 
		
		HashMap<Integer, Map<String, Object>> rows = CommonBaseClass.m_objDbOperations.Select(Sql);
		
		// Create a hash map
		//HashMap<Integer, Map<String, Object>> hm = new HashMap();
	      
	      // Put elements to the map
	      //hm.put(1,new HashMap(){{put("test",0);}});
	     
	      
	     //System.out.println(hm); 
	     //System.out.println(Sql); 
		
	   //  System.out.println("size of map:" + rows.size()); 
		return rows;
	}
	
	
	public static String mfetchMaintenanceBillList1(int iUnitId)
	{
		
		String Sql = "SELECT * FROM `billdetails` where `UnitID`=" + iUnitId;
		
		
		HashMap<Integer, Map<String, Object>> rows = CommonBaseClass.m_objDbOperations.Select(Sql);
		
		// Create a hash map
		//HashMap<Integer, Map<String, Object>> hm = new HashMap();
	      
	      // Put elements to the map
	      //hm.put(1,new HashMap(){{put("test",0);}});
	     
	      
	     //System.out.println(hm); 
	     //System.out.println(Sql); 
		
	     Sql += "size of map:" + rows.size(); 
	     return Sql;
	}
	
	/*public static String stringTest()
	{
		if(CommonBaseClass.m_objDbOperations.mMysqli == null)
		{
			return "mysql const"; 
		}
		else
		{
			return CommonBaseClass.m_objDbOperations.sConnErrorMsg;
		}
		
			
		//return "hello";
	}
	*/
	
	
	public static Connection objectTest()
	{
		return CommonBaseClass.m_objDbOperations.mMysqli;
			
		//return "hello";
	}
	
	
	public static void main(String[] args) throws Exception
	{
		//MaintenanceBill2 obj = new MaintenanceBill2(false, "hostmjbt_society7");
		//HashMap<Integer, Map<String, Object>> rows = obj.mfetchMaintenanceBillList(14);	
		//System.out.println(obj.convert(rows));
		//System.out.println(rows);
		
	}
	
	
	

}
