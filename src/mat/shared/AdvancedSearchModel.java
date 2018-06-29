package mat.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AdvancedSearchModel implements IsSerializable{
	
	private Boolean advanceSearch;
	private String searchTerm;
	private Boolean isDraft;
	private List<String> scoringTypes;
	private Boolean isPatientBased;
	private int modifiedDate;
	private String modifiedOwner;
	private String owner;
	private int startIndex;
	private int pageSize;
	private int isMyMeasureSearch;
	private String lastSearchText;
	
	
	public final static String ONLY_MY_MEASURE = "Only My Measures";
	public final static int MY_MEASURES = 0;
	public final static int ALL_MEASURES = 1;
	public final static String VERSION_MEASURE = "Versioned Measures";
	public final static String DRAFT_MEASURE ="Draft Measures";
	public final static String PATIENT_BASED = "Yes, Patient-based";
	public final static String NOT_PATIENT_BASED = "No, Not Patient-based";
	
	
	public AdvancedSearchModel() {
		
	}
	
	public AdvancedSearchModel(int myMeasureSearch,int startIndex, int pageSize, String lastSearchText, String searchTerm) {
		this.isMyMeasureSearch =myMeasureSearch;
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.lastSearchText = lastSearchText;
		this.searchTerm = searchTerm;
		
	}
	
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public Boolean isDraft() {
		return isDraft;
	}
	public void setIsDraft(Boolean type) {
		this.isDraft = type;
	}
	public Boolean isPatientBased() {
		return isPatientBased;
	}
	public void setPatientBased(Boolean patientBased) {
		isPatientBased = patientBased;
	}
	public int getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(int modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedOwner() {
		return modifiedOwner;
	}
	public void setModifiedOwner(String modifiedOwner) {
		this.modifiedOwner = modifiedOwner;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int isMyMeasureSearch() {
		return isMyMeasureSearch;
	}
	public void setIsMyMeasureSearch(int filter) {
		this.isMyMeasureSearch = filter;
	}
	public String getLastSearchText() {
		return lastSearchText;
	}
	public void setLastSearchText(String lastSearchText) {
		this.lastSearchText = lastSearchText;
	}

	public List<String> getScoringTypes() {
		return scoringTypes;
	}

	public void setScoringTypes(List<String> scoringTypes) {
		this.scoringTypes = scoringTypes;
	}
	
	public Boolean getAdvanceSearch() {
		return advanceSearch;
	}
	
	public void setAdvanceSearch(Boolean advanceSearch) {
		this.advanceSearch = advanceSearch;
	}
	
}
