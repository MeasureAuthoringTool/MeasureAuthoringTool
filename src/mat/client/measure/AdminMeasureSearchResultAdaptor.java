package mat.client.measure;

import java.sql.Timestamp;
import java.util.Comparator;

import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.util.CellTableUtility;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Label;

// TODO: Auto-generated Javadoc
/**
 * The Class AdminMeasureSearchResultAdaptor.
 */
public class AdminMeasureSearchResultAdaptor /*implements SearchResults<ManageMeasureSearchModel.Result> */{
	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		/**
		 * On history clicked.
		 * @param result
		 *            the result
		 */
		void onHistoryClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On transfer selected clicked.
		 * @param result
		 *            the result
		 */
		void onTransferSelectedClicked(ManageMeasureSearchModel.Result result);
	}
	/** CellTable Column Size. */
	private static final int COL_SIZE = 6;
	/** The headers. */
	private static String[] headers = new String[] { "Measure Name", "Version", "Finalized Date",
		"Status", "History", "TransferMeasureClear" };
	/** The widths. */
	private static String[] widths = new String[] { "35%", "16%", "16%", "8%", "5%","5%","10%" };
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	/** The is history clicked. */
	private boolean isHistoryClicked;
	/** The observer. */
	private Observer observer;
	/**
	 * Adds the column to table.
	 * @param table
	 *            the table
	 * @param sortHandler
	 *            the sort handler
	 * @return the cell table
	 */
	public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(final CellTable<
			ManageMeasureSearchModel.Result> table,
			ListHandler<ManageMeasureSearchModel.Result> sortHandler) {
		if (table.getColumnCount() != COL_SIZE) {
			Label searchHeader = new Label("Select Measures to Transfer Ownership.");
			searchHeader.getElement().setId("measureTransferOwnerShipCellTableCaption_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getName(), object.getName());
				}
			};
			measureName.setSortable(true);
			sortHandler.setComparator(measureName, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}
					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title=\"Measure Name\">"
					+ "Measure Name" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerName = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getOwnerfirstName()
							+ "  " + object.getOwnerLastName(),object.getOwnerfirstName()
							+ "  " + object.getOwnerLastName());
				}
			};
			ownerName.setSortable(true);
			sortHandler.setComparator(ownerName, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}
					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerfirstName().compareTo(o2.getOwnerfirstName()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(ownerName, SafeHtmlUtils.fromSafeConstant("<span title=\"Owner\">" + "Owner" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerEmailAddress = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getOwnerEmailAddress(),object.getOwnerEmailAddress());
				}
			};
			ownerEmailAddress.setSortable(true);
			sortHandler.setComparator(ownerEmailAddress, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}
					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerEmailAddress().compareTo(o2.getOwnerEmailAddress()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(ownerEmailAddress, SafeHtmlUtils.fromSafeConstant("<span title=\"Owner E-mail Address\">"
					+ "Owner E-mail Address" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> eMeasureID = new Column<ManageMeasureSearchModel.Result,
					SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip("" + object.geteMeasureId(), "" + object.geteMeasureId());
				}
			};
			table.addColumn(eMeasureID, SafeHtmlUtils.fromSafeConstant("<span title=\"eMeasure Id\">"
					+ "eMeasure Id" + "</span>"));
			Cell<String> historyButton = new MatButtonCell("Click to view history", "customClockButton");
			Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton) {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return "History";
				}
			};
			historyColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, String value) {
					observer.onHistoryClicked(object);
				}
			});
			table.addColumn(historyColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"History\">" + "History" + "</span>"));
			Cell<Boolean> transferCB = new MatCheckBoxCell();
			Column<Result, Boolean> transferColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(transferCB) {
				@Override
				public Boolean getValue(ManageMeasureSearchModel.Result object) {
					return object.isTransferable();
				}
			};
			transferColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, Boolean value) {
					object.setTransferable(value);
					observer.onTransferSelectedClicked(object);
				}
			});
			table.addColumn(transferColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Check for Ownership Transfer\">"
					+ "Transfer </span>"));
			table.setColumnWidth(0, 30.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
			table.setColumnWidth(3, 15.0, Unit.PCT);
			table.setColumnWidth(4, 5.0, Unit.PCT);
			table.setColumnWidth(5, 5.0, Unit.PCT);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		}
		return table;
	}
	/**
	 * TODO make use of a utility class ex. DateUtility.java though it must be
	 * usable on the client side currently it is not usable on the client side
	 * @param ts
	 *            the ts
	 * @return the string
	 */
	@SuppressWarnings("deprecation")
	public String convertTimestampToString(Timestamp ts){
		int hours = ts.getHours();
		String ap = hours < 12 ? "AM" : "PM";
		int modhours = hours % 12;
		String mins = ts.getMinutes()+"";
		if (mins.length() == 1) {
			mins = "0" + mins;
		}
		String hoursStr = modhours == 0 ? "12" : modhours + "";
		String tsStr = (ts.getMonth() + 1) + "/" + ts.getDate() + "/" + (ts.getYear() + 1900)
				+ " " + hoursStr + ":" + mins + " " + ap;
		return tsStr;
	}
	
	/**
	 * Gets the data.
	 * @return the data
	 */
	public ManageMeasureSearchModel getData() {
		return data;
	}
		 
	/**
	 * Checks if is history clicked.
	 * @return true, if is history clicked
	 */
	public boolean isHistoryClicked() {
		return isHistoryClicked;
	}
	/**
	 * Sets the data.
	 * @param data
	 *            the new data
	 */
	public void setData(ManageMeasureSearchModel data) {
		this.data = data;
	}
	/**
	 * Sets the history clicked.
	 * @param isHistoryClicked
	 *            the new history clicked
	 */
	public void setHistoryClicked(boolean isHistoryClicked) {
		this.isHistoryClicked = isHistoryClicked;
	}
	/**
	 * Sets the observer.
	 * @param observer
	 *            the new observer
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
