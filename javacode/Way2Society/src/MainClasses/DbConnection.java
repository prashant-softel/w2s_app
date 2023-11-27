package MainClasses;

import static MainClasses.DbConstants.DB_DATABASE;
import static MainClasses.DbConstants.DB_HOST;
import static MainClasses.DbConstants.DB_PASSWORD;
import static MainClasses.DbConstants.DB_USER;

import java.lang.*;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;;

public class DbConnection 
{
	public Connection mMysqli;
//	public Connection mMysqliRoot;
	public boolean bIsConnected = true;
	//public static String sConnErrorMsg ="";
	public static HashMap<String, Connection>  mDBConnectionMap = new HashMap<>();
	
	public DbConnection(Boolean bAccessRoot,String dbName) throws ClassNotFoundException 
	{
	//	System.out.println("dbconn " + dbName );
		Connection sqliConnection = null;
		if(bAccessRoot == true)
		{
			dbName = "hostmjbt_societydb";
		}
		//System.out.println("Database Name "+dbName);
		Class.forName("com.mysql.cj.jdbc.Driver");
		mMysqli = (Connection) mDBConnectionMap.get(dbName);
		if(mMysqli == null)
		{
			/*******connect to society database*********/
			//sConnErrorMsg +="2";
			
			try 
			{
				//System.out.println(dbName);
				mMysqli = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName + "?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC",DB_USER,DB_PASSWORD);
				mDBConnectionMap.put(dbName, mMysqli);
				//System.out.println("Created new dbconn " + dbName + " mMysqli db<" + mMysqli + ">");
				System.out.println("Created new dbconn " + dbName);
			} 
			catch (SQLException e) 
			{
				System.out.println("Connection failed to db<" + dbName + ">");
				//sConnErrorMsg +="4";
				e.printStackTrace();
				//sConnErrorMsg += "object not created if part";
			}
		}
		else
		{
			//System.out.println("Found existing mysql connection to " + dbName + " mMysqli db<" + mMysqli + ">");
			System.out.println("Existing Conn to " + dbName );
		}
	}

}
