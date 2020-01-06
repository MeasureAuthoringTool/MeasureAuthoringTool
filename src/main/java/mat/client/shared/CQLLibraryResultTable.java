package mat.client.shared;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.thirdparty.guava.common.annotations.VisibleForTesting;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.MultiSelectionModel;
import mat.client.cql.CQLLibrarySearchView.Observer;
import mat.client.util.CellTableUtility;
import mat.client.util.FeatureFlagConstant;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.model.util.MeasureDetailsUtil;

public class CQLLibraryResultTable {

    private Observer observer;
    private final int DELAY_TIME = 500;

    public CellTable<CQLLibraryDataSetObject> addColumnToTable(CQLibraryGridToolbar gridToolbar, CellTable<CQLLibraryDataSetObject> table, HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent) {
        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        table.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(event -> {
            CQLLibraryDataSetObject selectedItem = selectionModel.getSelectedSet().isEmpty() ? null : selectionModel.getSelectedSet().iterator().next();
            gridToolbar.updateOnSelectionChanged(selectedItem);
        });
        addToolbarHandlers(gridToolbar, selectionModel, fireEvent);

        CheckboxCell selectedCell = new CheckboxCell(true, false);
        Column<CQLLibraryDataSetObject, Boolean> selectedCol = new Column<CQLLibraryDataSetObject, Boolean>(selectedCell) {
            @Override
            public Boolean getValue(CQLLibraryDataSetObject object) {
                return selectionModel.isSelected(object);
            }
        };
        selectedCol.setFieldUpdater((int index, CQLLibraryDataSetObject object, Boolean value) -> {
            object.setSelected(value);
        });

        table.addColumn(selectedCol);
        table.setColumnWidth(0, "45px");

        // CQL Library Name Column
        Column<CQLLibraryDataSetObject, SafeHtml> cqlLibraryName = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new ClickableSafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return getCQLLibraryNameColumnToolTip(object);
            }

            /*
                Single Click to select row and enable checkbox
                Double Click to navigate to CQL Composer tab
            */
            @Override
            public void onBrowserEvent(Cell.Context context, Element elem, CQLLibraryDataSetObject object, NativeEvent event) {
                if (object.getLastClick() < System.currentTimeMillis() - DELAY_TIME) {
                    object.setSelected(!object.isSelected());
                    selectionModel.setSelected(object, object.isSelected());
                } else if (object.isFhirEditableFeatureFlag()) {
                    SelectionEvent.fire(fireEvent, object);
                }
                object.setLastClick(System.currentTimeMillis());
            }
        };
        table.addColumn(cqlLibraryName,
                SafeHtmlUtils.fromSafeConstant("<span title='CQL Library Name'>" + "CQL Library Name" + "</span>"));

        // Version Column
        Column<CQLLibraryDataSetObject, SafeHtml> version = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new ClickableSafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return CellTableUtility.getColumnToolTip(object.getVersion());
            }

            /*
                Single Click to select row and enable checkbox
                Double Click to navigate to CQL Composer tab
            */
            @Override
            public void onBrowserEvent(Cell.Context context, Element elem, CQLLibraryDataSetObject object, NativeEvent event) {
                if (object.getLastClick() < System.currentTimeMillis() - DELAY_TIME) {
                    object.setSelected(!object.isSelected());
                    selectionModel.setSelected(object, object.isSelected());
                } else if (object.isFhirEditableFeatureFlag()) {
                    SelectionEvent.fire(fireEvent, object);
                }
                object.setLastClick(System.currentTimeMillis());
            }
        };
        table.addColumn(version, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Version" + "</span>"));

        //Library Model Type
        Column<CQLLibraryDataSetObject, SafeHtml> model = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new ClickableSafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return CellTableUtility.getColumnToolTip(MeasureDetailsUtil.defaultTypeIfBlank(object.getLibraryModelType()));
            }

            /*
                Single Click to select row and enable checkbox
                Double Click to navigate to CQL Composer tab
            */
            @Override
            public void onBrowserEvent(Cell.Context context, Element elem, CQLLibraryDataSetObject object, NativeEvent event) {
                if (object.getLastClick() < System.currentTimeMillis() - DELAY_TIME) {
                    object.setSelected(!object.isSelected());
                    selectionModel.setSelected(object, object.isSelected());
                } else if (object.isFhirEditableFeatureFlag()) {
                    SelectionEvent.fire(fireEvent, object);
                }
                object.setLastClick(System.currentTimeMillis());
            }
        };
        if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR))
            table.addColumn(model, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Model" + "</span>"));

        table.addStyleName("table");

        return table;
    }

    @VisibleForTesting
    void addToolbarHandlers(CQLibraryGridToolbar gridToolbar, MultiSelectionModel<CQLLibraryDataSetObject> selectionModel, HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent) {
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
    }

    @VisibleForTesting
    void onDelete(MultiSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().stream()
                .filter(cqlLib -> cqlLib.isDeletable())
                .forEach(observer::onDeleteClicked);
    }

    @VisibleForTesting
    void onShare(MultiSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().stream()
                .filter(cqlLib -> cqlLib.isSharable())
                .forEach(observer::onShareClicked);
    }

    @VisibleForTesting
    void onEditOrViewClicked(MultiSelectionModel<CQLLibraryDataSetObject> selectionModel, HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent) {
        selectionModel.getSelectedSet().stream().findFirst().ifPresent(object -> {
            if (!object.isEditable()) {
                SelectionEvent.fire(fireEvent, object);
            } else if (!object.isLocked()) {
                observer.onEditClicked(object);
            }
        });
    }

    @VisibleForTesting
    void onHistory(MultiSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().forEach(observer::onHistoryClicked);
    }

    @VisibleForTesting
    void onDraftOrVersion(MultiSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        selectionModel.getSelectedSet().stream()
                .filter(cqlLib -> cqlLib.isDraftable() || cqlLib.isVersionable())
                .forEach(observer::onDraftOrVersionClick);
    }

    private SafeHtml getCQLLibraryNameColumnToolTip(CQLLibraryDataSetObject object) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        String cssClass = "customCascadeButton";
        String editState = MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR) ? getEditStateOfLibrary(object) : "";
        if (object.isFamily()) {
            sb.appendHtmlConstant("<div tabindex=\"-1\">");
            sb.appendHtmlConstant(editState);
            sb.appendHtmlConstant("<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>");
            sb.appendHtmlConstant("<span id='div2' title=\" Click to open " + object.getCqlName() + "\" tabindex=\"0\">"
                    + object.getCqlName() + "</span>");
            sb.appendHtmlConstant("</div>");
        } else {
            sb.appendHtmlConstant("<div tabindex=\"-1\">");
            sb.appendHtmlConstant(editState);
            sb.appendHtmlConstant("<button id='div1' type=\"button\" title=\"" + object.getCqlName()
                    + "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>");
            sb.appendHtmlConstant("<span id='div2' title=\" Click to open " + object.getCqlName() + "\" tabindex=\"0\">"
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

}
