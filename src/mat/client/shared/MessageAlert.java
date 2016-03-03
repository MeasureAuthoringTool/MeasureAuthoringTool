package mat.client.shared;

import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.HTML;

public class MessageAlert extends Alert {

	public MessageAlert() {
		setWidth("600px");
		setVisible(false);
		clear();
	}

	public void clearAlert() {
		clear();
		setVisible(false);
	}

	protected void setMessage(String errorMessage) {
		setMessage(new HTML(errorMessage));
	}

	protected void setMessage(HTML message) {
		add(message);
	}

	protected void setFocus() {
		try {
			getElement().focus();
			getElement().setAttribute("id", "WarningMessage");
			getElement().setAttribute("aria-role", "image");
			getElement().setAttribute("aria-labelledby", "LiveRegion");
			getElement().setAttribute("aria-live", "assertive");
			getElement().setAttribute("aria-atomic", "true");
			getElement().setAttribute("aria-relevant", "all");
			getElement().setAttribute("role", "alert");
		} catch (JavaScriptException e) {
			// This try/catch block is needed for IE7 since it is throwing
			// exception "cannot move
			// focus to the invisible control."
			// do nothing.
		}
	}

	/**
	 * Gets the msg panel.
	 *
	 * @param iconType
	 *            the icon type
	 * @param message
	 *            the message
	 * @return the msg panel
	 */
	protected HTML getMsgPanel(IconType iconType, String message) {
		Icon checkIcon = new Icon(iconType);
		HTML msgHtml = new HTML(checkIcon + " <b>" + message + "</b>");
		return msgHtml;
	}

}
