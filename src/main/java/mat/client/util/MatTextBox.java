package mat.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.TextBox;

public class MatTextBox extends TextBox {
	public MatTextBox() {
		
		super(Document.get().createTextInputElement());
		removeStyleName("gwt-Text-box");
		setStyleName("form-control");
	}
}