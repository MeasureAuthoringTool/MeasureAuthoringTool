package mat.client.measure.measuredetails.views;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.CustomPager;
import mat.client.cqlworkspace.DeleteConfirmationDialogBox;
import mat.client.measure.ReferenceTextAndType;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.ReferencesObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessagePanel;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.shared.SafeHtmlCell;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;
import mat.shared.measure.measuredetails.models.ReferencesModel;

import java.util.Arrays;

public class ReferencesView implements MeasureDetailViewInterface {
    public static final int MAX_TEXT_DISPLAY = 35;
    private FlowPanel mainPanel = new FlowPanel();
    private MeasureDetailsTextEditor measureDetailsTextEditor;
    private ReferencesModel originalModel;
    private ReferencesModel referencesModel;
    private ReferencesObserver observer;
    private MessagePanel messagePanel;
    private int editingIndex = 0;
    private boolean isReadOnly = false;
    private ListBox referenceTypeEditor;

    public ReferencesView(ReferencesModel originalModel) {
        this.originalModel = originalModel;
        this.referencesModel = new ReferencesModel(originalModel);
        editingIndex = referencesModel.getReferences().size();
        messagePanel = new MessagePanel();
        buildDetailView();
    }

    private void addColumnToTable(CellTable<ReferenceTextAndType> referencesTable) {
        Label measureSearchHeader = new Label("References List");
        measureSearchHeader.getElement().setId("referencesHeader_Label");
        measureSearchHeader.setStyleName("invisibleTableCaption");
        com.google.gwt.dom.client.TableElement elem = referencesTable.getElement().cast();
        measureSearchHeader.getElement().setAttribute("tabIndex", "0");
        TableCaptionElement caption = elem.createCaption();
        caption.appendChild(measureSearchHeader.getElement());
        Column<ReferenceTextAndType, SafeHtml> descriptionColumn = new Column<ReferenceTextAndType, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(ReferenceTextAndType ref) {
                String origRefText = ref.getReferenceText();
                String reference = origRefText.length() > MAX_TEXT_DISPLAY ? origRefText.substring(0, MAX_TEXT_DISPLAY) + "..." : origRefText;
                return CellTableUtility.getColumnToolTip(SafeHtmlUtils.htmlEscape(reference));
            }
        };
        referencesTable.addColumn(descriptionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Description\">" + "Description" + "</span>"));


        Column<ReferenceTextAndType, SafeHtml> referenceTypeColumn = new Column<ReferenceTextAndType, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(ReferenceTextAndType ref) {
                return CellTableUtility.getColumnToolTip(SafeHtmlUtils.htmlEscape(ref.getReferenceType().getDisplayName()));
            }
        };
        referencesTable.addColumn(referenceTypeColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Reference Type\">" + "Reference Type" + "</span>"));

        Column<ReferenceTextAndType, SafeHtml> editColumn = new Column<ReferenceTextAndType, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(ReferenceTextAndType ref) {
                return getEditColumnToolTip();
            }
        };

        editColumn.setFieldUpdater(new FieldUpdater<ReferenceTextAndType, SafeHtml>() {
            @Override
            public void update(int index, ReferenceTextAndType ref, SafeHtml value) {
                if (isEditorDirty(ref)) {
                    displayDirtyCheck();
                    messagePanel.getWarningConfirmationYesButton().addClickHandler(event -> handleYesButtonClicked(index));
                    messagePanel.getWarningConfirmationNoButton().addClickHandler(event -> hideDirtyCheck());
                    messagePanel.getWarningConfirmationYesButton().setFocus(true);
                } else {
                    observer.handleEditClicked(index);
                }
            }
        });

        String columnText = isReadOnly ? "View" : "Edit";
        referencesTable.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Index\">" + columnText + "</span>"));

        Column<ReferenceTextAndType, SafeHtml> deleteColumn = new Column<ReferenceTextAndType, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(ReferenceTextAndType ref) {
                return getDeleteColumnToolTip();
            }
        };
        deleteColumn.setFieldUpdater(new FieldUpdater<ReferenceTextAndType, SafeHtml>() {
            @Override
            public void update(int index, ReferenceTextAndType ref, SafeHtml value) {
                if (!isReadOnly) {
                    displayDeleteConfirmationDialog(index, ref.getReferenceText());
                }
            }
        });
        referencesTable.addColumn(deleteColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Index\">" + "Delete" + "</span>"));
    }

    private void displayDeleteConfirmationDialog(int index, String object) {
        DeleteConfirmationDialogBox deleteConfirmation = new DeleteConfirmationDialogBox();
        deleteConfirmation.getMessageAlert().createAlert("You have selected to delete reference: " + SafeHtmlUtils.htmlEscape(object.length() > 60 ? object.substring(0, 59) : object) + ". Please confirm that you want to remove this reference permanently.");
        deleteConfirmation.getYesButton().addClickHandler(event -> observer.handleDeleteReference(index));
        deleteConfirmation.show();
    }

    public void displayDirtyCheck() {
        hideDirtyCheck();
        messagePanel.getWarningConfirmationMessageAlert().createWarningAlert();
    }

    public void hideDirtyCheck() {
        messagePanel.clearAlerts();
    }

    private void handleYesButtonClicked(int index) {
        observer.handleEditClicked(index);
        hideDirtyCheck();
    }

    private SafeHtml getEditColumnToolTip() {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        String title = isReadOnly ? "View" : "Edit";
        String cssColor = isReadOnly ? "black" : "darkgoldenrod";
        String cssClass = "btn btn-link";
        String iconCss = isReadOnly ? "fa fa-binoculars fa-lg" : "fa fa-pencil fa-lg";
        sb.appendHtmlConstant("<button type=\"button\" title='"
                + title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: " + cssColor + ";\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
        return sb.toSafeHtml();
    }

    private SafeHtml getDeleteColumnToolTip() {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        String title = "Delete";
        String cssClass = "btn btn-link";
        String iconCss = "fa fa-trash fa-lg";
        String disabledMarkup = isReadOnly ? "disabled " : "";
        sb.appendHtmlConstant("<button type=\"button\" title='"
                + title + "' tabindex=\"0\" class=\"" + cssClass + "\" style=\"margin-left: 0px;\"" + disabledMarkup + "\"> <i class=\"" + iconCss + "\"></i><span style=\"font-size:0;\")>Delete</button>");
        return sb.toSafeHtml();
    }


    @Override
    public Widget getWidget() {
        return mainPanel;
    }

    @Override
    public boolean hasUnsavedChanges() {
        return false;
    }

    @Override
    public void buildDetailView() {
        mainPanel.clear();
        VerticalPanel cellTablePanel = new VerticalPanel();
        cellTablePanel.setStyleName("cellTablePanelMeasureDetails");
        cellTablePanel.setWidth("625px");
        cellTablePanel.add(messagePanel);
        CellTable<ReferenceTextAndType> referencesTable = new CellTable<>();
        referencesTable.setPageSize(5);
        referencesTable.redraw();
        referencesTable.setRowCount(referencesModel.getReferences().size(), true);
        ListDataProvider<ReferenceTextAndType> listDataProvider = new ListDataProvider<>();
        listDataProvider.refresh();
        listDataProvider.getList().addAll(referencesModel.getReferences());

        if (referencesModel.getReferences() != null && referencesModel.getReferences().size() > 0) {
            addColumnToTable(referencesTable);
            listDataProvider.addDataDisplay(referencesTable);
            CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
            MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true, "manageReferences");
            spager.setPageStart(0);
            spager.setDisplay(referencesTable);
            spager.setPageSize(5);
            referencesTable.setWidth("100%");
            referencesTable.setColumnWidth(0, 55.0, Unit.PCT);
            referencesTable.setColumnWidth(1, 15.0, Unit.PCT);
            referencesTable.setColumnWidth(2, 15.0, Unit.PCT);
            referencesTable.setColumnWidth(3, 15.0, Unit.PCT);
            referencesTable.setColumnWidth(4, 15.0, Unit.PCT);
            Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
                    "manageReferencesSummary",
                    "In the Following Manage references table, Index Number is given in the first column, Description in the "
                            + "second column, Reference type in the third column,"
                            + " Edit Reference in the fourth column, and DeleteReference in the fifth column.");
            referencesTable.getElement().setAttribute("id", "manageReferencesCellTable");
            referencesTable.getElement().setAttribute("aria-describedby", "manageReferencesSummary");
            cellTablePanel.add(invisibleLabel);
            cellTablePanel.add(referencesTable);
            cellTablePanel.add(new SpacerWidget());
            cellTablePanel.add(spager);
            mainPanel.add(cellTablePanel);
        }

        measureDetailsTextEditor = new MeasureDetailsTextEditor();
        mainPanel.add(measureDetailsTextEditor);
        measureDetailsTextEditor.setTitle("References");

        VerticalPanel referenceTypePanel = buildReferenceTypePanel();
        mainPanel.add(new SpacerWidget());
        mainPanel.add(referenceTypePanel);

        addEventHandlers();
    }

    private VerticalPanel buildReferenceTypePanel() {
        referenceTypeEditor = new ListBox();
        referenceTypeEditor.setMultipleSelect(false);
        Arrays.stream(MeasureReferenceType.values()).forEach(refType -> {
            referenceTypeEditor.addItem(refType.getDisplayName(), refType.name());
        });
        referenceTypeEditor.setSelectedIndex(MeasureReferenceType.CITATION.ordinal());
        referenceTypeEditor.setEnabled(!isReadOnly);
        VerticalPanel referenceTypePanel = new VerticalPanel();
        referenceTypePanel.add(new Label("Reference Type:"));
        referenceTypePanel.add(referenceTypeEditor);
        return referenceTypePanel;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
        buildDetailView();
        measureDetailsTextEditor.setReadOnly(readOnly);
    }

    @Override
    public ConfirmationDialogBox getSaveConfirmation() {
        return null;
    }

    @Override
    public void resetForm() {
        isReadOnly = false;
        editingIndex = referencesModel.getReferences().size();
        hideDirtyCheck();
        this.referencesModel = new ReferencesModel(originalModel);
        measureDetailsTextEditor.setText("");
        buildDetailView();
    }

    @Override
    public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
        return referencesModel;
    }

    @Override
    public TextArea getTextEditor() {
        return measureDetailsTextEditor.getTextEditor();
    }

    @Override
    public void clear() {
        hideDirtyCheck();
        measureDetailsTextEditor.setText("");
        referenceTypeEditor.setSelectedIndex(MeasureReferenceType.CITATION.ordinal());
        referenceTypeEditor.setEnabled(!isReadOnly);
    }

    @Override
    public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
        this.referencesModel = (ReferencesModel) model;
    }

    @Override
    public void setObserver(MeasureDetailsComponentObserver observer) {
        this.observer = (ReferencesObserver) observer;
    }

    @Override
    public MeasureDetailsComponentObserver getObserver() {
        return observer;
    }

    private void addEventHandlers() {
        measureDetailsTextEditor.getTextEditor().addKeyUpHandler(event -> observer.handleTextValueChanged());
    }

    public boolean isEditorDirty(ReferenceTextAndType ref) {
        String textValue = measureDetailsTextEditor.getText() == null ? "" : measureDetailsTextEditor.getText().trim();
        String typeValue = referenceTypeEditor.getSelectedValue();
        boolean textChanged = !textValue.equals(ref.getReferenceText());
        boolean typeChanged = !typeValue.equals(ref.getReferenceType().name());
        boolean refChanged = textChanged || typeChanged;
        return (!isReadOnly && (!textValue.isEmpty() && refChanged));
    }

    public boolean isEditorDirty() {
        String textValue = measureDetailsTextEditor.getText() == null ? "" : measureDetailsTextEditor.getText().trim();
        return !isReadOnly && !textValue.isEmpty();
    }

    public Integer getEditingIndex() {
        return editingIndex;
    }

    public void setEditingIndex(Integer editingIndex) {
        this.editingIndex = editingIndex;
    }

    public ReferencesModel getReferencesModel() {
        return referencesModel;
    }

    public void setReferencesModel(ReferencesModel referencesModel) {
        this.referencesModel = referencesModel;
    }

    @Override
    public Widget getFirstElement() {
        return null;
    }

    public ReferencesModel getOriginalModel() {
        return originalModel;
    }

    public void setOriginalModel(ReferencesModel originalModel) {
        this.originalModel = originalModel;
    }

    public ListBox getReferenceTypeEditor() {
        return referenceTypeEditor;
    }

}
