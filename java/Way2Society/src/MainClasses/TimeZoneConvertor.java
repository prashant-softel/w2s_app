package MainClasses;
import static MainClasses.DbConstants.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.sun.javafx.collections.MappingChange.Map;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import CommonUtilityFunctions.MapUtility;

public class TimeZoneConvertor{

	ProjectConstants m_objProjectConstants;
	private static CommonUtilityFunctions.MapUtility m_objMapUtility;
	
	public TimeZoneConvertor() throws ClassNotFoundException 
	{
		
		m_objProjectConstants = new ProjectConstants();
		m_objMapUtility = new MapUtility();
	}

	
	
	/*public static String LocalTimeConvertor(String Time) throws ParseException, java.text.ParseException
	{
		DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = utcFormat.parse(Time);

		DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pstFormat.setTimeZone(TimeZone.getTimeZone("IST"));

		System.out.println(pstFormat.format(date));
		return pstFormat.format(date);
		 // **** YOUR CODE **** BEGIN ****
	    
		
	}*/
	
	/*public static String convertToCurrentTimeZone(String Time) throws ParseException, java.text.ParseException
	{
		DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = utcFormat.parse(Time);

		DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pstFormat.setTimeZone(TimeZone.getTimeZone("IST"));

		System.out.println(pstFormat.format(date));
		return pstFormat.format(date);
		 // **** YOUR CODE **** BEGIN ****
	    
		
	}*/
	public static String convertToCurrentTimeZone(String Date) {
        String converted_date = "";
        try {

            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = utcFormat.parse(Date);

            DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            //currentTFormat.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));
            currentTFormat.setTimeZone(TimeZone.getTimeZone("IST"));
           // DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		//pstFormat.setTimeZone(TimeZone.getTimeZone("IST"));
    		//
            converted_date =  currentTFormat.format(date);
        }catch (Exception e){ e.printStackTrace();}
        //System.out.println(converted_date);
        return converted_date;
}
	public static String getCurrentTimeZone(){
        TimeZone tz = Calendar.getInstance().getTimeZone();
       // System.out.println(tz.getDisplayName());
        return tz.getID();
}
	
	
	public static void main(String[] args) throws Exception
	{
		//Staff st=new Staff();
		//HashMap objHash = st.getStaffDetails();
		
		//LocalTimeConvertor("2018-09-01 12:04:47");
		//LocalTimeConvertor("2018-06-17 16:13:43");
		//HashMap objHash=st.LocalTImeConvertor();
		//Gson objGson = new Gson();
		//String objStr = objGson.toJson(objHash);
		//System.out.println(objStr);
//		response.setContentType("application/json");
//		out.println(objStr);
//		st.monthlyStaffAttendance(7, 2018,4);
		//st.markEntry(21);
	//	st.markExit(21);

	}

}
