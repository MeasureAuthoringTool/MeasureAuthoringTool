package mat.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AdvancedSearchModel implements IsSerializable{
	
	private Boolean advanceSearch;
	private String searchTerm;
	private Boolean type;
	private List<String> scoringTypes;
	private Boolean PatientBased;
	private String modifiedDate;
	private String modifiedOwner;
	private String owner;
	private int startIndex;
	private int pageSize;
	private int filter;
	private String lastSearchText;
	
	public AdvancedSearchModel() {
		
	}
	
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public Boolean getType() {
		return type;
	}
	public void setType(Boolean type) {
		this.type = type;
	}
	public Boolean getPatientBased() {
		return PatientBased;
	}
	public void setPatientBased(Boolean patientBased) {
		PatientBased = patientBased;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
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
	public int getFilter() {
		return filter;
	}
	public void setFilter(int filter) {
		this.filter = filter;
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
