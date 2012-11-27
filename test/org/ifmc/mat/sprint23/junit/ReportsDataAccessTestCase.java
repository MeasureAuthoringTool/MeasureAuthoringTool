package org.ifmc.mat.sprint23.junit;



import mat.reports.dataaccess.DataAccess;

import org.junit.Before;
import org.junit.Test;


public class ReportsDataAccessTestCase {
	
	DataAccess dataAccess = new DataAccess();
	
	@Before
	public void initDatabase(){
		dataAccess.setDatabaseCreds("jdbc:mysql://ifdevdb01:3307/MAT_APP", "mat-dev", "***REMOVED***");
	}
	
  
	@Test
	 public void testQDMDataElementsRetrieval(){
		   try{
			   dataAccess.retrieveQDMElements("2012-03-01", "2012-03-14");
			   System.out.println("QDM Element Data Retrieval is Successful");
		   }catch(Exception e){
			   System.out.println(e.getMessage());
			   e.printStackTrace();
		   }
	}
	
	@Test
	 public void testSummaryReportDataRetrieval(){
		   try{
			   dataAccess.retrieveSummaryReportData("2012-03-01", "2012-03-14");
			   System.out.println("Summary Report Data Retrieval is Successful");
		   }catch(Exception e){
			   System.out.println(e.getMessage());
			   e.printStackTrace();
		   }
	}
	
	@Test
	 public void testSummaryofMeasuresDataRetrieval(){
		   try{
			   dataAccess.runSummaryOfMeasuresReport("2012-03-01", "2012-03-14");
			   System.out.println("Summary Of Measures Data Retrieval is Successful");
		   }catch(Exception e){
			   System.out.println(e.getMessage());
			   e.printStackTrace();
		   }
	}
	
	@Test
	 public void testSummaryofAccountsDataRetrieval(){
		   try{
			   dataAccess.runSummaryOfAccountsReport("2012-03-01", "2012-03-14");
			   System.out.println("Summary Of Accounts Data Retrieval is Successful");
		   }catch(Exception e){
			   System.out.println(e.getMessage());
			   e.printStackTrace();
		   }
	}
	
	@Test
	 public void testSummaryofTrendsRetrieval(){
		   try{
			   dataAccess.runSummaryOfTrendsOverTimeReport("2012-03-01", "2012-03-14");
			   System.out.println("Summary Of Trends OverTime Data Retrieval is Successful");
		   }catch(Exception e){
			   System.out.println(e.getMessage());
			   e.printStackTrace();
		   }
	}
}
