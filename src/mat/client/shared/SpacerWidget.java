package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

public class SpacerWidget extends Composite {
	public SpacerWidget() {
		SimplePanel panel = new SimplePanel();
		panel.setHeight("10px");
		initWidget(panel);
	}
}
