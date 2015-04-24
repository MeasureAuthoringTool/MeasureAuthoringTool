package mat.client.shared;

import java.util.List;

/**
 * The Interface SuccessMessageDisplayInterface.
 */
public interface SuccessMessageDisplayInterface {
	
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

	public void setAmberMessage(String packageSuccessAmberMessage);

	
}
