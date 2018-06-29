package mat.client.advancedSearch;

import mat.shared.AdvancedSearchModel;

public class CQLLibraryAdvancedSearchBuilder extends AdvancedSearchBuilder {
	
	private final String DESCRIPTION_OF_MODAL_FOR_508 = "This advanced search section "
			+ "allows you to search by the name of the library, library state, "
			+ "when the library was last modified, and by "
			+ "the user that last modified the library, and the library owner. ";
			
	public void createAdvancedSearch() {
		setModal(new AdvancedSearchModal("Library", "Libraries"));
		getModal().addSearchTextSection();
		getModal().addSearchBySection();
		getModal().addStateSection();
		getModal().addDaysSection();
		getModal().addModifiedBySection();
		getModal().addOwnedBySection();
		getModal().addButtonSection();
		getModal().setTitleOfPanel(DESCRIPTION_OF_MODAL_FOR_508);
	}

	@Override
	protected AdvancedSearchModel generateAdvancedSearchModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
