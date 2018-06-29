package mat.client.advancedSearch;

import java.util.ArrayList;
import java.util.List;

import mat.client.measure.MeasureSearchFilterPanel;
import mat.shared.AdvancedSearchModel;

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
		if(getModal().getSearchBoxList().getSelectedValue().equals("Only My Measures")) {
			model.setFilter(MeasureSearchFilterPanel.MY_MEASURES);
		} else {
			model.setFilter(MeasureSearchFilterPanel.ALL_MEASURES);
		}
		model.setOwner(getModal().getSearchBoxList().getSelectedValue());
		if(getModal().getSearchStateList().getSelectedValue().equals("Versioned Measures")) {
			model.setType(false);
		}
		else if(getModal().getSearchStateList().getSelectedValue().equals("Draft Measures")) {
			model.setType(true);
		}
		List<String> scoring = new ArrayList<String>();
		if(getModal().getCohortCheckbox().getValue()) {
			scoring.add("Cohort");
		}
		if(getModal().getContVariableCheckbox().getValue()) {
			scoring.add("Continuous Variable");
		}
		if(getModal().getProportionCheckbox().getValue()) {
			scoring.add("Proportion");
		}
		if(getModal().getRatioCheckbox().getValue()) {
			scoring.add("Ratio");
		}
		model.setScoringTypes(scoring);
		if((getModal().getPatientIndecatorList().getSelectedValue().equals("Yes, Patient-based"))){
			model.setPatientBased(true);
		}
		else if((getModal().getPatientIndecatorList().getSelectedValue().equals("No, Not Patient-based"))){
			model.setPatientBased(false);
		}
		else {
			model.setPatientBased(null);
		}
		model.setModifiedDate(getModal().getModifiedOnList().getSelectedValue());
		model.setModifiedOwner(getModal().getModifiedBy().getValue());
		model.setOwner(getModal().getOwnedBy().getValue());
		model.setAdvanceSearch(true);
		
		return model;
	}

}
