package MainClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import CommonUtilityFunctions.CalendarFunctions;
import CommonUtilityFunctions.MapUtility;

public class Photoalbum
{
	static DbOperations m_dbConn;
	static DbOperations m_dbConnRoot;
	
	public Photoalbum(DbOperations m_dbConnObj)
	{
	    try 
		{
	    	m_dbConnRoot = new DbOperations(true,"hostmjbt_societydb");
		}
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchPhotoalbum(int SocietyID)
	{
		String sSqlPhotoalbum = "";
		sSqlPhotoalbum="SELECT a.id as album_id,a.name,a.folder,b.id, b.url, b.cover, c.group_id from soc_group as c join album as a on c.group_id=a.group_id join photos as b where a.id=b.album_id and c.society_id="+SocietyID+" AND b.approveFlag=0 AND b.comment = ''";
		//System.out.println(sSqlClassifieds);
		//sSqlPhotoalbum = "SELECT album.* FROM album LEFT OUTER JOIN `group` ON album.group_id = `group`.group_id WHERE `group`.society_id = '" +SocietyID+ "'";
		HashMap<Integer, Map<String, Object>> Photoalbum =  m_dbConnRoot.Select(sSqlPhotoalbum);
		return Photoalbum;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchReportedPhotoalbum(int SocietyID)
	{
		String sSqlPhotoalbum = "";
		sSqlPhotoalbum="SELECT a.id as album_id,a.name,a.folder,b.id, b.url, b.comment, c.group_id from soc_group as c join album as a on c.group_id=a.group_id join photos as b where a.id=b.album_id and c.society_id="+SocietyID+" AND b.comment <> ''";
		//System.out.println(sSqlClassifieds);
		//sSqlPhotoalbum = "SELECT album.* FROM album LEFT OUTER JOIN `group` ON album.group_id = `group`.group_id WHERE `group`.society_id = '" +SocietyID+ "'";
		HashMap<Integer, Map<String, Object>> Photoalbum =  m_dbConnRoot.Select(sSqlPhotoalbum);
		return Photoalbum;
	}
	
	public static HashMap<Integer, Map<String, Object>> mfetchDisapprovedPhotoalbum(int SocietyID)
	{
		String sSqlPhotoalbum = "";
		sSqlPhotoalbum="SELECT a.id as album_id,a.name,a.folder,b.id, b.url,c.group_id from soc_group as c join album as a on c.group_id=a.group_id join photos as b where a.id=b.album_id and c.society_id="+SocietyID+" AND b.approveFlag=1";
		//System.out.println(sSqlClassifieds);
		//sSqlPhotoalbum = "SELECT album.* FROM album LEFT OUTER JOIN `group` ON album.group_id = `group`.group_id WHERE `group`.society_id = '" +SocietyID+ "'";
		HashMap<Integer, Map<String, Object>> Photoalbum =  m_dbConnRoot.Select(sSqlPhotoalbum);
		return Photoalbum;
	}
	
	public static HashMap<Integer, Map<String, Object>> getAlbumIdFromFolderName(String folderName) 
	{
		String selectSQL = "SELECT id FROM album WHERE folder = '"+ folderName +"'";
		HashMap<Integer, Map<String, Object>> albumID =  m_dbConnRoot.Select(selectSQL);
		
		return albumID;
	}
	
	public static long disApproveImage(int imageId) 
	{
		String SQL = "UPDATE photos SET approveFlag = 1 WHERE id = " + imageId;
		//System.out.println("Query:" + SQL);
		return m_dbConnRoot.Update(SQL);
	}
	
	public static long approveImage(int imageId) 
	{
		String SQL = "UPDATE photos SET approveFlag = 0 WHERE id = " + imageId;
		//System.out.println("Query:" + SQL);
		return m_dbConnRoot.Update(SQL);
	}
	
	public static long reportImage (int imageId, String comment)
	{
		String SQL = "UPDATE photos SET comment='" + comment + "' WHERE id = '" + imageId + "'";
		return m_dbConnRoot.Update(SQL);
	}
	
	public static long saveTimeStamp(int imageId, String date)
	{
		String Sql = "UPDATE photos SET timestamp='" + CalendarFunctions.convertToCurrentTimeZone(date) + "'";
		return m_dbConnRoot.Update(Sql);
	}
	
	public static long saveMemberId(int imageId, int member_id)
	{
		String sql = "UPDATE photos SET member_id='" + member_id + "' WHERE id = '" + imageId + "'";
		
		return m_dbConnRoot.Update(sql);
	}
	//Vaishali Code
		public static HashMap<Integer, Map<String, Object>> mGetPhotoAlbumName(int iSocietyID)
		{
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String sSqlPhotoalbum = "";
			//sSqlPhotoalbum="Select p.`album_id`,a.`name`,g.`group_id`,g.`group_name`,p.`cover`,p.`url`,a.`folder` from `photos` as p, `group` as g, `album` as a where g.`society_id` = '"+iSocietyID+"' and g.`status` = 'Y' and g.`group_id` = a.`group_id` and a.`id` = p.`album_id` and p.`cover`= '1' and p.`status` = 'Y'";
			sSqlPhotoalbum="SELECT society.society_name,group.group_name,album.`name`,album.`id` as album_id ,album.`folder` FROM `soc_group` JOIN `society` ON (society.society_id = soc_group.society_id) JOIN `group` ON (group.group_id = soc_group.group_id) join `album` on(soc_group.group_id=album.group_id) where society.society_id='"+iSocietyID+"'";
			//System.out.println("sSqlPhotoalbum"+sSqlPhotoalbum);
			HashMap<Integer, Map<String, Object>> Photoalbum =  m_dbConnRoot.Select(sSqlPhotoalbum);
			int iAlbumId= 0;
			//System.out.println("Photoalbum :"+Photoalbum);
			if(Photoalbum.size() > 0)
			{	
				for(Entry<Integer, Map<String, Object>> entry : Photoalbum.entrySet()) 
				{
					iAlbumId = (Integer) entry.getValue().get("album_id");
	
					String sSqlCoverPhoto = "select `url` from photos where cover = '1' and album_id = " + iAlbumId;	
					HashMap<Integer, Map<String, Object>> mpCover =  m_dbConn.Select(sSqlCoverPhoto);
					//System.out.println("sSqlOtherFamily: " + mpCover);
					if(mpCover.size() > 0)
					{
						Map.Entry<Integer, Map<String, Object>> entryCoverMain = mpCover.entrySet().iterator().next();
						String url =entryCoverMain.getValue().get("url").toString();
						
						entry.getValue().put("url", url);
					}
					else
					{
						String sSqlOtherCoverPhoto = "select `url` from photos where album_id = " + iAlbumId;	
						HashMap<Integer, Map<String, Object>> mpCover1 =  m_dbConn.Select(sSqlOtherCoverPhoto);
						//System.out.println("sSqlOtherCoverPhoto :"+sSqlOtherCoverPhoto);
						if(mpCover1.size() > 0)
						{
						Map.Entry<Integer, Map<String, Object>> entryCoverMain = mpCover1.entrySet().iterator().next();
						String url =entryCoverMain.getValue().get("url").toString();
					
						entry.getValue().put("url", url);
						}
						else
						{
							entry.getValue().put("url", "");
						}
					}
				}
			}		
			
			//rows.put("unit", MapUtility.HashMaptoList(resultMemberMain));
			
			/*String sSqlCoverPhoto = "select * from photos where cover = '1' and album_id = " + iAlbumId;	
			HashMap<Integer, Map<String, Object>> mpCover =  m_dbConn.Select(sSqlCoverPhoto);
			System.out.println("sSqlOtherFamily: " + mpCover);
			if(mpCover.size() > 0)
			{
				rows2.put("url", MapUtility.HashMaptoList(mpCover));
			}
			else
			{
				String sSqlOtherCoverPhoto = "select * from photos where album_id = " + iAlbumId;	
				HashMap<Integer, Map<String, Object>> mpOtherCover =  m_dbConn.Select(sSqlOtherCoverPhoto);
				rows2.put("url", MapUtility.HashMaptoList(mpOtherCover));
			}*/
			//rows2.put("Photoalbum", MapUtility.HashMaptoList(Photoalbum));
			//rows.put("Albums",rows2);
			return Photoalbum;
		}
		public static HashMap<Integer, Map<String, Object>> mFetchAllPhotosByAlbumId(int iAlbumId ,int iSocietyId)
		{
			String sSqlPhotoalbum = "";
			sSqlPhotoalbum="Select p.`id`as photo_id,a.`name`,a.`folder`,p.`url` from `album` as a, `photos` as p where p.`album_id` = a.`id` and a.`id` = '"+iAlbumId+"' and p.`approveFlag` = '0' and p.comment='' ";
			//System.out.println(sSqlClassifieds);
			//sSqlPhotoalbum = "SELECT album.* FROM album LEFT OUTER JOIN `group` ON album.group_id = `group`.group_id WHERE `group`.society_id = '" +SocietyID+ "'";
			HashMap<Integer, Map<String, Object>> Photoalbum =  m_dbConnRoot.Select(sSqlPhotoalbum);
			return Photoalbum;
		}
}