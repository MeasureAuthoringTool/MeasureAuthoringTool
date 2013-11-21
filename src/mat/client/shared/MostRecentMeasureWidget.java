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
/** @author jnarang */
public class MostRecentMeasureWidget extends Composite implements HasSelectionHandlers<ManageMeasureSearchModel.Result> {
	public static interface Observer {
		/** On export clicked.
		 * 
		 * @param result the result */
		public void onExportClicked(ManageMeasureSearchModel.Result result);
	}
	CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
	
	private HandlerManager handlerManager = new HandlerManager(this);
	ManageMeasureSearchModel measureSearchModel;
	private Observer observer;
	VerticalPanel searchPanel = new VerticalPanel();
	private CellTable<ManageMeasureSearchModel.Result> addColumnToTable(final CellTable<ManageMeasureSearchModel.Result> table) {
		
		if (table.getColumnCount() != 3) {
			
			Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
					new ClickableSafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant("<a cursor=\"pointer\" cursor=\"hand\" tabindex=\"0\" title='" + object.getName() + "'>");
					sb.appendEscaped(object.getName());
					sb.appendHtmlConstant("</a>");
					return sb.toSafeHtml();
				}
			};
			measureName.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
					MatContext.get().clearDVIMessages();
					SelectionEvent.fire(MostRecentMeasureWidget.this, object);
				}
			});
			table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name' tabindex=\"0\">" +"Measure Name"+ "</span>"));
			
			
			Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
					new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return getColumnToolTip(object.getVersion());
				}
			};
			table.addColumn(version, SafeHtmlUtils.fromSafeConstant("<span title='Version' tabindex=\"0\">" + "Version" + "</span>"));
			
			Cell<String> exportButton = new MatButtonCell("Click to Export", "customExportButton");
			Column<Result, String> exportColumn = new Column<ManageMeasureSearchModel.Result, String>(exportButton) {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return "Export";
				}
				
				@Override
				public void onBrowserEvent(Context context, Element elem, final ManageMeasureSearchModel.Result object, NativeEvent event) {
					if ((object != null) && object.isExportable()) {
						super.onBrowserEvent(context, elem, object, event);
					}
				}
				@Override
				public void render(Cell.Context context, ManageMeasureSearchModel.Result object, SafeHtmlBuilder sb) {
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
					SafeHtmlUtils.fromSafeConstant("<span title='Export' tabindex=\"0\">" + "Export" + "</span>"));
			table.setColumnWidth(0, 65.0, Unit.PCT);
			table.setColumnWidth(1, 30.0, Unit.PCT);
			table.setColumnWidth(2, 5.0, Unit.PCT);
			
			
		}
		return table;
	}
	
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageMeasureSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
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
		searchPanel.add(cellTable);
	}
	
	public VerticalPanel buildMostRecentWidget() {
		searchPanel.clear();
		// searchPanel.getElement().getStyle().setHeight(100, Unit.PX);
		// searchPanel.getElement().getStyle().setWidth(450, Unit.PX);
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("recentSearchPanel");
		Label searchHeader = new Label("Recent Acitvity");
		searchHeader.getElement().setId("searchHeader_Label");
		searchHeader.setStyleName("recentSearchHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		searchPanel.add(searchHeader);
		searchPanel.add(new SpacerWidget());
		if ((measureSearchModel != null) && (measureSearchModel.getData().size() > 0)) {
			buildCellTable();
		} else {
			HTML desc = new HTML("<p> No Recent Activity</p>");
			searchPanel.add(desc);
		}
		return searchPanel;
	}
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	/**
	 * Gets the column tool tip.
	 * 
	 * @param title
	 *            the title
	 * @return the column tool tip
	 */
	private SafeHtml getColumnToolTip(String title){
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='" + title + "'>" + title + "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	/** @return the measureSearchModel */
	public ManageMeasureSearchModel getMeasureSearchModel() {
		return measureSearchModel;
	}
	
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return this;
	}
	/** @param measureSearchModel the measureSearchModel to set */
	public void setMeasureSearchModel(ManageMeasureSearchModel measureSearchModel) {
		this.measureSearchModel = measureSearchModel;
	}
	/** Sets the observer.
	 * 
	 * @param observer the new observer */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
