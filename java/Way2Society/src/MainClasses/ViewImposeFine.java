package MainClasses;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static MainClasses.ProjectConstants.ENCRYPT_INIT_VECTOR;
import static MainClasses.ProjectConstants.ENCRYPT_SPKEY;
import java.lang.*;

import CommonUtilityFunctions.MapUtility;


public class ViewImposeFine extends CommonBaseClass
{
	static Fine  m_ImposeFine;
	
	public ViewImposeFine(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_ImposeFine = new Fine(CommonBaseClass.m_objDbOperations);
		//System.out.println(m_Classifieds);
	}

	public static  HashMap<Integer, Map<String, Object>> mfetchPeriod()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpImposeFine = m_ImposeFine.mfetchBillPeriod();
		System.out.println(mpImposeFine);
		if(mpImposeFine.size() > 0)
		{
			 rows2.put("Period",mpImposeFine);
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("Period","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mFineLedger()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpImposeFineLedger = m_ImposeFine.mfetchFineLedger();
		 System.out.println(mpImposeFineLedger);
		if(mpImposeFineLedger.size() > 0)
		{
			 rows2.put("Fine",MapUtility.HashMaptoList(mpImposeFineLedger));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("Fine","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}
	
	public static  HashMap<Integer, Map<String, Object>> mUnitDetails(int iSocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpUnitList = m_ImposeFine.mfetchUnitDetais(iSocietyID);
		 
		if(mpUnitList.size() > 0)
		{
			 rows2.put("Units",MapUtility.HashMaptoList(mpUnitList));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("Units","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}
	public static HashMap<Integer, Map<String, Object>> mAddImposeFine(int iSocietyID,long iMappingID,String sReportedby,String sEmail,int iLedgerID,int iPetiodId,int iUnitID,int iAmount,String desc,String img,String iPeriodDate,int isMail)
	{
	HashMap rows = new HashMap<>();
	HashMap rows2 = new HashMap<>();
	long mpAddFine = m_ImposeFine.mSetAddImposeFine(iSocietyID,iMappingID,sReportedby,sEmail,iLedgerID,iPetiodId,iUnitID,iAmount,desc,img,iPeriodDate,isMail);
	//System.out.println(mpAddFine);
	
	if(mpAddFine > 0)
		{
		rows2.put("new_fine_id",mpAddFine);
		rows2.put("message","Fine added Successfully");
		rows.put("response",rows2);
		rows.put("success",1);
		}
	else
		{
		rows2.put("new_fine_id","");
		rows2.put("message","Unable To Create Fine. Please try again");
		rows.put("response",rows2);
		rows.put("success",0);
		}
	System.out.println(rows);
	return rows;
	}
	
	/*----------------------------------------------Update FIne Function -----------------------------------------------------*/
	

	public static HashMap<Integer, Map<String, Object>> mUpdateImposeFine(int iRevID,int iPeriodID, int iLedgerID,int iUnitID,int iAmount,String SComment,String sDate,int isMail )
	{
	HashMap rows = new HashMap<>();
	//HashMap rows2 = new HashMap<>();
	//long mpDeleteFine = m_ImposeFine.mDeleteImposeFine(ID);
	HashMap rows2= m_ImposeFine.mUpdateImposeFine(iRevID, iPeriodID, iLedgerID, iUnitID, iAmount, SComment, sDate,isMail);
	
	//HashMap rows = new HashMap<>();
	if(rows2.size() > 0)
	{
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows.put("response","");
		rows.put("success",0);
	}
	
	return rows;
	}
	
/*------------------------------------------------Delete Fine ---------------------------------------------------*/	
	public static HashMap<Integer, Map<String, Object>> mDeleteImposeFine(int ID,int iUnitId,int iAmount, String sDesc, int isMail)
	{
	HashMap rows = new HashMap<>();
	//HashMap rows2 = new HashMap<>();
	//long mpDeleteFine = m_ImposeFine.mDeleteImposeFine(ID);
	HashMap rows2= m_ImposeFine.mDeleteImposeFine(ID,iUnitId,iAmount,sDesc,isMail);
	
	//HashMap rows = new HashMap<>();
	if(rows2.size() > 0)
	{
		 rows.put("response",rows2);
		 rows.put("success",1);
	}
	else
	{
		rows.put("response","");
		rows.put("success",0);
	}
	
	return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mImposeFineList(int iUnitId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpImposeList = m_ImposeFine.mfetchImposeFineList(iUnitId);
		
		 System.out.println("Size :"+mpImposeList);
		
		if(mpImposeList.size() > 0)
		{
			 rows2.put("FineList" ,MapUtility.HashMaptoList(mpImposeList));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("FineList","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
		 
		System.out.println("final :"+rows);
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mImposeHistory(int iRevID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mpImposeHistory = m_ImposeFine.mfetchImposeHistory(iRevID);
		 System.out.println(mpImposeHistory);
		if(mpImposeHistory.size() > 0)
		{
			 rows2.put("ImposeHistory" ,MapUtility.HashMaptoList(mpImposeHistory));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("ImposeHistory","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}
	public static void main(String[] args) throws Exception
	{
		//ViewImposeFine obj = new ViewImposeFine("wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQIuFNCuWfJpkIYVvW_zcRKWIJyzAMUhlYtmuEsePVESLcsMn3weIYzHWY6QXm7V4mXYOZ3hPGihVb6qEv2eo2w8WsPCMM0f8SAAatPUN1kdlw",true,"AVg1AdfB3JgCixtVoHHIv6tiExijODm8Swh0HA6Ioqmsi6p3JdABOP4Jr5YqhomBThC61Z9eSRvzo1fdwFFfTvfoXhV5f9tw1BbrbihpDKbtQNHSbFjx5S5SW-GoeHSYhnc-LoS2Rjtm_XQjUlQk55RkktEeB7UKKlBTgYHYNCs");
		// developer
		 String sToken = "wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQKOB9nwTehQh90KHOE_nm0cSW6amNSBpyb14dlg4R1mTTnwiSqwC_sJKOfUHgbeh-U5oETveEHD-7sy9OBuwUxJpOMy1SAXki65mzOe8UNRQw";
		 
		 // society 11
		//String sToken="LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM5N3Pd_xKoYAfaH-MFyntFbe3bHCnmjTrGIzCjB9sX33jBeNMEKbfzCG1SQCu30FTNhb3DGiRJkt_HDJT68Gw9C";
		 
		 // developer
		String sTkey = "BcWZ1TpXDkL7eIdNIKO1QkyeSz-xomn2kHVXouilR6Ook0Qw6HazUG9H1pxD3KLAuQWFYKiU-lgQ_6KP_MIjYqSVezvx888U5vfq8Gs7SPv7yUWMJmUTVNyVEET38WlAg4SH5YQKC-smjvlvXgVywg";
		
		// society 11
		//String sTkey ="j3Wk5ZsRl7gx_N1EGesT2Bo9r4MbHLdyyi8WWae_N1gxOxlWxfArLrkrDOhTfvkPbgLfAsB0KoNz6z5dZq6IHSQrl2Es4ii39_aRdwuAHs7ffax5nx_oWWSAK9V-dnL8n_qNaqbWQIXv76yUUSJP8Q";
		//raheja senetry
		//String sTkey ="4TIATQTSRudYjoww9c1OP2OCbNq56oFWFa8LyShIWsQKNF2pgMtb2jMFhEzFKFeH2fFZzfohKTvsLpNDc3YOQFHNpKerAbjzsNkB6q6dmWTY6sUlackESYICDWix6QrH0gMoXpuidWY7cHKZbheF_g";
		 //String sToken="to9uzc8Wct-Qruh_HsgQ5AAoh64vgufqOvIowLXye-xt03qfxzMAJkU2dkuFMHpUwEfDewl7NsvgrN9Ze5k9xW1GYwiL6BTx3drXsrDGbQU";
		
		ViewImposeFine obj = new ViewImposeFine(sToken, true, sTkey);		
		//HashMap rows =  ViewImposeFine.mImposeHistory(18);
		HashMap rows =  ViewImposeFine.mfetchPeriod();
		
	//	System.out.println(rows);
	}
}