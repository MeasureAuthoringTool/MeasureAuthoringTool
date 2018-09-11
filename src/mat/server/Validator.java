package mat.server;

import java.util.ArrayList;
import java.util.List;

public abstract class Validator {

	private boolean isValid; 
	private List<String> messages = new ArrayList<>(); 
		
	public boolean isValid() {
		return isValid; 
	}
	
	public void setValid(boolean isValid) {
		this.isValid = isValid; 
	}
	
	public List<String> getMessages() {
		return messages; 
	}
	
	
	
}
