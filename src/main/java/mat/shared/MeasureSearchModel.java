package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.shared.SearchWidgetWithFilter;

import java.util.ArrayList;
import java.util.List;

public class MeasureSearchModel extends SearchModel implements IsSerializable {

	private Boolean omitCompositeMeasure;

	private String qdmVersion;
	private String measureSetId;
	private String cqlLibraryName;

	private List<String> scoringTypes;
	
	private PatientBasedType patientBasedType;

	private boolean isMatOnFhir;
	
	public enum PatientBasedType {ALL, PATIENT, NOT_PATIENT}
	
	public MeasureSearchModel() {
		this.searchTerm = "";
		this.cqlLibraryName = "";
		this.versionType = VersionType.ALL;
		this.scoringTypes = new ArrayList<>();
		this.patientBasedType = PatientBasedType.ALL;
		this.modifiedDate = 0;
		this.modifiedOwner = "";
		this.owner = "";
		this.startIndex = 1;
		this.pageSize = Integer.MAX_VALUE;
		this.isMyMeasureSearch = SearchWidgetWithFilter.MY;
	}

	public MeasureSearchModel(int myMeasureSearch, int startIndex, int pageSize, String searchTerm) {
		this();
		
		this.isMyMeasureSearch = myMeasureSearch;
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.searchTerm = searchTerm;
	}

	public PatientBasedType isPatientBased() {
		return patientBasedType;
	}
	public void setPatientBased(PatientBasedType patientBased) {
		patientBasedType = patientBased;
	}

	public boolean isMatOnFhir() {
		return isMatOnFhir;
	}
	public void setMatOnFhir(boolean isMatOnFhir) {
        this.isMatOnFhir = isMatOnFhir;
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

	public String getCqlLibraryName() {
		return cqlLibraryName;
	}

	public void setCqlLibraryName(String cqlLibraryName) {
		this.cqlLibraryName = trimWhiteSpaces(cqlLibraryName);
	}

	@Override
	public void reset() {
		super.reset();
		cqlLibraryName = "";
		scoringTypes = new ArrayList<>();
		patientBasedType = PatientBasedType.ALL;
	}

	@Override
	public String toString() {
		return "MeasureSearchModel{" +
				"omitCompositeMeasure=" + omitCompositeMeasure +
				", qdmVersion='" + qdmVersion + '\'' +
				", measureSetId='" + measureSetId + '\'' +
				", cqlLibraryName='" + cqlLibraryName + '\'' +
				", scoringTypes=" + scoringTypes +
				", patientBasedType=" + patientBasedType +
				", startIndex=" + startIndex +
				", pageSize=" + pageSize +
				", modifiedDate=" + modifiedDate +
				", isMyMeasureSearch=" + isMyMeasureSearch +
				", totalResults=" + totalResults +
				", searchTerm='" + searchTerm + '\'' +
				", modifiedOwner='" + modifiedOwner + '\'' +
				", owner='" + owner + '\'' +
				", modelType=" + modelType +
				", versionType=" + versionType +
				'}';
	}
}
