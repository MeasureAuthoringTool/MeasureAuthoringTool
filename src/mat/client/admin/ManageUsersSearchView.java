package mat.client.admin;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay {

	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();

	private FlowPanel mainPanel = new FlowPanel();
//	private OrganizationAutoComplete searchText = new OrganizationAutoComplete();
	private TextBox searchText = new TextBox();
	private Widget searchLabel = LabelBuilder.buildLabel(searchText, "Search for a User");
	private SearchView<ManageUsersSearchModel.Result> view = new SearchView<ManageUsersSearchModel.Result>("Users");
	private Button searchButton = new SecondaryButton("Search");
	private Button createNewButton = new SecondaryButton("Add New User");
	
	public ManageUsersSearchView() {
		view.buildDataTable(new ManageUsersSearchModel());
		searchText.setWidth("256px");
		
		mainPanel.add(new SpacerWidget());
		mainPanel.add(createNewButton);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(searchLabel);
		mainPanel.add(searchText);
		searchButton.addStyleName("userSearchButton");
		mainPanel.add(searchButton);
		mainPanel.add(new SpacerWidget());
//		
//		searchCriteria.getCellFormatter().setHorizontalAlignment(0, 0, 
//				HasHorizontalAlignment.ALIGN_LEFT);
//		searchCriteria.getCellFormatter().setHorizontalAlignment(1, 0, 
//				HasHorizontalAlignment.ALIGN_LEFT);
//		searchCriteria.getCellFormatter().setWidth(1, 0, "250px");
//		searchText.setWidth("95%");
//		
//		searchCriteria.getCellFormatter().setHorizontalAlignment(1, 1, 
//				HasHorizontalAlignment.ALIGN_LEFT);
//		searchCriteria.getCellFormatter().setWidth(1, 1, "100px");
//		searchCriteria.getCellFormatter().setHorizontalAlignment(1, 2, 
//				HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		mainPanel.add(view.asWidget());
		mainPanel.setStyleName("contentPanel");
		containerPanel.setContent(mainPanel);
		//containerPanel.setEmbeddedLink("Manage Users");		
		containerPanel.setHeading("Manage Users","Manage Users");
		
	}
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	
	@Override
	public HasClickHandlers getCreateNewButton() {
		return createNewButton;
	}
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	@Override
	public HasSelectionHandlers<ManageUsersSearchModel.Result> getSelectIdForEditTool() {
		return view;
	}
	@Override
	public void buildDataTable(SearchResults<ManageUsersSearchModel.Result> results) {
		view.buildDataTable(results);
	}
	@Override 
	public int getPageSize() {
		return view.getPageSize();
	}
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	@Override
	public HasValue<String> getSearchString() {
		return searchText;
	}
	
}
