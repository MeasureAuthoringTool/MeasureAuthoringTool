package mat.client.login;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * The Class WelcomeLabelWidget.
 */
class WelcomeLabelWidget extends Composite {
	
	/**
	 * Instantiates a new welcome label widget.
	 */
	public WelcomeLabelWidget() {
		Label welcome = new Label("Welcome to the Measure Authoring Tool");
		welcome.getElement().setId("welcome_Label");
		welcome.addStyleName("loginHeader");
		initWidget(welcome);
	}
}
