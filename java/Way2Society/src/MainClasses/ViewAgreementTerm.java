package MainClasses;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import CommonUtilityFunctions.MapUtility;

public class ViewAgreementTerm
{
	static AgreementTerm m_AgreementTerm;
	public ViewAgreementTerm(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		CommonBaseClass b1 = new CommonBaseClass(sToken,bIsVerifyDbDetails,sTkey);
		m_AgreementTerm = new AgreementTerm(b1.m_objDbOperations);
	}
	public ViewAgreementTerm() throws ClassNotFoundException 
	{
		//super(sToken,bIsVerifyDbDetails,sTkey);
		m_AgreementTerm = new AgreementTerm(CommonBaseClass.m_objDbOperations);
	}
	//public static  HashMap<Integer, Map<String, Object>> msendDetailsToDR(int itenantModuleId,String propertyType,String propertyUse,String pAddress1,String pAddress2,String pPincode,String pcity,String pregion,String propertyArea,String rentType,String deposit,int isocietyId, String monthlyRent)
	public static  HashMap<Integer, Map<String, Object>> msendDetailsToDR(int itenantModuleId, int iUnitID,int isocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//long updateResult = AgreementTerm.sendDetailsToDr(itenantModuleId,propertyType,propertyUse,pAddress1,pAddress2,pPincode,pcity,pregion,propertyArea,rentType,deposit,isocietyId,monthlyRent);
		long updateResult = AgreementTerm.sendMailToDr(itenantModuleId,iUnitID,isocietyId);
		
		if(updateResult > 0)
		{	
			rows.put("success",1);
			rows2.put("Tenant","Updated Successfully");
			rows.put("response",updateResult);
		}
		else
		{
			rows2.put("Tenant","");
			rows.put("response","Error");
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static HashMap<Integer, Map<String, Object>> fetchAgreementTermDetails(int itenantModuleId,int SocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> OwnersDetails = m_AgreementTerm.fetchAgreementTermDetails(itenantModuleId, SocietyId);
		
		if(OwnersDetails.size() > 0)
		{	
			rows.put("success",1);
			rows2.put("OwnerDetails", OwnersDetails);
			rows.put("response", rows2);
		}
		else
		{
			rows2.put("OwnerDetails","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static HashMap<Integer, Map<String, Object>> mFetchPropertyAddressDetails(int iUnitId,int SocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> PropertyDetails = m_AgreementTerm.fetchPropertyAddress(iUnitId, SocietyId);
		
		if(PropertyDetails.size() > 0)
		{	
			rows.put("success",1);
			rows2.put("PropertyDetails", PropertyDetails);
			rows.put("response", rows2);
		}
		else
		{
			rows2.put("PropertyDetails","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mInsertAgreementTermDetails(int iPropertyType,int iPropertyUse,String sPAddress,String sPPincode,String sPCity,String sPRegion,String sDeposit,int iRentType,int iUnitId, int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		long lAgreementId = m_AgreementTerm.addAgreementDetails(iPropertyType,iPropertyUse,sPAddress,sPPincode,sPCity,sPRegion,sDeposit,iRentType,iUnitId, iSocietyId);
		
		if(lAgreementId > 0)
		{	
			rows.put("success",1);
			rows2.put("lRentalAgreementId",lAgreementId);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("lRentalAgreementId","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mInsertRentDetails(int iRentalAgreementId,int iRentType,String sStartDate,String sEndDate,String sMonthlyRent,int iFrom[],int iTo[],String sRent[],int iSocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		long lRentDetailsId = m_AgreementTerm.addRentDetails(iRentalAgreementId,iRentType,sStartDate,sEndDate,sMonthlyRent,iFrom,iTo,sRent,iSocietyId);
		
		if(lRentDetailsId > 0)
		{	
			rows.put("success",1);
			rows2.put("RentDetailsId",lRentDetailsId);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("lRentDetailsId","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	public static void main(String[] args) throws Exception
	{
		String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4yShq3gS2XOamTB-D4umDzi2TYQfnG5nBEn_d3K5HEbfKlAGWg8f-FjOl6u9vV4NJvFoxg2ZtUMth9Bdt6aVKx";
		String sTkey = "nXTYH6gXb5NrNS7bD-xf-7p-DLueHDMHV63rrzV8xdR5QxtyKNUQph89PNpuyjn5qq1PT2kOpx3iSYamT24xEmrftFMt-IfABEKDKRDSmEhS0-n02MO74v-qHylcskOL79mjDGY4VS4R1dqZybT3Mg";
		//ViewOwner obj = new ViewOwner(sToken, true, sTkey);
		ViewAgreementTerm obj = new ViewAgreementTerm();
		//HashMap rows =  obj.msendDetailsToDR(18,"Flat","Residential", "address1", "address2", "400016", "Mumbai", "Mahim", "Rural", "Fixed Rent", "1,00,000", "0", "0", "0", "0", "0", "0", 59, "12000");
		Gson objGson = new Gson();
	//	String objStr = objGson.toJson(rows);
		//System.out.println(objStr);
	}

}
