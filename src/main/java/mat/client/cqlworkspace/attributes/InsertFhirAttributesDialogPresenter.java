package mat.client.cqlworkspace.attributes;

import java.util.logging.Logger;

import mat.client.cqlworkspace.shared.EditorDisplay;

public class InsertFhirAttributesDialogPresenter {
    private Logger logger = Logger.getLogger("");
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
        logger.info("InsertFhirAttributesDialogPresenter :: onSelected " + event.toString());
        FhirAttributeModel attr = model.getAllAttributesById().get(event.getAttributeId());
        if (null == attr) {
            logger.info("Cannot find attribute");
        }
        updateSelectedAttribute(event, attr);
        updateSelectedDataType(attr.getDataType());
        dialog.getRightPanel().updateDataTypeSelection(attr.getDataType());
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
