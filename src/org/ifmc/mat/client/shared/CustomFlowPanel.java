package org.ifmc.mat.client.shared;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * this family of classes can be used in lieu of VerticalPanel and HorizontalPanel
 * and so avoid using a <table> element
 * @author aschmidt
 *
 */
public abstract class CustomFlowPanel extends FlowPanel {
	protected abstract String getFlowStyle();
	@Override
	public void add(Widget w) {
		w.getElement().getStyle().setProperty("display", getFlowStyle());
		super.add(w);
	}
}
