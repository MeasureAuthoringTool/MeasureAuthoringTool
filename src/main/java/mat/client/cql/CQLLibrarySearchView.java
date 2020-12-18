package mat.client.cql;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import mat.client.CustomPager;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.resource.CellTableResource;
import mat.client.shared.CQLLibraryResultTable;
import mat.client.shared.CQLibraryGridToolbar;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.client.util.ClientConstants;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.LibrarySearchModel;
import mat.shared.SafeHtmlCell;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class CQLLibrarySearchView is used to build the CQL Library Ownership table.
 */
public class CQLLibrarySearchView implements HasSelectionHandlers<CQLLibraryDataSetObject> {

    private VerticalPanel cellTablePanel = new VerticalPanel();

    private FlowPanel mainPanel = new FlowPanel();

    private static final int PAGE_SIZE = 25;

    private List<CQLLibraryDataSetObject> availableLibrariesList;

    private HandlerManager handlerManager = new HandlerManager(this);

    private Observer observer;

    private AdminObserver adminObserver;

    private CellTable<CQLLibraryDataSetObject> table;

    private Boolean even;

    private List<String> cellTableCssStyle;

    private String cellTableEvenRow = "cellTableEvenRow";

    private String cellTableOddRow = "cellTableOddRow";

    private int index;

    private String cqlLibraryListLabel;

    List<CQLLibraryDataSetObject> selectedList;

    private CQLLibraryResultTable cqlLibraryResultTable = new CQLLibraryResultTable();

    /**
     * The Interface Observer.
     */
    public interface Observer {

        /**
         * On share clicked.
         *
         * @param result the result
         */
        void onShareClicked(CQLLibraryDataSetObject result);

        /**
         * On edit clicked.
         *
         * @param result the result
         */
        void onEditClicked(CQLLibraryDataSetObject result);

        /**
         * On history clicked.
         *
         * @param result the result
         */
        void onHistoryClicked(CQLLibraryDataSetObject result);

        /**
         * On create clicked.
         *
         * @param object the object
         */
        void onDraftOrVersionClick(CQLLibraryDataSetObject object);

        /**
         * On delete clicked.
         *
         * @param object the object
         */
        void onDeleteClicked(CQLLibraryDataSetObject object);

        void onConvertClicked(CQLLibraryDataSetObject cqlLibraryDataSetObject);
    }

    /**
     * The Interface Observer.
     */
    public interface AdminObserver {

        /**
         * On history clicked.
         *
         * @param result the result
         */
        void onHistoryClicked(CQLLibraryDataSetObject result);

        /**
         * This method is called when information about an Admin
         * which was previously requested using an asynchronous
         * interface becomes available.
         *
         * @param result the result
         */
        void onTransferSelectedClicked(CQLLibraryDataSetObject result);

    }

    /**
     * Builds the CQL library cell table.
     *
     * @return the flow panel
     */
    public FlowPanel buildCQLLibraryCellTable() {
        mainPanel.clear();
        mainPanel.getElement().setId("cqlLibrarySearchView_mainPanel");
        mainPanel.setStylePrimaryName("measureSearchResultsContainer");
        mainPanel.add(new SpacerWidget());
        mainPanel.getElement().setId("cqlCellTablePanel_VerticalPanel");
        mainPanel.add(cellTablePanel);
        mainPanel.setStyleName("serachView_mainPanel");
        return mainPanel;
    }

    public void buildCellTable(SaveCQLLibraryResult result, LibrarySearchModel model, final int filter) {
        cellTablePanel.clear();
        cellTablePanel.setStyleName("cellTablePanel");
        if ((result != null) && (result.getCqlLibraryDataSetObjects().size() > 0)) {
            table = new CellTable<>(PAGE_SIZE, (Resources) GWT.create(CellTableResource.class));
            table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
            selectedList = new ArrayList<>();
            availableLibrariesList = new ArrayList<>();
            availableLibrariesList.addAll(result.getCqlLibraryDataSetObjects());
            table.setRowData(availableLibrariesList);
            table.setRowCount(result.getResultsTotal(), true);
            table.setPageSize(PAGE_SIZE);
            table.redraw();

            AsyncDataProvider<CQLLibraryDataSetObject> provider = new AsyncDataProvider<CQLLibraryDataSetObject>() {
                @Override
                protected void onRangeChanged(HasData<CQLLibraryDataSetObject> display) {
                    final int start = display.getVisibleRange().getStart();
                    index = start;
                    AsyncCallback<SaveCQLLibraryResult> callback = new AsyncCallback<SaveCQLLibraryResult>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                        }

                        @Override
                        public void onSuccess(SaveCQLLibraryResult result) {
                            List<CQLLibraryDataSetObject> manageCQLLibrarySearchList = new ArrayList<>();
                            manageCQLLibrarySearchList.addAll(result.getCqlLibraryDataSetObjects());
                            availableLibrariesList = manageCQLLibrarySearchList;
                            buildCellTableCssStyle();
                            updateRowData(start, manageCQLLibrarySearchList);
                        }
                    };

                    model.setStartIndex(index + 1);
                    model.setPageSize(index + PAGE_SIZE);
                    MatContext.get().getCQLLibraryService().search(model, callback);
                }
            };

