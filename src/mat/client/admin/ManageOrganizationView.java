package mat.client.admin;

import java.util.ArrayList;
import java.util.List;
import mat.client.CustomPager;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.search.SearchResults;
import mat.client.util.CellTableUtility;
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

/** ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay. **/
public class ManageOrganizationView implements ManageOrganizationPresenter.SearchDisplay,
HasSelectionHandlers<ManageOrganizationSearchModel.Result> {
	
	public static interface Observer {
		void onDeleteClicked(ManageOrganizationSearchModel.Result result);
	}
	
	/** Cell Table Columns width - 50% each. */
	private static final double CELLTABLE_COLUMN_WIDTH = 50.0;
	/** MARGIN Constant value. */
	private static final int MARGIN_VALUE = 4;
	/** Cell Table Page Size. */
	private static final int PAGE_SIZE = 25;
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	/** The create new button. */
	private Button createNewButton = new PrimaryButton("Add New Organization", "primaryGreyButton");
	/** The generate csv file button. */
	//private Button generateCSVFileButton = new SecondaryButton("Generate CSV File");
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	/** The search button. */
	private Button searchButton = new SecondaryButton("Search");
	/** The search text. */
	private TextBox searchText = new TextBox();
	/** The search label. */
	private Widget searchTextLabel = LabelBuilder.buildLabel(searchText, "Search for a Organization");
	/** The observer. */
	private Observer observer;
	/**
	 * The Success Message Display.
	 */
	private SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	/**
	 * The Erro Message Display.
	 */
	private ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();
	/** Constructor. **/
	public ManageOrganizationView() {
		searchText.setWidth("256px");
		mainPanel.add(new SpacerWidget());
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(createNewButton);
		//buttonPanel.add(generateCSVFileButton);
		//generateCSVFileButton.setTitle("Generate CSV file of Active OID's.");
		buttonPanel.getElement().getStyle().setMarginLeft(MARGIN_VALUE, Unit.PX);
		mainPanel.add(buttonPanel);
		mainPanel.add(new SpacerWidget());
		searchTextLabel.getElement().getStyle().setMarginLeft(MARGIN_VALUE, Unit.PX);
		mainPanel.add(searchTextLabel);
		searchText.getElement().getStyle().setMarginLeft(MARGIN_VALUE, Unit.PX);
		mainPanel.add(searchText);
		searchButton.addStyleName("userSearchButton");
		mainPanel.add(searchButton);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessageDisplay);
		mainPanel.add(errorMessageDisplay);
		cellTablePanel.getElement().setId("manageOrganizationCellTablePanel_VerticalPanel");
		cellTablePanel.setWidth("98%");
		mainPanel.add(cellTablePanel);
		mainPanel.setStyleName("contentPanel");
		containerPanel.setContent(mainPanel);
	}
	/**
	 * Adds the column to table.
	 *
	 * @param cellTable the cell table
	 * @return the cell table
	 */
	private CellTable<Result> addColumnToTable(CellTable<Result> cellTable) {
		Label cellTableCaption = new Label("Manage Organizations");
		cellTableCaption.getElement().setId("manageOrgCellTableCaption_Label");
		cellTableCaption.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(cellTableCaption.getElement());
		cellTableCaption.getElement().setAttribute("tabIndex", "0");
		Column<Result, SafeHtml> organizationColumn = new Column<Result, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
				safeHtmlBuilder.appendHtmlConstant("<a href=\"javascript:void(0);\" "
						+ " style=\"text-decoration:none\" "
						+ "title=\"Organization: " + object.getOrgName() + "\" >");
				safeHtmlBuilder.appendEscaped(object.getOrgName());
				safeHtmlBuilder.appendHtmlConstant("</a>");
				return safeHtmlBuilder.toSafeHtml();
			}
		};
		organizationColumn.setFieldUpdater(new FieldUpdater<Result, SafeHtml>() {
			@Override
			public void update(int index, Result object, SafeHtml value) {
				SelectionEvent.fire(ManageOrganizationView.this, object);
			}
		});
		cellTable.addColumn(organizationColumn,
				SafeHtmlUtils.fromSafeConstant("<span title=\"Organization\" >" + "Organization" + "</span>"));
		Column<Result, SafeHtml> oidColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return CellTableUtility.getColumnToolTip(object.getOid(), "OID: " + object.getOid());
			}
		};
		cellTable.addColumn(oidColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"OID\">" + "OID" + "</span>"));
		
		Column<Result, SafeHtml> editColumn = new Column<Result, SafeHtml>( new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return getDeleteColumnToolTip(object);
			}
		};
		editColumn.setFieldUpdater(new FieldUpdater<Result, SafeHtml>() {
			@Override
			public void update(int index, Result object,
					SafeHtml value) {
				if(!object.isUsed()){
					observer.onDeleteClicked(object);
				}
			}
		});
		cellTable.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Delete'>" + "Delete" + "</span>"));
		return cellTable;
	}
	
	private SafeHtml getDeleteColumnToolTip(Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title;
		String cssClass;
		if (!object.isUsed()) {
			title = "Delete";
			cssClass = "customDeleteButton";
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\">Delete</button>");
		} else {
			title = "Organization in use.";
			cssClass = "customDeleteDisableButton";
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' disabled tabindex=\"0\" class=\" " + cssClass + "\" disabled>Organization already in use.</button>");
		}
		return sb.toSafeHtml();
	}
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageOrganizationSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	/*
	 * (non-Javadoc)
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
		cellTable.setPageSize(PAGE_SIZE);
		cellTable.redraw();
		listDataProvider.refresh();
		listDataProvider.getList().addAll(((ManageOrganizationSearchModel) results).getData());
		cellTable = addColumnToTable(cellTable);
		listDataProvider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
		spager.setPageSize(PAGE_SIZE);
		/* spager.setToolTipAndTabIndex(spager); */
		cellTable.setWidth("100%");
		cellTable.setColumnWidth(0, 40, Unit.PCT);
		cellTable.setColumnWidth(1, 40, Unit.PCT);
		cellTable.setColumnWidth(2, 20, Unit.PCT);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
				"manageOrganizationSummary",
				"In the following Manage organizations table, Organization Name is given in the first column "
						+ ", OID in the second Column and Delete button in third column.");
		cellTable.getElement().setAttribute("id", "manageOrganizationCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "manageOrganizationSummary");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(cellTable);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
	}
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#getCreateNewButton()
	 */
	@Override
	public HasClickHandlers getCreateNewButton() {
		return createNewButton;
	}
	/*@Override
	public Button getGenerateCSVFileButton() {
		return generateCSVFileButton;
	}*/
	/*
	 * (non-Javadoc)
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
	/**
	 * @return the observer
	 */
	public Observer getObserver() {
		return observer;
	}
	/**
	 * @param observer the observer to set
	 */
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	/**
	 * @return the successMessageDisplay
	 */
	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessageDisplay;
	}
	/**
	 * @param successMessageDisplay the successMessageDisplay to set
	 */
	public void setSuccessMessageDisplay(SuccessMessageDisplay successMessageDisplay) {
		this.successMessageDisplay = successMessageDisplay;
	}
	/**
	 * @return the errorMessageDisplay
	 */
	@Override
	public ErrorMessageDisplay getErrorMessageDisplay() {
		return errorMessageDisplay;
	}
	/**
	 * @param errorMessageDisplay the errorMessageDisplay to set
	 */
	public void setErrorMessageDisplay(ErrorMessageDisplay errorMessageDisplay) {
		this.errorMessageDisplay = errorMessageDisplay;
	}
}
