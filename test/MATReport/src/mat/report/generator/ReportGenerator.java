package MATReport.src.mat.report.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import MATReport.src.mat.report.common.CommonConstants;
import MATReport.src.mat.report.dataaccess.DataAccess;
import MATReport.src.mat.report.row.SummaryReportRow;

public class ReportGenerator {
	private DataAccess dataAccess = new DataAccess();
	private ExcelWorkBookGenerator excelWrkbkGenerator = new ExcelWorkBookGenerator();
	
	public void setDatabaseCreds(String jdbcURL, String userName, String password){
		dataAccess.setDatabaseCreds(jdbcURL, userName, password);
	}
	
	public void generateSummaryReport(String startDate, String endDate){
		
		LinkedHashMap<String, Object> data = dataAccess.retrieveSummaryReportData(startDate, endDate);
		
		//apply rules and logic
		ArrayList<String> users = new ArrayList<String>();
		
		ArrayList<LinkedHashMap<String, Object>> measureRows = (ArrayList<LinkedHashMap<String, Object>>) data.get("measuresList");
		for(LinkedHashMap<String, Object> measureRow : measureRows){
			String emailAddress = (String) measureRow.get("userId"); 
			if(users.indexOf(emailAddress) == -1){
				users.add(emailAddress);
			}
		}

		ArrayList<LinkedHashMap<String, Object>> vSetRows = (ArrayList<LinkedHashMap<String, Object>>) data.get("valueSetList");
		for(LinkedHashMap<String, Object> vsetRow : vSetRows){
			String userId = (String) vsetRow.get("userId"); 
			if(users.indexOf(userId) == -1){
				users.add(userId);
			}
		}
		
		//for each email address, form a report row
		ArrayList<LinkedHashMap<String, String>> reportRows = new ArrayList<LinkedHashMap<String,String>>();
		for(String userId : users){
			
			LinkedHashMap<String, String> userMap = new LinkedHashMap<String, String>();
			
			SummaryReportRow row = new SummaryReportRow();
			
			for(LinkedHashMap<String, Object> measureRow : measureRows){				
				String msrUserId = (String) measureRow.get("userId");
				if(userId != null && userId.equalsIgnoreCase(msrUserId)){
					row.buildRow(measureRow);
				}
			}
			
			for(LinkedHashMap<String, Object> vsetRow : vSetRows){				
				String vSetUserId = (String) vsetRow.get("userId");
				if(userId != null && userId.equalsIgnoreCase(vSetUserId)){
					row.incrementValueSetCounter();
				}
			}
			

			
			userMap.put(CommonConstants.USER_ID, row.emailAddress);
			userMap.put(CommonConstants.ORGANIZATION, row.organization);
			userMap.put(CommonConstants.ORGANIZATION_OID, row.orgOid);
			userMap.put(CommonConstants.NO_MEASURES_CREATED, String.valueOf(row.noMeasures));
			userMap.put(CommonConstants.NO_COMPOSITE_MEASURES, String.valueOf(row.noComposite));
			userMap.put(CommonConstants.NO_COST_RES_MEASURES, String.valueOf(row.noCostRes));
			userMap.put(CommonConstants.NO_EFFICIENCY_MEASURES, String.valueOf(row.noEfficiency));
			userMap.put(CommonConstants.NO_OUTCOME_MEASURES, String.valueOf(row.noOutcome));
			userMap.put(CommonConstants.NO_PATIENT_EE_MEASURES, String.valueOf(row.noPatientEE));
			userMap.put(CommonConstants.NO_PROCESS_MEASURES, String.valueOf(row.noProcess));
			userMap.put(CommonConstants.NO_STRUCTURE_MEASURES, String.valueOf(row.noStructure));
			userMap.put(CommonConstants.NO_CONTINOUS_MEASURES, String.valueOf(row.noContinous));
			userMap.put(CommonConstants.NO_PROPORTION_MEASURES, String.valueOf(row.noProportion));
			userMap.put(CommonConstants.NO_RATIO_MEASURES, String.valueOf(row.noRatio));
			userMap.put(CommonConstants.NO_VALUE_SETS_CREATED, String.valueOf(row.noValueSet));
			//Don't output the default value sets and measures which have no user account
			//tied to them.
			if(row.emailAddress!=null)
				reportRows.add(userMap);
		}
		
		excelWrkbkGenerator.createSummaryWorkSheet("3-Summary of Meas. & CL by User", reportRows, getSummaryReportHeaders());
		System.out.println("Done Summary of Measures And CodeList Report");
	}
	
