package mat.client.advancedSearch;

import java.util.ArrayList;
import java.util.List;

import mat.shared.AdvancedSearchModel;
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
		AdvancedSearchModel model = new AdvancedSearchModel();
		
		model.setSearchTerm(getModal().getSearchText().getValue());
		if(getModal().getSearchBoxList().getSelectedValue().equals(AdvancedSearchModel.ONLY_MY_MEASURE)) {
			model.setIsMyMeasureSearch(AdvancedSearchModel.MY_MEASURES);
		} else {
			model.setIsMyMeasureSearch(AdvancedSearchModel.ALL_MEASURES);
		}
		model.setOwner(getModal().getSearchBoxList().getSelectedValue());
		if(getModal().getSearchStateList().getSelectedValue().equals(AdvancedSearchModel.VERSION_MEASURE)) {
			model.setIsDraft(false);
		}
		else if(getModal().getSearchStateList().getSelectedValue().equals(AdvancedSearchModel.DRAFT_MEASURE)) {
			model.setIsDraft(true);
		}
		List<String> scoring = new ArrayList<String>();
		if(getModal().getCohortCheckbox().getValue()) {
			scoring.add(MatConstants.COHORT);
		}
		if(getModal().getContVariableCheckbox().getValue()) {
			scoring.add(MatConstants.CONTINUOUS_VARIABLE);
		}
		if(getModal().getProportionCheckbox().getValue()) {
			scoring.add(MatConstants.PROPORTION);
		}
		if(getModal().getRatioCheckbox().getValue()) {
			scoring.add(MatConstants.RATIO);
		}
		model.setScoringTypes(scoring);
		if((getModal().getPatientIndecatorList().getSelectedValue().equals(AdvancedSearchModel.PATIENT_BASED))){
			model.setPatientBased(true);
		}
		else if((getModal().getPatientIndecatorList().getSelectedValue().equals(AdvancedSearchModel.NOT_PATIENT_BASED))){
			model.setPatientBased(false);
		}
		else {
			model.setPatientBased(null);
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
		model.setModifiedDate(time);
		model.setModifiedOwner(getModal().getModifiedBy().getValue());
		model.setOwner(getModal().getOwnedBy().getValue());
		model.setAdvanceSearch(true);
		
		return model;
	}
}
