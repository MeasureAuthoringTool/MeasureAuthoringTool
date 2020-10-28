package mat.client.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.CustomPager;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.dto.UserAuditLogDTO;

import java.util.Date;
import java.util.List;


/**
 * The Class ManageUserHistoryView.
 */

public class ManageUserHistoryView implements
		ManageUsersPresenter.HistoryDisplay {

	/** The user id. */
	private String userId;

	/** The measure name. */
	private String userName;

	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();

	/** The log entry label. */
	Label logEntryLabel = new Label();

	/** The go back link. */
	protected Anchor goBackLink = new Anchor("");

	/** The name text. */
	protected Label nameText = new Label("");

	/** The log entry panel. */
	private VerticalPanel logEntryPanel = new VerticalPanel();

	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();

	/** The cell table. */
	private CellTable<UserAuditLogDTO> cellTable;

	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;

	/** The spager. */
	private MatSimplePager spager;

	/**
	 * Instantiates a new manage user history view.
	 */
	public ManageUserHistoryView() {
		cellTablePanel.setWidth("900px");
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		nameText.addStyleName("labelStyling");
		logEntryPanel.add(nameText);
		logEntryPanel.add(new SpacerWidget());
		logEntryPanel.add(cellTablePanel);
		logEntryPanel.add(new SpacerWidget());
		logEntryPanel.add(new SpacerWidget());
		logEntryPanel.add(goBackLink);
		mainPanel.setStyleName("contentPanel");
		mainPanel.add(logEntryPanel);

	}

	/**
	 * Adds the column to table.
	 * 
	 * @param cellTable
	 *            the cell table
	 * @return the cell table
	 */
	private CellTable<UserAuditLogDTO> addColumnToTable(
			CellTable<UserAuditLogDTO> cellTable) {
		Label searchHeader = new Label("Log Entry");
		searchHeader.getElement().setId("historyCellTableCaption_Label");
		searchHeader.setStyleName("recentSearchHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		com.google.gwt.dom.client.TableElement elem = cellTable.getElement()
				.cast();
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(searchHeader.getElement());

		Column<UserAuditLogDTO, SafeHtml> userAction = new Column<UserAuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(UserAuditLogDTO object) {
				return CellTableUtility
						.getColumnToolTip(object.getActionType());
			}
		};
		cellTable.addColumn(userAction, SafeHtmlUtils
				.fromSafeConstant("<span title='User Action'>" + "User Action"
						+ "</span>"));

		Column<UserAuditLogDTO, SafeHtml> userActivity = new Column<UserAuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(UserAuditLogDTO object) {
				return CellTableUtility.getColumnToolTip(object
						.getActivityType());
			}
		};
		cellTable.addColumn(userActivity, SafeHtmlUtils
				.fromSafeConstant("<span title='User Activity'>"
						+ "User Activity" + "</span>"));

		Column<UserAuditLogDTO, SafeHtml> lastModifiedBy = new Column<UserAuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(UserAuditLogDTO object) {
				return CellTableUtility.getColumnToolTip(object.getUserEmail());
			}
		};
		cellTable.addColumn(lastModifiedBy, SafeHtmlUtils
				.fromSafeConstant("<span title='Last Modified By'>"
						+ "Last Modified By" + "</span>"));

		Column<UserAuditLogDTO, SafeHtml> lastModifiedDate = new Column<UserAuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(UserAuditLogDTO object) {
				return CellTableUtility
						.getColumnToolTip(convertDateToString(object.getTime()));
			}
		};
		cellTable.addColumn(lastModifiedDate, SafeHtmlUtils
				.fromSafeConstant("<span title='Last Modified Date'>"
						+ "Last Modified Date" + "</span>"));

		Column<UserAuditLogDTO, SafeHtml> additionalInfo = new Column<UserAuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(UserAuditLogDTO object) {
				if (object.getAdditionalInfo() != null) {
					return CellTableUtility.getColumnToolTip(object
							.getAdditionalInfo());
				}
				return null;
			}
		};
		cellTable.addColumn(additionalInfo, SafeHtmlUtils
				.fromSafeConstant("<span title='Administrator Notes'>"
						+ "Administrator Notes" + "</span>"));

		return cellTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.admin.ManageUsersPresenter.HistoryDisplay#buildCellTable(java
	 * .util.List)
	 */
	@Override
	public void buildCellTable(List<UserAuditLogDTO> results) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		if ((results != null) && (results.size() > 0)) {
			cellTable = new CellTable<UserAuditLogDTO>();
			ListDataProvider<UserAuditLogDTO> listDataProvider = new ListDataProvider<UserAuditLogDTO>();
			cellTable
					.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			cellTable.setRowData(results);
			cellTable.setRowCount(results.size(), true);
			cellTable.setPageSize(PAGE_SIZE);
			cellTable.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(results);
			cellTable = addColumnToTable(cellTable);
			listDataProvider.addDataDisplay(cellTable);
			CustomPager.Resources pagerResources = GWT
					.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER,
					pagerResources, false, 0, true,"userHistory");
			spager.setPageStart(0);
			spager.setDisplay(cellTable);
			spager.setPageSize(PAGE_SIZE);
			cellTable.setWidth("100%");
			cellTable.setColumnWidth(0, 15.0, Unit.PCT);
			cellTable.setColumnWidth(1, 20.0, Unit.PCT);
			cellTable.setColumnWidth(2, 15.0, Unit.PCT);
			cellTable.setColumnWidth(3, 20.0, Unit.PCT);
			cellTable.setColumnWidth(4, 30.0, Unit.PCT);
			Label invisibleLabel = (Label) LabelBuilder
					.buildInvisibleLabel(
							"historySearchSummary",
							"In the following Log Entry table, User Action is given in first column,"
									+ " User Activity in second column, Last Modified By in third column,"
									+ "Last Modified Date in fourth column and Administrator Notes in last column.");
			cellTable.getElement().setAttribute("id", "HistorySearchCellTable");
			cellTable.getElement().setAttribute("aria-describedby",
					"historySearchSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(cellTable);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);

		}
	}

	/**
	 * Convert date to string.
	 * 
	 * @param ts
	 *            the ts
	 * @return the string
	 */
	private String convertDateToString(Date ts) {
		String tsStr;
		if (ts == null) {
			tsStr = "";
		} else {
			DateTimeFormat formatter = DateTimeFormat
					.getFormat("MM'/'dd'/'yyyy h:mm:ss a");
			tsStr = formatter.format(ts);
			tsStr = tsStr + " CST";
		}

		return tsStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.HistoryDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.HistoryDisplay#getUserId()
	 */
	@Override
	public String getUserId() {
		return userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.HistoryDisplay#getUserName()
	 */
	@Override
	public String getUserName() {
		return userName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.admin.ManageUsersPresenter.HistoryDisplay#getReturnToLink()
	 */
	@Override
	public HasClickHandlers getReturnToLink() {
		return goBackLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.admin.ManageUsersPresenter.HistoryDisplay#setUserId(java.lang
	 * .String)
	 */
	@Override
	public void setUserId(String id) {
		this.userId = id;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.admin.ManageUsersPresenter.HistoryDisplay#setUserName(java
	 * .lang.String)
	 */
	@Override
	public void setUserName(String name) {
		this.userName = name;
		nameText.setText("User: " + name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.admin.ManageUsersPresenter.HistoryDisplay#setReturnToLinkText
	 * (java.lang.String)
	 */
	@Override
	public void setReturnToLinkText(String s) {
		goBackLink.setText(s);

	}

}