	private ArrayList<String> getSummaryReportHeaders(){
		ArrayList<String> summaryHeader = new ArrayList<String>();
		summaryHeader.add(CommonConstants.USER_ID);
		summaryHeader.add(CommonConstants.ORGANIZATION);
		summaryHeader.add(CommonConstants.ORGANIZATION_OID);
		summaryHeader.add(CommonConstants.NO_MEASURES_CREATED);
		summaryHeader.add(CommonConstants.NO_COMPOSITE_MEASURES);
		summaryHeader.add(CommonConstants.NO_COST_RES_MEASURES);
		summaryHeader.add(CommonConstants.NO_EFFICIENCY_MEASURES);
		summaryHeader.add(CommonConstants.NO_OUTCOME_MEASURES);
		summaryHeader.add(CommonConstants.NO_PATIENT_EE_MEASURES);
		summaryHeader.add(CommonConstants.NO_PROCESS_MEASURES);
		summaryHeader.add(CommonConstants.NO_STRUCTURE_MEASURES);
		summaryHeader.add(CommonConstants.NO_CONTINOUS_MEASURES);
		summaryHeader.add(CommonConstants.NO_PROPORTION_MEASURES);
		summaryHeader.add(CommonConstants.NO_RATIO_MEASURES);
		summaryHeader.add(CommonConstants.NO_VALUE_SETS_CREATED);
		
		return summaryHeader;
		
	}
	
	@SuppressWarnings("unchecked")
	public void generateQDMElementsReport(String startDate, String endDate){
		
		LinkedHashMap<String, Object> data = dataAccess.retrieveQDMElementsData(startDate, endDate);
		
		
		
		ArrayList<LinkedHashMap<String, LinkedHashMap<String, Object>>> qdmRows = (ArrayList<LinkedHashMap<String, LinkedHashMap<String, Object>>>) data.get("qdmList");
		LinkedHashMap<String, ArrayList<String>> msrRows = (LinkedHashMap<String, ArrayList<String>>) data.get("msrList");
		
		Iterator<LinkedHashMap<String, LinkedHashMap<String, Object>>> qdmRowsItr = qdmRows.iterator();
		
		ArrayList<LinkedHashMap<String, Object>> reportData = new ArrayList<LinkedHashMap<String,Object>>();
		
		while(qdmRowsItr.hasNext()){
			LinkedHashMap<String, LinkedHashMap<String, Object>> catMap = qdmRowsItr.next();
			Iterator<Map.Entry<String, LinkedHashMap<String, Object>>> catMapItr = catMap.entrySet().iterator();			
			while(catMapItr.hasNext()){
				ArrayList<String> measures = new ArrayList<String>();
				Map.Entry<String, LinkedHashMap<String, Object>> entry = catMapItr.next();
				String key = entry.getKey();

				LinkedHashMap<String, Object> value = entry.getValue();				
				//check out measure
				Iterator<Map.Entry<String, ArrayList<String>>> msrRowsItr = msrRows.entrySet().iterator();
				while(msrRowsItr.hasNext()){
					Map.Entry<String, ArrayList<String>> entryMsr = msrRowsItr.next();
					if(entryMsr.getValue().indexOf(key) != -1){
						measures.add(entryMsr.getKey());
					}
				}
				value.put("msrList", measures);
				reportData.add(value);
			}
		}
		
		excelWrkbkGenerator.createQDMWorkSheet("5-QDM Elements", reportData, getQDMElementsHeaders(msrRows.keySet()));
		System.out.println("DONE QDM Report");
	}
	
