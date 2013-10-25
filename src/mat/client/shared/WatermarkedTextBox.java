package mat.client.shared;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

/**
 * WaterMarkTextBox class that is used where water mark is to be displayed when
 * text box is empty.
 */
public class WatermarkedTextBox extends TextBox implements BlurHandler, FocusHandler
{
	
	/** The blur handler. {@link BlurHandler}. */
	private HandlerRegistration blurHandler;
	
	/** The focus handler. {@link FocusHandler}. */
	private HandlerRegistration focusHandler;
	/**
	 * Water mark text String.
	 */
	private String watermark;
	/**
	 * Default Constructor .
	 */
	public WatermarkedTextBox() {
		super();
		this.removeStyleName("gwt-textInput");
	}
	
	/**
	 * Overloaded COnstructor.
	 * 
	 * @param defaultValue
	 *            the default value {@link String}
	 */
	public WatermarkedTextBox(String defaultValue) {
		this();
		setText(defaultValue);
	}
	
	/**
	 * Overloaded COnstructor.
	 * 
	 * @param defaultValue
	 *            the default value
	 * @param watermark
	 *            the watermark {@link String} {@link String}
	 */
	public WatermarkedTextBox(String defaultValue, String watermark) {
		this(defaultValue);
		setWatermark(watermark);
	}
	/**
	 * If no text is entered in Text box, water mark style is applied on text
	 * box.
	 */
	final void enableWatermark() {
		String text = getText();
		if ((text.length() == 0) || (text.equalsIgnoreCase(watermark))) {
			// Show watermark
			setText(watermark);
			addStyleName("textInput-watermark"); // ("watermark");
		}
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.event.dom.client.BlurHandler#onBlur(com.google.gwt.event
	 * .dom.client.BlurEvent)
	 */
	@Override
	public final void onBlur(BlurEvent event) {
		enableWatermark();
	}
	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event
	 * .dom.client.FocusEvent)
	 */
	@Override
	public final void onFocus(FocusEvent event) {
		// removeStyleDependentName("watermark");
		removeStyleName("textInput-watermark");
		addStyleName("textInput");
		if (getText().equalsIgnoreCase(watermark)) {
			// Hide watermark
			setText("");
		}
	}
	
	/**
	 * Adds a water mark if the parameter is not NULL or EMPTY.
	 * 
	 * @param watermark
	 *            the new water mark text String {@link String}
	 */
	public final void setWatermark(final String watermark) {
		this.watermark = watermark;
		if ((watermark != null) && (watermark != "")) {
			blurHandler = addBlurHandler(this);
			focusHandler = addFocusHandler(this);
			enableWatermark();
		} else {
			// Remove handlers
			blurHandler.removeHandler();
			focusHandler.removeHandler();
		}
	}
}
