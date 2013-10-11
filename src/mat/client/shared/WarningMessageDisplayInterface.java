package mat.client.shared;

import java.util.List;

public interface WarningMessageDisplayInterface {
	public void clear();
	public void setMessages(List<String> messages);
	public void setMessage(String message);
	public void setFocus();
	public void setMessageWithButtons(String message, List<String> buttonNames);
}
