package org.ifmc.mat.reports.dataaccess;

import org.ifmc.mat.reports.generator.ReportGenerator;
import org.junit.Before;
import org.junit.Test;


public class DataAccessTestCase {
	ReportGenerator reportGen = new ReportGenerator();
	
	@Before
	public void initDatabase(){
		reportGen.setDatabaseCreds("jdbc:mysql://ifdevdb01:3307/MAT_APP", "mat-dev", "***REMOVED***");
	}
	
   @Test
   public void testQDMDataElementsRetrieval(){
	   try{
		   reportGen.generateQDMElementsReport("2012-03-01", "2012-03-14");
	   }catch(Exception e){
		   System.out.println(e.getMessage());
			e.printStackTrace();
	   }
   }   
}