            provider.addDataDisplay(table);
            CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
            MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,
                    "cqlLib");
            spager.setPageStart(0);
            buildCellTableCssStyle();
            spager.setDisplay(table);
            spager.setPageSize(PAGE_SIZE);
            table.setWidth("100%");
            if (ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())) {
                setCQLLibraryListLabel("Select Libraries to Transfer Ownership.");
                addColumnToAdminTable();
                Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("CQLLibraryTransferOwnershipSummary",
                        "In the following CQL Library Transfer Ownership Cell table, CQL Library Name is given in first column,"
                                + " Owner in second column, Owner E-mail Address in third column,"
                                + "History in fourth column, Transfer Check box in fifth column");
                table.getElement().setAttribute("id", "CQLLibrarySearchCellTable");
                table.getElement().setAttribute("aria-describedby", "CQLLibraryTransferOwnershipSummary");
                table.setColumnWidth(0, 6.0, Unit.PCT);
                table.setColumnWidth(1, 15.0, Unit.PCT);
                table.setColumnWidth(2, 15.0, Unit.PCT);
                table.setColumnWidth(3, 5.0, Unit.PCT);
                table.setColumnWidth(4, 5.0, Unit.PCT);

                cellTablePanel.add(invisibleLabel);

            } else {
                Label gridPanelHeader = new Label(getCQLlibraryListLabel());
                gridPanelHeader.getElement().setId("cqlLibrarySearchHeader_Label");
                gridPanelHeader.setStyleName("recentSearchHeader");
                gridPanelHeader.getElement().setTabIndex(-1);

                CQLibraryGridToolbar gridToolbar = CQLibraryGridToolbar.withOptionsFromFlags();
                gridToolbar.getElement().setAttribute("id", "cqlLibrarySearchCellTable_gridToolbar");

                table = cqlLibraryResultTable.addColumnToTable(gridToolbar, table, CQLLibrarySearchView.this);
                Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("CQLLibrarySearchSummary",
                        "In the following CQL Library Cell table, Measure selection is given in the first column, "
                                + "CQL Library Name is given in second column, "
                                + "Version in second third, "
                                + "Model in fourth column");
                table.getElement().setAttribute("id", "CQLLibrarySearchCellTable");
                table.getElement().setAttribute("aria-describedby", "CQLLibrarySearchSummary");
                MatSimplePager topSPager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,
                        "cqlLibTopSpager");
                topSPager.setPageStart(0);
                topSPager.setDisplay(table);
                topSPager.setPageSize(PAGE_SIZE);

                cellTablePanel.add(gridPanelHeader);
                cellTablePanel.add(gridToolbar);
                cellTablePanel.add(new SpacerWidget());
                cellTablePanel.add(topSPager);
                cellTablePanel.add(new SpacerWidget());
                cellTablePanel.add(invisibleLabel);
            }

            cellTablePanel.add(table);
            cellTablePanel.add(new SpacerWidget());
            cellTablePanel.add(spager);
        } else {
            Label cqlLibrarySearchHeader = new Label(getCQLlibraryListLabel());
            cqlLibrarySearchHeader.getElement().setId("cqlLibrarySearchHeader_Label");
            cqlLibrarySearchHeader.setStyleName("recentSearchHeader");
            cqlLibrarySearchHeader.getElement().setAttribute("tabIndex", "-1");
            HTML desc = new HTML(MatContext.get().getMessageDelegate().getNoLibrarues());
            cellTablePanel.add(cqlLibrarySearchHeader);
            cellTablePanel.add(new SpacerWidget());
            cellTablePanel.add(desc);
        }
    }

    /**
     * Adds the column to table For Non Admin.
     *
     * @return the cell table
     */
    private CellTable<CQLLibraryDataSetObject> addColumnToAdminTable() {
        Label cqlLibrarySearchHeader = new Label(getCQLlibraryListLabel());
        cqlLibrarySearchHeader.getElement().setId("cqlLibrarySearchHeader_Label");
        cqlLibrarySearchHeader.setStyleName("recentSearchHeader");
        com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
        cqlLibrarySearchHeader.getElement().setAttribute("tabIndex", "-1");
        TableCaptionElement caption = elem.createCaption();
        caption.appendChild(cqlLibrarySearchHeader.getElement());

        // CQL Library Name Column
        Column<CQLLibraryDataSetObject, SafeHtml> cqlLibraryName = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new ClickableSafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return getCQLLibraryNameColumnToolTip(object);
            }
        };

        table.addColumn(cqlLibraryName,
                SafeHtmlUtils.fromSafeConstant("<span title='CQL Library Name'>" + "CQL Library Name" + "</span>"));

        // Version Column
        Column<CQLLibraryDataSetObject, SafeHtml> ownerName = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return CellTableUtility.getColumnToolTip(object.getOwnerFirstName() + "  " + object.getOwnerLastName(),
                        object.getOwnerFirstName() + "  " + object.getOwnerLastName());
            }
        };
        table.addColumn(ownerName,
                SafeHtmlUtils.fromSafeConstant("<span title='ownerName'>" + "Owner Name" + "</span>"));

        // Finalized Date
        Column<CQLLibraryDataSetObject, SafeHtml> emailAddress = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return CellTableUtility.getColumnToolTip(object.getOwnerEmailAddress());

            }
        };
        table.addColumn(emailAddress, SafeHtmlUtils
                .fromSafeConstant("<span title='Owner E-mail Address'>" + "Owner E-mail Address" + "</span>"));

        // History
        //MAT-9000. Changes for CQL Library Ownership table to use the bootstrap history column icon.
        Cell<String> historyButton = new MatButtonCell("Click to view history", "btn btn-link", "fa fa-clock-o fa-lg", "History");
        Column<CQLLibraryDataSetObject, String> historyColumn = new Column<CQLLibraryDataSetObject, String>(
                historyButton) {
            @Override
            public String getValue(CQLLibraryDataSetObject object) {
                return "";
            }
        };
        historyColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, String>() {
            @Override
            public void update(int index, CQLLibraryDataSetObject object, String value) {
                adminObserver.onHistoryClicked(object);
            }
        });
        table.addColumn(historyColumn,
                SafeHtmlUtils.fromSafeConstant("<span title='History'>" + "History" + "</span>"));

        Cell<Boolean> transferCB = new MatCheckBoxCell();
        Column<CQLLibraryDataSetObject, Boolean> transferColumn = new Column<CQLLibraryDataSetObject, Boolean>(
                transferCB) {
            @Override
            public Boolean getValue(CQLLibraryDataSetObject object) {
                if (selectedList.size() > 0) {
                    for (int i = 0; i < selectedList.size(); i++) {
                        if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
                            object.setTransferable(true);
                            break;
                        }
                    }
                } else {
                    object.setTransferable(false);
                }
                return object.isTransferable();
            }
        };
        transferColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, Boolean>() {
            @Override
            public void update(int index, CQLLibraryDataSetObject object, Boolean value) {
                if (value) {
                    if (!selectedList.contains(object)) {
                        selectedList.add(object);
                    }
                } else {
                    for (int i = 0; i < selectedList.size(); i++) {
                        if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
                            selectedList.remove(i);
                            break;
                        }
                    }
                }
                object.setTransferable(value);
                adminObserver.onTransferSelectedClicked(object);
            }
        });
        table.addColumn(transferColumn,
                SafeHtmlUtils.fromSafeConstant("<span title=\"Check for Ownership Transfer\">" + "Transfer </span>"));
        return table;
    }

    /**
     * Builds the cell table css style.
     */
    private void buildCellTableCssStyle() {
        cellTableCssStyle = new ArrayList<>();
        for (int i = 0; i < availableLibrariesList.size(); i++) {
            cellTableCssStyle.add(i, null);
        }
        table.setRowStyles(new RowStyles<CQLLibraryDataSetObject>() {
            @Override
            public String getStyleNames(CQLLibraryDataSetObject rowObject, int rowIndex) {
                if (rowIndex > PAGE_SIZE - 1) {
                    rowIndex = rowIndex - index;
                }
                if (rowIndex != 0) {
                    if (cellTableCssStyle.get(rowIndex) == null) {
                        if (even) {
                            if (rowObject.getCqlSetId()
                                    .equalsIgnoreCase(availableLibrariesList.get(rowIndex - 1).getCqlSetId())) {
                                even = true;
                                cellTableCssStyle.add(rowIndex, cellTableOddRow);
                                return cellTableOddRow;
                            } else {
                                even = false;
                                cellTableCssStyle.add(rowIndex, cellTableEvenRow);
                                return cellTableEvenRow;
                            }
                        } else {
                            if (rowObject.getCqlSetId()
                                    .equalsIgnoreCase(availableLibrariesList.get(rowIndex - 1).getCqlSetId())) {
                                even = false;
                                cellTableCssStyle.add(rowIndex, cellTableEvenRow);
                                return cellTableEvenRow;
                            } else {
                                even = true;
                                cellTableCssStyle.add(rowIndex, cellTableOddRow);
                                return cellTableOddRow;
                            }
                        }
                    } else {
                        return cellTableCssStyle.get(rowIndex);
                    }
                } else {
                    if (cellTableCssStyle.get(rowIndex) == null) {
                        even = true;
                        cellTableCssStyle.add(rowIndex, cellTableOddRow);
                        return cellTableOddRow;
                    } else {
                        return cellTableCssStyle.get(rowIndex);
                    }
                }
            }
        });
    }

    /**
     * Gets the CQL Library name column tool tip.
     *
     * @param object the object
     * @return the CQL Library name column tool tip
     */
    private SafeHtml getCQLLibraryNameColumnToolTip(CQLLibraryDataSetObject object) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        String cssClass = "customCascadeButton";
        if (object.isFamily()) {
            sb.appendHtmlConstant("<div id='container'><a href=\"javascript:void(0);\" "
                    + "style=\"text-decoration:none\">"
                    + "<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>");
            sb.appendHtmlConstant("<span id='div2' title=\" " + object.getCqlName() + "\">"
                    + object.getCqlName() + "</span>");
            sb.appendHtmlConstant("</a></div>");
        } else {
            sb.appendHtmlConstant("<div id='container'><a href=\"javascript:void(0);\" "
                    + "style=\"text-decoration:none\">");
            sb.appendHtmlConstant("<button id='div1' type=\"button\" title=\"" + object.getCqlName()
                    + "\" class=\" " + cssClass + "\"></button>");
            sb.appendHtmlConstant("<span id='div2' title=\" " + object.getCqlName() + "\">"
                    + object.getCqlName() + "</span>");
            sb.appendHtmlConstant("</a></div>");
        }
        return sb.toSafeHtml();
    }


    /**
     * Gets the cql Library List Label.
     *
     * @return the cql Library List Label
     */
    public String getCQLlibraryListLabel() {
        return cqlLibraryListLabel;
    }

    /**
     * Sets the cqlLibrary list label.
     *
     * @param cqlLibraryListLabel the new cqlLibrary list label
     */
    public void setCQLLibraryListLabel(String cqlLibraryListLabel) {
        this.cqlLibraryListLabel = cqlLibraryListLabel;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<CQLLibraryDataSetObject> handler) {
        return handlerManager.addHandler(SelectionEvent.getType(), handler);
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
     * Gets the cell table panel.
     *
     * @return the cell table panel
     */
    public VerticalPanel getCellTablePanel() {
        return cellTablePanel;
    }

    /**
     * Gets the observer.
     *
     * @return the observer
     */
    public Observer getObserver() {
        return observer;
    }

    /**
     * Sets the observer.
     *
     * @param observer the new observer
     */
    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public void setTableObserver(mat.client.cql.CQLLibrarySearchView.Observer observer) {
        this.cqlLibraryResultTable.setObserver(observer);
    }

    /**
     * Gets the admin observer.
     *
     * @return the admin observer
     */
    public AdminObserver getAdminObserver() {
        return adminObserver;
    }

    /**
     * Sets the admin observer.
     *
     * @param adminObserver the new admin observer
     */
    public void setAdminObserver(AdminObserver adminObserver) {
        this.adminObserver = adminObserver;
    }

    /**
     * Gets the available libraries list.
     *
     * @return the available libraries list
     */
    public List<CQLLibraryDataSetObject> getAvailableLibrariesList() {
        return availableLibrariesList;
    }

    /**
     * Gets the table.
     *
     * @return the table
     */
    public CellTable<CQLLibraryDataSetObject> getTable() {
        return table;
    }

    /**
     * Sets the available libraries list.
     *
     * @param availableLibrariesList the new available libraries list
     */
    public void setAvailableLibrariesList(List<CQLLibraryDataSetObject> availableLibrariesList) {
        this.availableLibrariesList = availableLibrariesList;
    }
}
