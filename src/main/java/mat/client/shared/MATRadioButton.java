package mat.client.shared;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RadioButton;


/**
 * The Class MATRadioButton.
 */
public class MATRadioButton extends RadioButton {

	/** The handler registration. */
	HandlerRegistration handlerRegistration = null;
	
	/**
	 * Instantiates a new mAT radio button.
	 * 
	 * @param name
	 *            the name
	 */
	public MATRadioButton(String name) {
		super(name);
	}
	
	/**
	 * Instantiates a new mAT radio button.
	 * 
	 * @param string
	 *            the string
	 * @param string2
	 *            the string2
	 */
	public MATRadioButton(String string, String string2) {
		super(string, string2);
		
	}
	
	/**
	 * Instantiates a new mAT radio button.
	 * 
	 * @param string
	 *            the string
	 * @param string2
	 *            the string2
	 * @param bool
	 *            the bool
	 */
	public MATRadioButton(String string, String string2, boolean bool){
		super(string, string2, bool);
	}
		
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.FocusWidget#addClickHandler(com.google.gwt.event.dom.client.ClickHandler)
	 */
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler){
		if(handlerRegistration == null)
			handlerRegistration = super.addClickHandler(handler);
		return handlerRegistration;
	}
}
