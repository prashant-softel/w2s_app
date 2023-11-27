package MainClasses;

import static MainClasses.ProjectConstants.ENCRYPT_INIT_VECTOR;
import static MainClasses.ProjectConstants.ENCRYPT_SPKEY;

import java.lang.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import CommonUtilityFunctions.MapUtility;
import MainClasses.Bill;
import MainClasses.Payment;
import MainClasses.Receipt;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;

public class ViewDirectory extends CommonBaseClass 
{
	static Directory m_Directory;
	private static Utility m_Utility;
	static Receipt m_Reciept;
	public  ViewDirectory(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
		m_Directory = new Directory(CommonBaseClass.m_objDbOperations);
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Reciept = new Receipt(CommonBaseClass.m_objDbOperations); 
		
	}
	

	public static  HashMap<Integer, Map<String, Object>> mfetchDirectory()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpDirectory = m_Directory.mfetchDirectory(0);
		
		
		 
		if(mpDirectory.size() > 0)
		{
			//add bill to map
			 rows2.put("directory",MapUtility.HashMaptoList(mpDirectory));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("directory","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}

	public static  HashMap<Integer, Map<String, Object>> mVisitorinside(int iSocietyID,int iUnitId) throws ClassNotFoundException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpVisitor = m_Utility.mfetchvisitor(iSocietyID,iUnitId);
		
		
		System.out.println(mpVisitor);
		if(mpVisitor ==null)
		{
			rows2.put("visitorinside","No Database selected");
			rows2.put("DB","0");
			rows.put("response",rows2);
			rows.put("success",1);
		}
		else
		{
		if(mpVisitor.size() > 0)
		{
			
			 rows2.put("visitorinside",MapUtility.HashMaptoList(mpVisitor));
			 rows2.put("DB","1");
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			
			 rows2.put("visitor","");
			 rows2.put("DB","1");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
		}
	  
	    return rows;
	}	
	
	public static  HashMap<Integer, Map<String, Object>> mVisitoroutside(int iSocietyID,int iUnitId) throws ClassNotFoundException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpVisitor = m_Utility.mfetchvisitoroutside(iSocietyID,iUnitId);
		
		
		System.out.println(mpVisitor);
		if(mpVisitor ==null)
		{
			rows2.put("visitorinside","No Database selected");
			rows2.put("DB","0");
			rows.put("response",rows2);
			rows.put("success",1);
		}
		else
		{
		if(mpVisitor.size() > 0)
		{
			
			 rows2.put("visitoroutside",MapUtility.HashMaptoList(mpVisitor));
			 rows2.put("DB","1");
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			
			 rows2.put("visitoroutside","");
			 rows2.put("DB","1");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
		}
	  
	    return rows;
	}	
	
	
	public static  HashMap<Integer, Map<String, Object>> mfetchProfessionalDirectory()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpDirectory1 = m_Directory.mfetchDirectory(1);
		 
		if(mpDirectory1.size() > 0)
		{
			//add bill to map
			 rows2.put("directory", MapUtility.HashMaptoList(mpDirectory1));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("directory","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}

	public static  HashMap<Integer, Map<String, Object>> mfetchBloodGroupDirectory()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpDirectory = m_Directory.mfetchDirectory(2);
		 
		if(mpDirectory.size() > 0)
		{
			//add bill to map
			 rows2.put("directory",MapUtility.HashMaptoList(mpDirectory));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("directory","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}

	public static  HashMap<Integer, Map<String, Object>> mfetchEmergencyDirectory()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpDirectory1 = m_Directory.mfetchDirectory(4);
		 
		if(mpDirectory1.size() > 0)
		{
			//add bill to map
			 rows2.put("directory", MapUtility.HashMaptoList(mpDirectory1));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("directory","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mfetchCategoryDirectory()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpDirectory1 = m_Directory.mfetchDirectory(5);
		 
		if(mpDirectory1.size() > 0)
		{
			//add bill to map
			 rows2.put("category", MapUtility.HashMaptoList(mpDirectory1));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("category","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mfetchVisitorDetails(int iVisitorId, int iSocietyId) throws ClassNotFoundException, ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		System.out.print("Test0");
		HashMap<Integer, Map<String, Object>> mpVisitorDetails = m_Utility.mfetchAllVisitorsDetails(iVisitorId, iSocietyId);		
		System.out.println("mpVisitorDetails :"+ mpVisitorDetails);
		HashMap<Integer, Map<String, Object>> ApprovalMsgDetails = m_Utility.fetchApprovalMsg(iSocietyId);		
		System.out.println("ApprovalMsgDetails :"+ ApprovalMsgDetails);
		System.out.print("Test11");
		if(mpVisitorDetails.size() > 0 ){	
			//bHasRecords = true;
			//add mpBlockedUnit to map
			rows.put("success", "1");
			rows.put("details", rows2);
			rows2.put("VisitorDetails",mpVisitorDetails);
			rows2.put("ApprovalMsgDetails",ApprovalMsgDetails);
			System.out.println(rows2);
		}
		else{
			//no bills found
			rows.put("success", "0");
			rows.put("details", rows2);
			rows2.put("VisitorDetails","");
			rows2.put("ApprovalMsgDetails","");
		}
		return rows;

	}
	public static  HashMap<Integer, Map<String, Object>> mfetchLetestFature()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//System.out.print("Test0");
		
		String sRole =  getRole();
		boolean bTitleOnly = false;
		int iFetchCount=0;
		HashMap<Integer, Map<String, Object>> mpFetureDetails = m_Utility.getAdvertisements(sRole,bTitleOnly,iFetchCount);		
		if(mpFetureDetails.size() > 0)
		{
			//add bill to map
			 rows2.put("LatestFeature", MapUtility.HashMaptoList(mpFetureDetails));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("LatestFeature","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
		 return rows;

	}
	public static  HashMap<Integer, Map<String, Object>> UnitsStatistics()
	{
		int iSocietyid = getSocietyId();
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//System.out.print("Test0");
		
		String sRole =  getRole();
		boolean bTitleOnly = false;
		int iFetchCount=0;
		HashMap<Integer, Map<String, Object>> mpUnitDetails = m_Utility.mfetchUnitsStatistics(iSocietyid);	
		//System.out.println("mpUnitDetails"+mpUnitDetails);
		if(mpUnitDetails.size() > 0)
		{
			//add bill to map
			 rows2.put("UnitStatistics",mpUnitDetails);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("UnitStatistics","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
		//System.out.println(rows);
		return rows;

	}
	/* -----------------------------  Dues amount in admin section ------------------------ */
	
	public static  HashMap<Integer, Map<String, Object>> mFetchDuesAmount(int iUnitID)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpReciept = m_Reciept.getDueAmount(iUnitID);
		 
		if(mpReciept.size() > 0)
		{
			//add bill to map
			 rows2.put("DuesAmount", mpReciept);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("DuesAmount","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
		public static   HashMap<Integer, Map<String, Object>>  LinkLoginIDToActivationCode(String sUserid,int iLoginID,String sActivationCode)
		{
		//	jgpparmar@gmail.com5014
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			int iSocietyid = getSocietyId();
			HashMap<Integer, Map<String, Object>> mpLinkFlate = m_Utility.LinkLoginIDToActivationCode(sUserid, iLoginID, sActivationCode);
			if(mpLinkFlate.size() > 0)
			{
				//add bill to map
				 rows2.put("LinkFlat", mpLinkFlate);
				 rows.put("response",rows2);
				 rows.put("success",1);
			}
			else
			{
				//bills not found
				 rows2.put("LinkFlat","");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
		  
		    return rows;
			
			
			//return m_Utility.LinkLoginIDToActivationCode(sUserid, iLoginID, sActivationCode);
		}
  public static   HashMap<Integer, Map<String, Object>>  mSendActivationCode(int iUnit_id,int iOther_Member_id)
	{
	  
	  	HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		int iSocietyid = getSocietyId();
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpSendCode = m_Utility.SendActivationCode_2(iSocietyid, iUnit_id, iOther_Member_id);;
		
		if(mpSendCode.size() > 0)
		{
			//add bill to map
			 rows2.put("ActivationCode", mpSendCode);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("ActivationCode","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	 // System.out.println("Result "+rows);
	    return rows;
	   
	}
  // Send Bill Email 
 public static   HashMap<Integer, Map<String, Object>>  SendBillEMail(int iUnit_id,int iPeriodID, int iBillType,String BillFor ) throws AddressException, MessagingException
	{
	  
	  	HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		int iSocietyid = getSocietyId();
		//fetching all bills
		
		HashMap<Integer, Map<String, Object>> mpSendEmail = m_Utility.SendBillEmail(iSocietyid,iUnit_id, iPeriodID,iBillType,BillFor);
		
		if(mpSendEmail.size() > 0)
		{
			//add bill to map
			 rows2.put("Sending Bill", mpSendEmail);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("Sending Bill","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	 // System.out.println("Result "+rows);
	    return rows;
	   
	}
  
  public static  HashMap<Integer, Map<String, Object>> mFetchProperty()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpProperty = m_Utility.getPropertyType();
		 
		if(mpProperty.size() > 0)
		{
			//add bill to map
			 rows2.put("PropertyType", mpProperty);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("PropertyType","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
  public static  HashMap<Integer, Map<String, Object>> mFetchAgreementText()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpAgreement = m_Utility.getAgreementText();
		 
		if(mpAgreement.size() > 0)
		{
			//add bill to map
			 rows2.put("AgreementContent", mpAgreement);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//bills not found
			 rows2.put("AgreementContent","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
  
  public static  HashMap<Integer, Map<String, Object>> mfetchexpected(int iSocietyID,int iUnitId) throws ClassNotFoundException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpexVisitor = m_Utility.mfetchexpected(iSocietyID,iUnitId);
		
		
		System.out.println(mpexVisitor);
		if(mpexVisitor ==null)
		{
			rows2.put("visitorinside","No Database selected");
			rows2.put("DB","0");
			rows.put("response",rows2);
			rows.put("success",1);
		}
		else
		{
		if(mpexVisitor.size() > 0)
		{
			
			 rows2.put("visitorlist",MapUtility.HashMaptoList(mpexVisitor));
			 rows2.put("DB","1");
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			
			 rows2.put("visitorlist","");
			 rows2.put("DB","1");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
		}
	    return rows;
	}	
	
  public static  HashMap<Integer, Map<String, Object>> mfetchReport(int iSocietyID,int visitorID,int iUnitId) throws ClassNotFoundException, ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpexVisitor = m_Utility.fetchVisitorsReports(iSocietyID,visitorID,iUnitId);
		
		
		System.out.println(mpexVisitor);
		if(mpexVisitor.size() > 0)
		{
			
			 rows2.put("visitorlist",MapUtility.HashMaptoList(mpexVisitor));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			
			 rows2.put("visitorlist","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}	
	  
  public static  HashMap<Integer, Map<String, Object>> minsertVisitor(int iSocietyID,String sFName,String sLName,String mobile,String ex_date,String ex_time,int unitid,int iPurposeID,String sNote) throws ClassNotFoundException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		
	long minsert = m_Utility.insertexpectedvisitor(iSocietyID,sFName,sLName,mobile,ex_date,ex_time,unitid,iPurposeID,sNote);
		 
		if(minsert > 0)
		{
			 rows2.put("InsertExpected",minsert);
			 rows2.put("message","Inserted Successfully");
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			
			 rows2.put("InsertExpected","");
			 rows2.put("message","No Database available");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
  public static  HashMap<Integer, Map<String, Object>> mFetchPurpose(int iSocietyID,int iUnitId) throws ClassNotFoundException, ParseException
 	{
 		
 		HashMap rows = new HashMap<>();
 		HashMap rows2 = new HashMap<>();
 		
 		HashMap<Integer, Map<String, Object>> mpexPurpose = m_Utility.fetchPurpose(iSocietyID,iUnitId);
 		
 		
 		System.out.println(mpexPurpose);
 		if(mpexPurpose.size() > 0)
 		{
 			
 			 rows2.put("purpose",MapUtility.HashMaptoList(mpexPurpose));
 			 rows.put("response",rows2);
 			 rows.put("success",1);
 		}
 		else
 		{
 			
 			 rows2.put("purpose","");
 			 rows.put("response",rows2);
 			 rows.put("success",0);
 		}
 	  
 	    return rows;
 	}	

  public static  HashMap<Integer, Map<String, Object>> minsertapproval(int iSocietyID,int iUnitId,String l_id,String l_name,String visitorid,String approvalstatus,String note) throws ClassNotFoundException, ParseException
 	{
		System.out.println("Hi");
 		HashMap rows = new HashMap<>();
 		HashMap rows2 = new HashMap<>();
 		long result = m_Utility.pinsertapproval(iSocietyID,iUnitId,l_id,l_name,visitorid,approvalstatus,note);
 		
 		
 		System.out.println(result);
 		if(result > 0)
 		{
 			
 			 rows.put("success",1);
 		}
 		else
 		{
 			 rows.put("success",0);
 		}
 	  
 	    return rows;
 	}	
  public static  HashMap<Integer, Map<String, Object>> minsertitemvisitor(int iSocietyID,int iUnitId,String l_id,String l_name,String checkoutnote,String id,String imagestring) throws ClassNotFoundException, ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		String result = m_Utility.minsertVisitorItem(iSocietyID,iUnitId,l_id,l_name,checkoutnote,id,imagestring);
		 if(!result.equals(""))
		 {
		String[] split = result.split("/");
		long itemid=Long.valueOf(split[0]);
		String dbname = split[1].toString();
	
 		System.out.println(result);
 		if(itemid > 0)
 		{
 			
 			 rows.put("success",1);
 			 rows.put("id",itemid);
 			 rows.put("dbname",dbname);
 		}
 		else
 		{
 			 rows.put("success",0);
 			 rows.put("id","");
 			 rows.put("dbname","");
 		}
		 }
		 else
		 {
			 rows.put("success",0);
 			 rows.put("id","");
 			 rows.put("dbname",""); 
		 }
 	    return rows;
 	}
  
  public static  HashMap<Integer, Map<String, Object>> minsertitemstaff(int iSocietyID,int iUnitId,String l_id,String l_name,String checkoutnote,String id,String fetchstaffid) throws ClassNotFoundException, ParseException
	{
		
	  HashMap DecryptedTokenMap = getDecryptedTokenMap();
	  String dbname1 = ((String)DecryptedTokenMap.get("dbname"));
	  System.out.println("dbname : " + dbname1);
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		String result = m_Utility.minsertstaffItem(iSocietyID,iUnitId,l_id,l_name,checkoutnote,id,fetchstaffid,dbname1);
		 if(!result.equals(""))
		 {
		String[] split = result.split("/");
		long itemid=Long.valueOf(split[0]);
		String dbname = split[1].toString();
	
 		System.out.println(result);
 		if(itemid > 0)
 		{
 			
 			 rows.put("success",1);
 			 rows.put("staffid",itemid);
 			 rows.put("dbname",dbname);
 		}
 		else
 		{
 			 rows.put("success",0);
 			 rows.put("staffid","");
 			 rows.put("dbname","");
 		}
		 }
		 else
		 {
			 rows.put("success",0);
 			 rows.put("staffid","");
 			 rows.put("dbname",""); 
		 }
 	    return rows;
 	}	

  public static long mfetchstaffattend(int iSocietyID,String id) throws ClassNotFoundException
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_Utility.mfetchstaffattend(iSocietyID,id);

	System.out.println("Staff Entry Id :"+ lResult);
	
	return lResult;
	}

  public static  HashMap<Integer, Map<String, Object>> minsertstaffmarkentry(int iSocietyID,String id,String jobprofile) throws ClassNotFoundException
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_Utility.markentry(iSocietyID,id,jobprofile);

	System.out.println("Staff Entry Id :"+ lResult);
	if(lResult > 0)
		{
			
			 rows.put("success",1);
			 rows.put("staffid",lResult);
			
		}
		else
		{
			 rows.put("success",0);
			 rows.put("staffid","");
		}
	return rows;
	}
  
  public static  HashMap<Integer, Map<String, Object>> VehicleRenew(int iMemberParkingID, int iSocietyID, int VehicleType) throws ClassNotFoundException
	{
	HashMap rows = new HashMap<>();
	
	long lResult = m_Utility.VehicleRenewal(iMemberParkingID,iSocietyID,VehicleType);

	System.out.println("Renewal :"+ lResult);
	if(lResult > 0)
		{
			
			 rows.put("success",1);
			 rows.put("reneval",lResult);
			
		}
		else
		{
			 rows.put("success",0);
			 rows.put("reneval","");
		}
	return rows;
	}

	public static void main(String[] args) throws Exception
	{
		String sToken ="wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQLVutDlCbOYwLVr1HW818h2SX6hIALpCnnU2gSDzz9IHDJ8OU4Ed2HIvYdmgjocOvVV5k3zdqCpgwIP8jCapXp__6ZxGzFKdD5awKNtQQTjRQ";
		//sToken ="wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQI6Yg08ysk-HQELgJW36dHO_HTmUfWF_jp3PfcNder1x1UQ-5M-KAQuKT2A6DsZyhbXbokv6lkoTBHloHnGuIOANbQAKLrxKnNL7dnfNymNLw";
		String sTkey = "cOPu1Tv6gdHuGdvff5YaRMUI6R-0brnoKAHZ0DbvBqPfxbVHUVgbFr4QY4ZfCPnYpD_y434G-IhANbMmfP1h34pAUN2D56g6JnJxZA8pgyGeihzqt0RH8eFpM58LHQi5DQwy3TWjeaVzxBWXj8Sy1mK01Bau6GZ0xYpaEMxwgsU";
		
		//sTkey = "nqP7yCbTzmntnfeAa7tzVjUYqDqaTgnAA6aN5AUQBqx_0frfaUBOTdsf63nFb3se0BUWPOahJySq45usYomP3-7z1KHt19eSFCKSJtJT7qe60K5dnKMtCQ0ZhJGpNKv0-dN7D_EdUzf3KCgva7Xi8A";//501 member
	
		
		ViewDirectory obj = new ViewDirectory(sToken, true, sTkey);		
	//	mSendActivationCode(int iUnit_id,int iOther_Member_id)
		HashMap rows =  obj.mSendActivationCode(20, 285);
		//HashMap rows =  obj.mFetchPurpose(59, 28);
		//System.out.println(obj.convert(rows));
	System.out.println(rows);	
	}

}


