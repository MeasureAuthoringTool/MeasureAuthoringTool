package org.ifmc.mat.client.shared;

import java.util.List;

public interface SuccessMessageDisplayInterface {
	public void clear();
	public void setMessages(List<String> messages);
	public void setMessage(String message);
	public void setFocus();
}
