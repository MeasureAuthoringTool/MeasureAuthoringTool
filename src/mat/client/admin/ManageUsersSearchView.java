package mat.client.admin;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.admin.ManageUsersSearchModel.Result;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.SearchResults;
import mat.shared.ClickableSafeHtmlCell;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay.**/
public class ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay, HasSelectionHandlers<ManageUsersSearchModel.Result> {

	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();

	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();

	/** The search text. */
	private TextBox searchText = new TextBox();
	
	/** The search label. */
	private Widget searchLabel = LabelBuilder.buildLabel(searchText, "Search for a User");
	
	/** The search button. */
	private Button searchButton = new SecondaryButton("Search");
	
	/** The create new button. */
	private Button createNewButton = new PrimaryButton("Add New User", "primaryGreyButton");
	
	/** The generate csv file button. */
	private Button generateCSVFileButton = new SecondaryButton("Generate CSV File");
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel(); 
	
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);

	/**Constructor.**/
	public ManageUsersSearchView() {
		searchText.setWidth("256px");

		mainPanel.add(new SpacerWidget());
		HorizontalPanel buttonPanel = new HorizontalPanel();		
		buttonPanel.add(createNewButton);
		buttonPanel.add(generateCSVFileButton);
		generateCSVFileButton.setTitle("Generate CSV file of Email Addresses.");
		buttonPanel.getElement().getStyle().setMarginLeft(4, Unit.PX);
		mainPanel.add(buttonPanel);
		mainPanel.add(new SpacerWidget());

		searchLabel.getElement().getStyle().setMarginLeft(4, Unit.PX);
		mainPanel.add(searchLabel);
		searchText.getElement().getStyle().setMarginLeft(4, Unit.PX);
		mainPanel.add(searchText);
		searchButton.addStyleName("userSearchButton");
		mainPanel.add(searchButton);
		mainPanel.add(new SpacerWidget());

		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");	
		cellTablePanel.setWidth("98%");
		mainPanel.add(cellTablePanel);
		
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
		return this;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<ManageUsersSearchModel.Result> results) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		
		CellTable<Result> cellTable = new CellTable<Result>();
		ListDataProvider<Result> listDataProvider = new ListDataProvider<Result>();
		
		List<Result> users = new ArrayList<Result>();
		users.addAll(((ManageUsersSearchModel)results).getDataList());
		cellTable.setPageSize(25);
		cellTable.redraw();
		cellTable.setRowCount(users.size(), true);
		listDataProvider.refresh();
		listDataProvider.getList().addAll(((ManageUsersSearchModel)results).getDataList());
		cellTable = addColumnToTable(cellTable);
		listDataProvider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);	
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
        spager.setPageSize(25);
        spager.setToolTipAndTabIndex(spager);
        cellTable.setWidth("100%");
        cellTable.setColumnWidth(0, 35.0, Unit.PCT);
        cellTable.setColumnWidth(1, 45.0, Unit.PCT);
        cellTable.setColumnWidth(2, 20.0, Unit.PCT);
        cellTablePanel.add(cellTable);
        cellTablePanel.add(new SpacerWidget());
        cellTablePanel.add(spager);
        
        Label cellTableCaption = new Label("Users");
		cellTableCaption.getElement().setId("cellTableCaption_Label");
		cellTableCaption.setStyleName("recentSearchHeader");
		cellTableCaption.getElement().setAttribute("tabIndex", "0");
	    com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
	    TableCaptionElement caption = elem.createCaption();
	    caption.appendChild(cellTableCaption.getElement());
	}
	
	private CellTable<Result> addColumnToTable(final CellTable<Result> cellTable) {

		Column<Result, SafeHtml> nameColumn = new Column<Result, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
				safeHtmlBuilder.appendHtmlConstant("<a href=\"javascript:void(0);\" "
					    + "tabindex=\"0\" style=\"text-decoration:none\" " 
					    + "title='Name: " + object.getFirstName() + " " + object.getLastName() + "' >");
				safeHtmlBuilder.appendEscaped(object.getFirstName() + " " + object.getLastName());
				safeHtmlBuilder.appendHtmlConstant("</a>");
				return safeHtmlBuilder.toSafeHtml();
			}			
		};
		nameColumn.setFieldUpdater(new FieldUpdater<Result, SafeHtml>() {
			@Override
			public void update(int index, Result object, SafeHtml value) {
				MatContext.get().clearDVIMessages();
				SelectionEvent.fire(ManageUsersSearchView.this, object);
			}
		});		
		cellTable.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Name' tabindex=\"0\">" +"Name"+ "</span>"));
		
		Column<Result, SafeHtml> organizationColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {			
			@Override
			public SafeHtml getValue(Result object) {
				return getColumnValueWithToolTip("Organization", object.getOrgName());
			}
		};
		cellTable.addColumn(organizationColumn, SafeHtmlUtils.fromSafeConstant("<span title='Organization' tabindex=\"0\">" +"Organization"+ "</span>"));
		
		Column<Result, SafeHtml> statusColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {			
			@Override
			public SafeHtml getValue(Result object) {
				return getColumnValueWithToolTip("Status", object.getStatus());
			}
		};
		cellTable.addColumn(statusColumn, SafeHtmlUtils.fromSafeConstant("<span title='Status' tabindex=\"0\">" +"Status"+ "</span>"));
		
		return cellTable;
	}

	/**
	 * Gets the column value with tool tip.
	 *
	 * @param columnName the column name
	 * @param columnValue the column value
	 * @return the column value with tool tip
	 */
	private SafeHtml getColumnValueWithToolTip(String columnName, String columnValue){
		String htmlConstant = "<span tabindex=\"0\" title='" + columnName + ": " + columnValue + "'>"+ columnValue + "</span>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageUsersSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchText;
	}	
}
