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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay.**/
public class ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay {

	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();

	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();

	/** The search text. */
	private TextBox searchText = new TextBox();
	
	/** The search label. */
	private Widget searchLabel = LabelBuilder.buildLabel(searchText, "Search for a User");
	
	/** The view. */
	private SearchView<ManageUsersSearchModel.Result> view = new SearchView<ManageUsersSearchModel.Result>("Users");
	
	/** The search button. */
	private Button searchButton = new SecondaryButton("Search");
	
	/** The create new button. */
	private Button createNewButton = new SecondaryButton("Add New User");
	
	/** The generate csv file button. */
	private Button generateCSVFileButton = new SecondaryButton("Generate CSV File");

	/**Constructor.**/
	public ManageUsersSearchView() {
		view.buildDataTable(new ManageUsersSearchModel());
		searchText.setWidth("256px");

		mainPanel.add(new SpacerWidget());
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(createNewButton);
		buttonPanel.add(generateCSVFileButton);
		generateCSVFileButton.setTitle("Generate CSV file of Email Addresses.");
		mainPanel.add(buttonPanel);
		mainPanel.add(new SpacerWidget());

		mainPanel.add(searchLabel);
		mainPanel.add(searchText);
		searchButton.addStyleName("userSearchButton");
		mainPanel.add(searchButton);
		mainPanel.add(new SpacerWidget());

		mainPanel.add(view.asWidget());
		mainPanel.setStyleName("contentPanel");
		containerPanel.setContent(mainPanel);
		containerPanel.setHeading("Manage Users", "Manage Users");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#getGenerateCSVFileButton()
	 */
	@Override
	public Button getGenerateCSVFileButton() {
		return generateCSVFileButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#getCreateNewButton()
	 */
	@Override
	public HasClickHandlers getCreateNewButton() {
		return createNewButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getSearchButton()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#getSelectIdForEditTool()
	 */
	@Override
	public HasSelectionHandlers<ManageUsersSearchModel.Result> getSelectIdForEditTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<ManageUsersSearchModel.Result> results) {
		view.buildDataTable(results);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getPageSizeSelectionTool()
	 */
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchText;
	}
}
