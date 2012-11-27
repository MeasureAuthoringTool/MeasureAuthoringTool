package org.ifmc.mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class MeasureNameLabel extends Composite {
	private Label measureName = new Label();
	
	public MeasureNameLabel() {
		FlowPanel measureNamePanel = new FlowPanel();
		HTML measureLabel = new HTML("Measure:&nbsp");
		measureLabel.addStyleName("bold");
		measureLabel.addStyleName("measureLabel");
		measureName.addStyleName("measureName");
		measureNamePanel.add(measureLabel);
		measureNamePanel.add(measureName);
		SimplePanel clearBoth = new SimplePanel();
		clearBoth.addStyleName("clearBoth");
		measureNamePanel.add(clearBoth);
		measureNamePanel.add(new SpacerWidget());
		measureNamePanel.add(new SpacerWidget());
		initWidget(measureNamePanel);
	}
	
	public void setMeasureName(String text) {
		measureName.setText(text);
	}
}
