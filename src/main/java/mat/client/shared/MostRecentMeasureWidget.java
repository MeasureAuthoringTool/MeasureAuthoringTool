package mat.client.shared;

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

import java.util.ArrayList;

public class MostRecentMeasureWidget extends Composite implements HasSelectionHandlers<ManageMeasureSearchModel.Result> {

    public interface Observer {
        void onExportClicked(ManageMeasureSearchModel.Result result);
    }

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
        MeasureLibraryGridToolbar gridToolbar = MeasureLibraryGridToolbar.withOptionsFromFlags("Recent Activity");
        gridToolbar.getElement().setAttribute("id", "MostRecentActivityCellTable_gridToolbar");

        searchPanel.setStyleName("cellTablePanel");
        cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
        ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
        ArrayList<ManageMeasureSearchModel.Result> selectedMeasureList = new ArrayList<Result>();
        selectedMeasureList.addAll(measureSearchModel.getData());
        cellTable.setPageSize(2);
        cellTable.setRowCount(selectedMeasureList.size(), true);
        cellTable.redraw();
        sortProvider.refresh();
        sortProvider.getList().addAll(measureSearchModel.getData());
        cellTable = measureLibraryResultTable.addColumnToTable(gridToolbar, cellTable, MostRecentMeasureWidget.this);
        sortProvider.addDataDisplay(cellTable);
        Label invisibleLabel = (Label) LabelBuilder
                .buildInvisibleLabel(
                        "recentActivitySummary", "In the following Recent Activity table, Measure selection is given in the first column, "
                                + "Measure Name is given in second column, "
                                + "Version in third column, "
                                + "Model in fourth column");
        cellTable.getElement().setAttribute("id", "MostRecentActivityCellTable");
        cellTable.getElement().setAttribute("aria-describedby", "recentActivitySummary");

        Label measureSearchHeader = new Label("Recent Activity");
        measureSearchHeader.getElement().setId("measureSearchHeader_Label");
        measureSearchHeader.setStyleName("recentSearchHeader");

        searchPanel.add(measureSearchHeader);
        searchPanel.add(gridToolbar);

        searchPanel.add(invisibleLabel);
        searchPanel.add(cellTable);
    }

    public VerticalPanel buildMostRecentWidget() {
        searchPanel.clear();
        searchPanel.getElement().setId("searchPanel_VerticalPanel");

        if ((measureSearchModel != null) && (measureSearchModel.getData().size() > 0)) {
            cellTable = new CellTable<>();
            buildCellTable();
        } else {
            Label searchHeader = new Label("Recent Activity");
            searchHeader.getElement().setId("searchHeader_Label");
            searchHeader.setStyleName("recentSearchHeader");
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
