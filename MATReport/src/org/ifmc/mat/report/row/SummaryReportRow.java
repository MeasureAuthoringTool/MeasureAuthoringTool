package mat.report.row;

import java.util.ArrayList;
import java.util.HashMap;

public class SummaryReportRow {
	
	private HashMap<String, Object> rowMap = null;
	
	public String emailAddress = null;
	public String organization = null;
	public String orgOid = null;
	
	public int noMeasures = 0;
	public int noComposite = 0;
	public int noCostRes = 0;
	public int noEfficiency = 0;
	public int noOutcome = 0;
	public int noPatientEE = 0;
	public int noProcess = 0;
	public int noStructure = 0;
	public int noContinous = 0;
	public int noProportion = 0;
	public int noRatio = 0;
	public int noValueSet = 0;
	
	public void buildRow(HashMap<String, Object> rowMap){
		this.rowMap = rowMap; 
		noMeasures++;			
		
		processUserInfo();
		
		processMeasureType();
		
		processMeasureScoring();
	}
	
	public void incrementValueSetCounter(){
		noValueSet++; 
	}
	
	private void processUserInfo(){
		emailAddress = (String) rowMap.get("emailAddress");
		organization = (String) rowMap.get("organization");
		orgOid = (String) rowMap.get("orgOid");
	}
	
	private void processMeasureType(){
		 ArrayList<String> measureTypes = (ArrayList<String>) rowMap.get("measureType");
		 for(String type: measureTypes){
			 if(type.equalsIgnoreCase("Composite")){
				 noComposite++;
			 }else if(type.equalsIgnoreCase("Cost/Resource Use")){
				noCostRes++; 
			 }else if(type.equalsIgnoreCase("Efficiency")){
				 noEfficiency++;
			 }else if(type.equalsIgnoreCase("Outcome")){
				 noOutcome++;
			 }else if(type.equalsIgnoreCase("Patient Engagement/Experience")){
				 noPatientEE++;
			 }else if(type.equalsIgnoreCase("Process")){
				 noProcess++;
			 }else if(type.equalsIgnoreCase("Structure")){
				 noStructure++;
			 }
		 }
	}
	
	private void processMeasureScoring(){
		 String scoring = (String) rowMap.get("scoring");

		 if(scoring.equalsIgnoreCase("Continuous Variable")){
			 noContinous++;
		 }else if(scoring.equalsIgnoreCase("Proportion")){
			noProportion++; 
		 }else if(scoring.equalsIgnoreCase("Ratio")){
				noRatio++; 
		 }
	}



}
