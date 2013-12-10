package mat.client.shared;

import java.util.ArrayList;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.shared.ClickableSafeHtmlCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
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
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

/** The Class MostRecentMeasureWidget.
 * 
 * @author jnarang */
public class MostRecentMeasureWidget extends Composite implements HasSelectionHandlers<ManageMeasureSearchModel.Result> {
	
	/** The Interface Observer.
	 * 
	 * @author jnarang. */
	public static interface Observer {
		/** On export clicked.
		 * @param result the result */
		void onExportClicked(ManageMeasureSearchModel.Result result);
	}
	/** Cell Table Column Count. */
	private static final int MAX_TABLE_COLUMN_SIZE = 3;
	/** Cell Table Instance. */
	private CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
	/** HandlerManager Instance. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** ManageMeasureSearchModel Instance. */
	private ManageMeasureSearchModel measureSearchModel;
	/** Observer Instance. */
	private Observer observer;
	/** VerticalPanel Instance which hold's View for Most Recent Measure. */
	private VerticalPanel searchPanel = new VerticalPanel();
	
	/** Method to Add Column's in Table.
	 * 
	 * @param table the table
	 * @return the cell table */
	private CellTable<Result> addColumnToTable(final CellTable<ManageMeasureSearchModel.Result> table) {
		if (table.getColumnCount() != MAX_TABLE_COLUMN_SIZE) {
			Label searchHeader = new Label("Recent Activity");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			Column<ManageMeasureSearchModel.Result, SafeHtml> measureName =
					new Column<ManageMeasureSearchModel.Result, SafeHtml>(new
							ClickableSafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant("<a href=\"#\" "
							+ "style=\"text-decoration:none\" title='" + object.getName()
							+ "' >");
					sb.appendEscaped(object.getName()); sb.appendHtmlConstant("</a>"); return sb.toSafeHtml(); } };
					measureName.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
						@Override
						public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
							MatContext.get().clearDVIMessages();
							SelectionEvent.fire(MostRecentMeasureWidget.this, object);
						}
					});
					table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant(
							"<span title='Measure Name Column'>" + "Measure Name" + "</span>"));
					Column<ManageMeasureSearchModel.Result, SafeHtml> version =
							new Column<ManageMeasureSearchModel.Result, SafeHtml>(
									new MatSafeHTMLCell()) {
						@Override
						public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
							return getColumnToolTip(object.getVersion());
						}
					};
					table.addColumn(version, SafeHtmlUtils.fromSafeConstant(
							"<span title='Version'>" + "Version" + "</span>"));
					Cell<String> exportButton = new MatButtonCell("Click to Export", "customExportButton");
					Column<Result, String> exportColumn =
							new Column<ManageMeasureSearchModel.Result, String>(exportButton) {
						@Override
						public String getValue(ManageMeasureSearchModel.Result object) {
							return "Export";
						}
						@Override
						public void onBrowserEvent(Context context, Element elem,
								final ManageMeasureSearchModel.Result object, NativeEvent event) {
							if ((object != null) && object.isExportable()) {
								super.onBrowserEvent(context, elem, object, event);
							}
						}
						@Override
						public void render(Cell.Context context, ManageMeasureSearchModel.Result object,
								SafeHtmlBuilder sb) {
							if (object.isExportable()) {
								super.render(context, object, sb);
							}
						}
					};
					exportColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
						@Override
						public void update(int index, ManageMeasureSearchModel.Result object, String value) {
							if ((object != null) && object.isExportable()) {
								observer.onExportClicked(object);
							}
						}
					});
					table.addColumn(exportColumn,
							SafeHtmlUtils.fromSafeConstant("<span title='Export'>"
									+ "Export" + "</span>"));
					table.setColumnWidth(0, 65.0, Unit.PCT);
					table.setColumnWidth(1, 30.0, Unit.PCT);
					table.setColumnWidth(2, 5.0, Unit.PCT);
		}
		return table;
	}
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler
	 * (com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageMeasureSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	/** Method to create Recent Measure Cell Table. */
	void buildCellTable() {
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
		ArrayList<ManageMeasureSearchModel.Result> selectedMeasureList = new ArrayList<Result>();
		selectedMeasureList.addAll(measureSearchModel.getData());
		cellTable.setPageSize(2);
		cellTable.redraw();
		cellTable.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(measureSearchModel.getData());
		cellTable = addColumnToTable(cellTable);
		sortProvider.addDataDisplay(cellTable);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("MostRecentMeasureActivityTable"),
				"MostRecentMeasureActivityTable");
		cellTable.getElement().setAttribute("id", "MostRecentActivityCellTable");
		cellTable.getElement().setAttribute("Summary", "Recent Measure Activity Table.");
		searchPanel.add(invisibleLabel);
		searchPanel.add(cellTable);
	}
	
	/** Builds the most recent widget.
	 * 
	 * @return VerticalPanel. */
	public VerticalPanel buildMostRecentWidget() {
		searchPanel.clear();
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("recentSearchPanel");
		if ((measureSearchModel != null) && (measureSearchModel.getData().size() > 0)) {
			buildCellTable();
		} else {
			HTML desc = new HTML("<p> No Recent Activity</p>");
			searchPanel.add(desc);
		}
		return searchPanel;
	}
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#fireEvent(com.google.gwt.event.shared.GwtEvent)
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	/**
	 * Gets the column tool tip.
	 * @param title
	 *            the title
	 * @return the column tool tip
	 */
	private SafeHtml getColumnToolTip(String title) {
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='" + title + "'>" + title + "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	/** Gets the manageMeasureSearchModel Instance.
	 * 
	 * @return the measureSearchModel */
	public ManageMeasureSearchModel getMeasureSearchModel() {
		return measureSearchModel;
	}
	
	/** Gets the select id for edit tool.
	 * 
	 * @return the select id for edit tool */
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return this;
	}
	
	/** Sets the manageMeasureSearchModel Instance.
	 * 
	 * @param measureSearchModel the measureSearchModel to set */
	public void setMeasureSearchModel(ManageMeasureSearchModel measureSearchModel) {
		this.measureSearchModel = measureSearchModel;
	}
	/** Sets the observer.
	 * @param observer the new observer */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
