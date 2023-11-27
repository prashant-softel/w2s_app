package MainClasses;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import CommonUtilityFunctions.MapUtility;

public class ViewPhotoAlbum extends CommonBaseClass 
{
	static Photoalbum m_Photoalbum;
	
	public ViewPhotoAlbum(String sToken, Boolean bIsVerifyDbDetails, String sTkey) throws ClassNotFoundException 
	{
		super(sToken,bIsVerifyDbDetails,sTkey);
		m_Photoalbum = new Photoalbum(CommonBaseClass.m_objDbOperations);
	}
	
	public static  HashMap<Integer, Map<String, Object>> mfetchPhotoalbum(int SocietyID)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mPhotoalbum = m_Photoalbum.mfetchPhotoalbum(SocietyID);
		 
		if(mPhotoalbum.size() > 0)
		{
			 rows2.put("photoalbum",MapUtility.HashMaptoList(mPhotoalbum));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("photoalbum","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}

	public static HashMap<Integer, Map<String, Object>> getAlbumID(String folder)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mPhotoalbum = m_Photoalbum.getAlbumIdFromFolderName(folder);
		 
		if(mPhotoalbum.size() > 0)
		{
//			 rows2.put("photoalbum",MapUtility.HashMaptoList(mPhotoalbum));
			 rows.put("response",MapUtility.HashMaptoList(mPhotoalbum));
			 rows.put("success",1);
		}
		else
		{
//			 rows2.put("photoalbum","");
			 rows.put("response","");
			 rows.put("success",0);
		}
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> approveImage(int imageId) 
	{
		HashMap rows = new HashMap<>();
		
		long mPhotoalbum = Photoalbum.approveImage(imageId);
		 
		if(mPhotoalbum > 0)
		{
			 rows.put("response", "1");
			 rows.put("success",1);
		}
		else
		{
			 rows.put("response","0");
			 rows.put("success",0);
		}
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> disApproveImage(int imageId) 
	{
		HashMap rows = new HashMap<>();
		
		long mPhotoalbum = Photoalbum.disApproveImage(imageId);
		 
		if(mPhotoalbum > 0)
		{
			 rows.put("response","1");
			 rows.put("success",1);
		}
		else
		{
			 rows.put("response","0");
			 rows.put("success",0);
		}
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> saveTimestamp(int imageId, String date) 
	{
		HashMap rows = new HashMap<>();
		long mPhotoalbum = Photoalbum.saveTimeStamp(imageId, date);
		 
		if(mPhotoalbum > 0)
		{
			 rows.put("response","1");
			 rows.put("success",1);
		}
		else
		{
			 rows.put("response","0");
			 rows.put("success",0);
		}
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> saveMemberId(int imageId, int memberId) 
	{
		HashMap rows = new HashMap<>();
		long mPhotoalbum = Photoalbum.saveMemberId(imageId, memberId);
		 
		if(mPhotoalbum > 0)
		{
			 rows.put("response","1");
			 rows.put("success",1);
		}
		else
		{
			 rows.put("response","0");
			 rows.put("success",0);
		}
	    return rows;
	}

	public static  HashMap<Integer, Map<String, Object>> mfetchReportedPhotoalbum(int SocietyID)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mPhotoalbum = m_Photoalbum.mfetchReportedPhotoalbum(SocietyID);
		 
		if(mPhotoalbum.size() > 0)
		{
			 rows2.put("photoalbum",MapUtility.HashMaptoList(mPhotoalbum));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("photoalbum","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}

	public static  HashMap<Integer, Map<String, Object>> mfetchDisapprovedPhotoalbum(int SocietyID)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mPhotoalbum = m_Photoalbum.mfetchDisapprovedPhotoalbum(SocietyID);
		 
		if(mPhotoalbum.size() > 0)
		{
			 rows2.put("photoalbum",MapUtility.HashMaptoList(mPhotoalbum));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("photoalbum","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}
	
	public static HashMap<Integer, Map<String, Object>> reportImage(int imageId, String comment) 
	{
		HashMap rows = new HashMap<>();
		
		long mPhotoalbum = m_Photoalbum.reportImage(imageId, comment);
		 
		if(mPhotoalbum > 0)
		{
			 rows.put("response","1");
			 rows.put("success",1);
		}
		else
		{
			 rows.put("response","0");
			 rows.put("success",0);
		}
	    return rows;
	}
	//Vaishali
	public static  HashMap<Integer, Map<String, Object>> mGetPhotoAlbum(int SocietyID)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mPhotoalbum = m_Photoalbum.mGetPhotoAlbumName(SocietyID);
		 
		if(mPhotoalbum.size() > 0)
		{
			 rows2.put("PhotoAlbumDetails",MapUtility.HashMaptoList(mPhotoalbum));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("PhotoAlbumDetails","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}
	public static  HashMap<Integer, Map<String, Object>> mFetchAllPhotos(int iAlbumId,int SocietyID)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mPhotoalbum = m_Photoalbum.mFetchAllPhotosByAlbumId(iAlbumId,SocietyID);
		 
		if(mPhotoalbum.size() > 0)
		{
			 rows2.put("PhotoDetails",MapUtility.HashMaptoList(mPhotoalbum));
			 rows.put("response",rows2);
			 rows.put("success",1);
		}
		else
		{
			 rows2.put("PhotoDetails","");
			 rows.put("response",rows2);
			 rows.put("success",0);
		}
	    return rows;
	}
	public static void main(String[] args) throws Exception
	{
		ViewPhotoAlbum obj = new ViewPhotoAlbum("LTewE2eQiSIiNJvWTEk0uot5iLGglX6RLlwW9iQGYM6Q-gafiUZ_XTYSlvRn4tPxOunUwrlSK3bnqnnuYWZMJgLhCUe8zVXraO2JL67LCxG8O687p4tpzfjjXr_A7yw6", true, "dzHwGEkbzyLzEcjmm73EXHAfURjmSuDll7rbNGV8PCjFUPR_HosXCPU3LPP80FpNAZ5KVznrhPBxLZzGTORwsswuebkZzmToPdp45nuPeAxt7k3BolcGum40_cnRz9uPq2i2Uw8Br7lzsyrxt6guPw");		
		HashMap rows =  obj.mGetPhotoAlbum(59);
		//HashMap rows1 =  obj.mAddClassifieds(17,15,"prjjrs", "hrthhy", "2017-07-01", "gfs.jpg","","","",1);
	//	System.out.println(rows1);
		System.out.println(rows);
	}

}