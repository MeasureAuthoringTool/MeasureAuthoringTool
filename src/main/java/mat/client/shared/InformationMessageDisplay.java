package mat.client.shared;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.ImageResources;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class InformationMessageDisplay.
 */
public class InformationMessageDisplay extends Composite implements InformationMessageDisplayInterface{
	/** The s panel. */
	private VerticalPanel vPanel;
	
	/** The informationIcon icon. */
	private Image informationIcon = new Image(ImageResources.INSTANCE.help());
	
	/** The image panel. */
	private FlowPanel imagePanel;
	
	/** The msg panel. */
	private FlowPanel msgPanel;
	
	/** The horz panel. */
	private HorizontalPanel hPanel;
	
	/** The buttons. */
	private List<SecondaryButton> buttons = new ArrayList<SecondaryButton>();
	
	
	/**
	 * Instantiates a new information message display.
	 */
	public InformationMessageDisplay() {
		vPanel = new VerticalPanel();
		vPanel.setWidth("700px");
		vPanel.getElement().setId("vPanel_VerticalPanel");
		hPanel = new  HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		imagePanel = new FlowPanel();
		msgPanel = new FlowPanel();
		msgPanel.getElement().setId("msgPanel_FlowPanel");
		informationIcon.getElement().setAttribute("alt", "Warning");
		imagePanel.getElement().setId("imagePanel_FlowPanel");
		imagePanel.setTitle("Information");
		imagePanel.add(informationIcon);
		initWidget(vPanel);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#clear()
	 */
	@Override
	public void clear() {
		vPanel.removeStyleName("informationMessage");
		msgPanel.clear();
		vPanel.remove(hPanel);
		if(!buttons.isEmpty()){
			for (SecondaryButton button : buttons) {
				vPanel.remove(button);
			}
			buttons.clear();
		}
		//Turn off images
		
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#setMessages(java.util.List)
	 */
	@Override
	public void setMessages(List<String> messages) {
		vPanel.addStyleName("informationMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		vPanel.add(hPanel);
		
		
		for(String message : messages) {
			msgPanel.add(wrap(message));
		}
		msgPanel.add(new SpacerWidget());
		setFocus();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#setMessage(java.lang.String)
	 */
	@Override
	public void setMessage(String message) {
		vPanel.addStyleName("informationMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		vPanel.add(hPanel);
		msgPanel.add(wrap(message));
		msgPanel.add(new SpacerWidget());
		setFocus();
	}
	
	/**
	 * Wrap.
	 * 
	 * @param arg
	 *            the arg
	 * @return the widget
	 */
	private Widget wrap(String arg) {
		return new HTML(arg);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#setFocus()
	 */
	@Override
	public void setFocus(){
		try{
		vPanel.getElement().focus();
		vPanel.getElement().setAttribute("id", "informationMessage");
		vPanel.getElement().setAttribute("aria-role", "image");
		vPanel.getElement().setAttribute("aria-labelledby", "LiveRegion");
		vPanel.getElement().setAttribute("aria-live", "assertive");
		vPanel.getElement().setAttribute("aria-atomic", "true");
		vPanel.getElement().setAttribute("aria-relevant", "all");
		vPanel.getElement().setAttribute("role", "alert");
		}catch(JavaScriptException e){
			//This try/catch block is needed for IE7 since it is throwing exception "cannot move
		    //focus to the invisible control." 
			//do nothing.
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#setMessageWithButtons(java.lang.String, java.util.List)
	 */
	@Override
	public void setMessageWithButtons(String message, List<String> buttonNames) {
		vPanel.addStyleName("informationMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		vPanel.add(hPanel);
		for (String btnName : buttonNames) {
			SecondaryButton button1 = new SecondaryButton(btnName);
			button1.getElement().setId("button1_SecondaryButton");
			buttons.add(button1);
			vPanel.add(button1);
		}
		msgPanel.add(wrap(message));
		msgPanel.add(new SpacerWidget());
		setFocus();
		
	}


	/**
	 * Gets the buttons.
	 * 
	 * @return the buttons
	 */
	public List<SecondaryButton> getButtons() {
		return buttons;
	}

	/**
	 * Sets the buttons.
	 * 
	 * @param buttons
	 *            the buttons to set
	 */
	public void setButtons(List<SecondaryButton> buttons) {
		this.buttons = buttons;
	}
}
