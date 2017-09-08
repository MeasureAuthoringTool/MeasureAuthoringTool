package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * The Class MeasureNameLabel.
 */
public class MeasureNameLabel extends Composite {
	
	/** The measure name. */
	private Label measureName = new Label();
	
	/**
	 * Instantiates a new measure name label.
	 */
	public MeasureNameLabel() {
		FlowPanel measureNamePanel = new FlowPanel();
		measureNamePanel.getElement().setId("measureNamePanel_FlowPanel");
		HTML measureLabel = new HTML("Measure:&nbsp");
		measureLabel.addStyleName("bold");
		measureLabel.addStyleName("measureLabel");
		measureName.addStyleName("measureName");
		measureName.getElement().setAttribute("tabIndex", "0");
		measureNamePanel.add(measureLabel);
		measureNamePanel.add(measureName);
		SimplePanel clearBoth = new SimplePanel();
		clearBoth.getElement().setId("clearBoth_SimplePanel");
		clearBoth.addStyleName("clearBoth");
		measureNamePanel.add(clearBoth);
		measureNamePanel.add(new SpacerWidget());
		measureNamePanel.add(new SpacerWidget());
		initWidget(measureNamePanel);
	}
	
	/**
	 * Sets the measure name.
	 * 
	 * @param text
	 *            the new measure name
	 */
	public void setMeasureName(String text) {
		measureName.setText(text);
	}
}
