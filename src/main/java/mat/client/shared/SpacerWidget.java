package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * The Class SpacerWidget.
 */
public class SpacerWidget extends Composite {

    /**
     * Instantiates a new spacer widget.
     */
    public SpacerWidget() {
        SimplePanel panel = new SimplePanel();
        panel.getElement().setId("panel_SimplePanel");
        panel.setHeight("10px");
        initWidget(panel);
    }
}
