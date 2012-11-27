package MATReport.src.org.ifmc.mat.report.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportingUtil {

	public String getStartDateForLastWeek(int startDay){
		Calendar c = Calendar.getInstance();  

		c.set(Calendar.DAY_OF_WEEK, startDay);  

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 

		c.add(Calendar.DATE, -7);
		return df.format(c.getTime());
	}
	
	
	public String getEndDateForLastWeek(int endDay){
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		//The default calendar end date is Saturday. so, 
		//if Sunday then no need to go back to the previous week.
		if(endDay != Calendar.SUNDAY){ 
			c.add(Calendar.DATE, -7);
		}
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return df.format(c.getTime());
	}
}
