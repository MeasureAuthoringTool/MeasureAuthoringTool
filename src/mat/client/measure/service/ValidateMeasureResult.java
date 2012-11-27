package mat.client.measure.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ValidateMeasureResult implements IsSerializable {
	private boolean valid;
	private List<String> validationMessages;
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public List<String> getValidationMessages() {
		return validationMessages;
	}
	public void setValidationMessages(List<String> validationMessages) {
		this.validationMessages = validationMessages;
	}
	
	
}
