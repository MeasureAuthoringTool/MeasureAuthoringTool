package org.ifmc.mat.client.login;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

class WelcomeLabelWidget extends Composite {
	public WelcomeLabelWidget() {
		Label welcome = new Label("Welcome to the ### Measure Authoring Tool");
		welcome.addStyleName("loginHeader");
		initWidget(welcome);
	}
}
