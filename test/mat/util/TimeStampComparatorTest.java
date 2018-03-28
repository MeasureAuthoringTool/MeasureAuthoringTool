package mat.util;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.TestCase;

public class TimeStampComparatorTest extends TestCase {
	
	public void testTimeStampComparison(){
		Date currentDate1 = new Date();

		System.out.println("Current date "+currentDate1 );
		
		Timestamp currentTimeStamp1 = new Timestamp(currentDate1.getTime());
		
		System.out.println("currentTimeStamp1"+ currentTimeStamp1.getTime());
		
		
		
		try {

		Thread.sleep(100000);

		} catch (InterruptedException e) {

		// TODO Auto-generated catch block

		e.printStackTrace();

		}

		Date currentDate2 = new Date();

		System.out.println("stamp1"+currentDate2 );
		
		Timestamp currentTimeStamp2 = new Timestamp(currentDate2.getTime());
		
		System.out.println("currentTimeStamp2" + currentTimeStamp2.getTime());
		
		
		
		
		long timediff = currentTimeStamp2.getTime() - currentTimeStamp1.getTime();
		System.out.println("TimeStamp Minutes difference "+timediff/(60 * 1000) );

	}
	
	
	
}