	private ArrayList<String> getQDMElementsHeaders(Set<String> measures){
		ArrayList<String> qdmHeader = new ArrayList<String>();
		qdmHeader.add(CommonConstants.CATEGORY);
		qdmHeader.add(CommonConstants.DATATYPE);
		qdmHeader.add(CommonConstants.ATTRIBUTE);
		
		for(String measureName: measures){
			qdmHeader.add(measureName);
		}
		
		qdmHeader.add(CommonConstants.TOT_FOR_MSRS_DISPLAYED);
		
		return qdmHeader;
	}

	public void genereateSummaryofMeasures(String startDate,String endDate){
		//Accessing data for Measures Summary
		HashMap<String, HashMap<String, Object>> measuresData = dataAccess.runSummaryOfMeasuresReport(startDate, endDate);
		ArrayList<String> headerList = new ArrayList<String>();
		headerList.add(CommonConstants.EMEASURE_NAME);
		headerList.add(CommonConstants.MAT_IDENTIFIER);
		headerList.add(CommonConstants.VERSION);
		headerList.add(CommonConstants.MEASURE_STEWARD);
		headerList.add(CommonConstants.ENDORSED);
		headerList.add(CommonConstants.DESCRIPTION);
		headerList.add(CommonConstants.MEASURE_SCORING);
		headerList.add(CommonConstants.MEASURE_TYPE);
		headerList.add(CommonConstants.STATUS);
		headerList.add(CommonConstants.CREATION_DATE_TIME);
		headerList.add(CommonConstants.PACKAGE_DATE_TIME);
		excelWrkbkGenerator.createWorkSheet("4-Summary of Measures", measuresData,headerList);
		System.out.println("DONE Summary of Measures Report");
	}
	
	public void genereateSummaryofAccounts(String startDate,String endDate){
	
		HashMap<String, HashMap<String, Object>> measuresData = dataAccess.runSummaryOfAccountsReport(startDate, endDate);
		ArrayList<String> headerList = new ArrayList<String>();
		headerList.add("First Name");
		headerList.add("Last Name");
		headerList.add("Email");
		headerList.add("Organization");
		headerList.add("Organization OID");
		headerList.add("Date Account Created");
		headerList.add("Last Date User Accessed MAT");
		headerList.add("# of Measures Created");
		headerList.add("# of Value Sets Created");
		headerList.add("Account Type");
		
		HSSFSheet sheet = excelWrkbkGenerator.initWorkSheet("1-Summary of User Accounts", headerList);
		
		headerList = new ArrayList<String>();
		headerList.add("FIRST_NAME");
		headerList.add("LAST_NAME");
		headerList.add("EMAIL_ADDRESS");
		headerList.add("ORGANIZATION_NAME");
		headerList.add("ORG_OID");
		headerList.add("ACTIVATION_DATE");
		headerList.add("SIGN_IN_DATE");
		headerList.add("MEASURES_CREATED");
		headerList.add("VALUE_SETS_CREATED");
		headerList.add("SECURITY_ROLE_ID");
		
		excelWrkbkGenerator.fillInSheet(sheet, measuresData, headerList);	
		System.out.println("DONE Summary of Accounts Report");
	}
	public void genereateTrendsOverTime(String startDate,String endDate){
		
		HashMap<String,Object> measuresData = dataAccess.runSummaryOfTrendsOverTimeReport(startDate, endDate);
		
		
		ArrayList<String> headerList = new ArrayList<String>();
		headerList.add("");
		headerList.add("# of New User Accounts Created");
		headerList.add("# of Unique Measures in a 'Complete' Status");
		headerList.add("# of Unique Organization OIDs in the Tool");
		HSSFSheet sheet = excelWrkbkGenerator.initWorkSheet("6-Trends over Time",headerList);
		//measuresData.put("Week, value)
		excelWrkbkGenerator.fillInTrendSheet(sheet, measuresData, headerList);
		System.out.println("DONE Trends Over Time Report");
	}
	
	
}
