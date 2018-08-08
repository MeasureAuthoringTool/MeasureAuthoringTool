package mat.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureSearchModel implements IsSerializable {
	private String searchTerm;
	private VersionMeasureType versionMeasureType;
	private List<String> scoringTypes;
	private PatientBasedType patientBasedType;
	private int modifiedDate;
	private String modifiedOwner;
	private String owner;
	private int startIndex;
	private int pageSize;
	private int isMyMeasureSearch;
	private String lastSearchText;
	private Boolean omitCompositeMeasure;
	private String qdmVersion;
	private String measureSetId;
	public final static String ONLY_MY_MEASURE = "Only My Measures";
	public final static int MY_MEASURES = 0;
	public final static int ALL_MEASURES = 1;
	public final static String VERSION_MEASURE = "Versioned Measures";
	public final static String DRAFT_MEASURE ="Draft Measures";
	public final static String PATIENT_BASED = "Yes, Patient-based";
	public final static String NOT_PATIENT_BASED = "No, Not Patient-based";
	
	public enum VersionMeasureType {ALL, VERSION, DRAFT};
	
	public enum PatientBasedType {ALL, PATIENT, NOT_PATIENT};
	
	public MeasureSearchModel() {
		this.searchTerm = "";
		this.versionMeasureType = VersionMeasureType.ALL;
		this.scoringTypes = new ArrayList<String>();
		this.patientBasedType = PatientBasedType.ALL;
		this.modifiedDate = 0;
		this.modifiedOwner = "";
		this.owner = "";
		this.startIndex = 1;
		this.pageSize = Integer.MAX_VALUE;
		this.isMyMeasureSearch = MY_MEASURES;
	}
	
	public MeasureSearchModel(String searchterm, VersionMeasureType versionMeasureType, List<String> scoringTypes, PatientBasedType patientBasedType,
			int modifiedDate, String modifiedOwner, String owner, int startIndex, int pageSize, int isMyMeasureSearch, String lastSearchText) {
		
		this.searchTerm = searchterm;
		this.versionMeasureType = versionMeasureType;
		this.scoringTypes = scoringTypes;
		this.patientBasedType = patientBasedType;
		this.modifiedDate = modifiedDate;
		this.modifiedOwner = modifiedOwner;
		this.owner = owner;
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.isMyMeasureSearch = isMyMeasureSearch;
		this.lastSearchText = lastSearchText;
	}

	public MeasureSearchModel(int myMeasureSearch, int startIndex, int pageSize, String lastSearchText, String searchTerm) {
		this();
		
		this.isMyMeasureSearch = myMeasureSearch;
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
	public VersionMeasureType isDraft() {
		return versionMeasureType;
	}
	public void setIsDraft(VersionMeasureType version) {
		this.versionMeasureType = version;
	}
	public PatientBasedType isPatientBased() {
		return patientBasedType;
	}
	public void setPatientBased(PatientBasedType patientBased) {
		patientBasedType = patientBased;
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

	public String getQdmVersion() {
		return qdmVersion;
	}

	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}
	
	public Boolean isOmitCompositeMeasure() {
		return omitCompositeMeasure;
	}

	public void setOmitCompositeMeasure(Boolean omitCompositeMeasure) {
		this.omitCompositeMeasure = omitCompositeMeasure;
	}
	
	public String getMeasureSetId() {
		return measureSetId;
	}

	public void setMeasureSetId(String measureSetId) {
		this.measureSetId = measureSetId;
	}
}
