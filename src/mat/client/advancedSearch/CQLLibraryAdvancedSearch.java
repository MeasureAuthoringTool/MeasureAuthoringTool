package mat.client.advancedSearch;


public class CQLLibraryAdvancedSearch extends AdvancedSearchBuilder {
	
	public void createAdvancedSearch() {
		
		modal = new AdvancedSearchModel("Library");
		modal.addSearchTextSection();
		modal.addSearchBySection();
		modal.addStateSection();
		modal.addDaysSection();
		modal.addModifiedSection();
		modal.addOwnedBySection();
		modal.addButtonSection();
		
	}

}
