package mat.client.advancedSearch;

import java.util.ArrayList;
import java.util.List;

import mat.shared.AdvancedSearchModel;
import mat.shared.AdvancedSearchModel.PatientBasedType;
import mat.shared.AdvancedSearchModel.VersionMeasureType;
import mat.shared.MatConstants;

public class MeasureLibraryAdvancedSearchBuilder extends AdvancedSearchBuilder {
	
	private final String DESCRIPTION_OF_MODAL_FOR_508 = "This advanced search section allows you to "
			+ "search by the name of the measure, measure state, measure score, "
			+ "patient-based indicator, when the measure was last modified, and by the user that"
			+ " last modified the measure, and the measure owner. ";
			
	public void createAdvancedSearch() {
		setModal(new AdvancedSearchModal("Measure", "Measures"));
		getModal().addSearchTextSection();
		getModal().addSearchBySection();
		getModal().addStateSection();
		getModal().addScoreSection();
		getModal().addPatientSection();
		getModal().addDaysSection();
		getModal().addModifiedBySection();
		getModal().addOwnedBySection();
		getModal().addButtonSection();
		getModal().setHelpBlock(DESCRIPTION_OF_MODAL_FOR_508);
	}

	@Override
	public AdvancedSearchModel generateAdvancedSearchModel() {
		
		String searchTerm = getModal().getSearchText().getValue();
		int isMyMeasureIndicator = 0;
		if(getModal().getSearchBoxList().getSelectedValue().equals(AdvancedSearchModel.ONLY_MY_MEASURE)) {
			isMyMeasureIndicator = AdvancedSearchModel.MY_MEASURES;
		} else {
			isMyMeasureIndicator = AdvancedSearchModel.ALL_MEASURES;
		}
		VersionMeasureType versionMeasureType;
		if(getModal().getSearchStateList().getSelectedValue().equals(AdvancedSearchModel.VERSION_MEASURE)) {
			versionMeasureType = AdvancedSearchModel.VersionMeasureType.VERSION;
		}
		else if(getModal().getSearchStateList().getSelectedValue().equals(AdvancedSearchModel.DRAFT_MEASURE)) {
			versionMeasureType = AdvancedSearchModel.VersionMeasureType.DRAFT;
		} else {
			versionMeasureType = AdvancedSearchModel.VersionMeasureType.ALL;
		}
		List<String> scoring = new ArrayList<String>();
		if(getModal().getCohortCheckbox().getValue()) {
			scoring.add(MatConstants.COHORT.toLowerCase());
		}
		if(getModal().getContVariableCheckbox().getValue()) {
			scoring.add(MatConstants.CONTINUOUS_VARIABLE.toLowerCase());
		}
		if(getModal().getProportionCheckbox().getValue()) {
			scoring.add(MatConstants.PROPORTION.toLowerCase());
		}
		if(getModal().getRatioCheckbox().getValue()) {
			scoring.add(MatConstants.RATIO.toLowerCase());
		}
		PatientBasedType patientBasedType;
		if((getModal().getPatientIndecatorList().getSelectedValue().equals(AdvancedSearchModel.PATIENT_BASED))){
			patientBasedType = AdvancedSearchModel.PatientBasedType.PATIENT;
		}
		else if((getModal().getPatientIndecatorList().getSelectedValue().equals(AdvancedSearchModel.NOT_PATIENT_BASED))){
			patientBasedType = AdvancedSearchModel.PatientBasedType.NOT_PATIENT;
		}
		else {
			patientBasedType = AdvancedSearchModel.PatientBasedType.ALL;
		}
		
		String stringTime = getModal().getModifiedOnList().getSelectedValue();
		int time = 0;
		if(stringTime.contains("14")) {
			time = 14;
		}
		else if(stringTime.contains("30")) {
			time = 30;
		}
		else if(stringTime.contains("60")) {
			time = 60;
		}
		else if(stringTime.contains("90")) {
			time = 90;
		}
		
		String modifiedBy = getModal().getModifiedBy().getValue();
		String ownedBy = getModal().getOwnedBy().getValue();
		
		AdvancedSearchModel model = new AdvancedSearchModel(searchTerm, versionMeasureType, scoring, patientBasedType, time, modifiedBy, ownedBy, 1, Integer.MAX_VALUE, isMyMeasureIndicator, searchTerm);
		
		return model;
	}
}
