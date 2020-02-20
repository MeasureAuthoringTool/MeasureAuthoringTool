package mat.client.cqlworkspace.attributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Legend;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class LightBoxRightPanelView implements LightBoxRightPanelDisplay {

    private static final Map<String, InputType> INPUT_TYPE_MAP;

    static {
        Map<String, InputType> inputTypeMap = new HashMap<>();
        inputTypeMap.put("ID", InputType.TEXT);
        inputTypeMap.put("IDENTIFIER", InputType.TEXT);
        inputTypeMap.put("CODEABLECONCEPT", InputType.TEXT);
        inputTypeMap.put("DATETIME", InputType.DATETIME_LOCAL);
        inputTypeMap.put("INSTANT", InputType.DATETIME);
        inputTypeMap.put("DATE", InputType.DATE);
        inputTypeMap.put("TIMING", InputType.TIME);
        inputTypeMap.put("REFERENCE", InputType.TEXT);
        inputTypeMap.put("RESOURCE", InputType.TEXT);
        INPUT_TYPE_MAP = Collections.unmodifiableMap(inputTypeMap);
    }

    private Widget rootWidget;
    // By data type id
    private final Map<String, Widget> dataTypeEditors = new HashMap<>();
    private final Map<String, Widget> attributeEditors = new HashMap<>();

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
                // TODO create input type based on the attribute type
                // TODO bind back to model
                // TODO Multiplier
                FormGroup fg = GWT.create(FormGroup.class);
                String attributeId = attr.getId();
                fg.ensureDebugId("LightBoxRightPanelView_form_group_" + attributeId);
                fg.setVisible(false);
                FormLabel label = GWT.create(FormLabel.class);
                String inputId = "LightBoxRightPanelView_form_input_" + attributeId;
                label.setText(attr.getFhirElement());
                label.setFor(inputId);
                fg.add(label);
                Input input = newInputForAttributeType(attr.getFhirType());
                input.setId(inputId);
                input.setWidth("300px");
                HorizontalPanel multiplierInput = new HorizontalPanel();
                multiplierInput.add(input);
                AddMultiplierButton addButton = new AddMultiplierButton("LightBoxRightPanelView_form_group_" + attributeId);
                multiplierInput.add(addButton);
                RemoveMultiplierButton removeButton = new RemoveMultiplierButton("LightBoxRightPanelView_form_group_" + attributeId);
                multiplierInput.add(removeButton);
                fg.add(multiplierInput);
                fs.add(fg);
                attributeEditors.put(attributeId, fg);
            }
            dataTypeEditors.put(dataTypeEditorModel.getId(), fs);
            form.add(fs);
        }

        ScrollPanel scroller = GWT.create(ScrollPanel.class);
        scroller.setWidget(form);
        scroller.setSize(width, height);

        rootWidget = scroller;
    }

    private Input newInputForAttributeType(String attributeType) {
        InputType inputType = INPUT_TYPE_MAP.getOrDefault(attributeType.toUpperCase(), InputType.TEXT);
        Input input = GWT.create(Input.class);
        input.setType(inputType);
        return input;
    }

    @Override
    public Widget asWidget() {
        return rootWidget;
    }

    @Override
    public void update(FhirDataTypeModel dataType) {
        updateDataTypeEditorVisible(dataType);
        for (FhirAttributeModel attr : dataType.getAttributesByElement().values()) {
            attributeEditors.get(attr.getId()).setVisible(attr.isSelected());
        }

    }

    private void updateDataTypeEditorVisible(FhirDataTypeModel dataType) {
        dataTypeEditors.get(dataType.getId()).setVisible(dataType.isSelected());
    }
}
