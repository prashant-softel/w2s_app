package MainClasses;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ManageUser extends CommonBaseClass 
{
	static User m_User;
	//static Payment m_Payment;
	public  ManageUser(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException {
		super(sToken,bIsVerifyDbDetails,sTkey);
		// TODO Auto-generated constructor stub
		m_User = new User(CommonBaseClass.m_objDbOperations);
		//m_Payment = new Payment(CommonBaseClass.m_objDbOperations);
		
	}
	
	public static HashMap<Integer, Map<String, Object>> mAddUser(String sName, String sEmailID, String sPassword, String sFBID) throws SQLException
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		//fetching all bills
		int iRetValue = m_User.mAddUser();
		
		if(iRetValue == 1)
		{
			//add bill to map
			 rows.put("success",1);
			 rows2.put("message","User added successfully");
			 rows.put("response",rows2);
		}
		else
		{
			//bills not found
			rows.put("success",0);
			rows2.put("message","Unable to add user. Please try again.");
			 rows.put("response",rows2);
		}
	  
	    return rows;
	}
	
	public static void main(String[] args) throws Exception
	{
		ManageUser obj = new ManageUser("LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM4rN4UvW36NPHGaIgIoQdhRHazieK-x57rsPgmCHlTFQUroo7KcWQ2Pj_MRe0CJYlLFw_aOUq6_zH58af3hJwYX", true, "g6HoIfwqvn-e9HKlLi1XmHLLGpDoDVXDCNmzM3w9QNNIvEChod9HTBjp5gtkgJrMX59Ok3ciPvbV2luYx4Yde55FKR217XkQbsRab1FCMR-vKczR8jYQQnDz8t4EJhsU2NyJ7nEsMQfjZ-02XDoKIw");
		//HashMap rows =  obj.mfetchNEFTBanks();
		HashMap rows = obj.mAddUser("", "", "", "");
		//System.out.println(obj.convert(rows));
		System.out.println(rows);	
	}
}


