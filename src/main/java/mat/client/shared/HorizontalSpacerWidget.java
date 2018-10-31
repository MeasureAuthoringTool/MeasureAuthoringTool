package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

public class HorizontalSpacerWidget extends Composite {
	
	public HorizontalSpacerWidget() {
		SimplePanel panel = new SimplePanel();
		panel.getElement().setId("panel_HorizontalSimplePanel");
		panel.setWidth("25px");
		initWidget(panel);
	}
}
