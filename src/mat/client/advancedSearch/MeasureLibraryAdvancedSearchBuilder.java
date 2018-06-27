package mat.client.advancedSearch;


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

}
