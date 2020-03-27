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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.model.cql.CQLLibraryDataSetObject;

public class MostRecentCQLLibraryWidget extends Composite implements HasSelectionHandlers<CQLLibraryDataSetObject> {

    private CellTable<CQLLibraryDataSetObject> cellTable;

    private HandlerManager handlerManager = new HandlerManager(this);

    private SaveCQLLibraryResult result;

    private VerticalPanel searchPanel = new VerticalPanel();

    private CQLLibraryResultTable cqlLibraryResultTable = new CQLLibraryResultTable();

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<CQLLibraryDataSetObject> handler) {
        return handlerManager.addHandler(SelectionEvent.getType(), handler);
    }

    /**
     * Method to create Recent Measure Cell Table.
     */
    void buildCellTable() {
        String gridCaption = "Recent Activity";

        Label gridPanelHeader = new Label(gridCaption);
        gridPanelHeader.getElement().setId("cqlLibrarySearchHeader_Label");
        gridPanelHeader.setStyleName("recentSearchHeader");
        gridPanelHeader.getElement().setTabIndex(0);

        searchPanel.add(gridPanelHeader);


        CQLibraryGridToolbar gridToolbar = CQLibraryGridToolbar.withOptionsFromFlags();
        gridToolbar.getElement().setAttribute("id", "recentActivitySummary_gridToolbar");

        cellTable.setWidth("100%");
        cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
        ListDataProvider<CQLLibraryDataSetObject> sortProvider = new ListDataProvider<>();
        cellTable.setPageSize(2);
        cellTable.redraw();
        cellTable.setRowCount(result.getCqlLibraryDataSetObjects().size(), true);
        sortProvider.refresh();
        sortProvider.getList().addAll(result.getCqlLibraryDataSetObjects());
        cellTable = cqlLibraryResultTable.addColumnToTable(gridToolbar, cellTable, MostRecentCQLLibraryWidget.this);
        sortProvider.addDataDisplay(cellTable);
        Label invisibleLabel = (Label) LabelBuilder
                .buildInvisibleLabel(
                        "recentActivitySummary", "In the following Recent Activity table, Measure selection is given in the first column, "
                                + "CQL Library Name is given in second column, "
                                + "Version in third column, "
                                + "Model in fourth column");
        cellTable.getElement().setAttribute("aria-describedby", "recentActivitySummary");
        searchPanel.add(invisibleLabel);

        FlowPanel gridBody = new FlowPanel();
        gridBody.add(gridToolbar);
        gridBody.add(cellTable);

        searchPanel.add(gridBody);
    }

    /**
     * Builds the most recent widget.
     *
     * @return VerticalPanel.
     */
    public VerticalPanel buildMostRecentWidget() {
        searchPanel.clear();
        searchPanel.getElement().setId("searchPanel_VerticalPanel");
        searchPanel.setStyleName("cellTablePanel");
        searchPanel.setWidth("915px");
        if ((result != null) && (result.getCqlLibraryDataSetObjects().size() > 0)) {
            cellTable = new CellTable<CQLLibraryDataSetObject>();
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

        }
        return searchPanel;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    public SaveCQLLibraryResult getResult() {
        return result;
    }

    public HasSelectionHandlers<CQLLibraryDataSetObject> getSelectIdForEditTool() {
        return this;
    }

    public void setResult(SaveCQLLibraryResult result) {
        this.result = result;
    }

    public void setTableObserver(mat.client.cql.CQLLibrarySearchView.Observer observer) {
        this.cqlLibraryResultTable.setObserver(observer);
    }
}
