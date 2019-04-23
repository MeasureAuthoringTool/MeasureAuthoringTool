package mat.shared;

import java.util.ArrayList;
import java.util.List;

public class CompositeMeasurePackageValidationResult {
	
	private List<String> messages = new ArrayList<>();
	
	public CompositeMeasurePackageValidationResult() {
		
	}
	
	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}
