package mat.client.admin;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;

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
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import mat.client.CustomPager;
import mat.client.admin.ManageUsersSearchModel.Result;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.search.SearchResults;
import mat.client.util.CellTableUtility;

public class ManageBonnieTokenView implements ManageUsersPresenter.SearchDisplay, HasSelectionHandlers<ManageUsersSearchModel.Result> {
	/** Cell Table Page Size. */
	private static final int PAGE_SIZE = 25;
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	/** The create new button. */
	private Button createNewButton = new Button("Add New User");
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search");
	private Observer observer;
	private MessageAlert successMessageDisplay = new SuccessMessageAlert();
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();
	
	
	/**
	 * The Interface Observer.
	 */
	public static interface Observer {

		void onStopBonnieSessionClicked(Result object);
	}
	
	/**Constructor.**/
	public ManageBonnieTokenView() {
		mainPanel.add(new SpacerWidget());
		mainPanel.add(searchWidgetBootStrap.getSearchWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessageDisplay);
		mainPanel.add(errorMessageAlert);
		mainPanel.add(new SpacerWidget());
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");
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
	private CellTable<Result> addColumnToTable(final CellTable<Result> cellTable) {
		Label cellTableCaption = new Label("Manage Users Bonnie Connection");
		cellTableCaption.getElement().setId("manageUserCellTableCaption_Label");
		cellTableCaption.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(cellTableCaption.getElement());
		cellTableCaption.getElement().setAttribute("tabIndex", "0");
		
		Column<Result, SafeHtml> nameColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return CellTableUtility.getColumnToolTip(object.getFirstName() + " " + object.getLastName(), "Name: " + object.getFirstName() + " " + object.getLastName());
			}
		};
		cellTable.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));			
		
		Column<Result, SafeHtml> matIdColumn = new Column<Result, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Result object) {
				return CellTableUtility.getColumnToolTip(object.getLoginId(), "MAT ID: " + object.getLoginId());
			}
		};
		cellTable.addColumn(matIdColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"MAT ID\">" + "MAT ID" + "</span>"));
		
		ButtonCell revokeBonnieButton = new ButtonCell();
		Column<Result, String> revokeColumn = new Column<Result, String>(revokeBonnieButton) {
			@Override
			public String getValue(Result object) {
				return "Stop Bonnie Session";
			}
		};
		
		revokeColumn.setFieldUpdater(new FieldUpdater<Result, String>() {
			@Override
			public void update(int index, Result object, String value) {
				observer.onStopBonnieSessionClicked(object);
			}
		});
		cellTable.addColumn(revokeColumn, SafeHtmlUtils.fromSafeConstant("<span title='Stop Bonnie Session'>" + "Stop Bonnie Session" + "</span>"));

		return cellTable;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler(com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageUsersSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
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
		users.addAll(((ManageUsersSearchModel) results).getDataList());
		cellTable.setPageSize(PAGE_SIZE);
		cellTable.redraw();
		cellTable.setRowCount(users.size(), true);
		listDataProvider.refresh();
		listDataProvider.getList().addAll(((ManageUsersSearchModel) results).getDataList());
		cellTable = addColumnToTable(cellTable);
		listDataProvider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"manageUser");
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
		spager.setPageSize(PAGE_SIZE);
		cellTable.setWidth("100%");
		cellTable.setColumnWidth(0, 35.0, Unit.PCT);
		cellTable.setColumnWidth(1, 45.0, Unit.PCT);
		cellTable.setColumnWidth(2, 20.0, Unit.PCT);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
				"manageUsersSummary",
				"In the Following Manage users table, Name is given in the first column, Organization in the "
						+ "second column, Status in the third column, and User Role in the fourth column.");
		cellTable.getElement().setAttribute("id", "manageUsersCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "manageUsersSummary");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(cellTable);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.HasHandlers#fireEvent(com.google.gwt.event.shared.GwtEvent)
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
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
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#getSelectIdForEditTool()
	 */
	@Override
	public HasSelectionHandlers<ManageUsersSearchModel.Result> getSelectIdForEditTool() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		containerPanel.setHeading(title, "Manage Users");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.SearchDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return successMessageDisplay;
	}
	
	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}

	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	@Override
	public void setObserver(mat.client.admin.ManageUsersSearchView.Observer observer) {
		// TODO Auto-generated method stub
		
	}
}
