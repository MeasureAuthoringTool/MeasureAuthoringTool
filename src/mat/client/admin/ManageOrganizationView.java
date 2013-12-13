package mat.client.admin;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.SearchResults;
import mat.client.util.CellTableUtility;
import mat.shared.ClickableSafeHtmlCell;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.dom.client.Style.Unit;
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

/** ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay. **/
public class ManageOrganizationView implements ManageOrganizationPresenter.SearchDisplay, HasSelectionHandlers<ManageOrganizationSearchModel.Result> {
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The create new button. */
	private Button createNewButton = new PrimaryButton("Add New Organization", "primaryGreyButton");
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The search button. */
	private Button searchButton = new SecondaryButton("Search");
	
	/** The search text. */
	private TextBox searchText = new TextBox();
	
	/** The search label. */
	private Widget searchTextLabel = LabelBuilder.buildLabel(searchText, "Search for a Organization");
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel(); 
	
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	
	
	/** Constructor. **/
	public ManageOrganizationView() {
		searchText.setWidth("256px");
		
		mainPanel.add(new SpacerWidget());
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(createNewButton);
		buttonPanel.getElement().getStyle().setMarginLeft(4, Unit.PX);
		mainPanel.add(buttonPanel);
		mainPanel.add(new SpacerWidget());
		
		searchTextLabel.getElement().getStyle().setMarginLeft(4, Unit.PX);
		mainPanel.add(searchTextLabel);
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
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		
		CellTable<Result> cellTable = new CellTable<Result>();
		ListDataProvider<Result> listDataProvider = new ListDataProvider<Result>();
		
		List<Result> organizations = new ArrayList<Result>();
		organizations.addAll(((ManageOrganizationSearchModel) results).getData());
		cellTable.setPageSize(25);
		cellTable.redraw();
		//cellTable.setRowCount(organizations.size(), true);
		listDataProvider.refresh();
		listDataProvider.getList().addAll(((ManageOrganizationSearchModel) results).getData());
		cellTable = addColumnToTable(cellTable);
		listDataProvider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);	
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
        spager.setPageSize(25);
        spager.setToolTipAndTabIndex(spager);
        cellTable.setWidth("100%");
        cellTable.setColumnWidth(0, 50.0, Unit.PCT);
        cellTable.setColumnWidth(1, 50.0, Unit.PCT);
        cellTablePanel.add(cellTable);
        cellTablePanel.add(new SpacerWidget());
        cellTablePanel.add(spager);
        
        Label cellTableCaption = new Label("Manage Organizations");
		cellTableCaption.getElement().setId("cellTableCaption_Label");
		cellTableCaption.setStyleName("recentSearchHeader");
		cellTableCaption.getElement().setAttribute("tabIndex", "0");
	    com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
	    TableCaptionElement caption = elem.createCaption();
	    caption.appendChild(cellTableCaption.getElement());
	}
	
	/**
	 * Adds the column to table.
	 *
	 * @param cellTable the cell table
	 * @return the cell table
	 */
	private CellTable<Result> addColumnToTable(CellTable<Result> cellTable) {
		Column<Result, SafeHtml> organizationColumn = new Column<Result, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
				safeHtmlBuilder.appendHtmlConstant("<a href=\"javascript:void(0);\" "
					    + "tabindex=\"0\" style=\"text-decoration:none\" " 
					    + "title='Organization: " + object.getOrgName() + "' >");
				safeHtmlBuilder.appendEscaped(object.getOrgName());
				safeHtmlBuilder.appendHtmlConstant("</a>");
				return safeHtmlBuilder.toSafeHtml();
			}			
		};
		organizationColumn.setFieldUpdater(new FieldUpdater<Result, SafeHtml>() {
			@Override
			public void update(int index, Result object, SafeHtml value) {
				MatContext.get().clearDVIMessages();
				SelectionEvent.fire(ManageOrganizationView.this, object);
			}
		});		
		cellTable.addColumn(organizationColumn, 
				SafeHtmlUtils.fromSafeConstant("<span title='Organization' tabindex=\"0\">" + "Organization" + "</span>"));
		
		Column<Result, SafeHtml> oidColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {			
			@Override
			public SafeHtml getValue(Result object) {
				return CellTableUtility.getColumnToolTip(object.getOid(), "OID: " + object.getOid());
			}
		};
		cellTable.addColumn(oidColumn, SafeHtmlUtils.fromSafeConstant("<span title='OID' tabindex=\"0\">" + "OID" + "</span>"));
		
		return cellTable;
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageOrganizationSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
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
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageOrganizationPresenter.SearchDisplay#getSelectIdForEditTool()
	 */
	@Override
	public HasSelectionHandlers<ManageOrganizationSearchModel.Result> getSelectIdForEditTool() {
		return this;
	}
}
