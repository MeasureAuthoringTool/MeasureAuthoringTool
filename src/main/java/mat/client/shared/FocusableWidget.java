package mat.client.shared;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class FocusableWidget.
 */
public class FocusableWidget extends SimplePanel implements KeyPressHandler {
	
	/** The img. */
	private Widget img;
	
	/**
	 * Instantiates a new focusable widget.
	 * 
	 * @param widget
	 *            the widget
	 */
	public FocusableWidget(Widget widget)
	{
		this.img = widget;
		this.setWidget(img);
	}

	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.KeyPressHandler#onKeyPress(com.google.gwt.event.dom.client.KeyPressEvent)
	 */
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyCodes.KEY_ENTER || event.getCharCode() == ' ')
		{
			 NativeEvent nativeEvent = Document.get().createClickEvent(1, 0, 0, 0, 0, false,
				        false, false, false);
			 DomEvent.fireNativeEvent(nativeEvent, this);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setStyleName(java.lang.String)
	 */
	public void setStyleName(String styleName)
	{
		this.img.setStyleName(styleName);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#addStyleName(java.lang.String)
	 */
	public void addStyleName(String styleName)
	{
		this.img.addStyleName(styleName);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#removeStyleName(java.lang.String)
	 */
	public void removeStyleName(String styleName)
	{
		this.img.removeStyleName(styleName);
	}
}
