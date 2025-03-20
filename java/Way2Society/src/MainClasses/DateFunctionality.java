package MainClasses;
import java.util.*;

public class DateFunctionality {
	
	public static String getCurrentDate()
	{
		Calendar cal=Calendar.getInstance();
		int Date=cal.get(Calendar.DATE);
		int Month=cal.get(Calendar.MONTH);
		int Year= cal.get(Calendar.YEAR);
		String CurDate=Year+"-"+(Month+1)+"-"+Date;
		return CurDate;
	}
	public static String convertToStandardFormat(String date)
	{
		String[] date_s=date.split("-");   
		String cDate=date_s[2]+"-"+date_s[1]+"-"+date_s[0];
		return cDate;
	}
	public static String convertToSqlFormat(String date)
	{ 
		String[] date_s=date.split("-");
		String cDate=date_s[0]+"-"+date_s[1]+"-"+date_s[2];
		return cDate;
	}
}
