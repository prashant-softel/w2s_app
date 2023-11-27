package Utility;

import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import java.text.*;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import java.security.MessageDigest;

public class UtilityClass {

	private static Pattern pattern;
	private static Matcher matcher;

	private static final String DATE_PATTERN ="(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";

		
	TimeZone zone=TimeZone.getDefault();
	Calendar calender=Calendar.getInstance(zone);
	
	public static Date getDBFormatDate(String yyyymmdd) throws ParseException
	{
		try 
			{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(yyyymmdd);
			String formattedTime = output.format(d);
			Date d1 = output.parse(formattedTime);
			if(formattedTime!= "" && formattedTime != "00-00-0000")
				{
				return d1;
				}
			else
				{
				
				return output.parse("00-00-0000");
				}
			}
			
		catch(Exception e)
			{
			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
			return output.parse("00-00-0000");
			}
	}
	
	public String getDisplayFormatDate(String yyyymmdd, String seperator) throws ParseException
		{
	    seperator = "-";	
		String ddmmyyyy = "";
		if((new SimpleDateFormat("yyyy-mm-dd").parse("yyyymmdd")) != null &&  yyyymmdd != "0000-00-00" && yyyymmdd != "00-00-0000")
		{
			DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			ddmmyyyy = dateFormat.format(ddmmyyyy);
			return ddmmyyyy;
		}
		//else
		//{
		//	return '00-00-0000';
		//}
		return ddmmyyyy;
		
		}
	
	private String getRandomUniqueCode() throws NoSuchAlgorithmException
		{
		try {
			String timestamp=(Calendar.getInstance().get(Calendar.MILLISECOND))+"";
			String uniqueID = UUID.randomUUID().toString();
			String input=timestamp+uniqueID;
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while (hashtext.length() < 32) 
				{
				hashtext = "0" + hashtext;
				}
			return hashtext;
			}
		catch(java.security.NoSuchAlgorithmException e)
			{
			
			}
		return "";
		}
	
	public HashMap<String,Date> getCurrentTimeStamp() throws ParseException
	{
		//$aryDateTime = array();
		//$current_DateTime = date('Y-m-d H:i:s');
		//$current_Time = date('H:i:s');
		//$current_Date = date('Y-m-d');
		
		//aryDateTime['DateTime'] = $current_DateTime;
		//aryDateTime['Date'] = $current_Date;
		//aryDateTime['Time'] = $current_Time;
		Date d=calender.getTime();
		HashMap<String,Date> aryDateTime = null;
		SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date1=sdf1.format(d);
		SimpleDateFormat sdf2 =new SimpleDateFormat("HH:mm:ss");
		String date2=sdf2.format(d);
		SimpleDateFormat sdf3 =new SimpleDateFormat("yyyy-MM-dd");
		String date3=sdf3.format(d);
		
		aryDateTime.put("DateTime",sdf1.parse(date1));
		aryDateTime.put("Date",sdf2.parse(date2));
		aryDateTime.put("Time",sdf3.parse(date3));
		
		return aryDateTime;

	}
	
	public static boolean isValidEmailID(String email)
	{
		try{
			String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
	        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
	        java.util.regex.Matcher m = p.matcher(email);
	        return m.matches();
			}
		finally{
			
		}
	}

	public static boolean isValidNumber(String number)
	{
		try{
		if(number.isEmpty())
			{
			return false;
			}
		if(Float.parseFloat(number)>0)
			{
			return true;
			}
			}
		catch(Exception e)
			{
			return false;
			}
		return false;
	}	

	public static boolean isValidDate(Date date1) 	
		{
	   /* if (date == null || !date.matches("\\d\\d-[0-3]\\d{4}-[01]"))
	        
	    	{
	    	System.out.println("nbvhbm"+date.matches("\\d\\d-[0-3]\\d{4}-[01]"));
	    	return false;
	    	}
	    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	    try {
	        df.parse(date);
	        return true;
	    	} 
	    catch (ParseException ex)
	    	{
	        return false;
	    	}
	     */
		SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
		String date = output.format(date1);
		
		pattern = Pattern.compile(DATE_PATTERN);
		matcher = pattern.matcher(date);
		if(matcher.matches())
			{
			 matcher.reset();
			 if(matcher.find())
			 {
             String day = matcher.group(1);
             String month = matcher.group(2);
			 int year = Integer.parseInt(matcher.group(3));
		     if (day.equals("31") &&  (month.equals("4") || month .equals("6") || month.equals("9") ||
		                  month.equals("11") || month.equals("04") || month .equals("06") ||month.equals("09"))) 
		     	{
		    	 return false; // only 1,3,5,7,8,10,12 has 31 days
			    }
		     else if (month.equals("2") || month.equals("02")) 
		     	{
		          //leap year
		    	 if(year % 4==0)
			  		{
		    		 if(day.equals("30") || day.equals("31"))
				  		{
		    			 return false;
				  		}
		    		 else
				  		{
		    			 return true;
				  		}
			  		}
		    	 else
		    	 	{
				    if(day.equals("29")||day.equals("30")||day.equals("31"))
				   		{
					   return false;
				   		}
				    else
				   		{
						return true;
				   		}
		    	 	}
		     	}
		     else
		     	{
				return true;
			    }
		     }
		  else
			 {
		     return false;
			 }
		  }
	   else
		  {
		  return false;
		  }
	   }
	}
