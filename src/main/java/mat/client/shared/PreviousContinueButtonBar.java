package mat.client.shared;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import mat.client.Enableable;
import mat.client.shared.search.MATAnchor;

/**
 * This is the class for the two buttons at the bottom of the Measure Composer tab
 * that send you to the previous or next tab
 */
public class PreviousContinueButtonBar extends Composite implements HasVisible, Enableable {
	private final String UNDEFINED = "undefined";
	
	private MATAnchor previousButton = new MATAnchor("");
	private MATAnchor continueButton = new MATAnchor("");
	private FlowPanel buttonPanel = new FlowPanel();
	
	public int state = 0;
	public int subState = 0; //can be extended further
	
	public PreviousContinueButtonBar() {
		continueButton.addStyleName("continueButton");
		continueButton.getElement().setId("continueButton_MATAnchor");
		previousButton.getElement().setId("previousButton_MATAnchor");
		SimplePanel sPanel = new SimplePanel();
		sPanel.getElement().setId("sPanel_SimplePanel");
		sPanel.addStyleName("clearBoth");
		buttonPanel.getElement().setId("buttonPanel_FlowPanel");
		buttonPanel.add(sPanel);
		initWidget(buttonPanel);
		setPageNamesOnState();
	}
	
	public void setPageNamesOnState(){
		if(state <= 0) {
			state = 0;
		}

		switch(state) {
			case 0: 
				if (subState == 0) {
					updateButtons(UNDEFINED, "CQL Workspace");
				}
				break;
				
			case 1: 
				updateButtons("Measure Details", "Population Workspace");
				break;
				
			case 2:
				updateButtons("CQL Workspace", "Measure Packager");
				break;
				
			case 3:
				updateButtons("Population Workspace", UNDEFINED);
				break;
			
			default:
				updateButtons(UNDEFINED, UNDEFINED);
				break;
		}
	}
	

	public void  updateButtons(String previousPage, String nextPage){
		previousButton.setText("<< Go to "+previousPage);
		previousButton.getElement().setId(previousPage+"_MATAnchor");
		continueButton.setText("Go to "+ nextPage +" >>");
		continueButton.getElement().setId(nextPage+"_MATAnchor");
		setVisibility(previousPage, nextPage);
		addButtons(previousPage, nextPage);
	}
	
	private void addButtons(String previous, String next) {
		buttonPanel.remove(previousButton);
		buttonPanel.remove(continueButton);
		
		if(!previous.equals(UNDEFINED)) {
			buttonPanel.add(previousButton);
		}
		if(!next.equals(UNDEFINED)) {
			buttonPanel.add(continueButton);
		}
	}
	
	private void setVisibility(String previous, String next) {
		MatContext.get().setVisible(previousButton, !previous.equals(UNDEFINED));
		MatContext.get().setVisible(continueButton, !next.equals(UNDEFINED));
	}

	public HasClickHandlers getContinueButton() {
		return continueButton;
	}

	public HasClickHandlers getPreviousButton() {
		return previousButton;
	}

	@Override
	public void setEnabled(boolean enabled){
		previousButton.setEnabled(enabled);
		continueButton.setEnabled(enabled);
	}
}
