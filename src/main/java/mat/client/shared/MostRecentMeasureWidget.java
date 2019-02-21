package mat.client.shared;

import java.util.ArrayList;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;

public class MostRecentMeasureWidget extends Composite implements HasSelectionHandlers<ManageMeasureSearchModel.Result> {
	
	public static interface Observer {
		void onExportClicked(ManageMeasureSearchModel.Result result);
	}
	
	private static final int MAX_TABLE_COLUMN_SIZE = 4;
	private CellTable<ManageMeasureSearchModel.Result> cellTable;
	private HandlerManager handlerManager = new HandlerManager(this);
	private ManageMeasureSearchModel measureSearchModel;
	private Observer observer;
	private VerticalPanel searchPanel = new VerticalPanel();
	private MeasureLibraryResultTable measureLibraryResultTable = new MeasureLibraryResultTable();

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
		cellTable = measureLibraryResultTable.addColumnToTable("Recent Activity", cellTable, selectedMeasureList, false, MostRecentMeasureWidget.this);
		sortProvider.addDataDisplay(cellTable);
		Label invisibleLabel = (Label) LabelBuilder
				.buildInvisibleLabel(
						"recentActivitySummary", "In the following Recent Activity table, Measure Name is given in first column,"
								+ " Version in second column, Version/Draft in third column for creating version/draft,"
								+ "History in fourth column, Edit in fifth column, Share in sixth column"
								+ "Clone in seventh column and Export in eight column.");
		cellTable.getElement().setAttribute("id", "MostRecentActivityCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "recentActivitySummary");
		searchPanel.add(invisibleLabel);
		searchPanel.add(cellTable);
	}

	public VerticalPanel buildMostRecentWidget() {
		searchPanel.clear();
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("recentSearchPanel");

		if ((measureSearchModel != null) && (measureSearchModel.getData().size() > 0)) {
			cellTable = new CellTable<ManageMeasureSearchModel.Result>();
			buildCellTable();
		} else {
			Label searchHeader = new Label("Recent Activity");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No Recent Activity</p>");
			searchPanel.add(searchHeader);
			searchPanel.add(new SpacerWidget());
			searchPanel.add(desc);
			searchPanel.setWidth("915px");
		}
		return searchPanel;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	
	public ManageMeasureSearchModel getMeasureSearchModel() {
		return measureSearchModel;
	}
	
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return this;
	}
	
	public void setMeasureSearchModel(ManageMeasureSearchModel measureSearchModel) {
		this.measureSearchModel = measureSearchModel;
	}

	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	public void setTableObserver(mat.client.measure.MeasureSearchView.Observer observer) {
		measureLibraryResultTable.setObserver(observer);
	}
}
