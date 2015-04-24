package mat.client.shared;

import java.util.List;

/**
 * The Interface AlerMessageDisplayInterface.
 */
public interface InformationMessageDisplayInterface {
	 /* Clear.
	 */
	/**
 	 * Clear.
 	 */
 	public void clear();
	
	/**
	 * Sets the messages.
	 * 
	 * @param messages
	 *            the new messages
	 */
	public void setMessages(List<String> messages);
	
	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message);
	
	/**
	 * Sets the focus.
	 */
	public void setFocus();
	
	/**
	 * Sets the message with buttons.
	 * 
	 * @param message
	 *            the message
	 * @param buttonNames
	 *            the button names
	 */
	public void setMessageWithButtons(String message, List<String> buttonNames);
}
