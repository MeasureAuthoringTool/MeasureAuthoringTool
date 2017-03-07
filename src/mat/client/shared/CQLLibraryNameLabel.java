package mat.client.shared;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class CQLLibraryNameLabel extends Composite {
	
	/** The measure name. */
	private Label cqlLibraryName = new Label();
	
	/**
	 * Instantiates a new measure name label.
	 */
	public CQLLibraryNameLabel() {
		FlowPanel cqlLibraryNamePanel = new FlowPanel();
		cqlLibraryNamePanel.getElement().setId("cqlLibraryNamePanel_FlowPanel");
		HTML cqlLibraryLabel = new HTML("CQL Library:&nbsp");
		cqlLibraryLabel.addStyleName("bold");
		cqlLibraryLabel.addStyleName("measureLabel");
		cqlLibraryName.addStyleName("measureName");
		cqlLibraryNamePanel.add(cqlLibraryLabel);
		cqlLibraryNamePanel.add(cqlLibraryName);
		SimplePanel clearBoth = new SimplePanel();
		clearBoth.getElement().setId("clearBoth_SimplePanel");
		clearBoth.addStyleName("clearBoth");
		cqlLibraryNamePanel.add(clearBoth);
		cqlLibraryNamePanel.add(new SpacerWidget());
		cqlLibraryNamePanel.add(new SpacerWidget());
		initWidget(cqlLibraryNamePanel);
	}
	
	/**
	 * Sets the cqlLibrary name.
	 * 
	 * @param text
	 *            the new cqlLibrary name
	 */
	public void setCQLLibraryName(String text) {
		cqlLibraryName.setTitle(text);
		if(text.length()>45){
			text = text.substring(0, 45);
		} 
		cqlLibraryName.setText(text);
	}
}
