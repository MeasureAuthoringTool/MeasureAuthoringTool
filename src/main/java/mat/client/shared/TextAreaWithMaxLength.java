package mat.client.shared;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import org.gwtbootstrap3.client.ui.TextArea;
//import com.google.gwt.user.client.ui.TextArea;

/**
 * The Class TextAreaWithMaxLength.
 */
public class TextAreaWithMaxLength extends TextArea {

	/**
	 * Sets the max length.
	 * 
	 * @param length
	 *            the new max length
	 */
	public void setMaxLength(final int length) {
		addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String value = getValue();
				if(value.length() > length) {
					value = value.substring(0, length);
					setValue(value);
				}
			}
		});
	}
}
