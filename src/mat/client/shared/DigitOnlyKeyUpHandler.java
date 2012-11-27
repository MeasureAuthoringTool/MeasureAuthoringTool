package mat.client.shared;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HasValue;

public class DigitOnlyKeyUpHandler implements KeyUpHandler {
	public static DigitOnlyKeyUpHandler INSTANCE = new DigitOnlyKeyUpHandler();
	
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
