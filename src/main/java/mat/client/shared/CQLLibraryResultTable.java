package mat.client.shared;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.MultiSelectionModel;
import mat.client.cql.CQLLibrarySearchView.Observer;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.model.util.MeasureDetailsUtil;

public class CQLLibraryResultTable {

    private Observer observer;

    public CellTable<CQLLibraryDataSetObject> addColumnToTable(CQLibraryGridToolbar gridToolbar, CellTable<CQLLibraryDataSetObject> table, HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent) {
        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        table.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(event -> {
            CQLLibraryDataSetObject selectedItem = selectionModel.getSelectedSet().isEmpty() ? null : selectionModel.getSelectedSet().iterator().next();
            gridToolbar.updateOnSelectionChanged(selectedItem);
        });
        addToolbarHandlers(gridToolbar, selectionModel);

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
        };
//		Double Click event on Grid row to navigate to Composer Tab.
        table.addDomHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                for (CQLLibraryDataSetObject object : selectionModel.getSelectedSet()) {
                    SelectionEvent.fire(fireEvent, object);
                }
            }
        }, DoubleClickEvent.getType());
        table.addColumn(cqlLibraryName,
                SafeHtmlUtils.fromSafeConstant("<span title='CQL Library Name'>" + "CQL Library Name" + "</span>"));

        // Version Column
        Column<CQLLibraryDataSetObject, SafeHtml> version = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new MatSafeHTMLCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                return CellTableUtility.getColumnToolTip(object.getVersion());
            }
        };
        table.addColumn(version, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Version" + "</span>"));

        // Library Model Type
        Column<CQLLibraryDataSetObject, SafeHtml> model = new Column<CQLLibraryDataSetObject, SafeHtml>(
                new MatSafeHTMLCell()) {
            @Override
            public SafeHtml getValue(CQLLibraryDataSetObject object) {
                if (object.getLibraryModelType() != null && !object.getLibraryModelType().isEmpty())
                    return CellTableUtility.getColumnToolTip(object.getLibraryModelType());
                else
                    return CellTableUtility.getColumnToolTip(MeasureDetailsUtil.PRE_CQL);
            }
        };
        if (MatContext.get().getMatOnFHIR().getFlagOn())
            table.addColumn(model, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Model" + "</span>"));

        return table;
    }

    void addToolbarHandlers(CQLibraryGridToolbar gridToolbar, MultiSelectionModel<CQLLibraryDataSetObject> selectionModel) {
        gridToolbar.getVersionButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream()
                    .filter(cqlLib -> cqlLib.isDraftable() || cqlLib.isVersionable())
                    .forEach(observer::onDraftOrVersionClick);
        });

        gridToolbar.getHistoryButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().forEach(observer::onHistoryClicked);
        });

        gridToolbar.getEditButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream()
                    .filter(cqlLib -> cqlLib.isEditable() && !cqlLib.isLocked())
                    .forEach(observer::onEditClicked);
        });

        gridToolbar.getShareButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream()
                    .filter(cqlLib -> cqlLib.isSharable())
                    .forEach(observer::onShareClicked);
        });

        gridToolbar.getDeleteButton().addClickHandler(event -> {
            selectionModel.getSelectedSet().stream()
                    .filter(cqlLib -> cqlLib.isDeletable())
                    .forEach(observer::onDeleteClicked);
        });
    }

    private SafeHtml getCQLLibraryNameColumnToolTip(CQLLibraryDataSetObject object) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        String cssClass = "customCascadeButton";
        String editState = MatContext.get().getMatOnFHIR().getFlagOn() ? getEditStateOfLibrary(object) : "";
        if (object.isFamily()) {
            sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
                    + "style=\"text-decoration:none\" tabindex=\"-1\">");
            sb.appendHtmlConstant(editState);
            sb.appendHtmlConstant("<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>");
            sb.appendHtmlConstant("<span id='div2' title=\" Click to open " + object.getCqlName() + "\" tabindex=\"0\">"
                    + object.getCqlName() + "</span>");
            sb.appendHtmlConstant("</a></div>");
        } else {
            sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
                    + "style=\"text-decoration:none\" tabindex=\"-1\" >");
            sb.appendHtmlConstant(editState);
            sb.appendHtmlConstant("<button id='div1' type=\"button\" title=\"" + object.getCqlName()
                    + "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>");
            sb.appendHtmlConstant("<span id='div2' title=\" Click to open " + object.getCqlName() + "\" tabindex=\"0\">"
                    + object.getCqlName() + "</span>");
            sb.appendHtmlConstant("</a></div>");
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
        return "<i class=\"pull-left " + iconCss + "\" title=\"" + title + "\"></i>";
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

}
