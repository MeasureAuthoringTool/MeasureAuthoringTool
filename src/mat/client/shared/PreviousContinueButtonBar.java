package mat.client.shared;

import mat.client.Enableable;
import mat.client.shared.search.MATAnchor;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
 
/**
 * The Class PreviousContinueButtonBar.
 */
public class PreviousContinueButtonBar extends Composite implements HasVisible, Enableable {
	
	/** The previous button. */
	private MATAnchor previousButton = new MATAnchor("");
	
	/** The continue button. */
	private MATAnchor continueButton = new MATAnchor("");
	
	/** The state. */
	public int state =0;
	
	/** The sub state. */
	public int subState = 0; //can be extended further
	
	/**
	 * Sets the page names on state.
	 */
	public void setPageNamesOnState(){
		if(state<=0){
			state =0;
			if(subState == 0){
				setPageNames("UNDEFINED", "QDM Elements");				
				buttonPanel.remove(previousButton);
				buttonPanel.remove(continueButton);
				buttonPanel.add(continueButton);
			}
		}
		else if(state ==1){
			
			setPageNames("Measure Details", "Clause Workspace");
			
			buttonPanel.remove(previousButton);
			buttonPanel.remove(continueButton);
			buttonPanel.add(previousButton);
			buttonPanel.add(continueButton);
		}
		else if(state ==2){
			
			setPageNames("QDM Elements", "Measure Packager");
			
			buttonPanel.remove(previousButton);
			buttonPanel.remove(continueButton);
			buttonPanel.add(previousButton);
			buttonPanel.add(continueButton);
		}
		else if(state ==3){
			
			state =3;
			setPageNames("Clause Workspace", "Measure Notes");
			buttonPanel.remove(previousButton);
			buttonPanel.remove(continueButton);
			buttonPanel.add(previousButton);
			buttonPanel.add(continueButton);
			
		}
		else if(state >=4){
			
			state =4;
			setPageNames("Measure Packager", "UNDEFINED");
			buttonPanel.remove(previousButton);
			buttonPanel.remove(continueButton);
			buttonPanel.add(previousButton);
			
		}
	}
	
	/**
	 * Sets the page names.
	 * 
	 * @param previousPage
	 *            the previous page
	 * @param nextPage
	 *            the next page
	 */
	public void  setPageNames(String previousPage, String nextPage){
		previousButton.setText("<< Go to "+previousPage);
		continueButton.setText("Go to "+ nextPage +" >>");
	}
	
	/** The button panel. */
	private FlowPanel buttonPanel = new FlowPanel();
	
	/**
	 * Instantiates a new previous continue button bar.
	 */
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
	
	/**
	 * Sets the continue button visible.
	 * 
	 * @param visible
	 *            the new continue button visible
	 */
	public void setContinueButtonVisible(boolean visible) {
		MatContext.get().setVisible(continueButton, visible);
	}
	
	/**
	 * Sets the previous button visible.
	 * 
	 * @param visible
	 *            the new previous button visible
	 */
	public void setPreviousButtonVisible(boolean visible) {
		MatContext.get().setVisible(previousButton, visible);
	}
	
	/**
	 * Gets the continue button.
	 * 
	 * @return the continue button
	 */
	public HasClickHandlers getContinueButton() {
		return continueButton;
	}
	
	/**
	 * Gets the previous button.
	 * 
	 * @return the previous button
	 */
	public HasClickHandlers getPreviousButton() {
		return previousButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.Enableable#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled){
		previousButton.setEnabled(enabled);
		continueButton.setEnabled(enabled);
	}
	
}
