package mat.client.shared;

import mat.client.Enableable;
import mat.client.shared.search.MATAnchor;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
 
public class PreviousContinueButtonBar extends Composite implements HasVisible, Enableable {
	private MATAnchor previousButton = new MATAnchor("");
	private MATAnchor continueButton = new MATAnchor("");
	
	public int state =0;
	public int subState = 0; //can be extended further
	
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
	
	public void  setPageNames(String previousPage, String nextPage){
		previousButton.setText("<< Go to "+previousPage);
		continueButton.setText("Go to "+ nextPage +" >>");
	}
	
	private FlowPanel buttonPanel = new FlowPanel();
	
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
	
	public void setContinueButtonVisible(boolean visible) {
		MatContext.get().setVisible(continueButton, visible);
	}
	
	public void setPreviousButtonVisible(boolean visible) {
		MatContext.get().setVisible(previousButton, visible);
	}
	
	public HasClickHandlers getContinueButton() {
		return continueButton;
	}
	
	public HasClickHandlers getPreviousButton() {
		return previousButton;
	}
	
	public void setEnabled(boolean enabled){
		previousButton.setEnabled(enabled);
		continueButton.setEnabled(enabled);
	}
	
}
