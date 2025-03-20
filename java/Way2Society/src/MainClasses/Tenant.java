package MainClasses;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import CommonUtilityFunctions.MapUtility;

public class Tenant extends CommonBaseClass  {
	static DbOperations m_dbConn;
	static DbRootOperations m_dbConnRoot;
	static Utility m_Utility;
	
	public Tenant(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException{
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_dbConn = CommonBaseClass.m_objDbOperations;
		m_dbConnRoot = CommonBaseClass.m_objDbRootOperations;
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	public static HashMap<Integer, Map<String, Object>> mFetchClassified(int iSocietyID){
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try {
			//m_dbConnRoot = new DbOperations(false,"hostmjbt_societydb");
			String todayDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//			String sQuery = "SELECT * FROM `classified` WHERE society_id = '"+iSocietyID+"' AND active= '0' AND exp_date >= '"+todayDate+"' AND status = 'Y'";
			String sQuery = "SELECT c1.*, c2.name as category_name FROM `classified` as c1  JOIN `classified_cate` as c2 ON c1.cat_id = c2.cat_id WHERE c1.society_id = '"+iSocietyID+"' AND c1.active= '0' and c1.exp_date >= '"+todayDate+"'  AND c1.status = 'Y' ";
			//System.out.println(sQuery);
			HashMap<Integer, Map<String, Object>> fetchResult = m_dbConnRoot.Select(sQuery);
			if(fetchResult.size() > 0){
				rows.put("success",1);
				rows2.put("classified",MapUtility.HashMaptoList(fetchResult));
				rows.put("response",rows2);
			}
			else{
				rows.put("success",0); 
				rows2.put("message","empty");
				rows.put("response",rows2);
			}
		} catch (Exception e) {
			// TODO: handle exception
			 rows.put("success",0);
			 rows2.put("message", "Exception:"+e.getMessage());
			 rows.put("response",rows2);
		}
		return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mApproveClassified(int iClassifiedID) throws SQLException{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();		
		try {
			//m_dbConnRoot = new DbOperations(false,"hostmjbt_societydb");
			m_dbConnRoot.BeginTransaction();
			String sUpdateQuery = "";
			sUpdateQuery = "UPDATE `classified` SET `active` = '1' WHERE `classified`.`id` ='"+iClassifiedID+"';";
			//System.out.println(sUpdateQuery);	
			long lUpdateID = m_dbConnRoot.Update(sUpdateQuery);
			//System.out.println("lUpdateID<" + lUpdateID + ">");	
			
			if(lUpdateID == 0){
				m_dbConnRoot.RollbackTransaction();
				 rows2.put("message","Unable To approve classified. Please try again");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
			else{
				//update
				 rows2.put("message","Classified Approved Successfully");
				 rows.put("response",rows2);
				 rows.put("success",1);
			}
			m_dbConnRoot.EndTransaction();
		}
		catch (Exception e) {
			m_dbConnRoot.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
		return rows;		
	}
	public static HashMap<Integer, Map<String, Object>> mRemoveClassified(int iClassifiedID) throws SQLException{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();		
		try {
		//	m_dbConnRoot = new DbOperations(false,"hostmjbt_societydb");
			m_dbConnRoot.BeginTransaction();
			String sUpdateQuery = "";
			sUpdateQuery = "UPDATE `classified` SET `status` = 'N' WHERE `classified`.`id` ='"+iClassifiedID+"';";
			//System.out.println(sUpdateQuery);	
			long lUpdateID = m_dbConnRoot.Update(sUpdateQuery);
			//System.out.println("lUpdateID<" + lUpdateID + ">");	
			
			if(lUpdateID == 0){
				m_dbConnRoot.RollbackTransaction();
				 rows2.put("message","Unable To remove classified. Please try again");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
			else{
				//update
				 rows2.put("message","Classified Remove Successfully");
				 rows.put("response",rows2);
				 rows.put("success",1);
			}
			m_dbConnRoot.EndTransaction();
		}
		catch (Exception e) {
			m_dbConnRoot.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
		return rows;		
	}
	public static HashMap<Integer, Map<String, Object>> mFetchServiceProvider(int iSocietyID){
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try {
			//m_dbConnRoot = new DbOperations(false,"hostmjbt_societydb");
			
			String sQuery = "SELECT * FROM `service_prd_reg` WHERE society_id = '"+iSocietyID+"' AND active =  '0'  AND STATUS =  'Y' AND isBlock =  '0'";
			//System.out.println(sQuery);
			HashMap<Integer, Map<String, Object>> fetchResult = m_dbConnRoot.Select(sQuery);
			if(fetchResult.size() > 0){
				rows.put("success",1);
				rows2.put("serviceProvider",MapUtility.HashMaptoList(fetchResult));
				rows.put("response",rows2);
			}
			else{
				rows.put("success",0); 
				rows2.put("message","empty");
				rows.put("response",rows2);
			}
		} catch (Exception e) {
			// TODO: handle exception
			 rows.put("success",0);
			 rows2.put("message", "Exception:"+e.getMessage());
			 rows.put("response",rows2);
		}
		return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> mApproveServiceProvider(int iServPrdID) throws SQLException{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		try {
			//m_dbConnRoot = new DbOperations(false,"hostmjbt_societydb");
			m_dbConnRoot.BeginTransaction();
			String sUpdateQuery = "";
			sUpdateQuery = "UPDATE `service_prd_reg` SET `active` = '1' WHERE `service_prd_reg_id` ='"+iServPrdID+"'";
			//System.out.println(sUpdateQuery);	
			long lUpdateID = m_dbConnRoot.Update(sUpdateQuery);
			//System.out.println("lUpdateID<" + lUpdateID + ">");	
			
			if(lUpdateID == 0){
				m_dbConnRoot.RollbackTransaction();
				rows2.put("message","Unable To approve Service Provider. Please try again");
				rows.put("response",rows2);
				rows.put("success",0);
			}
			else{
				//update
				rows2.put("message","Service Provider Approved Successfully");
				rows.put("response",rows2);
				rows.put("success",1);
			}
			m_dbConnRoot.EndTransaction();
		}
		catch (Exception e) {
			m_dbConnRoot.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
		return rows;		
	}
	
	public static HashMap<Integer, Map<String, Object>> mFetchTenants(){
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try {
			String todayDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//			String sQuery = "SELECT * FROM `tenant_module` WHERE end_date >= '"+todayDate+"' AND status = 'Y'";
			// monami query
			//String sQuery = "SELECT t.*, u.`unit_no` FROM `tenant_module` AS t JOIN `unit` AS u ON t.`unit_id` = u.`unit_id` WHERE t.`end_date` >= '"+todayDate+"' AND t.`status` = 'Y'";
			// my query
			String sQuery = "SELECT t.*, u.`unit_no`,tm.contact_no,tm.email FROM `tenant_module` AS t JOIN `unit` AS u ON t.`unit_id` = u.`unit_id` join `tenant_member` as tm on tm.tenant_id=t.tenant_id WHERE t.`end_date` >= '"+todayDate+"' AND t.`status` = 'Y'";
			//System.out.println(sQuery);
			HashMap<Integer, Map<String, Object>> fetchResult = m_dbConn.Select(sQuery);
			if(fetchResult.size() > 0){
				rows.put("success",1);
				rows2.put("tenants",MapUtility.HashMaptoList(fetchResult));
				rows.put("response",rows2);
			}
			else{
				rows.put("success",0); 
				rows2.put("message","empty");
				rows.put("response",rows2);
			}
		} catch (Exception e) {
			// TODO: handle exception
			 rows.put("success",0);
			 rows2.put("message", "Exception:"+e.getMessage());
			 rows.put("response",rows2);
		}
		return rows;		
	}
	
	public static HashMap<Integer, Map<String, Object>> mApproveTenants(int iTenantsID) throws SQLException{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();		
		try {
			m_dbConn.BeginTransaction();
			String sUpdateQuery = "";
			sUpdateQuery = "UPDATE `tenant_module` SET  `active` = '1' WHERE `tenant_id` = '"+iTenantsID+"'";
			//System.out.println(sUpdateQuery);	
			long lUpdateID = m_dbConn.Update(sUpdateQuery);
			//System.out.println("lUpdateID<" + lUpdateID + ">");	
			
			if(lUpdateID == 0){
//				m_dbConn.RollbackTransaction();
				 rows2.put("message","Unable To approve tenant. Please try again");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
			else{
				 rows2.put("message","Tenant Approved!!");
				 rows.put("response",rows2);
				 rows.put("success",1);
			}
			m_dbConn.EndTransaction();
		}
		catch (Exception e) {
			m_dbConn.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
		return rows;		
	}
	
	public static HashMap<Integer, Map<String, Object>> mGetMoreDetailsOfTenantsForApproval(int iTenantsID, String sComment) throws SQLException{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();		
		try {
			m_dbConn.BeginTransaction();
			String sSelectQuery = "";
			sSelectQuery  = "SELECT * from tenant_module WHERE `tenant_id` = '" + iTenantsID + "'";
			//System.out.println(sSelectQuery );	
			HashMap<Integer, Map<String, Object>> pTenant = m_dbConn.Select(sSelectQuery );
			
			String sNote = "";
			//System.out.println("Tenant: " + pTenant );
			if(pTenant.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> entry_1 : pTenant.entrySet())
				{
					//if(entry_1.getValue().get("note") != null)
					{
						sNote = (String)entry_1.getValue().get("note");
						//System.out.println("Notes: " + sNote );
					}
					
				}
				
			}
			if(sNote.length()>0)
			{
				sNote = sNote + " " + sComment;
			}
			else
			{
				sNote = sComment;
			}
			
			String sUpdateQuery = "";
			sUpdateQuery = "UPDATE `tenant_module` SET  `active` = '2', `note` = '" + sNote + "' WHERE `tenant_id` = '"+iTenantsID+"'";
			//System.out.println(sUpdateQuery);	
			long lUpdateID = m_dbConn.Update(sUpdateQuery);
			//System.out.println("lUpdateID <" + lUpdateID + ">");	
			
			if(lUpdateID == 0){
				 rows2.put("message","Unable To approve tenant. Please try again at techsupportway2society.com");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
			else{
				 rows2.put("message","Tenant Get more details updated!!");
				 rows.put("response",rows2);
				 rows.put("success",1);
			}
			m_dbConn.EndTransaction();
		}
		catch (Exception e) {
			m_dbConn.RollbackTransaction();
			rows2.put("message","Exception : "+e);
			rows.put("response",rows2);
			rows.put("success",0);
		}
		return rows;		
	}

	public static void main(String[] args) throws Exception {
		String sToken = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQI1By2w6EZabMQYAzmQGezd9DqQtrTJpIzjXdGdYysVDnQmWnmpJLbiSlZajLNcAIr3bwLVF9h4H4KRdjNvwZhAj4osSMOyK0DJXYuytzdTsQ";
		String sTkey = "cyhVDzUWs0xc-bD5g3ifsp8h9bEoFjFyWUJjDvEs0NzznN6C7BC7odlvfI-dE9BQQeo-S0j1leyod2D8eCdwJwazKanHVpHxmZ2CnftCG1ZPrLH_IF4sIg1AMHpMiX1iQFpKvd36ddqkLxWCcuoFD2a9U4U9aMZlsv40fL7q_30";
		
	//	Approval obj = new Approval(sToken, true, sTkey);
	//	HashMap rows;
		//rows = obj.mFetchClassified(Integer.parseInt((String)obj.getDecryptedTokenMap().get("society_id")));		
//		rows = obj.mFetchServiceProvider(Integer.parseInt((String)obj.getDecryptedTokenMap().get("society_id")));
//		rows = obj.mFetchTenants();
//		rows = obj.mApproveTenants(12);
		
	//	Gson objGson = new Gson();
		//String objStr = objGson.toJson(rows);

//		System.out.println(obj.getDecryptedTokenMap());
	//	System.out.println(objStr);
	}

}
/**in Servlet, use DecryptedTokenMap.role == Super Admin to proceed **/