package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * The Class SpacerWidget.
 */
public class HorizontalSpacerWidget extends Composite {
	
	/**
	 * Instantiates a new spacer widget.
	 */
	public HorizontalSpacerWidget() {
		SimplePanel panel = new SimplePanel();
		panel.getElement().setId("panel_HorizontalSimplePanel");
		panel.setWidth("25px");
		initWidget(panel);
	}
}
