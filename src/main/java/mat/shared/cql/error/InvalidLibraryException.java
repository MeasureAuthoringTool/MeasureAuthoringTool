package mat.shared.cql.error;

import java.util.ArrayList;
import java.util.List;

public class InvalidLibraryException extends Exception {
	private static final long serialVersionUID = 1L;
	private List<String> messages = new ArrayList<>(); 
	
	public InvalidLibraryException(List<String> messages) {
		super(messages.get(0)); // set the first validation error as the message
		this.messages = messages;
	}
	
	public InvalidLibraryException(String message) {
		super(message);
	}
	
	public InvalidLibraryException() {
		
	}
	
	public List<String> getMessages() {
		return messages; 
	}
}
