package mat.client.shared;

import java.util.List;

public interface ErrorMessageDisplayInterface {
	public void clear();
	public void setMessages(List<String> messages);
	public void setMessage(String message);
	public void setFocus();
}
