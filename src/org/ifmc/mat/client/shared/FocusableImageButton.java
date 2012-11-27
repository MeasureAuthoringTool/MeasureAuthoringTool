package org.ifmc.mat.client.shared;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;

public class FocusableImageButton extends FocusableWidget {
	
	private boolean enableStatus=true;
	private Image image;
	
	/**
	 * 
	 * @param imageUrl
	 * @param title
	 * Note: alt attribute is being set to title, but if you want different values for alt and title then implement
	 * a 3-arg constructor. 
	 */
	public FocusableImageButton(ImageResource imageUrl, String title) {
		super(new Image(imageUrl));
		image = (Image)getWidget();
		setTitle(title);
		DOM.setElementProperty(getWidget().getElement(), "alt", title);
	}
	
	public void setTitle(String title) {
		((Image)getWidget()).setTitle(title);	
	}
	
	public void setUrl(String url) {
		image.setUrl(url);
	}
	
	public void enableImage(boolean enable) {
		if(enable) {
			removeStyleName("DisableImg");
			enableStatus = true;
		} else {
			addStyleName("DisableImg");
			enableStatus = false;
		}
	}
	
	public boolean isEnabled(){
		return enableStatus;
	}
}
