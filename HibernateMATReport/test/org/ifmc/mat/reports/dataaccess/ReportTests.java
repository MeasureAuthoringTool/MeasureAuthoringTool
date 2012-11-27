package org.ifmc.mat.reports.dataaccess;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ifmc.mat.reports.generator.ReportGenerator;
import org.junit.Before;
import org.junit.Test;


public class ReportTests {
	
	private DataAccess db = new DataAccess();
	
	//@Before
	//public void initDatabase(){
//		db.setDatabaseCreds("jdbc:mysql://ifdevdb01:3307/MAT_APP","mat-dev" ,"***REMOVED***" );
	//}
	
   @Test
   public void testGenerateSummaryReport(){
	   try{
		   ReportGenerator reportGen = new ReportGenerator();
		  // reportGen.setDatabaseCreds("jdbc:mysql://ifdevdb01:3307/MAT_APP","mat-dev" ,"***REMOVED***");
		  reportGen.setDatabaseCreds("jdbc:mysql://localhost:3306/MAT_APP","root" ,"mysql");
		   
		   DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		   String startDate = "2009-01-01";
		   String endDate = "2013-01-01";
		   Date sd = df.parse(startDate);
		   Date ed = df.parse(endDate);
		   System.out.println("startDate "+df.format(sd));
		   System.out.println("endDate "+df.format(ed));
		   System.out.println("ABOUT TO CREATE QDM");
	       reportGen.generateQDMElementsReport(startDate, endDate);
		   System.out.println("ABOUT TO CREATE SUMMARY OF USER ACCOUNTS");
		   reportGen.genereateSummaryofAccounts(startDate, endDate);
		   System.out.println("ABOUT TO CREATE SUMMARY OF MEASURES And CODE LIST");
		   reportGen.generateSummaryReport(startDate, endDate);
		   System.out.println("ABOUT TO CREATE SUMMARY OF MEASURE");
		   reportGen.genereateSummaryofMeasures(startDate, endDate);
		   System.out.println("ABOUT TO CREATE TRENDS OVER TIME");
		   reportGen.genereateTrendsOverTime(startDate, endDate);
		   System.out.println("REPORT FINISHED");
		   
		}catch(Exception e){
		   System.out.println(e.getMessage());
			e.printStackTrace();
	   }
	   
	   
   } 
   
}
