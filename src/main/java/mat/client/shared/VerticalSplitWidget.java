package mat.client.shared;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VerticalSplitWidget extends VerticalPanel {

    Button button = new Button();

    public VerticalSplitWidget() {
        setWidth("10px");
        setHeight("100%");
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
        button.getElement().setId("panel_SimplePanel");
        button.setHeight("100%");
        button.setText("<<");
        button.getElement().setTabIndex(0);
        button.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
        add(button);
    }

    public Button getButton() {
        return button;
    }
}
