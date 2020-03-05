package mat.client.cqlworkspace.attributes;

import java.util.HashMap;
import java.util.Map;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Legend;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class LightBoxRightPanelView implements LightBoxRightPanelDisplay {

    private Widget rootWidget;
    // By data type id
    private final Map<String, Widget> dataTypeEditors = new HashMap<>();
    private final Map<String, AttributeEditorDisplay> attributeEditors = new HashMap<>();

    public LightBoxRightPanelView(InsertFhirAttributesDialogModel model, String width, String height) {
        Form form = GWT.create(Form.class);
        form.ensureDebugId("LightBoxRightPanelView_form");
        form.setSize("100%", "100%");

        for (FhirDataTypeModel dataTypeEditorModel : model.getDataTypesByResource().values()) {
            FieldSet fs = new FieldSet();
            fs.ensureDebugId("LightBoxRightPanelView_form_fieldset_" + dataTypeEditorModel.getId());
            fs.add(new Legend(dataTypeEditorModel.getFhirResource()));
            fs.setVisible(false);
            for (FhirAttributeModel attr : dataTypeEditorModel.getAttributesByElement().values()) {
                AttributeEditorDisplay attributeEditor = new AttributeEditorView(attr);
                fs.add(attributeEditor.asWidget());
                attributeEditors.put(attr.getId(), attributeEditor);
            }
            dataTypeEditors.put(dataTypeEditorModel.getId(), fs);
            form.add(fs);
        }

        ScrollPanel scroller = GWT.create(ScrollPanel.class);
        scroller.setWidget(form);
        scroller.setSize(width, height);

        rootWidget = scroller;
    }

    @Override
    public Widget asWidget() {
        return rootWidget;
    }

    @Override
    public void updateDataTypeSelection(FhirDataTypeModel dataType) {
        updateDataTypeEditorVisible(dataType);
        for (FhirAttributeModel attr : dataType.getAttributesByElement().values()) {
            attributeEditors.get(attr.getId()).setSelected(attr.isSelected());
        }

    }

    private void updateDataTypeEditorVisible(FhirDataTypeModel dataType) {
        dataTypeEditors.get(dataType.getId()).setVisible(dataType.isSelected());
    }
}
