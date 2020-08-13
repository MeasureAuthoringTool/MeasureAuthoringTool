package mat.client.shared;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import mat.client.ImageResources;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class WarningMessageDisplay.
 */
public class WarningMessageDisplay extends Composite implements WarningMessageDisplayInterface {
	
	/** The h panel. */
	private HorizontalPanel hPanel;
	
	/** The warning icon. */
	private Image warningIcon = new Image(ImageResources.INSTANCE.msg_error());
	
	/** The image panel. */
	private FlowPanel imagePanel;
	
	/** The msg panel. */
	private FlowPanel msgPanel;
	
	/** The buttons. */
	private List<SecondaryButton> buttons = new ArrayList<SecondaryButton>();
	
	
	/**
	 * Instantiates a new warning message display.
	 */
	public WarningMessageDisplay() {
		hPanel = new HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		imagePanel = new FlowPanel();
		msgPanel = new FlowPanel();
		msgPanel.getElement().setId("msgPanel_FlowPanel");
		warningIcon.getElement().setAttribute("alt", "Warning");
		imagePanel.getElement().setId("imagePanel_FlowPanel");
		imagePanel.setTitle("Warning");
		imagePanel.add(warningIcon);
		initWidget(hPanel);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#clear()
	 */
	@Override
	public void clear() {
		hPanel.removeStyleName("warningMessage");
		msgPanel.clear();
		hPanel.remove(imagePanel);
		hPanel.remove(msgPanel);
		if(!buttons.isEmpty()){
			for (SecondaryButton button : buttons) {
				hPanel.remove(button);
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
		hPanel.addStyleName("warningMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		
		
		for(String message : messages) {
			msgPanel.add(wrap(message));
		}
		hPanel.add(new SpacerWidget());
		setFocus();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.WarningMessageDisplayInterface#setMessage(java.lang.String)
	 */
	@Override
	public void setMessage(String message) {
		hPanel.addStyleName("warningMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
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
		hPanel.getElement().focus();
		hPanel.getElement().setAttribute("id", "WarningMessage");
		hPanel.getElement().setAttribute("aria-role", "image");
		hPanel.getElement().setAttribute("aria-labelledby", "LiveRegion");
		hPanel.getElement().setAttribute("aria-live", "assertive");
		hPanel.getElement().setAttribute("aria-atomic", "true");
		hPanel.getElement().setAttribute("aria-relevant", "all");
		hPanel.getElement().setAttribute("role", "alert");
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
		hPanel.addStyleName("alertMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		for (String btnName : buttonNames) {
			SecondaryButton button1 = new SecondaryButton(btnName);
			button1.getElement().setId("button1_SecondaryButton");
			buttons.add(button1);
			hPanel.add(button1);
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
