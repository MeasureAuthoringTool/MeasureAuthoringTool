package mat.client.advancedSearch;


public class MeasureLibraryAdvancedSearchBuilder extends AdvancedSearchBuilder {
	
	final String DESCRIPTION_OF_MODAL_FOR_508 = "This advanced search section allows you to "
			+ "search by the name of the measure, measure state, measure score, "
			+ "patient-based indicator, when the measure was last modified, and by the user that"
			+ " last modified the measure, and the measure owner. ";
			
	public void createAdvancedSearch() {
		super.modal = new AdvancedSearchModel("Measure", "Measures");
		super.modal.addSearchTextSection();
		super.modal.addSearchBySection();
		super.modal.addStateSection();
		super.modal.addScoreSection();
		super.modal.addPatientSection();
		super.modal.addDaysSection();
		super.modal.addModifiedBySection();
		super.modal.addOwnedBySection();
		super.modal.addButtonSection();
		super.modal.setTitleOfPanel(DESCRIPTION_OF_MODAL_FOR_508);
	}

}
