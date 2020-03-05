package mat.client.cqlworkspace.attributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public class AttributeEditorView implements AttributeEditorDisplay {

    private static final Map<String, InputType> INPUT_TYPE_MAP;
    private final FhirAttributeModel attr;
    private FormGroup container;
    private boolean selected;
    private List<Repeater> instances = new LinkedList<>();
    private FormLabel label;

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

    public AttributeEditorView(FhirAttributeModel attr) {
        // TODO create input type based on the attribute type
        // TODO bind back to model
        // TODO Repeater
        this.attr = attr;
        container = GWT.create(FormGroup.class);
        String attributeId = attr.getId();
        container.ensureDebugId("LightBoxRightPanelView_form_group_" + attributeId);
        label = GWT.create(FormLabel.class);
        label.setText(attr.getFhirElement());

        setSelected(false);
        addInstance(null);
    }

    private Repeater addInstance(Repeater insertAfter) {
        Input input = newInputForAttributeType(attr.getFhirType());
        input.setWidth("300px");
        input.addChangeHandler(event -> {
            attr.getValues().clear();
            List<String> newValues = instances.stream().map(ins -> ins.getInput()).map(Input::getValue).collect(Collectors.toList());
            attr.getValues().addAll(newValues);
        });

        final Repeater repeater = new Repeater(attr, input);
        repeater.getAddButton().addClickHandler(event -> addInstance(repeater));
        repeater.getRemoveButton().addClickHandler(event -> removeInstance(repeater));

        if (insertAfter != null && instances.indexOf(insertAfter) != -1) {
            instances.add(instances.indexOf(insertAfter) + 1, repeater);
        } else {
            instances.add(repeater);
        }

        updateInstances();

        return repeater;
    }

    private void removeInstance(Repeater instance) {
        instances.remove(instance);
        updateInstances();
    }

    private void updateInstances() {
        container.clear();
        container.add(label);
        boolean hasRemoveVisible = instances.size() > 1;
        instances.forEach(r -> {
            r.setRemoveButtonVisible(hasRemoveVisible);
            container.add(r);
        });
    }

    private Input newInputForAttributeType(String attributeType) {
        InputType inputType = INPUT_TYPE_MAP.getOrDefault(attributeType.toUpperCase(), InputType.TEXT);
        Input input = GWT.create(Input.class);
        input.setType(inputType);
        return input;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        container.setVisible(selected);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
}
