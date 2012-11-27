package mat.client.shared;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextArea;

public class TextAreaWithMaxLength extends TextArea {

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
