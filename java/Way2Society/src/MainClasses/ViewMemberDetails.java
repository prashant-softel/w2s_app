package MainClasses;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CommonUtilityFunctions.MapUtility;

public class ViewMemberDetails extends CommonBaseClass 
{
	static Member m_Member;
	public  ViewMemberDetails(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
		m_Member = new Member(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	

	
	public static  HashMap<Integer, Map<String, Object>> mfetchMemberDetailsFromUnitID(int iUnitID) throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpMember = m_Member.mfetchMemberDetailsFromUnitID(iUnitID);
		
		
		 
		if(mpMember.size() > 0)
		{
			//add member to map
			 rows2.put("memberdetails",mpMember);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("memberdetails","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mAddMemberDetails(int iUnitID, int iMemberID, String sName, int iCoOwner, String sRelation, int iPublishContact , String sMobile, String sEmail, int iBloodGroup, String sDOB, int iPublishProfile, String sProfProfile, int iDesignation, String sSSC, String sWeddingDate,String sGender,String sAdhaar) throws SQLException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		int iRetValue = m_Member.AddMemberDetails(iMemberID, sName, iCoOwner, sRelation, iPublishContact , sMobile, sEmail, iBloodGroup, sDOB, iPublishProfile, sProfProfile, iDesignation, sSSC, sWeddingDate,sGender,sAdhaar);
		
		 
		if(iRetValue == 1)
		{
			// added successfully
			 rows.put("success",1);
			 rows2.put("message","Member added successfully");
			 rows.put("response",rows2);
		}
		else
		{
			// not found
			rows.put("success",0);
			rows2.put("message","Unable to add Member. Please try again.");
			 rows.put("response",rows2);
		}
	  
	    return rows;
	}

	
	//, String sOccupation, String sCompanyName
	public static  HashMap<Integer, Map<String, Object>> mUpdateMemberDetails(int iUnitID, int iMemberID, int iMemberOtherTableID, String sName, int iCoOwner, String sRelation, int iPublishContact , String sMobile, String sEmail, int iBloodGroup, String sDOB, int iPublishProfile, String sProfProfile, int iDesignation, String sSSC, String sWeddingDate,String sGender,String sAdhaar) throws SQLException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		int iRetValue = m_Member.UpdateMemberDetails(iMemberID, iMemberOtherTableID, sName, iCoOwner, sRelation, iPublishContact , sMobile, sEmail, iBloodGroup, sDOB, iPublishProfile, sProfProfile, iDesignation, sSSC, sWeddingDate,sGender,sAdhaar);
		 System.out.println("UpdateMember:"+iRetValue);
		if(iRetValue == 1)
		{
			// added successfully
			 rows.put("success",1);
			 rows2.put("message","Member updated successfully");
			 rows.put("response",rows2);
		}
		else
		{
			// not found
			rows.put("success",0);
			rows2.put("message","Unable to update Member. Please try again.");
			 rows.put("response",rows2);
		}
	  
	    return rows;	    
	}
	

	public static  HashMap<Integer, Map<String, Object>> mAddVehicleDetails(int iUnitID, int iMemberID, int iVehicleType, String sParkingSlot, String sParkingSticker, String sRegNo, String sOwner, String sVehicleMake, String sVehicleModel, String sVehicleColor) throws SQLException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//inserting Vehicle details
		int iRetValue = m_Member.AddVehicleDetails(iMemberID, iVehicleType, sParkingSlot, sParkingSticker, sRegNo, sOwner, sVehicleMake, sVehicleModel, sVehicleColor);
		  
		if(iRetValue == 1)
		{
			//Vehicle added successfully
			 rows.put("success",1);
			 rows2.put("message","Vehicle added successfully");
			 rows.put("response",rows2);
		}
		else
		{
			// not found
			rows.put("success",0);
			rows2.put("message","Unable to add Vehicle. Please try again.");
			 rows.put("response",rows2);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mUpdateVehicleDetails(int iUnitID, int iMemberID, int iVehicleTableID, int iVehicleType, String sParkingSlot, String sParkingSticker, String sRegNo, String sOwner, String sVehicleMake, String sVehicleModel, String sVehicleColor) throws SQLException
		{
			
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			
			//inserting Vehicle details
			int iRetValue = m_Member.UpdateVehicleDetails(iMemberID, iVehicleTableID, iVehicleType, sParkingSlot, sParkingSticker, sRegNo, sOwner, sVehicleMake, sVehicleModel, sVehicleColor);
			  
			if(iRetValue == 1)
			{
				//Vehicle added successfully
				 rows.put("success",1);
				 rows2.put("message","Vehicle updated successfully");
				 rows.put("response",rows2);
			}
			else
			{
				// not found
				rows.put("success",0);
				rows2.put("message","Unable to update Vehicle. Please try again.");
				 rows.put("response",rows2);
			}
		  
		    return rows;
		}
		
	public static  HashMap<Integer, Map<String, Object>> mDeleteVehicleDetails( int iMemberID, int iVehicleTableID, int iVehicleType) throws SQLException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//inserting Vehicle details
		int iRetValue = m_Member.DeleteVehicleDetails(iMemberID, iVehicleTableID, iVehicleType);
		  
		if(iRetValue == 1)
		{
			//Vehicle added successfully
			 rows.put("success",1);
			 rows2.put("message","Vehicle deleted successfully");
			 rows.put("response",rows2);
		}
		else
		{
			// not found
			rows.put("success",0);
			rows2.put("message","Unable to delete Vehicle. Please try again.");
			rows.put("response",rows2);
		}
	  
	    return rows;
	}

	public static  HashMap<Integer, Map<String, Object>> mfetchDesignations()
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all designations
		HashMap<Integer, Map<String, Object>> mpDesignations = m_Member.GetDesignations();		
		 
		if(mpDesignations.size() > 0)
		{
			//add member to map
			 rows2.put("designations",MapUtility.HashMaptoList(mpDesignations));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("designation","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mUpdateMemberContactNo(int iUnitId,int iOtherFemilyID) throws SQLException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all designations
		int mpMemberUpdateContact = m_Member.UpdateContacts(iUnitId,iOtherFemilyID);
		//HashMap<Integer, Map<String, Object>> mpMemberUpdateContact = m_Member.UpdateContacts(iUnitId,iOtherFemilyID);		
		 
		if(mpMemberUpdateContact == 1)
		{
			//add member to map
			 rows.put("success",1);
			 rows2.put("message","update successfully");
			 rows.put("response",rows2);
		}
		else
		{
			//member not found
			 rows.put("success",0);
			 rows2.put("message","unable to update successfully");
			 rows.put("response",rows2);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mUpdateOwnerAdd(int iMemberID,String OwnerAdd) throws SQLException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		
		int mpMemberUpdateContact = m_Member.UpdateAddress(iMemberID,OwnerAdd);	
		 
		if(mpMemberUpdateContact == 1)
		{
			//add member to map
			 rows.put("success",1);
			 rows2.put("message","update successfully");
			 rows.put("response",rows2);
		}
		else
		{
			//member not found
			 rows.put("success",0);
			 rows2.put("message","unable to update successfully");
			 rows.put("response",rows2);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mfetchMemberContactNo(int iUnitId,int iLogin)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all designations
		HashMap<Integer, Map<String, Object>> mpMemberContact = m_Member.GetContacts(iUnitId,iLogin);		
		 
		if(mpMemberContact.size() > 0)
		{
			//add member to map
			 rows2.put("MemberContact",MapUtility.HashMaptoList(mpMemberContact));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("MemberContact","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mUpdateMemberNotify(int iMemOtherID,int notify) throws SQLException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all designations
		int mpMemberUpdateContact = m_Member.UpdateMemberNofify(iMemOtherID,notify);
		//HashMap<Integer, Map<String, Object>> mpMemberUpdateContact = m_Member.UpdateContacts(iUnitId,iOtherFemilyID);		
		 
		if(mpMemberUpdateContact == 1)
		{
			//add member to map
			 rows.put("success",1);
			 rows2.put("message","update successfully");
			 rows.put("response",rows2);
		}
		else
		{
			//member not found
			 rows.put("success",0);
			 rows2.put("message","unable to update successfully");
			 rows.put("response",rows2);
		}
	  
	    return rows;
	}
	/* Add new function roe renting registration  -----  */
	public static  HashMap<Integer, Map<String, Object>> mfetchMemberDetailsForRentingRegistration(int iUnitID)
	{
		int iSocietyid = getSocietyId();
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpMemberDetails = m_Member.mfetchMemberDetailsForRegistration(iSocietyid,iUnitID);
		
		
		 
		if(mpMemberDetails.size() > 0)
		{
			//add member to map
			 rows2.put("memberdetails",mpMemberDetails);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("memberdetails","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mfetchTenantDetailsFromUnitID(int iUnitID) throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		HashMap<Integer, Map<String, Object>> mpMember = m_Member.mfetchTenantDetailsFromUnitID(iUnitID);
		
		
		 
		if(mpMember.size() > 0)
		{
			//add member to map
			 rows2.put("memberdetails",mpMember);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("memberdetails","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mfetchRenewDetails() throws ParseException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//row2.put("Society_id", "288");
		Map<String, String> map = new HashMap<String, String>();
		map.put("society_id", "288");
		map.put("endDate", "25-01-2020");
		map.put("ButtonName", "Apply");
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("society_id", "59");
		map1.put("endDate", "25-01-2020");
		map1.put("ButtonName", "Register");
		List<Map<String, String>> data = new ArrayList<>();
		data.add(0, map);
		data.add(1, map1);
		//fetching all bills
		//HashMap<Integer, Map<String, Object>> mpMember = m_Member.mfetchTenantDetailsFromUnitID(iUnitID);
		
		
		 
		if(data.size() > 0)
		{
			//add member to map
			 rows2.put("RenewRequest",data);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			//member not found
			 rows2.put("memberdetails","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	  //System.out.println(rows);
	    return rows;
	}
	//insert message 
		public static HashMap <Integer , Map <String, Object>> mUpdateDND(int iSocietyID,int unit_id, int unit_no, int dnd_type, String dnd_msg){
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			
			long mpInsertMessage = m_Member.UpdateDNDdetails(iSocietyID, unit_id, unit_no, dnd_type, dnd_msg);
		    if(mpInsertMessage ==1){
		    	rows.put("success", 1);
		    	rows2.put("message", "inserted or Updated successfully");
		    	rows.put("response", rows2);
		    }
		    else
		    {
		    	rows.put("success",0);
				 rows2.put("message","unable to insert or Update");
				 rows.put("response",rows2);
		    }
		    
		    return rows;
		
		}
		public static  HashMap<Integer, Map<String, Object>> mfetchDND(int iSocietyID,int unit_id) throws ClassNotFoundException
		{
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			
			HashMap<Integer, Map<String, Object>> mMessage = m_Member.getDNDdetails(iSocietyID, unit_id);		
			 
			if(mMessage.size() > 0)
			{
				
				 rows2.put("DND_message",MapUtility.HashMaptoList(mMessage));
				 rows.put("response",rows2);
				 rows.put("success",1);
			}
			else
			{
				
				 rows2.put("DND_message","");
				 rows.put("response",rows2);
				 rows.put("success",0);
			}
		  
		    return rows;

		}

	
	public static void main(String[] args) throws Exception
	{
		Boolean m_bIsTokenValid = true;
		String sTkey = "testing";
		ViewMemberDetails obj = new ViewMemberDetails("wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQKI9_C3V40SOX52-2mjVxBfZ9VCRq-dQH_5nlaboIjfXuPC5SmwGJgqJtPUumpDfR-QhLvi5fnZ442RI9wSFW4V", true , "I73uPx0mb3QATqU_iNdqCJ2H8GEy5L3GWUA-RrZ00tteJFGEm0H3EjuLu5hp286AnwRD4AprWG8bE8VbaZS5i5j52PdT7XEtIPldx3UHhb0bHvAt5luXaOvqulPX-9xY9XB-gD8ivgq0U21Y1rEVdf0UdeA1iLMsXQApxPwHFk0");
		
		//HashMap rows =  obj.mfetchRenewDetails();
		int iSocietyID = 59;
		int unit_id = 10;
		int unit_no = 11;
		int dnd_type = 3;
		String dnd_msg = "Testing  DND ";
//		HashMap rows = obj.mUpdateDND(iSocietyID, unit_id, unit_no, dnd_type, dnd_msg);
//	HashMap rows = obj.mfetchDND(iSocietyID, unit_id);

		
		//HashMap rows =  obj.mUpdateVehicleDetails(1, 16, 2, 1, "SlotA-13", "StickerB14", "MH-01-AN-6772", "Urmila Shetye", "Mahindra", "XUV", "sBikeColorPurple");
		//HashMap rows =  obj.mDeleteVehicleDetails(16, 2, 1);
		
		//HashMap rows =  obj.mAddMemberDetails(0, 16, "Urmila Shetye", 2, "Mother", 1,  "9933004455", "", 4, "2017-04-30");//, int iPublishProfile, String sProfProfile, String sDesignation, String sSSC, String sWeddingDate);
		//HashMap rows =  obj.mUpdateMemberDetails(0, 6, 15,"Urmila Babaria", 2, "Mother", 1,  "9933004455", "sunil@kashikar", 4, "2017-04-30", 1, "Sunil the Hero", 1, "", "2007-05-05");//, int iPublishProfile, String sProfProfile, String sDesignation, String sSSC, String sWeddingDate);
		
		//HashMap rows =  obj.mUpdateMemberDetails(14, 2, 154, "Demo Account 4", 0, "daughter", 0, "7575757575", "demo4@gmail.com", 9, "0000-00-00", 0, "", 2, "", "0000-00-00");
		//HashMap rows =  obj.AddVehicleDetails(16,4,"SujitKumar", 154, "Demo Account 4", 0, "daughter", 0, "7575757575", "demo4@gmail.com", 9, "0000-00-00", 0, "", 2, "", "0000-00-00");

		//System.out.println(obj.convert(rows));
		//System.out.println(rows);	
	}

}


