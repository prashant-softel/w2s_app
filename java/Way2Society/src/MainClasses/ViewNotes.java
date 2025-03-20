package MainClasses;
import java.util.HashMap;
import java.util.Map;

import CommonUtilityFunctions.MapUtility;
import MainClasses.DebitCrediNote;

public class ViewNotes extends CommonBaseClass {
	static DebitCrediNote m_Notes;
	static Utility m_Utility;
	public ViewNotes(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken, bIsVerifyDbDetails, sTkey);
		// TODO Auto-generated constructor stub
		m_Notes = new DebitCrediNote(CommonBaseClass.m_objDbOperations);	
		m_Utility = new Utility(CommonBaseClass.m_objDbOperations, CommonBaseClass.m_objDbRootOperations);
	}




	
	
	public static  HashMap<Integer, Map<String, Object>> mfetchNoteDetails(int iUnitId,int iBillType)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		
		HashMap<Integer, Map<String, Object>> mpNotesDetails= m_Notes.mfetchNotes(iUnitId, iBillType);
		 
		if(mpNotesDetails.size() > 0)
		{	
			
			rows2.put("notes",mpNotesDetails);
			rows.put("success",1);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("receipt","");
			rows.put("response",rows2);
			rows.put("success",0);
		}
	  
	    return rows;
	}
	
	
	
	
	public static void main(String[] args)
	{
		//http://way2society.com:8080/Mobile/receipts.php?token=vQS80AMt3VSC9HNXI5q15hqK2Z9mU3PmzSznPOHyR35-H62mxfRizw9HcYcq-00u6WJOXAlyYPbrtfKtjcZBzw&tkey=ehwFPo678H65Z_hO_gcjr2u2Qq0kIuYP1aYYdkHYcNlx64HNKg7NHUTUfP2PlHsHt2tZK629VmIZk0CVhNT15mLM-52KHS64xDA0q_PPKFs&map=3471&test=test
		ViewReceipts obj;
		try {
			obj = new ViewReceipts("wvSn5ujhqo2IgEmOgxIb2ZAGGHR-HbsaeKZZKxciGQKhI38wWkZxx8CPJXkD-jd9kN78mX1V9vfECrq6t3e4yrzI8_OWcPfbqHo06RWfUlxyEm_P5KJ5jEraCntLZ71k5fUsz7oH_MFm9Wo4jpwcZw", true, "ay0GeU5pO5RYD3D21j5pLKAiLX317KYbCrouaM4ecxuAf8JL4720kqUn5PnK1gN1YK1RZ56OL_T38K0TQXjjN0drhfnw1jtoU3TeeqiZvMlZD5nIq4v7HKwQjKlWYxSlmqq5cg5KiUskKgqkvlcZRSs-k9ipGQ8zrjAYkRn5FO8");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap rows = new HashMap<>();
		rows = mfetchNoteDetails(28,89);
		System.out.println(rows);
	}

}