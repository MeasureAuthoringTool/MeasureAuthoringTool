package mat.client.advancedSearch;


public class CQLLibraryAdvancedSearch extends AdvancedSearchBuilder {
	
	final String DESCRIPTION_OF_MODAL_FOR_508 = "This advanced search section "
			+ "allows you to search by the name of the library, library state, "
			+ "when the library was last modified, and by "
			+ "the user that last modified the library, and the library owner. ";
			
	public void createAdvancedSearch() {
		super.modal = new AdvancedSearchModel("Library", "Libraries");
		super.modal.addSearchTextSection();
		super.modal.addSearchBySection();
		super.modal.addStateSection();
		super.modal.addDaysSection();
		super.modal.addModifiedBySection();
		super.modal.addOwnedBySection();
		super.modal.addButtonSection();
		super.modal.setTitleOfPanel(DESCRIPTION_OF_MODAL_FOR_508);
	}
}
