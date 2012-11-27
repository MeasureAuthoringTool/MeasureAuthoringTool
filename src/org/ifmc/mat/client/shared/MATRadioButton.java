package org.ifmc.mat.client.shared;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RadioButton;


public class MATRadioButton extends RadioButton {

	HandlerRegistration handlerRegistration = null;
	
	public MATRadioButton(String name) {
		super(name);
	}
	public MATRadioButton(String string, String string2) {
		super(string, string2);
		
	}
	public MATRadioButton(String string, String string2, boolean bool){
		super(string, string2, bool);
	}
		
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler){
		if(handlerRegistration == null)
			handlerRegistration = super.addClickHandler(handler);
		return handlerRegistration;
	}
}
