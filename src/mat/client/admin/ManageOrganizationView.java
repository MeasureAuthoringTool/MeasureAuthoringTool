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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/** ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay. **/
public class ManageOrganizationView implements ManageOrganizationPresenter.SearchDisplay {
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The create new button. */
	private Button createNewButton = new SecondaryButton("Add New Organization");
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The search button. */
	private Button searchButton = new SecondaryButton("Search");
	
	/** The search text. */
	private TextBox searchText = new TextBox();
	
	/** The search label. */
	private Widget searchTextLabel = LabelBuilder.buildLabel(searchText, "Search for a Organization");
	
	
	
	/** The view. */
	private SearchView<ManageOrganizationSearchModel.Result> view = new SearchView<ManageOrganizationSearchModel.Result>("Users");
	
	/** Constructor. **/
	public ManageOrganizationView() {
		view.buildDataTable(new ManageOrganizationSearchModel());
		searchText.setWidth("256px");
		
		mainPanel.add(new SpacerWidget());
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(createNewButton);
		mainPanel.add(buttonPanel);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(searchTextLabel);
		mainPanel.add(searchText);
		searchButton.addStyleName("userSearchButton");
		mainPanel.add(searchButton);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(view.asWidget());
		mainPanel.setStyleName("contentPanel");
		containerPanel.setContent(mainPanel);
		containerPanel.setHeading("Manage Organizations", "Manage Organizations");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.shared.search.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<ManageOrganizationSearchModel.Result> results) {
		view.buildDataTable(results);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#getCreateNewButton()
	 */
	@Override
	public HasClickHandlers getCreateNewButton() {
		return createNewButton;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#getGenerateCSVFileButton()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.shared.search.SearchDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.shared.search.SearchDisplay#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.shared.search.SearchDisplay#getPageSizeSelectionTool()
	 */
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.shared.search.SearchDisplay#getSearchButton()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchText;
	}
	
}
