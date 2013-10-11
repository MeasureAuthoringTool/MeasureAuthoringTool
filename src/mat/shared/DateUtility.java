package mat.shared;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtility {
	
	private static final Log logger = LogFactory.getLog(DateUtility.class);
	
	
	public static String convertDateToString(Date date){
		String dateVal = "";
		if(date != null){
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a"); 
			dateVal = format.format(date);
		}
		return dateVal;
	}
	
	public static String convertDateToStringNoTime(Date date){
		String dateVal = "";
		if(date != null){
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy"); 
			dateVal = format.format(date);
		}
		return dateVal;
	}
	
	public static String convertDateToStringNoTime2(Date date){
		String dateVal = "";
		if(date != null){
			SimpleDateFormat format = new SimpleDateFormat("MMddyyyy"); 
			dateVal = format.format(date);
		}
		return dateVal;
	}
	
	public static Date convertStringToDate(String str_date){
		if(str_date != null && str_date.length() == 19)
			return convertStringToDateWithTime(str_date);
		
		Date dt = null; 
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			if(str_date != null){
				dt = (Date)formatter.parse(str_date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		  return dt;
	}
	
	public static Date convertStringToDateWithTime(String str_date){
		Date dt = null; 
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		try {
			if(str_date != null){
				dt = (Date)formatter.parse(str_date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		  return dt;
	}
	
	public static Timestamp addTimeToDate(Date dt){
		Timestamp dateVal = null; 
		try {
			if(dt != null){
				long onedayminusonesec = (24*60*60*1000)-10; 
				dateVal = new Timestamp(dt.getTime()+onedayminusonesec);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return dateVal;
	}
	
	/**
	 * 
	 * @return the current date time with seconds and milliseconds rounded down
	 */
	public static Timestamp getCurrentDateWithMinutePrecision(){
		
		Calendar c = new GregorianCalendar();
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		Timestamp ts = new Timestamp(c.getTimeInMillis());
		
		return ts;
	}
	
}
