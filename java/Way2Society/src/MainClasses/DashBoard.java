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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import CommonUtilityFunctions.MapUtility;
import MainClasses.Bill;
import MainClasses.Receipt;
import MainClasses.Notice;
import MainClasses.Payment;
import MainClasses.Event;
import MainClasses.CommonBaseClass.*;
import ecryptDecryptAlgos.ecryptDecrypt;

public class DashBoard extends CommonBaseClass
{
	private static Bill m_Bill;
	private static Receipt m_Receipt;
	private static Notice m_Notice;
	private static Payment m_Payment;
	private static Event m_Event;
	private static Task m_Task;
	private static Classifieds m_Classifieds;
	private static Polls m_Polls;
	private static ServiceRequest m_ServiceRequest;
	private static Utility m_Utility;
	static SOSAlert m_SOSAlert;
	public  DashBoard(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
		m_SOSAlert = new SOSAlert(CommonBaseClass.m_objDbOperations);
		m_Bill = new Bill(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Receipt = new Receipt(CommonBaseClass.m_objDbOperations);	
		m_Notice = new Notice(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Task = new Task(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Classifieds = new Classifieds(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Polls = new Polls(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_ServiceRequest = new ServiceRequest(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
		m_Receipt = new Receipt(CommonBaseClass.m_objDbOperations);	
		m_Payment = new Payment(CommonBaseClass.m_objDbOperations);	
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchDashBoardDetails_3() throws ParseException, ClassNotFoundException
	{
		int iSocietyId = getSocietyId();
		Integer iUnitId = getUnitId();
		int iLoginID = getLoginId();
		String role=getRole();
		if(role.equals("Contractor"))
		{
			return mfetchDashBoardDetail_contractor(iSocietyId,iLoginID);
		}
		else
		{
		return mfetchDashBoardDetails_2( iUnitId, iSocietyId, iLoginID);
		}
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchDashBoardDetail_contractor(int iSocietyId, int iLoginID) throws ParseException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpContractor = m_ServiceRequest.fetchContractor(iSocietyId,iLoginID); 
		HashMap<Integer, Map<String, Object>> mpFetaureSetting = m_SOSAlert.FetaureSetting(iSocietyId); 
		
		System.out.println("mpContractorDetails :"+ mpContractor);
		if(mpContractor.size() > 0 ){	
			//bHasRecords = true;
			//add mpBlockedUnit to map
			rows2.put("Srequest",MapUtility.HashMaptoList(mpContractor));
			rows2.put("FetureSetting",MapUtility.HashMaptoList(mpFetaureSetting));
			rows.put("response",rows2);
			rows.put("success",1);
		}
		else{
			//no bills found
			rows2.put("Srequest","");
			rows.put("response",rows2);
			rows.put("success",0);
		}
		return rows;
	}

	

	public static  HashMap<Integer, Map<String, Object>> mfetchVisitorDetails(int iVisitorId, int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//HashMap<Integer, Map<String, Object>> mpVisitorDetails = m_Utility.mfetchVisitorDetails(iVisitorId, iSocietyId);
		HashMap<Integer, Map<String, Object>> mpVisitorDetails = m_Utility.mfetchAllVisitorsDetails(iVisitorId, iSocietyId);
		
		System.out.println("mpVisitorDetails :"+ mpVisitorDetails);
		if(mpVisitorDetails.size() > 0 ){	
			//bHasRecords = true;
			//add mpBlockedUnit to map
			rows2.put("VisitorDetails",MapUtility.HashMaptoList(mpVisitorDetails));
		}
		else{
			//no bills found
			rows2.put("VisitorDetails","");
		}
		return mpVisitorDetails;

	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchDashBoardDetails(int iUnitId,int iSocietyId) throws ParseException, ClassNotFoundException
	{
		return mfetchDashBoardDetails_2( iUnitId, iSocietyId, 0);
	}	
	public static  HashMap<Integer, Map<String, Object>> mfetchDashBoardDetails_2(int iUnitId,int iSocietyId, int iLoginID) throws ParseException, ClassNotFoundException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();

		boolean bHasRecords = false;
		int iFetchCount = 5;
		boolean bDebugRun = true;
		System.out.println("Dashboard iUnitId :"+ iUnitId);
		
		
		if(iUnitId>0)
		{
			//fetch latest bill 
			m_Bill = new Bill(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
			HashMap<Integer, Map<String, Object>> mpBills = m_Bill.mfetchBills(iUnitId,0,true);
			System.out.println("mpBills :"+ mpBills);
			if(mpBills.size() > 0 ){	
				bHasRecords = true;
				//add bill to map
				rows2.put("bill",MapUtility.HashMaptoList(mpBills));
			}
			else{
				//no bills found
				rows2.put("bill","");
			}
			
			
			//fetch latest payments received
			HashMap<Integer, Map<String, Object>> mpReceipts = m_Receipt.mfetchReceipts(iUnitId, 0, true);
			//System.out.println("mpReceipts :"+ mpReceipts);
			if(mpReceipts.size() > 0)
			{	
				bHasRecords = true;
				//add receipts to map
				rows2.put("receipt",MapUtility.HashMaptoList(mpReceipts));
			}
			else
			{
				//no receipts found
				rows2.put("receipt","");
			}
			
			
			//fetch final due 
			HashMap<Integer, Map<String, Object>> mpDues = m_Receipt.getDueAmount(iUnitId);
			//System.out.println("mpDues :"+ mpDues);
			if(mpDues.size() > 0)
			{
				bHasRecords = true;
				//add dues to map
				rows2.put("dues",MapUtility.HashMaptoList(mpDues));
			}
			else
			{
				//no dues found
				rows2.put("dues","");
			}
			
			
			//fetch block unit 
			HashMap<Integer, Map<String, Object>> mpBlockedUnit = m_Utility.getBlockUnitDetails(iUnitId);		
			//System.out.println("mpBlockedUnit :"+ mpBlockedUnit);

			if(mpBlockedUnit.size() > 0 ){	
				bHasRecords = true;
				//add mpBlockedUnit to map
				rows2.put("BlockedUnit",MapUtility.HashMaptoList(mpBlockedUnit));
			}
			else{
				//no bills found
				rows2.put("BlockedUnit","");
			}


			//fetch latest serviceRequest
			boolean bFetchClosed = false;
			boolean bAssignedToMe = true;
			
			HashMap<Integer, Map<String, Object>> mpServiceRequest = m_ServiceRequest.mfetchServiceRequests(iUnitId, iFetchCount, bFetchClosed, bAssignedToMe, iLoginID, iSocietyId);
			//System.out.println("mpServiceRequest: " + mpServiceRequest);
			if(mpServiceRequest.size() > 0)
			{
				bHasRecords = true;
				//add dues to map
				rows2.put("Srequest",MapUtility.HashMaptoList(mpServiceRequest));
			}
			else
			{
				rows2.put("Srequest","");
				
			}
			HashMap<Integer, Map<String, Object>> mpServices = m_Utility.getNewServices();		
			//System.out.println("mpAgreementText :"+ mpAgreementText);

			if(mpServices.size() > 0 ){	
				bHasRecords = true;
				//add mpBlockedUnit to map
				rows2.put("MobileServices",MapUtility.HashMaptoList(mpServices));
			}
			else{
				//no bills found
				rows2.put("MobileServices","");
			}
			HashMap<Integer, Map<String, Object>> mpSubscription = m_Utility.getSubscription(iSocietyId);		
			//System.out.println("mpAgreementText :"+ mpAgreementText);

			if(mpSubscription.size() > 0 ){	
				bHasRecords = true;
				//add mpBlockedUnit to map
				rows2.put("SocietySubscription",MapUtility.HashMaptoList(mpSubscription));
			}
			else{
				//no bills found
				rows2.put("SocietySubscription","");
			}
			//if(rows2.size() > 0 && (mpBills.size() > 0 || mpDues.size() > 0 || mpNotices.size() > 0 || mpReceipts.size() > 0 || mpEvents.size() > 0 || mpTask.size() > 0 || mpServiceRequest.size() > 0))
		}
		else {
			
			//ADMIN(manager - iUnitId = 0)

			//fetch latest serviceRequest
			boolean bFetchClosed = false;
			boolean bAssignedToMe = true;
			int iCount = 8;
			HashMap<Integer, Map<String, Object>> mpServiceRequest = m_ServiceRequest.mFetchAllServiceRequest(iSocietyId, iCount, bFetchClosed, 0);
			//System.out.println("mpServiceRequest: " + mpServiceRequest);
			if(mpServiceRequest.size() > 0)
			{
				bHasRecords = true;
				//add dues to map
				rows2.put("Srequest",MapUtility.HashMaptoList(mpServiceRequest));
			}
			else
			{
				//no dues found
				rows2.put("Srequest","");
				//|| mpClassified.size() > 0 || mpPolls.size() > 0 || 
			}			
		}

		//fetch latest notices
		HashMap<Integer, Map<String, Object>> mpNotices;	
		mpNotices = m_Notice.mfetchNotices(iUnitId, iSocietyId, iFetchCount, false, 0);
		//System.out.println("Notices :"+mpNotices);
		if(mpNotices.size() > 0)
		{
			bHasRecords = true;
			//add notices to map
			rows2.put("notice",MapUtility.HashMaptoList(mpNotices));
		}
		else
		{
			//no notice found
			rows2.put("notice","");
		}		
		
		//fetch latest events
		HashMap<Integer, Map<String, Object>> mpEvents;
		m_Event = new Event(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);	
		mpEvents = m_Event.mfetchEvents(iUnitId, iSocietyId, iFetchCount, false, 0);
		//System.out.println("mpEvents: " + mpEvents);
		if(mpEvents.size() > 0)
		{
			bHasRecords = true;
			//add events to map
			rows2.put("events",MapUtility.HashMaptoList(mpEvents));
		}
		else
		{
			//no events found
			rows2.put("events","");
		}

		//fetch profile flags
		HashMap<Integer, Map<String, Object>> mpProfileFlags = m_Utility.getProfileDetails(iLoginID, iUnitId);		
		//System.out.println("mpProfileFlags: " + mpProfileFlags);
		
		if(mpProfileFlags.size() > 0 )
		{	
			bHasRecords = true;
			//add mpProfileFlags to map
			rows2.put("ProfileFlags",MapUtility.HashMaptoList(mpProfileFlags));
		}
		else{
			//no bills found
			rows2.put("ProfileFlags","");
		}

		//fetch Advertisement
		String sRole =  getRole();
		boolean bTitleOnly = true;
		HashMap<Integer, Map<String, Object>> mpAdvertisements = m_Utility.getAdvertisements(sRole, bTitleOnly, iFetchCount);		

		
		if(mpAdvertisements.size() > 0 ){	
			bHasRecords = true;
			//add mpProfileFlags to map
			rows2.put("Advertisements",MapUtility.HashMaptoList(mpAdvertisements));
		}
		else{
			//no bills found
			rows2.put("Advertisements","");
		}
		
	//if(bDebugRun == false)
	//{
		//fetch latest classified
		HashMap<Integer, Map<String, Object>> mpClassified = m_Classifieds.mFetchAllClassified(iSocietyId, iFetchCount, false, 0);
		//System.out.println("mpClassified: " + mpClassified);
				
		if(mpClassified.size() > 0)
		{
			bHasRecords = true;
			//add dues to map
			rows2.put("classified",MapUtility.HashMaptoList(mpClassified));
		}
		else
		{
			//no dues found
			rows2.put("classified","");
		}

		//fetch latest polls
		//HashMap<Integer, Map<String, Object>> mpPolls = m_Polls.mFetchPolls(iSocietyId, iFetchCount, false, 0);		
		HashMap<Integer, Map<String, Object>> mpPolls = m_Polls.mfetchActivePolls(0, iSocietyId);		
		
		//System.out.println("mpPolls: " + mpPolls);
		if(mpPolls.size() > 0)
		{
			bHasRecords = true;
			//add dues to map
			rows2.put("poll",MapUtility.HashMaptoList(mpPolls));
		}
		else
		{
			//no dues found
			rows2.put("poll","");
		}

		
		
		//fetch latest task 
		boolean bFetchOpenOnly = true;
		HashMap<Integer, Map<String, Object>> mpTaskForMe = m_Task.mfetchTasksForMe(iLoginID, iFetchCount, bFetchOpenOnly); 
		//System.out.println("TaskForMe: " + mpTaskForMe);
		if(mpTaskForMe.size() > 0)
		{
			bHasRecords = true;
			//add dues to map
			rows2.put("taskForMe",MapUtility.HashMaptoList(mpTaskForMe));
		}
		else
		{
			//no dues found
			rows2.put("taskForMe","");
		}
		
		//fetch latest task 
		HashMap<Integer, Map<String, Object>> mpTaskByMe = m_Task.mfetchTasksByMe(iLoginID, iFetchCount, bFetchOpenOnly); 
		//System.out.println("mpTaskByMe: " + mpTaskByMe);
		if(mpTaskByMe.size() > 0)
		{
			bHasRecords = true;
			//add dues to map
			rows2.put("taskByMe",MapUtility.HashMaptoList(mpTaskByMe));
		}
		else
		{
			//no dues found
			rows2.put("taskByMe","");
		}
		HashMap<Integer, Map<String, Object>> mpFetaureSetting = m_SOSAlert.FetaureSetting(iSocietyId); 
		rows2.put("FetureSetting",MapUtility.HashMaptoList(mpFetaureSetting));		
		if(rows2.size() > 0 && bHasRecords == true)
		{	
			rows.put("response",rows2);
			rows.put("success",1);
		}
		else
		{
			rows.put("response",rows2);
			rows.put("success",0);
		}
		HashMap<Integer, Map<String, Object>> mpSubscription = m_Utility.getSubscription(iSocietyId);		
		//System.out.println("mpAgreementText :"+ mpAgreementText);

		if(mpSubscription.size() > 0 ){	
			bHasRecords = true;
			//add mpBlockedUnit to map
			rows2.put("SocietySubscription",MapUtility.HashMaptoList(mpSubscription));
		}
		else{
			//no bills found
			rows2.put("SocietySubscription","");
		}
		
		return rows;
	}
		
	public static void main(String[] args) throws Exception
	{
		//acmeamayabc11- admin
		String sToken = "OFCea7Iz2XW2_z-UXCQetaK3ZDfY_NqDOi5TFlZ5U4yrtK1K69WC3S3QNH_rdgffSSQaE3s92MGM-kvsjYyu2thA4hlkZ1dzauXNQ-WpyV9lU4WqQwtj5WeuFSljuPg1" ;
		String sTkey = "ITCl83FX69-PVmWf9jzIjSEm9OqIAs62V1BEKJMdqaIqu9iVP1Ms_Qv88y25IN3--pUVzUXEb9DQF6DRU_DRCGqtENxEsr3vdlbiUXJ03f2a4xBNNkjyGgf-kf6p1XGWwcIyZou54nKj4tbcOjMC1w" ;
		DashBoard obj = new DashBoard(sToken,true,sTkey);
		//HashMap rows =  obj.mfetchDashBoardDetails_2(28, 59, 169);
		
		//HashMap rows =  obj.mfetchViisitor(1, 59);

		//System.out.println(rows);	
	}
	
}
