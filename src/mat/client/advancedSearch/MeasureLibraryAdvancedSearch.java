package mat.client.advancedSearch;


public class MeasureLibraryAdvancedSearch extends AdvancedSearchBuilder {

	public void createAdvancedSearch() {
		modal = new AdvancedSearchModel("Measure");
		modal.addSearchTextSection();
		modal.addSearchBySection();
		modal.addStateSection();
		modal.addScoreSection();
		modal.addPatientSection();
		modal.addDaysSection();
		modal.addModifiedSection();
		modal.addOwnedBySection();
		modal.addButtonSection();
	}

}
