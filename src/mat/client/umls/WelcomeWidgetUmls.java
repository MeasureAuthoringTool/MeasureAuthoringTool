package mat.client.umls;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class WelcomeWidgetUmls extends Composite {
	public WelcomeWidgetUmls() {
		Label welcome = new Label("Welcome to the UMLS Login.");
		welcome.addStyleName("loginHeader");
		initWidget(welcome);
	}
}
