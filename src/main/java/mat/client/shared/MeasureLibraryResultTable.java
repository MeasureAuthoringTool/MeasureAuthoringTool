package mat.client.shared;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.thirdparty.guava.common.annotations.VisibleForTesting;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Timer;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import mat.client.Mat;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.MeasureSearchView.Observer;
import mat.client.util.CellTableUtility;
import mat.shared.model.util.MeasureDetailsUtil;

public class MeasureLibraryResultTable {

    private static final int CHECKBOX_COLUMN_WIDTH = 4;
    private static final int VERSION_COLUMN_WIDTH = 8;
    private static final int MODEL_COLUMN_WIDTH = 10;
    private static final int MOUSE_CLICK_DELAY = 300;
    private Timer singleClickTimer;
    private MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel;
    private CellTable<ManageMeasureSearchModel.Result> table;
    private Observer observer;

    public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(MeasureLibraryGridToolbar gridToolbar, CellTable<ManageMeasureSearchModel.Result> table, HasSelectionHandlers<ManageMeasureSearchModel.Result> fireEvent) {
        this.table = table;

        selectionModel = new MultiSelectionModel<>();
        table.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(event -> {
            gridToolbar.updateOnSelectionChanged(selectionModel.getSelectedSet());
        });
        addToolbarHandlers(gridToolbar, selectionModel, fireEvent);

        Column<ManageMeasureSearchModel.Result, Boolean> checkColumn = getSelectionModelColumn();
        table.addColumn(checkColumn);
        table.setColumnWidth(checkColumn, CHECKBOX_COLUMN_WIDTH, Style.Unit.PCT);

        // Measure Name Column
        Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
                new MatSafeHTMLCell()) {
            @Override
            public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
                return getMeasureNameColumnToolTip(object);
            }
        };

        table.addColumn(measureName,
                SafeHtmlUtils.fromSafeConstant("<span title='Measure Name Column'>" + "Measure Name" + "</span>"));

        // Version Column
        Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
                new MatSafeHTMLCell()) {
            @Override
            public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
                return CellTableUtility.getColumnToolTip(object.getVersion());
            }
        };
        table.addColumn(version, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Version" + "</span>"));
        table.setColumnWidth(version, VERSION_COLUMN_WIDTH, Style.Unit.PCT);

        // Measure Model Column
        Column<ManageMeasureSearchModel.Result, SafeHtml> model = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
                new MatSafeHTMLCell()) {
            @Override
            public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
                return CellTableUtility.getColumnToolTip(MeasureDetailsUtil.defaultTypeIfBlank(object.getMeasureModel()));
            }
        };
        if (MatContext.get().getMatOnFHIR().getFlagOn()) {
            table.addColumn(model, SafeHtmlUtils.fromSafeConstant("<span title='Model'>" + "Model" + "</span>"));
            table.setColumnWidth(model, MODEL_COLUMN_WIDTH, Style.Unit.PCT);
        }
        // Add event handler for table
        table.addCellPreviewHandler(event -> {
            String eventType = event.getNativeEvent().getType();
            EventTarget target = event.getNativeEvent().getCurrentEventTarget();
            Element element = Element.as(target);
            Result obj = event.getValue();
            event.getNativeEvent().preventDefault();
            if (BrowserEvents.CLICK.equalsIgnoreCase(eventType)) {
                Element input = element
                        .getElementsByTagName("tbody")
                        .getItem(0)
                        .getElementsByTagName("tr")
                        .getItem(event.getIndex())
                        .getElementsByTagName("input")
                        .getItem(0);
                obj.incrementClickCount();
                if (obj.getClickCount() == 1) {
                    singleClickTimer = new Timer() {
                        @Override
                        public void run() {
                            obj.setClickCount(0);
                            InputElement checkbox = input.cast();
                            checkbox.click();
                            selectionModel.setSelected(obj, checkbox.isChecked());
                            GWT.log("Selected? " + selectionModel.isSelected(obj));
                        }
                    };
                    singleClickTimer.schedule(MOUSE_CLICK_DELAY);
                } else if (obj.getClickCount() == 2) {
                    singleClickTimer.cancel();
                    obj.setClickCount(0);
                    SelectionEvent.fire(fireEvent, obj);
                } else {
                    event.setCanceled(true);
                }
            }
        });
        table.addStyleName("table");
        return table;
    }

    @VisibleForTesting
    void addToolbarHandlers(MeasureLibraryGridToolbar gridToolbar, MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel, HasSelectionHandlers<ManageMeasureSearchModel.Result> fireEvent) {
        gridToolbar.getVersionButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
                if (object.isDraftable() || object.isVersionable()) {
                    observer.onDraftOrVersionClick(object);
                }
            });
        });

        gridToolbar.getHistoryButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
                observer.onHistoryClicked(object);
            });
        });

        gridToolbar.getEditButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
                SelectionEvent.fire(fireEvent, object);
            });
        });

        gridToolbar.getShareButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
                if (object.isSharable()) {
                    observer.onShareClicked(object);
                }
            });
        });

        gridToolbar.getCloneButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
                if (object.isClonable()) {
                    Mat.showLoadingMessage();
                    observer.onCloneClicked(object);
                }
            });
        });

        gridToolbar.getExportButton().addClickHandler(event -> {
            List<ManageMeasureSearchModel.Result> exportList = selectionModel.getSelectedSet()
                    .stream().filter(result -> result.isExportable()).collect(Collectors.toList());
            if (exportList.size() == 1) {
                observer.onExport(exportList.iterator().next());
            } else {
                observer.onBulkExport(exportList);
            }
        });

    }

    /**
     * Gets the measure name column tool tip.
     *
     * @param object the object
     * @return the measure name column tool tip
     */
    private SafeHtml getMeasureNameColumnToolTip(ManageMeasureSearchModel.Result object) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        String cssClass = "customCascadeButton";
        String editState = MatContext.get().getMatOnFHIR().getFlagOn() ? getEditStateOfMeasure(object) : "";
        if (object.isMeasureFamily()) {
            sb.appendHtmlConstant("<div class=\"pull-left\">")
                    .appendHtmlConstant(editState)
                    .appendHtmlConstant("<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>")
                    .appendHtmlConstant("</div>");
        } else {
            sb.appendHtmlConstant("<div class=\"pull-left\">")
                    .appendHtmlConstant(editState)
                    .appendHtmlConstant("<button id='div1' type=\"button\" title=\""
                            + object.getName() + "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>")
                    .appendHtmlConstant("</div>");

        }
        sb.appendHtmlConstant("<div class=\"pull-left\" title=\" Click to open "
                + object.getName() + "\" tabindex=\"0\">" + object.getName() + "</div>");
        return sb.toSafeHtml();
    }

    /**
     * Clear bulk export check boxes.
     */
    public void clearBulkExportCheckBoxes() {
        ((MultiSelectionModel) table.getSelectionModel()).clear();
        table.redraw();
    }


    private Column<ManageMeasureSearchModel.Result, Boolean> getSelectionModelColumn() {
        // Add a selection model so we can select cells.
        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        table.setSelectionModel(selectionModel, DefaultSelectionEventManager.createCheckboxManager());
        Column<ManageMeasureSearchModel.Result, Boolean> checkColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(ManageMeasureSearchModel.Result object) {
                return selectionModel.isSelected(object);
            }
        };
        return checkColumn;
    }

    /**
     * This method creates icons relevant to the edit state of Measure Library
     *
     * @param result Result
     * @return a string containing edit state icon
     */
    private String getEditStateOfMeasure(Result result) {
        String title;
        String iconCss;
        if (result.isEditable()) {
            if (result.isMeasureLocked()) {
                String emailAddress = result.getLockedUserInfo().getEmailAddress();
                title = "Measure in use by " + emailAddress;
                iconCss = "fa fa-lock fa-lg";
            } else {
                title = "Edit";
                iconCss = "fa fa-pencil fa-lg width-14x";

            }
        } else {
            title = "Read-Only";
            iconCss = "fa fa-eye fa-lg width-14x";
        }
        return "<i class=\"pull-left edit-state " + iconCss + "\" title=\"" + title + "\"></i>";
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public MultiSelectionModel<Result> getSelectionModel() {
        return selectionModel;
    }

}
