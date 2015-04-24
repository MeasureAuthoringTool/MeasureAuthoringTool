package mat.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dateutil {
	  public static void main(String[] av) {
		     String dateString = "12-06-0000";
		     DateFormat formatter ; 
		     Date date ;    
		   /*  try{
		              formatter = new SimpleDateFormat("MM-dd-yyyy");
		              date = (Date)formatter.parse("12-06-0000");  
		              System.out.println(date);
		              String s = formatter.format(date);
		              
		              DateFormat newFormatter = new SimpleDateFormat("yyyyMMdd");
		              String newString = newFormatter.format(date);
		              System.out.println("New date is " + newString );
		              
		              System.out.println("Today is " + s);
		    } catch (ParseException e){}
		    */
		     String monthText = dateString.substring(0, 2);
		     String dayText  = dateString.substring(3, 5);
		     String yearText  = dateString.substring(6);
		     String newDate = yearText+monthText+dayText;
		     System.out.println(newDate);

	  }
}
