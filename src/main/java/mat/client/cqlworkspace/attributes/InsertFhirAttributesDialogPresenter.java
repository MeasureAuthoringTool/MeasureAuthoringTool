package mat.client.cqlworkspace.attributes;

import com.google.gwt.core.client.GWT;
import mat.client.cqlworkspace.shared.EditorDisplay;

public class InsertFhirAttributesDialogPresenter {
    private final EditorDisplay editor;
    private final InsertFhirAttributesDialogDisplay dialog;
    private final InsertFhirAttributesDialogModel model;
    private final AttributesCQLBuilder cqlBuilder;

    public InsertFhirAttributesDialogPresenter(InsertFhirAttributesDialogModel model, EditorDisplay editor, InsertFhirAttributesDialogDisplay dialog, AttributesCQLBuilder cqlBuilder) {
        this.editor = editor;
        this.dialog = dialog;
        this.model = model;
        this.cqlBuilder = cqlBuilder;
        bind();
    }

    private void bind() {
        dialog.getInsertButton().addClickHandler(event -> {
            String cqlToInsert = cqlBuilder.buildCQL(model);
            editor.insertAtCursor(cqlToInsert);
            editor.focus();
            dialog.hide();
        });
        dialog.getLeftPanel().addSelectionHandler(this::onSelected);
    }

    private void onSelected(FhirAttributeSelectionEvent event) {
        GWT.log("InsertFhirAttributesDialogPresenter :: onSelected " + event.toString());
        FhirAttributeModel attr = model.getAllAttributesById().get(event.getAttributeId());
        if (null == attr) {
            GWT.log("Cannot find attribute");
        }
        updateSelectedAttribute(event, attr);
        updateSelectedDataType(attr.getDataType());
        dialog.getRightPanel().update(attr.getDataType());
    }

    private void updateSelectedDataType(FhirDataTypeModel dataType) {
        boolean dataTypeSelected = false;
        for (FhirAttributeModel dataTypeAttribute : dataType.getAttributesByElement().values()) {
            dataTypeSelected = dataTypeSelected || dataTypeAttribute.isSelected();
        }
        dataType.setSelected(dataTypeSelected);
    }

    private void updateSelectedAttribute(FhirAttributeSelectionEvent event, FhirAttributeModel attr) {
        attr.setSelected(event.isSelected());
    }

    public void show() {
        dialog.show();
    }

}
