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
	/**
	 * Water mark text String.
	 */
	private String watermark;
	/**
	 * {@link BlurHandler}.
	 */
	private HandlerRegistration blurHandler;
	/**
	 * {@link FocusHandler}.
	 */
	private HandlerRegistration focusHandler;

	/**
	 * Default Constructor .
	 */
	public WatermarkedTextBox() {
		super();
		// this.setStylePrimaryName("textInput");
		this.removeStyleName("gwt-textInput");
	}

	/**
	 * Overloaded COnstructor.
	 * @param defaultValue
	 *            {@link String}
	 */
	public WatermarkedTextBox(String defaultValue) {
		this();
		setText(defaultValue);
	}

	/**
	 * Overloaded COnstructor.
	 * @param defaultValue
	 *            {@link String}
	 * @param watermark
	 *            {@link String}
	 */
	public WatermarkedTextBox(String defaultValue, String watermark) {
		this(defaultValue);
		setWatermark(watermark);
	}

	/**
	 * Adds a water mark if the parameter is not NULL or EMPTY.
	 * @param watermark
	 *            {@link String}
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

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.event.dom.client.BlurHandler#onBlur(com.google.gwt.event
	 * .dom.client.BlurEvent)
	 */
	@Override
	public void onBlur(BlurEvent event) {
		enableWatermark();
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
	 * com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event
	 * .dom.client.FocusEvent)
	 */
	@Override
	public void onFocus(FocusEvent event) {
		// removeStyleDependentName("watermark");
		removeStyleName("textInput-watermark");
		addStyleName("textInput");
		if (getText().equalsIgnoreCase(watermark)) {
			// Hide watermark
			setText("");
		}
	}
}
