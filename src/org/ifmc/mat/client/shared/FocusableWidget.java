package org.ifmc.mat.client.shared;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class FocusableWidget extends FocusPanel implements KeyPressHandler {
	
	private Widget img;
	
	public FocusableWidget(Widget widget)
	{
		this.img = widget;
		this.setWidget(img);
		this.addKeyPressHandler(this);
	}

	
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyCodes.KEY_ENTER || event.getCharCode() == ' ')
		{
			 NativeEvent nativeEvent = Document.get().createClickEvent(1, 0, 0, 0, 0, false,
				        false, false, false);
			 DomEvent.fireNativeEvent(nativeEvent, this);
		}
	}

	public void setStyleName(String styleName)
	{
		this.img.setStyleName(styleName);
	}
	
	public void addStyleName(String styleName)
	{
		this.img.addStyleName(styleName);
	}
	
	public void removeStyleName(String styleName)
	{
		this.img.removeStyleName(styleName);
	}
}
