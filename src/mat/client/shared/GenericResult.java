package mat.client.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GenericResult implements IsSerializable {
	private boolean success;
	private int failureReason;
	private List<String> messages;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(int failureReason) {
		this.failureReason = failureReason;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	/**
	 * @return the message
	 */
	public List<String> getMessages() {
		return messages;
	}
	
	
}
