package MainClasses;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DbOperations  extends  DbConnection 
{
	static Connection mMysqli;
	public DbOperations(Boolean bAccessRoot,String dbName) throws ClassNotFoundException 
	{
		super(bAccessRoot,dbName);
		
		this.mMysqli = super.mMysqli;
		if(this.mMysqli == null)
		{
			//System.out.println("mysql const"); 
		}
		else
		{
			//System.out.println("mysql nt empty"); 
		}
		
	}
	
	public  static HashMap<Integer, Map<String, Object>> Select(String Sql)
	{
		HashMap<Integer, Map<String, Object>> rows = new HashMap<Integer, Map<String, Object>>();
		if(Sql == null || Sql.isEmpty())
		{
			//sql query is empty or null
			System.out.println("Problem In SQL Query!,Your sql query seems wrong. Please check your SQL query.");
			
		}
		
		try
		{
			
			Statement stmt = mMysqli.createStatement();
			ResultSet rs = stmt.executeQuery(Sql);
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			int count = 0;
			
			//add fetched record to map
		    while (rs.next())
		    {
		    	//use object for all types of database table columns datatype  i.e, character,int,float,varchar
			   Map<String, Object> row = new HashMap<String, Object>(columns);        
		       for(int i = 1; i <= columns; ++i)
		       {      
		    	   row.put(md.getColumnLabel(i), rs.getObject(i));        
		       }
		       
		       rows.put(count,row);      
		       count++;
		    }    
		    rs.close();
		    stmt.close();
		}
		catch(Exception e)
		{
			//exception occured 
			Map<String, Object> row = new HashMap<String, Object>(0);    
			row.put("0",e.getMessage());
			rows.put(0,row); 
		
		}
		
	 return rows;
		
	}

	public static long Insert(String Sql) {
		// TODO Auto-generated method stub
		long rs = 0;
		//HashMap<Integer, Map<String, Object>> rows = new HashMap<Integer, Map<String, Object>>();
		if(Sql == null || Sql.isEmpty())
		{
			//sql query is empty or null
			System.out.println("Problem In SQL Query!,Your sql query seems wrong. Please check your SQL query.");
		}
		else
		{
			
			try
			{
				Statement stmt = mMysqli.createStatement();
				
				rs = stmt.executeUpdate(Sql,Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt.getGeneratedKeys();
		        if (rs1.next()){
		        	rs=rs1.getInt(1);
		        }
		        rs1.close();

			    stmt.close();
			}
			catch(Exception e)
			{
				System.out.println(e);
				//exception occured 
			}
		}
		 return rs;
			
		}

	public static long Update(String Sql) {
		// TODO Auto-generated method stub
		long result = 0;
		//HashMap<Integer, Map<String, Object>> rows = new HashMap<Integer, Map<String, Object>>();
		if(Sql == null || Sql.isEmpty())
		{
			//sql query is empty or null
			System.out.println("Problem In SQL Query!,Your sql query seems wrong. Please check your SQL query.");
		}
		else
		{
			
			try
			{
				Statement stmt = mMysqli.createStatement();
				result = stmt.executeUpdate(Sql,Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt.getGeneratedKeys();
		        if (rs1.next()){
		        	result=rs1.getInt(1);
		        }
		        rs1.close();

			    stmt.close();
			}
			catch(Exception e)
			{
				System.out.println(e);
				//exception occured 
			}
		}
		 return result;
	}
	
	public static long Delete(String Sql) {
		// TODO Auto-generated method stub
		long result = 0;
		//HashMap<Integer, Map<String, Object>> rows = new HashMap<Integer, Map<String, Object>>();
		if(Sql == null || Sql.isEmpty())
		{
			//sql query is empty or null
			System.out.println("Problem In SQL Query!,Your sql query seems wrong. Please check your SQL query.");
		}
		else
		{
			
			try
			{
				Statement stmt = mMysqli.createStatement();
				result = stmt.executeUpdate(Sql,Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt.getGeneratedKeys();
		        if (rs1.next()){
		        	result=rs1.getInt(1);
		        }
		        rs1.close();

			    stmt.close();
			}
			catch(Exception e)
			{
				System.out.println(e);
				//exception occured 
			}
		}
		 return result;
	}
	
	public static void BeginTransaction() throws SQLException {
		mMysqli.setAutoCommit(false);
	}
	
	public static void EndTransaction() throws SQLException {
		mMysqli.commit();
	}
	
	public static void RollbackTransaction() throws SQLException {
		mMysqli.rollback();
	}

}
