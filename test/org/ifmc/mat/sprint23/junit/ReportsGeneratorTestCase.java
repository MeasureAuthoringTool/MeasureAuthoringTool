package org.ifmc.mat.sprint23.junit;

import mat.reports.generator.ReportGenerator;

import org.junit.Before;
import org.junit.Test;

public class ReportsGeneratorTestCase {
	ReportGenerator reportGen = new ReportGenerator();
	
	@Before
	public void initDatabase(){
		reportGen.setDatabaseCreds("jdbc:mysql://ifdevdb01:3307/MAT_APP", "mat-dev", "***REMOVED***");
	}
	
	/*Note:-
	 * Don't add this as part of quickbuild, every time the war file is building this junit will run and 
	 * generate xls files in the dev server.
	 */
	 @Test
	 public void testReportGeneration(){
	   try{
		   reportGen.generateQDMElementsReport("2012-03-01", "2012-03-14");
		   reportGen.generateSummaryReport("2012-03-01", "2012-03-14");
		   reportGen.genereateSummaryofAccounts("2012-03-01", "2012-03-14");
		   reportGen.genereateSummaryofMeasures("2012-03-01", "2012-03-14");
		   reportGen.genereateTrendsOverTime("2012-03-01", "2012-03-14");
	   }catch(Exception e){
		   System.out.println(e.getMessage());
			e.printStackTrace();
	   }
	 } 
		
}
