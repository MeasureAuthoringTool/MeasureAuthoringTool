package mat.shared;

/**
 * The Class DateStringValidator.
 */
public class DateStringValidator {
	/* TODO add static validation codes */
	/** The valid. */
	public static int VALID = 0;
	
	/** The invalid month. */
	public static int INVALID_MONTH = 1;
	
	/** The invalid day. */
	public static int INVALID_DAY = 2;
	
	/** The invalid year. */
	public static int INVALID_YEAR = 3;
	
	/** The invalid hour. */
	public static int INVALID_HOUR = 4;
	
	/** The invalid minute. */
	public static int INVALID_MINUTE = 5;
	
	/** The invalid am pm. */
	public static int INVALID_AM_PM = 6;
	
	/** The invalid date. */
	public static int INVALID_DATE = 7;
	
	/**
	 * Checks if is valid date string.
	 * 
	 * @param lmStr
	 *            the lm str
	 * @return the int
	 */
	public int isValidDateString(String lmStr){
		
		int dateLen = lmStr.length();
		//validate length of date
		if(dateLen != 10 && dateLen != 19){
			return INVALID_DATE;
		}
		
		try{
			//MM-dd-yyyy
			int month = Integer.parseInt(lmStr.substring(0, 2));
			if(month > 12)
				return INVALID_MONTH;
		}catch(NumberFormatException e){
			return INVALID_MONTH;
		}
		
		try{
			int day = Integer.parseInt(lmStr.substring(3, 5));
			//let the date format exception deal with more specific scenarios:: ex. 30 days in Feb
			if(day > 31)
				return INVALID_DAY;
		}catch(NumberFormatException e){
			return INVALID_DAY;
		}
		
		try{	
			int year = Integer.parseInt(lmStr.substring(6, 10));
			//more than an int can handle in milliseconds
			if(year < 1970 || year > 2037)
				return INVALID_YEAR;
		}catch(NumberFormatException e){
			return INVALID_YEAR;
		}
			
		//MM-dd-yyyy hh:mm aa
		if(dateLen > 10){
			
			try{
				int hour = Integer.parseInt(lmStr.substring(11, 13));
				if(hour > 12)
					return INVALID_HOUR;
			}catch(NumberFormatException e){
				return INVALID_HOUR;
			}
			
			try{
				int minute = Integer.parseInt(lmStr.substring(14, 16));
				if(minute > 59)
					return INVALID_MINUTE;
			}catch(NumberFormatException e){
				return INVALID_MINUTE;
			}
			
			String aa = lmStr.substring(17);
			if(!aa.equalsIgnoreCase("am") && !aa.equalsIgnoreCase("pm"))
				return INVALID_AM_PM;
		}
		
		return VALID;
	}
}
