package mat.client.cqlworkspace.attributes;

import org.gwtbootstrap3.client.ui.Input;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Repeater extends HorizontalPanel {
    private AddMultiplierButton addButton;
    private RemoveMultiplierButton removeButton;
    private Input input;

    public Repeater(FhirAttributeModel attr, Input input) {
        this.input = input;
        add(input);
        addButton = new AddMultiplierButton("LightBoxRightPanelView_form_group_" + DOM.createUniqueId());
        add(addButton);
        removeButton = new RemoveMultiplierButton("LightBoxRightPanelView_form_group_" + attr.getId() + "_remove_" + DOM.createUniqueId());
        add(removeButton);
    }

    public HasClickHandlers getAddButton() {
        return addButton;
    }

    public HasClickHandlers getRemoveButton() {
        return removeButton;
    }

    public void setRemoveButtonVisible(boolean visible) {
        this.removeButton.setVisible(visible);
    }

    public Input getInput() {
        return input;
    }
}
