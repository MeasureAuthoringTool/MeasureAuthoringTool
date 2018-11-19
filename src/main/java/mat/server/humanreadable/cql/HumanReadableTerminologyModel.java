package mat.server.humanreadable.cql;

public interface HumanReadableTerminologyModel {
	String getTerminologyDisplay(); 
	String getDataCriteriaDisplay(); 
	void setDataCriteriaDisplay(String display);
	void setTerminologyDisplay(String display); 
}
