package mat.client.shared;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.EventTarget;
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
import com.google.gwt.view.client.MultiSelectionModel;
import mat.client.Mat;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.MeasureSearchView.Observer;
import mat.client.util.CellTableUtility;
import mat.client.util.FeatureFlagConstant;
import mat.shared.model.util.MeasureDetailsUtil;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static mat.model.clause.ModelTypeHelper.isFhir;
import static mat.model.clause.ModelTypeHelper.isQdm;

public class MeasureLibraryResultTable {

    private static final int CHECKBOX_COLUMN_WIDTH = 4;
    private static final int VERSION_COLUMN_WIDTH = 8;
    private static final int MODEL_COLUMN_WIDTH = 12;
    private static final int MOUSE_CLICK_DELAY = 300;
    private Timer singleClickTimer;
    private MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel;
    private MeasureLibraryGridToolbar gridToolbar;
    private CellTable<ManageMeasureSearchModel.Result> table;
    private Observer observer;

    public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(MeasureLibraryGridToolbar gridToolbar, CellTable<ManageMeasureSearchModel.Result> table, HasSelectionHandlers<ManageMeasureSearchModel.Result> fireEvent) {
        this.table = table;
        this.gridToolbar = gridToolbar;
        selectionModel = new MultiSelectionModel<>();
        table.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(event -> {
            gridToolbar.updateOnSelectionChanged(selectionModel.getSelectedSet());
        });
        addToolbarHandlers(fireEvent);

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
                return CellTableUtility.getColumnToolTip(MeasureDetailsUtil.getModelTypeDisplayName(object.getMeasureModel()));
            }
        };

        table.addColumn(model, SafeHtmlUtils.fromSafeConstant("<span title='Model'>" + "Models" + "</span>"));
        table.setColumnWidth(model, MODEL_COLUMN_WIDTH, Style.Unit.PCT);
        // Add event handler for table
        table.addCellPreviewHandler(event -> {
            String eventType = event.getNativeEvent().getType();
            EventTarget target = event.getNativeEvent().getCurrentEventTarget();
            Result obj = event.getValue();
            event.getNativeEvent().preventDefault();
            if (BrowserEvents.CLICK.equalsIgnoreCase(eventType)) {
                obj.incrementClickCount();
                if (obj.getClickCount() == 1) {
                    singleClickTimer = new Timer() {
                        @Override
                        public void run() {
                            obj.setClickCount(0);
                            selectionModel.setSelected(obj, !selectionModel.isSelected(obj));
                        }
                    };
                    singleClickTimer.schedule(MOUSE_CLICK_DELAY);
                } else if (obj.getClickCount() == 2  && obj.isMeasureEditOrViewable()) {
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
    void addToolbarHandlers(HasSelectionHandlers<ManageMeasureSearchModel.Result> fireEvent) {
        gridToolbar.getVersionButton().addClickHandler(event -> {
            onVersionButtonClicked(selectionModel);
        });

        gridToolbar.getHistoryButton().addClickHandler(event -> {
            onHistoryButtonClicked(selectionModel);
        });

        gridToolbar.getEditOrViewButton().addClickHandler(event -> {
            onEditViewButtonClicked(selectionModel, fireEvent);
        });

        gridToolbar.getShareButton().addClickHandler(event -> {
            onShareButtonClicked(selectionModel);
        });

        gridToolbar.getCloneButton().addClickHandler(event -> {
            onCloneButtonClicked(selectionModel);
        });

        gridToolbar.getExportButton().addClickHandler(event -> {
            onExportButtonClicked(selectionModel);
        });

        gridToolbar.getFhirValidationButton().addClickHandler(event -> {
            onFhirValidationButtonClicked(selectionModel);
        });

        gridToolbar.getConvertButton().addClickHandler(event -> {
            onConvertClicked(selectionModel);
        });
    }

    @VisibleForTesting
    void onExportButtonClicked(MultiSelectionModel<Result> selectionModel) {
        Predicate<Result> fhirExportFeatureFlag = result -> isQdm(result.getMeasureModel()) ||
                (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR) && isFhir(result.getMeasureModel()));
        List<Result> exportList = selectionModel.getSelectedSet().stream()
                .filter(fhirExportFeatureFlag)
                .collect(Collectors.toList());
        if (exportList.size() == 1) {
            observer.onExport(exportList.get(0));
        } else {
            observer.onBulkExport(exportList);
        }
    }

    @VisibleForTesting
    void onCloneButtonClicked(MultiSelectionModel<Result> selectionModel) {
        selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
            if (object.isClonable()) {
                Mat.showLoadingMessage();
                observer.onCloneClicked(object);
            }
        });
    }

    @VisibleForTesting
    void onShareButtonClicked(MultiSelectionModel<Result> selectionModel) {
        selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
            if (object.isSharable()) {
                observer.onShareClicked(object);
            }
        });
    }

    @VisibleForTesting
    void onEditViewButtonClicked(MultiSelectionModel<Result> selectionModel, HasSelectionHandlers<Result> fireEvent) {
        selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
            SelectionEvent.fire(fireEvent, object);
        });
    }

    @VisibleForTesting
    void onHistoryButtonClicked(MultiSelectionModel<Result> selectionModel) {
        selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
            observer.onHistoryClicked(object);
        });
    }

    @VisibleForTesting
    void onVersionButtonClicked(MultiSelectionModel<Result> selectionModel) {
        selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
            if (object.isDraftable() || object.isVersionable()) {
                observer.onDraftOrVersionClick(object);
            }
        });
    }

    @VisibleForTesting
    void onFhirValidationButtonClicked(MultiSelectionModel<Result> selectionModel) {
        selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
            observer.onFhirValidationClicked(object);
        });
    }

    @VisibleForTesting
    void onConvertClicked(MultiSelectionModel<Result> selectionModel) {
        selectionModel.getSelectedSet().stream().filter(Result::isFhirConvertible).findFirst().ifPresent(object -> {
            observer.onConvertMeasureFhir(object);
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
        String editState = MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR) ? getEditStateOfMeasure(object) : "";
        if (object.isMeasureFamily()) {
            sb.appendHtmlConstant("<div class=\"pull-left\">")
                    .appendHtmlConstant(editState)
                    .appendHtmlConstant("<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>")
                    .appendHtmlConstant("</div>");
        } else {
            sb.appendHtmlConstant("<div class=\"pull-left\">")
                    .appendHtmlConstant(editState)
                    .appendHtmlConstant("<button id='div1' type=\"button\" title=\""
                            + SafeHtmlUtils.htmlEscape(object.getName()) + "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>")
                    .appendHtmlConstant("</div>");

        }
        sb.appendHtmlConstant("<div class=\"pull-left\" title=\" Double-Click to open "
                + SafeHtmlUtils.htmlEscape(object.getName()) + "\" tabindex=\"0\">" + SafeHtmlUtils.htmlEscape(object.getName()) + "</div>");
        return sb.toSafeHtml();
    }

    /**
     * Clear bulk export check boxes.
     */
    public void clearBulkExportCheckBoxes() {
        selectionModel.clear();
        table.redraw();
    }


    private Column<ManageMeasureSearchModel.Result, Boolean> getSelectionModelColumn() {
        // Add a selection model so we can select cells.
        Column<ManageMeasureSearchModel.Result, Boolean> checkColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(
                new CheckboxCell(true, true)) {
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
