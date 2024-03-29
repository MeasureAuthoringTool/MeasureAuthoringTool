package mat.client.shared;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.BrowserEvents;
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
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.cql.CQLLibrarySearchView.Observer;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.util.CellTableUtility;
import mat.client.util.FeatureFlagConstant;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.SafeHtmlCell;
import mat.shared.model.util.MeasureDetailsUtil;

import java.util.concurrent.atomic.AtomicLong;

public class CQLLibraryResultTable {
    private static final int MOUSE_CLICK_DELAY = 300;
    private Observer observer;
    private static final long DELAY_TIME = 300;
    private CQLLibraryDataSetObject lastRowCLicked;
    private Timer singleClickTimer;

    public CellTable<CQLLibraryDataSetObject> addColumnToTable(CQLibraryGridToolbar gridToolbar, CellTable<CQLLibraryDataSetObject> table, HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent) {
        SingleSelectionModel<CQLLibraryDataSetObject> selectionModel = new SingleSelectionModel<>();
        table.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(event -> {
            CQLLibraryDataSetObject selectedItem = selectionModel.getSelectedSet().isEmpty() ? null : selectionModel.getSelectedSet().iterator().next();
            gridToolbar.updateOnSelectionChanged(selectedItem);
        });
        addToolbarHandlers(gridToolbar, selectionModel, fireEvent);

        MatCheckBoxCell selectedCell = new MatCheckBoxCell(false, true);
        final Column<CQLLibraryDataSetObject, Boolean> selectColumn = new
                Column<CQLLibraryDataSetObject, Boolean>(selectedCell) {
                    @Override
                    public Boolean getValue(CQLLibraryDataSetObject object) {
                        boolean value = selectionModel.isSelected(object);
                        selectedCell.setTitle("Select " + (value ? "checked" : "unchecked") + " " +
                                (object.isEditable() ? "editable" : "read-only") + " row " +
                                object.getCqlName() + (object.isDraft() ? "Draft" : "Version") + " " +
                                getVersionReadableText(object));
                        return selectionModel.isSelected(object);
                    }
                };

        selectColumn.setFieldUpdater((index, object, value) -> {
            selectionModel.setSelected(object, value);
        });
        table.addColumn(selectColumn);
        table.setColumnWidth(0, "45px");

        // CQL Library Name Column
        Column<CQLLibraryDataSetObject, SafeHtml> cqlLibraryName = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return getCQLLibraryNameColumnToolTip(object);
            }
        };
        table.addColumn(cqlLibraryName,
                SafeHtmlUtils.fromSafeConstant("<span title='CQL Library Name'>" + "CQL Library Name" + "</span>"));

        // Model Version Column
        Column<CQLLibraryDataSetObject, SafeHtml> modelVersion = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return CellTableUtility.getColumnToolTip(getModelVersion(object));
            }
        };
        table.addColumn(modelVersion, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Model Version" + "</span>"));

        // Version Column
        Column<CQLLibraryDataSetObject, SafeHtml> version = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return CellTableUtility.getColumnToolTip(object.getVersion());
            }
        };
        table.addColumn(version, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Version" + "</span>"));

        //Library Model Type
        Column<CQLLibraryDataSetObject, SafeHtml> model = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return CellTableUtility.getColumnToolTip(MeasureDetailsUtil.getModelTypeDisplayName(object.getLibraryModelType()));
            }
        };
        table.addColumn(model, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Models" + "</span>"));

        AtomicLong lastClickTime = new AtomicLong(System.currentTimeMillis());

        table.addCellPreviewHandler(event -> {
            String eventType = event.getNativeEvent().getType();
            CQLLibraryDataSetObject obj = event.getValue();
            if (BrowserEvents.CLICK.equalsIgnoreCase(eventType)) {
                obj.setClickCount(obj.getClickCount() + 1);
                if (obj.getClickCount() == 1) {
                    if (selectionModel.isSelected(obj)) {
                        selectionModel.setSelected(obj, false);
                    } else {
                        selectionModel.getSelectedSet().forEach(o -> selectionModel.setSelected(o, false));
                        selectionModel.setSelected(obj, true);
                    }
                    singleClickTimer = new Timer() {
                        @Override
                        public void run() {
                            obj.setClickCount(0);
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

    public String getVersionReadableText(CQLLibraryDataSetObject object) {
        if (object.getVersion() == null) {
            return "";
        } else {
            return object.getVersion().
                    replace("Version ", "").
                    replace("Draft ", "").
                    replace("v","");
        }
    }

    private void setSelected(SelectionModel model, CQLLibraryDataSetObject obj, boolean isSelected) {
        obj.setSelected(isSelected);
        model.setSelected(obj, isSelected);
    }

    private boolean isDoubleClick(CQLLibraryDataSetObject obj, long clickDuration) {
        return lastRowCLicked != null &&
                lastRowCLicked.getId() != null &&
                lastRowCLicked.getId().equals(obj.getId()) && //same row clicked twice.
                clickDuration <= DELAY_TIME; //was within delay time.
    }

    @VisibleForTesting
    void addToolbarHandlers(CQLibraryGridToolbar gridToolbar, SingleSelectionModel<CQLLibraryDataSetObject> selectionModel, HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent) {
        gridToolbar.getVersionButton().addClickHandler(event -> {
            onDraftOrVersion(selectionModel);
        });

        gridToolbar.getHistoryButton().addClickHandler(event -> {
            onHistory(selectionModel);
        });

        gridToolbar.getEditOrViewButton().addClickHandler(event -> {
            onEditOrViewClicked(selectionModel, fireEvent);
        });

        gridToolbar.getShareButton().addClickHandler(event -> {
            onShare(selectionModel);
        });

        gridToolbar.getDeleteButton().addClickHandler(event -> {
            onDelete(selectionModel);
        });

        gridToolbar.getConvertButton().addClickHandler(event -> {
            onConvert(selectionModel);
        });
    }

    @VisibleForTesting
    void onConvert(SingleSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().stream()
                .filter(cqlLib -> cqlLib.isFhirConvertible())
                .forEach(observer::onConvertClicked);
    }

    @VisibleForTesting
    void onDelete(SingleSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().stream()
                .filter(cqlLib -> cqlLib.isDeletable())
                .forEach(observer::onDeleteClicked);
    }

    @VisibleForTesting
    void onShare(SingleSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().stream()
                .filter(cqlLib -> cqlLib.isSharable())
                .forEach(observer::onShareClicked);
    }

    @VisibleForTesting
    void onEditOrViewClicked(SingleSelectionModel<CQLLibraryDataSetObject> selectionModel, HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent) {
        selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
            if (!object.isEditable()) {
                SelectionEvent.fire(fireEvent, object);
            } else if (!object.isLocked()) {
                observer.onEditClicked(object);
            }
        });
    }

    @VisibleForTesting
    void onHistory(SingleSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().forEach(observer::onHistoryClicked);
    }

    @VisibleForTesting
    void onDraftOrVersion(SingleSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().stream()
                .filter(cqlLib -> cqlLib.isDraftable() || cqlLib.isVersionable())
                .forEach(observer::onDraftOrVersionClick);
    }

    private SafeHtml getCQLLibraryNameColumnToolTip(CQLLibraryDataSetObject object) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        String cssClass = "customCascadeButton";
        String editState = MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR) ? getEditStateOfLibrary(object) : "";
        if (object.isFamily()) {
            sb.appendHtmlConstant("<div>");
            sb.appendHtmlConstant(editState);
            sb.appendHtmlConstant("<span id='div1' class='textEmptySpaces'></span>");
            sb.appendHtmlConstant("<span id='div2' title=\" Double-Click to open " + object.getCqlName() + "\">"
                    + object.getCqlName() + "</span>");
            sb.appendHtmlConstant("</div>");
        } else {
            sb.appendHtmlConstant("<div>");
            sb.appendHtmlConstant(editState);
            sb.appendHtmlConstant("<span id='div1' disabled title=\"" + object.getCqlName()
                    + "\" class=\" " + cssClass + "\"></span>");
            sb.appendHtmlConstant("<span id='div2' title=\" Double-Click to open " + object.getCqlName() + "\">"
                    + object.getCqlName() + "</span>");
            sb.appendHtmlConstant("</div>");
        }
        return sb.toSafeHtml();
    }

    /**
     * This method creates icons relevant to the edit state of CQL Library
     *
     * @param object CQLLibraryDataSetObject
     * @return string containing edit state icon
     */
    private String getEditStateOfLibrary(CQLLibraryDataSetObject object) {
        String title;
        String iconCss;
        if (object.isEditable()) {
            if (object.isLocked()) {
                String emailAddress = object.getLockedUserInfo().getEmailAddress();
                title = "Library in use by " + emailAddress;
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

    private String getModelVersion(CQLLibraryDataSetObject object) {
        if (ModelTypeHelper.isFhir(object.getLibraryModelType()) && object.getFhirVersion() != null) {
            return object.getFhirVersion();
        } else if (ModelTypeHelper.isQdm(object.getLibraryModelType()) && object.getQdmVersion() != null) {
            return object.getQdmVersion();
        } else {
            return " ";
        }
    }
}
