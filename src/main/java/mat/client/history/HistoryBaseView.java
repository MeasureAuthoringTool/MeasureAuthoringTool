package mat.client.history;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
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

import mat.dto.AuditLogDTO;
import mat.client.CustomPager;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.util.CellTableUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class HistoryBaseView.
 */
public abstract class HistoryBaseView {
	
	
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
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The success messages. */
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The error messages. */
	protected MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The err msg. */
	private ErrorMessageDisplay errMsg = new ErrorMessageDisplay();
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** The cell table. */
	private CellTable<AuditLogDTO> cellTable;
	
	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;
	
	/** The spager. */
	private MatSimplePager spager;
	
	
	
	/**
	 * Instantiates a new history base view.
	 */
	public HistoryBaseView(){
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
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget() {
		return mainPanel;
	}
	
	/**
	 * Sets the parent name.
	 * 
	 * @param label
	 *            the label
	 * @param name
	 *            the name
	 */
	protected void setParentName(String label, String name) {
		Label parentLabel = new Label(label);
		Label parentName = new Label(name);
		containerPanel.setCodeListInfo(parentLabel);
		containerPanel.setCodeListInfo(parentName);
	}
	
	/**
	 * Gets the success message display.
	 * 
	 * @return the success message display
	 */
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	
	/**
	 * Gets the error message display.
	 * 
	 * @return the error message display
	 */
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/**
	 * Gets the page selection tool.
	 * 
	 * @return the page selection tool
	 */
	/**
	 * Adds the column to table.
	 *
	 * @param cellTable the cell table
	 * @return the cell table
	 */
	private CellTable<AuditLogDTO> addColumnToTable(CellTable<AuditLogDTO> cellTable) {
		Label searchHeader = new Label("Log Entry");
		searchHeader.getElement().setId("historyCellTableCaption_Label");
		searchHeader.setStyleName("recentSearchHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(searchHeader.getElement());
		
		Column<AuditLogDTO, SafeHtml> userAction = new Column<AuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(AuditLogDTO object) {
				return CellTableUtility.getColumnToolTip(object.getActivityType());
			}
		};
		cellTable.addColumn(userAction, SafeHtmlUtils
				.fromSafeConstant("<span title='User Action'>" + "User Action"
						+ "</span>"));
		
		Column<AuditLogDTO, SafeHtml> lastModifiedBy = new Column<AuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(AuditLogDTO object) {
				return CellTableUtility.getColumnToolTip(object.getUserId());
			}
		};
		cellTable.addColumn(lastModifiedBy, SafeHtmlUtils
				.fromSafeConstant("<span title='Last Modified By'>" + "Last Modified By"
						+ "</span>"));
		
		Column<AuditLogDTO, SafeHtml> lastModifiedDate = new Column<AuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(AuditLogDTO object) {
				return CellTableUtility.getColumnToolTip(convertDateToString(object.getEventTs()));
			}
		};
		cellTable.addColumn(lastModifiedDate, SafeHtmlUtils
				.fromSafeConstant("<span title='Last Modified Date'>" + "Last Modified Date"
						+ "</span>"));
		
		Column<AuditLogDTO, SafeHtml> additionalInfo = new Column<AuditLogDTO, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(AuditLogDTO object) {
				if(object.getAdditionlInfo()!=null){
					return CellTableUtility.getColumnToolTip(object.getAdditionlInfo());
				}
				return null;
			}
		};
		cellTable.addColumn(additionalInfo, SafeHtmlUtils
				.fromSafeConstant("<span title='Additional Information'>" + "Additional Information"
						+ "</span>"));
		
		
		return cellTable;
	}
	
	/**
	 * Builds the cell table.
	 *
	 * @param results the results
	 */
	public void buildCellTable(List<AuditLogDTO> results){
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		if((results!=null) && (results.size() > 0)){
			cellTable = new CellTable<AuditLogDTO>();
			ListDataProvider<AuditLogDTO> listDataProvider = new ListDataProvider<AuditLogDTO>();
			cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			cellTable.setRowData(results);
			cellTable.setRowCount(results.size(), true);
			cellTable.setPageSize(PAGE_SIZE);
			cellTable.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(results);
			cellTable = addColumnToTable(cellTable);
			listDataProvider.addDataDisplay(cellTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"historyBaseView");
			spager.setPageStart(0);
			spager.setDisplay(cellTable);
			spager.setPageSize(PAGE_SIZE);
			cellTable.setWidth("100%");
			cellTable.setColumnWidth(0, 25.0, Unit.PCT);
			cellTable.setColumnWidth(1, 25.0, Unit.PCT);
			cellTable.setColumnWidth(2, 25.0, Unit.PCT);
			cellTable.setColumnWidth(2, 25.0, Unit.PCT);
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("historySearchSummary",
					"In the following Log Entry table, User Action is given in first column,"
							+ " Last Modified By in second column and Last Modified Date in third column");
			cellTable.getElement().setAttribute("id", "HistorySearchCellTable");
			cellTable.getElement().setAttribute("aria-describedby", "historySearchSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(cellTable);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
			
		}
	}
	
	/**
	 * Sets the error message.
	 * 
	 * @param s
	 *            the new error message
	 */
	public void setErrorMessage(String s){
		errMsg.clear();
		errMsg.setMessage(s);
	}
	
	/**
	 * Clear error message.
	 */
	public void clearErrorMessage(){
		errMsg.clear();
	}
	
	/**
	 * Convert date to string.
	 *
	 * @param ts the ts
	 * @return the string
	 */
	private String convertDateToString(Date ts){
		String tsStr;
		if(ts == null){
			tsStr = "";
		} else {
			DateTimeFormat formatter = DateTimeFormat.getFormat("MM'/'dd'/'yyyy h:mm:ss a");
			tsStr = formatter.format(ts);
			tsStr = tsStr + " CST";
		}
		
		return tsStr;
	}
	
}



