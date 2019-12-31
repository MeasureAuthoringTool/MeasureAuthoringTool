package mat.shared;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * The Class DateUtility.
 */
public class DateUtility {

	/**
	 * Convert date to string.
	 *
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String convertDateToString(Date date){
		String dateVal = "";
		if(date != null){
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			dateVal = format.format(date);
		}
		return dateVal;
	}

	/**
	 * Convert date to string no time.
	 *
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String convertDateToStringNoTime(Date date){
		String dateVal = "";
		if(date != null){
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			dateVal = format.format(date);
		}
		return dateVal;
	}

	/**
	 * Convert date to string no time2.
	 *
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String convertDateToStringNoTime2(Date date){
		String dateVal = "";
		if(date != null){
			SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
			dateVal = format.format(date);
		}
		return dateVal;
	}

	/**
	 * Convert string to date.
	 *
	 * @param str_date
	 *            the str_date
	 * @return the date
	 */
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

	/**
	 * Convert string to date with time.
	 *
	 * @param str_date
	 *            the str_date
	 * @return the date
	 */
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

	/**
	 * Adds the time to date.
	 *
	 * @param dt
	 *            the dt
	 * @return the timestamp
	 */
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
	 * Gets the current date with minute precision.
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

	/**
	 * This method formats the Instant based on provided format and returns the string representation
	 *
	 * e.g. for "dd-MMM-YYYY" format value is 23-Dec-2019
	 * for "hh:mm aa" format value is 02:02 PM
	 *
	 * @param instant-> instance of java.time.Instant
	 * @return string formatted date or time
	 */
	public static String formatInstant(Instant instant, String format) {
		if (instant == null || format == null) {
			return "";
		}

		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = Date.from(instant);
		return dateFormat.format(date);
	}
}
