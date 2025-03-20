package CommonUtilityFunctions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CalendarFunctions 
{
	public static Date subtractDay(Date date,int iNoOfDays)
	{
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DAY_OF_MONTH, -iNoOfDays);
	    return cal.getTime();
	}
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
        System.out.println(converted_date);
        return converted_date;
}
}
