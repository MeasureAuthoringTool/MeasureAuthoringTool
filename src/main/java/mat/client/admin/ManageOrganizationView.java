package mat.client.admin;

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
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.CustomPager;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.shared.*;
import mat.client.shared.search.SearchResults;
import mat.client.util.CellTableUtility;
import mat.shared.ClickableSafeHtmlCell;
import org.gwtbootstrap3.client.ui.Button;

import java.util.ArrayList;
import java.util.List;

/** ManageUsersSearchView implements ManageUsersPresenter.SearchDisplay. **/
public class ManageOrganizationView implements ManageOrganizationPresenter.SearchDisplay,
HasSelectionHandlers<ManageOrganizationSearchModel.Result> {
	
	public static interface Observer {
		void onDeleteClicked(ManageOrganizationSearchModel.Result result);
	}
	
	SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search");
	
	/** MARGIN Constant value. */
	private static final int MARGIN_VALUE = 4;
	/** Cell Table Page Size. */
	private static final int PAGE_SIZE = 25;
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	/** The create new button. */
	private Button createNewButton = new Button("Add New Organization");
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	/** The observer. */
	private Observer observer;
	/**
	 * The Success Message Display.
	 */
	private MessageAlert successMessageDisplay = new SuccessMessageAlert();
	/**
	 * The Erro Message Display.
	 */
	private MessageAlert errorMessageDisplay = new ErrorMessageAlert();
	/** Constructor. **/
	public ManageOrganizationView() {
		mainPanel.add(new SpacerWidget());
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(createNewButton);
		buttonPanel.getElement().getStyle().setMarginLeft(MARGIN_VALUE, Unit.PX);
		mainPanel.add(buttonPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(searchWidgetBootStrap.getSearchWidget());
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
		cellTableCaption.getElement().setAttribute("tabIndex", "-1");
		Column<Result, SafeHtml> organizationColumn = new Column<Result, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
				safeHtmlBuilder.appendHtmlConstant("<a href=\"javascript:void(0);\" "
						+ " style=\"text-decoration:none\" "
						+ "title=\"Organization: " + SafeHtmlUtils.htmlEscape(object.getOrgName()) + "\" >");
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
		//MAT-9000. Changes to Account Management -> Manage Organization table to use bootstrap delete icon. 
		String cssClass = "btn btn-link";
		String iconCss ="fa fa-trash fa-lg";
		if (!object.isUsed()) {
			title = "Delete";			
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' class=\"" + cssClass + "\" style=\"margin-left: 0px;\" > <i class=\"" + iconCss + "\"></i><span style=\"font-size:0;\">Delete</button>");
		} else {
			title = "Organization in use.";			
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' class=\"" + cssClass + "\" disabled style=\"margin-left: 0px;\"><i class=\""+iconCss + "\"></i> <span style=\"font-size:0;\">Organization already in use.</span></button>");
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
		addColumnToTable(cellTable);
		listDataProvider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"organization");
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
		spager.setPageSize(PAGE_SIZE);
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

	/*
	 * (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getSearchButton()
	 */
	@Override
	public HasClickHandlers getSearchButton() {
		return searchWidgetBootStrap.getGo();
	}
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#getSearchString()
	 */
	@Override
	public HasValue<String> getSearchString() {
		return searchWidgetBootStrap.getSearchBox();
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
	public MessageAlert getSuccessMessageDisplay() {
		return successMessageDisplay;
	}
	/**
	 * @param successMessageDisplay the successMessageDisplay to set
	 */
	public void setSuccessMessageDisplay(MessageAlert successMessageDisplay) {
		this.successMessageDisplay = successMessageDisplay;
	}
	/**
	 * @return the errorMessageDisplay
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessageDisplay;
	}
	/**
	 * @param errorMessageDisplay the errorMessageDisplay to set
	 */
	public void setErrorMessageDisplay(MessageAlert errorMessageDisplay) {
		this.errorMessageDisplay = errorMessageDisplay;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageOrganizationPresenter.SearchDisplay#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		containerPanel.setHeading(title, "Manage Organizations");
	}
}
