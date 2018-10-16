package mat.client.shared;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HasValue;

/**
 * The Class DigitOnlyKeyUpHandler.
 */
public class DigitOnlyKeyUpHandler implements KeyUpHandler {
	
	/** The instance. */
	public static DigitOnlyKeyUpHandler INSTANCE = new DigitOnlyKeyUpHandler();
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.KeyUpHandler#onKeyUp(com.google.gwt.event.dom.client.KeyUpEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onKeyUp(KeyUpEvent event) {
		HasValue<String> source = (HasValue<String>) event.getSource();
		String value = source.getValue();
		String newValue = "";
		
		for(int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if(Character.isDigit(c)) {
				newValue += c;
			}
		}
		
		source.setValue(newValue);
	}
}
