package mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * The Class GenericResult.
 */
public class GenericResult implements IsSerializable {
	
	/** The success. */
	private boolean success;
	
	/** The failure reason. */
	private int failureReason;
	
	/** The messages. */
	private List<String> messages;
	
	/**
	 * Checks if is success.
	 * 
	 * @return true, if is success
	 */
	public boolean isSuccess() {
		return success;
	}
	
	/**
	 * Sets the success.
	 * 
	 * @param success
	 *            the new success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	/**
	 * Gets the failure reason.
	 * 
	 * @return the failure reason
	 */
	public int getFailureReason() {
		return failureReason;
	}
	
	/**
	 * Sets the failure reason.
	 * 
	 * @param failureReason
	 *            the new failure reason
	 */
	public void setFailureReason(int failureReason) {
		this.failureReason = failureReason;
	}
	
	/**
	 * Sets the messages.
	 * 
	 * @param messages
	 *            the new messages
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	/**
	 * Gets the messages.
	 * 
	 * @return the message
	 */
	public List<String> getMessages() {
		return messages;
	}
	
	
}
