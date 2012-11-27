package org.ifmc.mat.client.shared;

import java.util.List;

import org.ifmc.mat.client.ImageResources;

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
	
	
	public ErrorMessageDisplay() {
		hPanel = new HorizontalPanel();
		imagePanel = new FlowPanel();
		msgPanel = new FlowPanel();
		imagePanel.add(errorIcon);
		initWidget(hPanel);
	}

	@Override
	public void clear() {
		hPanel.removeStyleName("alertMessage");
		msgPanel.clear();
		hPanel.remove(imagePanel);
		hPanel.remove(msgPanel);
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
		}catch(JavaScriptException e){
			//This try/catch block is needed for IE7 since it is throwing exception "cannot move
		    //focus to the invisible control." 
			//do nothing.
		}
	}
}
