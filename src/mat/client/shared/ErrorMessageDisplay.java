package mat.client.shared;

import java.util.ArrayList;
import java.util.List;

import mat.client.ImageResources;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ErrorMessageDisplay extends Composite implements ErrorMessageDisplayInterface {
	private HorizontalPanel hPanel;
	private Image errorIcon = new Image(ImageResources.INSTANCE.msg_error());
	
	private FlowPanel imagePanel;
	private FlowPanel msgPanel;
	private List<SecondaryButton> buttons = new ArrayList<SecondaryButton>();
	
	
	public ErrorMessageDisplay() {
		hPanel = new HorizontalPanel();
		imagePanel = new FlowPanel();
		msgPanel = new FlowPanel();
		errorIcon.getElement().setAttribute("alt", "ErrorMessage");
		imagePanel.setTitle("Error");
		imagePanel.add(errorIcon);
		initWidget(hPanel);
	}

	@Override
	public void clear() {
		hPanel.removeStyleName("alertMessage");
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

	@Override
	public void setMessages(List<String> messages) {
		hPanel.addStyleName("alertMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		
		
		for(String message : messages) {
			msgPanel.add(wrap(message));
		}
		hPanel.add(new SpacerWidget());
		setFocus();
	}

	@Override
	public void setMessage(String message) {
		hPanel.addStyleName("alertMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		msgPanel.add(wrap(message));
		msgPanel.add(new SpacerWidget());
		setFocus();
	}
	
	private Widget wrap(String arg) {
		return new HTML(arg);
	}
	
	@Override
	public void setFocus(){
		try{
		hPanel.getElement().focus();
		hPanel.getElement().setAttribute("id", "ErrorMessage");
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

	@Override
	public void setMessageWithButtons(String message, List<String> buttonNames) {
		hPanel.addStyleName("alertMessage");
		msgPanel.clear();
		hPanel.add(imagePanel);
		hPanel.add(msgPanel);
		for (String btnName : buttonNames) {
			SecondaryButton button1 = new SecondaryButton(btnName);
			buttons.add(button1);
			hPanel.add(button1);
		}
		msgPanel.add(wrap(message));
		msgPanel.add(new SpacerWidget());
		setFocus();
		
	}


	/**
	 * @return the buttons
	 */
	public List<SecondaryButton> getButtons() {
		return buttons;
	}

	/**
	 * @param buttons the buttons to set
	 */
	public void setButtons(List<SecondaryButton> buttons) {
		this.buttons = buttons;
	}
	
	
}
