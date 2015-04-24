package MATReport.src.mat.report;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import MATReport.src.mat.report.generator.ReportGenerator;

public class ReportRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		System.out.println("Database Reporting Query Utility.");
		
		
		//Date functionality
		//Hitting enter twice makes it go from a week in the past to today
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		
		try{
			System.out.println("Please enter the database:{1:Prod, 2:Staging, 3:QA, 4:Dev, 5:UserInput}");
			String databaseChoice = reader.readLine();
			String jdbcURL = "";
			
			Integer dbCase = Integer.parseInt(databaseChoice);
			
			if(dbCase == null || dbCase < 1 || dbCase > 5){
				dbCase = 3;
			}
			switch(dbCase){
				case 1: jdbcURL ="jdbc:mysql://ifprddb01:3306/MAT_APP";
				break;
				case 2: jdbcURL ="jdbc:mysql://ifprddb01:3306/MAT_APP_STAGING";
				break;
				case 3: jdbcURL ="jdbc:mysql://iftstdb01:3306/MAT_APP";
				break;
				case 4: jdbcURL ="jdbc:mysql://ifdevdb01:3306/MAT_APP";
				break;
				case 5: 
					System.out.println("Please enter the database URL");
					jdbcURL = reader.readLine();
					break;
			}
			
			System.out.println("Please enter the database username:");
			String userName = reader.readLine();
			System.out.println("Please enter the database password:");
			String password = reader.readLine();
			
			ReportGenerator reportGen = new ReportGenerator();
			reportGen.setDatabaseCreds(jdbcURL, userName, password);
			
			
			System.out.println("Enter begin date(yyyy-MM-dd): ");
			String startDate = reader.readLine();
			
			if(startDate.equalsIgnoreCase("")){
				Calendar inst = Calendar.getInstance();
				inst.add(Calendar.DATE, -7);
				startDate =df.format(inst.getTime());
			}
			System.out.println("Enter ending date(yyyy-MM-dd): ");
			String endDate = reader.readLine();
			if(endDate.equalsIgnoreCase("")){
				endDate =df.format(Calendar.getInstance().getTime());
			}
			Date sd = df.parse(startDate);
			Date ed = df.parse(endDate);
			System.out.println("startDate "+df.format(sd));
			System.out.println("endDate "+df.format(ed));
			
			
			System.out.println("ABOUT TO CREATE SUMMARY OF USER ACCOUNTS");
			reportGen.genereateSummaryofAccounts(startDate, endDate);
			System.out.println("ABOUT TO CREATE SUMMARY OF MEASURES And CODE LIST");
			reportGen.generateSummaryReport(startDate, endDate);
			System.out.println("ABOUT TO CREATE SUMMARY OF MEASURE");
			reportGen.genereateSummaryofMeasures(startDate, endDate);
			System.out.println("ABOUT TO CREATE TRENDS OVER TIME");
			reportGen.genereateTrendsOverTime(startDate, endDate);
			System.out.println("ABOUT TO CREATE QDM");
			reportGen.generateQDMElementsReport(startDate, endDate);
			System.out.println("REPORT FINISHED");
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
