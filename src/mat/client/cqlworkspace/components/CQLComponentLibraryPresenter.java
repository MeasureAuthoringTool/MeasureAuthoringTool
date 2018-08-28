package mat.client.cqlworkspace.components;

public class CQLComponentLibraryPresenter {
	
	CQLComponentLibraryView view;
	
	public CQLComponentLibraryPresenter() {
		view = new CQLComponentLibraryView();
	}
	
	public CQLComponentLibraryView getView() {
		return view;
	}
}