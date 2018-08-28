package mat.client.clause.cqlworkspace.leftNavBar.sections;

public class CQLComponentLibraryPresenter {
	
	CQLComponentLibraryView view;
	
	public CQLComponentLibraryPresenter() {
		view = new CQLComponentLibraryView();
	}
	
	public CQLComponentLibraryView getView() {
		return view;
	}
}