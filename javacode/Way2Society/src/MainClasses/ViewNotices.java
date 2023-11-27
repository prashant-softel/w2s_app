package MainClasses;

import java.util.HashMap;
import java.util.Map;
import CommonUtilityFunctions.MapUtility;

public class ViewNotices extends CommonBaseClass 
{
	static Notice m_Notice;
	
	public ViewNotices(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_Notice = new Notice(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchAllNotices(int iUnitId,int iSocietyId, int iNoticeID, boolean bIsNotification)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpNotices = m_Notice.mfetchNotices(iUnitId, iSocietyId, 0, true, iNoticeID);
		
		if(mpNotices.size() > 0)
		{	
			if(bIsNotification)
			{
				rows.put("response", mpNotices.get(0));
			}
			else
			{
				rows2.put("notices",MapUtility.HashMaptoList(mpNotices));
				rows.put("response",rows2);
			}
			rows.put("success",1);
		}
		else
		{
			rows2.put("notices","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	 // System.out.println(rows);
	    return rows;
	}
	
	/*public static  HashMap<Integer, Map<String, Object>> mfetchNotice(int iUnitId, int iSocietyId, int iNoticeID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		HashMap<Integer, Map<String, Object>> mpNotices = m_Notice.mfetchNotices(iUnitId, iSocietyId, 0, true, iNoticeID);
		
		if(mpNotices.size() > 0)
		{	
			rows2.put("notices",MapUtility.HashMaptoList(mpNotices));
			rows.put("response",rows2);
			rows.put("success",1);
		}
		else
		{
			rows2.put("notices","");
			rows.put("response",rows2);
		    rows.put("success",0);
		}
	  
	    return rows;
	}*/
	
	
	
	public static void main(String[] args) throws Exception
	{
		String sToken = "LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4yShq3gS2XOamTB-D4umDzi2TYQfnG5nBEn_d3K5HEbfKlAGWg8f-FjOl6u9vV4NJvFoxg2ZtUMth9Bdt6aVKx";
		String sTkey = "nXTYH6gXb5NrNS7bD-xf-7p-DLueHDMHV63rrzV8xdR5QxtyKNUQph89PNpuyjn5qq1PT2kOpx3iSYamT24xEmrftFMt-IfABEKDKRDSmEhS0-n02MO74v-qHylcskOL79mjDGY4VS4R1dqZybT3Mg";
		ViewNotices obj = new ViewNotices(sToken, true, sTkey);
		HashMap rows =  obj.mfetchAllNotices(26, 59, 0, true);
		//System.out.println(obj.convert(rows));
		System.out.println(rows);	
	}
}


