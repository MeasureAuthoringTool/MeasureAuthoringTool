package mat.client.shared;

import org.gwtbootstrap3.client.ui.Alert;

import com.google.gwt.user.client.ui.HTML;

public class MessageAlert extends Alert {
	
	public MessageAlert() {
		setWidth("600px");
		setVisible(false);
		clear();
	}

	public void setMessage(HTML message) {
		this.add(message);
	}

}
